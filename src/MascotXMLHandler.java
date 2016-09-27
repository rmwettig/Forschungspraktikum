import java.util.ArrayList;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class MascotXMLHandler extends DefaultHandler{
	/*
	 *  <hits> 
	 *  	<hit>
	 *  	 <protein>
	 *  		<peptide>
	 *  		</peptide>
	 *  	</protein>
	 *   </hit>
	 *  </hits>
	 * Probe Nr
	 * Hit Score <prot_score>
	 * Hit Seq Cov <prot_cover> ?
	 * emPAI <prot_empai>
	 * mat. Pep <pep_exp_mz> 
	 * mat. Queries, <prot_matches>
	 * Hit Mass, <prot_mass>
	 * pI <prot_pi>
	 * HitAcc <protein accession="">
	 * Hit Protein Name, <prot_name>
	 * result_url <URI>
	 * threshold <ignoreionsscorebelow>
	 *  
	 *  datei, <FILENAME>
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	
	//queries = all measured peaks
	//matched peptides = all unique peaks
	//Matrix Science: prot_matches equals queries !!
	
	//tag definition
	String hitScoreTag = "prot_score";
	String hitSeqCovTag = "prot_cover";
	String empaiTag = "prot_empai";
	String matQueriesTag = "prot_matches";
	String hitMassTag = "prot_mass";
	String protPiTag = "prot_pi";
	String proteinTag = "protein";
	String proteinNameTag = "prot_desc";
	String resultUrlTag = "URI";
	String thresholdTag = "ignoreionsscorebelow";
	String filenameTag = "FILENAME";
	String matPeptideTag = "pep_exp_mz"; //mass over charge value ?
	
	//inside tag flags
	boolean inScore = false;
	boolean inSeqCov = false;
	boolean inEmpai = false;
	boolean inMatQueries = false;
	boolean inMass = false;
	boolean inPi = false;
	//boolean inProtein = false;
	boolean inName = false;
	boolean inResultUrl = false;
	boolean inThreshold = false;
	boolean inFilename = false;
	boolean inMatPeptide = false;
	
	//global values
	String resultUrl = "";
	String threshold = "";
	String filename = "";
	String probeNr = "";
	String inputDataFile = "";
	
	//protein-specific
	String score = "";
	String seqCov = "";
	String empai = "";
	String matQueries = ""; 
	String mass = "";
	String pi = "";
	String accession = "";
	String name = "";
	//String matPep = "";
	
	//int matPepCounter = 0;
	ArrayList<String> matPep = new ArrayList<String>();
	
	String line = "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s";
	//String line2;
	
	ArrayList<String> output = new ArrayList<String>();
	
	public void startDocument() throws SAXException
	{
		threshold = "N/A";
		resultUrl = "N/A";
		filename = "N/A";
		probeNr = "N/A";
		inputDataFile = "N/A";
	}
		
	public String ExtractTrialNumber(String s)
	{
		int end = s.lastIndexOf("-");
		int beg = end - 1;
		boolean begFound = false;
		while(!begFound)
		{
			if(s.charAt(beg) != '-')
				beg--;
			else
				begFound = true;
		}
		return s.substring(beg + 1, end);
	}
	
	public void ResetValues()
	{
		score = "N/A";
		seqCov = "N/A";
		empai = "N/A";
		matQueries = "N/A";
		mass = "N/A";
		pi = "N/A";
		accession = "N/A";
		name = "N/A";
		//matPep = "N/A";
		//matPepCounter = 0;
		matPep.clear();
		//probeNr = "N/A";
	}
	
	public void startElement(String uri, String localName, String qname, Attributes attr) throws SAXException
	{		
		if(localName.equalsIgnoreCase(proteinTag))
		{
			ResetValues();
			accession = attr.getValue(0);
			//line2 = "Probe: %s Score: %s SeqCov: %s Empai: %s MatPep: %s matQry: %s Mass: %s PI: %s Accession: %s Name: %s Thresh: %s URL: %s";
		}
		else if(localName.equalsIgnoreCase(hitScoreTag))
			inScore = true;
		else if(localName.equalsIgnoreCase(hitSeqCovTag))
			inSeqCov = true;
		else if (localName.equalsIgnoreCase(hitMassTag))
			inMass = true;
		else if (localName.equalsIgnoreCase(empaiTag))
			inEmpai = true;
		else if (localName.equalsIgnoreCase(matQueriesTag))
			inMatQueries = true;
		else if (localName.equalsIgnoreCase(protPiTag))
			inPi = true;
		else if (localName.equalsIgnoreCase(thresholdTag))
			inThreshold = true;
		else if (localName.equalsIgnoreCase(resultUrlTag))
			inResultUrl = true;
		else if (localName.equalsIgnoreCase(proteinNameTag))
			inName = true;
		else if (localName.equalsIgnoreCase(filenameTag))
			inFilename = true;
		else if (localName.equalsIgnoreCase(matPeptideTag))
			inMatPeptide = true;
		
	}
	
	public void endElement(String uri, String localName, String qname) throws SAXException
	{
		if(localName.equalsIgnoreCase(proteinTag))
		{
			//for each protein create a row
			//as it wraps the protein information tags there is no need for the inside protein flag

			//System.out.println(String.format("Acc: %s Score: %s SeqCov: %s Mass: %s Empai: %s", accession, score, seqCov, mass, empai));
			String s = String.format(line, filename, probeNr, score, seqCov, empai, Integer.toString(matPep.size()), matQueries, mass, pi, accession, name, threshold, resultUrl, inputDataFile);
			//System.out.println(s);
			output.add(s);
		}
		else if(localName.equalsIgnoreCase(hitScoreTag))
			inScore = false;
		else if(localName.equalsIgnoreCase(hitSeqCovTag))
			inSeqCov = false;
		else if (localName.equalsIgnoreCase(hitMassTag))
			inMass = false;
		else if (localName.equalsIgnoreCase(empaiTag))
			inEmpai = false;
		else if (localName.equalsIgnoreCase(matQueriesTag))
			inMatQueries = false;
		else if (localName.equalsIgnoreCase(protPiTag))
			inPi = false;
		else if (localName.equalsIgnoreCase(thresholdTag))
			inThreshold = false;
		else if (localName.equalsIgnoreCase(resultUrlTag))
			inResultUrl = false;
		else if (localName.equalsIgnoreCase(proteinNameTag))
			inName = false;
		else if (localName.equalsIgnoreCase(filenameTag))
			inFilename = false;
		else if (localName.equalsIgnoreCase(matPeptideTag))
			inMatPeptide = false;
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		String s = new String(ch, start, length).trim();
		if(inScore)
			score = s;
		else if (inSeqCov)
			seqCov = s.replace('.',',');
		else if (inMass)
			mass = s;
		else if (inEmpai)
			empai = s.replace('.', ',');
		else if (inMatQueries)
			matQueries = s;
		else if (inPi)
			pi = s.replace('.', ',');
		else if (inThreshold)
			threshold = s;
		else if (inResultUrl)
			resultUrl = s;
		else if (inName)
			name = s.substring(0, s.indexOf("OS")).trim(); //extract name
		else if (inFilename)
		{
			inputDataFile = s;
			filename = s.substring(s.lastIndexOf("\\")+1, s.length()); //*.mgf part
			probeNr = ExtractTrialNumber(filename);
		}
		else if (inMatPeptide)
		{
			s = s.substring(0, s.indexOf(".")); //consider integer numbers only
			if(!matPep.contains(s)) //collect distinct peptide masses
				matPep.add(s);	
		}
	}
	
	public ArrayList<String> GetDataList()
	{
		return this.output;
	}
}
