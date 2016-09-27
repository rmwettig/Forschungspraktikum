import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import sonumina.math.graph.DirectedGraph;
import junit.framework.TestCase;


public class NetworkAnalysisTest extends TestCase {

	public void testLoadNetworkFromFile()
	{
		String path = "data\\savedNetwork.txt";
//		DirectedGraph<Protein> g = NetworkAnalysis.loadNetworkFromFile(path);
	}
	public void testFromFile() 
	{
		String f = "data\\testnetz.txt";
		DirectedGraph<Protein> graph2 = new DirectedGraph<Protein>();

		NetworkAnalysis.loadNodesFromFile(graph2, f, "", " ", true);
		
		for(Protein n: graph2.getVertices())
			System.out.println(n.ensemblProtId);
		
		if(true)
			return;
		String file = "data\\test_net.txt";
		DirectedGraph<Protein> graph = new DirectedGraph<Protein>();
		
		NetworkAnalysis.loadNodesFromFile(graph, file, "", " ", true);
		
		/*     2
		 *   / |  \
		 * 1 - 3   5
		 *   \ |  /
		 *     4
		 */    
		
		
		for(Protein n : graph.getVertices())
		{
			System.out.println("Current node: " + n.ensemblProtId);
			Iterator<Protein> children = graph.getChildNodes(n);
			String conns = "";
			while(children.hasNext())
			{
				Protein v = children.next();
				conns += v.ensemblProtId + " ";
			}
			
			System.out.println("Has connections to: " + conns);
		}
		
		HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
		ArrayList<String> tmp = new ArrayList<String>();
		tmp.add("2");
		tmp.add("3");
		tmp.add("4");
		map.put("1", tmp);
		tmp.clear();
		
		tmp.add("1");
		tmp.add("3");
		tmp.add("5");
		map.put("2", tmp);
		tmp.clear();
		
		tmp.add("2");
		tmp.add("4");
		tmp.add("1");
		map.put("3", tmp);
		tmp.clear();
		
		tmp.add("1");
		tmp.add("3");
		tmp.add("5");
		map.put("4", tmp);
		tmp.clear();
		
		tmp.add("2");
		tmp.add("3");
		tmp.add("4");
		map.put("5", tmp);
		tmp.clear();
		
		for(String k : map.keySet())
			for(String v : map.get(k))
				assertTrue(String.format("Edge (%s,%s) does not exist. ",k,v), 
						graph.hasEdge(new Protein(k), new Protein(v)));

	}

}
