//
//  LoginView.m
//  SOSBEACON
//
//  Created by Tran Ngoc Anh on 08/06/2010.
//  Copyright 2010 CNC. All rights reserved.
//

#import "LoginView.h"
#import "SOSBEACONAppDelegate.h"
#import "RestConnection.h"
#import "ValidateData.h"
#import "TableGroup.h"
#import "HomeView.h"
#import "AppSetting.h"
#import "LoginView.h"

@implementation LoginView
@synthesize restConnection,txtEmail,txtPassWord,txtName,actSignup,txtPhoneNumber;
@synthesize username,password,uniqueIdentifier,phone,waitingActive,waitAcitveView;
@synthesize timer,txtnewPhone;
// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
	
    [super viewDidLoad];
	NSLog(@"view did load register");
	loadIndex1 = 0;
	
	scrollView.contentSize = CGSizeMake(320, 600);
	//get imei
	UIDevice *device = [UIDevice currentDevice];
	uniqueIdentifier = [[NSString alloc] initWithString:[device uniqueIdentifier]];
	waitingActive=TRUE;
	notConnected=1;
	countWaitActive=0;
	actSignup.hidden=YES;
	RestConnection *tempConnection = [[RestConnection alloc] initWithBaseURL:SERVER_URL];
	[self setRestConnection:tempConnection];
	[tempConnection release];
	tempConnection = nil;
	
	restConnection.delegate = self;
	appDelegate = (SOSBEACONAppDelegate*)[[UIApplication sharedApplication] delegate];
	
	
	NSString *file=[DOCUMENTS_FOLDER stringByAppendingPathComponent:@"detailUser.plist"];
	if([[NSFileManager defaultManager] fileExistsAtPath:file])
	{
		
		NSMutableDictionary *abc = [[NSMutableDictionary alloc] initWithContentsOfFile:file];
		
		if ([[abc objectForKey:@"active"] isEqualToString:@"actived"]) {

			txtName.text = [abc objectForKey:@"userName"];
			txtEmail.text=[abc objectForKey:@"eMail"];
			txtPassWord.text = [abc objectForKey:@"passWord"];
			txtPhoneNumber.text = [abc objectForKey:@"Phone"];
			self.username = [abc objectForKey:@"eMail"];
			self.password = [abc objectForKey:@"passWord"];
			self.phone = txtPhoneNumber.text;
	
		}
		[abc release];
	}
	
}

- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    // Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)dealloc {
	[scrollView release];
	[phone release];
	[password release];
	[username release];
	[uniqueIdentifier release];
	[txtPhoneNumber release];
	[restConnection release];
	[txtEmail release];
	[txtPassWord release];
	[txtName release];
	[actSignup release];
    [super dealloc];
}

- (void) DimisAlertView:(UIAlertView*)alertView {
	
	[alertView dismissWithClickedButtonIndex:2 animated:TRUE];
}

#pragma mark Action

-(void)getData {
	[AlertWaitting dismissWithClickedButtonIndex:2 animated:YES];
	[[NSURLCache sharedURLCache] removeAllCachedResponses];
	appDelegate = (SOSBEACONAppDelegate*)[[UIApplication sharedApplication] delegate];
	RestConnection *tempConnection = [[RestConnection alloc] initWithBaseURL:SERVER_URL];
	[self setRestConnection:tempConnection];
	[tempConnection release];
	tempConnection = nil;
	restConnection.delegate = self;
	//API get user pass
	[restConnection getPath:[NSString stringWithFormat:@"/rest?email=%@&password=%@&phoneNumber=%@&imei=%@&format=json",
							 self.username,self.password,self.phone,uniqueIdentifier] withOptions:nil];
}

