//
//  CaptorView.m
//  SOSBEACON
//
//  Created by Tran Ngoc Anh on 10/06/2010.
//  Copyright 2010 CNC. All rights reserved.
//

#import "CaptorView.h"
#import "AudioRecorder.h"
#import "SOSBEACONAppDelegate.h"
#import "RestConnection.h"
#import "CameraView.h"
#import "AppSetting.h"
#import "Uploader.h"
#import <QuartzCore/QuartzCore.h>
#import <math.h>
#import "SaveImageOperation.h"
static inline double radians (double degrees) {return degrees * M_PI/180;}


@implementation CaptorView
@synthesize countLabel;
@synthesize capturePhotoButton;
@synthesize audioRecorder;
@synthesize picker;
@synthesize label1,label2,label3,isCheckIn;
@synthesize vwToolbarHolder;
@synthesize busy;
@synthesize lnlTopMessage;
@synthesize vwTop;
@synthesize isauto;
@synthesize captureButton;
- (id)init {
	if(self=[super init])
	{
	}
	return self;
}

- (void)viewDidLoad {
	self.lnlTopMessage.text = @"";
	[busy stopAnimating];
	busy.hidden = YES;
	//stop.enabled = NO;
	 [super viewDidLoad];
	uploading.hidden = YES;
	isauto = YES;
	mainOpQueue=[[NSOperationQueue alloc] init];
	[mainOpQueue setMaxConcurrentOperationCount:1];
	appDelegate = (SOSBEACONAppDelegate*)[[UIApplication sharedApplication] delegate];
	captureSetting = [[appDelegate.settingArray objectForKey:ST_ImageRecordFrequency] intValue];
	[audioRecorder initAudio];
	 audioRecorder.hidden=NO;
	
	capCount = 0;
	//	[self updateCountDown];
	//countDownTimer=[NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(timerTick) userInfo:nil repeats:YES];
	
	
	[appDelegate.uploader setCaptorView:self];

	[self newCaptor];
	//capturePhotoButton.hiden =YES;
	//Audio record process...
	//Photo capture process...
	//[label1 se]
	
	label1.text = @"Start Audio Recording Now ";
	label2.text = @"Narrate the Screen or Record Your Message";
	label1.textAlignment = UITextAlignmentCenter;
	label2.textAlignment = UITextAlignmentCenter;
	 
	
	
}	

- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    NSLog(@"@@@@@@@@@@@@@@ memory error capture!!!");
    // Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)dealloc {
	NSLog(@"DEALLOC CapterView");
	[self.vwTop release];
	[self.busy release];
	//[stop release];
	//[self.lnlTopMessage release];
	//[label1 performSelector:@selector(release) withObject:nil afterDelay:1.0];
	//[label2 performSelector:@selector(release) withObject:nil afterDelay:1.0];
	//[label3 performSelector:@selector(release) withObject:nil afterDelay:1.0];

//	[label2 release];
//	[label3 release];
	[vwToolbarHolder release];
	//[picker release];
	[audioRecorder removeFromSuperview];
	[audioRecorder release];
	//[countDownTimer release];
	[super dealloc];
}

#pragma mark Action
-(IBAction)takePicture {
	if(capCount==-1) return;
	
	//return;

	picker = [[CameraView alloc] init] ;
	picker.delegate = self;
	picker.captorView=self;
	
	
	picker.cameraOverlayView =vwToolbarHolder;
	
	[self presentModalViewController:picker animated:NO];

	NSLog(@"takePicture",nil);
}

