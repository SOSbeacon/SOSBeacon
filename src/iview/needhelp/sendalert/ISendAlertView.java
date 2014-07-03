package iview.needhelp.sendalert;

public interface ISendAlertView {

	void showMessage(String message);

	void hideProgress();

	void showProgress(String message);

	void updateProgress(String message);

}
