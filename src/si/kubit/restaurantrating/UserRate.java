package si.kubit.restaurantrating;


public class UserRate {

	public UserRate(){
		
	}	
	
	private String avgRate;
	private String rateDateTime;
	private String rateHoursAgo;
	private String userName;
	private String userSurname;
	private String restaurantName;

	
	public String getAvgRate() {
		return avgRate;
	}

	public void setAvgRate(String avgRate) {
		this.avgRate = avgRate;
	}

	public String getRateDateTime() {
		return rateDateTime;
	}

	public void setRateDateTime(String rateDateTime) {
		this.rateDateTime = rateDateTime;
	}

	public String getRateHoursAgo() {
		return rateHoursAgo;
	}

	public void setRateHoursAgo(String rateHoursAgo) {
		this.rateHoursAgo = rateHoursAgo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserSurname() {
		return userSurname;
	}

	public void setUserSurname(String userSurname) {
		this.userSurname = userSurname;
	}

	public String getRestaurantName() {
		return restaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}


	
}