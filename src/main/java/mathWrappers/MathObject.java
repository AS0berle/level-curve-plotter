package mathWrappers;

public abstract class MathObject {
	
	public abstract double evaluate(EvalVar vals[]);
 	public abstract MathObject derivative(String varName);
 	public abstract String toString();
 	
 	public Vector gradient(Variable[] vars) {
 		MathObject components[] = new MathObject[vars.length];
 		for (int i = 0; i < components.length; i++) {
 			components[i] = this.derivative(vars[i].toString());
 		}
 		
 		return new Vector(components);
 	}

}
