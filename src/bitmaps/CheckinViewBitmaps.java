package bitmaps;

import util.Util;
import view.custom.Header;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;

public class CheckinViewBitmaps {
	/*
	 * CheckinView bitmaps
	 * */
	private static Bitmap _checkinViewBg;
	public static Bitmap getCheckinViewBg(){
		if(_checkinViewBg==null){
			_checkinViewBg = Bitmap.getBitmapResource("bg1.png");
			_checkinViewBg = Util.resizeBitmap(_checkinViewBg, Display.getWidth(), Display.getHeight()-Header.getHeaderHeight());
		}
		return _checkinViewBg;
	}
	
	private static Bitmap _btnCheckinImOKBg;
	public static Bitmap getBtnCheckinImOKBg(){
		if(_btnCheckinImOKBg==null){
			_btnCheckinImOKBg = Bitmap.getBitmapResource("green_button.png");
			_btnCheckinImOKBg = Util.resizeBitmap(_btnCheckinImOKBg, _btnCheckinImOKBg.getWidth(),
					(int) (_btnCheckinImOKBg.getHeight() * 1.2));
		}
		return _btnCheckinImOKBg;
	}
	
	private static Bitmap _btnCheckinGroupBg;
	public static Bitmap getBtnCheckinGroupBg(){
		if(_btnCheckinGroupBg==null){
			_btnCheckinGroupBg = Bitmap.getBitmapResource("yellow_button.png");
			_btnCheckinGroupBg = Util.resizeBitmap(_btnCheckinGroupBg, _btnCheckinGroupBg.getWidth(),
					(int) (_btnCheckinGroupBg.getHeight() * 1.2));
		}
		return _btnCheckinGroupBg;
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
