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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class RestaurantPhotoActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	private JSONArray jPhotos;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_photo);
        
		Bundle extras = getIntent().getBundleExtra("si.kubit.restaurantrating.RestaurantPhotoActivity");
		String photo = extras.getString("photo_url");
		String user = extras.getString("user");
		String count = extras.getString("count");
		String selected = extras.getString("selected"); 
        
        ImageView imagePhoto = (ImageView)this.findViewById(R.id.image_photo);
        Drawable image = ImageOperations(photo);
	  	imagePhoto.setImageDrawable(image);
	  	
	  	TextView textUser = (TextView)this.findViewById(R.id.text_user);
	  	textUser.setText(user);	  	
	  	TextView textNumberPhotos = (TextView)this.findViewById(R.id.button_number_photos);
	  	textNumberPhotos.setText(count + " " + this.getString(R.string.photos_small));
	  	TextView textPhotosTitle = (TextView)this.findViewById(R.id.text_photo_title);
	  	textPhotosTitle.setText(selected + " " + this.getString(R.string.of) + " " + count);

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