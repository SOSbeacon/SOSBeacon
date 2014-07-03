package controller.group.contacts;

import net.rim.device.api.ui.UiApplication;
import icontroller.group.contacts.IContactController;
import imodel.group.contacts.DeleteContactCompletedArgs;
import imodel.group.contacts.IContactModel;
import imodel.group.contacts.IDeleteContactCompleted;
import imodel.group.contacts.addnew.AddContactCompletedArgs;
import imodel.group.contacts.addnew.IAddContactCompletedListener;
import imodel.group.contacts.edit.EditContactCompletedArgs;
import imodel.group.contacts.edit.IEditContactCompletedListener;
import iview.group.contacts.IContactView;
import util.Navigation;
import util.Session;
import view.group.GroupView;
import view.group.contacts.addnew.AddNewContactView;
import view.group.contacts.edit.EditContactView;
import view.group.contacts.imports.GoogleImportView;
import view.group.contacts.imports.YahooImportView;
import constants.Values;
import entities.Contact;
import entities.ContactGroup;

public class ContactController implements IContactController {

	IContactView view;
	IContactModel model;
	ContactGroup g;
	Contact[] contacts, add, edit, delete;

	public ContactController(IContactView contactView,
			IContactModel contactModel, ContactGroup g, Contact[] contacts,
			Contact[] add, Contact[] edit, Contact[] delete) {
		// TODO Auto-generated constructor stub
		view = contactView;
		model = contactModel;
		this.g = g;
		this.contacts = contacts;
		this.add = add;
		this.edit = edit;
		this.delete = delete;
	}

	IEditContactCompletedListener editLi;
	IDeleteContactCompleted deleteLi;
	IAddContactCompletedListener addLi;
	int editIndex;
	int deleteIndex;
	int addIndex;

