package view;

import icontroller.IRegisterController;
import icontroller.IRegisterController2;
import iview.IRegisterView2;
import model.RegisterModel2;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import util.Util;
import view.custom.FCLabelField;
import view.custom.GridManager;
import view.custom.Header;
import view.custom.ImageButtonField;
import view.custom.MessageBox;
import view.custom.OneLineTextField;
import view.custom.ProgressPopup;
import bitmaps.RegisterViewBitmaps;
import controller.RegisterController2;
import entities.User;

public class RegisterView2 extends AView implements IRegisterView2{

	/**
	 * 
	 */
	public static final int MARGIN=7;
	OneLineTextField txtPhone;
	ImageButtonField btnSubmit,btnCancel;
	IRegisterController2 controller;
	Manager mngWrapper;
	public RegisterView2() {
		super();
		// TODO Auto-generated constructor stub
		controller=new RegisterController2(this,new RegisterModel2());
		
		HorizontalFieldManager mngHeader=new HorizontalFieldManager(Manager.NO_HORIZONTAL_SCROLL|Manager.NO_HORIZONTAL_SCROLLBAR);
		Header header = new Header();
		mngHeader.add(header);
		
		add(mngHeader);
		
		VerticalFieldManager mngContent=new VerticalFieldManager(Manager.NO_VERTICAL_SCROLL|Manager.NO_VERTICAL_SCROLLBAR|Field.FIELD_VCENTER|Field.USE_ALL_HEIGHT|Field.USE_ALL_WIDTH);
		mngContent.setBackground(BackgroundFactory.createBitmapBackground(RegisterViewBitmaps.getRegisterViewBg()));
		mngWrapper = new GridManager(2,Field.USE_ALL_WIDTH|Field.FIELD_VCENTER);
		//mngWrapper.setBackground(BackgroundFactory.createSolidBackground(Color.GRAY));
		FCLabelField lblPhone=new FCLabelField(Color.GRAY,"Phone:     ");
		txtPhone=new OneLineTextField("",100,0);
		mngWrapper.add(lblPhone);
		mngWrapper.add(txtPhone);
		add(mngContent);
		mngContent.setPadding(0, 0, 0, (Display.getWidth()-txtPhone.getPreferredWidth()-getFont().getAdvance(lblPhone.getText()))/2);
		mngContent.add(mngWrapper);
		HorizontalFieldManager mngButton = new HorizontalFieldManager(Field.USE_ALL_WIDTH);
		btnSubmit = new ImageButtonField("Submit", RegisterViewBitmaps.getButtonBg(), RegisterViewBitmaps.getButtonFocusbg(),Util.getFont(7),Color.BLACK,0);
		btnSubmit.setMargin(0, MARGIN, 0, 0);
		btnCancel = new ImageButtonField("Cancel", RegisterViewBitmaps.getButtonBg(), RegisterViewBitmaps.getButtonFocusbg(),Util.getFont(7),Color.BLACK,0);
		mngButton.add(btnSubmit);
		mngButton.add(btnCancel);
		mngButton.setMargin(10, 0, 0, 0);
		btnSubmit.setChangeListener(new FieldChangeListener() {
			
			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				//controller.submitClick(txtUsername.getText(), txtEmail.getText(), txtPassword.getText(), txtPhoneNumber.getText());
				controller.submitClick(txtPhone.getText());
			}
		});
		btnCancel.setChangeListener(new FieldChangeListener() {
			
			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.cancelClick();
			}
		});
		mngWrapper.add(new FCLabelField("", Field.FIELD_RIGHT));
		mngWrapper.add(mngButton);
		mngWrapper.setMargin(160, 0, 0, 0);
	}
	public void showProgress(String message) {
		// TODO Auto-generated method stub
		ProgressPopup.show(message);
	}
	public void showMessage(String message) {
		MessageBox.show(message);
	}
	public void hideProgress() {
		// TODO Auto-generated method stub
		ProgressPopup.hide();
	}
	protected void onDisplay() {
		// TODO Auto-generated method stub
		super.onDisplay();
		controller.initialize();
	}
}
