//
//  CheckingIn.m
//  SOSBEACON
//
//  Created by cncsoft on 9/10/10.
//  Copyright 2010 CNC. All rights reserved.
//

#import <QuartzCore/QuartzCore.h>
#import "CheckingIn.h"
#import "RestConnection.h"
#import "SOSBEACONAppDelegate.h"
#import "ValidateData.h"
#import "CaptorView.h"
#import "Uploader.h"
#import "StatusView.h"
#import "SlideToCancelViewController.h"

@implementation CheckingIn

@synthesize restConnection,lblContact,tvTextMessage,scrollView;
@synthesize arrayCheckin;
@synthesize countArray;
@synthesize groupArray;
@synthesize typeArray;
@synthesize actionSheet1;

/*
-(void)checkTextLen
{
	NSLog(@"check length: length = %d",[tvTextMessage.text length]);
//	NSLog(@" test : %d",[test.text length]);
	if ([tvTextMessage.text length] ==121)
	{
		NSString *message = tvTextMessage.text;
		tvTextMessage.text= [ message substringToIndex:119];

		
		UIAlertView *alert =[[UIAlertView alloc] initWithTitle:nil 
													   message:@"message length exceeds sms limit"
													  delegate:nil
											 cancelButtonTitle:@"OK"
											 otherButtonTitles:nil];
		[alert show];
		[alert release];
		//[self di]
	}

	
}
 */
- (void)viewWillAppear:(BOOL)animated {
	[super viewWillAppear:animated];
	appDelegate.uploader.delegate = self;
	
	//if ([[appDelegate.settingArray objectForKey:CK_CheckingIn] intValue] == 0)
	if (newFlag == 1) 
	{
		
	}
	else {
		

		lblContact.text = @"Family";
	tvTextMessage.hidden = YES;
	tvTextMessage.text = @"I'm ok";
	}
	


}

// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
- (void)viewDidLoad {
    [super viewDidLoad];
	
	//
	
	//
	 
	//custom check
	[btnCheck setTitle:@"Check in Now" forState:UIControlStateNormal];
	//[[btnCheck titleLabel] setTextColor:[UIColor blackColor]];
	[[btnCheck titleLabel] setTextAlignment:UITextAlignmentCenter];
	[[btnCheck titleLabel] setLineBreakMode:UILineBreakModeWordWrap];
	[[btnCheck titleLabel] setNumberOfLines:0];
	[[btnCheck titleLabel] setFont:[UIFont boldSystemFontOfSize:20.0f]];
	
	appDelegate = (SOSBEACONAppDelegate*)[[UIApplication sharedApplication] delegate];
		
	restConnection = [[RestConnection alloc] initWithBaseURL:SERVER_URL];
	restConnection.delegate = self;
	scrollView.contentSize = CGSizeMake(320, 480);
	tvTextMessage.delegate = self;
	
	if (!slideCheckin) {
		// Create the slider
		slideCheckin = [[SlideToCancelViewController alloc] init];
		slideCheckin.delegate = self;
	//	slideCheckin.label.font =[UIFont systemFontOfSize:14.0];
		slideCheckin.labelText = @"  Send audio/image recording";
		// Position the slider off the bottom of the view, so we can slide it up
		CGRect sliderFrame = slideCheckin.view.frame;
		sliderFrame.origin.y = self.view.frame.size.height;
		slideCheckin.view.frame = sliderFrame;
		// Add slider to the view
		[self.view addSubview:slideCheckin.view];
	}
	slideCheckin.enabled=YES;
	CGPoint sliderCenter = slideCheckin.view.center;
	sliderCenter.y -= slideCheckin.view.bounds.size.height+125;
	slideCheckin.view.center = sliderCenter;
	[UIView commitAnimations];
	////*******
	NSString *fileCount=[DOCUMENTS_FOLDER stringByAppendingPathComponent:@"count.plist"];
	// array save message template
	NSString *file=[DOCUMENTS_FOLDER stringByAppendingPathComponent:@"message.plist"];
	if ([[NSFileManager defaultManager] fileExistsAtPath:file]) 
	{
	
		NSLog(@"co ton tai file message.plist");
		arrayCheckin = [[NSMutableArray alloc] initWithContentsOfFile:file];
		countArray = [[NSMutableArray alloc] initWithContentsOfFile:fileCount];
	}
	else 
	{
		[[NSFileManager defaultManager] createFileAtPath:file contents:nil attributes:nil];
		arrayCheckin = [[NSMutableArray alloc] initWithObjects:
						@"I'm ok",
						@"I will call later",
						@"Please call me asap",
						@"I will be late",
						@"Problems Resolved",
						@"Medical Alert",
						@"Fire Alert",
						@"Family Emergency",
						@"Weather Alert ",
						@"Security Problem",
						@"Enter your message",nil];		
		//arrayCheckin = [[NSMutableArray alloc] initWithObjects:@"I'm ok",@"Enter your message",nil];
		[arrayCheckin writeToFile:file atomically:YES];
		NSLog(@"khong ton tai file");
		[[NSFileManager defaultManager] createFileAtPath:fileCount contents:nil attributes:nil];
		countArray = [[NSMutableArray alloc] initWithObjects:@"0",@"0",@"0",@"0",@"0",@"0",@"0",@"0",@"0",@"0",@"0",nil];
		[countArray writeToFile:fileCount atomically:YES];
		
	}
	tableViewCheckIn = [[UITableView alloc] initWithFrame:CGRectMake(10, 15, 280, 320) style:UITableViewStyleGrouped];
	tableViewCheckIn.delegate = self;
	tableViewCheckIn.dataSource = self;	
	
	///
	flag = 2;
	[restConnection getPath:[NSString stringWithFormat:@"/groups?phoneId=%d&token=%@&format=json",appDelegate.phoneID,appDelegate.apiKey] withOptions:nil];
	NSLog(@"get contact view did load");	
	///
	//countDownTimer=[NSTimer scheduledTimerWithTimeInterval:0.5 target:self selector:@selector(checkTextLen) userInfo:nil repeats:YES];

}
//function catch slide event
- (void) cancelled:(SlideToCancelViewController*)sender {
	if(sender == slideCheckin){
		appDelegate.uploader.autoUpload = YES;
		CaptorView *captor = [[CaptorView alloc] init] ;
		captor.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
		captor.isCheckIn=TRUE;
		[self presentModalViewController:captor animated:YES];
		
		slideCheckin.enabled = YES;
		NSLog(@"slide");
		if (tvTextMessage.hidden) 
		{
			NSLog(@"khong lam j");
		}
		else 
		{
			newFlag = 1;
		}

	}	
}

- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    //NSLog(@"memory warning");
    // Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
    [super viewDidUnload];
    // Release any retained subviews of the main view.
    // e.g. self.myOutlet = nil;
}

- (void)dealloc {
	[groupArray release];
	[typeArray release];
	[actionSheet1 release];
	[tableViewCheckIn release];
	[messageBackground release];
	[arrayCheckin release];
	[restConnection release];
	[lblStatusUpload release];
	[actChecking release];
	[lblGetMessage release];
	[scrollView release];
	[tvTextMessage release];
	[lblContact release];
    [super dealloc];
}

#pragma mark textviewdelegate

- (void)textViewDidChange:(UITextView *)textView{
	if (textView.text.length >=95) {
		textView.text = [tvTextMessage.text substringToIndex:95];
		UIAlertView *alert =[[UIAlertView alloc] initWithTitle:nil 
													   message:NSLocalizedString(@"messageLimit",@"")
													  delegate:nil
											 cancelButtonTitle:@"OK"
											 otherButtonTitles:nil];
		[alert show];
		[alert release];
	}
}

