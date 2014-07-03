package controller.setting.tellafriend;

import icontroller.setting.tellafriend.ITellAFriendController;
import imodel.setting.email.IEmailModel;
import iview.setting.tellafriend.ITellAFriendView;
import net.rim.blackberry.api.mail.Address;
import net.rim.blackberry.api.mail.Folder;
import net.rim.blackberry.api.mail.Message;
import net.rim.blackberry.api.mail.ServiceConfiguration;
import net.rim.blackberry.api.mail.Session;
import net.rim.blackberry.api.mail.Store;
import net.rim.blackberry.api.mail.Transport;
import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import util.Navigation;
import view.custom.MessageBox;

public class TellAFriendController implements ITellAFriendController {

	ITellAFriendView view;
	IEmailModel model;
	public TellAFriendController(ITellAFriendView view, IEmailModel model){
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

	public void send(String to, String cc, String subject, String content) {
		try{
		Session session = Session.getDefaultInstance();


		if (session != null) {

		//now the store

		Store store = session.getStore();

		//then the service configuration

		ServiceConfiguration serviceConfig = store.getServiceConfiguration();


		//now get the email address

		String emailAddress = serviceConfig.getEmailAddress();
		MessageBox.show("Email: "+emailAddress);

		}else MessageBox.show("session is null");
		
		}catch(Exception e){
			MessageBox.show("Exception: "+e.getMessage());
		}
		// TODO Auto-generated method stub
		//view.showMessage(to+cc+subject+content);
//		ServiceBook sb = ServiceBook.getSB();
//		ServiceRecord[] srs = sb.findRecordsByCid("CMIME");
//		if (srs != null) {
//			MessageBox.show(String.valueOf(srs.length));
//		    for (int i = srs.length -1; i >= 0; --i) {
//		        ServiceRecord sr = srs[i];
//		        try {
//		            ServiceConfiguration sc = new ServiceConfiguration(sr);
//		            Session session = Session.getInstance(sc);
//		            Store store = session.getStore();
//		            Folder[] folders = store.list(Folder.SENT); 
//		            Folder sentfolder = folders[0]; 
//		            //Create a new message and associate it with
//		            //the sent folder of the email account. The “From” field of the
//		            //new message will also be populated with the associated
//		            //email account address.
//		            Message msg = new Message(sentfolder); 
//		            Address[] addrs = new Address[1]; 
//		            addrs[0] = new Address("btrongbkhn@gmail.com", "Btrong"); 
//		            msg.addRecipients(Message.RecipientType.TO, addrs);
//		            msg.setSubject("Hello");
//		            msg.setContent("Hi there!");
//		            Transport.send(msg); 
//		        }
//		        catch (Exception e) {
//		        	MessageBox.show(e.getMessage());
//		        }
//		    }
//		}
		//Store store = Session.getDefaultInstance().getStore();

		//retrieve the sent folder
		//Folder[] folders = store.list(Folder.SENT);
		//Folder sentfolder = folders[0];

		//create a new message and store it in the sent folder
//		Message msg = new Message();
//		Address recipients[] = new Address[1];
//		int count=0;
//		try {
//		     recipients[0]= new Address("btrongbkhn@gmail.com", "btrong");count++;
//		     msg.setFrom(new Address("btrongbkhn@gmail.com","Tet"));count++;
//		     //add the recipient list to the message
//		     msg.addRecipients(Message.RecipientType.TO, recipients);count++;
//
//		     //set a subject for the message
//		     msg.setSubject("Test email");count++;
//
//		     //sets the body of the message
//		     msg.setContent("This is a test email from my BlackBerry Wireless Handheld");count++;
//
//		     //sets priority
//		     msg.setPriority(Message.Priority.HIGH);count++;
//
//		     //send the message
//		     Transport.send(msg);count++;
//		 }
//		catch (Exception me) {
//		     System.err.println(me);
//		     MessageBox.show("Count= "+count+me.getMessage());
//		}
	}

}
