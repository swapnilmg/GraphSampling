package input;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import algorithms.RankDegreeAlgorithm;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.ConstantEdgeStrokeFunction;
import edu.uci.ics.jung.graph.decorators.ConstantVertexPaintFunction;
import edu.uci.ics.jung.graph.decorators.DefaultToolTipFunction;
import edu.uci.ics.jung.graph.decorators.EdgeShape;
import edu.uci.ics.jung.graph.decorators.EllipseVertexShapeFunction;
import edu.uci.ics.jung.graph.decorators.PickableEdgePaintFunction;
import edu.uci.ics.jung.graph.decorators.ToStringLabeller;
import edu.uci.ics.jung.graph.decorators.VertexFontFunction;
import edu.uci.ics.jung.graph.impl.SparseVertex;
import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.UndirectedSparseGraph;
import edu.uci.ics.jung.utils.Pair;
import edu.uci.ics.jung.visualization.FRLayout;
import edu.uci.ics.jung.visualization.ISOMLayout;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.MultiPickedState;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.ShapePickSupport;
import edu.uci.ics.jung.visualization.SpringLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

public class MyGraph extends JPanel {

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
	MyGraph() {
		vertexmaps = new HashMap<Integer, Vertex>();
		// setupInformationPanel();
		/* type of graph you are using... */
		mainGraph = new UndirectedSparseGraph();

		mainRender = new PluggableRenderer();
		/* how nodes will be places....change this */
		mainLayout = new FRLayout(mainGraph);//CircleLayout(mainGraph);// SpringLayout(mainGraph);//

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
		mainRender.setEdgePaintFunction(new PickableEdgePaintFunction(VV.getPickedState(), Color.black, Color.red));
		mainRender.setVertexShapeFunction(new EllipseVertexShapeFunction());
		mainRender.setVertexPaintFunction(new ConstantVertexPaintFunction(Color.red));
		mainRender.setVertexLabelCentering(true);
		// lets color the edges appropriatly
		mainRender.setEdgeShapeFunction(new EdgeShape.Line() {

		});
		mainRender.setEdgeStrokeFunction(new ConstantEdgeStrokeFunction(new BasicStroke(0.5f)));
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
		testwindow.getContentPane().add(getInformationPanel(),BorderLayout.NORTH);
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
	public static void createRandom(int numVertices, int numEdges) {
		// create a test graph
		MyGraph JCV = new MyGraph();
		for (int i = 0; i < numVertices; i++) {
			JCV.addVertex(new SparseVertex());
		}
		/*HashMap<String, String> seenEdges = new HashMap<String, String>();
		for (int i = 0; i < numEdges; ) {
			int a = (int) (Math.random() * numVertices);
			int b = (int) (Math.random() * numVertices);
			// need to keep track of seen edges
			if (!seenEdges.containsKey(a + " " + b)) {
				JCV.addEdge(new UndirectedSparseEdge(JCV.getVertex(a), JCV
						.getVertex(b)));
				seenEdges.put(a + " " + b, "");
				seenEdges.put(b + " " + a, "");
				i++;
			}
		}*/
		JCV.addEdge(new UndirectedSparseEdge(JCV.getVertex(0),JCV.getVertex(1)));
		JCV.addEdge(new UndirectedSparseEdge(JCV.getVertex(0),JCV.getVertex(2)));
		JCV.addEdge(new UndirectedSparseEdge(JCV.getVertex(0),JCV.getVertex(3)));
		JCV.addEdge(new UndirectedSparseEdge(JCV.getVertex(1),JCV.getVertex(3)));
		JCV.addEdge(new UndirectedSparseEdge(JCV.getVertex(1),JCV.getVertex(4)));
		JCV.addEdge(new UndirectedSparseEdge(JCV.getVertex(2),JCV.getVertex(3)));
		JCV.addEdge(new UndirectedSparseEdge(JCV.getVertex(2),JCV.getVertex(5)));
		JCV.addEdge(new UndirectedSparseEdge(JCV.getVertex(2),JCV.getVertex(6)));
		JCV.addEdge(new UndirectedSparseEdge(JCV.getVertex(3),JCV.getVertex(4)));
		JCV.addEdge(new UndirectedSparseEdge(JCV.getVertex(3),JCV.getVertex(5)));
		JCV.addEdge(new UndirectedSparseEdge(JCV.getVertex(3),JCV.getVertex(6)));
		JCV.addEdge(new UndirectedSparseEdge(JCV.getVertex(4),JCV.getVertex(6)));
		JCV.addEdge(new UndirectedSparseEdge(JCV.getVertex(5),JCV.getVertex(6)));
		JCV.showSomething();
		JCV.RankDegree();
	}
	
	private void RankDegree() {
		RankDegreeAlgorithm.sample(mainGraph, 3, 1, 3);
		
	}

	public static void createFromFile(File file){
		
	}
	
	public static void main(String[] argc){
		createRandom(7, 8);
	}

}