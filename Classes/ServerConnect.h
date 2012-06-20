//
//  ServerConnect.h
//  SOSBEACON
//
//  Created by Tran Ngoc Anh on 10/06/2010.
//  Copyright 2010 CNC. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ServerConnect : NSObject {
	NSString *apiKey;
	NSString *userName;
	NSString *passWord;
}

@property (nonatomic,retain) NSString *apiKey;
@property (nonatomic,assign) NSString *userName;
@property (nonatomic,assign) NSString *passWord;

- (BOOL)loginUser:(NSString*)username andPassword:(NSString*)password;
- (BOOL)loginUser;
- (NSMutableArray*)getGroups;
- (void)uploadPhoto;
- (void)uploadAudio;

@end
