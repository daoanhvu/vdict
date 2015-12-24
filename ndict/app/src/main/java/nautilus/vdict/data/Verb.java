package nautilus.vdict.data;

import java.nio.charset.Charset;

public class Verb extends PartOfSpeech {
	private String past;
	private String pastPerfect;
	private String gerundForm;
	
	public Verb() {
		past = null;
		pastPerfect = null;
		gerundForm = null;
	}
	
	public Verb(byte pc) {
		word = null;
		partCode = pc;
		past = null;
		pastPerfect = null;
		gerundForm = null;
	}
	
	public Verb(byte pc, WordData w) {
		word = w;
		word.addPart(this);
		partCode = pc;
		past = null;
		pastPerfect = null;
		gerundForm = null;
	}

    @Override
    public short getLength() {
        short length = super.getLength();
        length += (past==null?0:(past.getBytes(Charset.forName("utf-8"))).length) + 1;
        length += pastPerfect==null?0:(pastPerfect.getBytes(Charset.forName("utf-8"))).length + 1;
        length += gerundForm==null?0:(gerundForm.getBytes(Charset.forName("utf-8"))).length + 1;
        return length;
    }
	
	public String getPast() {
		return past;
	}
	
	public void setPast(String val) {
		past = val;
	}
	
	public String getPastPerfect() {
		return pastPerfect;
	}
	
	public void setPastPerfect(String val) {
		pastPerfect = val;
	}
	
	public String getGerundForm() {
		return gerundForm;
	}
	
	public void setGerundForm(String val) {
		gerundForm = val;
	}
	
	@Override
	public byte getPartCode() {
		return 1;
	}
}
