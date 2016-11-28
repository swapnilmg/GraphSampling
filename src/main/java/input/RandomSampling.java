package input;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import algorithms.RankDegree;
import analytics.ClusteringCoefficient;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import visualizers.CircleVisualizer;
import visualizers.StaticVisualizer;


public class RandomSampling {
	public static void main(String[] argc) throws IOException {
		Graph<Integer, String> mainGraph = createRandomGraph(10, 30);
		double ccMain = ClusteringCoefficient.calculate(mainGraph);
		System.out.println("Main Graph: "+mainGraph.toString());
		System.out.println("Clustering Coefficient of main graph : "+ccMain);
		CircleVisualizer.visualize(mainGraph, "Main Graph - "+ccMain);
		
		Graph<Integer, String> mainGraph1 = createRandomGraph(10, 30);
		Graph<Integer, String> sampleRD01 = RankDegree.sample(mainGraph1, 1, 0.1, 8);
		double ccSampleRD01 = ClusteringCoefficient.calculate(sampleRD01);
		System.out.println("Rank Degree (0.1): "+sampleRD01.toString());
		System.out.println("Clustering Coefficient of sampled RD(0.1) : "+ccSampleRD01);
		
		Graph<Integer, String> mainGraph2 = createRandomGraph(10, 30);
		Graph<Integer, String> sampleRD1 = RankDegree.sample(mainGraph2, 1, 1, 8);
		double ccSampleRD1 = ClusteringCoefficient.calculate(sampleRD1);
		System.out.println("Rank Degree (Max): "+sampleRD1.toString());
		System.out.println("Clustering Coefficient of sampled RD(Max) : "+ccSampleRD1);
		
		CircleVisualizer.visualize(mainGraph, "Main Graph - "+ccMain);
		CircleVisualizer.visualize(sampleRD01, "RankDegree 0.1 - "+ccSampleRD01);
		CircleVisualizer.visualize(sampleRD1, "RankDegree Max - "+ccSampleRD1);
	}

	private static Graph<Integer, String> createRandomGraph(int vertices, int edges) throws IOException {
		Graph<Integer, String> g = new SparseMultigraph<Integer, String>();
		File verticesFile = new File("src/main/resources/random/vertices.txt");
		File edgesFile = new File("src/main/resources/random/edges.txt");
		if(!(verticesFile.exists()&&edgesFile.exists())){
			verticesFile.delete();
			edgesFile.delete();
			verticesFile.createNewFile();
			edgesFile.createNewFile();
			FileWriter fw = new FileWriter(verticesFile.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);
			for(int i=0; i<vertices; i++){
				out.println(i);
			}
			out.close();
			bw.close();
			fw.close();
			FileWriter fwe = new FileWriter(edgesFile.getAbsoluteFile(), true);
			BufferedWriter bwe = new BufferedWriter(fwe);
			PrintWriter oute = new PrintWriter(bwe);
			HashMap<String, String> seenEdges = new HashMap<String, String>();
			int i=0;
			while(i<edges) {
				int a = (int) (Math.random() * vertices);
				int b = (int) (Math.random() * vertices);
				// need to keep track of seen edges
				if (!(seenEdges.containsKey(a + "-" + b)||a==b)) {
					System.out.println(a+" "+b);
					oute.println(a+" "+b);
					seenEdges.put(a+"-"+b, "");
					seenEdges.put(b+"-"+a, "");
					i++;
				}
			}
			oute.close();
			bwe.close();
			fwe.close();
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(verticesFile));
		    for(String line; (line = br.readLine()) != null; ) {
		        String[] vertex = line.split(" ");
		        g.addVertex(Integer.parseInt(vertex[0]));
		    }	    
		} catch (IOException e) {
			System.out.println("Unable to read vertices:\n");
			e.printStackTrace();
			return null;
		}

		// add edges to create a circuit
		try {
			BufferedReader br = new BufferedReader(new FileReader(edgesFile));
		    for(String line; (line = br.readLine()) != null; ) {
		        String[] vertex = line.split(" ");
		        g.addEdge(vertex[0]+"-"+vertex[1],Integer.parseInt(vertex[0]), Integer.parseInt(vertex[1]), EdgeType.UNDIRECTED);
		    }	    
		} catch (IOException e) {
			System.out.println("Unable to read edges:\n");
			e.printStackTrace();
			return null;
		}
		return g;
	}
}
