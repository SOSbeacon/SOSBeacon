//
//  AppSetting.m
//  SOSBEACON
//
//  Created by cncsoft on 7/12/10.
//  Copyright 2010 CNC. All rights reserved.
//

#import "AppSetting.h"
#import "SOSBEACONAppDelegate.h"

#import "ValidateData.h"
#import "LoginView.h"
#import "TableGroup.h"
#import "Personal.h"
#import "SOWebView.h"
#import "HomeView.h"
#import "EmergencyView.h"
#import "Register.h"
#import "TermsService.h"

@implementation AppSetting

@synthesize scrollView,imageDuration;
@synthesize voiceRecord;
@synthesize locationReporting,editIndex;
@synthesize lblSendToAlert,lblSamaritanRange;
@synthesize rest,incomingGovernment,samaritanStatus;
@synthesize actSetting,txtPanicPhone,txtEmail,txtuserName,txtPassWord,txtPhoneNumber,btnSave,btnRangeSamaritan;
@synthesize receiverSamaritan,receiveRange,receiveRangeStatus,btnFamily;
@synthesize btnResetSetting,loadIndex,isEdit;

#pragma mark -
#pragma mark UIview

- (void)viewDidLoad {
	[super viewDidLoad];
	loadIndex = 0;
	isEdit = NO;
	
	scrollView.contentSize=CGSizeMake(320, 2250);
	
	rest = [[RestConnection alloc] initWithBaseURL:SERVER_URL];
	rest.delegate =self;	
	appDelegate = (SOSBEACONAppDelegate*)[[UIApplication sharedApplication] delegate];
	
	// set default value
	if ([[appDelegate.settingArray objectForKey:ST_SendToAlert] intValue] == 1) 
		selectAlert = @"1";
	else if ([[appDelegate.settingArray objectForKey:ST_SendToAlert] intValue] ==2 )
		selectAlert = @"2";
	else if ([[appDelegate.settingArray objectForKey:ST_SendToAlert] intValue] ==3 )
		selectAlert = @"3";
	else if ([[appDelegate.settingArray objectForKey:ST_SendToAlert] intValue] ==4 )
		selectAlert = @"4";
	else if ([[appDelegate.settingArray objectForKey:ST_SendToAlert] intValue] ==5 )
		selectAlert = @"5";
	else 
		selectAlert = @"0";

	//set default value for recevieRange
	if ([[appDelegate.settingArray objectForKey:ST_ReciveRange] intValue] == 1) {
		selectReciveRange = @"1";
	}
	else if([[appDelegate.settingArray objectForKey:ST_ReciveRange] intValue] == 3) {
		selectReciveRange = @"3";
	}
	else if([[appDelegate.settingArray objectForKey:ST_ReciveRange] intValue] == 5) {
		selectReciveRange = @"5";
	}
	else if([[appDelegate.settingArray objectForKey:ST_ReciveRange] intValue] == 10) {
		selectReciveRange = @"10";
	}
	else if([[appDelegate.settingArray objectForKey:ST_ReciveRange] intValue] == 20) {
		selectReciveRange = @"20";
	}
	else {
		selectReciveRange = @"0";
	}
	
	if ([[appDelegate.settingArray objectForKey:ST_SamaritanRange] intValue] == 1) {
		selectSamaritanRange = @"1";
	}
	else if([[appDelegate.settingArray objectForKey:ST_SamaritanRange] intValue] == 3) {
		selectSamaritanRange = @"3";
	}
	else if([[appDelegate.settingArray objectForKey:ST_SamaritanRange] intValue] == 5) {
		selectSamaritanRange = @"5";
	}
	else if([[appDelegate.settingArray objectForKey:ST_SamaritanRange] intValue] == 10) {
		selectSamaritanRange = @"10";
	}
	else if([[appDelegate.settingArray objectForKey:ST_SamaritanRange] intValue] == 20) {
		selectSamaritanRange = @"20";
	}
	else {
		selectSamaritanRange = @"0";
	}
	
}

