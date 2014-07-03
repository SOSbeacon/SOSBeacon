package view.custom;

import util.Util;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;

public class OneLineTextField extends Manager {
	private Field _editField;
	private int _border = 2;
	private int _padding = 5;
	private HorizontalFieldManager _textMgr;
	private boolean isPasswordEdit;

	public OneLineTextField(String initialValue, int maxChars, long style) {
		this(false, initialValue, maxChars, style);
	}

	public OneLineTextField(boolean isPassword, String initialValue,
			int maxChars, long style) {
		super(NO_HORIZONTAL_SCROLL | NO_VERTICAL_SCROLL | Field.FOCUSABLE);
		bacgroundBitmap = Bitmap.getBitmapResource("textboxunfocus.png");
		focusBitmap = Bitmap.getBitmapResource("textboxfocus.png");
		// System.out.println("TEXTBOXFI: "+bacgroundBitmap.getWidth()+","+bacgroundBitmap.getHeight());
		normal = BackgroundFactory.createBitmapBackground(bacgroundBitmap);
		focus = BackgroundFactory.createBitmapBackground(focusBitmap);
		_textMgr = new HorizontalFieldManager(HORIZONTAL_SCROLL);
		this.isPasswordEdit = isPassword;
		if (!isPassword)
			_editField = new EditField("", initialValue, maxChars, style
					| TextField.NO_NEWLINE | Field.FOCUSABLE | Field.EDITABLE
					| Field.NON_SPELLCHECKABLE);
		else
			_editField = new PasswordEditField("", initialValue);
		_textMgr.add(_editField);
		add(_textMgr);
		_textMgr.setFocusListener(new FocusChangeListener() {

			public void focusChanged(Field field, int eventType) {
				// TODO Auto-generated method stub
				if (eventType == FOCUS_GAINED) {
					repaint(true);
				} else {
					repaint(false);
				}
			}
		});
		_textMgr.setFont(Util.getFont(7));
		setBackground(BackgroundFactory.createBitmapBackground(bacgroundBitmap));
	}

	void repaint(boolean focus) {
		if (focus)
			setBackground(this.focus);
		else
			setBackground(normal);
		invalidate();
	}

	public String getText() {
		if (isPasswordEdit)
			return ((PasswordEditField) _editField).getText();
		else
			return ((EditField) _editField).getText();
	}

	public void setBorderThickness(int border) {
		_border = border;
		updateLayout();
	}

	public void setPaddingWidth(int padding) {
		_padding = padding;
		updateLayout();
	}

	public void setText(String newText) {
		if (isPasswordEdit)
			((PasswordEditField) _editField).setText(newText);
		else
			((EditField) _editField).setText(newText);
	}

	public void setFont(Font font) {
		_editField.setFont(font);
		updateLayout();
	}

	protected void sublayout(int w, int h) {
		int height = 0;
		int width = 0;
		w = bacgroundBitmap.getWidth() + width;
		layoutChild(_textMgr, w - width - 2 * (_border + _padding), h - 2
				* (_border + _padding));
		width += _textMgr.getWidth() + 2 * (_border + _padding);
		height = Math.max(height, _textMgr.getHeight() + 2
				* (_border + _padding));
		height = Math.max(height, bacgroundBitmap.getHeight());
		setPositionChild(_textMgr, _border + _padding,
				(height - _textMgr.getHeight()) / 2);
		setExtent(bacgroundBitmap.getWidth(), height);
	}

	// protected void paintBackground(Graphics g) {
	// int width = _textMgr.getWidth() + 2 * (_border + _padding);
	// int height = _textMgr.getHeight() + 2 * (_border + _padding);
	// int hPos = _label.getWidth();
	// height=Math.max(height, bmp.getHeight());
	// g.clear();
	//
	// int prevColor = g.getColor();
	// g.setColor(0x00000000);
	// g.drawBitmap(hPos, 0, width, height, bmp, 0, 0);
	// g.setColor(prevColor);
	// }
	Bitmap bacgroundBitmap;
	Bitmap focusBitmap;
	Background normal, focus;

	public int getPreferredHeight() {
		// TODO Auto-generated method stub
		return bacgroundBitmap.getHeight();
	}
	public int getPreferredWidth() {
		// TODO Auto-generated method stub
		//return super.getPreferredWidth();
		return bacgroundBitmap.getWidth();
	}
}