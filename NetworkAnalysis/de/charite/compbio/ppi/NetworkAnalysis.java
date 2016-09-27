package de.charite.compbio.ppi;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import sonumina.math.graph.DirectedGraph;
import sonumina.math.graph.DirectedGraph.IDistanceVisitor;
import sonumina.math.graph.DirectedGraph.IDistributionBinner;
import sonumina.math.graph.DirectedGraph.IVertexMeasure;
import sonumina.math.graph.DirectedGraph.IVertexSelector;
import de.charite.compbio.helpers.GraphIO;

public class NetworkAnalysis
{
	public interface IResultGrabber<T>
	{
		public void setResult(AnalysisResults anre, T values);
	}
	/**
	 * Helper class for selecting the appropiate nodes
	 * matchesCriterion is already specialized for finding genes on a particular chromosome
	 * getMeasure will be overridden within the LocalVertexSelector class in the methods for calculating the concrete measures
	 */
	static class SnpGeneSelector implements IVertexSelector<Protein, String>
	{
		SnpGeneSelector(){ };
		public boolean matchesCriterion(Protein v, String criterion)
		{
			if(v.hasGene())
				if(v.getGene().getChromosome().equalsIgnoreCase(criterion) && v.getGene().hasSNP())
					return true;
			return false;
		}
	}
	static class ProteinChangedSelector implements IVertexSelector<Protein, String>
	{
		final boolean condition = true;
		ProteinChangedSelector(){ };
		public boolean matchesCriterion(Protein v, String criterion)
		{
			if(v.has2DGelData())
			{
				if(!v.getGelData().containsKey(criterion))
					return false;
				for(TwoDGelData dat : v.getGelData().get(criterion))
					if(dat.hasChanged() == condition)
						return true;
			}
			return false;
		}
	}
	static class LargerThanOneBinner implements IDistributionBinner
	{
		public boolean isInBin(double currentValue, double currentBin, float binSize)
		{
			if(currentValue < currentBin + binSize)
				return true;
			return false;
		}
	}
	static class SmallerThanOneBinner implements IDistributionBinner
	{
		static final int decimalPlaces = 1000;
		public boolean isInBin(double currentValue, double currentBin, float binSize)
		{
			double transformCurrVal = currentValue*decimalPlaces;
			double transformCurrentBin = currentBin*decimalPlaces;
			if(transformCurrVal < transformCurrentBin + binSize)
				return true;
			return false;
		}
	}
	
	//Measure Keys
	public final static String CLUSTERING_COEFFICIENT = AnalysisResults.CLUSTERING_COEFFICIENT;
	public final static String TOPOLOGICAL_COEFFICIENT = AnalysisResults.TOPOLOGICAL_COEFFICIENT;
	public final static String NEIGHBOURHOOD_CONNECTIVITY = AnalysisResults.NEIGHBOURHOOD_CONNECTIVITY;
	public final static String DEGREE = AnalysisResults.DEGREE;
	private final static String GENE_IDENTIFIER = "gene";
	private final static String PROTEIN_IDENTIFIER = "protein";
	
	//static HashMap<Protein, HashMap<String, Double>> proteinStatistics = new HashMap<Protein, HashMap<String,Double>>();
	public static HashSet<Protein> genesWithSnps;
	public static HashSet<Protein> proteinsChanged;
	//static DirectedGraph<Protein> g = new DirectedGraph<Protein>();
	
