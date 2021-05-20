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
		
		
		MathObject nano = new Power(10, 9);
		EvalVar dumb[] = {new EvalVar("x", 0)};
		double nanoConstant = nano.evaluate(dumb);
		
		long startTime = System.nanoTime();

		
		
		
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		for (double i = initialZ; i <= endZ; i += zIncrement) {
			XYSeries series = new XYSeries("Z = " + i);
			
			for (Point point : getZTrace(i)) {
				series.add(point.getX(), point.getY());
			}
			
			dataset.addSeries(series);
		}
		
		
		long endTime = System.nanoTime();
		System.out.println("Grid Time: " + ((endTime - startTime) / nanoConstant) + " seconds");
		
		
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
		MathObject nano = new Power(10, 9);
		EvalVar dumb[] = {new EvalVar("x", 0)};
		double nanoConstant = nano.evaluate(dumb);
		
		long startTime = System.nanoTime();
		
		
		
		
		XYSeriesCollection dataset = new XYSeriesCollection();

		HashMap<Double, Point> points = getInitialPoints();
		for (Entry<Double, Point> entry: points.entrySet()) {
			dataset.addSeries(getTraceFast(entry));
		}
		
		
		
		
		long endTime = System.nanoTime();
		System.out.println("Gradient Time: " + ((endTime - startTime) / nanoConstant) + " seconds");
		return dataset;
	}
	

	private XYSeries getTraceFast(Entry<Double, Point> start) {
		XYSeries series = new XYSeries("Z = " + start.getKey());
		
		HashSet<Point> set = new HashSet<Point>();
		set.add(start.getValue());
		
		Variable vars[] = {new Variable("x"), new Variable("y")};
		
		Vector gradient = mathFunction.gradient(vars);
		MathObject perpComps[] = new MathObject[gradient.getSize()];
		perpComps[0] = new Mult(-1, gradient.getField(1));
		perpComps[1] = gradient.getField(0);
		Vector perpGrad1 = new Vector(perpComps);
		
		perpComps[0] = gradient.getField(1);
		perpComps[1] = new Mult(-1, gradient.getField(0));
		Vector perpGrad2 = new Vector(perpComps);
		

		int loopCount = 0;
		Point lastPoint1 = start.getValue();
		Point lastPoint2 = start.getValue();
		
		boolean exit = false;
		boolean stop1 = false;
		boolean stop2 = false;
		do {
			
			if (!stop1) {
				Point currPoint1 = gradStep(perpGrad1, lastPoint1);
				double newX = currPoint1.getX();
				double newY = currPoint1.getY();
				
				if (withinAcc(start.getKey(), currPoint1, .02)) {
					Point setPoint = new Point(round(newX, 3), round(newY, 3));
					if(set.add(setPoint) && checkBounds(setPoint)) {
						series.add(newX, newY);
						lastPoint1 = currPoint1;
					} else {
						stop1 = true;
					}
				} else {
					lastPoint1 = resetPoint(start.getKey(), gradient, currPoint1);
					series.add(lastPoint1.getX(), lastPoint1.getY());
				}
			}
			
			if (!stop2) {
				Point currPoint2 = gradStep(perpGrad2, lastPoint2);
				double newX = currPoint2.getX();
				double newY = currPoint2.getY();
				if (withinAcc(start.getKey(), currPoint2, .02)) {
					Point setPoint = new Point(round(newX, 3), round(newY, 3));
					if(set.add(setPoint) && checkBounds(setPoint)) {
						series.add(newX, newY);
						lastPoint2 = currPoint2;
					} else {
						stop2 = true;
					}
				} else {
					lastPoint2 = resetPoint(start.getKey(), gradient, currPoint2);
					series.add(lastPoint2.getX(), lastPoint2.getY());
				}				
			}
			
			exit = stop1 && stop2;
			
			if (loopCount == 100000) {
				exit = true;
				System.out.println("LOOP EXCEEDED LIMIT");
			}
			
			loopCount += 1;
		} while(!exit);
		
		return series;
	}
	
	private Point gradStep(Vector gradient, Point point) {
		
		EvalVar vals[] = {new EvalVar("x", point.getX()), new EvalVar("y", point.getY())};
		double delta[] = gradient.evaluate(vals);
		double mag = Math.sqrt(Math.pow(delta[0], 2) + Math.pow(delta[1], 2));
		double newX = round(point.getX() + .01 * (delta[0] / mag), 6);
		double newY = round(point.getY() + .01 * (delta[1] / mag), 6);
		
		return new Point(newX, newY);
	}
	
	private boolean checkBounds(Point p) {
		double x = p.getX();
		double y = p.getY();
		if (x < maxX && x > minX && y < maxY && y > minY)
			return true;
		return false;
	}
	
	
	//TODO: Allow multiple sampling locations for each z trace
	private HashMap<Double, Point> getInitialPoints() {
		HashMap<Double, Point> points = new HashMap<Double, Point>();
		
		int numPoints = (int)((endZ-initialZ + 1)*(1/zIncrement));
		double[] zVals = new double[numPoints];
		for (int i = 0; i < numPoints; i++) {
			zVals[i] = initialZ + i*zIncrement;
		}

		
		double yLoc = (maxY + minY) / 2;		
		EvalVar[] eval = {new EvalVar("x", minX), new EvalVar("y", yLoc)};
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
	
	private boolean withinAcc(double zGoal, Point point, double acc) {
		EvalVar eval[] = {new EvalVar("x", point.getX()), new EvalVar("y", point.getY())};
		double calcVal = mathFunction.evaluate(eval);
		
		if (Math.abs(calcVal - zGoal) / zGoal < acc) {
			return true;
		}
		return false;
	}
	
	private Point resetPoint(double zGoal, Vector gradient, Point oPoint) {

		EvalVar eval[] = {new EvalVar("x", oPoint.getX()), new EvalVar("y", oPoint.getY())};
		double calcVal = mathFunction.evaluate(eval);
		if (calcVal > zGoal) {
			// Step opposite in direction of gradient until near zGoal again
				MathObject[] antiFields = new MathObject[gradient.getSize()];
				for (int i = 0; i < gradient.getSize(); i++) {
					antiFields[i] = new Mult(-1, gradient.getField(i));
				}
				// antiGrad is the vector pointing opposite to the gradient
				Vector antiGrad = new Vector(antiFields);
				Point newP = new Point(oPoint.getX(), oPoint.getY());
				while (!withinAcc(zGoal, newP, .01)) {
					EvalVar vals[] = {new EvalVar("x", newP.getX()), new EvalVar("y", newP.getY())};
					double delta[] = antiGrad.evaluate(vals);
					double mag = Math.sqrt(Math.pow(delta[0], 2) + Math.pow(delta[1], 2));
					double newX = round(newP.getX() + .01 * (delta[0] / mag), 6);
					double newY = round(newP.getY() + .01 * (delta[1] / mag), 6);
					
					newP = new Point(newX, newY);
				}
				return newP;
				
		} else {
			// Step in direction of gradient until near zGoal again
			Point newP = new Point(oPoint.getX(), oPoint.getY());
			while (!withinAcc(zGoal, newP, .005)) {
				EvalVar vals[] = {new EvalVar("x", newP.getX()), new EvalVar("y", newP.getY())};
				double delta[] = gradient.evaluate(vals);
				double mag = Math.sqrt(Math.pow(delta[0], 2) + Math.pow(delta[1], 2));
				double newX = round(newP.getX() + .01 * (delta[0] / mag), 6);
				double newY = round(newP.getY() + .01 * (delta[1] / mag), 6);
				
				newP = new Point(newX, newY);
			}
			return newP;
			
		}
		
	}
}
