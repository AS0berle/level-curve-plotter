package traceGrapher;
import javax.swing.WindowConstants;
import java.util.Scanner;
import mathWrappers.*;

public class ZTraceRunner {
	
	public static void main(String[] args) {
				
		double initialZ = 0;		
		double endZ = 4;
		double zIncrement = 1;
		double minX = -3;
		double maxX = 3;
		double minY = -3;
		double maxY = 3;
		double xyIncrement = .0001;
		
		Variable x = new Variable("x");
		Variable y = new Variable("y");
		MathObject sq = new Power(x, 2);
		MathObject next = new Mult(y, new Cos(x));
		MathObject exp = (new Power(y));
		MathObject complex = new Add(exp, next);
		
		MathObject simple = new Add(sq, new Power(y, 2));
		
		MathObject tester = new Mult(y, new Log(x));
		
		PointAggregator simplePoints = new PointAggregator(simple, initialZ, endZ, zIncrement, minX, maxX, minY, maxY, xyIncrement);
		CoordGraph graph = new CoordGraph(simplePoints.getZTracesFast(), simple.toString());
		
		graph.setSize(800, 800);
		graph.setLocationRelativeTo(null);
	    graph.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    graph.setVisible(true);
	    graph.setTitle("Gradient Graph");
	    
		
	
	}

}
