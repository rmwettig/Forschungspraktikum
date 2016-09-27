package de.charite.compbio.ppi;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
/**
 * Holds the results from the network analysis. (For a more uniform output in future, hopefully)
 *
 */
public class AnalysisResults 
{
	//Measure Keys
	public final static String CLUSTERING_COEFFICIENT = "CC";
	public final static String TOPOLOGICAL_COEFFICIENT = "TC";
	public final static String NEIGHBOURHOOD_CONNECTIVITY = "NC";
	public final static String DEGREE = "DEG";
	
	private final String fileTag = "NetworkAnalysisResults_";
	private final String columnHeader = "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n";
	//settings
	private float binSize;
	private int samplingRepetitions;
	private int geneSampleSetSize;
	private int proteinSampleSetSize;
	private String consomicStrain;	
	private String description;
	private String gender;
	private String networkname;
	private String species;
	private double scoreThreshold;
	//measurements
	//private HashMap<Protein, HashMap<String, Double>> proteinStatistics;
	private HashMap<Double, Integer> empiricalDegreeDistributionOfGenes;
	private HashMap<Double, Integer> empiricalClusteringCoefficientDistributionOfGenes;
	private HashMap<Double, Integer> empiricalNeighbourhoodConnectivityDistributionOfGenes;
	private HashMap<Double, Integer> empiricalTopologicalCoefficientDistributionOfGenes;
	
	private HashMap<Double, Integer> empiricalDegreeDistributionOfProteins;
	private HashMap<Double, Integer> empiricalClusteringCoefficientDistributionOfProteins;
	private HashMap<Double, Integer> empiricalNeighbourhoodConnectivityDistributionOfProteins;
	private HashMap<Double, Integer> empiricalTopologicalCoefficientDistributionOfProteins;
	
	private HashMap<String, Double> overallGraphStatistics;
	
	private double snpGeneDegree;
	private double snpGeneClustCoeff;
	private double snpGeneNeighbHConn;
	private double snpGeneTopologCoeff;
	
	private double snpGeneDegreePValue;
	private double snpGeneClustCoeffPValue;
	private double snpGeneNeighbHConnPValue;
	private double snpGeneTopologCoeffPValue;
	
	private double proteinChangedDegree;
	private double proteinChangedClustCoeff;
	private double proteinChangedNeighbHConn;
	private double proteinChangedTopologCoeff;
	
	private double proteinChangedDegreePValue;
	private double proteinChangedClustCoeffPValue;
	private double proteinChangedNeighbHConnPValue;
	private double proteinChangedTopologCoeffPValue;


