package mathWrappers;

public class Variable extends MathObject{
	
	//Used for partial derivations in the future
	private String varName;
	
	// Variables are essentially placeholders that act as a base case
	public Variable(String name) {
		varName = name;
	}
	
	public double evaluate(EvalVar vals[]) {
		for(int i = 0; i < vals.length; i++) {
			if (vals[i].getName() == varName) {
				return vals[i].getVal();
			}
		}
		throw new RuntimeException("Variable could not be found\n");
	}
	
	public MathObject derivative(String varName) {
		if (this.varName == varName) {
			return new Constant(1);
		} else {
			return new Constant(0);
		}
	}
	
	public String toString() {
		
		return varName;
	}

}
