package controller.group.contacts;

import icontroller.group.contacts.IGoogleImportController;
import imodel.group.contacts.GetContactsCompletedArgs;
import imodel.group.contacts.GoogleGetAccessTokenCompletedArgs;
import imodel.group.contacts.GoogleGetDeviceCodeCompletedArgs;
import imodel.group.contacts.IGetContactsCompletedListener;
import imodel.group.contacts.IGoogleGetAccessTokenListener;
import imodel.group.contacts.IGoogleGetDeviceCodeListener;
import imodel.group.contacts.IGoogleImportModel;
import iview.group.contacts.IGoogleImportView;
import net.rim.device.api.ui.UiApplication;
import util.Navigation;
import view.group.contacts.imports.ContactImportView;
import constants.Values;
import entities.Contact;
import entities.ContactGroup;

public class GoogleImportController implements IGoogleImportController{

	IGoogleImportView view;
	IGoogleImportModel model;
	ContactGroup g;
	Contact[] contacts,add,edit,delete;
	public GoogleImportController(IGoogleImportView googleImportView,
			IGoogleImportModel googleImportModel,ContactGroup g,Contact[] contacts,Contact[] add, Contact[] edit, Contact[] delete) {
		// TODO Auto-generated constructor stub
		view=googleImportView;
		model=googleImportModel;
		this.g=g;
		this.contacts=contacts;
		this.add=add;
		this.edit=edit;
		this.delete=delete;
	}

	IGoogleGetDeviceCodeListener lid;
	IGoogleGetAccessTokenListener lia;
	IGetContactsCompletedListener lic;
	GoogleGetDeviceCodeCompletedArgs gdc;
	public void initialize() {
		if(lid==null){
			lid = new IGoogleGetDeviceCodeListener() {
				
				public void onGetDeviceCodeCompleted(Object sender,
						GoogleGetDeviceCodeCompletedArgs arg) {
					// TODO Auto-generated method stub
					view.hideProgress();
					if(arg.isSuccess()){
						gdc=arg;
						view.loadUrl(arg.getVerificationUrl());
					}else{
						final String message = "Failed!";
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								view.showMessage(message);	
							}
						});
					}
				}
			};
			lia = new IGoogleGetAccessTokenListener() {
				
				public void onGetAccessTokenCompleted(Object sender,
						GoogleGetAccessTokenCompletedArgs arg) {
					// TODO Auto-generated method stub
					view.hideProgress();
					if(arg.isSuccess()){
						//view.showMessage("Access token is"+arg.getAccessToken());
						view.showProgress("Getting your contacts...");
						model.getContacts(arg.getAccessToken());
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
			lic = new IGetContactsCompletedListener() {
				
				public void onGetContactsCompleted(Object sender,
						GetContactsCompletedArgs arg) {
					// TODO Auto-generated method stub
					view.hideProgress();
					if(arg.isSuccess()){
						Navigation.navigate(new ContactImportView(arg.getContacts(),g,"Google Contacts",contacts,add,edit,delete));
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
			model.addGetDeviceCodeListener(lid);
			model.addGetAccessTokenListener(lia);
			model.addGetContactsCompletedListener(lic);
		}
		// TODO Auto-generated method stub
		view.showProgress("Getting the device code...");
		model.getDeviceCode(Values.GOOGLE_CLIENT_ID, Values.GOOGLE_SCOPE);
	}
	public void browserLoaded(String url) {
		// TODO Auto-generated method stub
		if(url.indexOf("usercode")!=-1)
			view.fillUserCode(gdc.getUserCode());
		else if(url.indexOf("approval")!=-1){
			view.showProgress("Getting access token...");
			model.getAccessToken(Values.GOOGLE_CLIENT_ID, Values.GOOGLE_CLIENT_SECRET, gdc.getDeviceCode(), Values.GOOGLE_GIANT_TYPE_DEVICE);
		}
	}
	public void back() {
		// TODO Auto-generated method stub
		Navigation.goBack();
	}

}
