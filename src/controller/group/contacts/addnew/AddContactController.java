package controller.group.contacts.addnew;

import icontroller.group.contacts.addnew.IAddContactController;
import imodel.group.contacts.GetContactsCompletedArgs;
import imodel.group.contacts.IGetContactsCompletedListener;
import imodel.group.contacts.addnew.IAddContactModel;
import iview.group.contacts.addnew.IAddContactView;
import util.Navigation;
import view.group.contacts.ContactView;
import view.group.contacts.imports.ContactImportView;
import constants.Values;
import entities.Contact;
import entities.ContactGroup;

public class AddContactController implements IAddContactController {

	IAddContactView view;
	IAddContactModel model;
	ContactGroup group;
	Contact[] contacts,add,edit,delete;
	public AddContactController(IAddContactView view, IAddContactModel model, ContactGroup group,Contact[] contacts, Contact[] add, Contact[] edit, Contact[] delete){
		this.view=view;
		this.model=model;
		this.group=group;
		this.contacts=contacts;
		this.add=add;
		this.delete=delete;
		this.edit=edit;
	}
	IGetContactsCompletedListener ig;
	public void initialize() {
		// TODO Auto-generated method stub
		if(ig==null){
			ig=new IGetContactsCompletedListener() {
				
				public void onGetContactsCompleted(Object sender,
						GetContactsCompletedArgs arg) {
					//try{
						view.hideProgress();
					//}catch(Exception e){}
					// TODO Auto-generated method stub
					Navigation.navigate(new ContactImportView(arg.getContacts(),group,"Phone Contacts",contacts,add,edit,delete));
				}
			};
			model.addGetPhoneContactsCompletedListener(ig);
		}
	}
	public void back(String name, String email, String voice, String text) {
		// TODO Auto-generated method stub
		//Navigation.navigate(new ContactView(group));
		//Navigation.navigateScreen(new ContactView(group));
		if(c==null){
			if(name.length()>0||email.length()>0||voice.length()>0||text.length()>0)
				view.showConfirm("This contact information was changed, do you want to save this contact?");
			else goBack();
		}else{
			c.setName(name);
			c.setEmail(email);
			c.setVoicePhone(voice);
			c.setTextPhone(text);
			if(c.isChanged()){
				view.showConfirm("This contact information was changed, do you want to save this contact?");
			}else{ 
				goBack();
			}
		}
	}
	final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	Contact c=null;
	public void save(String name, String email, String voice,
			String text) {
		// TODO Auto-generated method stub
//		try {
//			RE r = new RE(EMAIL_PATTERN);
//			if(!r.match(email)){
//				view.showMessage("Email is invalid!");
//				return;
//			}
//		} catch (RESyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();s
//			view.showMessage(e.getMessage());
//			return;
//		}
		view.showProgress("Saving...");
		if(c==null){
			System.out.println("CNC.Debug: c=null");
			c = new Contact("tmp"+System.currentTimeMillis(), name, email, voice, text, Values.NORMAL_CONTACT);
			add = Contact.concat(add, c);
			contacts = Contact.concat(contacts,c);
		}else{
			c.setName(name);
			c.setEmail(email);
			c.setVoicePhone(voice);
			c.setTextPhone(text);
			c.commit();
		}
		view.hideProgress();
		view.showMessage("Contact saved successfully");
	}
	
	public void goBack(){
		Navigation.navigateScreen(new ContactView(group,contacts,add,edit,delete));
	}
	public void importPhone() {
		// TODO Auto-generated method stub
		view.showProgress("Loading phone contacts...");
		model.getPhoneContact();
	}

}
