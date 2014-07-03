package controller;

import net.rim.device.api.ui.UiApplication;
import icontroller.IRegisterController;
import imodel.IRegisterCompleted;
import imodel.IRegisterModel;
import imodel.RegisterCompletedArgs;
import iview.IRegisterView;
import util.Navigation;
import util.Session;
import view.MainView;
import entities.User;

public class RegisterController implements IRegisterController {

	IRegisterView view;
	IRegisterModel model;
	public RegisterController(IRegisterView view,IRegisterModel model){
		this.view=view;
		this.model=model;
	}
	IRegisterCompleted registerCompleted;
	public void initialize() {
		// TODO Auto-generated method stub
		if(registerCompleted==null){
			registerCompleted=new IRegisterCompleted() {
				
				public void onRegisterCompleted(Object sender, RegisterCompletedArgs args) {
					view.hideProgress();
					// TODO Auto-generated method stub
					if(args.isSuccess()){
						//Save and navigate
						Session.setUser(u);
						Navigation.navigate(new MainView());
					}else{
						final String message = args.getMessage();
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								view.showErrorMessage(message);
							}
						});
					}
				}
			};
			model.addRegsiterCompletedListener(registerCompleted);
		}
		User u=Session.getUser();
		if(u!=null)
			view.fillFields(u);
	}

	public void cancelClick() {
		// TODO Auto-generated method stub
		Navigation.exit();
	}
	private User u;
	public void submitClick(String username,String email,String password,String phoneNumber) {
		// TODO Auto-generated method stub
		//view.showProgress();
		if(username==null||username.length()<6){
			view.showErrorMessage("Username is too short (at least 6 characters)");
			return;
		}
		if(email==null){
			view.showErrorMessage("Please enter email");
			return;
		}
		if(password==null){
			view.showErrorMessage("Please enter password");
		}
		if(phoneNumber==null){
			view.showErrorMessage("Please enter phone number");
		}
		view.showProgress("Connecting...");
		u=new User();
		u.setUserName(username);
		u.setEmail(email);
		u.setPassword(password);
		u.setPhoneNumber(phoneNumber);
		model.submit(username, password, email, phoneNumber);
	}

	
}
