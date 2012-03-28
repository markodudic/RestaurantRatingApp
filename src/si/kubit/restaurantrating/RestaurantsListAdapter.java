package si.kubit.restaurantrating;

import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RestaurantsListAdapter extends BaseAdapter {

	  private Activity activity;
	  private JSONArray jdata;
	  private JSONArray jdataAll;
	  boolean extraLine = false;
	  private static LayoutInflater inflater=null;
	  private Context context;
	  private DecimalFormat decimalFormat = new DecimalFormat("0.0");
	 
	  public RestaurantsListAdapter(Activity a, JSONArray jdata, Context context, boolean extraLine) {
	      this.activity = a;  
	      this.context = context;
	      this.jdata = jdata;
	      this.jdataAll = jdata;
	      this.extraLine = extraLine;

	      RestaurantsListAdapter.inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);      
	  }
		
	  public int getCount() {
		if (extraLine)
			return jdata.length()+1; 
		else
			return jdata.length();
	  }
	 
	  public int getCountAll() {
		  	return jdataAll.length();    	
	  }

	  public Object getItem(int position) {
	      try {
	    	  return jdata.getJSONObject(position);
	      } catch (Exception e) {e.printStackTrace();}
	      return null;
	  }

	  public Object getItemAll(int position) {
	      try {
	    	  return jdataAll.getJSONObject(position);
	      } catch (Exception e) {e.printStackTrace();}
	      return null;
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
	    	  if (position == jdata.length()) {
	    		  ImageView iv = (ImageView)vi.findViewById(R.id.restaurants_next);
	    		  iv.setVisibility(View.INVISIBLE);
	    		  holder.textRate.setText("");
			      holder.textReviews.setText("");
			      holder.textRestaurantName.setText("");
			      holder.textRestaurantCategory.setText("");
			      holder.textRestaurantDistance.setText("");

				  LinearLayout lh = (LinearLayout) vi.findViewById(R.id.restaurants_layout);
	        	  lh.setBackgroundColor(context.getResources().getColor(R.color.firstListColor));
		          holder.textRate.setTextColor(context.getResources().getColor(R.color.secondListColor));
	        	  holder.textReviews.setTextColor(context.getResources().getColor(R.color.secondListColor));
	        	  holder.textRestaurantName.setTextColor(context.getResources().getColor(R.color.secondListColor));  	  
	        	  holder.textRestaurantDistance.setTextColor(context.getResources().getColor(R.color.secondListColor));
	    	  } else {
	    		  JSONObject jobject = (JSONObject) jdata.getJSONObject(position);
		    	  holder.textRate.setText(decimalFormat.format(Double.parseDouble(jobject.getString("rateAvg"))));
				  holder.textReviews.setText(jobject.getString("rateCount")+" "+context.getString(R.string.reviews));
				  holder.textRestaurantName.setText(jobject.getString("name").toUpperCase());
				  holder.textRestaurantCategory.setText(jobject.getString("category").toUpperCase());
				  holder.textRestaurantDistance.setText(jobject.getString("distance")+" "+context.getString(R.string.distance));

				  LinearLayout lh = (LinearLayout) vi.findViewById(R.id.restaurants_layout);
	        	  if (position%2==0) {
		        	  //lh.setBackgroundColor(context.getResources().getColor(R.color.secondListColor));
		        	  lh.setBackgroundResource(R.drawable.app_background);
			          holder.textRate.setTextColor(context.getResources().getColor(R.color.firstListColor));
		        	  holder.textReviews.setTextColor(context.getResources().getColor(R.color.firstListColor));
		        	  holder.textRestaurantName.setTextColor(context.getResources().getColor(R.color.firstListColor));
		        	  holder.textRestaurantDistance.setTextColor(context.getResources().getColor(R.color.firstListColor));
		          }	 else {
		        	  lh.setBackgroundColor(context.getResources().getColor(R.color.firstListColor));
			          holder.textRate.setTextColor(context.getResources().getColor(R.color.secondListColor));
		        	  holder.textReviews.setTextColor(context.getResources().getColor(R.color.secondListColor));
		        	  holder.textRestaurantName.setTextColor(context.getResources().getColor(R.color.secondListColor));  	  
		        	  holder.textRestaurantDistance.setTextColor(context.getResources().getColor(R.color.secondListColor));
		          }
	    	  }
			  
			  
	      } catch (Exception e) {e.printStackTrace();}
	      
	      return vi;
	  }

	  
	  public void filter(CharSequence s) {
	      try {
			  jdata = new JSONArray();
			  for (int i=0; i<getCountAll(); i++) {
				  JSONObject jobject=(JSONObject)this.getItemAll(i);
				  String name = jobject.getString("name"); 
				  String srch = (s+"").toUpperCase();
				  Log.d(name.toUpperCase(), srch);
				  if (name.toUpperCase().contains(srch)) {
					  jdata.put(jobject);
				  }		       
			  }
			  notifyDataSetChanged();
	      } catch (Exception e) {e.printStackTrace();}
	  }

}
