package model.checkin;

import http.HttpRequestDispatcher;
import http.HttpUploadDispatcher;
import http.IHttpListener;
import imodel.checkin.AlertCompletedArgs;
import imodel.checkin.IAlertCompleted;
import imodel.checkin.ICheckinModel;
import imodel.checkin.ISendDataCompleted;
import imodel.checkin.SendDataCompletedArgs;
import java.util.Hashtable;
import java.util.Vector;

import net.rim.blackberry.api.browser.URLEncodedPostData;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import constants.Values;

public class CheckinModel implements ICheckinModel {

	private Vector alertListeners=new Vector();
	private Vector sendDataListeners=new Vector();
	public void addAlertCompleted(IAlertCompleted li) {
		// TODO Auto-generated method stub
		if(!alertListeners.contains(li))
			alertListeners.addElement(li);
	}

	public void removeAlertCompleted(IAlertCompleted li) {
		// TODO Auto-generated method stub
		if(alertListeners.contains(li))
			alertListeners.removeElement(li);
	}

	//protected void fireAlertCompleted()
	public void startAlert(String token, String phoneId, String longitude,
			String latitude, String type, String toGroup, String singleContact,
			String message) {
		// TODO Auto-generated method stub
		System.out.println("Long: "+longitude+"Lat: "+latitude);
		URLEncodedPostData post=new URLEncodedPostData(null, false);
		post.append("token", token);
		post.append("phoneid", phoneId);
		post.append("latitude", latitude);
		post.append("longtitude", longitude);
		post.append("type", type);
		post.append("toGroup", toGroup);
		post.append("singleContact", singleContact);
		post.append("message", message);
		post.append("format", "json");
		System.err.println("StartAlert URL: "+post.toString());
		HttpRequestDispatcher dispatcher=new HttpRequestDispatcher(Values.ALERT_URL, "POST", post.getBytes());
		dispatcher.addChangeListener(new IHttpListener() {
			
			public void onHttpOk(Object sender, String response) {
				// TODO Auto-generated method stub
				JSONObject outer;
				try {
					outer = new JSONObject(response);
					JSONObject res=outer.getJSONObject("response");
					boolean success=res.getBoolean("success");
					if(success){
						String alertId=res.getString("id");
						fireAlertCompleted(new AlertCompletedArgs(alertId));
					}else{
						String message="";
						try{
							message=res.getString("error");
						}catch(Exception ex){
							message=res.getString("message");
						}
						fireAlertCompleted(new AlertCompletedArgs(false,message));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					fireAlertCompleted(new AlertCompletedArgs(false,e.getMessage()));
				}
			}
			
			public void onHttpError(Object sender, String message) {
				// TODO Auto-generated method stub
				fireAlertCompleted(new AlertCompletedArgs(false,message));
			}
		});
		dispatcher.start();
	}

	public void uploadData(String token, String alertId, String phoneId,
			String type, byte[] data) {
		// TODO Auto-generated method stub
		
		Hashtable params = new Hashtable();
		params.put("token", token);
		params.put("alertid", alertId);
		params.put("type",type);
		params.put("format", "json");
		params.put("phoneid", phoneId);
		HttpUploadDispatcher upload;
		try {
			String fileName;
			String mime;
			if(type.equalsIgnoreCase(Values.IMAGE_TYPE)){
				fileName = "image.jpeg";
				mime="image/jpeg";
			}else{
				fileName = "audio.amr";
				mime = "audio/amr";
			}
				
			upload = new HttpUploadDispatcher(Values.UPLOAD_DATA_URL, params, "mediafile", fileName, mime, data);
			upload.addChangeListener(new IHttpListener() {
				
				public void onHttpOk(Object sender, String response) {
					// TODO Auto-generated method stub
					JSONObject outer;
					try {
						outer = new JSONObject(response);
						JSONObject res=outer.getJSONObject("response");
						boolean success=res.getBoolean("success");
						if(success){
							fireSendDataCompleted(new SendDataCompletedArgs(true, ""));
						}else{
							String message="";
							try{
								message=res.getString("error");
							}catch(Exception ex){
								message=res.getString("message");
							}
							fireSendDataCompleted(new SendDataCompletedArgs(true, message));
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						onHttpError(this, e.getMessage());
						e.printStackTrace();
					}
				}
				
				public void onHttpError(Object sender, String message) {
					// TODO Auto-generated method stub
					fireSendDataCompleted(new SendDataCompletedArgs(false, "onHttpError Upload: "+message));
				}
			});
			upload.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			SendDataCompletedArgs arg= new SendDataCompletedArgs(false, "Constructor exception!!");
			fireSendDataCompleted(arg);
			e.printStackTrace();
		}
	}

	public void addSendDataCompleted(ISendDataCompleted li) {
		// TODO Auto-generated method stub
		if(!sendDataListeners.contains(li))
			sendDataListeners.addElement(li);
	}

	public void removeSendDataCompleted(ISendDataCompleted li) {
		// TODO Auto-generated method stub
		if(sendDataListeners.contains(li))
			sendDataListeners.removeElement(li);
	}

	protected void fireAlertCompleted(AlertCompletedArgs arg){
		int length=alertListeners.size();
		for(int i=0;i<length;i++){
			((IAlertCompleted)alertListeners.elementAt(i)).onAlertCompleted(this, arg);
		}
	}
	protected void fireSendDataCompleted(SendDataCompletedArgs arg){
		int length=sendDataListeners.size();
		for(int i=0;i<length;i++){
			((ISendDataCompleted)sendDataListeners.elementAt(i)).onSendDataCompleted(this, arg);
		}
	}
}
