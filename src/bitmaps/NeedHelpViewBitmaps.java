package bitmaps;

import util.Util;
import view.custom.Header;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;

public class NeedHelpViewBitmaps {
	private static Bitmap _needHelpViewBg;
	public static Bitmap getNeedHelpViewBg(){
		if(_needHelpViewBg==null){
			_needHelpViewBg = Bitmap.getBitmapResource("bg1.png");
			_needHelpViewBg = Util.resizeBitmap(_needHelpViewBg, Display.getWidth(), Display.getHeight()-Header.getHeaderHeight());
		}
		return _needHelpViewBg;
	}
	
	private static Bitmap _btnCallEmergencyBg;
	public static Bitmap getBtnCallEmergencyBg(){
		if(_btnCallEmergencyBg==null){
			_btnCallEmergencyBg = Bitmap.getBitmapResource("red_button.png");
			_btnCallEmergencyBg = Util.resizeBitmap(_btnCallEmergencyBg, _btnCallEmergencyBg.getWidth(),
					(int) (_btnCallEmergencyBg.getHeight() * 1.2));
		}
		return _btnCallEmergencyBg;
	}
	
	private static Bitmap _btnSendAlertBg;
	public static Bitmap getBtnSendAlertBg(){
		if(_btnSendAlertBg==null){
			_btnSendAlertBg = Bitmap.getBitmapResource("yellow_button.png");
			_btnSendAlertBg = Util.resizeBitmap(_btnSendAlertBg, _btnSendAlertBg.getWidth(),
					(int) (_btnSendAlertBg.getHeight() * 1.2));
		}
		return _btnSendAlertBg;
	}
	
	private static Bitmap _btnCancelBg;
	public static Bitmap getBtnCancelBg(){
		if(_btnCancelBg==null){
			_btnCancelBg = Bitmap.getBitmapResource("white_button.png");
			_btnCancelBg = Util.resizeBitmap(_btnCancelBg, _btnCancelBg.getWidth(),
					(int) (_btnCancelBg.getHeight() * 1.2));
		}
		return _btnCancelBg;
	}
	
	private static Bitmap _btnFocusBg;
	public static Bitmap getBtnFocusBg(){
		if(_btnFocusBg==null){
			_btnFocusBg = Bitmap.getBitmapResource("focus_big.png");
			_btnFocusBg = Util.resizeBitmap(_btnFocusBg, _btnFocusBg.getWidth(),
					(int) (_btnFocusBg.getHeight() * 1.2));
		}
		return _btnFocusBg;
	}
}
