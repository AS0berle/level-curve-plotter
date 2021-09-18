package traceGrapher;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import mathWrappers.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HashSet;
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
	private double stepSize;
	private int roundPrecision;
	
	/**
	 * Automatically calculates stepsize based on the given domain and range
	 * 
	 * @param function The function f(x,y) to graph the z traces of
	 * @param initialZ Lowest Z value of a z trace
	 * @param endZ Highest Z value of a z trace
	 * @param zIncrement How much to step between initialZ and endZ
	 * @param minX Minimum X value to be graphed
	 * @param maxX Maximum X value to be graphed
	 * @param minY Minimum Y value to be graphed
	 * @param maxY Maximum Y value to be graphed
	 */
	public PointAggregator(MathObject function, double initialZ, double endZ, double zIncrement, double minX, double maxX, double minY, double maxY) {
		mathFunction = function;
		this.initialZ = initialZ;
		this.endZ = endZ;
		this.zIncrement = zIncrement;
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		stepSize = Math.sqrt(Math.pow((maxX - minX),2) + Math.pow((maxY-minY),2)) * .001;
		roundPrecision = (int)(-1 * Math.floor(Math.log10(stepSize / 10)));

	}

	/**
	 * Allows user to specify a step size
	 * 
	 * @param function The function f(x,y) to graph the z traces of
	 * @param initialZ Lowest Z value of a z trace
	 * @param endZ Highest Z value of a z trace
	 * @param zIncrement How much to step between initialZ and endZ
	 * @param minX Minimum X value to be graphed
	 * @param maxX Maximum X value to be graphed
	 * @param minY Minimum Y value to be graphed
	 * @param maxY Maximum Y value to be graphed
	 * @param stepSize The size of the step to be used in internal calculations
	 */	
	public PointAggregator(MathObject function, double initialZ, double endZ, double zIncrement, double minX, double maxX, double minY, double maxY, double stepSize) {
		mathFunction = function;
		this.initialZ = initialZ;
		this.endZ = endZ;
		this.zIncrement = zIncrement;
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		this.stepSize = stepSize;
	}
	
	/**
	 * Rounds a double to a given number of decimal places
	 * 
	 * @param value The number to be rounded
	 * @param places How many decimal places to round to
	 * @return
	 */
	private static double round(double value, int places) {
		if (value != value) {
			return value;
		}
		if (value == 0) {
			return 0.0;
		}
	    if (places < 0) {
			places *= -1;
			return Math.pow(10, places) * round(value / Math.pow(10, places), 0);
		}

	    BigDecimal bd = new BigDecimal(Double.toString(value));
	    bd = bd.setScale(places, RoundingMode.HALF_DOWN);
	    return bd.doubleValue();
	}

	/**
	 * Calculates sufficient points on level curves to graph them on an XYChart
	 * 
	 * @return An XYSeriesCollection that will can be used in an XYChart to display all level curves
	 */
	public XYSeriesCollection getZTracesFast() {

		MathObject nano = new Power(10, 9);
		EvalVar dumb[] = {new EvalVar("x", 0)};
		double nanoConstant = nano.evaluate(dumb);
		
		long startTime = System.nanoTime();
		
		
		
		
		XYSeriesCollection dataset = new XYSeriesCollection();

		HashMap<Double, HashSet<Point>> points = getInitialPoints2();
		System.out.println("Initial points found!");
		int numInitials = 0;
		for(Entry<Double, HashSet<Point>> entry: points.entrySet()) {
			numInitials+= entry.getValue().size();
		}
		System.out.println(numInitials);
		
		for (Entry<Double, HashSet<Point>> entry: points.entrySet()) {
			dataset.addSeries(getTraceFast(entry));
		}
		
				
		long endTime = System.nanoTime();
		System.out.println("Gradient Time: " + ((endTime - startTime) / nanoConstant) + " seconds");
		return dataset;
	}
	
	/**
	 * Calculates a level curve for a specified Z value given an unknown number of initial points
	 * 
	 * @param points Contains a Z value and some initial points (x1, y1) such that f(x1, y1) = a given Z value
	 * @return An XYSeries that contains a level curve f(x,y) = a given Z value
	 */
	private XYSeries getTraceFast(Entry<Double, HashSet<Point>> points) {
		XYSeries series = new XYSeries("Z = " + points.getKey());
		
		HashSet<Point> set = new HashSet<Point>();
		
		for (Point p : points.getValue()) {
			set.add(p);
		}
		

		// Calculate the gradient of f(x,y). Then, create two vectors perpendicular to the gradient
		// Which will be used to estimate f(x,y) using the initial points
		Variable vars[] = {new Variable("x"), new Variable("y")};
		Vector gradient = mathFunction.gradient(vars);
		MathObject perpComps[] = new MathObject[gradient.getSize()];
		
		perpComps[0] = new Mult(-1, gradient.getField(1));
		perpComps[1] = gradient.getField(0);
		Vector perpGrad1 = new Vector(perpComps);
		
		perpComps[0] = gradient.getField(1);
		perpComps[1] = new Mult(-1, gradient.getField(0));
		Vector perpGrad2 = new Vector(perpComps);
		
		// For each initial point, step in the direction of both vectors (found above) until all exit conditions are met
		for (Point start : points.getValue()) {
			Point lastPoint1 = start;
			Point lastPoint2 = start;
			
			boolean exit = false;
			boolean stop1 = false;
			boolean stop2 = false;
			do {
				
				if (!stop1) {
					// Step in the direction of the vector perpGrad1
					Point currPoint1 = gradStep(perpGrad1, lastPoint1);
					double newX = currPoint1.getX();
					double newY = currPoint1.getY();
					
					// If the new point is within 1% of the expected point, check if it has already been found
					if (withinAcc(points.getKey(), currPoint1, .01)) {
						
						Point setPoint = new Point(round(newX, roundPrecision), round(newY, roundPrecision));

						// If the point has not already been found, save it and get ready for the next iteration
						if(set.add(setPoint) && checkBounds(setPoint)) {
							series.add(newX, newY);
							lastPoint1 = currPoint1;
						} else {
							// If the point is already in set, it's time to stop
							stop1 = true;
						}

						// First check if the point is a real number
					} else if(currPoint1.getX() == currPoint1.getX() && currPoint1.getY() == currPoint1.getY()){
						// If the point is a real number, reset it back to the correct curve
						lastPoint1 = resetPoint(points.getKey(), gradient, currPoint1, 0);
						if (lastPoint1.getX() == lastPoint1.getX()){
							// If the reset was successful, add the new point to the set and get ready to repeat
							series.add(lastPoint1.getX(), lastPoint1.getY());
						} else { 
							// If the reset was not successful, it's time to stop
							stop1 = true;
						}
					} else {
						// If the point is not a real number, it's time to stop
						stop1 = true;
					}
				}
				
				// Exactly the same process as above, but using a different vector
				if (!stop2) {
					Point currPoint2 = gradStep(perpGrad2, lastPoint2);
					double newX = currPoint2.getX();
					double newY = currPoint2.getY();
					if (withinAcc(points.getKey(), currPoint2, .01)) {
						
						Point setPoint = new Point(round(newX, roundPrecision), round(newY, roundPrecision));
						if(set.add(setPoint) && checkBounds(setPoint)) {
							series.add(newX, newY);
							lastPoint2 = currPoint2;
						} else {
							stop2 = true;
						}
					} else if(currPoint2.getX() == currPoint2.getX() && currPoint2.getY() == currPoint2.getY()){
						lastPoint2 = resetPoint(points.getKey(), gradient, currPoint2, 0);
						if (lastPoint2.getX() == lastPoint2.getX()) {
							series.add(lastPoint2.getX(), lastPoint2.getY());
						} else { 
							stop2 = true;
						}
					} else {
						stop2 = true;
					}			
				}
				
				// Once both vectors have stopped iterating, continue on to the next initial point
				exit = stop1 && stop2;
			} while(!exit);
		}
		
		return series;
	}
	
	/**
	 * Steps in the direction of the given vector given an initial points
	 * 
	 * @param gradient A vector
	 * @param point A point
	 * @return A new point in the direction of the vector. If a local maxima or minima is reached
	 * 			return a point with values NaN
	 */
	private Point gradStep(Vector gradient, Point point) {
		
		EvalVar vals[] = {new EvalVar("x", point.getX()), new EvalVar("y", point.getY())};
		double delta[] = gradient.evaluate(vals);
		double mag = Math.sqrt(Math.pow(delta[0], 2) + Math.pow(delta[1], 2));
		
		if (mag == 0) {
			return new Point(Double.NaN, Double.NaN);
		}
		

		
		double newX = round(point.getX() + stepSize * (delta[0] / mag), roundPrecision + 1);
		double newY = round(point.getY() + stepSize * (delta[1] / mag), roundPrecision + 1);
		return new Point(newX, newY);
		
	}
	
	/**
	 * Checks if a point p is within bounds
	 * 
	 * @param p A point p
	 * @return True if p is within bounds
	 */
	private boolean checkBounds(Point p) {
		double x = p.getX();
		double y = p.getY();
		if (x < maxX && x > minX && y < maxY && y > minY)
			return true;
		return false;
	}
	
	/**
	 * Finds initial points on all level curves via gradient descent
	 * 
	 * @return A map where the key is a Z value we are finding a trace of, and the values are initial points on that trace
	 */
	private HashMap<Double, HashSet<Point>> getInitialPoints2() {
		// Initial sampling of points
		final int NUM_SECTIONS = 30;
		final double X_GRID_DIST = (Math.abs(maxX) + Math.abs(minX)) / NUM_SECTIONS;
		final double Y_GRID_DIST = (Math.abs(maxY) + Math.abs(minY)) / NUM_SECTIONS;
		
		HashMap<Double, HashSet<Point>> points = new HashMap<Double, HashSet<Point>>();
		
		
		// Calculate the gradient and anti gradient for use later
		// Gradient points in direction of maximum increase, anti gradient in direction of maximum decrease
		Variable vars[] = {new Variable("x"), new Variable("y")};
		Vector gradient = mathFunction.gradient(vars);
		
		MathObject[] antiGradTerms = new MathObject[2];
		antiGradTerms[0] = new Mult(-1, gradient.getField(0));
		antiGradTerms[1] = new Mult(-1, gradient.getField(1));
		Vector antiGrad = new Vector(antiGradTerms);
		

		// Calculates the value for all Z traces
		int numTraces = (int)((endZ-initialZ + 1)*(1/zIncrement));
		double[] zVals = new double[numTraces];
		for (int i = 0; i < numTraces; i++) {
			zVals[i] = initialZ + i*zIncrement;
			points.put(zVals[i], new HashSet<Point>());
		}
		
		// For viewing initial points
		//XYSeries initialPoints = new XYSeries("Initial Points");

		// For each initial point sampled, step in the direction of the gradient and antigradient
		// until one of the z values found above is reached
		for (double x  = minX + X_GRID_DIST; x < maxX; x += X_GRID_DIST) {
			for (double y = minY + Y_GRID_DIST; y < maxY; y+= Y_GRID_DIST) {
				
				Point point1 = new Point(x, y);
				Point point2 = new Point(x, y);
				boolean stop1 = false;
				boolean stop2 = false;
				do {
						
						if (!stop1) {
							// Find if the point is on one of the z traces
							EvalVar eval[] = {new EvalVar("x", point1.getX()), new EvalVar("y", point1.getY())};
							double zVal = mathFunction.evaluate(eval);
							for (int i = 0; i <zVals.length; i++) {
								double inaccuracy = Math.abs(zVals[i] - zVal) /(1 + Math.abs(zVals[i]));
								// If the point is very close to one of the Z values we're looking for, save it
								if (inaccuracy < .001) {
									// old 3 3 
									points.get(zVals[i]).add(new Point(round(point1.getX(), roundPrecision), round(point1.getY(), roundPrecision)));
									stop1 = true;


									// For viewing initial points found
									//initialPoints.add(round(point1.getX(), 4), round(point1.getY(), 4));
								}
							}
							
							double delta[] = gradient.evaluate(eval);
							// Make sure the gradient exists at this location
							if (!(delta[0] == 0 && delta[1] == 0) && delta[0] == delta[0] && delta[1] == delta[1]) {
								// Step in the direction of the gradient, and get ready to repeat
								point1 = gradStep(gradient, point1);
								// old 4 4
								point1.setX(round(point1.getX(), roundPrecision + 1));
								point1.setY(round(point1.getY(), roundPrecision + 1));
							} else {
								stop1 = true;
							}
							if (!checkBounds(point1))
								stop1 = true;
						
						}
						
						
						// Same process as above, but using the antigradient instead of gradient
						if (!stop2) {
							EvalVar eval2[] = {new EvalVar("x", point2.getX()), new EvalVar("y", point2.getY())};
							double zVal = mathFunction.evaluate(eval2);
							for (int i = 0; i <zVals.length; i++) {
								double inaccuracy = Math.abs(zVals[i] - zVal) /(1 + Math.abs(zVals[i]));
								if (inaccuracy < .001) {
									// old 3 3
									points.get(zVals[i]).add(new Point(round(point2.getX(), roundPrecision), round(point2.getY(), roundPrecision)));
									stop2 = true;


									// for viewing initial points
									//initialPoints.add(round(point2.getX(), 4), round(point2.getY(), 4));
								}
							}
							
							double delta2[] = antiGrad.evaluate(eval2);
							
							if (!(delta2[0] == 0 && delta2[1] == 0) && delta2[0] == delta2[0] && delta2[1] ==delta2[1]) {
								point2 = gradStep(antiGrad, point2);
								// old 4 4
								point2.setX(round(point2.getX(), roundPrecision + 1));
								point2.setY(round(point2.getY(), roundPrecision + 1));
							} else {
								stop2 = true;
							}
							if (!checkBounds(point2))
								stop2 = true;
						
						}
				
				
				} while(!stop1 && !stop2);
		
					
			
			}
		}
		/*
		// Uncomment for viewing initial sampled points
		XYSeriesCollection aaa = new XYSeriesCollection();
		aaa.addSeries(initialPoints);
		CoordGraph testGraph = new CoordGraph(aaa, "Initial Points");
		
		testGraph.setSize(800, 800);
		testGraph.setLocationRelativeTo(null);
	    testGraph.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    testGraph.setVisible(true);
	    testGraph.setTitle("Point Graph");
	    */
		return points;
	}
	
	/**
	 * Compares how close f(x,y) at a given point is to an expected value of f(x,y)
	 * 
	 * @param zGoal A value of f(x,y)
	 * @param point A point (x1, y1) used to compare f(x1, y1) to zGoal
	 * @param acc The % accuracy that f(x1, y1) may diverge from zGoal
	 * @return Whether or not f(point) is within a given % accuracy of zGoal
	 */
	private boolean withinAcc(double zGoal, Point point, double acc) {
		EvalVar eval[] = {new EvalVar("x", point.getX()), new EvalVar("y", point.getY())};
		double calcVal = mathFunction.evaluate(eval);
		
		// Instead of trying to computer a % accuracy from 0, the value is compared directly to the step size
		if (zGoal == 0) {
			if (Math.abs(calcVal) < stepSize / 2) {
				return true;
			}
			return false;
		} else {
			if (Math.abs(calcVal - zGoal) / (Math.abs(zGoal)) < acc) {
				return true;
			}
			return false;
		}
	}

	/**
	 * Takes a point (x1, y1) that has moved too far from its' z trace and try to move it back onto the curves
	 * 
	 * @param zGoal The Z value we want to get to
	 * @param gradient The vector we're moving in the direction of
	 * @param oPoint The initial point
	 * @param depth How many times this function has recursed
	 * @return Returns (NaN, NaN) if the reset failed, and a new point if the reset succeeded
	 * 			Failure conditions:
	 * 				Too many recursions (>2000)
	 * 				A point has NaN as one of its components
	 */
	private Point resetPoint(double zGoal, Vector gradient, Point oPoint, int depth) {

		depth += 1;
		if (depth > 2000) {
			return new Point(Double.NaN, Double.NaN);
		}
		EvalVar eval[] = {new EvalVar("x", oPoint.getX()), new EvalVar("y", oPoint.getY())};
		double calcVal = mathFunction.evaluate(eval);
		if (calcVal != calcVal) {
			return new Point(Double.NaN, Double.NaN);
		}

		MathObject[] antiFields = new MathObject[gradient.getSize()];
		for (int i = 0; i < gradient.getSize(); i++) {
			antiFields[i] = new Mult(-1, gradient.getField(i));
		}
		// antiGrad is the direction of greatest decrease
		Vector antiGrad = new Vector(antiFields);

		Point newP = new Point(oPoint.getX(), oPoint.getY());
		if (calcVal > zGoal) {
			// Step opposite in direction of gradient until near zGoal again
			newP = gradStep(antiGrad, newP);
	
		} else if(calcVal < zGoal){
			// Step in direction of gradient until near zGoal again
			newP = gradStep(gradient, newP);
		}

		// If the new point has NaN as a component, return a new point with NaN as both components
		if (newP.getX() != newP.getX() || newP.getY() != newP.getY()) {
			return new Point(Double.NaN, Double.NaN);
		}

		// If the new point is very close to the expected value, return it
		if (withinAcc(zGoal, newP, .005)) {
			return newP;
		} else {
			// Recurse until a closer point has been found
			return resetPoint(zGoal, gradient, newP, depth);
		}
			
	}
}
