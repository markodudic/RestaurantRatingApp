package si.kubit.restaurantrating.components;

import si.kubit.restaurantrating.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;


public class RestaurantRateItemLayout extends LinearLayout implements OnClickListener, OnTouchListener  {

	private Context context;
	private TextView rateItem1;
	private TextView rateItem2;
	private TextView rateItem3;
	private TextView rateItem4;
	private TextView rateItem5;
	private TextView rate_item_value;
	private TextView[] rateitems = new TextView[5];
	private ShapeDrawable rateItemDrawable;
	private ShapeDrawable rateItemPressedDrawable;
	private ShapeDrawable rateItemDefaultDrawable;
	
	private Rect rateItem1Rect = new Rect(); 
	private Rect rateItem2Rect = new Rect(); 
	private Rect rateItem3Rect = new Rect(); 
	private Rect rateItem4Rect = new Rect(); 
	private Rect rateItem5Rect = new Rect(); 
	
	
	public RestaurantRateItemLayout(Context context) {
		super(context);
	}

    public RestaurantRateItemLayout(Context context, AttributeSet attrs) { 
    	super(context, attrs);
    	this.context = context;
    	
    	LayoutInflater.from(context).inflate(R.layout.restaurant_rate_item, this, true);
		
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.restaurantrating, 0, 0);
		String text = array.getString(R.styleable.restaurantrating_rate_item_text);
		int fillColor = array.getInt(R.styleable.restaurantrating_color_fill, Color.RED);
		int strokeColor = array.getInt(R.styleable.restaurantrating_color_stroke, Color.RED);
		int strokeWidth = array.getInt(R.styleable.restaurantrating_stroke_width, 4);
		int width = array.getInt(R.styleable.restaurantrating_width, 60);
		int height = array.getInt(R.styleable.restaurantrating_height, 60);
		int defaultColor = array.getInt(R.styleable.restaurantrating_color_default, Color.BLACK);

		OvalShape os = new OvalShape();
        rateItemDrawable = new CustomShapeDrawable(os, fillColor, strokeColor, strokeWidth);
        rateItemDrawable.setIntrinsicHeight(height);
        rateItemDrawable.setIntrinsicWidth(width);
        rateItemPressedDrawable = new CustomShapeDrawable(os, strokeColor, strokeColor, strokeWidth);
        rateItemPressedDrawable.setIntrinsicHeight(height);
        rateItemPressedDrawable.setIntrinsicWidth(width);
        rateItemDefaultDrawable = new CustomShapeDrawable(os, defaultColor, defaultColor, strokeWidth);
        rateItemDefaultDrawable.setIntrinsicHeight(height);
        rateItemDefaultDrawable.setIntrinsicWidth(width);
       
		TextView t = (TextView)findViewById(R.id.text_rate_item);
		t.setText(text);
		
		rateItem1 = (TextView) findViewById(R.id.rate_item_1); 
		rateItem1.setOnClickListener(this);
		rateItem1.setOnTouchListener(this);
		rateItem1.setBackgroundDrawable(rateItemDrawable);
		rateitems[0] = rateItem1;
		rateItem2 = (TextView) findViewById(R.id.rate_item_2); 
		rateItem2.setOnClickListener(this);
		rateItem2.setOnTouchListener(this);
		rateItem2.setBackgroundDrawable(rateItemDrawable);
		rateitems[1] = rateItem2;
		rateItem3 = (TextView) findViewById(R.id.rate_item_3); 
		rateItem3.setOnClickListener(this);
		rateItem3.setOnTouchListener(this);
		rateItem3.setBackgroundDrawable(rateItemDrawable);
		rateitems[2] = rateItem3;
		rateItem4 = (TextView) findViewById(R.id.rate_item_4); 
		rateItem4.setOnClickListener(this);
		rateItem4.setOnTouchListener(this);
		rateItem4.setBackgroundDrawable(rateItemDrawable);
		rateitems[3] = rateItem4;
		rateItem5 = (TextView) findViewById(R.id.rate_item_5); 
		rateItem5.setOnClickListener(this);
		rateItem5.setOnTouchListener(this);
		rateItem5.setBackgroundDrawable(rateItemDrawable);
		rateitems[4] = rateItem5;
		rate_item_value = (TextView) findViewById(R.id.rate_item_value); 	
		
		
    } 

    
    public void onClick(View v) {
    	switch (v.getId()) { 
			case R.id.rate_item_1:
				setSelection(1);
				break;
			case R.id.rate_item_2:
				setSelection(2);
				break;
			case R.id.rate_item_3:
				setSelection(3);
				break;
			case R.id.rate_item_4:
				setSelection(4);
				break;
			case R.id.rate_item_5:
				setSelection(5);
				break;
    	}
	}
    
    public boolean onTouch(View v, MotionEvent me) {
    	rateItem1.getGlobalVisibleRect(rateItem1Rect);
    	rateItem2.getGlobalVisibleRect(rateItem2Rect);
    	rateItem3.getGlobalVisibleRect(rateItem3Rect);
    	rateItem4.getGlobalVisibleRect(rateItem4Rect);
    	rateItem5.getGlobalVisibleRect(rateItem5Rect);
		
    	if (checkIntersection(rateItem1Rect, me)) {
			setSelection(1);    		
    	} else if (checkIntersection(rateItem2Rect, me)) {
    		setSelection(2);    		
    	} else if (checkIntersection(rateItem3Rect, me)) {
    		setSelection(3);    		
    	} else if (checkIntersection(rateItem4Rect, me)) {
        	setSelection(4);    		
    	} else if (checkIntersection(rateItem5Rect, me)) {
    		setSelection(5);    		
        }
    	
    	return false; 
	}

    private boolean checkIntersection(Rect rect, MotionEvent me) {
    	return (((rect.left) < me.getRawX()) &&
    			((rect.left + rect.width()) > me.getRawX()) &&
    			((rect.top) < me.getRawY()) &&
    			((rect.top + rect.height()) > me.getRawY()));
    }
	
    public void setSelection(int sel) {
    	rate_item_value.setText(sel+"");
		
		for (int i=0; i<rateitems.length; i++) {
			TextView rateItemView = (TextView) rateitems[i];
    		if (i < sel)
    			rateItemView.setBackgroundDrawable(rateItemPressedDrawable);
    		else
    			rateItemView.setBackgroundDrawable(rateItemDrawable);
    	}
		
		
    }

	public ShapeDrawable getRateItemDrawable() {
		return rateItemDrawable;
	}

	public ShapeDrawable getRateItemPressedDrawable() {
		return rateItemPressedDrawable;
	}

	public ShapeDrawable getRateItemDefaultDrawable() {
		return rateItemDefaultDrawable;
	}

    
}
