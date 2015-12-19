package nautilus.vdict.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class VDictUtility
{
	public static byte[] long2Bytes(long value) throws IOException
	{
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();  
	    DataOutputStream dos = new DataOutputStream(bos);  
	    dos.writeLong(value);
	    byte[] data = bos.toByteArray();
	    
	    return data;
	}
	
	public static byte[] getBytesFitLen(byte[] data, int len)
	{
		byte[] rs = new byte[len];
		
		for(int i=0;i<len;i++)
			rs[i] = i<data.length?data[i]:0;
		
		return rs;
	}
}
