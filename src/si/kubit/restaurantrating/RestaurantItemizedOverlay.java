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

public class RestaurantItemizedOverlay extends ItemizedOverlay {
//public class FriendsItemizedOverlay extends ItemizedOverlay<OverlayItem> {

	private Context context;
	private int screenWidth;
	private int screenHeight;
	
	private final int SCALE_SIZE = 10;

	private ArrayList<OverlayItem> overlayItems = new ArrayList<OverlayItem>();
	
	public RestaurantItemizedOverlay(Drawable defaultMarker, Context context, int screenWidth, int screenHeight) {
		super(boundCenterBottom(defaultMarker));
		this.context = context;
	    this.screenWidth = screenWidth;
	    this.screenHeight = screenHeight;
	}

	public void addOverlay(OverlayItem overlay) {
		overlayItems.add(overlay);
	    populate();
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
