package nautilus.vdict.data;

public class Article extends PartOfSpeech {
	public Article() {
	}
	
	public Article(WordData w) {
		partCode = 5;
		word = w;
		word.addPart(this);
	}

	@Override
	public byte getPartCode() {
		// TODO Auto-generated method stub
		return 5;
	}
}
