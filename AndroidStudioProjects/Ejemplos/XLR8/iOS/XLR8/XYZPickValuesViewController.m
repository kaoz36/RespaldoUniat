//
//  XYZPickValuesViewController.m
//  XLR8
//
//  Created by Jaguar Labs on 21/03/14.
//  Copyright (c) 2014 Jaguar Labs. All rights reserved.
//

#import "XYZPickValuesViewController.h"
#import "XYZMakeYourListItem.h"
#import "XYZOrderValuesViewController.h"

@interface XYZPickValuesViewController ()
@property (weak, nonatomic) IBOutlet UIBarButtonItem *nextButton;

@property NSInteger pickCount;

@end

@implementation XYZPickValuesViewController

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    [self saveDataArray:self.dataArray];
    
    NSMutableArray *valuesToPass = [[NSMutableArray alloc] init];
    XYZMakeYourListItem *checkPickedItem = [[XYZMakeYourListItem alloc] init];
    NSInteger i = 0, added = 0;
    while (added < 10) {
        NSLog(@"get in here: %ld",(long)i);
        checkPickedItem = [self.dataArray objectAtIndex:i];
        if (checkPickedItem.checked) {
            [valuesToPass addObject:checkPickedItem];
            added++;
        }
        i++;
    }
    i = 0;
    added = 0;
    while (added < 10) {
        NSLog(@"and over here too: %ld",(long)i);
        checkPickedItem = [self.dataArray objectAtIndex:i];
        if (!checkPickedItem.checked) {
            [valuesToPass addObject:checkPickedItem];
            added++;
        }
        i++;
    }
    XYZOrderValuesViewController *destVC = [segue destinationViewController];
    destVC.valuesToOrder = [NSMutableArray arrayWithArray:valuesToPass];
    NSLog(@"values to pass count: %lu",(unsigned long)valuesToPass.count);
    NSLog(@"values to order count: %lu",(unsigned long)destVC.valuesToOrder.count);
    
}

- (BOOL)shouldPerformSegueWithIdentifier:(NSString *)identifier sender:(id)sender
{
    if (sender == self.nextButton) {
        if (self.pickCount != 10) {
            UIAlertView *alart = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Please pick exactly 10 values" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [alart show];
            return NO;
        }
    }
    return YES;
}

- (void)saveDataArray:(NSMutableArray *)array
{
    NSUserDefaults *usdef = [NSUserDefaults standardUserDefaults];
    NSMutableString *keyName = [[NSMutableString alloc] init];
    for (int i = 0; i < 20; i++) {
        XYZMakeYourListItem *objectToSave = [[XYZMakeYourListItem alloc] init];
        objectToSave = [array objectAtIndex:i];
        
        keyName = [NSMutableString stringWithFormat:@"LOVED_THING_%d",i];
        [usdef setObject:objectToSave.lovedThing forKey:keyName];
        
        keyName = [NSMutableString stringWithFormat:@"NEED_PEOPLE_%d",i];
        [usdef setBool:objectToSave.needPeople forKey:keyName];
        
        keyName = [NSMutableString stringWithFormat:@"NEED_MONEY_%d",i];
        [usdef setBool:objectToSave.needMoney forKey:keyName];
        
        keyName = [NSMutableString stringWithFormat:@"LAST_DATE_%d",i];
        [usdef setObject:objectToSave.dateDone forKey:keyName];
        
        keyName = [NSMutableString stringWithFormat:@"GIVEN_VALUE_%d",i];
        [usdef setObject:objectToSave.givenValue forKey:keyName];
        
        keyName = [NSMutableString stringWithFormat:@"CHECKED_%d",i];
        [usdef setBool:objectToSave.checked forKey:keyName];
        
        keyName = [NSMutableString stringWithFormat:@"VALUE_DESC_%d",i];
        [usdef setObject:objectToSave.description forKey:keyName];
        
        keyName = [NSMutableString stringWithFormat:@"VALUE_BEHAVE_%d",i];
        [usdef setObject:objectToSave.behaviors forKey:keyName];
    }
    
    [usdef setBool:YES forKey:@"SAVED_DATA"];
    [usdef synchronize];
}

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (NSInteger)countChecks
{
    NSInteger count = 0;
    for (int i = 0; i < 20; i++) {
        if (((XYZMakeYourListItem *)[self.dataArray objectAtIndex:i]).checked) {
            count++;
        }
        
    }
    return count;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.pickCount = [self countChecks];
    NSLog(@"Starting checks: %ld",(long)self.pickCount);
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
    return 20;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"ValueCell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    
    // Configure the cell...
    XYZMakeYourListItem *listItem = [self.dataArray objectAtIndex:indexPath.row];
    cell.textLabel.text = listItem.givenValue;
    if (listItem.checked) {
        cell.accessoryType = UITableViewCellAccessoryCheckmark;
    } else {
        cell.accessoryType = UITableViewCellAccessoryNone;
    }
    
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
    [tableView deselectRowAtIndexPath:indexPath animated:NO];
    XYZMakeYourListItem *tappedItem = [self.dataArray objectAtIndex:indexPath.row];
    if ((tappedItem.checked = !tappedItem.checked)) {
        self.pickCount++;
    } else {
        self.pickCount--;
    }
    NSLog(@"Picked count: %ld",(long)self.pickCount);
    [tableView reloadRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationNone];
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

/*
#pragma mark - Navigation

// In a story board-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}

 */

@end
