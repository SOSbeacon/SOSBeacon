//
//  NewLogin.h
//  SOSBEACON
//
//  Created by bon on 7/27/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import"RestConnection.h"
#import"SOSBEACONAppDelegate.h"

@interface NewLogin : UIViewController  <RestConnectionDelegate,UIAlertViewDelegate> {
	IBOutlet UITextField *text_Phone;
	IBOutlet UIButton *submit_button;
	IBOutlet UIButton *cancel_button;
	IBOutlet UIActivityIndicatorView *actSignup;
	
	
	NSInteger flagForRequest;
	NSString *strImei;
	NSString *strPhoneNumber;
	RestConnection *restConnection;
	SOSBEACONAppDelegate *appDelegate;
	NSInteger flagForAlert;
	NSString *token;

}
@property(nonatomic,retain)NSString *token;
@property(nonatomic)NSInteger flagForAlert;
@property(nonatomic)NSInteger flagForRequest;
@property(nonatomic,retain)NSString *strImei;
@property (nonatomic, retain) RestConnection *restConnection;
@property(nonatomic,retain)NSString *strPhoneNumber;



-(IBAction)cancelButtonPress:(id)sender;
-(IBAction)submitButtonPress:(id)sender;
-(IBAction)backgroundTap:(id)sender;

@end
