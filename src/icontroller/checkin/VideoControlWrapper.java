package icontroller.checkin;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VideoControl;

import net.rim.device.api.ui.Field;

public class VideoControlWrapper {
	Player player;
	VideoControl control;
	Field vField;
	IUpdateUi li;
	public VideoControlWrapper(){
		this(null);
	}
	public VideoControlWrapper(IUpdateUi li){
		this.li=li;
	}
	
	public boolean isReady() {
		return ready;
	}
	public void initialize() throws IOException, MediaException {
		fileNames = new Vector();
		ready=false;
		player = javax.microedition.media.Manager
				.createPlayer("capture://video?encoding=jpeg&width=1024&height=768");
		player.realize();
		player.start();
		control = (VideoControl) player.getControl("VideoControl");
		if(control!=null){
			vField=(Field) control.initDisplayMode(
					VideoControl.USE_GUI_PRIMITIVE,
			"net.rim.device.api.ui.Field");
			control.setDisplayFullScreen(false);
			control.setDisplaySize(150, 150);
			control.setVisible(true);
			ready=true;
		}
	}
	Vector fileNames;
	boolean ready;
	public void takeSnapshot(){
		if(ready){
			ready=false;
			try {
				byte[] data;
				data = control.getSnapshot("encoding=jpeg&width=1024&height=768");
				if(data!=null&&data.length>0){
					try {
						String fileName = "file:///SDCard/BlackBerry/"+System.currentTimeMillis()+".jpeg";
						FileConnection file = (FileConnection)Connector.open(fileName,Connector.READ_WRITE);
						if(file.exists()){
							file.delete();
						}
						file.create();
						OutputStream output = file.openOutputStream();
						output.write(data);
						output.close();
						file.close();
						fileNames.addElement(fileName);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} catch(Exception e){
				e.printStackTrace();
			}
			if(li!=null)
				li.onUpdate(new Integer(fileNames.size()));
			ready=true;
		}
	}
	public String[] getFileNames(){
		String[] files = new String[fileNames.size()];
		for(int i=0;i<files.length;i++){
			files[i] = (String)fileNames.elementAt(i);
		}
		return files;
	}
	public Field getVideoField(){
		return vField;
	}
	public void stop(Runnable closed){
		final Runnable c = closed;
		player.addPlayerListener(new PlayerListener() {
			
			public void playerUpdate(Player player, String event, Object eventData) {
				// TODO Auto-generated method stub
				if(event==PlayerListener.CLOSED){
					System.out.println("CNC.Debug: -------------------------------------------closed");
					c.run();
				}
				player.removePlayerListener(this);
			}
		});
		player.close();
	}
	public int getDisplayHeight(){
		return control.getDisplayHeight();
	}
	public int getDisplayWidth(){
		return control.getDisplayWidth();
	}
}
