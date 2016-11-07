package input;

import java.util.HashMap;

import algorithms.RankDegree;

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
	}
	
	public void addEdge(int a, int b){
		edges.put(a+"-"+b, "");
		//edges.put(b+" "+a, "");
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
		G.addVertex(1);
		G.addVertex(2);
		G.addVertex(3);
		G.addVertex(4);
		G.addVertex(5);
		G.addVertex(6);
		G.addVertex(7);
		G.addEdge(1, 2);
		G.addEdge(1, 3);
		G.addEdge(1, 4);
		G.addEdge(2, 4);
		G.addEdge(2, 5);
		G.addEdge(3, 4);
		G.addEdge(3, 6);
		G.addEdge(3, 7);
		G.addEdge(4, 5);
		G.addEdge(4, 6);
		G.addEdge(4, 7);
		G.addEdge(5, 7);
		G.addEdge(6, 7);
		G.calculate();
		System.out.println("Vertices = "+G.getVertices().keySet());
		System.out.println("Edges = "+G.getEdges().keySet());
		System.out.println("Friends = "+G.getFriends());
		RankDegree.sample(G, 2, 0.1, 1);
	}
}
