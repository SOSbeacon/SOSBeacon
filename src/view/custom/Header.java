package view.custom;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;

public class Header extends HorizontalFieldManager{
	Bitmap backgroundBitmap;
	public Header(){
		this("");
	}
	String title;
	public Header(String title){
		backgroundBitmap=Bitmap.getBitmapResource("bg_titilebar.png");
		Background bg=BackgroundFactory.createBitmapBackground(backgroundBitmap, Background.POSITION_X_LEFT, Background.POSITION_Y_CENTER, Background.REPEAT_BOTH);
		setBackground(bg);
		this.title=title;
		try{
			setFont(FontFamily.forName("BBAlpha Sans").getFont(Font.PLAIN,7,Ui.UNITS_pt));
		}catch(Exception e){
			
		}
	}
	protected void paint(Graphics graphics) {
		// TODO Auto-generated method stub
		super.paint(graphics);
		int old=graphics.getColor();
		graphics.setColor(Color.WHITE);
		graphics.drawText(title, (getPreferredWidth()-getFont().getAdvance(title))/2, (getPreferredHeight()-getFont().getHeight())/2);
		graphics.setColor(old);
	}
	public int getPreferredHeight() {
		// TODO Auto-generated method stub
		return backgroundBitmap.getHeight();
	}
	public int getPreferredWidth() {
		// TODO Auto-generated method stub
		return Display.getWidth();
	}
	protected void sublayout(int maxWidth, int maxHeight) {
		// TODO Auto-generated method stub
		super.sublayout(maxWidth, maxHeight);
		setExtent(getPreferredWidth(), getPreferredHeight());
	}
	public static int getHeaderHeight(){
		return 44;
	}
}
