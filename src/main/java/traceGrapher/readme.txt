The traceGrapher package utilizes the mathWrappers package to calculate levl curves, while JFreeChart handles the GUI

Point.java is used internally, and is simply a pair of doubles
CoordGraph.java is an extended JFrame that visualizes the graph
PointAggregator.java is used to calculate all the points on the level curves

When initializing a new CoordGraph, you will need to provide an XYSeriesCollection (from JFreeChart) that contains the points to graph
as well as the name of the function you are graphing

Use a PointAggregator's getZracesFast() function to create an XYSeriesCollection for CoordGraph
