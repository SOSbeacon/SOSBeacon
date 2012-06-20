//
//  CaptorView.h
//  SOSBEACON
//
//  Created by Tran Ngoc Anh on 10/06/2010.
//  Copyright 2010 CNC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AudioToolbox/AudioToolbox.h>
#import "RestConnection.h"
#import "SOSBEACONAppDelegate.h"
#import <CoreLocation/CoreLocation.h>
//#import "CameraToolBar.h"
#define ST_ImageRecordFrequency @"imageRecordFrequency"

@class AudioRecorder;
@class CameraView;

@interface CaptorView : UIViewController <UINavigationControllerDelegate,UIImagePickerControllerDelegate> {
	SOSBEACONAppDelegate *appDelegate;
	AudioRecorder *audioRecorder;
	
	CameraView *picker;
	NSInteger captureSetting;
	
	NSInteger capCount;
	NSInteger currentBlock;
	
	NSInteger block;
	
	UILabel *label1;
	UILabel *label2;
	UILabel *label3;
	IBOutlet UIToolbar *toolBarForCamera;
	BOOL isCheckIn;
	
	
	NSInteger countDown;
	NSTimer *mainTImer;
	
	NSOperationQueue *mainOpQueue;
	IBOutlet UILabel *countLabel;
	UIBarButtonItem *capturePhotoButton;
	//IBOutlet UIButton *doneButton;
	 UIButton *captureButton;
	BOOL isauto;
	//UIBarButtonItem *stop;
	IBOutlet UILabel *uploading;

	
}
//@property(nonatomic, retain) IBOutlet UIBarButtonItem *stop;
@property(nonatomic)  BOOL isauto;
@property(nonatomic, retain) IBOutlet UIButton *captureButton;
@property(nonatomic ,retain) IBOutlet UIBarButtonItem *capturePhotoButton;
@property(nonatomic,retain)UILabel *countLabel;
@property(nonatomic,retain) IBOutlet UIActivityIndicatorView *busy;
@property(nonatomic,retain) IBOutlet UILabel *lnlTopMessage;
@property (nonatomic,retain) IBOutlet UIView *vwTop;
@property (nonatomic, retain) UIImagePickerController *picker;
@property (nonatomic,retain) IBOutlet AudioRecorder *audioRecorder;
@property (nonatomic,retain) IBOutlet UILabel *label1;
@property (nonatomic,retain) IBOutlet UILabel *label2;
@property (nonatomic,retain) IBOutlet UILabel *label3;
@property (nonatomic,retain) IBOutlet UIView *vwToolbarHolder;
@property (nonatomic,assign)BOOL isCheckIn;
- (IBAction)takePicture;
- (IBAction)back;
- (IBAction)stopCaptor:(UIBarButtonItem*)sender;
- (IBAction)stopCaptorOnCamera:(id)sender;
- (IBAction)btnCameraTapped:(id)sender ;
- (void)newCaptor;
- (void)newCheckin;
-(void)updateCountDown;
@end