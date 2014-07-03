package view;

import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.container.MainScreen;

public abstract class AView extends MainScreen {
	public AView(){
		super(Manager.NO_VERTICAL_SCROLL | Manager.NO_VERTICAL_SCROLLBAR);
	}
	public boolean onClose() {
		// TODO Auto-generated method stub
		setDirty(false);
		return super.onClose();
	}
}
