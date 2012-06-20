//
//  LoginView.h
//  SOSBEACON
//
//  Created by Tran Ngoc Anh on 08/06/2010.
//  Copyright 2010 CNC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RestConnection.h"
#import "SOSBEACONAppDelegate.h"

@interface LoginView : UIViewController <RestConnectionDelegate,UIAlertViewDelegate> {
	RestConnection *restConnection;
	SOSBEACONAppDelegate *appDelegate;
	IBOutlet UIScrollView *scrollView;
	WaitActive *waitAcitveView;
	
	UITextField *txtName;
	UITextField *txtPassWord;
	UITextField *txtEmail;
	UITextField *txtPhoneNumber;
	UIActivityIndicatorView *actSignup;
	IBOutlet UIButton *btnSubmit;
	IBOutlet UIButton *btnExit;
	
	NSString *username;
	NSString *password;
	NSString *phone;
	
	NSInteger loadIndex1;
	NSString *uniqueIdentifier; 
	BOOL connected;
	
	BOOL waitingActive;
	NSMutableDictionary *dictDetail;
	int notConnected;
	
	
	NSTimer *timer;
	//BOOL statusActive;
	int countWaitActive;
	UITextField *txtnewPhone;
	UIAlertView *AlertWaitting;
}
@property (nonatomic, retain) NSTimer *timer;
@property (nonatomic, retain) NSString *uniqueIdentifier;
@property (nonatomic, retain) NSString *username;
@property (nonatomic, retain) NSString *password;
@property (nonatomic,retain) NSString *phone;
@property (nonatomic,assign) BOOL waitingActive;
@property (nonatomic,retain) WaitActive *waitAcitveView;

@property (nonatomic, retain) RestConnection *restConnection;

@property (nonatomic, retain) IBOutlet UITextField *txtPhoneNumber;
@property (nonatomic, retain) IBOutlet UITextField *txtName;
@property (nonatomic, retain) IBOutlet  UITextField *txtPassWord;
@property (nonatomic, retain) IBOutlet  UITextField *txtEmail;
@property (nonatomic, retain) IBOutlet UIActivityIndicatorView *actSignup;
@property (nonatomic, retain) UITextField *txtnewPhone;


-(IBAction)signUp:(id)sender;
- (IBAction)textFieldDoneEditing:(id)sender;
- (IBAction)backgroundTap:(id)sender;
-(void)getData;
- (IBAction) Cancel;
- (void)delayAlert;
- (void)sendLocationAuto;

@end
	
