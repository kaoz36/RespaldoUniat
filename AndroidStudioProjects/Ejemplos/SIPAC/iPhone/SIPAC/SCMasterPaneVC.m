//
//  MSMasterPaneVC.m
//
//  Created by Rodrigo Galvez on 03/10/13.
//  Copyright (c) 2013-2014 JaguarLabs. All rights reserved.
//


#import "SCMasterPaneVC.h"
#import "SCMainMenuVC.h"
#import "SCManager.h"

NSString * const MSMasterViewControllerCellReuseIdentifier = @"MSMasterViewControllerCellReuseIdentifier";

typedef NS_ENUM(NSUInteger, MSMasterViewControllerTableViewSectionType) {
    MSMasterViewControllerTableViewSectionTypeAppearanceTypes,
    MSMasterViewControllerTableViewSectionTypeControls,
    MSMasterViewControllerTableViewSectionTypeAbout,
    MSMasterViewControllerTableViewSectionTypeCount
};

@interface SCMasterPaneVC () <MSNavigationPaneViewControllerDelegate>

@property (nonatomic, strong) NSArray *paneViewControllerTitles;

@property (nonatomic, strong) NSArray *tableViewSectionBreaks;

@property (nonatomic, strong) UIBarButtonItem *paneStateBarButtonItem;
@property (nonatomic, strong) UIBarButtonItem *paneRevealBarButtonItem;

@property (nonatomic, strong) UINavigationController *paneNavigationViewController;

- (void)navigationPaneRevealBarButtonItemTapped:(id)sender;
- (void)navigationPaneStateBarButtonItemTapped:(id)sender;

@end

@implementation SCMasterPaneVC

#pragma mark - UIViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        [self initialize];
    }
    return self;
}

