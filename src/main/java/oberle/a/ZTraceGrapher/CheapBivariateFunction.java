package oberle.a.ZTraceGrapher;
import org.apache.commons.math3.analysis.*;
import org.apache.commons.math3.analysis.function.*;
import java.lang.Math;

//For now, I'm hard coding f(x,y)
public class CheapBivariateFunction implements BivariateFunction{


	public double value(double x, double y) {
		
		double zValue = Math.pow(x, (1/3.0)) + Math.sin(x*y);
		
		return zValue;
	}
	
	public String toString() {
		return "e^x + sin(xy)";
	}

}
