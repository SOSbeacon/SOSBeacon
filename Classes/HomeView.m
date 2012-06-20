//
//  HomeView.m
//  SOSBEACON
//
//  Created by cncsoft on 7/30/10.
//  Copyright 2010 CNC. All rights reserved.
//

#import <QuartzCore/QuartzCore.h>
#import "HomeView.h"
#import "SOSBEACONAppDelegate.h"
#import "RestConnection.h"
#import "ValidateData.h"
#import "CaptorView.h"
#import "SOWebView.h" 
#import "SlideToCancelViewController.h"
#import "CheckingIn.h"
#import "Uploader.h"
#import "StatusView.h"
#import "VideoViewController.h"

@implementation HomeView
@synthesize rest;
@synthesize lblVersion;
@synthesize vwImOkPopUp,vwINeedHelpPopUp;

-(void)showvideo
{
	flag =4;
	NSString *pass = [appDelegate.informationArray objectForKey:@"password"];
	NSString *emergency = [appDelegate.settingArray objectForKey:ST_EmergencySetting];
	//[pass length]
	NSLog(@" pass length ========= : %d",[pass length]);
	NSLog(@" %@ ,%@",pass,emergency);
	
	if ([[NSFileManager defaultManager]  fileExistsAtPath:[NSString stringWithFormat:@"%@/allAccount.plist",DOCUMENTS_FOLDER] ]) 
	{
		
		NSMutableDictionary *accArray =[[NSMutableDictionary alloc] initWithContentsOfFile:[NSString stringWithFormat:@"%@/allAccount.plist",DOCUMENTS_FOLDER]];
		NSString *strPhoneNumber = appDelegate.phone;
		NSLog(@" home view-------------> %@",strPhoneNumber);
		NSMutableArray *acc = [accArray  objectForKey:strPhoneNumber];
		if (acc == nil) 
		{
			
			
		}
		else 
		{
			NSInteger active = [[acc objectAtIndex:1] intValue];
			NSLog(@" active : %d",active);
			if (active == 0 && ([pass length] == 0 ) && [emergency isEqualToString:@"0"]&&( appDelegate.contactCount == 0)&&appDelegate.canShowVideo)
			{
				NSLog(@" home view-------------> ");
				[acc replaceObjectAtIndex:1 withObject:@"1"];
				[accArray  setObject:acc forKey:strPhoneNumber];
				[accArray writeToFile:[NSString stringWithFormat:@"%@/allAccount.plist",DOCUMENTS_FOLDER] atomically:YES];
				
				flagforAlert =1;
				UIAlertView *alert =[[UIAlertView alloc] initWithTitle:nil message:NSLocalizedString(@"Video",@"")
															  delegate:self cancelButtonTitle:@"Yes"
													 otherButtonTitles:@"Not Now",nil];
				[alert show];
				[alert release];
				
			}
			
			else
			//if ([emergency isEqualToString:@"0"] || [pass length] == 0)
			if ([emergency isEqualToString:@"0"] ||( [pass length] == 0 )||(appDelegate.contactCount == 0))
			{
				appDelegate.flagSetting = 3; 
				appDelegate.tabBarController.selectedIndex = 3;
				NSLog(@"sao the nhi lai loi roi a");
			}
			else 
			{
				appDelegate.tabBarController.selectedIndex = 0;

			}

			
		
		}
		
		[accArray release];
	}
}
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
	if (flagforAlert ==1)
	{
		if(buttonIndex == 0)
		{
			VideoViewController *video = [[VideoViewController alloc] init];
			[self presentModalViewController:video animated:YES];
			flag = 2;
		}else 
		if(buttonIndex ==1)	
		{
			
			NSString *pass = [appDelegate.informationArray objectForKey:@"password"];
			NSString *emergency = [appDelegate.settingArray objectForKey:ST_EmergencySetting];
				if ([emergency isEqualToString:@"0"] || [pass length] == 0 ||appDelegate.contactCount == 0) 
				{
					appDelegate.flagSetting = 3; 
					appDelegate.tabBarController.selectedIndex = 3;
				}
				else 
			{
				appDelegate.tabBarController.selectedIndex = 0;

			}

			
		}

	}
	else
	if (flagforAlert == 100)
	{
	
		if (buttonIndex == 0)
		{
			/*
			 */
			[self dismissUIView: self.vwINeedHelpPopUp];
			appDelegate.flagSetting = 100;
			appDelegate.tabBarController.selectedIndex = 3;
		}
		else 
		if(buttonIndex == 1)
		{
			
		}

	}
	

}
- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
	[super didReceiveMemoryWarning];
    
	[self viewDidUnload];
	[self viewDidLoad];
}

