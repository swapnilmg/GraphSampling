package algorithms;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import input.MyGraph;
import input.SimpleGraph;

public class RankDegree {
	public static void sample(SimpleGraph G, int seed, double d, int sampleSize){
		HashMap<String, String> vertices = G.getVertices();
		HashMap<String, String> edges = G.getEdges();
		HashMap<String, String> seedMap = new HashMap<String, String>();
		for(int i=0; i<seed;){
			int v = (int) (Math.random()*vertices.size());
			if(!(seedMap.containsKey(v))){
				seedMap.put(""+v, "");
				i++;
			}
		}
		
		SimpleGraph sample = new SimpleGraph();
		while(sample.getEdges().size()< sampleSize){
			System.out.println("");
			System.out.println("Sampled Vertices = "+sample.getVertices().keySet());
			System.out.println("Sampled Edges = "+sample.getEdges().keySet());
			System.out.println("Seeds = "+seedMap.keySet());
			HashMap<String, String> selectedEdges = new HashMap<String, String>();
			HashMap<String, String> newSeedMap = new HashMap<String, String>();
			
			for(String i : seedMap.keySet()){
				System.out.println("\nFinding suitable friend of "+i);
				HashMap<String, String> friends = G.getFriends().get(i);
				System.out.println("Friends of "+i+" = "+friends.keySet());
				
				TreeMap <Integer, String> friendsByDegree = new TreeMap<Integer, String>(Collections.reverseOrder());
				for(String j : friends.keySet()){
					int degree = G.getFriends().get(j).size();
					System.out.println("Degree of "+j+" = "+degree);
					if(friendsByDegree.get(degree)==null)
						friendsByDegree.put(degree,j);
					else
						friendsByDegree.put(degree,friendsByDegree.get(degree).concat(","+j));
				}
				
				int k = (int) Math.floor(d*friends.size());
				if(k==0)
					k=1;
				System.out.println("Value of k = "+k);
				
				HashMap <String, String> selectedFriends = new HashMap<String, String>();
				Iterator it = friendsByDegree.entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry pair = (Map.Entry)it.next();
			        String vert = (String) pair.getValue();
			        it.remove(); // avoids a ConcurrentModificationException
			        String[] v = vert.split(",");
			        for(String v1 : v){
			        	selectedFriends.put(v1, "");
			        	k--;
			        	if(k==0)
			        	break;
			        }
			        if(k==0)
			        	break;
			    }
			    
			    System.out.println("Selected Vertices = "+selectedFriends.keySet());
			    for(String s : selectedFriends.keySet()){
			    	if(edges.containsKey(i+"-"+s))
			    		selectedEdges.put(i+"-"+s, "");
			    	else
			    		selectedEdges.put(s+"-"+i, "");
			    	
			    	newSeedMap.put(s, "");
			    }    
			}
			for(String e: selectedEdges.keySet()){
				String[] v = e.split("-");
				int v1 = Integer.parseInt(v[0]);
				int v2 = Integer.parseInt(v[1]);
				sample.addEdge(v1, v2);
				sample.addVertex(v1);
				sample.addVertex(v2);
				G.removeEdge(v1, v2);
				G.calculate();
			}
			seedMap = newSeedMap;
		}
		
		System.out.println("\nSampled Vertices = "+sample.getVertices().keySet());
		System.out.println("Sampled Edges = "+sample.getEdges().keySet());
		MyGraph.createRandom(sample);
	}
}
