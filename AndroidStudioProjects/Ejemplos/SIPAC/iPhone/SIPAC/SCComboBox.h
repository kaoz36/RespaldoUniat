//
//  SCComboBox.h
//  SIPAC
//
//  Created by Roy on 27/05/13.
//  Copyright (c) 2013 Roy. All rights reserved.
//

#import <UIKit/UIKit.h>

/**
 Class that present a comboBox
 */
@interface SCComboBox : UIView <UITableViewDataSource,UITableViewDelegate>

@property (nonatomic, retain) UIButton *theButton;
@property (nonatomic, retain) UITableView *theTableView;
@property (nonatomic, assign) int index;

@property (nonatomic, assign) BOOL GFH;
@property (nonatomic, assign) BOOL Ocupacion;
@property (nonatomic, assign) BOOL Pago;
@property (nonatomic, assign) BOOL Calculo;

@property (nonatomic, assign) BOOL CP1;
@property (nonatomic, assign) BOOL CP2;
@property (nonatomic, assign) BOOL CP3;

/**
 Initialize a comboBox with a button
 @param button the button with is initialized 
 @param isTableUp the YES if the table is up  otherwise returns NO.
 @param visibleOptionLimit the limit of the table
 @param TableDelegate the delegate
 @param onView the delegate
 @param dataSource the delegate
 @returns a newly initialized object
 */
- (id)initWithButton:(UIButton*)button isTableUp:(bool)isTableUp visibleOptionLimit:(int)thelimit TableDelegate:(id)delegate  onView:(UIView*)parent dataSource:(NSArray*)theDataSource;

- (void)showOrHideCombo;
- (void)didSelectRowOcupacion;
- (void)didSelectRowGFH;
- (void)didSelectRowCalculoWithIndex:(NSInteger)indexLocal;
- (void)didSelectRowPago;
- (void)didSelectRowCancerPLus1;
- (void)didSelectRowCancerPLus2;
- (void)didSelectRowCancerPLus3;

@end
