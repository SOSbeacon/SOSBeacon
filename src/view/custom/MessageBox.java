package view.custom;

import util.Util;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.BorderFactory;

public class MessageBox extends PopupScreen {
	public static final int D_OK=1;
	public static final int D_YES_NO=2;
	public static final int D_OK_CANCEL=3;
	public static final int D_CALL_CANCEL=4;
	public static final int OK=1;
	public static final int YES=2;
	public static final int NO=3;
	public static final int CANCEL=4;
	public static final int CALL=5;
	public static final int NONE=-1;
	final int MARGIN_TOP=20,MARGIN_BOTTOM=0,MARGIN_LEFT=20,MARGIN_RIGHT=0;;
	Bitmap header,footer,content,btn,focus;
	Font font,btnFont;
	LabelField lblMessage;
	HorizontalFieldManager mngButton;
	HorizontalFieldManager mngContent;
	private MessageBox(){
		super(new VerticalFieldManager(),Field.FOCUSABLE|Field.USE_ALL_HEIGHT|Field.USE_ALL_WIDTH);
		try {
			FontFamily family = FontFamily.forName("BBAlpha Sans");
			font = family.getFont(Font.PLAIN, 7, Ui.UNITS_pt);
			btnFont = family.getFont(Font.PLAIN,7,Ui.UNITS_pt);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			btnFont=null;
			font=null;
		}
		header = Bitmap.getBitmapResource("msg_header.png");
		footer = Bitmap.getBitmapResource("msg_footer.png");
		content = Bitmap.getBitmapResource("msg_content.png");
		btn = Bitmap.getBitmapResource("msg_btn.png");
		focus = Bitmap.getBitmapResource("msg_btn_focus.png");
		setBackground(BackgroundFactory.createSolidTransparentBackground(Color.BLACK, 128));
		setBorder(BorderFactory.createSimpleBorder(new XYEdges()));
		System.out.println("Header: "+header.getWidth()+","+header.getHeight());
		System.out.println("Footer: "+footer.getWidth()+","+footer.getHeight());
		lblMessage = new LabelField();
		HorizontalFieldManager mngHeader = new HorizontalFieldManager(){
			public int getPreferredHeight() {
				return header.getHeight();
			}
			public int getPreferredWidth() {
				return header.getWidth();
			}
			protected void sublayout(int maxWidth, int maxHeight) {
				// TODO Auto-generated method stub
				super.sublayout(maxWidth, maxHeight);
				setExtent(getPreferredWidth(), getPreferredHeight());
			}
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				if(font!=null)
					graphics.setFont(font);
				graphics.drawText(caption, 40, (getPreferredHeight()-graphics.getFont().getHeight())/2);
				super.paint(graphics);
			}
		};
		
		mngHeader.setBackground(BackgroundFactory.createBitmapBackground(header));
		mngContent = new HorizontalFieldManager(){
			public int getPreferredWidth() {
				return header.getWidth();
			}
			protected void sublayout(int maxWidth, int maxHeight) {
				// TODO Auto-generated method stub
				super.sublayout(maxWidth, maxHeight);
				setExtent(getPreferredWidth(), lblMessage.getHeight()+MARGIN_TOP+MARGIN_BOTTOM);
			}
			protected void paint(Graphics graphics) {
				// TODO Auto-generated method stub
				super.paint(graphics);
			}
			public int getPreferredHeight() {
				// TODO Auto-generated method stub
				return lblMessage.getHeight()+MARGIN_TOP+MARGIN_BOTTOM;
			}
		};
		if(font!=null)
			mngContent.setFont(font);
		mngContent.setPadding(MARGIN_TOP, MARGIN_RIGHT, MARGIN_BOTTOM, MARGIN_LEFT);
		mngContent.setBackground(BackgroundFactory.createBitmapBackground(content, 0, 0, Background.REPEAT_BOTH));
		mngContent.add(lblMessage);
		HorizontalFieldManager mngFooter = new HorizontalFieldManager(){
			public int getPreferredHeight() {
				return footer.getHeight();
			}
			public int getPreferredWidth() {
				return footer.getWidth();
			}
			protected void sublayout(int maxWidth, int maxHeight) {
				// TODO Auto-generated method stub
				super.sublayout(maxWidth, maxHeight);
				setExtent(getPreferredWidth(), getPreferredHeight());
			}
		};
		mngFooter.setBackground(BackgroundFactory.createBitmapBackground(footer));
		mngButton = new HorizontalFieldManager(Manager.USE_ALL_HEIGHT|Manager.USE_ALL_WIDTH){
			public int getPreferredHeight() {
				return footer.getHeight();
			}
			public int getPreferredWidth() {
				return footer.getWidth();
			}
		};
		//mngButton.setBackground(BackgroundFactory.createSolidBackground(Color.RED));
		mngFooter.add(mngButton);	
		add(mngHeader);
		add(mngContent);
		add(mngFooter);
		
