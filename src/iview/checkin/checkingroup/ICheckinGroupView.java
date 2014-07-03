package iview.checkin.checkingroup;

import entities.ContactGroup;

public interface ICheckinGroupView {

	void fillGroups(ContactGroup[] groups);

	void hideProgress();

	void showProgress(String string);

	void showMessage(String message);

	void fillChoices(Object[] choices);

	void showTextBox();

	void hideTextBox();

	void updateProgress(String msg);

}
