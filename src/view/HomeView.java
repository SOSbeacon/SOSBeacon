package view;

import bitmaps.HomeViewBitmaps;
import icontroller.IHomeController;
import iview.IHomeView;
import model.HomeModel;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import view.custom.Header;
import view.custom.ImageButtonField;
import controller.HomeController;

public class HomeView extends AView implements IHomeView {

	IHomeController controller;
	/**
	 * 
	 */
	
	ButtonField btnAccept,btnCancel;
	protected void onDisplay() {
		// TODO Auto-generated method stub
		super.onDisplay();
		controller.initialize();
	}

	LabelField termContent;
	
	public HomeView() {
		super();
		// TODO Auto-generated constructor stub
		controller=new HomeController(this,new HomeModel());
		
		HorizontalFieldManager mngHeader=new HorizontalFieldManager(Manager.NO_HORIZONTAL_SCROLL|Manager.NO_HORIZONTAL_SCROLLBAR);
		mngHeader.add(new Header("Terms of Service"));
		
		VerticalFieldManager mngContent=new VerticalFieldManager(Manager.NO_VERTICAL_SCROLL|Manager.NO_VERTICAL_SCROLLBAR|Field.USE_ALL_HEIGHT);
		mngContent.setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		
		VerticalFieldManager mngPopup=new VerticalFieldManager(Manager.NO_VERTICAL_SCROLL|Manager.NO_VERTICAL_SCROLLBAR);
		mngPopup.setBackground(BackgroundFactory.createSolidBackground(Color.WHITE));
		//mngPopup.setBorder(BorderFactory.createRoundedBorder(new XYEdges(10,10,10,10), Color.RED, Border.STYLE_SOLID));
		mngPopup.setPadding(10, 10, 0, 10);
		termContent=new LabelField();
		termContent.setMargin(0, 0, 50, 0);
		mngPopup.add(termContent);
		HorizontalFieldManager mngButton=new HorizontalFieldManager(Field.FIELD_HCENTER);
		ImageButtonField btnAccept = new ImageButtonField("Accept", HomeViewBitmaps.getButtonBg(), HomeViewBitmaps.getButtonFocusBg(),null,Color.BLACK,0);
		btnAccept.setMargin(0, 10, 10, 0);
		mngButton.add(btnAccept);
		ImageButtonField btnCancel = new ImageButtonField("Cancel", HomeViewBitmaps.getButtonBg(), HomeViewBitmaps.getButtonFocusBg(),null,Color.BLACK,0);
		mngButton.add(btnCancel);
		mngPopup.add(mngButton);
		mngContent.add(mngPopup);
		btnAccept.setChangeListener(new FieldChangeListener() {
			
			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.accept();
			}
		});
		btnCancel.setChangeListener(new FieldChangeListener() {
			
			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.cancel();
			}
		});
		
		add(mngHeader);
		add(mngContent);
	}
	
	public void showTerm(String term) {
		// TODO Auto-generated method stub
		termContent.setText(term);
	}

}
