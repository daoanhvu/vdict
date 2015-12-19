package nautilus.vdict;

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

    public static long byte2LongBigEndian(byte[] b)
    {
        long value = 0;
        //the first byte the most significant byte
        for(int i=0;i<7;i++)
          value = (value<<8) + (b[i]&0xff);

        return value;
    }

    public static long bytes2LongLittleEndian(byte[] b)
    {
        long value = 0;
        //the first byte is the least significant byte
        for (int i = 0; i < 8; i++)
            value += ((long) b[i] & 0xffL) << (8 * i);
        return value;
    }
	
	public static byte[] getBytesFitLen(byte[] data, int len)
	{
		byte[] rs = new byte[len];
		
		for(int i=0;i<len;i++)
			rs[i] = i<data.length?data[i]:0;
		
		return rs;
	}
}
