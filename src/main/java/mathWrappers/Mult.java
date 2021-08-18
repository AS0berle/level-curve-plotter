package mathWrappers;

public class Mult extends BiFunction{
	
	public Mult(MathObject arg1, MathObject arg2) {
		super(arg1, arg2);
	}
	
	public Mult(double arg1, double arg2) {
		super(new Constant(arg1), new Constant(arg2));
	}
	
	public Mult(double arg1, MathObject arg2) {
		super(new Constant(arg1), arg2);
	}
	
	public Mult(MathObject arg1, double arg2) {
		super(arg1, new Constant(arg2));
	}
	
	@Override
	public double evaluate(EvalVar vals[]) {
		return arg1.evaluate(vals) * arg2.evaluate(vals);
	}
	
	public MathObject derivative(String varName) {
		MathObject term1 = new Mult(arg1.derivative(varName), arg2);
		MathObject term2 = new Mult(arg1, arg2.derivative(varName));

		return new Add(term1, term2);		
	}
	
	public String toString() {
		return "(" + arg1 + "*" + arg2 + ")";
	}
}
