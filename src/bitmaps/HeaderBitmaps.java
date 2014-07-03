package bitmaps;

import net.rim.device.api.system.Bitmap;

public class HeaderBitmaps {
	private static Bitmap _buttonBg;
	public static Bitmap getButtonBg(){
		if(_buttonBg==null){
			_buttonBg = Bitmap.getBitmapResource("btn_header.png");
		}
		return _buttonBg;
	}
	
	private static Bitmap _buttonFocusBg;
	public static Bitmap getButtonFocusBg(){
		if(_buttonFocusBg==null){
			_buttonFocusBg = Bitmap.getBitmapResource("btn_header_focus.png");
		}
		return _buttonFocusBg;
	}
}
