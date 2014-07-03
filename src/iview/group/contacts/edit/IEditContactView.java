package iview.group.contacts.edit;

public interface IEditContactView {

	void hideProgress();

	void showMessage(String string);

	void showProgress(String string);

	void showConfirm(String message);
	
}
