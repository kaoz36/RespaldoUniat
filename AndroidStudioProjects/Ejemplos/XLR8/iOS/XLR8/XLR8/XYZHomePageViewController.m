//
//  XYZHomePageViewController.m
//  XLR8
//
//  Created by Jaguar Labs on 07/03/14.
//  Copyright (c) 2014 Jaguar Labs. All rights reserved.
//

#import "XYZHomePageViewController.h"

@interface XYZHomePageViewController ()

@end

@implementation XYZHomePageViewController

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
    NSLog(@"Loading...");
    [self.navigationController setNavigationBarHidden:YES];
    UIImageView*imageView=[[UIImageView alloc]initWithImage:[UIImage imageNamed:@"splashpage.png"]];
    [self.view addSubview:imageView];
    [self.view bringSubviewToFront:imageView];
    [UIView transitionWithView:self.view duration:2.0f options:UIViewAnimationOptionTransitionNone animations:^(void){imageView.alpha=1.5f;} completion:^(BOOL finished){[imageView removeFromSuperview];}];
    [super viewDidLoad];
    [self.navigationController setNavigationBarHidden:NO animated:YES];

	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
