package nautilus.vdict;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
//import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import nautilus.vdict.data.*;

public class VDictActivity extends Activity {
	//private static final String TAG = "VDictActivity";
	
	private ArrayAdapter<String> suggestionAdapter;
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
        wvResult = (WebView)findViewById(R.id.wvResult);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        
        //Initialize data
        dict = new VDictionary(this.getApplicationContext(), "English-Vietnamese", "./sdcard/vdict/vdic.data", "./sdcard/vdict/vdic.idx");
        byte isExisted = dict.checkDataExisted();
        if(isExisted < 0) {
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage("Data not found!\nPlease check if data existed!").setCancelable(false)
	        	.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					try {
						VDictActivity.this.finalize();
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
	        AlertDialog dialog = builder.create();
	        dialog.show();
        } else
        	dict.loadIndex();
        
        btnSearch.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBtnSearchClick();
			}
		});
        
        autoCompleteWord.addTextChangedListener(textWatcher);
        
    }//end onCreate
    
    private void onBtnSearchClick() {
    	String strWord = autoCompleteWord.getText().toString();
    	
    	if(dict == null || strWord.length()<=0)
    		return;
  
    	WordIndex index = dict.findIndex(strWord);
    	WordData word = null;
    	String strHtml="";
    	if(index != null) {
    		word = dict.readWord(index.getAddress());
    		word.setWord(index.getWord());
    		strHtml = initHtml(word);
    		wvResult.loadData("<?xml version=\"1.0\" encoding\"utf-8\" ?><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /></head><body><h1>" + word.getWord() + "</h1><br>" + strHtml + "</body>", "text/html", "utf-8");
    	}
    }
    
    private String initHtml(WordData data) {
    	String html = "";
    	
    	html = "<table>";
    	for(PartOfSpeech p : data.getParts()) {
    		html += "<tr><td>&nbsp;&nbsp;&nbsp;" + p.getMean() + "</td></tr>";
    	}
    	html += "</table>";
    	
    	return html;
    }
    
    private TextWatcher textWatcher = new TextWatcher() {
		
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if(s.length()<=0)
				return;
			
			ArrayList<String> wordLists = dict.initListWordString(s.toString());
			suggestionAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.words, R.id.txtSuggestionWord, wordLists);
			suggestionAdapter.setNotifyOnChange(true);
			autoCompleteWord.setAdapter(suggestionAdapter);
		}
		
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	};
}