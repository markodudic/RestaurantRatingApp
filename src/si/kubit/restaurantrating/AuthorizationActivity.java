package si.kubit.restaurantrating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

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
    }
        
    @Override
	protected void onResume() { 
		super.onResume();
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
   		} catch (JSONException ne) {
   			ne.printStackTrace();
   		}
        
        WebView webview = (WebView)findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                String fragment = "#access_token=";
                int start = url.indexOf(fragment);
                if (start > -1) {
                    // You can use the accessToken for api calls now.
                    String accessToken = url.substring(start + fragment.length(), url.length());
        			
                    Log.d(TAG, "OAuth complete, token: [" + accessToken + "].");
                	
                    view.destroy();
                }
            }
        });
        Log.d("URL=",url);
        webview.loadUrl(url);
    }

    
}