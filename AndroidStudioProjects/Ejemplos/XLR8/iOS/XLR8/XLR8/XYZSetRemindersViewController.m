//
//  XYZSetRemindersViewController.m
//  XLR8
//
//  Created by Jaguar Labs on 07/03/14.
//  Copyright (c) 2014 Jaguar Labs. All rights reserved.
//

#import <EventKit/EventKit.h>
#import "XYZSetRemindersViewController.h"

@interface XYZSetRemindersViewController ()
@property (weak, nonatomic) IBOutlet UISwitch *repeatSwitch;
@property (weak, nonatomic) IBOutlet UISwitch *dailySwitch;
@property (weak, nonatomic) IBOutlet UISwitch *weeklySwitch;
@property (weak, nonatomic) IBOutlet UISwitch *monthlySwitch;
@property (weak, nonatomic) IBOutlet UIDatePicker *datePicker;
@property (weak, nonatomic) IBOutlet UIBarButtonItem *saveButton;
@property (weak, nonatomic) IBOutlet UIBarButtonItem *backButton;

@end

@implementation XYZSetRemindersViewController

- (void)exitReminders:(id)sender
{
    if (sender == self.saveButton)
    {
        if ([self checkDate]) {
            EKEventStore *store = [[EKEventStore alloc] init];
            [store requestAccessToEntityType:EKEntityTypeEvent completion:^(BOOL granted, NSError *error){
                
                if (!granted) {
                    UIAlertView *alart = [[UIAlertView alloc] initWithTitle:@"Alert" message:@"Reminder could not be set.\nPlease allow calendar access in your Privacy settings" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                    [alart show];
                    return;
                }
                EKEvent *event = [EKEvent eventWithEventStore:store];
                event.title = self.textToRemind;
                event.startDate = self.datePicker.date;
                event.endDate = [event.startDate dateByAddingTimeInterval:60*60];
                
                if (self.repeatSwitch.isOn) {
                    EKRecurrenceFrequency customFreq;
                    
                    if (self.dailySwitch.isOn) {
                        customFreq = EKRecurrenceFrequencyDaily;
                    } else if (self.weeklySwitch.isOn) {
                        customFreq = EKRecurrenceFrequencyWeekly;
                    } else {
                        customFreq = EKRecurrenceFrequencyMonthly;
                    }
                    
                    EKRecurrenceRule *customRecurrence = [[EKRecurrenceRule alloc] init];
                    customRecurrence = [customRecurrence initRecurrenceWithFrequency:customFreq interval:1 end:nil];
                    
                    [event addRecurrenceRule:customRecurrence];
                }
                
                [event setCalendar:[store defaultCalendarForNewEvents]];
                NSError *err = nil;
                [store saveEvent:event span:EKSpanThisEvent commit:YES error:&err];
                
            }];
            
            NSLog(@"Reminder Saved");
        } else {
            return;
        }
        
    }
    [self.navigationController dismissViewControllerAnimated:YES completion:nil];
    
}

- (BOOL)checkDate
{
        NSDate *dateToRemind = self.datePicker.date;
        NSDate *currentDate = [NSDate date];
        
        NSCalendar *gregorian = [[NSCalendar alloc] initWithCalendarIdentifier:NSGregorianCalendar];
        NSDateComponents *remindComponents = [gregorian components:(NSYearCalendarUnit | NSMonthCalendarUnit | NSDayCalendarUnit) fromDate:dateToRemind];
        NSDateComponents *currentComponents = [gregorian components:(NSYearCalendarUnit | NSMonthCalendarUnit | NSDayCalendarUnit) fromDate:currentDate];
        
        dateToRemind = [gregorian dateFromComponents:remindComponents];
        currentDate = [gregorian dateFromComponents:currentComponents];
        
        if ([currentDate isEqualToDate:dateToRemind] || [[currentDate earlierDate:dateToRemind] isEqualToDate:dateToRemind]) {
            UIAlertView *alart = [[UIAlertView alloc] initWithTitle:@"Alert" message:@"Please set a future date" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [alart show];
            return NO;
        }
    return YES;
}

- (void)checkRepeatSwitch:(id)sender
{
    if (sender == self.repeatSwitch) {
        if (self.repeatSwitch.on) {
            [self.dailySwitch setEnabled:YES];
            [self.weeklySwitch setEnabled:YES];
            [self.monthlySwitch setEnabled:YES];
        } else {
            [self.dailySwitch setEnabled:NO];
            [self.weeklySwitch setEnabled:NO];
            [self.monthlySwitch setEnabled:NO];
        }
    } else if (sender == self.dailySwitch){
        [self.monthlySwitch setOn:NO animated:YES];
        [self.weeklySwitch setOn:NO animated:YES];
        [self.dailySwitch setOn:YES];

    } else if (sender == self.weeklySwitch){
        [self.monthlySwitch setOn:NO animated:YES];
        [self.dailySwitch setOn:NO animated:YES];
        [self.weeklySwitch setOn:YES];

    } else if (sender == self.monthlySwitch){
        [self.dailySwitch setOn:NO animated:YES];
        [self.weeklySwitch setOn:NO animated:YES];
        [self.monthlySwitch setOn:YES];

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
    [super viewDidLoad];
    [self.repeatSwitch setOn:NO];
    [self checkRepeatSwitch:self.repeatSwitch];
    [self.repeatSwitch addTarget:self action:@selector(checkRepeatSwitch:) forControlEvents:UIControlEventValueChanged];
    [self.dailySwitch addTarget:self action:@selector(checkRepeatSwitch:) forControlEvents:UIControlEventValueChanged];
    [self.weeklySwitch addTarget:self action:@selector(checkRepeatSwitch:) forControlEvents:UIControlEventValueChanged];
    [self.monthlySwitch addTarget:self action:@selector(checkRepeatSwitch:) forControlEvents:UIControlEventValueChanged];
    [self.backButton setTarget:self];
    [self.backButton setAction:@selector(exitReminders:)];
    [self.saveButton setTarget:self];
    [self.saveButton setAction:@selector(exitReminders:)];
    NSLog(@"Initialized SetReminders");

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
/*
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
#warning Potentially incomplete method implementation.
    // Return the number of sections.
    return 0;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
#warning Incomplete method implementation.
    // Return the number of rows in the section.
    return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier forIndexPath:indexPath];
    
    // Configure the cell...
    
    return cell;
}
*/
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
