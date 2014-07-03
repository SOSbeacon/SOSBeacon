package bitmaps;

import util.Util;
import view.RegisterView;
import view.custom.Header;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;

public class RegisterViewBitmaps {
	private static Bitmap _registerViewBg;
	public static Bitmap getRegisterViewBg(){
		if(_registerViewBg==null){
			_registerViewBg=Bitmap.getBitmapResource("bg1.png");
			_registerViewBg=Util.resizeBitmap(_registerViewBg, Display.getWidth(), Display.getHeight()-Header.getHeaderHeight());
		}
		return _registerViewBg;
	}
	private static Bitmap _buttonBg;
	public static Bitmap getButtonBg(){
		if(_buttonBg==null){
			_buttonBg = Bitmap.getBitmapResource("msg_btn.png");
			_buttonBg = Util.resizeBitmap(_buttonBg, (getTextboxBg().getWidth()-RegisterView.MARGIN)/2, (int)(_buttonBg.getHeight()*1.2));
		}
		return _buttonBg;
	}
	
	private static Bitmap _buttonFocusBg;
	public static Bitmap getButtonFocusbg(){
		if(_buttonFocusBg==null){
			_buttonFocusBg = Bitmap.getBitmapResource("msg_btn_focus.png");
			_buttonFocusBg = Util.resizeBitmap(_buttonFocusBg, (getTextboxBg().getWidth()-RegisterView.MARGIN)/2, (int)(_buttonFocusBg.getHeight()*1.2));
		}
		return _buttonFocusBg;
	}
	
	private static Bitmap _textboxBg;
	public static Bitmap getTextboxBg(){
		if(_textboxBg==null){
			_textboxBg = Bitmap.getBitmapResource("textboxunfocus.png");
		}
		return _textboxBg;
	}
	
	private static Bitmap _textboxFocusBg;
	public static Bitmap getTextboxFocusbg(){
		if(_textboxFocusBg==null){
			_textboxFocusBg = Bitmap.getBitmapResource("textboxfocus.png");
		}
		return _textboxFocusBg;
	}
}
