package traceGrapher;
import javax.swing.WindowConstants;
import java.util.Scanner;
import mathWrappers.*;

public class ZTraceRunner {
	
	public static void main(String[] args) {
		
		/*
		// User input on domain/range and increments
		Scanner input = new Scanner(System.in);
		System.out.println("Enter the initial Z value: ");
		double initialZ = input.nextDouble();
		
		System.out.println("Enter the end Z value: ");
		double endZ = input.nextDouble();
		
		System.out.println("Enter the z increment: ");
		double zIncrement = input.nextDouble();
		
		System.out.println("Enter the min X value: ");
		double minX = input.nextDouble();
		
		System.out.println("Enter the max X value: ");
		double maxX = input.nextDouble();
		
		System.out.println("Enter the min Y value: ");
		double minY = input.nextDouble();
		
		System.out.println("Enter the max Y value: ");
		double maxY = input.nextDouble();
		
		System.out.println("Enter the increment for X and Y ");
		double xyIncrement = input.nextDouble();
		
		input.close();
		
		int count = 0;
		for (double i = initialZ; i <= endZ; i += zIncrement) {
			count += 1;
		}
		
		int count2 = (int)((endZ-initialZ + 1)*(1/zIncrement));
		System.out.println(count +" "+ count2);
		
		*/
		
		double initialZ = 1;
		
		double endZ = 5;
		
		double zIncrement = 1;
		
		double minX = -10;
		
		double maxX = 10;
		
		double minY = -10;
		
		double maxY = 10;
		
		double xyIncrement = .005;

		Variable x = new Variable("x");
		Variable y = new Variable("y");
		MathObject sq = new Power(x, 2);
		MathObject next = new Mult(sq, new Cos(y));
		MathObject exp = (new Power(y));
		MathObject complexFunction = new Add(exp, next);
		
		MathObject simpleFunction = new Add(new Mult(.25, sq), new Mult(new Power(y, 2), .1));
		
		PointAggregator simplePoints = new PointAggregator(simpleFunction, initialZ, endZ, zIncrement, minX, maxX, minY, maxY, xyIncrement);
		CoordGraph graph = new CoordGraph(simplePoints.getZTracesFast(), simpleFunction.toString());
		
		graph.setSize(800, 800);


		graph.setLocationRelativeTo(null);
	    graph.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    graph.setVisible(true);
	    graph.setTitle("Gradient Graph");
	    
	    
		PointAggregator simplePoints2 = new PointAggregator(simpleFunction, initialZ, endZ, zIncrement, minX, maxX, minY, maxY, xyIncrement);
		CoordGraph graph2 = new CoordGraph(simplePoints2.getAllZTraces(), simpleFunction.toString());
		
		graph2.setSize(800, 800);


		graph2.setLocationRelativeTo(null);
	    graph2.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    graph2.setVisible(true);
	    graph2.setTitle("Grid Graph");
		
	
	}

}
