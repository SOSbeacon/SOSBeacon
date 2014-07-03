package iview;

public interface IMainView {
	//void showLoginStatus();
	//void hideLoginStatus();
	void showActiveConfirm(String caption, String message);
	//void showActiveStatus();
	//void updateActiveStatus(int time);
	//void hideActiveStatus();
	void showReceiveSmsConfirm(String caption, String message);
	void showErrorMessage(String message);
	void loginSucceed();
	void showEmergencyNotSetConfirm(String message);
	
	
	void showProgress(String msg);
	void updateProgressMessage(String msg);
	void hideProgress();
	void showMessage(String msg);
}
