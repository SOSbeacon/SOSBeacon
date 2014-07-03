package model.group.contacts;

import http.HttpRequestDispatcher;
import http.IHttpListener;
import imodel.group.contacts.GetContactsCompletedArgs;
import imodel.group.contacts.IGetContactsCompletedListener;
import imodel.group.contacts.IYahooGetAccessTokenListener;
import imodel.group.contacts.IYahooGetRequestTokenListener;
import imodel.group.contacts.IYahooImportModel;
import imodel.group.contacts.YahooGetAccessTokenCompletedArgs;
import imodel.group.contacts.YahooGetRequestTokenCompletedArgs;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import util.URLUTF8Encoder;
import util.Util;
import constants.Values;
import entities.Contact;

public class YahooImportModel implements IYahooImportModel {

	Vector gli = new Vector();
	public void addGetRequestTokenCompletedListener(
			IYahooGetRequestTokenListener li) {
		// TODO Auto-generated method stub
		if(gli.contains(li))
			return;
		gli.addElement(li);
	}
	public void removeGetRequestTokenCompletedListener(
			IYahooGetRequestTokenListener li) {
		// TODO Auto-generated method stub
		if(gli.contains(li))
			gli.removeElement(li);
	}
	void fireGetRequestTokenCompleted(YahooGetRequestTokenCompletedArgs arg){
		int length=gli.size();
		for(int i=0;i<length;i++){
			((IYahooGetRequestTokenListener)gli.elementAt(i)).onGetRequestTokenCompleted(this, arg);
		}
	}
	public void getRequestToken(String yahooConsumerKey, String nonce,
			String signatureMethod, String consumeSecret, String timestamp,
			String version, String langPref, String callback) {
		// TODO Auto-generated method stub
		String parameter = "oauth_callback="+callback+"&oauth_consumer_key="+URLUTF8Encoder.encode(yahooConsumerKey)+"&oauth_nonce="+nonce+"&oauth_signature_method="+signatureMethod
		+"&oauth_timestamp="+timestamp+"&oauth_version="+version+"&xoauth_lang_pref="+langPref;
		System.out.println("Parameter: "+parameter);
		String baseString = "GET&"+URLUTF8Encoder.encode(Values.YAHOO_GET_REQUEST_TOKEN_URL)+"&"+URLUTF8Encoder.encode(parameter);
		String signingSecret = URLUTF8Encoder.encode(consumeSecret)+"&";
		String signature;
		try {
			System.out.println("Base String: "+baseString);
			signature = Util.hmacsha1(signingSecret, baseString);
			System.out.println("Signature: "+signature);
		} catch (Exception e) {
			signature="";
			e.printStackTrace();
		} 
		String url = Values.YAHOO_GET_REQUEST_TOKEN_URL+"?"+parameter+"&oauth_signature="+URLUTF8Encoder.encode(signature);
		System.out.println("URL: "+url);
		HttpRequestDispatcher dispatcher = new HttpRequestDispatcher(url,"GET",null);
		dispatcher.addChangeListener(new IHttpListener() {
			
			public void onHttpOk(Object sender, String response) {
				// TODO Auto-generated method stub
				System.out.println(response);
				String token="",tokenSecret="",expires="",requestUrl="",confirm="";
				String[] para=Util.split(response, "&");
				for(int i=0;i<para.length;i++){
					String[] p = Util.split(para[i], "=");
					if(p.length==2){
						if(p[0].equalsIgnoreCase("oauth_token")){
							token=p[1];
						}else if(p[0].equalsIgnoreCase("oauth_token_secret")){
							tokenSecret=p[1];
						}else if(p[0].equalsIgnoreCase("oauth_expires_in")){
							expires=p[1];
						}else if(p[0].equalsIgnoreCase("xoauth_request_auth_url")){
							requestUrl=p[1];
						}else if(p[0].equalsIgnoreCase("oauth_callback_confirmed")){
							confirm=p[1];
						}
					}
				}
				YahooGetRequestTokenCompletedArgs arg = new YahooGetRequestTokenCompletedArgs(token, tokenSecret, expires, requestUrl, confirm);
				fireGetRequestTokenCompleted(arg);
			}
			
			public void onHttpError(Object sender, String message) {
				// TODO Auto-generated method stub
				fireGetRequestTokenCompleted(new YahooGetRequestTokenCompletedArgs(message));
			}
		});
		dispatcher.start();
	}
	Vector vAuth = new Vector();
	public void getAccessToken(String consumerKey, String nonce, String signatureMethod, String consumerSecret,String tokenSecret,
				String timestamp, String version, String oauthToken, String verifierCode) {
		// TODO Auto-generated method stub
		/*
		 * https://api.login.yahoo.com/oauth/v2/get_token?
		 * oauth_consumer_key=dj0yJmk9OEJnWDZVRjBta1RZJmQ9WVdrOVVIWTNZakZLTXpBbWNHbzlNVFV5TXprd01UYzJNZy0tJnM9Y29uc3VtZXJzZWNyZXQmeD04Yw--
		 * &oauth_signature_method=plaintext
		 * &oauth_version=1.0
		 * &oauth_verifier=mu9vdm
		 * &oauth_token=gk47gtp
		 * &oauth_timestamp=1228169762
		 * &oauth_nonce=8B9SpF
		 * &oauth_signature=4f8cbed1ac24a2fe2121d6f16030226e0782fbd2%26dc33c687aab1ddbb521826cf1a580ad7b6b06d13
		 * */
		String parameter = "oauth_consumer_key="+URLUTF8Encoder.encode(consumerKey)+"&oauth_nonce="+nonce+"&oauth_signature_method="+signatureMethod
		+"&oauth_timestamp="+timestamp+"&oauth_token="+oauthToken+"&oauth_verifier="+verifierCode+"&oauth_version="+version;
//		String parameter1 = "oauth_consumer_key="+consumer
//							+"&oauth_signature_method="+method
//							+"&oauth_version="+version
//							+"&oauth_verifier="+verifierCode
//							+"&oauth_token="+oauthToken
//							+"&oauth_timestamp="+timespan
//							+"&oauth_nonce="+nonce
//							+"&oauth_signature="+signature;
		System.out.println("Parameter: "+parameter);
		String baseString = "GET&"+URLUTF8Encoder.encode(Values.YAHOO_GET_ACCESS_TOKEN_URL)+"&"+URLUTF8Encoder.encode(parameter);
		String signingSecret = URLUTF8Encoder.encode(consumerSecret)+"&"+URLUTF8Encoder.encode(tokenSecret);
		String signature;
		try {
			System.out.println("Base String: "+baseString);
			signature = Util.hmacsha1(signingSecret, baseString);
			System.out.println("Signature: "+signature);
		} catch (Exception e) {
			signature="";
			e.printStackTrace();
		} 
		HttpRequestDispatcher dispatcher = new HttpRequestDispatcher(Values.YAHOO_GET_ACCESS_TOKEN_URL+"?"+parameter+"&oauth_signature="+URLUTF8Encoder.encode(signature),"GET",null);
		
		//HttpRequestDispatcher dispatcher = new HttpRequestDispatcher(Values.YAHOO_GET_ACCESS_TOKEN_URL, "POST", parameter.getBytes());
		dispatcher.addChangeListener(new IHttpListener() {
			
			public void onHttpOk(Object sender, String response) {
				// TODO Auto-generated method stub
				System.out.println("getAccessToken: "+response);
				String token="",tokenSecret="",expires="",session="",oauthExpires="",guid="";
				String[] para=Util.split(response, "&");
				for(int i=0;i<para.length;i++){
					String[] p = Util.split(para[i], "=");
					if(p.length==2){
						if(p[0].equalsIgnoreCase("oauth_token")){
							token=p[1];
						}else if(p[0].equalsIgnoreCase("oauth_token_secret")){
							tokenSecret=p[1];
						}else if(p[0].equalsIgnoreCase("oauth_expires_in")){
							expires=p[1];
						}else if(p[0].equalsIgnoreCase("oauth_session_handle")){
							session=p[1];
						}else if(p[0].equalsIgnoreCase("oauth_authorization_expires_in")){
							oauthExpires=p[1];
						}else if(p[0].equalsIgnoreCase("xoauth_yahoo_guid")){
							guid=p[1];
						}
					}
					System.out.println(token);
					System.out.println(tokenSecret);
					System.out.println(expires);
					System.out.println(session);
					System.out.println(oauthExpires);
					System.out.println(guid);
				}
				YahooGetAccessTokenCompletedArgs arg = new YahooGetAccessTokenCompletedArgs(token, tokenSecret, expires, session, oauthExpires, guid);
				fireGetAccessTokenCompleted(arg);
			}
			
			public void onHttpError(Object sender, String message) {
				// TODO Auto-generated method stub
				fireGetAccessTokenCompleted(new YahooGetAccessTokenCompletedArgs(message));
			}
		});
		dispatcher.start();
	}
	public void addGetAccessTokenCompletedListener(
			IYahooGetAccessTokenListener li) {
		// TODO Auto-generated method stub
		if(vAuth.contains(li))
			return;
		vAuth.addElement(li);
	}
	public void removeGetAccessTokenCompletedListener(
			IYahooGetAccessTokenListener li) {
		// TODO Auto-generated method stub
		if(vAuth.contains(li))
			vAuth.removeElement(li);
	}
	void fireGetAccessTokenCompleted(YahooGetAccessTokenCompletedArgs arg){
		int length=vAuth.size();
		for(int i=0;i<length;i++){
			((IYahooGetAccessTokenListener)vAuth.elementAt(i)).onGetAccessTokenCompleted(this, arg);
		}
	}
	Vector vC = new Vector();
	public void addGetContactsCompletedListener(IGetContactsCompletedListener li) {
		// TODO Auto-generated method stub
		if(vC.contains(li))
			return;
		vC.addElement(li);
	}
	public void removeGetContactsCompletedListener(
			IGetContactsCompletedListener li) {
		// TODO Auto-generated method stub
		if(vC.contains(li))
			vC.removeElement(li);
	}
	public void getContacts(String guid, String view, String version, String nonce,
			String timestamp, String consumerKey, String accessToken, String tokenSecret) {
		
		String url = Values.YAHOO_GET_CONTACTS_URL+guid+"/contacts";
		// TODO Auto-generated method stub		
		String normalized = "format=json&oauth_consumer_key="+URLUTF8Encoder.encode(consumerKey)
		+"&oauth_nonce="+nonce
		+"&oauth_signature_method="+"HMAC-SHA1"
		+"&oauth_timestamp="+timestamp
		+"&oauth_token="+accessToken
		+"&oauth_version="+version;
		String baseString = "GET&"+URLUTF8Encoder.encode(url)+"&"+URLUTF8Encoder.encode(normalized);
//		String baseString = "GET&" 
//			+ URLUTF8Encoder.encode(url) 
//			+ "&format%3Djson%26oauth_consumer_key%3D" + URLUTF8Encoder.encode(consumerKey)
//			+ "%26oauth_nonce%3D" + nonce
//			+ "%26oauth_signature_method%3D" + "HMAC-SHA1"
//			+ "%26oauth_timestamp%3D" + timestamp
//			+ "%26oauth_token%3D" + URLUTF8Encoder.encode(URLUTF8Encoder.encode(accessToken)) 
//			+ "%26oauth_version%3D" + version;
		
		String signingSecret = URLUTF8Encoder.encode(Values.YAHOO_CONSUMER_SECRET)+"&"+URLUTF8Encoder.encode(tokenSecret);
		String signature;
		try {
			System.out.println("1----------------------:"+signingSecret);
			System.out.println("2----------------------:"+baseString);
			signature = Util.hmacsha1(signingSecret, baseString);
			System.out.println("3----------------------:"+signature);
			System.out.println("4----------------------:"+accessToken);
		} catch (Exception e) {
			signature="";
			e.printStackTrace();
		} 
		
		String header = new StringBuffer("OAuth realm=\"").append(URLUTF8Encoder.encode("yahooapis.com"))
		.append("\",oauth_version=\"").append(version)
		.append("\",oauth_consumer_key=\"").append(URLUTF8Encoder.encode(Values.YAHOO_CONSUMER_KEY))
		.append("\",oauth_token=\"").append(URLUTF8Encoder.encode(accessToken))
		.append("\",oauth_timestamp=\"").append(URLUTF8Encoder.encode(timestamp)) 
		.append("\",oauth_nonce=\"").append(URLUTF8Encoder.encode(nonce)) 
		.append("\",oauth_signature_method=\"").append(URLUTF8Encoder.encode("HMAC-SHA1"))
		.append("\",oauth_signature=\"").append(URLUTF8Encoder.encode(signature)).append("\"").toString();
		
		System.out.println("URL: "+Values.YAHOO_GET_CONTACTS_URL+guid+"/contacts");
		System.out.println("Header: "+header);
		
		//encoded.append("format", "json");
		//encoded.append("q", "SELECT * FROM social.contacts(200) WHERE ((guid=me)  AND ((fields.type=\"email\") OR (fields.type=\"phone\")))  limit 200");
		String uGet=Values.YAHOO_GET_CONTACTS_URL+guid+"/contacts?"+normalized+"&oauth_signature="+URLUTF8Encoder.encode(signature);
		System.out.println("URL: "+uGet);
		//HttpRequestDispatcher dispatcher = new HttpRequestDispatcher(Values.YAHOO_GET_CONTACTS_URL+guid+"/contacts?format=json", "GET", null);
		HttpRequestDispatcher dispatcher = new HttpRequestDispatcher(uGet, "GET", null);
		//dispatcher.setHeader(header, "OAuth realm=yahooapis.com");
		//HttpRequestDispatcher dispatcher = new HttpRequestDispatcher("http://query.yahooapis.com/v1/yql", "POST", encoded.getBytes());
		dispatcher.addChangeListener(new IHttpListener() {
			
			public void onHttpOk(Object sender, String response) {
				// TODO Auto-generated method stub
				//System.out.println("yahoo get contacts: "+response);
				try {
					JSONObject outer = new JSONObject(response);
					JSONObject contacts = outer.getJSONObject("contacts");
					JSONArray contact = contacts.getJSONArray("contact");
					Vector v = new Vector();
					for(int i=0;i<contact.length();i++){
						JSONObject o = contact.getJSONObject(i);
						JSONArray fields = o.getJSONArray("fields");
						String email=null,phone=null,name=null;
						for(int j=0;j<fields.length();j++){
							JSONObject uri = fields.getJSONObject(j);
							String type=uri.getString("type");
							if(type.equalsIgnoreCase("email")){
								email = uri.getString("value");
							}else if(type.equalsIgnoreCase("name")){
								JSONObject jname=uri.getJSONObject("value");
								name=jname.getString("prefix")+jname.getString("suffix")+jname.getString("givenName")+jname.getString("middleName")+jname.getString("familyName");
							}else if(type.equalsIgnoreCase("phone")){
								phone=uri.getString("value");
							}
						}
						if((name!=null&&email!=null)||(name!=null&&phone!=null)){
							Contact c = new Contact("",name,email,phone,phone,Values.NORMAL_CONTACT);
							v.addElement(c);
						}
					}
					Contact[] co = new Contact[v.size()];
					for(int i=0;i<co.length;i++){
						co[i]=(Contact)v.elementAt(i);
					}
					fireGetContactsCompleted(new GetContactsCompletedArgs(true, co));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					fireGetContactsCompleted(new GetContactsCompletedArgs(false, e.getMessage()));
				}
			}
			
			public void onHttpError(Object sender, String message) {
				// TODO Auto-generated method stub
				fireGetContactsCompleted(new GetContactsCompletedArgs(false, message));
			}
		});
		dispatcher.start();
	}
	void fireGetContactsCompleted(GetContactsCompletedArgs arg){
		int length=vC.size();
		for(int i=0;i<length;i++){
			((IGetContactsCompletedListener)vC.elementAt(i)).onGetContactsCompleted(this, arg);
		}
	}
}