	public double getProteinChangedDegree() {
		return proteinChangedDegree;
	}
	public void setProteinChangedDegree(double proteinChangedDegree) {
		this.proteinChangedDegree = proteinChangedDegree;
	}
	public double getProteinChangedClustCoeff() {
		return proteinChangedClustCoeff;
	}
	public void setProteinChangedClustCoeff(double proteinChangedClustCoeff) {
		this.proteinChangedClustCoeff = proteinChangedClustCoeff;
	}
	public double getProteinChangedNeighbHConn() {
		return proteinChangedNeighbHConn;
	}
	public void setProteinChangedNeighbHConn(double proteinChangedNeighbHConn) {
		this.proteinChangedNeighbHConn = proteinChangedNeighbHConn;
	}
	public double getProteinChangedTopologCoeff() {
		return proteinChangedTopologCoeff;
	}
	public void setProteinChangedTopologCoeff(double proteinChangedTopologCoeff) {
		this.proteinChangedTopologCoeff = proteinChangedTopologCoeff;
	}
	public double getProteinChangedDegreePValue() {
		return proteinChangedDegreePValue;
	}
	public void setProteinChangedDegreePValue(double proteinChangedDegreePValue) {
		this.proteinChangedDegreePValue = proteinChangedDegreePValue;
	}
	public double getProteinChangedClustCoeffPValue() {
		return proteinChangedClustCoeffPValue;
	}
	public void setProteinChangedClustCoeffPValue(
			double proteinChangedClustCoeffPValue) {
		this.proteinChangedClustCoeffPValue = proteinChangedClustCoeffPValue;
	}
	public double getProteinChangedNeighbHConnPValue() {
		return proteinChangedNeighbHConnPValue;
	}
	public void setProteinChangedNeighbHConnPValue(
			double proteinChangedNeighbHConnPValue) {
		this.proteinChangedNeighbHConnPValue = proteinChangedNeighbHConnPValue;
	}
	public double getProteinChangedTopologCoeffPValue() {
		return proteinChangedTopologCoeffPValue;
	}
	public void setProteinChangedTopologCoeffPValue(
			double proteinChangedTopologCoeffPValue) {
		this.proteinChangedTopologCoeffPValue = proteinChangedTopologCoeffPValue;
	}
	public double getSnpGeneDegree() {
		return snpGeneDegree;
	}
	public void setSnpGeneDegree(double snpGeneDegree) {
		this.snpGeneDegree = snpGeneDegree;
	}
	public double getSnpGeneClustCoeff() {
		return snpGeneClustCoeff;
	}
	public void setSnpGeneClustCoeff(double snpGeneClustCoeff) {
		this.snpGeneClustCoeff = snpGeneClustCoeff;
	}
	public double getSnpGeneNeighbHConn() {
		return snpGeneNeighbHConn;
	}
	public void setSnpGeneNeighbHConn(double targetGroupNeighbHConn) {
		this.snpGeneNeighbHConn = targetGroupNeighbHConn;
	}
	public double getSnpGeneTopologCoeff() {
		return snpGeneTopologCoeff;
	}
	public void setSnpGeneTopologCoeff(double targetGroupTopologCoeff) {
		this.snpGeneTopologCoeff = targetGroupTopologCoeff;
	}
//	public HashMap<Protein, HashMap<String, Double>> getProteinStatistics() {
//		return proteinStatistics;
//	}
//	public void setProteinStatistics(
//			HashMap<Protein, HashMap<String, Double>> proteinStatistics) {
//		this.proteinStatistics = proteinStatistics;
//	}
	public HashMap<Double, Integer> getEmpiricalDegreeDistributionOfGenes() {
		return empiricalDegreeDistributionOfGenes;
	}
	public void setEmpiricalDegreeDistributionOfGenes(HashMap<Double, Integer> empiricalDegreeDistributionOfGenes) {
		this.empiricalDegreeDistributionOfGenes = empiricalDegreeDistributionOfGenes;
	}
	public HashMap<Double, Integer> getEmpiricalClusteringCoefficientDistributionOfGenes() {
		return empiricalClusteringCoefficientDistributionOfGenes;
	}
	public void setEmpiricalClusteringCoefficientDistributionOfGenes(HashMap<Double, Integer> empiricalClusteringCoefficientDistributionOfGenes) {
		this.empiricalClusteringCoefficientDistributionOfGenes = empiricalClusteringCoefficientDistributionOfGenes;
	}
	public HashMap<Double, Integer> getEmpiricalNeighbourhoodConnectivityDistributionOfGenes() {
		return empiricalNeighbourhoodConnectivityDistributionOfGenes;
	}
	public void setEmpiricalNeighbourhoodConnectivityDistributionOfGenes(HashMap<Double, Integer> empiricalNeighbourhoodConnectivityDistributionOfGenes) {
		this.empiricalNeighbourhoodConnectivityDistributionOfGenes = empiricalNeighbourhoodConnectivityDistributionOfGenes;
	}
	public HashMap<Double, Integer> getEmpiricalTopologicalCoefficientDistributionOfGenes() {
		return empiricalTopologicalCoefficientDistributionOfGenes;
	}
	public void setEmpiricalTopologicalCoefficientDistributionOfGenes(HashMap<Double, Integer> empiricalTopologicalCoefficientDistributionOfGenes) {
		this.empiricalTopologicalCoefficientDistributionOfGenes = empiricalTopologicalCoefficientDistributionOfGenes;
	}
	
	public HashMap<Double, Integer> getEmpiricalDegreeDistributionOfProteins() {
		return empiricalDegreeDistributionOfProteins;
	}
	public void setEmpiricalDegreeDistributionOfProteins(HashMap<Double, Integer> empiricalDegreeDistributionOfProteins) {
		this.empiricalDegreeDistributionOfProteins = empiricalDegreeDistributionOfProteins;
	}
	public HashMap<Double, Integer> getEmpiricalClusteringCoefficientDistributionOfProteins() {
		return empiricalClusteringCoefficientDistributionOfProteins;
	}
	public void setEmpiricalClusteringCoefficientDistributionOfProteins(HashMap<Double, Integer> empiricalClusteringCoefficientDistributionOfProteins) {
		this.empiricalClusteringCoefficientDistributionOfProteins = empiricalClusteringCoefficientDistributionOfProteins;
	}
	public HashMap<Double, Integer> getEmpiricalNeighbourhoodConnectivityDistributionOfProteins() {
		return empiricalNeighbourhoodConnectivityDistributionOfProteins;
	}
	public void setEmpiricalNeighbourhoodConnectivityDistributionOfProteins(HashMap<Double, Integer> empiricalNeighbourhoodConnectivityDistributionOfProteins) {
		this.empiricalNeighbourhoodConnectivityDistributionOfProteins = empiricalNeighbourhoodConnectivityDistributionOfProteins;
	}
	public HashMap<Double, Integer> getEmpiricalTopologicalCoefficientDistributionOfProteins() {
		return empiricalTopologicalCoefficientDistributionOfProteins;
	}
	public void setEmpiricalTopologicalCoefficientDistributionOfProteins(HashMap<Double, Integer> empiricalTopologicalCoefficientDistributionOfProteins) {
		this.empiricalTopologicalCoefficientDistributionOfProteins = empiricalTopologicalCoefficientDistributionOfProteins;
	}
	
