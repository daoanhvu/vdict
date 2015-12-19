package nautilus.vdict.data;

import java.util.List;
import java.util.ArrayList;

public class WordData
{
	private long address;
	private String word;
	
	private List<PartOfSpeed> parts = null;
	private byte[] hashCode = new byte[4]; //Use Soundex algorithm to hash the word
	
	public WordData()
	{
		word = null;
	}
	
	public long getAddress()
	{
		return address;
	}
	
	public void setAddress(long value)
	{
		address = value;
	}
	
	public String getWord()
	{
		return word;
	}
	
	public void setWord(String value)
	{
		word = value;
	}
	
	public List<PartOfSpeed> getParts()
	{
		return parts;
	}
	
	public void setParts(List<PartOfSpeed> _parts)
	{
		parts = _parts;
	}
	
	public void addPart(PartOfSpeed part)
	{
		if(parts == null)
			parts = new ArrayList<PartOfSpeed>();
		
		parts.add(part);
	}
	
	public void addPart(byte partCode, String meaning, String example)
	{
		PartOfSpeed part = new PartOfSpeed();
		part.setPartCode(partCode);
		part.setMean(meaning);
		part.setExample(example);
		
		if(parts == null)
			parts = new ArrayList<PartOfSpeed>();
		
		parts.add(part);
	}
	
	public byte[] getHashCode()
	{
		return hashCode;
	}
	
	public void setHashCode(byte[] hc, int offset)
	{
		hashCode = new byte[4];
		
		hashCode[0] = hc[offset];
		hashCode[1] = hc[offset+1];
		hashCode[2] = hc[offset+2];
		hashCode[3] = hc[offset+3];
	}
}//end class WordData