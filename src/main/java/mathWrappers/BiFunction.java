package mathWrappers;

public abstract class BiFunction extends MathObject{
	
	protected MathObject arg1;
	protected MathObject arg2;
	
	public BiFunction(MathObject arg1, MathObject arg2) {
		this.arg1 = arg1;
		this.arg2 = arg2;
	}
	
	public BiFunction(double arg1, double arg2) {
		this(new Constant(arg1), new Constant(arg2));
	}
	
	public BiFunction(double arg1, MathObject arg2) {
		this(new Constant(arg1), arg2);
	}
	
	public BiFunction(MathObject arg1, double arg2) {
		this(arg1, new Constant(arg2));
	}
	
	public abstract double evaluate(EvalVar vals[]);
	public abstract MathObject derivative(String varName);

}
