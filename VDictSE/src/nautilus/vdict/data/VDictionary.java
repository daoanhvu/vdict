package nautilus.vdict.data;

import nautilus.vdict.VDictUtility;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class VDictionary {
	/* Implements the mapping
	   * from: AEHIOUWYBFPVCGJKQSXZDTLMNR
	   * to:   00000000111122222222334556
	   */
	private static final char[] SOUNDEX_MAP_ENGLISH = {
	    //A  B   C   D   E   F   G   H   I   J   K   L   M
	    '0','1','2','3','0','1','2','0','0','2','2','4','5',
	    //N  O   P   W   R   S   T   U   V   W   X   Y   Z
	    '5','0','1','2','6','2','3','0','1','0','2','0','2'
	  };
	
	private static final char[] SOUNDEX_MAP_FRENCH = {
	    //A  B   C   D   E   F   G   H   I   J   K   L   M
	    '0','1','2','3','0','1','2','0','0','2','2','4','5',
	    //N  O   P   W   R   S   T   U   V   W   X   Y   Z
	    '5','0','1','2','6','2','3','0','1','0','2','0','2'
	  };
	
	private static final int NUMOFWORDLOAD = 20;
	private static final int NUMINDEX = 7;
	private static final String UTF8 = "utf-8";
	
	private byte version;
	private String name;
	private String data_file;
	private String index_file;
	private WordIndex[][][][] hashTable = null;
	
	public VDictionary() {
		name = "noname";
	}
	
	public VDictionary(String name, String datafile, String indexfile) {
		this.name = name;
		this.data_file = datafile;
		this.index_file = indexfile;
	}
	
	public byte getVersion() {
		return version;
	}
	
	public String getName()	{
		return name;
	}
	
	public WordData readWord(long address) throws IOException {
		short length = 0;
        RandomAccessFile raf = null;
		try{
			File file = new File(data_file);
			raf = new RandomAccessFile(file, "r");
			
			//go to address of word will-be read
			raf.seek(address);
			
			byte partCode, domain;
            int numOfPart = 0, numOfMean, numOfIdiom;
			WordData word = new WordData();
            PartOfSpeech part = null;
			WordMean wordMean = null;
			Idiom idm;
			String mean, example, usage;
			byte[] buffer;
			
			//Read total length of word
			length = raf.readShort();
			
			//Read inuse flag
			byte isInUse = raf.readByte();
			if(isInUse == 0)
				return null;
			
			//Read relative words
			int len = raf.readByte();
			buffer = new byte[len];
			raf.read(buffer);
			word.setRelativeWord(new String(buffer, 0, len, Charset.forName(UTF8)));
			
			//Read number of partOfSpeed
			numOfPart = raf.readByte();
			//System.out.println("[VDICTIONARY] Num of parts " + numOfPart);
			
			for(byte i=0; i<numOfPart; i++)	{
				//read part code
				partCode = raf.readByte();
				
				switch(partCode) {
					case PartOfSpeechFactory.NOUN:
						byte countability;
						part = new Noun();
						countability = raf.readByte();
						((Noun)part).setCountability(countability);

                        //Read length of plural form
						len = raf.readUnsignedByte();
						buffer = new byte[len];
						raf.read(buffer);
						((Noun)part).setPluralForm(new String(buffer, 0, len, Charset.forName(UTF8)));
                        break;
						
					case PartOfSpeechFactory.TRVERB:
					case PartOfSpeechFactory.INTRVERB:
						part = new Verb((byte)partCode);
						len = raf.readUnsignedByte();
						buffer = new byte[len];
						raf.read(buffer);
						((Verb)part).setPast(new String(buffer, 0, len, Charset.forName(UTF8)));
						
						len = raf.readUnsignedByte();
						buffer = new byte[len];
						raf.read(buffer);
						((Verb)part).setPastPerfect(new String(buffer, 0, len, Charset.forName(UTF8)));
						
						len = raf.readUnsignedByte();
						buffer = new byte[len];
						raf.read(buffer);
						((Verb)part).setGerundForm(new String(buffer, 0, len, Charset.forName(UTF8)));
						break;
						
					case PartOfSpeechFactory.ADVERB:
						part = new Adverb();
						len = raf.readUnsignedByte();
						buffer = new byte[len];
						raf.read(buffer);
						((Adverb)part).setComparative(new String(buffer, 0, len, Charset.forName(UTF8)));
						
						len = raf.readUnsignedByte();
						buffer = new byte[len];
						raf.read(buffer);
						((Adverb)part).setSuperlative(new String(buffer, 0, len, Charset.forName(UTF8)));
						break;
						
					case PartOfSpeechFactory.ADJECTIVE: //
						part = new Adjective();
						len = raf.readUnsignedByte();
						buffer = new byte[len];
						raf.read(buffer);
						((Adjective)part).setComparative(new String(buffer, 0, len, Charset.forName(UTF8)));
						
						len = raf.readUnsignedByte();
						buffer = new byte[len];
						raf.read(buffer);
						((Adjective)part).setSuperlative(new String(buffer, 0, len, Charset.forName(UTF8)));
						
						len = raf.readUnsignedByte();
						buffer = new byte[len];
						raf.read(buffer);
						((Adjective)part).setAdverbForm(new String(buffer, 0, len, Charset.forName(UTF8)));
						break;
						
					case PartOfSpeechFactory.ARTICLE: //article
						part = new Article();
						break;
						
					case PartOfSpeechFactory.PREPOSITION: //preposition
						part = new Preposition();
						break;
						
					default:
						part = new Noun();
						break;
				}
				
				//read pronunciation
				len = raf.readShort();
				buffer = new byte[len];
				raf.read(buffer);
				part.setPronunciation(new String(buffer, 0, len, Charset.forName(UTF8)));

				//read number of mean
				numOfMean = raf.readByte();
				//System.out.println("[VDICT-READ] parts " + i + "; Pronunciation: " + part.getPronunciation()+ "; NumOfMean: " + numOfMean);
				for(byte j=0; j<numOfMean; j++)	{
					//read usage
					len = raf.readByte();
					buffer = new byte[len];
					raf.read(buffer);
					usage = new String(buffer, 0, len, Charset.forName(UTF8));
					
					//Read length of mean
					len = raf.readByte();
					buffer = new byte[len];
					raf.read(buffer);
					mean = new String(buffer, 0, len, Charset.forName(UTF8));
                    //System.out.println(String.format("mean: %s", mean));

					//Read len of Example
					len = raf.readShort();
					buffer = new byte[len];
					raf.read(buffer);
					example = new String(buffer, 0, len, Charset.forName(UTF8));
					
					//read domain
					domain = raf.readByte();

                    //System.out.println(String.format("[VDICTIONARY] mean: %s, example: %s, usage: %s, domain: %d", mean, example, usage, domain));

					wordMean = new WordMean(mean, example, usage, domain);
					
					part.addMean(wordMean);
				}

                numOfIdiom = raf.readByte();
				for(byte j=0; j<numOfIdiom; j++) {
					idm = new Idiom();
					
					len = raf.readByte();
					buffer = new byte[len];
					raf.read(buffer);
					idm.setIdm(new String(buffer, 0, len, Charset.forName(UTF8)));
					
					//read usage
					len = raf.readByte();
					buffer = new byte[len];
					raf.read(buffer);
					usage = new String(buffer, 0, len, Charset.forName(UTF8));
					
					//Read length of mean
					len = raf.readByte();
					buffer = new byte[len];
					raf.read(buffer);
					mean = new String(buffer, 0, len, Charset.forName(UTF8));
					
					//Read len of Example
					len = raf.readShort();
					buffer = new byte[len];
					raf.read(buffer);
					example = new String(buffer, 0, len, Charset.forName(UTF8));
					
					//read domain
					domain = raf.readByte();
					wordMean = new WordMean(mean, example, usage, domain);
					
					idm.setMean(wordMean);
					
					part.addIdiom(idm);
				}
				
				word.addPart(part);
			}
			
			return word;
		} catch(IOException ex) {
            ex.printStackTrace();
			return null;
		} finally {
            if(raf != null)
                raf.close();
        }
	}
	
	public boolean markAsDeleted(WordData word) throws IOException {
        RandomAccessFile raf = null;
		try {
			File file = new File(data_file);
			if(!file.exists())
				return true;
			
			raf = new RandomAccessFile(file, "rw");
			raf.seek(word.getAddress()+2);
			raf.writeByte(0);
            word.getIndex().setAddress(-1);
		} finally {
            if(raf != null)
                raf.close();
        }
		return true;
	}
	
	public long writeWord(WordData word) throws IOException {
        RandomAccessFile raf = null;
		try{
			File file = new File(data_file);
			byte[] temp = null;
			
			if(!file.exists())
				file.createNewFile();
			
			raf = new RandomAccessFile(file, "rw");
			
			//System.out.println("Address of word before asign: " + word.getAddress());
			
			//if this is a new word we append it to end of file
			if(word.getAddress() < 0)
				word.getIndex().setAddress(raf.length());
			
			//System.out.println(data_file + "(size " + raf.length() + "); Address of word: " + word.getAddress());
			
			raf.seek(word.getAddress());
			
			//write total length
			raf.writeShort(word.getLength());
			
			//write isInUse
			raf.writeByte(1);
			
			//write length of relative words
			if(word.getRelativeWord()==null || 
					word.getRelativeWord().trim().equals("")) {
				raf.writeByte(0);
			} else {
				temp = word.getRelativeWord().getBytes(Charset.forName(UTF8));
				raf.writeByte(temp.length);
				raf.write(temp);
			}
				
			//write num of part
			raf.writeByte(word.getParts().size());
			
			for(PartOfSpeech part : word.getParts()) {
				//write part code
				raf.writeByte(part.getPartCode());
				
				switch(part.getPartCode()) {
					case 0:
						Noun noun = (Noun)part;
						raf.writeByte(noun.getCountability());
						if(noun.getPluralForm() != null) {
							temp = noun.getPluralForm().getBytes(Charset.forName(UTF8));
							raf.writeByte(temp.length);
							raf.write(temp);
						} else
							raf.writeByte(0);
						break;
						
					case 1: //transitive verb
					case 2: //intransitive verb
						Verb verb = (Verb)part;
						if(verb.getPast() != null) {
							temp = verb.getPast().getBytes(Charset.forName(UTF8));
							raf.writeByte(temp.length);
							raf.write(temp);
						} else 
							raf.writeByte(0);
						
						if(verb.getPastPerfect() != null) {
							temp = verb.getPastPerfect().getBytes(Charset.forName(UTF8));
							raf.writeByte(temp.length);
							raf.write(temp);
						} else
							raf.writeByte(0);
						
						if(verb.getGerundForm() != null ) {
							temp = verb.getGerundForm().getBytes(Charset.forName(UTF8));
							raf.writeByte(temp.length);
							raf.write(temp);
						} else 
							raf.writeByte(0);
						break;
						
					case 3: //Adverb
						Adverb adv = (Adverb)part;
						temp = adv.getComparative().getBytes(Charset.forName(UTF8));
						raf.writeByte(temp.length);
						raf.write(temp);
						
						temp = adv.getSuperlative().getBytes(Charset.forName(UTF8));
						raf.writeByte(temp.length);
						raf.write(temp);
						break;
						
					case 4:
						Adjective adj = (Adjective)part;
						if(adj.getComparative() != null) {
							temp = adj.getComparative().getBytes(Charset.forName(UTF8));
							raf.writeByte(temp.length);
							raf.write(temp);
						} else 
							raf.writeByte(0);
						
						if(adj.getSuperlative() != null) {
							temp = adj.getSuperlative().getBytes(Charset.forName(UTF8));
							raf.writeByte(temp.length);
							raf.write(temp);
						} else 
							raf.writeByte(0);
						
						if(adj.getAdverbForm() != null) {
							temp = adj.getAdverbForm().getBytes(Charset.forName(UTF8));
							raf.writeByte(temp.length);
							raf.write(temp);
						} else {
							raf.writeByte(0);
						}
						break;
						
					case 5:
					case 6:
						break;
						
					default:
						break;
				}
				
				//write pronunciation
				if(part.getPronunciation()!=null) {
					temp = part.getPronunciation().getBytes(Charset.forName(UTF8));
					raf.writeShort(temp.length);
					raf.write(temp);
				} else
					raf.writeShort(0);
				
				//write num of means
				raf.writeByte(part.getMeans().size());

				//write means
				for(WordMean mean: part.getMeans())	{
					//write usage
					if(mean.getUsage() != null) {
						byte[] usage = mean.getUsage().getBytes(UTF8);
						raf.writeByte(usage.length);
						raf.write(usage);
					} else
						raf.writeByte(0);
					
					//write length of mean
					byte[] meanBytes = mean.getMean().getBytes(UTF8);
					raf.writeByte(meanBytes.length);
					raf.write(meanBytes);
					
					//write example
					if(mean.getExample() != null) {
						byte[] exampleBytes = mean.getExample().getBytes(UTF8);
						raf.writeShort(exampleBytes.length);
						raf.write(exampleBytes);
					} else
						raf.writeShort(0);
					
					//write domain
					raf.writeByte(mean.getDomain());
				}

                //write num of idiom
                if(part.getIdms() != null) {
                    raf.writeByte(part.getIdms().size());
                    for(Idiom idm: part.getIdms()) {
                        byte[] buff = idm.getIdm().getBytes(UTF8);

                        raf.writeByte(buff.length);
                        raf.write(buff);

                        buff = idm.getMean().getUsage().getBytes(UTF8);
                        raf.writeByte(buff.length);
                        raf.write(buff);

                        buff = idm.getMean().getMean().getBytes(UTF8);
                        raf.writeByte(buff.length);
                        raf.write(buff);

                        buff = idm.getMean().getExample().getBytes(UTF8);
                        raf.writeByte(buff.length);
                        raf.write(buff);

                        //write domain
                        raf.writeByte(idm.getMean().getDomain());
                    }
                }
			}
			return word.getAddress();
		} finally {
            if(raf != null)
                raf.close();
        }
	}
	
	public boolean loadIndex() throws FileNotFoundException, IOException {
		FileInputStream fileInputStream = null;
		boolean result = false;
        int byteRead;
		byte[] buffer;
		
		//First, we initialize the hastable
		hashTable = new WordIndex[26][NUMINDEX][NUMINDEX][NUMINDEX];
		for(int i=0;i<26;i++)
			for(int j=0;j<NUMINDEX;j++)
				for(int k=0;k<NUMINDEX;k++)
					for(int l=0;l<NUMINDEX;l++)
						hashTable[i][j][k][l] = null;
		try {
			fileInputStream = new FileInputStream(index_file);
            DataInputStream dis = new DataInputStream(fileInputStream);
			//read version
			this.version = dis.readByte();
			while( true ) {
				WordIndex idx = new WordIndex();
				
				//read address of word start at 1 and is 8 bytes in length
				idx.setAddress(dis.readLong());
				
				//Read word text length
				int wordlen = dis.readByte();
				if(wordlen <=0)
					break;
				buffer = new byte[wordlen];
				//word start at 8 and is wordlen in length
				byteRead = fileInputStream.read(buffer, 0, wordlen);
				String strWord = new String(buffer, 0, wordlen, UTF8); 
				idx.setWord(strWord);
				
				idx.setHashCode(this.hash(idx.getWord()), 0);
				byte h1 = idx.getHashCode()[0];
				byte h2 = idx.getHashCode()[1];
				byte h3 = idx.getHashCode()[2];
				byte h4 = idx.getHashCode()[3];
				
				//System.out.println("[VDICT-LOADINDEX] " + strWord+ ": " + h1 + " " + h2 + " "+h3 + " " + h4);
				
				if(hashTable[h1][h2][h3][h4] == null) {
					hashTable[h1][h2][h3][h4] = idx;
				} else
					hashTable[h1][h2][h3][h4].setNext(idx);
				
				//System.out.println("[VDICT-LOADINDEX] Load " + wordlen + " - " +idx.getWord() + " at address " + idx.getAddress());
			}
		} catch(EOFException ee) {
            result = true;
        } finally {
            if(fileInputStream != null)
                fileInputStream.close();
        }
		return result;
	}
	
	/*	
	 * 
	 * 
	 * */
	public boolean addWord2HashTable(WordData word) throws IOException {
		WordIndex index = word.getIndex();
		byte[] hc = this.hash(index.getWord());
		index.setHashCode(hc, 0);
		
		byte h1 = index.getHashCode()[0];
		byte h2 = index.getHashCode()[1];
		byte h3 = index.getHashCode()[2];
		byte h4 = index.getHashCode()[3];
		
		//System.out.println("[VDICT-ADD] " + index.getWord()+ " with hashcode: " + h1 + " " + h2 + " " + h3 + " " + h4);
		
		//if this is a new index (word) completely
		if(hashTable[h1][h2][h3][h4] == null) {
			hashTable[h1][h2][h3][h4] = index;
			hashTable[h1][h2][h3][h4].setAddress(writeWord(word));
		} else {
			WordIndex tempIndex = hashTable[h1][h2][h3][h4];
			
			/*
			 * 	if this slot has been used already and the word at this slop different to the one we are going to add, so
			 * 	we check the next node at this slot.
			 * */
			while(tempIndex!=null && 
					(!index.getWord().trim().toUpperCase().equals(tempIndex.getWord().trim().toUpperCase())))
				tempIndex = tempIndex.getNext();
				
			
			if(tempIndex==null)//if this word is a new word which has hashcode = hashTable[h1][h2][h3][h4] 
				hashTable[h1][h2][h3][h4].setNext(index);
			else {
				//if this word is already existed
				//write word to data file and update its address to hashtable
				tempIndex.setAddress(writeWord(word));
			}
		}
		return true;
	}
	
 	private byte[] hash(String strWord)	{
		byte[] hc = new byte[4];
		String hashString = soundex(strWord);
		
		//first
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
	    for (int i=0; i<t.length() && res.length() < 4 && (c = t.charAt(i)) != ','; i++)  {
	    	//System.out.println("[VDICT-HASH] SOUNDEX " + s + " - After hash " + t.charAt(i));	
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
	          char m = SOUNDEX_MAP_ENGLISH[c-'A'];
	          if (m != '0')
	            res.append(m);
	        }
	      }
	    }
	    if (res.length() == 0)
	      return "0000";
	    for (int i=res.length(); i<4; i++)
	      res.append('0');
	    return res.toString();
	}
	
	/**
	 * 
	 * @param strWord
	 * @return a list of word that has prefix a strWord
	 */
	public ArrayList<WordIndex> initListWord(String strWord) {
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
					for(int l=d;l<NUMINDEX && flag;l++)	{
						idx = hashTable[i][j][k][l];
						while(idx != null)	{
							wordlist.add(idx);
							idx = idx.getNext();
							count++;
						}
						
						if(count > 20)
							flag = false;
					}
		return wordlist;
	}
	
	public WordIndex findIndex(String word) {
		byte[] hashCode = hash(word);
		WordIndex idx = null;
		
		int a = hashCode[0];
		int b = hashCode[1];
		int c = hashCode[2];
		int d = hashCode[3];
		
		//System.out.println("[VDICT-FINDINDEX] " + word+ ": " + a + " " + b + " "+c + " " + d);
		
		if(hashTable[a][b][c][d] != null) {
			WordIndex temp = hashTable[a][b][c][d];
			
			//System.out.println("[VDICT-FINDINDEX] word in hashtable " + temp.getWord());
			
			while(temp!=null && 
					!temp.getWord().toUpperCase().equals(word.toUpperCase())) {
				temp = temp.getNext();
			}
			
			if(temp != null)
				idx = temp;
		}
		
		return idx;
	}
	
	public WordData loadWord(String word) {
		return null;
	}
	
	public boolean overwriteIndex() {
		try {
			File file = new File(index_file);
			FileOutputStream os = new FileOutputStream(file);
			DataOutputStream dos = new DataOutputStream(os);
			
			os.write(this.version);
			for(int i=0;i<26;i++)
				for(int j=0;j<NUMINDEX;j++)
					for(int k=0;k<NUMINDEX;k++)
						for(int l=0;l<NUMINDEX;l++)	{
							if(hashTable[i][j][k][l] != null) {
								dos.writeLong(hashTable[i][j][k][l].getAddress());
								byte[] word = hashTable[i][j][k][l].getWord().getBytes(UTF8);
								dos.writeByte(word.length);
								os.write(word, 0, word.length);
							}
						}
			os.close();
			
			return true;
		}catch(IOException ex){
			return false;
		}
	}
	
	/**
	 * This is function is to test the byte order of the systems
	 */
	public void testByteOrder(){
		try{
			FileOutputStream fileout = new FileOutputStream(new File("D:\\projects\\nautilus-dictionary\\vdict\\data\\test.data"));
			DataOutputStream dos = new DataOutputStream(fileout);
			dos.writeShort(100);
			dos.writeShort(560);
			dos.flush();
			dos.close();
		}catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}
