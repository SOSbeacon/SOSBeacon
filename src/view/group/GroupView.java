package view.group;

import icontroller.group.IGroupController;
import iview.group.IGroupView;
import model.group.GroupModel;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import view.AView;
import view.custom.Header;
import view.custom.ListStyleButtonField;
import view.custom.ListStyleButtonSet;
import view.custom.MessageBox;
import view.custom.ProgressPopup;
import controller.group.GroupController;
import entities.ContactGroup;

public class GroupView extends AView implements IGroupView{

	/**
	 * 
	 */
	IGroupController controller;
	VerticalFieldManager mngContent;
	public GroupView() {
		 super();
		 controller =new GroupController(this,new GroupModel());
	     add(new Header("Alert Groups"));
	     mngContent = new VerticalFieldManager(Field.USE_ALL_HEIGHT|Field.USE_ALL_WIDTH);
	     mngContent.setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
	     add(mngContent);
	}
	
	protected void onDisplay() {
		// TODO Auto-generated method stub
		super.onDisplay();
		controller.initialize();
	}

	public void showProgress(String msg) {
		// TODO Auto-generated method stub
		ProgressPopup.show(msg);
	}

	public void hideProgress() {
		// TODO Auto-generated method stub
		ProgressPopup.hide();
	}

	Bitmap _caret = Bitmap.getBitmapResource( "caret.png" );
	public void fillGroups(ContactGroup[] groups) {
		// TODO Auto-generated method stub
		synchronized(Application.getEventLock()){
			ListStyleButtonSet buttonSet = new ListStyleButtonSet();

			int length= groups.length;
			for(int i=0;i<length;i++){
				ListStyleButtonField btn = new ListStyleButtonField(groups[i].getName(), _caret);
				buttonSet.add(btn);
				btn.setChangeListener(new SelectGroup(groups[i]));
			}
			mngContent.add(buttonSet);
		}
	}

	public void showMessage(String message) {
		// TODO Auto-generated method stub
		MessageBox.show(message);
	}
	class SelectGroup implements FieldChangeListener{

		ContactGroup gr;
		public SelectGroup(ContactGroup gr){
			this.gr=gr;
		}
		public void fieldChanged(Field field, int context) {
			// TODO Auto-generated method stub
			controller.selectContactGroup(gr);
		}
		
	}

}
