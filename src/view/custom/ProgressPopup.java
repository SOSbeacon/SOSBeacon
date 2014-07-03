package view.custom;

import util.Util;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.BorderFactory;

public class ProgressPopup extends PopupScreen {

	LabelField lbl;
	ProgressAnimationField anim;
	Bitmap background;
	private ProgressPopup(){
		super(new VerticalFieldManager(),Field.FOCUSABLE|Field.USE_ALL_HEIGHT|Field.USE_ALL_WIDTH);
		setBackground(BackgroundFactory.createSolidTransparentBackground(Color.BLACK, 128));
		setBorder(BorderFactory.createSimpleBorder(new XYEdges()));
		background=Bitmap.getBitmapResource("progress.png");
		Font font;
		try {
			FontFamily family = FontFamily.forName("BBAlpha Sans");
			font = family.getFont(Font.PLAIN, 6, Ui.UNITS_pt);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			font=null;
		}
		if(font!=null)
			setFont(font);
		HorizontalFieldManager h = new HorizontalFieldManager(){
			protected void sublayout(int maxWidth, int maxHeight) {
				// TODO Auto-generated method stub
				super.sublayout(maxWidth, maxHeight);
				setExtent(getPreferredWidth(),getPreferredHeight());
			}
			public int getPreferredHeight() {
				// TODO Auto-generated method stub
				return background.getHeight();
			}
			public int getPreferredWidth() {
				// TODO Auto-generated method stub
				return background.getWidth();
			}
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				graphics.setColor(0xFFBEBEBE);
				super.paint(graphics);
			}
		};
		h.setBackground(BackgroundFactory.createBitmapBackground(background));
		//h.setBackground(BackgroundFactory.createSolidBackground(Color.GRAY));
		lbl=new LabelField();
		lbl.setMargin((h.getPreferredHeight()-getFont().getHeight())/2,0,0,10);
		Bitmap spin = Bitmap.getBitmapResource("spinner.png");
		anim=new ProgressAnimationField(spin, 5, 0);
		anim.setMargin((h.getPreferredHeight()-spin.getHeight())/2, 0, 0, 5);
		h.add(anim);
		h.add(lbl);
		setPadding((getPreferredHeight()-h.getPreferredHeight())/2, 0, 
				0, (getPreferredWidth()-h.getPreferredWidth())/2);
		add(h);
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
	public void setLabel(String label){
		synchronized(Application.getEventLock()){
			lbl.setText(label);
		}
	}
	public static void setMessage(String msg){
		synchronized (UiApplication.getEventLock()) {
			getInstance().setLabel(msg);
		}
	}
	public static void show(String msg){
		setMessage(msg);
//		UiApplication.getUiApplication().invokeLater(new Runnable() {
//			
//			public void run() {
//				// TODO Auto-generated method stub
//				UiApplication.getUiApplication().pushScreen(getInstance());
//			}
//		});
		synchronized (UiApplication.getEventLock()) {
			UiApplication.getUiApplication().pushScreen(getInstance());
		}
	}
	public static void hide(){
		synchronized(Application.getEventLock()){
			//if(UiApplication.getUiApplication().getActiveScreen()==getInstance())
				UiApplication.getUiApplication().popScreen(getInstance());
		}
	}
	private static ProgressPopup _instance;
	public static ProgressPopup getInstance(){
		if(_instance==null){
			_instance=new ProgressPopup();
		}
		return _instance;
	}
}
