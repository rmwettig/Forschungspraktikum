package de.charite.compbio.ppi;

public class TwoDGelData 
{
	private int spotNr;
	private String consomic;
	private double ratio;
	//redundant information - perhaps for debugging
	//String chromosome;
	//String geneName;
	private char gender;
	private char changeDirection; //u,d,n
	private boolean translationHasChanged = false;
	
	public int getSpotNr() {
		return spotNr;
	}

	public void setSpotNr(int spotNr) {
		this.spotNr = spotNr;
	}

	public String getConsomic() {
		return consomic;
	}

	public void setConsomic(String consomic) {
		this.consomic = consomic;
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public char getChangeDirection() {
		return changeDirection;
	}

	public void setChangeDirection(char changeDirection) {
		this.changeDirection = changeDirection;
	}

	public boolean hasChanged() {
		return translationHasChanged;
	}

	public void setTranslationHasChanged(boolean translationHasChanged) {
		this.translationHasChanged = translationHasChanged;
	}

	@Override
	public String toString()
	{
		return String.format("%s\t%d\t%.2f\t%s\t%s", consomic, spotNr, ratio, Boolean.toString(translationHasChanged), changeDirection);
	}
}
