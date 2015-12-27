package nautilus.vdict.data;

import java.nio.charset.Charset;

public class Adjective extends PartOfSpeech {
	private static final String PART_NAME = "adj";
	private String comparative;
	private String superlative;
	private String adverbForm;
	
	public Adjective() {
		comparative = null;
		superlative = null;
		adverbForm = null;
	}
	
	public Adjective(WordData w) {
		word = w;
		word.addPart(this);
		comparative = null;
		superlative = null;
		adverbForm = null;
	}

    @Override
	public short getLength() {
		short length = super.getLength();
        length += (comparative==null?0:(comparative.getBytes(Charset.forName("utf-8"))).length) + 1;
        length += superlative==null?0:(superlative.getBytes(Charset.forName("utf-8"))).length + 1;
        length += adverbForm==null?0:(adverbForm.getBytes(Charset.forName("utf-8"))).length + 1;
		return length;
	}
	
	public String getComparative() {
		return comparative;
	}
	
	public void setComparative(String val) {
		comparative = val;
	}
	
	public String getSuperlative() {
		return superlative;
	}
	
	public void setSuperlative(String val) {
		superlative = val;
	}
	
	public String getAdverbForm() {
		return adverbForm;
	}
	
	public void setAdverbForm(String val) {
		adverbForm = val;
	}

	@Override
	public byte getPartCode() {
		return 4;
	}

	@Override
	public String getPartName() {
		return PART_NAME;
	}
}
