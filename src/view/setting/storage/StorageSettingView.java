package view.setting.storage;

import icontroller.setting.storage.IStorageSettingController;
import iview.setting.storage.IStorageSettingView;
import model.setting.storage.StorageSettingModel;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import util.Util;
import view.AView;
import view.custom.Header;
import view.custom.ImageButtonField;
import view.custom.MessageBox;
import bitmaps.HeaderBitmaps;
import bitmaps.StorageSettingViewBitmaps;
import controller.setting.storage.StorageSettingController;

public class StorageSettingView extends AView implements IStorageSettingView{

	/**
	 * 
	 */
	final int MARGIN = 10;
	IStorageSettingController controller;
	ImageButtonField btnClear,btnReset;
	public StorageSettingView() {
		super();
		controller=new StorageSettingController(this,new StorageSettingModel());
		Header header = new Header("Storage Setting");
		add(header);
		Font font = Util.getFont(6,Font.BOLD);
		ImageButtonField btnBack = new ImageButtonField("Back", HeaderBitmaps.getButtonBg(),HeaderBitmaps.getButtonFocusBg(),font,Color.WHITE,0);
		btnBack.setMargin((Header.getHeaderHeight()-btnBack.getPreferredHeight())/2, 0, 0, 10);
		btnBack.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.back();
			}
		});
		header.add(btnBack);
		Bitmap bmp = StorageSettingViewBitmaps.getStorageSettingViewBg();
		VerticalFieldManager mngContent = new VerticalFieldManager(Field.USE_ALL_HEIGHT|Field.USE_ALL_WIDTH);
		mngContent.setBackground(BackgroundFactory.createBitmapBackground(bmp));
		
		VerticalFieldManager mngWrapper = new VerticalFieldManager(Field.USE_ALL_WIDTH);
		// TODO Auto-generated constructor stub
		font = Util.getFont(7);
		btnClear=new ImageButtonField("Clear all recordings",StorageSettingViewBitmaps.getButtonBg(),StorageSettingViewBitmaps.getButtonFocusbg(),font,Color.BLACK,0);
		btnReset=new ImageButtonField("Reset all to defaults",StorageSettingViewBitmaps.getButtonBg(),StorageSettingViewBitmaps.getButtonFocusbg(),font,Color.BLACK,0);
		btnClear.setChangeListener(new FieldChangeListener() {
			
			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.clear();
			}
		});
		btnReset.setChangeListener(new FieldChangeListener() {
			
			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.reset();
			}
		});
		mngWrapper.add(btnClear);
		btnClear.setMargin(0, 0, MARGIN, 0);
		mngWrapper.add(btnReset);
		mngWrapper.setMargin((bmp.getHeight()-btnClear.getPreferredHeight()-btnReset.getPreferredHeight()-MARGIN)/2, 0, 0, (bmp.getWidth()-btnClear.getPreferredWidth())/2);
		mngContent.add(mngWrapper);
		add(mngContent);
	}

	public void showClearConfirm() {
		// TODO 
		if(MessageBox.show("Confirm","Sure to clear?",MessageBox.D_YES_NO)==MessageBox.YES){
			controller.clearRecordings();
		}
	}

	public void showResetConfirm() {
		// TODO Auto-generated method stub
		if(MessageBox.show("Confirm","Sure to reset?",MessageBox.D_YES_NO)==MessageBox.YES){
			controller.resetAll();
		}
	}

	public void showClearSucceed() {
		// TODO Auto-generated method stub
		MessageBox.show("Clear successfull");
		controller.clearSucceed();
	}

	public void showResetSucceed() {
		// TODO Auto-generated method stub
		MessageBox.show("Reset successfull");
		controller.resetSucceed();
	}

}
