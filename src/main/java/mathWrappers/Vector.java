package mathWrappers;
//import mathWrappers.MathObject;

public class Vector {

	MathObject component[];
	
	public Vector(MathObject[] vComponent) {
		component = new MathObject[vComponent.length];
		for (int i = 0; i < vComponent.length; i++) {
			component[i] = vComponent[i];
		}
	}
	
	public MathObject getField(int index) {
		for (int i = 0; i < component.length; i++) {
			if (i == index) {
				return component[i];
			}
		}
		
		throw new RuntimeException("Component could not be found\n");
	}
	
	public double[] evaluate(EvalVar vals[]) {
		double evalComponent[] = new double[component.length];
		for (int i = 0; i < component.length; i++)
			evalComponent[i] = component[i].evaluate(vals);
		
		return evalComponent;
	}
	
	public int getSize() {
		return component.length;
	}
	
	public String toString() {
		if (component.length == 0) {
			return "";
		}
		if (component.length == 1) {
			return "<" + component[0] + ">";
		}
		String output = "<" + component[0] ;
		for (int i = 1; i < component.length; i++) {
			output += ", " + component[i];
		}
		return output + ">";
	}
}
