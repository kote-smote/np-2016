import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Random;

public class PhonebookTester {

	public static void main(String[] args) throws Exception {
		Scanner jin = new Scanner(System.in);
		String line = jin.nextLine();
		switch( line ) {
			case "test_contact":
				testContact(jin);
				break;
			case "test_phonebook_exceptions":
				testPhonebookExceptions(jin);
				break;
			case "test_usage":
				testUsage(jin);
				break;
		}
	}

//	private static void testFile(Scanner jin) throws Exception {
//		PhoneBook phonebook = new PhoneBook();
//		while ( jin.hasNextLine() ) 
//			phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
//		String text_file = "phonebook.txt";
//		PhoneBook.saveAsTextFile(phonebook,text_file);
//		PhoneBook pb = PhoneBook.loadFromTextFile(text_file);
//        if ( ! pb.equals(phonebook) ) System.out.println("Your file saving and loading doesn't seem to work right");
//        else System.out.println("Your file saving and loading works great. Good job!");
//	}

	private static void testUsage(Scanner jin) throws Exception {
		PhoneBook phonebook = new PhoneBook();
		while ( jin.hasNextLine() ) {
			String command = jin.nextLine();
			switch ( command ) {
				case "add":
					phonebook.addContact(new Contact(jin.nextLine(),jin.nextLine().split("\\s++")));
					break;
				case "remove":
					phonebook.removeContact(jin.nextLine());
					break;
				case "print":
					System.out.println(phonebook.numberOfContacts());
					System.out.println(Arrays.toString(phonebook.getContacts()));
					System.out.println(phonebook.toString());
					break;
				case "get_name":
					System.out.println(phonebook.getContactForName(jin.nextLine()));
					break;
				case "get_number":
					System.out.println(Arrays.toString(phonebook.getContactsForNumber(jin.nextLine())));
					break;
			}			
		}
	}

	private static void testPhonebookExceptions(Scanner jin) {
		PhoneBook phonebook = new PhoneBook();
		boolean exception_thrown = false;
		try {
			while ( jin.hasNextLine() ) {
				phonebook.addContact(new Contact(jin.nextLine()));
			}
		}
		catch ( InvalidNameException e ) {
			System.out.println(e.name);
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw InvalidNameException");
        /*
		exception_thrown = false;
		try {
		phonebook.addContact(new Contact(jin.nextLine()));
		} catch ( MaximumSizeExceddedException e ) {
			exception_thrown = true;
		}
		catch ( Exception e ) {}
		if ( ! exception_thrown ) System.out.println("Your addContact method doesn't throw MaximumSizeExcededException");
        */
	}

	private static void testContact(Scanner jin) throws Exception {		
		boolean exception_thrown = true;
		String names_to_test[] = { "And\nrej","asd","AAAAAAAAAAAAAAAAAAAAAA","Ð�Ð½Ð´Ñ€ÐµÑ˜A123213","Andrej#","Andrej<3"};
		for ( String name : names_to_test ) {
			try {
				new Contact(name);
				exception_thrown = false;
			} catch (InvalidNameException e) {
				exception_thrown = true;
			} 
			if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNameException");
		}
		String numbers_to_test[] = { "+071718028","number","078asdasdasd","070asdqwe","070a56798","07045678a","123456789","074456798","073456798","079456798" };
		for ( String number : numbers_to_test ) {
			try {
				new Contact("Andrej",number);
				exception_thrown = false;
			} catch (InvalidNumberException e) {
				exception_thrown = true;
			} 
			if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw an InvalidNumberException");
		}
		String nums[] = new String[10];
		for ( int i = 0 ; i < nums.length ; ++i ) nums[i] = getRandomLegitNumber();
		try {
			new Contact("Andrej",nums);
			exception_thrown = false;
		} catch (MaximumSizeExceddedException e) {
			exception_thrown = true;
		} 
		if ( ! exception_thrown ) System.out.println("Your Contact constructor doesn't throw a MaximumSizeExceddedException");
		Random rnd = new Random(5);
		Contact contact = new Contact("Andrej",getRandomLegitNumber(rnd),getRandomLegitNumber(rnd),getRandomLegitNumber(rnd));
		System.out.println(contact.getName());
		System.out.println(Arrays.toString(contact.getNumbers()));
		System.out.println(contact.toString());
		contact.addNumber(getRandomLegitNumber(rnd));
		System.out.println(Arrays.toString(contact.getNumbers()));
		System.out.println(contact.toString());
		contact.addNumber(getRandomLegitNumber(rnd));
		System.out.println(Arrays.toString(contact.getNumbers()));
		System.out.println(contact.toString());
	}

	static String[] legit_prefixes = {"070","071","072","075","076","077","078"};
	static Random rnd = new Random();
	
	private static String getRandomLegitNumber() {
		return getRandomLegitNumber(rnd);
	}
	
	private static String getRandomLegitNumber(Random rnd) {
		StringBuilder sb = new StringBuilder(legit_prefixes[rnd.nextInt(legit_prefixes.length)]);
		for ( int i = 3 ; i < 9 ; ++i ) 
			sb.append(rnd.nextInt(10));
		return sb.toString();
	}
	

}


class PhoneBook {
	private List<Contact> contacts;
	
	public PhoneBook() {
		contacts = new ArrayList<>();
	}
	
	public void addContact(Contact contact) {
		if (contacts.size() == 250)
			throw new MaximumSizeExceddedException();
		if (contacts.stream().anyMatch(c -> c.equals(contact))) 
			throw new InvalidNameException(contact.getName());
		contacts.add(contact);
	}
	
