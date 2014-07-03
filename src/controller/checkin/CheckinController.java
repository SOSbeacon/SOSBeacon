package controller.checkin;

import icontroller.checkin.ICheckinController;
import imodel.checkin.AlertCompletedArgs;
import imodel.checkin.IAlertCompleted;
import imodel.checkin.ICheckinModel;
import imodel.checkin.ISendDataCompleted;
import imodel.checkin.SendDataCompletedArgs;
import iview.checkin.ICheckinView;
import net.rim.device.api.ui.UiApplication;
import util.Geo;
import util.GeoArgs;
import util.IGetLocationCompleted;
import util.Navigation;
import util.Session;
import view.checkin.checkingroup.CheckinGroupView;
import view.custom.MessageBox;
import constants.Values;
import entities.Location;
import entities.Setting;

public class CheckinController implements ICheckinController {
	
	ICheckinView view;
	ICheckinModel model;
	public CheckinController(ICheckinView view, ICheckinModel model) {
		// TODO Auto-generated constructor stub
		this.view=view;
		this.model=model;
	}
	
	IAlertCompleted alertCompleted;
	ISendDataCompleted uploadCompleted;
	//Bitmap bm;
	//EncodedImage jpeg;
	//byte[] data;
	public void initialize() {
//		bm=Bitmap.getBitmapResource("red_big.png");
//		jpeg=PNGEncodedImage.encode(bm);
		//data=jpeg.getData();
		// TODO Auto-generated method stub
		if(alertCompleted==null){
			alertCompleted=new IAlertCompleted() {
				
				public void onAlertCompleted(Object sender, AlertCompletedArgs arg) {
					// TODO Auto-generated method stub
					view.hideProgress();
					if(arg.isSuccess()){
						final String message = "Check-in sent successfully";
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								view.showMessage(message);
							}
						});	
						//view.showProgress();
						//model.uploadData(Session.getToken(), arg.getAlertId(), s.getPhoneId() ,"0", data);
					}else{
						final String message = arg.getMessage();
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								view.showMessage(message);
							}
						});
					}
				}
			};
			uploadCompleted=new ISendDataCompleted() {
				
				public void onSendDataCompleted(Object sender, SendDataCompletedArgs arg) {
					// TODO Auto-generated method stub
					view.hideProgress();
					if(arg.isSuccess()){
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								view.showMessage("Alert success");
							}
						});
					}else{
						final String message = arg.getMessage();
						UiApplication.getUiApplication().invokeLater(new Runnable(){
							public void run(){
								view.showMessage(message);
							}
						});
					}
				}
			};
			model.addAlertCompleted(alertCompleted);
			model.addSendDataCompleted(uploadCompleted);
		}
	}
	Setting s;
	public void checkinImOK() {
		// TODO Auto-generated method stub
		s=Session.getSetting();
		if(s!=null){
			view.showProgress();
			Geo g = new Geo();
			g.addGetLocationCompleted(new IGetLocationCompleted() {
				
				public void onGetLocationCompleted(GeoArgs arg) {
					// TODO Auto-generated method stub
					if(arg.isSuccess()){
						Location loc = arg.getLocation();
						model.startAlert(Session.getToken(), s.getPhoneId(), loc.getLongitude(), loc.getLatitude(), Values.CHECKIN_TYPE, Values.FAMILY_GROUP, "", Values.IM_OK_MESSAGE);
					}else{
						view.hideProgress();
						final String message = arg.getMessage();
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								view.showMessage(message);
							}
						});
					}
				}
			});	
			g.getCurrentLocation();
		}
	}

	public void checkinGroup() {
		// TODO Auto-generated method stub
		Navigation.navigate(new CheckinGroupView());
	}

	public void cancel() {
		// TODO Auto-generated method stub
		Navigation.goBack();
	}

}
