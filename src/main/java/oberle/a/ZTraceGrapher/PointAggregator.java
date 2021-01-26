package oberle.a.ZTraceGrapher;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.ArrayList;


public class PointAggregator {
	
	private BivariateFunction mathFunction;
	private double initialZ;
	private double endZ;
	private double zIncrement;
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	private double xyIncrement;
	private double xyPrecision;
	
	public PointAggregator(BivariateFunction function, double initialZ, double endZ, double zIncrement, double minX, double maxX, double minY, double maxY, double xyIncrement, double precision) {
		mathFunction = function;
		this.initialZ = initialZ;
		this.endZ = endZ;
		this.zIncrement = zIncrement;
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.xyIncrement = xyIncrement;
		xyPrecision = precision;
	}
	
	public PointAggregator(BivariateFunction function, double initialZ, double endZ, double zIncrement, double minX, double maxX, double minY, double maxY, double xyIncrement) {
		this(function, initialZ, endZ, zIncrement, minX, maxX, minY, maxY, xyIncrement, .01);
		
	}
	
	// Tests all values of X and Y in f(x,y), and returns the coordinates that match the specified Z value within range of specified precision
	private ArrayList<Point> getZTrace(double zConstant){
		ArrayList<Point> zTracePoints= new ArrayList<Point>();
		
		for(double i = minX; i < maxX; i += xyIncrement) {
			for(double j = minY; j < maxY; j += xyIncrement) {
			
				double inaccuracy = Math.abs(zConstant - mathFunction.value(i, j));
				if(inaccuracy < xyPrecision) {
					// Rounds off decimals to a few digits
					zTracePoints.add(new Point((double)((int)(i*100000))/100000, (double)((int)(j*100000))/100000));
				}
				
				
			}
		}
		
		
		return zTracePoints;
	}
	
	// Calls getZTrace (above) on all Z values in the specified range, stores the data in a data set for the graph to use
	public XYSeriesCollection getAllZTraces() {
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		for (double i = initialZ; i <= endZ; i += zIncrement) {
			XYSeries series = new XYSeries("Z = " + i);
			
			for (Point point : getZTrace(i)) {
				series.add(point.getX(), point.getY());
			}
			
			dataset.addSeries(series);
		}
		
		return dataset;
	}

}
