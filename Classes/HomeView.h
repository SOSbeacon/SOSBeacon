//
//  HomeView.h
//  SOSBEACON
//
//  Created by cncsoft on 7/30/10.
//  Copyright 2010 CNC. All rights reserved.
//	

#import <UIKit/UIKit.h>
#import "RestConnection.h"
#import "SlideToCancelViewController.h"
#import "SOSBEACONAppDelegate.h"
#import "Uploader.h"

typedef enum {ActionType_None=0,ActionType_OK=1,ActionType_Help=2}ActionType;
@interface HomeView : UIViewController<SlideToCancelDelegate,UINavigationControllerDelegate,RestConnectionDelegate,UploaderDelegate,UIActionSheetDelegate> {
	RestConnection *rest;
	SOSBEACONAppDelegate *appDelegate;	
	
	SlideToCancelViewController *slideToCancel;
	SlideToCancelViewController *slideToCancel2;
	SlideToCancelViewController *slideToCancel3;
	
	IBOutlet UIActivityIndicatorView  *actAlert;
	IBOutlet UILabel *lblSendAlert;
	
	IBOutlet UILabel *lblVersion;
		
	NSInteger loadIndex;
	
	ActionType currentAction;
	BOOL isSendOK;
	NSInteger flag;
	NSInteger flagforAlert;
	NSInteger newflag1;
	NSTimer *countDownTimer;
	IBOutlet UIActivityIndicatorView  *senAlertaction;
	IBOutlet UIActivityIndicatorView  *imOkaction;
	IBOutlet UILabel *checkin;
	IBOutlet UILabel *startingAlert;
	IBOutlet UILabel *Imokversion;
	IBOutlet UILabel *nedhelpversion;

	
}

@property (nonatomic, retain) IBOutlet UIView *vwImOkPopUp;
@property (nonatomic, retain) IBOutlet UIView *vwINeedHelpPopUp;

@property (nonatomic,retain) IBOutlet UILabel *lblVersion;
@property (nonatomic, retain) RestConnection *rest;
- (void)callPanic;
- (void)requestUploadIdFinish:(NSInteger)uploadId;
-(IBAction)showImNeedHelpMenu:(id)sender;
-(IBAction)showImOKMenu:(id)sender;

-(IBAction)btnCheckIn_Tapped:(id)sender;
-(IBAction)btnCheckInGroup_Tapped:(id)sender;
-(IBAction)btnSendAlert_Tapped:(id)sender;
-(IBAction)btnEmergencyPhone_Tapped:(id)sender;
-(IBAction)btnCancelImOk_Tapped:(id)sender;
-(IBAction)btnCancelNeedHelp_Tapped:(id)sender;
-(void)showUIView:(UIView*)theView;
-(void)dismissUIView:(UIView*)theView;
-(void)doCheckIn;
-(void)doEmercgenyCall;
-(void)doAlert;
-(void)doImOkCheckIn;
-(void)showvideo;
-(void)timerTick;

@end
