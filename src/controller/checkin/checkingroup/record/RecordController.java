package controller.checkin.checkingroup.record;

import icontroller.checkin.checkingroup.record.IRecordController;
import imodel.checkin.checkingroup.record.IRecordModel;
import iview.checkin.checkingroup.record.IRecordView;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import util.Navigation;

public class RecordController implements IRecordController{

	IRecordView view;
	IRecordModel model;
	static Vector audioPaths = new Vector(),imagePaths = new Vector();
	public static void clear(){
		audioPaths = new Vector();
		imagePaths = new Vector();
	}
	public RecordController(IRecordView view, IRecordModel model){
		this.view=view;
		this.model=model;
	}
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	public void back() {
		// TODO Auto-generated method stub
		Navigation.goBack();
	}
	public void save(byte[] voiceNote, String[] fileNames) {
		// TODO Auto-generated method stub
		try {
			String fileName = "file:///SDCard/BlackBerry/"+System.currentTimeMillis()+".amr";
			FileConnection file = (FileConnection)Connector.open(fileName,Connector.READ_WRITE);
			if(file.exists()){
				file.delete();
			}
			file.create();
			OutputStream output = file.openOutputStream();
			output.write(voiceNote);
			output.close();
			file.close();
			audioPaths.addElement(fileName);
			for(int i=0;i<fileNames.length;i++){
				imagePaths.addElement(fileNames[i]);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static String[] getImagePaths(){
		int length = imagePaths.size();
		System.out.println("CNC.Debug: "+length+"------------------------");
		String[]  result = new String[length];
		for(int i=0;i<length;i++){
			result[i]=(String)imagePaths.elementAt(i);
		}
		return result;
	}
	public static String[] getAudioPaths(){
		int length = audioPaths.size();
		System.out.println("CNC.Debug: "+length+"------------------------");
		String[]  result = new String[length];
		for(int i=0;i<length;i++){
			result[i]=(String)audioPaths.elementAt(i);
		}
		return result;
	}
}
