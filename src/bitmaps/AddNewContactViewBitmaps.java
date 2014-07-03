package bitmaps;

import util.Util;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;

public class AddNewContactViewBitmaps {
	private static Bitmap _buttonBg;
	public static Bitmap getButtonBg(){
		if(_buttonBg==null){
			_buttonBg = Bitmap.getBitmapResource("msg_btn.png");
			_buttonBg = Util.resizeBitmap(_buttonBg, (int)(Display.getWidth()-30), (int)(_buttonBg.getHeight()*1.2));
		}
		return _buttonBg;
	}
	
	private static Bitmap _buttonFocusBg;
	public static Bitmap getButtonFocusbg(){
		if(_buttonFocusBg==null){
			_buttonFocusBg = Bitmap.getBitmapResource("msg_btn_focus.png");
			_buttonFocusBg = Util.resizeBitmap(_buttonFocusBg, (int)(Display.getWidth()-30), (int)(_buttonFocusBg.getHeight()*1.2));
		}
		return _buttonFocusBg;
	}
}
