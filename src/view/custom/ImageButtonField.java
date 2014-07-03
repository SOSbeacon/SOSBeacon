package view.custom;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.component.ButtonField;

public class ImageButtonField extends Field {

	private Bitmap[] _bitmaps;
    private static final int NORMAL = 0;
    private static final int CLICK = 1;
    private String label;
    
    public ImageButtonField(String label,Bitmap normalState )
    {        
        this(label, normalState, normalState, 0 );
    }
    
    public ImageButtonField(String label, Bitmap normalState, Bitmap clickState )
    {        
        this(label, normalState, clickState, 0 );
    }
    
    public ImageButtonField(String label, Bitmap normalState, Bitmap clickState, long style )
    {        
        super( Field.FOCUSABLE |ButtonField.CONSUME_CLICK| style );
        this.label=label;
        font=null;
        foreground=-1;
        if( (normalState.getWidth() != clickState.getWidth())
            || (normalState.getHeight() != clickState.getHeight()) ){
            
            throw new IllegalArgumentException( "Image sizes don't match" );
        }
        
        _bitmaps = new Bitmap[] { normalState, clickState };
    }
    public ImageButtonField(String label, Bitmap normalState, Bitmap clickState, Font font, int foreground, long style){
    	this(label,normalState,clickState,style);
    	this.font=font;
    	this.foreground=foreground;
    }
    Font font;
    int foreground;
    public void setLabel(String label){
    	this.label=label;
    	invalidate();
    }
    public void setImage( Bitmap normalState ){
        _bitmaps[NORMAL] = normalState;
        invalidate();
    }
    
    public void setClickImage( Bitmap focusState ){
        _bitmaps[CLICK] = focusState;
        invalidate();
    }
    
    public int getPreferredWidth() {
        return _bitmaps[NORMAL].getWidth();
    }
    
    public int getPreferredHeight() {
        return _bitmaps[NORMAL].getHeight();
    }
    
    protected void layout( int width, int height ) {
        setExtent( _bitmaps[NORMAL].getWidth(), _bitmaps[NORMAL].getHeight() );
    }
    
    protected void paint( Graphics g ) {
    	int index = g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS)?CLICK:NORMAL;
        g.drawBitmap( 0, 0, _bitmaps[index].getWidth(), _bitmaps[index].getHeight(), _bitmaps[index], 0, 0 );
        if(foreground!=-1)
        	g.setColor(foreground);
        if(font==null)
        	g.drawText(label,(getPreferredWidth()-getFont().getAdvance(label))/2 , (getPreferredHeight()-getFont().getHeight())/2);
        else{ 
        	g.setFont(font);
        	g.drawText(label,(getPreferredWidth()-font.getAdvance(label))/2 , (getPreferredHeight()-font.getHeight())/2);
        }
    }
    
    /**
     * With this commented out the default focus will show through
     * If an app doesn't want focus colours then it should override this and do nothing
     **/
    /*
    protected void paintBackground( Graphics g ) {
        // Nothing to do here
    }
    */
    
    protected void drawFocus( Graphics g, boolean on ) {
    	g.setDrawingStyle(Graphics.DRAWSTYLE_FOCUS, true);
    	paintBackground(g);
    	paint(g);
    }
    long touchedAt = -1;
    final long HOLD_TIME=2000;

	protected boolean touchEvent(TouchEvent message) {
		if(message.getX(1) < 0 || message.getX(1) > getWidth() || message.getY(1) < 0 || message.getY(1) > getHeight()) {
	        return false;
	    }
		if (message.getEvent() == TouchEvent.DOWN) {
			touchedAt = System.currentTimeMillis();
		} else if (message.getEvent() == TouchEvent.UP) {
			if (System.currentTimeMillis() - touchedAt < HOLD_TIME) {
				touchedAt = -1; // reset
				click();
			}
		}
		return true;
	}
    protected boolean navigationClick(int status, int time) {
    	// TODO Auto-generated method stub
    	click();
    	return true;
    }
    protected boolean keyChar(char character, int status, int time) {
    	// TODO Auto-generated method stub
    	if(character==Keypad.KEY_ENTER){
    		click();
    	}
    	return super.keyChar(character, status, time);
    }
    void click(){
    	fieldChangeNotify(0);
    }
}
