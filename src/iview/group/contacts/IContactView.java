package iview.group.contacts;

import entities.Contact;

public interface IContactView {
	void fillContacts(Contact[] contacts);

	void showProgress(String string);

	void hideProgress();

	void showMessage(String string);

	void showAddContactPopup();

	void hideAddContactPopup();

	void showConfirmSave(String message);
}
