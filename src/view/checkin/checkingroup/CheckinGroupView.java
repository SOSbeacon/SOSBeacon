package view.checkin.checkingroup;

import icontroller.checkin.checkingroup.ICheckinGroupController;
import iview.checkin.checkingroup.ICheckinGroupView;
import model.checkin.checkingroup.CheckinGroupModel;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import util.Util;
import view.AView;
import view.custom.Header;
import view.custom.ImageButtonField;
import view.custom.MessageBox;
import view.custom.MultiLineTextField;
import view.custom.ProgressPopup;
import bitmaps.HeaderBitmaps;
import controller.checkin.checkingroup.CheckinGroupController;
import entities.ContactGroup;

public class CheckinGroupView extends AView implements ICheckinGroupView {
	ICheckinGroupController controller;
	ObjectChoiceField cmbGroup;
	ObjectChoiceField cmbMessage;
	MultiLineTextField txtMessage;
	VerticalFieldManager mngWrapper,mngText;
	public CheckinGroupView(){
		controller = new CheckinGroupController(this, new CheckinGroupModel());
		Header header = new Header("Checking In");
		add(header);
		ImageButtonField btnBack = new ImageButtonField("Back", HeaderBitmaps.getButtonBg(),HeaderBitmaps.getButtonFocusBg(),Util.getFont(6,Font.BOLD),Color.WHITE,0);
		btnBack.setMargin(
				(header.getPreferredHeight() - btnBack.getPreferredHeight()) / 2,
				0, 0, 10);
		btnBack.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.back();
			}
		});
		header.add(btnBack);
		mngWrapper = new VerticalFieldManager(Manager.USE_ALL_HEIGHT|Manager.USE_ALL_WIDTH);
		mngWrapper.setPadding(0, 5, 0, 5);
		mngWrapper.setFont(Util.getFont(7));
		Bitmap bg = Bitmap.getBitmapResource("settings_hor.png");
		bg = Util.resizeBitmap(bg,Display.getWidth() , Display.getHeight()-Header.getHeaderHeight());
		mngWrapper.setBackground(BackgroundFactory.createBitmapBackground(bg));
		add(mngWrapper);
		cmbGroup = new ObjectChoiceField("To: ", null){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		mngWrapper.add(cmbGroup);
		cmbMessage = new ObjectChoiceField("Message:",null){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		mngWrapper.add(cmbMessage);
		cmbMessage.setChangeListener(new FieldChangeListener() {
			
			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.changeMessage(cmbMessage.getSelectedIndex());
			}
		});
		txtMessage = new MultiLineTextField(Display.getWidth()-10, 100);
		//mngWrapper.add(txtMessage);
		mngText = new VerticalFieldManager();
		mngWrapper.add(mngText);
		final int SPACE=10;
		int width = (Display.getWidth()-mngWrapper.getPaddingLeft()-mngWrapper.getPaddingRight())/2-10;
		int height = 70;
		Bitmap btnBg = Bitmap.getBitmapResource("msg_btn.png");
		btnBg = Util.resizeBitmap(btnBg, width, height);
		Bitmap btnBgFocus = Bitmap.getBitmapResource("msg_btn_focus.png");
		btnBgFocus = Util.resizeBitmap(btnBgFocus, width, height);
		ImageButtonField btnSubmit = new ImageButtonField("Checkin now", btnBg,btnBgFocus);
		ImageButtonField btnCapture = new ImageButtonField("Record", btnBg,btnBgFocus);
		HorizontalFieldManager mngButton = new HorizontalFieldManager();
		btnSubmit.setMargin(0, 0, 0, SPACE);
		mngWrapper.add(mngButton);
		mngButton.add(btnCapture);
		mngButton.add(btnSubmit);
		btnSubmit.setChangeListener(new FieldChangeListener() {
			
			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.submit(cmbGroup.getSelectedIndex(),cmbMessage.getSelectedIndex(),txtMessage.getText());
			}
		});
		btnCapture.setChangeListener(new FieldChangeListener() {
			
			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.record();
			}
		});
	}
	protected void onUiEngineAttached(boolean attached) {
		// TODO Auto-generated method stub
		super.onUiEngineAttached(attached);
		if(attached){
			controller.initialize();
		}
	}
	public void fillGroups(ContactGroup[] groups) {
		// TODO Auto-generated method stub
		cmbGroup.setChoices(groups);
	}
	public void hideProgress() {
		// TODO Auto-generated method stub
		ProgressPopup.hide();
	}
	public void showProgress(String msg) {
		// TODO Auto-generated method stub
		ProgressPopup.show(msg);
	}
	public void showMessage(String message) {
		// TODO Auto-generated method stub
		MessageBox.show(message);
	}
	public void fillChoices(Object[] choices) {
		// TODO Auto-generated method stub
		cmbMessage.setChoices(choices);
	}
	boolean visible = false;
	public void showTextBox() {
		// TODO Auto-generated method stub
		if(!visible){
			mngText.add(txtMessage);
			visible=!visible;
		}
	}
	public void hideTextBox() {
		// TODO Auto-generated method stub
		if(visible){
			mngText.delete(txtMessage);
			visible=!visible;
		}
	}
	public void updateProgress(String msg) {
		// TODO Auto-generated method stub
		ProgressPopup.setMessage(msg);
	}
	protected boolean keyChar(char c, int status, int time) {
		// TODO Auto-generated method stub
		if(c==Characters.ESCAPE){
			controller.back();
			return true;
		}
		return super.keyChar(c, status, time);
	}
}
