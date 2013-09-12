//
//  TableGroup.m
//  SOSBEACON
//
//  Created by Tran Ngoc Anh on 14/06/2010.
//  Copyright 2010 CNC. All rights reserved.
//

#import "TableGroup.h"
#import "TablePersonalDetail.h"
#import "Personal.h"
#import "RestConnection.h"
#import "GroupPersonal.h"
#import "ValidateData.h"
#import "StatusView.h"
#import "AddContactViewController.h"
#import "GroupsListResultViewController.h"

@implementation TableGroup
@synthesize arrayContacts;
@synthesize rest,groupID, groupName;
@synthesize selectRow;
@synthesize isUpdate,isEdited;
@synthesize parentController,personalDetail;
@synthesize hasJoinFeature;
@synthesize keyWord;
@synthesize arrayContactsOrigin;

#pragma mark -
#pragma mark View lifecycle
- (void)viewDidLoad {
	///
	UIBarButtonItem *backButton = [[UIBarButtonItem alloc] 
								   initWithTitle:@"Back" style:UIBarButtonItemStylePlain target:nil action:nil];
	[self.navigationItem setBackBarButtonItem:backButton];
	[backButton release];
	///
    [super viewDidLoad];
	[self displayButtonAdd];
	arrayContacts = [[NSMutableArray alloc] init];
	rest = [[RestConnection alloc] initWithBaseURL:SERVER_URL];	
	rest.delegate=self;

	appDelegate = (SOSBEACONAppDelegate *)[[UIApplication sharedApplication] delegate];
	[rest getPath:[NSString stringWithFormat:@"/contacts?token=%@&groupId=%d&format=json",
				   appDelegate.apiKey,groupID] withOptions:nil];
	isUpdate = YES;
    NSLog(@"request path: %@", [NSString stringWithFormat:@"/contacts?token=%@&groupId=%d&format=json",
                                appDelegate.apiKey,groupID]);
	UIBarButtonItem *cancelButton = [[UIBarButtonItem alloc]
                                     initWithTitle:@"Back"
                                     style:UIBarButtonItemStylePlain
                                     target:self
                                     action:@selector(cancel:)];
    self.navigationItem.leftBarButtonItem = cancelButton;
    [cancelButton release];	
	actContact = [[UIActivityIndicatorView alloc] init];
	actContact.frame = CGRectMake(140, 130, 30, 30);
	actContact.activityIndicatorViewStyle = 2;
	actContact.hidden = NO;
	[actContact startAnimating];
	isUpdateToServer = NO;
	isEdited = NO;
	appDelegate.saveContact = NO;
    isSearching = NO;
}

- (void)viewDidUnload {
    // Relinquish ownership of anything that can be recreated in viewDidLoad or on demand.
    // For example: self.myOutlet = nil;
}

- (void)dealloc {
    self.arrayContactsOrigin = nil;
    self.keyWord = nil;
	[personal1 release];
	[actContact release];
	[rest release];
    self.arrayContacts = nil;
	[groupName release];
    [super dealloc];
}

#pragma mark -
#pragma mark Table view data source
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
	[tableView addSubview:actContact];
    // Return the number of sections.
    if (hasJoinFeature)
        return 1;
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    // Return the number of rows in the section.
    if(section==0) 
	return [arrayContacts count];
	else 
	return 1;
}
/*
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
	return 64;
}
 */
// Customize the appearance of table view cells.
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
	
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier] autorelease];
    }
    // Configure the cell...

	if(indexPath.section==0)
	{
		personal1 = [arrayContacts objectAtIndex:indexPath.row];
		cell.textLabel.text = personal1.contactName;
	}
	
	if (personal1.typeContact) {
		cell.textLabel.textColor = [UIColor redColor];
	}
    else {
        cell.textLabel.textColor = [UIColor blackColor];
    }
	///
	

  //  cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;

    return cell;
}

- (NSString *)tableView:(UITableView *)tableView titleForFooterInSection:(NSInteger)section {
	if (section == 0) {
		return @"Swipe contact to delete";
	} else {
		return @"";
	}
}

#pragma mark -
#pragma mark Table view delegate
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	if(indexPath.section==0)
	{
		//appDelegate.flagforGroup = 1;
		personalDetail = [[TablePersonalDetail alloc] initWithStyle:UITableViewStyleGrouped];
		personalDetail.personal = [arrayContacts objectAtIndex:indexPath.row];
		personalDetail.title = @"Edit Contact";
		personalDetail.personalIndex = indexPath.row;
		personalDetail.tableGroupDetail = self;
        personalDetail.groupID = self.groupID;        
        personalDetail.groupName = [self groupNameForPersonal:personalDetail.personal];
		appDelegate.groupName = groupName;        
        if (hasJoinFeature) {
            if (personalDetail.personal.groupID != self.groupID) {
                personalDetail.otherGroup = YES;
            }
        }
		[self.navigationController pushViewController:personalDetail animated:YES];
		[personalDetail release];
	}
	[tableView deselectRowAtIndexPath:indexPath animated:YES];
}

