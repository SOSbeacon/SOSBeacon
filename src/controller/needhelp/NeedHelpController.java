package controller.needhelp;

import constants.Values;
import icontroller.needhelp.INeedHelpController;
import icontroller.needhelp.VoiceNotesRecorderThread;
import imodel.checkin.AlertCompletedArgs;
import imodel.checkin.IAlertCompleted;
import imodel.checkin.ICheckinModel;
import imodel.checkin.ISendDataCompleted;
import imodel.checkin.SendDataCompletedArgs;
import iview.needhelp.INeedHelpView;
import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.invoke.PhoneArguments;
import net.rim.blackberry.api.phone.Phone;
import net.rim.blackberry.api.phone.PhoneListener;
import net.rim.device.api.ui.UiApplication;
import util.Geo;
import util.GeoArgs;
import util.IGetLocationCompleted;
import util.Navigation;
import util.Session;
import view.custom.MessageBox;
import view.needhelp.sendalert.SendAlertView;
import entities.Location;
import entities.Setting;

public class NeedHelpController implements INeedHelpController {
	INeedHelpView view;
	ICheckinModel model;

	public NeedHelpController(INeedHelpView view, ICheckinModel model) {
		this.view = view;
		this.model = model;
	}

	IAlertCompleted alertCompleted;
	ISendDataCompleted uploadCompleted;
	final int COUNT_DOWN = 10;

