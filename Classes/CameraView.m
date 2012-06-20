//
//  CameraView.m
//  SOSBEACON
//
//  Created by Tran Ngoc Anh on 9/9/10.
//  Copyright 2010 CNC. All rights reserved.
//

#define kTimeWaitForCap 7

#import "SOSBEACONAppDelegate.h"
#import "CameraView.h"
#import "CaptorView.h"

@implementation CameraView
@synthesize capCount;
@synthesize captorView;
@synthesize autoMode;
@synthesize countTime;
@synthesize count;
- (id) init
{
	countTime = 30;
	capCount = 0;
	self = [super init];
	if (self != nil) {
		if([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera])
		{
			self.sourceType = UIImagePickerControllerSourceTypeCamera;
			
		}
		else self.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
		
		self.showsCameraControls = NO;
		//self.navigationBarHidden = YES;
		//self.toolbarHidden = YES;
		
		autoMode=YES;
		appDelegate = (SOSBEACONAppDelegate*)[[UIApplication sharedApplication] delegate];

	}
	return self;
}

- (void)stopCapture {
	capCount = -1;
	if (countDownTimer!=nil)
		[countDownTimer invalidate];
}

- (void)startCapture {
	captorView.label3.text = @"Photo : 0";
	capCount = 0;
	countTime = 30;
	countDown=10;
	[self updateCountDown];
	//if (autoMode)
	//	[self performSelector:@selector(beginCapturePhoto) withObject:nil afterDelay:kTimeWaitForCap];
	countDownTimer=[NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(timerTick) userInfo:nil repeats:YES];
}

- (void)beginCapturePhoto {
	if(capCount==-1) return;
	
	[appDelegate playSound3];
	[self performSelector:@selector(capturePhoto:) withObject:nil afterDelay:3];
}

- (void)capturePhoto 
{
	
	if(capCount==-1) return;
	
	[self takePicture];
	//capCount++;
	//[captorView.capturePhotoButton setEnabled:NO];
	captorView.captureButton.hidden = YES;
	//[captorView.capturePhotoButton se]
}

-(void)timerTick{
	
	countTime--;
	countDown--;
	if (((countTime  == 20)||(countTime == 10)) && captorView.isauto) 
	{
		[self capturePhoto];
	}
	if (((countTime == 13) || (countTime == 23)) && captorView.isauto) 
	{
		countDown = 3;
		[appDelegate playSound3];
		
	}
	if (countDown >0 && countDown <=3 && captorView.isauto) 
	{
		captorView.busy.hidden = NO;
		[captorView.busy startAnimating];
		captorView.lnlTopMessage.textColor =[ UIColor redColor];
		captorView.lnlTopMessage.text=[NSString stringWithFormat:@"Next Photo in %d secs",countDown];
		//[captorView.capturePhotoButton setEnabled:NO];
		captorView.captureButton.hidden = YES;
	}
	else
	{
		[captorView.busy stopAnimating];
		captorView.busy.hidden = YES;
		captorView.lnlTopMessage.text=[NSString stringWithFormat:@" "];
	}
	
	if (countTime == 0) 
	{
		[countDownTimer invalidate];
		[captorView stopCaptorOnCamera:nil];	
	}	
	captorView.label3.text = [NSString stringWithFormat:@"Photo : %d",capCount];
	captorView.countLabel.text = [NSString stringWithFormat:@"%d",countTime];
	//NSLog(@"timerTick",nil);
	
}
-(void)updateCountDown{
	//captorView.lnlTopMessage.text=[NSString stringWithFormat:@"Next Photo in %i secs",countDown];
}
- (void)dealloc {
	NSLog(@"DEALLOC Camera View");
	[countDownTimer release];
    [super dealloc];
}

- (void)viewDidDisappear:(BOOL)animated {
	[super viewDidDisappear:animated];
}

-(void) viewDidAppear: (BOOL)animated {
	[super viewDidAppear:animated];	
	[self startCapture];
}

@end
