package de.charite.compbio.ppi;
import java.util.HashMap;

import sonumina.math.graph.Edge;

public class PPIEdge<VertexType> extends Edge<VertexType>
{
	int id;
	
	private HashMap<String, Double> scores;  
	
	public PPIEdge(VertexType source, VertexType dest)
	{
		super(source, dest);
		this.scores = new HashMap<String, Double>();
		this.id = doubleHash(source, dest);
	}
	
	private int doubleHash(VertexType a, VertexType b)
	{
		return a.hashCode() * b.hashCode() + a.hashCode() + b.hashCode(); //really unique?
	}
	
	public PPIEdge(VertexType source, VertexType dest, HashMap<String, Double> scores)
	{
		super(source, dest);
		this.scores = scores;
	}

	public double GetScore(String scoreName)
	{
		return this.scores.get(scoreName);
	}
	
	public void SetScore(String scoreName, double value)
	{
		this.scores.put(scoreName, value);
	}
}
