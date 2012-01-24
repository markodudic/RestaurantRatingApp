package si.kubit.restaurantrating;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class Comm {

	private String host;
	private String user;
	private String pass;
	
	public Comm(String host, String user, String pass) {
		Log.d("comm", "host: "+host);
		this.host = host;
		this.user = user;
		this.pass = pass;
	}
	
	public String post(String url, List<NameValuePair> nameValuePairs) {
		Log.d("comm", this.host+url);
		String response = null;
		
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(this.host+url);
	    if (user != null)
	    	httppost.addHeader("Authorization", "Basic " + Base64.encodeToString(new String(this.user+":"+this.pass).getBytes(), false));
	    
	    try {
	    	if (nameValuePairs != null)
	    		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        ResponseHandler<String> responseHandler=new BasicResponseHandler();
	        response = httpclient.execute(httppost, responseHandler);
	        Log.d("comm", "http-response "+response.toString());
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	Log.d("comm", "http-error "+e.toString());
	    }
        return response;
	}
	
	public String get(String url) {
		Log.d("comm", this.host+url);
		String response = null;
		
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpGet httpget = new HttpGet(this.host+url);
	    if (user != null)
	    	httpget.addHeader("Authorization", "Basic " + Base64.encodeToString(new String(this.user+":"+this.pass).getBytes(), false));
	    
	    try {
	        ResponseHandler<String> responseHandler=new BasicResponseHandler();
	        response = httpclient.execute(httpget, responseHandler);
	        Log.d("comm", "http-response "+response.toString());
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	Log.d("comm", "http-error "+e.toString());
	    }
        return response;
	}
}