//
//  RestConnection.m
//  SOSBEACON
//
//  Created by Tran Ngoc Anh on 14/06/2010.
//  Copyright 2010 CNC. All rights reserved.
//

#import "RestConnection.h"
#import "NSDictionaryParam.h"
#import "JSON.h"
#import "NSStringEscaping.h"

@implementation RestConnection
@synthesize delegate;
@synthesize baseURLString;
@synthesize data1;

- (id)initWithBaseURL:(NSString *)baseURL {
	if(self=[super init])
	{
		baseURLString = baseURL;
	}
	return self;
}

- (void) dealloc
{
	NSLog(@"dealloc connection");
	[baseURLString release];
	[data1 release];	
	[super dealloc];
}

#pragma mark Method
- (void)getPath:(NSString*)path withOptions:(NSDictionary*)options {
	NSURL *url = [NSURL URLWithString:path
						relativeToURL:[NSURL URLWithString:baseURLString]];
	
	NSMutableURLRequest *mutableRequest = [[NSMutableURLRequest alloc] initWithURL:url];

	[mutableRequest setValue:@"application/xhtml+xml;charset=utf-8" forHTTPHeaderField:@"Content-Type"];
	[mutableRequest setValue:@"application/xhtml+xml" forHTTPHeaderField:@"accept"];
	[mutableRequest setHTTPMethod:@"GET"];
 
	urlConnection = [[NSURLConnection alloc] initWithRequest:mutableRequest delegate:self];
	[mutableRequest release];
	
}

