//
//  Tracking.h
//  SOSBEACON
//
//  Created by Geoff Heeren on 2/9/11.
//  Copyright 2011 AppTight, Inc. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "FlurryAPI.h"

@interface Tracking : NSObject {

}
+(void)startTracking;
+(void)trackEvent:(NSString*)msg;
+(void)trackException:(NSException *)exception ;
+(void)updateLocation:(CLLocation*)location;
+(BOOL)doTracking;
@end
