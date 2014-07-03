package controller.group.contacts.edit;

import icontroller.group.contacts.edit.IEditContactController;
import imodel.group.contacts.edit.IEditContactModel;
import iview.group.contacts.edit.IEditContactView;
import util.Navigation;
import view.group.contacts.ContactView;
import entities.Contact;
import entities.ContactGroup;

public class EditContactController implements IEditContactController{

	IEditContactView view;
	IEditContactModel model;
	ContactGroup g;
	Contact[] contacts,add,edit,delete;
	Contact c;
	public EditContactController(IEditContactView editContactView,
			IEditContactModel editContactModel,ContactGroup g,Contact[] contacts, Contact[] add, Contact[] edit, Contact[] delete,Contact c) {
		// TODO Auto-generated constructor stub
		view=editContactView;
		model=editContactModel;
		this.contacts=contacts;
		this.add=add;
		this.edit=edit;
		this.delete=delete;
		this.c=c;
		this.g=g;
	}

	public void initialize() {
	}

	public void back(String name, String email, String voice, String text) {
		// TODO Auto-generated method stub
		c.setName(name);
		c.setEmail(email);
		c.setVoicePhone(voice);
		c.setTextPhone(text);
		if(c.isChanged()){
			view.showConfirm("This contact information was changed, do you want to save this contact?");
		}else goBack();
	}

	public void goBack(){
		//Navigation.navigate(new ContactView(g));
		Navigation.navigateScreen(new ContactView(g, contacts, add, edit, delete));
	}
	public void save(String name, String email, String voice, String text) {
		// TODO Auto-generated method stub
		view.showProgress("Saving...");
		c.setName(name);
		c.setEmail(email);
		c.setVoicePhone(voice);
		c.setTextPhone(text);
		if(c.isChanged()){
			for(int i=0;i<contacts.length;i++){
				if(contacts[i].getId().equalsIgnoreCase(c.getId())){
					contacts[i]=c;
					contacts[i].commit();
					if(contacts[i].getId().startsWith("tmp"))
						add=Contact.concat(add, contacts[i]);
					else edit=Contact.concat(edit, contacts[i]);
					break;
				}
			}
		}
		System.out.println("CNC.Debug: run here");
		view.hideProgress();
		view.showMessage("Contact saved successfully");
	}

}
