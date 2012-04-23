package si.kubit.restaurantrating.objects;


public class Restaurant {

	public Restaurant(){
		
	}	
	
	private String id;
	private String name;
	private String category;
	private int rateCount;
	private double rateAvg;
	private double rateFoodAvg;
	private double rateAmbientAvg;
	private double rateServiceAvg;
	private double rateValueAvg;
	private long distance;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public double getRateAvg() {
		return rateAvg;
	}

	public void setRateAvg(double rateAvg) {
		this.rateAvg = rateAvg;
	}
	
	public double getRateFoodAvg() {
		return rateFoodAvg;
	}

	public void setRateFoodAvg(double rateFoodAvg) {
		this.rateFoodAvg = rateFoodAvg;
	}

	public double getRateAmbientAvg() {
		return rateAmbientAvg;
	}

	public void setRateAmbientAvg(double rateAmbientAvg) {
		this.rateAmbientAvg = rateAmbientAvg;
	}

	public double getRateServiceAvg() {
		return rateServiceAvg;
	}

	public void setRateServiceAvg(double rateServiceAvg) {
		this.rateServiceAvg = rateServiceAvg;
	}

	public double getRateValueAvg() {
		return rateValueAvg;
	}

	public void setRateValueAvg(double rateValueAvg) {
		this.rateValueAvg = rateValueAvg;
	}

	public long getDistance() {
		return distance;
	}

	public void setDistance(long distance) {
		this.distance = distance;
	}

	
}