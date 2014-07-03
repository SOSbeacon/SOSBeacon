package controller.group.contacts;

import icontroller.group.contacts.IYahooImportController;
import imodel.group.contacts.GetContactsCompletedArgs;
import imodel.group.contacts.IGetContactsCompletedListener;
import imodel.group.contacts.IYahooGetAccessTokenListener;
import imodel.group.contacts.IYahooGetRequestTokenListener;
import imodel.group.contacts.IYahooImportModel;
import imodel.group.contacts.YahooGetAccessTokenCompletedArgs;
import imodel.group.contacts.YahooGetRequestTokenCompletedArgs;
import iview.group.contacts.IYahooImportView;
import net.rim.device.api.ui.UiApplication;
import util.Navigation;
import util.URLUTF8Encoder;
import util.Util;
import view.group.contacts.imports.ContactImportView;
import constants.Values;
import entities.Contact;
import entities.ContactGroup;

public class YahooImportController implements IYahooImportController {

	IYahooImportView view;
	IYahooImportModel model;
	ContactGroup group;
	private Contact[] contacts;
	private Contact[] add;
	private Contact[] edit;
	private Contact[] delete;
	public YahooImportController(IYahooImportView yahooImportView,
			IYahooImportModel yahooImportModel, ContactGroup group,Contact[] contacts,Contact[] add, Contact[] edit, Contact[] delete) {
		// TODO Auto-generated constructor stub
		view = yahooImportView;
		model = yahooImportModel;
		this.group=group;
		this.contacts=contacts;
		this.add=add;
		this.edit=edit;
		this.delete=delete;
	}

	IYahooGetRequestTokenListener li;
	IYahooGetAccessTokenListener ali;
	IGetContactsCompletedListener cli;
	public void initialize() {
		// TODO Auto-generated method stub
		if(li==null){
			li=new IYahooGetRequestTokenListener() {
				
				public void onGetRequestTokenCompleted(Object sender,
						YahooGetRequestTokenCompletedArgs arg) {
					// TODO Auto-generated method stub
					view.hideProgress();
					if(arg.isSuccess()){
						//view.showProgress("Getting User Authorization...");
						//model.getRequestAuth(arg.getOauthToken());
						a=arg;
						view.loadUrl(Values.YAHOO_GET_REQUEST_AUTH_URL+"?oauth_token="+arg.getOauthToken());
					}else{
						final String message = arg.getMessage();
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								view.showMessage(message);
							}
						});
						//view.showMessage(arg.getMessage());
					}
				}
			};
			ali=new IYahooGetAccessTokenListener() {
				
				public void onGetAccessTokenCompleted(Object sender,
						YahooGetAccessTokenCompletedArgs arg) {
					view.hideProgress();
					// TODO Auto-generated method stub
					if(arg.isSuccess()){
						System.out.println(this.getClass().toString()+": AccessToken: "+arg.getOauthToken()+"Token secret: "+arg.getOauthTokenSecret()+"GUID: "+arg.getXoauthYahooGuid());
						view.showProgress("Getting contacts...");
						String nonce=Util.getRandomString();
						String timestamp=Util.getTimeStamp();
						model.getContacts(arg.getXoauthYahooGuid(), "compact", "1.0", nonce, timestamp, Values.YAHOO_CONSUMER_KEY, arg.getOauthToken(),arg.getOauthTokenSecret());
					}else{
						final String message = arg.getMessage();
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								view.showMessage(message);
							}
						});
						//view.showMessage(arg.getMessage());
					}
				}
			};
			cli = new IGetContactsCompletedListener() {
				
				public void onGetContactsCompleted(Object sender,
						GetContactsCompletedArgs arg) {
					// TODO Auto-generated method stub
					view.hideProgress();
					if(arg.isSuccess()){
						Navigation.navigate(new ContactImportView(arg.getContacts(),group,"Yahoo Contacts",contacts,add,edit,delete));
					}else{
						final String message = arg.getMessage();
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							
							public void run() {
								// TODO Auto-generated method stub
								view.showMessage(message);
							}
						});
						//view.showMessage(arg.getMessage());
					}
				}
			};
			model.addGetAccessTokenCompletedListener(ali);
			model.addGetRequestTokenCompletedListener(li);
			model.addGetContactsCompletedListener(cli);
		}
		view.showProgress("Getting the request token...");
		String nonce=Util.getRandomString();
		String timespan=Util.getTimeStamp();
		/*
		 * oauth_callback=oob&
		 * oauth_consumer_key=dj0yJmk9OEJnWDZVRjBta1RZJmQ9WVdrOVVIWTNZakZLTXpBbWNHbzlNVFV5TXprd01UYzJNZy0tJnM9Y29uc3VtZXJzZWNyZXQmeD04Yw--&
		 * oauth_nonce=123456789&
		 * oauth_signature_method=HMAC-SHA1&
		 * oauth_timestamp=1311608404&
		 * oauth_version=1.0&
		 * xoauth_lang_pref=en-us
		 * */
		model.getRequestToken(Values.YAHOO_CONSUMER_KEY,nonce,"HMAC-SHA1",Values.YAHOO_CONSUMER_SECRET,timespan,"1.0","en-us","oob");
	}
	YahooGetRequestTokenCompletedArgs a;
	public void receiveVerifier(String verifierCode) {
		// TODO Auto-generated method stub
		/*
		 * https://api.login.yahoo.com/oauth/v2/get_token?
		 * oauth_consumer_key=dj0yJmk9OEJnWDZVRjBta1RZJmQ9WVdrOVVIWTNZakZLTXpBbWNHbzlNVFV5TXprd01UYzJNZy0tJnM9Y29uc3VtZXJzZWNyZXQmeD04Yw--
		 * &oauth_signature_method=plaintext
		 * &oauth_version=1.0
		 * &oauth_verifier=mu9vdm
		 * &oauth_token=gk47gtp
		 * &oauth_timestamp=1228169762
		 * &oauth_nonce=8B9SpF
		 * &oauth_signature=4f8cbed1ac24a2fe2121d6f16030226e0782fbd2%26dc33c687aab1ddbb521826cf1a580ad7b6b06d13
		 * */
		view.showProgress("Getting access token...");
		String nonce=Util.getRandomString();
		String timespan=Util.getTimeStamp();
		model.getAccessToken(Values.YAHOO_CONSUMER_KEY,nonce,"HMAC-SHA1",Values.YAHOO_CONSUMER_SECRET,a.getOauthTokenSecret(),timespan,"1.0",a.getOauthToken(),verifierCode);
	}
	public void back() {
		// TODO Auto-generated method stub
		Navigation.goBack();
	}

}
