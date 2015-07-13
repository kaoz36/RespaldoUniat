//
//  XYZValuesBeliefsViewController.m
//  XLR8
//
//  Created by Jaguar Labs on 24/03/14.
//  Copyright (c) 2014 Jaguar Labs. All rights reserved.
//

#import "XYZValuesBeliefsViewController.h"
#import "XYZMakeYourListItem.h"

@interface XYZValuesBeliefsViewController ()
@property (weak, nonatomic) IBOutlet UILabel *headerValueLabel;
@property (weak, nonatomic) IBOutlet UITextView *valueDescField;
@property (weak, nonatomic) IBOutlet UIPageControl *valuePager;
@property (weak, nonatomic) IBOutlet UIBarButtonItem *nextButton;
@property (weak, nonatomic) IBOutlet UILabel *infoBehaviors;

@end

@implementation XYZValuesBeliefsViewController

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    if (sender == self.nextButton) {
        NSLog(@"Segue prepared");
        [self saveDataArray:self.dataArray];
    }
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    [self.view endEditing:YES];
}

- (void)respondToSwipeGesture:(UISwipeGestureRecognizer *)swipeGesture
{
    switch (swipeGesture.direction) {
        case UISwipeGestureRecognizerDirectionLeft:
            [self showNextValue:swipeGesture];
            break;
            
        case UISwipeGestureRecognizerDirectionRight:
            [self showPreviousValue:swipeGesture];
            break;

        default:
            break;
    }
}

- (void)showPreviousValue:(id)sender
{
    NSInteger page = self.valuePager.currentPage;
    XYZMakeYourListItem *saveItem = [[XYZMakeYourListItem alloc] init];
    saveItem = [self.dataArray objectAtIndex:page];
    saveItem.behaviors = self.valueDescField.text;
    [self.dataArray replaceObjectAtIndex:page withObject:saveItem];
    
    if (page > 0) {
        page--;
        
        XYZMakeYourListItem *loadItem = [self.dataArray objectAtIndex:page];
        self.headerValueLabel.text = loadItem.givenValue;
        self.valueDescField.text = loadItem.behaviors;
        self.valuePager.currentPage = page;
        NSLog(@"Behaviors: %@",loadItem.behaviors);

        self.nextButton.title = @"Next";
    }
}

- (void)showNextValue:(id)sender
{
    NSInteger page = self.valuePager.currentPage;
    XYZMakeYourListItem *saveItem = [[XYZMakeYourListItem alloc] init];
    saveItem = [self.dataArray objectAtIndex:page];
    saveItem.behaviors = self.valueDescField.text;
    [self.dataArray replaceObjectAtIndex:page withObject:saveItem];
    
    if (sender == self.nextButton) {
        XYZMakeYourListItem *checkItem = [[XYZMakeYourListItem alloc] init];
        for (int i = 0; i < 5; i++) {
            checkItem = [self.dataArray objectAtIndex:i];
            NSString *rawString = checkItem.behaviors;
            NSCharacterSet *whitespace = [NSCharacterSet whitespaceAndNewlineCharacterSet];
            NSString *trimmedString = [rawString stringByTrimmingCharactersInSet:whitespace];
            NSLog(@"behavior length: %lu",(unsigned long)trimmedString.length);
            if (trimmedString.length == 0) {
                UIAlertView *alart = [[UIAlertView alloc] initWithTitle:@"Incomplete" message:@"Please fill all fields" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                [alart show];
                return;
            }
        }
        [self performSegueWithIdentifier:@"unwindToValues" sender:sender];
        
    } else if (page < 4) {
            page++;
            
            XYZMakeYourListItem *loadItem = [self.dataArray objectAtIndex:page];
            self.headerValueLabel.text = loadItem.givenValue;
            self.valueDescField.text = loadItem.behaviors;
            self.valuePager.currentPage = page;
            NSLog(@"Behaviors: %@",loadItem.behaviors);
    }
}

- (void)registerForKeyboardNotifications
{
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardDidShow:) name:UIKeyboardWillShowNotification object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardDidHide:) name:UIKeyboardWillHideNotification object:nil];
}

- (void)keyboardDidShow:(NSNotification *)notification
{
    
    if (self.valueDescField.isFirstResponder) {
        NSDictionary *info = [notification userInfo];
        CGSize kbSize = [[info objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue].size;
        
        [UIView animateWithDuration:1.0f animations:^{
            CGRect theFrame = self.view.frame;
            theFrame.origin.y -= kbSize.height - 70;
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
    
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateStyle:NSDateFormatterShortStyle];
    NSString *createdDate = [dateFormatter stringFromDate:[NSDate date]];
    
    [usdef setObject:createdDate forKey:@"LAST_UPDATE"];
    [usdef setBool:YES forKey:@"ALL_SAVED_DATA"];
    [usdef synchronize];
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)loadInitialData
{
    XYZMakeYourListItem *loadItem = [self.dataArray objectAtIndex:0];
    self.headerValueLabel.text = loadItem.givenValue;
    self.valueDescField.text = loadItem.behaviors;
    self.valuePager.currentPage = 0;
    NSLog(@"Behaviors: %@",loadItem.behaviors);
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self loadInitialData];
    [self registerForKeyboardNotifications];
    [self.nextButton setTarget:self];
    [self.nextButton setAction:@selector(showNextValue:)];
    
    UISwipeGestureRecognizer *moveRightGesture = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(respondToSwipeGesture:)];
    UISwipeGestureRecognizer *moveLeftGesture = [[UISwipeGestureRecognizer alloc] initWithTarget:self action:@selector(respondToSwipeGesture:)];
    
    moveRightGesture.direction = UISwipeGestureRecognizerDirectionRight;
    moveLeftGesture.direction = UISwipeGestureRecognizerDirectionLeft;
    
    [self.view addGestureRecognizer:moveRightGesture];
    [self.view addGestureRecognizer:moveLeftGesture];
    
    [self.infoBehaviors setText:@"What do you do when you practice this value?\nFor example: Value - Happiness,\n Behavior - Dancing\nWhen I am happy I dance"];
    
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
