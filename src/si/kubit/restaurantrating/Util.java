package si.kubit.restaurantrating;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;

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


}
