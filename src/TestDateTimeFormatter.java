import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class TestDateTimeFormatter {

	public static void main(String[] args) {
		LocalDateTime ldt = LocalDateTime.of(1996, Month.DECEMBER, 19, 19, 35);
		String dateTime;
		
		// Default string representation of LocalDateTime
		System.out.println(ldt); 

		// Getting string from LocalDateTime with desired format, 
		// using DateTimeFormatter 
		dateTime = ldt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd H:m"));
		System.out.println(dateTime);
		
		dateTime = ldt.format(DateTimeFormatter.ofPattern("y MMM dd H:m"));
		System.out.println(dateTime);
		
		dateTime = ldt.format(DateTimeFormatter.ofPattern("yy-MM-dd H:m"));
		System.out.println(dateTime);
		
		dateTime = ldt.format(DateTimeFormatter.ofPattern("dd MMM, y H:m"));
		System.out.println(dateTime);
		
		dateTime = ldt.format(DateTimeFormatter.ofPattern("dd MMMM, y H:m"));
		System.out.println(dateTime);
		
		// Parsing LocalDateTime from string using DateTimeFormatter
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yy, H:m");
		ldt = LocalDateTime.parse("19 Dec 96, 13:55", formatter);
		System.out.println(ldt);
		
		formatter = DateTimeFormatter.ofPattern("y-MM-dd, H:m:s");
		ldt = LocalDateTime.parse("1856-06-13, 13:55:18", formatter);
		System.out.println(ldt);
		
		// format using a delimiter (') 
		formatter = DateTimeFormatter.ofPattern("'Date: 'yy MMM dd, 'Time: 'H:m:s");
		String date = "Date: 96 Jan 15, Time: 15:17:34";
		ldt = LocalDateTime.parse(date, formatter);
		System.out.println(ldt);
	}
	
}
