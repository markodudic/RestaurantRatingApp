package si.kubit.restaurantrating;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import si.kubit.restaurantrating.objects.Restaurant;
import android.app.ListActivity;
import android.app.ProgressDialog;
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

public class RestaurantsActivity extends ListActivity implements OnClickListener {
	
	private LocationManager locationManager;
	private LocationListener locationListener;
	private Location location;
	private String provider;
	
	private JSONArray jRestaurants;
    
	private ListView lv;
	private EditText filterText;
	private RestaurantsListAdapter listAdapter;
	private ArrayList<Restaurant> restaurantsList = null;
	private Runnable viewRestaurants;
	private ProgressDialog m_ProgressDialog = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurants);
        Log.d("**********************************", "RestaurantsActivity");
		
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            
        restaurantsList = new ArrayList<Restaurant>();
        this.listAdapter = new RestaurantsListAdapter(this, R.layout.restaurants_list, restaurantsList, true);
        setListAdapter(this.listAdapter);
                
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
		    }
		  };

		lv = (ListView) findViewById(android.R.id.list);
        lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> a, View v, int position, long id) {		    	
		    	try {
		    		JSONObject jobject = (JSONObject) jRestaurants.getJSONObject(position);
	  	    	  	
			    	Intent intentRestaurantRate = new Intent(RestaurantsActivity.this, RestaurantRateActivity.class);
				  	Bundle extras = new Bundle();
				  	
				  	extras.putString("restaurant_id", jobject.getString("id"));
				  	extras.putBoolean("user_rate", false);
				  	extras.putBoolean("show_menu", true);
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

    }

	private void registerListener() { 
		locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
	}

	private void unRegisterListener() { 
		locationManager.removeUpdates(locationListener);
	}

	
	@Override
	protected void onResume() { 
		super.onResume();
		registerListener();
		Location lastKnownLocation = locationManager.getLastKnownLocation(provider);
		if(lastKnownLocation != null) { 
			GetRestaurantsList(lastKnownLocation);
		}
		//dummy podatki za emulator
		Location loc = new Location(provider);
		loc.setLongitude(14.5d);
		loc.setLatitude(46.05d);
		GetRestaurantsList(loc);
		//
	}
	
    @Override
    protected void onPause() {
    	super.onPause();
    	unRegisterListener();
    }

    public void onClick(View v) {
    	switch (v.getId()) { 
			case R.id.button_map:
		    	Intent mapRestaurantsActivity = new Intent(RestaurantsActivity.this, MapRestaurantsActivity.class);
			  	RestaurantsActivity.this.startActivity(mapRestaurantsActivity);
				break;
			case R.id.button_cancel:
		    	Intent intentUserRates = new Intent(RestaurantsActivity.this, UserRatesActivity.class);
			  	RestaurantsActivity.this.startActivity(intentUserRates);
				break;
    	}
	}
    
    
    private void GetRestaurantsList(Location location)
    {
    	this.location = location;
    	
    	//ce trenutno prikazujem podatke, prekinem
    	if ((m_ProgressDialog != null) && ( m_ProgressDialog.isShowing())) {
    		m_ProgressDialog.dismiss();
    	}
     	
		if (location == null) {
        	Toast toast = Toast.makeText(this, getString(R.string.location_error), Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        	toast.show();
		} else {
			Log.d("GetRestaurantsList=",location.getLatitude() + " " + location.getLongitude());
            
			m_ProgressDialog = ProgressDialog.show(RestaurantsActivity.this, getString(R.string.please_wait), getString(R.string.retriving_data), true);
	
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("lat", location.getLatitude()+""));
	        nameValuePairs.add(new BasicNameValuePair("lng", location.getLongitude()+""));
	
	        try { 
	        	String restaurants = ((RestaurantRating)getApplicationContext()).getComm().post("restaurants", nameValuePairs);
	        	jRestaurants = (JSONArray)new JSONTokener(restaurants).nextValue();
	        	Log.d("+++++++++++++", jRestaurants.toString());
			  	
	        	viewRestaurants = new Runnable(){
	                public void run() {
	                    getRestaurants();
	                }
	            };
				
	            Thread thread =  new Thread(null, viewRestaurants, "ViewRestaurants");
	            thread.start();

	            TextView places = (TextView)findViewById(R.id.text_places_nearby);
		  		places.setText(jRestaurants.length() + " " + getString(R.string.places_nearby));
		  		
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	Toast toast = Toast.makeText(this, getString(R.string.json_error), Toast.LENGTH_LONG);
	        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
	        	toast.show();
	   		}
	    	
		}
    }      
 
    private void getRestaurants(){
        try{
        	restaurantsList = new ArrayList<Restaurant>();
        	for(int i=0; i<jRestaurants.length(); i++) {
        		JSONObject jobject = (JSONObject) jRestaurants.getJSONObject(i);
  	    	  	Restaurant r = new Restaurant();
        		r.setRateAvg(Double.parseDouble(jobject.getString("rateAvg")));
        		r.setRateCount(Integer.parseInt(jobject.getString("rateCount")));
        		r.setName(jobject.getString("name"));
        		r.setCategory(jobject.getString("category"));
        		r.setDistance(Long.parseLong(jobject.getString("distance")));
        		restaurantsList.add(r);
        	}
            //Thread.sleep(1000);
          } catch (Exception e) { 
            Log.e("BACKGROUND_PROC", e.getMessage());
          }
          runOnUiThread(returnRes);
      }

    
    private Runnable returnRes = new Runnable() {
    	public void run() {
    		if(restaurantsList != null && restaurantsList.size() > 0){
            	listAdapter.clear();
            	listAdapter.notifyDataSetChanged();
                for(int i=0;i<restaurantsList.size();i++)
                	listAdapter.add(restaurantsList.get(i));
            }
            m_ProgressDialog.dismiss();
            listAdapter.notifyDataSetChanged();
        }
      };
}