package mathWrappers;
public class MathTester {
	
	public static void main(String args[]) {
		
		Variable x = new Variable("x");
		Variable y = new Variable("y");
		MathObject sq = new Power(x, 2);
		MathObject next = new Mult(x, y);
		MathObject function = new Sub(sq, next);
		
		MathObject cu = new Power(new Mult(x, y), new Sin(x));
		MathObject pl = new Add(cu, 10);
		
		System.out.println(cu);
		System.out.println(cu.derivative("x"));
		MathObject gunction = new Div(function, pl);

        MathParser p = new MathParser();
        MathObject parsed = p.parse(cu.toString());
        System.out.println(parsed);
		/*
		Variable vars[] = {x, y};
		Vector grad = function.gradient(vars);
		
		EvalVar val1[] = {new EvalVar("x", 3), new EvalVar("y", 2)};
		EvalVar val2[] = {new EvalVar("x", 4), new EvalVar("y", 1)};
		EvalVar val3[] = {new EvalVar("x", 1), new EvalVar("y", 0)};
		
		double[] conGrad1 = grad.evaluate(val1);
		double[] conGrad2 = grad.evaluate(val2);
		double[] conGrad3 = grad.evaluate(val3);
		*/


		/*
		System.out.println("f(x,y)=" + function);
		System.out.println("gradient: " + grad);
		System.out.println("Gradient at (3,2): <" + conGrad1[0] + ", " + conGrad1[1] + ">");
		System.out.println("Gradient at (4,1): <" + conGrad2[0] + ", " + conGrad2[1] + ">");
		System.out.println("Gradient at (1,0): <" + conGrad3[0] + ", " + conGrad3[1] + ">");
		
		System.out.println("g(x,y)=" + gunction);
		System.out.println("gradient" + gunction.gradient(vars));
		*/
	}

}
