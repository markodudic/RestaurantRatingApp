package si.kubit.restaurantrating;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RestaurantsListAdapter extends BaseAdapter {

	  private Activity activity;
	  private JSONArray jdata;
	  private static LayoutInflater inflater=null;
	  private Context context;
	 
	  public RestaurantsListAdapter(Activity a, String data, Context context) {
	      this.activity = a;  
	      this.context = context;
	      try {
	    	  jdata = (JSONArray)new JSONTokener(data).nextValue();
	      } catch (Exception e) {}

	      RestaurantsListAdapter.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);      
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
	      public TextView textReviews;
	      public TextView textRestaurantName;
	      public TextView textRestaurantCategory;
	      public TextView textRestaurantDistance;
	  }
	 
	  public View getView(int position, View convertView, ViewGroup parent) {
	      View vi=convertView;
	      ViewHolder holder;
	      if(convertView==null){
	    	  vi = inflater.inflate(R.layout.restaurants_list, null);
	 
	          holder=new ViewHolder();
	          holder.textRate=(TextView)vi.findViewById(R.id.text_rate);
	          holder.textReviews=(TextView)vi.findViewById(R.id.text_reviews);
	          holder.textRestaurantName = (TextView)vi.findViewById(R.id.text_restaurant_name);
	          holder.textRestaurantCategory = (TextView)vi.findViewById(R.id.text_restaurant_category);
	          holder.textRestaurantDistance = (TextView)vi.findViewById(R.id.text_restaurant_distance);
	 
	          vi.setTag(holder);
	      }
	      else {
	          holder=(ViewHolder)vi.getTag();
	      }

	      try {
	    	  JSONObject jobject = (JSONObject) jdata.getJSONObject(position);
		      holder.textRate.setText(jobject.getString("rateAvg"));
			  holder.textReviews.setText(jobject.getString("rateCount")+" "+context.getString(R.string.reviews));
			  holder.textRestaurantName.setText(jobject.getString("name"));
			  holder.textRestaurantCategory.setText(jobject.getString("category"));
			  holder.textRestaurantDistance.setText(jobject.getString("distance")+" "+context.getString(R.string.distance));
	      } catch (Exception e) {e.printStackTrace();}
	      
	      return vi;
	  }
}
