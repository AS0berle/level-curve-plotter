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
		
		double endZ = 4;
		
		double zIncrement = 1;
		
		double minX = -2;
		
		double maxX = 2;
		
		double minY = -2;
		
		double maxY = 2;
		
		double xyIncrement = .005;

		Variable x = new Variable("x");
		Variable y = new Variable("y");
		MathObject sq = new Power(x, 2);
		MathObject next = new Mult(sq, new Cos(y));
		MathObject exp = new Add(next, new Power(y));
		MathObject complexFunction = new Add(exp, next);
		
		MathObject simpleFunction = new Add(sq, new Power(y, 2));
		
		MathObject nano = new Power(10, 9);
		EvalVar dumb[] = {new EvalVar("x", 0)};
		double nanoConstant = nano.evaluate(dumb);
		
		
		long startTime = System.nanoTime();
		PointAggregator simplePoints = new PointAggregator(simpleFunction, initialZ, endZ, zIncrement, minX, maxX, minY, maxY, xyIncrement);
		CoordGraph graph = new CoordGraph(simplePoints.getZTracesFast(), simpleFunction.toString());
		
		graph.setSize(800,800);
		long endTime = System.nanoTime();

		System.out.println("Time: " + ((endTime - startTime) / nanoConstant) + " seconds");
		graph.setLocationRelativeTo(null);
	    graph.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    graph.setVisible(true);
		
	
	}

}
