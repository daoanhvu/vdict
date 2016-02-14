package nautilus.vdict;

import android.app.Activity;
import android.os.Bundle;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;
//import org.json.

public class ImportWordActivity extends Activity {

	private final static String URL = "";
	private HttpTransport mTransport = new NetHttpTransport();
	private HttpRequestFactory requestFactory;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.importword);

		requestFactory = mTransport.createRequestFactory();
	}

	private void executeRequest() {
		GenericUrl url = new GenericUrl(URL);
		try {
			HttpRequest request = requestFactory.buildGetRequest(url);
			HttpResponse response = request.execute();
		} catch (IOException ie) {
			android.util.Log.e("ImportWordActivity", ie.getMessage());
		}
	}
}