	boolean update=true;
	public void callEmergency() {
		// TODO Auto-generated method stub
		if(s!=null&&s.getPanicNumber().length()>0){
			update=true;
			MessageBox.setPad(20, 80, 0, 20);
			view.showMessage("SOSbeacon", "CALLING EMERGENCY PHONE " + COUNT_DOWN
					+ " seconds left", new Thread(new Runnable() {
	
				public void run() {
					// TODO Auto-generated method stub
					for (int i = COUNT_DOWN*10; i >= 0; i--) {
						if(update){
							view.updateMessage("CALLING EMERGENCY PHONE " + (int)(i/10)
									+ " seconds left");
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					try {
						view.hideMessage();
					} catch (Exception e) {
					}
				}
			}));
		}else{
			view.showMessage("SOSbeacon", "Emergency phone not yet set for your location");
		}
	}

	public void sendAlert() {
		// TODO Auto-generated method stub
		Setting s = Session.getSetting();
		int time=1;
		if(s!=null){
			try{
				time = Integer.parseInt(s.getVoiceDuration());
			}catch(Exception e){}
		}
		Navigation.navigate(new SendAlertView(time*Values.SECONDS_PER_SESSION));
	}

	public void cancel() {
		// TODO Auto-generated method stub
		Navigation.goBack();
	}
	VoiceNotesRecorderThread record;

	public void callNow() {
		// TODO Auto-generated method stub
		// countDown.
		update = false;
		System.out.println("Call now!!");
		Phone.addPhoneListener(new PhoneListener() {

			public void conferenceCallDisconnected(int callId) {
				// TODO Auto-generated method stub
				System.out
						.println("-----------------------------conferenceCallDisconnected");
			}

			public void callWaiting(int callid) {
				// TODO Auto-generated method stub
				System.out
						.println("-----------------------------NeedHelpController:callWaiting");
			}

			public void callResumed(int callId) {
				// TODO Auto-generated method stub
				System.out
						.println("------------------------------callResumed");
			}

			public void callRemoved(int callId) {
				// TODO Auto-generated method stub
				System.out
						.println("---------------------------callRemoved");
			}

			public void callInitiated(int callid) {
				// TODO Auto-generated method stub
				System.out.println("------------------------callInitiated");
			}

			public void callIncoming(int callId) {
				// TODO Auto-generated method stub
				System.out.println("--------------------callIncoming");
			}

			public void callHeld(int callId) {
				// TODO Auto-generated method stub
				System.out
						.println("-------------------------------callHeld");
			}

			public void callFailed(int callId, int reason) {
				// TODO Auto-generated method stub
				System.out
						.println("----------------------------callFailed");
			}

			public void callEndedByUser(int callId) {
				// TODO Auto-generated method stub
				System.out
						.println("-------------------------callEndedByUser");
			}

			public void callDisconnected(int callId) {
				// TODO Auto-generated method stub
				System.out
						.println("----------------------------------------------callDisconnected");
				try {
					record.stop();
					Phone.removePhoneListener(this);
					data = record.getVoiceNote();
					System.out
							.println("--------------------------------------------DATA LENGTH: "
									+ record.getVoiceNote().length);
					view.showProgress("Alerting...");
					Geo g = new Geo();
					g.addGetLocationCompleted(new IGetLocationCompleted() {
						
						public void onGetLocationCompleted(GeoArgs arg) {
							// TODO Auto-generated method stub
							if(arg.isSuccess()){
								Location loc = arg.getLocation();
								model.startAlert(Session.getToken(), s.getPhoneId(), loc.getLongitude()
										, loc.getLatitude(), Values.PANIC_TYPE, Values.FAMILY_GROUP, "", Values.IM_OK_MESSAGE);
							}else{
								view.hideProgress();
								final String message = arg.getMessage();
								UiApplication.getUiApplication().invokeLater(new Runnable() {
									
									public void run() {
										// TODO Auto-generated method stub
										view.showMessage("SOSbeacon",message);
									}
								});
							}
						}
					});	
					g.getCurrentLocation();
				} catch (Exception e) {

				}
			}

			public void callDirectConnectDisconnected(int callId) {
				// TODO Auto-generated method stub

			}

			public void callDirectConnectConnected(int callId) {
				// TODO Auto-generated method stub

			}

			public void callConnected(int callId) {
				// TODO Auto-generated method stub
				System.out
						.println("-------------------------------------callConnected");
				try {
					record = new VoiceNotesRecorderThread();
					record.start();
				} catch (Exception e) {
					System.out
							.println("--------------Call connected exception "
									+ e.getMessage());
				}
			}

			public void callConferenceCallEstablished(int callId) {
				// TODO Auto-generated method stub
				System.out
						.println("--------------------callConferenceCallEstablished");
			}

			public void callAnswered(int callId) {
				// TODO Auto-generated method stub
				System.out.println("-------------------------callAnswered");
			}

			public void callAdded(int callId) {
				// TODO Auto-generated method stub
				System.out.println("--------------------------callAdded");
			}
		});
		 PhoneArguments call = new PhoneArguments(PhoneArguments.ARG_CALL,s.getPanicNumber());
		 Invoke.invokeApplication(Invoke.APP_TYPE_PHONE, call);
	}
	Setting s;
	byte[] data;
	public void cancelCall() {
		// TODO Auto-generated method stub
		System.out.println("Cancel call!!");
		update =false;
	}

	public void initialize() {
		// TODO Auto-generated method stub}
		s = Session.getSetting();
		if(alertCompleted==null){
			alertCompleted=new IAlertCompleted() {
				
				public void onAlertCompleted(Object sender, AlertCompletedArgs arg) {
					// TODO Auto-generated method stub
					view.hideProgress();
					if(arg.isSuccess()){
						//final String message = "you're ok: alert id: "+arg.getAlertId();
						view.showProgress("Uploading data...");
						model.uploadData(Session.getToken(), arg.getAlertId(), s.getPhoneId() ,Values.AUDIO_TYPE, data);
					}else{
						final String message = arg.getMessage();
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								view.showMessage("SOSbeacon",message);
							}
						});
					}
				}
			};
			uploadCompleted=new ISendDataCompleted() {
				
				public void onSendDataCompleted(Object sender, SendDataCompletedArgs arg) {
					// TODO Auto-generated method stub
					view.hideProgress();
					if(arg.isSuccess()){
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								view.showMessage("SOSbeacon","Call success");
							}
						});
					}else{
						final String message = arg.getMessage();
						UiApplication.getUiApplication().invokeLater(new Runnable(){
							public void run(){
								view.showMessage("SOSbeacon",message);
							}
						});
					}
				}
			};
			model.addAlertCompleted(alertCompleted);
			model.addSendDataCompleted(uploadCompleted);
		}
	}

	
}
