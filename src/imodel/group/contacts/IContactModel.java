package imodel.group.contacts;

import imodel.group.contacts.addnew.IAddContactCompletedListener;
import imodel.group.contacts.edit.IEditContactCompletedListener;


public interface IContactModel{

	void getContacts(String token, String id);
	void addGetContactsCompletedListener(IGetContactsCompletedListener li);
	void removeGetContactsCompletedListener(IGetContactsCompletedListener li);
	
	void delete(String token, String contactId, String groupId, String name, String email,
			String voicePhone, String textPhone, String type);
	void addDeleteContactsCompletedListener(IDeleteContactCompleted li);
	void removeDeleteContactsCompletedListener(IDeleteContactCompleted li);
	
	
	void addEditContactCompletedListener(IEditContactCompletedListener li);
	void removeEditContactCompletedListener(IEditContactCompletedListener li);
	void save(String token, String contactId, String groupId, String name, String email, String voicephone, String textphone, String type);

	void addAddContactCompletedListener(IAddContactCompletedListener li);
	void removeAddContactCompletedListener(IAddContactCompletedListener li);
	void addContact(String token, String groupId,String name, String email, String voicephone, String textphone, String type);
}
