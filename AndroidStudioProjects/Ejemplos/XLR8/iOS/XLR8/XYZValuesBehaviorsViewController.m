//
//  XYZValuesBehaviorsViewController.m
//  XLR8
//
//  Created by Jaguar Labs on 27/03/14.
//  Copyright (c) 2014 Jaguar Labs. All rights reserved.
//

#import "XYZValuesBehaviorsViewController.h"
#import "XYZMakeYourListItem.h"
#import "XYZValuesBeliefsViewController.h"

@interface XYZValuesBehaviorsViewController ()

@property (weak, nonatomic) IBOutlet UITextField *firstValueField;
@property (weak, nonatomic) IBOutlet UITextField *secondValueField;
@property (weak, nonatomic) IBOutlet UITextField *thirdValueField;
@property (weak, nonatomic) IBOutlet UITextField *fourthValueField;
@property (weak, nonatomic) IBOutlet UITextField *fifthValueField;

@property (weak, nonatomic) IBOutlet UILabel *firstValue;
@property (weak, nonatomic) IBOutlet UILabel *secondValue;
@property (weak, nonatomic) IBOutlet UILabel *thirdValue;
@property (weak, nonatomic) IBOutlet UILabel *fourthValue;
@property (weak, nonatomic) IBOutlet UILabel *fifthValue;

@property (weak, nonatomic) IBOutlet UIBarButtonItem *nextButton;

@property NSMutableArray *labelArray;
@property NSMutableArray *fieldArray;

@end

@implementation XYZValuesBehaviorsViewController

- (BOOL)shouldPerformSegueWithIdentifier:(NSString *)identifier sender:(id)sender
{
    NSLog(@"Should perform segue?");
    if (sender == self.nextButton) {
        NSLog(@"Validating...");
        for (int i = 0; i < 5; i++) {
            UITextField *checkField = [self.fieldArray objectAtIndex:i];
            NSString *rawString = checkField.text;
            NSCharacterSet *whitespace = [NSCharacterSet whitespaceAndNewlineCharacterSet];
            NSString *trimmedString = [rawString stringByTrimmingCharactersInSet:whitespace];
            NSLog(@"behavior length: %lu",(unsigned long)trimmedString.length);
            if (trimmedString.length == 0) {
                UIAlertView *alart = [[UIAlertView alloc] initWithTitle:@"Incomplete" message:@"Please fill all fields" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                [alart show];
                return NO;
            }
        }
        
    }
    NSLog(@"YES");
    return YES;
}


- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
    if (sender == self.nextButton) {
        [self saveToArray];
        [self saveDataArray:self.dataArray];
        XYZValuesBeliefsViewController *destVC = [segue destinationViewController];
        destVC.dataArray = [NSMutableArray arrayWithArray:self.dataArray];
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

- (void)saveToArray
{
    for (int i = 0; i < 5; i++) {
        UITextField *saveField = [self.fieldArray objectAtIndex:i];
        ((XYZMakeYourListItem *)[self.dataArray objectAtIndex:i]).description = saveField.text;
    }
}

- (void)loadData
{
    self.labelArray = [[NSMutableArray alloc] init];
    [self.labelArray addObject:self.firstValue];
    [self.labelArray addObject:self.secondValue];
    [self.labelArray addObject:self.thirdValue];
    [self.labelArray addObject:self.fourthValue];
    [self.labelArray addObject:self.fifthValue];
    
    self.fieldArray = [[NSMutableArray alloc] init];
    [self.fieldArray addObject:self.firstValueField];
    [self.fieldArray addObject:self.secondValueField];
    [self.fieldArray addObject:self.thirdValueField];
    [self.fieldArray addObject:self.fourthValueField];
    [self.fieldArray addObject:self.fifthValueField];
    
    NSMutableString *labelString = [[NSMutableString alloc] init];
    for (int i = 0; i < 5; i++) {
        XYZMakeYourListItem *loadItem = [self.dataArray objectAtIndex:i];
        labelString = [NSMutableString stringWithFormat:@"%@ is important to me because",loadItem.givenValue];
        ((UILabel *)[self.labelArray objectAtIndex:i]).text = labelString;
        
        ((UITextField *)[self.fieldArray objectAtIndex:i]).text = loadItem.description;
    }
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self loadData];

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

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [self.tableView deselectRowAtIndexPath:indexPath animated:NO];
    [self.view endEditing:YES];
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