- (void)viewDidUnload {
	self.btnRangeSamaritan = nil;
	self.btnSave = nil;
	self.txtPhoneNumber = nil;
	self.txtPassWord = nil;
	self.samaritanStatus = nil;
	self.incomingGovernment = nil;
	self.txtPanicPhone = nil;
	self.scrollView = nil;
	self.imageDuration = nil;
	self.voiceRecord = nil;
	self.locationReporting = nil;
	self.lblSendToAlert = nil;
	self.lblSamaritanRange = nil;
	self.txtuserName = nil;
	self.txtEmail = nil;
	self.actSetting = nil;
	[super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated {
	isEdit = NO;
	[super viewWillAppear:animated];
	[self loadData];
}

- (void)viewDidDisappear:(BOOL)animated {
	[super viewDidDisappear:animated];
	
	NSLog(@"save setting");
	if(isEdit){
		alertIndex = 9;
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Save settings"
															message:@"Do you want to save settings changes?"
														   delegate:self
												  cancelButtonTitle:@"Ok"
												  otherButtonTitles:@"Cancel",nil];
		[alertView show];
		[alertView release];
		
	}
}


- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    NSLog(@"Setting...........");
    // Release any cached data, images, etc that aren't in use.¥
}

- (void)dealloc {
	[selectReciveRange release];
	[selectSamaritanRange release];
	[selectAlert release];
	[btnResetSetting release];
	[btnFamily release];
	[receiveRangeStatus release];
	[receiveRange release];
	[btnRangeSamaritan release];
	[btnSave release];
	[tb release];
	[personal release];
	[rest release];
	
	[txtPanicPhone release];
	
	[txtPassWord release];
	[txtPhoneNumber release];
	[txtuserName release];
	[txtEmail release];
	
	[lblSamaritanRange release];
	[locationReporting release];
	[lblSendToAlert release];
	
	[actSetting release];
	[scrollView release];
	
	[samaritanStatus release];
	[incomingGovernment release];
	[imageDuration release];
	[voiceRecord release];
	[imageDuration release];
	[locationReporting release];
	
	[super dealloc];
}													

#pragma mark IBActionsheet

-(IBAction)choicesImageRecord{
	editIndex = 1;
	UIActionSheet *actionSheet = [[UIActionSheet alloc] initWithTitle:@""
															 delegate:self 
													cancelButtonTitle:@"Cancel"
											   destructiveButtonTitle:nil 
													otherButtonTitles:@"5 seconds",@"10 seconds",@"15 seconds",@"30 seconds",nil];
	actionSheet.actionSheetStyle = UIActionSheetStyleBlackOpaque;
	[actionSheet showInView:appDelegate.window];
	[actionSheet release];
}

-(IBAction)choicesRecordingDuration{
	editIndex = 2;
	UIActionSheet *actionSheet1 = [[UIActionSheet alloc] initWithTitle:@""
															  delegate:self 
													 cancelButtonTitle:@"Cancel"
												destructiveButtonTitle:nil 
													 otherButtonTitles:@"1 Session",@"2 Sessions",@"3 Sessions",@"4 Sessions",@"5 Sessions",@"6 Sessions",nil];
	actionSheet1.actionSheetStyle = UIActionSheetStyleBlackOpaque;
	[actionSheet1 showInView:appDelegate.window];
	[actionSheet1 release];
}

-(IBAction)locationReport{
	editIndex = 3;
	UIActionSheet *actionSheet2 = [[UIActionSheet alloc] initWithTitle:@""
															  delegate:self 
													 cancelButtonTitle:@"Cancel"
												destructiveButtonTitle:nil 
													 otherButtonTitles:@"5 minutes",@"15 minutes",@"30 minutes",@"60 minutes",nil];
	actionSheet2.actionSheetStyle = UIActionSheetStyleBlackOpaque;
	[actionSheet2 showInView:appDelegate.window];
	[actionSheet2 release];
}

- (IBAction)sendToAlert {
	editIndex = 4;
	UIActionSheet *actionSheet3 = [[UIActionSheet alloc] initWithTitle:@""
															  delegate:self 
													 cancelButtonTitle:@"Cancel"
												destructiveButtonTitle:nil 
													 otherButtonTitles:@"Family",@"Friends",
								   @"Neighborhood Watch",@"Group A",@"Group B",@"Family & Friends",nil];
	actionSheet3.actionSheetStyle = UIActionSheetStyleBlackOpaque;
	[actionSheet3 showInView:appDelegate.window];
	[actionSheet3 release];
}

