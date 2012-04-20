package si.kubit.restaurantrating;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import si.kubit.restaurantrating.conn.Foursquare;
import si.kubit.restaurantrating.util.Util;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapRestaurantActivity extends MapActivity {
	
//	private static final int AUTHORIZATION_REQUEST = 1338;
	
	MapView mapView;
	List<Overlay> mapOverlays;
	protected MapController mapController;
	protected LocationListener locationListener;
	private MyLocationOverlay myLocationOverlay;
	
	private RestaurantItemizedOverlay restaurantItemizedOverlay;

	private JSONObject jRestaurant;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_restaurant);
        
   		mapView = (MapView) findViewById(R.id.restaurant_map_view);
        mapView.setBuiltInZoomControls(true);
        mapController = mapView.getController();
	    mapController.setZoom(Integer.parseInt(getString(R.string.map_restaurant_zoom_level)));
	        
        myLocationOverlay = new MyLocationOverlay(this, mapView);
		
		mapOverlays = mapView.getOverlays();
        mapOverlays.add(myLocationOverlay);

		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();
		
		restaurantItemizedOverlay = new RestaurantItemizedOverlay(this.getResources().getDrawable(R.drawable.poi), this, screenWidth, screenHeight);
        mapOverlays.add(restaurantItemizedOverlay);
        
        mapView.postInvalidate();
    }

    private void getRestaurant(){
        try{
			Bundle extras = getIntent().getBundleExtra("si.kubit.restaurantrating.MapRestaurantActivity");
			String rateCount = extras.getString("rate_count");
			String rateAvg = extras.getString("rate_avg");
			String restaurantId = PreferenceManager.getDefaultSharedPreferences(getBaseContext()). getString("restaurant_id", null);
			
			jRestaurant = ((RestaurantRating)getApplicationContext()).getFoursquare().getRestaurant(restaurantId);
        	restaurantItemizedOverlay.clear();
			
			ImageView restaurantIcon = (ImageView) findViewById(R.id.restaurant_icon);
			TextView restaurantName = (TextView) findViewById(R.id.text_restaurant_name);
			TextView restaurantAddress = (TextView) findViewById(R.id.text_restaurant_address);
			TextView restaurantCity = (TextView) findViewById(R.id.text_restaurant_city);
			TextView restaurantPhone = (TextView) findViewById(R.id.text_restaurant_phone);
			TextView restaurantRateAvg = (TextView) findViewById(R.id.text_rate_avg_value);
			TextView restaurantRateCount = (TextView) findViewById(R.id.text_rate_count_value);
			restaurantRateAvg.setText(rateAvg);
			restaurantRateCount.setText(rateCount);
			
			JSONObject jContact = (JSONObject) jRestaurant.get("contact");
			JSONObject jLocation = (JSONObject) jRestaurant.get("location");
			JSONArray jCategories = (JSONArray) jRestaurant.get("categories");
			JSONObject jCategoryIcon = null;
			for(int i=0; i<jCategories.length(); i++) {
				JSONObject jCategory = jCategories.getJSONObject(0);
				if (jCategory.has("primary") && Boolean.parseBoolean(jCategory.getString("primary"))) {
					jCategoryIcon = (JSONObject) jCategory.get("icon");
					break;
				}
			}
			
			BitmapDrawable d = (BitmapDrawable)Util.ImageOperations((String)jCategoryIcon.getString("prefix")+64+(String)jCategoryIcon.getString("name"));
			restaurantIcon.setBackgroundDrawable(d);
			restaurantName.setText(jRestaurant.getString("name"));
			if (jContact.has("address"))
				restaurantAddress.setText(jLocation.getString("address"));
			if (jContact.has("city"))
				restaurantCity.setText(jLocation.getString("city"));
			if (jContact.has("formattedPhone"))
				restaurantPhone.setText(jContact.getString("formattedPhone"));
			
    		int lat = (int) (Double.parseDouble(jLocation.getString("lat")) * 1E6);
			int lng = (int) (Double.parseDouble(jLocation.getString("lng")) * 1E6);
			GeoPoint point = new GeoPoint(lat, lng);

   			OverlayItem overlayitem = new OverlayItem(point, null, null);
   			restaurantItemizedOverlay.addOverlay(overlayitem);

   			mapController.animateTo(point);
	        mapView.invalidate();

       } catch (Exception e) { 
 			e.printStackTrace();
        	Toast toast = Toast.makeText(this, getString(R.string.json_error), Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        	toast.show();
      }
    }

	
    @Override
    protected void onResume() { 
		super.onResume();
		myLocationOverlay.enableCompass();
        getRestaurant();
	}
	
    @Override
    protected void onPause() {
    	super.onPause();
    	myLocationOverlay.disableCompass();
    }
    
	@Override
    protected boolean isRouteDisplayed() { 
		return false; 
	}     

}