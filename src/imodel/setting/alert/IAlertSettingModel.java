package imodel.setting.alert;

public interface IAlertSettingModel {
	void addGetContactGroupsCompletedListener(IGetContactGroupsCompleted arg);
	void removeGetContactGroupsCompletedListener(IGetContactGroupsCompleted arg);
	void getContactGroups(String phoneId, String token);
	void saveAlert(String settingId,String phoneId,String token,String voiceDuration,String defaultGroup,String emergencyNumber,boolean receiveGovernmentAlert);
	void addSaveAlertCompletedListener(ISaveAlertCompleted arg);
	void removeSaveAlertCompletedListener(ISaveAlertCompleted arg);
	Object[] getVoiceDurations();
}
