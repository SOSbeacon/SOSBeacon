package controller.group.contacts.imports;

import icontroller.group.contacts.imports.IContactImportController;
import imodel.group.contacts.addnew.IAddContactModel;
import iview.group.contacts.imports.IContactImportView;
import util.Navigation;
import view.group.contacts.ContactView;
import entities.Contact;
import entities.ContactGroup;

public class ContactImportController implements IContactImportController {

	IContactImportView view;
	IAddContactModel model;

	Contact[] contacts,add,edit,delete;
	public ContactImportController(IContactImportView view,
			IAddContactModel model,ContactGroup group,Contact[] contacts,Contact[] add, Contact[] edit, Contact[] delete) {
		this.view = view;
		this.model = model;
		this.group=group;
		this.contacts=contacts;
		this.add=add;
		this.edit=edit;
		this.delete=delete;
	}

	public void back() {
		// TODO Auto-generated method stub
		Navigation.navigateScreen(new ContactView(group,contacts,add,edit,delete));
	}
	static long id = 0;
	public void imports(entities.Contact[] selectedItems) {
		// TODO Auto-generated method stub
		if(selectedItems.length>0){
			for(int i=0;i<selectedItems.length;i++){
				selectedItems[i].setId("tmp"+(id++));
			}
			contacts = concat(contacts,selectedItems);
			add = concat(add,selectedItems);
			view.showMessage("Import successfull!");
			back();
		}else{
			view.showMessage("Please select contacts to import");
		}
	}

	Contact[] concat(Contact[] c1, Contact[] c2){
		Contact[] result= new Contact[c1.length+c2.length];
		int i;
		for(i=0;i<c1.length;i++){
			result[i]=c1[i];
		}
		for(int k=0;k<c2.length;k++){
			result[i++]=c2[k];
		}
		return result;
	}
	ContactGroup group;

	public void initialize() {
		// TODO Auto-generated method stub
	}

	public void importClick() {
		// TODO Auto-generated method stub
		view.showConfirm("SOSbeacon","Are you sure you want to import these contacts to "+group.getName());
	}

}
