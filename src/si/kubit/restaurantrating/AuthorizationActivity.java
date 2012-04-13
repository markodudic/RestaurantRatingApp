package si.kubit.restaurantrating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * https://developer.foursquare.com/docs/oauth.html
 * https://foursquare.com/oauth/
 * 
 * @date May 17, 2011
 * @author Mark Wyszomierski (markww@gmail.com)
 */
public class AuthorizationActivity extends Activity 
{
    private static final String TAG = "AuthorizationActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorization);

        String url = "";
        
		try {
	        String settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("settings", null);				
	        JSONObject jSettings = ((JSONArray)new JSONTokener(settings).nextValue()).getJSONObject(0);
			Log.d("SETTINGS=",jSettings.toString());
	        url = "https://foursquare.com/oauth2/authenticate" + 
	                "?client_id=" + jSettings.getString("clientId") + 
	                "&response_type=token" + 
	                "&redirect_uri=" + jSettings.getString("redirectURI");
        
	        WebView webview = (WebView)findViewById(R.id.webview);
	        webview.getSettings().setJavaScriptEnabled(true);
	        webview.setWebViewClient(new WebViewClient() {
	            public void onPageStarted(WebView view, String url, Bitmap favicon) {
	                String fragmentOk = "#access_token=";
	                String fragmentCancel = "#error=";
	                int startOk = url.indexOf(fragmentOk);
	                int startCancel = url.indexOf(fragmentCancel);
	                if (startOk > -1) {
	                    // You can use the accessToken for api calls now.
	                    String accessToken = url.substring(startOk + fragmentOk.length(), url.length());
	        			
	                    Intent intent= getIntent();
	                    intent.putExtra("accessToken", accessToken);
	
	                    setResult(RESULT_OK, intent);
	                    Log.d(TAG, "FINISH");
	                    
	                    finish();
	                } else if (startCancel > -1) {
	                	String error = url.substring(startCancel + fragmentCancel.length(), url.length());
	        			
	                	Intent intent= getIntent();
	                    intent.putExtra("error", error);
	
	                	setResult(RESULT_CANCELED, intent);
	                    Log.d(TAG, "CANCELED");	
	                    
	                    finish();
	                }
	            }
	        });
	        
	        Log.d("URL=",url);
	        webview.loadUrl(url);
   		} catch (Exception e) {
   			e.printStackTrace();
   		}
    }

	@Override
	protected void onResume() { 
		super.onResume();
		Log.d("RESUME", "****************************");
	}
	
    @Override
    protected void onPause() {
    	super.onPause();
		Log.d("PAUSE", "****************************");
    }   
}