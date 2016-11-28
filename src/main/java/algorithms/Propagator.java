package algorithms;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import edu.uci.ics.jung.graph.Graph;

public class Propagator extends Thread implements Runnable{
	Graph<Integer, String> G;
	double d;
	Integer w;
	Graph<Integer, String> sample;
	HashMap<String, String> selectedEdges;
	HashMap<Integer, String> newSeedMap;

	public Propagator(Graph<Integer, String> g, double d, int seed, Graph<Integer, String> sample, HashMap<String, String> selectedEdges, HashMap<Integer, String> newSeedMap) {
		// TODO Auto-generated constructor stub
		this.G = g;
		this.d = d;
		this.w = seed;
		this.sample = sample;
		this.selectedEdges = selectedEdges;
		this.newSeedMap = newSeedMap;
	}
	
	public void run() {
		// System.out.println("Analyzing for seed: "+w);
		// Find w’s friends degree
		// Rank friends based on their degree values
		Collection<Integer> friends = G.getNeighbors(w);
		// System.out.println("Friends of seed "+w+" : "+friends);

		if (friends!=null&&!friends.isEmpty()) {
			TreeMap<Integer, String> friendsByDegree = new TreeMap<Integer, String>(Collections.reverseOrder());
			for (Integer f : friends) {
				int degree = G.getNeighborCount(f);
				if (friendsByDegree.get(degree) == null)
					friendsByDegree.put(degree, "" + f);
				else
					friendsByDegree.put(degree, friendsByDegree.get(degree).concat("," + f));
			}
			// System.out.println("Friends of seed "+w+" by degree:"+friendsByDegree);

			// Select the first k = ρ·(#friends(w)), 0 < ρ ≤ 1, from the ranking list
			int k = (int) Math.floor(d * friends.size());
			if (k == 0)
				k = 1;
			// System.out.println("Value of k = "+k);

			// Let v1,v2,...vk are the top-k friends of w
			HashMap<Integer, String> selectedFriends = new HashMap<Integer, String>();
			Iterator<Map.Entry<Integer, String>> it = friendsByDegree.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, String> pair = (Map.Entry<Integer, String>) it.next();
				String vert = (String) pair.getValue();
				it.remove();
				String[] vertices = vert.split(",");
				for (String vertex : vertices) {
					selectedFriends.put(Integer.parseInt(vertex), "");
					k--;
					if (k == 0)
						break;
				}
				if (k == 0)
					break;
			}
			// System.out.println("Selected Friends: "+selectedFriends.keySet());

			// {Selected Edges} ← {Selected Edges} {(w, v1),...,(w,vk)} {(v1,w),...,(vk,w)}
			for (Integer f : selectedFriends.keySet()) {
				if (G.containsEdge(w + "-" + f)){
					//System.out.println(w + "-" + f);
					selectedEdges.put(w + "-" + f, "");
				}else{
					//System.out.println(w + "-" + f);
					selectedEdges.put(f + "-" + w, "");
				}
				// {New Seeds} ← {New Seeds} {v1,v2,...,vk}
				newSeedMap.put(f, "");
				sample.addVertex(f);
			}
		}
	}

	
}
