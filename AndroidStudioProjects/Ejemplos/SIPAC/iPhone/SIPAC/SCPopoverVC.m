//
//  SCPopoverVCViewController.m
//  SIPAC
//
//  Created by Jaguar3 on 16/05/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import "SCPopoverVC.h"

@interface SCPopoverVC () {
    NSMutableArray *locations;
}

@end

@implementation SCPopoverVC

@synthesize delegate = _delegate;

- (id)initWithLocationArray:(NSMutableArray*)locationArray{
    self = [self initWithNibName:@"SCPopoverVC" bundle:nil];
    locations = locationArray;
    return self;
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
    // Do any additional setup after loading the view from its nib.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark UITableViewDataSourceDelegate

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return locations.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = (UITableViewCell*)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        
    }
    
    NSDictionary *row = [locations objectAtIndex:indexPath.row];
    cell.textLabel.text = [row objectForKey:@"nombre"];
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    if ([self.delegate respondsToSelector: @selector(didSelectPopoverRowAtIndex:)]){
        [self.delegate didSelectPopoverRowAtIndex:indexPath.row];
    }
}

@end
