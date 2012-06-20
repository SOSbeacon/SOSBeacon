//
//  AppSetting.h
//  SOSBEACON
//
//  Created by cncsoft on 7/12/10.
//  Copyright 2010 CNC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RestConnection.h"
#import "TableGroup.h"
#import "Personal.h"
#import "SOSBEACONAppDelegate.h"

#define ST_ImageRecordFrequency @"imageRecordFrequency"
#define ST_VoiceRecordDuration @"voiceRecordDuration"
#define ST_LocationReporting @"locationReporting"
#define ST_EmergencySetting @"emergencySetting"
#define ST_SendToAlert @"sendToAlert"
#define ST_SamaritanRange @"samaritanRange"
#define ST_IncomingGovernment @"incomingGovernment"
#define ST_SamaritanStatus @"samaritanStatus"
#define ST_ReceiverSamaritan @"receiverSamaritan"
#define ST_ReciveRange @"receiveRangeSamaritan"

@interface AppSetting : UIViewController <UITextFieldDelegate,UIAlertViewDelegate,UIActionSheetDelegate,RestConnectionDelegate>{

	
	SOSBEACONAppDelegate *appDelegate;
	RestConnection *rest;	
	TableGroup *tb;
	Personal *personal;
	
	UISwitch *samaritanStatus;
	UISwitch *incomingGovernment;	
	UISwitch *receiverSamaritan;
	
	UITextField *txtPanicPhone;
	UIScrollView *scrollView;
	UILabel *imageDuration;
	UILabel *voiceRecord;
	UILabel *locationReporting;
	UILabel *lblSendToAlert;
	UILabel *lblSamaritanRange;
	UILabel *receiveRange;
	UIButton *btnFamily;
	
	UIActivityIndicatorView *actSetting;
	
	UITextField *txtuserName;
	UITextField *txtEmail;
	UITextField *txtPassWord;
	UITextField *txtPhoneNumber;
	UIBarButtonItem *btnSave;
	UIButton *btnRangeSamaritan;
	UIButton *receiveRangeStatus;
	UIButton *btnResetSetting;
	
	NSInteger editIndex;
	NSInteger getPhone;
		
	BOOL save;
	BOOL isEdit;
	NSInteger alertIndex;
	NSInteger loadIndex;
	BOOL sendingSamarisan;
	NSString *selectAlert;
	NSString *selectSamaritanRange;
	NSString *selectReciveRange;
	
	BOOL allowSendSamaritan;


}
@property (nonatomic) BOOL isEdit;
@property (nonatomic,retain) RestConnection *rest;
@property (nonatomic, retain) IBOutlet UIBarButtonItem *btnSave;

@property (nonatomic, retain) IBOutlet UISwitch *samaritanStatus;
@property (nonatomic, retain) IBOutlet UISwitch *incomingGovernment;
@property (nonatomic, retain) IBOutlet UISwitch *receiverSamaritan;
@property (nonatomic, retain) IBOutlet UIButton *receiveRangeStatus;
@property (nonatomic, retain) IBOutlet UIButton *btnFamily;
@property (nonatomic,retain) IBOutlet UITextField *txtPanicPhone;
@property (nonatomic,retain) IBOutlet UITextField *txtPassWord;
@property (nonatomic,retain) IBOutlet UITextField *txtPhoneNumber;
@property (nonatomic,retain) IBOutlet UIButton *btnRangeSamaritan;
@property (nonatomic,retain) IBOutlet UIButton *btnResetSetting;

@property (nonatomic,retain) IBOutlet UIScrollView *scrollView;

@property (nonatomic,retain) IBOutlet UILabel *locationReporting;
@property (nonatomic,retain) IBOutlet UILabel *voiceRecord;
@property (nonatomic,retain) IBOutlet UILabel *imageDuration;
@property (nonatomic,retain) IBOutlet UILabel *lblSendToAlert;
@property (nonatomic,retain) IBOutlet  UILabel *lblSamaritanRange;
@property (nonatomic, retain) IBOutlet UILabel *receiveRange;

@property (nonatomic,retain) IBOutlet UIActivityIndicatorView *actSetting;
@property (nonatomic,retain) IBOutlet UITextField *txtEmail;
@property (nonatomic,retain) IBOutlet UITextField *txtuserName;

@property (nonatomic) NSInteger editIndex;
@property (nonatomic) NSInteger loadIndex;

- (IBAction)choicesRecordingDuration;
- (IBAction)choicesImageRecord;
- (IBAction)locationReport;
- (IBAction)sendToAlert;
- (IBAction)rangeSatus;
- (IBAction)ReceiveRangeSamaritan;

- (IBAction)textFieldDoneEditing:(id)sender;
- (IBAction)backgroundTap:(id)sender;

- (IBAction)BackInfor;
- (IBAction)SaveSetting;
- (IBAction)ResetSetting;

- (IBAction)ReceiverSamaritan:(id)sender;
- (IBAction)SamaritanSatus:(id)sender;
- (IBAction)IncomingGovernment:(id)sender;

- (IBAction) LoadWebView;
- (void)sendSamaristanAuto;
- (void)delayAlert;
- (void) loadData; 

@end
 
