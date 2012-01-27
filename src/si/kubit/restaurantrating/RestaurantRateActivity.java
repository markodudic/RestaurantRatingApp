package si.kubit.restaurantrating;

import java.text.DecimalFormat;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class RestaurantRateActivity extends Activity implements OnClickListener {
	
	LocationManager locationManager;
	LocationListener locationListener;
	String provider;
	
	String restaurants = "";
    
	ListView lv;
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


    }
    
	@Override
	protected void onResume() { 
		super.onResume();
		try {
			Bundle extras = getIntent().getBundleExtra("si.kubit.restaurantrating.RestaurantRateActivity");
			String restaurant = extras.getString("restaurant");
			Log.d("restaurant=", restaurant);
		    JSONObject jobject = (JSONObject)new JSONTokener(restaurant).nextValue();
	    	
			TextView textRate = (TextView)this.findViewById(R.id.text_rate);
			TextView textReviews=(TextView)this.findViewById(R.id.text_reviews);
			TextView textRestaurantName = (TextView)this.findViewById(R.id.text_restaurant_name);
			TextView textRestaurantCategory = (TextView)this.findViewById(R.id.text_restaurant_category);
			TextView textRestaurantDistance = (TextView)this.findViewById(R.id.text_restaurant_distance);
			Button buttonTips = (Button)this.findViewById(R.id.button_tips);
			Button buttonPhotos = (Button)this.findViewById(R.id.button_photos);
			textRateTitle = (TextView)this.findViewById(R.id.text_restaurant_rate_title);
			
			textRate.setText(jobject.getString("rateAvg"));
			textReviews.setText(jobject.getString("rateCount")+" "+getString(R.string.reviews));
			textRestaurantName.setText(jobject.getString("name"));
			textRestaurantCategory.setText(jobject.getString("category"));
			textRestaurantDistance.setText(jobject.getString("distance")+" "+getString(R.string.distance));
			buttonTips.setText(getString(R.string.tips) + " (" + jobject.getString("tipCount") + ")");
			buttonPhotos.setText(getString(R.string.photos) + " (" + jobject.getString("photoCount") + ")");
			textRateTitle.setText(getString(R.string.rate_title));
			
			mHandler.removeCallbacks(mUpdateTimeTask);
	        mHandler.postDelayed(mUpdateTimeTask, 2000);
	        

	            
	            
		} catch (Exception e) {e.printStackTrace();}			
	}
	
    private Runnable mUpdateTimeTask = new Runnable() {
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
 	        
 	 	       mHandler.removeCallbacks(mUpdateTimeTask);
 	       } else {
 			   mHandler.postDelayed(this, 2000);
 	       }
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

 	@Override
    protected void onPause() {
    	super.onPause();
    	mHandler.removeCallbacks(mUpdateTimeTask);
    	finish(); 
    }

    public void onClick(View v) {
    	Log.d("******CLICK ********", "");
		switch (v.getId()) { 
			case R.id.button_map:
				break;
			case R.id.button_settings:
				break;
    	}
	}

 
}