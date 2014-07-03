package view.group.contacts.imports;

import icontroller.group.contacts.IGoogleImportController;
import iview.group.contacts.IGoogleImportView;
import model.group.contacts.GoogleImportModel;
import net.rim.device.api.browser.field2.BrowserField;
import net.rim.device.api.browser.field2.BrowserFieldConfig;
import net.rim.device.api.browser.field2.BrowserFieldListener;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import org.w3c.dom.Document;

import util.Util;
import view.AView;
import view.custom.Header;
import view.custom.ImageButtonField;
import view.custom.MessageBox;
import view.custom.ProgressPopup;
import bitmaps.HeaderBitmaps;
import controller.group.contacts.GoogleImportController;
import entities.Contact;
import entities.ContactGroup;

public class GoogleImportView extends AView implements IGoogleImportView{

	/**
	 * 
	 */
	BrowserField browser;
	IGoogleImportController controller;
	public GoogleImportView(ContactGroup g,Contact[] contacts, Contact[] add, Contact[] edit, Contact[] delete) {
		super();
		controller = new GoogleImportController(this,new GoogleImportModel(),g,contacts, add, edit, delete);
		// TODO Auto-generated constructor stub
		Header header = new Header("Google");
		add(header);
		Font font = Util.getFont(6,Font.BOLD);
		ImageButtonField btnBack = new ImageButtonField("Back", HeaderBitmaps.getButtonBg(),HeaderBitmaps.getButtonFocusBg(),font,Color.WHITE,0);
		btnBack.setMargin((Header.getHeaderHeight()-btnBack.getPreferredHeight())/2, 0, 0, 10);
		btnBack.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.back();
			}
		});
		header.add(btnBack);
		VerticalFieldManager mngContent = new VerticalFieldManager(Manager.USE_ALL_HEIGHT|Manager.USE_ALL_WIDTH);
		mngContent.setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		BrowserFieldConfig config = new BrowserFieldConfig();  
        config.setProperty(BrowserFieldConfig.ALLOW_CS_XHR, Boolean.TRUE);  
        config.setProperty(BrowserFieldConfig.INITIAL_SCALE, new Float(0.5));
		browser = new BrowserField(config);
		browser.addListener(new BrowserFieldListener() {
			public void documentLoaded(BrowserField browserField,
					Document document) throws Exception {
				// TODO Auto-generated method stub
				System.out.println("Loaded!");
				browser.setFocus();
				super.documentLoaded(browserField, document);
				controller.browserLoaded(browser.getDocumentUrl());
			}
		});
		mngContent.add(browser);
		add(mngContent);
	}

	public void showProgress(String msg) {
		// TODO Auto-generated method stub
		ProgressPopup.show(msg);
	}

	public void hideProgress() {
		// TODO Auto-generated method stub
		ProgressPopup.hide();
	}

	public void showMessage(String msg) {
		// TODO Auto-generated method stub
		MessageBox.show(msg);
	}

	public void loadUrl(String url) {
		// TODO Auto-generated method stub
		System.out.println("URL: "+url);
		browser.requestContent(url);
	}

	protected void onDisplay() {
		// TODO Auto-generated method stub
		super.onDisplay();
		controller.initialize();
	}

	public void fillUserCode(String userCode) {
		// TODO Auto-generated method stub
		try{
			browser.getDocument().getElementById("user_code").setAttribute("value", userCode);
			//browser.getDocument().getElementById("user_code").setAttribute("disabled", "disabled");
		}catch(Exception e){
			
		}
	}
}
