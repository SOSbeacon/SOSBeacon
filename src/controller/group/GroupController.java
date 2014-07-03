package controller.group;

import icontroller.group.IGroupController;
import imodel.group.GetContactGroupsCompletedArgs;
import imodel.group.IGetContactGroupsCompletedListener;
import imodel.group.IGroupModel;
import imodel.group.contacts.GetContactsCompletedArgs;
import imodel.group.contacts.IGetContactsCompletedListener;
import iview.group.IGroupView;
import net.rim.device.api.ui.UiApplication;
import util.Navigation;
import util.Session;
import view.group.contacts.ContactView;
import entities.Contact;
import entities.ContactGroup;
import entities.Setting;

public class GroupController implements IGroupController {

	IGroupView view;
	IGroupModel model;
	public GroupController(IGroupView view, IGroupModel model) {
		// TODO Auto-generated constructor stub
		this.view=view;
		this.model=model;
	}
	IGetContactGroupsCompletedListener li;
	IGetContactsCompletedListener getContact;
	public void initialize() {
		// TODO Auto-generated method stub
		view.showProgress("Loading...");
		if(li==null){
			li = new IGetContactGroupsCompletedListener() {
				
				public void onGetContactGroupsCompleted(Object sender,GetContactGroupsCompletedArgs arg) {
					// TODO Auto-generated method stub
					view.hideProgress();
					if(arg.isSuccess()){
						view.fillGroups(arg.getGroups());
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
			getContact = new IGetContactsCompletedListener() {
				
				public void onGetContactsCompleted(Object sender,
						GetContactsCompletedArgs arg) {
					// TODO Auto-generated method stub
					view.hideProgress();
					if(arg.isSuccess()){
						//view.fillContacts(arg.getContacts());
						Navigation.navigate(new ContactView(g, arg.getContacts(), new Contact[0], new Contact[0], new Contact[0]));
					}else{
						final String message = "Failed: "+arg.getMessage();
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								view.showMessage(message);
							}
						});
					}
				}
			};
			model.addGetContactsCompletedListener(getContact);
			model.addGetContactGroupsCompletedListener(li);
		}
		String token = Session.getToken();
		Setting s = Session.getSetting();
		if(s!=null&&token!=null){
			model.getContactGroups(token,s.getPhoneId());
		}else{
			view.hideProgress();
		}
	}
	ContactGroup g;
	public void selectContactGroup(ContactGroup ind) {
		// TODO Auto-generated method stub
		g=ind;
		view.showProgress("Loading...");
		model.getContacts(Session.getToken(), ind.getId());
	}

}
