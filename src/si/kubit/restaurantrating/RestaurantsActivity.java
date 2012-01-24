package si.kubit.restaurantrating;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class RestaurantsActivity extends Activity implements OnClickListener {
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurants);
        Log.d("**********************************", "RestaurantsActivity");
		GetRestaurantsList();
		
		View mapButtonSubmit = findViewById(R.id.button_map); 
		mapButtonSubmit.setOnClickListener(this);
		View settingsButtonSubmit = findViewById(R.id.button_settings); 
		settingsButtonSubmit.setOnClickListener(this);

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

    public void onClick(View v) {
    	switch (v.getId()) { 
			case R.id.button_map:
				GetRestaurantsList();
				break;
			case R.id.button_settings:
				break;
    	}
	}
    
    
    private void GetRestaurantsList()
    {
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		Log.d("LOCATION=",lastKnownLocation.getLatitude() + " " + lastKnownLocation.getLongitude());

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("lat", lastKnownLocation.getLatitude()+""));
        nameValuePairs.add(new BasicNameValuePair("lng", lastKnownLocation.getLongitude()+""));

        String restaurants = "";
        Comm c = new Comm(getString(R.string.server_url), null, null);
        try { 
        	restaurants = c.post("venues", nameValuePairs);
        } catch (Exception e) {
			showMessageBox(Constants.MESSAGE_BOX_CLOSE_TIME+"", "false", getString(R.string.json_error), getString(R.string.json_title));
   		}
    	
        ListView lv = (ListView) findViewById(R.id.restaurant_list);
        RestaurantsListAdapter listAdapter = new RestaurantsListAdapter(this, restaurants, getApplicationContext());
		lv.setAdapter(listAdapter);
		
		try {
	  		int l = ((JSONArray)new JSONTokener(restaurants).nextValue()).length();
	  		TextView places = (TextView)findViewById(R.id.text_places_nearby);
	  		places.setText(l + " " + getString(R.string.places_nearby));
	    } catch (Exception e) {}
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