package mathWrappers;
import java.lang.Math;

public class Power extends BiFunction{

	public Power(MathObject arg1, MathObject arg2) {
		super(arg1, arg2);
	}
	
	public Power(double arg1, double arg2) {
		super(arg1, arg2);
	}
	
	public Power(MathObject arg1, double arg2) {
		super(arg1, arg2);
	}
	
	public Power(MathObject arg1) {
		super(Math.E, arg1);
	}
	
	public Power(double arg1) {
		super(Math.E, arg1);
	}
	
	@Override
	public double evaluate(EvalVar vals[]) {
		return Math.pow(arg1.evaluate(vals), arg2.evaluate(vals));
	}
	
	public MathObject derivative(String varName) {
		// This implementation of derivative assumes that arg2 is constant. Will function incorrectly when it is not
		if (arg2 instanceof Constant) {
			MathObject expo = new Sub(arg2, 1);
			MathObject newTerm = new Power(arg1, expo);
			return new Mult(arg1.derivative(varName), new Mult(arg2, newTerm));
		} else {
			
			MathObject self = this;
			MathObject log = new Log(arg1);
			MathObject expo = arg2;
			MathObject otherTerm = new Mult(log, expo);
			otherTerm = otherTerm.derivative(varName);
			
			return new Mult(self, otherTerm);
		}
		 
	}
	
	public String toString() {
		return "(" + arg1 + "^" + arg2 + ")";
	}
}
