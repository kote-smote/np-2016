import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FindOldestPerson {

	public static void main(String[] args) {
		try {
			findWithStream(new FileInputStream(args[0]));
		} catch (FileNotFoundException e) {
			System.err.println(e);
		}
	}
	
	static void findWithStream(InputStream inputStream) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String[] oldest = reader.lines()
                .map(line -> line.split("\\s+"))
                .reduce(new String[]{}, (prev, next) -> {
                	if (prev.length == 0) return next;
                    if (next.length == 0) return prev;
                    int prevAge = Integer.parseInt(prev[1]);
                    int nextAge = Integer.parseInt(next[1]);
                    System.out.println(prev[0] + " " + prev[1]);
                    System.out.println(next[0] + " " + next[1]);
                    return prevAge > nextAge ? prev : next;
                });
		System.out.println("Name: " + oldest[0]);
        System.out.println("Age: " + oldest[1]);
	}
	
}
