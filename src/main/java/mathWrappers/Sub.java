package mathWrappers;

public class Sub extends BiFunction{
	
	public Sub(MathObject arg1, MathObject arg2) {
		super(arg1, arg2);
	}
	
	public Sub(double arg1, double arg2) {
		super(new Constant(arg1), new Constant(arg2));
	}
	
	public Sub(double arg1, MathObject arg2) {
		super(new Constant(arg1), arg2);
	}
	
	public Sub(MathObject arg1, double arg2) {
		super(arg1, new Constant(arg2));
	}

	public double evaluate(EvalVar vals[]) {
		return arg1.evaluate(vals) - arg2.evaluate(vals);
	}
	
	public MathObject derivative(String derVar) {
		return new Sub(arg1.derivative(derVar), arg2.derivative(derVar));
	}
	
	public String toString() {
		return "(" + arg1 + ")-(" + arg2 + ")";
	}
}
