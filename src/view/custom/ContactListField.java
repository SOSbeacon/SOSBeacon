package view.custom;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import entities.Contact;

public class ContactListField extends ListField {
	// 0,ListField.MULTI_SELECT
	private boolean hasFocus = false;

	public void onFocus(int direction) {
		hasFocus = true;
	}

	public void onUnfocus() {
		hasFocus = false;
		super.onUnfocus();
		invalidate();
	}

	static Bitmap uncheck = Bitmap.getBitmapResource("uncheck.png");
	static Bitmap check = Bitmap.getBitmapResource("check.png");

	public void paint(Graphics graphics) {
		int width = Display.getWidth();
		// Get the current clipping region
		XYRect redrawRect = graphics.getClippingRect();
		if (redrawRect.y < 0) {
			throw new IllegalStateException("Error with clipping rect.");
		}

		// Determine the start location of the clipping region and end.
		int rowHeight = getRowHeight();
		int marginY = (rowHeight - check.getHeight()) / 2;
		int marginX = 10;
		int positionX = width - check.getWidth() - 10;
		int curSelected;

		// If the ListeField has focus determine the selected row.
		if (hasFocus) {
			curSelected = getSelectedIndex();

		} else {
			curSelected = -1;
		}

		int startLine = redrawRect.y / rowHeight;
		int endLine = (redrawRect.y + redrawRect.height - 1) / rowHeight;
		endLine = Math.min(endLine, getSize() - 1);
		int y = startLine * rowHeight;

		// Setup the data used for drawing.
		int[] yInds = new int[] { y, y, y + rowHeight, y + rowHeight };
		int[] xInds = new int[] { 0, width, width, 0 };

		// Set the callback - assuming String values.
		ListFieldCallback callBack = this.getCallback();

		// Draw each row
		int be = 4;
		int et = 10;
		for (; startLine <= endLine; ++startLine) {
			 //If the line we're drawing is the currentlySelected line then draw
			// the fill path in LIGHTYELLOW and the
			 //font text in Black.
			Contact c = (Contact) callBack.get(this, startLine);
			String name = c.getName();
			String phone = c.getTextPhone();
			String email = c.getEmail();
			if(startLine==curSelected){
				graphics.setColor(0xFF0080FF);
			}else graphics.setColor(Color.BLACK);
			graphics.drawFilledPath(xInds, yInds, null, null);
			graphics.setColor(0xFF313031);
			graphics.drawLine(xInds[0], yInds[0], xInds[1], yInds[1]);
			// graphics.drawLine(xInds[2], yInds[2], xInds[3], yInds[3]);
			graphics.setColor(Color.LIGHTGREY);
			try {
				FontFamily family = FontFamily.forName("BBAlpha Sans");
				Font font = family.getFont(Font.BOLD, 6, Ui.UNITS_pt);
				int nameHeight = font.getHeight();
				Font font2 = family.getFont(Font.PLAIN, 6, Ui.UNITS_pt);
				int emailHeight = font2.getHeight();
				int mY = (rowHeight - nameHeight - emailHeight - be) / 2;
				int emailWidth = font2.getAdvance(email);
				int emailX = positionX - emailWidth - et;
				graphics.setFont(font);
				graphics.drawText(name, marginX, yInds[0] + mY);
				graphics.setFont(font2);
				graphics.drawText(phone, marginX, yInds[0] + nameHeight + mY
						+ be);
				graphics.drawText(email, emailX, yInds[0] + nameHeight + mY
						+ be);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// graphics.drawText((String) callBack.get(this, startLine), 0,
			// yInds[0] + marginY);
			if (isSelected(startLine))
				graphics.drawBitmap(positionX, yInds[0] + marginY,
						check.getWidth(), check.getHeight(), check, 0, 0);
			else
				graphics.drawBitmap(positionX, yInds[0] + marginY,
						uncheck.getWidth(), uncheck.getHeight(), uncheck, 0, 0);
			// Assign new values to the y axis moving one row down.
			y += rowHeight;
			yInds[0] = y;
			yInds[1] = yInds[0];
			yInds[2] = y + rowHeight;
			yInds[3] = yInds[2];
		}

		// super.paint(graphics);
	}

	Vector selected = new Vector();

	public int getRowHeight() {
		// TODO Auto-generated method stub
		//return 50;
		 return super.getRowHeight();
	}

	private Vector selectedIndexes = new Vector();

	public boolean isSelected(int index) {
		int length = selectedIndexes.size();
		for (int i = 0; i < length; i++) {
			if (((Integer) selectedIndexes.elementAt(i)).intValue() == index) {
				return true;
			}
		}
		return false;
	}

	public void select(int index) {
		if (!isSelected(index))
			selectedIndexes.addElement(new Integer(index));
	}

	public void unselect(int index) {
		int length = selectedIndexes.size();
		for (int i = 0; i < length; i++) {
			if (((Integer) selectedIndexes.elementAt(i)).intValue() == index) {
				selectedIndexes.removeElementAt(i);
				return;
			}
		}
	}
	protected boolean touchEvent(TouchEvent message) {
		// TODO Auto-generated method stub
		if(message.getEvent()==TouchEvent.CLICK){
			click();
			return true;
		}
		return super.touchEvent(message);
	}
	protected boolean navigationClick(int status, int time) {
		// TODO Auto-generated method stub
		click();
		return true;
	}
	protected boolean keyChar(char key, int status, int time) {
		// TODO Auto-generated method stub
		if(key==Keypad.KEY_ENTER){
			click();
			return true;
		}
		return super.keyChar(key, status, time);
	}
	void click(){
		if(hasFocus){
			this.fieldChangeNotify(2);
			int selected=getSelectedIndex();
			if(isSelected(selected)){
				unselect(selected);
			}else select(selected);
		}
	}
	public Contact[] getSelectedContacts(){
		ListFieldCallback callBack = this.getCallback();
		int length=selectedIndexes.size();
		Contact[] result = new Contact[length];
		for(int i=0;i<length;i++){
			Integer index =  (Integer)selectedIndexes.elementAt(i);
			result[i] =(Contact)callBack.get(this, index.intValue());
		}
		return result;
	}
}
