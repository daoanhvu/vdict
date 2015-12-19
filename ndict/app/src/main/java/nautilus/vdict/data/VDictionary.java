package nautilus.vdict.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

import android.util.Log;

import android.content.Context;

public class VDictionary
{
	/* Implements the mapping
	   * from: AEHIOUWYBFPVCGJKQSXZDTLMNR
	   * to:   00000000111122222222334556
	   */
	private static final char[] MAP = {
	    //A  B   C   D   E   F   G   H   I   J   K   L   M
	    '0','1','2','3','0','1','2','0','0','2','2','4','5',
	    //N  O   P   W   R   S   T   U   V   W   X   Y   Z
	    '5','0','1','2','6','2','3','0','1','0','2','0','2'
	  };
	private static final int NUMOFWORDLOAD = 20;
	private static final int NUMINDEX = 7;
	//private static final short version = 1;
	
	private byte version;
	private String name;
	private File data_file;
	private File index_file;
	private WordIndex[][][][] hashTable = null;
	
	private Context _appContext;
	
	private VDictionary()
	{
		
	}
	
	public VDictionary(Context context)
	{
		name = "noname";
		_appContext = context;
	}
	
	public VDictionary(Context ctx, String name, String datafile, String indexfile)
	{
		this.name = name;
		this._appContext = ctx;
		this.data_file = new File(_appContext.getExternalFilesDir(null), datafile);
		this.index_file = new File(_appContext.getExternalFilesDir(null), indexfile);
		
		Log.i("[VDictionary Construct]", "data: " + data_file.getPath() + " " + data_file.getName() + " - index: " + index_file.getName());
	}
	
	public byte checkDataExisted()
	{
	
		if(!index_file.exists())
		{
			Log.e("", "Index file note found!");
			return -1;
		}
		
		if(!data_file.exists())
		{
			Log.e("", "Data file note found!");
			return -2;
		}
		 
		return 0;
	}
	
	public byte getVersion()
	{
		return version;
	}
	
	public String getName()
	{
		return name;
	}
	
	public WordData readWord(long address)
	{
		try{
			RandomAccessFile raf = new RandomAccessFile(data_file, "r");
			raf.seek(address);
			byte partCode, numOfPart = 0;
			WordData word = new WordData();
			PartOfSpeed part = null;
			numOfPart = raf.readByte();
			String mean, example;
			byte[] buffer = new byte[512];
			
			for(byte i=0; i<numOfPart; i++)
			{
				partCode = raf.readByte();
				
				//Read length of mean
				int len = raf.readInt();
				buffer = new byte[len];
				raf.read(buffer);
				mean = new String(buffer, 0, len, "utf-8");
				
				
				//Read len of Example
				len = raf.readInt();
				buffer = new byte[len];
				raf.read(buffer);
				example = new String(buffer, 0, len, "utf-8");
				
				part = new PartOfSpeed();
				part.setPartCode(partCode);
				part.setMean(mean);
				part.setExample(example);
				
				word.addPart(part);
			}
			raf.close();
			
			return word;
		}catch(IOException ex)
		{
			return null;
		}	
	}
	
	public long writeWord(WordData word)
	{
		try{
			if(!data_file.exists())
				data_file.createNewFile();
			
			RandomAccessFile raf = new RandomAccessFile(data_file, "rw");
			
			System.out.println("Address of word before asign: " + word.getAddress());
			
			if(word.getAddress() < 0)
				word.setAddress(raf.length());
			
			Log.i("VDictionary", data_file + "(size " + raf.length() + "); Address of word: " + word.getAddress());
			
			raf.seek(word.getAddress());
			
			int numOfPart = word.getParts().size(); 
			raf.writeByte(numOfPart);
			
			for(byte i=0; i<numOfPart; i++)
			{
				//write part code
				raf.writeByte(word.getParts().get(i).getPartCode());
				byte[] meanBytes = word.getParts().get(i).getMean().getBytes("utf-8");
				byte[] exampleBytes = word.getParts().get(i).getExample().getBytes("utf-8");
				
				//write length of mean
				raf.writeInt(meanBytes.length);
				raf.write(meanBytes);
				
				//write example
				raf.writeInt(exampleBytes.length);
				raf.write(exampleBytes);
			}
			
			raf.close();
			return word.getAddress();
		}catch(IOException ex)
		{
			ex.printStackTrace();
			return -1;
		}
	}
	
