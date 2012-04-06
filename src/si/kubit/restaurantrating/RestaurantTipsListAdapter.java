package si.kubit.restaurantrating;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RestaurantTipsListAdapter extends ArrayAdapter<Tip> {

	  private JSONArray jdata;
	  private static LayoutInflater inflater=null;
	  private Context context;
	  private ArrayList<Tip> aItems;
	  private ArrayList<Tip> aItemsAll;
	 
      public RestaurantTipsListAdapter(Context context, int resourceId, ArrayList<Tip> items) {
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
              vi = inflater.inflate(R.layout.restaurant_tips_list, null);
	      }

	      Tip tip = aItems.get(position);
          if (tip != null) {
        	  try{
		    	  TextView textTip=(TextView)vi.findViewById(R.id.text_tip);
		    	  TextView textUserPrefix=(TextView)vi.findViewById(R.id.text_user_prefix);
		    	  TextView textUser=(TextView)vi.findViewById(R.id.text_user);
		    	  ImageView imageUser = (ImageView)vi.findViewById(R.id.icon_user);
		          
		          textTip.setText(tip.getTextTip().toUpperCase());
	
			      //dolocim cas
			      String date = tip.getCreatedAt();
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
			      DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		          textUserPrefix.setText(Util.formatTime(df1.format(dateD), diffHours+"", context) + " ");
					
			      //izpis
			      textUser.setText(tip.getFirstName().toUpperCase() + (tip.getLastName()!=null?" " + tip.getLastName().toUpperCase():""));
				  Drawable image = ImageOperations(tip.getPhoto());
				  imageUser.setImageDrawable(image);
		      } catch (Exception e) {e.printStackTrace();}
          }
	      
	      return vi;
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
