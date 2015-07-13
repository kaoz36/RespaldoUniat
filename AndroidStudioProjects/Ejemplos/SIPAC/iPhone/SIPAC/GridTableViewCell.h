//
//  GridTableViewCell.h
//  Grid
//
//  Created by Sean Casserly on 4/6/11.
//  Copyright 2011 Sean Casserly. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface GridTableViewCell : UITableViewCell {
	
	UIColor *lineColor;
	BOOL topCell;
    int defaultWitdh;
    int defaultHeight;
    
    
}

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier defaultWitdh:(int)theDefaultWidth defaultHeight:(int)theDefaultHeight numberOfColumns:(int)theNumberOfColumns  FontSize:(float)fontSize;
- (void)setLabelsForCell:(id)firstObject, ...;

@property (nonatomic, retain) UIColor* lineColor;
@property (nonatomic) BOOL topCell;
@property (nonatomic) bool changeColor;
@property (nonatomic, retain) NSMutableArray *labelsArray;


@end
