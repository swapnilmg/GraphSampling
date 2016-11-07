package input;

import java.util.HashMap;

import algorithms.RankDegree;
import edu.uci.ics.jung.graph.impl.DirectedSparseEdge;

public class SimpleGraph {
	HashMap<String, String> vertices;
	HashMap<String, String> edges;
	HashMap<String, HashMap<String, String>> friends;
	
	public SimpleGraph(){
		init();
	}
	
	private void init(){
		vertices = new HashMap<String, String>();
		edges = new HashMap<String, String>();
	}
	
	public void addVertex(int i){
		vertices.put(""+i, "");
		this.calculate();
	}
	
	public void addEdge(int a, int b){
		edges.put(a+"-"+b, "");
		//edges.put(b+" "+a, "");
		this.calculate();
	}
	
	public void removeEdge(int a, int b){
		edges.remove(a+"-"+b);
		this.calculate();
	}
	
	public HashMap<String, String> getVertices(){
		return vertices;
	}
	
	public HashMap<String, String> getEdges(){
		return edges;
	}
	
	public HashMap<String, HashMap<String, String>> getFriends(){
		return friends;
	}
	
	public void calculate(){
		friends = new HashMap<String, HashMap<String,String>>();
		for(String i : vertices.keySet()){
			HashMap<String, String> temp = new HashMap<String, String>();
			for(String j : edges.keySet()){
				String[] vert = j.split("-");
				if(i.equals(vert[0])){
					temp.put(vert[1], "");
				}else if(i.equalsIgnoreCase(vert[1])){
					temp.put(vert[0], "");
				}
			}
			friends.put(i, temp);
		}
	}
	
	public static void main(String[] argc){
		SimpleGraph G = new SimpleGraph();
		for(int i=0; i<100; i++){
			G.addVertex(i);
		}
		/*G.addVertex(0);
		G.addVertex(1);
		G.addVertex(2);
		G.addVertex(3);
		G.addVertex(4);
		G.addVertex(5);
		G.addVertex(6);*/
		HashMap<String, String> seenEdges = new HashMap<String, String>();
		for (int i = 0; i < 80; i++) {
			int a = (int) (Math.random() * 45);
			int b = (int) (Math.random() * 90);
			// need to keep track of seen edges
			if (!seenEdges.containsKey(a + " " + b)) {
				G.addEdge(a,b);
				seenEdges.put(a+"-"+b, "");
			}
		}
		/*G.addEdge(0, 1);
		G.addEdge(0, 2);
		G.addEdge(0, 3);
		G.addEdge(1, 3);
		G.addEdge(1, 4);
		G.addEdge(2, 3);
		G.addEdge(2, 5);
		G.addEdge(2, 6);
		G.addEdge(3, 4);
		G.addEdge(3, 5);
		G.addEdge(3, 6);
		G.addEdge(4, 6);
		G.addEdge(5, 6);*/
		MyGraph.createRandom(G);
		System.out.println("Vertices = "+G.getVertices().keySet());
		System.out.println("Edges = "+G.getEdges().keySet());
		int seed = 4;
		double top = 0.1;
		int sampleSize = 20;
		System.out.println("Finding sample of size "+sampleSize+" with "+seed+" seeds and "+top+" influence");
		RankDegree.sample(G, seed, top, sampleSize);
	}
}
