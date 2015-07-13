//
//  SCMainMenuVC.h
//  SIPAC
//
//  Created by Jaguar3 on 09/04/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SCWebServicesMgr.h"
/**
 Class that present the menu of the app
 */
@interface SCMainMenuVC : UIViewController <BWConnectionManagerDelegate,UIAlertViewDelegate>

- (IBAction)goToProfile:(id)sender;
- (IBAction)goToSearch:(id)sender;
- (IBAction)goToQuotation:(id)sender;
- (IBAction)goToProspect:(id)sender;
- (IBAction)goToLocalization:(id)sender;
- (IBAction)goToUpdate:(id)sender;

- (void)update;

@end
