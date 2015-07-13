//
//  SCManager.h
//  SIPAC
//
//  Created by Jaguar3 on 05/04/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "MSNavigationPaneViewController.h"
#import "SCPopoverCotizadorVC.h"
#import "SCCotizadorVC.h"
#import "SCWebServicesMgr.h"

/**
 class that manage all the viewController
 */
@interface SCManager : NSObject <BWConnectionManagerDelegate>


+ (SCManager *)sharedInstance;

@property (nonatomic, strong) MSNavigationPaneViewController *paneVC;
@property (nonatomic,strong)  SCPopoverCotizadorVC *popOver;
@property (nonatomic,strong)  SCCotizadorVC *cotizadorVC;


- (UIViewController*)getRootViewController;
- (void)GoToProfile;
- (void)GoToSearch;
- (void)GoToGeolocalization;
- (void)goToQuotation;
- (void)goToProspect;
- (void)ChangeToMasterPane;
- (void)logOut;
- (void)showPaneMenu;

- (void)GoToPopoverCotizador:(NSString*)TheTitle description:(NSString*)TheDescription;
- (void) pulse:(UIView*)theView;

- (void)saveImageFromUrl:(NSString*)url;
- (UIImage*)imageFromFile;

@end
