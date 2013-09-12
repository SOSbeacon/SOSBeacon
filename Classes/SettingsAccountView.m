//
//  SettingsAccountView.m
//  SOSBEACON
//
//  Created by Geoff Heeren on 6/18/11.
//  Copyright 2011 AppTight, Inc. All rights reserved.
//

#import "SettingsAccountView.h"

#import "ValidateData.h"
@implementation SettingsAccountView
@synthesize txtuserName,txtEmail,txtPhoneNumber,txtPassword;
@synthesize actSetting,btnSave,rest;
// The designated initializer.  Override if you create the controller programmatically and want to perform customization that is not appropriate for viewDidLoad.



-(IBAction)cannotEditPhone
{
}
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
       	btnSave=[[UIBarButtonItem alloc] initWithTitle:@"Save" style:UIBarButtonItemStyleBordered target:self action:@selector(SaveSetting)];
		self.navigationItem.rightBarButtonItem = btnSave;
		
    }
    return self;
}


/*
 
 if ([[NSFileManager defaultManager]  fileExistsAtPath:[NSString stringWithFormat:@"%@/allAccount.plist",DOCUMENTS_FOLDER] ]) 
 {
 
 NSMutableDictionary *accArray =[[NSMutableDictionary alloc] initWithContentsOfFile:[NSString stringWithFormat:@"%@/allAccount.plist",DOCUMENTS_FOLDER]];
 NSString *strPhoneNumber = appDelegate.phone;
 NSLog(@" home view-------------> %@",strPhoneNumber);
 NSMutableArray *acc = [accArray  objectForKey:strPhoneNumber];
 if (acc == nil) 
 {
 
 
 }
 else 
 {
 
 
 */




// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
	self.title=@"Account Info";
	rest = [[RestConnection alloc] initWithBaseURL:SERVER_URL];
	rest.delegate =self;	
	appDelegate = (SOSBEACONAppDelegate*)[[UIApplication sharedApplication] delegate];
	[self loadData];
	txtPhoneNumber.enabled = NO;
    [super viewDidLoad];
    
    //NSLog(@" pass : %@",txtPassword.text);
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
	rest.delegate = nil;
	[rest release];
	[btnSave release];
	[actSetting release];
	[txtuserName release]; 
	[txtEmail release]; 
	[txtPhoneNumber release];
	[txtPassword release];
    [super dealloc];
}
-(void)loadData{
	
	appDelegate = (SOSBEACONAppDelegate*)[[UIApplication sharedApplication] delegate];
	txtuserName.text = [appDelegate.informationArray objectForKey:@"username"];
	txtEmail.text = [appDelegate.informationArray objectForKey:@"email"];
	txtPhoneNumber.text = [appDelegate.informationArray objectForKey:@"number"];
	//txtPassword.text =[appDelegate.informationArray objectForKey:@"password"];
	
	if([[NSFileManager defaultManager] fileExistsAtPath:[NSString stringWithFormat:@"%@/info.plist",DOCUMENTS_FOLDER]])
	{
        NSLog(@"read file");
		NSString *password =[[NSString alloc] initWithContentsOfFile:[NSString stringWithFormat:@"%@/info.plist",DOCUMENTS_FOLDER]];
       // NSString *password =[[NSString alloc ]initWithContentsOfFile:[NSString stringWithFormat:@"%@/info.plist",DOCUMENTS_FOLDER] encoding:[NSString defaultCStringEncoding] error:nil];
		if ([password isEqualToString:@"1" ]) 
		{
			txtPassword.text=@"";
		}
		else 
		{
			//NSLog(@" password :%@",password);
			txtPassword.text = password;
		}

		

		[password  release];
	}
	///////
	 
	 if ([[NSFileManager defaultManager]  fileExistsAtPath:[NSString stringWithFormat:@"%@/allAccount.plist",DOCUMENTS_FOLDER] ]) 
	 {
	 
		 NSMutableDictionary *accArray =[[NSMutableDictionary alloc] initWithContentsOfFile:[NSString stringWithFormat:@"%@/allAccount.plist",DOCUMENTS_FOLDER]];
		 NSString *strPhoneNumber = [appDelegate.phone retain];

		 NSMutableArray *acc = [accArray  objectForKey:strPhoneNumber];
		 if (acc == nil) 
		 {
			// NSLog(@"do no thing");
			
		 }
			else
		{
			 NSString *strpass = [ acc objectAtIndex:0]; 
			 if([strpass length]>6) txtPassword.text = strpass;
		}
	[strPhoneNumber  release];
		 [accArray release];
	 }
	 
	 //////

	
}
-(void)save
{
	save = TRUE;
	btnSave.enabled = FALSE;
	actSetting.hidden=NO;
	[actSetting startAnimating];
	
	if(![txtEmail.text isEqualToString:@""]&&!(checkMail(txtEmail.text))) {
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error"
															message:NSLocalizedString(@"EmailNotValid",@"")
														   delegate:nil
												  cancelButtonTitle:@"Ok"
												  otherButtonTitles:nil];
		[alertView show];
		[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:CONF_DIALOG_DELAY_TIME];
		[alertView release];
		//save = FALSE;
		btnSave.enabled = TRUE;
		[actSetting stopAnimating];
		actSetting.hidden = YES;
		return;		
	}else
		if (!([txtPassword.text isEqualToString:@""]) &&!(checkPassWord(txtPassword.text)) )
		{
			//save = FALSE;
			btnSave.enabled = TRUE;
			[actSetting stopAnimating];
			actSetting.hidden = YES;		
			UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error"
																message:NSLocalizedString(@"PassShort",@"")
															   delegate:nil
													  cancelButtonTitle:@"Ok"
													  otherButtonTitles:nil];
			[alertView show];
			[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:CONF_DIALOG_DELAY_TIME];
			[alertView release];
			return;
		}
		else
			
		{
			[appDelegate.informationArray setObject:txtuserName.text forKey:@"username"];
			[appDelegate.informationArray setObject:txtEmail.text forKey:@"email"];
			[appDelegate.informationArray setObject:txtPhoneNumber.text forKey:@"number"];
			[appDelegate.informationArray setObject:txtPassword.text forKey:@"password"];	
			UIDevice *device = [UIDevice currentDevice];
			NSString  *strImei = [[NSString alloc] initWithString:[device uniqueIdentifier]];
			
			NSArray *key1= [NSArray arrayWithObjects:@"id",@"imei",@"token",@"name",@"email",@"password",@"number",@"do",nil];
			NSArray *obj1 =[NSArray arrayWithObjects:[NSString stringWithFormat:@"%d",appDelegate.phoneID],strImei,appDelegate.apiKey,txtuserName.text,txtEmail.text,txtPassword.text,txtPhoneNumber.text,@"UPDATE",nil];
			NSDictionary *params1 = [NSDictionary dictionaryWithObjects:obj1 forKeys:key1];
			[rest putPath:[NSString stringWithFormat:@"/phones/%d?&format=json",appDelegate.phoneID] withOptions:params1];	
			[strImei release];
			
		}	
	
	
}
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
	if (buttonIndex == 0) 
	{
		if (flagalert ==1)
		{
			flagalert = 2;
			[self save];
		}
	}
	else {
		
	}

}
-(IBAction)SaveSetting
{
	flagalert =1;
	UIAlertView *alert =[ [UIAlertView alloc] initWithTitle:nil 
													message:NSLocalizedString(@"SaveChange",@"")
												   delegate:self
										  cancelButtonTitle:@"Yes" otherButtonTitles:@"No",nil];
	[alert show];
	[alert release];
	/*
	save = TRUE;
	btnSave.enabled = FALSE;
	actSetting.hidden=NO;
	[actSetting startAnimating];

		if(![txtEmail.text isEqualToString:@""]&&!(checkMail(txtEmail.text))) {
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error"
															message:@"Email address is not valid"
														   delegate:nil
												  cancelButtonTitle:@"Ok"
												  otherButtonTitles:nil];
		[alertView show];
		[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:CONF_DIALOG_DELAY_TIME];
		[alertView release];
		//save = FALSE;
		btnSave.enabled = TRUE;
		[actSetting stopAnimating];
		actSetting.hidden = YES;
		return;		
	}else
	if (!([txtPassword.text isEqualToString:@""]) &&!(checkPassWord(txtPassword.text)) )
	{
		//save = FALSE;
		btnSave.enabled = TRUE;
		[actSetting stopAnimating];
		actSetting.hidden = YES;		
		UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error"
															message:@"Password can not be shorter than 6 characters"
														   delegate:nil
												  cancelButtonTitle:@"Ok"
												  otherButtonTitles:nil];
		[alertView show];
		[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:CONF_DIALOG_DELAY_TIME];
		[alertView release];
		return;
	}
	else
	 
	{
		[appDelegate.informationArray setObject:txtuserName.text forKey:@"username"];
		[appDelegate.informationArray setObject:txtEmail.text forKey:@"email"];
		[appDelegate.informationArray setObject:txtPhoneNumber.text forKey:@"number"];
		[appDelegate.informationArray setObject:txtPassword.text forKey:@"password"];	
		UIDevice *device = [UIDevice currentDevice];
		NSString  *strImei = [[NSString alloc] initWithString:[device uniqueIdentifier]];
		NSArray *key1= [NSArray arrayWithObjects:@"id",@"imei",@"token",@"name",@"email",@"password",@"number",@"do",nil];
		NSArray *obj1 =[NSArray arrayWithObjects:[NSString stringWithFormat:@"%d",appDelegate.phoneID],strImei,appDelegate.apiKey,txtuserName.text,txtEmail.text,txtPassword.text,txtPhoneNumber.text,@"UPDATE",nil];
		NSDictionary *params1 = [NSDictionary dictionaryWithObjects:obj1 forKeys:key1];
		[rest putPath:[NSString stringWithFormat:@"/phones/%d?&format=json",appDelegate.phoneID] withOptions:params1];		


	}	
	
*/	
}

