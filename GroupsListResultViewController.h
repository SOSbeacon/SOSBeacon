//
//  GroupsListResultViewController.h
//  SOSBEACON
//
//  Created by Thao Nguyen Huy on 8/17/12.
//  Copyright (c) 2012 CNC Software. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RestConnection.h"
#import "SOSBEACONAppDelegate.h"
#import "TablePersonalDetail.h"
#import "GroupPersonal.h"

@class TableGroup;

typedef enum {
    RequestTypeSearch = 0,
    RequestTypeJoin,
    RequestTypeUnjoin
}RequestType;

typedef enum {
    AlertTypeNone = 0,
    AlertTypeRequest,
    AlertTypeBack
}AlertType;

@interface GroupsListResultViewController : UIViewController <UITableViewDataSource, UITableViewDelegate, RestConnectionDelegate, UIAlertViewDelegate> {
	SOSBEACONAppDelegate *appDelegate;
	RestConnection *rest;
	UIActivityIndicatorView *actGroup;    
	NSMutableArray *arrayGroups;
	IBOutlet UITableView *tableView1;
    NSString *keyWord;
    RequestType requestType;
    AlertType   alertType;
    NSInteger neighborhoodId;
    TableGroup *tableGroup;
}

@property (nonatomic, retain) NSMutableArray *arrayGroups;
@property (nonatomic, retain) NSString *keyWord;
@property (nonatomic, assign) NSInteger neighborhoodId;
@property (nonatomic, retain) TableGroup *tableGroup;

- (IBAction)joinButtonPressed:(id)sender;

@end
