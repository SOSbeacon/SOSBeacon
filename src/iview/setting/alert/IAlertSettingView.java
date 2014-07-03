package iview.setting.alert;

import entities.ContactGroup;

public interface IAlertSettingView {
	void showProgress(String message);
	void hideProgress();
	void showMessage(String message);
	void fillChoices(ContactGroup[] groups, int selectedIndex);
	void fillFields(String panicNumber, boolean receiveAlert);
	public void fillVoices(Object[] o, int selectedIndex);
}
