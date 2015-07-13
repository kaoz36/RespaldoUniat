//
//  MSMasterPaneVC.h
//
//  Created by Rodrigo Galvez on 03/10/13.
//  Copyright (c) 2013-2014 JaguarLabs. All rights reserved.
//


#import <UIKit/UIKit.h>
#import "MSNavigationPaneViewController.h"
/**
 An enumeration of appearance type
 */
typedef NS_ENUM(NSUInteger, MSPaneViewControllerType) {
    MSPaneViewControllerTypeAppearanceNone,/**< An AppearanceNone */
    MSPaneViewControllerTypeAppearanceParallax,/**< An AppearanceParallax */
    MSPaneViewControllerTypeAppearanceZoom,/**< An AppearanceZoom */
    MSPaneViewControllerTypeAppearanceFade,/**< An AppearanceFade */
    MSPaneViewControllerTypeControls,/**< A Controls*/
    MSPaneViewControllerTypeMonospace,/**< A Monospace */
    MSPaneViewControllerTypeCount/**< A Count */
};

/**
 class that present the frameWork that Making animation
 */
@interface SCMasterPaneVC : UITableViewController

@property (nonatomic, assign) MSPaneViewControllerType paneViewControllerType;
@property (nonatomic, strong) MSNavigationPaneViewController *navigationPaneViewController;

- (void)transitionToViewController:(UIViewController*)theViewController;

@end
