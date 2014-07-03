package view;

import net.rim.device.api.browser.field2.BrowserField;
import net.rim.device.api.browser.field2.BrowserFieldConfig;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import util.Session;
import view.custom.Header;
import constants.Values;

public class ReviewView extends AView {

	/**
	 * 
	 */
	BrowserField browser;
	public ReviewView() {
		super();
		// TODO Auto-generated constructor stub
		add(new Header("Review History"));
		BrowserFieldConfig config=new BrowserFieldConfig();
		config.setProperty(BrowserFieldConfig.ALLOW_CS_XHR, Boolean.TRUE);
		config.setProperty(BrowserFieldConfig.INITIAL_SCALE, new Float(0.5));
		browser=new BrowserField(config);
		VerticalFieldManager mng = new VerticalFieldManager(Manager.VERTICAL_SCROLL|Manager.HORIZONTAL_SCROLL);
		mng.add(browser);
		add(mng);
		browser.requestContent(Values.REVIEW_URL+Session.getToken());
		//browser.requestContent("http://google.com.vn");
	}
}