	public static void main(String[] args)
	{
		AnalysisResults ar = new AnalysisResults();
		DirectedGraph<Protein> g = new DirectedGraph<Protein>();
		
		HashSet<String> strains = GraphIO.readNetworkSetupFile(g, ar, args[0]);
		//long start = System.currentTimeMillis();
		
		for(String strain : strains)
		{
			getProteinStatistics(g, ar);
			ar.setConsomicStrain(strain);
			ar.setDescription(ar.getConsomicStrain() + "_" + ar.getGender());
			
			String chromosome = strain.replace("C","");
			genesWithSnps = g.getVertexSubset(chromosome, new SnpGeneSelector()); //uses chromosome number w/o C
			ar.setGeneSampleSetSize(genesWithSnps.size());
			proteinsChanged = g.getVertexSubset(ar.getConsomicStrain(), new ProteinChangedSelector()); //must use consomic strain as the data is saved using C and a number
			ar.setProteinSampleSetSize(proteinsChanged.size());
			extractSubsetMeasures(ar, genesWithSnps, new IResultGrabber<Double[]>() {
										public void setResult(AnalysisResults anre, Double[] values)
										{
											anre.setSnpGeneClustCoeff(values[0]);
											anre.setSnpGeneDegree(values[1]);
											anre.setSnpGeneNeighbHConn(values[2]);
											anre.setSnpGeneTopologCoeff(values[3]);
										}
									});
			extractSubsetMeasures(ar, proteinsChanged, new IResultGrabber<Double[]>() {
				public void setResult(AnalysisResults anre, Double[] values)
				{
					anre.setProteinChangedClustCoeff(values[0]);
					anre.setProteinChangedDegree(values[1]);
					anre.setProteinChangedNeighbHConn(values[2]);
					anre.setProteinChangedTopologCoeff(values[3]);
				}
			});
			/**
			 * determine the empirical distribution for the genes
			 */
			float binSize = ar.getBinSize();
			int numOfRep = ar.getSamplingRepetitions();
			String gId = "gene";
			String outFileFmt = "data\\%s_%s_%s_%s_%d_rep_%.1fer_bins.txt"; //Strain - gender - group - measure - repetitions - binSize
			String outFile = String.format(outFileFmt, ar.getConsomicStrain(), ar.getGender(), gId, "degDist", numOfRep, binSize);
			calculateEmpiricalDegreeDistribution(g, ar, GENE_IDENTIFIER, numOfRep, genesWithSnps.size(), binSize);
			ar.saveEmpiricalDegreeDistribution(outFile, gId);
			
			outFile = String.format(outFileFmt, ar.getConsomicStrain(), ar.getGender(), gId, "NeighbHConn", numOfRep, binSize);
			calculateEmpiricalNeighbourhoodConnectivity(g, ar, GENE_IDENTIFIER, numOfRep, genesWithSnps.size(), binSize);
			ar.saveEmpiricalNeighbourhoodConnectivityDistribution(outFile, gId);
			
			outFile = String.format(outFileFmt, ar.getConsomicStrain(), ar.getGender(), gId, "TopologCoeff", numOfRep, binSize);
			calculateEmpiricalTopologicalCoefficient(g, ar, GENE_IDENTIFIER, numOfRep, genesWithSnps.size(), binSize);
			ar.saveEmpiricalTopologicalCoefficientDistribution(outFile, gId);
			
			outFile = String.format(outFileFmt, ar.getConsomicStrain(), ar.getGender(), gId, "ClustCoeff", numOfRep, binSize);
			calculateClusteringCoefficient(g, ar, GENE_IDENTIFIER, numOfRep, genesWithSnps.size(), binSize);
			ar.saveEmpiricalClusteringCoefficientDistribution(outFile, gId);
			
			ar.saveGroupFeatures(proteinsChanged, String.format("data\\%s%s_changedProteins_features.txt",ar.getConsomicStrain(), ar.getGender()));
			ar.saveGroupFeatures(genesWithSnps, String.format("data\\%s_genesWithSnps_features.txt",ar.getConsomicStrain()));
			//changed gene p values
			calculateEmpiricalPValue(ar.getSnpGeneClustCoeff(), ar.getEmpiricalClusteringCoefficientDistributionOfGenes(), ar, 
					new IResultGrabber<Double>() {
						public void setResult(AnalysisResults anre, Double pV)
						{
							anre.setSnpGeneClustCoeffPValue(pV);
						}
					});
			calculateEmpiricalPValue(ar.getSnpGeneDegree(), ar.getEmpiricalDegreeDistributionOfGenes(), ar, 
					new IResultGrabber<Double>()
					{
						public void setResult(AnalysisResults anre, Double pV)
						{
							anre.setSnpGeneDegreePValue(pV);
						}
					});
			calculateEmpiricalPValue(ar.getSnpGeneTopologCoeff(), ar.getEmpiricalTopologicalCoefficientDistributionOfGenes(), ar, 
					new IResultGrabber<Double>()
					{
						public void setResult(AnalysisResults anre, Double pV)
						{
							anre.setSnpGeneTopologCoeffPValue(pV);
						}
					});
			
			calculateEmpiricalPValue(ar.getSnpGeneNeighbHConn(), ar.getEmpiricalNeighbourhoodConnectivityDistributionOfGenes(), ar, 
					new IResultGrabber<Double>()
					{
						public void setResult(AnalysisResults anre, Double pV)
						{
							anre.setSnpGeneNeighbHConnPValue(pV);
						}
					});
			
			gId = "protein";
			outFile = String.format(outFileFmt, ar.getConsomicStrain(), ar.getGender(), gId, "degDist", numOfRep, binSize);
			calculateEmpiricalDegreeDistribution(g, ar, PROTEIN_IDENTIFIER, numOfRep, proteinsChanged.size(), binSize);
			ar.saveEmpiricalDegreeDistribution(outFile, gId);
			
			outFile = String.format(outFileFmt, ar.getConsomicStrain(), ar.getGender(), gId, "NeighbHConn", numOfRep, binSize);
			calculateEmpiricalNeighbourhoodConnectivity(g, ar, PROTEIN_IDENTIFIER, numOfRep, proteinsChanged.size(), binSize);
			ar.saveEmpiricalNeighbourhoodConnectivityDistribution(outFile, gId);
			
			outFile = String.format(outFileFmt, ar.getConsomicStrain(), ar.getGender(), gId, "TopologCoeff", numOfRep, binSize);
			calculateEmpiricalTopologicalCoefficient(g, ar, PROTEIN_IDENTIFIER, numOfRep, proteinsChanged.size(), binSize);
			ar.saveEmpiricalTopologicalCoefficientDistribution(outFile, gId);
			
			outFile = String.format(outFileFmt, ar.getConsomicStrain(), ar.getGender(), gId, "ClustCoeff", numOfRep, binSize);
			calculateClusteringCoefficient(g, ar, PROTEIN_IDENTIFIER, numOfRep, proteinsChanged.size(), binSize);
			ar.saveEmpiricalClusteringCoefficientDistribution(outFile, gId);
			
			//changed protein stuff
			calculateEmpiricalPValue(ar.getProteinChangedClustCoeff(), ar.getEmpiricalClusteringCoefficientDistributionOfProteins(), ar, 
					new IResultGrabber<Double>() {
						public void setResult(AnalysisResults anre, Double pV)
						{
							anre.setProteinChangedClustCoeffPValue(pV);
						}
					});
			calculateEmpiricalPValue(ar.getProteinChangedDegree(), ar.getEmpiricalDegreeDistributionOfProteins(), ar, 
					new IResultGrabber<Double>()
					{
						public void setResult(AnalysisResults anre, Double pV)
						{
							anre.setProteinChangedDegreePValue(pV);
						}
					});
			calculateEmpiricalPValue(ar.getProteinChangedTopologCoeff(), ar.getEmpiricalTopologicalCoefficientDistributionOfProteins(), ar, 
					new IResultGrabber<Double>()
					{
						public void setResult(AnalysisResults anre, Double pV)
						{
							anre.setProteinChangedTopologCoeffPValue(pV);
						}
					});
			
			calculateEmpiricalPValue(ar.getProteinChangedNeighbHConn(), ar.getEmpiricalNeighbourhoodConnectivityDistributionOfProteins(), ar, 
					new IResultGrabber<Double>()
					{
						public void setResult(AnalysisResults anre, Double pV)
						{
							anre.setProteinChangedNeighbHConnPValue(pV);
						}
					});

			ar.saveSummary(String.format("data\\%s_%s_summary.txt", ar.getConsomicStrain(), ar.getGender()));
			//System.out.printf("%s finished in %ds\n", strain,(System.currentTimeMillis() - start)/1000);
		}
	}
	
