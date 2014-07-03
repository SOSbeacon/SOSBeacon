package util;

import controller.checkin.checkingroup.CheckinGroupController;
import model.checkin.checkingroup.CheckinGroupModel;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import entities.Setting;
import entities.User;

public class Session {
	public static final long TOKEN_KEY=0x2bf2f76b6f8e9c04L;
	public static void clearToken(){
		PersistentObject persistent=PersistentStore.getPersistentObject(TOKEN_KEY);
		persistent.setContents(null);
		persistent.commit();
	}
	public static String getToken(){
		PersistentObject persistent=PersistentStore.getPersistentObject(TOKEN_KEY);
		Object o =persistent.getContents();
		if(o!=null)
			return (String)o;
		else return null;
	}
	public static void setToken(String token){
		PersistentObject persistent=PersistentStore.getPersistentObject(TOKEN_KEY);
		persistent.setContents(token);
		persistent.commit();
	}
	public static User getUser(){
		PersistentObject persistent=PersistentStore.getPersistentObject(User.KEY);
		Object o =persistent.getContents();
		if(o!=null&& o instanceof User)
			return (User)o;
		else return null;
	}
	public static void setUser(User u){
		PersistentObject persistent=PersistentStore.getPersistentObject(User.KEY);
		persistent.setContents(u);
		persistent.commit();
	}
	
	public static Setting getSetting(){
		PersistentObject persistent=PersistentStore.getPersistentObject(Setting.KEY);
		Object o =persistent.getContents();
		if(o!=null && o instanceof Setting)
			return (Setting)o;
		else return null;
	}
	public static void setSetting(Setting s){
		PersistentObject persistent=PersistentStore.getPersistentObject(Setting.KEY);
		persistent.setContents(s);
		persistent.commit();
	}
	public static void clearUser() {
		// TODO Auto-generated method stub
		setUser(null);
		
	}
	public static void clearMessage(){
		PersistentObject persistent=PersistentStore.getPersistentObject(CheckinGroupModel.RECENTLY_MESSAGE_KEY);
		persistent.setContents(null);
		persistent.commit();
	}
}
