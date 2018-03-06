import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.function.Predicate;

/**
 * @author Martin Kotevski
 * Created on 12/24/16.
 * Check problems_info/WeatherStation/text.txt to see the
 * explanation of this problem
 */
public class WeatherStation {
    private int days;
    private TreeSet<Measurement> measurements;

    public WeatherStation(int days) {
        this.days = days;

        Comparator<Measurement> SmallTimeDiffCmpr = (m1, m2) -> {
            long span = Math.abs(Duration.between(m1.date, m2.date).getSeconds());
            if (span < 150) return 0;
            else return m1.date.compareTo(m2.date);
        };
        this.measurements = new TreeSet<>(SmallTimeDiffCmpr);
    }

    public int total() {
        return measurements.size();
    }

    public void addMeasurement(float temperature, float wind, float humidity,
                               float visibility, LocalDateTime date) {

        Measurement m = new Measurement(temperature, wind, humidity,
                visibility, date);

        if (!measurements.add(m))
            return;

        Predicate<Measurement> old = measurement ->
                m.date.minusDays(days).isAfter(measurement.date);
        measurements.removeIf(old);
    }

    public void status(LocalDateTime from, LocalDateTime to) {

        Predicate<Measurement> isInRange = measurement ->
                !measurement.date.isBefore(from) && !measurement.date.isAfter(to); // to be inclusive

        double averageTemperature = measurements
                .stream()
                .filter(isInRange)
                .mapToDouble(Measurement::getTemperature)
                .average()
                .orElse(0);

        measurements.stream().filter(isInRange).forEach(System.out::println);
        if (averageTemperature == 0)
            throw new RuntimeException();
        System.out.printf("Average temperature: %.2f\n", averageTemperature);
    }
}

class Measurement {
    float temperature;
    float wind;
    float humidity;
    float visibility;
    LocalDateTime date;

    Measurement(float temperature, float wind, float humidity,
                       float visibility, LocalDateTime date) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    float getTemperature() {
        return temperature;
    }

    @Override
    public String toString() {
        return String.format("%4.1f  %5.1f km/h  %5.1f%%  %6.1f km  %s", temperature,
                wind, humidity, visibility, date);
    }
}
