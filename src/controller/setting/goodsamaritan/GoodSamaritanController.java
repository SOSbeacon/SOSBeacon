package controller.setting.goodsamaritan;

import net.rim.device.api.ui.UiApplication;
import icontroller.setting.goodsamaritan.IGoodSamaritanController;
import imodel.setting.goodsamaritan.IGoodSamaritanModel;
import imodel.setting.goodsamaritan.ISaveSamaritanCompleted;
import imodel.setting.goodsamaritan.SaveSamaritanCompletedArgs;
import iview.setting.goodsamaritan.IGoodSamaritanView;
import util.Navigation;
import util.Session;
import entities.Distance;
import entities.Setting;

public class GoodSamaritanController implements IGoodSamaritanController {

	IGoodSamaritanView view;
	IGoodSamaritanModel model;

	public GoodSamaritanController(IGoodSamaritanView view,
			IGoodSamaritanModel model) {
		this.view = view;
		this.model = model;
	}

	ISaveSamaritanCompleted saveSamaritan;

	public void initialize() {
		// TODO Auto-generated method stub
		if (saveSamaritan == null) {
			saveSamaritan = new ISaveSamaritanCompleted() {

				public void onSaveSamaritanCompleted(Object sender,
						SaveSamaritanCompletedArgs arg) {
					view.hideProgress();
					// TODO Auto-generated method stub
					if (arg.isSuccess()) {
						Session.setSetting(s);
						UiApplication.getUiApplication().invokeLater(
								new Runnable() {

									public void run() {
										// TODO Auto-generated method stub
										view.showMessage("Save successfull!");
									}
								});
					} else {
						final String message = arg.getMessage();
						UiApplication.getUiApplication().invokeLater(
								new Runnable() {

									public void run() {
										// TODO Auto-generated method stub
										view.showMessage(message);
									}
								});
					}
				}
			};
			model.addSaveSamaritanCompletedListener(saveSamaritan);
		}
		ranges = model.getRanges();
		s = Session.getSetting();
		boolean alert = s.getGoodSamaritanStatus().equalsIgnoreCase("1");
		boolean nearby = s.getPanicAlertGoodSamaritanStatus().equalsIgnoreCase(
				"1");
		int alertIndex = -1, nearbyIndex = -1;
		for (int i = 0; i < ranges.length; i++) {
			if (s.getGoodSamaritanRange().equalsIgnoreCase(
					String.valueOf(ranges[i].getValue())))
				alertIndex = i;
			if (s.getPanicAlertGoodSamaritanRange().equalsIgnoreCase(
					String.valueOf(ranges[i].getValue())))
				nearbyIndex = i;
		}
		synchronized(UiApplication.getEventLock()){
			view.fillFields(alert, ranges, alertIndex, nearby, ranges, nearbyIndex);
		}
	}

	Setting s;

	public void cancel() {
		// TODO Auto-generated method stub
		Navigation.goBack();
	}

	Distance[] ranges;

	public void save(boolean alert, int maxAlertIndex, boolean nearby,
			int maxNearbyIndex) {
		// TODO Auto-generated method stub
		view.showProgress();
		s.setGoodSamaritanStatus(alert ? "1" : "0");
		s.setGoodSamaritanRange(String.valueOf(ranges[maxAlertIndex].getValue()));
		s.setPanicAlertGoodSamaritanStatus(nearby ? "1" : "0");
		s.setPanicAlertGoodSamaritanRange(String.valueOf(ranges[maxNearbyIndex]
				.getValue()));
		model.saveSamaritan(s.getSettingId(), s.getPhoneId(),
				Session.getToken(), alert, ranges[maxAlertIndex].getValue(),
				nearby, ranges[maxNearbyIndex].getValue());
	}

}
