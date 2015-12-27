package nautilus.vdict.data;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.ArrayList;

public class WordData {
	private WordIndex index;
	private String word;
	
	private List<PartOfSpeech> parts = null;
	private byte[] hashCode = new byte[4]; //Use Soundex algorithm to hash the word
	private String relativeWord = null;
	
	public WordData() {
		index = null;
		word = null;
	}

	public short getLength() {
		short length = 1;
		try {
			if (relativeWord != null)
				length += (short) (relativeWord.getBytes("utf-8")).length;
		}catch (UnsupportedEncodingException ex){}

		for(int i=0;i<parts.size();i++)
			length += parts.get(i).getLength();

		return length;
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
	
	public void addPart(byte partCode, List<WordMean> means) {
		PartOfSpeech part = createPartByCode(partCode);
		part.setMeans(means);
		
		if(parts == null)
			parts = new ArrayList<PartOfSpeech>();
		
		parts.add(part);
	}

	public String getRelativeWord()	{
		return relativeWord;
	}

	public void setRelativeWord(String value) {
		relativeWord = value;
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

	public static PartOfSpeech createPartByCode(byte partCode) {
		PartOfSpeech p = null;

		switch(partCode) {
			case 0:
				p = new Noun();
				break;

			case 1:
			case 2:
				p = new Verb(partCode);
				break;

			case 3:
				p = new Adverb();
				break;

			case 4:
				p = new Adjective();
				break;

			case 5:
				p = new Article();
				break;

			case 6:
				p = new Preposition();
				break;
		}

		return p;
	}
}//end class WordData