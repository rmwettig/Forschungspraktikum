package uniprot.webservice.retriever;

import java.util.ArrayList;

public class UniProtResult
{
	String recommendedName = "";
	String upAccession = "";
	ArrayList<String> alternativeNames = new ArrayList<String>();
	ArrayList<String> genes = new ArrayList<String>();
	
	public void setRecommendedName(String name)
	{
		this.recommendedName = name;
	}
	public void setAccession(String acc)
	{
		this.upAccession = acc;
	}
	public void addGene(String g)
	{
		this.genes.add(g);
	}
	public void addAltName(String n)
	{
		this.alternativeNames.add(n);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((alternativeNames == null) ? 0 : alternativeNames.hashCode());
		result = prime * result + ((genes == null) ? 0 : genes.hashCode());
		result = prime * result
				+ ((recommendedName == null) ? 0 : recommendedName.hashCode());
		result = prime * result
				+ ((upAccession == null) ? 0 : upAccession.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UniProtResult other = (UniProtResult) obj;
		if (alternativeNames == null) {
			if (other.alternativeNames != null)
				return false;
		} else if (!alternativeNames.equals(other.alternativeNames))
			return false;
		if (genes == null) {
			if (other.genes != null)
				return false;
		} else if (!genes.equals(other.genes))
			return false;
		if (recommendedName == null) {
			if (other.recommendedName != null)
				return false;
		} else if (!recommendedName.equals(other.recommendedName))
			return false;
		if (upAccession == null) {
			if (other.upAccession != null)
				return false;
		} else if (!upAccession.equals(other.upAccession))
			return false;
		return true;
	}
	
	
}
