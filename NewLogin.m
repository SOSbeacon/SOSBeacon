//
//  NewLogin.m
//  SOSBEACON
//
//  Created by bon on 7/27/11.
//  Copyright 2011 CNC Software. All rights reserved.
//

#define CK_CheckingIn @"checkingIn"
#import "SettingsMain.h"
#import "NewLogin.h"
#import "SplashView.h"
#import "SettingsAlertView.h"
#import "VideoViewController.h"
@implementation NewLogin
@synthesize flagForAlert;
@synthesize strImei;
@synthesize strPhoneNumber;
@synthesize restConnection;
@synthesize flagForRequest,flag;
@synthesize token;
@synthesize video;
@synthesize text_Phone;
@synthesize submit_button;
@synthesize cancel_button;

-(void)showLoading
{
	submit_button.hidden = YES;
	cancel_button.hidden = YES;
	text_Phone.hidden= YES;
	actSignup.hidden = NO;
	[actSignup startAnimating];
}
-(IBAction)backgroundTap:(id)sender
{
	[text_Phone resignFirstResponder];
}
-(IBAction)cancelButtonPress:(id)sender
{
	exit(0);
}
-(IBAction)submitButtonPress:(id)sender
{
	//flag = 1;
	UIDevice *device = [UIDevice currentDevice];
	strImei = [[NSString alloc] initWithString:[device uniqueIdentifier]];
	strPhoneNumber =[[NSString alloc] init];	
	[text_Phone  resignFirstResponder];
	submit_button.hidden = YES;
	cancel_button.hidden = YES;
	text_Phone.hidden= YES;
	////
	
	
	//////
	
	NSString *value1 =[[NSString alloc] initWithString:text_Phone.text];	
	/////
	NSString *strcut =@"0";

	if ([value1 length]>2) 
	{
		strcut = [value1 substringToIndex:2];

	}
	char b ='0';
	if ([value1 length ] >1) 
	{
		b =[value1 characterAtIndex:0];

	}
	//NSLog(@" %@",value1);
	//NSLog(@" %@",strcut);
	if ([strcut isEqualToString:@"+1"]||[strcut  isEqualToString:@"1+"]) 
	{
		if ([value1  length] >2) 
		{
			NSString *phone = [[value1 substringFromIndex:2] retain];
			text_Phone.text = phone;
			//NSLog(@" %@",phone);
			[value1 release];
			[phone release];
		}else 
		{   
			text_Phone.text = @"";
			[value1 release];
		}
		
		
	}
	else
	/////
	if (b == '1'|| b=='+')
	{
		if ([value1  length] >1) 
		{
			NSString *phone = [[value1 substringFromIndex:1] retain];
			text_Phone.text = phone;
			[value1 release];
			[phone release];
		}else 
		{   
			text_Phone.text = @"";
			[value1 release];
		}
	}
	else 
	{
		[value1 release];
		
	}
		
	if (text_Phone.text != @"") 
	{
	strPhoneNumber = text_Phone.text;
	}
	NSString *Version = [NSString stringWithFormat:@"Version:%@",[[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"]];
	NSLog(@"Version: %@",Version);
	NSString *model= [[UIDevice currentDevice] model];            
	NSString *systemName= [[UIDevice currentDevice] systemName];     
	NSString *systemVersion =[[UIDevice currentDevice] systemVersion]; 
	NSString *phoneInfor = [NSString stringWithFormat:@"%@;Model:%@;SystemName:%@;Systemversion:%@",Version,model,systemName,systemVersion];
	phoneInfor =[phoneInfor stringByReplacingOccurrencesOfString:@" "withString:@""];
	
	//
	NSArray *key = [[NSArray alloc] initWithObjects:@"imei",@"number",@"phoneType",@"do",@"phoneInfo",nil];
	NSArray *obj = [[NSArray alloc] initWithObjects:strImei,strPhoneNumber,@"1",@"",phoneInfor,nil];
	NSDictionary *param = [[NSDictionary alloc] initWithObjects:obj forKeys:key];
	[restConnection postPath:[NSString stringWithFormat:@"/phones?format=json"]withOptions:param];
	[key release];
	[obj release];
	[param release];
	
	actSignup.hidden = NO;
	[actSignup startAnimating]; 
	
	///
	
}
-(void)process
{
	if (flag == 1) 	
	{ 	
		//SplashView *splashView = [[SplashView alloc] init];
		//[appDelegate.window addSubview:splashView.view];
			NSLog(@"removefromsuperview flag ==1");
		[self.view removeFromSuperview];
	}
	if (flag == 2) 
	{
		//SplashView *splashView = [[SplashView alloc] init];
		//[appDelegate.window addSubview:splashView.view];
		NSLog(@"dismissModalViewControllerAnimated");
		[self dismissModalViewControllerAnimated:YES];
		appDelegate.tabBarController.selectedIndex= 0;
	}
	if (flag == 3) 	
	{
		//SplashView *splashView = [[SplashView alloc] init];
		//[appDelegate.window addSubview:splashView.view];
		
	//	NSLog(@"removefromsuperview flag == 3");
	//	[self.view removeFromSuperview];
	}
	appDelegate.tabBarController.selectedIndex = 0;
	
	if (contactCount == 0 ) 
	{  
		
		NSInteger aloflag= flagForAlert;
		flagForAlert = 20;
		//NSLog(@"show alert");
		UIAlertView *alert =[[UIAlertView alloc] initWithTitle:nil message:NSLocalizedString(@"doYouWantAddcontact",@"")
													  delegate:self cancelButtonTitle:@"Yes"
											 otherButtonTitles:@"Not Now",nil];
		//[alert show];
		if (aloflag == 30) {
			[alert show];
		}else 
		{
			[alert performSelector:@selector(show) withObject:nil afterDelay:1.5];
		}

		
		
		[alert release];
		
	}
	else 
	{
//		NSString *phoneString =[[appDelegate.settingArray objectForKey:ST_EmergencySetting] retain];
//		NSString *password =[[NSString alloc] initWithContentsOfFile:[NSString stringWithFormat:@"%@/info.plist",DOCUMENTS_FOLDER]];
//		if ([phoneString isEqualToString:@"0"] || [password isEqualToString:@"1"]);
//		{
//			
//			appDelegate.tabBarController.selectedIndex =3;
//			
//			
//		}
//		[phoneString release];
//		[password release];
		
	}
	
}


-(void)getdata
{
	
	NSString *Version = [NSString stringWithFormat:@"Version:%@",[[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"]];
	NSLog(@"version: %@",Version);
	NSString *model= [[UIDevice currentDevice] model];            
	NSString *systemName= [[UIDevice currentDevice] systemName];     
	NSString *systemVersion =[[UIDevice currentDevice] systemVersion]; 
	NSString *phoneInfor = [NSString stringWithFormat:@"%@;Model:%@;SystemName:%@;Systemversion:%@",Version,model,systemName,systemVersion];
	phoneInfor =[phoneInfor stringByReplacingOccurrencesOfString:@" "withString:@""];
	NSLog(@"phone infor:%@",phoneInfor);
	[restConnection getPath:[NSString stringWithFormat:@"/phones/%@?format=json&imei=%@&number=%@&phoneInfo=%@",strImei,strImei,strPhoneNumber,phoneInfor] withOptions:nil];
	
	text_Phone.hidden = YES;
	submit_button.hidden = YES;
	cancel_button.hidden = YES;
	actSignup.hidden = NO;
	[actSignup startAnimating]; 
	
	
}
-(void)timerTick
{
	countTime--;
	if (video.flag == 2) 
	{ 
		//[self.video dismissModalViewControllerAnimated:YES];
		[self.video.view removeFromSuperview];
		[self performSelector:@selector(process) withObject:nil afterDelay:1.0];
		[countDownTimer invalidate];

		
		
	}else 
	if (countTime == 0)
		{
		   [self.video dismissModalViewControllerAnimated:YES];
			[self performSelector:@selector(process) withObject:nil afterDelay:1.0];
			[countDownTimer invalidate];

		}

	
}
- (void)viewDidLoad {
	countActive=0;
	NSLog(@"view did load");
		actSignup.hidden=YES;
	/*
	RestConnection *tempConnection = [[RestConnection alloc] initWithBaseURL:SERVER_URL];
	restConnection = tempConnection;
	 */
	restConnection = [[RestConnection alloc] initWithBaseURL:SERVER_URL];
	restConnection.delegate = self;
	appDelegate = (SOSBEACONAppDelegate*)[[UIApplication sharedApplication] delegate];
	[super viewDidLoad];
	
	

}



- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (void)viewDidUnload {
	actSignup= nil;
	text_Phone = nil;
	submit_button= nil;
	cancel_button = nil;
    [super viewDidUnload];
}


- (void)dealloc {
	NSLog(@"dealloc new login");
	[video release];
	[countDownTimer release];
	[strImei release];
	[strPhoneNumber release];
	[restConnection release];
    [super dealloc];
	NSLog(@"end delloc new login");
}
- (void)cantConnection:(NSError *)error andRestConnection:(id)connector{
	
	
	text_Phone.text = strPhoneNumber;
	text_Phone.hidden = NO;
	[actSignup stopAnimating];
	actSignup.hidden = YES;
	cancel_button.hidden = NO;
	submit_button.hidden = NO;
    
	/*
	//NSLog(@"cant connection");
	submit_button.enabled = TRUE;
	cancel_button.enabled = TRUE;
	[appDelegate hiddenSplash];
	[actSignup stopAnimating];
	actSignup.hidden=YES;
	flagForAlert = 10;
	UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Cannot open application"
													    message:@"Can not get data from sever. Please check your Internet connection."
													   delegate:self
											  cancelButtonTitle:@"OK"
											  otherButtonTitles:nil];
	[alertView show];
	[alertView release];	
	 */
}

-(void)finishRequest:(NSDictionary *)arrayData andRestConnection:(id)connector
{

    NSLog(@"%@",arrayData);
    
    [appDelegate hiddenSplash];

	[actSignup stopAnimating];
	actSignup.hidden = YES;
	NSDictionary *respondArray =[arrayData objectForKey:@"response"];
	//NSLog(@" bon bon :%@", respondArray);
	
	NSInteger responsecode =[[respondArray objectForKey:@"responseCode"] intValue];
	NSString *message = [respondArray objectForKey:@"message"];
	if(responsecode == 1 )
	{ /// 1: thanh cong -> main form
		countActive =0;
		if ([[NSFileManager defaultManager] fileExistsAtPath:[NSString stringWithFormat:@"%@/respond.plist",DOCUMENTS_FOLDER]])
		{
			NSMutableDictionary *repondse = [[NSMutableDictionary alloc] initWithContentsOfFile:[NSString stringWithFormat:@"%@/respond.plist",DOCUMENTS_FOLDER]];
			if ([[repondse objectForKey:strPhoneNumber] isEqualToString:@"6"]) 
			{
				appDelegate.canShowVideo = YES;
			}
			[repondse release];
			[[NSFileManager defaultManager]  removeItemAtPath:[NSString stringWithFormat:@"%@/respond.plist",DOCUMENTS_FOLDER] error:nil];
		}
		/////
		
		NSString *file=[DOCUMENTS_FOLDER stringByAppendingPathComponent:@"newlogin.plist"];
		NSArray *key = [NSArray arrayWithObjects:@"imei",@"number",nil];
		NSArray *obj = [NSArray arrayWithObjects:strImei,strPhoneNumber,nil];			
		NSDictionary *param = [[NSDictionary alloc] initWithObjects:obj forKeys:key];
		[param writeToFile:file atomically:YES];
		[param release];
		appDelegate.phone = [strPhoneNumber retain];
		[appDelegate.informationArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"number"] forKey:@"number"];	
		[appDelegate.informationArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"name"] forKey:@"username"];
		[appDelegate.informationArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"email"] forKey:@"email"];	
		[appDelegate.informationArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"password"] forKey:@"password"];	
		appDelegate.apiKey = [[arrayData objectForKey:@"response"] objectForKey:@"token"];
		appDelegate.userID = [[[arrayData objectForKey:@"response"] objectForKey:@"user_id"] intValue];
		appDelegate.phoneID =[[[arrayData objectForKey:@"response"] objectForKey:@"id"] intValue];
		
		// save user id
		NSMutableDictionary *dic_userID = [[NSMutableDictionary alloc] init];
		NSString *fileUserId = [DOCUMENTS_FOLDER stringByAppendingPathComponent:@"UserID.plist"];
		[dic_userID setObject:[[arrayData objectForKey:@"response"] objectForKey:@"id"] forKey:@"userId"];
		[dic_userID writeToFile:fileUserId atomically:YES];
		[dic_userID release];
		//
		
		// save emergence phone for offline mode
		NSString *emergenceFile = [DOCUMENTS_FOLDER stringByAppendingPathComponent:@"emergencyNumber.plist"];
		NSMutableDictionary *emergencePhone = [[NSMutableDictionary alloc] init];
		[emergencePhone setObject:[[arrayData objectForKey:@"response"] objectForKey:@"emergencyNumber"] forKey:@"emerPhone"];
		[emergencePhone writeToFile:emergenceFile atomically:YES];
		NSLog(@" emergence phone : %@",emergencePhone);
		[emergencePhone release];
		//
		[appDelegate.settingArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"recordDuration"] forKey:ST_VoiceRecordDuration];

		appDelegate.settingId = [[[arrayData objectForKey:@"response"] objectForKey:@"settingId"] intValue];

		
		[appDelegate.settingArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"locationId"] forKey:ST_LocationReporting];
		[appDelegate.settingArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"emergencyNumber"] forKey:ST_EmergencySetting];
		[appDelegate.settingArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"goodSamaritanRange"] forKey:ST_ReciveRange];
		[appDelegate.settingArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"goodSamaritanStatus"] forKey:ST_ReceiverSamaritan];
		[appDelegate.settingArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"incomingGovernmentAlert"] forKey:ST_IncomingGovernment];
		[appDelegate.settingArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"panicStatus"] forKey:ST_SamaritanStatus];
		[appDelegate.settingArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"panicRange"] forKey:ST_SamaritanRange];	
		
		
		[appDelegate.settingArray setObject:[[arrayData objectForKey:@"response"] objectForKey:@"alertSendToGroup"] forKey:ST_SendToAlert];
		///
		NSString *defaultFile = [DOCUMENTS_FOLDER stringByAppendingPathComponent:@"defaultGroup.plist"];

		if ([[NSFileManager defaultManager ] fileExistsAtPath:defaultFile])
		{
			
		}
		else
		{
			//NSLog(@"file not exist");
			NSMutableArray *defaultGroupArr = [[NSMutableArray alloc]initWithObjects:@"Family",[appDelegate.settingArray objectForKey:ST_SendToAlert],nil];
			//[defaultGroup addObject:[appDelegate.settingArray objectForKey:ST_SendToAlert]]
			//NSLog(@"array default group : %@",defaultGroupArr);
			[defaultGroupArr writeToFile:defaultFile atomically:YES];
			[defaultGroupArr release];
		}
		
		
		appDelegate.contactCount = [[[arrayData objectForKey:@"response"] objectForKey:@"countContact"] intValue];
		NSString *file1=[DOCUMENTS_FOLDER stringByAppendingPathComponent:@"newlogin.plist"];
		NSArray *key1 = [NSArray arrayWithObjects:@"imei",@"number",nil];
		NSArray *obj1 = [NSArray arrayWithObjects:strImei,strPhoneNumber,nil];			
		NSDictionary *param1 = [[NSDictionary alloc] initWithObjects:obj1 forKeys:key1];
		[param1 writeToFile:file1 atomically:YES];
		[param1 release];
		NSMutableDictionary *accArray =[[NSMutableDictionary alloc] initWithContentsOfFile:[NSString stringWithFormat:@"%@/allAccount.plist",DOCUMENTS_FOLDER]];
		if (accArray == NULL) {
			accArray =[[NSMutableDictionary alloc] init];
		}
			NSMutableArray *acc = [accArray  objectForKey:strPhoneNumber];
			if (acc == nil) 
			{
				acc =[[NSMutableArray alloc] initWithObjects:@"1",@"0",nil];
				[accArray  setObject:acc forKey:strPhoneNumber];
				[accArray writeToFile:[NSString stringWithFormat:@"%@/allAccount.plist",DOCUMENTS_FOLDER] atomically:YES];
				[accArray  release];
				[acc release];
			}
			else 
			{
				[accArray  release];

			}

		if (flag == 1) 
		{
			[self.view removeFromSuperview];
			appDelegate.flagSetting =10;
		}
		else
		if(flag == 2)
		{
			[self dismissModalViewControllerAnimated:YES];
		}
		else if(flag == 3)
		{
			[self.view removeFromSuperview];
		 	appDelegate.flagSetting =10;

		}
			
	}else
	if (responsecode == 2) 
	{  // 2: error
		countActive =0;
		submit_button.hidden = NO;
		cancel_button.hidden = NO;
		text_Phone.hidden = NO;
		flagForAlert = 2;
		UIAlertView *alert =[[UIAlertView alloc] initWithTitle:@" Error " message:message
													  delegate:self cancelButtonTitle:@"OK"
											 otherButtonTitles:nil];
		[alert show];
		[alert release];
	}
	if (responsecode == 3) 
	{
		countActive =0;
		submit_button.hidden = NO;
		cancel_button.hidden = NO;
		text_Phone.hidden = NO;
		//// 3: imei va phone moi -> dk moi
		if (flag == 3) 
		{
			NSMutableDictionary *newinfo = [[NSMutableDictionary alloc] init];
		    [newinfo setObject:@"" forKey:@"imei"];
			[newinfo setObject:@"" forKey:@"number"];
			[newinfo writeToFile:[NSString stringWithFormat:@"%@/newlogin.plist",DOCUMENTS_FOLDER] atomically:YES];		
			[newinfo autorelease];
			
		}
		else
		{
		flagForAlert = 3;
		UIAlertView *alert =[[UIAlertView alloc] initWithTitle:nil message:message
													  delegate:self cancelButtonTitle:@"OK"
											 otherButtonTitles:@"Cancel",nil];
		[alert show];
		[alert release];
		}		
		
	}	
	if (responsecode == 4) 
	{ 
		countActive =0;
		submit_button.hidden = NO;
		cancel_button.hidden = NO;
		text_Phone.hidden = NO;
		// 4: new number + emei exist
		
		//NSLog(@"responsecode =4");		
		flagForAlert =  4;
		UIAlertView *alert =[[UIAlertView alloc] initWithTitle:nil message:message
													  delegate:self cancelButtonTitle:nil
											 otherButtonTitles:@"Keep Current Account",@"Set Up New Account",@"Cancel",nil];
		[alert  show];
		[alert release];
		
		
	}	
	if (responsecode == 5) 
	{ 
		countActive =0;
		submit_button.hidden = NO;
		cancel_button.hidden = NO;
		text_Phone.hidden = NO;
		// 5: new imei + exist phone number
		//NSLog(@"responsecode =5");
		if (flag == 3) 
		{
			NSMutableDictionary *newinfo = [[NSMutableDictionary alloc] init];
		    [newinfo setObject:@"" forKey:@"imei"];
			[newinfo setObject:@"" forKey:@"number"];
			[newinfo writeToFile:[NSString stringWithFormat:@"%@/newlogin.plist",DOCUMENTS_FOLDER] atomically:YES];		
			[newinfo autorelease];
			//flag = 1;
			
		}else 
		{
		flagForAlert =  5;
		UIAlertView *alert =[[UIAlertView alloc] initWithTitle:nil message:message
													  delegate:self cancelButtonTitle:nil
											 otherButtonTitles:@"Keep Current Account",@"Set Up New Account",@"Cancel",nil];
		
		[alert show];
		[alert release];
		}
	}	
	if (responsecode == 6) 
	{ 
		submit_button.hidden = YES;
		cancel_button.hidden = YES;
		text_Phone.hidden = YES;
		
		////
		if([[NSFileManager defaultManager] fileExistsAtPath:[NSString stringWithFormat:@"%@/respond.plist",DOCUMENTS_FOLDER]])
		{
			
		}
		else
		{
			[[NSFileManager defaultManager] createFileAtPath:[NSString stringWithFormat:@"%@/respond.plist",DOCUMENTS_FOLDER] contents:nil attributes:nil];
			NSMutableDictionary *repondse = [[NSMutableDictionary alloc] init];
			[repondse setObject:@"6" forKey:strPhoneNumber];
			[repondse writeToFile:[NSString stringWithFormat:@"%@/respond.plist",DOCUMENTS_FOLDER] atomically:YES];
			[repondse release];
		}
		// 6: account chua active
		/////////////////
		NSString *file=[DOCUMENTS_FOLDER stringByAppendingPathComponent:@"newlogin.plist"];
		NSArray *key = [NSArray arrayWithObjects:@"imei",@"number",nil];
		NSArray *obj = [NSArray arrayWithObjects:strImei,strPhoneNumber,nil];			
		NSDictionary *param = [[NSDictionary alloc] initWithObjects:obj forKeys:key];
		[param writeToFile:file atomically:YES];
		[param release];
		/////////////////////
		
		countActive++;
		if (countActive <= 1)
		{
			flagForAlert = 6;
			isDissmiss = NO;
			alert1 =[[UIAlertView alloc] initWithTitle:nil message:message
														  delegate:self cancelButtonTitle:@"Ok"
												 otherButtonTitles:nil];
			[alert1 show];
			
		//	[alert1 dismissWithClickedButtonIndex:0 animated:YES];
			
			[alert1 release]; 
		}
		else
		{
			activMessage =[message  retain];
			flagForAlert = 7;
			UIAlertView *alert =[[UIAlertView alloc] initWithTitle:nil message:NSLocalizedString(@"TextActive",@"")
														  delegate:self cancelButtonTitle:@"Yes"
												 otherButtonTitles:@"No",nil];
			[alert show];
			[alert release];
		}
		/*
		NSString *fileactive=[NSString stringWithFormat:@"%@/countactive.plist",DOCUMENTS_FOLDER];
		if ([[NSFileManager defaultManager]fileExistsAtPath:fileactive ]) 
		{
			NSMutableDictionary *active =[[NSMutableDictionary alloc] initWithContentsOfFile:fileactive];
			NSString *numberactive = [active objectForKey:strPhoneNumber];

			NSLog(@" numberactive : %@",numberactive);
			if ([numberactive length] == 0)
			{
				NSLog(@"alo");
				if (active != nil) {
					[active  release];
					active = nil;
				}
				active =[[NSMutableDictionary alloc] init];
				[active setValue:@"0" forKey:strPhoneNumber];
				NSLog(@"write to file");
				NSLog(@" %@",active);
				[active writeToFile:fileactive atomically:NO];
				[active  autorelease];
				flagForAlert = 6;
				NSLog(@" ?");
				UIAlertView *alert =[[UIAlertView alloc] initWithTitle:nil message:message
															  delegate:self cancelButtonTitle:@"Ok"
													 otherButtonTitles:nil];
				[alert show];
				[alert release]; 
				
			}else
			{
				NSLog(@"co ton tai file");
				if (active != nil) 
				{
					[active  release];
					active = nil;
				}
				NSArray *ob =[NSArray arrayWithObjects:@"1",nil];
				NSArray *ke =[NSArray arrayWithObjects:strPhoneNumber,nil];
				active =[[NSMutableDictionary alloc] initWithObjects:ob forKeys:ke];		
				[active writeToFile:fileactive atomically:YES];
				[active  release];
				activMessage =[message  retain];
				flagForAlert = 7;
				UIAlertView *alert =[[UIAlertView alloc] initWithTitle:nil message:NSLocalizedString(@"TextActive",@"")
															  delegate:self cancelButtonTitle:@"Yes"
													 otherButtonTitles:@"No",nil];
				[alert show];
				//[alert performSelector:@selector(show) withObject:nil afterDelay:3.0];
				[alert release];
			}

			
			
		}
		else 
		{
			[[NSFileManager defaultManager] createFileAtPath:fileactive contents:nil attributes:nil];
			NSArray *ob =[NSArray arrayWithObjects:@"0",nil];
			NSArray *ke =[NSArray arrayWithObjects:strPhoneNumber,nil];
			NSMutableDictionary *active =[[NSMutableDictionary alloc] initWithObjects:ob forKeys:ke];
			[active writeToFile:fileactive atomically:YES];
			[active  release];
			flagForAlert = 6;
			NSLog(@"khong ton tai file");
			UIAlertView *alert =[[UIAlertView alloc] initWithTitle:nil message:message
														  delegate:self cancelButtonTitle:@"Ok"
												 otherButtonTitles:nil];
			[alert show];
			[alert release];
		}
		 */
		//////
		//NSLog(@"responsecode = 6");
		
		
	}	

}

- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex
{
	
	if (buttonIndex ==0) 
	{
		if(flagForAlert == 2)// error
		{}
		else
			if(flagForAlert == 3)/// new account
			{
				NSArray *key = [[NSArray alloc] initWithObjects:@"imei",@"number",@"phoneType",@"do",nil];
				NSArray *obj = [[NSArray alloc] initWithObjects:strImei,strPhoneNumber,@"1",@"NEW",nil];
				NSDictionary *param = [[NSDictionary alloc] initWithObjects:obj forKeys:key];
				
				[restConnection postPath:[NSString stringWithFormat:@"/phones?format=json"]withOptions:param];
				[key release];
				[obj release];
				[param release];
			}
			else
				if (flagForAlert == 4) //new account
				{
					/*
					 NSArray *key = [[NSArray alloc] initWithObjects:@"imei",@"number",@"phoneType",@"do",nil];
					 NSArray *obj = [[NSArray alloc] initWithObjects:strImei,strPhoneNumber,@"1",@"NEW",nil];
					 NSDictionary *param = [[NSDictionary alloc] initWithObjects:obj forKeys:key];
					 
					 [restConnection postPath:[NSString stringWithFormat:@"/phones?format=json"]withOptions:param];
					 */
					
					NSArray *key = [[NSArray alloc] initWithObjects:@"imei",@"number",@"phoneType",@"do",nil];
					NSArray *obj = [[NSArray alloc] initWithObjects:strImei,strPhoneNumber,@"1",@"UPDATE",nil];
					NSDictionary *param = [[NSDictionary alloc] initWithObjects:obj forKeys:key];
					[restConnection postPath:[NSString stringWithFormat:@"/phones?format=json"]withOptions:param];
					
					[key release];
					[obj release];
					[param release];
					
				}
				else
					if (flagForAlert == 5)//new account
					{
						/*
						 NSArray *key = [[NSArray alloc] initWithObjects:@"imei",@"number",@"phoneType",@"do",nil];
						 NSArray *obj = [[NSArray alloc] initWithObjects:strImei,strPhoneNumber,@"1",@"NEW",nil];
						 NSDictionary *param = [[NSDictionary alloc] initWithObjects:obj forKeys:key];
						 
						 [restConnection postPath:[NSString stringWithFormat:@"/phones?format=json"]withOptions:param];
						 
						 */
						
						NSArray *key = [[NSArray alloc] initWithObjects:@"imei",@"number",@"phoneType",@"do",nil];
						NSArray *obj = [[NSArray alloc] initWithObjects:strImei,strPhoneNumber,@"1",@"UPDATE",nil];
						NSDictionary *param = [[NSDictionary alloc] initWithObjects:obj forKeys:key];
						[restConnection postPath:[NSString stringWithFormat:@"/phones?format=json"]withOptions:param];
						
						[key release];
						[obj release];
						[param release];
					}
					else
						if (flagForAlert == 6) //thoat app de active
						{
							isDissmiss = YES;
							actSignup.hidden = NO;
							[actSignup startAnimating];
							[self performSelector:@selector(getdata) withObject:nil afterDelay:3.0];
						}
						else 
							if (flagForAlert == 7 )
							{
								isDissmiss = NO;
								flagForAlert = 6;
								alert1 =[[UIAlertView alloc] initWithTitle:nil message:activMessage
																  delegate:self cancelButtonTitle:@"Ok"
														 otherButtonTitles:nil];
								[alert1 show];
								[alert1 release]; 
								//	alert1 = nil;
								
								[activMessage  release];
								
								
							}
		
							else 
								if (flagForAlert == 10) 
								{
									exit(0);
								}
								else 
									if (flagForAlert == 20) 
									{
										//	appDelegate.flagSetting =2;
										//NSLog(@" alo mot hai ba bon alo");
										appDelegate.tabBarController.selectedIndex = 2;
										
									}else
										if(flagForAlert == 30)	/// xem video
										{
											/*
											video =[[VideoViewController alloc] init];
											video.flag == 1;
											[appDelegate.window addSubview:video.view];
											//[self.tabBarController presentModalViewController:video animated:YES];
											NSLog(@" xe video xong thi quit");
											countTime= 221;
											countDownTimer=[NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(timerTick) userInfo:nil repeats:YES];
											*/
										}
		
		
	}
	else
		if(buttonIndex == 1)
			
		{
			//NSLog(@"buttonindex = 1");
			if (flagForAlert == 4) // user old account (update number)
			{
				
				NSArray *key = [[NSArray alloc] initWithObjects:@"imei",@"number",@"phoneType",@"do",nil];
				NSArray *obj = [[NSArray alloc] initWithObjects:strImei,strPhoneNumber,@"1",@"NEW",nil];
				NSDictionary *param = [[NSDictionary alloc] initWithObjects:obj forKeys:key];
				
				[restConnection postPath:[NSString stringWithFormat:@"/phones?format=json"]withOptions:param];
				[key release];
				[obj release];
				[param release];
				
				/*
				 NSArray *key = [[NSArray alloc] initWithObjects:@"imei",@"number",@"phoneType",@"do",nil];
				 NSArray *obj = [[NSArray alloc] initWithObjects:strImei,strPhoneNumber,@"1",@"UPDATE",nil];
				 NSDictionary *param = [[NSDictionary alloc] initWithObjects:obj forKeys:key];
				 [restConnection postPath:[NSString stringWithFormat:@"/phones?format=json"]withOptions:param];
				 */
			}
			else
				if (flagForAlert == 5) //  user old account (update imei)
				{
					/*
					 NSArray *key = [[NSArray alloc] initWithObjects:@"imei",@"number",@"phoneType",@"do",nil];
					 NSArray *obj = [[NSArray alloc] initWithObjects:strImei,strPhoneNumber,@"1",@"UPDATE",nil];
					 NSDictionary *param = [[NSDictionary alloc] initWithObjects:obj forKeys:key];
					 [restConnection postPath:[NSString stringWithFormat:@"/phones?format=json"]withOptions:param];
					 */
					
					NSArray *key = [[NSArray alloc] initWithObjects:@"imei",@"number",@"phoneType",@"do",nil];
					NSArray *obj = [[NSArray alloc] initWithObjects:strImei,strPhoneNumber,@"1",@"NEW",nil];
					NSDictionary *param = [[NSDictionary alloc] initWithObjects:obj forKeys:key];
					
					[restConnection postPath:[NSString stringWithFormat:@"/phones?format=json"]withOptions:param];
					
					[key release];
					[obj release];
					[param release];
				}
				else 
					if(flagForAlert == 6)
					{
					}
					else 
						if (flagForAlert == 7)
						{
							submit_button.hidden = NO;
							cancel_button.hidden = NO;
							text_Phone.hidden = NO;
							text_Phone.text = strPhoneNumber;
							UIAlertView *alert =[[UIAlertView alloc] initWithTitle:nil 
																		   message:NSLocalizedString(@"VerifyPhone",@"")
																		  delegate:nil
																 cancelButtonTitle:@"Ok"
																 otherButtonTitles:nil];
							[alert show];
							[alert release];
						}
						else
							if(flagForAlert == 20)
							{
								
								appDelegate.tabBarController.selectedIndex = 3;
								
							}else
								if(flagForAlert == 30)
								{
									
									[self process];				
								}
			
			
		}
		else 
			if (buttonIndex == 2)
			{
				//NSLog(@"button index = 2");
				
				
			}
			else 
			{
				
			}
	
	
}
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
	}

- (void) DimisAlertView1
{
	//NSLog(@"dismisalert view");
	if (alert1) 
	{
		if (flagForAlert == 6 && !isDissmiss) 
		{
			//NSLog(@"1");
			[alert1 dismissWithClickedButtonIndex:0 animated:YES];
			//NSLog(@"2");
			[self alertView:alert1 didDismissWithButtonIndex:0];
			//NSLog(@"3");
			alert1 = nil;
		}
	}
	else
	{
		//NSLog(@"alert1== NULL");
	}
	
	 
}
 
@end
