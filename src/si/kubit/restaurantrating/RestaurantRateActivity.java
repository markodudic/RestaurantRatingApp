package si.kubit.restaurantrating;
 
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class RestaurantRateActivity extends Activity implements OnClickListener {
	
	JSONObject jobject;
	
	private String restaurantId;
	private String rateAvg;
	private String rateFoodAvg;
	private String rateAmbientAvg;
	private String rateServiceAvg;
	private String rateValueAvg;
    
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

	ListView lv;
	
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

		lv = (ListView) findViewById(R.id.restaurant_rate_list);
    }
    
	@Override
	protected void onResume() { 
		super.onResume();
		try {
			Bundle extras = getIntent().getBundleExtra("si.kubit.restaurantrating.RestaurantRateActivity");
			String restaurant = extras.getString("restaurant");
			Log.d("restaurant=", restaurant);
			jobject = new JSONObject(restaurant);

			restaurantId = (String) jobject.getString("id");
			rateAvg = decimalFormat.format(Double.parseDouble(jobject.getString("rateAvg")));
			rateFoodAvg = decimalFormat.format(Double.parseDouble(jobject.getString("rateFoodAvg")));
			rateAmbientAvg = decimalFormat.format(Double.parseDouble(jobject.getString("rateAmbientAvg")));
			rateServiceAvg = decimalFormat.format(Double.parseDouble(jobject.getString("rateServiceAvg")));
			rateValueAvg = decimalFormat.format(Double.parseDouble(jobject.getString("rateValueAvg")));

			/*TextView textRate = (TextView)this.findViewById(R.id.text_rate);
			TextView textReviews=(TextView)this.findViewById(R.id.text_reviews);
			TextView textRestaurantName = (TextView)this.findViewById(R.id.text_restaurant_name);
			TextView textRestaurantCategory = (TextView)this.findViewById(R.id.text_restaurant_category);
			TextView textRestaurantDistance = (TextView)this.findViewById(R.id.text_restaurant_distance);
			
			textRate.setText(decimalFormat.format(Double.parseDouble(jobject.getString("rateAvg"))));
			textReviews.setText(jobject.getString("rateCount")+" "+getString(R.string.reviews));
			textRestaurantName.setText(jobject.getString("name").toUpperCase());
			textRestaurantCategory.setText(jobject.getString("category").toUpperCase());
			textRestaurantDistance.setText(jobject.getString("distance")+" "+getString(R.string.distance));
			*/
			JSONArray jdata = new JSONArray();
			jdata.put(jobject);
			RestaurantsListAdapter listAdapter = new RestaurantsListAdapter(this, jdata, getApplicationContext());
			lv.setAdapter(listAdapter);
			
			
			TextView buttonTips = (TextView)this.findViewById(R.id.button_tips);
			TextView buttonPhotos = (TextView)this.findViewById(R.id.button_photos);
			textRateTitle = (TextView)this.findViewById(R.id.text_restaurant_rate_title);
			buttonTips.setText(getString(R.string.tips) + "\n (" + jobject.getString("tipCount") + ")");
			buttonPhotos.setText(getString(R.string.photos) + "\n (" + jobject.getString("photoCount") + ")");
			textRateTitle.setText(getString(R.string.rate_title));
			
			mHandler.removeCallbacks(mSetRatesTask);
	        mHandler.postDelayed(mSetRatesTask, 2000);
		} catch (Exception e) {e.printStackTrace();}			
	}
	
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
 	 	       setRateItem(layoutRateFood, rateFood);
 	 	       setRateItem(layoutRateAmbient, rateAmbient);
 	 	       setRateItem(layoutRateService, rateService);
 	 	       setRateItem(layoutRateValue, rateValue);
 	 	        
 	 	       //nastavim nove vrednosti za rate
 	 	       try {
 	 	    	   int count = Integer.parseInt(jobject.getString("rateCount"));
 	 	    	   jobject.put("rateAvg", ((Double.parseDouble(rateAvg)*count)+rateFood+rateAmbient+rateService+rateValue)/(count+4));
	 	 	       jobject.put("rateFoodAvg", ((Double.parseDouble(rateFoodAvg)*count)+rateFood)/(count+1));
	 	 	       jobject.put("rateAmbientAvg", ((Double.parseDouble(rateAmbientAvg)*count)+rateAmbient)/(count+1));
	 	 	       jobject.put("rateServiceAvg", ((Double.parseDouble(rateServiceAvg)*count)+rateService)/(count+1));
	 	 	       jobject.put("rateValueAvg", ((Double.parseDouble(rateValueAvg)*count)+rateValue)/(count+1));
	 	 	       rateFoodAvg = decimalFormat.format(Double.parseDouble(jobject.getString("rateFoodAvg")));
	 	 	       rateAmbientAvg = decimalFormat.format(Double.parseDouble(jobject.getString("rateAmbientAvg")));
	 			   rateServiceAvg = decimalFormat.format(Double.parseDouble(jobject.getString("rateServiceAvg")));
	 			   rateValueAvg = decimalFormat.format(Double.parseDouble(jobject.getString("rateValueAvg")));
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
		 	
	 	      
	 	       Comm c = new Comm(getString(R.string.server_url), null, null);
	 	       try { 
	 	       		String result = c.post("rates", nameValuePairs);
	 	       } catch (Exception e) {}	

			   mHandler.removeCallbacks(mShowRatesTask);
		       mHandler.postDelayed(mShowRatesTask, 5000);
	 	       
 	 	       mHandler.removeCallbacks(mSetRatesTask);
 	       } else {
 			   mHandler.postDelayed(this, 2000);
 	       }
 	   }
 	};
 	
    private Runnable mShowRatesTask = new Runnable() {
  	   public void run() {
  		   int rateFood = Integer.parseInt(rateFoodValue.getText()+"");
	       int rateAmbient = Integer.parseInt(rateAmbientValue.getText()+"");
	       int rateService = Integer.parseInt(rateServiceValue.getText()+"");
	       int rateValue = Integer.parseInt(rateValueValue.getText()+"");

	       showRateItem(layoutRateFood, rateFoodAvg, rateFood);
  	       showRateItem(layoutRateAmbient, rateAmbientAvg, rateAmbient);
  	       showRateItem(layoutRateService, rateServiceAvg, rateService);
  	       showRateItem(layoutRateValue, rateValueAvg, rateValue);
	       
	       mHandler.removeCallbacks(mShowRatesTask);
   	   }
  	};
  	
  	private void setRateItem(View layout, int rate) {
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
	}

  	private void showRateItem(View layout, String rateAvg, int rate) {
        TextView rateItem5 = (TextView) layout.findViewById(R.id.rate_item_5);
        rateItem5.setText(rateAvg);
        if (rate < 5)
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
    	}
	}

 
}