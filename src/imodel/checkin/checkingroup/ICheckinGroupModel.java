package imodel.checkin.checkingroup;

import imodel.checkin.ICheckinModel;
import imodel.group.IGetContactGroupsCompletedListener;

public interface ICheckinGroupModel extends ICheckinModel {
	void getContactGroups(String token, String phoneId);
	void addGetContactGroupsCompletedListener(IGetContactGroupsCompletedListener arg);
	void removeGetContactGroupsCompletedListener(IGetContactGroupsCompletedListener arg);
	Object[] getChoices();
	void saveMessage(String message);
}
