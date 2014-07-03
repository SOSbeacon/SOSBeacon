package view.custom;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.BorderFactory;

public class AddContactPopup extends PopupScreen{
	Bitmap _caret = Bitmap.getBitmapResource( "chevron_right_black_15x22.png" );
	public AddContactPopup(FieldChangeListener enter, FieldChangeListener google, FieldChangeListener yahoo){
		super(new VerticalFieldManager(),Field.FOCUSABLE|Field.USE_ALL_HEIGHT|Field.USE_ALL_WIDTH);
		setBackground(BackgroundFactory.createSolidTransparentBackground(Color.BLACK, 128));
		setBorder(BorderFactory.createSimpleBorder(new XYEdges()));
		ListStyleButtonSet buttonSet = new ListStyleButtonSet(){
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				int old = graphics.getColor();
				graphics.setColor(Color.BLACK);
				super.paint(graphics);
				graphics.setColor(old);
			}
		};
	       
        ListStyleButtonField btnEnter = new ListStyleButtonField( "Enter contact",_caret);
        btnEnter.setChangeListener(enter);
        buttonSet.add( btnEnter );
        
        ListStyleButtonField btnGoogle = new ListStyleButtonField( "Import Google contacts",_caret);
        btnGoogle.setChangeListener(google);
        buttonSet.add( btnGoogle );
        
        ListStyleButtonField btnYahoo = new ListStyleButtonField( "Import Yahoo contacts",_caret);
        btnYahoo.setChangeListener(yahoo);
        buttonSet.add( btnYahoo );
        buttonSet.setMargin(150,0,0,20);
        add(buttonSet);
	}
	AddContactPopup p;
	public void show(){
		p=this;
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				// TODO Auto-generated method stub
				UiApplication.getUiApplication().pushModalScreen(p);
			}
		});
	}
	public void hide(){
		synchronized(Application.getEventLock()){
			UiApplication.getUiApplication().popScreen(this);
		}
	}
	protected void sublayout(int width, int height) {
		// TODO Auto-generated method stub
		super.sublayout(getPreferredWidth(), getPreferredHeight());
		setExtent(getPreferredWidth(), getPreferredHeight());
		setPosition(0,0);
	}
	public int getPreferredHeight() {
		// TODO Auto-generated method stub
		return Display.getHeight();
	}
	public int getPreferredWidth() {
		// TODO Auto-generated method stub
		return Display.getWidth();
	}
	protected boolean keyChar(char c, int status, int time) {
		// TODO Auto-generated method stub
		if(c==Keypad.KEY_ESCAPE){
			hide();
			return true;
		}
		return super.keyChar(c, status, time);
	}
}
