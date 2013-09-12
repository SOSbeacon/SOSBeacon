//
//  GroupsListResultViewController.m
//  SOSBEACON
//
//  Created by Thao Nguyen Huy on 8/17/12.
//  Copyright (c) 2012 CNC Software. All rights reserved.
//

#import "GroupsListResultViewController.h"
#import "Personal.h"
#import "TableGroup.h"

@implementation GroupsListResultViewController

@synthesize arrayGroups;
@synthesize keyWord;
@synthesize neighborhoodId;
@synthesize tableGroup;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)didReceiveMemoryWarning
{
    // Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
    
    // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
    [super viewDidLoad];
	rest = [[RestConnection alloc] initWithBaseURL:SERVER_URL];
	rest.delegate = self;
	appDelegate = (SOSBEACONAppDelegate*)[[UIApplication sharedApplication] delegate];
	actGroup = [[UIActivityIndicatorView alloc] init];
	actGroup.frame = CGRectMake(140, 130, 30, 30);
	actGroup.activityIndicatorViewStyle = 2;
    actGroup.hidden = YES;
    
	UIBarButtonItem *backButton = [[UIBarButtonItem alloc] initWithTitle:@"Back" style:UIBarButtonItemStyleDone target:nil action:nil];
	self.navigationItem.backBarButtonItem = backButton;
	[backButton release];
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    tableView1 = nil;
}

- (void)dealloc {
    self.tableGroup = nil;
    self.keyWord = nil;
    self.arrayGroups = nil;
    [super dealloc];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    // Return YES for supported orientations
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

#pragma mark -
#pragma mark - Action methods
- (IBAction)joinButtonPressed:(id)sender {
    BOOL hasJoinedGroup = NO;
    for (NSDictionary *group in arrayGroups) {
        if (![[group objectForKey:@"joid_id"] isEqual:[NSNull null]]) {
            hasJoinedGroup = YES;
            break;
        }
    }
    UIButton *button = (UIButton *)sender;
    NSDictionary *oneGroup = [arrayGroups objectAtIndex:button.tag];
    NSString *message = @"Are you sure you want to join this group?";
    if (![[oneGroup objectForKey:@"joid_id"] isEqual:[NSNull null]]) {
        message = @"Are you sure you want to unjoin this group?";
    }
    BOOL joining = [[oneGroup objectForKey:@"joid_id"] isEqual:[NSNull null]];
    if (hasJoinedGroup && joining) {
        message = @"You have to unjoin current joined group if you want to join to other group.";
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:nil message:message delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        [alertView show];
        [alertView release];
    }
    else {
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:nil message:message delegate:self cancelButtonTitle:@"YES" otherButtonTitles:@"NO", nil];
        alertView.tag = button.tag;
        alertType = AlertTypeRequest;
        [alertView show];
        [alertView release];        
    }
}

