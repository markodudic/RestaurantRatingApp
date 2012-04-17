package si.kubit.restaurantrating;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import si.kubit.restaurantrating.objects.User;
import si.kubit.restaurantrating.util.Util;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapFriendsActivity extends MapActivity {
	
	private static int ZOOM_LEVEL = 14;
	private static final int AUTHORIZATION_REQUEST = 1338;
	
	MapView mapView;
	List<Overlay> mapOverlays;
	protected MapController mapController;
	protected LocationManager locationUpdateRequester;
	protected LocationListener locationListener;
	private MyLocationOverlay myLocationOverlay;

	private JSONArray jLocations;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_friends);
        
   		mapView = (MapView) findViewById(R.id.friends_map_view);
        mapView.setBuiltInZoomControls(true);
        MapController mc = mapView.getController();
        mc.setZoom(ZOOM_LEVEL);
        
        myLocationOverlay = new MyLocationOverlay(this, mapView);
		
		mapOverlays = mapView.getOverlays();
        mapOverlays.add(myLocationOverlay);
        mapView.postInvalidate();
    }

    private void getFriendsLocations()
    {
    	try { 
			User user = Util.getUserFromPreferencies(this);	
			if (user.getOauthToken()==null || user.getOauthToken().equals("null")) {
				//uporabnik nima autorizacije. Zahtevam
    			Intent authorization = new Intent(this, AuthorizationActivity.class); 
    			startActivityForResult(authorization, AUTHORIZATION_REQUEST); 
			} else {
    			getLocations();
		    }
   		} catch (Exception ne) {
   			ne.printStackTrace();
        	Toast toast = Toast.makeText(this, getString(R.string.json_error), Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        	toast.show();
   		}
    }      

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (requestCode == AUTHORIZATION_REQUEST) {
    		if (resultCode == RESULT_OK) {
    			String accessToken = data.getStringExtra("accessToken");
    			//shrani oatuh za userja
    			Util.SetUserOAuth(this, accessToken);
    			getLocations();
        	} else if (resultCode == RESULT_CANCELED) {
        		if (data != null) {
		        	Toast toast = Toast.makeText(this, getString(R.string.oauth_requered), Toast.LENGTH_LONG);
		        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		        	toast.show();
        		}
	        }
    	}
    }

    private void getLocations(){
        try{
			jLocations = ((RestaurantRating)getApplicationContext()).getFoursquare().getFriendsLocations(Util.getUserFromPreferencies(this).getOauthToken());
			
			for(int i=0; i<jLocations.length(); i++) {
        		JSONObject jobject = (JSONObject) jLocations.getJSONObject(i);
        		JSONObject jUser = (JSONObject) jobject.get("user");
        		JSONObject jVenue = (JSONObject) jobject.get("venue");
        		JSONObject jVenueLocation = (JSONObject) jVenue.get("location");
    	        Log.d("jUser=", (String)jUser.getString("photo"));
    	        
    	        //Drawable d = Util.ImageOperations((String)jUser.getString("photo"));
				
    	        //FriendsItemizedOverlay friendsOverlay = new FriendsItemizedOverlay(d, this);  

    	        int lat = (int) (Double.parseDouble(jVenueLocation.getString("lat")) * 1E6);
    			int lng = (int) (Double.parseDouble(jVenueLocation.getString("lng")) * 1E6);
    			GeoPoint point = new GeoPoint(lat, lng);
    	        OverlayItem friendsLocation = new OverlayItem(point, "", "");
    	        //friendsOverlay.addOverlay(friendsLocation, point, jVenue.getString("name"), jUser.getString("firstName") + " " + (jUser.has("lastName")?jUser.getString("lastName"):""));
        		
    	        //mapOverlays.add(friendsOverlay);
    	        
    	        mapOverlays.add(new FriendsItemizedOverlay(this, point, (String)jUser.getString("photo")));
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
            }});
        getFriendsLocations();
		
	}
	
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