	public static void calculateShortestPaths(DirectedGraph<Protein> g) 
	{
		for(Protein p : g.getVertices())
		{
			final HashMap<Protein, List<Protein>> paths = new HashMap<Protein,List<Protein>>();
			g.singleSourceShortestPath(p, false, new IDistanceVisitor<Protein>()
					{
						@Override
						public boolean visit(Protein vertex, List<Protein> path, int distance)
						{
							paths.put(vertex, path);
							return true;
						}
						
					});
		}
	}
	
	/**
	 * Determines the subgraph that comprises the mutated genes, the changed proteins and the connecting nodes between these two groups
	 * The subgraph is created by checking whether two nodes in the subgraph set have an edge in the original graph
	 * @param g
	 * @param genes
	 * @param proteins
	 * @return
	 */
	public static DirectedGraph<Protein> extractSubgraph(DirectedGraph<Protein> g, HashSet<Protein> genes, HashSet<Protein> proteins)
	{
		System.out.println("Extracting subgraph.");
		HashSet<Protein> subgraphNodes = new HashSet<Protein>();
		subgraphNodes.addAll(genes);
		subgraphNodes.addAll(proteins);
		DirectedGraph<Protein> subgraph = new DirectedGraph<Protein>();

		for(Protein v : subgraphNodes)
		{
			for(Protein u : subgraphNodes)
			{
				if(u.equals(v))
					continue;

				subgraph.addVertex(u);
				subgraph.addVertex(v);

				if(g.hasEdge(u, v))
				{
					subgraph.addEdge( new PPIEdge<Protein>(u,v) );
					subgraph.addEdge( new PPIEdge<Protein>(v,u) );
				}
			}
		}
		System.out.printf("Subgraph successfully created (%d nodes, %d edges).\n", subgraph.getNumberOfVertices(), subgraph.getNumberEdges());
		return subgraph;
	}
	
