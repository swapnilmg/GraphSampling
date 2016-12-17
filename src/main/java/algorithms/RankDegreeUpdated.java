package algorithms;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import analytics.ClusteringCoefficient;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

public class RankDegreeUpdated {

	private static HashMap<Integer, String> generateSeeds(Graph<Integer, String> G, int seed) {
		HashMap<Integer, String> seedMap = new HashMap<Integer, String>();
		for (int i = 0; i < seed;) {
			int v = (int) (Math.random() * G.getVertexCount());
			if (!(seedMap.containsKey(v))) {
				seedMap.put(v, "");
				i++;
			}
		}
		return seedMap;
	}

	public static Graph<Integer, String> sample(Graph<Integer, String> G, int sampleSize) {
		HashMap<Integer, String> seedMap = new HashMap<Integer, String>();

		// Initialize seeds: {Seeds} ← select s nodes uniformly at random
		seedMap = generateSeeds(G, 1);
		// System.out.println("Initial Seeds: "+seedMap.keySet());

		// Sample←∅
		Graph<Integer, String> sample = new SparseMultigraph<Integer, String>();
		double ccMain = 0.6055;//ClusteringCoefficient.calculate(G);
		
		double d=0.5;

		// while|Sample|< x do
		while (sample.getVertexCount() < sampleSize) {
			// {Selected Edges} ← ∅
			HashMap<String, String> selectedEdges = new HashMap<String, String>();
			// {New Seeds} ← ∅
			HashMap<Integer, String> newSeedMap = new HashMap<Integer, String>();

			// for ∀w ∈ {Seeds} do
			for (Integer w : seedMap.keySet()) {
				sample.addVertex(w);
				// System.out.println("Analyzing for seed: "+w);
				// Find w’s friends degree
				// Rank friends based on their degree values
				Collection<Integer> friends = G.getNeighbors(w);
				// System.out.println("Friends of seed "+w+" : "+friends);

				if (!friends.isEmpty()) {
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
						if (G.containsEdge(w + "-" + f))
							selectedEdges.put(w + "-" + f, "");
						else
							selectedEdges.put(f + "-" + w, "");

						// {New Seeds} ← {New Seeds} {v1,v2,...,vk}
						newSeedMap.put(f, "");
						sample.addVertex(f);
					}
				}
			}

			// Sample <- Sample + {Selected Edges}
			for (String e : selectedEdges.keySet()) {
				String[] v = e.split("-");
				int v1 = Integer.parseInt(v[0]);
				int v2 = Integer.parseInt(v[1]);
				sample.addEdge(e, v1, v2, EdgeType.UNDIRECTED);
				// G ← G \ {S elected Edges}
				G.removeEdge(e);
			}
			
			long start = System.currentTimeMillis();
			double ccSample = ClusteringCoefficient.calculate(sample);
			long end = System.currentTimeMillis();
			System.out.println(end-start);
			if(ccSample>ccMain&&d<=0.9){
				d=d+0.1;
			}
			if(ccSample<ccMain&&d>=0.2){
				d=d-0.1;
			}

			// {Seeds} ← {New Seeds}
			seedMap = newSeedMap;
			if (seedMap.isEmpty()) {
				seedMap = generateSeeds(G, 1);
			}
		}
		//System.out.println("D = "+d);
		return sample;
	}
}
