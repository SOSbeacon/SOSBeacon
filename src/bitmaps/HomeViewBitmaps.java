package bitmaps;

import util.Util;
import net.rim.device.api.system.Bitmap;

public class HomeViewBitmaps {
	private static Bitmap _buttonBg;
	private static Bitmap _buttonFocusBg;
	public static Bitmap getButtonBg(){
		if(_buttonBg==null){
			_buttonBg = Bitmap.getBitmapResource("msg_btn.png");
			_buttonBg = Util.resizeBitmap(_buttonBg, (int)(_buttonBg.getWidth()*0.75), (int)(_buttonBg.getHeight()*1.2));
		}
		return _buttonBg;
	}
	public static Bitmap getButtonFocusBg(){
		if(_buttonFocusBg==null){
			_buttonFocusBg = Bitmap.getBitmapResource("msg_btn_focus.png");
			_buttonFocusBg = Util.resizeBitmap(_buttonFocusBg, (int)(_buttonFocusBg.getWidth()*0.75), (int)(_buttonFocusBg.getHeight()*1.2));
		}
		return _buttonFocusBg;
	}
}