- (IBAction)rangeSatus {
	editIndex=5;
	UIActionSheet *actionSheet4 = [[UIActionSheet alloc] initWithTitle:@""
															  delegate:self 
													 cancelButtonTitle:@"Cancel"
												destructiveButtonTitle:nil 
													 otherButtonTitles:@"1 Km",@"3 Km",@"5 Km",@"10 Km",@"20 Km",nil];
	actionSheet4.actionSheetStyle = UIActionSheetStyleBlackOpaque;
	[actionSheet4 showInView:appDelegate.window];
	[actionSheet4 release];
}	

- (IBAction)ReceiveRangeSamaritan {
	editIndex=6;
	UIActionSheet *actionSheet5 = [[UIActionSheet alloc] initWithTitle:@""
															  delegate:self 
													 cancelButtonTitle:@"Cancel"
												destructiveButtonTitle:nil 
													 otherButtonTitles:@"1 Km",@"3 Km",@"5 Km",@"10 Km",@"20 Km",nil];
	actionSheet5.actionSheetStyle = UIActionSheetStyleBlackOpaque;
	[actionSheet5 showInView:appDelegate.window];
	[actionSheet5 release];
}	

#pragma mark UIActionSheetDelegate

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
	isEdit = YES;
	if (buttonIndex != [actionSheet cancelButtonIndex]) {
		NSString *value;
		if (editIndex == 1) {
			switch (buttonIndex) {
				case 0:
					value=@"5";
					break;
				case 1:
					value=@"10";
					break;
				case 2:
					value=@"15";
					break;
				case 3:
					value=@"30";
					break;
				default:
					value=@"0";
					break;
			}
			[appDelegate.settingArray setObject:value forKey:ST_ImageRecordFrequency];
			imageDuration.text = [NSString stringWithFormat:@"%@ seconds",[appDelegate.settingArray objectForKey:ST_ImageRecordFrequency]];
		}
		else if (editIndex == 2){
			NSString *session;
			switch (buttonIndex) {
				case 0:
					//session=@"1";
					voiceRecord.text = @"1 Session";
					break;
				case 1:
					//session=@"2";
					voiceRecord.text = @"2 Sessions";
					break;
				case 2:
					//session=@"3";
					voiceRecord.text = @"3 Sessions";
					break;
				case 3:
					//session=@"4";
					voiceRecord.text = @"4 Sessions";
					break;
				case 4:
					//session=@"5";
					voiceRecord.text = @"5 Sessions";
					break;
				case 5:
					//session=@"6";
					voiceRecord.text = @"6 Sessions";
					break;
				default:
					break;
					
			}
			//[appDelegate.settingArray setObject:session forKey:ST_VoiceRecordDuration];
		}
		else if(editIndex==3) {
			switch (buttonIndex) {
				case 0:
					value=@"5";
					break;
				case 1:
					value=@"15";
					break;
				case 2:
					value=@"30";
					break;
				case 3:
					value=@"60";
					break;
				default:
					value=@"0"; 
					break;
			}
			[appDelegate.settingArray setObject:value forKey:ST_LocationReporting];
			locationReporting.text = [NSString stringWithFormat:@"%@ minutes",[appDelegate.settingArray objectForKey:ST_LocationReporting]];
		}
		else if(editIndex==4) {
			switch (buttonIndex) {
				case 0:
					selectAlert=@"0";
					lblSendToAlert.text=@"Family";
					break;
				case 1:
					selectAlert=@"1";
					lblSendToAlert.text=@"Friends";
					break;
				case 2:
					selectAlert=@"2";
					lblSendToAlert.text=@"Neighborhood Watch";
					break;
				case 3:
					selectAlert=@"3";
					lblSendToAlert.text=@"Group A";
					break;
				case 4:
					selectAlert=@"4";
					lblSendToAlert.text=@"Group B";
					break;
				case 5:
					selectAlert=@"5";
					lblSendToAlert.text=@"Family & Friends";
					break;
				default:
					break;
			}
		}
		else if(editIndex==5) {
			switch (buttonIndex) {
				case 0:
					selectSamaritanRange=@"1";
					lblSamaritanRange.text = @"1 Km";
					break;
				case 1:
					selectSamaritanRange=@"3";
					lblSamaritanRange.text = @"3 Km";
					break;
				case 2:
					selectSamaritanRange=@"5";
					lblSamaritanRange.text = @"5 Km";
					break;
				case 3:
					selectSamaritanRange=@"10";
					lblSamaritanRange.text = @"10 Km";
					break;
				case 4:
					selectSamaritanRange=@"20";
					lblSamaritanRange.text = @"20 Km";
					break;
				default:
					break;
			}
		}
		
		else if(editIndex==6) {
			switch (buttonIndex) {
				case 0:
					selectReciveRange=@"1";
					receiveRange.text = @"1 Km";
					break;
				case 1:
					selectReciveRange=@"3";
					receiveRange.text = @"3 Km";
					break;
				case 2:
					selectReciveRange=@"5";
					receiveRange.text = @"5 Km";
					break;
				case 3:
					selectReciveRange=@"10";
					receiveRange.text = @"10 Km";
					break;
				case 4:
					selectReciveRange=@"20";
					receiveRange.text = @"20 Km";
					break;
				default:
					break;
			}
		}
		
	}
}

