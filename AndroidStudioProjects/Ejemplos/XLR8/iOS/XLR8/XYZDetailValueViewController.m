//
//  XYZDetailValueViewController.m
//  XLR8
//
//  Created by Jaguar Labs on 28/03/14.
//  Copyright (c) 2014 Jaguar Labs. All rights reserved.
//

#import "XYZDetailValueViewController.h"

@interface XYZDetailValueViewController ()

@property (weak, nonatomic) IBOutlet UILabel *valueLabel;
@property (weak, nonatomic) IBOutlet UILabel *dateLabel;
@property (weak, nonatomic) IBOutlet UITextView *infoView;


@end

@implementation XYZDetailValueViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)loadInfo
{
    NSUserDefaults *usdef = [NSUserDefaults standardUserDefaults];
    NSString *valueKey = [NSString stringWithFormat:@"GIVEN_VALUE_%ld",(long)self.pickedValue];
    NSString *dateKey = [NSString stringWithFormat:@"LAST_DATE_%ld",(long)self.pickedValue];
    NSMutableString *description = [NSMutableString stringWithFormat:@"VALUE_DESC_%ld",(long)self.pickedValue];
    NSMutableString *behaviors = [NSMutableString stringWithFormat:@"VALUE_BEHAVE_%ld",(long)self.pickedValue];
    
    self.valueLabel.text = [usdef objectForKey:valueKey];
    self.dateLabel.text = [usdef objectForKey:dateKey];
    description = [usdef objectForKey:description];
    behaviors = [usdef objectForKey:behaviors];
    
    
    self.infoView.text = [NSString stringWithFormat:@"This value is important to me because\n%@\nThese behaviors describe my value\n%@",description,behaviors];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self loadInfo];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
