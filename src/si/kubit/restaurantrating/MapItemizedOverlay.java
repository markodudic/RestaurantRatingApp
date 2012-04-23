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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class MapItemizedOverlay extends ItemizedOverlay {

	protected Context context;
	protected LinearLayout layout;
	protected TextView venueName;
	protected TextView userName;
	protected ImageView showLocation;
	protected float newWidth;
	protected float newHeight;
	protected int extraWidth;
	protected int extraHeight;
	protected int correctionX;
	protected int correctionY;
	
	protected final int SCALE_SIZE = 10;

	protected ArrayList<OverlayItem> overlayItems = new ArrayList<OverlayItem>();
	protected ArrayList<Boolean> overlayIsRestaurants = new ArrayList<Boolean>();
	
	public MapItemizedOverlay(Drawable defaultMarker, 
								Context context, 
								int screenWidth, 
								int screenHeight, 
								LinearLayout layout, 
								TextView venueName, 
								TextView userName, 
								ImageView showLocation,
								int extraWidth,
								int extraHeight,
								int correctionX,
								int correctionY) {
		super(boundCenterBottom(defaultMarker));
		this.context = context;
	    this.layout = layout;
	    this.venueName = venueName;
	    this.userName = userName;
	    this.showLocation = showLocation;
	    this.extraWidth = extraWidth;
	    this.extraHeight = extraHeight;
	    this.correctionX = correctionX;
	    this.correctionY = correctionY;
		layout.setVisibility(View.INVISIBLE);
		
		newWidth = screenWidth / SCALE_SIZE;
		newHeight = screenWidth / SCALE_SIZE;		
	}

	public void addOverlay(OverlayItem overlay, boolean isRestaurant) {
		overlayItems.add(overlay);
		overlayIsRestaurants.add(isRestaurant);
	    populate();
	}
	
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
	    super.draw(canvas, mapView, false);
	    
	    for (int i=0; i<size(); i++) {
	    	OverlayItem overlayItem = ((OverlayItem)overlayItems.get(i));
	    	
	    	// Convert geo coordinates to screen pixels
			Point screenPoint = new Point();
			mapView.getProjection().toPixels(overlayItem.getPoint(), screenPoint);
			
			Paint paint = new Paint();
			paint.setFilterBitmap(true);

			//zrisem back
			Drawable markerBack = context.getResources().getDrawable(R.drawable.image_bck);	
			Bitmap bitmapBack = ((BitmapDrawable)markerBack).getBitmap();
			
			float ratioX = (newWidth + extraWidth) / ((float) bitmapBack.getWidth());
			float ratioY = (newHeight + extraHeight) / ((float) bitmapBack.getHeight());
				
			Matrix matrix = new Matrix();
			matrix.setScale(ratioX, ratioY);
			matrix.postTranslate(screenPoint.x - (newWidth + extraWidth)/2 - correctionX, screenPoint.y - newHeight - correctionY - (extraWidth/2));

			canvas.drawBitmap(bitmapBack, matrix, paint);

			//zrisem front
			Drawable marker = overlayItem.getMarker(0);
	    	Bitmap bitmap = ((BitmapDrawable)marker).getBitmap();
	    	
			ratioX = (newWidth) / ((float) bitmap.getWidth());
			ratioY = (newHeight) / ((float) bitmap.getHeight());
			
			matrix.setScale(ratioX, ratioY);
			matrix.postTranslate(screenPoint.x - newWidth/2, screenPoint.y - newHeight - correctionY);
			canvas.drawBitmap(bitmap, matrix, paint);
			
			
	    }
		return true;
	}
	
	@Override
	protected boolean onTap(int index) {
		if (overlayIsRestaurants.get(index)) {
			showLocation.setVisibility(View.VISIBLE);
		} else {
			showLocation.setVisibility(View.INVISIBLE);			
		}
	    layout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_top));
	    layout.setVisibility(View.VISIBLE);
	    venueName.setText(((OverlayItem)overlayItems.get(index)).getTitle());
	    userName.setText(((OverlayItem)overlayItems.get(index)).getSnippet());
	  
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
	
    public void removeOverlay(OverlayItem overlay) {
    	overlayItems.remove(overlay);
        populate();
    }


    public void clear() {
    	overlayItems.clear();
        populate();
    }	
}
