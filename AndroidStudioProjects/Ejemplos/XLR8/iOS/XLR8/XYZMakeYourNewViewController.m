//
//  XYZMakeYourNewViewController.m
//  XLR8
//
//  Created by Jaguar Labs on 20/03/14.
//  Copyright (c) 2014 Jaguar Labs. All rights reserved.
//

#import "XYZMakeYourNewViewController.h"
#import "XYZMakeYourListItem.h"
#import "XYZPickValuesViewController.h"

#define MAX_THINGS 20

@interface XYZMakeYourNewViewController ()
@property (weak, nonatomic) IBOutlet UILabel *currentThingCountLabel;
@property (weak, nonatomic) IBOutlet UITextView *lovedThingField;
@property (weak, nonatomic) IBOutlet UISwitch *needPeopleSwitch;
@property (weak, nonatomic) IBOutlet UISwitch *needMoneySwitch;
@property (weak, nonatomic) IBOutlet UITextField *lastDateField;
@property (weak, nonatomic) IBOutlet UITextField *valueField;
@property (weak, nonatomic) IBOutlet UIBarButtonItem *nextButton;
@property (weak, nonatomic) IBOutlet UIButton *previousButton;

@property NSMutableArray *dataArray;
@property NSInteger currentCount;

- (void)nextButtonPress:(id)sender;

@end

@implementation XYZMakeYourNewViewController

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
        NSLog(@"Saving %@: %@",keyName,objectToSave.description);
        
        keyName = [NSMutableString stringWithFormat:@"VALUE_BEHAVE_%d",i];
        [usdef setObject:objectToSave.behaviors forKey:keyName];
    }
    
    [usdef setBool:YES forKey:@"SAVED_DATA"];
    [usdef synchronize];
}

- (NSMutableArray *)loadDataArray
{
    NSUserDefaults *usdef = [NSUserDefaults standardUserDefaults];
    NSMutableArray *loadArray = [[NSMutableArray alloc] init];
    
    if ([usdef boolForKey:@"SAVED_DATA"]) {
        NSMutableString *keyName = [[NSMutableString alloc] init];
        NSLog(@"There is saved data");
        for (int i = 0; i < 20; i++) {
            XYZMakeYourListItem *itemToLoad = [[XYZMakeYourListItem alloc] init];

            keyName = [NSMutableString stringWithFormat:@"LOVED_THING_%d",i];
            itemToLoad.lovedThing = [usdef objectForKey:keyName];
            NSLog(@"%@: %@",keyName,itemToLoad.lovedThing);
            
            keyName = [NSMutableString stringWithFormat:@"NEED_PEOPLE_%d",i];
            itemToLoad.needPeople = [usdef boolForKey:keyName];
            NSLog(@"%@: %hhd",keyName,itemToLoad.needPeople);

            keyName = [NSMutableString stringWithFormat:@"NEED_MONEY_%d",i];
            itemToLoad.needMoney = [usdef boolForKey:keyName];
            NSLog(@"%@: %hhd",keyName,itemToLoad.needMoney);

            keyName = [NSMutableString stringWithFormat:@"LAST_DATE_%d",i];
            itemToLoad.dateDone = [usdef objectForKey:keyName];
            NSLog(@"%@: %@",keyName,itemToLoad.dateDone);

            keyName = [NSMutableString stringWithFormat:@"GIVEN_VALUE_%d",i];
            itemToLoad.givenValue = [usdef objectForKey:keyName];
            NSLog(@"%@: %@",keyName,itemToLoad.givenValue);

            keyName = [NSMutableString stringWithFormat:@"CHECKED_%d",i];
            itemToLoad.checked = [usdef boolForKey:keyName];
            NSLog(@"%@: %hhd",keyName,itemToLoad.checked);

            keyName = [NSMutableString stringWithFormat:@"VALUE_DESC_%d",i];
            itemToLoad.description = [usdef objectForKey:keyName];
            NSLog(@"%@: %@",keyName,itemToLoad.description);

            keyName = [NSMutableString stringWithFormat:@"VALUE_BEHAVE_%d",i];
            itemToLoad.behaviors = [usdef objectForKey:keyName];
            NSLog(@"%@: %@",keyName,itemToLoad.behaviors);

            [loadArray addObject:itemToLoad];
        }
    } else {
        NSMutableString *string = [[NSMutableString alloc] init];
        NSLog(@"There's no saved data");
        for (int i = 0; i < 20; i++) {
            XYZMakeYourListItem *itemToLoad = [[XYZMakeYourListItem alloc] init];

            string = [NSMutableString stringWithFormat:@"Thing %d",i+1];
            itemToLoad.lovedThing = string;
            itemToLoad.needPeople = NO;
            itemToLoad.needMoney = NO;
            itemToLoad.dateDone = @"";
            string = [NSMutableString stringWithFormat:@"Value %d",i+1];
            itemToLoad.givenValue = string;
            itemToLoad.checked = NO;
            itemToLoad.description = @"";
            itemToLoad.behaviors = @"";
            
            [loadArray addObject:itemToLoad];
        }

    }
    
    return loadArray;
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    [self.view endEditing:YES];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    NSLog(@"preparing for segue");
    if (sender == self.nextButton) {
        [self saveDataArray:self.dataArray];
        NSLog(@"saving data");
        XYZPickValuesViewController *destVC = [segue destinationViewController];
        destVC.dataArray = /*[NSArray arrayWithArray:*/self.dataArray/*]*/;
    }
}