- (IBAction) back{

	[self dismissModalViewControllerAnimated:YES];
}
- (UIImage *)scaleAndRotateImage:(UIImage *)image
{
	int kMaxResolution = 20000; // Or whatever
	
	CGImageRef imgRef = image.CGImage;
	
	CGFloat width = CGImageGetWidth(imgRef);
	CGFloat height = CGImageGetHeight(imgRef);
	
	CGAffineTransform transform = CGAffineTransformIdentity;
	CGRect bounds = CGRectMake(0, 0, width, height);
	if (width > kMaxResolution || height > kMaxResolution) {
		CGFloat ratio = width/height;
		if (ratio > 1) {
			bounds.size.width = kMaxResolution;
			bounds.size.height = bounds.size.width / ratio;
		}
		else {
			bounds.size.height = kMaxResolution;
			bounds.size.width = bounds.size.height * ratio;
		}
	}
	
	CGFloat scaleRatio = bounds.size.width / width;
	CGSize imageSize = CGSizeMake(CGImageGetWidth(imgRef), CGImageGetHeight(imgRef));
	CGFloat boundHeight;
	UIImageOrientation orient = image.imageOrientation;
	switch(orient) {
			
		case UIImageOrientationUp: //EXIF = 1
			transform = CGAffineTransformIdentity;
			break;
			
		case UIImageOrientationUpMirrored: //EXIF = 2
			transform = CGAffineTransformMakeTranslation(imageSize.width, 0.0);
			transform = CGAffineTransformScale(transform, -1.0, 1.0);
			break;
			
		case UIImageOrientationDown: //EXIF = 3
			transform = CGAffineTransformMakeTranslation(imageSize.width, imageSize.height);
			transform = CGAffineTransformRotate(transform, M_PI);
			break;
			
		case UIImageOrientationDownMirrored: //EXIF = 4
			transform = CGAffineTransformMakeTranslation(0.0, imageSize.height);
			transform = CGAffineTransformScale(transform, 1.0, -1.0);
			break;
			
		case UIImageOrientationLeftMirrored: //EXIF = 5
			boundHeight = bounds.size.height;
			bounds.size.height = bounds.size.width;
			bounds.size.width = boundHeight;
			transform = CGAffineTransformMakeTranslation(imageSize.height, imageSize.width);
			transform = CGAffineTransformScale(transform, -1.0, 1.0);
			transform = CGAffineTransformRotate(transform, 3.0 * M_PI / 2.0);
			break;
			
		case UIImageOrientationLeft: //EXIF = 6
			boundHeight = bounds.size.height;
			bounds.size.height = bounds.size.width;
			bounds.size.width = boundHeight;
			transform = CGAffineTransformMakeTranslation(0.0, imageSize.width);
			transform = CGAffineTransformRotate(transform, 3.0 * M_PI / 2.0);
			break;
			
		case UIImageOrientationRightMirrored: //EXIF = 7
			boundHeight = bounds.size.height;
			bounds.size.height = bounds.size.width;
			bounds.size.width = boundHeight;
			transform = CGAffineTransformMakeScale(-1.0, 1.0);
			transform = CGAffineTransformRotate(transform, M_PI / 2.0);
			break;
			
		case UIImageOrientationRight: //EXIF = 8
			boundHeight = bounds.size.height;
			bounds.size.height = bounds.size.width;
			bounds.size.width = boundHeight;
			transform = CGAffineTransformMakeTranslation(imageSize.height, 0.0);
			transform = CGAffineTransformRotate(transform, M_PI / 2.0);
			break;
			
		default:
			[NSException raise:NSInternalInconsistencyException format:@"Invalid image orientation"];
			
	}
	UIGraphicsBeginImageContext(bounds.size);
	
	CGContextRef context = UIGraphicsGetCurrentContext();
	
	if (orient == UIImageOrientationRight || orient == UIImageOrientationLeft) {
		CGContextScaleCTM(context, -scaleRatio, scaleRatio);
		CGContextTranslateCTM(context, -height, 0);
	}
	else {
		CGContextScaleCTM(context, scaleRatio, -scaleRatio);
		CGContextTranslateCTM(context, 0, -height);
	}
	
	CGContextConcatCTM(context, transform);
	
	CGContextDrawImage(UIGraphicsGetCurrentContext(), CGRectMake(0, 0, width, height), imgRef);
	UIImage *imageCopy = UIGraphicsGetImageFromCurrentImageContext();
	UIGraphicsEndImageContext();
	
	return imageCopy;
}
#pragma mark UIImagePickerControllerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker1 didFinishPickingImage:(UIImage *)inImage
				  editingInfo:(NSDictionary *)editingInfo {
	
	NSLog(@"imagePickerController:didFinishPickingImage:0",nil);
	capCount++;
	
	NSDateFormatter *formatter;
	NSString        *dateString;
	
	formatter = [[NSDateFormatter alloc] init];
	[formatter setDateFormat:@"HH:mm:ss"];
	dateString = [formatter stringFromDate:[NSDate date]];
	[formatter release];
	
	SaveImageOperation *op=[[SaveImageOperation alloc] initWithImage:inImage];
	op.mainDelegate=self;
	op.fileToWriteTo=[PHOTO_FOLDER stringByAppendingFormat:@"/A%d_%d_%@.jpg",currentBlock,capCount,dateString];
	[mainOpQueue addOperation:op];
	[op release];
	
/////***
//////////
	captureButton.hidden = NO;
//	[capturePhotoButton setEnabled:YES];
	picker.capCount ++;

}
-(void)finishedSavingImage{
	NSLog(@"CaptorView:finishedSavingImage",nil);
	/*
	if(picker.countTime==0)
	{
		NSLog(@"CaptorView:finishedSavingImage:capCount==2",nil);
		currentBlock++;
		capCount=0;
		[picker dismissModalViewControllerAnimated:NO];
		
		
		if(currentBlock<=block)
		{
			[self performSelector:@selector(takePicture) withObject:nil afterDelay:3];
		}
		else
		{
			//cho phep send alert;
			[appDelegate.uploader endUploadPhoto];
			if(!appDelegate.uploader.autoUpload) [appDelegate.uploader uploadPhoto];
		}
		
		//upload photo here
		if(appDelegate.uploader.autoUpload){
			NSLog(@"CaptorView:finishedSavingImage:uploadPhoto",nil);
			[appDelegate.uploader uploadPhoto];
		}
		
	}
	 */
	 
}
- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker1 {
	[picker dismissModalViewControllerAnimated:NO];
}

