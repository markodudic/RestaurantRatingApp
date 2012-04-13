package si.kubit.restaurantrating.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class User {

	public User(){
		
	}	
	
	private String username;
	private String password;
	private String name;
	private String surname;
	private String oauthToken;
	
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getOauthToken() {
		return oauthToken;
	}
	public void setOauthToken(String oauthToken) {
		this.oauthToken = oauthToken;
	}

	public String user2json() {
		JSONArray jUserArray = new JSONArray();
		try {
			JSONObject jUser = new JSONObject();
			jUser.put("name", this.name);
			jUser.put("surname", this.surname);
			jUser.put("password", this.password);
			jUser.put("username", this.username);
			jUser.put("oauthtoken", this.oauthToken);
			jUserArray.put(jUser);
		} catch (JSONException ne) {
   			ne.printStackTrace();
		}
		
		return jUserArray.toString();
	}

	public void json2user(String userText) {
		try {
			JSONObject jUser = ((JSONArray)new JSONTokener(userText).nextValue()).getJSONObject(0);
			
			this.setName(jUser.getString("name"));
			this.setSurname(jUser.getString("surname"));
			this.setPassword(jUser.getString("password"));
			this.setUsername(jUser.getString("username"));
			this.setOauthToken(jUser.getString("oauthtoken"));
		} catch (JSONException ne) {
   			ne.printStackTrace();
		}
	}

	
}