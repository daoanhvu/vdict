package nautilus.vdict.data;

public class Preposition extends PartOfSpeech
{
	public Preposition()
	{
		partCode = 6;
	}
	
	public Preposition(WordData w)
	{
		partCode = 6;
		word = w;
		word.addPart(this);
	}
}
