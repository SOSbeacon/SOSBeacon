package view.setting.goodsamaritan;

import icontroller.setting.goodsamaritan.IGoodSamaritanController;
import iview.setting.goodsamaritan.IGoodSamaritanView;
import model.setting.goodsamaritan.GoodSamaritanModel;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import util.Util;
import view.AView;
import view.custom.Header;
import view.custom.ImageButtonField;
import view.custom.MessageBox;
import view.custom.ProgressPopup;
import bitmaps.GoodSamaritanViewBitmaps;
import bitmaps.HeaderBitmaps;
import controller.setting.goodsamaritan.GoodSamaritanController;

public class GoodSamaritanView extends AView implements IGoodSamaritanView {

	/**
	 * 
	 */
	IGoodSamaritanController controller;

	CheckboxField ckbAlert, ckbNearby;
	ObjectChoiceField cmbMaxAlert, cmbMaxNearby;
	ImageButtonField btnSave, btnCancel;

	public GoodSamaritanView() {
		super();
		controller = new GoodSamaritanController(this, new GoodSamaritanModel());
		// TODO Auto-generated constructor stub
		Header header = new Header("Good Samaritan");
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
		VerticalFieldManager mngContent = new VerticalFieldManager(
				Field.USE_ALL_HEIGHT);
		mngContent.setBackground(BackgroundFactory
				.createBitmapBackground(GoodSamaritanViewBitmaps
						.getGoodSamaritanViewBg()));
		VerticalFieldManager mngWrapper = new VerticalFieldManager() {
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(Color.GRAY);
				super.paint(graphics);
			}
		};
		font = Util.getFont(7);
		ckbAlert = new CheckboxField("Receive Good Samaritan Alert: ", false);
		ckbNearby = new CheckboxField("Alert Nearby Good Samaritan:", false);
		ckbAlert.setFont(font);
		ckbNearby.setFont(font);
		cmbMaxAlert = new ObjectChoiceField("Max Distance:", null);
		cmbMaxNearby = new ObjectChoiceField("Max Distance:", null);
		cmbMaxAlert.setFont(font);
		cmbMaxNearby.setFont(font);
		btnSave = new ImageButtonField("Save",GoodSamaritanViewBitmaps.getButtonBg(),GoodSamaritanViewBitmaps.getButtonFocusbg(),font,Color.BLACK,0);
		btnCancel = new ImageButtonField("Cancel",GoodSamaritanViewBitmaps.getButtonBg(),GoodSamaritanViewBitmaps.getButtonFocusbg(),font,Color.BLACK,0);
		btnSave.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.save(ckbAlert.getChecked(),
						cmbMaxAlert.getSelectedIndex(), ckbNearby.getChecked(),
						cmbMaxNearby.getSelectedIndex());
			}
		});
		btnCancel.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.cancel();
			}
		});
		mngWrapper.add(ckbAlert);
		mngWrapper.add(cmbMaxAlert);
		mngWrapper.add(ckbNearby);
		mngWrapper.add(cmbMaxNearby);

		HorizontalFieldManager mngButton = new HorizontalFieldManager(
				Field.USE_ALL_WIDTH);
		mngButton.setMargin(10, 0, 0, 0);
		btnSave.setMargin(0, 10, 0, 0);
		mngButton.add(btnSave);
		mngButton.add(btnCancel);

		mngWrapper.add(mngButton);
		mngWrapper.setMargin(150, 0, 0, 0);
		mngContent.add(mngWrapper);
		add(mngContent);
	}

	protected void onDisplay() {
		// TODO Auto-generated method stub
		super.onDisplay();
		controller.initialize();
	}

	public void fillFields(boolean samaritanAlert, Object[] maxDistanceAlert,
			int selectedIndexAlert, boolean nearbyAlert,
			Object[] maxDistanceNearby, int selectedIndexNearby) {
		// TODO Auto-generated method stub
		ckbAlert.setChecked(samaritanAlert);
		cmbMaxAlert.setChoices(maxDistanceAlert);
		cmbMaxAlert.setSelectedIndex(selectedIndexAlert);
		cmbMaxNearby.setChoices(maxDistanceNearby);
		cmbMaxNearby.setSelectedIndex(selectedIndexNearby);
		ckbNearby.setChecked(nearbyAlert);
	}

	public void showProgress() {
		// TODO Auto-generated method stub
		ProgressPopup.show("Saving...");
	}

	public void hideProgress() {
		// TODO Auto-generated method stub
		ProgressPopup.hide();
	}

	public void showMessage(String message) {
		// TODO Auto-generated method stub
		MessageBox.show(message);
	}
}
