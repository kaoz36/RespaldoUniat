//
//  SCProfileVC.h
//  SIPAC
//
//  Created by Jaguar3 on 05/04/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import <UIKit/UIKit.h>
/**
 Class that present the profile of the user
 */
@interface SCProfileVC : UIViewController

@property (weak, nonatomic) IBOutlet UITextField *txt_id;
@property (weak, nonatomic) IBOutlet UITextField *txt_name;
@property (weak, nonatomic) IBOutlet UITextField *txt_rfc;
@property (weak, nonatomic) IBOutlet UITextField *txt_birthday;
@property (weak, nonatomic) IBOutlet UITextField *txt_expiration;
@property (weak, nonatomic) IBOutlet UITextField *txt_mon;
@end
