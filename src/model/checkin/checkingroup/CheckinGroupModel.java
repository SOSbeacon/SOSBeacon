package model.checkin.checkingroup;

import java.util.Hashtable;
import java.util.Vector;

import net.rim.blackberry.api.browser.URLEncodedPostData;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import constants.Values;
import entities.ContactGroup;
import entities.Setting;
import http.HttpRequestDispatcher;
import http.HttpUploadDispatcher;
import http.IHttpListener;
import imodel.checkin.AlertCompletedArgs;
import imodel.checkin.IAlertCompleted;
import imodel.checkin.ISendDataCompleted;
import imodel.checkin.SendDataCompletedArgs;
import imodel.checkin.checkingroup.ICheckinGroupModel;
import imodel.group.GetContactGroupsCompletedArgs;
import imodel.group.IGetContactGroupsCompletedListener;

public class CheckinGroupModel implements ICheckinGroupModel {
	public void getContactGroups(String token, String phoneId) {
		// TODO Auto-generated method stub
		URLEncodedPostData post =new URLEncodedPostData(null, false);
		post.append("token", token);
		post.append("phoneid", phoneId);
		post.append("format", "json");
		HttpRequestDispatcher dispatcher =new HttpRequestDispatcher(Values.GET_CONTACT_GROUPS_URL+"?"+post.toString(), "GET", null);
		dispatcher.addChangeListener(new IHttpListener() {
			
			public void onHttpOk(Object sender, String response) {
				// TODO Auto-generated method stub
				System.out.println("AlertSettingModel.getContactGroups: "+response);
				try {
					JSONObject outer=new JSONObject(response);
					JSONObject res=outer.getJSONObject("response");
					boolean success=res.getBoolean("success");
					System.out.println("GroupModel.response: "+response);
					if(success){
						try{
							JSONArray items=res.getJSONArray("data");
							int length=items.length();
							ContactGroup[] groups=new ContactGroup[length];
							for(int i=0;i<length;i++){
								JSONObject item=items.getJSONObject(i);
								String id=item.getString("id");
								String name=item.getString("name");
								String type=item.getString("type");
								groups[i]=new ContactGroup(id, name, type);
							}
							GetContactGroupsCompletedArgs arg=new GetContactGroupsCompletedArgs(true, groups);
							fireGetContactGroupsCompleted(arg);
						}catch(Exception ex){
							onHttpError(this, "Parsing error");
						}
					}else{
						String message="";
						try{
							message=res.getString("error");
						}catch(Exception ex){
							message=res.getString("message");
						}
						onHttpError(this, message);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					onHttpError(this, e.getMessage());
					e.printStackTrace();
				}
			}
			
			public void onHttpError(Object sender, String message) {
				// TODO Auto-generated method stub
				GetContactGroupsCompletedArgs arg =new GetContactGroupsCompletedArgs(false, message);
				fireGetContactGroupsCompleted(arg);
			}
		});
		dispatcher.start();
	}

	private void fireGetContactGroupsCompleted(
			GetContactGroupsCompletedArgs arg) {
		// TODO Auto-generated method stub
		int length=listeners.size();
		for(int i=0;i<length;i++){
		((IGetContactGroupsCompletedListener)listeners.elementAt(i)).onGetContactGroupsCompleted(this,arg);
		}
	}
	Vector listeners = new Vector();
	public void addGetContactGroupsCompletedListener(
			IGetContactGroupsCompletedListener arg) {
		// TODO Auto-generated method stub
		if(listeners.contains(arg))
			return;
		listeners.addElement(arg);
	}

	public void removeGetContactGroupsCompletedListener(
			IGetContactGroupsCompletedListener arg) {
		// TODO Auto-generated method stub
		if(listeners.contains(arg))
			listeners.removeElement(arg);
	}

	public Object[] getChoices() {
		// TODO Auto-generated method stub
//		return new String[]{
//				"Enter the message",
//				"I'm OK"
//		};
		PersistentObject persistent=PersistentStore.getPersistentObject(RECENTLY_MESSAGE_KEY);
		Object o =persistent.getContents();
		if(o!=null && o instanceof Vector){
		}else{
			o = new Vector();
		}
		Vector v =(Vector)o;
		String[] result = new String[v.size()+2];
		for(int i=1;i<result.length-1;i++){
			result[result.length-1-i]=(String)v.elementAt(i-1);
		}
		result[0]="Enter the message";
		result[result.length-1]="I'm OK";
		return result;
	}
	
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

	public static final long RECENTLY_MESSAGE_KEY=0xe574ba6b743e28cfL;
	final int SIZE=8;
	public void saveMessage(String message) {
		// TODO Auto-generated method stub
		PersistentObject persistent=PersistentStore.getPersistentObject(RECENTLY_MESSAGE_KEY);
		Object o =persistent.getContents();
		if(o!=null && o instanceof Vector){
			((Vector)o).addElement(message);
		}else{
			o = new Vector();
			((Vector)o).addElement(message);
		}
		if(((Vector)o).size()>SIZE){
			((Vector)o).removeElementAt(0);
		}
		persistent.setContents(o);
		persistent.commit();
	}
}
