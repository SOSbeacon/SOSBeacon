//
//  NewLogin.m
//  SOSBEACON
//
//  Created by bon on 7/27/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "NewLogin.h"


@implementation NewLogin
@synthesize flagForAlert;
@synthesize strImei;
@synthesize strPhoneNumber;
@synthesize restConnection;
@synthesize flagForRequest;
@synthesize token;


-(IBAction)backgroundTap:(id)sender
{
	[text_Phone resignFirstResponder];
}
-(IBAction)cancelButtonPress:(id)sender
{
	[self.view removeFromSuperview ];
}
-(IBAction)submitButtonPress:(id)sender
{
	//flagForRequest = 1;
	submit_button.userInteractionEnabled = NO;
	cancel_button.userInteractionEnabled = NO;
	NSLog(@"11");
	strPhoneNumber = text_Phone.text;
	NSLog(@"22");
	NSLog(@"%@",strPhoneNumber);
	NSLog(@"%@",strImei);
	NSLog(@"33");
	
	/*
	[restConnection getPath:[NSString stringWithFormat:@"/phone/%@?number=%@&",strImei,strPhoneNumber];
				withOptions:nil]
	 
	*/
	
	[restConnection getPath:[NSString stringWithFormat:@"/rest?email=nguyenngothien@gmail.com&password=111111&phoneNumber=%@&imei=%@&format=json",
							strPhoneNumber,strImei] withOptions:nil];
	actSignup.hidden = NO;
	[actSignup startAnimating]; 
	NSLog(@"submit");

}


- (void)viewDidLoad {
	
	UIDevice *device = [UIDevice currentDevice];
	strImei = [[NSString alloc] initWithString:[device uniqueIdentifier]];
	strPhoneNumber =[[NSString alloc] init];
	actSignup.hidden=YES;
	RestConnection *tempConnection = [[RestConnection alloc] initWithBaseURL:SERVER_URL];
	restConnection = tempConnection;
	restConnection.delegate = self;
	appDelegate = (SOSBEACONAppDelegate*)[[UIApplication sharedApplication] delegate];
   
	
	/////////******** kiem tra neu da ton tai tai khoan thi vao luon
	
	 NSString *file=[DOCUMENTS_FOLDER stringByAppendingPathComponent:@"newLogin.plist"];
	 if ([[NSFileManager defaultManager] fileExistsAtPath:file])
	 {
		 NSDictionary *param = [[NSDictionary alloc]initWithContentsOfFile:file ];
		 
		 strImei =[param objectForKey:@"imei"];
		 strPhoneNumber =[param objectForKey:@"number"];
		 [restConnection getPath:[NSString stringWithFormat:@"/phone/%@?number=%@",strImei,strPhoneNumber]
					 withOptions:nil];
		NSLog(@"ton tai file");
	 }
	 else
	 {
		NSLog(@"khong ton tai file");
	 }
	 
	

	 [super viewDidLoad];
}



- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (void)viewDidUnload {
	actSignup= nil;
	text_Phone = nil;
	submit_button= nil;
	cancel_button = nil;
    [super viewDidUnload];
}


- (void)dealloc {
	[strImei release];
	[strPhoneNumber release];
	[restConnection release];
    [super dealloc];
}
-(void)cantConnection:(NSError *)error andRestConnection:(id)connector
{
	NSLog(@"can't connect ");
}

