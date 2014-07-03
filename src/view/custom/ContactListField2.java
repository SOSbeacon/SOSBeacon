package view.custom;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import entities.Contact;

public class ContactListField2 extends ListField{
	public ContactListField2(){
		setRowHeight(50);
	}
	private boolean hasFocus = false;

	public void onFocus(int direction) {
		hasFocus = true;
	}

	public void onUnfocus() {
		hasFocus = false;
		super.onUnfocus();
		invalidate();
	}

	public void paint(Graphics graphics) {
		int width = Display.getWidth();
		// Get the current clipping region
		XYRect redrawRect = graphics.getClippingRect();
		if (redrawRect.y < 0) {
			throw new IllegalStateException("Error with clipping rect.");
		}

		// Determine the start location of the clipping region and end.
		int rowHeight = getRowHeight();
		int marginX = 10;
		int positionX = width - 10;
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
		final int be = 8;
		final int et = 10;
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
			graphics.setColor(Color.WHITE);
			graphics.fillRoundRect(xInds[0], yInds[0]+3, width, rowHeight-6, 18, 18);
			// graphics.drawLine(xInds[2], yInds[2], xInds[3], yInds[3]);
			try {
				FontFamily family = FontFamily.forName("BBAlpha Sans");
				Font font = family.getFont(Font.PLAIN, 6, Ui.UNITS_pt);
				int nameHeight = font.getHeight();
				Font font2 = family.getFont(Font.PLAIN, 6, Ui.UNITS_pt);
				int emailHeight = font2.getHeight();
				int mY = (rowHeight - nameHeight - emailHeight - be) / 2;
				int emailWidth = font2.getAdvance(email);
				int emailX = positionX - emailWidth - et;
				graphics.setFont(font);
				graphics.setColor(Color.BLACK);
				graphics.drawText(name, marginX, yInds[0] + mY);
				graphics.setFont(font2);
				graphics.setColor(Color.GRAY);
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
			// Assign new values to the y axis moving one row down.
			y += rowHeight;
			yInds[0] = y;
			yInds[1] = yInds[0];
			yInds[2] = y + rowHeight;
			yInds[3] = yInds[2];
		}

		// super.paint(graphics);
	}
}
