//
//  XYZEditHistoryMissionViewController.m
//  XLR8
//
//  Created by Jaguar Labs on 07/03/14.
//  Copyright (c) 2014 Jaguar Labs. All rights reserved.
//

#import "XYZEditHistoryMissionViewController.h"

@interface XYZEditHistoryMissionViewController ()
@property (weak, nonatomic) IBOutlet UITextView *editSelectedMissionBox;
@property (weak, nonatomic) IBOutlet UIButton *saveEditMissionButton;
@property (weak, nonatomic) IBOutlet UITextView *questionsInformation;


@end

@implementation XYZEditHistoryMissionViewController

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    [self.view endEditing:YES];
}

- (BOOL)shouldPerformSegueWithIdentifier:(NSString *)identifier sender:(id)sender
{
    if (sender == self.saveEditMissionButton) {
        NSString *rawString = [self.editSelectedMissionBox text];
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

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if (sender != self.saveEditMissionButton) return;
    if (self.editSelectedMissionBox.text.length > 0) {
        self.missionItem = [[XYZMissionItem alloc] init];
        self.missionItem.missionDescription = self.editSelectedMissionBox.text;
        
        NSUserDefaults *defs = [NSUserDefaults standardUserDefaults];
        NSInteger missionCount = [defs integerForKey:@"MISSION_COUNT"];
        missionCount = missionCount - self.selectedRow;
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

- (void)registerForKeyboardNotifications
{
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardDidShow:) name:UIKeyboardWillShowNotification object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardDidHide:) name:UIKeyboardWillHideNotification object:nil];
}

- (void)keyboardDidShow:(NSNotification *)notification
{
    
    //CGRect viewFrame = self.view.frame;
    
    if (self.editSelectedMissionBox.isFirstResponder) {
        NSDictionary *info = [notification userInfo];
        CGSize kbSize = [[info objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
        
        //[self.view setFrame:CGRectMake(0,- kbSize.height, viewFrame.size.width, viewFrame.size.height)];
        
        [UIView animateWithDuration:1.0f animations:^{
            CGRect theFrame = self.view.frame;
            theFrame.origin.y -= kbSize.height;
            self.view.frame = theFrame;
        }];
    }
}

- (void)keyboardDidHide:(NSNotification *)notification
{
    //CGRect viewFrame = self.view.frame;
    //[self.view setFrame:CGRectMake(0, 0, viewFrame.size.width, viewFrame.size.height)];
    
    [UIView animateWithDuration:1.0f animations:^{
        CGRect theFrame = self.view.frame;
        theFrame.origin.y = 0;
        self.view.frame = theFrame;
    }];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self registerForKeyboardNotifications];
	// Do any additional setup after loading the view.
    NSUserDefaults *defs = [NSUserDefaults standardUserDefaults];
    NSInteger missionCount = [defs integerForKey:@"MISSION_COUNT"];
    missionCount = missionCount - self.selectedRow;
    NSString *selectedMission = [NSString stringWithFormat:@"MISSION_%ld",(long)missionCount];
    [self.editSelectedMissionBox setText:[defs objectForKey:selectedMission]];
    [self.questionsInformation setText:@"If it weren't for money, time and circumstance, what would I really like to do with my life?\nWhat did I love to do that I did really well in my childhood?\nWhat would I love to be doing in my life right now?\nWhat would I love to do that would improve other people's lives and increase my self-respect and fulfillment?"];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
