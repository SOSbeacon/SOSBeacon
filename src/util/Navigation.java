package util;

import net.rim.device.api.system.Application;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;


public class Navigation {
	public static void exit(){
		System.exit(0);
	}
	public static void navigate(Screen scr){
		synchronized(Application.getEventLock()){
			try{
				//UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
			}catch(Exception ex){
				
			}finally{
				UiApplication.getUiApplication().pushScreen(scr);
			}
		}
	}
	public static void navigateScreen(Screen scr){
		synchronized(Application.getEventLock()){
			Screen active = UiApplication.getUiApplication().getActiveScreen();
			while(!scr.getClass().isInstance(active)){
				UiApplication.getUiApplication().popScreen(active);
				active = UiApplication.getUiApplication().getActiveScreen();
			}
			UiApplication.getUiApplication().popScreen(active);
			UiApplication.getUiApplication().pushScreen(scr);
		}
	}
	public static void navigateScreen(Class scr){
		synchronized(Application.getEventLock()){
			Screen active = UiApplication.getUiApplication().getActiveScreen();
			while(!scr.isInstance(active)){
				UiApplication.getUiApplication().popScreen(active);
				active = UiApplication.getUiApplication().getActiveScreen();
			}
		}
	}
	public static void goBack() {
		// TODO Auto-generated method stub
		synchronized(Application.getEventLock()){
			try{
				UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
			}catch(Exception ex){
				
			}finally{
				//UiApplication.getUiApplication().pushScreen(scr);
			}
		}
	}
	public static void pushModal(Screen scr){
		synchronized(Application.getEventLock()){
			try{
				//UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
			}catch(Exception ex){
				
			}finally{
				UiApplication.getUiApplication().pushModalScreen(scr);
			}
		}
	}
}
