/**
 * 
 */
package example;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.ConstantEdgeStrokeFunction;
import edu.uci.ics.jung.graph.decorators.ConstantVertexPaintFunction;
import edu.uci.ics.jung.graph.decorators.DefaultToolTipFunction;
import edu.uci.ics.jung.graph.decorators.EdgeShape;
import edu.uci.ics.jung.graph.decorators.EllipseVertexShapeFunction;
import edu.uci.ics.jung.graph.decorators.PickableEdgePaintFunction;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;
import edu.uci.ics.jung.graph.impl.SparseVertex;
import edu.uci.ics.jung.utils.Pair;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.MultiPickedState;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.ShapePickSupport;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.contrib.CircleLayout;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

/**
 * @author shlomo Spring 2007
 * 
 * Example on how to create and display simple jung graph... make sure you can
 * follow the code
 * 
 * NOTE: need to add the .jar files in the lib subdirectory to the eclipse
 * project.... go to project properties->java build path->jars->add ->browse to
 * lib and add all files
 */
public class jungExample extends JPanel {

	private Graph mainGraph;
	// helpers
	private VisualizationViewer VV;
	private PluggableRenderer mainRender;
	private Layout mainLayout;
	private DefaultModalGraphMouse m_graphmouse;
	// helper to help refer to specific vertices...
	private HashMap<Integer, Vertex> vertexmaps;

	/**
	 * Constructor for the class to setup graph things....
	 * 
	 */
	jungExample() {
		vertexmaps = new HashMap<Integer, Vertex>();
		// setupInformationPanel();
		/* type of graph you are using... */
		mainGraph = new DirectedSparseGraph();

		mainRender = new PluggableRenderer();
		/* how nodes will be places....change this */
		mainLayout = new CircleLayout(mainGraph);// SpringLayout(mainGraph);//

		/* single DS to help you work with layouts... */
		VV = new VisualizationViewer(mainLayout, mainRender, new Dimension(500,
				500));

		/* allow clickable stuff */
		VV.setPickSupport(new ShapePickSupport());
		/* pretty much says it all */
		VV.setBackground(Color.GRAY);
		/* allow us to define mouse stuff in the graph and respond... */
		m_graphmouse = new DefaultModalGraphMouse();
		VV.setGraphMouse(m_graphmouse);
		/* this will show up when mouse moves over...feel free to change */
		VV.setToolTipFunction(new DefaultToolTipFunction() {
			public String getToolTipText(Vertex v) {
				return v.toString();
			}
			public String getToolTipText(Edge edge) {
				Pair accts = edge.getEndpoints();
				Vertex v1 = (Vertex) accts.getFirst();
				Vertex v2 = (Vertex) accts.getSecond();
				return v1 + " -- " + v2;
			}
		});

		VV.setPickedState(new MultiPickedState());

		// allow edges to light up
		mainRender.setEdgePaintFunction(new PickableEdgePaintFunction(VV
				.getPickedState(), Color.black, Color.red));
		mainRender.setVertexShapeFunction(new EllipseVertexShapeFunction());
		mainRender.setVertexPaintFunction(new ConstantVertexPaintFunction(
				Color.red));

		// lets color the edges appropriatly
		mainRender.setEdgeShapeFunction(new EdgeShape.Line() {

		});
		mainRender.setEdgeStrokeFunction(new ConstantEdgeStrokeFunction(
				new BasicStroke(0.5f)));
		add(VV);
		VV.init();

		// some scaling

	}

	private JPanel getControlPanel() {
		return new JPanel();
	}

	private JPanel getInformationPanel() {
		return new JPanel();
	}

	public void showSomething() {

		// this will make sure everything is placed correctly
		VV.restart();

		// want to zoom in correctly
		MutableTransformer mut = VV.getLayoutTransformer();
		mut.setScale(1.0, 1.0, new Point2D.Float(250, 250));
		mut = VV.getViewTransformer();
		mut.setScale(1.0, 1.0, new Point2D.Float(250, 250));

		JFrame testwindow = new JFrame();
		testwindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testwindow.setLayout(new BorderLayout());
		testwindow.getContentPane().add(this, BorderLayout.CENTER);
		testwindow.getContentPane().add(getControlPanel(), BorderLayout.SOUTH);
		testwindow.getContentPane().add(getInformationPanel(),
				BorderLayout.NORTH);
		testwindow.pack();// setSize(new Dimension(400,400));
		testwindow.setVisible(true);

	}

	/**
	 * Allows us to hide how the graph is represented... feel free to change as
	 * needed
	 * 
	 * @param v
	 */
	public void addVertex(Vertex v) {
		mainGraph.addVertex(v);
		vertexmaps.put(vertexmaps.size(), v);
	}

	/**
	 * allows us to hide how the graph is represented...
	 * 
	 * @param e
	 */
	public void addEdge(Edge e) {
		mainGraph.addEdge(e);
	}

	/**
	 * feel free to change this
	 * 
	 * @param v
	 * @return
	 */
	public Vertex getVertex(int v) {
		return vertexmaps.get(v);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// create a test graph
		jungExample JCV = new jungExample();
		for (int i = 0; i < 100; i++) {
			JCV.addVertex(new SparseVertex());
		}
		HashMap<String, String> seenEdges = new HashMap<String, String>();
		for (int i = 0; i < 40; i++) {
			int a = (int) (Math.random() * 45);
			int b = (int) (Math.random() * 90);
			// need to keep track of seen edges
			if (!seenEdges.containsKey(a + " " + b)) {
				JCV.addEdge(new DirectedSparseEdge(JCV.getVertex(a), JCV
						.getVertex(b)));
				seenEdges.put(a + " " + b, "");
			}
		}

		JCV.showSomething();

	}

}