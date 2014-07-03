package view.group.contacts.edit;

import bitmaps.HeaderBitmaps;
import icontroller.group.contacts.edit.IEditContactController;
import iview.group.contacts.edit.IEditContactView;
import model.group.contacts.edit.EditContactModel;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.BorderFactory;
import util.Util;
import view.AView;
import view.custom.FCLabelField;
import view.custom.GridManager;
import view.custom.Header;
import view.custom.ImageButtonField;
import view.custom.MessageBox;
import view.custom.OneLineTextField;
import view.custom.ProgressPopup;
import controller.group.contacts.edit.EditContactController;
import entities.Contact;
import entities.ContactGroup;

public class EditContactView extends AView implements IEditContactView{

	/**
	 * 
	 */
	OneLineTextField txtUsername,txtEmail,txtVoice,txtText;
	IEditContactController controller;
	public EditContactView(ContactGroup g,Contact[] contacts, Contact[] add, Contact[] edit, Contact[] delete, Contact c) {
		super();
		// TODO Auto-generated constructor stub
		controller = new EditContactController(this, new EditContactModel(),g,contacts,add,edit,delete,c);
		Header header = new Header("Edit Contact");
		txtUsername = new OneLineTextField("", 100, 0);
		txtEmail = new OneLineTextField("", 100, 0);
		txtVoice = new OneLineTextField("", 100, 0);
		txtText = new OneLineTextField("", 100, 0);
		Font font = Util.getFont(6,Font.BOLD);
		ImageButtonField btnBack = new ImageButtonField("Back", HeaderBitmaps.getButtonBg(),HeaderBitmaps.getButtonFocusBg(),font,Color.WHITE,0);
		btnBack.setMargin((Header.getHeaderHeight()-btnBack.getPreferredHeight())/2, 0, 0, 10);
		btnBack.setChangeListener(new FieldChangeListener() {
			
			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.back(txtUsername.getText(),txtEmail.getText(),txtVoice.getText(),txtText.getText());
			}
		});
		header.add(btnBack);
		VerticalFieldManager mngRight = new VerticalFieldManager(Field.USE_ALL_WIDTH|Field.FIELD_RIGHT);
		ImageButtonField btnSave = new ImageButtonField("Save",HeaderBitmaps.getButtonBg(),HeaderBitmaps.getButtonFocusBg(),font,Color.WHITE,Field.FIELD_RIGHT);
		btnSave.setMargin((Header.getHeaderHeight()-btnSave.getPreferredHeight())/2, 10, 0, 0);
		header.add(mngRight);
		mngRight.add(btnSave);
		btnSave.setChangeListener(new FieldChangeListener() {
			
			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.save(txtUsername.getText(),txtEmail.getText(),txtVoice.getText(),txtText.getText());
			}
		});
		VerticalFieldManager mngContent = new VerticalFieldManager(Manager.USE_ALL_WIDTH);
		mngContent.setBorder(BorderFactory.createBitmapBorder(new XYEdges(12, 12, 12, 12), Bitmap.getBitmapResource("rounded-border.png")));
		GridManager mngWrapper = new GridManager(2,Field.USE_ALL_WIDTH|Field.FIELD_VCENTER);
		//mngWrapper.setBackground(BackgroundFactory.createSolidBackground(Color.GRAY));
		FCLabelField lblUserName=new FCLabelField(Color.GRAY,"User Name:     ");
		FCLabelField lblEmail=new FCLabelField(Color.GRAY,"Email:");
		FCLabelField lblVoice=new FCLabelField(Color.GRAY,"Voice phone:");
		FCLabelField lblText=new FCLabelField(Color.GRAY,"Text phone:");
		
		mngWrapper.add(lblUserName);
		mngWrapper.add(txtUsername);
		
		mngWrapper.add(lblEmail);
		mngWrapper.add(txtEmail);
		
		mngWrapper.add(lblVoice);
		mngWrapper.add(txtVoice);
		
		mngWrapper.add(lblText);
		mngWrapper.add(txtText);
		
		
		mngContent.add(mngWrapper);
		
		VerticalFieldManager mngWrap = new VerticalFieldManager(Manager.USE_ALL_HEIGHT|Manager.USE_ALL_WIDTH);
		mngWrap.setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		add(header);
		add(mngWrap);
		mngWrap.add(mngContent);
		fillFields(c);
	}
	public void hideProgress() {
		// TODO Auto-generated method stub
		ProgressPopup.hide();
	}
	public void showMessage(String message) {
		// TODO Auto-generated method stub
		MessageBox.show(message);
	}
	public void showProgress(String msg) {
		// TODO Auto-generated method stub
		ProgressPopup.show(msg);
	}
	void fillFields(Contact c){
		txtUsername.setText(c.getName());
		txtEmail.setText(c.getEmail());
		txtVoice.setText(c.getVoicePhone());
		txtText.setText(c.getTextPhone());
	}
	protected void onDisplay() {
		// TODO Auto-generated method stub
		super.onDisplay();
		controller.initialize();
	}
	public void showConfirm(String message) {
		// TODO Auto-generated method stub
		if(MessageBox.show(message,MessageBox.D_YES_NO)==MessageBox.YES){
			controller.save(txtUsername.getText(),txtEmail.getText(),txtVoice.getText(),txtText.getText());
			controller.goBack();
		}else{
			controller.goBack();
		}
	}
	protected boolean keyChar(char c, int status, int time) {
		// TODO Auto-generated method stub
		if(c==Characters.ESCAPE){
			controller.back(txtUsername.getText(),txtEmail.getText(),txtVoice.getText(),txtText.getText());
			return true;
		}
		return super.keyChar(c, status, time);
	}
}