#pragma mark -
-(void)timerTick
{
	//NSLog(@"time tick---------------");
	if (appDelegate.flagSetting == 10)
	{
		[countDownTimer invalidate];
		[self showvideo];
		//[self performSelector:@selector(showvideo) withObject:nil afterDelay:2.0];
	}
	//if (flag == 4)[ countDownTimer invalidate];
}
- (void)viewDidLoad {
	checkin.hidden = YES;
	countDownTimer=[NSTimer scheduledTimerWithTimeInterval:2.5 target:self selector:@selector(timerTick) userInfo:nil repeats:YES];
	
	flag =1;
	[super viewDidLoad];

	lblVersion.text=[NSString stringWithFormat:@"Version %@",[[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"]];
	nedhelpversion.text = lblVersion.text;
	Imokversion.text = lblVersion.text;
	appDelegate = (SOSBEACONAppDelegate *)[[UIApplication sharedApplication] delegate];
	actAlert.hidden=YES;
	rest = [[RestConnection alloc] initWithBaseURL:SERVER_URL];
	rest.delegate = self;	
	isSendOK=NO;
	}
	
- (void)viewDidUnload 
{

    [super viewDidUnload];
}

- (void)viewWillAppear:(BOOL)animated {
	[super viewWillAppear:animated];
	slideToCancel.enabled = YES;
	slideToCancel2.enabled = YES;
	slideToCancel3.enabled=YES;
	appDelegate.uploader.delegate = self;
	if (flag == 1 ) 
		{
		//	[self performSelector:@selector(showvideo) withObject:nil afterDelay:2.5];
		}

	else 
	if (appDelegate.flagSetting == 1) 
	{
		[self showvideo];
	}
	else 
	{
		if (flag ==2) 
		{
			[self showvideo];
		}
	}


}

- (void)dealloc {	
	[self.vwImOkPopUp release];
	[self.vwINeedHelpPopUp release];
	
	 [slideToCancel2 release];
	 [slideToCancel3 release];
	 [slideToCancel release];
	 slideToCancel = nil;
	 slideToCancel2 = nil;
	 slideToCancel3=nil;
	 
	[actAlert release];
	[lblSendAlert release];
	[actAlert release];
	[rest release];
    [super dealloc];
}

- (void) cancelled:(SlideToCancelViewController*)sender {
	if(sender==slideToCancel){
		NSLog(@"%^$%^ emergency Phone is :%@",[appDelegate.settingArray objectForKey:@"emergencySetting"]);
		if ([[appDelegate.settingArray objectForKey:@"emergencySetting"] isEqualToString:@"0"]) {
			//UIAlertView *alertPhone = [[UIAlertView alloc] initWithTitle:@"Message" message:@"Emergency phone number not yet set for your location" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
			//[alertPhone show];
			//[alertPhone release];
			[self doEmercgenyCall];
			[slideToCancel loadView];
			slideToCancel.enabled=YES;
			slideToCancel.view.alpha=1.0;
		}
		else {
			slideToCancel3.enabled=NO;
			slideToCancel2.enabled=NO;
			slideToCancel.view.alpha=0.0;
			[self doEmercgenyCall];
			
			/*
			actAlert.hidden=NO;
			[actAlert startAnimating];
			lblSendAlert.text = @"Sending alert to server...";
			loadIndex = 0;
			NSArray *key1 = [NSArray arrayWithObjects:@"phoneid",@"token",@"type",nil];
			NSArray *obj1 = [NSArray arrayWithObjects:[NSString stringWithFormat:@"%d",appDelegate.phoneID],
							 appDelegate.apiKey,@"1",nil];
			NSDictionary *param1 =[NSDictionary dictionaryWithObjects:obj1 forKeys:key1];
			[rest postPath:[NSString stringWithFormat:@"/alert?latitude=%@&longtitude=%@&format=json",appDelegate.latitudeString,appDelegate.longitudeString] withOptions:param1];
			*/
			
		}
			}
	else if(sender==slideToCancel2){
		//Sending alert to server
		slideToCancel.enabled=NO;
		slideToCancel3.enabled=NO;
		[self doAlert];
	
		/*
		appDelegate.uploader.delegate = self;
		[appDelegate.uploader sendAlert];
		appDelegate.uploader.isAlert=TRUE;
		 */
	}
	
	else if(sender == slideToCancel3) {
				
		slideToCancel.enabled=NO;
		slideToCancel2.enabled=NO;
		[self doCheckIn];
	}

}
-(void)doEmercgenyCall{
	if ([[appDelegate.settingArray objectForKey:@"emergencySetting"] isEqualToString:@"0"]) 
	{
		flagforAlert = 100;
		UIAlertView *alertPhone = [[UIAlertView alloc] initWithTitle:@"Message" message:NSLocalizedString(@"SetEmergency",@"")
							delegate:self cancelButtonTitle:@"Yes"
								otherButtonTitles:@"Not Now",nil];
		[alertPhone show];
		[alertPhone release];
	}
	else {
		NSLog(@"call");
		actAlert.hidden=NO;
		[actAlert startAnimating];
		lblSendAlert.text = @"Sending alert to server...";
		loadIndex = 0;
		NSArray *key1 = [NSArray arrayWithObjects:@"phoneId",@"token",@"type",nil];
		NSArray *obj1 = [NSArray arrayWithObjects:[NSString stringWithFormat:@"%d",appDelegate.phoneID],
						 appDelegate.apiKey,@"1",nil];
		NSDictionary *param1 =[NSDictionary dictionaryWithObjects:obj1 forKeys:key1];
		[rest postPath:[NSString stringWithFormat:@"/alert?latitude=%@&longtitude=%@&format=json",appDelegate.latitudeString,appDelegate.longitudeString] withOptions:param1];
		
		
	//	NSArray *key2 = [[NSArray alloc] initWithObjects:@"token",@"phoneId",@"latitude",@"longtitude",@"type",@"toGroup",]
		
		
	}
}
-(void)doAlert{
	isSendOK=NO;
	appDelegate.uploader.delegate = self;
	[appDelegate.uploader sendAlert];
	appDelegate.uploader.isAlert=TRUE;
}
-(void)doCheckIn{
	isSendOK=NO;
	appDelegate.uploader.isAlert=FALSE;
	CheckingIn *viewCheckIn=[[CheckingIn alloc] init];
	[self.navigationController pushViewController:viewCheckIn animated:YES];
	[viewCheckIn release];
}
-(void)doImOkCheckIn{
	isSendOK=YES;
	appDelegate.uploader.delegate = self;
	appDelegate.uploader.isAlert=FALSE;
	[appDelegate.uploader sendImOkAlert];
	
}
#pragma mark -
- (void)uploadFinish
{
	NSLog(@"up load finish");
	NSLog(@"up load finish ----------************........");
}

- (void)requestUploadIdFinish:(NSInteger)uploadId 
{
	NSLog(@"newflag1: %d",newflag1);
	if (newflag1 == 1) 
	{
		//newflag1 =2;
		isSendOK=NO;

		return;
	}
	else
	if (uploadId > 0 && !isSendOK) 
	{
		NSLog(@"65456564564561563131654654654654654+6+64+966565+566545");
		[senAlertaction stopAnimating];
		senAlertaction.hidden = YES;
		startingAlert.hidden = YES;
		///
		slideToCancel2.enabled = YES;
		appDelegate.uploader.autoUpload = YES;
		appDelegate.uploader.isSendAlert = YES;
		CaptorView *captor = [[CaptorView alloc] init];
		captor.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
		[self presentModalViewController:captor animated:YES];	
		appDelegate.flagsentalert = 1;
	//	[captor autorelease];
		isSendOK=NO;

	}
}
-(IBAction)btnCheckIn_Tapped:(id)sender{
	
	//[self dismissUIView: self.vwImOkPopUp];
	newflag1 = 1;
	checkin.hidden = NO;
	imOkaction.hidden =NO;
	[imOkaction startAnimating ];
	
	[self doImOkCheckIn];
	
	[imOkaction performSelector:@selector(stopAnimating) withObject:nil afterDelay:1.0];
	[self performSelector:@selector(hidetext) withObject:nil afterDelay:1.0];
	

	
}
-(void)hidetext
{
	checkin.hidden = YES;
}
-(IBAction)btnCheckInGroup_Tapped:(id)sender{
	
	[self dismissUIView: self.vwImOkPopUp];
	[self doCheckIn];

}
-(IBAction)btnSendAlert_Tapped:(id)sender{
	//[self dismissUIView: self.vwINeedHelpPopUp];
	newflag1 =2;
	senAlertaction.hidden = NO;
	startingAlert.hidden = NO;
	[senAlertaction startAnimating];
	[self doAlert];
	
}
-(IBAction)btnEmergencyPhone_Tapped:(id)sender{
//	[self dismissUIView: self.vwINeedHelpPopUp];
	[self doEmercgenyCall];
}
-(IBAction)btnCancelImOk_Tapped:(id)sender{
	[self dismissUIView: self.vwImOkPopUp];
}

-(IBAction)btnCancelNeedHelp_Tapped:(id)sender{
	[self dismissUIView: self.vwINeedHelpPopUp];
}
-(void)dismissUIView:(UIView*)theView{
	[UIView beginAnimations:nil context:NULL];
	theView.frame=CGRectMake(0, 480, theView.frame.size.width, theView.frame.size.height);
	
	
	[UIView commitAnimations];
	[theView performSelector:@selector(removeFromSuperview) withObject:nil afterDelay:0.5];
	
}
-(void)showUIView:(UIView*)theView{
	
	theView.frame=CGRectMake(0, 480, theView.frame.size.width, theView.frame.size.height);
	[self.tabBarController.view addSubview:theView];
	[UIView beginAnimations:nil context:NULL];
	theView.frame=CGRectMake(0, 480-theView.frame.size.height, theView.frame.size.width, theView.frame.size.height);
	[UIView commitAnimations];
}
#pragma mark ActionMenus
-(IBAction)showImOKMenu:(id)sender{
	imOkaction.hidden = YES;
	currentAction=ActionType_OK;
	[self showUIView:self.vwImOkPopUp];
	
//	[self.navigationController pushViewController:self.vwImOkPopUp animated:YES];
	
	//self.vwImOkPopUp.frame=CGRectMake(0, 480-self.vwImOkPopUp.frame.size.height, self.vwImOkPopUp.frame.size.width, self.vwImOkPopUp.frame.size.height);
	//[self.tabBarController.view addSubview:self.vwImOkPopUp];
	
	
	
	//self.vwImOkPopUp.frame=self.view.frame;
	/*
	UIActionSheet *sheet=[[UIActionSheet alloc] init];
	sheet.actionSheetStyle=UIBarStyleDefault;
	
	[sheet addButtonWithTitle:@"Check-in I'm OK"];
	[sheet addButtonWithTitle:@"Check-in Group Message"];
	[sheet addButtonWithTitle:@"Cancel"];
	sheet.cancelButtonIndex=2;
	sheet.delegate=self;
	[sheet showInView:self.parentViewController.tabBarController.view];
	[sheet release];
	 */
	
}
-(IBAction)showImNeedHelpMenu:(id)sender{
	//[senAlertaction startAnimating];
	senAlertaction.hidden = YES;
	startingAlert.hidden = YES;
	currentAction=ActionType_Help;
	[self showUIView:self.vwINeedHelpPopUp];
	/*
	UIActionSheet *sheet=[[UIActionSheet alloc] init];
	sheet.actionSheetStyle=UIBarStyleDefault;
	
	[sheet addButtonWithTitle:@"Call Emergency Phone"];
	[sheet addButtonWithTitle:@"Send Alert"];
	[sheet addButtonWithTitle:@"Cancel"];
	sheet.cancelButtonIndex=2;
	sheet.destructiveButtonIndex=0;
	sheet.delegate=self;
	[sheet showInView:self.parentViewController.tabBarController.view];
	[sheet release];
	 */
	
}
#pragma mark ActionSheet
- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex{
	
	
	
	switch (currentAction) {
		case ActionType_OK:
		{
			if (buttonIndex==0) //Send I'm Ok
			{
				[self doImOkCheckIn];
				
			}
			if (buttonIndex==1) // Check In
			{
				[self doCheckIn];
			}
		}
			break;
			
		case ActionType_Help:
		{
			if (buttonIndex==0) // Emergency Call
			{
				[self doEmercgenyCall];
			}
			if (buttonIndex==1) // Emergency Alert
			{
				[self doAlert];
			}
		}
			break;
		default:
			break;
	}
	
	
	
	currentAction=ActionType_None;
}
- (void)actionSheetCancel:(UIActionSheet *)actionSheet{
	currentAction=ActionType_None;
}
#pragma mark finishRequest

-(void)finishRequest:(NSDictionary *)arrayData andRestConnection:(id)connector{
	actAlert.hidden = YES;
	[actAlert stopAnimating];
	lblSendAlert.text = @"";
	slideToCancel.enabled = YES;
	slideToCancel.view.alpha = 1.0;
	slideToCancel2.enabled=YES;	
	slideToCancel3.enabled=YES;
	NSLog(@" array data: %@",arrayData);
	if (loadIndex == 0) {
		if ([[[arrayData objectForKey:@"response"] objectForKey:@"success"] isEqualToString:@"true"])
		{	
			//NSLog(@" call roi ma sao no ko call nhi");
			[self callPanic];
			//NSLog(@"ngon");
			
		}else {
			UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error"
																message:@"Error with alert data"
															   delegate:nil
													  cancelButtonTitle:@"Ok"
													  otherButtonTitles:nil];
			[alertView show];
			[alertView release];
		}
	}

}

// function error connection
#pragma mark connectionFail

-(void)cantConnection:(NSError *)error andRestConnection:(id)connector{
	alertView();
	[actAlert stopAnimating];
	lblSendAlert.text = @"";
	actAlert.hidden=YES;	
	slideToCancel.view.alpha = 1.0;
	slideToCancel.enabled = YES;
	slideToCancel2.enabled = YES;
	slideToCancel3.enabled=YES;
}
//Function callemergency
- (void)callPanic {
	NSString *panic = [appDelegate.settingArray objectForKey:ST_EmergencySetting];
	NSLog(@" emergecy number : %@",panic);
	NSURL *phoneNumberURL = [NSURL URLWithString:[NSString stringWithFormat:@"tel:%@",panic]];
	[[UIApplication sharedApplication] openURL:phoneNumberURL];	
	//NSLog(@"ok");
}

@end
