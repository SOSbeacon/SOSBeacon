package model.setting.alert;

import http.HttpRequestDispatcher;
import http.IHttpListener;
import imodel.setting.alert.GetContactGroupsCompletedArgs;
import imodel.setting.alert.IAlertSettingModel;
import imodel.setting.alert.IGetContactGroupsCompleted;
import imodel.setting.alert.ISaveAlertCompleted;
import imodel.setting.alert.SaveAlertCompletedArgs;

import java.util.Vector;

import net.rim.blackberry.api.browser.URLEncodedPostData;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import constants.Values;
import entities.ContactGroup;

public class AlertSettingModel implements IAlertSettingModel {

	Vector contactListeners =new Vector();
	Vector alertListeners=new Vector();
	public void addGetContactGroupsCompletedListener(
			IGetContactGroupsCompleted arg) {
		// TODO Auto-generated method stub
		if(!contactListeners.contains(arg))
			contactListeners.addElement(arg);
	}

	public void removeGetContactGroupsCompletedListener(
			IGetContactGroupsCompleted arg) {
		// TODO Auto-generated method stub
		if(contactListeners.contains(arg))
			contactListeners.removeElement(arg);
		
	}

	public void getContactGroups(String phoneId, String token) {
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
							System.out.println("AlertSettingModel.fire");
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

	public void saveAlert(String settingId,String phoneId,String token,String voiceDuration, String defaultGroup, String emergencyNumber,
			boolean receiveGovernmentAlert) {
		// TODO Auto-generated method stub
		String url=Values.UPDATE_SETTING_URL+settingId;
		URLEncodedPostData post=new URLEncodedPostData(null, false);
		post.append("phoneid", phoneId);
		post.append("token",token);
		post.append("alertSendtoGroup", defaultGroup);
		post.append("panicNumber",emergencyNumber);
		post.append("voiceDuration", voiceDuration);
		post.append("incomingGovernmentAlert", receiveGovernmentAlert?"1":"0");
		post.append("format","json");
		HttpRequestDispatcher dispatcher=new HttpRequestDispatcher(url,"POST",post.getBytes());
		dispatcher.addChangeListener(new IHttpListener() {
			
			public void onHttpOk(Object sender, String response) {
				// TODO Auto-generated method stub
				System.out.println("AlertSettingModel.saveAlert: "+response);
				JSONObject outer;
				try {
					outer = new JSONObject(response);
					JSONObject res=outer.getJSONObject("response");
					boolean success=res.getBoolean("success");
					if(success){
						fireSaveAlertCompleted(new SaveAlertCompletedArgs(true, ""));
					}else{
						String message="";
						try{
							message=res.getString("error");
						}catch(Exception ex){
							message=res.getString("message");
						}
						fireSaveAlertCompleted(new SaveAlertCompletedArgs(false, message));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					fireSaveAlertCompleted(new SaveAlertCompletedArgs(false, e.getMessage()));
					e.printStackTrace();
				}
				
			}
			
			public void onHttpError(Object sender, String message) {
				// TODO Auto-generated method stub
				SaveAlertCompletedArgs arg=new SaveAlertCompletedArgs(false, message);
				fireSaveAlertCompleted(arg);
			}
		});
		dispatcher.start();
	}

	public void addSaveAlertCompletedListener(ISaveAlertCompleted arg) {
		// TODO Auto-generated method stub
		if(!alertListeners.contains(arg))
			alertListeners.addElement(arg);
	}

	public void removeSaveAlertCompletedListener(ISaveAlertCompleted arg) {
		// TODO Auto-generated method stub
		if(alertListeners.contains(arg))
			alertListeners.removeElement(arg);
	}

	protected void fireGetContactGroupsCompleted(GetContactGroupsCompletedArgs arg){
		int length=contactListeners.size();
		for(int i=0;i<length;i++){
			((IGetContactGroupsCompleted)contactListeners.elementAt(i)).onGetContactGroupsCompleted(this, arg);
		}
	}
	protected void fireSaveAlertCompleted(SaveAlertCompletedArgs arg){
		int length=alertListeners.size();
		for(int i=0;i<length;i++){
			((ISaveAlertCompleted)alertListeners.elementAt(i)).onSaveAlertCompleted(this, arg);
		}
	}

	public Object[] getVoiceDurations() {
		// TODO Auto-generated method stub
		return new Integer[]{new Integer(1),new Integer(2),new Integer(3),new Integer(4),new Integer(5),new Integer(6)};
	}
}
