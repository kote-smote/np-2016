import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Arrays;

public class F1Test {

	public static void main(String[] args) {
		F1Race f1Race = new F1Race();
		f1Race.readResults(System.in);
		f1Race.printSorted(System.out);
	}

}

class F1Race {
	List<Driver> drivers;
	
	public F1Race() {
		drivers = new ArrayList<>();
	}
	
	public void readResults(InputStream inputStream) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		reader.lines()
			.forEach(line -> drivers.add(Driver.getDriverFromString(line)));
	}
	
	public void printSorted(OutputStream outputStream) {
		PrintWriter writer = new PrintWriter(outputStream);
		drivers.sort(Comparator.comparing(Driver::getBestLap));
		for (int i = 1; i <= drivers.size(); i++)
			writer.println(i + ". " + drivers.get(i-1));
        writer.flush();
	}
	
}

class Driver {
	private String name;
	private List<String> laps = new ArrayList<>(3);
	
	public Driver(String name, String[] laps) {
		this.name = name;
        this.laps = Arrays.asList(laps);
	}
    
    public static Driver getDriverFromString(String str) {
    	String[] parts = str.split(" ");
		String name = parts[0];
		String lap1 = parts[1];
        String lap2 = parts[2];
        String lap3 = parts[3];
        return new Driver(name, new String[]{lap1, lap2, lap3});
    }
	
	public String getBestLap() {
		return laps.stream()
				.min(Comparator.naturalOrder())
				.orElse(null);
	}
	
	@Override
	public String toString() {
		return String.format("%-9s %10s", name, getBestLap());
	}
}
