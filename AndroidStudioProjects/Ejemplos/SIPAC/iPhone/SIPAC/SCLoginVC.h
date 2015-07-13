//
//  SCLoginVC.h
//  SIPAC
//
//  Created by Jaguar3 on 04/04/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SCWebServicesMgr.h"
/**
 Class to enter a sipac 
 */
@interface SCLoginVC : UIViewController <BWConnectionManagerDelegate>

@property (weak, nonatomic) IBOutlet UITextField *txtName;
@property (weak, nonatomic) IBOutlet UITextField *txtPass;
@property (strong) IBOutlet UILabel *titleLogin;

- (IBAction)login:(id)sender;


@end
