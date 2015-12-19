package nautilus.vdict.data;

import java.nio.charset.Charset;

public class Idiom 
{
	private PartOfSpeech parent;
	private WordMean mean;
	private String idm;
	
	private short length = 0;
	
	public Idiom()
	{
		parent = null;
		mean = null;
		idm = null;
	}
	
	public short getLength()
	{
		if(length <= 0)
		{
			length = (short)(idm.getBytes(Charset.forName("utf-8"))).length;
			length += (mean.getLength()) + 1;
		}
		
		return length;
	}
	
	public PartOfSpeech getParent() {
		return parent;
	}
	public void setParent(PartOfSpeech parent) {
		this.parent = parent;
	}
	
	public WordMean getMean() {
		return mean;
	}
	public void setMean(WordMean mean) {
		this.mean = mean;
	}
	
	public String getIdm() {
		return idm;
	}
	public void setIdm(String idm) {
		this.idm = idm;
	}
}
