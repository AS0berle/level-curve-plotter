package oberle.a.ZTraceGrapher;
import javax.swing.WindowConstants;
import java.util.Scanner;

public class ZTraceRunner {
	
	public static void main(String[] args) {
		
		
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
		BivariateFunction simpleFunction = new BivariateFunction();
		PointAggregator simplePoints = new PointAggregator(simpleFunction, initialZ, endZ, zIncrement, minX, maxX, minY, maxY, xyIncrement);
		CoordGraph graph = new CoordGraph(simplePoints.getAllZTraces(), simpleFunction.toString());
		
		graph.setSize(800,800);
		graph.setLocationRelativeTo(null);
	    graph.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    graph.setVisible(true);
		
	}

}
