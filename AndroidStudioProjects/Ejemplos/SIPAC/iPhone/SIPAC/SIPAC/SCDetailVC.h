//
//  SCDetailVCViewController.h
//  SIPAC
//
//  Created by Jaguar3 on 20/03/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SCContentForPageViewVC.h"
/**
 class that present the detail of the information
 */
@interface SCDetailVC : UIViewController <UIPageViewControllerDataSource,UIPageViewControllerDelegate>

@property (nonatomic,strong) NSString *Poliza;

@property (nonatomic, strong) UIPageViewController *pageViewController;
@property (nonatomic, strong) NSMutableArray *modelArray;

@property (weak, nonatomic) IBOutlet UITextField *txtPoliza;
@property (weak, nonatomic) IBOutlet UITextField *txtStatus;
@property (weak, nonatomic) IBOutlet UITextField *txtRetenedor;
@property (weak, nonatomic) IBOutlet UITextField *txtNombre;
@property (weak, nonatomic) IBOutlet UITextField *txtRFC;
@property (weak, nonatomic) IBOutlet UITextField *txtNoEmp;

- (IBAction)firstTab:(id)sender;
- (IBAction)secondTab:(id)sender;
- (IBAction)thirdTab:(id)sender;
- (IBAction)fourthTab:(id)sender;

@property (weak, nonatomic) IBOutlet UIButton *btnTab1;
@property (weak, nonatomic) IBOutlet UIButton *btnTab2;
@property (weak, nonatomic) IBOutlet UIButton *btnTab3;
@property (weak, nonatomic) IBOutlet UIButton *btnTab4;


//Only for Iphone
- (IBAction)showInfo:(id)sender;
@property (weak, nonatomic) IBOutlet UIView *infoView;
@property (weak, nonatomic) IBOutlet UIButton *hiddenButton;
@end
