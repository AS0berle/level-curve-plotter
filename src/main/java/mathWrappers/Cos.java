package mathWrappers;
import java.lang.Math;

public class Cos extends MonoFunction{

	public Cos(MathObject arg) {
		super(arg);
	}
	
	public Cos(double arg) {
		super(arg);
	}

	@Override
	public double evaluate(EvalVar[] vals) {
		return Math.cos(arg.evaluate(vals));
	}

	@Override
	public MathObject derivative(String varName) {
		MathObject sin = new Mult (-1, new Sin(arg));
		return new Mult(arg.derivative(varName), sin);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "cos(" + arg + ")";
	}

}
