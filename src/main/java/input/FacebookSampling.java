package input;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import algorithms.RankDegree;
import algorithms.RankDegreeParallel;
import algorithms.RankDegreeUpdated;
import algorithms.RankDegreeUpdatedParallel;
import analytics.ClusteringCoefficient;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

public class FacebookSampling {
	
	public static void main(String[] argc) throws IOException{
		//Graph<Integer, String> mainGraph = createGraph();
		System.out.println("Calculating clustering coefficient");
		double ccMain = 0.4970; //ClusteringCoefficient.calculate(mainGraph);
		System.out.println("Graph\tCC\t\t\tTime");
		System.out.println("Orig\t"+ccMain+"\t-");
		
		Graph<Integer, String> mainGraph0 = createGraph();
		int vc = mainGraph0.getVertexCount();
		long start = System.currentTimeMillis();
		Graph<Integer, String> sampleRD0 = RankDegreeUpdatedParallel.sample(mainGraph0, (int)Math.round(vc*0.01));
		long end = System.currentTimeMillis();
		//System.out.println("time: "+(end-start));
		double ccSampleRD0 = ClusteringCoefficient.calculate(sampleRD0);
		System.out.println("RD(0.1)\t"+ccSampleRD0+"\t"+(end-start));
		System.out.println(sampleRD0.getVertexCount());
		
		File fFile = new File("sample.txt");
		if (!(fFile.exists() && fFile.isFile())) {
			fFile.createNewFile();
		}
		FileWriter fw = new FileWriter(fFile.getAbsoluteFile(), true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw);
		for(Integer v : sampleRD0.getVertices()){
			out.println(v);
		}
		for(String e : sampleRD0.getEdges()){
			out.println(e);
		}
		
		/*Graph<Integer, String> mainGraph1 = createGraph();
		start = System.currentTimeMillis();
		Graph<Integer, String>  sampleRD01 = RankDegree.sample(mainGraph1, 10, 0.5, 1000);
		end = System.currentTimeMillis();
		double ccSampleRD01 = ClusteringCoefficient.calculate(sampleRD01);
		System.out.println("RD(0.5)\t"+ccSampleRD01+"\t"+(end-start));
		
		Graph<Integer, String> mainGraph2 = createGraph();
		start = System.currentTimeMillis();
		Graph<Integer, String> sampleRD1 = RankDegree.sample(mainGraph2, 1, 1, 1000);
		end = System.currentTimeMillis();
		double ccSampleRD1 = ClusteringCoefficient.calculate(sampleRD1);
		System.out.println("RD(Max)\t"+ccSampleRD1+"\t"+(end-start));
		
		Graph<Integer, String> mainGraph3 = createGraph();
		start = System.currentTimeMillis();
		Graph<Integer, String> sampleRDU = RankDegreeUpdated.sample(mainGraph3, 1000);
		end = System.currentTimeMillis();
		double ccSampleRDU = ClusteringCoefficient.calculate(sampleRDU);
		//System.out.println("Rank Degree (Max): "+sampleRD1.toString());
		System.out.println("RDU\t"+ccSampleRDU+"\t"+(end-start));
		
		Graph<Integer, String> mainGraph4 = createGraph();
		start = System.currentTimeMillis();
		Graph<Integer, String> sampleRDP = RankDegreeParallel.sample(mainGraph4, 1, 0.1, 1000);
		end = System.currentTimeMillis();
		double ccSampleRDP = ClusteringCoefficient.calculate(sampleRDP);
		//System.out.println("Rank Degree (Max): "+sampleRD1.toString());
		//System.out.println(sampleRDP.toString());
		System.out.println("RDP(0.1)\t"+ccSampleRDP+"\t"+(end-start));*/
		
		/*Graph<Integer, String> mainGraph5 = createGraph();
		long start = System.currentTimeMillis();
		Graph<Integer, String> sampleRDUP = RankDegreeUpdatedParallel.sample(mainGraph5, 1000);
		long end = System.currentTimeMillis();
		double ccSampleRDUP = ClusteringCoefficient.calculate(sampleRDUP);
		System.out.println("RDUP\t"+ccSampleRDUP+"\t"+(end-start));*/
	}
	
	private static Graph<Integer, String> createGraph() {
		Graph<Integer, String> g = new SparseMultigraph<Integer, String>();
		
		// add the vertices
		
	    /*for(int i=1; i<=4039; i++) {
	        g.addVertex(i);
	    }*/	    
		
		// add edges to create a circuit
	    //String[] files = {"0.edges","107.edges","1684.edges","1912.edges","3437.edges","348.edges","3980.edges","414.edges","686.edges","698.edges"};
	    String[] files={"email-Enron.txt"};
	    for(String file: files){
	    	//System.out.println("Adding edges from: "+file);
			File edgesFile = new File("src/main/resources/facebook/"+file);
			try {
				BufferedReader br = new BufferedReader(new FileReader(edgesFile));
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
	    }
		return g;
	}

}