-(IBAction)signUp:(id)sender {
	loadIndex1 = 0;
	self.username = txtEmail.text;
	self.password = txtPassWord.text;
	
	NSString * cutStringPhone=[NSString stringWithString:txtPhoneNumber.text];
	
	cutStringPhone=[cutStringPhone stringByReplacingOccurrencesOfString:@"(" withString:@""];
	cutStringPhone=[cutStringPhone stringByReplacingOccurrencesOfString:@")" withString:@""];
	cutStringPhone=[cutStringPhone stringByReplacingOccurrencesOfString:@"-" withString:@""];
	cutStringPhone=[cutStringPhone stringByReplacingOccurrencesOfString:@"." withString:@""];
	cutStringPhone=[cutStringPhone stringByReplacingOccurrencesOfString:@" " withString:@""];
	
	self.phone = cutStringPhone;
	actSignup.hidden=NO;
	[actSignup startAnimating];
	btnExit.enabled = FALSE;
	btnSubmit.enabled = FALSE;
	
	
	if ([txtName.text isEqualToString:@""] )
	{
		btnExit.enabled = TRUE;
		btnSubmit.enabled = TRUE;
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error"
															message:@"Username must be entered to proceed"
														   delegate:nil
												  cancelButtonTitle:@"Ok"
												  otherButtonTitles:nil];
		[alertView show];
		//Delay appear alertView
		[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:CONF_DIALOG_DELAY_TIME];
		[alertView release];
		[actSignup stopAnimating];
		actSignup.hidden=YES;
		return;
	}
	
	else if ([txtEmail.text isEqualToString:@""] )
	{
		btnExit.enabled = TRUE;
		btnSubmit.enabled = TRUE;
		
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error"
															message:@"Email must be entered to proceed"
														   delegate:nil
												  cancelButtonTitle:@"Ok"
												  otherButtonTitles:nil];
		[alertView show];
		[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:CONF_DIALOG_DELAY_TIME];
		[alertView release];
		[actSignup stopAnimating];
		actSignup.hidden=YES;
		return;
	}
	
	else if (!checkMail(txtEmail.text)) {
		btnExit.enabled = TRUE;
		btnSubmit.enabled = TRUE;
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Email Error"
															message:NSLocalizedString(@"EmailNotValid",@"")
														   delegate:nil
												  cancelButtonTitle:nil
												  otherButtonTitles:@"Ok",nil];
		[alertView show];
		[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:CONF_DIALOG_DELAY_TIME];
		[alertView release];
		[actSignup stopAnimating];
		actSignup.hidden=YES;
		
		return;
	}
	else if ([txtPassWord.text isEqualToString:@""] )
	{
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error"
															message:@"Password must be entered to proceed"
														   delegate:nil
												  cancelButtonTitle:@"Ok"
												  otherButtonTitles:nil];
		[alertView show];
		[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:CONF_DIALOG_DELAY_TIME];
		[alertView release];
		[actSignup stopAnimating];
		actSignup.hidden=YES;
		return;
	}
	else if (!(checkPassWord(txtPassWord.text)))
	{
		btnExit.enabled = TRUE;
		btnSubmit.enabled = TRUE;
		
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error"
															message:@"Password is invalid"
														   delegate:nil
												  cancelButtonTitle:@"Ok"
												  otherButtonTitles:nil];
		[alertView show];
		[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:CONF_DIALOG_DELAY_TIME];
		[alertView release];
		[actSignup stopAnimating];
		actSignup.hidden=YES;
		return;
	}
	
	else if ([txtPhoneNumber.text isEqualToString:@""] )
	{
		btnExit.enabled = TRUE;
		btnSubmit.enabled = TRUE;
		
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error"
															message:@"Phonenumber must be entered to proceed"
														   delegate:nil
												  cancelButtonTitle:@"Ok"
												  otherButtonTitles:nil];
		[alertView show];
		[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:CONF_DIALOG_DELAY_TIME];
		[alertView release];
		[actSignup stopAnimating];
		actSignup.hidden=YES;
		return;
	}
	
	else if(!(checkPhone(phone))){
		btnExit.enabled = TRUE;
		btnSubmit.enabled = TRUE;
		
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Phone number Error"
															message:@"Phone number must be number"
														   delegate:nil
												  cancelButtonTitle:nil
												  otherButtonTitles:@"Ok",nil];
		[alertView show];
		[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:CONF_DIALOG_DELAY_TIME];
		[alertView release];
		[actSignup stopAnimating];
		actSignup.hidden=YES;
		return;
	}
	
	else
	{
		
		// post information of new user
		NSArray *key = [NSArray arrayWithObjects:@"name",@"email",@"password",@"imei",@"phoneNumber",@"phoneType",nil];
		NSArray *obj = [NSArray arrayWithObjects:txtName.text,username,password,uniqueIdentifier,phone,@"1",nil];
		NSDictionary *params = [NSDictionary dictionaryWithObjects:obj forKeys:key];
		
		[restConnection postPath:@"/users?format=json" withOptions:params];	
		
	}
	
}

