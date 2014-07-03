package imodel.group.contacts.addnew;

import imodel.group.contacts.IGetContactsCompletedListener;

public interface IAddContactModel {
	
	void getPhoneContact();
	void addGetPhoneContactsCompletedListener(IGetContactsCompletedListener li);
	void removeGetPhoneContactsCompletedListener(IGetContactsCompletedListener li);
}
