package bitmaps;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import util.Util;
import view.custom.Header;

public class AlertSettingViewBitmaps {
	private static Bitmap _alertSettingViewBg;
	public static Bitmap getAlertSettingViewBg(){
		if(_alertSettingViewBg==null){
			_alertSettingViewBg = Bitmap.getBitmapResource("bg1.png");
			_alertSettingViewBg = Util.resizeBitmap(_alertSettingViewBg, Display.getWidth(), Display.getHeight()-Header.getHeaderHeight());
		}
		return _alertSettingViewBg;
	}
	private static Bitmap _buttonBg;
	public static Bitmap getButtonBg(){
		if(_buttonBg==null){
			_buttonBg = Bitmap.getBitmapResource("msg_btn.png");
			//_buttonBg = Util.resizeBitmap(_buttonBg, (getTextboxBg().getWidth()-RegisterView.MARGIN)/2, (int)(_buttonBg.getHeight()*1.2));
		}
		return _buttonBg;
	}
	
	private static Bitmap _buttonFocusBg;
	public static Bitmap getButtonFocusbg(){
		if(_buttonFocusBg==null){
			_buttonFocusBg = Bitmap.getBitmapResource("msg_btn_focus.png");
			//_buttonFocusBg = Util.resizeBitmap(_buttonFocusBg, (getTextboxBg().getWidth()-RegisterView.MARGIN)/2, (int)(_buttonFocusBg.getHeight()*1.2));
		}
		return _buttonFocusBg;
	}
}
