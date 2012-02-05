package si.kubit.restaurantrating;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class RestaurantPhotosActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	private JSONArray jPhotos;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_photos);
        
        GetRestaurantPhotosList();

        GridView grid = (GridView) findViewById(R.id.grid_photos);
        grid.setAdapter(new restaurantPhotoAdapter());

        grid.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
    	        try { 
			        //pripravim podatke
    	        	Intent intentRestaurantPhoto = new Intent(RestaurantPhotosActivity.this, RestaurantPhotoActivity.class);
				  	Bundle extras = new Bundle();
				  	extras.putString("photos", jPhotos.toString());
				  	extras.putInt("position", arg2);
				  	intentRestaurantPhoto.putExtra("si.kubit.restaurantrating.RestaurantPhotoActivity", extras);
				  	RestaurantPhotosActivity.this.startActivity(intentRestaurantPhoto);
    	        } catch (Exception e) {e.printStackTrace();}
            }
        });
        
		View locationButtonSubmit = findViewById(R.id.button_location); 
		locationButtonSubmit.setOnClickListener(this);
		View photoButtonSubmit = findViewById(R.id.button_photo); 
		photoButtonSubmit.setOnClickListener(this);
    }

	@Override
	protected void onResume() { 
		super.onResume();
	}
	
    @Override
    protected void onPause() {
    	super.onPause();
    } 

    public void onClick(View v) {
    	switch (v.getId()) { 
			case R.id.button_location:
				break;
			case R.id.button_photo:
				break;
    	}
	}
    
    
    private void GetRestaurantPhotosList()
    {
    	String photos = "";
        Comm c = new Comm(getString(R.string.server_url), null, null);
        try { 
			Bundle extras = getIntent().getBundleExtra("si.kubit.restaurantrating.RestaurantPhotosActivity");
			String restaurantId = extras.getString("restaurant_id");

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("venue_id", restaurantId));
	        
	        photos = c.post("photos",nameValuePairs);

	        jPhotos = (JSONArray)new JSONTokener(photos).nextValue();
        	
	    	TextView title = (TextView)findViewById(R.id.text_photos_title);
			title.setText(jPhotos.length() + " " + getString(R.string.photos_title));
    		
        } catch (SocketException e) {
			showMessageBox(Constants.MESSAGE_BOX_CLOSE_TIME_LONG+"", "false", getString(R.string.conn_error), getString(R.string.conn_title));
   		} catch (Exception ne) {
   			ne.printStackTrace();
			showMessageBox(Constants.MESSAGE_BOX_CLOSE_TIME+"", "false", getString(R.string.json_error), getString(R.string.json_title));
   		}
    }          

    
	private void showMessageBox(String closeTime, String redirect, String msg, String title) {
		Intent messageBox = new Intent(this, MessageBox.class);
    	messageBox.putExtra("redirect", redirect);
    	messageBox.putExtra("closeTime", closeTime);
		messageBox.putExtra("msg", msg);
		messageBox.putExtra("title", title);
		startActivityForResult(messageBox, 1);
	}
	
	public class restaurantPhotoAdapter extends BaseAdapter {

	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView i = new ImageView(getApplicationContext());

	        try { 
		        JSONArray photos = (JSONArray)((JSONObject)((JSONObject)jPhotos.get(position)).get("sizes")).get("items");
		        String imageUri = ((JSONObject)photos.get(1)).getString("url");
		        
		        Drawable image = ImageOperations(imageUri);
				i.setImageDrawable(image);
		        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		        final int w = (int) (36 * getResources().getDisplayMetrics().density + 0.5f);
		        i.setLayoutParams(new GridView.LayoutParams(w * 2, w * 2));
	        } catch (Exception e) {}
	        
	        return i;
	    }

		public final int getCount() {
	        return jPhotos.length();
	    }

	    public final long getItemId(int position) {
	        return position;
	    }

		public Object getItem(int position) {
			return position;
		}	

		private Drawable ImageOperations(String urlString) {
			try {
				URL url = new URL(urlString);
				InputStream is = (InputStream) url.getContent();
				Drawable d = Drawable.createFromStream(is, "src");
				return d;
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
	    }
	}


}