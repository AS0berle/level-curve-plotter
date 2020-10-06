package oberle.a.ZTraceGrapher;

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

}
