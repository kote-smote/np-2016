import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by martin on 12/28/16.
 */
public class EventCalendar {
    private int year;
    private Map<LocalDate, TreeSet<Event>> eventsByDate;
    private Map<Month, Integer> numberOfEventsByMonth;


    public EventCalendar(int year) {
        this.year = year;
        this.eventsByDate = new HashMap<>();
        this.numberOfEventsByMonth = new TreeMap<>(Comparator.comparing(Month::getValue));
    }

    public void addEvent(String name, String location, LocalDateTime date) throws WrongDateException {
        if (this.year != date.getYear())
            throw new WrongDateException(date);

        Event event = new Event(name, location, date);

        this.eventsByDate.computeIfPresent(date.toLocalDate(), (key, set) -> {
            set.add(event);
            return set;
        });
        TreeSet<Event> set = this.eventsByDate.computeIfAbsent(date.toLocalDate(),
                key -> new TreeSet<>(Comparator.comparing(Event::getTime).thenComparing(Event::getName)));
        if (set != null)
            set.add(event);

        Month month = date.getMonth();
        this.numberOfEventsByMonth.computeIfAbsent(month, key -> 0);
        this.numberOfEventsByMonth.computeIfPresent(month, (key, freq) -> freq = freq + 1);
    }

    public void listEvents(LocalDateTime date) {
        TreeSet<Event> events = eventsByDate.get(date.toLocalDate());
        if (events == null)
            System.out.println("No events on this day!");
        else
            events.forEach(System.out::println);
    }

    public void listByMonth() {
        for (Month m : Month.values()) {
            Integer freq = this.numberOfEventsByMonth.get(m);
            if (freq == null)
                freq = 0;
            System.out.println(m.getValue() + " : " + freq);
        }
    }

}

class Event {
    private String name;
    private String location;
    LocalDateTime date;

    public Event(String name, String location, LocalDateTime date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    @Override
    public String toString() {
        String date = this.date.format(DateTimeFormatter.ofPattern("dd MMM, uuuu HH:mm"));
        return String.format("%s at %s, %s", date, this.location, this.name);
    }

    public String getName() {
        return name;
    }

    public LocalTime getTime() {
        return date.toLocalTime();
    }
}

class WrongDateException extends Exception {
    LocalDateTime date;

    public WrongDateException(LocalDateTime date) {
        this.date = date;
    }

    public String getMessage() {
        String date = this.date.format(DateTimeFormatter.ofPattern("dd MMM, uuuu HH:mm"));
        return String.format("Wrong date: %s", date.toString());
    }
}
