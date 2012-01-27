package si.kubit.restaurantrating;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;


public class RestaurantRateItemLayout extends LinearLayout implements OnClickListener {

	Context context;
	TextView rateItem1;
	TextView rateItem2;
	TextView rateItem3;
	TextView rateItem4;
	TextView rateItem5;
	TextView rate_item_value;
	TextView[] rateitems = new TextView[5];
	int rateItemSelected = 0;

	
	
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
		rateitems[0] = rateItem1;
		rateItem2 = (TextView) findViewById(R.id.rate_item_2); 
		rateItem2.setOnClickListener(this);
		rateitems[1] = rateItem2;
		rateItem3 = (TextView) findViewById(R.id.rate_item_3); 
		rateItem3.setOnClickListener(this);
		rateitems[2] = rateItem3;
		rateItem4 = (TextView) findViewById(R.id.rate_item_4); 
		rateItem4.setOnClickListener(this);
		rateitems[3] = rateItem4;
		rateItem5 = (TextView) findViewById(R.id.rate_item_5); 
		rateItem5.setOnClickListener(this);
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
    
    public void setSelection(int sel) {
    	rateItemSelected = sel;    	
    	rate_item_value.setText(sel+"");
		
		for (int i=0; i<rateitems.length; i++) {
			TextView rateItem = (TextView) rateitems[i];
    		if (i < sel)
    			rateItem.setBackgroundDrawable(getResources().getDrawable(R.drawable.rate_item_press));
    		else
    			rateItem.setBackgroundDrawable(getResources().getDrawable(R.drawable.rate_item));
    	}
		
		
    }
    
    
    
}
