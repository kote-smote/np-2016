import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ContactsTester {

	public static void main(String[] args) {
		
        Scanner scanner = new Scanner(System.in);

		int tests = scanner.nextInt();
		Faculty faculty = null;

		int rvalue = 0;
		long rindex = -1;

		DecimalFormat df = new DecimalFormat("0.00");

		for (int t = 0; t < tests; t++) {

			rvalue++;
			String operation = scanner.next();

			switch (operation) {
			case "CREATE_FACULTY": {
				String name = scanner.nextLine().trim();
				int N = scanner.nextInt();

				Student[] students = new Student[N];

				for (int i = 0; i < N; i++) {
					rvalue++;

					String firstName = scanner.next();
					String lastName = scanner.next();
					String city = scanner.next();
					int age = scanner.nextInt();
					long index = scanner.nextLong();

					if ((rindex == -1) || (rvalue % 13 == 0))
						rindex = index;

					Student student = new Student(firstName, lastName, city,
							age, index);
					students[i] = student;
				}

				faculty = new Faculty(name, students);
				break;
			}

			case "ADD_EMAIL_CONTACT": {
				long index = scanner.nextInt();
				String date = scanner.next();
				String email = scanner.next();

				rvalue++;

				if ((rindex == -1) || (rvalue % 3 == 0))
					rindex = index;

				faculty.getStudent(index).addEmailKontakt(date, email);
				break;
			}

			case "ADD_PHONE_CONTACT": {
				long index = scanner.nextInt();
				String date = scanner.next();
				String phone = scanner.next();

				rvalue++;

				if ((rindex == -1) || (rvalue % 3 == 0))
					rindex = index;

				faculty.getStudent(index).addPhoneKontakt(date, phone);
				break;
			}

			case "CHECK_SIMPLE": {
				System.out.println("Average number of contacts: "
						+ df.format(faculty.getAverageNumberOfKontakts()));

				rvalue++;

				String city = faculty.getStudent(rindex).getCity();
				System.out.println("Number of students from " + city + ": "
						+ faculty.countStudentsFromCity(city));

				break;
			}

			case "CHECK_DATES": {

				rvalue++;

				System.out.print("Latest contact: ");
				Kontakt latestKontakt = faculty.getStudent(rindex)
						.getLatestKontakt();
				if (latestKontakt.getType().equals("Email"))
					System.out.println(((EmailKontakt) latestKontakt)
							.getEmail());
				if (latestKontakt.getType().equals("Phone"))
					System.out.println(((PhoneKontakt) latestKontakt)
							.getPhone()
							+ " ("
							+ ((PhoneKontakt) latestKontakt).getOperator()
									.toString() + ")");

				if (faculty.getStudent(rindex).getEmailKontakts().length > 0&&faculty.getStudent(rindex).getPhoneKontakts().length > 0) {
					System.out.print("Number of email and phone contacts: ");
					System.out
							.println(faculty.getStudent(rindex)
									.getEmailKontakts().length
									+ " "
									+ faculty.getStudent(rindex)
											.getPhoneKontakts().length);

					System.out.print("Comparing dates: ");
					int posEmail = rvalue
							% faculty.getStudent(rindex).getEmailKontakts().length;
					int posPhone = rvalue
							% faculty.getStudent(rindex).getPhoneKontakts().length;

					System.out.println(faculty.getStudent(rindex)
							.getEmailKontakts()[posEmail].isNewerThan(faculty
							.getStudent(rindex).getPhoneKontakts()[posPhone]));
				}

				break;
			}

			case "PRINT_FACULTY_METHODS": {
				System.out.println("Faculty: " + faculty.toString());
				System.out.println("Student with most contacts: "
						+ faculty.getStudentWithMostKontakts().toString());
				break;
			}

			}

		}

		scanner.close();
	}
}


abstract class Kontakt {
	private LocalDate creationDate;
	
	public Kontakt(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("y-MM-dd");
		creationDate = LocalDate.parse(date, formatter);
	}
	
	public boolean isNewerThan(Kontakt other) {
		return (this.creationDate.isAfter(other.creationDate));
	}
	
	public abstract String getType();
	
	public abstract String toString();
}

class EmailKontakt extends Kontakt {
	private String email;
	
