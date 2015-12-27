package nautilus.vdict;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
//import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import nautilus.vdict.data.*;
import nautilus.vdict.util.FileUtil;

public class VDictActivity extends Activity {
	//private static final String TAG = "VDictActivity";
	
	private AutoCompleteWordAdapter suggestionAdapter;
	private AutoCompleteTextView autoCompleteWord;
	private WebView wvResult;
	private Button btnSearch;
	private VDictionary dict = null;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        autoCompleteWord = (AutoCompleteTextView)findViewById(R.id.autoCompleteWord);
		autoCompleteWord.setThreshold(1);
        wvResult = (WebView)findViewById(R.id.wvResult);
		WebSettings webSettings = wvResult.getSettings();
		webSettings.setDefaultTextEncodingName("utf-8");
        btnSearch = (Button)findViewById(R.id.btnSearch);
        
        //Initialize data, here we read data from assets and write into app folder.
        dict = new VDictionary(this.getApplicationContext(), "English-Vietnamese",
				FileUtil.DATA_FILE, FileUtil.INDEX_FILE);
        if(!FileUtil.checkDataExisted(this)) {
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage("Data not found!\nPlease check if data existed!").setCancelable(false)
	        	.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					try {
						VDictActivity.this.finalize();
					} catch (Throwable e) {
						android.util.Log.e("VDictionary", e.getMessage());
					}
					
				}
			});
	        AlertDialog dialog = builder.create();
	        dialog.show();
        } else {
			dict.loadIndex();

			//For testing purpose
			//FileUtil.readTestData(this);
		}

		List<String> wordLists = new ArrayList<>();
		suggestionAdapter = new AutoCompleteWordAdapter(this, R.layout.words, R.id.txtSuggestionWord, wordLists, dict);
		autoCompleteWord.setAdapter(suggestionAdapter);
        
        btnSearch.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBtnSearchClick();
			}
		});
        
//        autoCompleteWord.addTextChangedListener(textWatcher);
        
    }//end onCreate
    
    private void onBtnSearchClick() {
    	String strWord = autoCompleteWord.getText().toString();
    	
    	if(dict == null || strWord.length()<=0)
    		return;
  
    	WordIndex index = dict.findIndex(strWord);
    	WordData word;
    	String strHtml;
		try {
			if (index != null) {
				word = dict.readWord(index.getAddress());
				word.setWord(index.getWord());
				strHtml = initHtml(word);
//				wvResult.loadData("<?xml version=\"1.0\" encoding\"utf-8\" ?><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /></head><body><h1><font color='#4c6ff4'>" + word.getWord() + "</color></h1><br>" + strHtml + "</body>", "text/html; charset=utf-8", "utf-8");
				wvResult.loadData("<html><head><meta http-equiv=\"Content-Type\" " +
						"content=\"text/html; charset=UTF-8\" /></head><body>" +
						"<h1><font color='#4c6ff4'>" + word.getWord() + "</color></h1><br>" + strHtml + "</body></html>",
						"text/html; charset=utf-8", "utf-8");
			}
		} catch (IOException ex){
			wvResult.loadData("<html></html>", "text/html", "utf-8");
		}
    }
    
    private String initHtml(WordData data) {
    	String html = "<table>";
    	for(PartOfSpeech p : data.getParts()) {
			html += "<tr><td bgcolor='#ccffcc'>";
			html += "(" + p.getPartName() + ") &nbsp; ";
			if(p.getPronunciation()!=null) {
				html += "/" + p.getPronunciation() + "/";
			}
			html += "</td></tr>";
			html += "<tr><td>";
			for(WordMean m: p.getMeans()) {
				html += "<ul>";
				html += "<li>&nbsp;" + m.getMean() + "</li>";
				if( (m.hasExample()) ) {
					html += "<ul>";
					html += "<li><font color='#833a40'>&nbsp;&nbsp;" + m.getExample() + "</font></li>" ;
					html += "</ul>";
				}
				html += "</ul>";
			}
    		html += "</td></tr>";
    	}
    	html += "</table>";
    	return html;
    }
}