//
//  WaitActive.m
//  SOSBEACON
//
//  Created by Kevin Hoang on 11/18/10.
//  Copyright 2010 CNC. All rights reserved.
//

#import "WaitActive.h"
#import "SOSBEACONAppDelegate.h"
#import "Register.h"
@implementation WaitActive
@synthesize actSplash, timer,viewSplash,splashImageView, noConnection,regActive;

/*
 // The designated initializer.  Override if you create the controller programmatically and want to perform customization that is not appropriate for viewDidLoad.
 - (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
 if ((self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil])) {
 // Custom initialization
 }
 return self;
 }
 */


// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
    [super viewDidLoad];
	appDelegate = (SOSBEACONAppDelegate*)[[UIApplication sharedApplication] delegate];
	//appDelegate
	viewSplash.frame = CGRectMake(0, 0, 320, 480);
	[actSplash startAnimating];
	
}

/*
 // Override to allow orientations other than the default portrait orientation.
 - (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
 // Return YES for supported orientations
 return (interfaceOrientation == UIInterfaceOrientationPortrait);
 }
 */

- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	self.splashImageView = nil;
	self.viewSplash = nil;
	self.actSplash = nil;
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)dealloc {
	[splashImageView release];
	[viewSplash release];
	[actSplash release];
	[timer release];
    [super dealloc];
}

#pragma mark Other Function

- (void)time
{
	timer = [NSTimer scheduledTimerWithTimeInterval:3.0 target:self selector:@selector(fadeScreen) userInfo:nil repeats:YES];//thoi gian load la 1.0

}
- (void)fadeScreen
{
	if (appDelegate.reg.waitingActive==TRUE) {
		[appDelegate.reg getData];
		
	}
	else {
		[splashImageView removeFromSuperview];
		[self.view removeFromSuperview];
		[appDelegate.reg.view removeFromSuperview];
		
	}

}
- (void) finishedFading
{
	[UIView beginAnimations:nil context:nil];
	[UIView setAnimationDuration:0.5]; 
	
	UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Message"
														message:@"Please active acc"
													   delegate:nil
											  cancelButtonTitle:@"OK"
											  otherButtonTitles:nil];
	[alertView show];
	[alertView release];
	
	exit(0);
	[UIView commitAnimations];
}

@end
