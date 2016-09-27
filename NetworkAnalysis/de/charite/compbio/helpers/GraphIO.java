package de.charite.compbio.helpers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import sonumina.math.graph.DirectedGraph;
import sonumina.math.graph.Edge;
import de.charite.compbio.ppi.AnalysisResults;
import de.charite.compbio.ppi.Gene;
import de.charite.compbio.ppi.PPIEdge;
import de.charite.compbio.ppi.Protein;
import de.charite.compbio.ppi.SNP;
import de.charite.compbio.ppi.TwoDGelData;

public class GraphIO 
{

	//Tag definitions for graph text file output
	public final static String tagSource = "[SOURCE]";
	public final static String tagTargets = "[TARGETS]";
	public final static String tagMappings = "[MAPPINGS]";
	public final static String tagSNP = "[SNP]";
	public final static String tag2dGel = "[2DGEL]";
	public final static String tagStatistics = "[STATISTICS]";
	public final static String outputSNPFormat = "%s\t%s\t%s\t%s\t%s\n";
	public final static String output2DGelFormat = "%s\t%d\t%.2f\t%s\t%s\n";
	public final static String outputMappingsFormat = "%s\t%s\t%s\t%s\t%s\n";
	//tags for the network setup file
	private final static String sectionSettings = "[SETTINGS]";
	private final static String sectionProtein = "[PROTEINS]";
	private final static String sectionGO = "[GO]";
	private final static String sectionGenes = "[GENES]";
	private final static String sectionSNPs = "[SNPS]";
	private final static String sectionGel = "[GEL]";
	private final static HashMap<String,String> delimiterMap;
	static
	{
		delimiterMap = new HashMap<String, String>();
		delimiterMap.put("t","\t"); 
		delimiterMap.put("w"," ");	
	}
	
