package model.group.contacts;

import http.HttpRequestDispatcher;
import http.IHttpListener;
import imodel.group.contacts.GoogleGetAccessTokenCompletedArgs;
import imodel.group.contacts.GetContactsCompletedArgs;
import imodel.group.contacts.GoogleGetDeviceCodeCompletedArgs;
import imodel.group.contacts.IGoogleGetAccessTokenListener;
import imodel.group.contacts.IGetContactsCompletedListener;
import imodel.group.contacts.IGoogleGetDeviceCodeListener;
import imodel.group.contacts.IGoogleImportModel;

import java.util.Vector;

import net.rim.blackberry.api.browser.URLEncodedPostData;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import constants.Values;
import entities.Contact;

public class GoogleImportModel implements IGoogleImportModel{

	Vector getDeviceCodes = new Vector();
	public void addGetDeviceCodeListener(IGoogleGetDeviceCodeListener li) {
		// TODO Auto-generated method stub
		if(getDeviceCodes.contains(li))
			return;
		getDeviceCodes.addElement(li);
	}

	public void removeGetDeviceCodeListener(IGoogleGetDeviceCodeListener li) {
		// TODO Auto-generated method stub
		if(getDeviceCodes.contains(li))
			getDeviceCodes.removeElement(li);
	}

	protected void fireGetDeviceCodeCompleted(GoogleGetDeviceCodeCompletedArgs arg){
		int length = getDeviceCodes.size();
		for(int i=0;i<length;i++){
			((IGoogleGetDeviceCodeListener)getDeviceCodes.elementAt(i)).onGetDeviceCodeCompleted(this, arg);
		}
	}
	public void getDeviceCode(String clientId, String scope) {
		// TODO Auto-generated method stub
		URLEncodedPostData encoded = new URLEncodedPostData(null, false);
		encoded.append("client_id", clientId);
		encoded.append("scope", scope);
		HttpRequestDispatcher dispatcher = new HttpRequestDispatcher(Values.GOOGLE_GET_DEVICE_CODE_URL, "POST", encoded.getBytes());
		dispatcher.addChangeListener(new IHttpListener() {
			
			public void onHttpOk(Object sender, String response) {
				// TODO Auto-generated method stub
				System.out.println("getDeviceCode: "+response);
				try {
					JSONObject object = new JSONObject(response);
					String deviceCode = object.getString("device_code");
					String userCode = object.getString("user_code");
					String vertificationUrl = object.getString("verification_url");
					String expiresIn = object.getString("expires_in");
					String interval = object.getString("interval");
					fireGetDeviceCodeCompleted(new GoogleGetDeviceCodeCompletedArgs(deviceCode,userCode,vertificationUrl,expiresIn,interval));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					fireGetDeviceCodeCompleted(new GoogleGetDeviceCodeCompletedArgs(e.getMessage()));
				}
			}
			
			public void onHttpError(Object sender, String message) {
				// TODO Auto-generated method stub
				fireGetDeviceCodeCompleted(new GoogleGetDeviceCodeCompletedArgs(message));
			}
		});
		dispatcher.start();
	}
	
	Vector v = new Vector();
	public void addGetAccessTokenListener(IGoogleGetAccessTokenListener li) {
		// TODO Auto-generated method stub
		if(v.contains(li))
			return;
		v.addElement(li);
	}

	public void removeAccessTokenListener(IGoogleGetAccessTokenListener li) {
		// TODO Auto-generated method stub
		if(v.contains(li))
			v.removeElement(li);
	}

