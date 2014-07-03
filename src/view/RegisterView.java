package view;

import icontroller.IRegisterController;
import iview.IRegisterView;
import model.RegisterModel;
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
import controller.RegisterController;
import entities.User;

public class RegisterView extends AView implements IRegisterView{

	/**
	 * 
	 */
	public static final int MARGIN=7;
	OneLineTextField txtUsername,txtEmail,txtPhoneNumber;
	OneLineTextField txtPassword;
	ImageButtonField btnSubmit,btnCancel;
	IRegisterController controller;
	Manager mngWrapper;
	public RegisterView() {
		super();
		// TODO Auto-generated constructor stub
		controller=new RegisterController(this,new RegisterModel());
		
		HorizontalFieldManager mngHeader=new HorizontalFieldManager(Manager.NO_HORIZONTAL_SCROLL|Manager.NO_HORIZONTAL_SCROLLBAR);
		Header header = new Header();
		mngHeader.add(header);
		
		add(mngHeader);
		
		VerticalFieldManager mngContent=new VerticalFieldManager(Manager.NO_VERTICAL_SCROLL|Manager.NO_VERTICAL_SCROLLBAR|Field.FIELD_VCENTER|Field.USE_ALL_HEIGHT|Field.USE_ALL_WIDTH);
		mngContent.setBackground(BackgroundFactory.createBitmapBackground(RegisterViewBitmaps.getRegisterViewBg()));
		mngWrapper = new GridManager(2,Field.USE_ALL_WIDTH|Field.FIELD_VCENTER);
		//mngWrapper.setBackground(BackgroundFactory.createSolidBackground(Color.GRAY));
		FCLabelField lblUserName=new FCLabelField(Color.GRAY,"User Name:     ");
		FCLabelField lblEmail=new FCLabelField(Color.GRAY,"Email:");
		FCLabelField lblPassword=new FCLabelField(Color.GRAY,"Password:");
		FCLabelField lblPhone=new FCLabelField(Color.GRAY,"Phone #:");
		txtUsername=new OneLineTextField("",100,0);
		txtEmail=new OneLineTextField("",100,0);
		txtEmail.setMargin(MARGIN, 0, MARGIN, 0);
		txtPhoneNumber=new OneLineTextField("",100,0);
		txtPhoneNumber.setMargin(MARGIN, 0, MARGIN, 0);
		txtPassword=new OneLineTextField(true,"",100,0);
		mngWrapper.add(lblUserName);
		mngWrapper.add(txtUsername);
		mngWrapper.add(lblEmail);
		mngWrapper.add(txtEmail);
		mngWrapper.add(lblPassword);
		mngWrapper.add(txtPassword);
		mngWrapper.add(lblPhone);
		mngWrapper.add(txtPhoneNumber);
		add(mngContent);
		mngContent.add(mngWrapper);
		HorizontalFieldManager mngButton = new HorizontalFieldManager(Field.USE_ALL_WIDTH);
		btnSubmit = new ImageButtonField("Submit", RegisterViewBitmaps.getButtonBg(), RegisterViewBitmaps.getButtonFocusbg(),Util.getFont(7),Color.BLACK,0);
		btnSubmit.setMargin(0, MARGIN, 0, 0);
		btnCancel = new ImageButtonField("Cancel", RegisterViewBitmaps.getButtonBg(), RegisterViewBitmaps.getButtonFocusbg(),Util.getFont(7),Color.BLACK,0);
		mngButton.add(btnSubmit);
		mngButton.add(btnCancel);
		btnSubmit.setChangeListener(new FieldChangeListener() {
			
			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.submitClick(txtUsername.getText(), txtEmail.getText(), txtPassword.getText(), txtPhoneNumber.getText());
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
		mngWrapper.setMargin(130, 0, 0, 0);
	}
	public void showProgress(String message) {
		// TODO Auto-generated method stub
		ProgressPopup.show(message);
	}
	public void showErrorMessage(String message) {
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
	public void fillFields(User u) {
		// TODO Auto-generated method stub
		txtUsername.setText(u.getUserName());
		txtPassword.setText(u.getPassword());
		txtEmail.setText(u.getEmail());
		txtPhoneNumber.setText(u.getPhoneNumber());
	}
	protected void sublayout(int width, int height) {
		// TODO Auto-generated method stub
		super.sublayout(width, height);
	}
}
