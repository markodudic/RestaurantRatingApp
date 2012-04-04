package si.kubit.restaurantrating;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RestaurantsActivity extends Activity implements OnClickListener {
	
	LocationManager locationManager;
	LocationListener locationListener;
	String provider;
	
	private String restaurants = "";
	private JSONArray jRestaurants;
    
	private ListView lv;
	private EditText filterText;
	private RestaurantsListAdapter listAdapter;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurants);
        Log.d("**********************************", "RestaurantsActivity");
		
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
		View mapButtonSubmit = findViewById(R.id.button_map); 
		mapButtonSubmit.setOnClickListener(this);
		View cancelButtonSubmit = findViewById(R.id.button_cancel); 
		cancelButtonSubmit.setOnClickListener(this);

		// Acquire a reference to the system Location Manager
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		Criteria crit = new Criteria();
		crit.setAccuracy(Criteria.ACCURACY_FINE);
		provider = locationManager.getBestProvider(crit, true);
		Log.d("PROVIDER", provider);
		
		// Define a listener that responds to location updates
		locationListener = new LocationListener() {
		    public void onLocationChanged(Location location) {
		      // Called when a new location is found by the network location provider.
		    	Log.d("GET LOCATION", "******************");
				GetRestaurantsList(location);
				locationManager.removeUpdates(locationListener);
			}

		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {
	        	Toast toast = Toast.makeText(RestaurantsActivity.this, getString(R.string.location_error), Toast.LENGTH_LONG);
	        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
	        	toast.show();
//				showMessageBox(Constants.MESSAGE_BOX_CLOSE_TIME_LONG+"", "false", getString(R.string.location_error), getString(R.string.location_title));
		    }
		  };

		registerListener();
		
		lv = (ListView) findViewById(R.id.restaurant_list);
        lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> a, View v, int position, long id) {		    	
		    	try {
		    		//JSONObject jobject = (JSONObject) jRestaurants.getJSONObject(position);
		    		JSONObject jobject = (JSONObject) listAdapter.getItem(position);
		    		
			    	Intent intentRestaurantRate = new Intent(RestaurantsActivity.this, RestaurantRateActivity.class);
				  	Bundle extras = new Bundle();
				  	
				  	extras.putString("restaurant", jobject.toString());
				  	extras.putBoolean("user_rate", false);
				  	intentRestaurantRate.putExtra("si.kubit.restaurantrating.RestaurantRateActivity", extras);
				  	intentRestaurantRate.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	    			RestaurantsActivity.this.startActivity(intentRestaurantRate);

		    	} catch (Exception e) {e.printStackTrace();}
		    }				
		});
        
        filterText = (EditText) findViewById(R.id.text_search);
        filterText.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,	int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (listAdapter != null)
					listAdapter.filter(s);
			}
        });

		Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
		if(lastKnownLocation != null) { 
			Log.d("lastKnownLocation", lastKnownLocation.getLatitude() + "-" + lastKnownLocation.getLongitude());
			GetRestaurantsList(lastKnownLocation);
		}
		//dummy podatki za emulator
		Location loc = new Location(provider);
		loc.setLongitude(14.5d);
		loc.setLatitude(46.05d);
		GetRestaurantsList(loc);
		//
    }

	private void registerListener() { 
		locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
		//zaradi emulatorja
		//locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	}

	
	@Override
	protected void onResume() { 
		super.onResume();
		//registerListener();
	}
	
    @Override
    protected void onPause() {
    	super.onPause();
    }

    public void onClick(View v) {
    	switch (v.getId()) { 
			case R.id.button_map:
				registerListener();
				break;
			case R.id.button_cancel:
		    	Intent intentUserRates = new Intent(RestaurantsActivity.this, UserRatesActivity.class);
			  	RestaurantsActivity.this.startActivity(intentUserRates);
				break;
    	}
	}
    
    
    private void GetRestaurantsList(Location location)
    {

		if (location == null) {
        	Toast toast = Toast.makeText(this, getString(R.string.location_error), Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        	toast.show();
//			showMessageBox(Constants.MESSAGE_BOX_CLOSE_TIME_LONG+"", "false", getString(R.string.location_error), getString(R.string.location_title));
		} else {
			Log.d("GetRestaurantsList=",location.getLatitude() + " " + location.getLongitude());
	
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("lat", location.getLatitude()+""));
	        nameValuePairs.add(new BasicNameValuePair("lng", location.getLongitude()+""));
	
	        Comm c = new Comm(getString(R.string.server_url), null, null);
	        try { 
	        	restaurants = c.post("venues", nameValuePairs);
	        	jRestaurants = (JSONArray)new JSONTokener(restaurants).nextValue();
	        	Log.d("+++++++++++++", jRestaurants.toString());
			  	
		        listAdapter = new RestaurantsListAdapter(this, jRestaurants, getApplicationContext(), true);
		        lv.setAdapter(listAdapter);
				
				TextView places = (TextView)findViewById(R.id.text_places_nearby);
		  		places.setText(jRestaurants.length() + " " + getString(R.string.places_nearby));
		  		
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	Toast toast = Toast.makeText(this, getString(R.string.json_error), Toast.LENGTH_LONG);
	        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
	        	toast.show();
//				showMessageBox(Constants.MESSAGE_BOX_CLOSE_TIME+"", "false", getString(R.string.json_error), getString(R.string.json_title));
	   		}
	    	
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