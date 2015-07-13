//
//  SCComboBox.m
//  SIPAC
//
//  Created by Roy on 27/05/13.
//  Copyright (c) 2013 Roy. All rights reserved.
//

#import "SCComboBox.h"
#import <QuartzCore/QuartzCore.h>
#import "SCManager.h"

@implementation SCComboBox{
    bool isup;
    int limit;
    UIView *parentView;
    NSArray *dataSource;
    BOOL wasShown;
}

@synthesize theButton,theTableView,index;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

#pragma mark public Methods

- (id)initWithButton:(UIButton*)button isTableUp:(bool)isTableUp visibleOptionLimit:(int)thelimit TableDelegate:(id)delegate  onView:(UIView*)parent dataSource:(NSArray*)theDataSource{
    self = [self initWithFrame:button.frame];
    theButton = button;
    isup = isTableUp;
    limit = thelimit;
    parentView = parent;
    dataSource = theDataSource;
    [self prepareTable];
    
    return self;
}

- (void)showOrHideCombo{
    
    if (wasShown) {
        [self hide];
    }else{
        [self show];
    }
    
}

#pragma mark private Methods

- (void)prepareTable{
    
    theTableView = [[UITableView alloc] initWithFrame:[self GetOriginalFrame]];
    theTableView.delegate = self;
    theTableView.dataSource = self;
    theTableView.layer.cornerRadius = 5;
    theTableView.layer.borderColor = [UIColor grayColor].CGColor;
    theTableView.layer.borderWidth = 1.0;
    
    [parentView addSubview:theTableView];
    
    self.index = -1;
}

-(CGRect)GetOriginalFrame{
    CGRect frame;
    if (isup) {
        frame = CGRectMake(theButton.frame.origin.x, theButton.frame.origin.y, theButton
                           .frame.size.width, 0);
    }else{
        frame = CGRectMake(theButton.frame.origin.x, theButton.frame.origin.y + theButton.frame.size.height, theButton.frame.size.width, 0);
    }
    return frame;
}

-(CGRect)GetTargetFrame{
    CGSize size;
    int numberOfLines = 0;
    if([theTableView numberOfRowsInSection:0] >= limit) {
        numberOfLines = limit;
    }else{
        numberOfLines = [theTableView numberOfRowsInSection:0];
    }
    
    size = CGSizeMake([self GetOriginalFrame].size.width, [theTableView rowHeight]*numberOfLines);
    
    CGRect newframe;
    if (isup) {
       newframe = CGRectMake(theButton.frame.origin.x, theButton.frame.origin.y-size.height, size.width, size.height);
    }else{
       newframe = CGRectMake(theButton.frame.origin.x, theButton.frame.origin.y+theButton.frame.size.height, size.width, size.height);
    }
    
    return newframe;
}

-(void)show{
        [UIView animateWithDuration:0.3 animations:^(void){
            
            theTableView.frame = [self GetTargetFrame];
            
        }];
            
        [theTableView reloadData];
    wasShown = YES;
    [theButton setSelected:YES];
    
    if (self.index != -1) {
        NSIndexPath *indexPath = [NSIndexPath indexPathForRow:self.index inSection:0];
        [theTableView selectRowAtIndexPath:indexPath animated:YES scrollPosition:UITableViewScrollPositionMiddle];
    }
}


-(void)hide{
    [UIView animateWithDuration:0.3 animations:^(void){
        
        theTableView.frame = [self GetOriginalFrame];
        
    }];
    wasShown = NO;
    [theButton setSelected:NO];
}

- (void)didSelectRowOcupacion{
    [[SCManager sharedInstance].cotizadorVC selectOcupacion];
    [[SCManager sharedInstance].cotizadorVC initCotizador];
}

- (void)didSelectRowGFH{
    
    [[SCManager sharedInstance].cotizadorVC selectGFH];
    [[SCManager sharedInstance].cotizadorVC initCotizador];
}

- (void)didSelectRowPago{
    [[SCManager sharedInstance].cotizadorVC selectPago];
    [[SCManager sharedInstance].cotizadorVC initCotizador];
}

- (void)didSelectRowCalculo{
    [[SCManager sharedInstance].cotizadorVC selectCalculo];
    [[SCManager sharedInstance].cotizadorVC initCotizador];
}


- (void)didSelectRowCancerPLus1{
    [[SCManager sharedInstance].cotizadorVC initCotizador];
}

- (void)didSelectRowCancerPLus2{
    [[SCManager sharedInstance].cotizadorVC initCotizador];
}

- (void)didSelectRowCancerPLus3{
    [[SCManager sharedInstance].cotizadorVC initCotizador];
}


-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return dataSource.count;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = (UITableViewCell*)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.textLabel.font = [UIFont systemFontOfSize:14];
    }
    
    cell.textLabel.text = [dataSource objectAtIndex:indexPath.row];
    
    return cell;
    
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    self.index = indexPath.row;
    [theButton setTitle:[dataSource objectAtIndex:index] forState:UIControlStateNormal];
    [theButton setTitle:[dataSource objectAtIndex:index] forState:UIControlStateSelected];
    [self hide];
    
    if (self.GFH == YES) {
        [self didSelectRowGFH];
    }else if (self.Ocupacion == YES){
        [self didSelectRowOcupacion];
    }else if (self.Pago == YES){
        [self didSelectRowPago];
    }else if (self.Calculo == YES){
         [self didSelectRowCalculo];
    }
    if (self.CP1 == YES){
        [self didSelectRowCancerPLus1];
    }
    if (self.CP2 == YES){
        [self didSelectRowCancerPLus2];
    }
    if (self.CP3 == YES){
        [self didSelectRowCancerPLus3];
    }
}


@end
