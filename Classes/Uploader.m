//
//  Uploader.m
//  SOSBEACON
//
//  Created by Tran Ngoc Anh on 9/14/10.
//  Copyright 2010 CNC. All rights reserved.
//

#import "Uploader.h"
#import "AudioUploader.h"
#import "PhotoUploader.h"
#import "CaptorView.h"
#import "StatusView.h"
#import "ValidateData.h"
#define CK_CheckingIn @"checkingIn"

@implementation Uploader
@synthesize delegate;
@synthesize isAudioUpOK,isPhotoUpOK,isSendAlert,autoUpload;
@synthesize uploadId,isAlert;

- (id) init
{
	self = [super init];
	if (self != nil) {
		isSendAlert = YES;
		
		photoUploader = [[PhotoUploader alloc] init];
		audioUploader = [[AudioUploader alloc] init];
		isAudioUpOK = NO;
		isPhotoUpOK = NO;
		
		appDelegate = (SOSBEACONAppDelegate*)[[UIApplication sharedApplication] delegate];
		
		restConnection = [[RestConnection alloc] initWithBaseURL:SERVER_URL];
		restConnection.delegate=self;
	}
	return self;
}

- (void) dealloc
{
	[photoUploader release];
	[audioUploader release];
	[super dealloc];
}

#pragma mark -
#pragma mark Other
- (void)uploadPhoto {
	NSLog(@"Uploader:uploadPhoto",nil);
	if(autoUpload) 
	{
		NSLog(@"Uploader:uploadPhoto:autoUpload:0",nil);
		[photoUploader uploadAll:self];
		NSLog(@"Uploader:uploadPhoto:autoUpload:1",nil);
	}
	else 
	{
		NSLog(@"Uploader:uploadPhoto:a!utoUpload",nil);
		isPhotoUpOK=YES;
		[self setTitle2:@"Photo process complete"];
		[photoUploader uploadAll:self];
		[self finishWait];
	}

}

- (void)uploadAudio {
	if(autoUpload) 
	{
		[audioUploader uploadAll:self];
	}
	else 
	{
		isAudioUpOK = YES;
		[self setTitle1:@"Audio record complete"];
		[audioUploader uploadAll:self];
		[self finishWait];
		
	}

}

- (void)finishUpload 
{
	NSLog(@"UPload status : %d - %d",isAudioUpOK,isPhotoUpOK);
	if(isAudioUpOK&&isPhotoUpOK)
	{
		isAudioUpOK = NO;
		isPhotoUpOK = NO;
		photoUploader.endUpload = NO;
		audioUploader.endUpload = NO;
		if (isAlert==FALSE) {
			[captorView dismissModalViewControllerAnimated:YES];
		}
		if(isSendAlert) 
		{
			//send alert
			[captorView dismissModalViewControllerAnimated:YES];
		}
		else {
			//send checkin
			
			if(delegate!=nil && [delegate respondsToSelector:@selector(uploadFinish)])
			{
				[delegate uploadFinish];
				NSLog(@"up load finish******-*-*-*-*-*-*-*---*");
			}
			
		}
		
		if (appDelegate.flagsentalert ==1) 
		{
			appDelegate.flagsentalert =10;
			UIAlertView *alert =[[UIAlertView alloc] initWithTitle:nil
												message:NSLocalizedString(@"SendAlertSuccess",@"")
												delegate:nil
												cancelButtonTitle:@"Ok" 
												otherButtonTitles:nil];
			[alert show];
			[alert release];
		}

	}
}

- (void)finishWait {
	if(isAudioUpOK&&isPhotoUpOK)
	{		isAudioUpOK = NO;
		isPhotoUpOK = NO;
		photoUploader.endUpload = NO;
		audioUploader.endUpload = NO;
		[captorView dismissModalViewControllerAnimated:YES];
	}
}

- (void)setCaptorView:(CaptorView*)object {
	captorView = object;
}

- (void)setTitle1:(NSString*)title {
	if(captorView) captorView.label1.text = title;
}

- (void)setTitle2:(NSString*)title {
	if(captorView) captorView.label2.text = title;
}

- (void)endUploadPhoto {
	photoUploader.endUpload = YES;
}
- (void)endUploadAudio {
	audioUploader.endUpload = YES;
}

- (void)removeAllFileCache {
	photoUploader.endUpload = NO; //add 15.10
	audioUploader.endUpload = NO; //add 15.10
	[audioUploader removeAllOldFile];
	[photoUploader removeAllOldFile];
}

