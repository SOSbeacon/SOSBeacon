package http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public class HttpUploadDispatcher extends Thread{
	static final String BOUNDARY = "----------V2ymHFg03ehbqgZCaKO6jy";

	byte[] postBytes = null;
	String url = null;

	public HttpUploadDispatcher(String url, Hashtable params, String fileField, String fileName, String fileType, byte[] fileBytes) throws Exception
	{
		this.url = url;
		String boundary = getBoundaryString();
		String boundaryMessage = getBoundaryMessage(boundary, params, fileField, fileName, fileType);
		String endBoundary = "\r\n--" + boundary + "--\r\n";
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bos.write(boundaryMessage.getBytes());
		bos.write(fileBytes);
		bos.write(endBoundary.getBytes());
		this.postBytes = bos.toByteArray();
		bos.close();
	}

	String getBoundaryString(){
		return BOUNDARY;
	}

	public void run() {
		try {
			send();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			errorNotify(e.getMessage());
			e.printStackTrace();
		}
	}
	
	String getBoundaryMessage(String boundary, Hashtable params, String fileField, String fileName, String fileType){
		StringBuffer res = new StringBuffer("--").append(boundary).append("\r\n");
		Enumeration keys = params.keys();
		while(keys.hasMoreElements()){
			String key = (String)keys.nextElement();
			String value = (String)params.get(key);
			res.append("Content-Disposition: form-data; name=\"").append(key).append("\"\r\n")    
			.append("\r\n").append(value).append("\r\n")
			.append("--").append(boundary).append("\r\n");
		}
		res.append("Content-Disposition: form-data; name=\"").append(fileField).append("\"; filename=\"").append(fileName).append("\"\r\n") 
		.append("Content-Type: ").append(fileType).append("\r\n\r\n");
		return res.toString();
	}

	public void send() throws Exception{
		HttpConnection hc = null;
		InputStream is = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] res = null;
		try{
			try{
				hc = (HttpConnection) Connector.open(url);
			}catch(Exception e){
				hc = (HttpConnection)Connector.open(url+ ";interface=wifi");
			}
			hc.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + getBoundaryString());
			hc.setRequestMethod(HttpConnection.POST);
			OutputStream dout = hc.openOutputStream();
			dout.write(postBytes);
			dout.close();
			int ch;
			is = hc.openInputStream();
			while ((ch = is.read()) != -1){
				bos.write(ch);
			}
			res = bos.toByteArray();
		}
		catch(Exception e){
			errorNotify("Exception e1: "+e.getMessage());
			e.printStackTrace();
		}
		finally{
			try{
				if(bos != null)
					bos.close();
				if(is != null)
					is.close();
				if(hc != null)
					hc.close();
			}
			catch(Exception e2){
				errorNotify("Exception e2" + e2.getMessage());
				e2.printStackTrace();
			}
		}
		if(res!=null){
			String response=new String(res);
			responsedNotify(response);
		}
	}
	Vector listeners=new Vector();
	public void addChangeListener(IHttpListener li){
		if(!listeners.contains(li)){
			listeners.addElement(li);
		}
	}
	public void removeChangeListener(IHttpListener li){
		if(listeners.contains(li)){
			listeners.removeElement(li);
		}
	}
	protected void responsedNotify(String response){
		int l=listeners.size();
		for(int i=0;i<l;i++){
			((IHttpListener)listeners.elementAt(i)).onHttpOk(this,response);
		}
	}
	protected void errorNotify(String message){
		int l=listeners.size();
		for(int i=0;i<l;i++){
			((IHttpListener)listeners.elementAt(i)).onHttpError(this, message);
		}
	}
}