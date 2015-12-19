package nautilus.vdict.data;

public class PartOfSpeed
{
	public static final byte VERB = 0;
	public static final byte NOUN = 1;
	public static final byte ADVERB = 2;
	
	private byte partCode;
	private String mean;
	private String example;
	
	public PartOfSpeed()
	{
		partCode = 0;
		mean = null;
		example = null;
	}
	
	public String getMean()
	{
		return mean;
	}
	
	public void setMean(String value)
	{
		mean = value;
	}
	
	public String getExample()
	{
		return example;
	}
	
	public void setExample(String value)
	{
		example = value;
	}
	
	public byte getPartCode()
	{
		return partCode;
	}
	
	public void setPartCode(byte value)
	{
		partCode = value;
	}
}