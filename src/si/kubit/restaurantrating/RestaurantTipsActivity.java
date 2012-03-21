package si.kubit.restaurantrating;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RestaurantTipsActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_tips);
        GetRestaurantTipsList();

		
		View locationButtonSubmit = findViewById(R.id.button_location); 
		locationButtonSubmit.setOnClickListener(this);
		View tipButtonSubmit = findViewById(R.id.button_tip); 
		tipButtonSubmit.setOnClickListener(this);
    }

	@Override
	protected void onResume() { 
		super.onResume();
	}
	
    @Override
    protected void onPause() {
    	super.onPause();
		//finish(); 
    } 

    public void onClick(View v) {
    	switch (v.getId()) { 
			case R.id.button_location:
				break;
			case R.id.button_tip:
				break;
    	}
	}
    
    
    private void GetRestaurantTipsList()
    {
    	String tips = "";
        Comm c = new Comm(getString(R.string.server_url), null, null);
        try { 
			Bundle extras = getIntent().getBundleExtra("si.kubit.restaurantrating.RestaurantTipsActivity");
			String restaurantId = extras.getString("restaurant_id");

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("venue_id", restaurantId));
	        
	        tips = c.post("tips",nameValuePairs);

	        JSONArray jTips = (JSONArray)new JSONTokener(tips).nextValue();
        	
	    	ListView lv = (ListView) findViewById(R.id.restuarant_tips_list);
        	RestaurantTipsListAdapter listAdapter = new RestaurantTipsListAdapter(this, jTips, getApplicationContext());
    		lv.setAdapter(listAdapter);
    		
			TextView places = (TextView)findViewById(R.id.text_tips_title);
	  		places.setText(jTips.length() + " " + getString(R.string.tips_title));
    		
        } catch (SocketException e) {
        	Toast toast = Toast.makeText(this, getString(R.string.conn_error), Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        	toast.show();
			//showMessageBox(Constants.MESSAGE_BOX_CLOSE_TIME_LONG+"", "false", getString(R.string.conn_error), getString(R.string.conn_title));
   		} catch (Exception ne) {
   			ne.printStackTrace();
        	Toast toast = Toast.makeText(this, getString(R.string.json_error), Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        	toast.show();
   			//showMessageBox(Constants.MESSAGE_BOX_CLOSE_TIME+"", "false", getString(R.string.json_error), getString(R.string.json_title));
   		}
    }      
 
	private void showMessageBox(String closeTime, String redirect, String msg, String title) {
		Intent messageBox = new Intent(this, MessageBox.class);
    	messageBox.putExtra("redirect", redirect);
    	messageBox.putExtra("closeTime", closeTime);
		messageBox.putExtra("msg", msg);
		messageBox.putExtra("title", title);
		startActivityForResult(messageBox, 1);
	}    
}