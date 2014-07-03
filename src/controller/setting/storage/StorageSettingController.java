package controller.setting.storage;

import icontroller.setting.storage.IStorageSettingController;
import imodel.setting.storage.IStorageSettingModel;
import iview.setting.storage.IStorageSettingView;
import util.Navigation;
import util.Session;
import view.HomeView;

public class StorageSettingController implements IStorageSettingController {

	private IStorageSettingView view;
	private IStorageSettingModel model;

	public StorageSettingController(IStorageSettingView view, IStorageSettingModel model){
		this.view=view;
		this.model=model;
	}
	public void initialize() {
		// TODO Auto-generated method stub

	}

	public void clear() {
		// TODO Auto-generated method stub
		view.showClearConfirm();
	}

	public void reset() {
		// TODO Auto-generated method stub
		view.showResetConfirm();
	}
	public void clearRecordings() {
		// TODO Auto-generated method stub
		//clear all recordings
		view.showClearSucceed();
	}
	public void resetAll() {
		// TODO Auto-generated method stub
		Session.clearToken();
		Session.clearUser();
		Session.clearMessage();
		view.showResetSucceed();
	}
	public void clearSucceed() {
		// TODO Auto-generated method stub
	}
	public void resetSucceed() {
		// TODO Auto-generated method stub
		Navigation.navigate(new HomeView());
	}
	public void back() {
		// TODO Auto-generated method stub
		Navigation.goBack();
	}

}
