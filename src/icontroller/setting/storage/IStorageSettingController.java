package icontroller.setting.storage;

import icontroller.IController;

public interface IStorageSettingController extends IController {
	void clear();
	void reset();
	void clearRecordings();
	void resetAll();
	void clearSucceed();
	void resetSucceed();
	void back();
}
