package si.kubit.restaurantrating;

import java.net.SocketException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import si.kubit.restaurantrating.objects.User;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class Util {

	//dolocim format datuma za izpis
	static public String formatTime(String time, String hoursAgo, Context context) throws ParseException {
		Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new Date());
        int yearNow = calendar1.get(Calendar.YEAR);
        int yearTime = Integer.parseInt(time.substring(0,4));
        
        if (yearNow != yearTime) {
        	DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateFormat df = DateFormat.getDateInstance();
        	return df.format(df1.parse(time));
        }

        int ha = Integer.parseInt(hoursAgo);
		if (ha < 24) return hoursAgo+" "+context.getString(R.string.hours_ago);
		if (ha < 168) return Math.round(ha/24)+" "+context.getString(R.string.days_ago);
		if (ha < 672) return Math.round(ha/168)+" "+context.getString(R.string.weeks_ago);
		return Math.round(ha/672)+" "+context.getString(R.string.months_ago);
	}


	static public void addPreferencies(String key, String value, Context context) {
    	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
    	SharedPreferences.Editor editor = settings.edit();
    	editor.remove(key);
    	editor.putString(key, value);
    	editor.commit();
    }

	static public String cutText(String text, int end) {
    	if (end < text.length())
    		return text.substring(0, end) + "...";
    	else
    		return text;
    }

	static public User getUserFromPreferencies(Context context) {
		String userText = PreferenceManager.getDefaultSharedPreferences(context).getString("user", null);				
		User user = new User();
		user.json2user(userText);
		return user;
    }

    static public void SetUserOAuth(Context context, String oauth)
    {
    	User user = Util.getUserFromPreferencies(context);	
		user.setOauthToken(oauth);
        addPreferencies("user", user.user2json(), context);
		
        Comm c = new Comm(context.getString(R.string.server_url), null, null);
        try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("username", user.getUsername()));
	        nameValuePairs.add(new BasicNameValuePair("oauth", oauth));
	        
	        String result = c.post("set_oauth",nameValuePairs);
        } catch (SocketException e) {
        	Toast toast = Toast.makeText(context, context.getString(R.string.conn_error), Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        	toast.show();
   		} catch (Exception ne) {
   			ne.printStackTrace();
        	Toast toast = Toast.makeText(context, context.getString(R.string.json_error), Toast.LENGTH_LONG);
        	toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        	toast.show();
   		}
    }
}
