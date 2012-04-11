package si.kubit.restaurantrating.objects;



public class UserRate {

	public UserRate(){
		
	}	
	
	private String avgRate;
	private String rateDateTime;
	private String rateHoursAgo;
	private String userName;
	private String userSurname;
	private String restaurantName;
	private String restaurantId;
	private String category;
	private int rateCount;
	private double foodRate;
	private double ambientRate;
	private double serviceRate;
	private double valueRate;
	private int tipCount;
	private int photoCount;	

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

	public String getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getRateCount() {
		return rateCount;
	}

	public void setRateCount(int rateCount) {
		this.rateCount = rateCount;
	}

	public double getFoodRate() {
		return foodRate;
	}

	public void setFoodRate(double foodRate) {
		this.foodRate = foodRate;
	}

	public double getAmbientRate() {
		return ambientRate;
	}

	public void setAmbientRate(double ambientRate) {
		this.ambientRate = ambientRate;
	}

	public double getServiceRate() {
		return serviceRate;
	}

	public void setServiceRate(double serviceRate) {
		this.serviceRate = serviceRate;
	}

	public double getValueRate() {
		return valueRate;
	}

	public void setValueRate(double valueRate) {
		this.valueRate = valueRate;
	}

	public int getTipCount() {
		return tipCount;
	}

	public void setTipCount(int tipCount) {
		this.tipCount = tipCount;
	}

	public int getPhotoCount() {
		return photoCount;
	}

	public void setPhotoCount(int photoCount) {
		this.photoCount = photoCount;
	}


	
}