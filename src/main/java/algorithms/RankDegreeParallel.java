package algorithms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

public class RankDegreeParallel {
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

	public static Graph<Integer, String> sample(Graph<Integer, String> G, int seed, double d, int sampleSize) {
		HashMap<Integer, String> seedMap = new HashMap<Integer, String>();

		// Initialize seeds: {Seeds} ← select s nodes uniformly at random
		seedMap = generateSeeds(G, seed);
		// System.out.println("Initial Seeds: "+seedMap.keySet());

		// Sample←∅
		Graph<Integer, String> sample = new SparseMultigraph<Integer, String>();

		// while|Sample|< x do
		while (sample.getVertexCount() < sampleSize) {
			// {Selected Edges} ← ∅
			HashMap<String, String> selectedEdges = new HashMap<String, String>();
			// {New Seeds} ← ∅
			HashMap<Integer, String> newSeedMap = new HashMap<Integer, String>();

			// for ∀w ∈ {Seeds} do
			Iterator<Entry<Integer, String>> it = seedMap.entrySet().iterator();
			int s = seedMap.size();
			//System.out.println("SeedMap size = "+s);
			if(s>0){
				Thread[] threads = new Thread[s];
				while (it.hasNext()) {
					s--;
					Map.Entry<Integer, String> pair = (Map.Entry<Integer, String>)it.next();
					int w = pair.getKey();
					System.out.println(w+","+s);
					sample.addVertex(w);
					//System.out.println("creating "+s);
					threads[s] = new Propagator(G, d, w, sample, selectedEdges, newSeedMap);
					threads[s].start();
				}
				for(int i = seedMap.size()-1; i>=0; i--){
					//System.out.println(i);
					try {
						if(threads[i]!=null)
							threads[i].join();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
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

			// {Seeds} ← {New Seeds}
			seedMap = newSeedMap;
			if (seedMap.isEmpty()) {
				seedMap = generateSeeds(G, seed);
			}
		}
		return sample;
	}
}
