//
//  SCMainMenuVC.m
//  SIPAC
//
//  Created by Jaguar3 on 09/04/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import "SCMainMenuVC.h"
#import "SCManager.h"
#import "SCDatabaseManager.h"

@interface SCMainMenuVC ()

@end

@implementation SCMainMenuVC

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
    
    [SCManager sharedInstance].paneVC.paneDraggingEnabled = NO;
    
    
    NSString *version = [[UIDevice currentDevice] systemVersion];
    float version_float = [version floatValue];
    if (version_float != 7.0){
        
        NSDictionary *textTitleOptions = [NSDictionary dictionaryWithObjectsAndKeys:[UIColor whiteColor], UITextAttributeTextColor, [UIColor darkGrayColor], UITextAttributeTextShadowColor, nil];
        [[UINavigationBar appearance] setTitleTextAttributes:textTitleOptions];
        
        [[UINavigationBar appearance] setBackgroundImage:[UIImage imageNamed:@"img_navBar.png"] forBarMetrics:UIBarMetricsDefault];
       
        [self.navigationController.navigationBar setBackgroundImage:[UIImage imageNamed:@"img_navBar.png"] forBarMetrics:UIBarMetricsDefault];
       
    }
   
    self.navigationController.navigationBar.topItem.title = @"Menu Principal";
    UIImage *btnImgAlert = [UIImage imageNamed:@"btn_salir_unpressed"];
    UIImage *btnImgAlertPressed = [UIImage imageNamed:@"btn_salir_pressed"];
    UIButton *myleftButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [myleftButton setImage:btnImgAlert forState:UIControlStateNormal];
    [myleftButton setImage:btnImgAlertPressed forState:UIControlStateHighlighted];
    myleftButton.frame = CGRectMake(200.0, 10.0, btnImgAlert.size.width, btnImgAlert.size.height);
    [myleftButton addTarget:self action:@selector(logOutTapped:) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *leftButton = [[UIBarButtonItem alloc]initWithCustomView:myleftButton];
    self.navigationItem.leftBarButtonItem = leftButton;
}

- (void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    
    [[SCWebServicesMgr sharedInstance] download];
}

- (void)logOutTapped:(UIButton*)sender{
    [[SCManager sharedInstance] logOut];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)goToProfile:(id)sender {
    if ([[SCDatabaseManager sharedInstance]hasAllData]) {
        [[SCManager sharedInstance].paneVC setPaneViewSlideOffAnimationEnabled:NO];
        [[SCManager sharedInstance] GoToProfile];
        [SCManager sharedInstance].paneVC.paneDraggingEnabled = YES;
        [[SCManager sharedInstance].paneVC setPaneViewSlideOffAnimationEnabled:YES];
    }else{
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Advertencia" message:@"Aun no se ha descargado todos los datos, por favor actualiza la información." delegate:Nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
        [alert show];
    }
    
}

- (IBAction)goToSearch:(id)sender {
    if ([[SCDatabaseManager sharedInstance]hasAllData]) {
        [[SCManager sharedInstance].paneVC setPaneViewSlideOffAnimationEnabled:NO];
        [[SCManager sharedInstance] GoToSearch];
        [SCManager sharedInstance].paneVC.paneDraggingEnabled = YES;
        [[SCManager sharedInstance].paneVC setPaneViewSlideOffAnimationEnabled:YES];
    }else{
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Advertencia" message:@"Aun no se ha descargado todos los datos, por favor actualiza la información." delegate:Nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
        [alert show];
    }
}

- (IBAction)goToQuotation:(id)sender {
    if ([[SCDatabaseManager sharedInstance]hasAllData]) {
        [[SCManager sharedInstance].paneVC setPaneViewSlideOffAnimationEnabled:NO];
        [[SCManager sharedInstance] goToQuotation];
        [SCManager sharedInstance].paneVC.paneDraggingEnabled = YES;
        [[SCManager sharedInstance].paneVC setPaneViewSlideOffAnimationEnabled:YES];
    }else{
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Advertencia" message:@"Aun no se ha descargado todos los datos, por favor actualiza la información." delegate:Nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
        [alert show];
    }
}

- (IBAction)goToProspect:(id)sender {
    if ([[SCDatabaseManager sharedInstance]hasAllData]) {
        [[SCManager sharedInstance].paneVC setPaneViewSlideOffAnimationEnabled:NO];
        [[SCManager sharedInstance] goToProspect];
        [SCManager sharedInstance].paneVC.paneDraggingEnabled = YES;
        [[SCManager sharedInstance].paneVC setPaneViewSlideOffAnimationEnabled:YES];
    }else{
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Advertencia" message:@"Aun no se ha descargado todos los datos, por favor actualiza la información." delegate:Nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
        [alert show];
    }
}

- (IBAction)goToLocalization:(id)sender {
    if ([[SCDatabaseManager sharedInstance]hasAllData]) {
        [[SCManager sharedInstance].paneVC setPaneViewSlideOffAnimationEnabled:NO];
        [[SCManager sharedInstance] GoToGeolocalization];
        [SCManager sharedInstance].paneVC.paneDraggingEnabled = YES;
        [[SCManager sharedInstance].paneVC setPaneViewSlideOffAnimationEnabled:YES];
    }else{
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Advertencia" message:@"Aun no se ha descargado todos los datos, por favor actualiza la información." delegate:Nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
        [alert show];
    }
}

- (IBAction)goToUpdate:(id)sender {
    if ([[SCDatabaseManager sharedInstance] hasAllData]) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Advertencia" message:@"Se borraran todos los datos antes de proceder \n¿Deseas continuar?" delegate:self cancelButtonTitle:@"Cancelar" otherButtonTitles:@"Continuar", nil];
        [alert show];
    }else{
        [[SCWebServicesMgr sharedInstance] download];
    }
    
}

- (void)update{
    [self goToUpdate:Nil];
}

-(void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex{
    if (buttonIndex != 0) {
        [[SCDatabaseManager sharedInstance] deleteTables];
        [[SCWebServicesMgr sharedInstance] download];
    }
}


@end
