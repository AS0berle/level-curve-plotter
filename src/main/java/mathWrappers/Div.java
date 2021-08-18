package mathWrappers;

public class Div extends BiFunction{
	
	public Div(MathObject arg1, MathObject arg2) {
		super(arg1, arg2);
	}
	
	public Div(double arg1, double arg2) {
		super(new Constant(arg1), new Constant(arg2));
	}
	
	public Div(double arg1, MathObject arg2) {
		super(new Constant(arg1), arg2);
	}
	
	public Div(MathObject arg1, double arg2) {
		super(arg1, new Constant(arg2));
	}
	
	@Override
	public double evaluate(EvalVar vals[]) {
		return arg1.evaluate(vals) / arg2.evaluate(vals);
	}
	
	
	public MathObject derivative(String varName) {
		MathObject numTerm1 = new Mult(arg1.derivative(varName), arg2);
		MathObject numTerm2 = new Mult(arg1, arg2.derivative(varName));
		MathObject numerator = new Sub(numTerm1, numTerm2);
		MathObject denominator = new Power(arg2, 2);
		return new Div(numerator, denominator);
	}
	
	public String toString() {
		return "(" + arg1 + "/" + arg2 + ")";
	}
}
