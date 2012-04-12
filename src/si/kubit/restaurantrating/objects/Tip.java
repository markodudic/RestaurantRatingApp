package si.kubit.restaurantrating.objects;


public class Tip {

	public Tip(){
		
	}	
    
	private String textTip;
	private String createdAt;
	private String firstName;
	private String lastName;
	private String photo;
	
	public String getTextTip() {
		return textTip;
	}
	public void setTextTip(String textTip) {
		this.textTip = textTip;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	
}