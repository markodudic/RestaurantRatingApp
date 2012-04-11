package si.kubit.restaurantrating;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import si.kubit.restaurantrating.objects.UserRate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class UserRatesListAdapter extends ArrayAdapter<UserRate> {

	 private JSONArray jdata;
	  private static LayoutInflater inflater=null;
	  private Context context;
	  private ArrayList<UserRate> aItems;
	  private ArrayList<UserRate> aItemsAll;
	 
      public UserRatesListAdapter(Context context, int resourceId, ArrayList<UserRate> items) {
          super(context, resourceId, items);
          this.context = context;
          this.aItems = items;
          this.aItemsAll = items;
      }
		
	  public int getCount() {
	  	return aItems.size();    	
	  }
	 
	  public View getView(int position, View convertView, ViewGroup parent) {
	      View vi=convertView;
	      if(convertView==null){
	    	  LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
              vi = inflater.inflate(R.layout.user_rates_list, null);
	      }

    	  UserRate ur = aItems.get(position);
          if (ur != null) {
        	  try{
	              TextView textRate = (TextView) vi.findViewById(R.id.text_rate);
	              TextView textUser = (TextView) vi.findViewById(R.id.text_user);
	              TextView textRestaurant = (TextView) vi.findViewById(R.id.text_restaurant);
	              TextView textHoursAgo = (TextView) vi.findViewById(R.id.text_hours_ago);
	 	          
	              textRate.setText(ur.getAvgRate());
				  textUser.setText((ur.getUserName()+" "+ur.getUserSurname()).toUpperCase());
				  textRestaurant.setText(ur.getRestaurantName().toUpperCase());
				  textHoursAgo.setText(Util.formatTime(ur.getRateDateTime(), ur.getRateHoursAgo(), context));
	
				  LinearLayout lh = (LinearLayout) vi.findViewById(R.id.user_rate_layout);
	        	  if (position%2==0) {
		        	  lh.setBackgroundResource(R.drawable.app_background);
			          textRate.setTextColor(context.getResources().getColor(R.color.firstListColor));
		        	  textUser.setTextColor(context.getResources().getColor(R.color.firstListColor));
		        	  textHoursAgo.setTextColor(context.getResources().getColor(R.color.firstListColor));
		          }	 else {
		        	  lh.setBackgroundColor(context.getResources().getColor(R.color.firstListColor));
			          textRate.setTextColor(context.getResources().getColor(R.color.secondListColor));
		        	  textUser.setTextColor(context.getResources().getColor(R.color.secondListColor));
		        	  textHoursAgo.setTextColor(context.getResources().getColor(R.color.secondListColor));  	  
		          }
		      } catch (Exception e) {e.printStackTrace();}
	  	  }
	      
	      return vi;
	  }
}
