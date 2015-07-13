//
//  XYZOrderValuesViewController.m
//  XLR8
//
//  Created by Jaguar Labs on 21/03/14.
//  Copyright (c) 2014 Jaguar Labs. All rights reserved.
//

#import "XYZOrderValuesViewController.h"
#import "XYZMakeYourListItem.h"
#import "XYZValuesBehaviorsViewController.h"

@interface XYZOrderValuesViewController ()

@end

@implementation XYZOrderValuesViewController

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

- (void)startEdit
{
    [super setEditing:YES];
    [self.tableView setEditing:YES];
    [self.tableView reloadData];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    //self.navigationItem.rightBarButtonItem = self.editButtonItem;
    
    [self startEdit];

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
    return 10;
}



- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"orderValueCell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    
    // Configure the cell...
    XYZMakeYourListItem *valueItem = [self.valuesToOrder objectAtIndex:indexPath.row];
    cell.textLabel.text = valueItem.givenValue;
    if (valueItem.needPeople) {
    //    cell.
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

-(UITableViewCellEditingStyle)tableView:(UITableView *)tableView editingStyleForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return UITableViewCellEditingStyleNone;
}

- (BOOL)tableView:(UITableView *)tableView shouldIndentWhileEditingRowAtIndexPath:(NSIndexPath *)indexPath
{
    return NO;
}

// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
{
    XYZMakeYourListItem *tmpItem = [[XYZMakeYourListItem alloc] init];
    tmpItem = [self.valuesToOrder objectAtIndex:fromIndexPath.row];

   // if (fromIndexPath.row > toIndexPath.row) {
        [self.valuesToOrder removeObjectAtIndex:fromIndexPath.row];
        [self.valuesToOrder insertObject:tmpItem atIndex:toIndexPath.row];
   // } else {
   //     [self.valuesToOrder insertObject:tmpItem atIndex:toIndexPath.row];
   //     [self.valuesToOrder removeObjectAtIndex:fromIndexPath.row];
   // }
    
    [self.tableView reloadData];
    NSLog(@"Moved value from %ld to %ld",(long)fromIndexPath.row,(long)toIndexPath.row);
    
}



// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}



#pragma mark - Navigation

// In a story board-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    if ([[segue identifier] isEqualToString:@"goToBeliefs"]) {
        [self saveDataArray:self.valuesToOrder];
        XYZValuesBehaviorsViewController *destVC = [segue destinationViewController];
        destVC.dataArray = [NSMutableArray arrayWithArray:self.valuesToOrder];
    }
    
}

 

@end
