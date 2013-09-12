//
//  Tracking.m
//  SOSBEACON
//
//  Created by Geoff Heeren on 2/9/11.
//  Copyright 2011 AppTight, Inc. All rights reserved.
//

#import "Tracking.h"


@implementation Tracking
+(void)startTracking{
	if ([self doTracking]){
		[FlurryAPI startSession:[[[NSBundle mainBundle] infoDictionary] objectForKey:@"FlurryKey"]];
	
	}

}
+(void)updateLocation:(CLLocation*)location{
	if ([self doTracking]){
		[FlurryAPI setLocation:location];
	}
}
+(void)trackEvent:(NSString*)eventName{
	if ([self doTracking]){
		[FlurryAPI logEvent:eventName];
	}
}
+(void)trackException:(NSException *)exception {
	if ([self doTracking]){
		[FlurryAPI logError:@"Uncaught" message:@"Crash!" exception:exception];
	}
}

+(BOOL)doTracking{
	if ([UIDevice currentDevice] && [UIDevice currentDevice]!=nil && [UIDevice currentDevice].model!=nil)
		return ![[UIDevice currentDevice].model isEqualToString: @"iPhone Simulator"];
	else 
		return NO;
}

@end
