//
//  SettingsAlertView.m
//  SOSBEACON
//
//  Created by Geoff Heeren on 6/18/11.
//  Copyright 2011 AppTight, Inc. All rights reserved.
//

#import "SettingsAlertView.h"
#import "EmergencyView.h"
@implementation SettingsAlertView
@synthesize incomingGovernment,txtPanicPhone,lblSendToAlert,voiceRecord,rest,btnSave,actSetting;
@synthesize flag;
@synthesize actionSheet3;
@synthesize groupArray;
@synthesize typeArray;
@synthesize scoll;
// The designated initializer.  Override if you create the controller programmatically and want to perform customization that is not appropriate for viewDidLoad.

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
		btnSave=[[UIBarButtonItem alloc] initWithTitle:@"Save" style:UIBarButtonItemStyleBordered target:self action:@selector(SaveSetting)];
		self.navigationItem.rightBarButtonItem = btnSave;
    }
    return self;
}



// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
	self.scoll.contentSize =  CGSizeMake(320, 800);
	self.title=@"Alert Settings";
	rest = [[RestConnection alloc] initWithBaseURL:SERVER_URL];
	rest.delegate =self;	
	appDelegate = (SOSBEACONAppDelegate*)[[UIApplication sharedApplication] delegate];
	[self loadData];
    [super viewDidLoad];
}


/*
// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations.
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
*/

- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc. that aren't in use.
}

- (void)viewDidUnload {
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}


- (void)dealloc {
	rest.delegate = nil;
	[rest release];
	[scoll release];
	[selectAlert  release];
	[typeArray release];
	[groupArray release];
	[actionSheet3 release];
    [super dealloc];
}
-(void)loadData{
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
	flag = 2;
	[rest getPath:[NSString stringWithFormat:@"/groups?phoneId=%d&token=%@&format=json",appDelegate.phoneID,appDelegate.apiKey] withOptions:nil];

	
}
- (IBAction) LoadWebView {
	EmergencyView *emergency = [[EmergencyView alloc] init];
	[self presentModalViewController:emergency animated:YES];
	[emergency release];
}
- (IBAction)sendToAlert {
	editIndex = 4;

	actionSheet3.actionSheetStyle = UIActionSheetStyleBlackOpaque;
	[actionSheet3 showInView:appDelegate.window];
	

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

#pragma mark UIActionSheetDelegate

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
	isEdit = YES;
	if (buttonIndex != [actionSheet cancelButtonIndex]) {
		NSString *value;
		if (editIndex == 2){
		
			switch (buttonIndex) {
				case 0:
					voiceRecord.text = @"1 Session";
					break;
				case 1:
					voiceRecord.text = @"2 Sessions";
					break;
				case 2:
					voiceRecord.text = @"3 Sessions";
					break;
				case 3:
					voiceRecord.text = @"4 Sessions";
					break;
				case 4:
					voiceRecord.text = @"5 Sessions";
					break;
				case 5:
					voiceRecord.text = @"6 Sessions";
					break;
				default:
					break;
					
			}
		}
		
		else if(editIndex==4) 
		{
			NSInteger i = buttonIndex;
			lblSendToAlert.text = [groupArray objectAtIndex:i];
			if (selectAlert)
			{
				//NSLog(@"release : selectAlert");
				[selectAlert release];
				selectAlert = nil;
			}
			selectAlert  = [[typeArray objectAtIndex:i] retain];		
		}
		}
}

