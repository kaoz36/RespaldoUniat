//
//  XYZValuesViewController.m
//  XLR8
//
//  Created by Jaguar Labs on 25/03/14.
//  Copyright (c) 2014 Jaguar Labs. All rights reserved.
//

#import "XYZValuesViewController.h"
#import "XYZMakeYourNewViewController.h"
#import "XYZDetailValueViewController.h"
#import "XLR8/XYZSetRemindersViewController.h"

@interface XYZValuesViewController () <UITableViewDelegate, UITableViewDataSource>
@property (weak, nonatomic) IBOutlet UITableView *topValuesTable;
@property (weak, nonatomic) IBOutlet UILabel *updateLabel;

@property NSArray *dataArray;
@property NSMutableString *topValues;
@end

@implementation XYZValuesViewController

- (IBAction)toSetReminder
{
    UIStoryboard *sb = [UIStoryboard storyboardWithName:@"Alt" bundle:nil];
    UINavigationController *destVC = (UINavigationController *)[sb instantiateViewControllerWithIdentifier:@"ReminderVC"];
    
    [self presentViewController:destVC animated:YES completion:nil];
    XYZSetRemindersViewController *remindVC = [destVC.viewControllers objectAtIndex:0];
    remindVC.textToRemind = [NSString stringWithString:self.topValues];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if ([segue.identifier isEqualToString:@"pushDetail"]) {
        XYZDetailValueViewController *destVC = [segue destinationViewController];
        destVC.pickedValue = ((NSIndexPath *)sender).row;
    }
}

- (IBAction)unwindToValues:(UIStoryboardSegue *)segue
{
    self.updateLabel.text = [NSString stringWithFormat:@"Last update: %@",[[NSUserDefaults standardUserDefaults] objectForKey:@"LAST_UPDATE"]];
    [self.topValuesTable reloadData];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)checkAndLoadSavedData
{
    NSUserDefaults *usdef = [NSUserDefaults standardUserDefaults];
    if ([usdef boolForKey:@"ALL_SAVED_DATA"]) {
        // TO DO if there's saved data
    } else {
        // TO DO if there's no saved data
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Alt" bundle:nil];
        XYZMakeYourNewViewController *missionVC = (XYZMakeYourNewViewController *)[storyboard instantiateViewControllerWithIdentifier:@"MakeYourListVC"];
        
        [self presentViewController:missionVC animated:YES completion:nil];
    }
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.topValuesTable.dataSource = self;
    self.topValuesTable.delegate = self;
    [self checkAndLoadSavedData];
    self.topValues = [NSMutableString stringWithFormat:@""];
    self.updateLabel.text = [NSString stringWithFormat:@"Last update: %@",[[NSUserDefaults standardUserDefaults] objectForKey:@"LAST_UPDATE"]];
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark Table view methods

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 5;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *cellIdentifier = @"TopValueCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier forIndexPath:indexPath];
    
    NSUserDefaults *usdef = [NSUserDefaults standardUserDefaults];
    NSString *string = [NSString stringWithFormat:@"GIVEN_VALUE_%ld",(long)indexPath.row];
    cell.textLabel.text = [usdef objectForKey:string];
    self.topValues = [NSMutableString stringWithFormat:@"%@ %@",self.topValues,cell.textLabel.text];
    
    if ((indexPath.row % 2) == 0) {
        cell.backgroundColor = [[UIColor alloc] initWithRed:0.098f green:0.451f blue:0.623f alpha:1.0f];
        cell.contentView.backgroundColor = [[UIColor alloc] initWithRed:0.098f green:0.451f blue:0.623f alpha:1.0f];

    } else {
        cell.backgroundColor = [[UIColor alloc] initWithRed:0.090f green:0.357f blue:0.490f alpha:1.0f];
        cell.contentView.backgroundColor = [[UIColor alloc] initWithRed:0.090f green:0.357f blue:0.490f alpha:1.0f];

    }
    
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [self.topValuesTable deselectRowAtIndexPath:indexPath animated:NO];
    [self performSegueWithIdentifier:@"pushDetail" sender:indexPath];
}

@end