#pragma mark Actionsheet
//get contact to send checkin
- (IBAction)GetContact {
	editIndex = 1;
	/*
	UIActionSheet *actionSheet1 = [[UIActionSheet alloc] initWithTitle:@""
															  delegate:self 
													 cancelButtonTitle:nil
												destructiveButtonTitle:nil 
													 otherButtonTitles:@"Family",@"Friends",
	 @"Neighborhood Watch",@"Group A",@"Group B",@"All Groups",@"Single Contact",nil];
	 */
	actionSheet1.actionSheetStyle = UIActionSheetStyleBlackOpaque;
	[actionSheet1 showInView:appDelegate.window];
	//[actionSheet1 release];
}	
//get mesage checkin
- (IBAction)getMessageCheckIn {
	/*
	tableViewCheckIn = [[UITableView alloc] initWithFrame:CGRectMake(10, 15, 280, 320) style:UITableViewStyleGrouped];
		tableViewCheckIn.delegate = self;
	tableViewCheckIn.dataSource = self;
	 */
//	tableViewCheckIn.backgroundColor = [UIColor clearColor];
	messageBackground = [[UIView alloc] initWithFrame:CGRectMake(10, 50, 300, 360)];
	messageBackground.backgroundColor = [UIColor blackColor];
	[messageBackground.layer setCornerRadius:15];
	[messageBackground addSubview:tableViewCheckIn];
	[self.view addSubview:messageBackground];
	
}

#pragma mark -
#pragma mark Table view data source
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	NSString *data = [arrayCheckin objectAtIndex:indexPath.row];
	lblGetMessage.text = data;
	tvTextMessage.text = data;	
	if (indexPath.row==([arrayCheckin count]-1)) 
	{
		tvTextMessage.hidden = NO;
		tvTextMessage.text = @"";
		//countDownTimer=[NSTimer scheduledTimerWithTimeInterval:0.2 target:self selector:@selector(checkTextLen) userInfo:nil repeats:YES];

	}else 
	{
		//NSLog(@" array count : %@",countArray);
		tvTextMessage.hidden = YES;
		NSInteger i = [[countArray objectAtIndex:[indexPath row]]intValue];
		i++;
		NSString *count =[[NSString alloc] initWithFormat:@"%d",i];
		[countArray  replaceObjectAtIndex:[indexPath row] withObject:count];
		NSLog(@" array count : %@",countArray);
		
		NSString *fileCount=[DOCUMENTS_FOLDER stringByAppendingPathComponent:@"count.plist"];
		[countArray writeToFile:fileCount atomically:YES];
		[count release];

	}
	[tableViewCheckIn removeFromSuperview];	
	[messageBackground removeFromSuperview];
	
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
	
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    // Return the number of rows in the section.
	return [arrayCheckin count];
}	

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableViewCheckIn dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:nil] autorelease];
    }
    
    // Configure the cell...
	NSString *message = [NSString stringWithFormat:@"%@",[arrayCheckin objectAtIndex:indexPath.row]];
	cell.textLabel.text = message;
	
	if (([message isEqual:@"Medical Alert"]) || ([message isEqual:@"Fire Alert"]) ||([message isEqual:@"Family Emergency"]) ||([message isEqual:@"Weather Alert "]) ||([message isEqual:@"Security Problem"]))
	{
	cell.textLabel.textColor = [UIColor redColor];	
	}
	else 
		if ([message isEqual:@"Enter your message" ])
	{
		cell.textLabel.textColor =[UIColor colorWithRed:1.0/255 green:220.0/255 blue:100.0/255 alpha:1.0]	;
		//@"Problems Resolved"
	}
	else 
	if (([message isEqual:@"I'm ok" ]) ||([message isEqual:@"I will be late" ]) ||([message isEqual:@"I will call later" ]) ||([message isEqual:@"Please call me asap" ] )||([message isEqual:@"Problems Resolved" ]) )
	{
		cell.textLabel.textColor =[UIColor colorWithRed:1.0/255 green:1.0/255 blue:1.0/255 alpha:1.0]	;
		}
	else 
	{
		cell.textLabel.textColor =[UIColor colorWithRed:1.0/255 green:100.0/255 blue:100.0/255 alpha:1.0]	;
	}
/*
 @"I'm ok",
 @"I will be late",
 @"I will call later",
 @"Please call me asap",
 
 */

	
	return cell;
	
}

#pragma mark -
#pragma mark actionSheetDelegate