	/**
	 * Determines the subset averages and saves them accordingly
	 * @param ar
	 * @param subset
	 * @param assigner
	 */
	public static void extractSubsetMeasures(AnalysisResults ar, HashSet<Protein> subset, IResultGrabber<Double[]> assigner)
	{
		double cc = 0.0;
		double nc = 0.0;
		double tc = 0.0;
		double deg = 0.0;
		
		for(Protein p: subset)
		{
			//HashMap<String,Double> protStats = ar.getProteinStatistics().get(p);
			cc += p.getCc(); //protStats.get(CLUSTERING_COEFFICIENT);
			nc += p.getNc(); //protStats.get(NEIGHBOURHOOD_CONNECTIVITY);
			tc += p.getTc(); //protStats.get(TOPOLOGICAL_COEFFICIENT);
			deg += p.getDeg(); //protStats.get(DEGREE);
		}
		Double[] values = new Double[]{(double) cc/subset.size(), (double) deg/subset.size(), (double) nc/subset.size(), (double) tc/subset.size()}; 
		assigner.setResult(ar, values);
	}
	
	public static void calculateClusteringCoefficient(final DirectedGraph<Protein> g, AnalysisResults ar, String groupIdentifier, int numOfRep, int sampleSize, float binSize)
	{
		
		class VertexMeasure implements IVertexMeasure<Protein>
		{
			@Override
			public double getMeasure(Protein v)
			{
				return v.getCc();
			}
		}
		
		System.out.println(String.format("Calculating empirical %s distribution", "clustering coefficient"));

		HashMap<Double, Integer> ed = g.getEmpiricalDistribution(numOfRep, sampleSize,	binSize, new VertexMeasure(), new SmallerThanOneBinner());
		if(groupIdentifier.equalsIgnoreCase(GENE_IDENTIFIER))
			ar.setEmpiricalClusteringCoefficientDistributionOfGenes(ed);
		else if(groupIdentifier.equalsIgnoreCase(PROTEIN_IDENTIFIER))
			ar.setEmpiricalClusteringCoefficientDistributionOfProteins(ed);
		System.out.println("Done.");
	}

	public static void calculateEmpiricalTopologicalCoefficient(DirectedGraph<Protein> g, AnalysisResults ar, String groupIdentifier, int numOfRep, int sampleSize, float binSize)
	{
		class VertexMeasure implements IVertexMeasure<Protein>
		{
			@Override
			public double getMeasure(Protein v)
			{
				return v.getTc();
			}
		}
		
		System.out.println(String.format("Calculating empirical %s distribution", "topological coefficient"));
		HashMap<Double, Integer> ed = g.getEmpiricalDistribution(numOfRep, sampleSize, binSize, new VertexMeasure(), new SmallerThanOneBinner());
		if(groupIdentifier.equalsIgnoreCase(GENE_IDENTIFIER))
			ar.setEmpiricalTopologicalCoefficientDistributionOfGenes(ed);
		else if(groupIdentifier.equalsIgnoreCase(PROTEIN_IDENTIFIER))
			ar.setEmpiricalTopologicalCoefficientDistributionOfProteins(ed);
		System.out.println("Done.");
	}

