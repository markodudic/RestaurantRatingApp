package si.kubit.restaurantrating;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RestaurantRateActivity extends Activity implements OnClickListener {
	
	private String restaurantId;
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
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_rate);
        
        Log.d("**********************************", "RestaurantsRateActivity");
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
		View settingsButtonSubmit = findViewById(R.id.button_settings); 
		settingsButtonSubmit.setOnClickListener(this);
		View tipsButtonSubmit = findViewById(R.id.button_tips); 
		tipsButtonSubmit.setOnClickListener(this);
		View photosButtonSubmit = findViewById(R.id.button_photos); 
		photosButtonSubmit.setOnClickListener(this);


    }
    
	@Override
	protected void onResume() { 
		super.onResume();
		try {
			Bundle extras = getIntent().getBundleExtra("si.kubit.restaurantrating.RestaurantRateActivity");
			String restaurant = extras.getString("restaurant");
			Log.d("restaurant=", restaurant);
			JSONObject jobject = new JSONObject(restaurant);
			restaurantId = (String) jobject.getString("id");
			rateFoodAvg = decimalFormat.format(Double.parseDouble(jobject.getString("rateFoodAvg")));
			rateAmbientAvg = decimalFormat.format(Double.parseDouble(jobject.getString("rateAmbientAvg")));
			rateServiceAvg = decimalFormat.format(Double.parseDouble(jobject.getString("rateServiceAvg")));
			rateValueAvg = decimalFormat.format(Double.parseDouble(jobject.getString("rateValueAvg")));

			TextView textRate = (TextView)this.findViewById(R.id.text_rate);
			TextView textReviews=(TextView)this.findViewById(R.id.text_reviews);
			TextView textRestaurantName = (TextView)this.findViewById(R.id.text_restaurant_name);
			TextView textRestaurantCategory = (TextView)this.findViewById(R.id.text_restaurant_category);
			TextView textRestaurantDistance = (TextView)this.findViewById(R.id.text_restaurant_distance);
			Button buttonTips = (Button)this.findViewById(R.id.button_tips);
			Button buttonPhotos = (Button)this.findViewById(R.id.button_photos);
			textRateTitle = (TextView)this.findViewById(R.id.text_restaurant_rate_title);
			
			textRate.setText(decimalFormat.format(Double.parseDouble(jobject.getString("rateAvg"))));
			textReviews.setText(jobject.getString("rateCount")+" "+getString(R.string.reviews));
			textRestaurantName.setText(jobject.getString("name"));
			textRestaurantCategory.setText(jobject.getString("category"));
			textRestaurantDistance.setText(jobject.getString("distance")+" "+getString(R.string.distance));
			buttonTips.setText(getString(R.string.tips) + " (" + jobject.getString("tipCount") + ")");
			buttonPhotos.setText(getString(R.string.photos) + " (" + jobject.getString("photoCount") + ")");
			textRateTitle.setText(getString(R.string.rate_title));
			
			mHandler.removeCallbacks(mSetRatesTask);
	        mHandler.postDelayed(mSetRatesTask, 2000);
	        

	            
	            
		} catch (Exception e) {e.printStackTrace();}			
	}
	
    private Runnable mSetRatesTask = new Runnable() {
 	   public void run() {
 	       Log.d("RUN", "********************************");
 	       int rateFood = Integer.parseInt(rateFoodValue.getText()+"");
 	       int rateAmbient = Integer.parseInt(rateAmbientValue.getText()+"");
 	       int rateService = Integer.parseInt(rateServiceValue.getText()+"");
 	       int rateValue = Integer.parseInt(rateValueValue.getText()+"");

 	       if (rateFood > 0 &&
 	    	   rateAmbient > 0 &&
 	    	   rateService > 0 &&
 	    	   rateValue > 0) {
 	 	       Log.d("**********", "PREKINI IZVAJANJE"); 
 	 	       setRateItem(layoutRateFood, rateFood);
 	 	       setRateItem(layoutRateAmbient, rateAmbient);
 	 	       setRateItem(layoutRateService, rateService);
 	 	       setRateItem(layoutRateValue, rateValue);
 	 	       textRateTitle.setText(getString(R.string.rate_values_title));
 	        
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
  	       Log.d("RUN2", "********************************");
  	       showRateItem(layoutRateFood, rateFoodAvg);
  	       showRateItem(layoutRateAmbient, rateAmbientAvg);
  	       showRateItem(layoutRateService, rateServiceAvg);
  	       showRateItem(layoutRateValue, rateValueAvg);
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
      rateItem1.setVisibility(rate > 0 ? View.VISIBLE : View.INVISIBLE);
      rateItem2.setVisibility(rate > 1 ? View.VISIBLE : View.INVISIBLE);
      rateItem3.setVisibility(rate > 2 ? View.VISIBLE : View.INVISIBLE);
      rateItem4.setVisibility(rate > 3 ? View.VISIBLE : View.INVISIBLE);
      rateItem5.setVisibility(rate > 4 ? View.VISIBLE : View.INVISIBLE);
	}

  	private void showRateItem(View layout, String rateAvg) {
        TextView rateItem5 = (TextView) layout.findViewById(R.id.rate_item_5);
        rateItem5.setText(rateAvg);
        rateItem5.setVisibility(View.VISIBLE);

  	}	
  		
  	@Override
    protected void onPause() {
    	super.onPause();
    	mHandler.removeCallbacks(mSetRatesTask);
    	mHandler.removeCallbacks(mShowRatesTask);
    	finish(); 
    }

    public void onClick(View v) {
    	Log.d("******CLICK ********", "");
		switch (v.getId()) { 
			case R.id.button_map:
				break;
			case R.id.button_settings:
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