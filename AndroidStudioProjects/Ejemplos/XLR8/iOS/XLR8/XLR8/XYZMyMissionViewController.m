//
//  XYZMyMissionViewController.m
//  XLR8
//
//  Created by Jaguar Labs on 04/03/14.
//  Copyright (c) 2014 Jaguar Labs. All rights reserved.
//

#import "XYZMyMissionViewController.h"
#import "XYZAddMissionViewController.h"
#import "XYZSetRemindersViewController.h"

@interface XYZMyMissionViewController ()
@property (weak, nonatomic) IBOutlet UILabel *currentMissionLabel;

@end

@implementation XYZMyMissionViewController

- (IBAction)toSetReminder
{
    UIStoryboard *sb = [UIStoryboard storyboardWithName:@"Alt" bundle:nil];
    UINavigationController *destVC = (UINavigationController *)[sb instantiateViewControllerWithIdentifier:@"ReminderVC"];
    
    [self presentViewController:destVC animated:YES completion:nil];
    XYZSetRemindersViewController *remindVC = [destVC.viewControllers objectAtIndex:0];
    remindVC.textToRemind = [NSString stringWithString:self.currentMissionLabel.text];
}

- (IBAction)unwindToMyMission:(UIStoryboardSegue *)segue
{
    NSString *segueId = [segue identifier];
    if ([segueId  isEqual: @"unwindFromSetReminders"]) {
        ;
    } else if ([segueId isEqual: @"unwindFromNewMission"] || [segueId isEqualToString: @"unwindFromEditMission"] ){
        XYZAddMissionViewController *source = [segue sourceViewController];
        XYZMissionItem *item = source.missionItem;
    
        if (item != nil) {
            [self.currentMissionLabel setText:item.missionDescription];
            NSUserDefaults *addUserDefaultsMission = [NSUserDefaults standardUserDefaults];
            NSString *newCurrentMission = [NSString stringWithFormat:@"MISSION_%ld",(long)item.missionNewNumber];
            NSString *newCreationDate = [NSString stringWithFormat:@"MISSION_%ld_DATE",(long)item.missionNewNumber];
        
            [addUserDefaultsMission setInteger:item.missionNewNumber forKey:@"MISSION_COUNT"];
            [addUserDefaultsMission setObject:item.missionDescription forKey:newCurrentMission];
            [addUserDefaultsMission setObject:item.creationDate forKey:newCreationDate];
        }
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
    NSLog(@"Start My Mission");
	// Do any additional setup after loading the view.
    NSUserDefaults *missionsUserDefaults = [NSUserDefaults standardUserDefaults];
    NSInteger lastMissionCount = [missionsUserDefaults integerForKey:@"MISSION_COUNT"];
    if (lastMissionCount != 0) {
        NSString *lastMissionKey = [NSString stringWithFormat:@"MISSION_%ld",(long)lastMissionCount];
        NSString *lastMission = [missionsUserDefaults stringForKey:lastMissionKey];
        [self.currentMissionLabel setText:lastMission];
    } else {
        UIStoryboard *storyboard = [UIStoryboard storyboardWithName:@"Alt" bundle:nil];
        XYZAddMissionViewController *missionVC = (XYZAddMissionViewController *)[storyboard instantiateViewControllerWithIdentifier:@"AddMissionVC"];
        
        [self presentViewController:missionVC animated:YES completion:nil];
        
        [missionVC.questionsInformation setHidden:NO];
        [missionVC.urlLabel setHidden:NO];
        
        [missionVC.questionsInformation setUserInteractionEnabled:YES];
        [missionVC.urlLabel setUserInteractionEnabled:YES];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