	/**
	 * Reads network sources and settings from a setup file.
	 * @param g an empty graph
	 * @param ar an empty AnalysisResults object
	 * @param file path to the setup file
	 * @return
	 */
	public static HashSet<String> readNetworkSetupFile(DirectedGraph<Protein> g, AnalysisResults ar, String file)
	{
		HashSet<String> strains = null;
		try
		{
			BufferedReader reader = new BufferedReader( new FileReader(file) );
			String line;
			String activeSection = "";
			HashMap<String, HashMap<String,HashSet<String>>> sectionKeyValuePairs = new HashMap<String, HashMap<String,HashSet<String>>>();
			while( (line = reader.readLine()) != null )
			{
				//ignore comments
				if(line.charAt(0) == '#')
					continue;
				//get active section
				if(line.charAt(0) == '[')
				{
					activeSection = line.trim();
					sectionKeyValuePairs.put(activeSection, new HashMap<String, HashSet<String>>());
					continue;
				}
				//get keys and values
				String[] keyAndValue = line.trim().split("=");				
				if(!keyAndValue[0].equalsIgnoreCase("consomic"))
				{
					HashSet<String> value = new HashSet<String>();
					value.add(keyAndValue[1]);
					sectionKeyValuePairs.get(activeSection).put(keyAndValue[0], value);
				}
				else
				{
					HashSet<String> value = new HashSet<String>();
					for(String s : keyAndValue[1].split(","))
						value.add(s);
					sectionKeyValuePairs.get(activeSection).put(keyAndValue[0], value);
				}
			}
			reader.close();
			
			ar.setBinSize( Float.parseFloat(sectionKeyValuePairs.get(sectionSettings).get("sampling_bin_size").iterator().next()) );
			ar.setSamplingRepetitions( Integer.parseInt(sectionKeyValuePairs.get(sectionSettings).get("sampling_repetitions").iterator().next()) );
			ar.setGender( sectionKeyValuePairs.get(sectionSettings).get("gender").iterator().next() );
			ar.setSpecies( sectionKeyValuePairs.get(sectionSettings).get("species").iterator().next() );
			ar.setScoreThreshold( Double.parseDouble( sectionKeyValuePairs.get(sectionSettings).get("edge_threshold").iterator().next() ) );
			
			loadProteinsFromFile(g, sectionKeyValuePairs.get(sectionProtein).get("file").iterator().next(),
									ar.getSpecies(),
									delimiterMap.get( sectionKeyValuePairs.get(sectionProtein).get("delimiter").iterator().next() ),
									Boolean.parseBoolean( sectionKeyValuePairs.get(sectionProtein).get("header").iterator().next() ),
									ar.getScoreThreshold());
			
			loadGO(g, sectionKeyValuePairs.get(sectionGO).get("file").iterator().next(), 
					  delimiterMap.get( sectionKeyValuePairs.get(sectionGO).get("delimiter").iterator().next() ),
					  Boolean.parseBoolean( sectionKeyValuePairs.get(sectionGO).get("header").iterator().next() ) );
			
			loadGenesFromFile(g, sectionKeyValuePairs.get(sectionGenes).get("file").iterator().next(),
								 delimiterMap.get( sectionKeyValuePairs.get(sectionGenes).get("delimiter").iterator().next() ),
								 Boolean.parseBoolean( sectionKeyValuePairs.get(sectionGenes).get("header").iterator().next() ) );
			
			loadSNPFromFile(g, sectionKeyValuePairs.get(sectionSNPs).get("file").iterator().next(), 
					  		   delimiterMap.get( sectionKeyValuePairs.get(sectionSNPs).get("delimiter").iterator().next() ),
					  		   Boolean.parseBoolean( sectionKeyValuePairs.get(sectionSNPs).get("header").iterator().next() ) );
			
			strains = sectionKeyValuePairs.get(sectionSettings).get("consomic");
			load2dGEdata(g,
					sectionKeyValuePairs.get(sectionGel).get("file").iterator().next(),
					delimiterMap.get( sectionKeyValuePairs.get(sectionGel).get("delimiter").iterator().next() ), strains, 
					Boolean.parseBoolean( sectionKeyValuePairs.get(sectionGel).get("header").iterator().next() ) );
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return strains;
	}
	
	/**
	 * loads a network from a STRING DB interaction file into an empty graph.
	 * @param g Graph
	 * @param file STRING DB interaction file
	 * @param species organism from which the proteins should originate
	 * @param delimiter symbol that separates the entries in a row
	 * @param hasHeader set to true if the column titles should be skipped.
	 * @param scoreThreshold threshold that the string db combined score must exceed to include the edge
	 */
	public static void loadProteinsFromFile(DirectedGraph<Protein> g, String file, String species, String delimiter, boolean hasHeader, double scoreThreshold)
	{
		//column indices for scores:
		final int NEIGHBOURHOOD_SCORE_IDX = 2;//2: neighbourhood
		final int FUSION_SCORE_IDX = 3;//3: fusion
		final int COOCCURENCE_SCORE_IDX = 4;//4: cooccurence
		final int COEXPRESSION_SCORE_IDX = 5;//5: coexpression
		final int EXPERIMENTAL_SCORE_IDX = 6;//6: experimental
		final int DATABASE_SCORE_IDX = 7; //7: database
		//8: textmining
		//9: combined_score (von Mering, et al Nucleic Acids Res. 2005) S = 1 - product over all scores (1 - S_i)
		System.out.println(String.format("Creating network from %s.", file.substring(file.lastIndexOf("\\")+1)));
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
	
			if(hasHeader)
				reader.readLine(); //remove header
	
			String line = "";
	
			boolean streamNotEmpty = true;
	
			while (streamNotEmpty)
			{					
				line = reader.readLine();
	
				if(line == null)
				{
					streamNotEmpty = false;
					break;
				}
	
				if(line.contains(species))
				{
					String[] data = GraphIO.ProcessDataString(line, delimiter);
					Protein source = new Protein(data[0]);
					Protein target = new Protein(data[1]);
	
					g.addVertex(source);
					g.addVertex(target);
	
					if(Double.parseDouble(data[2]) >= scoreThreshold && !g.hasEdge(source, target))
					{
						PPIEdge<Protein> source2target = new PPIEdge<Protein>(source, target);
						source2target.SetScore("Combined", Double.parseDouble(data[2]));
						g.addEdge( source2target );
						PPIEdge<Protein> target2source = new PPIEdge<Protein>(target, source);
						target2source.SetScore("Combined", Double.parseDouble(data[2]) );
						g.addEdge( target2source );
					}
				}
			}
			reader.close();
			System.out.println(String.format("Network was successfully created (%d nodes, %d edges).",
					g.getNumberOfVertices(),g.getNumberEdges()/2)
					);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Simple function to extract the ENSEMBL protein identifiers and the scores from the STRING DB file.
	 * Excludes the textmining score
	 * @param s a line from STRING DB file
	 * @param delimiter
	 * @return
	 */
	public static String[] ProcessDataString(String s, String delimiter)
	{
		String[] data = s.split(delimiter);
		String[] result = new String[3];
		//extract the ENSEMBL protein identifyer
		result[0] = data[0].substring(data[0].lastIndexOf(".")+1);
		result[1] = data[1].substring(data[1].lastIndexOf(".")+1);
		
		double combinedScoreWithoutTextmining = 0.0;
		double scoreProduct = 1.0;
		for(int i = 2; i < 8; i++)
		{
			String score = data[i].trim();
			scoreProduct = scoreProduct * ( 1.0 - (Double.parseDouble(score)/(double) 1000.0) );
		}
		combinedScoreWithoutTextmining = 1.0 - scoreProduct;
		result[2] = Double.toString(combinedScoreWithoutTextmining);
		return result;
	}
	
	public static void loadGO(DirectedGraph<Protein> g, String file, String delimiter, boolean hasHeader)
	{
		try
		{
			System.out.printf("Loading GO terms from %s\n", file);
			BufferedReader reader = new BufferedReader( new FileReader(file) );

			if(hasHeader)
				reader.read();
			
			String line;
			HashMap<String, HashMap<String,String>> goCollector = new HashMap<String, HashMap<String,String>>();
			int totalGOterms = 0;
			int assignedGOterms = 0;
			while( (line = reader.readLine()) != null)
			{
				String[] data = line.split(delimiter);
				if(data.length > 3)
				{
					++totalGOterms;
					String protein = data[2];
					String goTermAcc = data[3];
					String goTermName = data[4];
					String goDomain = data[5];
					HashMap<String,String> go = new HashMap<String,String>();
					go.put("Accession", goTermAcc);
					go.put("Name", goTermName);
					go.put("Domain", goDomain);
					
					goCollector.put(protein, go);
				}
			}
			for(Protein p : g.getVertices())
			{
				if(goCollector.containsKey(p.getEnsemblProteinId()))
				{
					p.setGo(goCollector.get(p.getEnsemblProteinId()));
					++assignedGOterms;
				}
			}
			reader.close();
			System.out.printf("%d of %d GO-Terms assigned (%.2f%%)\n", assignedGOterms, totalGOterms, ((float)assignedGOterms/totalGOterms) * 100f);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * loads genes into an existing protein network from a biomart Ensembl-UniProt mapping file. Biomart returns its gene names in uppercase letters.
	 * @param g Graph
	 * @param file filename
	 * @param delimiter separation symbol
	 * @param hasHeader set to true if the file has a header
	 */
	public static void loadGenesFromFile(DirectedGraph<Protein> g, String file, String delimiter, boolean hasHeader)
	{
		try
		{
			System.out.println(String.format("Loading genes from %s", file.substring(file.lastIndexOf("\\")+1)));
			BufferedReader reader = new BufferedReader( new FileReader(file) );
			if(hasHeader)
				reader.readLine(); //remove header
			String line = "";
			String[] map;
			int mapCounter = 0;
	
			while( (line = reader.readLine()) != null)
			{
				if(!line.equals(""))
				{
					map = line.split(delimiter);
			/*		if(map.length < 2)
						continue;*/
	
					Protein p = new Protein(map[0]);
					for(Protein pr : g.getVertices())
						if(pr.equals(p))
						{
							if(!map[1].equals(""))
							{
								Gene ge = new Gene(map[1].toUpperCase());
								if(!map[5].equals(""))
									ge.setChromosome(map[5]);
								pr.setGene(ge);
							}
							else
								if(!map[5].equals(""))
								{
									Gene ge = new Gene();
									ge.setChromosome(map[5]);
									pr.setGene(ge);
								}
							if(!map[2].equals(""))
								pr.setUniprotAcc(map[2]);
							if(!map[3].equals(""))
								pr.setEnsemblGeneId(map[3]);
							if(!map[4].equals(""))
								pr.setEnsemblTranscriptId(map[4]);
							++mapCounter;
							break;
						}
				}
			}
	
			reader.close();
			System.out.println(String.format("Found %d Ensembl - UniProt mappings (%.1f%% of nodes).", mapCounter, ((float)mapCounter/g.getNumberOfVertices())*100));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * loads SNPs into an existing protein network from an MGI snp file. MGI returns its gene names in lowercase letters, therefore these are converted to uppercase letters for matching with Biomart.
	 * @param g Graph
	 * @param file filename
	 * @param delimiter separation symbol
	 * @param hasHeader set to true if the file has a header
	 */
	public static void loadSNPFromFile(DirectedGraph<Protein> g, String file, String delimiter, boolean hasHeader)
	{
		try
		{
			System.out.println(String.format("Loading SNPs from %s", file.substring(file.lastIndexOf("\\")+1)));
			BufferedReader reader = new BufferedReader( new FileReader(file) );
			if(hasHeader)
				reader.readLine(); //remove header
			String line = "";
	
			String[] data;
			String snpId, chromosome, posOnChr, readingStrandOrientation, geneName, functionClass, assays, variationType, refNT, strainNT;
			int assignedSNPs = 0;
			int total = 0;
			while( (line = reader.readLine()) != null)
			{				
				data = line.split(delimiter);
	
				snpId = data[0];//.substring(data[0].indexOf("s") + 1);
				chromosome = data[1].substring(data[1].lastIndexOf("r") + 1, data[1].lastIndexOf(":"));
				posOnChr = data[1].substring(data[1].lastIndexOf(":") + 1);
				readingStrandOrientation = data[2];
	
				String[] subdata = data[3].split(":");
				geneName = subdata[0].trim();
				functionClass = subdata[1].trim();
	
				assays = data[4];
				variationType = data[5];
				refNT = data[6];
				strainNT = data[7];
	
				SNP snp = new SNP();
				snp.addExchangedNT(refNT, strainNT);
				snp.setFunctionClass(functionClass);
				snp.setPositionOnChromosome(Integer.parseInt(posOnChr));
				snp.setId(snpId);
				Gene newGene = new Gene(geneName.toUpperCase());
				++total;
				//newGene.addAnnotation(chromosome, snp);
				//System.out.println(String.format("ref: %s, strain: %s", refNT, strainNT));
				
				for(Protein p : g.getVertices())
				{
					Gene gen = p.getGene();
					if(gen == null)
						continue;
					//if the gene name from biomart import matches the gene name in the MGI snp file
					if(gen.equals(newGene))
					{
						//gen.addAnnotation(posOnChr, chromosome, snp);
						//newGene.addAnnotation(chromosome, snp);
						if(p.getGene().getChromosome() == null)
							p.getGene().setChromosome(chromosome);
						p.getGene().addSNP(snp);
						//System.out.println(gen.toString());
						++assignedSNPs;
					}
				}
			}
			reader.close();
			System.out.println(String.format("%d SNPs of %d were assigned (%.1f%%).", assignedSNPs, total, ((float) assignedSNPs/total)*100));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
		 * loads 2D gelectrophoresis data
		 * @param g the graph which should be enriched
		 * @param gelData 2D gel electrophoresis spot measurements
		 * @param gelDelimiter
		 * @param gelHasHeader
		 */
		public static void load2dGEdata(DirectedGraph<Protein> g, String gelData, String gelDelimiter, HashSet<String> consomicStrains, boolean gelHasHeader)
		{
			//removed char gender from signature 
			try
			{
				final int CONSOMIC_IDX = 0;
				final int SPOTNR_IDX = 1;
				
				System.out.println(String.format("Loading 2D GE data from %s", gelData.substring(gelData.lastIndexOf("\\")+1)));
				BufferedReader reader = new BufferedReader( new FileReader(gelData) );
				reader.readLine();
				String line = reader.readLine();
				String[] data = line.split(gelDelimiter);
	
				if(gelHasHeader)
					while(!Character.isDigit(data[0].charAt(0)))
					{
						line = reader.readLine();
						data = line.split(gelDelimiter);
					}
				//mapping gene onto mapping strain onto changes
				HashMap<String,HashMap<String, ArrayList<TwoDGelData>>> gene2strains = new HashMap<String, HashMap<String, ArrayList<TwoDGelData>>>();
				HashMap<String, ArrayList<TwoDGelData>> strain2Changes;
				ArrayList<TwoDGelData> temp;
				
				while( line != null )
				{
					data = line.split(gelDelimiter);
					
					if(line.trim().equals("")) //skip empty tab lines
					{
						line = reader.readLine();
						data = line.split(gelDelimiter);
						continue;
					}
					//TODO generalize SNP extraction part
					//this processing is particularly designed for C12 and C14 CS !
					if(data[1].lastIndexOf("_") == data[1].indexOf("_"))
					{
						//System.out.println(data[1]);
						String[] consomicSpotnr = data[1].split("_");
						if(consomicStrains.contains(consomicSpotnr[CONSOMIC_IDX].toUpperCase()) || consomicStrains.contains("ALL") )
						//if(consomicSpotnr[CONSOMIC_IDX].equalsIgnoreCase("C12") || consomicSpotnr[CONSOMIC_IDX].equalsIgnoreCase("C14"))
						{
							TwoDGelData tdgd = new TwoDGelData();
							tdgd.setConsomic(consomicSpotnr[CONSOMIC_IDX]);
							if(consomicSpotnr.length > 1)
								tdgd.setSpotNr(Integer.parseInt(consomicSpotnr[SPOTNR_IDX]));
							//tdgd.setGender(gender);
							
							try
							{
								tdgd.setRatio(Double.parseDouble(data[2].replace(',', '.')));
								if(tdgd.getRatio() > 1)
								{
									tdgd.setChangeDirection('u');
									tdgd.setTranslationHasChanged(true);
								}
								else if(tdgd.getRatio() < 1)
								{
									tdgd.setChangeDirection('d');
									tdgd.setTranslationHasChanged(true);
								}
								else
									tdgd.setChangeDirection('n');
							}
							catch(NumberFormatException nfe)
							{
								tdgd.setRatio(-1);
							}
							
							/*
							 * if the gene2strain map does not contain the current ENSEMBL gene identifier 
							 * 		create a new strain2change map
							 * otherwise
							 * 		get the existing map
							 */
							if(!gene2strains.containsKey(data[6]))
								strain2Changes = new HashMap<String,ArrayList<TwoDGelData>>();			
							else 
								strain2Changes = gene2strains.get(data[6]);
							
							/*
							 * if the strain2change map does not contain the current consomic strain key
							 * 		create a new list for the associated changes
							 * otherwise
							 * 		get the list of the changes already recorded and add the new one
							 */
							if(!strain2Changes.containsKey(consomicSpotnr[0]))
							{
								temp = new ArrayList<TwoDGelData>();
								temp.add(tdgd);
								strain2Changes.put(consomicSpotnr[0].toUpperCase(), temp);
								gene2strains.put(data[6], strain2Changes);
							}
							else
							{
								temp = strain2Changes.get(consomicSpotnr[0].toUpperCase());
								temp.add(tdgd);
							}
							
							
							//tdgd.geneName = data[3];
							//tdgd.chromosome = data[4];
						}
					}
					line = reader.readLine();
				}
				reader.close();
				int assignedGEData = 0;
				
				//ArrayList<String> foundKeys = new ArrayList<String>(); //debug stuff
				
				for(Protein p : g.getVertices())
				{
	/*				if(p.ensemblProteinId.equals("ENSMUSP00000130979"))
						System.out.println("ENSMUSP00000130979 (ENSMUSG00000079012) found");*/
					
					if(gene2strains.containsKey(p.getEnsemblGeneId()))
					{
						p.setGelData(gene2strains.get(p.getEnsemblGeneId()));
						//foundKeys.add(p.ensemblGeneId);
						++assignedGEData;
					}
				}
	/*			for(String s : gene2strains.keySet())
					if(!foundKeys.contains(s))
						System.out.println("Not found: " + s);*/
				
				System.out.println(String.format("%d of %d gel data entries assigned.", assignedGEData, gene2strains.size()));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

	public static void simpleInteractionSaver(DirectedGraph<Protein> g, String file, HashSet<Protein> snpGenes, HashSet<Protein> changedProteins)
	{
		try
		{
			BufferedWriter writer = new BufferedWriter( new FileWriter(file) );
			writer.write("Source\tTarget\n");
			for(Protein source : g.getVertices())
			{
				String src = source.getEnsemblProteinId();
				Iterator<Protein> children = g.getChildNodes(source);
				String labSrc = "";
				if(snpGenes.contains(source))
					labSrc = "G";
				else if(changedProteins.contains(source))
					labSrc = "P";
				if(children.hasNext())
				{
					while(children.hasNext())
					{
						String labdest = "";
						Protein dest = children.next();
						
						if(snpGenes.contains(dest))
							labdest = "G";
						else if(changedProteins.contains(dest))
							labdest = "P";
						writer.write(src + "\t" + dest.getEnsemblProteinId() + "\t" + labSrc + "\t" + labdest + "\n");
					}
				}
				else
					writer.write(src +"\t\t" + labSrc + "\t" + "\n");
			}
			writer.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
		 * Saves the given network as text file (more human readable).
		 * @param g
		 * @param filename
		 */
		public static void saveNetworkToFile(DirectedGraph<Protein> g, String filename)
		{
			try
			{
				System.out.println(String.format("Saving network to %s", filename.substring(filename.lastIndexOf("\\")+1)));
				BufferedWriter writer = new BufferedWriter( new FileWriter(filename) );
				String upGeneName = "";
				String geneChromosome = "";
				for(Protein p : g.getVertices())
				{
					writer.write(GraphIO.tagSource + "\n");
					writer.write(p.getEnsemblProteinId() + "\n");
					writer.write(tagStatistics +"\n");
					writer.write("Clustering_Coefficient\t" + p.getCc() + "\n");
					writer.write("Degree\t" + p.getDeg() + "\n");
					writer.write("Neighbourhood_Connectivity\t" + p.getNc() + "\n");
					writer.write("Topological_Coefficient\t" + p.getTc() + "\n");	
					
					writer.write(GraphIO.tagTargets + "\n");
					Iterator<Protein> targets = g.getChildNodes(p);
					while(targets.hasNext())
					{
						Protein dest = targets.next();
						writer.write(dest.getEnsemblProteinId() + "\t" + Double.toString(1.0) + "\n");
					}
					
					upGeneName = "";
					geneChromosome = "";
					
					writer.write(GraphIO.tagMappings + "\n");
					if(p.hasGene())
					{
						upGeneName = p.getGene().getName();
						geneChromosome = p.getGene().getChromosome();
						
						//writer.write(String.format(outputLineFormat, fieldGeneUPGeneName, p.gene.name));
						//writer.write(String.format(outputLineFormat, fieldGeneChromosome, p.gene.chromosome));
						writer.write(String.format(GraphIO.outputMappingsFormat, p.getEnsemblGeneId(), p.getEnsemblTranscriptId(), p.getUniprotAcc(), upGeneName, geneChromosome));
						
						if(p.getGene().hasSNP())
						{
							writer.write(GraphIO.tagSNP + "\n");
							for(SNP sn : p.getGene().getSnps())
								writer.write(String.format(GraphIO.outputSNPFormat, sn.getId(), sn.getFromNT(), sn.getToNT(), sn.getFunctionClass(), sn.getPositionOnChromosome()));
						}
					}
					else
					{
						
						writer.write(String.format(GraphIO.outputMappingsFormat, p.getEnsemblGeneId(), p.getEnsemblTranscriptId(), p.getUniprotAcc(), upGeneName, geneChromosome));
	/*					writer.write(String.format(outputLineFormat, fieldMappingsEnsemblTranscriptId, p.ensemblTranscriptId));
						writer.write(String.format(outputLineFormat, fieldMappingsUPAcc, p.uniprotAcc));*/
					}
					
					if(p.has2DGelData())
					{
						writer.write(GraphIO.tag2dGel + "\n");
						for(String key : p.getGelData().keySet())
						{
							ArrayList<TwoDGelData> list = p.getGelData().get(key);
							for(TwoDGelData dat : list)
								writer.write(String.format(GraphIO.output2DGelFormat, key, dat.getSpotNr(), dat.getRatio(), dat.getChangeDirection(), Boolean.toString(dat.hasChanged())));
						}
					}
				}
				writer.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

	/**
	 * Load a network from a text file. (STILL BUGGY !!)
	 * @param filename
	 * @return
	 */
	public static DirectedGraph<Protein> loadNetworkFromFile(String filename)
	{
		DirectedGraph<Protein> g = new DirectedGraph<Protein>();
		try
		{
			System.out.println(String.format("Loading network from %s", filename.substring(filename.lastIndexOf("\\")+1)));
			
			boolean isSource = false;
			boolean isTargets = false;
			boolean isMappings = false;
			boolean isSNP = false;
			boolean is2DGel = false;
			
			final int ENSEMBL_GENE_ID_IDX = 0;
			final int ENSEMBL_TRANSCRIPT_ID_IDX = 1;
			final int UNIPROT_ACCESSION_IDX = 2;
			final int UNIPROT_GENENAME_IDX = 3;
			final int CHROMOSOME_IDX = 4;
			
			final int SNP_ID_IDX = 0;
			final int SNP_FROM_NT_IDX = 1;
			final int SNP_TO_NT_IDX = 2;
			final int SNP_CLASS_IDX = 3;
			final int SNP_POSITION_IDX = 4;
			
			final int GEL_CONSOMIC_IDX = 0;
			final int GEL_SPOTNR_IDX = 1;
			final int GEL_RATIO_IDX = 2;
			final int GEL_CHDIR_IDX = 3;
			final int GEL_ISCHANGED_IDX = 4;
			
			BufferedReader reader = new BufferedReader( new FileReader(filename) );
			String line = "";
			Protein source = new Protein();
			while( (line = reader.readLine()) != null)
			{
				//tag handling
				if(line.charAt(0) == '[')
				{
					if(line.equals(GraphIO.tagSource))
					{
						isSource = true;
						isTargets = false;
						isMappings = false;
						isSNP = false;
						is2DGel = false;
						continue;
					}
					else
						isSource = false;
	
					if(line.equals(GraphIO.tagTargets))
					{
						isSource = false;
						isTargets = true;
						isMappings = false;
						isSNP = false;
						is2DGel = false;
						continue;
					}
					else
						isTargets = false;
					
					if(line.equals(GraphIO.tagMappings))
					{
						isSource = false;
						isTargets = false;
						isMappings = true;
						isSNP = false;
						is2DGel = false;
						continue;
					}
					else
						isMappings = false;
	
					if(line.equals(GraphIO.tagSNP))
					{
						isTargets = false;
						isSource = false;
						isMappings = false;
						isSNP = true;
						is2DGel = false;
						continue;
					}
					else
						isSNP = false;
					
					if(line.equals(GraphIO.tag2dGel))
					{
						isTargets = false;
						isSource = false;
						isMappings = false;
						isSNP = false;
						is2DGel = true;
						continue;
					}
					else
						is2DGel = false;		
				}
				
				if(isSource)
				{
					source = new Protein(line.trim());
					g.addVertex(source);
					continue; //there is only one source per entry
				}
				if(isTargets)
				{
					String[] destAndWeight = line.split("\t");
					Protein destination = new Protein(destAndWeight[0].trim());
					g.addVertex(destination);
					PPIEdge<Protein> source2dest = new PPIEdge<Protein>(source, destination);
					PPIEdge<Protein> dest2source = new PPIEdge<Protein>(destination, source);
					source2dest.SetScore("Combined", Double.parseDouble(destAndWeight[1]));
					dest2source.SetScore("Combined", Double.parseDouble(destAndWeight[1]));
					g.addEdge( source2dest );
					g.addEdge( dest2source );
					continue;
				}
				if(isMappings)
				{
					String[] mappings = line.split("\t");
					//handle varying mappings length, if the length of the mapping split array is larger than one check also the previous cells
					switch(mappings.length)
					{
						case 5:
							if(!mappings[CHROMOSOME_IDX].equals(""))
							{
								if(!source.hasGene())
								{
									Gene gene = new Gene();
									gene.setChromosome(mappings[CHROMOSOME_IDX]);
									source.setGene(gene);
								}
								else
									source.getGene().setChromosome(mappings[CHROMOSOME_IDX]);
							}
						case 4:
							if(!mappings[UNIPROT_GENENAME_IDX].equals(""))
							{
								if(!source.hasGene())
								{
									Gene gene = new Gene();
									gene.setName(mappings[UNIPROT_GENENAME_IDX]);
									source.setGene(gene);
								}
								else
									source.getGene().setName(mappings[UNIPROT_GENENAME_IDX]);
							}
						case 3:
							if(!mappings[UNIPROT_ACCESSION_IDX].equals(""))
								source.setUniprotAcc(mappings[UNIPROT_ACCESSION_IDX]);
						case 2:
							if(!mappings[ENSEMBL_TRANSCRIPT_ID_IDX].equals(""))
								source.setEnsemblTranscriptId(mappings[ENSEMBL_TRANSCRIPT_ID_IDX]);
						case 1:
							if(!mappings[ENSEMBL_GENE_ID_IDX].equals(""))
								source.setEnsemblGeneId(mappings[ENSEMBL_GENE_ID_IDX]);
							break;
	
						default: break;
					}
					continue;
				}
				
				if(isSNP)
				{
					String[] snps = line.split("\t");
					if(source.hasGene())
					{
						SNP snp = new SNP();
						snp.setId(snps[SNP_ID_IDX]);
						snp.setFromNT(snps[SNP_FROM_NT_IDX]);
						snp.setToNT(snps[SNP_TO_NT_IDX]);
						snp.setFunctionClass(snps[SNP_CLASS_IDX]);
						snp.setPositionOnChromosome(Integer.parseInt(snps[SNP_POSITION_IDX]));
						source.getGene().getSnps().add(snp);
					}
					continue;
				}
				
				if(is2DGel)
				{
					String[] gelData = line.split("\t");
					if(!source.has2DGelData())
						source.setGelData(new HashMap<String, ArrayList<TwoDGelData>>());
					
					if(!source.getGelData().containsKey(gelData[GEL_CONSOMIC_IDX]))
					{
						TwoDGelData dat = new TwoDGelData();
						dat.setConsomic(gelData[GEL_CONSOMIC_IDX]);
						dat.setSpotNr(Integer.parseInt(gelData[GEL_SPOTNR_IDX]));
						dat.setRatio(Double.parseDouble(gelData[GEL_RATIO_IDX].replace(',', '.')));
						dat.setChangeDirection(gelData[GEL_CHDIR_IDX].charAt(0));
						dat.setTranslationHasChanged(Boolean.parseBoolean(gelData[GEL_ISCHANGED_IDX]));
						
						ArrayList<TwoDGelData> gelDataList = new ArrayList<TwoDGelData>();
						gelDataList.add(dat);
						
						source.getGelData().put(gelData[GEL_CONSOMIC_IDX], gelDataList);
					}
					else
					{
						TwoDGelData dat = new TwoDGelData();
						dat.setConsomic(gelData[GEL_CONSOMIC_IDX]);
						dat.setSpotNr(Integer.parseInt(gelData[GEL_SPOTNR_IDX]));
						dat.setRatio(Double.parseDouble(gelData[GEL_RATIO_IDX].replace(',', '.')));
						dat.setChangeDirection(gelData[GEL_CHDIR_IDX].charAt(0));
						dat.setTranslationHasChanged(Boolean.parseBoolean(gelData[GEL_ISCHANGED_IDX]));
						
						ArrayList<TwoDGelData> gelDataList = source.getGelData().get(gelData[GEL_CONSOMIC_IDX]);
						gelDataList.add(dat);
					}
					continue;
				}
			}
			reader.close();
			System.out.println("Done.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return g;
	}

//	/**
//	 * CAUSES Out of Memory EXCEPTION
//	 * Exports a graph as an XML file.
//	 * @param g
//	 * @param outFile
//	 */
//	public static void exportAsXML(DirectedGraph<Protein> g, String outFile)
//	{
//		System.out.println(String.format("Exporting network to %s", outFile));
//		Element root = new Element("Network");
//		Document doc = new Document(root);
//		Element proteins = new Element("Proteins");
//		Element edges = new Element("Edges");
//		for(Protein p : g.getVertices())
//		{
//			//node information
//			Element protein = new Element("Protein");
//			protein.setAttribute("ensemblId", p.getEnsemblProteinId());
//	
//	
//			if(p.hasGene()) //if gene is present
//			{
//				Element genes = new Element("Genes");
//				Element gene = new Element("Gene");
//				
//				gene.setAttribute("UniProt_GeneName", p.getGene().getName());		
//				if(p.getGene().hasSNP()) //and gene has snps
//				{
//					gene.setAttribute("Chromosome", p.getGene().getChromosome());
//					Element snps = new Element("SNPs");
//					for(SNP s : p.getGene().getSnps())
//					{
//						Element snp = new Element("SNP");
//						snp.setAttribute("id", s.getId());
//						snp.setAttribute("from", s.getFromNT());
//						snp.setAttribute("to", s.getToNT());
//						snp.setAttribute("position", Integer.toString(s.getPositionOnChromosome()));
//						//snp.setAttribute("type", )
//	
//						snps.addContent(snp);
//					}
//					gene.addContent(snps);
//				}
//				else
//					gene.setAttribute("Chromosome", "0");
//				
//				genes.addContent(gene);
//				protein.addContent(genes);
//			}
//	
//			proteins.addContent(protein);
//	
//			//edge information
//			Iterator<Edge<Protein>> iter = g.getOutEdges(p);
//			while(iter.hasNext())
//			{
//				Edge<Protein> e = iter.next();
//				Element ed = new Element("Edge");
//	
//				Element src = new Element("Source");
//				Protein v = (Protein) e.getSource();
//				src.setText(v.getEnsemblProteinId());
//	
//				Element dest = new Element("Destination");
//				v = (Protein) e.getDest();
//				dest.setText(v.getEnsemblProteinId());
//	
//				Element weight = new Element("Weight");
//				weight.setText(Integer.toString(e.getWeight()));
//	
//				ed.addContent(src);
//				ed.addContent(dest);
//				ed.addContent(weight);
//	
//				edges.addContent(ed);
//			}
//		}
//	
//		root.addContent(proteins);
//		root.addContent(edges);
//	
//		try
//		{
//			FileOutputStream out = new FileOutputStream(outFile);
//			XMLOutputter putter = new XMLOutputter(Format.getPrettyFormat());
//			putter.output(doc, out);
//			out.close();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//
//	public static DirectedGraph<Protein> importFromXML(String filename)
//	{
//		DirectedGraph<Protein> g = new DirectedGraph<Protein>();
//		try
//		{
//			SAXParserFactory spf = SAXParserFactory.newInstance();
//			spf.setNamespaceAware(true);
//			SAXParser parser = spf.newSAXParser();	
//	
//			NetworkXMLHandler handler = new NetworkXMLHandler();
//	
//			parser.parse(new File(filename), handler);
//	
//			g = handler.getGraph();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		return g;
//	}
	
	public static void arena3dFileWriter(DirectedGraph<Protein> g, String file)
	{
		//http://arena3d.org/inputfile.html
		
		try
		{
			BufferedWriter writer = new BufferedWriter( new FileWriter(file) );
			writer.write("number_of_layers::1\n");
			writer.write("layer::proteins\n");
			for(Protein p : g.getVertices())
				writer.write(p.getEnsemblProteinId() + "\n");
			writer.write("end_of_layer_inputs\n");
			writer.write("start_connections\n");
			for(Protein p : g.getVertices())
				for(Protein q : g.getDescendantVertices(p))
					writer.write(p.getEnsemblProteinId() + "::proteins\t" + q.getEnsemblProteinId()+ "::proteins\t" + "1" + "\n");
			writer.write("end_connections");
			writer.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void flatfileDB(DirectedGraph<Protein> g, String nodeFlatfile, String edgeFlatfile, HashMap<Protein,String> groupLabelMap)
	{
		try
		{
			BufferedWriter writer = new BufferedWriter( new FileWriter(nodeFlatfile) );
			writer.write("Ensembl_ProtID\tUniProt_Genename\tHasSNP\tSNP_From\tSNP_To\tChangeDirection\t2DGel_Ratio\tGroupLabel\n");
			for(Protein p : g.getVertices())
			{
				if(groupLabelMap.containsKey(p))
					writer.write(p.getEnsemblProteinId() +"\t" + groupLabelMap.get(p) +"\n");
				else
					writer.write(p.getEnsemblProteinId() +"\t" +"\n");
			}
			
			writer.close();
			
			writer = new BufferedWriter( new FileWriter(edgeFlatfile) );
			writer.write("Source\tTarget\tWeight\n");
			for(Protein p : g.getVertices())
			{
				Iterator<Protein> children = g.getChildNodes(p);
				while(children.hasNext())
				{
					Protein child = children.next();
					writer.write(p.getEnsemblProteinId() + "\t" + child.getEnsemblProteinId() +"\t" + Double.toString(((PPIEdge<Protein>) g.getEdge(p, child) ).GetScore("Combined")) +"\n");
				}
			}
			
			writer.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
