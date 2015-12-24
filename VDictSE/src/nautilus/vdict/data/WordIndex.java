package nautilus.vdict.data;

/*
 * 	Created on: 
 * 	Author: Dao Anh Vu
 * 	Updated on: Aug 1, 2011
 * 
 * */

public class WordIndex implements Comparable<WordIndex> {
	private static final char[][] uMap = {
			{0,'0'},
			{1,'1'},
			{2,'2'},
			{3,'3'},
			{4,'4'},
			{5,'5'},
			{6,'6'},
			{7,'7'},
			{8,'8'},
			{9,'9'},
			{10,'a'},
			{11,'ă'},
			{12,'â'},
			{13,'b'},
			{14,'c'},
			{15,'d'},
			{16,'đ'},
			{17,'e'},
			{18,'ê'},
			{19,'g'},
			{20,'h'},
			{21, 'i'},
			{22, 'k'},
			{23,'l'},
			{24, 'm'},
			{25,'n'},
			{26,'o'},
			{27,'ô'},
			{28,'ơ'},
			{29,'p'},
			{30,'q'},
			{31,'r'},
			{32,'s'},
			{33,'t'},
			{34,'u'},
			{35,'ư'},
			{36,'v'},
			{37,'x'},
			{38,'y'}};
	private static final int MAX_CHAR_COUNT = 39;
	
	private long address;
	private String word;
	private byte[] hashCode = new byte[4];
	
	private WordIndex next;
	
	public WordIndex()	{
		word = null;
		address = -1;
		next = null;
	}
	
	public WordIndex(String w){
		word = w;
		address = -1;
		next = null;
	}
	
	public long getAddress(){
		return address;
	}
	
	public String getWord()	{
		return word;
	}
	
	public void setWord(String w){
		word = w;
	}
	
	public void setAddress(long val){
		address = val;
	}
	
	public byte[] getHashCode()	{
		return hashCode;
	}
	
	public void setHashCode(byte[] hc, int offset){
		hashCode = new byte[4];
		
		hashCode[0] = hc[offset];
		hashCode[1] = hc[offset+1];
		hashCode[2] = hc[offset+2];
		hashCode[3] = hc[offset+3];
	}
	
	public void setNext(WordIndex _next){
		if( next != null ){
			next.setNext(_next);
			return;
		}
		
		next = _next;
	}
	
	public WordIndex getNext(){
		return next;
	}
	
	private static int compare(char a, char b){
		int i, codeA=-1, codeB=-1;
		
		for(i=0; i<MAX_CHAR_COUNT; i++)	{
			if(uMap[i][1]==a)
				codeA = uMap[i][0];
			
			if(uMap[i][1]==b)
				codeB = uMap[i][0];
		}
		
		return (codeA-codeB);
	}

	@Override
	public int compareTo(WordIndex value){
		String thisStr = this.getWord();
		String comparedStr = null;
		int shorterLen=0, cc=0;
		
		if(value != null){
			comparedStr = value.getWord();
			
			shorterLen = thisStr.length()<comparedStr.length()?thisStr.length():comparedStr.length();
			
			for(int i=0;i<shorterLen;i++){
				cc = compare(thisStr.charAt(i), comparedStr.charAt(i));
				if(cc > 0){
					return 1;
				}
				else {
					if(cc < 0)
						return -1;
				}
			}
		}
		
		return 0;
	}// end compareTo
}