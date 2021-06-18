package mathWrappers;

public class Constant extends MathObject{

	private double value;
	public Constant(double val) {
		value = val;
	}
	@Override
	// returns the same value regardless of value passed in
	public double evaluate(EvalVar vals[]) {
		return value;
	}
	
	public MathObject derivative(String varName) {
		return new Constant(0);
	}
	
	public String toString() {
		if (value == Math.E) {
			return "e";
		}
		
		if (value == Math.PI) {
			return "PI";
		}
		
		return "" + value;
	}
}
