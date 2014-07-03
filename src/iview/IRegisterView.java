package iview;

import entities.User;

public interface IRegisterView {
	void showProgress(String message);
	void showErrorMessage(String message);
	void hideProgress();
	void fillFields(User u);
}
