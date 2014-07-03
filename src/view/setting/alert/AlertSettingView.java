package view.setting.alert;

import icontroller.setting.alert.IAlertSettingController;
import iview.setting.alert.IAlertSettingView;
import model.setting.alert.AlertSettingModel;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import util.Util;
import view.AView;
import view.custom.FCLabelField;
import view.custom.Header;
import view.custom.ImageButtonField;
import view.custom.MessageBox;
import view.custom.OneLineTextField;
import view.custom.ProgressPopup;
import bitmaps.AlertSettingViewBitmaps;
import bitmaps.HeaderBitmaps;
import controller.setting.alert.AlertSettingController;
import entities.ContactGroup;

public class AlertSettingView extends AView implements IAlertSettingView{

	/**
	 * 
	 */
	IAlertSettingController controller;
	ObjectChoiceField cmbGroups,cmbVoice;
	OneLineTextField txtEmergency;
	CheckboxField ckbReceiveGovernmentAlert;
	ImageButtonField btnSave;
	ImageButtonField btnCancel;
	public AlertSettingView() {
		super();
		controller = new AlertSettingController(this,new AlertSettingModel());
		// TODO Auto-generated constructor stub
		//add(new Header("Alert Settings"));
		Header header = new Header("Alert Settings");
		add(header);
		Font font = Util.getFont(6,Font.BOLD);
		ImageButtonField btnBack = new ImageButtonField("Back", HeaderBitmaps.getButtonBg(),HeaderBitmaps.getButtonFocusBg(),font,Color.WHITE,0);
		btnBack.setMargin((Header.getHeaderHeight()-btnBack.getPreferredHeight())/2, 0, 0, 10);
		btnBack.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.cancel();
			}
		});
		header.add(btnBack);
		VerticalFieldManager mngContent = new VerticalFieldManager(Field.USE_ALL_HEIGHT);
		mngContent.setBackground(BackgroundFactory.createBitmapBackground(AlertSettingViewBitmaps.getAlertSettingViewBg()));
		VerticalFieldManager mngWrapper=new VerticalFieldManager(Field.USE_ALL_WIDTH){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.GRAY);
				super.paint(graphics);
			}
		};
		cmbVoice = new ObjectChoiceField("Voice duration:",null);
		cmbVoice.setFont(Util.getFont(7));
		cmbGroups=new ObjectChoiceField("Default groups:",null);
		cmbGroups.setFont(Util.getFont(7));
		//txtEmergency=new BasicEditField("Emergency Number: ","",100,Field.EDITABLE);
		HorizontalFieldManager h = new HorizontalFieldManager();
		txtEmergency = new OneLineTextField("", 100, 0);
		FCLabelField lbl = new FCLabelField(Color.GRAY, "Emergency number:");
		lbl.setMargin((txtEmergency.getPreferredHeight()-lbl.getFont().getHeight())/2, 0, 0, 0);
		h.add(lbl);
		h.add(txtEmergency);
		ckbReceiveGovernmentAlert=new CheckboxField("Receive Government Alerts: ",false);
		ckbReceiveGovernmentAlert.setFont(Util.getFont(7));
		btnSave = new ImageButtonField("Save",AlertSettingViewBitmaps.getButtonBg(),AlertSettingViewBitmaps.getButtonFocusbg(),Util.getFont(7),Color.BLACK,0);
		btnSave.setMargin(0, 10, 0, 0);
		btnCancel=new ImageButtonField("Cancel",AlertSettingViewBitmaps.getButtonBg(),AlertSettingViewBitmaps.getButtonFocusbg(),Util.getFont(7),Color.BLACK,0);
		
		btnSave.setChangeListener(new FieldChangeListener() {
			
			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.save(cmbVoice.getSelectedIndex(),cmbGroups.getSelectedIndex(),txtEmergency.getText(),ckbReceiveGovernmentAlert.getChecked());
			}
		});
		btnCancel.setChangeListener(new FieldChangeListener() {
			
			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.cancel();
			}
		});
		mngWrapper.add(cmbVoice);
		mngWrapper.add(cmbGroups);
		mngWrapper.add(h);
		mngWrapper.add(ckbReceiveGovernmentAlert);
		HorizontalFieldManager mngButton= new HorizontalFieldManager();
		mngButton.add(btnSave);
		mngButton.add(btnCancel);
		mngWrapper.add(mngButton);
		
		mngWrapper.setMargin(150, 0, 0, 20);
		mngContent.add(mngWrapper);
		add(mngContent);
	}

	public void showProgress(String message) {
		// TODO Auto-generated method stub
		ProgressPopup.show(message);
	}

	public void hideProgress() {
		// TODO Auto-generated method stub
		ProgressPopup.hide();
	}

	public void showMessage(String message) {
		// TODO Auto-generated method stub
		MessageBox.show(message);
	}

	public void fillChoices(ContactGroup[] groups, int selectedIndex) {
		// TODO Auto-generated method stub
		cmbGroups.setChoices(groups);
		if(selectedIndex>-1&&selectedIndex<groups.length)
			cmbGroups.setSelectedIndex(selectedIndex);
	}

	public void fillVoices(Object[] o, int selectedIndex){
		cmbVoice.setChoices(o);
		if(selectedIndex>-1&&selectedIndex<o.length)
			cmbVoice.setSelectedIndex(selectedIndex);
	}
	protected void onDisplay() {
		// TODO Auto-generated method stub
		super.onDisplay();
		controller.initialize();
	}

	public void fillFields(String panicNumber, boolean receiveAlert) {
		// TODO Auto-generated method stub
		txtEmergency.setText(panicNumber);
		ckbReceiveGovernmentAlert.setChecked(receiveAlert);
	}
}
