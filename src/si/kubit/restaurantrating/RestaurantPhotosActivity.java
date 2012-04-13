package si.kubit.restaurantrating;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RestaurantPhotosActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	private JSONArray jPhotos;
	private static final int CAMERA_PIC_REQUEST = 1337;
	public static final int MEDIA_TYPE_IMAGE = 1;

	private GridView grid;
	private File fileUri;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_photos);
        
        grid = (GridView) findViewById(R.id.grid_photos);
        
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
        GetRestaurantPhotosList();
        grid.setAdapter(new restaurantPhotoAdapter());
	}
	
    @Override
    protected void onPause() {
    	super.onPause();
    } 

    public void onClick(View v) {
    	switch (v.getId()) { 
			case R.id.button_location:
    			Intent restaurantRate = new Intent(this, RestaurantRateActivity.class); 
    			startActivity(restaurantRate);
				break;
			case R.id.button_photo:
			    if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
				    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				    
				    fileUri = getOutputMediaFile(MEDIA_TYPE_IMAGE); // create a file to save the image
				    Log.d("URI", Uri.fromFile(fileUri).toString());
				    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileUri)); // set the image file name

				    startActivityForResult(intent, CAMERA_PIC_REQUEST);
			    } else {
		        	Toast toast = Toast.makeText(this, getString(R.string.no_camera), Toast.LENGTH_LONG);
		        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
		        	toast.show();
			    }
				break;
    	}
	} 

    
   private static File getOutputMediaFile(int type){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().toString());
        
        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "IMG_"+ timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("PHOTO RESULT=",requestCode+":"+resultCode);
    	if (resultCode == RESULT_OK) {
         // Image captured and saved to fileUri specified in the Intent
            Toast.makeText(this, "Image saved to:\n" + fileUri.toURI(), Toast.LENGTH_LONG).show();
            
            try {
            	uploadImage(null, null, fileUri);
            } catch (Exception e) {
            	e.printStackTrace();
            }
            
            
        } else if (resultCode == RESULT_CANCELED) {
            // User cancelled the image capture
        } else {
            // Image capture failed, advise user
        	Toast toast = Toast.makeText(this, getString(R.string.camera_error), Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        	toast.show(); 
        }
    }
   
    
	public String uploadImage(String url, List<NameValuePair> nameValuePairs, File file) throws Exception {
		url = "https://api.foursquare.com/v2/photos/add";
		BufferedReader br = new BufferedReader(new FileReader(file), 8192);
	    String s = "";
	    String line;
	    
	    /*while ((line = br.readLine()) != null) {
	    	s = s + line;
		    Log.d("READ=", s);
	    }
	    Log.d("READ all=", s);
	    */
	    int imageLen = (int)file.length();
	    byte [] imgData = new byte[imageLen];
	    FileInputStream fis = new FileInputStream(file);
	    fis.read(imgData);
	    Log.d("READ all=", imgData.length+":"+imgData.toString());
	    
		String response = null;
		
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(url);
	    
	    //httppost.setTimeout(20000);
	    httppost.setHeader("enctype", "multipart/form-data");
	    //httppost.setHeader("Content-Type", "image/jpeg");
	    
	    
		nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("venueId", "4b2a765ff964a520a4a924e3"));
        nameValuePairs.add(new BasicNameValuePair("oauth_token", "PFXLQWFU3RZBWGVOZMZXNI41O44VJWZRK1AN2M13NKHDWFJR"));
        nameValuePairs.add(new BasicNameValuePair("photo", imgData));

        if (nameValuePairs != null)
    		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        ResponseHandler<String> responseHandler=new BasicResponseHandler();
        response = httpclient.execute(httppost, responseHandler);
        Log.d("comm", "http-response "+response.toString());

        return response;
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
        	Toast toast = Toast.makeText(this, getString(R.string.conn_error), Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        	toast.show();
			//showMessageBox(Constants.MESSAGE_BOX_CLOSE_TIME_LONG+"", "false", getString(R.string.conn_error), getString(R.string.conn_title));
   		} catch (Exception ne) {
        	Toast toast = Toast.makeText(this, getString(R.string.json_error), Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        	toast.show();
   			//showMessageBox(Constants.MESSAGE_BOX_CLOSE_TIME+"", "false", getString(R.string.json_error), getString(R.string.json_title));
   		}
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