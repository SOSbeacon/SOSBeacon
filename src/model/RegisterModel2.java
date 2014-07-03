package model;

import http.HttpRequestDispatcher;
import http.IHttpListener;
import imodel.IRegisterCompleted2;
import imodel.IRegisterModel2;
import imodel.RegisterCompletedArgs;
import imodel.RegisterCompletedArgs2;

import java.util.Vector;

import net.rim.blackberry.api.browser.URLEncodedPostData;
import net.rim.device.api.system.GPRSInfo;
import net.rim.device.api.system.IDENInfo;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import constants.Values;
import entities.Setting;
import entities.User;

public class RegisterModel2 implements IRegisterModel2 {

	public void submit(String imei, String phoneNumber, String phoneType,
			String action) {
		// TODO Auto-generated method stub
		URLEncodedPostData post = new URLEncodedPostData(null, false);
		post.append("imei", imei);
		post.append("number", phoneNumber);
		post.append("do", action);
		post.append("phoneType", Values.PHONE_TYPE);
		post.append("format", "json");
		HttpRequestDispatcher dispatcher = new HttpRequestDispatcher(
				Values.REGISTER_URL2, "POST", post.getBytes());
		dispatcher.addChangeListener(new IHttpListener() {

			public void onHttpOk(Object sender, String response) {
				// TODO Auto-generated method stub
				System.out.println("RegisterModel2: "+response);
				JSONObject outer;
				try {
					outer = new JSONObject(response);
					JSONObject res = outer.getJSONObject("response");
					int responseCode = res.getInt("responseCode");
					String message = res.getString("message");
					switch (responseCode) {
					case Values.SUCCESS: {
						String id = res.getString("id");
						String imei = res.getString("imei");
						String number = res.getString("number");
						String email = res.getString("email");
						String name = res.getString("name");
						String token = res.getString("token");
						String phoneType = res.getString("phoneType");
						String phoneStatus = res.getString("phoneStatus");
						String createdDate = res.getString("createdDate");
						String modifiedDate = res.getString("modifiedDate");
						String settingId = res.getString("settingId");
						String locationId = res.getString("locationId");
						String emergencyNumber = res
								.getString("emergencyNumber");
						String recordDuration = res.getString("recordDuration");
						String alertSendToGroup = res
								.getString("alertSendToGroup");
						String goodSamaritanStatus = res
								.getString("goodSamaritanStatus");
						String goodSamaritanRange = res
								.getString("goodSamaritanRange");
						String panicStatus = res.getString("panicStatus");
						String panicRange = res.getString("panicRange");
						String incomingGovernmentAlert = res
								.getString("incomingGovernmentAlert");
						String password = res.getString("password");

						User u = new User(name, email, password, number,
								phoneStatus.equalsIgnoreCase("1"), number, "",
								imei, name, phoneType);
						Setting s = new Setting(settingId, locationId, id,
								recordDuration, "", emergencyNumber,
								alertSendToGroup, goodSamaritanStatus,
								goodSamaritanRange, incomingGovernmentAlert,
								panicStatus, panicRange);

						RegisterCompletedArgs2 arg = new RegisterCompletedArgs2(
								responseCode, message, u, s, token);
						fireRegisterCompleted(arg);
					}
						break;
					case Values.ERROR: {
						RegisterCompletedArgs2 arg = new RegisterCompletedArgs2(
								responseCode, message);
						fireRegisterCompleted(arg);
					}
						break;
					case Values.ACCOUNT_NO_ACTIVATED: {
						RegisterCompletedArgs2 arg = new RegisterCompletedArgs2(
								responseCode, message);
						fireRegisterCompleted(arg);
					}
						break;
					case Values.ACCOUNT_NEW_NUMBER: {
						RegisterCompletedArgs2 arg = new RegisterCompletedArgs2(
								responseCode, message);
						fireRegisterCompleted(arg);
					}
						break;
					case Values.ACCOUNT_NEW_IMEI: {
						RegisterCompletedArgs2 arg = new RegisterCompletedArgs2(
								responseCode, message);
						fireRegisterCompleted(arg);
					}
						break;
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					onHttpError(this, "Parsing error");
				}
			}

			public void onHttpError(Object sender, String message) {
				// TODO Auto-generated method stub
				RegisterCompletedArgs2 arg = new RegisterCompletedArgs2(
						Values.ERROR, message);
				fireRegisterCompleted(arg);
			}
		});
		dispatcher.start();
	}

	public void addRegsiterCompletedListener(IRegisterCompleted2 listener) {
		// TODO Auto-generated method stub
		if (!listeners.contains(listener))
			listeners.addElement(listener);
	}

	public void removeRegisterCompletedListener(IRegisterCompleted2 listener) {
		// TODO Auto-generated method stub
		if (listeners.contains(listener))
			listeners.removeElement(listener);
	}

	private Vector listeners = new Vector();

	protected void fireRegisterCompleted(RegisterCompletedArgs2 arg) {
		int length = listeners.size();
		for (int i = 0; i < length; i++) {
			((IRegisterCompleted2) listeners.elementAt(i))
					.onRegisterCompleted(arg);
		}
	}

	public String getImei() {
		// TODO Auto-generated method stub
		String imei = IDENInfo.imeiToString(GPRSInfo.getIMEI());
		return imei;
	}
}
