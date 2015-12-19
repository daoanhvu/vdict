package nautilus.vdict.data;

import java.nio.charset.Charset;
import java.util.HashMap;

public class WordMean
{
	public static final HashMap<Byte,String> DomainMap = new HashMap<Byte,String>()
	{
		{
			put((byte)0, "General");
			put((byte)1, "Khoa hoc may tinh");
			put((byte)2, "Toan hoc");
			put((byte)3, "Vat ly");
			put((byte)4, "Hoa hoc");
			put((byte)5, "Sinh hoc");
			put((byte)6, "Quan su");
		}
	};
			
	private String usage;
	private String mean;
	private String example;
	private byte domain;
	
	public WordMean(String m, String ex, String us, byte dm)
	{
		domain = dm;
		usage = us;
		mean = m;
		example = ex;
	}
	
	public short getLength()
	{
        short length = 0;
		if(usage != null)
			length += (short)((usage.getBytes(Charset.forName("utf-8"))).length);
		if(mean != null)
			length += (short)((mean.getBytes(Charset.forName("utf-8"))).length);
		if(example != null)
			length += (short)((example.getBytes(Charset.forName("utf-8"))).length);
		
		return length;
	}
	
	public String getUsage()
	{
		return usage;
	}
	
	public void setUsage(String val)
	{
		usage = val;
	}
	
	public String getMean()
	{
		return mean;
	}
	
	public void setMean(String meanValue)
	{
		mean = meanValue;
	}
	
	public String getExample()
	{
		return example;
	}
	
	public void setExample(String exValue)
	{
		example = exValue;
	}

    /*
    *   Moi mean co the co nhieu example.
    *   moi example cach nhau bang mot dau ;
    * */
    public String[] getExamples()
    {
        if(example == null)
            return null;
        String[] ex = example.split(";");

        return ex;
    }

	public byte getDomain()
	{
		return domain;
	}
	
	public void setDomain(byte val)
	{
		domain = val;
	}
	
	@Override
	public String toString()
	{
		return mean + " - " + example;
	}
}