- (void) DimisAlertView:(UIAlertView*)alertView {
	[alertView dismissWithClickedButtonIndex:0 animated:TRUE];
}

#pragma mark IBAction 
- (IBAction)textFieldDoneEditing:(id)sender {
	[sender resignFirstResponder];	
}

- (IBAction)backgroundTap:(id)sender {
	[txtEmail resignFirstResponder];
	[txtPassword resignFirstResponder];
	[txtPhoneNumber resignFirstResponder];
	[txtuserName resignFirstResponder];
}

#pragma mark -
#pragma mark finish request

-(void)finishRequest:(NSDictionary *)arrayData andRestConnection:(id)connector{
	[actSetting stopAnimating];
	actSetting.hidden = YES;
	btnSave.enabled = TRUE;
	isEdit = NO;
	//NSLog(@" setting account array data-->>: %@ ",arrayData);
	NSInteger responsecode = [[[arrayData objectForKey:@"response"] objectForKey:@"responseCode"] intValue];
		if (responsecode == 1)
		{
			
			/*
			NSArray *key = [NSArray arrayWithObjects:@"username",@"password",@"phoneNumber",nil];
			NSArray *obj = [NSArray arrayWithObjects:txtEmail.text,txtPassword.text,txtPhoneNumber.text,nil];
			NSDictionary *dict = [NSDictionary dictionaryWithObjects:obj forKeys:key];
			[dict writeToFile:[NSString stringWithFormat:@"%@/info.plist",DOCUMENTS_FOLDER] atomically:YES];	
			*/
			
			if ([txtPassword.text length]>=6) 
			{
	
			NSString *pass = txtPassword.text;
				//NSLog(@" write to file");

			[pass writeToFile:[NSString stringWithFormat:@"%@/info.plist",DOCUMENTS_FOLDER] atomically:YES];
			//	[pass writeToFile:[NSString stringWithFormat:@"%@/info.plist",DOCUMENTS_FOLDER] atomically:YES encoding:NSUnicodeStringEncoding error:nil];	
			
			///////
			if ([[NSFileManager defaultManager]  fileExistsAtPath:[NSString stringWithFormat:@"%@/allAccount.plist",DOCUMENTS_FOLDER] ]) 
			{
				
				NSMutableDictionary *accArray =[[NSMutableDictionary alloc] initWithContentsOfFile:[NSString stringWithFormat:@"%@/allAccount.plist",DOCUMENTS_FOLDER]];
				NSString *strPhoneNumber = [appDelegate.phone retain];
				
				NSMutableArray *acc = [accArray  objectForKey:strPhoneNumber];
				if (acc == nil) 
				{
					//NSLog(@"do no thing");
					
				}
				else
				{
					[acc replaceObjectAtIndex:0 withObject:pass];
					[accArray  setObject:acc forKey:strPhoneNumber];
					[accArray  writeToFile:[NSString stringWithFormat:@"%@/allAccount.plist",DOCUMENTS_FOLDER] atomically:YES];
					
				}
				[strPhoneNumber  release];
				[accArray autorelease];
				
			}
			
			///////
		//	NSLog(@" passs word after save: %@",pass);
			}
			btnSave.enabled = TRUE;
			
		//	txtPhoneNumber.text = [appDelegate.informationArray objectForKey:@"number"];

			UIAlertView *alertView= [[UIAlertView alloc] initWithTitle:@""
															   message:@"Phone has been updated successfully" 
															  delegate:self 
													 cancelButtonTitle:@"Ok" 
													 otherButtonTitles:nil] ;
			[alertView show];
			[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:CONF_DIALOG_DELAY_TIME];
			[alertView release];
			save = NO;

		}
		else {
			NSString *message= [[arrayData objectForKey:@"response"] objectForKey:@"message"];
			
			btnSave.enabled = TRUE;
			[actSetting stopAnimating];
			actSetting.hidden = YES;			
			UIAlertView *alertView= [[UIAlertView alloc] initWithTitle:@"Error"
															   message:message 
															  delegate:self 
													 cancelButtonTitle:@"Ok" 
													 otherButtonTitles:nil] ;
			[alertView show];
			[self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:2];
			[alertView release];
			
		}
	[txtEmail resignFirstResponder];
	[txtPassword resignFirstResponder];
	[txtPhoneNumber resignFirstResponder];
	[txtuserName resignFirstResponder];
}

#pragma mark -
#pragma mark -network fail
-(void)cantConnection:(NSError *)error andRestConnection:(id)connector{
	btnSave.enabled = TRUE;
	[actSetting stopAnimating];
	actSetting.hidden=YES;
	if (save) {
		alertView();
		save = NO;
	}
}

@end
