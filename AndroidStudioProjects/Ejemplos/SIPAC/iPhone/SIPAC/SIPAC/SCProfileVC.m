//
//  SCProfileVC.m
//  SIPAC
//
//  Created by Jaguar3 on 05/04/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import "SCProfileVC.h"
#import "SCDatabaseManager.h"
#import "SCManager.h"

@interface SCProfileVC ()

@end

@implementation SCProfileVC

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    
    
    NSString *version = [[UIDevice currentDevice] systemVersion];
    float version_float = [version floatValue];
    if (version_float != 7.0){
        [[UINavigationBar appearance] setBackgroundImage:[UIImage imageNamed:@"img_navBar.png"] forBarMetrics:UIBarMetricsDefault];
        
    }else{
        self.edgesForExtendedLayout = UIRectEdgeNone;
    }
    
    

    self.navigationController.navigationBar.topItem.title = @"Perfil";
   
    NSDictionary *profile = [[SCDatabaseManager sharedInstance] GetProfile];
    
    NSDate *fechaExpiracion = [NSDate dateWithTimeIntervalSince1970:[profile[@"loginDate"] floatValue] + ([profile[@"expiracion"] integerValue] * 86400)];
    NSDateFormatter* formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"dd-MMMM-yyyy"];
    //[formatter setDoesRelativeDateFormatting:YES];
    [formatter setTimeZone:[NSTimeZone systemTimeZone]];
    
    self.txt_id.text = [profile objectForKey:@"id_user"];
    self.txt_name.text = [profile objectForKey:@"full_name_user"];
    self.txt_rfc.text = [profile objectForKey:@"rfc"];
    self.txt_birthday.text = [profile objectForKey:@"email"];
    self.txt_expiration.text = [formatter stringFromDate:fechaExpiracion];
    self.txt_mon.text = [profile objectForKey:@"monedero_user"];
    
    NSLog(@"%@",profile);
    
    
    UIImage *btnImgAlert = [UIImage imageNamed:@"btn_sidemenu_unpressed"];
    UIImage *btnImgAlertPressed = [UIImage imageNamed:@"btn_sidemenu_pressed"];
    UIButton *myleftButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [myleftButton setImage:btnImgAlert forState:UIControlStateNormal];
    [myleftButton setImage:btnImgAlertPressed forState:UIControlStateHighlighted];
    myleftButton.frame = CGRectMake(200.0, 10.0, btnImgAlert.size.width, btnImgAlert.size.height);
    [myleftButton addTarget:self action:@selector(paneMenuTapped:) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *leftButton = [[UIBarButtonItem alloc]initWithCustomView:myleftButton];
    self.navigationItem.leftBarButtonItem = leftButton;
  
    
    for (UITextField *txtView in self.view.subviews) {
        if ([txtView isKindOfClass:[UITextField class]]) {
            txtView.textColor = [UIColor colorWithRed:0.47 green:0.47 blue:0.48 alpha:1.0];
        }
        
    }
}

- (void)paneMenuTapped:(UIButton*)sender{
    [[SCManager sharedInstance] showPaneMenu];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
