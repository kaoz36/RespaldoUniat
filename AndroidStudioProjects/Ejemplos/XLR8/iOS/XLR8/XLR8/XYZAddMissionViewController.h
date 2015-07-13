//
//  XYZAddMissionViewController.h
//  XLR8
//
//  Created by Jaguar Labs on 04/03/14.
//  Copyright (c) 2014 Jaguar Labs. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "XYZMissionItem.h"

@interface XYZAddMissionViewController : UIViewController <UITextFieldDelegate>

@property XYZMissionItem *missionItem;
@property (weak, nonatomic) IBOutlet UITextView *questionsInformation;
@property (weak, nonatomic) IBOutlet UIButton *urlLabel;

- (IBAction)openURLWorksheets:(id)sender;
@end
