//
//  SCManager.m
//  SIPAC
//
//  Created by Jaguar3 on 05/04/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import "SCManager.h"
#import "SCDatabaseManager.h"

#import "SCMainMenuVC.h"
#import "SCLoginVC.h"
#import "SCProfileVC.h"
#import "SCSearchVC.h"
#import "SCMasterPaneVC.h"
#import "SCGeolocalization.h"
#import "SCProspectionVC.h"
#import "SCCotizadorVC.h"
#import "SCPopoverCotizadorVC.h"
#import "Toast+UIView.h"

@interface SCManager (){
    SCLoginVC *loginVC;
    SCProfileVC *profileVC;
    SCSearchVC *searchVC;
    SCProspectionVC *prospectionVC;
   
    NSTimer *timer10min;
}

@end


@implementation SCManager

+ (SCManager *)sharedInstance
{
    static dispatch_once_t pred;
    static SCManager *shared = nil;
    dispatch_once(&pred, ^{
        shared = [[SCManager alloc] init];
        //[shared checkConnectionStatus];
        
    });
    return shared;
}

- (UIViewController*)getRootViewController{
    //NSDictionary *profile = [[SCDatabaseManager sharedInstance] GetProfile];
    UIViewController *theRootViewController;
    //if ([[profile objectForKey:@"logged_user"] isEqualToString:@"0"]) {
        if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
            //Device is ipad
            loginVC = [[SCLoginVC alloc] initWithNibName:@"SCLoginVC" bundle:nil];
        }else{
            //Device is iphone
            loginVC = [[SCLoginVC alloc] initWithNibName:@"SCLoginVCIphone" bundle:nil];
        }
        
        theRootViewController = loginVC;
//    }else{
//        _paneVC = [[MSNavigationPaneViewController alloc] init];
//        SCMasterPaneVC *masterPane = [[SCMasterPaneVC alloc] initWithStyle:UITableViewStylePlain];
//        masterPane.navigationPaneViewController = _paneVC;
//        _paneVC.masterViewController = masterPane;
//        theRootViewController = _paneVC;
//    }
    
    return theRootViewController;
}

- (void)sendLocation{
    [[SCWebServicesMgr sharedInstance] SWSaveConsultLocationWithDelegate:self];
}

- (void)showPaneMenu{
    [self.paneVC setPaneState:MSNavigationPaneStateOpen animated:YES completion:nil];
}

- (void)logOut{
    
    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
        //Device is ipad
        loginVC = [[SCLoginVC alloc] initWithNibName:@"SCLoginVC" bundle:nil];
    }else{
        //Device is iphone
        loginVC = [[SCLoginVC alloc] initWithNibName:@"SCLoginVCIphone" bundle:nil];
    }
    [loginVC setModalTransitionStyle:UIModalTransitionStyleFlipHorizontal];
    
    [_paneVC presentViewController:loginVC animated:YES completion:^(void){}];
    
    //[[SCDatabaseManager sharedInstance] CreateProfileTable];
}

- (void)ChangeToMasterPane{
    if (timer10min != NULL) {
        [timer10min invalidate];
        timer10min = NULL;
    }
    timer10min = [NSTimer scheduledTimerWithTimeInterval:600.0f target:self selector:@selector(sendLocation) userInfo:nil repeats:YES];
    _paneVC = [[MSNavigationPaneViewController alloc] init];
       SCMasterPaneVC *masterPane = [[SCMasterPaneVC alloc] initWithStyle:UITableViewStylePlain];
    masterPane.navigationPaneViewController = _paneVC;
    _paneVC.masterViewController = masterPane;
    
    
    [_paneVC setModalTransitionStyle:UIModalTransitionStyleFlipHorizontal];
    
    //LoginVC instead of self
    [loginVC presentViewController:_paneVC animated:YES completion:^(void){}];
}

- (void)GoToProfile{
    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
        //Device is ipad
        profileVC = [[SCProfileVC alloc] initWithNibName:@"SCProfileVC" bundle:nil];
    }else{
        //Device is iphone
        profileVC = [[SCProfileVC alloc] initWithNibName:@"SCProfileVCIphone" bundle:nil];
    }
    
    [(SCMasterPaneVC*)_paneVC.masterViewController transitionToViewController:profileVC];
}

- (void)GoToSearch{
    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
        //Device is ipad
        searchVC = [[SCSearchVC alloc] initWithNibName:@"SCSearchVC" bundle:nil];
    }else{
        //Device is iphone
        searchVC = [[SCSearchVC alloc] initWithNibName:@"SCSearchVCIphone" bundle:nil];
    }
    
    [(SCMasterPaneVC*)_paneVC.masterViewController transitionToViewController:searchVC];
}

-(void)GoToPopoverCotizador:(NSString*)TheTitle description:(NSString*)TheDescription{
    
//    self.popOver = [[SCPopoverCotizadorVC alloc]initWithNibName:@"SCPopoverCotizadorVC" bundle:nil title:TheTitle description:TheDescription];
//    [cotizadorVC.view addSubview:self.popOver.view];
//    [self pulse:self.popOver.view];
}

- (void)GoToGeolocalization{
    SCGeolocalization *geoVC = [[SCGeolocalization alloc] initWithNibName:@"SCGeolocalization" bundle:nil];
    
    [(SCMasterPaneVC*)_paneVC.masterViewController transitionToViewController:geoVC];
}

- (void)goToQuotation{
    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
        //Device is ipad
        self.cotizadorVC = [[SCCotizadorVC alloc] initWithNibName:@"SCCotizadorVC" bundle:nil];
    }else{
        //Device is iphone
        self.cotizadorVC = [[SCCotizadorVC alloc] initWithNibName:@"SCCotizadorVCphone" bundle:nil];
    }
    
    [(SCMasterPaneVC*)_paneVC.masterViewController transitionToViewController:self.cotizadorVC];
}

- (void)goToProspect{
    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
        //Device is ipad
        prospectionVC = [[SCProspectionVC alloc] initWithNibName:@"SCProspectionVC" bundle:nil];
    }else{
        //Device is iphone
        prospectionVC = [[SCProspectionVC alloc] initWithNibName:@"SCProspectionVCIphone" bundle:nil];
    }
    
    [(SCMasterPaneVC*)_paneVC.masterViewController transitionToViewController:prospectionVC];
}

- (void) pulse:(UIView*)theView
{
	// pulse animation thanks to:  http://delackner.com/blog/2009/12/mimicking-uialertviews-animated-transition/
    theView.transform = CGAffineTransformMakeScale(0.6, 0.6);
	[UIView animateWithDuration: 0.2
					 animations: ^{
						 theView.transform = CGAffineTransformMakeScale(1.1, 1.1);
					 }
					 completion: ^(BOOL finished){
						 [UIView animateWithDuration:1.0/15.0
										  animations: ^{
											  theView.transform = CGAffineTransformMakeScale(0.9, 0.9);
										  }
										  completion: ^(BOOL finished){
											  [UIView animateWithDuration:1.0/7.5
															   animations: ^{
																   theView.transform = CGAffineTransformIdentity;
															   }];
										  }];
					 }];
	
}

-(void)didGetResponse:(id)response inConnectionTag:(int)tag{
    [self.paneVC.view makeToast:@"La ubicación se guardo exitosamente."];
}

@end
