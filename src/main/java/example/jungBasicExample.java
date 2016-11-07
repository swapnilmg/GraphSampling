package example;

import java.awt.Color;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import samples.graph.BasicRenderer;
import samples.graph.PluggableRendererDemo;

import edu.uci.ics.jung.algorithms.importance.PageRank;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;
import edu.uci.ics.jung.graph.impl.SparseGraph;
import edu.uci.ics.jung.graph.impl.SparseVertex;
import edu.uci.ics.jung.graph.impl.UndirectedSparseEdge;
import edu.uci.ics.jung.visualization.FRLayout;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.Renderer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.contrib.CircleLayout;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;

public class jungBasicExample extends JPanel {

	private Graph m_graph;
	private Layout mVisualizer;
	private Renderer mRenderer;
	private VisualizationViewer mVizViewer;
	private	DefaultModalGraphMouse m_graphmouse;
	
	public jungBasicExample(){
	
		m_graph = new SparseGraph();//PluggableRendererDemo().getGraph();
		
		
		
		//for fun try ....  new CircleLayout(m_graph);  
		mVisualizer = new FRLayout(m_graph);
		
		mRenderer = new PluggableRenderer();//BasicRenderer();
				
		mVizViewer = new VisualizationViewer(mVisualizer, mRenderer);
		mVizViewer.setBackground(Color.WHITE);
		
		m_graphmouse = new DefaultModalGraphMouse();
	    mVizViewer.setGraphMouse(m_graphmouse);
		
		add(mVizViewer);
		
			somefunction();
	}
	
	public void somefunction(){
		for(int i=0;i<200;i++){
			m_graph.addVertex(new SparseVertex());
		}
		
		
		//as an example...
		//lets loop through all verticies....
		Iterator walker = m_graph.getVertices().iterator();
		for(;walker.hasNext();){
			Object j = walker.next();
			System.out.println(j.getClass()+" "+j.toString());
		}
		//need to recalcualte where everyone is drawn...
		mVisualizer.restart();
		
		//show it off
		mVizViewer.revalidate();
		mVizViewer.repaint();
	}
	
	
	
	
	public static void main(String[] args) {
	//create a window to display...	
	JFrame jf = new JFrame("basic graph");
	//this is something we are adding from this class
	jf.getContentPane().add(new jungBasicExample());
	
	//set some size
	jf.setSize(700, 500);
	
	//do something when click on x
	jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	//make sure everything fits...
	jf.pack();
	//make it show up...
	jf.setVisible(true);

	}
	
}