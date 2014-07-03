package view.checkin.checkingroup.record;

import icontroller.checkin.IUpdateUi;
import icontroller.checkin.VideoControlWrapper;
import icontroller.checkin.VideoRecorderThread;
import icontroller.checkin.checkingroup.record.IRecordController;
import icontroller.needhelp.VoiceNotesRecorderThread;
import iview.checkin.checkingroup.record.IRecordView;
import model.checkin.checkingroup.record.RecordModel;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import util.Util;
import view.AView;
import view.custom.FCLabelField;
import view.custom.Header;
import view.custom.ImageButtonField;
import view.custom.MessageBox;
import view.custom.ProgressPopup;
import bitmaps.HeaderBitmaps;
import controller.checkin.checkingroup.record.RecordController;

public class RecordView extends AView implements IRecordView {
	/**
	 * 
	 */
	VideoRecorderThread vRecord;
	VoiceNotesRecorderThread aRecord;
	VideoControlWrapper vControl;
	IRecordController controller;

	int timeRecord ;
//	protected boolean invokeAction(int action) {
//		boolean handled = super.invokeAction(action);
//
//		if (!handled) {
//			if (action == ACTION_INVOKE) {
//				try {
//					vControl.takeSnapshot();
//				} catch (Exception e) {
//					MessageBox.show(e.toString());
//				}
//			}
//		}
//		return handled;
//	}

	void back(){
		vRecord.stop();
		aRecord.stop();
		//controller.save(aRecord.getVoiceNote(),
				//vControl.getFileNames());
	}
	protected boolean keyChar(char c, int status, int time) {
		// TODO Auto-generated method stub
		try {
			if (c == Keypad.KEY_ESCAPE) {
				back();
				return true;
			}
		} catch (Exception e) {
			showMessage("Count=" +  e.getMessage());
		}
		return super.keyChar(c, status, time);
	}

	protected void onUiEngineAttached(boolean attached) {
		// TODO Auto-generated method stub
		super.onUiEngineAttached(attached);
		if (attached) {
			controller.initialize();
//			vRecord = new VideoRecorderThread(60, vControl, new IUpdateUi() {
//				
//				public void onUpdate(Object o) {
//					// TODO Auto-generated method stub
//					Integer i = (Integer)o;
//					int time = 60-i.intValue();
//					synchronized(UiApplication.getEventLock()){
//						if(time>=10){
//							if(time%10==3){
//								lblStatus.setText("Auto capture.");
//							}else if(time%10==2){
//								lblStatus.setText("Auto capture..");
//							}else if(time%10==1){
//								lblStatus.setText("Auto capture...");
//							}
//						}else lblStatus.setText("");
//						lblTime.setText("Time: "+time+"/60");
//					}
//				}
//			},new Runnable() {
//				
//				public void run() {
//					// TODO Auto-generated method stub
//					controller.back();
//				}
//			});
//			vRecord.start();
			try {
				vControl.initialize();
				Field vField = vControl.getVideoField();
				if (vField != null) {
					mngCapture = new HorizontalFieldManager() {
						public int getPreferredHeight() {
							return vControl.getDisplayHeight();
						}

						public int getPreferredWidth() {
							return vControl.getDisplayWidth();
						}
					};
					mngCapture.add(vField);
					mngCapture.setMargin(
							(mngContent.getPreferredHeight() - mngCapture
									.getPreferredHeight()) / 2, 0, 0,
							(mngContent.getPreferredWidth() - mngCapture
									.getPreferredWidth()) / 2);
					mngContent.add(mngCapture);
					vRecord = new VideoRecorderThread(timeRecord, vControl, new IUpdateUi() {
						
						public void onUpdate(Object o) {
							// TODO Auto-generated method stub
							Integer i = (Integer)o;
							int time = timeRecord-i.intValue();
							synchronized(UiApplication.getEventLock()){
								if(time>=10){
									if(time%10==3){
										lblStatus.setText("Auto capture.");
									}else if(time%10==2){
										lblStatus.setText("Auto capture..");
									}else if(time%10==1){
										lblStatus.setText("Auto capture...");
									}else
										lblStatus.setText("");
								}else lblStatus.setText("");
								lblTime.setText("Time: "+time+"/"+timeRecord);
							}
						}
					},new Runnable() {
						
						public void run() {
							// TODO Auto-generated method stub
							vControl.stop(new Runnable() {
								
								public void run() {
									// TODO Auto-generated method stub
									UiApplication.getUiApplication().invokeLater(new Runnable() {
										
										public void run() {
											// TODO Auto-generated method stub
											aRecord.stop();
											controller.save(aRecord.getVoiceNote(),
													vControl.getFileNames());
											controller.back();
										}
									});
								}
							});
						}
					});
					aRecord = new VoiceNotesRecorderThread();
					aRecord.start();
					vRecord.start();
				}
			} catch (Exception e) {
				MessageBox.show("onEngine: " + e.getMessage());
			}
		}
	}