	protected void fireGetAccessTokenCompleted(GoogleGetAccessTokenCompletedArgs arg) {
		int length = v.size();
		for(int i=0;i<length;i++){
			((IGoogleGetAccessTokenListener)v.elementAt(i)).onGetAccessTokenCompleted(this, arg);
		}
	}
	public void getAccessToken(String clientId, String clientSecret,
			String code, String grantType) {
		// TODO Auto-generated method stub
		URLEncodedPostData encoded = new URLEncodedPostData(null, false);
		encoded.append("client_id", clientId);
		encoded.append("client_secret", clientSecret);
		encoded.append("code", code);
		encoded.append("grant_type", grantType);
		HttpRequestDispatcher dispatcher = new HttpRequestDispatcher(Values.GOOGLE_GET_ACCESS_TOKEN_URL, "POST", encoded.getBytes());
		dispatcher.addChangeListener(new IHttpListener() {
			
			public void onHttpOk(Object sender, String response) {
				// TODO Auto-generated method stub
				System.out.println("getAccessToken: "+response);
				try {
					JSONObject object = new JSONObject(response);
					String accessToken=object.getString("access_token");
					String expiresIn = object.getString("expires_in");
					String refreshToken=object.getString("refresh_token");
					fireGetAccessTokenCompleted(new GoogleGetAccessTokenCompletedArgs(accessToken, expiresIn, refreshToken));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					fireGetAccessTokenCompleted(new GoogleGetAccessTokenCompletedArgs(e.getMessage()));
				}
			}
			
			public void onHttpError(Object sender, String message) {
				// TODO Auto-generated method stub
				fireGetAccessTokenCompleted(new GoogleGetAccessTokenCompletedArgs(message));
			}
		});
		dispatcher.start();
	}

	
	public void addGetContactsCompletedListener(IGetContactsCompletedListener li) {
		// TODO Auto-generated method stub
		if(listeners.contains(li))
			return;
		listeners.addElement(li);
	}

	public void removeGetContactsCompletedListener(
			IGetContactsCompletedListener li) {
		// TODO Auto-generated method stub
		if(listeners.contains(li))
			listeners.removeElement(li);
	}

	Vector listeners = new Vector();
	protected void fireGetContactsCompleted(GetContactsCompletedArgs arg){
		int length=listeners.size();
		for(int i=0;i<length;i++){
			((IGetContactsCompletedListener)listeners.elementAt(i)).onGetContactsCompleted(this, arg);
		}
	}

	public void getContacts(String accesstoken) {
		// TODO Auto-generated method stub
		URLEncodedPostData encoded = new URLEncodedPostData(null, false);
		encoded.append("oauth_token", accesstoken);
		encoded.append("alt", "json");
		HttpRequestDispatcher dispatcher = new HttpRequestDispatcher(Values.GOOGLE_GET_CONTACTS_URL+"?"+encoded.toString(), "GET", null);
		dispatcher.addChangeListener(new IHttpListener() {
			
			public void onHttpOk(Object sender, String response) {
				// TODO Auto-generated method stub
				System.out.println("GETCONTACTS: "+response);
				try {
					JSONObject outer = new JSONObject(response);
					JSONObject feed = outer.getJSONObject("feed");
					JSONArray entries = feed.getJSONArray("entry");
					Vector v = new Vector();
					for(int i=0;i<entries.length();i++){
						JSONObject ji = entries.getJSONObject(i);
						String name;
						try{
							JSONObject title=ji.getJSONObject("title");
							name = title.getString("$t");
						}catch(Exception e){
							name="";
						}
						System.out.println("Name "+i+": "+name);
						String email;
						try{
							JSONArray gdemail = ji.getJSONArray("gd$email");
							JSONObject gd1 = gdemail.getJSONObject(0);
							email=gd1.getString("address");
						}catch(Exception e){
							email="";
						}
						System.out.println("Email "+i+": "+email);
						String phone;
						try{
							JSONArray gdphone = ji.getJSONArray("gd$phoneNumber");
							JSONObject gdphone1=gdphone.getJSONObject(0);
							phone=gdphone1.getString("$t");
						}catch(Exception e){
							phone="";
						}
						if((name.length()>0&&email.length()>0)||(name.length()>0||phone.length()>0))
							v.addElement(new Contact("", name, email,phone,phone,Values.NORMAL_CONTACT));
						
					}
					Contact[] contacts = new Contact[v.size()];
					for(int id=0;id<contacts.length;id++){
						contacts[id]=(Contact)v.elementAt(id);
					}
					GetContactsCompletedArgs arg = new GetContactsCompletedArgs(true, contacts);
					fireGetContactsCompleted(arg);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					fireGetContactsCompleted(new GetContactsCompletedArgs(false, e.getMessage()));
					e.printStackTrace();
				}
			}
			
			public void onHttpError(Object sender, String message) {
				// TODO Auto-generated method stub
				fireGetContactsCompleted(new GetContactsCompletedArgs(false, message));
			}
		});
		dispatcher.start();
	}

}
