package http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

public class HttpRequestDispatcher extends Thread{
	String url;
	String method;
	byte[] postData;
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
	public HttpRequestDispatcher(String url,String method,byte[] postData){
		this.url=url;
		this.method=method;
		this.postData=postData;
	}
	public void run() {
		// TODO Auto-generated method stub
		int count=0;
		try{
			HttpConnection connection;
			try {
				connection = (HttpConnection) Connector.open(url
						+ ";interface=wifi");
			} catch (Exception e) {
				connection = (HttpConnection)Connector.open(url);	
			}
			count++;
			connection.setRequestMethod(method);count++;
			if(method.equals("POST")&&postData!=null){
				connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");count++;
				OutputStream requestOutput=connection.openOutputStream();count++;
				requestOutput.write(postData);count++;
				requestOutput.close();count++;
			}
			int responseCode=connection.getResponseCode();count++;
			if(responseCode!=HttpConnection.HTTP_OK){
				//failed;
				errorNotify("Connection failed: "+responseCode);count++;
				connection.close();count++;
				return;
			}
			ByteArrayOutputStream baos=new ByteArrayOutputStream();count++;
			InputStream responseData=connection.openInputStream();count++;
			byte[] buffer=new byte[10000];count++;
			int bytesRead=responseData.read(buffer);count++;
			while(bytesRead>0){
				baos.write(buffer,0,bytesRead);count++;
				bytesRead=responseData.read(buffer);count++;
			}
			responsedNotify(baos.toString());count++;
			baos.close();count++;
			connection.close();count++;
		}catch(IOException ex){
			errorNotify("Exception count="+count+": "+ex.getMessage());
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
