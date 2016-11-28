package visualizers;

import javax.swing.JFrame;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;

public class CircleVisualizer {
	public static void visualize(Graph<Integer, String> g, String name) {
		
		Layout<Integer, String> layout = new CircleLayout<Integer, String>(g);
		//layout.setSize(new Dimension(300, 300)); // sets the initial size of the space
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		BasicVisualizationServer<Integer, String> vv = new BasicVisualizationServer<Integer, String>(layout);
		//vv.setPreferredSize(new Dimension(350, 350)); // Sets the viewing area size

		JFrame frame = new JFrame(name);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);
	}

}
