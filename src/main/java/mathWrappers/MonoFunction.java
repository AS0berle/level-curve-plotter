package mathWrappers;

public abstract class MonoFunction extends MathObject {
	
	protected MathObject arg;
	
	public MonoFunction(MathObject arg) {
		this.arg = arg;
	}
	
	public MonoFunction(double arg) {
		this(new Constant(arg));
	}

}