-(void)finishRequest:(NSDictionary *)arrayData andRestConnection:(id)connector
{
	submit_button.userInteractionEnabled = YES;
	cancel_button.userInteractionEnabled = YES;
	NSLog(@"new login");
	printf("\n \n \n");
	NSLog(@"%@",arrayData);
	[actSignup stopAnimating];
	actSignup.hidden = YES;
	appDelegate.apiKey = [[arrayData objectForKey:@"response"] objectForKey:@"token"];
	[self.view removeFromSuperview];
	
	//if (flagForRequest == 1)
		
			NSInteger respondcode =[ [arrayData  objectForKey:@"responseCode"] intValue];
	
			if (respondcode ==1 ) 
				{ 
					NSLog(@"responsecode =1");
					////imei va phone moi -> dk moi
					NSLog(@"responsecode =1");
					NSArray *key = [[NSArray alloc] initWithObjects:@"imei",@"number",@"phoneType",nil];
					NSArray *obj = [[NSArray alloc] initWithObjects:strImei,strPhoneNumber,@"1",nil];
					
					NSDictionary *param = [[NSDictionary alloc] initWithObjects:obj forKeys:key];
					
					[restConnection postPath:@"/phones?format=json" withOptions:param];
					
					
				}
			else if(respondcode ==2 )
			{
				NSLog(@"responsecode = 2");
				NSLog(@"ngon");
				NSString *file=[DOCUMENTS_FOLDER stringByAppendingPathComponent:@"newLogin.plist"];
				if ([[NSFileManager defaultManager] fileExistsAtPath:file])
					{
						/*
						[[NSFileManager defaultManager] removeItemAtPath:file error:nil];
						[[NSFileManager defaultManager] createFileAtPath:file contents:nil attributes:nil];
						NSArray *key = [[NSArray alloc] initWithObjects:@"imei",@"number",nil];
						NSArray *obj = [[NSArray alloc] initWithObjects:strImei,strPhoneNumber,nil];
						NSDictionary *param = [[NSDictionary alloc] initWithObjects:obj forKeys:key];
						[param writeToFile:file atomically:YES];
						 */
					}
					else
					{
						[[NSFileManager defaultManager] createFileAtPath:file contents:nil attributes:nil];
						NSArray *key = [[NSArray alloc] initWithObjects:@"imei",@"number",nil];
						NSArray *obj = [[NSArray alloc] initWithObjects:strImei,strPhoneNumber,nil];
						NSDictionary *param = [[NSDictionary alloc] initWithObjects:obj forKeys:key];
						[param writeToFile:file atomically:YES];
					}

				[self.view removeFromSuperview];
				
			}
			else if(respondcode == 3)
			{
				NSLog(@"responsecode =3");
				flagForAlert =1;
				NSLog(@"show alert chua active");
				UIAlertView *alert =[[UIAlertView alloc] initWithTitle:nil message:@"please active your account"
															  delegate:self cancelButtonTitle:@"YES"
													          otherButtonTitles:@"NO",nil];
				[alert show];
				[alert release];
				
			}
			else if(respondcode == 4)
			{
				NSLog(@"responsecode =4");
				token = [[NSString alloc] initWithString:[[arrayData objectForKey:@"phone data and settings"] objectForKey:@"token"]];	

				flagForAlert =2;
				UIAlertView *alert =[[UIAlertView alloc] initWithTitle:nil message:@"your phone id had been used to register!"
															  delegate:self cancelButtonTitle:@"register new account"
													 otherButtonTitles:@"use old account",nil];
			}
			else if(respondcode == 5)
			{
				NSLog(@"responsecode =5");
				token = [[NSString alloc] initWithString:[[arrayData objectForKey:@"phone data and settings"] objectForKey:@"token"]];	

				flagForAlert = 3;
				UIAlertView *alert =[[UIAlertView alloc] initWithTitle:nil message:@"your phone number had been used to register!"
															  delegate:self cancelButtonTitle:@"register new account"
													 otherButtonTitles:@"use old account",nil];
			}
			else if(respondcode == 6)
			{
				NSLog(@"responsecode =6");
				NSLog(@"request invalid");
			}
			else if(respondcode == 7)
			{
				NSLog(@"responsecode =7");
				flagForAlert =4;
				UIAlertView *alert =[[UIAlertView alloc] initWithTitle:nil message:@"error! please check phonenumber"
															  delegate:self cancelButtonTitle:@"OK"
													 otherButtonTitles:nil];
			}

			
}
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
	if (buttonIndex == 0) 
	{
		if (flagForAlert ==1 )
		{
			exit(0);
		}
		if (flagForAlert ==2 || flagForAlert == 3 ) 
		{
			NSArray *key = [[NSArray alloc] initWithObjects:@"imei",@"number",@"phoneType",nil];
			NSArray *obj = [[NSArray alloc] initWithObjects:strImei,strPhoneNumber,@"1",nil];
			
			NSDictionary *param = [[NSDictionary alloc] initWithObjects:obj forKeys:key];
			[restConnection postPath:@"/phones?format=json" withOptions:param];
			
		}
		
		
	}
	else 
	{
		if (flagForAlert == 1)
		{
			
		}
		if (flagForAlert ==2 || flagForAlert == 3 ) 
		{
			NSArray *key = [[NSArray alloc] 
							initWithObjects:@"imei",@"number",@"token,",@"name",@"email",@"password",@"requestType",nil];
			
			NSArray *obj =[[NSArray alloc]
						   initWithObjects:strImei,strPhoneNumber,token,@"your name",@"",@"",@"UPDATE",nil];
			NSDictionary *param =[[NSDictionary alloc] initWithObjects:obj forKeys:key];
			
			[restConnection putPath:@"/phones?format=json" withOptions:param];
		}		
	}
	
}


@end
