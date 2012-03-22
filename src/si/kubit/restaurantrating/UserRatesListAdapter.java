package si.kubit.restaurantrating;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserRatesListAdapter extends BaseAdapter {

	  private Activity activity;
	  private JSONArray jdata;
	  private static LayoutInflater inflater=null;
	  private Context context;
	 
	  public UserRatesListAdapter(Activity a, String userRates, Context context) {
	      this.activity = a;  
	      this.context = context;
	      try {
	    	  jdata = (JSONArray)new JSONTokener(userRates).nextValue();
	      } catch (Exception e) {}

	      UserRatesListAdapter.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);      
	  }
		
	  public int getCount() {
	  	return jdata.length();    	
	  }
	 
	  public Object getItem(int position) {
	      return position;
	  }
	 
	  public long getItemId(int position) {
	      return position;
	  }
	 
	  public static class ViewHolder{
	      public TextView textRate;
	      public TextView textUser;
	      public TextView textRestaurant;
	      public TextView textHoursAgo;
	  }
	 
	  public View getView(int position, View convertView, ViewGroup parent) {
	      View vi=convertView;
	      ViewHolder holder;
	      if(convertView==null){
	    	  vi = inflater.inflate(R.layout.user_rates_list, null);
	 
	          holder=new ViewHolder();
	          holder.textRate=(TextView)vi.findViewById(R.id.text_rate);
	          holder.textUser=(TextView)vi.findViewById(R.id.text_user);
	          holder.textRestaurant = (TextView)vi.findViewById(R.id.text_restaurant);
	          holder.textHoursAgo = (TextView)vi.findViewById(R.id.text_hours_ago);
	          
	          vi.setTag(holder);
	      }
	      else {
	          holder=(ViewHolder)vi.getTag();
	      }

	      try {
	    	  JSONObject jobject = (JSONObject) jdata.getJSONObject(position);
		      holder.textRate.setText(jobject.getString("avgRate"));
			  holder.textUser.setText((jobject.getString("userName")+" "+jobject.getString("userSurname")).toUpperCase());
			  holder.textRestaurant.setText(jobject.getString("restaurantName").toUpperCase());
			  //holder.textHoursAgo.setText(jobject.getString("rateHoursAgo")+" "+context.getString(R.string.hours_ago));
			  holder.textHoursAgo.setText(Util.formatTime(jobject.getString("rateDateTime"), jobject.getString("rateHoursAgo"), context));
			  
			  LinearLayout lh = (LinearLayout) vi.findViewById(R.id.user_rate_layout);
        	  if (position%2==0) {
	        	  lh.setBackgroundColor(context.getResources().getColor(R.color.secondListColor));
		          holder.textRate.setTextColor(context.getResources().getColor(R.color.firstListColor));
	        	  holder.textUser.setTextColor(context.getResources().getColor(R.color.firstListColor));
	        	  holder.textHoursAgo.setTextColor(context.getResources().getColor(R.color.firstListColor));
	          }	 else {
	        	  lh.setBackgroundColor(context.getResources().getColor(R.color.firstListColor));
		          holder.textRate.setTextColor(context.getResources().getColor(R.color.secondListColor));
	        	  holder.textUser.setTextColor(context.getResources().getColor(R.color.secondListColor));
	        	  holder.textHoursAgo.setTextColor(context.getResources().getColor(R.color.secondListColor));  	  
	          }
	      } catch (Exception e) {e.printStackTrace();}
	      
	      return vi;
	  }
}
