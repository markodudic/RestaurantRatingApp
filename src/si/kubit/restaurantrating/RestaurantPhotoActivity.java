package si.kubit.restaurantrating;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import si.kubit.restaurantrating.util.Util;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public class RestaurantPhotoActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	private JSONArray jPhotos;
	private TextView textUser;
  	private TextView textPhotosTitle;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_photo);
        
		Bundle extras = getIntent().getBundleExtra("si.kubit.restaurantrating.RestaurantPhotoActivity");
		int position = extras.getInt("position");
		String photos = extras.getString("photos");
	  	
		try {
			jPhotos = (JSONArray)new JSONTokener(photos).nextValue();
		
	        Gallery gallery = (Gallery) findViewById(R.id.gallery);
	        gallery.setAdapter(new ImageAdapter(this));
	        gallery.setSelection(position);
	
	        gallery.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView parent, View v, int position, long id) {
	            }
	        });
	
		  	textUser = (TextView)findViewById(R.id.text_user);
		  	textPhotosTitle = (TextView)findViewById(R.id.text_photo_title);
		  	TextView textNumberPhotos = (TextView)this.findViewById(R.id.button_number_photos);
		  	textNumberPhotos.setText(jPhotos.length() + " " + this.getString(R.string.photos_small));
		} catch (Exception e) {}
	
	  	View numberPhotosButtonSubmit = findViewById(R.id.button_number_photos); 
		numberPhotosButtonSubmit.setOnClickListener(this);
    }

	@Override
	protected void onResume() { 
		super.onResume();
	}
	
    @Override
    protected void onPause() {
    	super.onPause();
		//finish(); 
    } 

    public void onClick(View v) {
    	switch (v.getId()) { 
			case R.id.button_number_photos:
				finish();
				break;
    	}
	}
	
	public class ImageAdapter extends BaseAdapter {
	    int mGalleryItemBackground;
	    private Context mContext;

	    public ImageAdapter(Context c) {
	        mContext = c;
	    }

	    public int getCount() {
	        return jPhotos.length();
	    }

	    public Object getItem(int position) {
	        return position;
	    }

	    public long getItemId(int position) {
	        return position;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
		  	ImageView imageView = new ImageView(mContext);

	        try { 
	        	JSONObject jData = (JSONObject)jPhotos.get(position);
	        	JSONArray photos = (JSONArray)((JSONObject)jData.get("sizes")).get("items");
		        String imageUri = ((JSONObject)photos.get(1)).getString("url");
		        JSONObject user = (JSONObject)jData.get("user");

		        
		        Drawable image = ImageOperations(imageUri);
		        imageView.setImageDrawable(image);
		        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		        imageView.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.FILL_PARENT, Gallery.LayoutParams.FILL_PARENT));

		        /*Display display = getWindowManager().getDefaultDisplay();
		        int screenWidth = display.getWidth();
		        Log.d("WIDTH=", screenWidth+":"+imageView.getHeight()+":"+imageView.getWidth());
		        int width = screenWidth;
		        int height = width * 10 / 10; 
		        imageView.setLayoutParams(new Gallery.LayoutParams(width, height));
*/
		        //dolocim cas
		        String date = jData.getString("createdAt");
		        Date dateD = new Date(Long.parseLong(date) * 1000);
		        
		        Calendar calendar1 = Calendar.getInstance();
		        Calendar calendar2 = Calendar.getInstance();
		        calendar1.setTime(dateD);
		        calendar2.setTime(new Date());
		        long milliseconds1 = calendar1.getTimeInMillis();
		        long milliseconds2 = calendar2.getTimeInMillis();
		        long diff = milliseconds2 - milliseconds1;
		        //long diffSeconds = diff / 1000;
		        //long diffMinutes = diff / (60 * 1000);
		        long diffHours = diff / (60 * 60 * 1000);
		        long diffDays = diff / (24 * 60 * 60 * 1000);
		        
		        //izpis
		        //textUser.setText(getString(R.string.added) + " " + diffDays + " " + getString(R.string.days_ago_by) + " " + user.getString("firstName") + (user.has("lastName")?" " + user.getString("lastName"):""));	  	
		        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        textUser.setText(Util.formatTime(df1.format(dateD), diffHours+"", mContext) + " " + user.getString("firstName") + (user.has("lastName")?" " + user.getString("lastName"):""));
				textPhotosTitle.setText((position+1) + " " + mContext.getString(R.string.of) + " " + jPhotos.length());

	        } catch (Exception e) {e.printStackTrace();}

	        return imageView;
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