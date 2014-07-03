package controller.setting.alert;

import net.rim.device.api.ui.UiApplication;
import icontroller.setting.alert.IAlertSettingController;
import imodel.setting.alert.GetContactGroupsCompletedArgs;
import imodel.setting.alert.IAlertSettingModel;
import imodel.setting.alert.IGetContactGroupsCompleted;
import imodel.setting.alert.ISaveAlertCompleted;
import imodel.setting.alert.SaveAlertCompletedArgs;
import iview.setting.alert.IAlertSettingView;
import util.Navigation;
import util.Session;
import entities.ContactGroup;
import entities.Setting;

public class AlertSettingController implements IAlertSettingController {

	IAlertSettingView view;
	IAlertSettingModel model;
	public AlertSettingController(IAlertSettingView view, IAlertSettingModel model){
		this.view=view;
		this.model=model;
	}
	ContactGroup[] groups;
	Object[] voiceDurations;
	IGetContactGroupsCompleted getContact;
	ISaveAlertCompleted saveAlert;
	public void initialize() {
		// TODO Auto-generated method stub
		if(getContact==null){
			getContact=new IGetContactGroupsCompleted() {
				
				public void onGetContactGroupsCompleted(Object sender,
						GetContactGroupsCompletedArgs arg) {
					// TODO Auto-generated method stub
					view.hideProgress();
					System.out.println("AlertSettingController.hideProgress");
					if(arg.isSuccess()){
						groups=arg.getGroups();
						if(groups.length>0){
							int selectedIndex=-1;
							for(int i=0;i<groups.length;i++){
								if(groups[i].getId().equalsIgnoreCase(s.getAlertSendToGroup())){
									selectedIndex=i;
									break;
								}
							}
							int voiceIndex = -1;
							voiceDurations = model.getVoiceDurations();
							for(int i=0;i<voiceDurations.length;i++){
								if(s.getVoiceDuration().equalsIgnoreCase(String.valueOf(voiceDurations[i]))){
									voiceIndex=i;
									break;
								}
							}
							synchronized(UiApplication.getEventLock()){
								System.out.println("AlertSettingController.beginfill");
								view.fillChoices(arg.getGroups(), selectedIndex);
								System.out.println("AlertSettingController.fill");
								view.fillFields(s.getPanicNumber(),s.getIncomingGovernmentAlert().equalsIgnoreCase("1"));
								System.out.println("AlertSettingController.endfill");
								view.fillVoices(voiceDurations, voiceIndex);
							}
						}
					}else{
						final String message = arg.getMessge();
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								view.showMessage(message);
							}
						});
					}
				}
			};
			model.addGetContactGroupsCompletedListener(getContact);
		}
		if(saveAlert==null){
			saveAlert=new ISaveAlertCompleted() {
				
				public void onSaveAlertCompleted(Object sender, SaveAlertCompletedArgs arg) {
					// TODO Auto-generated method stub
					view.hideProgress();
					if(arg.isSuccess()){
						Session.setSetting(s);
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								view.showMessage("Save successfull");
							}
						});
					}else{
						final String message = arg.getMessage();
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								view.showMessage(message);
							}
						});
					}
				}
			};
			model.addSaveAlertCompletedListener(saveAlert);
		}
		view.showProgress("Loading...");
		s=Session.getSetting();
		String token=Session.getToken();
		if(s!=null&&token!=null){
			model.getContactGroups(s.getPhoneId(), token);
		}
		
	}
	Setting s;
	public void save(int voiceIndex, int groupIndex, String emergencyNumber, boolean receiveGovernmentAlert) {
		// TODO Auto-generated method stub
		view.showProgress("Saving...");
		s.setAlertSendToGroup(groups[groupIndex].getId());
		s.setPanicNumber(emergencyNumber);
		s.setIncomingGovernmentAlert(receiveGovernmentAlert?"1":"0");
		s.setVoiceDuration(voiceDurations[voiceIndex].toString());
		model.saveAlert(s.getSettingId(),s.getPhoneId(),Session.getToken(),s.getVoiceDuration(),s.getAlertSendToGroup(), emergencyNumber, receiveGovernmentAlert);
	}
	public void cancel() {
		// TODO Auto-generated method stub
		Navigation.goBack();
	}

}
