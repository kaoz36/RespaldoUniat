//
//  SCPopoverVCViewController.h
//  SIPAC
//
//  Created by Jaguar3 on 16/05/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import <UIKit/UIKit.h>

@class DYJPopUps;          
/**
define delegate protocol
 */
@protocol SCPopoverVCDelegate <NSObject>  
@optional

- (void)didSelectPopoverRowAtIndex:(NSInteger)index;

@end 

@interface SCPopoverVC : UIViewController <UITableViewDataSource,UITableViewDelegate> {
    id<SCPopoverVCDelegate> delegate;
    
}

@property (nonatomic, assign) id<SCPopoverVCDelegate> delegate;
/**
 Initialize a new cotizacion object
 @param locationArray The array that contain the information pin
 @returns a newly initialized object
 */
- (id)initWithLocationArray:(NSMutableArray*)locationArray;

@end


