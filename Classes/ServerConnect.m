//
//  ServerConnect.m
//  SOSBEACON
//
//  Created by Tran Ngoc Anh on 10/06/2010.
//  Copyright 2010 CNC. All rights reserved.
//

#import "ServerConnect.h"


@implementation ServerConnect
@synthesize apiKey;
@synthesize userName;
@synthesize passWord;

- (id)init {
	if(self=[super init])
	{}
	return self;
}

- (void) dealloc
{
	[apiKey release];
	[userName release];
	[passWord release];
	[super dealloc];
}

- (BOOL)loginUser:(NSString*)username andPassword:(NSString*)password {
	userName=username;
	passWord=password;
	return [self loginUser];
}

- (BOOL)loginUser {
	return YES;
}

- (NSMutableArray*)getGroups {
	return nil;
}

- (void)uploadPhoto {}
- (void)uploadAudio {}

@end
