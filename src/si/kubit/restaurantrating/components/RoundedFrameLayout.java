package si.kubit.restaurantrating.components;

import si.kubit.restaurantrating.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class RoundedFrameLayout extends FrameLayout {

	public RoundedFrameLayout(Context context) {
		super(context);
	}

	public RoundedFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.restaurantrating, 0, 0);
		String radii = array.getString(R.styleable.restaurantrating_radii);
		int colorFill = array.getInt(R.styleable.restaurantrating_color_fill, 0);
		int colorStroke = array.getInt(R.styleable.restaurantrating_color_stroke, 0);
		int strokeWidth = array.getInt(R.styleable.restaurantrating_stroke_width, 0);

        RoundRectShape rs = new RoundRectShape(convertStringArraytoFloatArray(radii.split(",")), null, null);
        ShapeDrawable sd = new CustomShapeDrawable(rs, colorFill, colorStroke, strokeWidth);
        setBackgroundDrawable(sd);
		
	}
	
	public float[] convertStringArraytoFloatArray(String[] num) { 
		if (num != null) { 
			float floatarray[] = new float[num.length]; 
			for (int i = 0; i <num.length; i++) { 
			    floatarray[i] = Float.parseFloat(num[i]);
			} 
			return floatarray; 
		} 
		return null; 
	}


}