	HorizontalFieldManager mngCapture;
	VerticalFieldManager mngContent;
	FCLabelField lblTime,lblCount,lblStatus;
	public RecordView(int timeRecord) {
		this.timeRecord=timeRecord;
		controller = new RecordController(this, new RecordModel());
		vControl = new VideoControlWrapper(new IUpdateUi() {
			
			public void onUpdate(Object o) {
				// TODO Auto-generated method stub
				synchronized(UiApplication.getEventLock()){
					lblCount.setText("Photo: "+o);
				}
			}
		});
		// // TODO Auto-generated constructor stub
		Header header = new Header();
		Header footer = new Header();
		final int height = header.getPreferredHeight();
		mngContent = new VerticalFieldManager() {
			protected void sublayout(int maxWidth, int maxHeight) {
				// TODO Auto-generated method stub
				super.sublayout(maxWidth, maxHeight);
				setExtent(getPreferredWidth(), getPreferredHeight());
			}

			public int getPreferredHeight() {
				// TODO Auto-generated method stub
				return Display.getHeight() - 2 * height;
			}

			public int getPreferredWidth() {
				// TODO Auto-generated method stub
				return Display.getWidth();
			}
		};
		Bitmap bg = Bitmap.getBitmapResource("settings_hor.png");
		bg = Util.resizeBitmap(bg, mngContent.getPreferredWidth(),
				mngContent.getPreferredHeight());
		mngContent.setBackground(BackgroundFactory.createBitmapBackground(bg));
		add(header);
		add(mngContent);
		add(footer);
		final Font font = Util.getFont(6);
		lblTime=new FCLabelField(Color.WHITE,""){
			public int getPreferredWidth() {
				return font.getAdvance("Time: 60/60");
			}
		};
		lblStatus = new FCLabelField(Color.WHITE,"");
		lblStatus.setFont(font);
		lblCount=new FCLabelField(Color.WHITE,""){
			public int getPreferredWidth() {
				return font.getAdvance("Photo: 10");
			}
		};
		lblTime.setFont(font);
		lblCount.setFont(font);
		int h = getFont().getHeight();
		
		//header.add(lblTime);
		footer.add(lblCount);
		Bitmap bgCapture = Bitmap.getBitmapResource("capture.png");
		Bitmap bgFocus = Bitmap.getBitmapResource("focus_big.png");
		bgFocus = Util.resizeBitmap(bgFocus, bgCapture.getWidth(), bgCapture.getHeight());
		ImageButtonField btnCapture = new ImageButtonField("",bgCapture,bgFocus);
		btnCapture.setMargin((Header.getHeaderHeight()-btnCapture.getPreferredHeight())/2, 0, 0, (Display.getWidth()-btnCapture.getPreferredWidth())/2-lblCount.getPreferredWidth()-lblCount.getMarginLeft()-lblCount.getMarginRight());
		footer.add(btnCapture);
		footer.add(lblStatus);
		ImageButtonField btnBack = new ImageButtonField("Back", HeaderBitmaps.getButtonBg(),HeaderBitmaps.getButtonFocusBg(),Util.getFont(6,Font.BOLD),Color.WHITE,0);
		btnBack.setMargin(
				(header.getPreferredHeight() - btnBack.getPreferredHeight()) / 2,
				0, 0, 5);
		btnBack.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				back();
			}
		});
		btnCapture.setChangeListener(new FieldChangeListener() {
			
			public void fieldChanged(Field field, int context) {
				// TODO Auto-generated method stub
				vControl.takeSnapshot();
			}
		});
		header.add(btnBack);
		VerticalFieldManager mngRight = new VerticalFieldManager(
				Field.USE_ALL_WIDTH | Field.FIELD_RIGHT);
		mngRight.add(lblTime);
		header.add(mngRight);
		lblTime.setMargin((header.getPreferredHeight()-h)/2, 0, 0, Display.getWidth()-btnBack.getPreferredWidth()-btnBack.getMarginLeft()-btnBack.getMarginRight()-lblTime.getPreferredWidth()-5);
		lblCount.setMargin((footer.getPreferredHeight()-h)/2, 0, 0, 5);
		lblStatus.setMargin((footer.getPreferredHeight()-font.getHeight())/2, 0, 0, 5);
	}

	public void showMessage(String message) {
		// TODO Auto-generated method stub
		MessageBox.show(message);
	}

	public void hideProgress() {
		// TODO Auto-generated method stub
		ProgressPopup.hide();
	}

	public void showProgress(String message) {
		// TODO Auto-generated method stub
		ProgressPopup.show(message);
	}

	public void updateProgress(String message) {
		// TODO Auto-generated method stub
		ProgressPopup.setMessage(message);
	}

}