- (id)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self) {
        [self initialize];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.navigationPaneViewController.delegate = self;
    
    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
        //Device is ipad
        self.navigationPaneViewController.openStateRevealWidth = 100.0;
    }else{
        //Device is iphone
        self.navigationPaneViewController.openStateRevealWidth = 90.0;
    }
    
    self.navigationPaneViewController.paneViewSlideOffAnimationEnabled = (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone);
    self.navigationPaneViewController.appearanceType = MSPaneViewControllerTypeAppearanceZoom;
    
    SCMainMenuVC *mainMenuVC = nil;
    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
        //Device is ipad
        mainMenuVC = [[SCMainMenuVC alloc] initWithNibName:@"SCMainMenuVC" bundle:nil];
    }else{
        //Device is iphone
        mainMenuVC = [[SCMainMenuVC alloc] initWithNibName:@"SCMainMenuVCIphone" bundle:nil];
    }
    
    [self transitionToViewController:mainMenuVC];
    
    UIImageView *backImage = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"img_miniMenu_bckgr"]];
    backImage.frame = CGRectMake(0, 0, self.view.frame.size.width, backImage.image.size.height);
    
    [self.view addSubview:backImage];
    
    
    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
        //Device is ipad
        NSArray *NameBtnPressed = [[NSArray alloc]initWithObjects:@"btn_perfilSmll_pressed", @"btn_consultaSmll_pressed",@"btn_cotizaSmll_pressed",@"btn_prospectSmll_pressed",@"btn_geolocalSmll_pressed",@"btn_actualizaSmll_pressed",@"btn_salirSmll_pressed", nil];
        NSArray *NameBtn = [[NSArray alloc]initWithObjects:@"btn_perfilSmll_unpressed", @"btn_consultaSmll_unpressed",@"btn_cotizaSmll_unpressed",@"btn_prospectSmll_unpressed",@"btn_geolocalSmll_unpressed",@"btn_actualizaSmll_unpressed",@"btn_salirSmll_unpressed", nil];
        
        for (int x = 1; x <= [NameBtn count]; x++) {
            UIButton *btn = [[UIButton alloc] initWithFrame:CGRectMake(7, (97*(x-1))+(5*x)+10, 86, 97)];
            [btn addTarget:self action:@selector(btnTouched:) forControlEvents:UIControlEventTouchUpInside];
            [btn setBackgroundImage:[UIImage imageNamed:[NameBtnPressed objectAtIndex:x-1]] forState:UIControlStateHighlighted];
            [btn setBackgroundImage:[UIImage imageNamed:[NameBtn objectAtIndex:x-1]] forState:UIControlStateNormal];
            btn.tag = x;
            [self.view addSubview:btn];
            //[btn release];ARC
        }
    }else{
        //Device is iphone
        NSArray *NameBtnPressed = [[NSArray alloc]initWithObjects:@"btn_perfilsmll2_pressed", @"btn_consultasmll2_pressed",@"btn_cotizasmll2_pressed",@"btn_prospectsmll2_pressed",@"btn_localizasmll2_pressed",@"btn_actualizasmll2_pressed",@"btn_salirsmll2_pressed", nil];
        NSArray *NameBtn = [[NSArray alloc]initWithObjects:@"btn_perfilsmll2_unpressed", @"btn_consultasmll2_unpressed",@"btn_cotizasmll2_unpressed",@"btn_prospectsmll2_unpressed",@"btn_localizasmll2_unpressed",@"btn_actualizasmll2_unpressed",@"btn_salirsmll2_unpressed", nil];
        NSArray *LabelsArray = @[@"Perfil", @"Consulta de cartera", @"Cotización", @"Prospección", @"Geolocalización", @"Actualización", @"Salir"];
        
        UIScrollView *scroll = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
        for (int x = 1; x <= [NameBtn count]; x++) {
            UIButton *btn;
            UITextView *text;
            if (x > 2) {
                btn = [[UIButton alloc] initWithFrame:CGRectMake(13, (67*(x-1))+(2*x)+25, 56, 56)];
                text = [[UITextView alloc] initWithFrame:CGRectMake(-10, (67*((x+1)-1))+(5*x)-10, 100, 40)];
            }else{
                btn = [[UIButton alloc] initWithFrame:CGRectMake(13, (67*(x-1))+(2*x)+10, 56, 56)];
                text = [[UITextView alloc] initWithFrame:CGRectMake(-10, (67*((x+1)-1))+(5*x)+-15, 100, 40)];
            }
            [btn addTarget:self action:@selector(btnTouched:) forControlEvents:UIControlEventTouchUpInside];
            [btn setBackgroundImage:[UIImage imageNamed:[NameBtnPressed objectAtIndex:x-1]] forState:UIControlStateHighlighted];
            [btn setBackgroundImage:[UIImage imageNamed:[NameBtn objectAtIndex:x-1]] forState:UIControlStateNormal];
            btn.tag = x;
        
            text.userInteractionEnabled = NO;
            text.backgroundColor = [UIColor clearColor];
            text.textColor = [UIColor whiteColor];
            text.textAlignment = NSTextAlignmentCenter;
            text.text = [LabelsArray objectAtIndex:x-1];
        
            [scroll addSubview:btn];
            [scroll addSubview:text];
            
            scroll.contentSize = CGSizeMake(self.view.frame.size.width, text.frame.origin.y + text.frame.size.height);
            //[btn release];ARC
        }
        
    
        [self.view addSubview:scroll];
    }
    
    
    self.tableView.separatorColor = [UIColor clearColor];
    self.tableView.scrollEnabled = NO;
}

#pragma mark - MSMasterViewController

- (void)initialize
{
    self.paneViewControllerType = NSUIntegerMax;
    self.paneViewControllerTitles = @[@"Perfil",@"Busqueda",@"Cerrar Sesion"];
    
    self.tableViewSectionBreaks = @[
        @(MSPaneViewControllerTypeControls),
        @(MSPaneViewControllerTypeMonospace),
        @(MSPaneViewControllerTypeCount)
    ];
}

- (MSPaneViewControllerType)paneViewControllerTypeForIndexPath:(NSIndexPath *)indexPath
{
    MSPaneViewControllerType paneViewControllerType;
    if (indexPath.section == 0) {
        paneViewControllerType = indexPath.row;
    } else {
        paneViewControllerType = ([self.tableViewSectionBreaks[(indexPath.section - 1)] integerValue] + indexPath.row);
    }
    NSAssert(paneViewControllerType < MSPaneViewControllerTypeCount, @"Invalid Index Path");
    return paneViewControllerType;
}

