package controller;

import util.Navigation;
import net.rim.device.api.ui.UiApplication;
import constants.Values;
import icontroller.IRegisterController2;
import imodel.IRegisterCompleted2;
import imodel.IRegisterModel2;
import imodel.RegisterCompletedArgs2;
import iview.IRegisterView2;

public class RegisterController2 implements IRegisterController2 {

	IRegisterView2 view;
	IRegisterModel2 model;

	public RegisterController2(IRegisterView2 view, IRegisterModel2 model) {
		this.view = view;
		this.model = model;
	}

	public void cancelClick() {
		// TODO Auto-generated method stub
		Navigation.exit();
	}

	public void submitClick(String phoneNumber) {
		// TODO Auto-generated method stub
		view.showProgress("Loading...");
		String imei = model.getImei();
		model.submit(imei, phoneNumber, Values.PHONE_TYPE, "null");
	}

	IRegisterCompleted2 registerCompleted;

	public void initialize() {
		// TODO Auto-generated method stub
		if (registerCompleted == null) {
			registerCompleted = new IRegisterCompleted2() {

				public void onRegisterCompleted(RegisterCompletedArgs2 args) {
					 view.hideProgress();
					 final String message = args.getMessage();
					 final int responseCode = args.getResponseCode();
					 UiApplication.getUiApplication().invokeLater(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								view.showMessage("ResponseCode: "+responseCode+" message: "+message);
							}
						}); 
//					 switch(args.getResponseCode()){
//					 case Values.SUCCESS:{
//						 UiApplication.getUiApplication().invokeLater(new Runnable() {
//								
//								public void run() {
//									// TODO Auto-generated method stub
//									view.showMessage("SUCCESS");
//								}
//							}); 
//					 }break;
//					 case Values.ERROR:{
//						 UiApplication.getUiApplication().invokeLater(new Runnable() {
//							
//							public void run() {
//								// TODO Auto-generated method stub
//								view.showMessage("ERROR");
//							}
//						}); 
//					 }
//					 break;
//					 }
				}
			};
			model.addRegsiterCompletedListener(registerCompleted);
		}
	}
}
