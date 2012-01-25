package si.kubit.restaurantrating;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_rate);
        Log.d("**********************************", "RestaurantsRateActivity");
		
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
			JSONObject jobject = (JSONObject)new JSONTokener(restaurant).nextValue();
	    	
			TextView textRate = (TextView)this.findViewById(R.id.text_rate);
			TextView textReviews=(TextView)this.findViewById(R.id.text_reviews);
			TextView textRestaurantName = (TextView)this.findViewById(R.id.text_restaurant_name);
			TextView textRestaurantCategory = (TextView)this.findViewById(R.id.text_restaurant_category);
			TextView textRestaurantDistance = (TextView)this.findViewById(R.id.text_restaurant_distance);
			Button buttonTips = (Button)this.findViewById(R.id.button_tips);
			Button buttonPhotos = (Button)this.findViewById(R.id.button_photos);
			TextView textTipTitle = (TextView)this.findViewById(R.id.text_restaurant_tip_title);
			
		    textRate.setText(jobject.getString("rateAvg"));
			textReviews.setText(jobject.getString("rateCount")+" "+getString(R.string.reviews));
			textRestaurantName.setText(jobject.getString("name"));
			textRestaurantCategory.setText(jobject.getString("category"));
			textRestaurantDistance.setText(jobject.getString("distance")+" "+getString(R.string.distance));
			buttonTips.setText(getString(R.string.tips) + " (" + jobject.getString("tipCount") + ")");
			buttonPhotos.setText(getString(R.string.photos) + " (" + jobject.getString("photoCount") + ")");
			textTipTitle.setText(getString(R.string.tip_title_rate_it));
			
			
		} catch (Exception e) {e.printStackTrace();}			
	}
	
    @Override
    protected void onPause() {
    	super.onPause();
		finish(); 
    }

    public void onClick(View v) {
    	switch (v.getId()) { 
			case R.id.button_map:
				break;
			case R.id.button_settings:
				break;
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