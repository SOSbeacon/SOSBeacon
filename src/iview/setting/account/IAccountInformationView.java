package iview.setting.account;

import entities.User;

public interface IAccountInformationView {
	void showProgress(String message);
	void showMessage(String message);
	void hideProgress();
	void fillFields(User u);
}
