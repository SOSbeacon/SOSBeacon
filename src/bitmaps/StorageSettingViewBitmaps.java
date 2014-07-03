package bitmaps;

import util.Util;
import view.custom.Header;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;

public class StorageSettingViewBitmaps {
	private static Bitmap _storageSettingViewBg;
	public static Bitmap getStorageSettingViewBg(){
		if(_storageSettingViewBg==null){
			_storageSettingViewBg = Bitmap.getBitmapResource("bg1.png");
			_storageSettingViewBg = Util.resizeBitmap(_storageSettingViewBg, Display.getWidth(), Display.getHeight()-Header.getHeaderHeight());
		}
		return _storageSettingViewBg;
	}
	
	private static Bitmap _buttonBg;
	public static Bitmap getButtonBg(){
		if(_buttonBg==null){
			_buttonBg = Bitmap.getBitmapResource("msg_btn.png");
			_buttonBg = Util.resizeBitmap(_buttonBg, (int)(_buttonBg.getWidth()*1.5), (int)(_buttonBg.getHeight()*1.5));
		}
		return _buttonBg;
	}
	
	private static Bitmap _buttonFocusBg;
	public static Bitmap getButtonFocusbg(){
		if(_buttonFocusBg==null){
			_buttonFocusBg = Bitmap.getBitmapResource("msg_btn_focus.png");
			_buttonFocusBg = Util.resizeBitmap(_buttonFocusBg, (int)(_buttonFocusBg.getWidth()*1.5), (int)(_buttonFocusBg.getHeight()*1.5));
		}
		return _buttonFocusBg;
	}
}
