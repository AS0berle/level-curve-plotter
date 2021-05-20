package traceGrapher;

import java.util.Objects;

public class Point {
	
	private double xVal;
	private double yVal;
	
	public Point(double x, double y) {
		xVal = x;
		yVal = y;
	}
	
	public double getX(){
		return xVal;
	}
	
	public double getY() {
		return yVal;
	}
	
	public String toString() {
		return "(" + xVal + "," + yVal + ")";
	}
	
	public boolean equals(Object other) {
	    if (this == other)
	        return true;
	    if (other == null)
	        return false;
	    if (getClass() != other.getClass())
	        return false;
	    Point person = (Point) other;
	    return Objects.equals(xVal, person.xVal) && Objects.equals(yVal, person.yVal);
	}
	
	public int hashCode() {
		  return Objects.hash(xVal, yVal);
		}
}
