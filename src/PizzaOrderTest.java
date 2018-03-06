import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        jin.close();
    }

}

interface Item {
	public int getPrice();
}

class Standard implements Item {
	@Override
	public int getPrice() {
		return 10;
	}
}

class Pepperoni implements Item {
	@Override
	public int getPrice() {
		return 12;
	}
}

class Vegetarian implements Item {
    @Override
	public int getPrice() {
		return 8;
	}
}

class Ketchup implements Item {
	@Override
	public int getPrice() {
		return 3;
	}
}

class Coke implements Item {
	@Override
	public int getPrice() {
		return 5;
	}	
}

class ExtraItem implements Item {
	private Item item;
	private static List<String> VALID_TYPES;
	static { 
		VALID_TYPES = new ArrayList<>();
		VALID_TYPES.addAll(Arrays.asList("Ketchup","Coke"));
	}
	
	public ExtraItem(String type) {
		
		if (!VALID_TYPES.contains(type))
			throw new InvalidExtraTypeException();
			
		Class<?> typeClass = null;
		try {
			typeClass = Class.forName(type);
		} catch (ClassNotFoundException e) {
			// No need to do anything, type class surely exists 
		}
		try {
			item = (Item)typeClass.newInstance();
		} catch (InstantiationException e) {
			// No need to do anything, this level won't ever be reached
		} catch (IllegalAccessException e) {
			// No need to do anything, this level won't ever be reached
		}
	}
	
	public int getPrice() {
		return item.getPrice();
	}	
    
    public String toString() {
        return item.getClass().getSimpleName();
    }
}

class PizzaItem implements Item {
	private Item item;
	private static List<String> VALID_TYPES;
	static { 
		VALID_TYPES = new ArrayList<>();
		VALID_TYPES.addAll(Arrays.asList("Standard", "Pepperoni", "Vegetarian"));
	}
	
	public PizzaItem(String type) {
		
		if (!VALID_TYPES.contains(type))
			throw new InvalidPizzaTypeException();
			
		Class<?> typeClass = null;
		try {
			typeClass = Class.forName(type);
		} catch (ClassNotFoundException e) {
			// No need to do anything, type class surely exists 
		}
		try {
			item = (Item)typeClass.newInstance();
		} catch (InstantiationException e) {
			// No need to do anything, this level won't ever be reached
		} catch (IllegalAccessException e) {
			// No need to do anything, this level won't ever be reached
		}
	}
	
	public int getPrice() {
		return item.getPrice();
	}
    
    public String toString() {
        return item.getClass().getSimpleName();
    }
}

class CountableItem {
	public final Item item;
	public final int count;
	
	public CountableItem(Item item, int count) {
		this.item = item;
		this.count = count;
	}
	
	public int getPrice() {
		return item.getPrice() * count;
	}
}

class Order {
	List<CountableItem> items;
	private boolean locked;
	
	public Order() {
		items = new ArrayList<>();
		locked = false;
	}
	
	public void addItem(Item item, int count) {
		if (locked)
			throw new OrderLockedException();
		if (count > 10)
			throw new ItemOutOfStockException(item);
		
		CountableItem countableItem = new CountableItem(item, count);
		
		int index = -1;
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).item.toString().equals(item.toString())) {
				items.remove(i);
				index = i;
				break;
			}
		}
		
		if (index != -1)
			items.add(index, countableItem);
		else 
			items.add(countableItem);		
	}
	
	public int getPrice() {
		return items.stream().mapToInt(CountableItem::getPrice).sum();
	}
	
	public void displayOrder() {
		int index = 1;
		for (CountableItem cItem : items) {
			String out = String.format("%3d.%-15sx%2d%5d$", index++, 
					cItem.item, cItem.count, cItem.getPrice());
			System.out.println(out);
			
		}
		String out = String.format("%-22s%5d$", "Total:", getPrice());
        System.out.println(out);
	}
	
	public void removeItem(int index) {
		if (locked)
			throw new OrderLockedException();
		if (index < 0 || index >= items.size())
			throw new ArrayIndexOutOfBoundsException(index);
		items.remove(index);
	}
	
	public void lock() {
		if (items.isEmpty())
			throw new EmptyOrder();
		locked = true;
	}
}


/*
 * 
 * EXCEPTION CLASSES
 * 
 */
class InvalidExtraTypeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public InvalidExtraTypeException() { super(); }
}

class InvalidPizzaTypeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public InvalidPizzaTypeException() { super(); }
}

class ItemOutOfStockException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public final Item item;
	
	public ItemOutOfStockException() {
		super();
		item = null;
	}
	
	public ItemOutOfStockException(Item item) {
		this.item = item;
	}
}

// Greska ima tomce vo zadacata zatoa ne e EmptyOrderException
class EmptyOrder extends RuntimeException {
	private static final long serialVersionUID = 1L;
}

class OrderLockedException extends RuntimeException {
	private static final long serialVersionUID = 1L;	
}