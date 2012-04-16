package si.kubit.restaurantrating;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import si.kubit.restaurantrating.conn.Comm;
import si.kubit.restaurantrating.objects.User;
import si.kubit.restaurantrating.util.Util;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RestaurantTipAddActivity extends Activity implements OnClickListener {
	private static final int CAMERA_PIC_REQUEST = 1337;
	public static final int MEDIA_TYPE_IMAGE = 1;
	
	Uri photoData;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_tip_add);
		
		View tipAddButtonSubmit = findViewById(R.id.button_tip_add); 
		tipAddButtonSubmit.setOnClickListener(this);
		View photoAddButtonSubmit = findViewById(R.id.button_photo_add); 
		photoAddButtonSubmit.setOnClickListener(this);
    }

	@Override
	protected void onResume() { 
		super.onResume();
		String restaurantName   = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("restaurant_name", null);
		TextView tipAddTitle = (TextView)findViewById(R.id.text_tips_title);
		tipAddTitle.setText(Util.cutText(restaurantName, 30));
		EditText tipEditText = (EditText) findViewById(R.id.tip_text); 
		tipEditText.setText("");
	}
	
    @Override
    protected void onPause() {
    	super.onPause();
		//finish(); 
    } 
  
    public void onClick(View v) {
    	switch (v.getId()) { 
			case R.id.button_tip_add:
				//dodam tip v bazo
				EditText tipEditText = (EditText) findViewById(R.id.tip_text); 
				Log.d("********TIP", tipEditText.getText().toString());
				//ALI DODAMO SE FOTKE
				if (photoData!=null)
					Log.d("********Photo data", photoData.toString());
		    	
				/*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("venue_id", PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("restaurant_id", null)));
				nameValuePairs.add(new BasicNameValuePair("text", tipEditText.getText().toString()));
    			User user = Util.getUserFromPreferencies(this);	
				/*nameValuePairs.add(new BasicNameValuePair("oauth", user.getOauthToken()));*/
		        
		        try { 
		        	//String tip = ((RestaurantRating)getApplicationContext()).getComm().post("add_tip", nameValuePairs);
					String tip = ((RestaurantRating)getApplicationContext()).getFoursquare().addTip(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("restaurant_id", null), tipEditText.getText().toString(), Util.getUserFromPreferencies(this).getOauthToken());
		        } catch (Exception e) {
		        	e.printStackTrace();
		        	Toast toast = Toast.makeText(this, getString(R.string.json_error), Toast.LENGTH_LONG);
		        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		        	toast.show();
		   		}
		        	
				Intent intentRestaurantTips = new Intent(RestaurantTipAddActivity.this, RestaurantTipsActivity.class);
			  	RestaurantTipAddActivity.this.startActivity(intentRestaurantTips);
				break;
			case R.id.button_photo_add:
			    if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
				    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				    startActivityForResult(intent, CAMERA_PIC_REQUEST);
			    } else {
		        	Toast toast = Toast.makeText(this, getString(R.string.no_camera), Toast.LENGTH_LONG);
		        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		        	toast.show();
			    }
				break;
    	}
	}
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
         // Image captured and saved to fileUri specified in the Intent
            photoData = data.getData();
        } else if (resultCode == RESULT_CANCELED) {
            // User cancelled the image capture
        } else {
            // Image capture failed, advise user
        	Toast toast = Toast.makeText(this, getString(R.string.camera_error), Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        	toast.show();
        }
    }
    
}