	public EmailKontakt(String date, String email) {
		super(date);
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	@Override
	public String getType() {
		return "Email";
	}
	
	@Override
	public String toString() {
		return String.format("\"%s\"", email);
	}
}

class PhoneKontakt extends Kontakt {
	enum Operator { VIP, ONE, TMOBILE }
	private String phone;
	Operator operator;
	
	public PhoneKontakt(String date, String phone) {
		super(date);
		this.phone = phone;
		if (phone.charAt(2) == '7' || phone.charAt(2) == '8')
			operator = Operator.VIP;
		if (phone.charAt(2) == '5' || phone.charAt(2) == '6')
			operator = Operator.ONE;
		if (phone.charAt(2) == '0' || phone.charAt(2) == '1' || phone.charAt(2) == '2')
			operator = Operator.TMOBILE;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public Operator getOperator() {
		return operator;
	}
	
	@Override 
	public String getType() {
		return "Phone";
	}
	
	@Override
	public String toString() {
		return String.format("\"%s\"", phone);
	}
}

class Student {
	private String firstName;
	private String lastName;
	private String city;
	private int age;
	private long index;
	List<Kontakt> contacts;
	
	public Student(String firstName, String lastName, String city, int age, long index) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.city = city;
		this.age = age;
		this.index = index;
		contacts = new ArrayList<>();
	}
	
	public void addEmailKontakt(String date, String email) {
		contacts.add(new EmailKontakt(date, email));
	}
	
	public void addPhoneKontakt(String date, String phone) {
		contacts.add(new PhoneKontakt(date, phone));
	}
	
	public Kontakt[] getEmailKontakts() {
		return contacts.stream()
				.filter(contact -> contact.getType().equals("Email"))
				.collect(Collectors.toList())
				.toArray(new Kontakt[]{});
	}
	
	public Kontakt[] getPhoneKontakts() {
		return contacts.stream()
				.filter(contact -> contact.getType().equals("Phone"))
				.collect(Collectors.toList())
				.toArray(new Kontakt[]{});
	}
	
	public String getCity() {
		return city;
	}
	
	public String getFullName() {
		return String.format("%s %s", firstName, lastName);
	}
	
	public long getIndex() {
		return index;
	}

	public Kontakt getLatestKontakt() {    
        return contacts.stream()
				.reduce(null, (prev, next) -> {
					if (prev == null) return next;
					if (next == null) return prev;
					return prev.isNewerThan(next) ? prev : next;
				});
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("{");
		result.append("\"ime\":\"");
		result.append(firstName);
		result.append("\", ");
		result.append("\"prezime\":\"");
		result.append(lastName);
		result.append("\", ");
		result.append("\"vozrast\":");
		result.append(age);
		result.append(", ");
		result.append("\"grad\":\"");
		result.append(city);
		result.append("\", ");
		result.append("\"indeks\":");
		result.append(index);
		result.append(", ");
		result.append("\"telefonskiKontakti\":");
		result.append(new ArrayList<Kontakt>(Arrays.asList(getPhoneKontakts())));
		result.append(", ");
		result.append("\"emailKontakti\":");
		result.append(new ArrayList<Kontakt>(Arrays.asList(getEmailKontakts())));
		result.append("}");
		return result.toString();
	}
	
	public int numberOfContacts() {
		return contacts.size();
	}
}

class Faculty {
	String name;
	private Student[] students;
	
	public Faculty(String name, Student[] students) {
		this.name = name;
		this.students = Arrays.copyOf(students, students.length);
	}
	
	public int countStudentsFromCity(String cityName) {
		return (int) Arrays.stream(students)
				.filter(student -> student.getCity().equals(cityName))
				.count();
	}
	
	public Student getStudent(long index) {
		for (Student s : students) 
			if (s.getIndex() == index) 
				return s;
		throw new NoSuchElementException();
	}
	
	public double getAverageNumberOfKontakts() {
		return Arrays.stream(students)
				.mapToInt(Student::numberOfContacts)
				.average()
				.getAsDouble();
	}
	
	public Student getStudentWithMostKontakts() {
        return Arrays.stream(students)
            .max(Comparator.comparing(Student::numberOfContacts).thenComparing(Student::getIndex))
            .orElse(null);
            }
	
	public String toString() {
		StringBuilder result = new StringBuilder("{\"fakultet\":\"");
		result.append(name);
		result.append("\", \"studenti\":");
		List<Student> ls = new ArrayList<Student>(Arrays.asList(students));
		result.append(ls);
		result.append("}");
		return result.toString();		
	}
}