package imodel;

import entities.Setting;
import entities.User;

public class RegisterCompletedArgs2 {
	String message;
	int responseCode;
	User user;
	Setting setting;
	String token;
	public RegisterCompletedArgs2(int responseCode, String message) {
		super();
		this.responseCode = responseCode;
		this.message = message;
	}
	
	public RegisterCompletedArgs2(int responseCode, String message, User user, Setting s,String token) {
		super();
		this.responseCode = responseCode;
		this.message = message;
		this.user = user;
		this.setting=s;
		this.token = token;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Setting getSetting() {
		return setting;
	}

	public void setSetting(Setting setting) {
		this.setting = setting;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
