package mathWrappers;

public class EvalVar {

	private String varName;
	private double varEvaluation;
	
	public EvalVar(String name, double val) {
		varName = name;
		varEvaluation = val;
	}
	
	public String getName() {
		return varName;
	}
	
	public double getVal() {
		return varEvaluation;
	}
}
