import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import uk.ac.ebi.kraken.interfaces.uniprot.Gene;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.description.FieldType;
import uk.ac.ebi.kraken.uuw.services.remoting.EntryIterator;
import uk.ac.ebi.kraken.uuw.services.remoting.Query;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtJAPI;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtQueryBuilder;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtQueryService;
import uniprot.webservice.retriever.UniProtResult;


public class MGItoUniProttoStringDB 
{
	/**
	 * A method to extract gene names from the MGI SNP file and saves it to a text file.
	 * @param file: Path to the MGI SNP file.
	 * @param outFile: name of the output file.
	 * @return list of gene names in the SNP file.
	 */
	public static ArrayList<String> extractGenesFromMGI(String file, String outFile)
	{
		ArrayList<String> geneNames = new ArrayList<String>();
		try
		{
			BufferedReader reader = new BufferedReader( new FileReader(file) );
			BufferedWriter writer = new BufferedWriter( new FileWriter(outFile) );

			String line = reader.readLine(); //remove header
			writer.write(line + "\n");

			String[] data;
			String[] tmp;

			String functionClass = "", gene = "", variationType = "", id ="", posOnChr = "",
					assays = "", chromosome = "", readingStrandOrientation = "", refNT = "", strainNT = "";
			String newLine = "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n";

			while( (line = reader.readLine() ) != null)
			{
				data = line.split("\t");

				id = data[0].substring(data[0].indexOf("s") + 1);

				chromosome = data[1].substring(data[1].lastIndexOf("r") + 1, data[1].lastIndexOf(":") - 1);
				posOnChr = data[1].substring(data[1].lastIndexOf(":") + 1);

				readingStrandOrientation = data[2];

				tmp = data[3].split(":");
				gene = tmp[0].trim();
				functionClass = tmp[1].trim();
				geneNames.add(gene);

				assays = data[4];

				variationType = data[5];

				refNT = data[6];
				strainNT = data[7];

				writer.write(
						String.format(newLine, id,chromosome,posOnChr,readingStrandOrientation, gene, functionClass, assays, refNT, strainNT)
						);
			}
			reader.close();
			writer.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return geneNames;
	}

	/**
	 * This method queries the UniProt database in order to get the gene products of the specified gene name list. Only revised entries are processed.
	 * @param geneNames a list of gene names which should be search in the UniProt DB
	 * @return a hashmap that maps a gene to all of its products
	 * @throws IOException 
	 */
	public static HashMap<String, ArrayList<UniProtResult>> getUniProtData(ArrayList<String> geneNames, String outFile) throws IOException
	{
		//does not consider alternative names
		
		HashMap<String, ArrayList<UniProtResult>> geneToProductMap = new HashMap<String, ArrayList<UniProtResult>>();
		UniProtQueryService qs = UniProtJAPI.factory.getUniProtQueryService();
		Query q;
		Query revQ;
		String genes;
		String acc;
		String recName;
		ArrayList<String> altNames = new ArrayList<String>();
		ArrayList<UniProtResult> uprList = new ArrayList<UniProtResult>();
		
		BufferedWriter writer = new BufferedWriter( new FileWriter(outFile) );
		
		String geneLine, altNamesLine;
		
		for(String name : geneNames)
		{
			q = UniProtQueryBuilder.buildGeneNameQuery(name);
			revQ = UniProtQueryBuilder.setReviewedEntries(q); //only revised entries

			EntryIterator<UniProtEntry> result = qs.getEntryIterator(revQ);

			for(UniProtEntry e : result)
			{
				//check for species
				if(e.getOrganism().getCommonName().getValue().equalsIgnoreCase("mouse"))
				{
					UniProtResult upr = new UniProtResult();
					genes = "";
					acc = "";
					recName = "";
					geneLine = "";
					altNamesLine = "";
					altNames.clear();
					
					acc = e.getPrimaryUniProtAccession().getValue(); //uniprot accession
					recName = e.getProteinDescription().getRecommendedName().getFieldsByType(FieldType.FULL).get(0).getValue(); //full prot name
					for(int i = 0; i < e.getProteinDescription().getAlternativeNames().size(); i++)
					{
						String na = e.getProteinDescription().getAlternativeNames().get(i).getFieldsByType(FieldType.FULL).get(0).getValue();
						upr.addAltName(na);
						altNamesLine += na + "\t";
					}
					
					upr.setAccession(acc);
					upr.setRecommendedName(recName);

					for(Gene g : e.getGenes()) //all genes
					{
						String ge = g.getGeneName().getValue();
						upr.addGene(ge);
						geneLine += ge + "\t";
					}
					
					writer.write(name + "\t" + acc + "\t" + recName + "\t" + altNamesLine + "\t" + "#" +"\t" + geneLine);	
					uprList.add(upr);
				}
			}
			if(!geneToProductMap.containsKey(name))
				geneToProductMap.put(name, uprList);
			else
			{
				ArrayList<UniProtResult> temp = geneToProductMap.get(name);
				for(UniProtResult r : uprList)
					if(!temp.contains(r))
						temp.add(r);
			}
		}
		writer.close();
		
		return geneToProductMap;
	}
	
	/**
	 * Searches the product names in the STRING DB aliases file to combine the ENSEMBL identifier with the corresponding UniProt accession identifier.
	 * @param data HashMap that contains the genes and their products.
	 * @param inFile Path to the STRING DB aliases file.
	 * @param outFile Path to the result file.
	 */
	public static void combineEnsemblUniProt(HashMap<String, ArrayList<UniProtResult>> data, String inFile, String outFile)
	{
		try
		{
			BufferedReader reader = new BufferedReader( new FileReader(inFile) );
			BufferedWriter writer = new BufferedWriter( new FileWriter(outFile) );
			
			reader.readLine(); // remove header
			writer.write("ENSEMBL_ID\tUniProt_Accession\tGeneName\n");
			
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
