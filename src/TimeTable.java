import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

enum TimeFormat {
	FORMAT_24, FORMAT_AMPM
}

public class TimeTable {
	List<String> times;
	
	public TimeTable() {
		times = new ArrayList<>();
	}
	
	public static void main(String[] args) throws IOException {
		TimeTable table = new TimeTable();
		table.readTimes(System.in);
		table.writeTimes(System.out, TimeFormat.FORMAT_24);
	}
	
	void readTimes(InputStream inputStream) throws IOException {
		Scanner scanner = new Scanner(inputStream);
		String regexTime = "([01]?[0-9]|2[0-3]).+[0-5][0-9]";
		String regexFormat = "([01]?[0-9]|2[0-3])[\\.,:][0-5][0-9]";
		while (scanner.hasNext()) {
			System.out.println("sdfsf");
			String time = scanner.next();
			if (!time.matches(regexTime))
				throw new InvalidTimeException(time);
			if (!time.matches(regexFormat))
				throw new UnsupportedFormatException(time);
			times.add(time.replace(".", ":"));
		}
		scanner.close();
	}
	
	public void writeTimes(OutputStream outputStream, TimeFormat format) {
		PrintWriter writer = new PrintWriter(outputStream);
//		times.sort(String::compareTo);
		for (String time : times) {
//			writer.println(time);
			System.out.println(time);
		}
//		writer.flush();
	}	
}

class UnsupportedFormatException extends RuntimeException {
    String msg;
    
    public UnsupportedFormatException() {
    	super();
        msg = "";
    }
    
    public UnsupportedFormatException(String msg) {
    this.msg = msg;
    }

}

class InvalidTimeException extends RuntimeException {
    String msg;
    
    public InvalidTimeException() {
    	super();
        msg = "";
    }
    
    public InvalidTimeException(String msg) {
    this.msg = msg;
    }

}
