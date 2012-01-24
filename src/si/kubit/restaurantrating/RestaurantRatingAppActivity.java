package si.kubit.restaurantrating;

import org.json.JSONObject;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class RestaurantRatingAppActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.d("**********************************", "START");
		GetUsersRatesList();

		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		//Log.d("LOCATION=",lastKnownLocation.getLatitude() + " " + lastKnownLocation.getLongitude());
    }

	@Override
	protected void onResume() { 
		super.onResume();
	}
	
    @Override
    protected void onPause() {
    	super.onPause();
		finish(); 
    }

    
    private void GetUsersRatesList()
    {
    	String userRates = "";
        Comm c = new Comm(getString(R.string.server_url), null, null);
        try { 
        	userRates = c.get("userrates");
        } catch (Exception e) {
			showMessageBox(Constants.MESSAGE_BOX_CLOSE_TIME+"", "false", getString(R.string.json_error), getString(R.string.json_title));
   		}
    	
        ListView lv = (ListView) findViewById(R.id.userRateslist);
        ListAdapter listAdapter = new ListAdapter(this, userRates, getApplicationContext());
		lv.setAdapter(listAdapter);
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