package mathWrappers;
import java.lang.Math;

public class Sin extends MonoFunction {

	public Sin(MathObject arg) {
		super(arg);
	}
	
	public Sin(double arg) {
		super(arg);
	}

	@Override
	public double evaluate(EvalVar[] vals) {
		return Math.sin(arg.evaluate(vals));
	}

	@Override
	public MathObject derivative(String varName) {
		// TODO Auto-generated method stub
		return new Mult(arg.derivative(varName), new Cos(arg));
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "sin(" + arg + ")";
	}

}
