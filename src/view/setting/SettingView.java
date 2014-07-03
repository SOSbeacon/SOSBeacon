package view.setting;

import icontroller.setting.ISettingController;
import iview.setting.ISettingView;
import model.setting.SettingModel;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import view.AView;
import view.custom.Header;
import view.custom.ListStyleButtonField;
import view.custom.ListStyleButtonSet;
import bitmaps.SettingViewBitmaps;
import controller.setting.SettingController;

public class SettingView extends AView implements ISettingView{

	/**
	 * 
	 */
	ISettingController controller;
	ButtonField btnAccountInformation,btnAlertSetting;
	ButtonField btnGoodSamaritan,btnStorageSetting;
	ButtonField btnContact,btnTell,btnAbout;
	public SettingView() {
		 super();
		 controller =new SettingController(this,new SettingModel());
	        add(new Header("More"));
	        VerticalFieldManager mngButton = new VerticalFieldManager(Field.USE_ALL_HEIGHT);
	        mngButton.setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
	        ListStyleButtonSet buttonSet = new ListStyleButtonSet();
	       
	        ListStyleButtonField btnAccountInformation = new ListStyleButtonField( "Account Information", SettingViewBitmaps.getCaret());
	        buttonSet.add( btnAccountInformation );
	        
	        ListStyleButtonField btnAlertSetting = new ListStyleButtonField( "Alert Settings", SettingViewBitmaps.getCaret() );
	        buttonSet.add( btnAlertSetting );
	        
	        ListStyleButtonField btnGoodSamaritan = new ListStyleButtonField( "Good Samaritan", SettingViewBitmaps.getCaret() );
	        buttonSet.add( btnGoodSamaritan );
	        
	        ListStyleButtonField btnStorageSetting = new ListStyleButtonField( "Storage Settings", SettingViewBitmaps.getCaret() );
	        buttonSet.add( btnStorageSetting );
	        
	        ListStyleButtonField btnContact = new ListStyleButtonField( "Contact SOSbeacon", SettingViewBitmaps.getCaret() );
	        buttonSet.add( btnContact );
	        
	        ListStyleButtonField btnTell = new ListStyleButtonField( "Tell a friend", SettingViewBitmaps.getCaret() );
	        buttonSet.add( btnTell );
	        
	        ListStyleButtonField btnAbout = new ListStyleButtonField( "About SOSbeacon", SettingViewBitmaps.getCaret() );
	        buttonSet.add( btnAbout );
	        
	        mngButton.add(buttonSet);
	        add(mngButton);
	        
	        btnAccountInformation.setChangeListener(new FieldChangeListener() {
				
				public void fieldChanged(Field field, int context) {
					// TODO Auto-generated method stub
					controller.accountInformation();
				}
			});
			btnAlertSetting.setChangeListener(new FieldChangeListener() {
				
				public void fieldChanged(Field field, int context) {
					// TODO Auto-generated method stub
					controller.alertSetting();
				}
			});
			btnGoodSamaritan.setChangeListener(new FieldChangeListener() {
				
				public void fieldChanged(Field field, int context) {
					// TODO Auto-generated method stub
					controller.goodSamaritan();
				}
			});
			btnStorageSetting.setChangeListener(new FieldChangeListener() {
				
				public void fieldChanged(Field field, int context) {
					// TODO Auto-generated method stub
					controller.storageSetting();
				}
			});
			btnContact.setChangeListener(new FieldChangeListener() {
				
				public void fieldChanged(Field field, int context) {
					// TODO Auto-generated method stub
					controller.contactSOS();
				}
			});
			btnTell.setChangeListener(new FieldChangeListener() {
				
				public void fieldChanged(Field field, int context) {
					// TODO Auto-generated method stub
					controller.tellAFriend();
				}
			});
			btnAbout.setChangeListener(new FieldChangeListener() {
				
				public void fieldChanged(Field field, int context) {
					// TODO Auto-generated method stub
					controller.aboutSOS();
				}
			});
	}

}