#pragma mark IBAction 

- (IBAction)textFieldDoneEditing:(id)sender {
	[sender resignFirstResponder];	
}

- (IBAction)backgroundTap:(id)sender {
	[txtPanicPhone resignFirstResponder];
	[txtEmail resignFirstResponder];
	[txtPassWord resignFirstResponder];
	[txtPhoneNumber resignFirstResponder];
	[txtuserName resignFirstResponder];
}

-(IBAction)BackInfor {
	[self dismissModalViewControllerAnimated:YES];
}

- (IBAction) LoadWebView {
	EmergencyView *emergency = [[EmergencyView alloc] init];
	[self presentModalViewController:emergency animated:YES];
	[emergency release];
}

- (IBAction)IncomingGovernment:(id)sender {
	isEdit = YES;	
	NSString *switchs;
	if (incomingGovernment.on)
	{
		switchs = @"1";
		
	}
	else
	{
		switchs = @"0";
		
	}
	[appDelegate.settingArray setObject:switchs forKey:ST_IncomingGovernment];
	
}	

- (IBAction)SamaritanSatus:(id)sender{
	isEdit = YES;
	NSString *status;
	if (samaritanStatus.on) {
		//status = @"1";		
		btnRangeSamaritan.enabled = TRUE;
		
	}
	else {
		btnRangeSamaritan.enabled = FALSE;
		//status = @"0";
	}
}

- (IBAction)ReceiverSamaritan:(id)sender{
	isEdit = YES;
	NSString *receiver;
	if (receiverSamaritan.on) {
		//receiver = @"1";
		receiveRangeStatus.enabled =TRUE;	
		
	}
	else {
		//receiver = @"0";
		receiveRangeStatus.enabled = FALSE;
		
	}
}

- (void)sendSamaristanAuto{
		loadIndex = 3;
		[self delayAlert];
		NSArray *key = [NSArray arrayWithObjects:@"token",@"latitude",@"longtitude",nil];
		NSArray *obj = [NSArray arrayWithObjects:appDelegate.apiKey,appDelegate.latitudeString,appDelegate.longitudeString,nil];
		NSDictionary *dict = [NSDictionary dictionaryWithObjects:obj forKeys:key];
		[rest postPath:[NSString stringWithFormat:@"/location?phoneId=%d&format=json",appDelegate.phoneID] withOptions:dict];
		
	
		
}

- (IBAction)ResetSetting {
	isEdit = NO;
	alertIndex = 10;
	UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Reset all to default?"
													message:@"Are you sure you want to reset all settings to default"
												   delegate:self
										  cancelButtonTitle:@"Yes"
										  otherButtonTitles:@"No",nil];
	[alert show];
	[alert release];
}

