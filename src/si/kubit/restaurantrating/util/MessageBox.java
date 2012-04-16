package si.kubit.restaurantrating.util;


import si.kubit.restaurantrating.R;
import si.kubit.restaurantrating.R.id;
import si.kubit.restaurantrating.R.layout;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;


public class MessageBox extends Activity {
	String redirect;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messagebox);
		
		Bundle extras = getIntent().getExtras();
		// Set Runnable to remove splash screen just in case
		String closeTime = extras.getString("closeTime");
		redirect = extras.getString("redirect");
		context = this;
		if ((closeTime != null) && (Integer.parseInt(closeTime) > 0)) {
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				public void run() {
						finish();		    			
				}
			}, Integer.parseInt(closeTime));
		}
		
	}

    @Override
    protected void onPause() {
    	super.onPause();
    	finish();
    }
  
    @Override
    protected void onResume() {
    	super.onResume();
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
    		TextView body_msg = (TextView)findViewById(R.id.msg_body);
    		body_msg.setText(extras.getString("msg"));
    		setTitle(extras.getString("title"));
        }
    }	
   
}