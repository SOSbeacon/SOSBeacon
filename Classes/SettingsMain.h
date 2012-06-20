//
//  SettingsMain.h
//  SOSBEACON
//
//  Created by Geoff Heeren on 6/18/11.
//  Copyright 2011 AppTight, Inc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import"SOSBEACONAppDelegate.h";

@interface SettingsMain : UIViewController<UITableViewDelegate,UITableViewDataSource> {

	NSArray *aryDataSource;
	NSInteger flag;
	SOSBEACONAppDelegate *appDelegate;

	
}
@property(nonatomic)NSInteger flag;
@property(retain,nonatomic) IBOutlet UITableView *mainTable;
//-(IBAction)cancel:(id)sender;

@end
