
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;



public class DailyTemperaturesTest {

	public static void main(String[] args) throws IOException {
		DailyTemperatures dailyTemperatures = new DailyTemperatures();
		dailyTemperatures.readTemperatures(System.in);
		System.out.println("=== Daily temperatures in Celsius (C) ===");
		dailyTemperatures.writeDailyStats(System.out, 'C');
		System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
		dailyTemperatures.writeDailyStats(System.out, 'F');
	}

}

class DailyTemperatures {
	List<DailyTemperature> dailyTemperatures;

	public DailyTemperatures() {
		dailyTemperatures = new ArrayList<>();
	}

	public void readTemperatures(InputStream in) throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = reader.readLine()) != null) {
			dailyTemperatures.add(DailyTemperature.createFromString(line));
		}

//		reader.close();
	}

	public void writeDailyStats(OutputStream out, char scale) {
		PrintWriter writer = new PrintWriter(out);
		dailyTemperatures.sort(Comparator.comparing(DailyTemperature::getDayNumber));
		for (DailyTemperature dt : dailyTemperatures) {
			writer.println(dt.getDayStatistics(scale));
		}
		writer.flush();
//		writer.close();
	}

}



class DailyTemperature {
	private int day;
	private List<Double> temeperatures;
	private boolean isCelsius;

	public DailyTemperature(int day, List<Double> temperatures, boolean isCelsius) {
		this.day = day;
		this.temeperatures = temperatures.stream().collect(Collectors.toList()); // make a copy
		this.isCelsius = isCelsius;
	}

	public static DailyTemperature createFromString(String str) {
		List<Double> temperatures = new ArrayList<>();
		String[] parts = str.split("\\s+");
		int day = Integer.parseInt(parts[0]);
		for (int i = 1; i < parts.length; i++) {
			int len = parts[i].length();
			temperatures.add(Double.parseDouble(parts[i].substring(0, len - 1)));
		}
		boolean isCelsius = false;
		if (parts[1].charAt(parts[1].length() - 1) == 'C')
			isCelsius = true;
		return new DailyTemperature(day, temperatures, isCelsius);
	}

	public int getDayNumber() {
		return this.day;
	}

	private double convertCelsiusToFahrenheit(double c) {
		return (c * 9./5) + 32;
	}

	private double convertFahrenheitToCelsius(double f) {
		return (f - 32) * 5./9;
	}

	public double maxTemperature() {
		return temeperatures.stream().mapToDouble(Double::doubleValue).max().orElse(0);
	}

	public double minTemperature() {
		return temeperatures.stream().mapToDouble(Double::doubleValue).min().orElse(0);
	}

	public double averageTemperature() {
		return temeperatures.stream().mapToDouble(Double::doubleValue).average().orElse(0);
	}

	public String getDayStatistics(char scale) {
		double max = maxTemperature();
		double min = minTemperature();
		double average = averageTemperature();
		int count = this.temeperatures.size();
		if (scale == 'C') {
			if (!isCelsius) {
				max = convertFahrenheitToCelsius(max);
				min = convertFahrenheitToCelsius(min);
				average = convertFahrenheitToCelsius(average);
			}
			return String.format("%3d: Count: %3d Min: %6.2fC Max: %6.2fC Avg: %6.2fC", day, count, min, max, average);
		}
		else {
			if (isCelsius) {
				max = convertCelsiusToFahrenheit(max);
				min = convertCelsiusToFahrenheit(min);
				average = convertCelsiusToFahrenheit(average);
			}
			return String.format("%3d: Count: %3d Min: %6.2fF Max: %6.2fF Avg: %6.2fF", day, count, min, max, average);
		}

	}

}
