package view.setting.aboutsos;

import net.rim.device.api.system.GPRSInfo;
import net.rim.device.api.system.IDENInfo;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import view.AView;
import view.custom.Header;
import view.custom.MessageBox;
import bitmaps.AboutViewBitmaps;

public class AboutSOSView extends AView {

	/**
	 * 
	 */
	public AboutSOSView() {
		super();
		// TODO Auto-generated constructor stub
		add(new Header("About SOSbeacon"));
		VerticalFieldManager mngContent = new VerticalFieldManager(Field.USE_ALL_HEIGHT|Field.USE_ALL_WIDTH);
		mngContent.setBackground(BackgroundFactory.createBitmapBackground(AboutViewBitmaps.getAboutViewBg()));
		add(mngContent);
		try{
		String imei = IDENInfo.imeiToString(GPRSInfo.getIMEI());
		LabelField lb = new LabelField(imei);
		mngContent.add(lb);
		}catch(Exception e){
			final String msg = e.getMessage();
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					MessageBox.show(msg);
				}
			});
		}
	}

}
