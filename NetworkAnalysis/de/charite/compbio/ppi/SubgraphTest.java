package de.charite.compbio.ppi;

import java.util.ArrayList;

import sonumina.math.graph.DirectedGraph;
import sonumina.math.graph.Edge;
import junit.framework.TestCase;

public class SubgraphTest extends TestCase {

	public void testExtractSubgraph() 
	{
		DirectedGraph<VertexData> g = new DirectedGraph<VertexData>();
		
		VertexData[] vertices = new VertexData[10];
		for(int i = 0; i < 10; i++)
		{
			vertices[i] = new VertexData(Integer.toString(i));
			g.addVertex(vertices[i]);
		}
		/*
		 * 
		 *  0           7
		 *    \       /
		 *     3 - 5 - 8
		 *   / |       |
		 *  1  |       |
		 *     4 - 6 - 9
		 *      \ / 
		 *       2    
		 * 
		 * 
		 */
		g.addEdge(new Edge<VertexData>(vertices[0], vertices[3]) );
		g.addEdge(new Edge<VertexData>(vertices[3], vertices[0]) );
		
		g.addEdge(new Edge<VertexData>(vertices[1], vertices[3]) );
		g.addEdge(new Edge<VertexData>(vertices[3], vertices[1]) );
		
		g.addEdge(new Edge<VertexData>(vertices[2], vertices[4]) );
		g.addEdge(new Edge<VertexData>(vertices[4], vertices[2]) );
		g.addEdge(new Edge<VertexData>(vertices[2], vertices[6]) );
		g.addEdge(new Edge<VertexData>(vertices[6], vertices[2]) );
		
		g.addEdge(new Edge<VertexData>(vertices[3], vertices[4]) );
		g.addEdge(new Edge<VertexData>(vertices[4], vertices[3]) );
		g.addEdge(new Edge<VertexData>(vertices[3], vertices[5]) );
		g.addEdge(new Edge<VertexData>(vertices[5], vertices[3]) );
		
		g.addEdge(new Edge<VertexData>(vertices[4], vertices[6]) );
		g.addEdge(new Edge<VertexData>(vertices[6], vertices[4]) );
		
		g.addEdge(new Edge<VertexData>(vertices[5], vertices[7]) );
		g.addEdge(new Edge<VertexData>(vertices[7], vertices[5]) );
		g.addEdge(new Edge<VertexData>(vertices[5], vertices[8]) );
		g.addEdge(new Edge<VertexData>(vertices[8], vertices[5]) );
		
		g.addEdge(new Edge<VertexData>(vertices[6], vertices[9]) );
		g.addEdge(new Edge<VertexData>(vertices[9], vertices[6]) );
		
		g.addEdge(new Edge<VertexData>(vertices[8], vertices[9]) );
		g.addEdge(new Edge<VertexData>(vertices[9], vertices[8]) );
		
		ArrayList<VertexData> genes = new ArrayList<VertexData>();
		genes.add(vertices[2]);
		genes.add(vertices[8]);
		ArrayList<VertexData> proteins = new ArrayList<VertexData>();
		proteins.add(vertices[3]);
		proteins.add(vertices[9]);
		//DirectedGraph<VertexData> subgraph = NetworkAnalysis.extractSubgraph(g, genes, proteins);
		
	}

}
