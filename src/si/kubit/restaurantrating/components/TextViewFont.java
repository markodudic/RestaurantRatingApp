package si.kubit.restaurantrating.components;

import si.kubit.restaurantrating.R;
import si.kubit.restaurantrating.R.string;
import si.kubit.restaurantrating.R.styleable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class TextViewFont extends TextView {

	public TextViewFont(Context context) { 
    	super(context);
    }
	
    public TextViewFont(Context context, AttributeSet attrs) { 
    	super(context, attrs);
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.restaurantrating, 0, 0);
		String fontName = array.getString(R.styleable.restaurantrating_font_name);
		if (fontName == null) fontName = context.getString(R.string.font_big_text);
		Typeface font = Typeface.createFromAsset(context.getAssets(), fontName);  
    	this.setTypeface(font); 
    }
}
