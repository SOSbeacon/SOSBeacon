//
//  SettingsStorageView.m
//  SOSBEACON
//
//  Created by Geoff Heeren on 6/18/11.
//  Copyright 2011 AppTight, Inc. All rights reserved.
//

#import "SettingsStorageView.h"
#import "SOSBEACONAppDelegate.h"
#import "LoginView.h"
#import "TermsService.h"
#import "Register.h"
#import "NewLogin.h"

@implementation SettingsStorageView

// The designated initializer.  Override if you create the controller programmatically and want to perform customization that is not appropriate for viewDidLoad.
/*
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization.
    }
    return self;
}
*/


// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
	self.title=@"Storage";
    [super viewDidLoad];
}


/*
// Override to allow orientations other than the default portrait orientation.
- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation {
    // Return YES for supported orientations.
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}
*/

- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc. that aren't in use.
}

- (void)viewDidUnload {
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}


- (void)dealloc {
    [super dealloc];
}

-(IBAction)btnClearAll_Tapped:(id)sender
{
	[self.navigationController popViewControllerAnimated:YES];
	SOSBEACONAppDelegate *appDelegate;
	appDelegate = (SOSBEACONAppDelegate*)[[UIApplication sharedApplication] delegate];
	appDelegate.tabBarController.selectedIndex = 0;

}
-(IBAction)btnResetAll_Tapped:(id)sender{
	alertIndex=1;
	UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Reset all to default?"
													message:NSLocalizedString(@"ResetAllToDefault",@"")
												   delegate:self
										  cancelButtonTitle:@"Yes"
										  otherButtonTitles:@"No",nil];
	[alert show];
	[alert release];
}
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
	if (buttonIndex == 0) {
		if (alertIndex==1) {
			SOSBEACONAppDelegate *appDelegate = (SOSBEACONAppDelegate*)[[UIApplication sharedApplication] delegate];
			appDelegate.logout = YES;
			/*
			NSMutableDictionary *newinfo = [[NSMutableDictionary alloc] init];
		    [newinfo setObject:@"" forKey:@"imei"];
			[newinfo setObject:@"" forKey:@"number"];
			[newinfo writeToFile:[NSString stringWithFormat:@"%@/newlogin.plist",DOCUMENTS_FOLDER] atomically:YES];		
			[newinfo autorelease];
			 */
		//	NewLogin *login = [[NewLogin alloc] init];
		//	login.flag = 2;
			if (appDelegate.newlogin) 
			{
				NSLog(@"release newlogin");
				[appDelegate.newlogin release];
			}
			appDelegate.newlogin =[[NewLogin alloc] init];
			appDelegate.newlogin.flag = 2;
			NSLog(@"flag = 2");
			[self presentModalViewController:appDelegate.newlogin animated:YES];
			[self.navigationController popViewControllerAnimated:YES];
			appDelegate.tabBarController.selectedIndex= 0;
		//	[appDelegate.groupView release];
		//	appDelegate.groupView = nil;

			//[login release];
			appDelegate.flagSetting = 1;
		//	NSLog(@"flag setting: %d",appDelegate.flagSetting);
			[[NSFileManager defaultManager] removeItemAtPath:[NSString stringWithFormat:@"%@/active.plist",DOCUMENTS_FOLDER] error:nil];
			[[NSFileManager defaultManager] removeItemAtPath:[NSString stringWithFormat:@"%@/newlogin.plist",DOCUMENTS_FOLDER] error:nil];
			NSString *fileCount=[DOCUMENTS_FOLDER stringByAppendingPathComponent:@"count.plist"];
			[[NSFileManager defaultManager] removeItemAtPath:fileCount error:nil];
			NSString *file1=[DOCUMENTS_FOLDER stringByAppendingPathComponent:@"message.plist"];
			[[NSFileManager defaultManager] removeItemAtPath:file1 error:nil];
			
			
			NSString *pass = [[NSString alloc] initWithString:@"1"];
			[pass writeToFile:[NSString stringWithFormat:@"%@/info.plist",DOCUMENTS_FOLDER] atomically:YES];
			[pass release];
			
			
		}else if(alertIndex==2) {
			
		}
		
	}
	alertIndex=0;
}
@end