//
//  XYZMissionHistoryViewController.m
//  XLR8
//
//  Created by Jaguar Labs on 06/03/14.
//  Copyright (c) 2014 Jaguar Labs. All rights reserved.
//

#import "XYZMissionHistoryViewController.h"
#import "XYZMissionHistoryCell.h"
#import "XYZEditHistoryMissionViewController.h"

@interface XYZMissionHistoryViewController ()

@property NSMutableArray *missionList;
@property NSMutableArray *dateList;
@property NSInteger touchedRow;

@end

@implementation XYZMissionHistoryViewController


- (void)unwindToHistory:(UIStoryboardSegue *)segue
{
    XYZEditHistoryMissionViewController *source = [segue sourceViewController];
    XYZMissionItem *item = source.missionItem;
    
    if (item != nil) {
        NSUserDefaults *addUserDefaultsMission = [NSUserDefaults standardUserDefaults];
        NSString *newEditedMission = [NSString stringWithFormat:@"MISSION_%ld",(long)item.missionNewNumber];
  //      NSString *newCreationDate = [NSString stringWithFormat:@"MISSION_%d_DATE",item.missionNewNumber];
        
       // [addUserDefaultsMission setInteger:item.missionNewNumber forKey:@"MISSION_COUNT"];
        [addUserDefaultsMission setObject:item.missionDescription forKey:newEditedMission];
        // [addUserDefaultsMission setObject:item.creationDate forKey:newCreationDate];
        [self.missionList setObject:item.missionDescription atIndexedSubscript:self.touchedRow];
        
        
        [self.tableView reloadData];
    }
}

- (void)loadInitialData
{
    NSLog(@"load user defaults");
    NSUserDefaults *usdef = [NSUserDefaults standardUserDefaults];
    NSLog(@"get mission count");
    NSInteger missionCount = [usdef integerForKey:@"MISSION_COUNT"];
    NSLog(@"start mutable strings");
    NSMutableString *selectMission, *dateMission;
    
    NSLog(@"starting for");
    for (int i = (int)missionCount; i > 0; i--) {
        NSLog(@"for %d",i);
        selectMission = [NSMutableString stringWithFormat:@"MISSION_%d",i];
        selectMission = (NSMutableString *)[usdef objectForKey:selectMission];
        dateMission = [NSMutableString stringWithFormat:@"MISSION_%d_DATE",i];
        dateMission = (NSMutableString *)[usdef objectForKey:dateMission];
        
        if (selectMission == nil) {
            selectMission = (NSMutableString *)@"NO MISSION";
            dateMission = (NSMutableString *)@"NO DATE";
        }
        
        NSLog(@"Date: %@",dateMission);
        [self.missionList addObject:selectMission];
        [self.dateList addObject:dateMission];
    }
}

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    NSLog(@"Mission History did load");
    [super viewDidLoad];
    
    NSLog(@"alloc init missionList");
    self.missionList = [[NSMutableArray alloc] init];
    
    self.dateList = [[NSMutableArray alloc] init];
    NSLog(@"load initial data");
    [self loadInitialData];

    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
 
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    return self.missionList.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"missionCell";
    XYZMissionHistoryCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    NSInteger row = [indexPath row];
    
    // Configure the cell...
    cell.missionLabel.text = [self.missionList objectAtIndex:row];
    cell.dateLabel.text = [self.dateList objectAtIndex:row];
    
    if ((indexPath.row % 2) == 0) {
        cell.backgroundColor = [[UIColor alloc] initWithRed:0.098f green:0.451f blue:0.623f alpha:1.0f];
        cell.contentView.backgroundColor = [[UIColor alloc] initWithRed:0.098f green:0.451f blue:0.623f alpha:1.0f];
        
    } else {
        cell.backgroundColor = [[UIColor alloc] initWithRed:0.090f green:0.357f blue:0.490f alpha:1.0f];
        cell.contentView.backgroundColor = [[UIColor alloc] initWithRed:0.090f green:0.357f blue:0.490f alpha:1.0f];
        
    }
    
    return cell;
}

/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    }   
    else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
{
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/


#pragma mark - Navigation

// In a story board-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    NSLog(@"Preparing segue");
    if ([segue.identifier isEqualToString:@"EditHistoryMission"]) {
        XYZEditHistoryMissionViewController *evc = [segue destinationViewController];
        evc.selectedRow = self.touchedRow;
        NSLog(@"Segue prepared");
    }
    NSLog(@"Doing segue");
}



- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
  //  UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Alt" bundle:nil];
  //  XYZEditHistoryMissionViewController *missionVC = (XYZEditHistoryMissionViewController *)[storyboard instantiateViewControllerWithIdentifier:@"EditMissionHistory"];
    NSLog(@"Touched a row");
 //   [tableView deselectRowAtIndexPath:indexPath animated:NO];
    NSLog(@"Getting row info");
    self.touchedRow = [indexPath row];
    NSLog(@"Will perform segue");
    [self performSegueWithIdentifier:@"EditHistoryMission" sender:self];
    NSLog(@"Touched row: %ld",(long)self.touchedRow);
    
   // [self presentViewController:missionVC animated:YES completion:nil];
}

@end
