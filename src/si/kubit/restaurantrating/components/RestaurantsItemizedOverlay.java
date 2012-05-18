package si.kubit.restaurantrating.components;


import java.util.ArrayList;

import si.kubit.restaurantrating.R;
import si.kubit.restaurantrating.R.anim;
import si.kubit.restaurantrating.util.Util;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.OverlayItem;

public class RestaurantsItemizedOverlay extends MapItemizedOverlay {

	private ImageView icon;
	private TextView category;
	protected ArrayList<String> overlayCategories = new ArrayList<String>();
	protected ArrayList<String> overlayIconUris = new ArrayList<String>();
	
	public RestaurantsItemizedOverlay(Drawable defaultMarker, 
								Context context, 
								int screenWidth, 
								int screenHeight, 
								LinearLayout layout, 
								TextView venueName, 
								TextView address, 
								ImageView showLocation,
								int extraWidth,
								int extraHeight,
								int correctionX,
								int correctionY,
								TextView category, 
								ImageView icon) {
		super(defaultMarker, context, screenWidth, screenHeight, layout, venueName, address, 
				showLocation, extraWidth, extraHeight, correctionX, correctionY);	
		this.category = category;
		this.icon = icon;
	}
	
	public void addOverlay(OverlayItem overlay, boolean isRestaurant, String category, String iconUri) {
		overlayItems.add(overlay);
		overlayIsRestaurants.add(isRestaurant);
		overlayCategories.add(category);
		overlayIconUris.add(iconUri);
	    populate();
	}
	
	@Override
	protected boolean onTap(int index) {
		//Drawable marker = ((OverlayItem)overlayItems.get(index)).getMarker(0);
		BitmapDrawable marker = (BitmapDrawable)Util.ImageOperations((String)overlayIconUris.get(index));
		icon.setBackgroundDrawable(marker);
		
    	layout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_top));
	    layout.setVisibility(View.VISIBLE);
	    venueName.setText(((OverlayItem)overlayItems.get(index)).getTitle());
	    userName.setText(((OverlayItem)overlayItems.get(index)).getSnippet());
	    category.setText((String)overlayCategories.get(index));
	  
	    selectedItem = index;	    

	    return true;
	}

	
}
