//
//  SCDetailVCViewController.m
//  SIPAC
//
//  Created by Jaguar3 on 20/03/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import "SCDetailVC.h"
#import "SCDatabaseManager.h"

@interface SCDetailVC (){
    NSTimer *timer;
}

@end

@implementation SCDetailVC

@synthesize Poliza;

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
    
    DebugLog(@"Poliza : %@",Poliza);
    
    if ([[[UIDevice currentDevice] systemVersion] integerValue] == 7){
        self.edgesForExtendedLayout = UIRectEdgeNone;
    }
    //Instantiate the model array
    self.modelArray = [[NSMutableArray alloc] init];
    for (int index = 1; index <= 4 ; index++)
    {
        [self.modelArray addObject:[NSString stringWithFormat:@"%d",index]];
    }
    
    //Step 1
    //Instantiate the UIPageViewController.
    self.pageViewController = [[UIPageViewController alloc] initWithTransitionStyle:UIPageViewControllerTransitionStylePageCurl
                                                              navigationOrientation:UIPageViewControllerNavigationOrientationHorizontal options:nil];
    
    //Step 2:
    //Assign the delegate and datasource as self.
    self.pageViewController.delegate = self;
    self.pageViewController.dataSource = self;
    
    //Step 3:
    //Set the initial view controllers.
    SCContentForPageViewVC *contentViewController = nil;
    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
        contentViewController = [[SCContentForPageViewVC alloc] initWithNibName:@"SCContentForPageViewVC" bundle:nil];
    }else{
        contentViewController = [[SCContentForPageViewVC alloc] initWithNibName:@"SCContentForPageViewVCIphone" bundle:nil];
    }
    
    contentViewController.parentViewVC = self;
    contentViewController.labelContents = [self.modelArray objectAtIndex:0];
    contentViewController.Poliza = self.Poliza;
    NSArray *viewControllers = [NSArray arrayWithObject:contentViewController];
    [self.pageViewController setViewControllers:viewControllers
                                      direction:UIPageViewControllerNavigationDirectionForward
                                       animated:NO
                                     completion:nil];
    
    //Step 4:
    //ViewController containment steps
    //Add the pageViewController as the childViewController
    [self addChildViewController:self.pageViewController];
    
    //Add the view of the pageViewController to the current view
    [self.view addSubview:self.pageViewController.view];
    
    //Call didMoveToParentViewController: of the childViewController, the UIPageViewController instance in our case.
    [self.pageViewController didMoveToParentViewController:self];
    
    //Step 5:
    // set the pageViewController's frame as an inset rect.
