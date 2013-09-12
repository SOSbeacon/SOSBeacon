//
//  TableGroup.h
//  SOSBEACON
//
//  Created by Tran Ngoc Anh on 14/06/2010.
//  Copyright 2010 CNC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RestConnection.h"
#import "SOSBEACONAppDelegate.h"
#import "TablePersonalDetail.h"
#import "TableGroups.h"

typedef enum {
    AlertViewTagNone = 0,
    AlertViewTagSave = 1,
    AlertViewTagSearch = 2
}AlertViewTag;

@interface TableGroup : UITableViewController <RestConnectionDelegate,UIAlertViewDelegate> {
	SOSBEACONAppDelegate *appDelegate;
	TablePersonalDetail *personalDetail;
	

	NSMutableArray *arrayContacts;
	NSArray *arrayContactsOrigin;
	
	RestConnection *rest;
	NSInteger groupID;
	NSString *groupName;
	NSInteger selectRow;
	BOOL isUpdate;
	UIActivityIndicatorView *actContact;
	Personal *personal1;
	BOOL isUpdateToServer;
	
	BOOL isEdited;
	id parentController;
	NSInteger requestServer;
    BOOL hasJoinFeature;
    BOOL isSearching;
    NSString *keyWord;
	UITextField *joinGroupNameTextField;    
}

@property (nonatomic, retain) TablePersonalDetail *personalDetail;
@property (nonatomic, retain) RestConnection *rest;
@property (nonatomic, retain) NSMutableArray *arrayContacts;
@property (nonatomic) NSInteger groupID;
@property (nonatomic, retain) NSString *groupName;
@property (nonatomic) NSInteger selectRow;
@property (nonatomic) BOOL isUpdate;
@property (nonatomic) BOOL isEdited;
@property (nonatomic, assign) id parentController;
@property (nonatomic) BOOL hasJoinFeature;
@property (nonatomic, retain) NSString *keyWord;
@property (nonatomic, retain) NSArray *arrayContactsOrigin;
 
-(void)displayButtonAdd;
-(void)addClick;
-(IBAction)cancel:(id)sender;
- (void)saveContactToServer;
- (void)updateFirstContact;
- (void)checkEditContact;
- (void)joinButtonPressed;
- (NSString *)groupNameForPersonal:(Personal *)person;

@end
