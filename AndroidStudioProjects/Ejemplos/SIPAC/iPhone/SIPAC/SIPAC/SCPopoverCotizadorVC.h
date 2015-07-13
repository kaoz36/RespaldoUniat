//
//  SCPopoverCotizadorVC.h
//  SIPAC
//
//  Created by MacBook Pro de Hugo on 20/06/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import <UIKit/UIKit.h>

/**
 Class that present a popOver 
 */
@interface SCPopoverCotizadorVC : UIViewController
/**
 Initialize a popOver
 @param title the title of popOver
 @param description the description of the popOver
 @returns a newly initialized object
 */
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil title:(NSString *)theTitle description: (NSString *)theDescription;

@end
