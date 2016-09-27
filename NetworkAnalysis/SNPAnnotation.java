
public class SNPAnnotation
{
	int id = -1;
	String gene = "";
	String functionClass = "";
	int posOnChr = 0;
	char chromosome = 0;
	char refNT = 0;
	char strainNT = 0;
	char readingStrandOrientation = 0;
	
	String variationType ="";
	int assays = 0;
	
	public void extractAnnotation(String s, String delimiter) 
	{
		String[] data = s.split(delimiter);
		
		this.id = Integer.parseInt(data[0].substring(data[0].indexOf("s") + 1));
		
		this.chromosome = (char) Integer.parseInt(data[1].substring(data[1].lastIndexOf("r") + 1, data[1].lastIndexOf(":") - 1));
		this.posOnChr = Integer.parseInt(data[1].substring(data[1].lastIndexOf(":") + 1));
		
		this.readingStrandOrientation = (char) Integer.parseInt(data[2]);
		
		String[] subdata = data[3].split(":");
		this.gene = subdata[0].trim();
		this.functionClass = subdata[1].trim();
		
		this.assays = Integer.parseInt(data[4]);
		
		this.variationType = data[5];
		
		this.refNT = (char) Integer.parseInt(data[6]);
		this.strainNT = (char) Integer.parseInt(data[7]);
	}

}