	public static void calculateEmpiricalNeighbourhoodConnectivity(final DirectedGraph<Protein> g, AnalysisResults ar, String groupIdentifier, int numOfRep, int sampleSize, float binSize) 
	{
		class VertexMeasure implements IVertexMeasure<Protein>
		{
			@Override
			public double getMeasure(Protein v)
			{
				//neighbourhood connectivity
				return v.getNc();
			}
		}
		
		System.out.println(String.format("Calculating empirical %s distribution", "neighbourhood connectivity"));
		HashMap<Double, Integer> ed = g.getEmpiricalDistribution(numOfRep, sampleSize, binSize, new VertexMeasure(), new LargerThanOneBinner());
		if(groupIdentifier.equalsIgnoreCase(GENE_IDENTIFIER))
			ar.setEmpiricalNeighbourhoodConnectivityDistributionOfGenes(ed);
		else if (groupIdentifier.equalsIgnoreCase(PROTEIN_IDENTIFIER))
			ar.setEmpiricalNeighbourhoodConnectivityDistributionOfProteins(ed);
		System.out.println("Done.");
	}
	
	public static void calculateEmpiricalDegreeDistribution(final DirectedGraph<Protein> g, AnalysisResults ar, String groupIdentifier, int numOfRep, int sampleSize, float binSize)
	{		
		class VertexMeasure implements IVertexMeasure<Protein>
		{
			@Override
			public double getMeasure(Protein v)
			{
				return v.getDeg();
			}
		}
		System.out.println(String.format("Calculating empirical %s distribution", "degree"));
		
		HashMap<Double,Integer> ed = g.getEmpiricalDistribution(numOfRep, sampleSize, binSize, new VertexMeasure(), new LargerThanOneBinner());
		if(groupIdentifier.equalsIgnoreCase(GENE_IDENTIFIER))
			ar.setEmpiricalDegreeDistributionOfGenes(ed);
		else if(groupIdentifier.equalsIgnoreCase(PROTEIN_IDENTIFIER))
			ar.setEmpiricalDegreeDistributionOfProteins(ed);
		System.out.println("Done.");
	}
	
	public static void calculateEmpiricalPValue(double subsetMeasureValue, HashMap<Double,Integer> empiricalDistribution, AnalysisResults ar, IResultGrabber<Double> valueGrabber)
	{
		int largerOrEqualCounter = 0;
		double mean = 0;
		for(double meas : empiricalDistribution.keySet())
			mean = mean + (meas * empiricalDistribution.get(meas));
		mean = (double) mean/ar.getSamplingRepetitions();
		if(subsetMeasureValue > mean)
		{
			for(double k : empiricalDistribution.keySet())
			{
				if( k >= subsetMeasureValue)
					largerOrEqualCounter += empiricalDistribution.get(k);
			}
		}
		else
		{
			for(double k : empiricalDistribution.keySet())
			{
				if( k <= subsetMeasureValue )
					largerOrEqualCounter += empiricalDistribution.get(k);
			}
		}
		
		valueGrabber.setResult(ar, (double) largerOrEqualCounter/ ar.getSamplingRepetitions());
	}
	
