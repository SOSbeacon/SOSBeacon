package model.group;

import http.HttpRequestDispatcher;
import http.IHttpListener;
import imodel.group.GetContactGroupsCompletedArgs;
import imodel.group.IGetContactGroupsCompletedListener;
import imodel.group.IGroupModel;
import imodel.group.contacts.GetContactsCompletedArgs;
import imodel.group.contacts.IGetContactsCompletedListener;

import java.util.Vector;

import net.rim.blackberry.api.browser.URLEncodedPostData;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import constants.Values;
import entities.Contact;
import entities.ContactGroup;

public class GroupModel implements IGroupModel {

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

	public void getContacts(String token, String id) {
		// TODO Auto-generated method stub
		URLEncodedPostData post =new URLEncodedPostData(null, false);
		post.append("token", token);
		post.append("groupid", id);
		post.append("format", "json");
		HttpRequestDispatcher dispatcher =new HttpRequestDispatcher(Values.GET_CONTACTS_URL+"?"+post.toString(), "GET", null);
		dispatcher.addChangeListener(new IHttpListener() {
			
			public void onHttpOk(Object sender, String response) {
				// TODO Auto-generated method stub
				System.out.println("ContactModel.getContacts: "+response);
				try {
					JSONObject outer=new JSONObject(response);
					JSONObject res=outer.getJSONObject("response");
					boolean success=res.getBoolean("success");
					if(success){
						try{
//							System.out.println("RUN HERE DATA");
//							JSONArray items=res.getJSONArray("data");
//							System.out.println("RUN HERE DATA");
//							int length=items.length();
//							Contact[] groups=new Contact[length];
//							for(int i=0;i<length;i++){
//								JSONObject item=items.getJSONObject(i);
//								String id=item.getString("id");
//								String name=item.getString("name");
//								String email=item.getString("email");
//								String voice=item.getString("voicephone");
//								String text=item.getString("textphone");
//								String type=item.getString("type");
//								groups[i]=new Contact(id, name, email,voice,text,type);
//							}
							Vector v = new Vector();
							int index=0;
							JSONObject data = res.getJSONObject("data");
							while(true){	
								try{
									JSONObject item = data.getJSONObject("contact_"+String.valueOf(index));
									String id=item.getString("id");
									String name=item.getString("name");
									String email=item.getString("email");
									String voice=item.getString("voicephone");
									String text=item.getString("textphone");
									String type=item.getString("type");
									v.addElement(new Contact(id,name,email,voice,text,type));
									index++;
								}catch(Exception e){
									break;
								}
							}
							Contact[] groups = new Contact[v.size()];
							for(int i=0;i<groups.length;i++){
								groups[i]=(Contact)v.elementAt(i);
							}
							GetContactsCompletedArgs arg=new GetContactsCompletedArgs(true, groups);
							fireGetContactsCompleted(arg);
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
				GetContactsCompletedArgs arg =new GetContactsCompletedArgs(false, message);
				fireGetContactsCompleted(arg);
			}
		});
		dispatcher.start();
	}

	public void addGetContactsCompletedListener(IGetContactsCompletedListener li) {
		// TODO Auto-generated method stub
		if(listeners2.contains(li))
			return;
		listeners2.addElement(li);
	}

	public void removeGetContactsCompletedListener(
			IGetContactsCompletedListener li) {
		// TODO Auto-generated method stub
		if(listeners2.contains(li))
			listeners2.removeElement(li);
	}

	Vector listeners2 = new Vector();
	protected void fireGetContactsCompleted(GetContactsCompletedArgs arg){
		int length=listeners2.size();
		for(int i=0;i<length;i++){
			((IGetContactsCompletedListener)listeners2.elementAt(i)).onGetContactsCompleted(this, arg);
		}
	}

}
