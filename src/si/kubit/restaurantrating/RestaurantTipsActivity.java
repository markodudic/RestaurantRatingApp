package si.kubit.restaurantrating;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RestaurantTipsActivity extends ListActivity implements OnClickListener {
	private JSONArray jTips;
	private RestaurantTipsListAdapter listAdapter;
	private ArrayList<Tip> tipsList = null;
	private Runnable viewTips;
	private ProgressDialog m_ProgressDialog = null;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_tips);
		
        tipsList = new ArrayList<Tip>();
        this.listAdapter = new RestaurantTipsListAdapter(this, R.layout.restaurant_tips_list, tipsList);
        setListAdapter(this.listAdapter);

        View locationButtonSubmit = findViewById(R.id.button_location); 
		locationButtonSubmit.setOnClickListener(this);
		View tipButtonSubmit = findViewById(R.id.button_tip); 
		tipButtonSubmit.setOnClickListener(this);
    }

	@Override
	protected void onResume() { 
		super.onResume();
        GetRestaurantTipsList();
	}
	
    @Override
    protected void onPause() {
    	super.onPause();
		//finish(); 
    } 

    public void onClick(View v) {
    	switch (v.getId()) { 
			case R.id.button_location:
    			Intent restaurantRate = new Intent(this, RestaurantRateActivity.class); 
    			startActivity(restaurantRate); 
				break;
			case R.id.button_tip:
    			Intent restaurantTipAdd = new Intent(this, RestaurantTipAddActivity.class); 
    			startActivity(restaurantTipAdd); 
				break;
    	}
	}
    
    
    private void GetRestaurantTipsList()
    {
    	String tips = "";
        Comm c = new Comm(getString(R.string.server_url), null, null);
        try { 
        	String restaurantId;
        	Bundle extras = getIntent().getBundleExtra("si.kubit.restaurantrating.RestaurantTipsActivity");
			if (extras != null) {
				restaurantId = extras.getString("restaurant_id");
				Util.addPreferencies("restaurant_id", restaurantId, this);
			} else {
				restaurantId = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("restaurant_id", null);				
			}
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("venue_id", restaurantId));
	        
	        tips = c.post("tips",nameValuePairs);

	        jTips = (JSONArray)new JSONTokener(tips).nextValue();
        	
	        viewTips = new Runnable(){
                public void run() {
                	getTips();
                }
            };
            Thread thread =  new Thread(null, viewTips, "ViewTips");
            thread.start();
            m_ProgressDialog = ProgressDialog.show(RestaurantTipsActivity.this, getString(R.string.please_wait), getString(R.string.retriving_data), true);
	        
	    	
			TextView places = (TextView)findViewById(R.id.text_tips_title);
	  		places.setText(jTips.length() + " " + getString(R.string.tips_title));
    		
        } catch (SocketException e) {
        	Toast toast = Toast.makeText(this, getString(R.string.conn_error), Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        	toast.show();
   		} catch (Exception ne) {
   			ne.printStackTrace();
        	Toast toast = Toast.makeText(this, getString(R.string.json_error), Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        	toast.show();
   		}
    }      
 
    private void getTips(){
        try{
        	tipsList = new ArrayList<Tip>();
        	for(int i=0; i<jTips.length(); i++) {
        		JSONObject jobject = (JSONObject) jTips.getJSONObject(i);
        		JSONObject user = (JSONObject)jobject.getJSONObject("user");
   		      	Tip t = new Tip();
        		t.setTextTip(jobject.getString("text"));
        		t.setCreatedAt(jobject.getString("createdAt"));
        		t.setFirstName(user.getString("firstName"));
        		if (user.has("lastName"))
        			t.setLastName(user.getString("lastName"));
        		t.setPhoto(user.getString("photo"));
        		tipsList.add(t);
        	}
            Thread.sleep(1000);
          } catch (Exception e) { 
            Log.e("BACKGROUND_PROC", e.getMessage());
          }
          runOnUiThread(returnRes);
      }
	  
    private Runnable returnRes = new Runnable() {
    	public void run() {
    		listAdapter.clear();
            if(tipsList != null && tipsList.size() > 0){
            	listAdapter.notifyDataSetChanged();
                for(int i=0;i<tipsList.size();i++)
                	listAdapter.add(tipsList.get(i));
            }
            m_ProgressDialog.dismiss();
            listAdapter.notifyDataSetChanged();
        }
      };   
}