-(void)save
{
	[txtPanicPhone resignFirstResponder];
	if ([incomingGovernment isOn]) 
	{
		[appDelegate.settingArray setObject:@"1" forKey:ST_IncomingGovernment];
	}
	else
	{
		[appDelegate.settingArray setObject:@"0" forKey:ST_IncomingGovernment];
	}
	[appDelegate.settingArray setObject:selectAlert forKey:ST_SendToAlert];
	[appDelegate.settingArray setObject:[voiceRecord.text substringToIndex:1] forKey:ST_VoiceRecordDuration];
	save = TRUE;
	btnSave.enabled = FALSE;
	actSetting.hidden=NO;
	[actSetting startAnimating];
	if ([txtPanicPhone.text isEqualToString:@""]|| txtPanicPhone.text == nil)	
	{
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error"
															message:@"Emergency phone number is invalid"
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
		txtPanicPhone.text = @"0";
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
	else
	{
		
		
		///
		
		
		NSString *value1 =[[NSString alloc] initWithString:txtPanicPhone.text];
		char b =[value1 characterAtIndex:0];
		
		
		///////
		NSString *strcut = @"0";

		if ([value1 length] >2) 
			strcut = [value1 substringToIndex:2];
		
		

		//NSLog(@" %@",value1);
		//NSLog(@" %@",strcut);
		if ([strcut isEqualToString:@"+1"]||[strcut  isEqualToString:@"1+"]) 
		{
			if ([value1  length] >2) 
			{
				NSString *phone = [[value1 substringFromIndex:2] retain];
				txtPanicPhone.text = phone;
			//	NSLog(@" %@",phone);
				[value1 release];
				[phone release];
			}else 
			{   
				txtPanicPhone.text = @"0";
				[value1 release];
				//value1 =@"0";
			}
			
			
		}else
			if (b == '1' || b=='+')
			{
			//	NSLog(@"alo");
				if ([value1  length] >1) 
				{
					NSString *phone = [[value1 substringFromIndex:1] retain];
					[appDelegate.settingArray setObject:phone forKey:ST_EmergencySetting];
					txtPanicPhone.text = phone;
					[value1 release];
					[phone release];
				}else 
				{   
					[value1 release];
					txtPanicPhone.text = @"0";
					//value1 = @"0";
				}			
			}
			else 
			{
				[appDelegate.settingArray setObject:value1 forKey:ST_EmergencySetting];
				[value1 release];
			}
		
		
		////
		
		
		
		///
		[appDelegate.settingArray setObject:txtPanicPhone.text forKey:ST_EmergencySetting];
	//	NSLog(@" save ST_sendtoalert : %@ ",[appDelegate.settingArray objectForKey:ST_SendToAlert]);
		
		///
		
		
		
		NSArray *key1 = [NSArray arrayWithObjects:@"id",@"phoneId",@"token",@"recordDuration",@"emergencyNumber",@"alertSendToGroup",@"goodSamaritanStatus",@"goodSamaritanRange",@"panicStatus",@"panicRange",@"incomingGovernmentAlert",nil];
		NSArray *obj1 = [NSArray arrayWithObjects:[NSString stringWithFormat:@"%d",appDelegate.settingId],
						 [NSString stringWithFormat:@"%d",appDelegate.phoneID],
						 appDelegate.apiKey,
						 [appDelegate.settingArray objectForKey:ST_VoiceRecordDuration],
						 [appDelegate.settingArray objectForKey:ST_EmergencySetting],
						 [appDelegate.settingArray objectForKey:ST_SendToAlert],
						 [appDelegate.settingArray objectForKey:ST_ReceiverSamaritan],
						 [appDelegate.settingArray objectForKey:ST_ReciveRange],
						 [appDelegate.settingArray objectForKey:ST_SamaritanStatus],
						 [appDelegate.settingArray objectForKey:ST_SamaritanRange],
						 [appDelegate.settingArray  objectForKey:ST_IncomingGovernment],
						 nil];
		NSDictionary *param1 =[NSDictionary dictionaryWithObjects:obj1 forKeys:key1];
		flag =1;
		[rest putPath:[NSString stringWithFormat:@"/setting/%d?format=json",appDelegate.settingId] withOptions:param1];
		
	}	
	
	
	
	
}
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
	if (buttonIndex == 0) 
	{
		if (flagalert ==1)
		{
			flagalert = 2;
			[self save];
		}
	}
	else {
		
	}
	
}
-(IBAction)SaveSetting
{
	flagalert =1;
	UIAlertView *alert =[ [UIAlertView alloc] initWithTitle:nil 
													message:NSLocalizedString(@"SaveChange",@"")
												   delegate:self
										  cancelButtonTitle:@"Yes" otherButtonTitles:@"No",nil];
	[alert show];
	[alert release];

	
}

#pragma mark IBAction 

- (IBAction)textFieldDoneEditing:(id)sender {
	[sender resignFirstResponder];	
}

- (IBAction)backgroundTap:(id)sender {
	

	[txtPanicPhone resignFirstResponder];
}

#pragma mark -
#pragma mark delayUIalert

- (void) DimisAlertView:(UIAlertView*)alertView {
	[alertView dismissWithClickedButtonIndex:0 animated:TRUE];
}

#pragma mark -
#pragma mark finish request

-(void)finishRequest:(NSDictionary *)arrayData andRestConnection:(id)connector{
	if (flag == 1) {
		
	
//	NSLog(@" setting alert array response : %@",arrayData);
	[actSetting stopAnimating];
	actSetting.hidden = YES;
	btnSave.enabled = TRUE;
	isEdit = NO;
			if ([[[arrayData objectForKey:@"response"] objectForKey:@"success"] isEqualToString:@"true"]){
				if (save) {
				
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
				/// save phone for offline mode
					NSString *emergenceFile = [DOCUMENTS_FOLDER stringByAppendingPathComponent:@"emergencyNumber.plist"];
					NSMutableDictionary *emergencePhone = [[NSMutableDictionary alloc] init];
					
					[emergencePhone setObject:txtPanicPhone.text forKey:@"emerPhone"];
					
					[emergencePhone writeToFile:emergenceFile atomically:YES];
					NSLog(@" emergence phone : %@",emergencePhone);
					[emergencePhone release];
				
			}
		}else {
		//	NSLog(@" error roi");
			
		}
	
	}
	else
	if (flag == 2) 
	{
		
		actionSheet3 = [[UIActionSheet alloc] initWithTitle:@""
												   delegate:self 
										  cancelButtonTitle:nil
									 destructiveButtonTitle:nil 
										  otherButtonTitles:nil];
		 groupArray = [[NSMutableArray alloc] init];
		 typeArray = [[NSMutableArray alloc] init];

	//	NSLog(@" ST_sendtoAlert in finish request: %@",[appDelegate.settingArray objectForKey:ST_SendToAlert]);
		if ([[[arrayData objectForKey:@"response"] objectForKey:@"success"] isEqualToString:@"true"]) {
			NSDictionary *data = [[arrayData objectForKey:@"response"] objectForKey:@"data"];
			
			for(NSDictionary *dict in data)
			{
				[actionSheet3 addButtonWithTitle:[dict objectForKey:@"name"]];
				[groupArray  addObject:[dict objectForKey:@"name"]];
				[typeArray addObject:[dict objectForKey:@"id"]];
				
				
				
				NSInteger type = [[dict objectForKey:@"id"] intValue];
				if( type == [[appDelegate.settingArray objectForKey:ST_SendToAlert] intValue])
				{
				lblSendToAlert.text = [dict objectForKey:@"name"];
				selectAlert =[[NSString stringWithFormat:@"%d",type] retain];
				}
			}
			//NSLog(@"group array : %@",groupArray);
			
		}else 
		{
			//NSLog(@"getcontact error");
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
@end
