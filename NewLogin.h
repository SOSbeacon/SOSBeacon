//
//  NewLogin.h
//  SOSBEACON
//
//  Created by bon on 7/27/11.
//  Copyright 2011 CNC Software. All rights reserved.
//

#import <UIKit/UIKit.h>
#import"RestConnection.h"
#import"SOSBEACONAppDelegate.h"
#import"VideoViewController.h"

@interface NewLogin : UIViewController  <RestConnectionDelegate,UIAlertViewDelegate> {
	IBOutlet UITextField *text_Phone;
	IBOutlet UIButton *submit_button;
	IBOutlet UIButton *cancel_button;
	IBOutlet UIActivityIndicatorView *actSignup;
	
	
	NSInteger flagForRequest;
	NSString *strImei;
	NSString *strPhoneNumber;
	RestConnection *restConnection;
	SOSBEACONAppDelegate *appDelegate;
	NSInteger flagForAlert;
	NSString *token;
	NSInteger flag;
	NSInteger contactCount;
	NSTimer *countDownTimer;
	NSInteger countTime;
	VideoViewController *video;
	NSString *activMessage;
	NSInteger countActive;
	UIAlertView *alert1;
	BOOL isDissmiss;
}
@property (nonatomic, retain) UITextField *text_Phone;
@property (nonatomic, retain) UIButton *submit_button;
@property (nonatomic, retain) UIButton *cancel_button;
@property (nonatomic, retain)VideoViewController *video;
@property(nonatomic)NSInteger flag;
@property(nonatomic,retain)NSString *token;
@property(nonatomic)NSInteger flagForAlert;
@property(nonatomic)NSInteger flagForRequest;
@property(nonatomic,retain)NSString *strImei;
@property (nonatomic, retain) RestConnection *restConnection;
@property(nonatomic,retain)NSString *strPhoneNumber;

-(void)timerTick;
-(void)process;
-(IBAction)cancelButtonPress:(id)sender;
-(IBAction)submitButtonPress:(id)sender;
-(IBAction)backgroundTap:(id)sender;
-(void)getdata;
- (void)DimisAlertView1;
-(void)showLoading;
@end