#pragma mark -
#pragma mark Alert
//message
- (void)sendAlert {
	
	//
	/*
	if (appDelegate.longitudeString == NULL)
	{
		appDelegate.longitudeString = @"0";
	}
	if (appDelegate.latitudeString == NULL) 
	{
		appDelegate.latitudeString = @"0";
	}
	*/
	//
//[appDelegate.statusView showStatus:@"Sending alert to server..."];
	
	NSArray *key = [NSArray arrayWithObjects:@"phoneId",@"token",@"latitude",@"longtitude",@"type",nil];
	NSArray *obj = [NSArray arrayWithObjects:[NSString stringWithFormat:@"%d",appDelegate.phoneID],
					appDelegate.apiKey,appDelegate.latitudeString,appDelegate.longitudeString,@"0",nil];
	NSDictionary *param =[NSDictionary dictionaryWithObjects:obj forKeys:key];
	[restConnection postPath:[NSString stringWithFormat:@"/alert?format=json"] withOptions:param];
}
- (void)sendImOkAlert {
	
	//[appDelegate.statusView showStatus:@"Sending alert to server..."];
	flag =1;
	NSArray *key = [NSArray arrayWithObjects:@"phoneId",@"token",@"latitude",@"longtitude",@"type",@"message",nil];
	
	//NSLog(@"%d",a)
	/*
	 NSLog(@"phoneid: %d   toke :%@    latitude:%@      longtitude:%@   ",appDelegate.phoneID,appDelegate.apiKey,appDelegate.latitudeString,appDelegate.longitudeString);	
	 
	 if (appDelegate.longitudeString == NULL)
	 {
	 appDelegate.longitudeString = @"0";
	 }
	 if (appDelegate.latitudeString == NULL) 
	 {
	 appDelegate.latitudeString = @"0";
	 }
	*/ 
	////////////////////////////////
	
	
	NSArray *obj = [NSArray arrayWithObjects:[NSString stringWithFormat:@"%d",appDelegate.phoneID],
					appDelegate.apiKey,appDelegate.latitudeString,appDelegate.longitudeString,@"2",@"i'm ok",nil];
	NSDictionary *param =[NSDictionary dictionaryWithObjects:obj forKeys:key];
	[restConnection postPath:[NSString stringWithFormat:@"/alert?format=json"] withOptions:param];
}

#pragma mark -
#pragma mark RestConnectionDelegate
-(void)finishRequest:(NSDictionary *)arrayData andRestConnection:(id)connector{
	if ([[[arrayData objectForKey:@"response"] objectForKey:@"success"] isEqualToString:@"true"]) 
	{
		///
		if (flag == 1) 
		{
			
			flag =2;
			UIAlertView *alert =[[UIAlertView alloc] initWithTitle:@"Success" 
											message:NSLocalizedString(@"chekin_success",@"") 
											delegate:nil
											cancelButtonTitle:@"Ok"
											otherButtonTitles:nil];
			[alert show];
			[alert release];
		}
		///
		[appDelegate.statusView setStatusTitle:@"Alert has been sent successfully"];
		uploadId = [[[arrayData objectForKey:@"response"] objectForKey:@"id"] intValue];
		if (delegate!=nil && [delegate respondsToSelector:@selector(requestUploadIdFinish:)]) {
			[delegate requestUploadIdFinish:uploadId];
		}
	}
	else {	
		[appDelegate.statusView setStatusTitle:@"Sending alert fail..."];
		if (delegate!=nil && [delegate respondsToSelector:@selector(requestUploadIdFinish:)]) {
			[delegate requestUploadIdFinish:-1];
		}
	}
	NSLog(@"----------->>>>>>>>>>");
	[appDelegate.statusView performSelector:@selector(hideStatus) withObject:nil afterDelay:0.7];
	//[appDelegate.statusView hideStatus];
	
}

- (void)cantConnection:(NSError*)error andRestConnection:(id)connector {
	[appDelegate.statusView setStatusTitle:@"Sending alert fail..."];
	[appDelegate.statusView beginHideStatus];
	isAudioUpOK = NO;
	isPhotoUpOK = NO;
	isSendAlert = NO;
	if(captorView!=nil) [captorView dismissModalViewControllerAnimated:YES];
}

- (void)setAutoUpload:(BOOL)value {
	autoUpload=value;
	
}


@end