-(IBAction)SaveSetting{
	//save array;
	[appDelegate.settingArray setObject:selectReciveRange forKey:ST_ReciveRange];
	[appDelegate.settingArray setObject:selectSamaritanRange forKey:ST_SamaritanRange];
	[appDelegate.settingArray setObject:selectAlert forKey:ST_SendToAlert];
	
	[appDelegate.settingArray setObject:[voiceRecord.text substringToIndex:1] forKey:ST_VoiceRecordDuration];
	
	if (samaritanStatus.on) {
		[appDelegate.settingArray setObject:[NSString stringWithFormat:@"%d",1] forKey:ST_SamaritanStatus] ;
	}
	else {
		[appDelegate.settingArray setObject:[NSString stringWithFormat:@"%d",0] forKey:ST_SamaritanStatus];
		
	}
	
	if (receiverSamaritan.on) {
		[appDelegate.settingArray setObject:[NSString stringWithFormat:@"%d",1] forKey:ST_ReceiverSamaritan] ;
	}
	else {
		[appDelegate.settingArray setObject:[NSString stringWithFormat:@"%d",0] forKey:ST_ReceiverSamaritan];
		
	}
	
	save = TRUE;
	btnSave.enabled = FALSE;
	actSetting.hidden=NO;
	[actSetting startAnimating];
	NSString *value1 = txtPanicPhone.text;
	[appDelegate.settingArray setObject:value1 forKey:ST_EmergencySetting];
	
	if ([txtPanicPhone.text isEqualToString:@""]|| txtPanicPhone.text == nil)	
	{
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error"
															message:@"Emergency phone number must be entered to proceed"
														   delegate:nil
												  cancelButtonTitle:@"Ok"
												  otherButtonTitles:nil];
		[alertView show];
		[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:2];
		[alertView release];
		//save = FALSE;
		btnSave.enabled = TRUE;
		[actSetting stopAnimating];
		actSetting.hidden = YES;
		return;
 		
	}
	else if([txtuserName.text isEqualToString:@""] || txtuserName.text == nil)
	{
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error"
															message:@"Name must be entered to proceed"
														   delegate:nil
												  cancelButtonTitle:@"Ok"
												  otherButtonTitles:nil];
		[alertView show];
		[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:CONF_DIALOG_DELAY_TIME];
		[alertView release];
		//save = FALSE;
		btnSave.enabled = TRUE;
		[actSetting stopAnimating];
		actSetting.hidden = YES;
		return;
	}
	else if(!checkSpace(txtuserName.text)) {
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error"
															message:@"Name can not be blank"
														   delegate:nil
												  cancelButtonTitle:@"Ok"
												  otherButtonTitles:nil];
		[alertView show];
		[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:CONF_DIALOG_DELAY_TIME];
		[alertView release];
		//save = FALSE;
		btnSave.enabled = TRUE;
		[actSetting stopAnimating];
		actSetting.hidden = YES;
		return;
		
	}
	
	else if([txtEmail.text isEqualToString:@""] || txtEmail.text == nil)
	{
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error"
															message:@"Email must be entered to proceed"
														   delegate:nil
												  cancelButtonTitle:@"Ok"
												  otherButtonTitles:nil];
		[alertView show];
		[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:CONF_DIALOG_DELAY_TIME];
		[alertView release];
		//save = FALSE;
		btnSave.enabled = TRUE;
		[actSetting stopAnimating];
		actSetting.hidden = YES;
		return;
	}
	else if(!(checkMail(txtEmail.text))) {
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error"
															message:NSLocalizedString(@"EmailNotValid",@"")
														   delegate:nil
												  cancelButtonTitle:@"Ok"
												  otherButtonTitles:nil];
		[alertView show];
		[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:CONF_DIALOG_DELAY_TIME];
		[alertView release];
		//save = FALSE;
		btnSave.enabled = TRUE;
		[actSetting stopAnimating];
		actSetting.hidden = YES;
		return;		
	}
	
	else if(!(checkPhone(txtPanicPhone.text))) {
		//save = FALSE;
		btnSave.enabled = TRUE;
		[actSetting stopAnimating];
		actSetting.hidden = YES;
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error"
															message:@"Emergency phone number is invalid"
														   delegate:nil
												  cancelButtonTitle:@"Ok"
												  otherButtonTitles:nil];
		[alertView show];
		[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:CONF_DIALOG_DELAY_TIME];
		[alertView release];
		return;
	}
	
	else if ([txtPassWord.text isEqualToString:@""] )
	{
		//save = FALSE;
		btnSave.enabled = TRUE;
		[actSetting stopAnimating];
		actSetting.hidden = YES;		
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error"
															message:@"Password must be entered to proceed"
														   delegate:nil
												  cancelButtonTitle:@"Ok"
												  otherButtonTitles:nil];
		[alertView show];
		[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:CONF_DIALOG_DELAY_TIME];
		[alertView release];
		return;
	}
	else if (!(checkPassWord(txtPassWord.text)))
	{
		//save = FALSE;
		btnSave.enabled = TRUE;
		[actSetting stopAnimating];
		actSetting.hidden = YES;		
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error"
															message:@"Password is invalid"
														   delegate:nil
												  cancelButtonTitle:@"Ok"
												  otherButtonTitles:nil];
		[alertView show];
		[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:CONF_DIALOG_DELAY_TIME];
		[alertView release];
		return;
	}
	else
	{
		loadIndex = 0;
		[appDelegate.informationArray setObject:txtuserName.text forKey:@"username"];
		[appDelegate.informationArray setObject:txtEmail.text forKey:@"email"];
		[appDelegate.informationArray setObject:txtPhoneNumber.text forKey:@"number"];
		[appDelegate.informationArray setObject:txtPassWord.text forKey:@"password"];		
		NSArray *key1 = [NSArray arrayWithObjects:@"username",@"email",@"password",@"number",nil];
		NSArray *obj1 = [NSArray arrayWithObjects:txtuserName.text,txtEmail.text,txtPassWord.text,txtPhoneNumber.text,nil];
		NSDictionary *params1 = [NSDictionary dictionaryWithObjects:obj1 forKeys:key1];
		[rest putPath:[NSString stringWithFormat:@"/phones/%d?token=%@&latitude=%@longtitude=%@&format=json",appDelegate.phoneID,appDelegate.apiKey,appDelegate.latitudeString,appDelegate.longitudeString] withOptions:params1];		
	}	
	
	
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
	if (buttonIndex == 0) {
		if (alertIndex==10) {
			appDelegate.logout = YES;
			txtEmail.text = @"";
			txtuserName.text = @"";
			txtPhoneNumber.text = @"";
			txtPassWord.text = @"";
			NSArray *key = [NSArray arrayWithObjects:@"username",@"password",@"phoneNumber",nil];
			NSArray *obj = [NSArray arrayWithObjects:txtEmail.text,txtPassWord.text,txtPhoneNumber.text,nil];
			NSDictionary *dict = [NSDictionary dictionaryWithObjects:obj forKeys:key];
			[dict writeToFile:[NSString stringWithFormat:@"%@/info.plist",DOCUMENTS_FOLDER] atomically:YES];			
			TermsService *termsService = [[TermsService alloc] init] ;
			termsService.view.frame = CGRectMake(0, 0, 320, 480);
			LoginView *login = [[LoginView alloc] init];
			[self presentModalViewController:login animated:YES];
			[appDelegate.window addSubview:termsService.view];
			
			[appDelegate.groupView release];
			appDelegate.groupView = nil;
			[termsService release];
			[login release];
		}else if(alertIndex==9) {
			[self SaveSetting];
		}
		
	}
	
}


