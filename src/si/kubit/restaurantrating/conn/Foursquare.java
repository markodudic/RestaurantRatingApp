package si.kubit.restaurantrating.conn;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.net.Uri;
import android.util.Log;

public class Foursquare {

	private String clientId;
	private String clientSecret;
	private String userOAuth;
	private String redirectURI;

	private String foursquareVenusUrl;
	private String foursquareVenusSearchUrl;
	private String foursquareVenusTipsUrl;
	private String foursquareVenusPhotosUrl;
	private String foursquareVenusCategoriesUrl;
	private String foursquareUrl;
	private String foursquareTipsAddUrl;
	private String foursquarePhotosAddUrl;
	private String foursquareCheckinsRecentUrl;
	private String foursquareUserFriendsUrl;

	private String foursquareAuthenticateUrl;
	private String foursquareAccessTokenUrl;

	private String venuesCategory;
	private String venuesLimit;
	private String venuesRadius;
	
	private JSONArray categories;

	public Foursquare() {
	}

	public Foursquare(String settings) {
		try {
			JSONObject jSettings = ((JSONArray)new JSONTokener(settings).nextValue()).getJSONObject(0);
			this.clientId = jSettings.getString("clientId");
			this.clientSecret = jSettings.getString("clientSecret");
			this.redirectURI = jSettings.getString("redirectURI");
			
			this.foursquareVenusUrl = jSettings.getString("foursquareVenusUrl");
			this.foursquareVenusSearchUrl = jSettings.getString("foursquareVenusSearchUrl");
			this.foursquareVenusTipsUrl = jSettings.getString("foursquareVenusTipsUrl");
			this.foursquareVenusPhotosUrl = jSettings.getString("foursquareVenusPhotosUrl");
			this.foursquareVenusCategoriesUrl = jSettings.getString("foursquareVenusCategoriesUrl");
			this.foursquareUrl = jSettings.getString("foursquareUrl");
			this.foursquareTipsAddUrl = jSettings.getString("foursquareTipsAddUrl");
			this.foursquarePhotosAddUrl = jSettings.getString("foursquarePhotosAddUrl");
			this.foursquareCheckinsRecentUrl = jSettings.getString("foursquareCheckinsRecentUrl");
			this.foursquareUserFriendsUrl = jSettings.getString("foursquareUserFriendsUrl");
					
			this.foursquareAuthenticateUrl = jSettings.getString("foursquareAuthenticateUrl");
			this.foursquareAccessTokenUrl = jSettings.getString("foursquareAccessTokenUrl");
			this.venuesCategory = jSettings.getString("venuesCategory");

			this.venuesLimit = jSettings.getString("venuesLimit");
			this.venuesRadius = jSettings.getString("venuesRadius");
		} catch (Exception e) {
        	e.printStackTrace();
        }
	}

	
	private String send(String foursquareUrl, String foursquareParams, String serverParams, String outputParam, String method) throws IOException {

		String jsonResponse = "";

		
		String server = foursquareUrl + 
						foursquareParams +
						"&" + serverParams;
		Log.d("URL=",server);
		
		URL  url = new URL(server);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
        con.setRequestMethod(method);
        con.setDoInput(true);
        if (method.equals("POST"))
        	con.setDoOutput(true);
        
        if (outputParam != null) {
        	Log.d("OUTPUT=",outputParam);
    		OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
        	out.write(outputParam);
        	out.close();
        }
        
        try 
    	{
          BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
          String inputLine;

          while ((inputLine = in.readLine()) != null) {
        	  jsonResponse += inputLine;
          }
          in.close();
    	} catch(Exception e) { 
    		System.out.println(e);
    		e.printStackTrace();
    	} finally {
	   		con.disconnect();
	   	}
    	
    	return jsonResponse;
       
	}

	public JSONObject getRestaurant(String venueId) throws Exception {
		String jrestaurant = send(this.foursquareVenusUrl+"/"+venueId, getFoursquareOAuthParams(userOAuth), "", null, "GET");
		JSONTokener jTokener = new JSONTokener(jrestaurant);
		if (jTokener.more()) {
			JSONObject jdata = (JSONObject)jTokener.nextValue();
			return (JSONObject)((JSONObject)jdata.get("response")).get("venue");
		} else {
			return null;
		}
	}

	public JSONArray getRestaurants(double lat, double lng) throws Exception {
		String jrestaurants = send(this.foursquareVenusSearchUrl, 
									getFoursquareVenuesParams(),
									"categoryId="+this.venuesCategory+
									"&ll="+lat+","+lng+"&limit="+
									this.venuesLimit+"&radius="+this.venuesRadius,
									null, "GET");
		
		JSONTokener jTokener = new JSONTokener(jrestaurants);
		if (jTokener.more()) {
			JSONObject jdata = (JSONObject)jTokener.nextValue();
			return (JSONArray)((JSONObject)jdata.get("response")).get("venues");
		} else {
			return null;
		}
	}
		
