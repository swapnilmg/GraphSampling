package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;

public class RankDegreeAlgorithm {

	public static void sample(Graph G, int seed, int top, int sampleSize ){
		Set vertices = G.getVertices();
		System.out.println("List of vertices: "+vertices);
		Set edges = G.getEdges();
		System.out.println("List of edges: "+edges);
		Set seedList=Collections.<Vertex>emptySet();

		for(int i=0; i<seed; ){
			int v = (int) (Math.random()*vertices.size());
			System.out.println("V"+v+" = "+vertices);
			i++;
		}
	}
}
