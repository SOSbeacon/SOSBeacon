package view.custom;

import util.Util;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;

public class FCLabelField extends LabelField {
	public FCLabelField(Object text, long style) {
        super(text, style);
        setFont(Util.getFont(7));
    }
	public FCLabelField(Object text){
		this(text,0);
	}
	public FCLabelField(int color,Object text){
		this(text);
		this.mFontColor=color;
	}
    private int mFontColor = -1;

    public void setFontColor(int fontColor) {
        mFontColor = fontColor;
    }

    protected void paint(Graphics graphics) {
        if (-1 != mFontColor)
            graphics.setColor(mFontColor);
        super.paint(graphics);
    }

}
