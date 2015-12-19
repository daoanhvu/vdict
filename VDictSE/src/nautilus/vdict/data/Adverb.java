package nautilus.vdict.data;

import java.nio.charset.Charset;

public class Adverb extends PartOfSpeech
{
	private String comparative;
	private String superlative;
	
	public Adverb()
	{
		partCode = 3;
		comparative = null;
		superlative = null;
	}

    @Override
    public short getLength()
    {
        short length = super.getLength();
        length += (comparative==null?0:(comparative.getBytes(Charset.forName("utf-8"))).length) + 1;
        length += superlative==null?0:(superlative.getBytes(Charset.forName("utf-8"))).length + 1;
        return length;
    }
	
	public Adverb(WordData w)
	{
		word = w;
		word.addPart(this);
		partCode = 3;
		comparative = null;
		superlative = null;
	}
	
	public String getComparative()
	{
		return comparative;
	}
	
	public void setComparative(String val)
	{
		comparative = val;
	}
	
	public String getSuperlative()
	{
		return superlative;
	}
	
	public void setSuperlative(String val)
	{
		superlative = val;
	}
	
}