-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex{
	if (buttonIndex != [actionSheet cancelButtonIndex]) {
		NSString *value;
		if(editIndex==1)
		{
			NSLog(@" button index : %d",buttonIndex);
			NSLog(@" group count : %d",[groupArray count]);
			if (buttonIndex == ([groupArray count] )) 
			{
				lblContact.text =	@"All Groups";
				[appDelegate.settingArray setObject:@"0" forKey:CK_CheckingIn];

			
			}
			else
			if (buttonIndex == [groupArray count] +1)
			{
				[appDelegate.settingArray setObject:@"-1" forKey:CK_CheckingIn];
				picker = [[ABPeoplePickerNavigationController alloc] init];
				NSLog(@"pickr alloc + init");
				picker.peoplePickerDelegate = self;
				[self presentModalViewController:picker animated:YES];
				[picker release];
			
			
			}
			else
			{
					
			NSInteger i = buttonIndex ;
				NSLog(@" i = %d",i);
			lblContact.text = [groupArray objectAtIndex:i];
				if (i < [groupArray  count])
				{
					[appDelegate.settingArray setObject:[typeArray objectAtIndex:i] forKey:CK_CheckingIn];

				}
							}
			NSLog(@"  setting array :%@:",[appDelegate.settingArray objectForKey:CK_CheckingIn])	;	

			/*
			switch (buttonIndex) {
				case 0:
					value=@"0";
					lblContact.text=@"Family";
					break;
				case 1:
					value=@"1";
					lblContact.text=@"Friends";
					break;
				case 2:
					value=@"2";
					lblContact.text=@"Neighborhood Watch";
					break;
				case 3:
					value=@"3";
					lblContact.text=@"Group A";
					break;
				case 4:
					value=@"4";
					lblContact.text=@"Group B";
					break;
				case 5:
					value=@"5";
					lblContact.text=@"All Groups";
					break;
				case 6:
					value = @"6";
					picker = [[ABPeoplePickerNavigationController alloc] init];
					picker.peoplePickerDelegate = self;
					[self presentModalViewController:picker animated:YES];
					[picker release];
					break;
				default:
					value=@"0";
					break;
			}
			[appDelegate.settingArray setObject:value forKey:CK_CheckingIn];
			 */
		}
			
	}
}

#pragma mark  dismisAlertview
- (void) dimisAlert:(UIAlertView *)alertView {
	[alertView dismissWithClickedButtonIndex:0 animated:TRUE];
}

#pragma mark IBAction

- (IBAction)textFieldDoneEditing:(id)sender {
	[sender resignFirstResponder];	
}

- (IBAction)backgroundTap:(id)sender {
	//[test resignFirstResponder];
	[tvTextMessage resignFirstResponder];	
}

- (IBAction)CheckingNow {
	/*
	if (countDownTimer != nil) 
	{
		[countDownTimer invalidate];
		countDownTimer = nil;

	}
	else {
		NSLog(@" count timer = nil");
	}
*/
	
	btnGetcontact.enabled = FALSE;
	btnGetMessage.enabled = FALSE;
	btnCheck.enabled = FALSE;
	btnCancel.enabled = FALSE;
	tvTextMessage.userInteractionEnabled = FALSE;	
	
	if ([[tvTextMessage.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]] isEqualToString:@""]) {
		UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error"
														message:NSLocalizedString(@"checkinWithOutMessage",@"")
													   delegate:self
											  cancelButtonTitle:@"Ok"
											  otherButtonTitles:nil];
		[alert show];
		[self performSelector:@selector(dimisAlert:) withObject:alert afterDelay:2];
		[alert release];
		btnCheck.enabled = TRUE;
		btnCancel.enabled = TRUE;
		btnGetMessage.enabled = TRUE;
		btnGetcontact.enabled = TRUE;
		tvTextMessage.userInteractionEnabled = TRUE;
		return;
		
	}
	else {
		lblGetMessage.text = tvTextMessage.text;
		[self sendCheckin];
	}
		
	actChecking.hidden = NO;
	[actChecking startAnimating];
	
}
-(IBAction)cancelAlert:(id)sender{
	[self.navigationController popViewControllerAnimated:YES];
	//[self.navigationController ];
}
- (IBAction)ClearMessage {
	tvTextMessage.text = @"";
	lblContact.text = @"Family";
	lblGetMessage.text = @"Enter your message";
	tvTextMessage.hidden = NO;
	tvTextMessage.userInteractionEnabled = YES;
}

