//
//  BWConnectionManager.m
//  SIPAC
//
//  Created by Jaguar3 on 19/03/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import "BWConnectionManager.h"
#import "SCAppDelegate.h"
#import "MBProgressHUD.h"
#import <SystemConfiguration/SystemConfiguration.h>

@interface BWConnectionManager (){
    NSMutableData *receivedData;
    bool withHud;
    bool activeHud;
    bool showWSResponse;
    NSString *Message;
    UIView *theActualView;
    MBProgressHUD* hud;
}

@end

@implementation BWConnectionManager

@synthesize delegate;

-(id)initWithJSONPost:(NSDictionary*)post withUrl:(NSString*)theURL inWS:(NSString*)wsName delegate:(id)theDelegate message:(NSString*)theMessage withHud:(BOOL)isHudActive requestWithTag:(int)tag ShowResponse:(BOOL)showResponse Resting3g:(BOOL)restring3g{
    
    if ([super init]) {
        
        self.delegate = theDelegate;
        activeHud = NO;
        Message = theMessage;
        withHud = isHudActive;
        self.tag = tag;
        showWSResponse = showResponse;
        
        if ([BWConnectionManager connectedToNetworkRestring3g:restring3g]) {
            
            // Create the request.
            // WSURL is a constant declared on the Prefix.ph file
            
            // create the connection with the request
            // and start loading the data
            NSLog(@"url : %@",[theURL stringByAppendingString:wsName]);
            NSError *parsingError = nil;
            NSData *postData = [NSJSONSerialization dataWithJSONObject:post options:NSJSONWritingPrettyPrinted error:&parsingError];
            NSLog(@"SAConectionManager - JSON postData: %@",[[NSString alloc]initWithData:postData encoding:NSUTF8StringEncoding]);
            NSString *postLength = [NSString stringWithFormat:@"%d", [postData length]];
            NSMutableURLRequest *request = [[NSMutableURLRequest alloc] init];
            [request setCachePolicy:NSURLRequestUseProtocolCachePolicy];
            [request setURL:[NSURL URLWithString:[[theURL stringByAppendingString:wsName] stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]]];
            [request setHTTPMethod:@"POST"];
            [request setTimeoutInterval:30.0];
            [request setValue:postLength forHTTPHeaderField:@"Content-Length"];
            [request setValue:@"application/json" forHTTPHeaderField:@"Content-Type"];
            [request setHTTPBody:postData];
            
            NSURLConnection *theConnection=[[NSURLConnection alloc] initWithRequest:request delegate:self];
            if (theConnection) {
                // Create the NSMutableData to hold the received data.
                // receivedData is an instance variable declared elsewhere.
                receivedData = [NSMutableData data];
            } else {
                // Inform the user that the connection failed.
            }
            
            if (withHud) {
                if (!activeHud) {
                    SCAppDelegate *tmpDel = (SCAppDelegate *)[[UIApplication sharedApplication] delegate];
                    UIWindow *topWindow = [tmpDel window];
                    theActualView = [[topWindow subviews] lastObject];
                    
                    hud = [MBProgressHUD showHUDAddedTo:theActualView animated:YES];
                    hud.labelText = NSLocalizedString(Message, nil);
                    activeHud = YES;
                }
            }
        }else{
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"No tienes conexion a internet o estas en una red 3g/Edge!" delegate:nil cancelButtonTitle:@"Aceptar" otherButtonTitles:nil];
            [alert show];
            if ([self.delegate respondsToSelector: @selector(didFailResponseInConnectionTag:)]){
                [self.delegate didFailResponseInConnectionTag:self.tag];
            }
        }
        
        
    }
    
    return self;
    
}

- (void)progress:(float)value{
    [hud setMode:MBProgressHUDModeDeterminate];
    
    [hud setProgress:value];
}

- (void)connection:(NSURLConnection *)connection didReceiveResponse:(NSURLResponse *)response
{
    // This method is called when the server has determined that it
    // has enough information to create the NSURLResponse.
    
    // It can be called multiple times, for example in the case of a
    // redirect, so each time we reset the data.
    
    // receivedData is an instance variable declared elsewhere.
    [receivedData setLength:0];
    NSLog(@"didReceiveResponse");
}

- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data
{
    // Append the new data to receivedData.
    // receivedData is an instance variable declared elsewhere.
    [receivedData appendData:data];
    
}

- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error
{
    
    // inform the user
    NSLog(@"Connection failed! Error - %@ %@",
          [error localizedDescription],
          [[error userInfo] objectForKey:NSURLErrorFailingURLStringErrorKey]);
    
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"No se puede conectar con el servidor intentelo mas tarde." delegate:nil cancelButtonTitle:@"Aceptar" otherButtonTitles: nil];
    [alert show];
    
    if (withHud) {
        [MBProgressHUD hideHUDForView:theActualView animated:YES];
        activeHud = NO;
    }
    
    if ([self.delegate respondsToSelector: @selector(didFailResponseInConnectionTag:)]){
        [self.delegate didFailResponseInConnectionTag:self.tag];
    }
}

- (void)connectionDidFinishLoading:(NSURLConnection *)connection
{
    // do something with the data
    // receivedData is declared as a method instance elsewhere
    NSLog(@"Succeeded! Received %d bytes of data",[receivedData length]);
    
    NSError *parsingError = nil;
    NSDictionary *dictionary = [NSJSONSerialization JSONObjectWithData:receivedData
                                                               options:NSJSONReadingAllowFragments
                                                                 error:&parsingError];
    
    if (showWSResponse) {
        NSLog(@"WS Response: %@",dictionary);
    }
    
    if (withHud) {
        [MBProgressHUD hideHUDForView:theActualView animated:YES];
        activeHud = NO;
    }
    
    if ([self.delegate respondsToSelector: @selector(didGetResponse:inConnectionTag:)]){
        [self.delegate didGetResponse:dictionary inConnectionTag:self.tag];
    }
    
    
}

+ (BOOL)connectedToNetworkRestring3g:(BOOL)restring{
    //Create zero addy
    struct sockaddr zeroAddress;
    bzero(&zeroAddress, sizeof(zeroAddress));
    zeroAddress.sa_len = sizeof(zeroAddress);
    zeroAddress.sa_family = AF_INET;
    
    //Recover reachability flags
    SCNetworkReachabilityRef defaultRouteReachability = SCNetworkReachabilityCreateWithAddress(NULL, (struct sockaddr*)&zeroAddress);
    SCNetworkReachabilityFlags flags;
    
    BOOL didRetrieveFlags = SCNetworkReachabilityGetFlags(defaultRouteReachability, &flags);
    CFRelease(defaultRouteReachability);
    
    if (!didRetrieveFlags) {
        printf("Error. Could not recover network reachability flags\n");
        return NO;
    }
    
    BOOL isReachable = flags & kSCNetworkFlagsReachable;
    BOOL needsConnection = flags & kSCNetworkFlagsConnectionRequired;
    BOOL isWWAN = ([[NSString stringWithFormat:@"%u",flags] integerValue] == 327683) ? YES : NO;
    
    //flags : 327683  262144 //3g
    //flags : 65538  262144  //wifi
    
    //NSLog(@"flags : %u  %d",flags,kSCNetworkReachabilityFlagsIsWWAN);
    
    if (!isWWAN) NSLog(@"You're on WiFi Connection");
    if (isWWAN) NSLog(@"You're on 3g or Edge Connection");
    
    if (!restring) {
        isWWAN = NO;
    }
    
    return (isReachable && !needsConnection && !isWWAN) ? YES : NO;
    
}

@end
