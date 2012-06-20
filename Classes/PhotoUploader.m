//
//  PhotoUploader.m
//  SOSBEACON
//
//  Created by Tran Ngoc Anh on 9/10/10.
//  Copyright 2010 CNC. All rights reserved.
//

#import "PhotoUploader.h"
#import "Uploader.h"
#import "ValidateData.h"

@implementation PhotoUploader
@synthesize endUpload;

- (id) init
{
	self = [super init];
	if (self != nil) {
		if(![[NSFileManager defaultManager] fileExistsAtPath:PHOTO_FOLDER])
		{
			[[NSFileManager defaultManager] createDirectoryAtPath:PHOTO_FOLDER withIntermediateDirectories:NO attributes:nil error:nil];
		}
		appDelegate = (SOSBEACONAppDelegate*)[[UIApplication sharedApplication] delegate];
		restConnection = [[RestConnection alloc] initWithBaseURL:SERVER_URL];
		restConnection.delegate=self;
		upNext = NO;
		endUpload = NO;
	}
	return self;
}

- (void) dealloc
{
	if(array!=nil) [array release];
	[super dealloc];
}

- (void)uploadAll:(Uploader*)sender {
	NSLog(@"PhotoUploader:uploadAll:0",nil);
	if(array!=nil)
	{
		if([array count]>0)
		{
			upNext = YES;
			return;
		}
	}

	uploader=sender;
	NSArray *fileList = [[NSFileManager defaultManager] contentsOfDirectoryAtPath:PHOTO_FOLDER error:nil];
	array = [[NSMutableArray alloc] init];
	
	for (NSString *str in fileList) {
		NSRange foundRange=[[str lowercaseString] rangeOfString:@".jpg"];
		if(foundRange.location != NSNotFound) {
			[array addObject:str];
		}
	}
	NSLog(@"PhotoUploader:uploadAll:1",nil);
	[self uploadPhoto];
}

- (void)uploadPhoto {
	NSLog(@"PhotoUploader:uploadAll:0",nil);
	if([array count]>0)
	{
		[uploader setTitle2:[NSString stringWithFormat:@"Uploading photo : %@",[array objectAtIndex:0]]];
		NSArray *key;
		NSArray *obj;
		if (appDelegate.uploader.isAlert==TRUE) {
		
			key = [NSArray arrayWithObjects:@"phoneId",@"alertId",@"token",@"type",nil];
			obj = [NSArray arrayWithObjects:[NSString stringWithFormat:@"%d",appDelegate.phoneID],[NSString stringWithFormat:@"%d",uploader.uploadId],appDelegate.apiKey,@"0",nil];
			
		}
		else {
			key = [NSArray arrayWithObjects:@"phoneId",@"alertlogType",@"token",@"type",nil];
			obj = [NSArray arrayWithObjects:[NSString stringWithFormat:@"%d",appDelegate.phoneID],@"2",appDelegate.apiKey,@"0",nil];
			
		}

		NSDictionary *params = [NSDictionary dictionaryWithObjects:obj forKeys:key];
		
		NSData *theData = [NSData dataWithContentsOfFile:[PHOTO_FOLDER stringByAppendingPathComponent:[array objectAtIndex:0]]];
		
		[restConnection uploadPath:@"/data?format=json" withOptions:params withFileData:theData];	
		NSLog(@"PhotoUploader:uploadAll:2",nil);
	}
	else
	{
		[uploader setTitle2:@"Upload photos done!"];
		[array release];
		array = nil;
		
		if(upNext)
		{
			upNext = NO;
			[self uploadAll:uploader];
		}
		else
		{
			//send alert here
			if(endUpload)
			{
				endUpload=NO;
				uploader.isPhotoUpOK = YES;
			}
			[uploader finishUpload];
		}
	}
}

- (void)removeAllOldFile {
	NSArray *fileList = [[NSFileManager defaultManager] contentsOfDirectoryAtPath:PHOTO_FOLDER error:nil];
	for (NSString *str in fileList) {
		NSRange foundRange=[[str lowercaseString] rangeOfString:@".jpg"];
		if(foundRange.location != NSNotFound) {
			[[NSFileManager defaultManager] removeItemAtPath:[PHOTO_FOLDER stringByAppendingPathComponent:str] error:nil];
		}
	}
}

#pragma mark -
#pragma mark finishRequest
- (void)finishRequest:(NSDictionary *)arrayData andRestConnection:(id)connector {
	if([[[arrayData objectForKey:@"response"] objectForKey:@"success"] isEqualToString:@"true"])
	{
		NSLog(@"upload photo ok : %@",[array objectAtIndex:0]);
		[[NSFileManager defaultManager] removeItemAtPath:[PHOTO_FOLDER stringByAppendingPathComponent:[array objectAtIndex:0]] error:nil];
		[array removeObjectAtIndex:0];
		[self uploadPhoto];
	}
	else
	{
		
		
		[uploader setTitle2:[NSString stringWithFormat:@"Upload fails photo : %@",[array objectAtIndex:0]]];
		[array removeObjectAtIndex:0];
		[self uploadPhoto];
	}
}

- (void) DimisAlertView:(UIAlertView*)alertView {
	[alertView dismissWithClickedButtonIndex:0 animated:TRUE];
}

- (void)cantConnection:(NSError*)error andRestConnection:(id)connector {
	
	alertView();
	
	if(endUpload)
	{
		endUpload=NO;
		uploader.isPhotoUpOK = YES;
	}
	[uploader finishUpload];
	
}

@end
