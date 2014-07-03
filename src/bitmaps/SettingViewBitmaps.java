package bitmaps;

import net.rim.device.api.system.Bitmap;

public class SettingViewBitmaps {
	private static Bitmap _caret;
	public static Bitmap getCaret(){
		if(_caret==null){
			_caret=Bitmap.getBitmapResource("caret.png");
		}
		return _caret;
	}
}