	public boolean loadIndex()
	{
		FileInputStream fileInputStream;
		boolean result = false;
		
		byte[] buffer;
		
		//First, we initialize the hastable
		hashTable = new WordIndex[26][NUMINDEX][NUMINDEX][NUMINDEX];
		for(int i=0;i<26;i++)
			for(int j=0;j<4;j++)
				for(int k=0;k<4;k++)
					for(int l=0;l<4;l++)
						hashTable[i][j][k][l] = null;
		try {
			//this is version for android so we used context to open file
			//fileInputStream = _appContext.openFileInput(index_file);
			fileInputStream = new FileInputStream(index_file);
			
			//read version
			this.version = (byte)fileInputStream.read();
			Log.i("LOAD_INDEX", "Read version successfully!");
			int byteRead = 0;
			do
			{
				WordIndex idx = new WordIndex();
				
				//read address of word start at 0 and is 8 bytes in length
				buffer = new byte[8];
				byteRead = fileInputStream.read(buffer, 0, 8);
				
				if(byteRead <=0)
					break;
				
				ByteBuffer bb = ByteBuffer.wrap(buffer, 0, 8);
				idx.setAddress(bb.getLong());
				
				//hash code of each word start at position 8 and is 4 bytes in length
				buffer = new byte[4];
				fileInputStream.read(buffer, 0, 4);
				idx.setHashCode(buffer, 0);
				
				//Read word length
				int wordlen = fileInputStream.read();
				if(wordlen <=0)
					break;
				
				buffer = new byte[wordlen];
				//word start at 12 and is 128 in length
				byteRead = fileInputStream.read(buffer, 0, wordlen);
				idx.setWord(new String(buffer, 0, wordlen, "utf-8"));
				
				//
				byte h1 = idx.getHashCode()[0];
				byte h2 = idx.getHashCode()[1];
				byte h3 = idx.getHashCode()[2];
				byte h4 = idx.getHashCode()[3];
				
				if(hashTable[h1][h2][h3][h4] == null)
				{
					hashTable[h1][h2][h3][h4] = idx;
				}
				else
					hashTable[h1][h2][h3][h4].setNext(idx);
				
				System.out.println("Load duoc: " + wordlen + " - " +idx.getWord() + " Address: " + idx.getAddress());
			}while(byteRead > 0);//end while
			
			fileInputStream.close();
			result = true;
		}
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();	
			Log.e("LOAD_INDEX: FileNotFound", e.getMessage());
		}
		catch(IOException ioe)
		{
			Log.e("LOAD_INDEX: IOException", ioe.getMessage());
		}
		return result;
	}
	
	public WordIndex addWord2HashTable(WordData word)
	{
		WordIndex index = new WordIndex();
		index.setWord(word.getWord());
		
		byte[] hc = this.hash(index.getWord());
		index.setHashCode(hc, 0);
		
		byte h1 = index.getHashCode()[0];
		byte h2 = index.getHashCode()[1];
		byte h3 = index.getHashCode()[2];
		byte h4 = index.getHashCode()[3];
		
		//System.out.println(h1 + " " + h2 + " " + h3 + " " + h4);
		
		
		if(hashTable[h1][h2][h3][h4] == null)
		{
			hashTable[h1][h2][h3][h4] = index;
			
			//write word to data file
			hashTable[h1][h2][h3][h4].setAddress(this.writeWord(word));
		}
		else
		{
			WordIndex temp = hashTable[h1][h2][h3][h4];
			
			while(temp!=null && (!word.getWord().trim().toUpperCase().equals(temp.getWord().trim().toUpperCase())))
				temp = temp.getNext();
				
			if(temp==null)
				hashTable[h1][h2][h3][h4].setNext(index);
			else
			{
				word.setAddress(temp.getAddress());
				//write word to data file at old address
				this.writeWord(word);
			}
		}
		
		
		return index;
	}
	
 	private byte[] hash(String strWord)
	{
		byte[] hc = new byte[4];
		String hashString = soundex(strWord);
		
		//firs
		hc[0] = (byte) (hashString.charAt(0) - 'A');
		hc[1] = (byte)((byte)hashString.charAt(1) - 48); 
		hc[2] = (byte)((byte)hashString.charAt(2) - 48);
		hc[3] = (byte)((byte)hashString.charAt(3) - 48);
		return hc;
	}
	
	private String soundex(String s) {

	    // Algorithm works on uppercase (mainframe era).
	    String t = s.toUpperCase();

	    StringBuffer res = new StringBuffer();
	    char c, prev = '?';

	    // Main loop: find up to 4 chars that map.
	    for (int i=0; i<t.length() && res.length() < 4 &&
	      (c = t.charAt(i)) != ','; i++) {

	      // Check to see if the given character is alphabetic.
	      // Text is already converted to uppercase. Algorithm
	      // only handles ASCII letters, do NOT use Character.isLetter()!
	      // Also, skip double letters.
	      if (c>='A' && c<='Z' && c != prev) {
	        prev = c;

	        // First char is installed unchanged, for sorting.
	        if (i==0)
	          res.append(c);
	        else {
	          char m = MAP[c-'A'];
	          if (m != '0')
	            res.append(m);
	        }
	      }
	    }
	    if (res.length() == 0)
	      return null;
	    for (int i=res.length(); i<4; i++)
	      res.append('0');
	    return res.toString();
	  }
	
	public ArrayList<WordIndex> initListWordIndex(String strWord)
	{
		ArrayList<WordIndex> wordlist = new ArrayList<WordIndex>();
		byte[] hashCode = hash(strWord);
		WordIndex idx;
		int a = hashCode[0];
		int b = hashCode[1];
		int c = hashCode[2];
		int d = hashCode[3];
		
		int count=0, i;
		boolean flag = true;
		for(i=a;i<26 && flag;i++)
			for(int j=b;j<NUMINDEX && flag;j++)
				for(int k=c;k<NUMINDEX && flag;k++)
					for(int l=d;l<NUMINDEX && flag;l++)
					{
						idx = hashTable[i][j][k][l];
						while(idx != null)
						{
							wordlist.add(idx);
							idx = idx.getNext();
							count++;
						}
						
						if(count > 20)
							flag = false;
					}
		return wordlist;
	}
	
	public ArrayList<String> initListWordString(String strWord)
	{
		ArrayList<String> wordlist = new ArrayList<String>();
		byte[] hashCode = hash(strWord);
		WordIndex idx;
		int a = hashCode[0];
		int b = hashCode[1];
		int c = hashCode[2];
		int d = hashCode[3];
		
		int count=0, i;
		boolean flag = true;
		for(i=a;i<26 && flag;i++)
			for(int j=b;j<NUMINDEX && flag;j++)
				for(int k=c;k<NUMINDEX && flag;k++)
					for(int l=d;l<NUMINDEX && flag;l++)
					{
						idx = hashTable[i][j][k][l];
						while(idx != null)
						{
							wordlist.add(idx.getWord());
							idx = idx.getNext();
							count++;
						}
						
						if(count > 20)
							flag = false;
					}
		return wordlist;
	}
	
	public WordIndex findIndex(String word)
	{
		byte[] hashCode = hash(word);
		WordIndex idx = null;
		int a = hashCode[0];
		int b = hashCode[1];
		int c = hashCode[2];
		int d = hashCode[3];
		
		if(hashTable[a][b][c][d] != null)
		{
			WordIndex temp = hashTable[a][b][c][d];
			
			while(!temp.getWord().equals(word))
			{
				temp = temp.getNext();
			}
			
			if(temp != null)
				idx = temp;
		}
		
		return idx;
	}
	
	public boolean overwriteIndex()
	{
		try{
			FileOutputStream os = new FileOutputStream(index_file);
			
			os.write(this.version);
			
			for(int i=0;i<26;i++)
				for(int j=0;j<NUMINDEX;j++)
					for(int k=0;k<NUMINDEX;k++)
						for(int l=0;l<NUMINDEX;l++)
						{
							if(hashTable[i][j][k][l] != null)
							{
								byte[] byteAddress = nautilus.vdict.util.VDictUtility.long2Bytes(hashTable[i][j][k][l].getAddress());
								byte[] word = hashTable[i][j][k][l].getWord().getBytes("utf-8");
								os.write(byteAddress, 0, 8);
								os.write(hashTable[i][j][k][l].getHashCode(), 0, 4);
								os.write(word.length);
								os.write(word, 0, word.length);
							}
						}
			os.close();
			
			return true;
		}catch(IOException ex)
		{
			return false;
		}
	}
}
