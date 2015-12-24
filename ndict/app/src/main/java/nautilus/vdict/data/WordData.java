package nautilus.vdict.data;

import java.util.List;
import java.util.ArrayList;

public class WordData {
	private WordIndex index;
	private String word;
	
	private List<PartOfSpeech> parts = null;
	private byte[] hashCode = new byte[4]; //Use Soundex algorithm to hash the word
	
	public WordData() {
		index = null;
		word = null;
	}
	
	public long getAddress() {
		return index.getAddress();
	}

	public WordIndex getIndex() {
		return index;
	}
	
	public void setIndex(WordIndex indexObj) {
		index = indexObj;
	}
	
	public String getWord()
	{
		return word;
	}
	
	public void setWord(String value)
	{
		word = value;
	}
	
	public List<PartOfSpeech> getParts()
	{
		return parts;
	}
	
	public void setParts(List<PartOfSpeech> _parts)
	{
		parts = _parts;
	}
	
	public void addPart(PartOfSpeech part) {
		if(parts == null)
			parts = new ArrayList<PartOfSpeech>();
		
		parts.add(part);
	}
	
	public void addPart(byte partCode, String meaning, String example) {
		PartOfSpeech part = new PartOfSpeech();
		part.setPartCode(partCode);
		part.setMean(meaning);
		part.setExample(example);
		
		if(parts == null)
			parts = new ArrayList<PartOfSpeech>();
		
		parts.add(part);
	}
	
	public byte[] getHashCode()
	{
		return hashCode;
	}
	
	public void setHashCode(byte[] hc, int offset) {
		hashCode = new byte[4];
		
		hashCode[0] = hc[offset];
		hashCode[1] = hc[offset+1];
		hashCode[2] = hc[offset+2];
		hashCode[3] = hc[offset+3];
	}
}//end class WordData