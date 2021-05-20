package traceGrapher;

import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;

import java.awt.geom.Ellipse2D;
import java.awt.Color;
import javax.swing.JFrame;
import java.awt.Shape;


public class CoordGraph extends JFrame{


	private static final long serialVersionUID = 1L;
	
	public CoordGraph(XYSeriesCollection dataset, String function) {
		
		// Creates a chart using JFreeChart in order to display the data
		JFreeChart chart = ChartFactory.createScatterPlot("Z Traces of f(x,y) = " + function, "X-Axis", "Y-Axis", dataset);
		XYPlot plot = (XYPlot)chart.getPlot();
		plot.setBackgroundPaint(new Color(255, 255, 255));
		
		Shape plotShape = new Ellipse2D.Double(0, 0, 4, 4);
		XYItemRenderer renderer = plot.getRenderer();
		
		for (int i = 0; i < dataset.getSeriesCount(); i++) {
			renderer.setSeriesShape(i, plotShape);
		}

		ChartPanel panel = new ChartPanel(chart);
		this.setContentPane(panel);
		
	}
	
	
}
