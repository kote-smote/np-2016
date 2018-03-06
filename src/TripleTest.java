import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Scanner;

public class TripleTest {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int a = scanner.nextInt();
		int b = scanner.nextInt();
		int c = scanner.nextInt();
		Triple<Integer> tInt = new Triple<Integer>(a, b, c);
		System.out.printf("%.2f\n", tInt.max());
		System.out.printf("%.2f\n", tInt.average());
		tInt.sort();
		System.out.println(tInt);
		float fa = scanner.nextFloat();
		float fb = scanner.nextFloat();
		float fc = scanner.nextFloat();
		Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
		System.out.printf("%.2f\n", tFloat.max());
		System.out.printf("%.2f\n", tFloat.average());
		tFloat.sort();
		System.out.println(tFloat);
		double da = scanner.nextDouble();
		double db = scanner.nextDouble();
		double dc = scanner.nextDouble();
		Triple<Double> tDouble = new Triple<Double>(da, db, dc);
		System.out.printf("%.2f\n", tDouble.max());
		System.out.printf("%.2f\n", tDouble.average());
		tDouble.sort();
		System.out.println(tDouble);
		scanner.close();
	}
}

class Triple<T extends Number> {
	private List<T> list = new ArrayList<>(3);
	
	public Triple(T n1, T n2, T n3) {
		list.add(n1);
		list.add(n2);
		list.add(n3);
	}
	
	public double max() {
		return list.stream()
			.mapToDouble(n->n.doubleValue())
			.max()
			.getAsDouble();
	}
	
	public double average() {
		return list.stream()
				.mapToDouble(n->n.doubleValue())
				.average()
				.getAsDouble();
	}
	
	public void sort() {
		list = list.stream()
			.sorted(Comparator.comparing(Number::doubleValue))
			.collect(Collectors.toList());
	}
	
	public String toString() {
		return String.format("%.2f %.2f %.2f", list.get(0).doubleValue(), list.get(1).doubleValue(), list.get(2).doubleValue());
	}
	
}