#pragma mark -
#pragma mark - UIAlertViewDelegate
- (void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex {
    if (buttonIndex == 0) {
        if (alertType == AlertTypeRequest) { // alertType request
            NSDictionary *oneGroup = [arrayGroups objectAtIndex:alertView.tag];
            actGroup.hidden = NO;
            [actGroup startAnimating];
            if ([[oneGroup objectForKey:@"joid_id"] isEqual:[NSNull null]]) { // Join now
                requestType = RequestTypeJoin;
                NSArray *keys = [NSArray arrayWithObjects:@"_method", @"format", @"phone_id", @"token", @"groupId", @"joinGroupId", @"joinContactId", nil];
                NSArray *values = [NSArray arrayWithObjects:@"post", @"json", [NSString stringWithFormat:@"%d", appDelegate.phoneID], appDelegate.apiKey, [NSString stringWithFormat:@"%d", self.neighborhoodId], [oneGroup objectForKey:@"group_id"], [oneGroup objectForKey:@"contact_id"], nil];
                NSDictionary *options = [NSDictionary dictionaryWithObjects:values forKeys:keys];
                NSLog(@"params: %@", options);
                [rest postPath:@"/join-group" withOptions:options];            
            }
            else { // Left now
                requestType = RequestTypeUnjoin;
                NSArray *keys = [NSArray arrayWithObjects:@"_method", @"format", @"phone_id", @"token", @"joinGroupId", nil];
                NSArray *values = [NSArray arrayWithObjects:@"delete", @"json", [NSString stringWithFormat:@"%d", appDelegate.phoneID], appDelegate.apiKey, [oneGroup objectForKey:@"group_id"], nil];
                NSDictionary *options = [NSDictionary dictionaryWithObjects:values forKeys:keys];
                NSLog(@"params: %@", options);
                NSString *requestPath = [NSString stringWithFormat:@"/join-group/%@", [oneGroup objectForKey:@"joid_id"]];
                NSLog(@"request path: %@", requestPath);
                [rest postPath:requestPath withOptions:options];
            }            
        }
        else
        if (alertType == AlertTypeBack) { // alertType back
            [self.navigationController popViewControllerAnimated:YES];
        }
    }
    alertType = AlertTypeNone;
}

#pragma mark -
#pragma mark Table view data source
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    // Return the number of sections.
	[tableView addSubview:actGroup];
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    // Return the number of rows in the section.
    if(section==0) 
		return [arrayGroups count];
	else 
		return 1;
}	

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *CellIdentifier = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        cell = [[[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier] autorelease];
    }
    
    // Configure the cell...
    NSDictionary *oneGroup = [arrayGroups objectAtIndex:indexPath.row];
    cell.textLabel.text = [oneGroup objectForKey:@"group_name"];
    UIImage *image = [UIImage imageNamed:@"button_up.png"];
    UIImage *highlightedImage = [UIImage imageNamed:@"button_down.png"];
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.tag = indexPath.row;
    button.frame = CGRectMake(0, 0, image.size.width + 5, image.size.height);
    [button setBackgroundImage:highlightedImage forState:UIControlStateHighlighted];
    [button setBackgroundImage:image forState:UIControlStateNormal];
    NSString *title = @"Unjoin";
    if ([[oneGroup objectForKey:@"joid_id"] isEqual:[NSNull null]]) {
        title = @"Join";
    }
    [button setTitle:title forState:UIControlStateNormal];
    [button addTarget:self action:@selector(joinButtonPressed:)
     forControlEvents:UIControlEventTouchUpInside];
    button.titleLabel.font = [UIFont systemFontOfSize:15];
    cell.accessoryView = button;
	return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    TablePersonalDetail* personalDetail = [[TablePersonalDetail alloc] initWithStyle:UITableViewStyleGrouped];
    NSDictionary *oneGroup = [arrayGroups objectAtIndex:indexPath.row];
    Personal* personal1 = [[Personal alloc] init];
    personal1.contactID = [[oneGroup objectForKey:@"group_id"] intValue];
    personal1.contactName = [oneGroup objectForKey:@"group_name"];
    
    if (![[oneGroup objectForKey:@"phone_number"] isEqual:[NSNull null]])
    {
        personal1.textphone = [oneGroup objectForKey:@"phone_number"];
    }else {
        personal1.textphone = [oneGroup objectForKey:@""];
    }
    
    if (![[oneGroup objectForKey:@"contact_email"] isEqual:[NSNull null]]) {
        personal1.email = [oneGroup objectForKey:@"contact_email"];
    }else {
        personal1.email = [oneGroup objectForKey:@""];
    }    
    if (![[oneGroup objectForKey:@"voicePhone"] isEqual:[NSNull null]])
    {
        personal1.voidphone = [oneGroup objectForKey:@"voicePhone"];
    }else {
        personal1.voidphone = [oneGroup objectForKey:@""];
    }
    personal1.typeContact = YES; 
    personalDetail.personal = personal1;
    [personal1 release];
    personalDetail.title = [oneGroup objectForKey:@"group_name"];
    personalDetail.groupName = [oneGroup objectForKey:@"group_name"];;
    personalDetail.readOnly = YES;
    personalDetail.personalIndex = -2;
    [self.navigationController pushViewController:personalDetail animated:YES];
    [personalDetail release];
	[tableView deselectRowAtIndexPath:indexPath animated:YES];    
}

- (void) DimisAlertView:(UIAlertView*)alertView {
	[alertView dismissWithClickedButtonIndex:0 animated:TRUE];
}

