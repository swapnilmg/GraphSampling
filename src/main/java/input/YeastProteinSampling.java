package input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import algorithms.RankDegree;
import algorithms.RankDegreeParallel;
import algorithms.RankDegreeUpdated;
import algorithms.RankDegreeUpdatedParallel;
import analytics.ClusteringCoefficient;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;


public class YeastProteinSampling {
	public static void main(String[] argc) {
		Graph<Integer, String> mainGraph = createYPGraph();
		double ccMain = ClusteringCoefficient.calculate(mainGraph);
		//System.out.println("Main Graph: "+mainGraph.toString());		
		//CircleVisualizer.visualize(mainGraph, "Main Graph - "+ccMain);
		System.out.println("Graph\tCC\t\t\tTime");
		System.out.println("Orig\t"+ccMain+"\t-");
		
		long start;
		long end;
		/*Graph<Integer, String> mainGraph0 = createYPGraph();
		long start = System.currentTimeMillis();
		Graph<Integer, String> sampleRD0 = RankDegree.sample(mainGraph0, 1, 1, (int) Math.round(mainGraph0.getVertexCount()*0.5));
		long end = System.currentTimeMillis();
		double ccSampleRD0 = ClusteringCoefficient.calculate(sampleRD0);
		//System.out.println("Rank Degree (0.1): "+sampleRD01.toString());
		System.out.println("RD(0.1)\t"+ccSampleRD0+"\t"+(end-start));
		System.out.println(sampleRD0.getVertexCount());*/
		
		
		/*Graph<Integer, String> mainGraph1 = createYPGraph();
		start = System.currentTimeMillis();
		Graph<Integer, String> sampleRD01 = RankDegree.sample(mainGraph1, 1, 0.5, 1000);
		end = System.currentTimeMillis();
		double ccSampleRD01 = ClusteringCoefficient.calculate(sampleRD01);
		//System.out.println("Rank Degree (0.1): "+sampleRD01.toString());
		System.out.println("RD(0.5)\t"+ccSampleRD01+"\t"+(end-start));
		
		Graph<Integer, String> mainGraph2 = createYPGraph();
		start = System.currentTimeMillis();
		Graph<Integer, String> sampleRD1 = RankDegree.sample(mainGraph2, 1, 1, 1000);
		end = System.currentTimeMillis();
		double ccSampleRD1 = ClusteringCoefficient.calculate(sampleRD1);
		//System.out.println("Rank Degree (Max): "+sampleRD1.toString());
		System.out.println("RD(Max)\t"+ccSampleRD1+"\t"+(end-start));*/
		
		Graph<Integer, String> mainGraph3 = createYPGraph();
		start = System.currentTimeMillis();
		Graph<Integer, String> sampleRDU = RankDegreeParallel.sample(mainGraph3, 1, 0.01, (int)Math.round(mainGraph3.getVertexCount()*0.3));
		end = System.currentTimeMillis();
		double ccSampleRDU = ClusteringCoefficient.calculate(sampleRDU);
		//System.out.println("Rank Degree (Max): "+sampleRD1.toString());
		System.out.println("RDU\t"+ccSampleRDU+"\t"+(end-start));
		
		/*Graph<Integer, String> mainGraph4 = createYPGraph();
		start = System.currentTimeMillis();
		Graph<Integer, String> sampleRDP = RankDegreeParallel.sample(mainGraph4, 1, 0.1, 1000);
		end = System.currentTimeMillis();
		double ccSampleRDP = ClusteringCoefficient.calculate(sampleRDP);
		//System.out.println("Rank Degree (Max): "+sampleRD1.toString());
		//System.out.println(sampleRDP.toString());
		System.out.println("RDP(0.1)\t"+ccSampleRDP+"\t"+(end-start));
		
		Graph<Integer, String> mainGraph5 = createYPGraph();
		start = System.currentTimeMillis();
		Graph<Integer, String> sampleRDUP = RankDegreeUpdatedParallel.sample(mainGraph5, 1000);
		end = System.currentTimeMillis();
		double ccSampleRDUP = ClusteringCoefficient.calculate(sampleRDUP);
		//System.out.println("Rank Degree (Max): "+sampleRD1.toString());
		//System.out.println(sampleRDP.toString());
		System.out.println("RDUP\t"+ccSampleRDUP+"\t"+(end-start));
		
		//CircleVisualizer.visualize(sampleRD01, "RankDegree 0.1 - "+ccSampleRD01);
		//CircleVisualizer.visualize(sampleRD1, "RankDegree Max - "+ccSampleRD1);
		
		/*System.out.println("Diameter: "+DistanceStatistics.diameter(mainGraph));
		System.out.println("Diameter: "+DistanceStatistics.diameter(sampleRD0));
		System.out.println("Diameter: "+DistanceStatistics.diameter(sampleRD01));
		System.out.println("Diameter: "+DistanceStatistics.diameter(sampleRD1));*/
		/*System.out.println("ClosenessCentrality: "+ClosenessCentrality.(mainGraph));*/
		
	}

	private static Graph<Integer, String> createYPGraph() {
		Graph<Integer, String> g = new SparseMultigraph<Integer, String>();
		
		// add the vertices
		File verticesFile = new File("src/main/resources/yeastProtein/vertices.txt");
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(verticesFile));
		    for(String line; (line = br.readLine()) != null; ) {
		        String[] vertex = line.split(" ");
		        g.addVertex(Integer.parseInt(vertex[0]));
		    }
		    br.close();
		} catch (IOException e) {
			System.out.println("Unable to read vertices:\n");
			e.printStackTrace();
			return null;
		}

		// add edges to create a circuit
		File edgesFile = new File("src/main/resources/yeastProtein/edges.txt");
		try {
			br = new BufferedReader(new FileReader(edgesFile));
		    for(String line; (line = br.readLine()) != null; ) {
		        String[] vertex = line.split(" ");
		        g.addEdge(vertex[0]+"-"+vertex[1],Integer.parseInt(vertex[0]), Integer.parseInt(vertex[1]), EdgeType.UNDIRECTED);
		    }
		    br.close();
		} catch (IOException e) {
			System.out.println("Unable to read edges:\n");
			e.printStackTrace();
			return null;
		}
		return g;
	}
}
