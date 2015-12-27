package nautilus.vdict.data;

public class Preposition extends PartOfSpeech {
	private static final String PART_NAME = "prep";
	public Preposition() {
	}
	
	public Preposition(WordData w)	{
		word = w;
		word.addPart(this);
	}

	@Override
	public byte getPartCode() {
		// TODO Auto-generated method stub
		return 6;
	}

	@Override
	public String getPartName() {
		return PART_NAME;
	}
}
