package nautilus.vdict.data;

import java.nio.charset.Charset;
import java.util.List;
import java.util.ArrayList;

public class WordData
{
	private WordIndex index;
	private String wordString;

	private List<PartOfSpeech> parts = null;
	private byte[] hashCode = new byte[4]; //Use Soundex algorithm to hash the word
	private String relativeWord = null;
	
	public WordData()
	{
		index = null;
	}
	
	public short getLength()
	{
        short length = 1;
		if(relativeWord != null)
		    length += (short) (relativeWord.getBytes(Charset.forName("utf-8"))).length;
			
		for(int i=0;i<parts.size();i++)
			length += parts.get(i).getLength();

		return length;
	}
	
	public long getAddress()
	{
		return index!=null?index.getAddress():-1;
	}
	
	public WordIndex getIndex()
	{
		return index;
	}
	
	public void setIndex(WordIndex value)
	{
		index = value;
	}
	
	public String getWordString() 
	{
		return wordString;
	}

	public void setWordString(String wordString) 
	{
		this.wordString = wordString;
	}
	
	public List<PartOfSpeech> getParts()
	{
		return parts;
	}
	
	public void setParts(List<PartOfSpeech> _parts)
	{
		parts = _parts;
	}
	
	public void addPart(PartOfSpeech part)
	{
		if(parts == null)
			parts = new ArrayList<PartOfSpeech>();
		
		part.setWord(this);
		parts.add(part);
	}
	
	public void addPart(byte partCode, List<WordMean> means)
	{
        PartOfSpeech part = createPartByCode(partCode);
		part.setMeans(means);
		
		if(parts == null)
			parts = new ArrayList<PartOfSpeech>();
		
		part.word = this;
		parts.add(part);
	}
	
	public void removePart(PartOfSpeech part)
	{
		parts.remove(part);
	}
	
	public void removePart(Byte pc)
	{
		for(PartOfSpeech p: parts)
		{
			if(p.getPartCode() == pc)
			{
				parts.remove(p);
				return;
			}
		}
	}
	
	public void addMean(byte partCode, String mean, String example, String pronuciation, String usage, String pastForm, byte dm)
	{
        PartOfSpeech part;
		
		if(parts == null)
			parts = new ArrayList<PartOfSpeech>();
		
		for(PartOfSpeech p: parts)
		{
			if(p.getPartCode() == partCode)
			{
				p.addMean(mean, example, usage, dm);
				return;
			}
		}
		
		part = createPartByCode(partCode);
		
		//part.setPartCode(partCode);
		part.setPronunciation(pronuciation);
		part.addMean(mean, example, usage, dm);
		//part.setPastForm(pastForm);
		parts.add(part);
	}
	
	public PartOfSpeech getPartByCode(byte pc)
	{
		for(PartOfSpeech p: parts)
		{
			if(p.getPartCode() == pc)
			{
				return p;
			}
		}
		
		return null;
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
	
	public String getRelativeWord()
	{
		return relativeWord;
	}
	
	public void setRelativeWord(String value)
	{
		relativeWord = value;
	}
	
	public static PartOfSpeech createPartByCode(byte partCode)
	{
        PartOfSpeech p = null;
		
		switch(partCode)
		{
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