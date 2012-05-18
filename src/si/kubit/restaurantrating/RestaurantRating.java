package si.kubit.restaurantrating;
import si.kubit.restaurantrating.conn.Comm;
import si.kubit.restaurantrating.conn.Foursquare;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;


public class RestaurantRating extends Application {

    Comm comm = null;
    Foursquare foursquare = null;
    private static RestaurantRating context;

    public RestaurantRating() {
    	context = this;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
    
    public static Context getContext() {
        return context;
    }
    
	public Comm getComm() {
		return comm;
	}

	public void setComm(Comm comm) {
		this.comm = comm;
	}

	public Foursquare getFoursquare() {
		return foursquare;
	}

	public void setFoursquare(Foursquare foursquare) {
		this.foursquare = foursquare;
	}


	
	
}