//    CGRect pageViewRect = self.view.bounds;
//    pageViewRect = CGRectInset(pageViewRect, 40.0, 60.0);
    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
        //Device is ipad
        self.pageViewController.view.frame = CGRectMake(5, 193, 760, 802 );//pageViewRect;
    }else{
        //Device is iphone
        self.pageViewController.view.frame = CGRectMake(0, 143, 320, 275 );//pageViewRect;
    }
    
    
    //Step 6:
    //Assign the gestureRecognizers property of our pageViewController to our view's gestureRecognizers property.
    self.view.gestureRecognizers = self.pageViewController.gestureRecognizers;
    
    
    NSMutableArray *array = [[SCDatabaseManager sharedInstance] GetPoliza:Poliza];
    NSDictionary *dict = [array objectAtIndex:0];
    //id_poliza, status_poliza, ret_poliza, nombre_poliza, rfc_poliza, emp_poliza, sexo_poliza, fuma_poliza, nac_poliza, calle_poliza, ext_poliza,
    
    self.txtPoliza.text = Poliza;
    self.txtNombre.text = [dict objectForKey:@"nombre_poliza"];
    self.txtStatus.text = [dict objectForKey:@"status_poliza"];
    self.txtRetenedor.text = [dict objectForKey:@"ret_poliza"];
    self.txtRFC.text = [dict objectForKey:@"rfc_poliza"];
    self.txtNoEmp.text = [dict objectForKey:@"emp_poliza"];
    
    UIImage *myImage = [UIImage imageNamed:@"btn_back_unpressed"];
    UIImage *myImage2 = [UIImage imageNamed:@"btn_back_pressed"];
    UIButton *myLeftButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [myLeftButton setImage:myImage forState:UIControlStateNormal];
    [myLeftButton setImage:myImage2 forState:UIControlStateHighlighted];
    myLeftButton.frame = CGRectMake(0.0, 5.0, myImage.size.width, myImage.size.height);
    [myLeftButton addTarget:self action:@selector(backTapped:) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *leftButton = [[UIBarButtonItem alloc] initWithCustomView:myLeftButton];
    self.navigationItem.leftBarButtonItem = leftButton;
    
    
    UIImage *btnImgAlert = [UIImage imageNamed:@"btn_gotoSearch_unpressed"];
    UIImage *btnImgAlertPressed = [UIImage imageNamed:@"btn_gotoSearch_pressed"];
    UIButton *myRightButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [myRightButton setImage:btnImgAlert forState:UIControlStateNormal];
    [myRightButton setImage:btnImgAlertPressed forState:UIControlStateHighlighted];
    myRightButton.frame = CGRectMake(200.0, 5.0, btnImgAlert.size.width, btnImgAlert.size.height);
    [myRightButton addTarget:self action:@selector(returnSearchTapped:) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *rightButton = [[UIBarButtonItem alloc]initWithCustomView:myRightButton];
    self.navigationItem.rightBarButtonItem = rightButton;
    
    self.btnTab1.selected = NO;
    self.btnTab2.selected = YES;
    self.btnTab3.selected = YES;
    self.btnTab4.selected = YES;
    
    timer = [NSTimer scheduledTimerWithTimeInterval:0.5 target:self selector:@selector(checkCurrentTab:) userInfo:nil repeats:YES];
    
    for (UITextField *txtView in self.view.subviews) {
        if ([txtView isKindOfClass:[UITextField class]]) {
            txtView.textColor = [UIColor colorWithRed:0.47 green:0.47 blue:0.48 alpha:1.0];
        }
    }


    if([UIDevice currentDevice].userInterfaceIdiom!=UIUserInterfaceIdiomPad) {
       //Device is iphone
        [self.infoView removeFromSuperview];
        [self.view addSubview:self.infoView];
        [self.infoView setHidden:YES];
        [self.hiddenButton setHidden:YES];
        [self.hiddenButton addTarget:self action:@selector(hideView:) forControlEvents:UIControlEventTouchUpInside];
    }

}

- (void)hideView:(id)sender{
    [self.infoView setHidden:YES];
    [self.hiddenButton setHidden:YES];
}

- (void)backTapped:(id)sender{
    [self.navigationController popViewControllerAnimated:YES];
    [timer invalidate];
}

- (void)returnSearchTapped:(id)sender{
    [self.navigationController popToRootViewControllerAnimated:YES];
    [timer invalidate];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - UIPageViewControllerDataSource Methods

- (UIViewController *)pageViewController:(UIPageViewController *)pageViewController
      viewControllerBeforeViewController:(UIViewController *)viewController
{
    NSUInteger currentIndex = [self.modelArray indexOfObject:[(SCContentForPageViewVC *)viewController labelContents]];
    if(currentIndex == 0)
    {
        return nil;
    }
    
    SCContentForPageViewVC *contentViewController = nil;
    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
        contentViewController = [[SCContentForPageViewVC alloc] initWithNibName:@"SCContentForPageViewVC" bundle:nil];
    }else{
        contentViewController = [[SCContentForPageViewVC alloc] initWithNibName:@"SCContentForPageViewVCIphone" bundle:nil];
    }
    //SCContentForPageViewVC *contentViewController = [[SCContentForPageViewVC alloc] init];
    contentViewController.parentViewVC = self;
    contentViewController.labelContents = [self.modelArray objectAtIndex:currentIndex - 1];
    contentViewController.Poliza = self.Poliza;
    return contentViewController;
}

- (UIViewController *)pageViewController:(UIPageViewController *)pageViewController
       viewControllerAfterViewController:(UIViewController *)viewController
{
    
    
    NSUInteger currentIndex = [self.modelArray indexOfObject:[(SCContentForPageViewVC *)viewController labelContents]];
    if(currentIndex == self.modelArray.count-1)
    {
        return nil;
    }
    
    SCContentForPageViewVC *contentViewController = nil;
    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
        contentViewController = [[SCContentForPageViewVC alloc] initWithNibName:@"SCContentForPageViewVC" bundle:nil];
    }else{
        contentViewController = [[SCContentForPageViewVC alloc] initWithNibName:@"SCContentForPageViewVCIphone" bundle:nil];
    }
    contentViewController.parentViewVC = self;
    //SCContentForPageViewVC *contentViewController = [[SCContentForPageViewVC alloc] init];
    contentViewController.labelContents = [self.modelArray objectAtIndex:currentIndex + 1];
    contentViewController.Poliza = self.Poliza;
    
    return contentViewController;
}

#pragma mark - UIPageViewControllerDelegate Methods

