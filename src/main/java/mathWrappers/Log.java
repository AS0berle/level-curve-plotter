package mathWrappers;
import java.lang.Math;

public class Log extends BiFunction{
	
	// arg1 = base, arg2 = argument
	// arg1 is always a double or a constant
	// In the case of 1 argument, the base is assumed to be e (2.7...)
	public Log(double arg1, double arg2) {
		super(arg1, arg2);
		
	}
	
	public Log(double arg1, MathObject arg2) {
		super(arg1, arg2);
	}
	
	// Default of base e (natural log)
	public Log(MathObject arg2) {
		super(Math.E, arg2);
	}
	
	// Same as above
	public Log(double arg2) {
		super(Math.E, arg2);
	}
	
	@Override
	public double evaluate(EvalVar[] vals) {
		return Math.log(arg2.evaluate(vals)) / Math.log(arg1.evaluate(vals));
	}

	@Override
	public MathObject derivative(String varName) {
		MathObject oneOver = new Mult(new Power(arg2, -1), new Log(arg1));
		return new Mult(oneOver, arg2.derivative(varName));
	}
	
	public String toString() {
		return "log[" + arg1 + "](" + arg2 + ")";
	}

}
