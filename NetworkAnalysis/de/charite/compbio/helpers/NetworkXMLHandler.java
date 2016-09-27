package de.charite.compbio.helpers;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import sonumina.math.graph.DirectedGraph;
import de.charite.compbio.ppi.*;

public class NetworkXMLHandler extends DefaultHandler
{
	DirectedGraph<Protein> g = new DirectedGraph<Protein>();
	
	//tags
	String tagProteins = "Proteins";
	String tagEdges = "Edges";
	String tagEdge = "Edge";
	String tagProtein = "Protein";
	String tagGenes = "Genes";
	String tagGene = "Gene";
	String tagSNPs = "SNPs";
	String tagSNP = "SNP";
	String tagSource = "Source";
	String tagDestination = "Destination";
	String tagWeight = "Weight";
	
	//attributes
	String attrGeneName = "UniProt_GeneName";
	String attrGeneChromo = "Chromosome";
	String attrSNPid = "id";
	String attrSNPfrom = "from";
	String attrSNPto = "to";
	String attrSNPposition = "position";
	String attrProteinEnsemblId = "ensemblId";
	
	//inside flags
	boolean inProteins = false;
	boolean inEdges = false;
	boolean inProtein = false;
	boolean inGenes = false;
	boolean inGene = false;
	boolean inSNPs = false;
	boolean inSNP = false;
	boolean inSource = false;
	boolean inDestination = false;
	boolean inWeight = false;
	boolean inEdge = false;
	
	Protein p;
	Gene gene;
	SNP snp;
	Protein src;
	Protein dest;
	int edgeWeight;
	
	public void startDocument() throws SAXException
	{
	
	}

	public void startElement(String uri, String localName, String qname, Attributes attr) throws SAXException
	{		
		if(localName.equalsIgnoreCase(tagProteins))
			inProteins = true;
		else if(localName.equalsIgnoreCase(tagProtein))
		{
			inProtein = true;
			p = new Protein(attr.getValue(attrProteinEnsemblId));
		}
		else if(localName.equalsIgnoreCase(tagGenes))
		{
			inGenes = true;
			gene = new Gene(attr.getValue(attrGeneName));
			gene.setChromosome(attr.getValue(attrGeneChromo));
		}
		else if(localName.equalsIgnoreCase(tagGene))
			inGene = true;
		else if(localName.equalsIgnoreCase(tagSNPs))
			inSNPs = true;
		else if(localName.equalsIgnoreCase(tagSNP))
		{
			inSNP = true;
			snp = new SNP();
			snp.addExchangedNT(attr.getValue(attrSNPfrom), attr.getValue(attrSNPto));
			snp.setPositionOnChromosome(Integer.parseInt(attr.getValue(attrSNPposition)));
			snp.setId(attr.getValue(attrSNPid));
		}
		else if(localName.equalsIgnoreCase(tagEdge))
			inEdge = true;
		else if(localName.equalsIgnoreCase(tagEdges))
			inEdges = true;
		else if(localName.equalsIgnoreCase(tagSource))
			inSource = true;
		else if(localName.equalsIgnoreCase(tagDestination))
			inDestination = true;
		else if(localName.equalsIgnoreCase(tagWeight))
			inWeight = true;
	}
	
	public void endElement(String uri, String localName, String qname) throws SAXException
	{
		if(localName.equalsIgnoreCase(tagProteins))
			inProteins = false;
		else if(localName.equalsIgnoreCase(tagProtein))
		{
			inProtein = false;
			g.addVertex(p);
		}
		else if(localName.equalsIgnoreCase(tagGenes))
			inGenes = false;
		else if(localName.equalsIgnoreCase(tagGene))
		{
			inGene = false;
			p.setGene(gene);
		}
		else if(localName.equalsIgnoreCase(tagSNPs))
			inSNPs = false;
		else if(localName.equalsIgnoreCase(tagSNP))
		{
			inSNP = false;
			gene.addSNP(snp);
		}
		else if(localName.equalsIgnoreCase(tagEdge))
		{
			inEdge = false;
			if(!g.hasEdge(src, dest))
			{
				g.addEdge( new PPIEdge<Protein>(src, dest) );
				g.addEdge( new PPIEdge<Protein>(dest, src) );
			}
		}
		else if(localName.equalsIgnoreCase(tagEdges))
			inEdges = false;
		else if(localName.equalsIgnoreCase(tagSource))
			inSource = false;
		else if(localName.equalsIgnoreCase(tagDestination))
			inDestination = false;
		else if(localName.equalsIgnoreCase(tagWeight))
			inWeight = false;
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		String s = new String(ch);
		if(inSource)
			src = new Protein(s);
		else if(inDestination)
			dest = new Protein(s);
		else if(inWeight)
			edgeWeight = Integer.parseInt(s);
	}
	
	public DirectedGraph<Protein> getGraph()
	{
		return this.g;
	}
	
}