- (UITableViewCellEditingStyle)tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath {
	personal1 = [arrayContacts objectAtIndex:indexPath.row];
	if (personal1.typeContact || personal1.groupID != self.groupID) {
		return UITableViewCellEditingStyleNone;
	}
	else {
		return UITableViewCellEditingStyleDelete;
	}
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle 
forRowAtIndexPath:(NSIndexPath *)indexPath {
	personal1 = [arrayContacts objectAtIndex:indexPath.row];
    if (!personal1.typeContact) {
		NSUInteger row = [indexPath row];
		//Personal *person = [arrayContacts objectAtIndex:row];
		//NSLog(@"person  ===%@",person);
		[self.arrayContacts removeObjectAtIndex:row];
		[tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] 
						 withRowAnimation:UITableViewRowAnimationFade];
		
		isEdited = YES;
		appDelegate.saveContact = YES;
	}
	else {
		return;
	}
}

#pragma mark -
#pragma mark - Custom methods
- (NSString *)groupNameForPersonal:(Personal *)person {
    for (Personal *onePerson in arrayContacts) {
        if (onePerson.typeContact && onePerson.groupID == person.groupID) {
            NSString *_groupName = [onePerson.contactName substringFromIndex:[onePerson.contactName rangeOfString:@"Neighborhood Watch"].location];
            return [_groupName stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
            break;
        }
    }
    return nil;
}

-(void)displayButtonAdd{
    NSMutableArray *rightButtons = [NSMutableArray array];    
    if (self.hasJoinFeature) {
        UIBarButtonItem *joinButton = [[UIBarButtonItem alloc] initWithTitle:@"Join"
                                                                       style:UIBarButtonItemStyleBordered 
                                                                      target:self
                                                                      action:@selector(joinButtonPressed)];        
        [rightButtons addObject:joinButton];
        [joinButton release];        
    }    
	UIBarButtonItem *addButton = [[UIBarButtonItem alloc] initWithTitle:@"Add"
                                                                  style:UIBarButtonItemStyleBordered 
                                                                 target:self
                                                                 action:@selector(addClick)];    
    [rightButtons addObject:addButton];
    [addButton release];
    self.navigationItem.rightBarButtonItems = rightButtons;
}

-(void)addClick {
	/*personalDetail = [[TablePersonalDetail alloc] initWithStyle:UITableViewStyleGrouped];
	personalDetail.title = @"Add Contact";
	personalDetail.tableGroupDetail=self;
	personalDetail.groupID=groupID;
	[self.navigationController pushViewController:personalDetail animated:YES];
	[personalDetail release];*/
	
	AddContactViewController *addContactViewController = [[AddContactViewController alloc] init];
	appDelegate.addContactViewController = addContactViewController;
	addContactViewController.title = @"Add Contact";
	addContactViewController.groupID = groupID;
    //NSLog(@"fix leak 15.10.2011");
    addContactViewController.groupName = groupName;
	[self.navigationController pushViewController:addContactViewController animated:YES];
	[addContactViewController release];
}

- (void)joinButtonPressed {
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Search Group" 
                                                        message:@"\n\n" 
                                                       delegate:self 
                                              cancelButtonTitle:@"Search" 
                                              otherButtonTitles:@"Cancel", nil];
    alertView.tag = AlertViewTagSearch;
	joinGroupNameTextField = [[UITextField alloc] initWithFrame:CGRectMake(12.0, 50.0, 260.0, 25.0)]; 
	[joinGroupNameTextField setBackgroundColor:[UIColor whiteColor]];
    joinGroupNameTextField.placeholder = @"Enter Group Name";
	[alertView addSubview:joinGroupNameTextField];
	[alertView setTransform:CGAffineTransformMakeTranslation(0.0, 0.0)];
    [alertView show];
    [alertView release];
	[joinGroupNameTextField becomeFirstResponder];    
    [joinGroupNameTextField release];
}

-(IBAction)cancel:(id)sender{
	appDelegate.saveContact = NO;
	[self checkEditContact];
}

- (void)checkEditContact {
	if (isEdited) {
		UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Contact" message:NSLocalizedString(@"ConfimChange",@"") 
													   delegate:self 
											  cancelButtonTitle:@"Yes"
											  otherButtonTitles:@"No",nil];
        alert.tag = AlertViewTagSave;
		[alert show];
		[alert release];
	}else {
		[self.navigationController popViewControllerAnimated:YES];
	}
}

