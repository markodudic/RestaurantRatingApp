package si.kubit.restaurantrating;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Context;
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
		Log.d("LOCATION=",lastKnownLocation.getLatitude() + " " + lastKnownLocation.getLongitude());
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
    	String userRates = "[{\"userName\":\"marko\",\"avgRate\":\"3.7\",\"rateDateTime\":\"2012-01-23 14:36:35.0\",\"rateHoursAgo\":\"2\",\"userSurname\":\"dudić\",\"restaurantName\":\"Joe Peña's Cantina y Bar\"},{\"userName\":\"marko\",\"avgRate\":\"3.75\",\"rateDateTime\":\"2012-01-23 14:36:33.0\",\"rateHoursAgo\":\"2\",\"userSurname\":\"dudić\",\"restaurantName\":\"Joe Peña's Cantina y Bar\"},{\"userName\":\"marko\",\"avgRate\":\"3.5\",\"rateDateTime\":\"2012-01-23 14:34:56.0\",\"rateHoursAgo\":\"2\",\"userSurname\":\"dudić\",\"restaurantName\":\"Joe Peña's Cantina y Bar\"}]";
    	
    	ListView lv = (ListView) findViewById(R.id.userRateslist);
        ListAdapter listAdapter = new ListAdapter(this, userRates, getApplicationContext());
		lv.setAdapter(listAdapter);
    }      
        
}