package controller.checkin.checkingroup;

import icontroller.checkin.checkingroup.ICheckinGroupController;
import imodel.checkin.AlertCompletedArgs;
import imodel.checkin.IAlertCompleted;
import imodel.checkin.ISendDataCompleted;
import imodel.checkin.SendDataCompletedArgs;
import imodel.checkin.checkingroup.ICheckinGroupModel;
import imodel.group.GetContactGroupsCompletedArgs;
import imodel.group.IGetContactGroupsCompletedListener;
import iview.checkin.checkingroup.ICheckinGroupView;

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
import view.checkin.checkingroup.record.RecordView;
import constants.Values;
import controller.checkin.checkingroup.record.RecordController;
import entities.ContactGroup;
import entities.Location;
import entities.Setting;

public class CheckinGroupController implements ICheckinGroupController {

	ICheckinGroupView view;
	ICheckinGroupModel model;

	public CheckinGroupController(ICheckinGroupView view,
			ICheckinGroupModel model) {
		this.view = view;
		this.model = model;
		RecordController.clear();
	}

	IGetContactGroupsCompletedListener li;
	Object[] choices;
	String[] imagePaths,audioPaths;
	ContactGroup[] groups;
	IAlertCompleted alertCompleted;
	ISendDataCompleted sendDataCompleted;
	String alertId;
	int audioIndex,imageIndex;
	Setting s;
	final int MAX_LENGTH=20;
	public void initialize() {
		// TODO Auto-generated method stub
		if (li == null) {
			li = new IGetContactGroupsCompletedListener() {

				public void onGetContactGroupsCompleted(Object sender,
						GetContactGroupsCompletedArgs arg) {
					// TODO Auto-generated method stub
					choices = model.getChoices();
					String[] oc = new String[choices.length];
					for(int i=0;i<oc.length;i++){
						oc[i]=(String)choices[i];
						if(oc[i].length()>MAX_LENGTH){
							oc[i] = oc[i].substring(0, MAX_LENGTH);
						}
					}
					view.hideProgress();
					if (arg.isSuccess()) {
						synchronized (UiApplication.getEventLock()) {
							groups = arg.getGroups();
							view.fillGroups(groups);
							view.fillChoices(oc);
						}
					} else {
						view.showMessage(arg.getMessage());
						back();
					}
				}
			};
			model.addGetContactGroupsCompletedListener(li);
			alertCompleted = new IAlertCompleted() {

				public void onAlertCompleted(Object sender,
						AlertCompletedArgs arg) {
					// TODO Auto-generated method stub
					view.hideProgress();
					if (arg.isSuccess()) {
						if(audioPaths.length>0){
							view.showProgress("Uploading audio...");
							alertId = arg.getAlertId();
							audioIndex=0;
							imageIndex=0;
							FileConnection file;
							try {
								file = (FileConnection) Connector.open(
										audioPaths[audioIndex++],
										Connector.READ_WRITE);
								if (file.exists()) {
									InputStream input = file.openInputStream();
									byte[] data = new byte[(int) file
											.fileSize()];
									input.read(data, 0, data.length);
									input.close();
									file.delete();
									// file.close();
									model.uploadData(Session.getToken(),
											alertId, s.getPhoneId(),
											Values.AUDIO_TYPE, data);
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								final String message = e.getMessage();
								UiApplication.getUiApplication().invokeLater(
										new Runnable() {
	
											public void run() {
												// TODO Auto-generated method
												// stub
												view.showMessage("sendDataCompleted: catch IOException"
														+ message);
											}
										});
							}
						}else{
							UiApplication.getUiApplication().invokeLater(
									new Runnable() {

										public void run() {
											// TODO Auto-generated method stub
											if(msgIndex==0)
												model.saveMessage(mesg);
											view.showMessage("Check in group successfull");
											back();
										}
									});
						}
					} else {
						final String message = arg.getMessage();
						UiApplication.getUiApplication().invokeLater(
								new Runnable() {

									public void run() {
										// TODO Auto-generated method stub
										view.showMessage("alertCompleted: "
												+ message);
									}
								});
					}
				}
			};

			sendDataCompleted = new ISendDataCompleted() {
				public void onSendDataCompleted(Object sender,
						SendDataCompletedArgs arg) {
					// TODO Auto-generated method stub
					if (arg.isSuccess()) {
//						view.updateProgress("Uploading " + (audioIndex + 1)
//								+ "/" + imagesPath.length + " images...");
						if (audioIndex < audioPaths.length) {
							FileConnection file;
							try {
								file = (FileConnection) Connector.open(
										audioPaths[audioIndex++],
										Connector.READ_WRITE);
								if (file.exists()) {
									InputStream input = file.openInputStream();
									byte[] data = new byte[(int) file
											.fileSize()];
									input.read(data, 0, data.length);
									input.close();
									file.delete();
									// file.close();
									model.uploadData(Session.getToken(),
											alertId, s.getPhoneId(),
											Values.AUDIO_TYPE, data);
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								final String message = e.getMessage();
								UiApplication.getUiApplication().invokeLater(
										new Runnable() {

											public void run() {
												// TODO Auto-generated method
												// stub
												view.showMessage("sendDataCompleted: catch IOException"
														+ message);
											}
										});
							}
						} else {
							view.updateProgress("Uploading " + (imageIndex + 1)
									+ "/" + imagePaths.length + " images...");
							if(imageIndex<imagePaths.length){
								FileConnection file;
								try {
									file = (FileConnection) Connector.open(
											imagePaths[imageIndex++],
											Connector.READ_WRITE);
									if (file.exists()) {
										InputStream input = file.openInputStream();
										byte[] data = new byte[(int) file
												.fileSize()];
										input.read(data, 0, data.length);
										input.close();
										file.delete();
										// file.close();
										model.uploadData(Session.getToken(),
												alertId, s.getPhoneId(),
												Values.IMAGE_TYPE, data);
									}
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									final String message = e.getMessage();
									UiApplication.getUiApplication().invokeLater(
											new Runnable() {
	
												public void run() {
													// TODO Auto-generated method
													// stub
													view.showMessage("sendDataCompleted: catch IOException"
															+ message);
												}
											});
								}
							}else{
								view.hideProgress();
								UiApplication.getUiApplication().invokeLater(
										new Runnable() {
	
											public void run() {
												// TODO Auto-generated method stub
												if(msgIndex==0)
													model.saveMessage(mesg);
												view.showMessage("Check in group successfull");
												back();
											}
										});
							}
						}
					} else {
						view.hideProgress();
						final String message = arg.getMessage();
						UiApplication.getUiApplication().invokeLater(
								new Runnable() {

									public void run() {
										// TODO Auto-generated method stub
										view.showMessage("sendDatacompleted failed: "
												+ message);
									}
								});
					}
				}
			};

			model.addAlertCompleted(alertCompleted);
			model.addSendDataCompleted(sendDataCompleted);
		}
		view.showProgress("Loading...");
		Setting s = Session.getSetting();
		if (s != null) {
			model.getContactGroups(Session.getToken(), s.getPhoneId());
		} else {
			view.hideProgress();
		}
	}

	public void back() {
		String[] aP = RecordController.getAudioPaths();
		String[] iP = RecordController.getImagePaths();
		String[] del = new String[aP.length+iP.length];
		for(int i=0;i<aP.length;i++){
			del[i]=aP[i];
		}
		for(int i=aP.length;i<del.length;i++){
			del[i]=iP[i-aP.length];
		}
		for(int i=0;i<del.length;i++){
			FileConnection file;
			try {
				file = (FileConnection) Connector.open(
						del[i],
						Connector.READ_WRITE);
				if (file.exists()) {
					file.delete();
				}
			} catch (IOException e) {
				
			}
		}
		RecordController.clear();
		Navigation.goBack();
	}

	public void changeMessage(int selectedIndex) {
		// TODO Auto-generated method stub
		if (selectedIndex == 0) {
			view.showTextBox();
		} else {
			view.hideTextBox();
		}
	}

	int msgIndex;
	String mesg;
	public void submit(int groupIndex, int messageIndex, String msg) {
		// TODO Auto-generated method stub	
		msgIndex=messageIndex;
		if (groupIndex >= 0) {
			if (messageIndex == 0) {		
				mesg=msg;
			}else{
				mesg=(String)choices[messageIndex];
			}
			imagePaths = RecordController.getImagePaths();
			audioPaths = RecordController.getAudioPaths();
			s = Session.getSetting();
			final String token = Session.getToken();
			final int ind = groupIndex;
			view.showProgress("Checking in...");
			if(s!=null&&token!=null){
				Geo g = new Geo();
				g.addGetLocationCompleted(new IGetLocationCompleted() {
					
					public void onGetLocationCompleted(GeoArgs arg) {
						// TODO Auto-generated method stub
						if(arg.isSuccess()){
							Location loc = arg.getLocation();
							model.startAlert(token, s.getPhoneId(), loc.getLongitude(), loc.getLatitude(), Values.CHECKIN_TYPE, groups[ind].getId(), "", mesg);
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
			//IDENInfo.imeiToString(IDENInfo.getIMEI())
		}
	}

	public void record() {
		// TODO Auto-generated method stub
		// Navigation.pushModal(new CheckinGroupView());
		Setting s = Session.getSetting();
		int time=1;
		if(s!=null){
			try{
				time = Integer.parseInt(s.getVoiceDuration());
			}catch(Exception e){}
		}
		Navigation.navigate(new RecordView(time*Values.SECONDS_PER_SESSION));
	}
}
