package view.custom;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;
import util.Util;

public class MultiLineTextField extends VerticalFieldManager {
	private int managerWidth;
	private int managerHeight;
	private EditField editField;
	Bitmap backgroundBitmap,focusBitmap;
	Background bg,focus;
	public MultiLineTextField(int width, int height) {
		super(Manager.NO_VERTICAL_SCROLL);
		managerWidth = width;
		managerHeight = height;
		backgroundBitmap = Bitmap.getBitmapResource("textboxunfocus.png");
		focusBitmap = Bitmap.getBitmapResource("textboxfocus.png");
		backgroundBitmap = Util.resizeBitmap(backgroundBitmap, width, height);
		focusBitmap = Util.resizeBitmap(focusBitmap, width, height);
		VerticalFieldManager vfm = new VerticalFieldManager(
				Manager.VERTICAL_SCROLL){
			protected void sublayout(int maxWidth, int maxHeight) {
				// TODO Auto-generated method stub
				super.sublayout(maxWidth, maxHeight);
				setExtent(managerWidth, managerHeight);
			}
		};
		bg = BackgroundFactory.createBitmapBackground(backgroundBitmap);
		focus = BackgroundFactory.createBitmapBackground(focusBitmap);
		setBackground(bg);
		editField = new EditField(Field.FOCUSABLE | Field.EDITABLE | Field.NON_SPELLCHECKABLE) {
			public void paint(Graphics g) {
				getManager().invalidate();
				super.paint(g);
			}
		};
		vfm.setPadding(10, 10, 10, 10);
		vfm.add(editField);
		editField.setFont(Util.getFont(7));
		vfm.setFocusListener(new FocusChangeListener() {
			
			public void focusChanged(Field field, int eventType) {
				// TODO Auto-generated method stub
				if(eventType==FOCUS_GAINED){
					repaint(true);
				}else repaint(false);
			}
		});
		add(vfm);

	}
	
	void repaint(boolean isFocus){
		if(isFocus){
			setBackground(focus);
		}else setBackground(bg);
		invalidate();
	}
	public void paint(Graphics g) {
		super.paint(g);
		g.drawRect(0, 0, getWidth(), getHeight());
	}

	public void sublayout(int width, int height) {
		if (managerWidth == 0) {
			managerWidth = width;
		}
		if (managerHeight == 0) {
			managerHeight = height;
		}
		super.sublayout(managerWidth, managerHeight);
		setExtent(managerWidth, managerHeight);
	}

	public String getText() {
		return editField.getText();
	}

	public void setText(String text) {
		editField.setText(text);
	}

}
