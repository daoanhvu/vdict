package nautilus.vdict.data;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class PartOfSpeech
{
	protected WordData word;
	protected byte partCode;
	protected String pronunciation;
	protected List<WordMean> means;
	protected List<Idiom> idms;
	protected String description;
	
	public static final HashMap<Byte,String> PartName = new HashMap<Byte,String>()
	{
		{
			put((byte)0, "n"); 		//noun
			put((byte)1, "tr. v"); 		//transitive verb
			put((byte)2, "intr. v");	//intransitive verb
			put((byte)3, "adv"); 	//adverb
			put((byte)4, "adj"); 	//adjective
			put((byte)5, "art"); 	//Article
			put((byte)6, "pre"); 	//preposition
		}
	};
	
	public PartOfSpeech()
	{
		partCode = 0;
		means = new ArrayList<WordMean>();
	}
	
	public PartOfSpeech(WordData w)
	{
		word = w;
		partCode = 0;
		means = new ArrayList<WordMean>();
		
		word.addPart(this);
	}
	
	public short getLength()
    {
        byte[] temp;
        short length;

        temp = pronunciation.getBytes(Charset.forName("utf-8"));
        length = (short)temp.length;
        length += 2;//part code + 1byte pronunciation length

        for(int i=0;i<means.size();i++)
            length += means.get(i).getLength();

        for(int i=0;i<idms.size();i++)
            length += idms.get(i).getLength();

        if(description != null)
            length += (short)(description.getBytes(Charset.forName("utf-8"))).length + 1;


        return length;
    }
	
	
	public byte getPartCode()
	{
		return partCode;
	}
	
	public void setPartCode(byte value)
	{
		partCode = value;
	}
	
	public WordData getWord()
	{
		return word;
	}
	
	public void setWord(WordData wd)
	{
		word = wd;
	}
	
	public String getPartText()
	{
		return PartOfSpeech.PartName.get(partCode);
	}
	
	public String getPronunciation()
	{
		return pronunciation;
	}
	
	public void setPronunciation(String value)
	{
		pronunciation = value;
	}
	
	public void setMeans(List<WordMean> value)
	{
		means = value;
	}
	
	public List<WordMean> getMeans()
	{
		return means;
	}
	
	public void setIdms(List<Idiom> value)
	{
		idms = value;
	}
	
	public List<Idiom> getIdms()
	{
		return idms;
	}
	
	public void addMean(WordMean mean)
	{
		if(means == null)
			means = new ArrayList<WordMean>();
		
		means.add(mean);
	}
	
	public void addMean(String m, String ex, String usg, byte dm)
	{
		WordMean mean = new WordMean(m, ex, usg, dm);
		if(means == null)
			means = new ArrayList<WordMean>();
		
		means.add(mean);
	}
	
	public void addIdiom(Idiom idm)
	{
		if(idms == null)
			idms = new ArrayList<Idiom>();
		
		idms.add(idm);
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String val)
	{
		description = val;
	}
	
	public boolean removeMean(WordMean mean)
	{
		if(means == null)
			return false;
		
		return means.remove(mean);
	}
}