package view.group.contacts.imports;

import icontroller.group.contacts.imports.IContactImportController;
import iview.group.contacts.imports.IContactImportView;
import model.group.contacts.addnew.AddContactModel;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import util.Util;
import view.AView;
import view.custom.ContactListField;
import view.custom.Header;
import view.custom.ImageButtonField;
import view.custom.ListCallBack;
import view.custom.MessageBox;
import view.custom.ProgressPopup;
import bitmaps.HeaderBitmaps;
import controller.group.contacts.imports.ContactImportController;
import entities.Contact;
import entities.ContactGroup;

public class ContactImportView extends AView implements IContactImportView {
	private ContactListField listField;
	ListCallBack lcb;
	IContactImportController controller;
	// Constructor, sets up the screen.
	public ContactImportView(entities.Contact[] contactsImport,ContactGroup group,String title,Contact[] contacts, Contact[] add, Contact[] edit, Contact[] delete) {
		super();
		this.group=group;
		controller = new ContactImportController(this,new AddContactModel(),group,contacts,add,edit,delete);
		Header header = new Header(title);
		add(header);
		Font font = Util.getFont(6,Font.BOLD);
		ImageButtonField btnBack = new ImageButtonField("Back", HeaderBitmaps.getButtonBg(),HeaderBitmaps.getButtonFocusBg(),font,Color.WHITE,0);
		btnBack.setMargin((Header.getHeaderHeight()-btnBack.getPreferredHeight())/2, 0, 0, 10);
		btnBack.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.back();
			}
		});
		header.add(btnBack);
		VerticalFieldManager mngRight = new VerticalFieldManager(
				Field.USE_ALL_WIDTH | Field.FIELD_RIGHT);
		ImageButtonField btnSave = new ImageButtonField("Import",HeaderBitmaps.getButtonBg(),HeaderBitmaps.getButtonFocusBg(),font,Color.WHITE,Field.FIELD_RIGHT);
		btnSave.setMargin((Header.getHeaderHeight()-btnSave.getPreferredHeight())/2, 10, 0, 0);
		header.add(mngRight);
		btnSave.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.importClick();
			}
		});
		mngRight.add(btnSave);
		listField = new ContactListField();
		lcb = new ListCallBack();
		listField.setCallback(lcb);
		VerticalFieldManager v = new VerticalFieldManager(Manager.VERTICAL_SCROLL|Manager.USE_ALL_HEIGHT);
		v.setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		v.add(listField);
		add(v);
		initialize(contactsImport);
	}
	void initialize(entities.Contact[] contacts) {
		listField.setSize(contacts.length);
		for(int i=0;i<contacts.length;i++){
			lcb.add(contacts[i]);
		}
		if(contacts.length==0){
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					showMessage("Your contact is empty or no contact has one phone number or email address");
				}
			});
		}

	}

//	// Convert the list of contacts from an Enumeration to a Vector
//
//	/**
//	 * Returns the name to be displayed for a given Contact
//	 * 
//	 * @param contact
//	 *            The Contact for which to extract the display name
//	 * @return The name to be displayed in the list field
//	 */
//	public static String getDisplayName(Contact contact) {
//		if (contact == null) {
//			return null;
//		}
//
//		String displayName = null;
//
//		// First, see if there is a meaningful name set for the contact.
//		if (contact.countValues(Contact.NAME) > 0) {
//			final String[] name = contact.getStringArray(Contact.NAME, 0);
//			final String firstName = name[Contact.NAME_GIVEN];
//			final String lastName = name[Contact.NAME_FAMILY];
//			if (firstName != null && lastName != null) {
//				displayName = firstName + " " + lastName;
//			} else if (firstName != null) {
//				displayName = firstName;
//			} else if (lastName != null) {
//				displayName = lastName;
//			}
//
//			if (displayName != null) {
//				final String namePrefix = name[Contact.NAME_PREFIX];
//				if (namePrefix != null) {
//					displayName = namePrefix + " " + displayName;
//				}
//				return displayName;
//			}
//		}
//
//		// If not, use the company name.
//		if (contact.countValues(Contact.ORG) > 0) {
//			final String companyName = contact.getString(Contact.ORG, 0);
//			if (companyName != null) {
//				return companyName;
//			}
//		}
//		return displayName;
//	}

	ContactGroup group;
	public void showConfirm(String caption,String message) {
		// TODO Auto-generated method stub
		if(MessageBox.show(caption,message,MessageBox.D_YES_NO)==MessageBox.YES){
			controller.imports(listField.getSelectedContacts());
		}
	}

	public void showMessage(String message) {
		// TODO Auto-generated method stub
		MessageBox.show(message);
	}

	public void showProgress(String message) {
		// TODO Auto-generated method stub
		ProgressPopup.show(message);
	}

	public void hideProgress() {
		// TODO Auto-generated method stub
		ProgressPopup.hide();
	}
	public void updateStatus(String status) {
		// TODO Auto-generated method stub
		ProgressPopup.setMessage(status);
	}
	protected void onDisplay() {
		// TODO Auto-generated method stub
		super.onDisplay();
		controller.initialize();
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