#pragma mark -
#pragma mark - UIAlertViewDelegate
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if (alertView.tag == AlertViewTagSearch) {
        if (buttonIndex == 0) {
            actContact.hidden = NO;
            [actContact startAnimating];
            self.keyWord = joinGroupNameTextField.text;
            isSearching = YES;
            NSString *requestPath = [NSString stringWithFormat:@"/join-group/%d?token=%@&groupName=%@&_method=get&format=json", appDelegate.phoneID, appDelegate.apiKey, self.keyWord];
            NSLog(@"request path: %@", requestPath);
            [rest getPath:requestPath withOptions:nil];            
        }
    }
    else {// tag = AlertViewTagSave
        if (buttonIndex == 0) {
            [self saveContactToServer];
        }
        [self.navigationController popViewControllerAnimated:YES];        
    }
}

- (void)saveContactToServer {
	isUpdateToServer = YES;
	[appDelegate.statusView showStatus:@"Contact to being updated..."];
	//add contact with status delete
	for(Personal *personOrigin in arrayContactsOrigin)
	{
		BOOL isContactDelete = YES;
		for(Personal *person in arrayContacts)
		{
			if(person.contactID == personOrigin.contactID)
			{
				isContactDelete = NO;
				break;
			}
		}
		if(isContactDelete)
		{
			personOrigin.status = CONTACT_STATUS_DELETED;
			[arrayContacts addObject:personOrigin];
		}
	}
	//remove contact normal
	int i=0;
	while (i<[arrayContacts count]) 
	{
		Personal *person = [arrayContacts objectAtIndex:i];
		
		if(person.status == CONTACT_STATUS_NORMAL) {
			[arrayContacts removeObjectAtIndex:i];
		}
		else if(person.typeContact==YES) {
			[arrayContacts removeObjectAtIndex:i];
		}
		else {
			i++;
		}
	}
	[self updateFirstContact];
}

- (void)updateFirstContact {
	if([arrayContacts count]>0)
	{
		Personal *person = [arrayContacts objectAtIndex:0];
		if(person.status==CONTACT_STATUS_MODIFIED)
		{	
			requestServer = 1;
			//NSLog(@"Edit contact");
			NSArray *key = [NSArray arrayWithObjects:@"token",@"name",@"email",@"voicePhone",@"textPhone",nil];
			NSArray *obj = [NSArray arrayWithObjects:appDelegate.apiKey,person.contactName
							,person.email,person.voidphone,person.textphone,nil];
			NSDictionary *p = [NSDictionary dictionaryWithObjects:obj forKeys:key];
			[rest putPath:[NSString stringWithFormat:@"/contacts/%d?format=json",person.contactID] withOptions:p];
		}
		else if(person.status == CONTACT_STATUS_NEW)
		{
			requestServer = 2;
			//NSLog(@"Add contact");
			//send request add new contact
			NSArray *key = [NSArray arrayWithObjects:@"token",@"groupId",@"name",@"email",@"voicePhone",@"textPhone",nil];
			NSArray *obj = [NSArray arrayWithObjects:appDelegate.apiKey,[NSString stringWithFormat:@"%d",groupID],person.contactName
							,person.email,person.voidphone,person.textphone,nil];
			NSDictionary *p = [NSDictionary dictionaryWithObjects:obj forKeys:key];
			[rest postPath:@"/contacts?format=json" withOptions:p];
		}
		else if(person.status == CONTACT_STATUS_DELETED)
		{
			requestServer = 3;
			//NSLog(@"Delete contact");
			//send request delete contact
			[rest deletePath:[NSString stringWithFormat:@"/contacts/%d?token=%@&format=json",person.contactID,appDelegate.apiKey] withOptions:nil];
		}
	}
	else {
        isUpdateToServer = NO;
		[appDelegate.statusView setStatusTitle:@"Contact saved successfully."];
		[appDelegate.statusView performSelector:@selector(hideStatus) withObject:nil afterDelay:1];
		[self.navigationController popViewControllerAnimated:YES];
	}
}

- (void) DimisAlertView:(UIAlertView*)alertView {
	[alertView dismissWithClickedButtonIndex:0 animated:TRUE];
}

