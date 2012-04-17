package si.kubit.restaurantrating;


import java.util.ArrayList;

import si.kubit.restaurantrating.util.Util;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class FriendsItemizedOverlay extends Overlay {
//public class FriendsItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private ArrayList<String> mOverlaysVenues = new ArrayList<String>();
	private ArrayList<String> mOverlaysFriends = new ArrayList<String>();
	//private Context mContext;
	private final GeoPoint geoPoint;
	private final Context context;
	private final String imageUri;

	/*public FriendsItemizedOverlay(Drawable defaultMarker, Context context) {
		  super(boundCenterBottom(defaultMarker));
		  mContext = context;
	}*/
	public FriendsItemizedOverlay(Context context, GeoPoint geoPoint, String imageUri) {
	    this.context = context;
	    this.geoPoint = geoPoint;
	    this.imageUri = imageUri;
	}
	
	/*public void addOverlay(OverlayItem overlay, GeoPoint geoPoint, String venue, String friend) {
	    mOverlays.add(overlay);
	    mOverlaysVenues.add(venue);
	    mOverlaysFriends.add(friend);
	    this.geoPoint = geoPoint;
	    this.drawable = drawable;
	    populate();
	}
	
	@Override
	protected OverlayItem createItem(int i) {
	  return mOverlays.get(i);
	}

	@Override
	public int size() {
		return mOverlays.size();
	}*/
	
	
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
	    super.draw(canvas, mapView, shadow);
	
	    // Convert geo coordinates to screen pixels
		Point screenPoint = new Point();
		mapView.getProjection().toPixels(geoPoint, screenPoint);
		
		// Read the image
		BitmapDrawable d = (BitmapDrawable)Util.ImageOperations(imageUri);
		
		//Bitmap markerImage = BitmapFactory.decodeResource(context.getResources(), d.);
		Bitmap markerImage = d.getBitmap();
		
		// Draw it, centered around the given coordinates
	    canvas.drawBitmap(markerImage,
	        screenPoint.x - markerImage.getWidth() / 2,
	        screenPoint.y - markerImage.getHeight() / 2, null);
	    return true;
	}
	
	protected boolean onTap(int index) {
	  /*OverlayItem item = mOverlays.get(index);
	  Intent intentPoi = new Intent(mContext, POIData.class);
  	  Bundle extras = new Bundle();
  	  extras.putInt("id", mOverlaysId.get(index));
      intentPoi.putExtra("si.kubit.travelguide.ProximityAlert", extras);
	  mContext.startActivity(intentPoi); */
	  return true;
	}
}