	public void initialize() {
		// TODO Auto-generated method stub
		if (addLi == null) {
			addLi = new IAddContactCompletedListener() {

				public void onAddContactCompleted(Object sender,
						AddContactCompletedArgs arg) {
					// TODO Auto-generated method stub
					if (arg.isSuccess()) {
						if (addIndex < add.length) {
							// post next added contact to server
							model.addContact(Session.getToken(), g.getId(),
									add[addIndex].getName(),
									add[addIndex].getEmail(),
									add[addIndex].getVoicePhone(),
									add[addIndex].getTextPhone(),
									add[addIndex].getType());
							addIndex++;
						} else {
							// posted all added contacts
							if (edit.length > 0) {
								editIndex = 0;
								model.save(Session.getToken(),
										edit[editIndex].getId(), g.getId(),
										edit[editIndex].getName(),
										edit[editIndex].getEmail(),
										edit[editIndex].getVoicePhone(),
										edit[editIndex].getTextPhone(),
										edit[editIndex].getType());
								editIndex++;
							} else if (delete.length > 0) {
								deleteIndex = 0;
								model.delete(Session.getToken(),
										delete[deleteIndex].getId(), g.getId(),
										delete[deleteIndex].getName(),
										delete[deleteIndex].getEmail(),
										delete[deleteIndex].getVoicePhone(),
										delete[deleteIndex].getTextPhone(),
										delete[deleteIndex].getType());
								deleteIndex++;
							} else {
								// no added contacts
								view.hideProgress();
								UiApplication.getUiApplication().invokeLater(
										new Runnable() {

											public void run() {
												// TODO Auto-generated method
												// stub
												view.showMessage("Contact saved successfully");
												goBack();
											}
										});
							}
						}
					} else {
						view.hideProgress();
						final String message = arg.getMessage();
						UiApplication.getUiApplication().invokeLater(
								new Runnable() {

									public void run() {
										// TODO Auto-generated method stub
										view.showMessage(message);
										goBack();
									}
								});
					}
				}
			};
			editLi = new IEditContactCompletedListener() {

				public void onEditContactCompleted(Object sender,
						EditContactCompletedArgs arg) {
					// TODO Auto-generated method stub
					if (arg.isSuccess()) {
						if (editIndex < edit.length) {
							// post next edited contact to server
							model.save(Session.getToken(),
									edit[editIndex].getId(), g.getId(),
									edit[editIndex].getName(),
									edit[editIndex].getEmail(),
									edit[editIndex].getVoicePhone(),
									edit[editIndex].getTextPhone(),
									edit[editIndex].getType());
							editIndex++;
						} else {
							// posted all edited contacts
							if (delete.length > 0) {
								// post deleted contacts
								deleteIndex = 0;
								model.delete(Session.getToken(),
										delete[deleteIndex].getId(), g.getId(),
										delete[deleteIndex].getName(),
										delete[deleteIndex].getEmail(),
										delete[deleteIndex].getVoicePhone(),
										delete[deleteIndex].getTextPhone(),
										delete[deleteIndex].getType());
								deleteIndex++;
							} else {
								// no deleted contacts
								view.hideProgress();
								UiApplication.getUiApplication().invokeLater(
										new Runnable() {

											public void run() {
												// TODO Auto-generated method
												// stub
												view.showMessage("Contact saved successfully");
												goBack();
											}
										});
							}
						}
					} else {
						view.hideProgress();
						final String message = arg.getMessage();
						UiApplication.getUiApplication().invokeLater(
								new Runnable() {

									public void run() {
										// TODO Auto-generated method stub
										view.showMessage(message);
										goBack();
									}
								});
					}
				}
			};
			deleteLi = new IDeleteContactCompleted() {

				public void onDeleteContactCompleted(Object sender,
						DeleteContactCompletedArgs arg) {
					// TODO Auto-generated method stub
					if (arg.isSuccess()) {
						if (deleteIndex < delete.length) {
							// next deleted contact
							model.delete(Session.getToken(),
									delete[deleteIndex].getId(), g.getId(),
									delete[deleteIndex].getName(),
									delete[deleteIndex].getEmail(),
									delete[deleteIndex].getVoicePhone(),
									delete[deleteIndex].getTextPhone(),
									delete[deleteIndex].getType());
							deleteIndex++;
						} else {
							// done
							view.hideProgress();
							UiApplication.getUiApplication().invokeLater(
									new Runnable() {

										public void run() {
											// TODO Auto-generated method stub
											view.showMessage("Contact saved successfully");
											goBack();
										}
									});
						}
					} else {
						view.hideProgress();
						final String message = arg.getMessage();
						UiApplication.getUiApplication().invokeLater(
								new Runnable() {

									public void run() {
										// TODO Auto-generated method stub
										view.showMessage(message);
										goBack();
									}
								});
					}
				}
			};
			model.addEditContactCompletedListener(editLi);
			model.addDeleteContactsCompletedListener(deleteLi);
			model.addAddContactCompletedListener(addLi);
		}
		view.fillContacts(contacts);
	}

	public void back() {
		// TODO Auto-generated method stub
		// Navigation.navigate(new GroupView());
		// Navigation.goBack();
		if (add.length > 0 || edit.length > 0 || delete.length > 0) {
			view.showConfirmSave("Contact was changed, Do you want to save contacts ?");
		} else
			goBack();
	}

	public void add() {
		// TODO Auto-generated method stub
		view.showAddContactPopup();
	}

	public void enterContact() {
		// TODO Auto-generated method stub
		view.hideAddContactPopup();
		Navigation.navigate(new AddNewContactView(g, contacts, add, edit,
				delete));
	}

	public void importGoogle() {
		// TODO Auto-generated method stub
		view.hideAddContactPopup();
		Navigation
				.navigate(new GoogleImportView(g, contacts, add, edit, delete));
	}

