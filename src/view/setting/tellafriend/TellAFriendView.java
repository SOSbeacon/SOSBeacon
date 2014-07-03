package view.setting.tellafriend;

import icontroller.setting.tellafriend.ITellAFriendController;
import iview.setting.tellafriend.ITellAFriendView;
import model.setting.email.EmailModel;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import util.Util;
import view.AView;
import view.custom.FCLabelField;
import view.custom.GridManager;
import view.custom.Header;
import view.custom.ImageButtonField;
import view.custom.MessageBox;
import view.custom.MultiLineTextField;
import view.custom.OneLineTextField;
import bitmaps.AccountInformationViewBitmaps;
import bitmaps.HeaderBitmaps;
import controller.setting.tellafriend.TellAFriendController;

public class TellAFriendView extends AView implements ITellAFriendView{

	/**
	 * 
	 */
	ITellAFriendController controller;
	public final int MARGIN=7;
	public TellAFriendView() {
		super();
		controller = new TellAFriendController(this,new EmailModel());
		// TODO Auto-generated constructor stub
		Header header = new Header("Tell a friend");
		add(header);
		Font font = Util.getFont(6,Font.BOLD);
		ImageButtonField btnBack = new ImageButtonField("Back", HeaderBitmaps.getButtonBg(),HeaderBitmaps.getButtonFocusBg(),font,Color.WHITE,0);
		btnBack.setMargin((Header.getHeaderHeight()-btnBack.getPreferredHeight())/2, 0, 0, 10);
		btnBack.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.back();
			}
		});
		header.add(btnBack);
		VerticalFieldManager mngRight = new VerticalFieldManager(
				Field.USE_ALL_WIDTH | Field.FIELD_RIGHT);
		ImageButtonField btnSend =new ImageButtonField("Send", HeaderBitmaps.getButtonBg(),HeaderBitmaps.getButtonFocusBg(),Util.getFont(6,Font.BOLD),Color.WHITE,Field.FIELD_RIGHT);
		btnSend.setMargin(
				(header.getPreferredHeight() - btnBack.getPreferredHeight()) / 2,
				10, 0, 0);
		btnSend.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				controller.send("to","bcc","subject","content");
			}
		});
		mngRight.add(btnSend);
		//mngRight.setBackground(BackgroundFactory.createSolidBackground(Color.RED));
		header.add(mngRight);
		
		VerticalFieldManager mngContent=new VerticalFieldManager(Manager.NO_VERTICAL_SCROLL|Manager.NO_VERTICAL_SCROLLBAR|Field.FIELD_VCENTER|Field.USE_ALL_HEIGHT|Field.USE_ALL_WIDTH);
		//mngContent.setBackground(BackgroundFactory.createBitmapBackground(AccountInformationViewBitmaps.getAccountInformationViewBg()));
		mngContent.setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		GridManager mngWrapper = new GridManager(2,Field.USE_ALL_WIDTH|Field.FIELD_VCENTER);
		//mngWrapper.setBackground(BackgroundFactory.createSolidBackground(Color.GRAY));
		FCLabelField lblTo=new FCLabelField(Color.GRAY,"To:");
		FCLabelField lblCC=new FCLabelField(Color.GRAY,"Cc/Bcc:");
		FCLabelField lblSubject=new FCLabelField(Color.GRAY,"Subject:");
		FCLabelField lblContent=new FCLabelField(Color.GRAY,"Content:");
		OneLineTextField txtTo=new OneLineTextField("",100,0);
		OneLineTextField txtCC=new OneLineTextField("",100,0);
		txtCC.setMargin(MARGIN, 0, MARGIN, 0);
		MultiLineTextField txtContent = new MultiLineTextField(250,120);
		//OneLineTextField txtContent=new OneLineTextField("",100,0);
		txtContent.setMargin(MARGIN, 0, MARGIN, 0);
		OneLineTextField txtSubject=new OneLineTextField("",100,0);
		mngWrapper.add(lblTo);
		mngWrapper.add(txtTo);
		mngWrapper.add(lblCC);
		mngWrapper.add(txtCC);
		mngWrapper.add(lblSubject);
		mngWrapper.add(txtSubject);
		mngWrapper.add(lblContent);
		mngWrapper.add(txtContent);
		add(mngContent);
		mngContent.add(mngWrapper);
		mngWrapper.setMargin(50, 0, 0, 0);
	}
	public void showMessage(String message) {
		// TODO Auto-generated method stub
		MessageBox.show(message);
	}

}
