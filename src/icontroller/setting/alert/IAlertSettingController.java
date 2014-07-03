package icontroller.setting.alert;

import icontroller.IController;

public interface IAlertSettingController extends IController {

	void save(int selectedVoiceIndex, int selectedGroupIndex, String emergencyPhone, boolean checked);

	void cancel();

}
