package traceGrapher;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import mathWrappers.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;


public class PointAggregator {
	
	private MathObject mathFunction;
	private double initialZ;
	private double endZ;
	private double zIncrement;
	private double minX;
	private double maxX;
	private double minY;
	private double maxY;
	private double xyIncrement;
	private double xyPrecision;
	
	public PointAggregator(MathObject function, double initialZ, double endZ, double zIncrement, double minX, double maxX, double minY, double maxY, double xyIncrement, double precision) {
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
	
	public PointAggregator(MathObject function, double initialZ, double endZ, double zIncrement, double minX, double maxX, double minY, double maxY, double xyIncrement) {
		this(function, initialZ, endZ, zIncrement, minX, maxX, minY, maxY, xyIncrement, .01);
		
	}
	
	// Tests all values of X and Y in f(x,y), and returns the coordinates that match the specified Z value within range of specified precision
	private ArrayList<Point> getZTrace(double zConstant){
		ArrayList<Point> zTracePoints= new ArrayList<Point>();
		
		for(double i = minX; i < maxX; i += xyIncrement) {
			for(double j = minY; j < maxY; j += xyIncrement) {
				
				EvalVar vals[] = {new EvalVar("x", i), new EvalVar("y", j)};
				double inaccuracy = Math.abs(zConstant - mathFunction.evaluate(vals));
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
		getZTracesFast();
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
	
	// Convert to using BigDecimals as a standard instead of regular decimals to replace this
	private static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(Double.toString(value));
	    bd = bd.setScale(places, RoundingMode.HALF_DOWN);
	    return bd.doubleValue();
	}

	
	public XYSeriesCollection getZTracesFast() {
		XYSeriesCollection dataset = new XYSeriesCollection();

		HashMap<Double, Point> points = getInitialPoints();
		for (Entry<Double, Point> entry: points.entrySet()) {
			dataset.addSeries(getTraceFast(entry));
		}
		
		
		return dataset;
	}
	
	public XYSeries getTraceFast(Entry<Double, Point> start) {
		Variable vars[] = {new Variable("x"), new Variable("y")};
		
		Vector gradient = mathFunction.gradient(vars);
		MathObject perpComps[] = new MathObject[gradient.getSize()];
		perpComps[0] = new Mult(-1, gradient.getField(1));
		perpComps[1] = gradient.getField(0);
		Vector perpGrad = new Vector(perpComps);
		
		HashSet<Point> set = new HashSet<Point>();
		set.add(start.getValue());
		boolean addLast = true;
		boolean exit = false;
		int loopCount = 0;
		Point lastPoint = start.getValue();
		XYSeries series = new XYSeries("Z = " + start.getKey());
		do {
			EvalVar vals[] = {new EvalVar("x", lastPoint.getX()), new EvalVar("y", lastPoint.getY())};
			double delta[] = perpGrad.evaluate(vals);
			double mag = Math.sqrt(Math.pow(delta[0], 2) + Math.pow(delta[1], 2));
			double newX = round(lastPoint.getX() + .01 * (delta[0] / mag), 4);
			double newY = round(lastPoint.getY() + .01 * (delta[1] / mag), 4);
			
			Point newPoint = new Point(newX, newY);
			Point setPoint = new Point(round(newX, 2), round(newY, 2));

			
			if(set.add(setPoint)) {
				series.add(newX, newY);
				lastPoint = newPoint;
			} else {
				exit = true;
				System.out.println("DUPLICATE FOUND");
			}
			
			
			if (loopCount == 1500) {
				exit = true;
				System.out.println("LOOP EXCEEDED LIMIT");
			}
			
			loopCount += 1;
		} while(!exit);
		
		
		/*
		for (Iterator<Point> it = set.iterator(); it.hasNext();) {
			Point p = it.next();
			System.out.println(p.getX() + " " + p.getY());
		}
		*/
		return series;
	}
	
	
	public HashMap<Double, Point> getInitialPoints() {
		HashMap<Double, Point> points = new HashMap<Double, Point>();
		
		int numPoints = (int)((endZ-initialZ + 1)*(1/zIncrement));
		double zVals[] = new double[numPoints];
		for (int i = 0; i < numPoints; i++) {
			zVals[i] = initialZ + i*zIncrement;
		}

		
		double yLoc = (maxY + minY) / 2;		
		EvalVar eval[] = {new EvalVar("x", minX), new EvalVar("y", yLoc)};
		for (double x = minX; x < maxX; x += xyIncrement) {
			eval[0] = new EvalVar("x", x);
			double zVal = mathFunction.evaluate(eval);
			for (int i = 0; i < zVals.length; i++) {
				double inaccuracy = Math.abs(zVals[i] - zVal);
				if(inaccuracy < xyPrecision) {
					points.put(round(zVal, 1), new Point(x, yLoc));		
				}
			}
			
			
		}
		
		return points;
	}
}
