//
//  SOSBEACONAppDelegate.m
//  SOSBEACON
//
//  Created by Tran Ngoc Anh on 08/06/2010.
//  Copyright CNC 2010. All rights reserved.
//

#import "SOSBEACONAppDelegate.h"
#import "SplashView.h"
#import "LoginView.h"
#import "HomeView.h"
#import "TermsService.h"
#import "Register.h"
#import "StatusView.h"
#import "Uploader.h"
#import <AVFoundation/AVFoundation.h>
#import "GroupsView.h"
#import "TableGroup.h"
#import "WaitActive.h"
#import "Tracking.h"
#import "YOSUser.h"
#import "YOSUserRequest.h"
#import "NSString+SBJSON.h"
#import "ContactListViewController.h"
#import "AddContactViewController.h"
#import "VideoViewController.h"
#import "NewLogin.h"

#define ST_ImageRecordFrequency @"imageRecordFrequency"

#define ST_VoiceRecordDuration @"voiceRecordDuration"
#define ST_LocationReporting @"locationReporting"
#define ST_EmergencySetting @"emergencySetting"
#define ST_SendToAlert @"sendToAlert"
#define ST_IncomingGovernment @"incomingGovernment"
#define ST_SamaritanStatus @"samaritanStatus"
#define ST_SamaritanRange @"samaritanRange"
#define ST_ReceiverSamaritan @"receiverSamaritan"
#define ST_ReciveRange @"receiveRangeSamaritan"
#define CK_CheckingIn @"checkingIn"

@implementation SOSBEACONAppDelegate

@synthesize window;
@synthesize flagsentalert;
@synthesize viewHome;
@synthesize loginView;
@synthesize apiKey,userID,settingId;
@synthesize phoneID;
@synthesize settingArray;
@synthesize panicEmgergency;
@synthesize coordinate,alertLocation,informationArray,latitudeString,longitudeString;
@synthesize webView;
@synthesize reg,tabBarController;
@synthesize statusView;
@synthesize uploader,logout,groupView,locationManager,saveContact,savePerson,waitActiveView;
@synthesize flagSetting;
@synthesize session;
@synthesize launchDefault;
@synthesize oauthResponse;
@synthesize guid;
@synthesize emailList;
@synthesize addContactViewController;
@synthesize phone,contactCount;
@synthesize groupName;
@synthesize flagforGroup;
@synthesize respondcode;
@synthesize canShowVideo;
@synthesize newlogin;

//Update location
- (void)showVideo
{
	VideoViewController *video =[[VideoViewController alloc] init];
	[self.tabBarController dismissModalViewControllerAnimated:YES];
	[video  autorelease];
}
- (void)updateLocation{
	[locationManager startUpdatingLocation];
}

//Function delay updated location
- (void)countUpdateLocation {
	[self performSelector:@selector(updateLocation) withObject:nil afterDelay:1];
}

#pragma mark LocationDelegate
- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation
{
	[self countUpdateLocation];

	NSString *tempLat = [[NSString alloc] initWithFormat:@"%g",newLocation.coordinate.latitude];
	self.latitudeString = tempLat;
	[tempLat release];
	NSString *tempLong = [[NSString alloc] initWithFormat:@"%g",newLocation.coordinate.longitude];
	self.longitudeString = tempLong;
	[tempLong release];
	
}

- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error {

	[self countUpdateLocation];
	}
void uncaughtExceptionHandler(NSException *exception) {
    [Tracking trackException:exception];
}



- (void)applicationDidEnterBackground:(UIApplication *)application
{
}

- (void)applicationWillEnterForeground:(UIApplication *)application 
{
	[newlogin DimisAlertView1];
	NSLog(@"ok ngon");
}



- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {  
//	flagSetting = 1;
	
	if (![[NSFileManager defaultManager]  fileExistsAtPath:[NSString stringWithFormat:@"%@/allAccount.plist",DOCUMENTS_FOLDER] ])
	{
		
		[[NSFileManager defaultManager] createFileAtPath:[NSString stringWithFormat:@"%@/allAccount.plist",DOCUMENTS_FOLDER] contents:nil attributes:nil];
	}

	emailList = [[NSMutableArray alloc] init];
	launchDefault = YES;
	
	NSSetUncaughtExceptionHandler(&uncaughtExceptionHandler);
	[Tracking startTracking];
	locationManager = [[CLLocationManager alloc] init];
	locationManager.delegate =self;
	locationManager.desiredAccuracy = kCLLocationAccuracyBest;
	
	[self countUpdateLocation];
	
	self.tabBarController.delegate = self;
	
	informationArray = [[NSMutableDictionary alloc] init]; 
	settingArray = [[NSMutableDictionary alloc] init];
	[settingArray setObject:@"15" forKey:@"imageRecordFrequency"];
	[settingArray setObject:@"1" forKey:@"voiceRecordDuration"];
	[settingArray setObject:@"30" forKey:@"locationReporting"];
	[settingArray setObject:@"0" forKey:@"emergencySetting"];
	[settingArray setObject:@"0" forKey:@"samaritanRange"];
	[settingArray setObject:@"0" forKey:@"incomingGovernment"];
	[settingArray setObject:@"0" forKey:@"samaritanStatus"];
	[settingArray setObject:@"0" forKey:@"receiverSamaritan"];
	[settingArray setObject:@"0" forKey:@"receiveRangeSamaritan"];
	[settingArray setObject:@"0" forKey:@"sendToAlert"];
	[settingArray setObject:@"0" forKey:@"checkingIn"];
	
	
	if ([[NSFileManager defaultManager] fileExistsAtPath:[NSString stringWithFormat:@"%@/info.plist",DOCUMENTS_FOLDER] ]) 
	{
		NSLog(@"co ton tai file");
		
	}
	else
	{
		
		NSString *pass = [[NSString alloc] initWithString:@"1"];
	[[NSFileManager defaultManager] createFileAtPath:[NSString stringWithFormat:@"%@/info.plist",DOCUMENTS_FOLDER] contents:nil attributes:nil];
	[pass writeToFile:[NSString stringWithFormat:@"%@/info.plist",DOCUMENTS_FOLDER] atomically:YES];
		NSLog(@" da luu xong");
		[pass release];
	}
	 
	[window addSubview:tabBarController.view];
		
		splashView = [[SplashView alloc] init];
	newlogin = [[NewLogin alloc] init ];
		///*********** new login
		if ([[NSFileManager defaultManager] fileExistsAtPath:[NSString stringWithFormat:@"%@/newlogin.plist",DOCUMENTS_FOLDER]])
		{
			NSDictionary *newarray = [[NSDictionary alloc] initWithContentsOfFile:[NSString stringWithFormat:@"%@/newlogin.plist",DOCUMENTS_FOLDER]];
			newlogin.strImei = [[newarray objectForKey:@"imei"] retain];
			newlogin.strPhoneNumber = [[newarray objectForKey:@"number"] retain];
			
			NSLog(@" array read from file: %@",newarray);
			NSLog(@"imei : %@",newlogin.strImei);
			NSLog(@"phone : %@",newlogin.strPhoneNumber);
			
			if ([newlogin.strPhoneNumber isEqualToString:@""]||[newlogin.strImei isEqualToString:@""]) 
			{
				newlogin.flag = 1 ;
				[window addSubview:newlogin.view];

			}
			else 
			{
				
				NSLog(@"getdata");
				newlogin.flag =3;
				newlogin.submit_button.hidden = YES;
				newlogin.cancel_button.hidden = YES;
				newlogin.text_Phone.hidden = YES;
				[window addSubview:newlogin.view];
				phone = [[NSString  alloc]initWithString:newlogin.strPhoneNumber];
				NSLog(@"imei : %@",newlogin.strImei);
				NSLog(@"phone : %@",newlogin.strPhoneNumber);

				[newlogin getdata];
			}
			[newarray release];

		}
		else
		{
			[[NSFileManager defaultManager] createFileAtPath:[NSString stringWithFormat:@"%@/newlogin.plist",DOCUMENTS_FOLDER] contents:nil attributes:nil];
			NSMutableDictionary *newinfo = [[NSMutableDictionary alloc] init];
		    [newinfo setObject:@"" forKey:@"imei"];
			[newinfo setObject:@"" forKey:@"number"];
			[newinfo writeToFile:[NSString stringWithFormat:@"%@/newlogin.plist",DOCUMENTS_FOLDER] atomically:YES];
			newlogin.flag = 1;
			TermsService *termsService = [[TermsService alloc] init] ;
			termsService.view.frame = CGRectMake(0, 0, 320, 480);
			[window addSubview:newlogin.view];
			[window addSubview:termsService.view];
			[newinfo release];
			
		}
	[window addSubview:splashView.view];
	[window makeKeyAndVisible];
		
	//add status view
	statusView.frame = CGRectMake(0, -60, 320, 60);
	[window addSubview:statusView];
	
	uploader = [[Uploader alloc] init];
	[uploader removeAllFileCache]; 
	NSURL *bipURL = [NSURL fileURLWithPath:[[NSBundle mainBundle] pathForResource:@"BIP" ofType:@"mp3"]];
	NSError *error;
	soundBip = [[AVAudioPlayer alloc] initWithContentsOfURL:bipURL error:&error];
	if(error)
	{
		UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"ERROR" message:@"Can't play sound" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
		[alert show];
		[alert release];
	}
	logout = NO;
	saveContact = NO;
	savePerson=NO;
	return YES;
}

