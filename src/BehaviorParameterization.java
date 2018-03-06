
public class BehaviorParameterization {

	public static void main(String[] args) {
		
		// You can assign 
		MathOperation addition = (x, y) -> x + y;
		MathOperation substitution = (x, y) -> x - y;
		MathOperation multiplication = (x, y) -> x * y;
		MathOperation division = (x, y) -> { return y != 0 ? x / y : Double.POSITIVE_INFINITY; };
		
		System.out.println(calculate(4, 2, addition));
		System.out.println(calculate(4, 2, substitution));
		System.out.println(calculate(4, 2, multiplication));
		System.out.println(calculate(4, 0, division));
		
		// Or you can pass lambda expressions to methods as arguments
		System.out.println(calculate(2, 3, (x, y) -> x + y));
	}
	
	public static double calculate(int a, int b, MathOperation operation) {
		return operation.operate(a, b);
	}

}

@FunctionalInterface
interface MathOperation {
	 double operate(int x, int y);
}

// Lambda expression itself represents an instance of the appropriate 
// functional interface. Lambda can be assigned to a reference of the
// appropriate interface or passed as an argument to a method that 
// accepts such interface.