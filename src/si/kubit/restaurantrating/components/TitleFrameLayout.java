package si.kubit.restaurantrating.components;

import si.kubit.restaurantrating.R;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class TitleFrameLayout extends FrameLayout {

	public TitleFrameLayout(Context context) {
		super(context);
	}
	
    public TitleFrameLayout(Context context, AttributeSet attrs) { 
    	super(context, attrs);

        int colors[] = { context.getResources().getColor(R.color.titleColor), context.getResources().getColor(R.color.titleColorEnd) };
        GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, colors);
		g.setCornerRadii(new float[] { 15, 15, 15, 15, 0, 0, 0, 0 });
		
		setBackgroundDrawable(g);
    }	

}
