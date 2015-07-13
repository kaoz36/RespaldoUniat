//
//  SCSearchResultVC.h
//  SIPAC
//
//  Created by Jaguar3 on 19/03/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import <UIKit/UIKit.h>

/**
 class that search for results
 */
@interface SCSearchResultVC : UIViewController <UITableViewDataSource, UITableViewDelegate>

@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet UILabel *LblResult;
@property (nonatomic, strong) NSMutableArray *searchResult;

@end
