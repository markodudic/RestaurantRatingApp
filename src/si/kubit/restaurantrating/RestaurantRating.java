package si.kubit.restaurantrating;
import si.kubit.restaurantrating.conn.Comm;
import si.kubit.restaurantrating.conn.Foursquare;
import android.app.Application;
import android.content.Context;


public class RestaurantRating extends Application {

    Comm comm = null;
    Foursquare foursquare = null;
    private static RestaurantRating instance;

    public RestaurantRating() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
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
