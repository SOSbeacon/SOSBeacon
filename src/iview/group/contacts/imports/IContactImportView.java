package iview.group.contacts.imports;

public interface IContactImportView {
	void showConfirm(String caption,String message);
	void showMessage(String message);
	void showProgress(String message);
	void hideProgress();
	void updateStatus(String status);
}
