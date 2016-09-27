public class SNPProperties
{

	private class ExchangedNT
	{
		char from;
		char to;

		ExchangedNT(char f, char t)
		{
			this.from = f;
			this.to = t;
		}
	}

	private class PeptideImpact
	{
		String fromTriplet;
		String toTriplet;
		char fromAA;
		char toAA;
		int atPos;

		PeptideImpact(String fT, String tT, String exchanged)
		{
			this.fromTriplet = fT;
			this.toTriplet = tT;
			Object[] temp = ExtractMutationInfo(exchanged);
			this.fromAA = (Character) temp[0];
			this.toAA = (Character) temp[1]; 
			this.atPos = Integer.parseInt((String) temp[2]);

		}

		private Object[] ExtractMutationInfo(String s)
		{
			Object[] info = new Object[3];
			info[0] = s.charAt(0);
			String pos = "";
			for(int p = 1; p < s.length(); p++)
			{
				char c = s.charAt(p);
				if(Character.isDigit(c))
					pos += Character.toString(c);
				else
					info[1] = c;
			}
			info[2] = pos;
			return info;
		}
	}

	String gene;
	String id;
	char chromosome;
	int posOnChr;
	ExchangedNT snp;
	PeptideImpact protMutation;

	SNPProperties() { };
	
	SNPProperties(String id, String gene, char chr, int pos, char from, char to, String fromTri, String toTri, String exchAA) 
	{
		this.id = id;
		this.gene = gene;
		this.chromosome = chr;
		this.posOnChr = pos;
		
		this.snp = new ExchangedNT(from, to);
		
		this.protMutation = new PeptideImpact(fromTri, toTri, exchAA);
		
	}
	
	public void addAnnotation(String id, String gene, char chr, int pos, char from, char to, String fromTri, String toTri, String exchAA)
	{
		this.id = id;
		this.gene = gene;
		this.chromosome = chr;
		this.posOnChr = pos;
		
		this.snp = new ExchangedNT(from, to);
		
		this.protMutation = new PeptideImpact(fromTri, toTri, exchAA);
	}
	
}
