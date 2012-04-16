package si.kubit.restaurantrating;
 
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import si.kubit.restaurantrating.conn.Comm;
import si.kubit.restaurantrating.objects.Restaurant;
import si.kubit.restaurantrating.util.Util;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class RestaurantRateActivity extends ListActivity implements OnClickListener {
	
	JSONObject jobject;
	
	private String restaurantId;
	private String rateAvg;
	private double rateFoodAvg;
	private double rateAmbientAvg;
	private double rateServiceAvg;
	private double rateValueAvg;
    
	private Handler mHandler = new Handler();
	private View layoutRateFood;
	private View layoutRateAmbient;
	private View layoutRateService;
	private View layoutRateValue;
	private TextView rateFoodValue;
	private TextView rateAmbientValue;
	private TextView rateServiceValue;
	private TextView rateValueValue;
	private TextView textRateTitle;
	private DecimalFormat decimalFormat = new DecimalFormat("0.0");

	private String restaurant;
	private boolean userRate = false;
	
	ListView lv;
	private RestaurantsListAdapter listAdapter;
	private ArrayList<Restaurant> restaurantsList = null;
	private Runnable viewRestaurants;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_rate);
        		
		layoutRateFood = (View) this.findViewById(R.id.layout_rate_food);
		rateFoodValue = (TextView) layoutRateFood.findViewById(R.id.rate_item_value);
        layoutRateAmbient = (View) this.findViewById(R.id.layout_rate_ambient);
		rateAmbientValue = (TextView) layoutRateAmbient.findViewById(R.id.rate_item_value);
        layoutRateService = (View) this.findViewById(R.id.layout_rate_service);
		rateServiceValue = (TextView) layoutRateService.findViewById(R.id.rate_item_value);
        layoutRateValue = (View) this.findViewById(R.id.layout_rate_value);
		rateValueValue = (TextView) layoutRateValue.findViewById(R.id.rate_item_value);

		View mapButtonSubmit = findViewById(R.id.button_map); 
		mapButtonSubmit.setOnClickListener(this);
		View backButtonSubmit = findViewById(R.id.button_back); 
		backButtonSubmit.setOnClickListener(this);
		View tipsButtonSubmit = findViewById(R.id.button_tips); 
		tipsButtonSubmit.setOnClickListener(this);
		View photosButtonSubmit = findViewById(R.id.button_photos); 
		photosButtonSubmit.setOnClickListener(this);

		View friendsButtonSubmit = findViewById(R.id.button_friends); 
		friendsButtonSubmit.setOnClickListener(this);
		View rateButtonSubmit = findViewById(R.id.button_rate); 
		rateButtonSubmit.setOnClickListener(this);
		View userButtonSubmit = findViewById(R.id.button_user); 
		userButtonSubmit.setOnClickListener(this);

		lv = (ListView) findViewById(android.R.id.list);
		
        restaurantsList = new ArrayList<Restaurant>();
        this.listAdapter = new RestaurantsListAdapter(this, R.layout.restaurants_list, restaurantsList, false);
        setListAdapter(this.listAdapter);
		
    }
    
	@Override
	protected void onResume() { 
		super.onResume();
		try {
			Bundle extras = getIntent().getBundleExtra("si.kubit.restaurantrating.RestaurantRateActivity");
			if (extras != null) {
				restaurant = extras.getString("restaurant");
				userRate = extras.getBoolean("user_rate");
				Util.addPreferencies("restaurant", restaurant, this);
				Util.addPreferencies("user_rate", Boolean.toString(userRate), this);
			} else {
				restaurant   = PreferenceManager.getDefaultSharedPreferences(getBaseContext()). getString("restaurant", null);
				userRate   = Boolean.parseBoolean(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("user_rate", null));				
			}
			Log.d("restaurant=", restaurant);
			jobject = new JSONObject(restaurant);

            getRestaurant();
 			
			restaurantId = (String) jobject.getString("id");
			rateAvg = decimalFormat.format(Double.parseDouble(jobject.getString("rateAvg")));
			rateFoodAvg = Double.parseDouble(jobject.getString("rateFoodAvg"));
			rateAmbientAvg = Double.parseDouble(jobject.getString("rateAmbientAvg"));
			rateServiceAvg = Double.parseDouble(jobject.getString("rateServiceAvg"));
			rateValueAvg = Double.parseDouble(jobject.getString("rateValueAvg"));
			
			TextView restaurantName = (TextView)this.findViewById(R.id.text_restaurnt_name);
			restaurantName.setText(Util.cutText(jobject.getString("name"), 15));
			Util.addPreferencies("restaurant_name", jobject.getString("name"), this);
			
			TextView buttonTips = (TextView)this.findViewById(R.id.button_tips);
			TextView buttonPhotos = (TextView)this.findViewById(R.id.button_photos);
			textRateTitle = (TextView)this.findViewById(R.id.text_restaurant_rate_title);
			buttonTips.setText(getString(R.string.tips) + "\n (" + jobject.getString("tipCount") + ")");
			buttonPhotos.setText(getString(R.string.photos) + "\n (" + jobject.getString("photoCount") + ")");
			textRateTitle.setText(getString(R.string.rate_title));
			
			mHandler.removeCallbacks(mSetRatesTask);
	        mHandler.postDelayed(mSetRatesTask, 2000);
	        
	        if (userRate) {
	        	showRatesTask();
	        }
	        
		} catch (Exception e) {e.printStackTrace();}			
	}
	
    private void getRestaurant(){
        try{
        	restaurantsList = new ArrayList<Restaurant>();
    		Restaurant r = new Restaurant();
    		r.setRateAvg(Double.parseDouble(jobject.getString("rateAvg")));
    		r.setRateCount(Integer.parseInt(jobject.getString("rateCount")));
    		r.setName(jobject.getString("name"));
    		r.setCategory(jobject.getString("category"));
    		if (jobject.has("distance"))
    			r.setDistance(Long.parseLong(jobject.getString("distance")));
    		restaurantsList.add(r);
          } catch (Exception e) { 
            Log.e("BACKGROUND_PROC", e.getMessage());
          }
          runOnUiThread(returnRes);
      }

    
    private Runnable returnRes = new Runnable() {
    	public void run() {
    		listAdapter.clear();
            if(restaurantsList != null && restaurantsList.size() > 0){
            	listAdapter.notifyDataSetChanged();
                for(int i=0;i<restaurantsList.size();i++)
                	listAdapter.add(restaurantsList.get(i));
            }
            listAdapter.notifyDataSetChanged();
        }
      };

      
    private Runnable mSetRatesTask = new Runnable() {
 	   public void run() {
 	       int rateFood = Integer.parseInt(rateFoodValue.getText()+"");
 	       int rateAmbient = Integer.parseInt(rateAmbientValue.getText()+"");
 	       int rateService = Integer.parseInt(rateServiceValue.getText()+"");
 	       int rateValue = Integer.parseInt(rateValueValue.getText()+"");

 	       if (rateFood > 0 &&
 	    	   rateAmbient > 0 &&
 	    	   rateService > 0 &&
 	    	   rateValue > 0) {
 	    	   textRateTitle.setText(getString(R.string.rate_values_title));
 	 	       setRateItem(layoutRateFood, rateFood, R.drawable.rate_item_food_press, R.drawable.rate_item_food);
 	 	       setRateItem(layoutRateAmbient, rateAmbient, R.drawable.rate_item_ambient_press, R.drawable.rate_item_ambient);
 	 	       setRateItem(layoutRateService, rateService, R.drawable.rate_item_service_press, R.drawable.rate_item_service);
 	 	       setRateItem(layoutRateValue, rateValue, R.drawable.rate_item_value_press, R.drawable.rate_item_value);
 	 	        
 	 	       //nastavim nove vrednosti za rate
 	 	       try {
 	 	    	   int count = Integer.parseInt(jobject.getString("rateCount"));
 	 	    	   jobject.put("rateAvg", ((Double.parseDouble(rateAvg)*count)+rateFood+rateAmbient+rateService+rateValue)/(count+4));
	 	 	       jobject.put("rateFoodAvg", ((rateFoodAvg*count)+rateFood)/(count+1));
	 	 	       jobject.put("rateAmbientAvg", ((rateAmbientAvg*count)+rateAmbient)/(count+1));
	 	 	       jobject.put("rateServiceAvg", ((rateServiceAvg*count)+rateService)/(count+1));
	 	 	       jobject.put("rateValueAvg", ((rateValueAvg*count)+rateValue)/(count+1));
	 	 	       rateFoodAvg = Double.parseDouble(jobject.getString("rateFoodAvg"));
	 	 	       rateAmbientAvg = Double.parseDouble(jobject.getString("rateAmbientAvg"));
	 			   rateServiceAvg = Double.parseDouble(jobject.getString("rateServiceAvg"));
	 			   rateValueAvg = Double.parseDouble(jobject.getString("rateValueAvg"));
	 			   TextView textRate = (TextView)findViewById(R.id.text_rate);
	 			   textRate.setText(decimalFormat.format(Double.parseDouble(jobject.getString("rateAvg"))));
 	 			} catch (Exception e) {e.printStackTrace();}			
 				
 			   //vpisem podatke v bazo
	 	 	   List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	 	       nameValuePairs.add(new BasicNameValuePair("restaurant_id", restaurantId+""));
	 	       nameValuePairs.add(new BasicNameValuePair("user_id", "1"));
	 	       nameValuePairs.add(new BasicNameValuePair("rate_food", rateFood+""));
	 	       nameValuePairs.add(new BasicNameValuePair("rate_ambient", rateAmbient+""));
	 	       nameValuePairs.add(new BasicNameValuePair("rate_service", rateService+""));
	 	       nameValuePairs.add(new BasicNameValuePair("rate_value", rateValue+""));
		 	
	 	      
	 	       try { 
	 	       		String result = ((RestaurantRating)getApplicationContext()).getComm().post("rates", nameValuePairs);
	 	       } catch (Exception e) {}	

			   mHandler.removeCallbacks(mShowRatesTask);
		       mHandler.postDelayed(mShowRatesTask, 4000);
	 	       
 	 	       mHandler.removeCallbacks(mSetRatesTask);
 	       } else {
 			   mHandler.postDelayed(this, 2000);
 	       }
 	   }
 	};
 	
    private Runnable mShowRatesTask = new Runnable() {
  	   public void run() {
  		 showRatesTask();
   	   }
  	};

  	private void showRatesTask() {
	       setRateItem(layoutRateFood, (int) Math.round(rateFoodAvg), R.drawable.rate_item_food_press, R.drawable.rate_item_food);
	       setRateItem(layoutRateAmbient, (int) Math.round(rateAmbientAvg), R.drawable.rate_item_ambient_press, R.drawable.rate_item_ambient);
	       setRateItem(layoutRateService, (int) Math.round(rateServiceAvg), R.drawable.rate_item_service_press, R.drawable.rate_item_service);
	       setRateItem(layoutRateValue, (int) Math.round(rateValueAvg), R.drawable.rate_item_value_press, R.drawable.rate_item_value);

	 	   showRateItem(layoutRateFood, rateFoodAvg, R.drawable.rate_item_food_press);
	       showRateItem(layoutRateAmbient, rateAmbientAvg, R.drawable.rate_item_ambient_press);
	       showRateItem(layoutRateService, rateServiceAvg, R.drawable.rate_item_service_press);
	       showRateItem(layoutRateValue, rateValueAvg, R.drawable.rate_item_value_press);
	       
	       mHandler.removeCallbacks(mShowRatesTask);  	
  	}

  	private void setRateItem(View layout, int rate, int item_pressed, int item) {
      TextView rateItem1 = (TextView) layout.findViewById(R.id.rate_item_1);
      TextView rateItem2 = (TextView) layout.findViewById(R.id.rate_item_2);
      TextView rateItem3 = (TextView) layout.findViewById(R.id.rate_item_3);
      TextView rateItem4 = (TextView) layout.findViewById(R.id.rate_item_4);
      TextView rateItem5 = (TextView) layout.findViewById(R.id.rate_item_5);
      rateItem1.setClickable(false);
      rateItem2.setClickable(false);
      rateItem3.setClickable(false);
      rateItem4.setClickable(false);
      rateItem5.setClickable(false);
      rateItem1.setOnClickListener(null);
      rateItem2.setOnClickListener(null);
      rateItem3.setOnClickListener(null);
      rateItem4.setOnClickListener(null);
      rateItem5.setOnClickListener(null);
      rateItem1.setOnTouchListener(null);
      rateItem2.setOnTouchListener(null);
      rateItem3.setOnTouchListener(null);
      rateItem4.setOnTouchListener(null);
      rateItem5.setOnTouchListener(null);
      rateItem1.setVisibility(rate > 0 ? View.VISIBLE : View.INVISIBLE);
      rateItem2.setVisibility(rate > 1 ? View.VISIBLE : View.INVISIBLE);
      rateItem3.setVisibility(rate > 2 ? View.VISIBLE : View.INVISIBLE);
      rateItem4.setVisibility(rate > 3 ? View.VISIBLE : View.INVISIBLE);
      rateItem5.setVisibility(rate > 4 ? View.VISIBLE : View.INVISIBLE);
      rateItem1.setBackgroundResource(rate > 0 ? item_pressed : item);
      rateItem2.setBackgroundResource(rate > 1 ? item_pressed : item);
      rateItem3.setBackgroundResource(rate > 2 ? item_pressed : item);
      rateItem4.setBackgroundResource(rate > 3 ? item_pressed : item);
      rateItem5.setBackgroundResource(rate > 4 ? item_pressed : item);
	}

  	private void showRateItem(View layout, double rateAvg, int item_pressed) {
        TextView rateItem5 = (TextView) layout.findViewById(R.id.rate_item_5);
        rateItem5.setText(decimalFormat.format(rateAvg));
        rateItem5.setBackgroundResource(item_pressed);
        if (Double.parseDouble(decimalFormat.format(rateAvg)) < 4.5)
        	rateItem5.setBackgroundDrawable(getResources().getDrawable(R.drawable.rate_item_default));
        rateItem5.setVisibility(View.VISIBLE);

  	}	
  		
  	@Override
    protected void onPause() {
    	super.onPause();
    	mHandler.removeCallbacks(mSetRatesTask);
    	mHandler.removeCallbacks(mShowRatesTask);
    }

    public void onClick(View v) {
    	switch (v.getId()) { 
			case R.id.button_map:
				break;
			case R.id.button_back:
		    	Intent intentRestaurants = new Intent(RestaurantRateActivity.this, RestaurantsActivity.class);
			  	RestaurantRateActivity.this.startActivity(intentRestaurants);
				break;
			case R.id.button_tips:
		    	Intent intentRestaurantTips = new Intent(RestaurantRateActivity.this, RestaurantTipsActivity.class);
				Bundle extras = new Bundle();
			  	extras.putString("restaurant_id", restaurantId);
			  	intentRestaurantTips.putExtra("si.kubit.restaurantrating.RestaurantTipsActivity", extras);
			  	RestaurantRateActivity.this.startActivity(intentRestaurantTips);
				break;
			case R.id.button_photos:
		    	Intent intentRestaurantPhotos = new Intent(RestaurantRateActivity.this, RestaurantPhotosActivity.class);
			  	extras = new Bundle();
			  	extras.putString("restaurant_id", restaurantId);
			  	intentRestaurantPhotos.putExtra("si.kubit.restaurantrating.RestaurantPhotosActivity", extras);
			  	RestaurantRateActivity.this.startActivity(intentRestaurantPhotos);
				break;
			case R.id.button_friends:
    			Intent userRates = new Intent(this, UserRatesActivity.class); 
    			startActivity(userRates); 
				break;
			case R.id.button_rate:
    			Intent restaurants = new Intent(this, RestaurantsActivity.class); 
    			startActivity(restaurants); 
				break;
			case R.id.button_user:
				break;
    	}
	}


}