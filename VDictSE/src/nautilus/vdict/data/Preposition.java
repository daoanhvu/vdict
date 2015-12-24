package nautilus.vdict.data;

public class Preposition extends PartOfSpeech {
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
}
