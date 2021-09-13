package traceGrapher;
import javax.swing.WindowConstants;
//import java.util.Scanner;
import mathWrappers.*;

public class ZTraceRunner {
	
	public static void main(String[] args) {
		
		double initialZ = -5;		
		double endZ = 5;
		double zIncrement = 1;
		double minX = -4;
		double maxX = 4;
		double minY = -7;
		double maxY = 7;
		

		Variable x = new Variable("x");
		Variable y = new Variable("y");
		MathObject sq = new Power(x, 2);
		MathObject sqY = new Power(y, 2);
		MathObject next = new Mult(y, new Cos(x));
		MathObject exp = new Power(y);
		MathObject complex = new Add(exp, next);
		
		MathObject simple = new Add(sq, new Power(y, 2));
		
		MathObject tester = new Mult(y, new Log(x));

		MathObject b1 = new Mult (4, new Mult(new Cos(x), new Cos(y)));
	

		PointAggregator simplePoints = new PointAggregator(tester, initialZ, endZ, zIncrement, minX, maxX, minY, maxY);
		CoordGraph graph = new CoordGraph(simplePoints.getZTracesFast(), tester.toString());
		graph.setSize(800, 800);
		graph.setLocationRelativeTo(null);
	    graph.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    graph.setVisible(true);
	    graph.setTitle("Gradient Graph");
	    
		
	
	}

}
