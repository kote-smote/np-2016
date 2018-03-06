import java.util.*;

/**
 * Created by martin on 12/28/16.
 */
public class Airports {
    private Map<String, Airport> airports;


    public Airports() {
        this.airports = new HashMap<>();
    }

    public void addAirport(String name, String country, String code, int passengersYearly) {
        Airport a = new Airport(name, country, code, passengersYearly);
        airports.put(a.code, a);
    }

    public void addFlights(String from, String to, int time, int duration) {
        Flight f = new Flight(from, to, time, duration);
        airports.get(from).addDepartingFlight(f);
        airports.get(to).addArrivingFlight(f);
    }

    public void showDirectFlightsFromTo(String from, String to) {
        Airport a = this.airports.get(from);
        a.showDirectFlightsTo(to);
    }

    public void showDirectFlightsTo(String to) {
        this.airports.get(to).showAllArrivingFlights();
    }

    public void showFlightsFromAirport(String code) {
        Airport a = this.airports.get(code);
        a.showAllDepartingFlights();
    }
}

class Airport {
    String name;
    String country;
    String code;
    int passengersYearly;
    Map<String, TreeSet<Flight>> departingFlights;
    TreeSet<Flight> arrivingFlights;

    public Airport(String name, String country, String code, int passengersYearly) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengersYearly = passengersYearly;
        departingFlights = new TreeMap<>();
        arrivingFlights = new TreeSet<>(Comparator.comparing(Flight::getTime).thenComparing(Flight::getFrom));
    }

    public void showDirectFlightsTo(String code) {
        TreeSet<Flight> res = departingFlights.get(code);
        if (res == null)
            System.out.println("No flights from " + this.code +  " to " + code);
        else
            res.forEach(System.out::println);
    }

    public void showAllArrivingFlights() {
        this.arrivingFlights.forEach(System.out::println);
    }

    public void showAllDepartingFlights() {
        List<Flight> ls = new ArrayList<>();
        System.out.println(this.name + " (" + this.code + ")");
        System.out.println(this.country);
        System.out.println(this.passengersYearly);

        for (Map.Entry<String, TreeSet<Flight>> entry : this.departingFlights.entrySet()) {
            entry.getValue().forEach(each -> ls.add(each));
        }
        int index = 1;
        for (Flight f : ls)
            System.out.println(index++ + ". " + f);
    }

    public void addDepartingFlight(Flight flight) {
        this.departingFlights.computeIfPresent(flight.to, (key, set) -> {
            set.add(flight);
            return set;
        });
        TreeSet<Flight> s = this.departingFlights.computeIfAbsent(flight.to,
                key -> new TreeSet<Flight>(Comparator.comparing(Flight::getTime)));
        s.add(flight);
    }

    public void addArrivingFlight(Flight flight) {
        this.arrivingFlights.add(flight);
    }

}

class Flight {
    String from;
    String to;
    int time;
    int duration;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }

    public int getTime() {
        return time;
    }

    public String getFrom() {
        return from;
    }

    @Override
    public String toString() {
        String H;
        int h = duration / 60;
        String M;
        int m = duration % 60;
        if (m < 10) M = "0" + m;
        else M = "" + m;
        String dur = "" + h + "h" + M + "m";
        String startTime = convert(this.time);
        String endTime = convert(this.time + this.duration);
        int a = Integer.parseInt(startTime.substring(0, 2));
        int b = Integer.parseInt(endTime.substring(0, 2));
        if (a > b)
            dur = "+1d " + dur;
        return String.format("%s-%s %s-%s %s", from, to, startTime, endTime, dur);
    }

    private String convert(int min) {
        int h = (min / 60) % 24;
        String H;
        if (h < 10) H = "0" + h;
        else H = "" + h;
        //if (h == 24) H = "00";

        int m = min % 60;
        String M;
        if (m < 10) M = "0" + m;
        else M = "" + m;
        return H + ":" + M;
    }

}