- (IBAction)textFieldDoneEditing:(id)sender {
	[sender resignFirstResponder];	
}

- (IBAction)backgroundTap:(id)sender {
	[txtPhoneNumber resignFirstResponder];
	[txtName resignFirstResponder];
	[txtPassWord resignFirstResponder];
	[txtEmail resignFirstResponder];
}

- (IBAction) Cancel{
	exit(0);
}

#pragma mark finishRequest

-(void)finishRequest:(NSDictionary*)arrayData andRestConnection:(RestConnection*)connector {
	
	if (loadIndex1==0) {
		
		
		if([[[arrayData objectForKey:@"response"] objectForKey:@"success"] isEqualToString:@"true"])
		{
			
			NSString *file=[DOCUMENTS_FOLDER stringByAppendingPathComponent:@"detailUser.plist"];
			if([[NSFileManager defaultManager] fileExistsAtPath:file])
			{
				
				NSMutableDictionary *abc = [[NSMutableDictionary alloc] initWithContentsOfFile:file];
				
				if ([[abc objectForKey:@"active"] isEqualToString:@"actived"]) {
					username = [abc objectForKey:@"eMail"];
					password = [abc objectForKey:@"passWord"];
					
					
				}
				[abc release];
			}
			
			[restConnection getPath:[NSString stringWithFormat:@"/rest?email=%@&password=%@&phoneNumber=%@&imei=%@&format=json",
									 username,password,phone,uniqueIdentifier] withOptions:nil];
			loadIndex1=1;
						
		}
		else {
			btnExit.enabled = TRUE;
			btnSubmit.enabled = TRUE;
			NSLog(@"Edit information error");	
			if ([[arrayData objectForKey:@"response"] objectForKey:@"message"]) {
				UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Message"
																	message:[[arrayData objectForKey:@"response"] objectForKey:@"message"]
																   delegate:nil
														  cancelButtonTitle:@"Ok"
														  otherButtonTitles:nil];
				[alertView show];
				[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:2];
				[alertView release];
			}
			else if ([[arrayData objectForKey:@"response"] objectForKey:@"error"]) {
				UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error"
																	message:[[arrayData objectForKey:@"response"] objectForKey:@"error"]
																   delegate:nil
														  cancelButtonTitle:@"Ok"
														  otherButtonTitles:nil];
				[alertView show];
				[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:2];
				[alertView release];
			}
			
			[actSignup stopAnimating];
			actSignup.hidden=YES;
			return;			
		}
	}
	else if (loadIndex1==1) {
		if([[[arrayData objectForKey:@"response"] objectForKey:@"success"] isEqualToString:@"true"])
		{
			//login true	
			appDelegate.apiKey = [[arrayData objectForKey:@"response"] objectForKey:@"token"];
			appDelegate.userID = [[[arrayData objectForKey:@"response"] objectForKey:@"user_id"] intValue];
			appDelegate.phoneID =[[[arrayData objectForKey:@"response"] objectForKey:@"phone_id"] intValue];
			//save user & pass
			NSArray *key = [NSArray arrayWithObjects:@"username",@"password",@"phoneNumber",nil];
			NSArray *obj = [NSArray arrayWithObjects:username,password,phone,nil];
			NSDictionary *dict = [NSDictionary dictionaryWithObjects:obj forKeys:key];
			[dict writeToFile:[NSString stringWithFormat:@"%@/info.plist",DOCUMENTS_FOLDER] atomically:YES];
			loadIndex1 = 2;
			// API get information of user
			[restConnection getPath:[NSString stringWithFormat:@"/phones/%d?token=%@&format=json",appDelegate.phoneID,appDelegate.apiKey] withOptions:nil];
			NSString *file=[DOCUMENTS_FOLDER stringByAppendingPathComponent:@"detailUser.plist"];
			
			if([[NSFileManager defaultManager] fileExistsAtPath:file])
			{
				[[NSFileManager defaultManager] removeItemAtPath:file error:nil];
				
			}
			waitingActive=FALSE;
			//[appDelegate.waitActiveView.view removeFromSuperview];
			appDelegate.tabBarController.selectedIndex=0;
			
		}
		else 
		{
			
			
			NSString *file=[DOCUMENTS_FOLDER stringByAppendingPathComponent:@"detailUser.plist"];
			
			if(![[NSFileManager defaultManager] fileExistsAtPath:file])
			{
				[[NSFileManager defaultManager] createFileAtPath:file contents:nil attributes:nil];
				dictDetail = [[NSMutableDictionary alloc] init];
				[dictDetail setObject:txtName.text forKey:@"userName"];
				[dictDetail setObject:txtEmail.text forKey:@"eMail"];
				[dictDetail setObject:txtPassWord.text forKey:@"passWord"];
				[dictDetail setObject:txtPhoneNumber.text forKey:@"Phone"];
				[dictDetail setObject:@"actived" forKey:@"active"];
				[dictDetail writeToFile:file atomically:YES];
				
				[txtPhoneNumber resignFirstResponder];
				[txtName resignFirstResponder];
				[txtPassWord resignFirstResponder];
				[txtEmail resignFirstResponder];
				
			}
			
			btnExit.enabled = TRUE;
			btnSubmit.enabled = TRUE;
			
			[actSignup stopAnimating];
			countWaitActive++;
			if (countWaitActive<3) {
				if (countWaitActive==0) {
					//
				}
				else if (countWaitActive==1)
				{
					NSLog(@"one");
					UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Login Fail"
																		message:[[arrayData objectForKey:@"response"] objectForKey:@"message"]
																	   delegate:self
															  cancelButtonTitle:@"OK"
															  otherButtonTitles:nil];
					notConnected=1;
					[alertView show];
					[alertView release];
				}
				else if (countWaitActive==2) {
					NSLog(@"two");
					countWaitActive=0;
					UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Message"
																		message:@"Did you receive text messages to activate?"
																	   delegate:self
															  cancelButtonTitle:@"Yes"
															  otherButtonTitles:@"No",nil];
					[alertView show];
					notConnected=2;
					//loadIndex1=0;
					[actSignup stopAnimating];
					[alertView release];
				}
				
			}
			else {
				countWaitActive=0;
			}
			
		} 
		
	}
	else if(loadIndex1==2)
	{
		if([[[arrayData objectForKey:@"response"] objectForKey:@"success"] isEqualToString:@"true"]){
			
			if ([[[arrayData objectForKey:@"response"] objectForKey:@"number"] isEqual:[NSNull null]])
				[appDelegate.informationArray setObject:@"" forKey:@"number"];
			
			else 
				[appDelegate.informationArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"number"] forKey:@"number"];	
			[appDelegate.informationArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"username"] forKey:@"username"];
			[appDelegate.informationArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"email"] forKey:@"email"];	
			appDelegate.settingId = [[[arrayData objectForKey:@"response"] objectForKey:@"settingId"] intValue];
			loadIndex1=3;
			// API get setting
			[restConnection getPath:[NSString stringWithFormat:@"/setting/%d?phoneid=%d&token=%@&format=json",appDelegate.settingId,appDelegate.phoneID,appDelegate.apiKey] withOptions:nil];
			
		}
		else {
			NSLog(@"get phone error");
		}
		
	}
	else if(loadIndex1==3){
		if([[[arrayData objectForKey:@"response"] objectForKey:@"success"] isEqualToString:@"true"]){
			//load setting
			if ([[[arrayData objectForKey:@"response"] objectForKey:@"good_samaritan_range"] isEqual:[NSNull null]]
				||[[[arrayData objectForKey:@"response"] objectForKey:@"panic_alert_good_samaritan_range"] isEqual:[NSNull null]])
			{
				[appDelegate.settingArray setObject:@"0" forKey:ST_ReciveRange];
				[appDelegate.settingArray setObject:@"0" forKey:ST_SamaritanRange];
				
			}else
			{
				[appDelegate.settingArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"voice_duration"] forKey:ST_VoiceRecordDuration];
				[appDelegate.settingArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"location_duration"] forKey:ST_LocationReporting];
				[appDelegate.settingArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"panic_number"] forKey:ST_EmergencySetting];
				[appDelegate.settingArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"alert_sendto_group"] forKey:ST_SendToAlert];
				[appDelegate.settingArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"good_samaritan_range"] forKey:ST_ReciveRange];
				[appDelegate.settingArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"good_samaritan_status"] forKey:ST_ReceiverSamaritan];
				[appDelegate.settingArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"incoming_government_alert"] forKey:ST_IncomingGovernment];
				[appDelegate.settingArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"panic_alert_good_samaritan_status"] forKey:ST_SamaritanStatus];
				[appDelegate.settingArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"panic_alert_good_samaritan_range"] forKey:ST_SamaritanRange];	
				
				NSLog(@"%^$%^ emergency Phone is :%@",[[arrayData objectForKey:@"response"] objectForKey:@"panic_number"]);
				if ([[[arrayData objectForKey:@"response"] objectForKey:@"panic_number"] isEqualToString:@"0"]) {
					UIAlertView *alertPhone = [[UIAlertView alloc] initWithTitle:@"Message" message:@"Emergency phone number not yet set for your location" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
					[alertPhone show];
					[alertPhone release];
				}
			}
			[self sendLocationAuto];
		}
		else {
			NSLog(@"Get setting error");
		}
		//appDelegate.tabBarController.selectedIndex=0;
		[self dismissModalViewControllerAnimated:YES];
		
		//tabBarController1.selectedIndex==0
	}	
	else if	(loadIndex1==4)
	{
	
	}
}