#pragma mark -
#pragma mark tabBarControllerDelegate

-(BOOL)tabBarController:(UITabBarController *)tabBarControl shouldSelectViewController:(UIViewController *)viewController{
	if (tabBarControl.selectedIndex == 3) {
		if ([tabBarControl.selectedViewController isMemberOfClass:[GroupsView class]]) {
			GroupsView *tbl = (GroupsView *)tabBarControl.selectedViewController;
			if (saveContact) {
				[[[tbl tableGroups] tbGroup] checkEditContact];
				
				saveContact = NO;
			}
			else if (savePerson && saveContact) {
				
				[[[tbl tableGroups] tbGroup] checkEditContact];
				savePerson = NO;
			}
		}
		
	}
	return YES;
}
- (void)tabBarController:(UITabBarController *)tabBarController1 didSelectViewController:(UIViewController *)viewController {
		
	[homNavigationController popToRootViewControllerAnimated:NO];
/*
	NewLogin *new = [[NewLogin alloc] init];
	[self.tabBarController.view addSubview:new.view];
*/	
}



- (void)dealloc {
	[phone release];
	[addContactViewController release];
	[latitudeString release];
	[longitudeString release];
	[soundBip release];
	[uploader release];
	[statusView release];
	[tabBarController release];
	[reg release];
	[locationManager release];
	[alertLocation release];
	[webView release];
	[informationArray release];
	[viewHome release];
	[loginView release];
    [window release];
	[groupView release];
	[homNavigationController release];
    [super dealloc];
}

- (void)playSound {
	soundBip.volume = 1.0;
	[soundBip play];
}

- (void)playSound3 {
	[soundBip play];
	[soundBip performSelector:@selector(play) withObject:nil afterDelay:1];
	[soundBip performSelector:@selector(play) withObject:nil afterDelay:2];
}

- (void) hiddenSplash {
	splashView = [[SplashView alloc] init];
	[splashView setNoConnection:YES];
	[window addSubview:splashView.view];
	[splashView release];
}


- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url 
{
	launchDefault = NO;
	
	if (!url) { 
		return NO; 
	}
	
	NSArray *pairs = [[url query] componentsSeparatedByString:@"&"];
	NSMutableDictionary *response = [NSMutableDictionary dictionary];
	
	for (NSString *item in pairs) {
		NSArray *fields = [item componentsSeparatedByString:@"="];
		NSString *name = [fields objectAtIndex:0];
		NSString *value = [[fields objectAtIndex:1] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
		
		[response setObject:value forKey:name];
	}
	
	self.oauthResponse = response;
	
	[self createYahooSession];
	
	return YES;
}

- (void)handlePostLaunch
{
	[self createYahooSession];
}

- (void)createYahooSession
{
	// create session with consumer key, secret and application id
	// set up a new app here: https://developer.yahoo.com/dashboard/createKey.html
	// because the default values here won't work
	
	self.session = [YOSSession sessionWithConsumerKey:@"dj0yJmk9VlY0ZmE2TWFETWJlJmQ9WVdrOWFYQXlWVzUxTjJrbWNHbzlOelF3T0RRNE5qWXkmcz1jb25zdW1lcnNlY3JldCZ4PWM0" 
									andConsumerSecret:@"6bff9fe33e498f037b246cc8d7049ae5a91755f6" 
									 andApplicationId:@"ip2Unu7i"];
	
	 
	/*
	self.session = [YOSSession sessionWithConsumerKey:@"dj0yJmk9WWs1Y05hbVlDT3hNJmQ9WVdrOVpVSnZUVTR3TkRJbWNHbzlNVEUwTmpNMU5ERTJNZy0tJnM9Y29uc3VtZXJzZWNyZXQmeD01Yg" 
								andConsumerSecret:@"f07151ee4e9fd53f7f7d1756beb51ca8e32b0790" 
									 andApplicationId:@"ip2Unu7i"];
	*/
	 
	if(self.oauthResponse) {
		NSString *verifier = [self.oauthResponse valueForKey:@"oauth_verifier"];
		[self.session setVerifier:verifier];
	}
	
	BOOL hasSession = [self.session resumeSession];
	
	if(!hasSession) {
		[self.session sendUserToAuthorizationWithCallbackUrl:@"http://sosbeacon.org:8085/web/contacts/oauth?a=1111&b=222"];
	} else {
		[self getUserProfile];
	}
}

- (void)getUserProfile
{
	index = 1;
	// initialize the profile request with our user.
	YOSUserRequest *userRequest = [YOSUserRequest requestWithSession:self.session];
	
	// get the users profile
	[userRequest fetchProfileWithDelegate:self];
}

- (void)requestDidFinishLoading:(YOSResponseData *)data
{
	if (index == 1) {
		NSDictionary *userProfile = [[data.responseText JSONValue] objectForKey:@"profile"];
		guid = [[NSString alloc] initWithString:[userProfile objectForKey:@"guid"]];
		//if(userProfile) {
		//		[viewController setUserProfile:userProfile];
		//	}
		YOSUserRequest *userRequest = [YOSUserRequest requestWithSession:self.session];
		
		// get the users profile
		[userRequest fetchContactsWithStart:0 andCount:500 withDelegate:self];
		index = 2;
	}
	else {
		NSMutableArray *contactList = [[[data.responseText JSONValue] objectForKey:@"contacts"] objectForKey:@"contact"];
		for (int i = 0; i < [contactList count]; i++) {
			NSArray *fiels = [[contactList objectAtIndex:i] objectForKey:@"fields"];
			NSMutableDictionary *dic = [[NSMutableDictionary alloc] init];
			BOOL hasEmail = NO;
			for (int j = 0; j< [fiels count]; j ++) {
				NSString *type = [[fiels objectAtIndex:j] objectForKey:@"type"];
				if ([type isEqualToString:@"email"]) {
					[dic setObject:[[fiels objectAtIndex:j] objectForKey:@"value"] forKey:@"email"];
					hasEmail = YES;
				}
				if ([type isEqualToString:@"name"]) {
					NSString *str = [[NSString alloc] initWithFormat:@"%@ %@ %@",[[[fiels objectAtIndex:j] objectForKey:@"value"] objectForKey:@"familyName"],[[[fiels objectAtIndex:j] objectForKey:@"value"] objectForKey:@"middleName"],[[[fiels objectAtIndex:j] objectForKey:@"value"] objectForKey:@"givenName"]];
					[dic setObject:[str stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]] forKey:@"name"];
					[str release];
				}
				if ([type isEqualToString:@"phone"]) {
					[dic setObject:[[fiels objectAtIndex:j] objectForKey:@"value"] forKey:@"phone"];
				}
			}
			if ([dic objectForKey:@"phone"] == nil) {
				[dic setObject:@"" forKey:@"phone"];
			}
			if ([dic objectForKey:@"name"] == nil) {
				[dic setObject:@"" forKey:@"name"];
			}
			if (hasEmail) {
				[emailList addObject:dic];
			}
			[dic release];
		}
		ContactListViewController *ymailContactList = [[ContactListViewController alloc] init];
		ymailContactList.contactList = [[NSMutableArray alloc] initWithArray:emailList];
		ymailContactList.title = @"Ymail Contacts";
		self.addContactViewController.gettingContact = NO;
		self.addContactViewController.indicator.hidden = YES;
		[self.addContactViewController.navigationController pushViewController:ymailContactList animated:YES];
		[ymailContactList release];
		[self.session clearSession];
	}
}

- (void)abc:(NSString *)str {
	NSMutableDictionary *response = [NSMutableDictionary dictionary];
	[response setObject:str forKey:@"oauth_verifier"];
	
	self.oauthResponse = response;
	
	[self createYahooSession];
}
//- (void)logout {
//	[self.session clearSession];
//}

@end








