package si.kubit.restaurantrating;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RestaurantsListAdapter extends ArrayAdapter<Restaurant> {

	  private Context context;
	  private DecimalFormat decimalFormat = new DecimalFormat("0.0");
	  private boolean showNext = true;
	  private ArrayList<Restaurant> aItems;
	  private ArrayList<Restaurant> aItemsAll;
	  
      public RestaurantsListAdapter(Context context, int resourceId, ArrayList<Restaurant> items, boolean showNext) {
          super(context, resourceId, items);
          this.context = context;
          this.showNext = showNext;
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
              vi = inflater.inflate(R.layout.restaurants_list, null);
	      }

    	  Restaurant r = aItems.get(position);
          if (r != null) {
              TextView textRate = (TextView) vi.findViewById(R.id.text_rate);
              TextView textReviews=(TextView)vi.findViewById(R.id.text_reviews);
              TextView textRestaurantName = (TextView)vi.findViewById(R.id.text_restaurant_name);
              TextView textRestaurantCategory = (TextView)vi.findViewById(R.id.text_restaurant_category);
              TextView textRestaurantDistance = (TextView)vi.findViewById(R.id.text_restaurant_distance);
	 
              textRate.setText(decimalFormat.format(r.getRateAvg()));
			  textReviews.setText(r.getRateCount()+" "+context.getString(R.string.reviews));
			  textRestaurantName.setText(r.getName().toUpperCase());
			  textRestaurantCategory.setText(r.getCategory().toUpperCase());
			  if (r.getDistance()>0)
				  textRestaurantDistance.setText(r.getDistance()+" "+context.getString(R.string.distance));

      
			  LinearLayout lh = (LinearLayout) vi.findViewById(R.id.restaurants_layout);
        	  if (position%2==0) {
	        	  lh.setBackgroundResource(R.drawable.app_background);
		          textRate.setTextColor(context.getResources().getColor(R.color.firstListColor));
	        	  textReviews.setTextColor(context.getResources().getColor(R.color.firstListColor));
	        	  textRestaurantName.setTextColor(context.getResources().getColor(R.color.firstListColor));
	        	  textRestaurantDistance.setTextColor(context.getResources().getColor(R.color.firstListColor));
	          }	 else {
	        	  lh.setBackgroundColor(context.getResources().getColor(R.color.firstListColor));
		          textRate.setTextColor(context.getResources().getColor(R.color.secondListColor));
	        	  textReviews.setTextColor(context.getResources().getColor(R.color.secondListColor));
	        	  textRestaurantName.setTextColor(context.getResources().getColor(R.color.secondListColor));  	  
	        	  textRestaurantDistance.setTextColor(context.getResources().getColor(R.color.secondListColor));
	          }
        	  
        	  if (!showNext) {
        		  ImageView iv = (ImageView) vi.findViewById(R.id.restaurants_next);
        		  iv.setVisibility(View.INVISIBLE);
        	  }
          }
          return vi;
	  }

	  
	  public void filter(CharSequence s) {
		  aItems = new ArrayList<Restaurant>();
		  for (int i=0; i<aItemsAll.size(); i++) {
			  String name = aItemsAll.get(i).getName().toUpperCase(); 
			  String srch = (s+"").toUpperCase();
			  if (name.contains(srch)) {
				  aItems.add((Restaurant)aItemsAll.get(i));
			  }		       
		  }
		  notifyDataSetChanged();
	  }

}