#pragma mark -
#pragma mark delayAlert

- (void)delayAlert{
	
	[NSObject cancelPreviousPerformRequestsWithTarget:self selector:@selector(sendSamaristanAuto) object:nil];
	[self performSelector:@selector(sendSamaristanAuto) withObject:nil afterDelay:1800]; 
}

#pragma mark -
#pragma mark textFieldDelegate

- (void)textFieldDidBeginEditing:(UITextField *)textField {
	NSLog(@"Editing textfield");
	isEdit = YES;
	if(textField==txtPanicPhone)
	{
		[scrollView setContentOffset:CGPointMake(0, 330) animated:YES];
	}
	
}

#pragma mark -
#pragma mark delayUIalert

- (void) DimisAlertView:(UIAlertView*)alertView {
	[alertView dismissWithClickedButtonIndex:0 animated:TRUE];
}

#pragma mark -
#pragma mark finish request

-(void)finishRequest:(NSDictionary *)arrayData andRestConnection:(id)connector{
	[actSetting stopAnimating];
	actSetting.hidden = YES;
	btnSave.enabled = TRUE;
	isEdit = NO;
	if (loadIndex==0) {
		if ([[[arrayData objectForKey:@"response"] objectForKey:@"success"] isEqualToString:@"true"]){
			NSArray *key = [NSArray arrayWithObjects:@"username",@"password",@"phoneNumber",nil];
			NSArray *obj = [NSArray arrayWithObjects:txtEmail.text,txtPassWord.text,txtPhoneNumber.text,nil];
			NSDictionary *dict = [NSDictionary dictionaryWithObjects:obj forKeys:key];
			[dict writeToFile:[NSString stringWithFormat:@"%@/info.plist",DOCUMENTS_FOLDER] atomically:YES];	
			loadIndex = 1;
			NSArray *key1 = [NSArray arrayWithObjects:@"phoneId",@"token",@"voiceDuration",@"locationDuration",@"panicNumber",@"alertSendtoGroup",@"goodSamaritanRange",@"panicStatus",@"goodSamaritanStatus",@"incomingGovernmentAlert",@"panicRange",nil];
			NSArray *obj1 = [NSArray arrayWithObjects:[NSString stringWithFormat:@"%d",appDelegate.phoneID],
							appDelegate.apiKey,
							[appDelegate.settingArray objectForKey:ST_VoiceRecordDuration],
							[appDelegate.settingArray objectForKey:ST_LocationReporting],
							[appDelegate.settingArray objectForKey:ST_EmergencySetting],
							[appDelegate.settingArray objectForKey:ST_SendToAlert],
							[appDelegate.settingArray objectForKey:ST_ReciveRange],
							[appDelegate.settingArray objectForKey:ST_SamaritanStatus],
							[appDelegate.settingArray objectForKey:ST_ReceiverSamaritan],
							[appDelegate.settingArray objectForKey:ST_IncomingGovernment],
							 [appDelegate.settingArray objectForKey:ST_SamaritanRange],nil];
			NSDictionary *param1 =[NSDictionary dictionaryWithObjects:obj1 forKeys:key1];
			[rest putPath:[NSString stringWithFormat:@"/setting/%d?format=json",appDelegate.settingId] withOptions:param1];
		}
		else {
			btnSave.enabled = TRUE;
			[actSetting stopAnimating];
			actSetting.hidden = YES;			
			UIAlertView *alertView= [[UIAlertView alloc] initWithTitle:@"Error"
															   message:@"Phone number already exists" 
															  delegate:self 
													 cancelButtonTitle:@"Ok" 
													 otherButtonTitles:nil] ;
			[alertView show];
			[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:2];
			[alertView release];
			
		}

	}
	else if (loadIndex==1) {
		if ([[[arrayData objectForKey:@"response"] objectForKey:@"success"] isEqualToString:@"true"]){
			if (save) {
				alertIndex = 11;
				btnSave.enabled = TRUE;
				UIAlertView *alertView= [[UIAlertView alloc] initWithTitle:@""
																   message:@"Settings saved successfully" 
																  delegate:self 
														 cancelButtonTitle:@"Ok" 
														 otherButtonTitles:nil] ;
				[alertView show];
				[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:CONF_DIALOG_DELAY_TIME];
				[alertView release];
				save = NO;
				if(samaritanStatus.on){
					[self delayAlert];
				}else {
					[NSObject cancelPreviousPerformRequestsWithTarget:self selector:@selector(sendSamaristanAuto) object:nil];
				}

			}
		}else {
			
		}
	}
	
	if (loadIndex == 3) {
		if ([[[arrayData objectForKey:@"response"] objectForKey:@"success"] isEqualToString:@"true"]){
			NSLog(@"Send goodsamaritan success");			
		}else {
			NSLog(@"False send goodsamaritan");
		}
		
	}
	
	
}

