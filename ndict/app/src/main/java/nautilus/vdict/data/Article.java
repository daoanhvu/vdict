package nautilus.vdict.data;

public class Article extends PartOfSpeech {
	private static final String PART_NAME = "adv";
	public Article() {
	}
	
	public Article(WordData w) {
		word = w;
		word.addPart(this);
	}

	@Override
	public byte getPartCode() {
		return 5;
	}

	public String getPartName() {
		return PART_NAME;
	}
}