- (BOOL)shouldPerformSegueWithIdentifier:(NSString *)identifier sender:(id)sender
{
    NSLog(@"should perform segue?");
    if (sender == self.nextButton) {
        NSLog(@"checking if it's last thing to perform segue");
        return (self.currentCount == 20) ? YES : NO;
    }
    NSLog(@"it was another segue");
    return YES;
}

- (void)nextButtonPress:(id)sender
{
    if (sender == self.nextButton) {
        NSLog(@"Next button pressed");
        NSString *rawLovedString = self.lovedThingField.text;
        NSString *rawValueString = self.valueField.text;
        NSCharacterSet *whitespace = [NSCharacterSet whitespaceAndNewlineCharacterSet];
        NSString *trimmedLovedString = [rawLovedString stringByTrimmingCharactersInSet:whitespace];
        NSString *trimmedValueString = [rawValueString stringByTrimmingCharactersInSet:whitespace];
        if ((trimmedLovedString.length == 0) || (trimmedValueString.length == 0)) {
            UIAlertView *alart = [[UIAlertView alloc] initWithTitle:@"Incomplete" message:@"Please fill all fields" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
            [alart show];
            return;
        }
    }
     //else {
        XYZMakeYourListItem *newItem = [self.dataArray objectAtIndex:self.currentCount-1];
        newItem.lovedThing = self.lovedThingField.text;
        newItem.needPeople = self.needPeopleSwitch.isOn;
        newItem.needMoney = self.needMoneySwitch.isOn;
        newItem.dateDone = self.lastDateField.text;
        newItem.givenValue = self.valueField.text;
        newItem.checked = NO;
        //[self.dataArray replaceObjectAtIndex:self.currentCount-1 withObject:newItem];
        
        if (self.currentCount < 20) {
            [self resetFields:sender];
        } else {
            // TO DO move to next screen
            /*UIAlertView *alart = [[UIAlertView alloc] initWithTitle:@"Completed" message:@"Success" delegate:nil cancelButtonTitle:@"Yay" otherButtonTitles:nil];
            [alart show];
             */
            [self performSegueWithIdentifier:@"goToPickValues" sender:self.nextButton];
        }
    //}
}

- (void)resetFields:(id)sender
{
    if (sender == self.previousButton) {
        self.currentCount--;
        [self.view endEditing:YES];
        XYZMakeYourListItem *previousItem = [self.dataArray objectAtIndex:self.currentCount-1];
        NSString *countString = [NSString stringWithFormat:@"%ld / %d",(long)self.currentCount,MAX_THINGS];
        self.currentThingCountLabel.text = countString;
        self.lovedThingField.text = previousItem.lovedThing;
        [self.needPeopleSwitch setOn:previousItem.needPeople];
        [self.needMoneySwitch setOn:previousItem.needMoney];
        self.lastDateField.text = previousItem.dateDone;
        self.valueField.text = previousItem.givenValue;
        
        self.nextButton.title = @"Next";
        
        if (self.currentCount == 1) {
            [sender setHidden:YES];
        }
    } else {
        self.currentCount++;
        [self.view endEditing:YES];
        XYZMakeYourListItem *nextItem = [self.dataArray objectAtIndex:self.currentCount-1];
        NSString *countString = [NSString stringWithFormat:@"%ld / %d",(long)self.currentCount,MAX_THINGS];
        self.currentThingCountLabel.text = countString;
        
     //   NSString *thingString = [NSString stringWithFormat:@"Thing %d",self.currentCount];
        self.lovedThingField.text = nextItem.lovedThing;
        [self.needPeopleSwitch setOn:nextItem.needPeople];
        [self.needMoneySwitch setOn:nextItem.needMoney];
      //  NSString *valueString = [NSString stringWithFormat:@"Value %d",self.currentCount];
        self.lastDateField.text = nextItem.dateDone;
        self.valueField.text = nextItem.givenValue;
        
        [self.previousButton setHidden:NO];
        
        if (self.currentCount == 20) {
            self.nextButton.title = @"Finish";
        }
        NSLog(@"Thing %ld",(long)self.currentCount);
    }
    
    
}

- (void)loadInitialData
{
    self.dataArray = /*[NSMutableArray arrayWithArray:*/[self loadDataArray]/*]*/;
  //  for (int i = 0; i < 20; i++) {
  //      [self.dataArray addObject:[[XYZMakeYourListItem alloc] init]];
  //  }
    XYZMakeYourListItem *firstItem = [self.dataArray objectAtIndex:0];
    self.currentCount = 1;
    self.lovedThingField.text = firstItem.lovedThing;
    self.lastDateField.text = firstItem.dateDone;
    self.valueField.text = firstItem.givenValue;
    NSLog(@"Thing 1");
    
    NSString *countString = [NSString stringWithFormat:@"%ld / %d",(long)self.currentCount,MAX_THINGS];
    self.currentThingCountLabel.text = countString;
}

- (void)registerForKeyboardNotifications
{
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardDidShow:) name:UIKeyboardWillShowNotification object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardDidHide:) name:UIKeyboardWillHideNotification object:nil];
}

- (void)keyboardDidShow:(NSNotification *)notification
{
    
    //CGRect viewFrame = self.view.frame;
    
    if (self.lastDateField.isFirstResponder || self.valueField.isFirstResponder) {
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
    [self loadInitialData];
    [self registerForKeyboardNotifications];
    [self.nextButton setTarget:self];
    [self.nextButton setAction:@selector(nextButtonPress:)];
    [self.previousButton setHidden:YES];
    [self.previousButton addTarget:self action:@selector(nextButtonPress:) forControlEvents:UIControlEventTouchUpInside];
    
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    [self saveDataArray:self.dataArray];
    // Dispose of any resources that can be recreated.
}

@end
