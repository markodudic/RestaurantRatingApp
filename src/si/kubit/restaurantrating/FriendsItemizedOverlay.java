package si.kubit.restaurantrating;


import java.util.ArrayList;

import si.kubit.restaurantrating.util.Util;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class FriendsItemizedOverlay extends ItemizedOverlay {
//public class FriendsItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private Context context;
	private int screenWidth;
	private int screenHeight;
	private LinearLayout layout;
	private TextView venueName;
	private TextView userName;

	private final int SCALE_SIZE = 10;

	private ArrayList<OverlayItem> overlayItems = new ArrayList<OverlayItem>();
	private ArrayList<GeoPoint> overlaysPoints = new ArrayList<GeoPoint>();
	private ArrayList<String> overlayImageUri = new ArrayList<String>();
	private ArrayList<Bitmap> overlayMarkerImages = new ArrayList<Bitmap>();
	private ArrayList<String> overlayVenueNames = new ArrayList<String>();
	private ArrayList<String> overlayUserNames = new ArrayList<String>();
	
	public FriendsItemizedOverlay(Drawable defaultMarker, Context context, int screenWidth, int screenHeight, LinearLayout layout, TextView venueName, TextView userName) {
		super(boundCenterBottom(defaultMarker));
		this.context = context;
	    this.screenWidth = screenWidth;
	    this.screenHeight = screenHeight;
	    this.layout = layout;
	    this.venueName = venueName;
	    this.userName = userName;
		layout.setVisibility(View.INVISIBLE);
	}

	public void addOverlay(OverlayItem overlay, GeoPoint geoPoint, String venue, String userName, String imageUri, Bitmap markerImage) {
		overlayItems.add(overlay);
		overlaysPoints.add(geoPoint);
		overlayImageUri.add(imageUri);
		overlayMarkerImages.add(markerImage);
		overlayVenueNames.add(venue);
		overlayUserNames.add(userName);
	    populate();
	}
	
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
	    super.draw(canvas, mapView, false);
	    
	    for (int i=0; i<size(); i++) {
	    	//Log.d("********************", i+"");
		    // Convert geo coordinates to screen pixels
			Point screenPoint = new Point();
			mapView.getProjection().toPixels(overlaysPoints.get(i), screenPoint);
			
			// Read the image
			//BitmapDrawable d = (BitmapDrawable)Util.ImageOperations(overlayImageUri.get(i));
			//Bitmap markerImage = d.getBitmap();
			overlayItems.get(i).setMarker(new BitmapDrawable(overlayMarkerImages.get(i)));
			
			float newWidth = screenWidth / SCALE_SIZE;
			float newHeight = screenWidth / SCALE_SIZE;
			
			float ratioX = newWidth / (float) overlayMarkerImages.get(i).getWidth();
			float ratioY = newHeight / (float) overlayMarkerImages.get(i).getHeight();
			float middleX = newWidth / 2.0f;
			float middleY = newHeight / 2.0f;
	
			Matrix matrix = new Matrix();
			matrix.setScale(ratioX, ratioY, middleX, middleY);
			matrix.postTranslate(screenPoint.x - newWidth / 2, screenPoint.y - newHeight);
			Paint paint = new Paint();
			paint.setFilterBitmap(true);
			canvas.drawBitmap(overlayMarkerImages.get(i), matrix, paint);
	    }
		return true;
	}
	
	@Override
	protected boolean onTap(int index) {
	  layout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_top));
	  layout.setVisibility(View.VISIBLE);
	  venueName.setText(overlayVenueNames.get(index));
	  userName.setText(overlayUserNames.get(index));
	  
	  return true;
	}

	
	@Override
	protected OverlayItem createItem(int i) {
	  return overlayItems.get(i);
	}

	@Override
	public int size() {
		return overlayItems.size();
	}
}
