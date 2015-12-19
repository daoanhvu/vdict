package nautilus.vdict.data;

public class Article extends PartOfSpeech
{
	public Article()
	{
		partCode = 5;
	}
	
	public Article(WordData w)
	{
		partCode = 5;
		word = w;
		word.addPart(this);
	}
}
