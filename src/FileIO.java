import java.io.*;
import java.util.*;

public class FileIO {
	public static void main(String[] args) throws IOException {
		List<String> lines1 = new ArrayList<>();
		List<String> lines2 = new ArrayList<>();

		// Reading with BufferedReader
		BufferedReader br = new BufferedReader(new FileReader("file_name.txt"));
		String line;
		while ((line = br.readLine()) != null) 
			lines1.add(line);

		
		// Reading with Scanner
		Scanner sc = new Scanner(new FileInputStream("file_name.txt"));

		while (sc.hasNextLine()) 
			lines2.add(sc.nextLine());

		
		// Writing to file
		PrintWriter writer = new PrintWriter("out.txt");
		for (String ln : lines1) 
			writer.println(ln);

		writer.flush();
		writer.close();
		br.close();
		sc.close();
	}
}