- (void)transitionToViewController:(UIViewController*)theViewController
{
    if (self.navigationPaneViewController.paneViewController == theViewController) {
        [self.navigationPaneViewController setPaneState:MSNavigationPaneStateClosed animated:YES completion:nil];
        return;
    }
    
    BOOL animateTransition = self.navigationPaneViewController.paneViewController != nil;
    
    //paneViewController.navigationItem.title = self.paneViewControllerTitles[@(paneViewControllerType)];
    
//    self.paneRevealBarButtonItem = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"MSBarButtonIconNavigationPane.png"] style:UIBarButtonItemStyleBordered target:self action:@selector(navigationPaneRevealBarButtonItemTapped:)];
//    theViewController.navigationItem.leftBarButtonItem = self.paneRevealBarButtonItem;
    
    self.paneNavigationViewController = [[UINavigationController alloc] initWithRootViewController:theViewController];
    [self.navigationPaneViewController setPaneViewController:self.paneNavigationViewController animated:animateTransition completion:nil];
    
}

- (void)navigationPaneRevealBarButtonItemTapped:(id)sender
{
    [self.navigationPaneViewController setPaneState:MSNavigationPaneStateOpen animated:YES completion:nil];
}

- (void)navigationPaneStateBarButtonItemTapped:(id)sender
{
    if (self.navigationPaneViewController.openDirection == MSNavigationPaneOpenDirectionLeft) {
        self.navigationPaneViewController.openDirection = MSNavigationPaneOpenDirectionTop;
    } else {
        self.navigationPaneViewController.openDirection =  MSNavigationPaneOpenDirectionLeft;
    }
}

#pragma mark - UITableViewDataSource

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 0;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.paneViewControllerTitles.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:MSMasterViewControllerCellReuseIdentifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:MSMasterViewControllerCellReuseIdentifier];
    }
    
    cell.textLabel.text = [self.paneViewControllerTitles objectAtIndex:indexPath.row];
    
    return cell;
}

#pragma mark - UITableViewDelegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{

//    switch (indexPath.row) {
//        case 0:
//            [[SCManager sharedInstance] GoToProfile];
//            break;
//            
//        case 1:
//            [[SCManager sharedInstance] GoToSearch];
//            break;
//            
//        case 2:
//            [[SCManager sharedInstance] logOut];
//            break;
//            
//        default:
//            break;
//    }
}

- (void)btnTouched:(UIButton*)button{
    switch (button.tag) {
        case 1:
            [[SCManager sharedInstance] GoToProfile];
            break;
            
        case 2:
            [[SCManager sharedInstance] GoToSearch];
            break;
            
        case 3:
            [[SCManager sharedInstance] goToQuotation];
            break;
            
        case 4:
            [[SCManager sharedInstance] goToProspect];
            break;
            
        case 5:
            [[SCManager sharedInstance] GoToGeolocalization];
            break;
            
        case 6:
            if (1) {
                SCMainMenuVC *mainMenuVC = nil;
                if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
                    //Device is ipad
                    mainMenuVC = [[SCMainMenuVC alloc] initWithNibName:@"SCMainMenuVC" bundle:nil];
                }else{
                    //Device is iphone
                    mainMenuVC = [[SCMainMenuVC alloc] initWithNibName:@"SCMainMenuVCIphone" bundle:nil];
                }
                
                [self transitionToViewController:mainMenuVC];
                [mainMenuVC update];
            }
            break;
            
        case 7:
            [[SCManager sharedInstance] logOut];
            break;
            
        default:
            break;
    }
}

#pragma mark - MSNavigationPaneViewControllerDelegate

- (void)navigationPaneViewController:(MSNavigationPaneViewController *)navigationPaneViewController didUpdateToPaneState:(MSNavigationPaneState)state
{
    // Ensure that the pane's table view can scroll to top correctly
    self.tableView.scrollsToTop = (state == MSNavigationPaneStateOpen);
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation{
    return NO;
}

@end