- (void)cantConnection:(NSError *)error andRestConnection:(id)connector{
	btnExit.enabled = TRUE;
	btnSubmit.enabled = TRUE;
	[appDelegate hiddenSplash];
	[actSignup stopAnimating];
	actSignup.hidden=YES;
	UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error Internet Connection"
													    message:NSLocalizedString(@"Cannotgetdata",@"")
													   delegate:self
											  cancelButtonTitle:@"OK"
											  otherButtonTitles:nil];
	notConnected=0;
	[alertView show];
	[alertView release];	
}

- (void)sendLocationAuto {
	loadIndex1=4;
	
	[restConnection postPath:[NSString stringWithFormat:@"/location?phoneid=%d&token=%@&latitude=%@&longtitude=%@&format=json",appDelegate.phoneID,appDelegate.apiKey,appDelegate.latitudeString,appDelegate.longitudeString] withOptions:nil];
	[self delayAlert];
	
	
	
	
	
}
- (void)delayAlert {
	
	[NSObject cancelPreviousPerformRequestsWithTarget:self selector:@selector(sendLocationAuto) object:nil];
	[self performSelector:@selector(sendLocationAuto) withObject:nil afterDelay:1800]; //default la 1800~30";
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
	if (buttonIndex==0) {
		if (notConnected==1) {
			btnExit.enabled=FALSE;
			btnSubmit.enabled=FALSE;
			txtPhoneNumber.enabled=FALSE;
			txtEmail.enabled=FALSE;
			txtName.enabled=FALSE;
			txtPassWord.enabled=FALSE;
			timer = [NSTimer scheduledTimerWithTimeInterval:30.0 target:self selector:@selector(getData) userInfo:nil repeats:NO];
			AlertWaitting = [[UIAlertView alloc] initWithTitle:nil
																  message:@"Waiting for activation..." 
																 delegate:nil 
														cancelButtonTitle:nil 
														otherButtonTitles:nil];
			actSignup.frame=CGRectMake(130.0, 50.0, 37.0, 37.0);
			[AlertWaitting addSubview:actSignup];
			notConnected=4;
			[AlertWaitting show];
			
			[actSignup startAnimating];
			
		}
		else if (notConnected==2) {
			
			[self getData];
		}
		else if (notConnected==5) {
			txtPhoneNumber.enabled=TRUE;
			txtEmail.enabled=TRUE;
			txtName.enabled=TRUE;
			txtPassWord.enabled=TRUE;
			btnExit.enabled=TRUE;
			btnSubmit.enabled=TRUE;
			[txtPhoneNumber becomeFirstResponder];
			NSString *file=[DOCUMENTS_FOLDER stringByAppendingPathComponent:@"detailUser.plist"];
			
			if([[NSFileManager defaultManager] fileExistsAtPath:file])
			{
				[[NSFileManager defaultManager] removeItemAtPath:file error:nil];
			}
		}
		
		else if (notConnected==0) {
			exit(0);
		}
	}
	else if (buttonIndex==1) {
		
		notConnected=5;
		UIAlertView *myAlertView = [[UIAlertView alloc] initWithTitle:@"Message" 
															  message:@"Please verify your mobile phone number" 
															 delegate:self 
													cancelButtonTitle:@"Ok" 
													otherButtonTitles:nil];
		
		[myAlertView show];
		[myAlertView release];
		
	}
	
}

@end
