package model.group.contacts;

import http.HttpRequestDispatcher;
import http.IHttpListener;
import imodel.group.contacts.DeleteContactCompletedArgs;
import imodel.group.contacts.GetContactsCompletedArgs;
import imodel.group.contacts.IContactModel;
import imodel.group.contacts.IDeleteContactCompleted;
import imodel.group.contacts.IGetContactsCompletedListener;
import imodel.group.contacts.addnew.AddContactCompletedArgs;
import imodel.group.contacts.addnew.IAddContactCompletedListener;
import imodel.group.contacts.edit.EditContactCompletedArgs;
import imodel.group.contacts.edit.IEditContactCompletedListener;

import java.util.Vector;

import net.rim.blackberry.api.browser.URLEncodedPostData;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import constants.Values;
import entities.Contact;

public class ContactModel implements IContactModel {

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

	public void delete(String token, String contactId, String groupId,
			String name, String email, String voicePhone, String textPhone,
			String type) {
		// TODO Auto-generated method stub
		URLEncodedPostData post =new URLEncodedPostData(null, false);
		post.append("token", token);
		post.append("format", "json");
		post.append("groupid", groupId);
		post.append("name", name);
		post.append("email", email);
		post.append("voicephone", voicePhone);
		post.append("textphone", textPhone);
		post.append("_method", "DELETE");
		post.append("type", type);
		HttpRequestDispatcher dispatcher =new HttpRequestDispatcher(Values.GET_CONTACTS_URL+"/"+contactId, "POST", post.getBytes());
		dispatcher.addChangeListener(new IHttpListener() {
			
			public void onHttpOk(Object sender, String response) {
				// TODO Auto-generated method stub
				JSONObject outer;
				try {
					outer = new JSONObject(response);
					JSONObject res = outer.getJSONObject("response");
					boolean success = res.getBoolean("success");
					if (success) {
						DeleteContactCompletedArgs arg = new DeleteContactCompletedArgs(
								true, "");
						fireDeleteContactCompleted(arg);
					} else {
						String message = "";
						try {
							message = res.getString("error");
						} catch (Exception ex) {
							message = res.getString("message");
						}
						DeleteContactCompletedArgs arg = new DeleteContactCompletedArgs(
								false, message);
						fireDeleteContactCompleted(arg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					onHttpError(this, e.getMessage());
					e.printStackTrace();
				}
			}

			public void onHttpError(Object sender, String message) {
				// TODO Auto-generated method stub
				DeleteContactCompletedArgs arg = new DeleteContactCompletedArgs(
						false, message);
				fireDeleteContactCompleted(arg);
			}
		});
		dispatcher.start();
	}
	Vector deletes = new Vector();
	protected void fireDeleteContactCompleted(DeleteContactCompletedArgs arg){
		int length = deletes.size();
		for(int i=0;i<length;i++){
			((IDeleteContactCompleted)deletes.elementAt(i)).onDeleteContactCompleted(this,arg);
		}
	}

	public void addDeleteContactsCompletedListener(IDeleteContactCompleted li) {
		// TODO Auto-generated method stub
		if(deletes.contains(li))
			return;
		deletes.addElement(li);
	}

	public void removeDeleteContactsCompletedListener(IDeleteContactCompleted li) {
		// TODO Auto-generated method stub
		if(deletes.contains(li))
			deletes.removeElement(li);
	}
	
	
	Vector edits = new Vector();
	public void addEditContactCompletedListener(IEditContactCompletedListener li) {
		// TODO Auto-generated method stub
		if(edits.contains(li))
			return;
		edits.addElement(li);
	}

	public void removeEditContactCompletedListener(
			IEditContactCompletedListener li) {
		// TODO Auto-generated method stub
		if(edits.contains(li))
			edits.removeElement(li);
	}

	public void save(String token, String contactId, String groupId,
			String name, String email, String voicephone, String textphone,
			String type) {
		// TODO Auto-generated method stub
		URLEncodedPostData post = new URLEncodedPostData(null, false);
		post.append("token", token);
		post.append("groupid", groupId);
		post.append("name", name);
		post.append("email", email);
		post.append("voicephone", voicephone);
		post.append("textphone", textphone);
		post.append("type", type);
		post.append("_method", "PUT");
		post.append("format", "json");
		HttpRequestDispatcher dispatcher = new HttpRequestDispatcher(
				Values.GET_CONTACTS_URL+"/"+contactId, "POST", post.getBytes());
		dispatcher.addChangeListener(new IHttpListener() {

			public void onHttpOk(Object sender, String response) {
				// TODO Auto-generated method stub
				JSONObject outer;
				try {
					outer = new JSONObject(response);
					JSONObject res = outer.getJSONObject("response");
					boolean success = res.getBoolean("success");
					if (success) {
						EditContactCompletedArgs arg = new EditContactCompletedArgs(
								true, "");
						fireEditContactCompleted(arg);
					} else {
						String message = "";
						try {
							message = res.getString("error");
						} catch (Exception ex) {
							message = res.getString("message");
						}
						EditContactCompletedArgs arg = new EditContactCompletedArgs(
								false, message);
						fireEditContactCompleted(arg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					onHttpError(this, e.getMessage());
					e.printStackTrace();
				}
			}

			public void onHttpError(Object sender, String message) {
				// TODO Auto-generated method stub
				EditContactCompletedArgs arg = new EditContactCompletedArgs(
						false, message);
				fireEditContactCompleted(arg);
			}
		});
		dispatcher.start();
	}
	
	protected void fireEditContactCompleted(EditContactCompletedArgs arg){
		int length=edits.size();
		for(int index=0;index<length;index++){
			((IEditContactCompletedListener)edits.elementAt(index)).onEditContactCompleted(this, arg);
		}
	}
	
	Vector addsLi = new Vector();

	public void addAddContactCompletedListener(IAddContactCompletedListener li) {
		// TODO Auto-generated method stub
		if (addsLi.contains(li))
			return;
		addsLi.addElement(li);
	}

	public void removeAddContactCompletedListener(
			IAddContactCompletedListener li) {
		// TODO Auto-generated method stub
		if (addsLi.contains(li))
			addsLi.removeElement(li);
	}

	public void addContact(String token, String groupId, String name,
			String email, String voicephone, String textphone, String type) {
		// TODO Auto-generated method stub
		URLEncodedPostData post = new URLEncodedPostData(null, false);
		post.append("token", token);
		post.append("groupid", groupId);
		post.append("name", name);
		post.append("email", email);
		post.append("voicephone", voicephone);
		post.append("textphone", textphone);
		post.append("type", type);
		post.append("format", "json");
		HttpRequestDispatcher dispatcher = new HttpRequestDispatcher(
				Values.GET_CONTACTS_URL, "POST", post.getBytes());
		dispatcher.addChangeListener(new IHttpListener() {

			public void onHttpOk(Object sender, String response) {
				// TODO Auto-generated method stub
				JSONObject outer;
				try {
					outer = new JSONObject(response);
					JSONObject res = outer.getJSONObject("response");
					boolean success = res.getBoolean("success");
					if (success) {
						AddContactCompletedArgs arg = new AddContactCompletedArgs(
								true, "");
						fireAddContactCompleted(arg);
					} else {
						String message = "";
						try {
							message = res.getString("error");
						} catch (Exception ex) {
							message = res.getString("message");
						}
						AddContactCompletedArgs arg = new AddContactCompletedArgs(
								false, message);
						fireAddContactCompleted(arg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					onHttpError(this, e.getMessage());
					e.printStackTrace();
				}
			}

			public void onHttpError(Object sender, String message) {
				// TODO Auto-generated method stub
				AddContactCompletedArgs arg = new AddContactCompletedArgs(
						false, message);
				fireAddContactCompleted(arg);
			}
		});
		dispatcher.start();
	}

	protected void fireAddContactCompleted(AddContactCompletedArgs arg) {
		int length = addsLi.size();
		for (int i = 0; i < length; i++) {
			((IAddContactCompletedListener) addsLi.elementAt(i))
					.onAddContactCompleted(this, arg);
		}
	}
}
