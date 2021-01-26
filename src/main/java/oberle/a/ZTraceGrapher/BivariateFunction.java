package oberle.a.ZTraceGrapher;
import java.lang.Math;


public class BivariateFunction{

	// Hard coded function to display
	public double value(double x, double y) {
		
		double zValue = Math.pow(x, 3) -.5* Math.pow(y, 2);
		
		return zValue;
	}
	
	public String toString() {
		return "x^3 - .5y^2";
	}

}