- (UIPageViewControllerSpineLocation)pageViewController:(UIPageViewController *)pageViewController
                   spineLocationForInterfaceOrientation:(UIInterfaceOrientation)orientation
{
    if(UIInterfaceOrientationIsPortrait(orientation))
    {
        //Set the array with only 1 view controller
        SCContentForPageViewVC *currentViewController = [self.pageViewController.viewControllers objectAtIndex:0];
        NSArray *viewControllers = [NSArray arrayWithObject:currentViewController];
        [self.pageViewController setViewControllers:viewControllers direction:UIPageViewControllerNavigationDirectionForward animated:YES completion:NULL];
        
        //Important- Set the doubleSided property to NO.
        self.pageViewController.doubleSided = NO;
        
        //Return the spine location
        return UIPageViewControllerSpineLocationMin;
    }
    else
    {
        NSArray *viewControllers = nil;
        SCContentForPageViewVC *currentViewController = [self.pageViewController.viewControllers objectAtIndex:0];
        
        NSUInteger currentIndex = [self.modelArray indexOfObject:[(SCContentForPageViewVC *)currentViewController labelContents]];
        if(currentIndex == 0 || currentIndex %2 == 0)
        {
            UIViewController *nextViewController = [self pageViewController:self.pageViewController viewControllerAfterViewController:currentViewController];
            viewControllers = [NSArray arrayWithObjects:currentViewController, nextViewController, nil];
        }
        else
        {
            UIViewController *previousViewController = [self pageViewController:self.pageViewController viewControllerBeforeViewController:currentViewController];
            viewControllers = [NSArray arrayWithObjects:previousViewController, currentViewController, nil];
        }
        //Now, set the viewControllers property of UIPageViewController
        [self.pageViewController setViewControllers:viewControllers direction:UIPageViewControllerNavigationDirectionForward animated:YES completion:NULL];
        
        return UIPageViewControllerSpineLocationMid;
    }
}

- (IBAction)firstTab:(id)sender {
    [self pageGoto:1];
}

- (IBAction)secondTab:(id)sender {
    [self pageGoto:2];
}

- (IBAction)thirdTab:(id)sender {
    [self pageGoto:3];
}

- (IBAction)fourthTab:(id)sender {
    [self pageGoto:4];
}

-(IBAction)pageGoto:(int)page {
    
    //get page to go to
    NSUInteger pageToGoTo = page;
    
    //get current index of current page
    SCContentForPageViewVC *theCurrentViewController = [self.pageViewController.viewControllers   objectAtIndex:0];
    NSUInteger retreivedIndex = [[theCurrentViewController labelContents] integerValue];
    
    SCContentForPageViewVC *targetPageViewController = nil;
    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
        targetPageViewController = [[SCContentForPageViewVC alloc] initWithNibName:@"SCContentForPageViewVC" bundle:nil];
    }else{
        targetPageViewController = [[SCContentForPageViewVC alloc] initWithNibName:@"SCContentForPageViewVCIphone" bundle:nil];
    }
    targetPageViewController.parentViewVC = self;
    NSArray *theViewControllers = nil;
    
    //check which direction to animate page turn then turn page accordingly
    if (retreivedIndex < (pageToGoTo) && retreivedIndex != (pageToGoTo)){
        //get the page(s) to go to
        targetPageViewController.labelContents = [self.modelArray objectAtIndex:pageToGoTo-1];
        targetPageViewController.Poliza = self.Poliza;
        
        //put it(or them if in landscape view) in an array
        
        theViewControllers = [NSArray arrayWithObjects: targetPageViewController, nil];
        
        [self.pageViewController setViewControllers:theViewControllers direction:UIPageViewControllerNavigationDirectionForward animated:YES completion:NULL];
    }
    
    if (retreivedIndex > (pageToGoTo) && retreivedIndex != (pageToGoTo)){
        //get the page(s) to go to
        
        targetPageViewController.labelContents = [self.modelArray objectAtIndex:pageToGoTo-1];
        targetPageViewController.Poliza = self.Poliza;
        
        //put it(or them if in landscape view) in an array
        theViewControllers = [NSArray arrayWithObjects: targetPageViewController, nil];
        
        [self.pageViewController setViewControllers:theViewControllers direction:UIPageViewControllerNavigationDirectionReverse animated:YES completion:NULL];
    }
    
}

- (void)checkCurrentTab:(id)sender{
    
    SCContentForPageViewVC *currentViewController = [self.pageViewController.viewControllers objectAtIndex:0];
    
    
    switch ([currentViewController labelContents].integerValue) {
        case 1:
            self.btnTab1.selected = NO;
            self.btnTab2.selected = YES;
            self.btnTab3.selected = YES;
            self.btnTab4.selected = YES;
            break;
            
        case 2:
            self.btnTab1.selected = YES;
            self.btnTab2.selected = NO;
            self.btnTab3.selected = YES;
            self.btnTab4.selected = YES;
            break;
            
        case 3:
            self.btnTab1.selected = YES;
            self.btnTab2.selected = YES;
            self.btnTab3.selected = NO;
            self.btnTab4.selected = YES;
            break;
            
        case 4:
            self.btnTab1.selected = YES;
            self.btnTab2.selected = YES;
            self.btnTab3.selected = YES;
            self.btnTab4.selected = NO;
            break;
            
        default:
            break;
    }
}


- (IBAction)showInfo:(id)sender {
    if (self.infoView.hidden) {
        [self.infoView setHidden:NO];
        [self.hiddenButton setHidden:NO];
        [self pulse:self.infoView];
    }else{
        [self.infoView setHidden:YES];
        [self.hiddenButton setHidden:YES];
    }
    
}

- (void) pulse:(UIView*)theView;
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

- (void)viewDidUnload {
    [self setInfoView:nil];
    [self setHiddenButton:nil];
    [super viewDidUnload];
}
@end
