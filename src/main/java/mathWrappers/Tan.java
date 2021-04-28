package mathWrappers;
import java.lang.Math;

public class Tan extends MonoFunction {
	
	public Tan(MathObject arg) {
		super(arg);
	}
	
	public Tan(double arg) {
		super(arg);
	}

	@Override
	public double evaluate(EvalVar[] vals) {
		return Math.tan(arg.evaluate(vals));
	}

	@Override
	public MathObject derivative(String varName) {
		MathObject denom = new Power(new Cos(arg), 2);
		return new Div(arg.derivative(varName), denom);
	}

	@Override
	public String toString() {
		return "tan(" + arg + ")";
	}

}