#pragma mark PeoplePickerDelegate
- (BOOL)peoplePickerNavigationController:(ABPeoplePickerNavigationController *)peoplePicker shouldContinueAfterSelectingPerson:(ABRecordRef)person{
	ABMultiValueRef multi = ABRecordCopyValue(person, kABPersonPhoneProperty);
	if(ABMultiValueGetCount(multi)>0)
	{
		NSString *phoneNumber = (NSString*)ABMultiValueCopyValueAtIndex(multi, 0);
		lblContact.text = phoneNumber;
		[phoneNumber release];
	}
	else {
		lblContact.text= @""; 
	}
	CFRelease(multi);
	[self dismissModalViewControllerAnimated:YES];
	return NO;
}

- (void)peoplePickerNavigationControllerDidCancel:(ABPeoplePickerNavigationController *)peoplePicker{
	[self dismissModalViewControllerAnimated:YES];
}

- (BOOL)peoplePickerNavigationController:(ABPeoplePickerNavigationController *)peoplePicker shouldContinueAfterSelectingPerson:(ABRecordRef)person property:(ABPropertyID)property identifier:(ABMultiValueIdentifier)identifier{
    return NO;
}

#pragma mark -
#pragma mark finishRequest

- (void)finishRequest:(NSDictionary *)arrayData andRestConnection:(id)connector{
	if (flag ==1) 
	{
		
	
	NSLog(@" %@ ",arrayData);
	btnCheck.enabled = TRUE;
	btnCancel.enabled = TRUE;
	btnGetMessage.enabled = TRUE;
	btnGetcontact.enabled = TRUE;
	tvTextMessage.userInteractionEnabled = TRUE;
	
	lblStatusUpload.hidden = YES;

		if (newFlag == 1) 
		{
			newFlag = 2;
		}
		else
		{
		tvTextMessage.hidden = YES;
		tvTextMessage.text = @"I'm ok";
		tvTextMessage.userInteractionEnabled = YES;
		lblGetMessage.text =  @"I'm ok";
		}
		[tableViewCheckIn reloadData];
	
	if ([[[arrayData objectForKey:@"response"] objectForKey:@"success"] isEqualToString:@"true"]) {
		UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Success"
														message:NSLocalizedString(@"chekin_success",@"")
													   delegate:self
											  cancelButtonTitle:@"Ok"
											  otherButtonTitles:nil];
		[alert show];
		[self performSelector:@selector(dimisAlert:) withObject:alert afterDelay:2];
		[alert release];
		appDelegate.uploader.isSendAlert = NO;
		
	}
	else {
		UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error"
														message:@"Error with send sms"
													   delegate:self
											  cancelButtonTitle:@"Ok"
											  otherButtonTitles:nil];
		[alert show];
		[self performSelector:@selector(dimisAlert:) withObject:alert afterDelay:2];
		[alert release];		
	}
	[actChecking stopAnimating];
	actChecking.hidden = YES;
	}
	else 
	if (flag == 2) 
	{
		groupArray = [[NSMutableArray alloc ] init];
		typeArray = [[NSMutableArray alloc ] init];
		NSLog(@" get group : %@",arrayData);
		if ([[[arrayData objectForKey:@"response"] objectForKey:@"success"] isEqualToString:@"true"]) 
		{
			///
			actionSheet1 = [[UIActionSheet alloc] initWithTitle:@""
																	  delegate:self 
															 cancelButtonTitle:nil
														destructiveButtonTitle:nil 
															 otherButtonTitles:nil];
			
			////
			
			
			NSDictionary *data = [[arrayData objectForKey:@"response"] objectForKey:@"data"];
			for(NSDictionary *dict in data)
			{
				[actionSheet1 addButtonWithTitle:[dict objectForKey:@"name"]];
				[groupArray  addObject:[dict objectForKey:@"name"]];
				[typeArray addObject:[dict objectForKey:@"id"]];
				
				if ([[dict objectForKey:@"name"] isEqualToString:@"Family"]) 
				{

					NSLog(@"set family alert");
					[appDelegate.settingArray setObject:[dict objectForKey:@"id"] forKey:CK_CheckingIn];
				}
				
			//	NSInteger type = [[dict objectForKey:@"type"] intValue];
				/*
				if( type == [[appDelegate.settingArray objectForKey:ST_SendToAlert] intValue])
				{
					lblContact.text = [dict objectForKey:@"name"];
				}
				 */
			}
			[actionSheet1 addButtonWithTitle:@"All Groups"];
			[actionSheet1 addButtonWithTitle:@"Single Contact"];
			
		}else
		{
			NSLog(@"getcontact error");
		}
		
	}

}

