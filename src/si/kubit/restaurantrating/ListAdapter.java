package si.kubit.restaurantrating;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {

	  private Activity activity;
	  private JSONArray jdata;
	  private static LayoutInflater inflater=null;
	  private Context context;
	 
	  public ListAdapter(Activity a, String userRates, Context context) {
	      this.activity = a;  
	      this.context = context;
	      try {
	    	  jdata = (JSONArray)new JSONTokener(userRates).nextValue();
	      } catch (Exception e) {}

	      ListAdapter.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);      
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
	 
	          vi = inflater.inflate(R.layout.two_line_list, null);
	 
	          holder=new ViewHolder();
	          holder.textRate=(TextView)vi.findViewById(R.id.text_rate);
	          holder.textUser=(TextView)vi.findViewById(R.id.text_user);
	          holder.textRestaurant = (TextView)vi.findViewById(R.id.text_restaurant);
	          holder.textHoursAgo = (TextView)vi.findViewById(R.id.text_hours_ago);
	 
	          vi.setTag(holder);
	      }
	      else {
		      try {
		    	  JSONObject jobject = (JSONObject) jdata.getJSONObject(position);
		          holder=(ViewHolder)vi.getTag();
		      	  holder.textRate.setText(jobject.getString("avgRate"));
		      	  holder.textUser.setText(jobject.getString("userName")+" "+jobject.getString("userSurname"));
		      	  holder.textRestaurant.setText(jobject.getString("restaurantName"));
		          holder.textHoursAgo.setText(jobject.getString("rateHoursAgo")+" "+context.getString(R.string.hours_ago));
		      } catch (Exception e) {}
	      }
	      
	      return vi;
	  }
}
