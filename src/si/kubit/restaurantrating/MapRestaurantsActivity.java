package si.kubit.restaurantrating;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import si.kubit.restaurantrating.conn.Foursquare;
import si.kubit.restaurantrating.util.Util;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapRestaurantsActivity extends MapActivity {
	
//	private static final int AUTHORIZATION_REQUEST = 1338;
	
	MapView mapView;
	List<Overlay> mapOverlays;
	protected MapController mapController;
	protected LocationManager locationUpdateRequester;
	protected LocationListener locationListener;
	private MyLocationOverlay myLocationOverlay;
	
	private RestaurantsItemizedOverlay restaurantsItemizedOverlay;
	//private RestaurantsItemizedOverlay restaurantsBackgroundItemizedOverlay;
	private ArrayList<String> overlayVenueIds = new ArrayList<String>();

	private JSONArray jLocations;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_restaurants);
        
   		mapView = (MapView) findViewById(R.id.restaurants_map_view);
        mapView.setBuiltInZoomControls(true);
        MapController mc = mapView.getController();
        mc.setZoom(Integer.parseInt(getString(R.string.map_restaurants_zoom_level)));
		mapOverlays = mapView.getOverlays();
        
        myLocationOverlay = new MyLocationOverlay(this, mapView);

        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();
		
		LinearLayout lf = (LinearLayout) findViewById(R.id.layout_restaurants);
		LinearLayout lfl = (LinearLayout) findViewById(R.id.layout_restaurants_location);
        lfl.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {		
    	        int i = restaurantsItemizedOverlay.getLastFocusedIndex();

	    		Intent intentRestaurantRate = new Intent(MapRestaurantsActivity.this, RestaurantRateActivity.class);
			  	Bundle extras = new Bundle();
			  	extras.putString("restaurant_id", overlayVenueIds.get(i));
			  	extras.putBoolean("user_rate", true);
			  	intentRestaurantRate.putExtra("si.kubit.restaurantrating.RestaurantRateActivity", extras);
			  	intentRestaurantRate.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			  	MapRestaurantsActivity.this.startActivity(intentRestaurantRate);
		    }
        });
        TextView venue = (TextView) findViewById(R.id.text_venue);
		TextView address = (TextView) findViewById(R.id.text_address);
		TextView category = (TextView) findViewById(R.id.text_category);
		ImageView icon = (ImageView) findViewById(R.id.restaurant_icon);
        
		//restaurantsBackgroundItemizedOverlay = new RestaurantsItemizedOverlay(this.getResources().getDrawable(R.drawable.poi), this, screenWidth, screenHeight, lf, venue, address, null, 10, 20, 0, 15, category, icon);
        //mapOverlays.add(restaurantsBackgroundItemizedOverlay); 
        restaurantsItemizedOverlay = new RestaurantsItemizedOverlay(this.getResources().getDrawable(R.drawable.poi), this, screenWidth, screenHeight, lf, venue, address, null, 10, 20, 0, 15, category, icon);
        mapOverlays.add(restaurantsItemizedOverlay);

        mapOverlays.add(myLocationOverlay);

        mapView.postInvalidate();
    }

    private void getLocations(){
        try{
        	Foursquare fsq = ((RestaurantRating)getApplicationContext()).getFoursquare();
        	jLocations = fsq.getRestaurants(myLocationOverlay.getLastFix().getLatitude(),
											myLocationOverlay.getLastFix().getLongitude());
        	overlayVenueIds.clear();
			restaurantsItemizedOverlay.clear();
			//restaurantsBackgroundItemizedOverlay.clear();
			
			if (jLocations == null) return;
			
			Drawable d1 = this.getResources().getDrawable(R.drawable.image_bck);	
			for(int i=0; i<jLocations.length(); i++) {
        		JSONObject jrestaurant = (JSONObject) jLocations.getJSONObject(i);
        		JSONObject jVenueLocation = (JSONObject) jrestaurant.get("location");
        		JSONArray jCategories = (JSONArray) jrestaurant.get("categories");
    			JSONObject jCategoryIcon = null;
    			JSONObject jCategory = null;
    			for(int ii=0; ii<jCategories.length(); ii++) {
    				jCategory = jCategories.getJSONObject(ii);
    				if (jCategory.has("primary") && Boolean.parseBoolean(jCategory.getString("primary"))) {
    					jCategoryIcon = (JSONObject) jCategory.get("icon");
    					break;
    				}
    			}
    	        
    	        int lat = (int) (Double.parseDouble(jVenueLocation.getString("lat")) * 1E6);
    			int lng = (int) (Double.parseDouble(jVenueLocation.getString("lng")) * 1E6);
    			GeoPoint point = new GeoPoint(lat, lng);
 
    	        //narisem ozadje slike
    	        /*OverlayItem overlayBackgroundItem = new OverlayItem(point, null, null);
    	        overlayBackgroundItem.setMarker(d1);
    			OverlayItem.setState(d1, 0);
    			restaurantsBackgroundItemizedOverlay.addOverlay(overlayBackgroundItem, true);    	        
*/
    	        //slika frienda
    			String iconUri = (String)jCategoryIcon.getString("prefix")+64+(String)jCategoryIcon.getString("name");
    			BitmapDrawable d = (BitmapDrawable)Util.ImageOperations(iconUri);
    			OverlayItem overlayItem = new OverlayItem(point, jrestaurant.getString("name"), (jVenueLocation.has("address")?jVenueLocation.getString("address"):""));
    			overlayItem.setMarker(d);
    			OverlayItem.setState(d, 0);
    			restaurantsItemizedOverlay.addOverlay(overlayItem, true, jCategory.getString("name").toUpperCase(), iconUri);
    	        overlayVenueIds.add(jrestaurant.getString("id"));
    	        
        	}
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
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.enableCompass();
		myLocationOverlay.runOnFirstFix(new Runnable(){public void run(){
            mapView.getController().animateTo(myLocationOverlay.getMyLocation());
            	runOnUiThread(returnRes);
            }});
	}
	
    private Runnable returnRes = new Runnable() {
    	public void run() {
        	getLocations();
        }
      };
      
   @Override
    protected void onPause() {
    	super.onPause();
    	myLocationOverlay.disableMyLocation();
    	myLocationOverlay.disableCompass();
		
    }
    
	@Override
    protected boolean isRouteDisplayed() { 
		return false; 
	}     

}