//
//  SCSearchVC.h
//  SIPAC
//
//  Created by Jaguar3 on 19/03/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import <UIKit/UIKit.h>

/**
  class that search the information
 */
@interface SCSearchVC : UIViewController <UITableViewDataSource,UITableViewDelegate,UISearchBarDelegate,UISearchBarDelegate>

- (IBAction)SearchPressed:(id)sender;
- (IBAction)autoSuggest:(id)sender;

@property (weak, nonatomic) IBOutlet UISearchBar *searchBar;
@property (weak, nonatomic) IBOutlet UISwitch *theSwitch;
@end