		int marginX = (getPreferredWidth()-mngHeader.getPreferredWidth())/2;
		int marginY = (getPreferredHeight()-mngHeader.getPreferredHeight()-mngContent.getPreferredHeight()-mngFooter.getPreferredHeight())/2;
		setPadding(marginY, 0, 0, marginX);
		//mngHeader.setMargin(marginY, 0, 0, marginX);
	}
	private static MessageBox _instance;
	public static MessageBox getInstance(){
		if(_instance==null){
			_instance = new MessageBox();
		}
		return _instance;
	}
	String caption;
	public void setCaption(String caption){
		this.caption=caption;
		invalidate();
	}
	public void setMsg(String message){
		lblMessage.setText(message);
		//invalidateLayout();
		//invalidate();	
	}
	static int result=NONE;
	void initializeButton(int type){
		mngButton.deleteAll();
		int space=16;
		int width = (mngButton.getPreferredWidth()-3*space)/2;
		switch(type){
		case D_OK:
			ImageButtonField btnOk = new ImageButtonField("OK",btn,focus,btnFont,Color.BLACK,0);
			mngButton.add(btnOk);
			btnOk.setMargin((mngButton.getPreferredHeight()-btnOk.getPreferredHeight())/2, 0
					, 0, (mngButton.getPreferredWidth()-btnOk.getPreferredWidth())/2);
			btnOk.setChangeListener(new FieldChangeListener() {
				
				public void fieldChanged(Field field, int context) {
					// TODO Auto-generated method stub
					returnValue(OK);
				}
			});
			break;
		case D_OK_CANCEL:
			ImageButtonField btnOk_ = new ImageButtonField("OK",Util.resizeBitmap(btn, width, btn.getHeight()),Util.resizeBitmap(focus, width, focus.getHeight()),btnFont,Color.BLACK,0);
			mngButton.add(btnOk_);
			btnOk_.setChangeListener(new FieldChangeListener() {
				
				public void fieldChanged(Field field, int context) {
					// TODO Auto-generated method stub
					returnValue(OK);
				}
			});
			btnOk_.setMargin((mngButton.getPreferredHeight()-btnOk_.getPreferredHeight())/2, space, 0, space);
			ImageButtonField btnCancel_ = new ImageButtonField("CANCEL", Util.resizeBitmap(btn, width, btn.getHeight()),Util.resizeBitmap(focus, width, focus.getHeight()),btnFont,Color.BLACK,0);
			mngButton.add(btnCancel_);
			btnCancel_.setChangeListener(new FieldChangeListener() {
				
				public void fieldChanged(Field field, int context) {
					// TODO Auto-generated method stub
					returnValue(CANCEL);
				}
			});
			btnCancel_.setMargin((mngButton.getPreferredHeight()-btnCancel_.getPreferredHeight())/2, 0, 0, 0);
			break;
		case D_YES_NO:
			ImageButtonField btnYes = new ImageButtonField("YES",Util.resizeBitmap(btn, width, btn.getHeight()),Util.resizeBitmap(focus, width, focus.getHeight()),btnFont,Color.BLACK,0);
			mngButton.add(btnYes);
			btnYes.setChangeListener(new FieldChangeListener() {
				
				public void fieldChanged(Field field, int context) {
					// TODO Auto-generated method stub
					returnValue(YES);
				}
			});
			btnYes.setMargin((mngButton.getPreferredHeight()-btnYes.getPreferredHeight())/2, space, 0, space);
			ImageButtonField btnNo = new ImageButtonField("NO", Util.resizeBitmap(btn, width, btn.getHeight()),Util.resizeBitmap(focus, width, focus.getHeight()),btnFont,Color.BLACK,0);
			mngButton.add(btnNo);
			btnNo.setChangeListener(new FieldChangeListener() {
				
				public void fieldChanged(Field field, int context) {
					// TODO Auto-generated method stub
					returnValue(NO);
				}
			});
			btnNo.setMargin((mngButton.getPreferredHeight()-btnNo.getPreferredHeight())/2, 0, 0, 0);
			break;
		case D_CALL_CANCEL:
			ImageButtonField btnCall = new ImageButtonField("CALL NOW",Util.resizeBitmap(btn, width, btn.getHeight()),Util.resizeBitmap(focus, width, focus.getHeight()),btnFont,Color.BLACK,0);
			mngButton.add(btnCall);
			btnCall.setChangeListener(new FieldChangeListener() {
				
				public void fieldChanged(Field field, int context) {
					// TODO Auto-generated method stub
					returnValue(CALL);
				}
			});
			btnCall.setMargin((mngButton.getPreferredHeight()-btnCall.getPreferredHeight())/2, space, 0, space);
			ImageButtonField btnCancel = new ImageButtonField("CANCEL", Util.resizeBitmap(btn, width, btn.getHeight()),Util.resizeBitmap(focus, width, focus.getHeight()),btnFont,Color.BLACK,0);
			mngButton.add(btnCancel);
			btnCancel.setChangeListener(new FieldChangeListener() {
				
				public void fieldChanged(Field field, int context) {
					// TODO Auto-generated method stub
					returnValue(CANCEL);
				}
			});
			btnCancel.setMargin((mngButton.getPreferredHeight()-btnCancel.getPreferredHeight())/2, 0, 0, 0);
			break;
		}
	}
	void returnValue(int value){
		synchronized(UiApplication.getEventLock()){
			result=value;
			UiApplication.getUiApplication().popScreen(getInstance());
		}
	}
	public static int show(String message){
		return show(message,D_OK);
	}
	public static int show(String message,int type){
		return show("SOSbeacon",message,type);
	}
	public static int show(String caption,String message,int type){
		synchronized (UiApplication.getEventLock()) {
			getInstance().setMsg(message);
			getInstance().setCaption(caption);
			getInstance().initializeButton(type);
			UiApplication.getUiApplication().pushModalScreen(getInstance());
			return result;
		}
	}
	public static int show(String caption, String message, int type, Thread thread){
		synchronized (UiApplication.getEventLock()) {
			getInstance().setMsg(message);
			getInstance().setCaption(caption);
			getInstance().initializeButton(type);
			if(thread!=null)
				thread.start();
			UiApplication.getUiApplication().pushModalScreen(getInstance());
			return result;
		}
	}
	public static void setMessage(String message){
		synchronized (UiApplication.getEventLock()) {
			getInstance().setMsg(message);
				
		}
	}
	public static void hide(){
		synchronized(UiApplication.getEventLock()){
			result = NONE;
			UiApplication.getUiApplication().popScreen(getInstance());
		}
	}
	public static void setPad(int top, int right, int bottom, int left){
		getInstance().mngContent.setPadding(top, right, bottom, left);
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
}
