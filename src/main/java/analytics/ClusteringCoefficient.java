package analytics;

import java.util.Map;

import edu.uci.ics.jung.algorithms.metrics.Metrics;
import edu.uci.ics.jung.graph.Graph;

public class ClusteringCoefficient {

	public static double calculate(Graph<Integer, String> graph) {
		Map <Integer, Double> cc = Metrics.clusteringCoefficients(graph);
		double sum = 0;
		for(double  e: cc.values()){
			//System.out.println(e);
			sum+=e;
		}
		
		return sum/cc.size();
	}
}