#pragma mark -
#pragma mark finishRequest
-(void)finishRequest:(NSDictionary *)arrayData andRestConnection:(id)connector{
    NSLog(@"response data: %@", arrayData);
	actContact.hidden = YES;
	[actContact stopAnimating];
    
    if (isSearching) {
        NSString *success = [arrayData objectForKey:@"success"];
        if ([success isEqual:@"true"]) {
            NSMutableArray *arrayGroups = [arrayData objectForKey:@"results"];
            if (arrayGroups.count <= 0) {
                NSString *message = [arrayData objectForKey:@"message"];
                UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:nil message:message delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                [alertView show];
                [alertView release];
            }
            else {
                GroupsListResultViewController *groupList = [[GroupsListResultViewController alloc] initWithNibName:@"GroupsListResultViewController" bundle:nil];
                groupList.title = @"Join Neighborhood Watch";
                groupList.keyWord = keyWord;
                groupList.arrayGroups = arrayGroups;
                groupList.neighborhoodId = self.groupID;
                groupList.tableGroup = self;
                [self.navigationController pushViewController:groupList animated:YES];
                [groupList release];
            }
        }
        isSearching = NO;
        return;
    }
    
	if(isUpdateToServer)
	{
		[arrayContacts removeObjectAtIndex:0];
		[self updateFirstContact];
		return;
	}

	if (isUpdate) {
		if ([[[arrayData objectForKey:@"response"] objectForKey:@"success"] isEqualToString:@"true"]) {
			NSDictionary *data = [[arrayData objectForKey:@"response"] objectForKey:@"data"];
            self.arrayContacts = [NSMutableArray array];
            NSMutableArray *groupsId = [NSMutableArray array];
            NSMutableArray *tempContacts = [NSMutableArray array];
			if(data)
			{
                NSArray *allKeys = [[data allKeys] sortedArrayUsingSelector:@selector(compare:)];
				for(NSString *personNameID in allKeys)
				{
                    // check group id
                    BOOL existed = NO;
                    for (int i = 0; i < groupsId.count; i++) {
                        if ([[groupsId objectAtIndex:i] isEqual:[[data objectForKey:personNameID] objectForKey:@"group_id"]]) {
                            existed = YES;
                            break;
                        }
                    }
                    if (!existed) {
                        [groupsId addObject:[[data objectForKey:personNameID] objectForKey:@"group_id"]];
                        [tempContacts addObject:[NSMutableArray array]];
                    }
                    
                    // create contact
					personal1 = [[Personal alloc] init];
					personal1.contactID = [[[data objectForKey:personNameID] objectForKey:@"id"] intValue];
					personal1.contactName = [[data objectForKey:personNameID] objectForKey:@"name"];
					if (![[[data objectForKey:personNameID] objectForKey:@"textPhone"] isEqual:[NSNull null]])
					{
						personal1.textphone = [[data objectForKey:personNameID] objectForKey:@"textPhone"];
					}else {
						personal1.textphone = [[data objectForKey:personNameID] objectForKey:@""];
					}
					
					if (![[[data objectForKey:personNameID] objectForKey:@"email"] isEqual:[NSNull null]]) {
						personal1.email = [[data objectForKey:personNameID] objectForKey:@"email"];
					}else {
						personal1.email = [[data objectForKey:personNameID] objectForKey:@""];
					}
					
					if (![[[data objectForKey:personNameID] objectForKey:@"voicePhone"] isEqual:[NSNull null]])
					{
						personal1.voidphone = [[data objectForKey:personNameID] objectForKey:@"voicePhone"];
					}else {
						personal1.voidphone = [[data objectForKey:personNameID] objectForKey:@""];
					}
					
					if ([[[data objectForKey:personNameID] objectForKey:@"type"] intValue]==1) {
						personal1.typeContact = YES;
					}
                    if (![[[data objectForKey:personNameID] objectForKey:@"group_id"] isEqual:[NSNull null]]) {
                        personal1.groupID = [[[data objectForKey:personNameID] objectForKey:@"group_id"] intValue];
                    }                    
                    for (int i = 0; i < groupsId.count; i++) {
                        if ([[[data objectForKey:personNameID] objectForKey:@"group_id"] isEqual:[groupsId objectAtIndex:i]]) {
                            if (personal1.typeContact) {
                                [[tempContacts objectAtIndex:i] insertObject:personal1 atIndex:0];
                            }
                            else {
                                [[tempContacts objectAtIndex:i] addObject:personal1];
                            }
                            break;
                        }
                    }
                    
					[personal1 release]; //notify
					isUpdate=NO;
				}
//                NSLog(@"temp: %@", tempContacts);
                
                for (int i = 0; i < groupsId.count; i++) {
                    [self.arrayContacts addObjectsFromArray:[tempContacts objectAtIndex:i]];
                }
                
				//copy to origin array
                self.arrayContactsOrigin = [NSArray arrayWithArray:arrayContacts];
//                NSLog(@"array contacts: %@", arrayContacts);
				[self.tableView reloadData];
			}
		}
		else {
			//NSLog(@"Error load contact");
		}
        isUpdate = NO;
	}
}

-(void)cantConnection:(NSError *)error andRestConnection:(id)connector{
	actContact.hidden = YES;
	[actContact stopAnimating];
    isUpdate = isUpdateToServer = isSearching = NO;
	alertView();
}

#pragma mark -
#pragma mark Memory management	
- (void)didReceiveMemoryWarning {
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
}

@end