	public void setCategories() throws Exception {
		String jcategories = send(this.foursquareVenusCategoriesUrl, getFoursquareOAuthParams(userOAuth), "", null, "GET");
		JSONTokener jTokener = new JSONTokener(jcategories);
		if (jTokener.more()) {
			JSONObject jdata = (JSONObject)jTokener.nextValue();
			categories = (JSONArray)((JSONObject)jdata.get("response")).get("categories");
		}
	}

	public JSONArray getCategories() {
		return categories;
	}

	public JSONObject getCategory(String categoryId) {
		try {
			for (int i=0; i<categories.length(); i++) {
				JSONObject category = (JSONObject) categories.getJSONObject(i);
				if (category.getString("id").equals(categoryId))
					return category;
			}
		} catch (JSONException e) {
			
		}
		return null;
	}
	
	public JSONArray getFriends() throws Exception {
		String loc = send(this.foursquareUserFriendsUrl, getFoursquareOAuthParams(userOAuth), "", null, "GET");
		JSONTokener jTokener = new JSONTokener(loc);
		if (jTokener.more()) {
			JSONObject jdata = (JSONObject)jTokener.nextValue();
			JSONObject responseFriends = (JSONObject)((JSONObject)jdata.get("response")).get("friends");
			return (JSONArray)(responseFriends.get("items"));
		} else {
			return null;
		}
	}

	public JSONArray getFriendsLocations() throws Exception {
		String loc = send(this.foursquareCheckinsRecentUrl, getFoursquareOAuthParams(userOAuth), "", null, "GET");
		JSONTokener jTokener = new JSONTokener(loc);
		if (jTokener.more()) {
			JSONObject jdata = (JSONObject)jTokener.nextValue();
			return (JSONArray)((JSONObject)jdata.get("response")).get("recent");
		} else {
			return null;
		}
	}


	public JSONArray getTips(String venueId) throws Exception {
		String tips = send(this.foursquareVenusTipsUrl.replace("VENUE_ID", venueId), getFoursquareVenuesParams(), "", null, "GET");
		JSONTokener jTokener = new JSONTokener(tips);
		if (jTokener.more()) {
			JSONObject jdata = (JSONObject)jTokener.nextValue();
			JSONObject responseTips = (JSONObject)((JSONObject)jdata.get("response")).get("tips");
			return (JSONArray)(responseTips.get("items"));
		} else {
			return null;
		}
	}

	public String addTip(String venue_id, String text) throws Exception {

		String tip = send(this.foursquareTipsAddUrl, 
								getFoursquareOAuthParams(userOAuth), 
								"venueId="+venue_id+"&text="+URLEncoder.encode(text,"UTF-8"),
								null, 
								"POST");
		
		JSONTokener jTokener = new JSONTokener(tip);
		if (jTokener.more()) {
			JSONObject jdata = (JSONObject)jTokener.nextValue();
			return ((JSONObject)((JSONObject)jdata.get("response")).get("tip")).toString();
		} else {
			return null;
		}
	}
	
	public JSONArray getPhotos(String venueId) throws Exception {
		String tips = send(this.foursquareVenusPhotosUrl.replace("VENUE_ID", venueId), getFoursquareVenuesParams(), "", null, "GET");
		JSONTokener jTokener = new JSONTokener(tips);
		if (jTokener.more()) {
			JSONObject jdata = (JSONObject)jTokener.nextValue();
			JSONObject responsePhotos = (JSONObject)((JSONObject)jdata.get("response")).get("photos");
			return (JSONArray)((JSONObject)((JSONArray)(responsePhotos.get("groups"))).get(1)).get("items");
		} else {
			return null;
		}
	}

	
	public String uploadPhoto(String venueId, File file) throws Exception {
		String uri = this.foursquarePhotosAddUrl+"?venueId="+venueId+"&oauth_token="+userOAuth;
		return upload(uri, file);
	}
	
	static public String upload(String uri, File file) throws Exception
	{
	       HttpURLConnection httpurlconnection = (HttpURLConnection)(new URL(uri)).openConnection();
	       httpurlconnection.setDoInput(true);
	       httpurlconnection.setRequestMethod("POST");
	       httpurlconnection.setRequestProperty("Accept", "application/json");

	       String s3 = Long.toString(System.currentTimeMillis(), 36);
	       httpurlconnection.setRequestProperty("Content-Type", (new StringBuilder()).append("multipart/form-data; boundary=----").append(s3).toString());
	       httpurlconnection.setDoOutput(true);
	       DataOutputStream dataoutputstream = new DataOutputStream(httpurlconnection.getOutputStream());

	       dataoutputstream.writeBytes((new StringBuilder()).append("------").append(s3).append("\r\n").toString());
	       dataoutputstream.writeBytes((new StringBuilder()).append("Content-Disposition: form-data; name=\"").append(Uri.fromFile(file).toString()).append("\"; filename=\"").append(Uri.fromFile(file).toString()).append("\"\r\n").toString());
	       dataoutputstream.writeBytes("Content-Type:image/jpeg\r\n\r\n");
	       BufferedInputStream bufferedinputstream = new BufferedInputStream(new FileInputStream(file));
	       for(int k = 0; (k = bufferedinputstream.read()) != -1;)
	           dataoutputstream.write(k);

	       bufferedinputstream.close();
	       dataoutputstream.writeBytes("\r\n");

	       dataoutputstream.writeBytes((new StringBuilder()).append("------").append(s3).append("--\r\n\r\n").toString());
	       dataoutputstream.flush();
	       dataoutputstream.close();
	       

	       if(httpurlconnection.getResponseCode() != 200)
	           throw new IllegalArgumentException((new StringBuilder()).append(httpurlconnection.getResponseCode()).append(" ").append(httpurlconnection.getResponseMessage()).append(" ").append(read(httpurlconnection.getErrorStream())).toString());
	       else
	           return read(httpurlconnection.getInputStream());
	   }

