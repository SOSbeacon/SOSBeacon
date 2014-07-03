package bitmaps;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.image.Image;
import net.rim.device.api.ui.image.ImageFactory;


public class MenuBitmaps {
	private static Image _homeIcon;
	public static Image getHomeIcon(){
		if(_homeIcon==null){
			_homeIcon = ImageFactory.createImage(Bitmap.getBitmapResource("ic_home.png"));
		}
		return _homeIcon;
	}
	
	private static Image _contactIcon;
	public static Image getContactIcon(){
		if(_contactIcon==null){
			_contactIcon = ImageFactory.createImage(Bitmap.getBitmapResource("ic_contact.png"));
		}
		return _contactIcon;
	}
	
	private static Image _reviewIcon;
	public static Image getReviewIcon(){
		if(_reviewIcon==null){
			_reviewIcon = ImageFactory.createImage(Bitmap.getBitmapResource("ic_review.png"));
		}
		return _reviewIcon;
	}
	
	private static Image _settingIcon;
	public static Image getSettingIcon() {
		if(_settingIcon==null){
			_settingIcon = ImageFactory.createImage(Bitmap.getBitmapResource("ic_setting.png"));
		}
		return _settingIcon;
	}
}
