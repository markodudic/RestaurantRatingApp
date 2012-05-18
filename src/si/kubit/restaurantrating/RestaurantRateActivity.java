package si.kubit.restaurantrating;
 
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import si.kubit.restaurantrating.components.RestaurantRateItemLayout;
import si.kubit.restaurantrating.objects.Restaurant;
import si.kubit.restaurantrating.util.Util;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
	private String rateCount;
	private double rateFoodAvg;
	private double rateAmbientAvg;
	private double rateServiceAvg;
	private double rateValueAvg;
    
	private Handler mHandler = new Handler();
	private RestaurantRateItemLayout layoutRateFood;
	private RestaurantRateItemLayout layoutRateAmbient;
	private RestaurantRateItemLayout layoutRateService;
	private RestaurantRateItemLayout layoutRateValue;
	private TextView rateFoodValue;
	private TextView rateAmbientValue;
	private TextView rateServiceValue;
	private TextView rateValueValue;
	private TextView textRateTitle;
	private DecimalFormat decimalFormat = new DecimalFormat("0.0");

	private boolean userRate = false;
	
	ListView lv;
	private RestaurantsListAdapter listAdapter;
	private ArrayList<Restaurant> restaurantsList = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_rate);
        		
		layoutRateFood = (RestaurantRateItemLayout) this.findViewById(R.id.layout_rate_food);
		rateFoodValue = (TextView) layoutRateFood.findViewById(R.id.rate_item_value);
        layoutRateAmbient = (RestaurantRateItemLayout) this.findViewById(R.id.layout_rate_ambient);
		rateAmbientValue = (TextView) layoutRateAmbient.findViewById(R.id.rate_item_value);
        layoutRateService = (RestaurantRateItemLayout) this.findViewById(R.id.layout_rate_service);
		rateServiceValue = (TextView) layoutRateService.findViewById(R.id.rate_item_value);
        layoutRateValue = (RestaurantRateItemLayout) this.findViewById(R.id.layout_rate_value);
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
			boolean showMenu = false;
			Bundle extras = getIntent().getBundleExtra("si.kubit.restaurantrating.RestaurantRateActivity");
			if (extras != null) {
				restaurantId = extras.getString("restaurant_id");
				userRate = extras.getBoolean("user_rate");
				showMenu = extras.getBoolean("show_menu");
				Util.addPreferencies("restaurant_id", restaurantId);
				Util.addPreferencies("user_rate", Boolean.toString(userRate));
				Util.addPreferencies("show_menu", Boolean.toString(showMenu));
			} else {
				restaurantId   = PreferenceManager.getDefaultSharedPreferences(getBaseContext()). getString("restaurant_id", null);
				userRate   = Boolean.parseBoolean(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("user_rate", "false"));				
				showMenu   = Boolean.parseBoolean(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("show_menu", "false"));				
			}
			
			View layoutFooter = findViewById(R.id.layout_menu_footer); 
			View layoutRateFood = findViewById(R.id.layout_menu_rate_food); 
			if (showMenu) {
				layoutFooter.setVisibility(View.VISIBLE);
				layoutRateFood.setVisibility(View.VISIBLE);
			} else {
				layoutFooter.setVisibility(View.GONE);
				layoutRateFood.setVisibility(View.GONE);				
			}
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("venue_id", restaurantId));
			
			String restaurant = ((RestaurantRating)getApplicationContext()).getComm().post("restaurant", nameValuePairs);
			Log.d("restaurant=", restaurant);
			
			JSONObject restaurantData = ((RestaurantRating)getApplicationContext()).getFoursquare().getRestaurant(restaurantId);
			Log.d("restaurantData=", restaurantData.toString());

			jobject = new JSONArray(restaurant).getJSONObject(0);

            getRestaurant();
 			
			restaurantId = (String) jobject.getString("id");
			rateCount = jobject.getString("rateCount");
			rateAvg = decimalFormat.format(Double.parseDouble(jobject.getString("rateAvg")));
			rateFoodAvg = Double.parseDouble(jobject.getString("rateFoodAvg"));
			rateAmbientAvg = Double.parseDouble(jobject.getString("rateAmbientAvg"));
			rateServiceAvg = Double.parseDouble(jobject.getString("rateServiceAvg"));
			rateValueAvg = Double.parseDouble(jobject.getString("rateValueAvg"));
			
			TextView restaurantName = (TextView)this.findViewById(R.id.text_restaurnt_name);
			restaurantName.setText(Util.cutText(jobject.getString("name"), 15).toUpperCase());
			Util.addPreferencies("restaurant_name", jobject.getString("name"));
			Util.addPreferencies("restaurant_id", restaurantId);
			
			TextView buttonTips = (TextView)this.findViewById(R.id.button_tips);
			TextView buttonPhotos = (TextView)this.findViewById(R.id.button_photos);
			textRateTitle = (TextView)this.findViewById(R.id.text_restaurant_rate_title);
			JSONObject restaurantStats = restaurantData.getJSONObject("stats");
			buttonTips.setText(getString(R.string.tips) + "\n (" + (restaurantStats.has("tipCount")?restaurantStats.getString("tipCount"):"0") + ")");
			buttonPhotos.setText(getString(R.string.photos) + "\n (" + (restaurantStats.has("photoCount")?restaurantStats.getString("photoCount"):"0") + ")");
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
 	 	       setRateItem(layoutRateFood, rateFood, layoutRateFood.getRateItemPressedDrawable(), layoutRateFood.getRateItemDrawable());
 	 	       setRateItem(layoutRateAmbient, rateAmbient, layoutRateAmbient.getRateItemPressedDrawable(), layoutRateAmbient.getRateItemDrawable());
 	 	       setRateItem(layoutRateService, rateService, layoutRateService.getRateItemPressedDrawable(), layoutRateService.getRateItemDrawable());
 	 	       setRateItem(layoutRateValue, rateValue, layoutRateValue.getRateItemPressedDrawable(), layoutRateValue.getRateItemDrawable());
 	 	        
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
	 	       nameValuePairs.add(new BasicNameValuePair("user_id", Util.getUserFromPreferencies().getId()+""));
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
	       setRateItem(layoutRateFood, (int) Math.round(rateFoodAvg), layoutRateFood.getRateItemPressedDrawable(), layoutRateFood.getRateItemDrawable());
	       setRateItem(layoutRateAmbient, (int) Math.round(rateAmbientAvg), layoutRateAmbient.getRateItemPressedDrawable(), layoutRateAmbient.getRateItemDrawable());
	       setRateItem(layoutRateService, (int) Math.round(rateServiceAvg), layoutRateService.getRateItemPressedDrawable(), layoutRateService.getRateItemDrawable());
	       setRateItem(layoutRateValue, (int) Math.round(rateValueAvg), layoutRateValue.getRateItemPressedDrawable(), layoutRateValue.getRateItemDrawable());

	 	   showRateItem(layoutRateFood, rateFoodAvg, layoutRateFood.getRateItemPressedDrawable());
	       showRateItem(layoutRateAmbient, rateAmbientAvg, layoutRateAmbient.getRateItemPressedDrawable());
	       showRateItem(layoutRateService, rateServiceAvg, layoutRateService.getRateItemPressedDrawable());
	       showRateItem(layoutRateValue, rateValueAvg, layoutRateValue.getRateItemPressedDrawable());
	       
	       mHandler.removeCallbacks(mShowRatesTask);  	
  	}

  	private void setRateItem(View layout, int rate, Drawable item_pressed, Drawable item) {
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
      rateItem1.setBackgroundDrawable(rate > 0 ? item_pressed : item);
      rateItem2.setBackgroundDrawable(rate > 1 ? item_pressed : item);
      rateItem3.setBackgroundDrawable(rate > 2 ? item_pressed : item);
      rateItem4.setBackgroundDrawable(rate > 3 ? item_pressed : item);
      rateItem5.setBackgroundDrawable(rate > 4 ? item_pressed : item);
	}

  	private void showRateItem(View layout, double rateAvg, Drawable item_pressed) {
        TextView rateItem5 = (TextView) layout.findViewById(R.id.rate_item_5);
        rateItem5.setText(decimalFormat.format(rateAvg));
        rateItem5.setBackgroundDrawable(item_pressed);
        if (Double.parseDouble(decimalFormat.format(rateAvg)) < 4.5)
        	rateItem5.setBackgroundDrawable(layoutRateFood.getRateItemDefaultDrawable());
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
		    	Intent mapRestaurantIntent = new Intent(RestaurantRateActivity.this, MapRestaurantActivity.class);
				Bundle extras = new Bundle();
			  	extras.putString("rate_count", rateCount);
			  	extras.putString("rate_avg", rateAvg);
			  	mapRestaurantIntent.putExtra("si.kubit.restaurantrating.MapRestaurantActivity", extras);
			  	RestaurantRateActivity.this.startActivity(mapRestaurantIntent);
				break;
			case R.id.button_back:
		    	Intent intentRestaurants = new Intent(RestaurantRateActivity.this, RestaurantsActivity.class);
			  	RestaurantRateActivity.this.startActivity(intentRestaurants);
				break;
			case R.id.button_tips:
		    	Intent intentRestaurantTips = new Intent(RestaurantRateActivity.this, RestaurantTipsActivity.class);
			  	RestaurantRateActivity.this.startActivity(intentRestaurantTips);
				break;
			case R.id.button_photos:
		    	Intent intentRestaurantPhotos = new Intent(RestaurantRateActivity.this, RestaurantPhotosActivity.class);
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