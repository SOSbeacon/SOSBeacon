package imodel.group;

import imodel.group.contacts.IGetContactsCompletedListener;

public interface IGroupModel {

	void getContactGroups(String token, String phoneId);
	void addGetContactGroupsCompletedListener(IGetContactGroupsCompletedListener arg);
	void removeGetContactGroupsCompletedListener(IGetContactGroupsCompletedListener arg);
	void addGetContactsCompletedListener(
			IGetContactsCompletedListener getContact);
	void getContacts(String token, String id);
	void removeGetContactsCompletedListener(IGetContactsCompletedListener li);

}
