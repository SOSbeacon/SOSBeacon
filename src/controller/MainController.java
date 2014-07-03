package controller;

import net.rim.device.api.ui.UiApplication;
import icontroller.IMainController;
import imodel.ILoginCompleted;
import imodel.IMainModel;
import imodel.LoginCompletedArgs;
import iview.IMainView;
import util.Navigation;
import util.Session;
import view.RegisterView;
import view.checkin.CheckinView;
import view.needhelp.NeedHelpView;
import entities.User;

public class MainController implements IMainController {

	IMainModel model;
	IMainView view;
	public MainController(IMainView view,IMainModel model){
		this.view=view;
		this.model=model;
	}
	ILoginCompleted loginCompleted;
	public void initialize() {
		// TODO Auto-generated method stub
		if(Session.getToken()==null){
			User user;
			try {
				user = model.getUser();
				view.showProgress("Logging in...");
				if(loginCompleted==null){
					loginCompleted=new ILoginCompleted() {
						
						public void onLoginCompleted(Object sender, LoginCompletedArgs arg) {
							// TODO Auto-generated method stub
							view.hideProgress();
							if(arg.isSuccess()){
								// login success
								Session.setUser(arg.getUser());
								Session.setSetting(arg.getSetting());
								Session.setToken(arg.getToken());
								view.loginSucceed();
								if(arg.getSetting().getPanicNumber().length()==0||arg.getSetting().getPanicNumber().equalsIgnoreCase("0")){
									UiApplication.getUiApplication().invokeLater(new Runnable() {
										
										public void run() {
											// TODO Auto-generated method stub
											view.showEmergencyNotSetConfirm("Emergency phone not set yet for your location");
										}
									});
								}
							}else{
								if(arg.getUser().isActived()){
									// unknown error
									final String msg = arg.getMessage();
									UiApplication.getUiApplication().invokeLater(new Runnable() {
										
										public void run() {
											// TODO Auto-generated method stub
											view.showErrorMessage(msg);
										}
									});
								}else{
									// user inactive
									final String message = arg.getMessage();
									UiApplication.getUiApplication().invokeLater(new Runnable() {
										
										public void run() {
											// TODO Auto-generated method stub
											view.showActiveConfirm("SOSbeacon", message);
										}
									});
								}
							}
						}
					};
					model.addLoginCompletedListener(loginCompleted);
				}
				model.login(user);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("MainController.initialize: "+e.getMessage());
				Navigation.navigate(new RegisterView());
				e.printStackTrace();
			}
		}else{
			view.loginSucceed();
		}
	}

	public void receivedSms() {
		// TODO Auto-generated method stub
		initialize();
	}

	public void notReceivedSms() {
		// TODO Auto-generated method stub
		Navigation.navigate(new RegisterView());
	}

	final int time=2;
	public void waitForActive() {
		// TODO Auto-generated method stub
		view.showProgress("Wait");
		Thread wait=new Thread(){
			public void run() {
				for(int i=0;i<time;i++){
					view.updateProgressMessage("Waiting in "+(time-i)+" seconds");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				view.hideProgress();
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						view.showReceiveSmsConfirm("SOSbeacon", "Did you receive text messages to activate?");
					}
				});	
			}
		};
		wait.start();
	}

	public void imokClick() {
		// TODO Auto-generated method stub
		Navigation.navigate(new CheckinView());
	}

	public void needHelpclick() {
		// TODO Auto-generated method stub
		Navigation.navigate(new NeedHelpView());
	}
}