	public Contact getContactForName(String name) {
		for (Contact c : contacts) {
			if (c.getName().equals(name))
				return c;
		}
		return null;
	}
	
	public int numberOfContacts() {
		return contacts.size();
	}
	
	public Contact[] getContacts() {
		sortContactsByName();
        return contacts.toArray(new Contact[]{});
	}
	
	public boolean removeContact(String name) {
		for (int i = 0; i < contacts.size(); i++) {
			if (contacts.get(i).getName().equals(name)) {
				contacts.remove(i);
				return true;
			}
		}
		return false;
	}
	
	public static boolean saveAsTextFile(PhoneBook phonebook, String path) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(path);
			writer.print(phonebook);
			return true;
		} catch (IOException e) {
			return false;
		} finally {
			if (writer != null)
				writer.close();
		}
	}
	
	public static PhoneBook loadFromTextFile(String path) throws IOException {
		PhoneBook phonebook = new PhoneBook();
		Contact contact;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(path));
			String line;
			while ((line = reader.readLine()) != null) {
				StringBuilder sb = new StringBuilder();
				String name = line;
				sb.append(name);
				sb.append("\n");
				int numberOfPhonenumbers = Integer.parseInt(reader.readLine());
				sb.append(numberOfPhonenumbers);
				sb.append("\n");
				for (int i = 0; i < numberOfPhonenumbers; i++) {
					sb.append(reader.readLine());
					sb.append("\n");
				}
				try {
					contact = Contact.valueOf(sb.toString());
					phonebook.addContact(contact);
				} catch (RuntimeException e) {
					throw new InvalidFormatException();
				}
				if ((line = reader.readLine()) == null) // skip empty line
					break;
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if (reader != null)
				reader.close();
		}
		return phonebook;
	}
	
	public Contact[] getContactsForNumber(String number_prefix) {
		sortContactsByName();
		List<Contact> resList = new ArrayList<>();
		Pattern pattern = Pattern.compile(number_prefix);
		for (Contact contact : contacts) {
			for (String number : contact.getNumbers()) {
				Matcher matcher = pattern.matcher(number);
				if (matcher.lookingAt()) {
					resList.add(contact);
					break;
				}
			}
		}
		return resList.toArray(new Contact[resList.size()]);
	}		
	
	public String toString() {
		sortContactsByName();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < contacts.size(); i++) {
			sb.append(contacts.get(i));
			sb.append("\n");
		}
		return sb.toString();
	}
	
	// PRIVATE METHODS
	private void sortContactsByName() {
		contacts.sort(Comparator.comparing(Contact::getName));
	}
		
}

class Contact {
	private String name;
	private List<String> phonenumbers;
	
	public Contact(String name, String... phonenumber) {
		if (phonenumber.length > 5)
			throw new MaximumSizeExceddedException();
		
		if(!isValidName(name)) 
			throw new InvalidNameException();
		this.name = name;
		
		phonenumbers = new ArrayList<>();
		for (String number : phonenumber) {
			if(!isValidNumber(number))
				throw new InvalidNumberException();
			phonenumbers.add(number);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String[] getNumbers() {
		sortNumberslexicographically();
        return phonenumbers.toArray(new String[]{});
	}
	
	public void addNumber(String number) {
		if (phonenumbers.size() == 5)
			throw new MaximumSizeExceddedException();
		if (!isValidNumber(number)) 
			throw new InvalidNumberException();
		phonenumbers.add(number);
	}
		
	public static Contact valueOf(String s) {
		Contact contact;
		String[] parts = s.split("\n");
		String name = parts[0];
		int numOfNumbers = Integer.parseInt(parts[1]);
		String[] numbers = new String[numOfNumbers];
		int index = 0;
		for (int i = 2; i < parts.length; i++)
			numbers[index++] = parts[i];
		
		try {
			contact = new Contact(name, numbers);
			return contact;
		} catch (RuntimeException e) {
			throw new InvalidFormatException();
		}
	}
	
	public boolean equals(Contact other) {
		if (other == null)
			return false;
		if (this == other)
			return true;
		return this.name.equals(other.name);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name);
		sb.append("\n");
		sb.append(phonenumbers.size());
		sb.append("\n");
		sortNumberslexicographically();
		for (int i = 0; i < phonenumbers.size(); i++) {
			sb.append(phonenumbers.get(i));
			sb.append("\n");
		}
		return sb.toString();
	}
	
	// PRIVATE METHODS 
	private boolean isValidName(String name) {
		if (name == null || !name.matches("^[a-zA-Z0-9]{5,10}")) 
			return false;
		return true;
	}
	
	private boolean isValidNumber(String number) {
		if (number == null || !number.matches("^(07)[0,1,2,5,6,7,8][0-9]{6}"))
			return false;
		return true;
	}
	
	private void sortNumberslexicographically() {
		phonenumbers.sort(Comparator.comparing(String::toString));
	}

}


/* 
 * 
 * EXCEPTION CLASSES
 *  
 */

class InvalidNameException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public final String name;
	
	public InvalidNameException() {
		super();
		name = "";
	}
		
	public InvalidNameException(String name) {
		this.name = name;
	}
}

class InvalidNumberException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidNumberException() {
		super();
	}
	
	public InvalidNumberException(String InvalidNumber) {
		System.err.println("The number " + InvalidNumber + " is not valid");
	}
}

class MaximumSizeExceddedException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public MaximumSizeExceddedException() {
		super();
	}
}

class InvalidFormatException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidFormatException() {
		super();
	}
}