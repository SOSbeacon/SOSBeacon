package view.group.contacts.imports;

import icontroller.group.contacts.IYahooImportController;
import iview.group.contacts.IYahooImportView;
import model.group.contacts.YahooImportModel;
import net.rim.device.api.browser.field2.BrowserField;
import net.rim.device.api.browser.field2.BrowserFieldConfig;
import net.rim.device.api.browser.field2.BrowserFieldListener;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import util.Util;
import view.AView;
import view.custom.Header;
import view.custom.ImageButtonField;
import view.custom.MessageBox;
import view.custom.ProgressPopup;
import bitmaps.HeaderBitmaps;
import constants.Values;
import controller.group.contacts.YahooImportController;
import entities.Contact;
import entities.ContactGroup;

public class YahooImportView extends AView implements IYahooImportView{

	ContactGroup group;
	IYahooImportController controller;
	BrowserField b;
	public YahooImportView(ContactGroup group,Contact[] contacts, Contact[] add, Contact[] edit, Contact[] delete){
		super();
		this.group=group;
		controller = new YahooImportController(this,new YahooImportModel(),group,contacts, add, edit, delete);
		Header header = new Header("Yahoo");
		add(header);
		ImageButtonField btnBack = new ImageButtonField("Back", HeaderBitmaps.getButtonBg(),HeaderBitmaps.getButtonFocusBg(),Util.getFont(6,Font.BOLD),Color.WHITE,0);
		btnBack.setMargin((Header.getHeaderHeight()-btnBack.getPreferredHeight())/2, 0, 0, 10);
		btnBack.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.back();
			}
		});
		header.add(btnBack);
		BrowserFieldConfig config = new BrowserFieldConfig();  
        config.setProperty(BrowserFieldConfig.ALLOW_CS_XHR, Boolean.TRUE);  
        config.setProperty(BrowserFieldConfig.INITIAL_SCALE, new Float(0.5));
		b = new BrowserField(config);
		b.addListener(new BrowserFieldListener() {
			public void documentLoaded(BrowserField browserField,
					Document document) throws Exception {
				// TODO Auto-generated method stub
				b.setFocus();
				super.documentLoaded(browserField, document);
				if(b.getDocumentUrl().equalsIgnoreCase(Values.YAHOO_GET_REQUEST_AUTH_URL)){
					Element e =b.getDocument().getElementById("shortCode");
					System.out.println("Short code: "+e.getTextContent());
					controller.receiveVerifier(e.getTextContent());
				}
			}
		});
		VerticalFieldManager mngContent = new VerticalFieldManager(Manager.USE_ALL_HEIGHT|Manager.USE_ALL_WIDTH);
		mngContent.setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		mngContent.add(b);
		add(mngContent);
	}
	public void hideProgress() {
		// TODO Auto-generated method stub
		ProgressPopup.hide();
	}
	public void showProgress(String msg) {
		// TODO Auto-generated method stub
		ProgressPopup.show(msg);
	}
	protected void onDisplay() {
		// TODO Auto-generated method stub
		super.onDisplay();
		controller.initialize();
	}
	public void showMessage(String message) {
		// TODO Auto-generated method stub
		MessageBox.show(message);
	}
	public void loadUrl(String url) {
		// TODO Auto-generated method stub
		synchronized (UiApplication.getEventLock()) {
			System.out.println("LoadUrl: "+url);
			b.requestContent(url);
		}
	}
}