#pragma mark -
#pragma mark - RestConnectionDelegate
- (void)finishRequest:(NSDictionary*)arrayData andRestConnection:(id)connector {
    NSLog(@"array data: %@", arrayData);
	actGroup.hidden = YES;
	[actGroup stopAnimating];
    if (requestType == RequestTypeJoin || requestType == RequestTypeUnjoin) {
        NSDictionary *response = [arrayData objectForKey:@"response"];
        NSString *success = [response objectForKey:@"success"];
        NSString *message = [response objectForKey:@"message"];
        NSLog(@"sucees:%@",success);
        if ([success isEqual:@"true"]) {
            // Add or delete contacts
            NSDictionary *contactsInJoinedGroup = [response objectForKey:@"joinContacts"];
            NSArray *allKeys = [[contactsInJoinedGroup allKeys] sortedArrayUsingSelector:@selector(compare:)];
            for(NSString *personNameID in allKeys)
            {
                // create contact
                Personal *personal1 = [[Personal alloc] init];
                personal1.contactID = [[[contactsInJoinedGroup objectForKey:personNameID] objectForKey:@"id"] intValue];
                personal1.contactName = [[contactsInJoinedGroup objectForKey:personNameID] objectForKey:@"name"];
                if (![[[contactsInJoinedGroup objectForKey:personNameID] objectForKey:@"textPhone"] isEqual:[NSNull null]])
                {
                    personal1.textphone = [[contactsInJoinedGroup objectForKey:personNameID] objectForKey:@"textPhone"];
                } else {
                    personal1.textphone = [[contactsInJoinedGroup objectForKey:personNameID] objectForKey:@""];
                }
                
                if (![[[contactsInJoinedGroup objectForKey:personNameID] objectForKey:@"email"] isEqual:[NSNull null]]) {
                    personal1.email = [[contactsInJoinedGroup objectForKey:personNameID] objectForKey:@"email"];
                }else {
                    personal1.email = [[contactsInJoinedGroup objectForKey:personNameID] objectForKey:@""];
                }
                
                if (![[[contactsInJoinedGroup objectForKey:personNameID] objectForKey:@"voicePhone"] isEqual:[NSNull null]])
                {
                    personal1.voidphone = [[contactsInJoinedGroup objectForKey:personNameID] objectForKey:@"voicePhone"];
                }else {
                    personal1.voidphone = [[contactsInJoinedGroup objectForKey:personNameID] objectForKey:@""];
                }
                
                if ([[[contactsInJoinedGroup objectForKey:personNameID] objectForKey:@"type"] intValue]==1) {
                    personal1.typeContact = YES;
                }
                if (![[[contactsInJoinedGroup objectForKey:personNameID] objectForKey:@"group_id"] isEqual:[NSNull null]]) {
                    personal1.groupID = [[[contactsInJoinedGroup objectForKey:personNameID] objectForKey:@"group_id"] intValue];
                }                    
                if (requestType == RequestTypeJoin) {
                    [self.tableGroup.arrayContacts addObject:personal1];                    
                }
                else {
                    for (Personal *one in tableGroup.arrayContacts) {
                        if (one.contactID == personal1.contactID) {
                            [tableGroup.arrayContacts removeObject:one];
                            break;
                        }
                    }
                }
                [personal1 release]; //notify
            }
            [self.tableGroup.tableView reloadData];
/*            
            actGroup.hidden = NO;
            [actGroup startAnimating];
            NSString *requestPath = [NSString stringWithFormat:@"/join-group/%d?token=%@&groupName=%@&_method=get&format=json", appDelegate.phoneID, appDelegate.apiKey, self.keyWord];
            requestType = RequestTypeSearch;
            [rest getPath:requestPath withOptions:nil];            
*/ 
        }
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:nil 
                                                            message:message 
                                                           delegate:self 
                                                  cancelButtonTitle:@"OK" 
                                                  otherButtonTitles:nil];
        alertType = AlertTypeBack;
        [alertView show];
        [self performSelector:@selector(DimisAlertView:) withObject:alertView afterDelay:CONF_DIALOG_DELAY_TIME];        
        [alertView release];
    }
/*    
    else
    if (requestType == RequestTypeUnjoin) {
        NSDictionary *response = [arrayData objectForKey:@"response"];
        NSString *success = [response objectForKey:@"success"];
        NSString *message = [response objectForKey:@"message"];
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:nil 
                                                            message:message 
                                                           delegate:nil 
                                                  cancelButtonTitle:@"OK" 
                                                  otherButtonTitles:nil];
        [alertView show];
        [alertView release];
        if ([success isEqual:@"true"]) {
            actGroup.hidden = NO;
            [actGroup startAnimating];
            NSString *requestPath = [NSString stringWithFormat:@"/join-group/%d?token=%@&groupName=%@&_method=get&format=json", appDelegate.phoneID, appDelegate.apiKey, self.keyWord];
            NSLog(@"request path: %@", requestPath);
            requestType = RequestTypeSearch;
            [rest getPath:requestPath withOptions:nil];                    
        }
    }
*/ 
    else
    if (requestType == RequestTypeSearch) {
        NSString *success = [arrayData objectForKey:@"success"];
        if ([success isEqual:@"true"]) {
            NSMutableArray *groups = [arrayData objectForKey:@"results"];
            if (groups.count <= 0) {
                NSString *message = [arrayData objectForKey:@"message"];
                UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:nil message:message delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                [alertView show];
                [alertView release];
            }
            else {
                self.arrayGroups = groups;
                [tableView1 reloadData];
            }
        }        
    }
}

- (void)cantConnection:(NSError*)error andRestConnection:(id)connector {
	actGroup.hidden = YES;
	[actGroup stopAnimating];    
}

@end