- (void)postPath:(NSString*)path withOptions:(NSDictionary*)options {
	NSURL *url = [NSURL URLWithString:path
						relativeToURL:[NSURL URLWithString:baseURLString]];
	NSMutableURLRequest *mutableRequest = [[NSMutableURLRequest alloc] initWithURL:url];
	
	[mutableRequest setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
	[mutableRequest setHTTPMethod:@"POST"];
	NSData *bodyData = [[options toQueryString] dataUsingEncoding:NSUTF8StringEncoding allowLossyConversion:YES];
	[mutableRequest setHTTPBody:bodyData];
	
	NSLog(@"POST param: %@",[options toQueryString]);
	printf("\n \n");
	urlConnection = [[NSURLConnection alloc] initWithRequest:mutableRequest delegate:self];
	[mutableRequest release];
	
}

- (void)putPath:(NSString*)path withOptions:(NSDictionary*)options {
	NSURL *url = [NSURL URLWithString:path
						relativeToURL:[NSURL URLWithString:baseURLString]];
	
	NSMutableURLRequest *mutableRequest = [[NSMutableURLRequest alloc] initWithURL:url];	
	
	[mutableRequest setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
	[mutableRequest setHTTPMethod:@"POST"];
	NSData *bodyData = [[options toQueryString] dataUsingEncoding:NSUTF8StringEncoding allowLossyConversion:YES];
	[mutableRequest setHTTPBody:bodyData];
	
	NSLog(@"oquerystring: %@",[options toQueryString]);
	
	urlConnection = [[NSURLConnection alloc] initWithRequest:mutableRequest delegate:self];
	[mutableRequest release];
	
	
}

- (void)deletePath:(NSString*)path withOptions:(NSDictionary*)options {
	NSURL *url = [NSURL URLWithString:path
						relativeToURL:[NSURL URLWithString:baseURLString]];
	
	NSMutableURLRequest *mutableRequest = [[NSMutableURLRequest alloc] initWithURL:url];	
	
	[mutableRequest setValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];	
	[mutableRequest setHTTPMethod:@"DELETE"];
	NSData *bodyData = [[options toQueryString] dataUsingEncoding:NSUTF8StringEncoding allowLossyConversion:YES];
	[mutableRequest setHTTPBody:bodyData];
	
	NSLog(@"toquerystring:  %@",[options toQueryString]);
	
	urlConnection = [[NSURLConnection alloc] initWithRequest:mutableRequest delegate:self];
	[mutableRequest release];
	
}

- (void)uploadPath:(NSString*)path withOptions:(NSDictionary*)options withFilePath:(NSString*)filePath {
	NSData *data = [[NSData alloc] initWithContentsOfFile:filePath];
	[self uploadPath:path withOptions:options withFileData:data];
}

- (void)uploadPath:(NSString*)path withOptions:(NSDictionary*)options withFileData:(NSData*)dataF {
	NSURL *url = [NSURL URLWithString:path
						relativeToURL:[NSURL URLWithString:baseURLString]];
	
	NSMutableURLRequest *mutableRequest = [[NSMutableURLRequest alloc] initWithURL:url];
	
	NSString *boundary = @"--ngocanh";
	
	[mutableRequest setValue:[NSString stringWithFormat:@"multipart/form-data; boundary=%@",boundary] forHTTPHeaderField:@"Content-Type"];
	[mutableRequest setHTTPMethod:@"POST"];
	NSMutableData *postbody = [NSMutableData data];
	//add param
	for(NSString *key in options)
	{
		NSString *value = [options objectForKey:key];
		[postbody appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n",boundary] dataUsingEncoding:NSUTF8StringEncoding]];
		[postbody appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"%@\"\r\n\r\n", key] dataUsingEncoding:NSUTF8StringEncoding]];
		[postbody appendData:[value dataUsingEncoding:NSUTF8StringEncoding]];
	}
	
	[postbody appendData:[[NSString stringWithFormat:@"\r\n--%@\r\n",boundary] dataUsingEncoding:NSUTF8StringEncoding]];
	
	if([[options objectForKey:@"type"] isEqualToString:@"0"])
		[postbody appendData:[[NSString stringWithString:@"Content-Disposition: form-data; name=\"data\"; filename=\"picture.jpg\"\r\n"] dataUsingEncoding:NSUTF8StringEncoding]];
	else
		[postbody appendData:[[NSString stringWithString:@"Content-Disposition: form-data; name=\"data\"; filename=\"sound.caf\"\r\n"] dataUsingEncoding:NSUTF8StringEncoding]];
	
	[postbody appendData:[[NSString stringWithString:@"Content-Type: image/png\r\n\r\n"] dataUsingEncoding:NSUTF8StringEncoding]];
	[postbody appendData:[NSData dataWithData:dataF]];
	[postbody appendData:[[NSString stringWithFormat:@"\r\n--%@--\r\n",boundary] dataUsingEncoding:NSUTF8StringEncoding]];
	//NSLog(@"Upload request:%@",postbody);
	
	[mutableRequest setHTTPBody:postbody];
	
	urlConnection = [[NSURLConnection alloc] initWithRequest:mutableRequest delegate:self];
	[mutableRequest release];
	
}

#pragma mark Connecting
- (NSURLRequest *)connection:(NSURLConnection *)connection willSendRequest:(NSURLRequest *)aRequest redirectResponse:(NSURLResponse *)aResponse;
{
	//NSLog(@"In connection: willSendRequest: %@ redirectResponse: %@", aRequest, aResponse);
	return aRequest;
}

- (void)connection:(NSURLConnection *)connection didReceiveAuthenticationChallenge:(NSURLAuthenticationChallenge *)challenge;
{
	//NSLog(@"In connection: didReceiveAuthenticationChallenge: %@", challenge);
}

- (void)connection:(NSURLConnection *)connection didCancelAuthenticationChallenge:(NSURLAuthenticationChallenge *)challenge;
{
	//NSLog(@"In connection: didCancelAuthenticationChallenge: %@", challenge);
}

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)aResponse;
{
	NSLog(@"Data Size:%i",[data1 length]);
	
	[data1 release];
	self.data1 = [[NSMutableData alloc] init];
	
	
	//	[delegate didReceiveResponse:response];
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)newData;
{
	[data1 appendData:newData];
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection;
{	
	[connection release];
	//NSLog(@"In connectionDidFinishLoading:");
	if(self.delegate!=nil&&[self.delegate respondsToSelector:@selector(finishRequest:andRestConnection:)])
	{
		SBJSON *parser = [SBJSON new];
		NSString *strtmp = [[NSString alloc] initWithData:data1 encoding:NSUTF8StringEncoding];
		//NSLog(@"string : %@",strtmp);
		NSDictionary *array = [parser objectWithString:strtmp];
		[parser release];
		[strtmp release];
		//NSLog(@" %@ ",array);
	//	NSLog(@"array in connectiondidfinishloading");
		[self.delegate finishRequest:array andRestConnection:nil];
	}	
	
}

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error;
{
	if(self.delegate!=nil)	
	{
		if([self.delegate respondsToSelector:@selector(cantConnection:andRestConnection:)])
		{
			[self.delegate cantConnection:error andRestConnection:nil]; 
		}
	}
	if (connection != nil)
	{
		[connection release];
		connection = nil;
	}
}

- (NSCachedURLResponse *)connection:(NSURLConnection *)connection willCacheResponse:(NSCachedURLResponse *)cachedResponse;
{
	//NSLog(@"connection: willCacheResponse:");
	return nil;
}
@end
