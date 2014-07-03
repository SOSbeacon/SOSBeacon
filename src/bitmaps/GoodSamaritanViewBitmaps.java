package bitmaps;

import util.Util;
import view.custom.Header;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;

public class GoodSamaritanViewBitmaps {
	private static Bitmap _goodSamaritanViewBg;
	public static Bitmap getGoodSamaritanViewBg(){
		if(_goodSamaritanViewBg==null){
			_goodSamaritanViewBg = Bitmap.getBitmapResource("bg1.png");
			_goodSamaritanViewBg = Util.resizeBitmap(_goodSamaritanViewBg, Display.getWidth(), Display.getHeight()-Header.getHeaderHeight());
		}
		return _goodSamaritanViewBg;
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
