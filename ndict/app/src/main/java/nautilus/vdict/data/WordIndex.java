package nautilus.vdict.data;

public class WordIndex
{
	private long address;
	private String word;
	private byte[] hashCode = new byte[4];
	
	private WordIndex next;
	
	public WordIndex()
	{
		address = -1;
		next = null;
	}
	
	public long getAddress()
	{
		return address;
	}
	
	public String getWord()
	{
		return word;
	}
	
	public void setWord(String w)
	{
		word = w;
	}
	
	public void setAddress(long val)
	{
		address = val;
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
	
	public void setNext(WordIndex _next)
	{
		if( next != null )
		{
			next.setNext(_next);
			return;
		}
		
		next = _next;
	}
	
	public WordIndex getNext()
	{
		return next;
	}
}