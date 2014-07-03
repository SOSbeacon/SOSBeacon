package controller.setting.account;

import constants.Values;
import net.rim.device.api.ui.UiApplication;
import icontroller.setting.account.IAccountInformationController;
import imodel.setting.account.IAccountInformationModel;
import imodel.setting.account.ISaveAccountCompleted;
import imodel.setting.account.SaveAccountCompletedArgs;
import iview.setting.account.IAccountInformationView;
import util.Geo;
import util.GeoArgs;
import util.IGetLocationCompleted;
import util.Navigation;
import util.Session;
import entities.Location;
import entities.Setting;
import entities.User;

public class AccountInformationController implements
		IAccountInformationController {

	private IAccountInformationView view;
	private IAccountInformationModel model;

	public AccountInformationController(IAccountInformationView view, IAccountInformationModel model){
		this.view=view;
		this.model=model;
	}
	public void save(String username, String email, String phone,
			String password) {
		// TODO Auto-generated method stub
		final Setting s=Session.getSetting();
		final String token=Session.getToken();
		if(isValidated(username,email,phone,password)&&s!=null&&token!=null){
			final String un=username;
			final String em = email;
			final String ph = phone;
			final String pa = password;
			view.showProgress("Saving...");
			Geo g = new Geo();
			g.addGetLocationCompleted(new IGetLocationCompleted() {
				
				public void onGetLocationCompleted(GeoArgs arg) {
					// TODO Auto-generated method stub
					if(arg.isSuccess()){
						Location loc = arg.getLocation();
						u.setUserName(un);
						u.setEmail(em);
						u.setPhoneNumber(ph);
						u.setPassword(pa);
						model.save(s.getPhoneId(), token, un, em, ph, pa, loc.getLongitude(), loc.getLatitude());
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
		}
	}
	
	private boolean isValidated(String username,String email,String phone,String password){
		return true;
	}
	ISaveAccountCompleted saveCompleted;
	public void initialize() {
		// TODO Auto-generated method stub
		if(saveCompleted==null){
			saveCompleted=new ISaveAccountCompleted() {
				
				public void onSaveAccountCompleted(Object sender,
						SaveAccountCompletedArgs arg) {
					// TODO Auto-generated method stub
					view.hideProgress();
					if(arg.isSuccess()){
						Session.setUser(u);
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
			model.addSaveAccountCompletedListener(saveCompleted);
		}
		u = Session.getUser();
		if(u!=null){
			view.fillFields(u);
		}
	}
	User u;
	public void cancel() {
		// TODO Auto-generated method stub
		Navigation.goBack();
	}
}