#pragma mark -
#pragma mark Other
- (IBAction)stopCaptor:(UIBarButtonItem*)sender {
	if(sender) sender.enabled = NO;
	capCount=-1;
	audioRecorder.block = -1; //stop next bock
	[audioRecorder closeAndStop];
	[self dismissModalViewControllerAnimated:YES];
	[appDelegate.uploader endUploadPhoto];
	NSLog(@"1");
	[appDelegate.uploader endUploadAudio];
	NSLog(@"2");

	[appDelegate.uploader uploadPhoto];
	NSLog(@"3");

	[appDelegate.uploader uploadAudio];
	NSLog(@"4");

	
	
}

- (IBAction)stopCaptorOnCamera:(id)sender {
	
	//stop.enabled = YES;
	label1.text = @"Audio record process....";
	label2.text = @"Photo capture process...";
	uploading.hidden = NO;

	label1.textAlignment = UITextAlignmentCenter;
	label2.textAlignment = UITextAlignmentCenter;

//	if(sender) sender.enabled = NO;	
	picker.count = picker.capCount;
	NSLog(@" count = %d",picker.count);
	capCount=-1;
	[picker stopCapture];
	audioRecorder.block = -1; //stop next block
	[audioRecorder closeAndStop];
	[picker dismissModalViewControllerAnimated:NO];
	
	[appDelegate.uploader endUploadPhoto];
	[appDelegate.uploader endUploadAudio];
	///// sua hom 17/08/2011
	//[appDelegate.uploader uploadPhoto];
	[appDelegate.uploader uploadAudio];
	if (picker.count == 0)
	{
		[appDelegate.uploader uploadPhoto];
		//NSLog(@" 0");
	}
	else
	if (picker.count == 1)
	{
		[appDelegate.uploader performSelector:@selector(uploadPhoto) withObject:nil afterDelay:5.0];
		

	}
	else
	if (picker.count <= 2)
	{
		[appDelegate.uploader performSelector:@selector(uploadPhoto) withObject:nil afterDelay:9.0];
		

	}
	else
	if (picker.count <= 5 )
	{
		[appDelegate.uploader performSelector:@selector(uploadPhoto) withObject:nil afterDelay:15.0];
		
	}
	else
		if(picker.count <= 7)
		{
			[appDelegate.uploader performSelector:@selector(uploadPhoto) withObject:nil afterDelay:22.0];
					}
	else
			if(picker.count <= 10)
			{
				[appDelegate.uploader performSelector:@selector(uploadPhoto) withObject:nil afterDelay:35.0];
				
			}
	else
	{
		[appDelegate.uploader performSelector:@selector(uploadPhoto) withObject:nil afterDelay:60.0];

	}
	//[self performSelector:@selector(uploadPhoto) withObject:appDelegate.uploader afterDelay:5.0];
	/////////*********
//	[appDelegate.uploader uploadAudio];
	
	
	
	
	
}

- (IBAction)btnCameraTapped:(id)sender {
	isauto = NO;
	captureButton.hidden = YES;
	[picker capturePhoto];
	
	
	
	
}

- (void)newCheckin {
	capCount=0;
	currentBlock=1;
	block = [[appDelegate.settingArray objectForKey:ST_VoiceRecordDuration] intValue];
	audioRecorder.block=block;
	[audioRecorder performSelector:@selector(startRecord) withObject:nil afterDelay:1];
	[self performSelector:@selector(takePicture) withObject:nil afterDelay:3];
}

- (void)newCaptor {
	//remove all capture
	[appDelegate.uploader removeAllFileCache];
		
	capCount=0;
	currentBlock=1;
	block = [[appDelegate.settingArray objectForKey:ST_VoiceRecordDuration] intValue];
	audioRecorder.block=block;
	[audioRecorder performSelector:@selector(startRecord) withObject:nil afterDelay:1];
	[self performSelector:@selector(takePicture) withObject:nil afterDelay:3];
	countDown=3;

}
-(void)timerTick{
	countDown--;
	[self updateCountDown];
//	NSLog(@"timerTick",nil);
}
-(void)updateCountDown{
	lnlTopMessage.text=[NSString stringWithFormat:@"Next Photp in %i secs",countDown];
}



@end
