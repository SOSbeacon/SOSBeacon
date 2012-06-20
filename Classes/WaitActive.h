//
//  WaitActive.h
//  SOSBEACON
//
//  Created by Kevin Hoang on 11/18/10.
//  Copyright 2010 CNC. All rights reserved.
//

#import <UIKit/UIKit.h>
@class SOSBEACONAppDelegate;
#import "Register.h"

@interface WaitActive : UIViewController {

	SOSBEACONAppDelegate *appDelegate;
	Register *regActive;
	
	NSTimer *timer;
	UIActivityIndicatorView *actSplash;
	UIView *viewSplash;
	UIImageView *splashImageView;
	BOOL noConnection;
}

@property (nonatomic, retain) NSTimer *timer;
@property (assign) Register *regActive;
@property (nonatomic, retain) IBOutlet UIActivityIndicatorView *actSplash;
@property (nonatomic, retain) IBOutlet UIView *viewSplash;
@property (nonatomic, retain) IBOutlet UIImageView *splashImageView;
@property BOOL noConnection;
@end