	public static <K,V> void saveHashMap(HashMap<K,V> map, String file)
	{
		try {
			BufferedWriter writer = new BufferedWriter( new FileWriter(file) );
			for(K k : map.keySet())
				writer.write(k +"\t" + map.get(k)+"\n");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	/**
	 * calculates the graph measures for each node in the given graph 
	 * @param g the graph under study
	 * @param ar result container
	 */
	public static void getProteinStatistics(DirectedGraph<Protein> g, AnalysisResults ar)
	{
		System.out.println("Calculating protein statistics.");
		double cc = 0.0;
		double nc = 0.0;
		double tc = 0.0;
		int deg = 0;
		
		double graphCC = 0.0;
		double graphNC = 0.0;
		double graphTC = 0.0;
		double graphDEG = 0.0;
		
		int total = g.getNumberOfVertices();
		int current = 0;
		
		//HashMap<Protein, HashMap<String, Double>> protStats = new HashMap<Protein, HashMap<String,Double>>();
		for(Protein p : g.getVertices())
		{		
//			if(p.getEnsemblProteinId().equals("ENSMUSP00000029248"))
//				System.out.println(p.getEnsemblProteinId() + " "+ g.getOutDegree(p));
//			else
//				continue;
			deg = g.getOutDegree(p);
			cc = g.getClusteringCoefficient(p);
			nc = g.getNeighbourhoodConnectivity(p);
			tc = g.getTopologicalCoefficient(p);
			
//			HashMap<String, Double> stats = new HashMap<String, Double>();
//			stats.put(CLUSTERING_COEFFICIENT, cc);
//			stats.put(NEIGHBOURHOOD_CONNECTIVITY, nc);
//			stats.put(TOPOLOGICAL_COEFFICIENT, tc);
//			stats.put(DEGREE, deg);
//			protStats.put(p, stats);
			
			p.setCc(cc);
			p.setNc(nc);
			p.setTc(tc);
			p.setDeg(deg);
			
			graphCC += cc;
			graphNC += nc;
			graphTC += tc;
			graphDEG += deg;
			++current;
			System.out.println(String.format("Node %d of %d (%.2f %% done).", current, total, ((float) current/total)*100));
		}
		//ar.setProteinStatistics(protStats);
		
		HashMap<String, Double> graphStats = new HashMap<String, Double>();
		graphStats.put(CLUSTERING_COEFFICIENT, (double) graphCC/total);
		graphStats.put(NEIGHBOURHOOD_CONNECTIVITY, (double) graphNC/total);
		graphStats.put(TOPOLOGICAL_COEFFICIENT, (double) graphTC/total);
		graphStats.put(DEGREE, (double) graphDEG/total);
		ar.setOverallGraphStatistics(graphStats);
		
		System.out.println("Done.");
	}
	
	
	/**
	 * Determines for a given set of genes on a particular chromosome the associated set of proteins that are differently expressed when a gene in the former set is altered
	 * @param g Graph
	 * @param consomicStrain Strain identifier, e.g. C12
	 * @param chromosome Chromosome identifier
	 */
	public static void getSnpToProtGraphs(ArrayList<Protein> subset, String consomicStrain, String chromosome)
	{
		//genes on the specified chromosome that have SNPs
		ArrayList<Protein> mutatedGenes = new ArrayList<Protein>(); 
		//all proteins that are differently translated when there are mutations in genes on the given chromosome
		ArrayList<Protein> differentlyTranslatedProteins = new ArrayList<Protein>(); 
		
		for(Protein p : subset)
		{
			//if p is changed check whether it is changed in the specified strain
			if(p.getGelData()!= null)
				if(p.getGelData().containsKey(consomicStrain))
					differentlyTranslatedProteins.add(p);
			
			//if p has gene information check if it lies on the given chromosome and is mutated
			if(p.hasGene())
				if(p.getGene().getChromosome().equals(chromosome) && p.getGene().hasSNP())
					mutatedGenes.add(p);		
		}
		
		for(Protein p : mutatedGenes)
		{
			System.out.println("mut: " +p.toString());
			System.out.println(String.format("%d %.3f %.3f %.3f",p.getDeg(), p.getCc(), p.getNc(), p.getTc()));
		}
		
		for(Protein p : differentlyTranslatedProteins)
		{
			System.out.println("diffTra: "+p.toString());
			System.out.println(String.format("%d %.3f %.3f %.3f",p.getDeg(), p.getCc(), p.getNc(), p.getTc()));
		}
	}

	/**
	 * Dummy function for checking whether data was correctly imported.
	 * @param g
	 */
	public static void Debug(DirectedGraph<Protein> g)
	{
		for(Protein p : g.getVertices())
		{
			if(p.hasGene())
			{
				if(p.getGene().hasSNP())
				{
					System.out.println(String.format("Protein: %s", p.getEnsemblProteinId()));
					System.out.println(String.format("Gene: %s on C%s", p.getGene().getName(), p.getGene().getChromosome()));
					System.out.println(String.format("Number of SNPs: %d", p.getGene().getSnps().size()));
					String s = "";
					for(SNP snp : p.getGene().getSnps())
						s += snp.getId() + " ";
					System.out.println(String.format("SNP-IDs: %s", s));
				}
			}

		}
	}

}
