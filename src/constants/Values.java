package constants;

public class Values {
	
	public static final String URL="http://www.sosbeacon.org";
	public static final String REGISTER_URL=URL+"/users";
	public static final String PHONE_TYPE="3";
	public static final String LOGIN_URL=URL+"/rest";
	public static final String UPDATE_PHONE_URL=URL+"/phones";
	public static final String GET_CONTACT_GROUPS_URL=URL+"/groups";
	public static final String UPDATE_SETTING_URL=URL+"/setting/";
	public static final String ALERT_URL=URL+"/alert";
	public static final String UPLOAD_DATA_URL=URL+"/data";
	public static final String REVIEW_URL=URL+"/web/alert/latest?token=";
	public static final String GET_CONTACTS_URL=URL+"/contacts";
	public static final String CHECKIN_TYPE="2";
	public static final String PANIC_TYPE="1";
	public static final String ALERT_TYPE="0";
	public static final String FAMILY_GROUP="0";
	public static final String FRIENDS_GROUP="1";
	public static final String FAMILY_FRIENDS_GROUP="2";
	public static final String NEIGHBORHOOD_GROUP="3";
	public static final String GROUPA_GROUP="4";
	public static final String GROUPB_GROUP="5";
	public static final String SINGLE_CONTACT_GROUP="6";
	public static final String IM_OK_MESSAGE="I'm OK";
	public static final String IMAGE_TYPE="0";
	public static final String AUDIO_TYPE="1";
	
	public static final String NORMAL_CONTACT="0";
	public static final String DEFAULT_CONTACT="1";
	/*server
	public static final String GOOGLE_OAUTH_URL="https://accounts.google.com/o/oauth2/auth";
	public static final String GOOGLE_CLIENT_ID="885793307909-hh1k4kasns6rb23prqk1jt3foadhqj2k.apps.googleusercontent.com";
	public static final String GOOGLE_REDIRECT_URI="http://sosbeacon.org/oauth2callback";
	public static final String GOOGLE_SCOPE="https://www.google.com/m8/feeds/";
	public static final String GOOGLE_RESPONSE_TYPE="code";
	public static final String GOOGLE_CLIENT_SECRET="LV7g4FXgxoRlVrnRza2lwYwd";
	*/
	// device oauth
	public static final String GOOGLE_GET_DEVICE_CODE_URL="https://accounts.google.com/o/oauth2/device/code";
	public static final String GOOGLE_CLIENT_ID="885793307909-s56j8keolc0kdl1sg2ikn5vijr7e36g8.apps.googleusercontent.com";
	public static final String GOOGLE_SCOPE="https://www.google.com/m8/feeds/";
	public static final String GOOGLE_CLIENT_SECRET="KHqJufRZbRU9HnSQvnWCkspV";
	public static final String GOOGLE_GIANT_TYPE_DEVICE = "http://oauth.net/grant_type/device/1.0";
	public static final String GOOGLE_GET_ACCESS_TOKEN_URL="https://accounts.google.com/o/oauth2/token";
	public static final String GOOGLE_GET_CONTACTS_URL="https://www.google.com/m8/feeds/contacts/default/full";
	
	public static final String YAHOO_GET_REQUEST_TOKEN_URL="https://api.login.yahoo.com/oauth/v2/get_request_token";
	public final static String YAHOO_CONSUMER_KEY = "dj0yJmk9OEJnWDZVRjBta1RZJmQ9WVdrOVVIWTNZakZLTXpBbWNHbzlNVFV5TXprd01UYzJNZy0tJnM9Y29uc3VtZXJzZWNyZXQmeD04Yw--";
	public final static String YAHOO_CONSUMER_SECRET = "4f8cbed1ac24a2fe2121d6f16030226e0782fbd2";
	public static final String YAHOO_GET_REQUEST_AUTH_URL="https://api.login.yahoo.com/oauth/v2/request_auth";
	public static final String YAHOO_GET_ACCESS_TOKEN_URL="https://api.login.yahoo.com/oauth/v2/get_token";
	public static final String YAHOO_GET_CONTACTS_URL="http://social.yahooapis.com/v1/user/";
	public static final int SECONDS_PER_SESSION = 30;
	
	public static final int SUCCESS=1;
	public static final int ERROR=2;
	public static final int NEW_ACCOUNT=3;
	public static final int ACCOUNT_NEW_NUMBER=4;
	public static final int ACCOUNT_NEW_IMEI=5;
	public static final int ACCOUNT_NO_ACTIVATED=6;
	public static final String URL2="http://sosbeacon.org:8085";
	public static final String REGISTER_URL2=URL2+"/phones";
	
	

}