	public HashMap<String, Double> getOverallGraphStatistics() {
		return overallGraphStatistics;
	}
	public void setOverallGraphStatistics(
			HashMap<String, Double> overallGraphStatistics) {
		this.overallGraphStatistics = overallGraphStatistics;
	}

	public float getBinSize() {
		return binSize;
	}
	public void setBinSize(float binSize) {
		this.binSize = binSize;
	}

	public int getSamplingRepetitions() {
		return samplingRepetitions;
	}
	public void setSamplingRepetitions(int samplingRepetitions) {
		this.samplingRepetitions = samplingRepetitions;
	}

	public int getGeneSampleSetSize() {
		return geneSampleSetSize;
	}
	public void setGeneSampleSetSize(int geneSampleSetSize) {
		this.geneSampleSetSize = geneSampleSetSize;
	}
	
	public int getProteinSampleSetSize()
	{
		return proteinSampleSetSize;
	}
	
	public void setProteinSampleSetSize(int proteinSampleSetSize)
	{
		this.proteinSampleSetSize = proteinSampleSetSize;
	}
	
	public String getConsomicStrain() {
		return consomicStrain;
	}
	public void setConsomicStrain(String consomicStrain){
		this.consomicStrain = consomicStrain;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getNetworkname() {
		return networkname;
	}
	public void setNetworkname(String networkname) {
		this.networkname = networkname;
	}
	public double getSnpGeneDegreePValue() {
		return snpGeneDegreePValue;
	}
	public void setSnpGeneDegreePValue(double targetGroupDegreePValue) {
		this.snpGeneDegreePValue = targetGroupDegreePValue;
	}
	public double getSnpGeneClustCoeffPValue() {
		return snpGeneClustCoeffPValue;
	}
	public void setSnpGeneClustCoeffPValue(double targetGroupClustCoeffPValue) {
		this.snpGeneClustCoeffPValue = targetGroupClustCoeffPValue;
	}
	public double getSnpGeneNeighbHConnPValue() {
		return snpGeneNeighbHConnPValue;
	}
	public void setSnpGeneNeighbHConnPValue(double targetGroupNeighbHConnPValue) {
		this.snpGeneNeighbHConnPValue = targetGroupNeighbHConnPValue;
	}
	public double getSnpGeneTopologCoeffPValue() {
		return snpGeneTopologCoeffPValue;
	}
	public void setSnpGeneTopologCoeffPValue(
			double targetGroupTopologCoeffPValue) {
		this.snpGeneTopologCoeffPValue = targetGroupTopologCoeffPValue;
	}
	public double getScoreThreshold() {
		return scoreThreshold;
	}
	public void setScoreThreshold(double scoreThreshold) {
		this.scoreThreshold = scoreThreshold;
	}
	public String getSpecies() {
		return species;
	}
	public void setSpecies(String species) {
		this.species = species;
	}
	/**
	 * Saves the calculated measures like clustering coefficient for the graph and the single nodes as a text file.
	 * @param file output filename
	 */
//	public void saveMeasures(String file)
//	{
//		try
//		{
//			System.out.println("Saving protein statistics in " + file.substring(file.lastIndexOf("\\") + 1));
//			BufferedWriter writer = new BufferedWriter( new FileWriter(file) );
//			String fmt = "%s\t%.3f\t%.3f\t%.3f\n";
//			writer.write("\t\tCC\tNC\tTC\n");
//			writer.write(String.format(fmt, "Graph", overallGraphStatistics.get(CLUSTERING_COEFFICIENT), overallGraphStatistics.get(NEIGHBOURHOOD_CONNECTIVITY), overallGraphStatistics.get(TOPOLOGICAL_COEFFICIENT)));
//			writer.write("Protein\tCC\tNC\tTC\n");
//
//			for(Protein p : proteinStatistics.keySet())
//			{
//				HashMap<String, Double> temp = proteinStatistics.get(p);
//				writer.write(String.format(fmt, p.getEnsemblProteinId(), temp.get(CLUSTERING_COEFFICIENT), temp.get(NEIGHBOURHOOD_CONNECTIVITY), temp.get(TOPOLOGICAL_COEFFICIENT)));
//			}
//			writer.close();
//			System.out.println("Done.");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	/**
	 * Saves the calculated emipirical degree distribution as a text file.
	 * @param file output filename
	 * @param group identifier "gene" or "protein"
	 */
	public void saveEmpiricalDegreeDistribution(String file, String group)
	{
		try
		{
			System.out.println("Saving empirical degree distribution in " + file.substring(file.lastIndexOf("\\") + 1));
			BufferedWriter writer = new BufferedWriter( new FileWriter(file) );
			writer.write("Degree\tCount\n");
			String fmt = "%.3f\t%d\n";
			HashMap<Double,Integer> localSet;
			if(group.equalsIgnoreCase("gene"))
				localSet = empiricalDegreeDistributionOfGenes;
			else if(group.equalsIgnoreCase("protein"))
				localSet = empiricalDegreeDistributionOfProteins;
			else
			{
				System.err.printf("Invalid group identifier: %s. Expected \"gene\" or \"protein\"\n", group);
				return;
			}
			
			for(double deg : localSet.keySet())
			{
				writer.write(String.format(fmt, deg, localSet.get(deg)));
			}
			writer.close();
			System.out.println("Done.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * Saves the calculated emipirical neighbourhood connectivity distribution as a text file.
	 * @param file output filename
	 * @param group identifier "gene" or "protein"
	 */
	public void saveEmpiricalNeighbourhoodConnectivityDistribution(String file, String group)
	{
		try
		{
			System.out.println("Saving neighbourhood connectivity distribution in " + file.substring(file.lastIndexOf("\\") + 1));
			BufferedWriter writer = new BufferedWriter( new FileWriter(file) );
			writer.write("Neighbourhood_Connectivity\tCount\n");
			String fmt = "%.3f\t%d\n";
			HashMap<Double,Integer> localSet;
			if(group.equalsIgnoreCase("gene"))
				localSet = empiricalNeighbourhoodConnectivityDistributionOfGenes;
			else if(group.equalsIgnoreCase("protein"))
				localSet = empiricalNeighbourhoodConnectivityDistributionOfProteins;
			else
			{
				System.err.printf("Invalid group identifier: %s. Expected \"gene\" or \"protein\"\n", group);
				return;
			}
			for(double nc : localSet.keySet())
			{
				writer.write(String.format(fmt, nc, localSet.get(nc)));
			}
			writer.close();
			System.out.println("Done.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * Saves the calculated emipirical clustering coefficient distributions as a text file.
	 * @param file output filename
	 * @param group TODO
	 */
	public void saveEmpiricalClusteringCoefficientDistribution(String file, String group)
	{
		try
		{
			System.out.println("Saving empirical clustering coefficient distribution in " + file.substring(file.lastIndexOf("\\") + 1));
			BufferedWriter writer = new BufferedWriter( new FileWriter(file) );
			writer.write("Clustering_Coefficient\tCount\n");
			String fmt = "%.3f\t%d\n";
			
			HashMap<Double,Integer> localSet;
			if(group.equalsIgnoreCase("gene"))
				localSet = empiricalClusteringCoefficientDistributionOfGenes;
			else if(group.equalsIgnoreCase("protein"))
				localSet = empiricalClusteringCoefficientDistributionOfProteins;
			else
			{
				System.err.printf("Invalid group identifier: %s. Expected \"gene\" or \"protein\"\n", group);
				return;
			}
			for(double cc : localSet.keySet())
			{
				writer.write(String.format(fmt, cc, localSet.get(cc)));
			}
			writer.close();
			System.out.println("Done.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * Saves the calculated emipirical degree distributions as a text file.
	 * @param file output filename
	 * @param group TODO
	 */
	public void saveEmpiricalTopologicalCoefficientDistribution(String file, String group)
	{
		try
		{
			System.out.println("Saving empirical topological toefficient distribution in " + file.substring(file.lastIndexOf("\\") + 1));
			BufferedWriter writer = new BufferedWriter( new FileWriter(file) );
			writer.write("Topological_Coefficient\tCount\n");
			String fmt = "%.3f\t%d\n";
			
			HashMap<Double,Integer> localSet;
			if(group.equalsIgnoreCase("gene"))
				localSet = empiricalTopologicalCoefficientDistributionOfGenes;
			else if(group.equalsIgnoreCase("protein"))
				localSet = empiricalTopologicalCoefficientDistributionOfProteins;
			else
			{
				System.err.printf("Invalid group identifier: %s. Expected \"gene\" or \"protein\"\n", group);
				return;
			}
			for(double tc : localSet.keySet())
			{
				writer.write(String.format(fmt, tc, localSet.get(tc)));
			}
			writer.close();
			System.out.println("Done.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * Saves a summary that contains general information about the network
	 * @param file
	 */
	public void saveSummary(String file)
	{
		try
		{
			System.out.println("Saving summary.");
			BufferedWriter writer = new BufferedWriter( new FileWriter(file) );
			writer.write("Network Analysis Summary\n");
			writer.write("Network name: " + networkname + "\n");
			writer.write("Species: " + species + "\n");
			writer.write("Consomic Strain: " + consomicStrain + "\n");
			writer.write("Gender: " + gender + "\n");
			writer.write("Description:\n" + description + "\n");
			writer.write("Edge Score Threshold: "+ Double.toString(scoreThreshold)+"\n");
			writer.write("Number of Repetitions: " + Integer.toString(samplingRepetitions) +"\n");
			writer.write("Gene Sampling size: " + Integer.toString(geneSampleSetSize) +"\n");
			writer.write("Protein Sampling size: " + Integer.toString(proteinSampleSetSize) + "\n");
			String subsumedHeader = "%s\t%s\t%s\t%s\t%s\n";
			writer.write(String.format(subsumedHeader, "OBJ", DEGREE, CLUSTERING_COEFFICIENT, TOPOLOGICAL_COEFFICIENT, NEIGHBOURHOOD_CONNECTIVITY));
			String outfmt = "%s\t%.3f\t%.3f\t%.3f\t%.3f\n";
			writer.write(String.format(outfmt, "Graph", overallGraphStatistics.get(DEGREE), overallGraphStatistics.get(CLUSTERING_COEFFICIENT), overallGraphStatistics.get(TOPOLOGICAL_COEFFICIENT), overallGraphStatistics.get(NEIGHBOURHOOD_CONNECTIVITY)));
			writer.write(String.format(outfmt, consomicStrain+"_GENES", snpGeneDegree, snpGeneClustCoeff, snpGeneTopologCoeff, snpGeneNeighbHConn));
			writer.write(String.format(outfmt, "emp. p-values", snpGeneDegreePValue, snpGeneClustCoeffPValue, snpGeneTopologCoeffPValue, snpGeneNeighbHConnPValue));
			writer.write(String.format(outfmt, "changed_Proteins", proteinChangedDegree, proteinChangedClustCoeff, proteinChangedTopologCoeff, proteinChangedNeighbHConn));
			writer.write(String.format(outfmt, "emp. p-values", proteinChangedDegreePValue, proteinChangedClustCoeffPValue, proteinChangedTopologCoeffPValue, proteinChangedNeighbHConnPValue));
			
			writer.close();
			System.out.println("Done.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	/**
	 * Saves the features of a group like chromosome, snp (if present)
	 * @param group
	 * @param file
	 */
	public void saveGroupFeatures(HashSet<Protein> group, String file)
	{
		try
		{
			System.out.printf("Saving group features to %s.\n", file);
			BufferedWriter writer = new BufferedWriter( new FileWriter(file) );
			
			for(Protein p : group)
			{
				writer.write("Protein:\t" + p.getEnsemblProteinId() + "\n");
				if(p.hasGO())
				{
					writer.write("GO_Term_Accession:\t" + p.getGo().get("Accession") + "\n");
					writer.write("GO_Term_Name:\t" + p.getGo().get("Name") + "\n");
					writer.write("GO_Domain:\t" + p.getGo().get("Domain") + "\n");
				}
				if(p.hasGene())
				{
					writer.write("UniProt_Genename:\t" + p.getGene().getName() + "\n");
					writer.write("Chromosome:\t" + p.getGene().getChromosome() + "\n");
					if(p.getGene().hasSNP())
					{
						for(SNP snp : p.getGene().getSnps())
							writer.write("SNP:\t" + snp.getId() + "\t" + snp.getPositionOnChromosome() + "\t" + snp.getFromNT() + "\t" + snp.getToNT() + "\t" + snp.getFunctionClass() + "\n");
					}
				}
				
				if(p.has2DGelData())
				{
					for(TwoDGelData data : p.getGelData().get(consomicStrain))
						writer.write("Spot:\t" + data.getSpotNr() + "\t" + Double.toString(data.getRatio()) + "\n");
				}
			}
			writer.close();
			System.out.println("Done.");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
