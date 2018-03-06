package samples;

public class Person {
	public final String firstName;
	public final String lastName;
	public final int birthYear;

	public Person(String firstName, String lastName, int birthYear){
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthYear = birthYear;
	} 

	@Override 
	public String toString() {
		return String.format("%s %s %d", firstName, lastName, birthYear);
	}
	
	public static Person getPersonFromString(String str) {
		String[] parts = str.split(" ");
		return new Person(parts[0], parts[1], Integer.parseInt(parts[2]));
	}

}
