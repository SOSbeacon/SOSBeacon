package iview.needhelp;

public interface INeedHelpView {
	void showMessage(String caption,String message,Thread thread);
	void showMessage(String caption,String message);
	void updateMessage(String message);
	void hideMessage();
	
	void showProgress(String message);
	void hideProgress();
}
