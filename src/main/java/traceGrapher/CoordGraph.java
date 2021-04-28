package traceGrapher;

import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;

import java.awt.Color;
import javax.swing.JFrame;


public class CoordGraph extends JFrame{


	private static final long serialVersionUID = 1L;
	
	public CoordGraph(XYSeriesCollection dataset, String function) {
		
		// Creates a chart using JFreeChart in order to display the data
		JFreeChart chart = ChartFactory.createScatterPlot("Z Traces of f(x,y) = " + function, "X-Axis", "Y-Axis", dataset);
		XYPlot plot = (XYPlot)chart.getPlot();
		plot.setBackgroundPaint(new Color(255, 255, 255));
		
			
		ChartPanel panel = new ChartPanel(chart);
		this.setContentPane(panel);
		
	}
	
	
}
