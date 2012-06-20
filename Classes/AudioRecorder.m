//
//  AudioRecorder.m
//  SOSBEACON
//
//  Created by Tran Ngoc Anh on 16/06/2010.
//  Copyright 2010 CNC. All rights reserved.
//

#import "AudioRecorder.h"
#import "SOSBEACONAppDelegate.h"
#import "RestConnection.h"
#import "AppSetting.h"
#import "HomeView.h"
#import "CaptorView.h"
#import "StatusView.h"
#import "Uploader.h"

#define DOCUMENTS_FOLDER [NSHomeDirectory() stringByAppendingPathComponent:@"Documents"]

@implementation AudioRecorder
@synthesize soundRecorder,timeDisplay,countDown,sizeDisplay;
@synthesize block;

#pragma mark -
#pragma mark initFrame

- (id)initWithFrame:(CGRect)frame {
    if ((self = [super initWithFrame:frame])) {
        // Initialization code
    }
    return self;
}

#pragma mark initAudio
// init audio record
- (void)initAudio {
	NSLog(@"initAudio");
	appDelegate = (SOSBEACONAppDelegate*)[[UIApplication sharedApplication] delegate];
	
	AVAudioSession *audioSession = [AVAudioSession sharedInstance];
	audioSession.delegate = self;
	[audioSession setActive:YES error: nil];
	[audioSession setCategory:AVAudioSessionCategoryPlayAndRecord error:nil];
	UInt32 audioRouteOverride = kAudioSessionOverrideAudioRoute_Speaker;
	AudioSessionSetProperty(kAudioSessionProperty_OverrideAudioRoute,sizeof(audioRouteOverride),&audioRouteOverride);
	
	timeDisplay.text=[NSString stringWithFormat:@"00:%@",[appDelegate.settingArray objectForKey:ST_VoiceRecordDuration]];
	sizeDisplay.text=@"0 KB";
	
	countDown = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(countTimer) userInfo:nil repeats:YES];
	
	if(![[NSFileManager defaultManager] fileExistsAtPath:[NSString stringWithFormat:@"%@/Audio",DOCUMENTS_FOLDER]])
	{
		[[NSFileManager defaultManager] createDirectoryAtPath:[NSString stringWithFormat:@"%@/Audio",DOCUMENTS_FOLDER] withIntermediateDirectories:NO attributes:nil error:nil];
	}
}

- (void)stopRecordAudio {
	NSLog(@"STOP Record");
	isRecording = NO;
	timeDisplay.text=@"Time: 00:00";
	sizeDisplay.text=@"Size: 0 KB";
	if(soundRecorder!=nil)
	{
		[soundRecorder stop];
		[soundRecorder release];
		soundRecorder = nil;
	}
	
	
}

- (void)startRecordAudio {
	NSLog(@"START Recording");
	isRecording = YES;
	
	[appDelegate playSound];
	
	//setting
	NSDateFormatter *formatter;
	NSString        *dateString;
	
	formatter = [[NSDateFormatter alloc] init];
	[formatter setDateFormat:@"HH:mm:ss"];
	
	dateString = [formatter stringFromDate:[NSDate date]];
	[formatter release];
	NSMutableDictionary *recordSettings = [[NSMutableDictionary alloc] init];
	
	[recordSettings setValue :[NSNumber numberWithInt:kAudioFormatLinearPCM] forKey:AVFormatIDKey];
	[recordSettings setValue:[NSNumber numberWithFloat:1000.0] forKey:AVSampleRateKey]; 
	[recordSettings setValue:[NSNumber numberWithInt: 1] forKey:AVNumberOfChannelsKey];
	
	[recordSettings setValue :[NSNumber numberWithInt:4] forKey:AVLinearPCMBitDepthKey];
	[recordSettings setValue :[NSNumber numberWithBool:NO] forKey:AVLinearPCMIsBigEndianKey];
	[recordSettings setValue :[NSNumber numberWithBool:NO] forKey:AVLinearPCMIsFloatKey];
	
	currentFile = [NSString stringWithFormat:@"%@/Audio/sound%d_%@.caf",DOCUMENTS_FOLDER,block,dateString];
	NSURL *fileUrl=[[[NSURL alloc] initFileURLWithPath:currentFile] autorelease];

	AVAudioRecorder *newRecorder =[[AVAudioRecorder alloc] initWithURL:fileUrl
															  settings:recordSettings
																 error:nil];
	
	[recordSettings release]; 	
	self.soundRecorder = newRecorder;
	soundRecorder.delegate = self;
	[soundRecorder prepareToRecord];
	[soundRecorder recordForDuration:30];
	
}

- (void)setCaptorView:(CaptorView*)captor {
	captorView=captor;
}

- (void)startRecord {
	isUpload = NO;
	isRecording = NO;
	blockDisplay.text = [NSString stringWithFormat:@"Session: %d",block];
	[self startRecordAudio];
}

- (void)endRecordBlock {
	if(appDelegate.uploader.autoUpload) [appDelegate.uploader uploadAudio]; //upload luon sau khi ghi xong
	[self stopRecordAudio]; 
	
	block--;
	if(block>0) 
	{
		[self startRecordAudio];
	}
	else
	{
		[[AVAudioSession sharedInstance] setActive:NO error:nil];
		
		
		[appDelegate.uploader endUploadAudio];
		if(!appDelegate.uploader.autoUpload) [appDelegate.uploader uploadAudio];
	}
}

#pragma mark Action
- (IBAction)closeAndStop {
	if(isRecording)
	{
		block = -1;
		[self stopRecordAudio];
	}
	else
	{
		[self endRecordBlock];
	}

}

#pragma mark -
#pragma mark AVAudioRecorderDelegate
- (void)audioRecorderDidFinishRecording:(AVAudioRecorder *) aRecorder successfully:(BOOL)flag
{
	NSLog (@"audioRecorderDidFinishRecording successfully");
	[self endRecordBlock];
}


#pragma mark counttimer
-(void)stopTimer{
	[self stopRecordAudio];
	[[AVAudioSession sharedInstance] setActive:NO error: nil];
}

-(void)countTimer {
	if(isRecording)
	{
		int count = 30 - soundRecorder.currentTime;
		NSFileManager *fileManager = [NSFileManager defaultManager];
		
		NSDictionary *fileAttributes = [fileManager attributesOfItemAtPath:currentFile error:nil];
		if(fileAttributes != nil)
		{
			NSInteger fileSize= [[fileAttributes objectForKey:NSFileSize] intValue];
			fileSize = fileSize / 1024;
			sizeDisplay.text=[NSString stringWithFormat:@"Size: %d KB", fileSize];
		}
		
		if (count>0) {
			if (count <10) {
				timeDisplay.text=[NSString stringWithFormat:@"Time: 00:0%d",count];
			}
			else{
				timeDisplay.text=[NSString stringWithFormat:@"Time: 00:%d",count];
			}
		}
		
	}
}

- (void)dealloc {
	NSLog(@"DEALLOC AudioRecorder");
	[countDown invalidate];
	[timeDisplay release];
	[sizeDisplay release];
	[blockDisplay release];
	[soundRecorder release];
    [super dealloc];
}
@end
