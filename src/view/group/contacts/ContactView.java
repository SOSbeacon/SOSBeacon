package view.group.contacts;

import icontroller.group.contacts.IContactController;
import iview.group.contacts.IContactView;
import model.group.contacts.ContactModel;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import util.Util;
import view.AView;
import view.custom.AddContactPopup;
import view.custom.ContactListField2;
import view.custom.Header;
import view.custom.ImageButtonField;
import view.custom.ListCallBack;
import view.custom.MessageBox;
import view.custom.ProgressPopup;
import bitmaps.HeaderBitmaps;
import controller.group.contacts.ContactController;
import entities.Contact;
import entities.ContactGroup;

public class ContactView extends AView implements IContactView {

	ContactGroup gr;
	VerticalFieldManager mngContent;
	IContactController controller;
	AddContactPopup addPopup;

	public ContactView(ContactGroup g,Contact[] contacts, Contact[] add, Contact[] edit, Contact[] delete) {
		super();
		controller = new ContactController(this, new ContactModel(), g,contacts,add,edit,delete);
		this.gr = g;
		Header header = new Header(g.getName());
		ImageButtonField btnBack = new ImageButtonField("Back", HeaderBitmaps.getButtonBg(),HeaderBitmaps.getButtonFocusBg(),Util.getFont(6,Font.BOLD),Color.WHITE,0);
		btnBack.setMargin(
				(header.getPreferredHeight() - btnBack.getPreferredHeight()) / 2,
				0, 0, 10);
		btnBack.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.back();
			}
		});
		header.add(btnBack);
		VerticalFieldManager mngRight = new VerticalFieldManager(
				Field.USE_ALL_WIDTH | Field.FIELD_RIGHT);
		ImageButtonField btnAdd =new ImageButtonField("Add", HeaderBitmaps.getButtonBg(),HeaderBitmaps.getButtonFocusBg(),Util.getFont(6,Font.BOLD),Color.WHITE,Field.FIELD_RIGHT);
		btnAdd.setMargin(
				(header.getPreferredHeight() - btnBack.getPreferredHeight()) / 2,
				10, 0, 0);
		btnAdd.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.add();
			}
		});
		mngRight.add(btnAdd);
		//mngRight.setBackground(BackgroundFactory.createSolidBackground(Color.RED));
		header.add(mngRight);
		add(header);
		mngContent = new VerticalFieldManager(Manager.VERTICAL_SCROLL
				| Field.USE_ALL_WIDTH | Field.USE_ALL_HEIGHT);
		mngContent.setBackground(BackgroundFactory
				.createSolidBackground(Color.BLACK));
		add(mngContent);
		addPopup = new AddContactPopup(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.enterContact();
			}
		}, new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.importGoogle();
			}
		}, new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.importYahoo();
			}
		});
		listField = new ContactListField2();
		lcb = new ListCallBack();
		listField.setCallback(lcb);
		mngContent.add(listField);
	}

	//Contact tmp;

	public void fillContacts(Contact[] contacts) {
		// TODO Auto-generated method stub
		synchronized (UiApplication.getEventLock()) {
			listField.setSize(contacts.length);
			lcb.erase();
			for (int i = 0; i < contacts.length; i++) {
				lcb.add(contacts[i]);
			}
		}
	}

	private ContactListField2 listField;
	ListCallBack lcb;

	protected void onUiEngineAttached(boolean attached) {
		// TODO Auto-generated method stub
		super.onUiEngineAttached(attached);
		if(attached){
			controller.initialize();
		}
	}
	public void showProgress(String msg) {
		// TODO Auto-generated method stub
		ProgressPopup.show(msg);
	}

	public void hideProgress() {
		// TODO Auto-generated method stub
		ProgressPopup.hide();
	}


	public void showMessage(String message) {
		// TODO Auto-generated method stub
		MessageBox.show(message);
	}

	public void showAddContactPopup() {
		// TODO Auto-generated method stub
		addPopup.show();
	}

	public void hideAddContactPopup() {
		// TODO Auto-generated method stub
		addPopup.hide();
	}
	private class EditContact extends MenuItem {
		public EditContact() {
			super("Edit", 100000, 10);
		}

		public void run() {
			int index = listField.getSelectedIndex();
			if(index!=-1){
				Contact c=(Contact)lcb.get(listField, index);
				controller.edit(gr, c);
			}
		}
	}
	
	private class DeleteContact extends MenuItem{
		public DeleteContact() {
			super("Delete", 100000, 10);
		}

		public void run() {
			int index = listField.getSelectedIndex();
			if(index!=-1){
				//Contact c=(Contact)lcb.get(listField, index);
				System.out.println("SElected index: "+index);
				controller.delete(index);
			}
		}
	}
	protected void makeMenu(Menu menu, int instance) {
		menu.add(new DeleteContact());
		menu.add(new EditContact());
		super.makeMenu(menu, instance);
	}

	public void showConfirmSave(String message) {
		// TODO Auto-generated method stub
		if(MessageBox.show(message, MessageBox.D_YES_NO)==MessageBox.YES){
			controller.save();
			//controller.goBack();
		}else{
			controller.goBack();
		}
	}

	public void deleteContact(int index) {
		// TODO Auto-generated method stub
		listField.delete(index);
	}
	
	protected boolean keyChar(char c, int status, int time) {
		// TODO Auto-generated method stub
		if(c==Characters.ESCAPE){
			controller.back();
			return true;
		}
		return super.keyChar(c, status, time);
	}
}
