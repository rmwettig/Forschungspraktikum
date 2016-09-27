package de.charite.compbio.ppi;

import java.util.ArrayList;
import java.util.HashMap;

public class Protein
{
	private int id;
	private String ensemblProteinId = "";
	private String ensemblGeneId = "";
	private String ensemblTranscriptId = "";
	private String uniprotAcc = "";
	private Gene gene;
	private HashMap<String,ArrayList<TwoDGelData>> gelData; //mapping for consomic strain onto all observed spots
	private HashMap<String,String> go;
	
	private double cc;
	private double nc;
	private double tc;
	private int deg;
	
	public double getCc() {
		return cc;
	}

	public void setCc(double cc) {
		this.cc = cc;
	}

	public double getNc() {
		return nc;
	}

	public void setNc(double nc) {
		this.nc = nc;
	}

	public double getTc() {
		return tc;
	}

	public void setTc(double tc) {
		this.tc = tc;
	}

	public int getDeg() {
		return deg;
	}

	public void setDeg(int deg) {
		this.deg = deg;
	}
	
	public boolean hasGO()
	{
		if(go != null)
			return true;
		return false;
	}
	
	public HashMap<String, String> getGo() {
		return go;
	}

	public void setGo(HashMap<String, String> go) {
		this.go = go;
	}

	public Protein(){ };
	
	public Protein(String ensemblProtId)
	{
		setEnsemblProteinId(ensemblProtId);
//		this.ensemblProteinId = ensemblProtId;
//		this.id = Integer.parseInt(ensemblProtId.substring(ensemblProtId.indexOf("P") + 1) );
	}
	public void setEnsemblProteinId(String id)
	{
		this.ensemblProteinId = id;
		this.id = Integer.parseInt(id.substring(id.indexOf("P") + 1) );
	}
	public String getEnsemblProteinId()
	{
		return this.ensemblProteinId;
	}
	public void setGene(Gene g)
	{
		this.gene = g;
	}
	public boolean hasGene()
	{
		if(gene != null)
			return true;
		return false;
	}
	public String getEnsemblGeneId() {
		return ensemblGeneId;
	}

	public void setEnsemblGeneId(String ensemblGeneId) {
		this.ensemblGeneId = ensemblGeneId;
	}

	public String getEnsemblTranscriptId() {
		return ensemblTranscriptId;
	}

	public void setEnsemblTranscriptId(String ensemblTranscriptId) {
		this.ensemblTranscriptId = ensemblTranscriptId;
	}

	public String getUniprotAcc() {
		return uniprotAcc;
	}

	public void setUniprotAcc(String uniprotAcc) {
		this.uniprotAcc = uniprotAcc;
	}

	public HashMap<String, ArrayList<TwoDGelData>> getGelData() {
		return gelData;
	}

	public void setGelData(HashMap<String, ArrayList<TwoDGelData>> gelData) {
		this.gelData = gelData;
	}

	public Gene getGene() {
		return gene;
	}

	public boolean has2DGelData()
	{
		if(gelData != null)
			return true;
		return false;
	}
	public String getGeneAnnotationAsString()
	{
		if(this.gene != null)
			return this.gene.toString();
		return "NO_GENE";
	}
	
	public String get2dGelAnnotationAsString()
	{
		if(this.gelData != null)
		{
			String line = "";
			for(String key : gelData.keySet())
			{
				line = key + "\t";
				for(int i = 0; i < gelData.get(key).size(); i++)
					line += gelData.get(key).get(i).toString() + "\t";

			}	
			line += "\t";
			return line;
		}
		return "NO_GEL_DATA";
	}
	
	@Override
	public String toString()
	{
		if(this.gene != null)
			return this.ensemblProteinId + "\t" + this.gene.toString();
		return this.ensemblProteinId + "\t" + "NO_GENE";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Protein other = (Protein) obj;
		if (id != other.id)
			return false;
		return true;
	}


	
	
}
