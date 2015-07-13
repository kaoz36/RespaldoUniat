//
//  XYZAddMissionViewController.m
//  XLR8
//
//  Created by Jaguar Labs on 04/03/14.
//  Copyright (c) 2014 Jaguar Labs. All rights reserved.
//

#import "XYZAddMissionViewController.h"

@interface XYZAddMissionViewController ()
@property (weak, nonatomic) IBOutlet UITextView *missionTextField;
@property (weak, nonatomic) IBOutlet UIButton *saveMissionButton;

@end

@implementation XYZAddMissionViewController

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    [self.view endEditing:YES];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [textField resignFirstResponder];
    return YES;
}

- (IBAction)openURLWorksheets:(id)sender
{
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"http://www.google.com/"]];
}

- (BOOL)shouldPerformSegueWithIdentifier:(NSString *)identifier sender:(id)sender
{
    if (sender == self.saveMissionButton) {
        NSString *rawString = [self.missionTextField text];
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
    if (sender != self.saveMissionButton) return;
    if (self.missionTextField.text.length > 0) {
        self.missionItem = [[XYZMissionItem alloc] init];
        self.missionItem.missionDescription = self.missionTextField.text;
        
        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
        [dateFormatter setDateStyle:NSDateFormatterShortStyle];
        NSString *createdDate = [dateFormatter stringFromDate:[NSDate date]];
        
        self.missionItem.creationDate = createdDate;
        self.missionItem.missionNewNumber = [[NSUserDefaults standardUserDefaults] integerForKey:@"MISSION_COUNT"] + 1;
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
    
    if (self.missionTextField.isFirstResponder) {
        NSDictionary *info = [notification userInfo];
        CGSize kbSize = [[info objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
        
        [UIView animateWithDuration:1.0f animations:^{
            CGRect theFrame = self.view.frame;
            theFrame.origin.y -= kbSize.height;
            self.view.frame = theFrame;
        }];
    }
}

- (void)keyboardDidHide:(NSNotification *)notification
{
    
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
    [self.questionsInformation setText:@"If it weren't for money, time and circumstance, what would I really like to do with my life?\nWhat did I love to do that I did really well in my childhood?\nWhat would I love to be doing in my life right now?\nWhat would I love to do that would improve other people's lives and increase my self-respect and fulfillment?"];
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
