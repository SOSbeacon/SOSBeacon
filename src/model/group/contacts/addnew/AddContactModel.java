package model.group.contacts.addnew;

import http.HttpRequestDispatcher;
import http.IHttpListener;
import imodel.group.contacts.GetContactsCompletedArgs;
import imodel.group.contacts.IGetContactsCompletedListener;
import imodel.group.contacts.addnew.AddContactCompletedArgs;
import imodel.group.contacts.addnew.IAddContactCompletedListener;
import imodel.group.contacts.addnew.IAddContactModel;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.pim.Contact;
import javax.microedition.pim.ContactList;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;

import net.rim.blackberry.api.browser.URLEncodedPostData;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import constants.Values;

public class AddContactModel implements IAddContactModel {

	

	Vector contactLi = new Vector();

	public void getPhoneContact() {
		// TODO Auto-generated method stub
		Thread t = new Thread(){
			public void run() {
				try {
					ContactList list = (ContactList) PIM.getInstance().openPIMList(
							PIM.CONTACT_LIST, PIM.READ_ONLY);
					Enumeration allContacts = list.items();
					Vector v = new Vector();
					while(allContacts.hasMoreElements()){
						Contact c = (Contact)allContacts.nextElement();
						entities.Contact co = convertToEntities(c);
						if(co!=null)
							v.addElement(co);
					}
					entities.Contact[] contacts = new entities.Contact[v.size()];
					for(int i=0;i<contacts.length;i++){
						contacts[i]=(entities.Contact)v.elementAt(i);
					}
					GetContactsCompletedArgs arg = new GetContactsCompletedArgs(true, contacts);
					fireGetPhoneContactsCompleted(arg);
				} catch (PIMException e) {
					// TODO Auto-generated catch block
					fireGetPhoneContactsCompleted(new GetContactsCompletedArgs(false,e.getMessage()));
					e.printStackTrace();
				}
			}
		};
		t.start();
	}

	public void addGetPhoneContactsCompletedListener(
			IGetContactsCompletedListener li) {
		// TODO Auto-generated method stub
		if (contactLi.contains(li))
			return;
		contactLi.addElement(li);
	}

	public void removeGetPhoneContactsCompletedListener(
			IGetContactsCompletedListener li) {
		// TODO Auto-generated method stub
		if (contactLi.contains(li))
			contactLi.removeElement(li);
	}

	protected void fireGetPhoneContactsCompleted(GetContactsCompletedArgs arg) {
		int length = contactLi.size();
		for (int i = 0; i < length; i++) {
			((IGetContactsCompletedListener) contactLi.elementAt(i))
					.onGetContactsCompleted(this, arg);
		}
	}

	public entities.Contact convertToEntities(
			javax.microedition.pim.Contact contact) {
		if (contact == null) {
			return null;
		}

		String contactName = null;

		// First, see if there is a meaningful name set for the contact.
		if (contact.countValues(Contact.NAME) > 0) {
			final String[] name = contact.getStringArray(Contact.NAME, 0);
			final String firstName = name[Contact.NAME_GIVEN];
			final String lastName = name[Contact.NAME_FAMILY];
			if (firstName != null && lastName != null) {
				contactName = firstName + " " + lastName;
			} else if (firstName != null) {
				contactName = firstName;
			} else if (lastName != null) {
				contactName = lastName;
			}

			if (contactName != null) {
				final String namePrefix = name[Contact.NAME_PREFIX];
				if (namePrefix != null) {
					contactName = namePrefix + " " + contactName;
				}
			}
		}

		// If not, use the company name.
		if (contact.countValues(Contact.ORG) > 0 && contactName == null) {
			final String companyName = contact.getString(Contact.ORG, 0);
			if (companyName != null) {
				contactName = companyName;
			}
		}
		if(contactName==null)
			contactName="";
		String email = "";
		try{
			email=contact.getString(Contact.EMAIL, 0);
		}catch(Exception e){
			email="";
		}
		String phone="";
		try{
			phone=contact.getString(Contact.TEL, 0);
		}catch(Exception e){
			phone="";
		}
		if(email.length()==0&&phone.length()==0)
			return null;
		return new entities.Contact("",contactName,email,phone,phone,"0");
	}
}
