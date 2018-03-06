/**
 * Napreno Programiranje - Lab 01 - Zadaca 2
 */

import java.util.*;
import java.util.stream.Collectors;

class Account {
    private String name;
    private String balance;
    private static long counter = 0;
    private final long id = counter++;

    public Account(String name, String balance) {
        this.name = name;
        this.balance = balance;
    }

    public String getBalance() { return balance; }

    public String getName() { return name; }

    public long getId() { return id; }

    public void setBalance(String b) {
        balance = b;
    }

    public String toString() {
        return ("Name: " + name + "\n" 
              + "Balance: " + balance + "\n");
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((balance == null) ? 0 : balance.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (id == other.id)
			return true;
		return false;		
	}
}

// Immutable abstract class
abstract class Transaction {
    protected final String amount;
    protected final long fromId;
    protected final long toId;
    protected final String description;

    public Transaction(long fromId, long toId, String description, String amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
        this.description = description;
    }

    public String getAmount() { return amount; }

    abstract public double getProvision();
    
    public long getFromId() { return fromId; }

    public long getToId() { return toId; }

    public String getDescription() { return description; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Transaction other = (Transaction) obj;
        if (amount.equals(other.amount)&&fromId == other.fromId&&toId == other.toId 
            && description.equals(other.description)
        ) 
                return true;     
        else 
            return false;
    }
}

class FlatAmountProvisionTransaction extends Transaction {
    private final String flatAmount;

    public FlatAmountProvisionTransaction(long fromId, long toId, String amount, 
        String flatAmount) {
        super(fromId, toId, "FlatAmount", amount);
        this.flatAmount = flatAmount;
    }

    public double getProvision() { 
        return Double.parseDouble(flatAmount.substring(0, flatAmount.length() - 1)); 
    }
}

class FlatPercentProvisionTransaction extends Transaction {
    private final int percent;

    public FlatPercentProvisionTransaction(long fromId, long toId, String amount, 
        int centsPerDollar) {
        super(fromId, toId, "FlatPercent", amount);
        this.percent = centsPerDollar;
    }

    public double getProvision() { return percent; } 
}

class Bank {
    private String name;
    private Account[] accounts;
    private double totalTransfers;
    private double totalProvision;

    public Bank(String name, Account[] acc) {
        this.name = name;
        accounts = Arrays.copyOf(acc, acc.length);
    }

    public boolean makeTransaction(Transaction tr) {
        boolean legitAccount = false;
        Account from = null, to = null;
        for (int i = 0; i < accounts.length; ++i) {
            if (accounts[i].getId() == tr.getFromId()) {
                legitAccount = true;
                from = accounts[i];
            }
        }
        if (legitAccount) {
            legitAccount = false;
            for (int i = 0; i < accounts.length; ++i) {
                if (accounts[i].getId() == tr.getToId()) {
                    legitAccount = true;
                    to = accounts[i];
                }
            }
            if (legitAccount) {
            	double amount = Double.parseDouble(tr.getAmount().substring(0, tr.getAmount().length() - 1));
                double provision = tr.getProvision();
                double balanceFrom = Double.parseDouble(from.getBalance().substring(0, from.getBalance().length() - 1));
                double balanceTo = Double.parseDouble(to.getBalance().substring(0, to.getBalance().length() - 1));
                
                // If from and to are the same account
                if (to == from) {
                    if (tr.getDescription().equals("FlatAmount")) {
                		from.setBalance(String.format("%.2f$", balanceFrom - provision));
                    	totalProvision += provision;
                    }
                    else if (tr.getDescription().equals("FlatPercent")) {
                    	from.setBalance(String.format("%.2f$", balanceFrom - ((double) provision / 100) * (int) amount));
                        totalProvision += ((double) provision / 100 * (int) amount);
                	}
                    totalTransfers += amount;
            		return true;
            	}
                
                double totalToPay = 0;
                if (tr.getDescription().equals("FlatAmount"))
                    totalToPay = amount + provision;
                else if (tr.getDescription().equals("FlatPercent"))
                    totalToPay = amount + ((double) provision / 100) * (int) amount;

                if (balanceFrom >= totalToPay) {
                    from.setBalance(String.format("%.2f$", balanceFrom - totalToPay));
                    to.setBalance(String.format("%.2f$", balanceTo + amount));
                    totalTransfers += amount;
                    if (tr.getDescription().equals("FlatAmount"))
                        totalProvision += provision;
                    else if (tr.getDescription().equals("FlatPercent"))
                        totalProvision += ((double) provision / 100 * (int) amount);
                    return true;
                }
            } else return false;
        } else return false;
        return false;
    }

    public String totalTransfers() {
        return String.format("%.2f$", totalTransfers);
    }

    public String totalProvision() {
        return String.format("%.2f$", totalProvision);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Name: ");
        sb.append(name);
        sb.append("\n\n");
        for (int i = 0; i < accounts.length; i++) {
               sb.append(accounts[i]);
        } 
        return sb.toString();  
    }

    public Account[] getAccounts() {return accounts;}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(accounts);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(totalProvision);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(totalTransfers);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bank other = (Bank) obj;
		if (!Arrays.equals(accounts, other.accounts))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(totalProvision) != Double.doubleToLongBits(other.totalProvision))
			return false;
		if (Double.doubleToLongBits(totalTransfers) != Double.doubleToLongBits(other.totalTransfers))
			return false;
		return true;
	}
    
    
}


public class BankTester {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", "20.00$");
        Account a2 = new Account("Andrej", "20.00$");
        Account a3 = new Account("Andrej", "30.00$");
        Account a4 = new Account("Gajduk", "20.00$");
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1)&&!a1.equals(a2)&&!a2.equals(a1) && !a3.equals(a1)
                && !a4.equals(a1)
                && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, "50.00$", "50.00$");
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, "20.00$", "10.00$");
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, "20.00$", 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, "50.00$", 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, "20.00$", 10);
        if (fa1.equals(fa1) &&
                !fa2.equals(null) &&
                fa2.equals(fa1) &&
                fa1.equals(fa2) &&
                fa1.equals(fa3) &&
                !fa1.equals(fa4) &&
                !fa1.equals(fa5) &&
                !fa1.equals(fp1) &&
                fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts[] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Test", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        long from_id = a2.getId();
        long to_id = a3.getId();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, "3.00$", "3.00$");
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), jin.nextLine());
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    String amount = jin.nextLine();
                    String parameter = jin.nextLine();
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + t.getAmount());
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + bank.totalProvision());
                    System.out.println("Total transfers: " + bank.totalTransfers());
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, String amount, String o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, Integer.parseInt(o));
        }
        return null;
    }


}
