package bitmaps;

import util.Util;
import view.custom.Header;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;

public class MainViewBitmaps {
	/*
	 * MainView bitmaps
	 */
	private static Bitmap _mainViewBg;

	public static Bitmap getMainViewBg() {
		if (_mainViewBg == null) {
			_mainViewBg = Bitmap.getBitmapResource("bg1.png");
			_mainViewBg = Util.resizeBitmap(_mainViewBg, Display.getWidth(),
					Display.getHeight() - Header.getHeaderHeight());
		}
		return _mainViewBg;
	}

	private static Bitmap _btnImOKBg;

	public static Bitmap getBtnImOKBg() {
		if (_btnImOKBg == null) {
			_btnImOKBg = Bitmap.getBitmapResource("green_button.png");
			_btnImOKBg = Util.resizeBitmap(_btnImOKBg, _btnImOKBg.getWidth(),
					(int) (_btnImOKBg.getHeight() * 1.5));	
		}
		return _btnImOKBg;
	}

	private static Bitmap _btnNeedHelpBg;

	public static Bitmap getBtnNeedHelpBg() {
		if (_btnNeedHelpBg == null) {
			_btnNeedHelpBg = Bitmap.getBitmapResource("red_button.png");
			_btnNeedHelpBg = Util.resizeBitmap(_btnNeedHelpBg, _btnNeedHelpBg.getWidth(),(int)( _btnNeedHelpBg.getHeight()*1.5));
		}
		return _btnNeedHelpBg;
	}

	private static Bitmap _btnFocusBg;

	public static Bitmap getBtnFocusBg() {
		if (_btnFocusBg == null) {
			_btnFocusBg = Bitmap.getBitmapResource("focus_big.png");
			_btnFocusBg = Util.resizeBitmap(_btnFocusBg, _btnFocusBg.getWidth(),(int)( _btnFocusBg.getHeight()*1.5));
		}
		return _btnFocusBg;
	}
}
