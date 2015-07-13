//
//  SCLoginVC.m
//  SIPAC
//
//  Created by Jaguar3 on 04/04/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import "SCLoginVC.h"
#import "SCDatabaseManager.h"
#import "SCManager.h"



@interface SCLoginVC (){
    UIButton *invisibleButton;
}

@property (weak, nonatomic) IBOutlet UILabel *lblVersion;
@end

@implementation SCLoginVC

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
        self.titleLogin.font = [UIFont fontWithName:@"Helvetica-Bold" size:19];
        self.titleLogin.textColor = [UIColor whiteColor];
        self.titleLogin.frame = CGRectMake(325, 11, 155, 21);
        [[UINavigationBar appearance] setBackgroundImage:[UIImage imageNamed:@"img_navBar.png"] forBarMetrics:UIBarMetricsDefault];
        self.navigationController.title = @"Inicio de Sesión";
        
        NSDictionary *textTitleOptions = [NSDictionary dictionaryWithObjectsAndKeys:[UIColor whiteColor], UITextAttributeTextColor, [UIColor darkGrayColor], UITextAttributeTextShadowColor, nil];
        [[UINavigationBar appearance] setTitleTextAttributes:textTitleOptions];
        
    }

    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWasShown:)
                                                 name:UIKeyboardDidShowNotification
                                               object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWasHidden:)
                                                 name:UIKeyboardDidHideNotification
                                               object:nil];
    
    self.lblVersion.text = [NSString stringWithFormat:@"Versión %@",[[NSBundle mainBundle] objectForInfoDictionaryKey:(NSString*)kCFBundleVersionKey]];
}

- (void)keyboardWasShown:(id)notification{
    invisibleButton = [[UIButton alloc] initWithFrame:[UIApplication sharedApplication].keyWindow.bounds];
    [invisibleButton addTarget:self action:@selector(hiddenBtnTouched:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:invisibleButton];
}

- (void)keyboardWasHidden:(id)notification{
    [invisibleButton removeFromSuperview];
}

- (void)hiddenBtnTouched:(id)sender{
    [self.txtName resignFirstResponder];
    [self.txtPass resignFirstResponder];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)login:(id)sender {
    if ([BWConnectionManager connectedToNetworkRestring3g:NO]) {
        if (self.txtName.text.length == 0) {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Advertencia" message:@"Aun no ha ingresado un nombre de usuario." delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
            [alert show];
            return;
        }
        if (self.txtPass.text.length == 0) {
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Advertencia" message:@"Aun no ha ingresado una contraseña." delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
            [alert show];
            return;
        }
        [SCWebServicesMgr WSSetLogin:self.txtName.text ThePassword:self.txtPass.text Delegate:self];
    }else{
        NSDictionary *profile = [[SCDatabaseManager sharedInstance] GetProfile];
        //@"user_name":userName, @"user_pass"
        if ([profile[@"user_name"] isEqualToString:self.txtName.text] && [profile[@"user_pass"] isEqualToString:self.txtPass.text]) {
            //@"expiracion":@"",@"loginDate":@"0"
            int day = 86400;
            float loginDate = [profile[@"loginDate"] floatValue];
            NSDate *limitDay = [NSDate dateWithTimeIntervalSince1970:(loginDate + ([profile[@"expiracion"]integerValue] * day))];
            NSLog(@"limit Day : %@ \nToday : %@",limitDay,[NSDate date]);
            if ([limitDay compare:[NSDate date]] == NSOrderedAscending) {
                NSLog(@"LimitDay es menor");
                [[SCDatabaseManager sharedInstance] deleteTables];
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Advertencia" message:@"Has llegado al limite de tiempo que puedes utilizar la aplicación sin internet, la base de datos se ha borrado." delegate:nil cancelButtonTitle:@"Aceptar" otherButtonTitles: nil];
                [alert show];
                return;
            }
            [[SCManager sharedInstance] ChangeToMasterPane];
        }else{
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Usuario o contraseña incorrectos, vuelva a intentarlo." delegate:nil cancelButtonTitle:@"Aceptar" otherButtonTitles: nil];
            [alert show];
        }
    
    }
    
}

-(void)didGetResponse:(id)response inConnectionTag:(int)tag{
    
    if (response == nil) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"No se puede conectar con el servidor intentelo mas tarde." delegate:nil cancelButtonTitle:@"Aceptar" otherButtonTitles: nil];
        [alert show];
        return;
    }
    
    NSString *error = [(NSDictionary*)response objectForKey:@"error"];
    if (error != nil) {
        DebugLog(@"error : %@",error);
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Usuario o contraseña incorrectos, vuelva a intentarlo." delegate:nil cancelButtonTitle:@"Aceptar" otherButtonTitles: nil];
        [alert show];
        return;
    }
    
    [SCWebServicesMgr parseLogin:response userName:self.txtName.text userPass:self.txtPass.text];
    
    [[SCManager sharedInstance] ChangeToMasterPane];
}

@end