	    private static String read(InputStream inputstream)  throws Exception
		{
		    StringBuffer stringbuffer = new StringBuffer();
		    InputStreamReader inputstreamreader = new InputStreamReader(new BufferedInputStream(inputstream), "UTF-8");
		    for(int i = inputstreamreader.read(); i != -1; i = inputstreamreader.read())
		        stringbuffer.append((char)i);
		
		    inputstreamreader.close();
		    return stringbuffer.toString();
		}

		private String getFoursquareVenuesParams () {
			SimpleDateFormat dfYear=new SimpleDateFormat("yyyyMMdd");
			return "?client_id=" + this.clientId + 
					"&client_secret=" + this.clientSecret +
					"&v=" + dfYear.format(new Date()); 
		}


		private String getFoursquareOAuthParams (String oauth) {
			SimpleDateFormat dfYear=new SimpleDateFormat("yyyyMMdd");
			return "?client_id=" + this.clientId + 
					//"&client_secret=" + this.clientSecret +
					"&oauth_token=" + oauth +
					"&v=" + dfYear.format(new Date()); 
		}

		public String getClientId() {
			return clientId;
		}

		public void setClientId(String clientId) {
			this.clientId = clientId;
		}

		public String getClientSecret() {
			return clientSecret;
		}

		public void setClientSecret(String clientSecret) {
			this.clientSecret = clientSecret;
		}

		public String getRedirectURI() {
			return redirectURI;
		}

		public void setRedirectURI(String redirectURI) {
			this.redirectURI = redirectURI;
		}

		public String getFoursquareVenusUrl() {
			return foursquareVenusUrl;
		}

		public void setFoursquareVenusUrl(String foursquareVenusUrl) {
			this.foursquareVenusUrl = foursquareVenusUrl;
		}

		public String getFoursquareVenusSearchUrl() {
			return foursquareVenusSearchUrl;
		}

		public void setFoursquareVenusSearchUrl(String foursquareVenusSearchUrl) {
			this.foursquareVenusSearchUrl = foursquareVenusSearchUrl;
		}

		public String getFoursquareVenusTipsUrl() {
			return foursquareVenusTipsUrl;
		}

		public void setFoursquareVenusTipsUrl(String foursquareVenusTipsUrl) {
			this.foursquareVenusTipsUrl = foursquareVenusTipsUrl;
		}

		public String getFoursquareVenusPhotosUrl() {
			return foursquareVenusPhotosUrl;
		}

		public void setFoursquareVenusPhotosUrl(String foursquareVenusPhotosUrl) {
			this.foursquareVenusPhotosUrl = foursquareVenusPhotosUrl;
		}

		public String getFoursquareUrl() {
			return foursquareUrl;
		}

		public void setFoursquareUrl(String foursquareUrl) {
			this.foursquareUrl = foursquareUrl;
		}

		public String getFoursquareTipsAddUrl() {
			return foursquareTipsAddUrl;
		}

		public void setFoursquareTipsAddUrl(String foursquareTipsAddUrl) {
			this.foursquareTipsAddUrl = foursquareTipsAddUrl;
		}

		public String getFoursquarePhotosAddUrl() {
			return foursquarePhotosAddUrl;
		}

		public void setFoursquarePhotosAddUrl(String foursquarePhotosAddUrl) {
			this.foursquarePhotosAddUrl = foursquarePhotosAddUrl;
		}

		public String getFoursquareAuthenticateUrl() {
			return foursquareAuthenticateUrl;
		}

		public void setFoursquareAuthenticateUrl(String foursquareAuthenticateUrl) {
			this.foursquareAuthenticateUrl = foursquareAuthenticateUrl;
		}

		public String getFoursquareAccessTokenUrl() {
			return foursquareAccessTokenUrl;
		}

		public void setFoursquareAccessTokenUrl(String foursquareAccessTokenUrl) {
			this.foursquareAccessTokenUrl = foursquareAccessTokenUrl;
		}

		public String getVenuesCategory() {
			return venuesCategory;
		}

		public void setVenuesCategory(String venuesCategory) {
			this.venuesCategory = venuesCategory;
		}

		public String getFoursquareVenusCategoriesUrl() {
			return foursquareVenusCategoriesUrl;
		}

		public String getFoursquareCheckinsRecentUrl() {
			return foursquareCheckinsRecentUrl;
		}

		public String getUserOAuth() {
			return userOAuth;
		}

		public void setUserOAuth(String userOAuth) {
			this.userOAuth = userOAuth;
		}	

	    
}
