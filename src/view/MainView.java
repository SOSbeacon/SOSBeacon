package view;

import icontroller.IMainController;
import iview.IMainView;
import model.MainModel;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import util.MenuUtil;
import util.Navigation;
import view.custom.Header;
import view.custom.ImageButtonField;
import view.custom.MessageBox;
import view.custom.ProgressPopup;
import bitmaps.MainViewBitmaps;
import controller.MainController;

public class MainView extends AView implements IMainView {

	/**
	 * 
	 */
	IMainController controller;
	ImageButtonField btnImOK;
	ImageButtonField btnNeedHelp;
	VerticalFieldManager mngContent;
	public MainView() {
		super();
		mngContent=new VerticalFieldManager(Manager.NO_VERTICAL_SCROLL|NO_VERTICAL_SCROLLBAR|Field.USE_ALL_HEIGHT|Field.USE_ALL_WIDTH);
		mngContent.setBackground(BackgroundFactory.createBitmapBackground(MainViewBitmaps.getMainViewBg()));	
		controller=new MainController(this,new MainModel());
		// TODO Auto-generated constructor stub
		//subManager.add(new BitmapField(Bitmap.getBitmapResource("red_big.png")));
		Header head=new Header();
		add(head);
		add(mngContent);
//		EncodedImage i=EncodedImage.getEncodedImageResource("gray_big.png");
//		EncodedImage scaled=i.scaleImage32(Fixed32.tenThouToFP(8000),Fixed32.tenThouToFP(9500));
//		BitmapField f=new BitmapField(scaled.getBitmap());
//		subManager.add(f);
	}
	protected void onDisplay() {
		// TODO Auto-generated method stub
		super.onDisplay();
		controller.initialize();
	}

	public void showErrorMessage(String message) {
		// TODO Auto-generated method stub
		MessageBox.show(message);
		controller.notReceivedSms();
	}
	
	public void loginSucceed(){
		synchronized(Application.getEventLock()){
			Font font;
			try {
				FontFamily family = FontFamily.forName("BBAlpha Sans");
				font = family.getFont(Font.BOLD, 9, Ui.UNITS_pt);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				font=null;
			}
			VerticalFieldManager mngButton=new VerticalFieldManager();
			if(font==null){
				btnImOK=new ImageButtonField("I'm OK", MainViewBitmaps.getBtnImOKBg(),MainViewBitmaps.getBtnFocusBg());
				btnNeedHelp=new ImageButtonField("NEED HELP", MainViewBitmaps.getBtnNeedHelpBg(),MainViewBitmaps.getBtnFocusBg());
			}else{
				btnImOK=new ImageButtonField("I'm OK", MainViewBitmaps.getBtnImOKBg(),MainViewBitmaps.getBtnFocusBg(),font,Color.BLACK,0);
				btnNeedHelp=new ImageButtonField("NEED HELP", MainViewBitmaps.getBtnNeedHelpBg(),MainViewBitmaps.getBtnFocusBg(),font,Color.WHITE,0);
			}
			int marginX=(mngContent.getWidth()-btnImOK.getPreferredWidth())/2;
			btnImOK.setChangeListener(new FieldChangeListener() {
				
				public void fieldChanged(Field field, int context) {
					// TODO Auto-generated method stub
					controller.imokClick();
				}
			});
			
			btnNeedHelp.setChangeListener(new FieldChangeListener() {
				
				public void fieldChanged(Field field, int context) {
					// TODO Auto-generated method stub
					controller.needHelpclick();
				}
			});
			final int SPACE = 20;
			btnNeedHelp.setMargin(SPACE,0,0,0);
			marginX=Math.min(marginX,(mngContent.getWidth()-btnNeedHelp.getPreferredWidth())/2);
			int height = btnNeedHelp.getPreferredHeight()+btnImOK.getPreferredHeight()+SPACE;
			int marginY= (Display.getHeight()-height)/2;
			mngButton.setMargin(marginY, marginX, 0, marginX);
			mngButton.add(btnImOK);
			mngButton.add(btnNeedHelp);
			mngContent.add(mngButton);
		}
	}

	protected void makeMenu(Menu menu, int instance) {
		// TODO Auto-generated method stub
		MenuUtil.attachMenu(this);
		super.makeMenu(menu, instance);
	}
	public void close() {
		// TODO Auto-generated method stub
		if(MessageBox.show("Confirm","Are you sure to exit?",MessageBox.D_YES_NO)==MessageBox.YES){
			Navigation.exit();
		}
	}

	public void showReceiveSmsConfirm(String caption, String message) {
		// TODO Auto-generated method stub
		int result = MessageBox.show(caption,message,MessageBox.D_YES_NO);
		if(result==MessageBox.YES){
			controller.receivedSms();
		}else if(result==MessageBox.NO){
			controller.notReceivedSms();
		}
	}

	public void showProgress(String msg) {
		// TODO Auto-generated method stub
		ProgressPopup.show(msg);
	}

	public void updateProgressMessage(String msg) {
		// TODO Auto-generated method stub
		ProgressPopup.setMessage(msg);
	}

	public void hideProgress() {
		// TODO Auto-generated method stub
		ProgressPopup.hide();
	}

	public void showMessage(String msg) {
		// TODO Auto-generated method stub
		MessageBox.show(msg);
	}

	public void showActiveConfirm(String caption, String message) {
		// TODO Auto-generated method stub
		if(MessageBox.show(caption,message,MessageBox.D_OK)==MessageBox.OK){
			controller.waitForActive();
		}
	}
	public void showEmergencyNotSetConfirm(String message) {
		// TODO Auto-generated method stub
		MessageBox.show(message);
	}
}
