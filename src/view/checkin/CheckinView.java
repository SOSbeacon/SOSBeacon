package view.checkin;

import icontroller.checkin.ICheckinController;
import iview.checkin.ICheckinView;
import model.checkin.CheckinModel;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import view.AView;
import view.custom.Header;
import view.custom.ImageButtonField;
import view.custom.MessageBox;
import view.custom.ProgressPopup;
import bitmaps.CheckinViewBitmaps;
import controller.checkin.CheckinController;

public class CheckinView extends AView implements ICheckinView {
	ICheckinController controller;
	ImageButtonField btnImOK,btnCheckinGroup,btnCancel;
	int deviceWidth=Display.getWidth();
	final int SPACE=15;
	public CheckinView(){
		super();
		controller=new CheckinController(this,new CheckinModel());
		Font font;
		try {
			FontFamily family = FontFamily.forName("BBAlpha Sans");
			font = family.getFont(Font.BOLD, 7, Ui.UNITS_pt);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			font=null;
		}
		
		VerticalFieldManager mngContent=new VerticalFieldManager(Manager.NO_VERTICAL_SCROLL|Manager.NO_VERTICAL_SCROLLBAR|Field.USE_ALL_HEIGHT|Field.USE_ALL_WIDTH);
		mngContent.setBackground(BackgroundFactory.createBitmapBackground(CheckinViewBitmaps.getCheckinViewBg()));
		if(font==null){
			btnImOK=new ImageButtonField("Check-in I'm OK", CheckinViewBitmaps.getBtnCheckinImOKBg(),CheckinViewBitmaps.getBtnFocusBg());
			btnCheckinGroup=new ImageButtonField("Check-in Group Message", CheckinViewBitmaps.getBtnCheckinGroupBg(),CheckinViewBitmaps.getBtnFocusBg());
			btnCancel=new ImageButtonField("Cancel", CheckinViewBitmaps.getBtnCancelBg(),CheckinViewBitmaps.getBtnFocusBg());
		}else{
			btnImOK=new ImageButtonField("Check-in I'm OK", CheckinViewBitmaps.getBtnCheckinImOKBg(),CheckinViewBitmaps.getBtnFocusBg(),font,Color.BLACK,0);
			btnCheckinGroup=new ImageButtonField("Check-in Group Message", CheckinViewBitmaps.getBtnCheckinGroupBg(),CheckinViewBitmaps.getBtnFocusBg(),font,Color.BLACK,0);
			btnCancel=new ImageButtonField("Cancel", CheckinViewBitmaps.getBtnCancelBg(),CheckinViewBitmaps.getBtnFocusBg(),font,Color.BLACK,0);
		}
		btnCheckinGroup.setMargin(SPACE, 0, SPACE, 0);
		
		VerticalFieldManager mngButton = new VerticalFieldManager(Field.USE_ALL_WIDTH);
		mngButton.add(btnImOK);
		mngButton.add(btnCheckinGroup);
		mngButton.add(btnCancel);
		int max =Math.max(btnImOK.getPreferredWidth(), btnCheckinGroup.getPreferredWidth());
		max=Math.max(max, btnCancel.getPreferredWidth());
		int marginX=(deviceWidth-max)/2;
		int height = btnImOK.getPreferredHeight()+btnCheckinGroup.getPreferredHeight()+btnCancel.getPreferredHeight()+2*SPACE;
		int marginY=(Display.getHeight()-height)/2;
		mngButton.setMargin(marginY,marginX,0,marginX);
		mngContent.add(mngButton);
		btnImOK.setChangeListener(new FieldChangeListener() {
			
			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.checkinImOK();
			}
		});
		btnCheckinGroup.setChangeListener(new FieldChangeListener() {
			
			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.checkinGroup();
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
	public void showProgress() {
		// TODO Auto-generated method stub
		ProgressPopup.show("Checking in...");
	}
	public void hideProgress() {
		// TODO Auto-generated method stub
		ProgressPopup.hide();
	}
	public void showMessage(String message) {
		// TODO Auto-generated method stub
		MessageBox.show(message);
	}
	protected void onDisplay() {
		// TODO Auto-generated method stub
		super.onDisplay();
		controller.initialize();
	}
}
