package controller.needhelp.sendalert;

import icontroller.needhelp.sendalert.ISendAlertController;
import imodel.checkin.AlertCompletedArgs;
import imodel.checkin.IAlertCompleted;
import imodel.checkin.ICheckinModel;
import imodel.checkin.ISendDataCompleted;
import imodel.checkin.SendDataCompletedArgs;
import iview.needhelp.sendalert.ISendAlertView;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.ui.UiApplication;
import util.Geo;
import util.GeoArgs;
import util.IGetLocationCompleted;
import util.Navigation;
import util.Session;
import constants.Values;
import entities.Location;
import entities.Setting;

public class SendAlertController implements ISendAlertController{
	ISendAlertView view;
	ICheckinModel model;

	public SendAlertController(ISendAlertView view, ICheckinModel model){
		this.view=view;
		this.model=model;
	}
	
	IAlertCompleted alertCompleted;
	ISendDataCompleted sendDataCompleted;
	byte[] audioData;
	String[] imagesPath;
	int currentIndex;
	String alertId;
	public void initialize() {
		// TODO Auto-generated method stub
		if(alertCompleted==null){
			alertCompleted = new IAlertCompleted() {
				
				public void onAlertCompleted(Object sender, AlertCompletedArgs arg) {
					// TODO Auto-generated method stub
					view.hideProgress();
					if(arg.isSuccess()){
						view.showProgress("Uploading audio...");
						alertId=arg.getAlertId();
						model.uploadData(Session.getToken(), arg.getAlertId(), s.getPhoneId(), Values.AUDIO_TYPE, audioData);
						currentIndex = 0;
					}else{
						final String message = arg.getMessage();
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								view.showMessage("alertCompleted: "+message);
							}
						});
					}
				}
			};
			
			sendDataCompleted = new ISendDataCompleted() {
				public void onSendDataCompleted(Object sender, SendDataCompletedArgs arg) {
					// TODO Auto-generated method stub
					if(arg.isSuccess()){
						view.updateProgress("Uploading "+(currentIndex+1)+"/"+imagesPath.length+" images...");
						if(currentIndex<imagesPath.length){
							FileConnection file;
							try {
								file = (FileConnection)Connector.open(imagesPath[currentIndex++],Connector.READ_WRITE);
								if(file.exists()){
									InputStream input = file.openInputStream();
									byte[] data = new byte[(int) file.fileSize()];
									input.read(data,0,data.length);
									input.close();
									file.delete();
									//file.close();
									model.uploadData(Session.getToken(), alertId, s.getPhoneId(), Values.IMAGE_TYPE, data);
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								final String message = e.getMessage();
								UiApplication.getUiApplication().invokeLater(new Runnable() {
									
									public void run() {
										// TODO Auto-generated method stub
										view.showMessage("sendDataCompleted: catch IOException"+message);
									}
								});
							}		
						}else{
							view.hideProgress();
							UiApplication.getUiApplication().invokeLater(new Runnable() {
								
								public void run() {
									// TODO Auto-generated method stub
									view.showMessage("Send alert successfull");
									back();
								}
							});
						}
					}else{
						view.hideProgress();
						final String message = arg.getMessage();
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								view.showMessage("sendDatacompleted failed: "+message);
							}
						});
					}
				}
			};
			
			model.addAlertCompleted(alertCompleted);
			model.addSendDataCompleted(sendDataCompleted);
		}
	}
	Setting s;
	public void sendAlert(byte[] audio, String[] imagesPaths) {
		// TODO Auto-generated method stub
		view.showProgress("Loading...");
		s = Session.getSetting();
		final String token = Session.getToken();
		final byte[] au = audio;
		final String[] im = imagesPaths;
		if(s!=null&&token!=null){
			Geo g = new Geo();
			g.addGetLocationCompleted(new IGetLocationCompleted() {
				
				public void onGetLocationCompleted(GeoArgs arg) {
					// TODO Auto-generated method stub
					if(arg.isSuccess()){
						Location loc = arg.getLocation();
						audioData=au;
						imagesPath=im;
						model.startAlert(token, s.getPhoneId(), loc.getLongitude(), loc.getLatitude(), Values.ALERT_TYPE, "", "", "");
					}else{
						view.hideProgress();
						final String message = arg.getMessage();
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								view.showMessage(message);
							}
						});
					}
				}
			});	
			g.getCurrentLocation();
		}else view.hideProgress();
	}
	public void back() {
		// TODO Auto-generated method stub
		Navigation.goBack();
	}

}
