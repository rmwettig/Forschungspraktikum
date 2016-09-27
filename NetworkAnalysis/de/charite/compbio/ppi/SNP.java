package de.charite.compbio.ppi;

/**
 * SNP class for keeping SNP information like exchanged nucleotides
 * 
 */
public class SNP 
{
	private String fromNT;
	private String toNT;
	private String functionClass;
	private String id;
	private int posOnChr;
	
	public SNP(){ };
	
	public SNP(String from, String to)
	{
		this.fromNT = from;
		this.toNT = to;
	}
	
	public void addExchangedNT(String from, String to)
	{
		this.fromNT = from;
		this.toNT = to;
	}
	
	public void setFunctionClass(String t)
	{
		this.functionClass = t;
	}
	
	public void setPositionOnChromosome(int pos)
	{
		this.posOnChr = pos;
	}
	public int getPositionOnChromosome()
	{
		return this.posOnChr;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getFromNT() {
		return fromNT;
	}

	public void setFromNT(String fromNT) {
		this.fromNT = fromNT;
	}

	public String getToNT() {
		return toNT;
	}

	public void setToNT(String toNT) {
		this.toNT = toNT;
	}

	public String getFunctionClass() {
		return functionClass;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString()
	{
		return this.fromNT + "\t" + this.toNT + "\t" + Integer.toString(this.posOnChr) + "\t" + this.functionClass;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fromNT == null) ? 0 : fromNT.hashCode());
		result = prime * result + posOnChr;
		result = prime * result + ((toNT == null) ? 0 : toNT.hashCode());
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
		SNP other = (SNP) obj;
		if (fromNT == null) {
			if (other.fromNT != null)
				return false;
		} else if (!fromNT.equals(other.fromNT))
			return false;
		if (posOnChr != other.posOnChr)
			return false;
		if (toNT == null) {
			if (other.toNT != null)
				return false;
		} else if (!toNT.equals(other.toNT))
			return false;
		return true;
	}



	
	

	
	
}
