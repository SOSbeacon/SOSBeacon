package bitmaps;

import util.Util;
import view.custom.Header;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;

public class AboutViewBitmaps {
	private static Bitmap _aboutViewBg;
	public static Bitmap getAboutViewBg(){
		if(_aboutViewBg==null){
			_aboutViewBg = Bitmap.getBitmapResource("bg1.png");
			_aboutViewBg = Util.resizeBitmap(_aboutViewBg, Display.getWidth(), Display.getHeight()-Header.getHeaderHeight());
		}
		return _aboutViewBg;
	}
}
