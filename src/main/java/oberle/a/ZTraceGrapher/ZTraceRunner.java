package oberle.a.ZTraceGrapher;
import javax.swing.WindowConstants;

public class ZTraceRunner {
	
	public static void main(String[] args) {
		
		CheapBivariateFunction simpleFunction = new CheapBivariateFunction();
		PointAggregator simplePoints = new PointAggregator(simpleFunction, 1, 4, 1, -5, 5, -5, 5, .001, .01);
		CoordGraph graph = new CoordGraph(simplePoints.getAllZTraces(), simpleFunction.toString());
		
		graph.setSize(800,800);
		graph.setLocationRelativeTo(null);
	    graph.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    graph.setVisible(true);
		
	}

}
