//
//  AudioUploader.m
//  SOSBEACON
//
//  Created by Tran Ngoc Anh on 9/14/10.
//  Copyright 2010 CNC. All rights reserved.
//

#import "AudioUploader.h"
#import "Uploader.h"

@implementation AudioUploader
@synthesize endUpload;

- (id)init
{
	self = [super init];
	if (self != nil) {
		if(![[NSFileManager defaultManager] fileExistsAtPath:AUDIO_FOLDER])
		{
			[[NSFileManager defaultManager] createDirectoryAtPath:AUDIO_FOLDER withIntermediateDirectories:NO attributes:nil error:nil];
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
	if(array!=nil) 
	{
		if([array count]>0)
		{
			upNext = YES;
			return;
		}
	}
	
	uploader=sender;
	NSArray *fileList = [[NSFileManager defaultManager] contentsOfDirectoryAtPath:AUDIO_FOLDER error:nil];
	array = [[NSMutableArray alloc] init  ];
	
	for (NSString *str in fileList) {
		NSRange foundRange=[[str lowercaseString] rangeOfString:@".caf"];
		if(foundRange.location != NSNotFound) {
			NSLog(@"file : %@",str);
			[array addObject:str];
		}
	}
	
	[self uploadAudio];
}

- (void)uploadAudio {
	if([array count]>0)
	{
		[uploader setTitle1:[NSString stringWithFormat:@"Uploading audio : %@",[array objectAtIndex:0]]];
		NSArray *key;
		NSArray *obj;
		if (appDelegate.uploader.isAlert==TRUE) {
			
			key = [NSArray arrayWithObjects:@"phoneId",@"alertId",@"token",@"type",nil];
			obj = [NSArray arrayWithObjects:[NSString stringWithFormat:@"%d",appDelegate.phoneID],[NSString stringWithFormat:@"%d",uploader.uploadId],appDelegate.apiKey,@"1",nil];
			
		}
		else {
			key = [NSArray arrayWithObjects:@"phoneId",@"alertlogType",@"token",@"type",nil];
			obj = [NSArray arrayWithObjects:[NSString stringWithFormat:@"%d",appDelegate.phoneID],@"2",appDelegate.apiKey,@"1",nil];
			
		}
		
		
		NSDictionary *params = [NSDictionary dictionaryWithObjects:obj forKeys:key];
		NSData *theData = [NSData dataWithContentsOfFile:[AUDIO_FOLDER stringByAppendingPathComponent:[array objectAtIndex:0]]];
		
		[restConnection uploadPath:@"/data?format=json" withOptions:params withFileData:theData];		
	}
	else
	{
		[uploader setTitle1:@"Upload audio done!"];
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
				endUpload = NO;
				uploader.isAudioUpOK = YES;
			}
			[uploader finishUpload];
		}
	}
}

- (void)removeAllOldFile {
	NSArray *fileList = [[NSFileManager defaultManager] contentsOfDirectoryAtPath:AUDIO_FOLDER error:nil];
	for (NSString *str in fileList) {
		NSRange foundRange=[[str lowercaseString] rangeOfString:@".caf"];
		if(foundRange.location != NSNotFound) {
			[[NSFileManager defaultManager] removeItemAtPath:[AUDIO_FOLDER stringByAppendingPathComponent:str] error:nil];
		}
	}
}

#pragma mark -
#pragma mark RestConnectionDelegate
- (void)finishRequest:(NSDictionary *)arrayData andRestConnection:(id)connector {
	if([[[arrayData objectForKey:@"response"] objectForKey:@"success"] isEqualToString:@"true"])
	{
		[[NSFileManager defaultManager] removeItemAtPath:[AUDIO_FOLDER stringByAppendingPathComponent:[array objectAtIndex:0]] error:nil];
		[array removeObjectAtIndex:0];
		[self uploadAudio];
	}
	else
	{		
		[uploader setTitle1:[NSString stringWithFormat:@"Upload audio fail: %@",[array objectAtIndex:0]]];
		[array removeObjectAtIndex:0];
		[self uploadAudio];
	}
}

- (void)cantConnection:(NSError*)error andRestConnection:(id)connector {
	if(endUpload)
	{
		endUpload=NO;
		uploader.isAudioUpOK = YES;
	}
	[uploader finishUpload];
}

@end
