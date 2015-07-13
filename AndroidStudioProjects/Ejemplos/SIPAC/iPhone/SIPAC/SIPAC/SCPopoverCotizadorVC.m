//
//  SCPopoverCotizadorVC.m
//  SIPAC
//
//  Created by MacBook Pro de Hugo on 20/06/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import "SCPopoverCotizadorVC.h"

@interface SCPopoverCotizadorVC (){
    
    NSString *title;
    NSString *descriptions;
}

@property (strong) IBOutlet UILabel *titlePop;
@property (strong) IBOutlet UITextView *description;

@end

@implementation SCPopoverCotizadorVC

@synthesize titlePop;
@synthesize description;


- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil title:(NSString *)theTitle description: (NSString *)theDescription{
    
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
        title = theTitle;
        descriptions = theDescription;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.titlePop.text = title;
    self.description.text = descriptions;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