	public void importYahoo() {
		// TODO Auto-generated method stub
		view.hideAddContactPopup();
		Navigation
				.navigate(new YahooImportView(g, contacts, add, edit, delete));
	}

	public void edit(ContactGroup g, Contact c) {
		// TODO Auto-generated method stub
		if (c.getType().equalsIgnoreCase(Values.DEFAULT_CONTACT)) {
			view.showMessage("Default contact not allow to edit or delete");
		} else {
			Navigation.navigate(new EditContactView(g, contacts, add, edit,
					delete, c));
		}
	}

	public void delete(int index) {
		// TODO Auto-generated method stub
		for (int i = 0; i < add.length; i++) {
			System.out.println("ADD: "+add[i].getId()+","+add[i].getName());
		}
//		view.showMessage("Index= " + index + ",contacts=" + contacts.length
//				+ ",add=" + add.length + ",edit=" + edit.length + ",delete="
//				+ delete.length);
		if (contacts[index].getType().equalsIgnoreCase(Values.DEFAULT_CONTACT)) {
			view.showMessage("Default contact not allow to edit or delete");
		} else {
			Contact[] tmp = new Contact[contacts.length - 1];
			int ind = 0;
			for (int i = 0; i < contacts.length; i++) {
				if (i == index) {
					System.out.println("CNC.Debug: ignore: "
							+ contacts[i].getName());
					if (!contacts[i].getId().startsWith("tmp")) {
//						view.showMessage("Add to delete:" + contacts[i].getId()
//								+ "," + contacts[i].getName());
						delete = Contact.concat(delete, contacts[i]);
					} else {
//						view.showMessage("Remove from add:"
//								+ contacts[i].getId() + ","
//								+ contacts[i].getName());
						int ind2 = 0;
						Contact[] nAdd = new Contact[add.length - 1];
						for (int k = 0; k < add.length; k++) {
//							view.showMessage(add[k].getId()+"=="+contacts[i].getId()+":"+add[k].getId().equalsIgnoreCase(
//									contacts[i].getId()));
							if (add[k].getId().equalsIgnoreCase(
									contacts[i].getId())) {
								System.out.println("Ignore "+add[k].getName());
								continue;
							}
							System.out.println("Clone "+add[k].getName());
							nAdd[ind2++] = add[k];
						}
						add = nAdd;
					}
					continue;
				}
				tmp[ind++] = contacts[i];
			}
			for (int i = 0; i < add.length; i++) {
				System.out.println("ADD: "+add[i].getId()+","+add[i].getName());
			}
			contacts = tmp;
			view.fillContacts(contacts);
		}
	}

	public void save() {
		// TODO Auto-generated method stub
		view.showProgress("Saving...");
		if (add.length > 0) {
			addIndex = 0;
			model.addContact(Session.getToken(), g.getId(),
					add[addIndex].getName(), add[addIndex].getEmail(),
					add[addIndex].getVoicePhone(),
					add[addIndex].getTextPhone(), add[addIndex].getType());
			addIndex++;
		} else if (edit.length > 0) {
			editIndex = 0;
			model.save(Session.getToken(), edit[editIndex].getId(), g.getId(),
					edit[editIndex].getName(), edit[editIndex].getEmail(),
					edit[editIndex].getVoicePhone(),
					edit[editIndex].getTextPhone(), edit[editIndex].getType());
			editIndex++;
		} else if (delete.length > 0) {
			deleteIndex = 0;
			model.delete(Session.getToken(), delete[deleteIndex].getId(),
					g.getId(), delete[deleteIndex].getName(),
					delete[deleteIndex].getEmail(),
					delete[deleteIndex].getVoicePhone(),
					delete[deleteIndex].getTextPhone(),
					delete[deleteIndex].getType());
			deleteIndex++;
		}
	}

	public void goBack() {
		// TODO Auto-generated method stub
		//Navigation.navigateScreen(new GroupView());
		Navigation.navigateScreen(GroupView.class);
	}

}
