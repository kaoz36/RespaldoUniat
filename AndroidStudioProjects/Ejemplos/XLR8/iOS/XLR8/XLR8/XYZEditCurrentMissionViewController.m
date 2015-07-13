//
//  XYZEditCurrentMissionViewController.m
//  XLR8
//
//  Created by Jaguar Labs on 05/03/14.
//  Copyright (c) 2014 Jaguar Labs. All rights reserved.
//

#import "XYZEditCurrentMissionViewController.h"
#import "XYZMyMissionViewController.h"

@interface XYZEditCurrentMissionViewController ()
@property (weak, nonatomic) IBOutlet UITextView *editCurrentMissionBox;
@property (weak, nonatomic) IBOutlet UIButton *saveEditMissionButton;
@property NSString *missionToEdit;

@end

@implementation XYZEditCurrentMissionViewController

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    [self.view endEditing:YES];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}

- (BOOL)shouldPerformSegueWithIdentifier:(NSString *)identifier sender:(id)sender
{
    if (sender == self.saveEditMissionButton) {
        NSString *rawString = [self.editCurrentMissionBox text];
        NSCharacterSet *whitespace = [NSCharacterSet whitespaceAndNewlineCharacterSet];
        NSString *trimmedString = [rawString stringByTrimmingCharactersInSet:whitespace];
        if (trimmedString.length == 0) {
            UIAlertView *alart = [[UIAlertView alloc] initWithTitle:@"No mission" message:@"You must input a mission" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [alart show];
            return NO;
        }
    }
    return YES;
}

- (void) prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if (sender != self.saveEditMissionButton) return;
    if (self.editCurrentMissionBox.text.length > 0) {
        self.missionItem = [[XYZMissionItem alloc] init];
        self.missionItem.missionDescription = self.editCurrentMissionBox.text;
        
        NSUserDefaults *defs = [NSUserDefaults standardUserDefaults];
        NSInteger missionCount = [defs integerForKey:@"MISSION_COUNT"];
        NSString *missionNumber = [NSString stringWithFormat:@"MISSION_%ld_DATE",(long)missionCount];
        NSString *savedCreatedDate = [defs objectForKey:missionNumber];
        
        self.missionItem.missionNewNumber = missionCount;
        self.missionItem.creationDate =  savedCreatedDate;
        }
}

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
	// Do any additional setup after loading the view.
    NSUserDefaults *defs = [NSUserDefaults standardUserDefaults];
    NSInteger missionCount = [defs integerForKey:@"MISSION_COUNT"];
    NSString *currentMission = [NSString stringWithFormat:@"MISSION_%ld",(long)missionCount];
    [self.editCurrentMissionBox setText:[defs objectForKey:currentMission]];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
