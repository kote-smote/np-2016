import java.util.Scanner;
import java.util.LinkedList;
import java.util.Arrays;

class ResizableArray<T> {
    private Object[] arr;
    int last;

    public ResizableArray() {
        arr = new Object[50];
        last = -1;
    }

    public ResizableArray(Object[] elements) {
        arr = Arrays.copyOf(elements, elements.length);
        last = arr.length - 1;
    }

    public int count() {
        return last + 1;
    }

    public void addElement(T element) {
        if (element == null)
            throw new NullPointerException();
        if (isFull())
            resize(arr.length * 2);
        arr[++last] = element;
    }

    public boolean removeElement(T element) {
        if (isEmpty())
            return false;
        int index = -1;
        for (int i = 0; i < count(); i++) {
            if (arr[i].equals(element)) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            for (int i = index; i < count() - 1; i++)
                arr[i] = arr[i + 1];

            arr[last--] = null;

            int numOfElements = last + 1;
            if (numOfElements <= arr.length / 4)
                resize(arr.length / 2);

            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public T elementAt(int pos) {
        if (pos < 0 || pos > last)
            throw new ArrayIndexOutOfBoundsException();
        return (T) arr[pos];
    }

    public boolean contains(T element) {
        return Arrays.stream(Arrays.copyOf(arr, count())).anyMatch(el -> el.equals(element));
    }

    public boolean isEmpty() {
        return last == -1;
    }

    public Object[] toArray() {
        return Arrays.copyOf(arr, count());
    }

    public static <T> void copyAll(ResizableArray<? super T> dest,
                                   ResizableArray<? extends T> src) {
    	int size = src.count();
    	
        for (int i = 0; i < size; i++) {
        	dest.addElement(src.elementAt(i));
         }       
    }
    
    private boolean isFull() {
        return last == arr.length - 1;
    }

    private void resize(int newSize) {
        arr = Arrays.copyOf(arr, newSize);
    }

}

class IntegerArray extends ResizableArray<Integer> {

    public IntegerArray() {
        super();
    }
    
    public IntegerArray(Object[] elements) {
    	super(elements);
    }

    public double sum() {
        return Arrays.stream(toArray())
				.mapToInt(el -> (Integer) el)
				.sum();
      
//        Integer[] ints = Arrays.copyOf(toArray(), count(), Integer[].class);
//        return Arrays.stream(ints)
//        		.mapToInt(Integer::intValue())
//        		.sum();
    }

    public double mean() {
//    	Solution 2:
//        return Arrays.stream(toArray())
//               .mapToInt(el -> (Integer) el)
//               .average()
//               .getAsDouble();
    	
    	 // Integer[] ints = (Integer[]) toArray(); // can't do this!!! ClassCastException
        Integer[] ints = Arrays.copyOf(toArray(), count(), Integer[].class); 
        return Arrays.stream(ints)
        		.mapToInt(Integer::intValue)
        		.average()
        		.getAsDouble();
    }

    public int countNonZero() {
      
        return (int) Arrays.stream(toArray())
               .filter(el -> !el.equals(0))
               .count();
    }

    public IntegerArray distinct() {
    	Object[] ints = Arrays.stream(toArray())
               .distinct()
               .toArray();
        return new IntegerArray(ints);
    }

    public IntegerArray increment(int offset) {
    	Object[] ints = toArray();
    	for (int i = 0; i < ints.length; i++) 
			ints[i] = new Integer((Integer)ints[i] + offset);
		
    	return new IntegerArray(ints);
    }
}

public class ResizableArrayTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int test = jin.nextInt();
        if ( test == 0 ) { //test ResizableArray on ints
            ResizableArray<Integer> a = new ResizableArray<Integer>();
            System.out.println(a.count());
            int first = jin.nextInt();
            a.addElement(first);
            System.out.println(a.count());
            int last = first;
            while ( jin.hasNextInt() ) {
                last = jin.nextInt();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
        }
        if ( test == 1 ) { //test ResizableArray on strings
            ResizableArray<String> a = new ResizableArray<String>();
            System.out.println(a.count());
            String first = jin.next();
            a.addElement(first);
            System.out.println(a.count());
            String last = first;
            for ( int i = 0 ; i < 4 ; ++i ) {
                last = jin.next();
                a.addElement(last);
            }
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(a.removeElement(first));
            System.out.println(a.contains(first));
            System.out.println(a.count());
            ResizableArray<String> b = new ResizableArray<String>();
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));
            System.out.println(b.removeElement(first));
            System.out.println(b.contains(first));

            System.out.println(a.removeElement(first));
            ResizableArray.copyAll(b, a);
            System.out.println(b.count());
            System.out.println(a.count());
            System.out.println(a.contains(first));
            System.out.println(a.contains(last));
            System.out.println(b.contains(first));
            System.out.println(b.contains(last));
        }
        if ( test == 2 ) { //test IntegerArray
            IntegerArray a = new IntegerArray();
            System.out.println(a.isEmpty());
            while ( jin.hasNextInt() ) {
                a.addElement(jin.nextInt());
            }
            jin.next();
            System.out.println(a.sum());
            System.out.println(a.mean());
            System.out.println(a.countNonZero());
            System.out.println(a.count());
            IntegerArray b = a.distinct();
            System.out.println(b.sum());
            IntegerArray c = a.increment(5);
            System.out.println(c.sum());
            if ( a.sum() > 100 )
                ResizableArray.copyAll(a, a);
            else
                ResizableArray.copyAll(a, b);
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.removeElement(jin.nextInt()));
            System.out.println(a.sum());
            System.out.println(a.contains(jin.nextInt()));
            System.out.println(a.contains(jin.nextInt()));
        }
        if ( test == 3 ) { //test insanely large arrays
            LinkedList<ResizableArray<Integer>> resizable_arrays = new LinkedList<ResizableArray<Integer>>();
            for ( int w = 0 ; w < 500 ; ++w ) {
                ResizableArray<Integer> a = new ResizableArray<Integer>();
                int k =  2000;
                int t =  1000;
                for ( int i = 0 ; i < k ; ++i ) {
                    a.addElement(i);
                }

                a.removeElement(0);
                for ( int i = 0 ; i < t ; ++i ) {
                    a.removeElement(k-i-1);
                }
                resizable_arrays.add(a);
            }
            System.out.println("You implementation finished in less then 3 seconds, well done!");
        }
        jin.close();
    }

}