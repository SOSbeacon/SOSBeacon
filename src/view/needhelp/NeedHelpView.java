package view.needhelp;

import icontroller.needhelp.INeedHelpController;
import iview.needhelp.INeedHelpView;
import model.checkin.CheckinModel;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import util.Util;
import view.AView;
import view.custom.Header;
import view.custom.ImageButtonField;
import view.custom.MessageBox;
import view.custom.ProgressPopup;
import bitmaps.NeedHelpViewBitmaps;
import controller.needhelp.NeedHelpController;

public class NeedHelpView extends AView implements INeedHelpView{

	/**
	 * 
	 */

	ImageButtonField btnCall,btnSend,btnCancel;
	INeedHelpController controller;
	int deviceWidth=Display.getWidth();
	final int SPACE=15;
	public NeedHelpView() {
		super();
		controller=new NeedHelpController(this,new CheckinModel());
		Font font=Util.getFont(7,Font.BOLD);
		VerticalFieldManager mngContent=new VerticalFieldManager(Manager.NO_VERTICAL_SCROLL|Manager.NO_VERTICAL_SCROLLBAR|Field.USE_ALL_HEIGHT|Field.USE_ALL_WIDTH);
		mngContent.setBackground(BackgroundFactory.createBitmapBackground(NeedHelpViewBitmaps.getNeedHelpViewBg()));
		if(font==null){
			btnCall=new ImageButtonField("Call Emergency Phone", NeedHelpViewBitmaps.getBtnCallEmergencyBg(),NeedHelpViewBitmaps.getBtnFocusBg());
			btnSend=new ImageButtonField("Send Alert", NeedHelpViewBitmaps.getBtnSendAlertBg(),NeedHelpViewBitmaps.getBtnFocusBg());
			btnCancel=new ImageButtonField("Cancel", NeedHelpViewBitmaps.getBtnCancelBg(),NeedHelpViewBitmaps.getBtnFocusBg());
		}else{
			btnCall=new ImageButtonField("Call Emergency Phone", NeedHelpViewBitmaps.getBtnCallEmergencyBg(),NeedHelpViewBitmaps.getBtnFocusBg(),font,Color.WHITE,0);
			btnSend=new ImageButtonField("Send Alert", NeedHelpViewBitmaps.getBtnSendAlertBg(),NeedHelpViewBitmaps.getBtnFocusBg(),font,Color.BLACK,0);
			btnCancel=new ImageButtonField("Cancel", NeedHelpViewBitmaps.getBtnCancelBg(),NeedHelpViewBitmaps.getBtnFocusBg(),font,Color.BLACK,0);
		}
		
		btnSend.setMargin(SPACE, 0, SPACE, 0);
		VerticalFieldManager mngButton = new VerticalFieldManager(Field.USE_ALL_WIDTH);
		mngButton.add(btnCall);
		mngButton.add(btnSend);
		mngButton.add(btnCancel);
		int max =Math.max(btnCall.getPreferredWidth(), btnSend.getPreferredWidth());
		max=Math.max(max, btnCancel.getPreferredWidth());
		int marginX=(deviceWidth-max)/2;
		int height = btnCall.getPreferredHeight()+btnSend.getPreferredHeight()+btnCancel.getPreferredHeight()+2*SPACE;
		int marginY=(Display.getHeight()-height)/2;
		mngButton.setMargin(marginY,marginX,0,marginX);
		mngContent.add(mngButton);
		btnCall.setChangeListener(new FieldChangeListener() {
			
			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.callEmergency();
			}
		});
		btnSend.setChangeListener(new FieldChangeListener() {
			
			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.sendAlert();
			}
		});
		btnCancel.setChangeListener(new FieldChangeListener() {
			
			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.cancel();
			}
		});
		add(new Header());
		add(mngContent);
	}
	public void showMessage(String caption,String message,Thread thread) {
		// TODO Auto-generated method stub
		int result = MessageBox.show(caption,message,MessageBox.D_CALL_CANCEL,thread);
		if(result==MessageBox.CALL){
			controller.callNow();
		}else if(result==MessageBox.CANCEL){
			controller.cancelCall();
		}else if(result==MessageBox.NONE){
			controller.callNow();
		}
	}
	public void updateMessage(String message) {
		// TODO Auto-generated method stub
		MessageBox.setMessage(message);
	}
	public void hideMessage() {
		// TODO Auto-generated method stub
		MessageBox.hide();
	}
	 
	protected void onDisplay() {
		// TODO Auto-generated method stub
		super.onDisplay();
		controller.initialize();
	}
	public void showProgress(String message) {
		// TODO Auto-generated method stub
		ProgressPopup.show(message);
	}
	public void hideProgress() {
		// TODO Auto-generated method stub
		ProgressPopup.hide();
	}
	public void showMessage(String caption, String message) {
		// TODO Auto-generated method stub
		MessageBox.show(caption, message, MessageBox.D_OK);
	}
}