#pragma mark -
#pragma mark -network fail

-(void)cantConnection:(NSError *)error andRestConnection:(id)connector{
	btnSave.enabled = TRUE;
	[actSetting stopAnimating];
	actSetting.hidden=YES;
	if (save) {
		alertView();
		save = NO;
	}
}

#pragma mark -
#pragma mark reload data
- (void) loadData {
	if([[NSFileManager defaultManager] fileExistsAtPath:[NSString stringWithFormat:@"%@/info.plist",DOCUMENTS_FOLDER]])
	{
		NSDictionary *info = [[NSDictionary alloc] initWithContentsOfFile:[NSString stringWithFormat:@"%@/info.plist",DOCUMENTS_FOLDER]];
		txtuserName.text = [info objectForKey:@"username"];
		txtPassWord.text = [info objectForKey:@"password"];
		txtPhoneNumber.text = [info objectForKey:@"phoneNumber"];
		[info autorelease];
	}	
	btnSave.enabled =TRUE; 
	actSetting.hidden=YES;
	lblSamaritanRange.text =[NSString stringWithFormat:@"%@ Km",[appDelegate.settingArray objectForKey:ST_SamaritanRange]];
	receiveRange.text =[NSString stringWithFormat:@"%@ Km",[appDelegate.settingArray objectForKey:ST_ReciveRange]];
	
	imageDuration.text =[NSString stringWithFormat:@"%@ seconds",[appDelegate.settingArray objectForKey:ST_ImageRecordFrequency]];
	locationReporting.text =[NSString stringWithFormat:@"%@ minutes",[appDelegate.settingArray objectForKey:ST_LocationReporting]];	
	
	//get voiceimage
	if([[appDelegate.settingArray objectForKey:ST_VoiceRecordDuration] intValue] == 2) 
		voiceRecord.text = @"2 Sessions";
	
	else if([[appDelegate.settingArray objectForKey:ST_VoiceRecordDuration] intValue] == 3)
		voiceRecord.text = @"3 Sessions";
	
	else if([[appDelegate.settingArray objectForKey:ST_VoiceRecordDuration] intValue] == 4)
		voiceRecord.text = @"4 Sessions";
	
	else if([[appDelegate.settingArray objectForKey:ST_VoiceRecordDuration] intValue] == 5)
		voiceRecord.text = @"5 Sessions";
	else if([[appDelegate.settingArray objectForKey:ST_VoiceRecordDuration] intValue] == 6) {
		voiceRecord.text = @"6 Sessions";
	}else {
		voiceRecord.text = @"1 Session";
	}
	if ([[appDelegate.settingArray objectForKey:ST_SendToAlert] intValue] == 0) 
		lblSendToAlert.text = @"Family";
	else 
		if ([[appDelegate.settingArray objectForKey:ST_SendToAlert] intValue] ==1 )
			lblSendToAlert.text = @"Friends";
		else if ([[appDelegate.settingArray objectForKey:ST_SendToAlert] intValue] ==2 )
			lblSendToAlert.text = @"Neighborhood Watch";
		else if ([[appDelegate.settingArray objectForKey:ST_SendToAlert] intValue] ==3 )
			lblSendToAlert.text = @"Group A";
		else if ([[appDelegate.settingArray objectForKey:ST_SendToAlert] intValue] ==4 )
			lblSendToAlert.text = @"Group B";
		else 
			lblSendToAlert.text=@"Family & Friends";
	
	//getreceiversamaritan
	if (![[appDelegate.settingArray objectForKey:ST_ReceiverSamaritan] isEqual:[NSNull null]]) {
		if ([[appDelegate.settingArray objectForKey:ST_ReceiverSamaritan] intValue] == 0) {
			receiveRangeStatus.enabled = FALSE;
			[receiverSamaritan setOn:NO];
		}
		else {
			receiveRangeStatus.enabled = TRUE;
			[receiverSamaritan setOn:YES];
			
		}
	}
	
	//get samaritanstatus
	if (![[appDelegate.settingArray objectForKey:ST_SamaritanStatus] isEqual:[NSNull null]]){
		if([[appDelegate.settingArray objectForKey:ST_SamaritanStatus] intValue] == 0)
		{
			btnRangeSamaritan.enabled = FALSE;
			[samaritanStatus setOn:NO];
			[NSObject cancelPreviousPerformRequestsWithTarget:self selector:@selector(sendSamaristanAuto) object:nil];
		}
		else {
			btnRangeSamaritan.enabled = TRUE;
			[samaritanStatus setOn:YES];
			[self delayAlert];
		}
	}
	
	//get incominggovement
	
	if (![[appDelegate.settingArray objectForKey:ST_IncomingGovernment] isEqual:[NSNull null]]) {
		if ([[appDelegate.settingArray objectForKey:ST_IncomingGovernment] intValue] == 0) {
			[incomingGovernment setOn:NO];
		}
		else {
			[incomingGovernment setOn:YES];
		}
	}
	
	txtPanicPhone.text =[appDelegate.settingArray objectForKey:ST_EmergencySetting];
	txtPanicPhone.keyboardType=UIKeyboardTypeNumberPad;
	
	//display information
	txtuserName.text = [appDelegate.informationArray objectForKey:@"username"];
	txtEmail.text = [appDelegate.informationArray objectForKey:@"email"];
	txtPhoneNumber.text = [appDelegate.informationArray objectForKey:@"number"];	
}

@end















