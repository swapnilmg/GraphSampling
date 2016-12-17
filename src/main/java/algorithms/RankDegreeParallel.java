package algorithms;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
		int i=0;

		// while|Sample|< x do
		while (sample.getVertexCount() < sampleSize) {
			System.out.println("Starting round "+(++i)+" with "+seedMap.size()+" seeds");
			
			// {Selected Edges} ← ∅
			HashMap<String, String> selectedEdges = new HashMap<String, String>();
			// {New Seeds} ← ∅
			HashMap<Integer, String> newSeedMap = new HashMap<Integer, String>();

			// for ∀w ∈ {Seeds} do
			Iterator<Entry<Integer, String>> it = seedMap.entrySet().iterator();
			int s = seedMap.size();
			//System.out.println("SeedMap size = "+s);
			ExecutorService executor = Executors.newFixedThreadPool(8);
			
			while (it.hasNext()) {
				Map.Entry<Integer, String> pair = (Map.Entry<Integer, String>) it.next();
				int w = pair.getKey();
				sample.addVertex(w);
				Runnable worker = new Propagator(G, d, w, sample, selectedEdges, newSeedMap);
				executor.execute(worker);
			}
			executor.shutdown();
	        while (!executor.isTerminated()) {
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
