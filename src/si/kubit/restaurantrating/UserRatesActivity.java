package si.kubit.restaurantrating;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import si.kubit.restaurantrating.conn.Comm;
import si.kubit.restaurantrating.conn.Foursquare;
import si.kubit.restaurantrating.objects.UserRate;
import si.kubit.restaurantrating.util.Util;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class UserRatesActivity extends ListActivity implements OnClickListener {
	private JSONArray jUserRates;

	private ListView lv;
	private UserRatesListAdapter listAdapter;
	private ArrayList<UserRate> userRatesList = null;
	private Runnable viewUserRates;
	private ProgressDialog m_ProgressDialog = null;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_rates);
        Log.d("**********************************", "START");

        //povezem na streznik
        Comm comm = new Comm(getString(R.string.server_url), null, null);
        ((RestaurantRating)getApplicationContext()).setComm(comm);
        
		//nastavim userja
        getUser("marko", "okram");
        
		//nastavim podatke za fsq dostop
        setFoursquare();

        
        
        userRatesList = new ArrayList<UserRate>();
        this.listAdapter = new UserRatesListAdapter(this, R.layout.user_rates_list, userRatesList);
        setListAdapter(this.listAdapter);

        lv = (ListView) findViewById(android.R.id.list);
        lv.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> a, View v, int position, long id) {		    	
		    	try {
		    		JSONObject jobject = (JSONObject) jUserRates.getJSONObject(position);
	  	    	  	
		    		String restaurantId 	= jobject.getString("restaurantId");
				    String restaurantName 	= jobject.getString("restaurantName");
		    		String category 		= jobject.getString("category");
		    		int rateCount 			= jobject.getInt("rateCount");
		    		String avgRate 			= jobject.getString("avgRate");
		    		double foodRate 		= jobject.getDouble("foodRate");
		    		double ambientRate		= jobject.getDouble("ambientRate");
		    		double serviceRate 		= jobject.getDouble("serviceRate");
		    		double valueRate 		= jobject.getDouble("valueRate");
		    		int tipCount 			= jobject.getInt("tipCount");
		    		int photoCount 			= jobject.getInt("photoCount");

		    		JSONObject jobjectRestaurant = new JSONObject();
		    		jobjectRestaurant.put("id",restaurantId);
		    		jobjectRestaurant.put("name",restaurantName);
		    		jobjectRestaurant.put("category",category);
		    		jobjectRestaurant.put("rateCount",rateCount);
		    		jobjectRestaurant.put("rateAvg",avgRate);
		    		jobjectRestaurant.put("rateFoodAvg",foodRate);
		    		jobjectRestaurant.put("rateAmbientAvg",ambientRate);
		    		jobjectRestaurant.put("rateServiceAvg",serviceRate);
		    		jobjectRestaurant.put("rateValueAvg",valueRate);
		    		jobjectRestaurant.put("tipCount",tipCount);
		    		jobjectRestaurant.put("photoCount",photoCount);
		    		 
		    		Intent intentRestaurantRate = new Intent(UserRatesActivity.this, RestaurantRateActivity.class);
				  	Bundle extras = new Bundle();
				  	
				  	extras.putString("restaurant", jobjectRestaurant.toString());
				  	extras.putBoolean("user_rate", true);
				  	intentRestaurantRate.putExtra("si.kubit.restaurantrating.RestaurantRateActivity", extras);
				  	intentRestaurantRate.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
	    			UserRatesActivity.this.startActivity(intentRestaurantRate);

		    	} catch (Exception e) {e.printStackTrace();}
		    }				
		});	      
		
		View mapButtonSubmit = findViewById(R.id.button_map); 
		mapButtonSubmit.setOnClickListener(this);
		View settingsButtonSubmit = findViewById(R.id.button_settings); 
		settingsButtonSubmit.setOnClickListener(this);
		View friendsButtonSubmit = findViewById(R.id.button_friends); 
		friendsButtonSubmit.setOnClickListener(this);
		View rateButtonSubmit = findViewById(R.id.button_rate); 
		rateButtonSubmit.setOnClickListener(this);
		View userButtonSubmit = findViewById(R.id.button_user); 
		userButtonSubmit.setOnClickListener(this);
	
    } 
    
    private void setFoursquare() {
		//pridobim serverske nastavitev za komunikacijo z 4SQ
    	try {
    		String settings = ((RestaurantRating)getApplicationContext()).getComm().get("settings");
    		Foursquare fsq = new Foursquare(settings);
    		
            ((RestaurantRating)getApplicationContext()).setFoursquare(fsq);
        } catch (SocketException e) {
        	Toast toast = Toast.makeText(this, getString(R.string.conn_error), Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        	toast.show();
   		} catch (Exception ne) {
   			ne.printStackTrace();
        	Toast toast = Toast.makeText(this, getString(R.string.json_error), Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        	toast.show();
   		}
    }
    
    private void getUser(String username, String password) {
		//preverim kateri uporabnik je prijavljen in ga vpišem v shared preferncies
        try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("username", username));
	        nameValuePairs.add(new BasicNameValuePair("password", password));
	        
	        String user = ((RestaurantRating)getApplicationContext()).getComm().post("login",nameValuePairs);
	        Util.addPreferencies("user", user);
        } catch (SocketException e) {
        	Toast toast = Toast.makeText(this, getString(R.string.conn_error), Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        	toast.show();
   		} catch (Exception ne) {
   			ne.printStackTrace();
        	Toast toast = Toast.makeText(this, getString(R.string.json_error), Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        	toast.show();
   		}
    }

	@Override
	protected void onResume() { 
		super.onResume();
		GetUsersRatesList();
	}
	
    @Override
    protected void onPause() {
    	super.onPause();
		//finish(); 
    }

    public void onClick(View v) {
    	switch (v.getId()) { 
			case R.id.button_map:
    			Intent mapUsers = new Intent(this, MapFriendsActivity.class); 
    			startActivity(mapUsers); 
				break;
			case R.id.button_settings:
				break;
			case R.id.button_friends:
				GetUsersRatesList();
				break;
			case R.id.button_rate:
    			Intent restaurants = new Intent(this, RestaurantsActivity.class); 
    			startActivity(restaurants); 
				break;
			case R.id.button_user:
				break;
    	}
	}
    
    
    private void GetUsersRatesList()
    {
    	String userRates = "";
        try { 
        	userRates = ((RestaurantRating)getApplicationContext()).getComm().get("userrates");
        	jUserRates = (JSONArray)new JSONTokener(userRates).nextValue();
        	
        	getUserRates();
        } catch (SocketException e) {
        	Toast toast = Toast.makeText(this, getString(R.string.conn_error), Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        	toast.show();
   		} catch (Exception ne) {
   			Toast toast = Toast.makeText(this, getString(R.string.json_error), Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        	toast.show();
   		}
    }      
 
    private void getUserRates(){
        try{
        	userRatesList = new ArrayList<UserRate>();
        	for(int i=0; i<jUserRates.length(); i++) {
        		JSONObject jobject = (JSONObject) jUserRates.getJSONObject(i);
        		UserRate ur = new UserRate();
        		ur.setAvgRate(jobject.getString("avgRate"));
        		ur.setUserName(jobject.getString("userName"));
        		ur.setUserSurname(jobject.getString("userSurname"));
        		ur.setRestaurantName(jobject.getString("restaurantName"));
        		ur.setRateHoursAgo(jobject.getString("rateHoursAgo"));
        		ur.setRateDateTime(jobject.getString("rateDateTime"));
        		userRatesList.add(ur);
        	}
          } catch (Exception e) { 
            Log.e("BACKGROUND_PROC", e.getMessage());
          }
          runOnUiThread(returnRes);
      }
	  
    private Runnable returnRes = new Runnable() {
    	public void run() {
    		listAdapter.clear();
            if(userRatesList != null && userRatesList.size() > 0){
            	listAdapter.notifyDataSetChanged();
                for(int i=0;i<userRatesList.size();i++)
                	listAdapter.add(userRatesList.get(i));
            }
            listAdapter.notifyDataSetChanged();
        }
      };

}