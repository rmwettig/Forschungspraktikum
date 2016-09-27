package de.charite.compbio.ppi;

import java.util.ArrayList;

/**
 * Gene class that holds relevant information about a gene like name, position on a chromosome, chromosome number and occuring SNPs 
 *
 */
public class Gene 
{
	private String name;
	private String chromosome;
	private ArrayList<SNP> snps = new ArrayList<SNP>();
	
	public Gene(){ };
	
	public Gene(String name)
	{
		this.name = name;
	}
	
	public void addAnnotation(String chromo, SNP snp)
	{
		this.chromosome = chromo;
		this.snps.add(snp);
	}
	
	public void addSNP(SNP snp)
	{
		this.snps.add(snp);
	}
	
	public void setChromosome(String num)
	{
		this.chromosome = num;
	}
	public String getChromosome()
	{
		return this.chromosome;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<SNP> getSnps() {
		return snps;
	}

	public void setSnps(ArrayList<SNP> snps) {
		this.snps = snps;
	}

	public boolean hasSNP()
	{
		if(snps.size() > 0)
			return true;
		return false;
	}
	
	@Override
	public String toString()
	{
		StringBuilder repr = new StringBuilder(this.name + "\t");
		if(this.snps.size() > 0)
		{
			repr.append(this.chromosome + "\t");
			for(SNP s : this.snps)
				repr.append(s.toString() + "\t");
		}
		else
			repr.append("NO_SNP" + "\t");
		return repr.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Gene other = (Gene) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
}
