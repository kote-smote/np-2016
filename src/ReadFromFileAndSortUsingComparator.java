import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import samples.Person;

public class ReadFromFileAndSortUsingComparator {
	
	public static void main(String[] args) throws IOException {
		List<Person> people = loadPeople("people.txt");
		
		people.forEach(each -> System.out.println(each.toString()));
		
		Collections.sort(people, new Comparator<Person>(){

			@Override
			public int compare(Person p1, Person p2) {
				return p1.birthYear < p2.birthYear ? -1
					 : p1.birthYear > p2.birthYear ? 1 : 0;
			}
			
		});
		
		System.out.println("SORTED:");
		people.stream().forEach(each -> System.out.println(each.toString()));
		
	} 
	
			
	public static List<Person> loadPeople(String fileName) {
			try {
				return Files.lines(Paths.get(fileName))
						.map(Person::getPersonFromString)
						.collect(Collectors.toList());
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
			return new ArrayList<>();
	}
}