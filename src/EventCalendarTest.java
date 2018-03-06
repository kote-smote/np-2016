import java.text.ParseException;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventCalendarTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd.MM.uuuu HH:mm");
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            LocalDateTime date = LocalDateTime.parse(parts[2], df);
            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }
        LocalDateTime date = LocalDateTime.parse(scanner.nextLine(), df);
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}