package mathWrappers;

public class Add extends BiFunction{
	
	public Add(MathObject arg1, MathObject arg2) {
		super(arg1, arg2);
	}
	
	public Add(double arg1, double arg2) {
		super(new Constant(arg1), new Constant(arg2));
	}
	
	public Add(double arg1, MathObject arg2) {
		super(new Constant(arg1), arg2);
	}
	
	public Add(MathObject arg1, double arg2) {
		super(arg1, new Constant(arg2));
	}
	
	@Override
	public double evaluate(EvalVar vals[]) {
		return arg1.evaluate(vals) + arg2.evaluate(vals);
	}
	
	public MathObject derivative(String derVar) {
		return new Add(arg1.derivative(derVar), arg2.derivative(derVar));
	}

	@Override
	public String toString() {
		return "(" + arg1 + "+" + arg2 + ")";
	}

}
