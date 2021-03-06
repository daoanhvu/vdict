package nautilus.vdict.data;

import java.nio.charset.Charset;

public class Noun extends PartOfSpeech {
	private static final String PART_NAME = "noun";
	// 0: non-count
	// 1: count
	// both of above
	private byte countability;
	private String pluralForm;
	
	public Noun() {
		word = null;
		countability = -1;
		pluralForm = null;
	}
	
	public Noun(WordData w)	{
		word = w;
		countability = -1;
		pluralForm = null;
		word.addPart(this);
	}

    @Override
	public short getLength() {
        short l = (short)(super.getLength() + 1);
        l += pluralForm==null?0:(pluralForm.getBytes(Charset.forName("utf-8"))).length + 1;
		return l;
	}
	
	public byte getCountability() {
		return countability;
	}
	
	public void setCountability(byte ctb) {
		countability = ctb;
	}
	
	public String getPluralForm() {
		return pluralForm;
	}
	
	public void setPluralForm(String val) {
		pluralForm = val;
	}

	@Override
	public byte getPartCode() {
		return 0;
	}

	@Override
	public String getPartName() {
		return PART_NAME;
	}
}
