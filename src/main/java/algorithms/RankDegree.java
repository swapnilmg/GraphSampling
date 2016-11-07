package algorithms;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import input.SimpleGraph;

public class RankDegree {
	public static void sample(SimpleGraph G, int seed, double d, int sampleSize){
		HashMap<String, String> seedMap = new HashMap<String, String>();
		for(int i=0; i<seed;){
			int v = (int) (1+(Math.random()*G.getVertices().size()));
			if(!seedMap.containsKey(v)){
				seedMap.put(""+v, "");
				i++;
			}
		}
		System.out.println("Seeds = "+seedMap.keySet());
		SimpleGraph sample = new SimpleGraph();
		int a=0;
		while(a< sampleSize){
			HashMap<String, String> selectedEdges = new HashMap<String, String>();
			HashMap<String, String> newSeedMap = new HashMap<String, String>();
			
			for(String i : seedMap.keySet()){
				
				HashMap<String, String> friends = G.getFriends().get(i);
				System.out.println("Friends of "+i+" = "+friends);
				
				TreeMap <Integer, String> friendsByDegree = new TreeMap<Integer, String>(Collections.reverseOrder());
				for(String j : friends.keySet()){
					int degree = G.getFriends().get(j).size();
					System.out.println("Degree of "+j+" = "+degree);
					if(friendsByDegree.get(degree)==null)
						friendsByDegree.put(degree,j);
					else
						friendsByDegree.put(degree,friendsByDegree.get(degree).concat(","+j));
				}
				
				int k = (int) d*friends.size();
				if(k==0)
					k=1;
				
				Iterator it = friendsByDegree.entrySet().iterator();
			    while (it.hasNext()) {
			        Map.Entry pair = (Map.Entry)it.next();
			        System.out.println(pair.getKey() + " = " + pair.getValue());
			        it.remove(); // avoids a ConcurrentModificationException
			    }
			}
			a++;
		}
	}
}
