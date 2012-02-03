package si.kubit.restaurantrating;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
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
	private int rateItemSelected = 0;
	private Drawable rateItem;
	private Drawable rateItemPress;
	
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
		rateItem = array.getDrawable(R.styleable.restaurantrating_rate_item);
		rateItemPress = array.getDrawable(R.styleable.restaurantrating_rate_item_press);

		TextView t = (TextView)findViewById(R.id.text_rate_item);
		t.setText(text);
		
		rateItem1 = (TextView) findViewById(R.id.rate_item_1); 
		rateItem1.setOnClickListener(this);
		rateItem1.setOnTouchListener(this);
		rateItem1.setBackgroundDrawable(rateItem);
		rateitems[0] = rateItem1;
		rateItem2 = (TextView) findViewById(R.id.rate_item_2); 
		rateItem2.setOnClickListener(this);
		rateItem2.setOnTouchListener(this);
		rateItem2.setBackgroundDrawable(rateItem);
		rateitems[1] = rateItem2;
		rateItem3 = (TextView) findViewById(R.id.rate_item_3); 
		rateItem3.setOnClickListener(this);
		rateItem3.setOnTouchListener(this);
		rateItem3.setBackgroundDrawable(rateItem);
		rateitems[2] = rateItem3;
		rateItem4 = (TextView) findViewById(R.id.rate_item_4); 
		rateItem4.setOnClickListener(this);
		rateItem4.setOnTouchListener(this);
		rateItem4.setBackgroundDrawable(rateItem);
		rateitems[3] = rateItem4;
		rateItem5 = (TextView) findViewById(R.id.rate_item_5); 
		rateItem5.setOnClickListener(this);
		rateItem5.setOnTouchListener(this);
		rateItem5.setBackgroundDrawable(rateItem);
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
    	rateItemSelected = sel;    	
    	rate_item_value.setText(sel+"");
		
		for (int i=0; i<rateitems.length; i++) {
			TextView rateItemView = (TextView) rateitems[i];
    		if (i < sel)
    			rateItemView.setBackgroundDrawable(rateItemPress);
    		else
    			rateItemView.setBackgroundDrawable(rateItem);
    	}
		
		
    }


    
}
