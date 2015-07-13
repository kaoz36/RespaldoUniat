//
//  BWConnectionManager.h
//  SIPAC
//
//  Created by Jaguar3 on 19/03/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import <Foundation/Foundation.h>



@class BWConnectionManager;             //define class, so protocol can see MyClass
@protocol BWConnectionManagerDelegate <NSObject>  //define delegate protocol
@optional
- (void) didGetResponse:(id)response inConnectionTag:(int)tag;  //define delegate method to be implemented within another class
- (void) activeInternetConnection;
- (void) didFailResponseInConnectionTag:(int)tag;
@end //end protocol

@interface BWConnectionManager : NSObject <NSURLConnectionDelegate,NSURLConnectionDataDelegate> {
}

@property (nonatomic, weak) id <BWConnectionManagerDelegate> delegate; //define MyClassDelegate as delegate
@property int tag;

- (id)initWithJSONPost:(NSDictionary*)post withUrl:(NSString*)theURL inWS:(NSString*)wsName delegate:(id)theDelegate message:(NSString*)theMessage withHud:(BOOL)isHudActive requestWithTag:(int)tag ShowResponse:(BOOL)showResponse Resting3g:(BOOL)restring3g;
+ (BOOL)connectedToNetworkRestring3g:(BOOL)restring;
- (void)progress:(float)value;

@end

