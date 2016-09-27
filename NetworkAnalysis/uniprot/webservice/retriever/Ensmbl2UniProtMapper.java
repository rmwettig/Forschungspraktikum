package uniprot.webservice.retriever;

import java.util.ArrayList;
import java.util.List;

import uk.ac.ebi.kraken.interfaces.uniprot.Gene;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.description.FieldType;
import uk.ac.ebi.kraken.uuw.services.remoting.EntryIterator;
import uk.ac.ebi.kraken.uuw.services.remoting.Query;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtJAPI;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtQueryBuilder;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtQueryService;

public class Ensmbl2UniProtMapper 
{
	public static EntryIterator<UniProtEntry> mapEnsemblToUniProt(List<String> ensemblIds)
	{
		//for single entries
		//EntryRetrievalService ers = UniProtJAPI.factory.getEntryRetrievalService();
		//multiple entries for matching query
		UniProtQueryService upqs = UniProtJAPI.factory.getUniProtQueryService();
		
		Query query = UniProtQueryBuilder.buildIDListQuery(ensemblIds);
		
		EntryIterator<UniProtEntry> entries = upqs.getEntryIterator(query);
		
		for(UniProtEntry entry : entries)
			System.out.println("Entry: " + entry.getPrimaryUniProtAccession().getValue());
		
		return entries;
	}
	
	private static ArrayList<UniProtResult> fetchSingleEntry(String id, boolean onlyReviewed)
	{
		ArrayList<UniProtResult> results = new ArrayList<UniProtResult>();
		
		UniProtQueryService qs = UniProtJAPI.factory.getUniProtQueryService();
		
		Query q = UniProtQueryBuilder.buildQuery(id);
		Query revQ = UniProtQueryBuilder.setReviewedEntries(q); //only revised entries
		
		EntryIterator<UniProtEntry> result = qs.getEntryIterator(revQ);
		
		String genes;
		String acc;
		String recName;
		for(UniProtEntry e : result)
		{
			genes = "";
			acc = "";
			recName = "";
			acc = e.getPrimaryUniProtAccession().getValue(); //uniprot accession
			recName = e.getProteinDescription().getRecommendedName().getFieldsByType(FieldType.FULL).get(0).getValue(); //full prot name
			
			UniProtResult upr = new UniProtResult();
			upr.setAccession(acc);
			upr.setRecommendedName(recName);
			
			for(Gene g : e.getGenes()) //all genes
				upr.addGene(g.getGeneName().getValue());
				//genes += g.getGeneName().getValue() + " ";
			
			//System.out.println(acc);
			//System.out.println(recName); //uniprot accession
			//System.out.println(genes); //full prot name
			results.add(upr);
		}
			
		
		return results;
	}
	
	public static void main(String[] args)
	{
		List<String> list = new ArrayList<String>();

		list.add("ENSMUSP00000000001"); //uniP: q9dc51 (rev)
		list.add("ENSMUSP00000000153"); //unip: p27600 (rev)
		list.add("ENSMUSP00000001812"); //unip: p56726 (rev), q4vbd5(unrev)
		list.add("ENSMUSP00000001825"); //unip: q9d1p4 (rev)
		list.add("ENSMUSP00000002398"); //unip: q91wf3 (rev)
		list.add("ENSMUSP00000003370"); //f6und7  (unrev)
		System.out.println("Query UniProt.");
		
		List<String> list2 = new ArrayList<String>();
		list2.add("Q9DC51");
		list2.add("P27600");
		
		//mapEnsemblToUniProt(list2);
		
		//mapEnsemblToUniProt(list);
		
		for(String s: list)
			fetchSingleEntry(s, true);
	}

}