#pragma mark -
#pragma mark connectionFail

- (void)cantConnection:(NSError *)error andRestConnection:(id)connector{
	btnCheck.enabled = TRUE;
	btnCancel.enabled = TRUE;
	btnGetMessage.enabled = TRUE;
	btnGetcontact.enabled = TRUE;
	tvTextMessage.userInteractionEnabled = TRUE;	
	
	[actChecking stopAnimating];
	actChecking.hidden = YES;
	alertView();
	[appDelegate.statusView hideStatus];
}
//function send checkin to server
- (void)sendCheckin {
	newFlag = 2;
	lblStatusUpload.text = @"Checking In...";
	lblStatusUpload.hidden = NO;
	
	NSString *message = [[NSString alloc ] initWithFormat:@"%@",tvTextMessage.text];

	if ((tvTextMessage.hidden == NO) && (![ message isEqualToString:@""]))
	{
		NSString *file=[DOCUMENTS_FOLDER stringByAppendingPathComponent:@"message.plist"];
		if ([arrayCheckin count ] == 11 ) 
		{
			//[arrayCheckin removeObjectAtIndex:[arrayCheckin count]-2];
			NSInteger min = [[countArray objectAtIndex:1] intValue];
			NSInteger min_index =8 ;
			for( NSInteger i =1;i<10; i++)
			{
				if (min >= [[countArray objectAtIndex:i] intValue]) 
				{
					min_index = i;
					min =[[countArray objectAtIndex:i] intValue];
					//NSLog(@"min index : %d",min_index);
				}
				else 
				{
					//NSLog(@"trong vong for");
				}

				
				  
				
			}
			[arrayCheckin replaceObjectAtIndex:min_index withObject:message];
			NSLog(@"replace count");
			NSLog(@"min index : %d",min_index);
			[countArray replaceObjectAtIndex:min_index withObject:@"1"];
			NSString *fileCount=[DOCUMENTS_FOLDER stringByAppendingPathComponent:@"count.plist"];

			[countArray writeToFile:fileCount atomically:YES];
			
			NSLog(@" array data : %@",arrayCheckin);
			NSLog(@" array count : %@",countArray);
			
		}
		[arrayCheckin writeToFile:file atomically:YES];

	}
	[message release];
	tvTextMessage.hidden = YES;
		
	NSArray *key = [NSArray arrayWithObjects:@"token",@"phoneId",@"type",@"toGroup",@"singleContact",@"message",nil];
	NSArray *obj = [NSArray arrayWithObjects:appDelegate.apiKey,[NSString stringWithFormat:@"%d",appDelegate.phoneID],@"2",
					[appDelegate.settingArray objectForKey:CK_CheckingIn],lblContact.text,
					tvTextMessage.text,nil];
	NSDictionary *param =[NSDictionary dictionaryWithObjects:obj forKeys:key];
	
	
	flag = 1;
	[restConnection postPath:[NSString stringWithFormat:@"/alert?latitude=%@&longtitude=%@&format=json",appDelegate.latitudeString,appDelegate.longitudeString] withOptions:param];
}

#pragma mark -
#pragma mark UploaderDelegate
- (void)uploadFinish {
	
}

- (void)requestUploadIdFinish:(NSInteger)uploadId {

}

@end
