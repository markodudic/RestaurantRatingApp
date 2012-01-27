package si.kubit.restaurantrating;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;


public class RestaurantRateItemLayout extends LinearLayout implements OnClickListener {

	Context context;
	Drawable selected;
	Drawable deselected;
	TextView rateItem1;
	TextView rateItem2;
	TextView rateItem3;
	TextView rateItem4;
	TextView rateItem5;
	
	
	public RestaurantRateItemLayout(Context context) {
		super(context);
	}

    public RestaurantRateItemLayout(Context context, AttributeSet attrs) { 
    	super(context, attrs);
    	this.context = context;
    	
		LayoutInflater.from(context).inflate(R.layout.restaurant_rate_item, this, true);
		
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.restaurantrating, 0, 0);
		
		String text = array.getString(R.styleable.restaurantrating_rate_item_text);

		TextView t = (TextView)findViewById(R.id.text_rate_item);
		t.setText(text);
		
		rateItem1 = (TextView) findViewById(R.id.rate_item_1); 
		rateItem1.setOnClickListener(this);
		rateItem2 = (TextView) findViewById(R.id.rate_item_2); 
		rateItem2.setOnClickListener(this);
		rateItem3 = (TextView) findViewById(R.id.rate_item_3); 
		rateItem3.setOnClickListener(this);
		rateItem4 = (TextView) findViewById(R.id.rate_item_4); 
		rateItem4.setOnClickListener(this);
		rateItem5 = (TextView) findViewById(R.id.rate_item_5); 
		rateItem5.setOnClickListener(this);
		
		Resources res = getResources();
		selected = res.getDrawable(R.drawable.rate_item_press);
		deselected = res.getDrawable(R.drawable.rate_item);
		

		
    }

    
    public void onClick(View v) {
    	switch (v.getId()) { 
			case R.id.rate_item_1:
				setSelection(selected, deselected, deselected, deselected, deselected);
				break;
			case R.id.rate_item_2:
				setSelection(selected, selected, deselected, deselected, deselected);
				break;
			case R.id.rate_item_3:
				setSelection(selected, selected, selected, deselected, deselected);
				break;
			case R.id.rate_item_4:
				setSelection(selected, selected, selected, selected, deselected);
				break;
			case R.id.rate_item_5:
				setSelection(selected, selected, selected, selected, selected);
				break;
    	}
	}
    
    public void setSelection(Drawable item1, Drawable item2, Drawable item3, Drawable item4, Drawable item5) {
		rateItem1.setBackgroundDrawable(item1);
		rateItem2.setBackgroundDrawable(item2);
		rateItem3.setBackgroundDrawable(item3);
		rateItem4.setBackgroundDrawable(item4);
		rateItem5.setBackgroundDrawable(item5);    
    }
    
}
