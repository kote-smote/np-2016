import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.Consumer;

public class TestFunctionalInterface {
	
	public static void execute(Runnable r) {
		r.run();
	}
	
	public static Integer convertDoubleToInt(Function<Double, Integer> f, 
			double number) {
		return f.apply(number);
	}
	
	public static void printList(List<Integer> listOfInt, 
			Consumer<Integer> consumer) {
		for (Integer i : listOfInt)
			consumer.accept(i);
	}
	
	public static List<Integer> filterList(List<Integer> listOfInts, 
			Predicate<Integer> predicate) {
		List<Integer> filteredList = new ArrayList<>();
		for (Integer i : listOfInts) {
			if (predicate.test(i))
				filteredList.add(i);
		}
		return filteredList;
	}
	
	public static void main(String[] args) {
		
		// Example of Runnable functional interface (not part of java.util.function)
		System.out.println("TEST Runnable<T> functional interface:");
		int num = 6;
		execute(() -> System.out.println(num));
		execute(() -> {
			System.out.println("print somth");
			System.out.println("print someth else");
		});
		
		// Example of Function<T, R> functional interface
		System.out.println("\nTEST Function<T, R> functional interface:");
		System.out.println(convertDoubleToInt(x -> x.intValue(), 4.5));
		
		// Example of Consumer<T> functional interface
		System.out.println("\nTEST Consumer<T> functional interface:");
		Consumer<Integer> printer = i -> System.out.print(i + " "); 
		System.out.println("only using accept() method of Consumer:");
		printList(new ArrayList<Integer>(Arrays.asList(2, 5, 1, 6)), printer);
		System.out.println();
		// Consumer<T> using andThen() default method
		System.out.println("using andThen() method of Consumer:");
		printList(new ArrayList<Integer>(Arrays.asList(2, 5, 1, 6)), 
				printer.andThen(i -> System.out.print("(printed " + i + ") ")));
		System.out.println();
		
		// Example of Predicate<T> functional interface
		System.out.println("\nTEST Predicate<T> functional interface:");
		List<Integer> ints = new ArrayList<>(Arrays.asList(3, 6, -1, 8, -8, 9));
		System.out.println("ints: " + ints);
		Predicate<Integer> positive = i -> i > 0;
		Predicate<Integer> negative = i -> i < 0;
		Predicate<Integer> even = i -> i % 2 == 0;
		List<Integer> positiveInts = filterList(ints, positive);
		List<Integer> negativeInts = filterList(ints, negative);
		List<Integer> evenInts = filterList(ints, even);
		System.out.println("positive: " + positiveInts);
		System.out.println("negative: " + negativeInts);
		System.out.println("even: " + evenInts);	
		
		// Example of Supplier<T> functional interface
		System.out.println("\nTEST Supplier<T> functional interface:");
		Supplier<String> helloStringSupplier = () -> new String("Hello");
		String hello = helloStringSupplier.get();
		System.out.println(hello);
	}
}