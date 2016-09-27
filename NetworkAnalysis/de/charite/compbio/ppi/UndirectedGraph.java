package de.charite.compbio.ppi;

import java.util.HashMap;
import java.util.LinkedHashMap;

import sonumina.math.graph.DirectedGraph;

final class VertexAttributes<VertexType>
{
	HashMap<Integer, PPIEdge<VertexType>> edges = new HashMap<Integer, PPIEdge<VertexType>>();
}
public class UndirectedGraph<VertexType> extends DirectedGraph<VertexType>
{
	private LinkedHashMap<VertexType,VertexAttributes<VertexType>> vertices;
	
	public void addEdge(PPIEdge<VertexType> e)
	{
		VertexAttributes<VertexType> vaSrc = vertices.get(e.getSource());
		VertexAttributes<VertexType> vaDest = vertices.get(e.getDest());
		
		vaSrc.edges.put(e.id, e);
		vaDest.edges.put(e.id, e);
	}
}
