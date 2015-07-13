//
//  GridTableViewCell.m
//  Grid
//
//  Created by Sean Casserly on 4/6/11.
//  Copyright 2011 Sean Casserly. All rights reserved.
//

#import "GridTableViewCell.h"

@implementation GridTableViewCell

@synthesize lineColor, topCell, labelsArray, changeColor;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier defaultWitdh:(int)theDefaultWidth defaultHeight:(int)theDefaultHeight numberOfColumns:(int)theNumberOfColumns FontSize:(float)fontSize
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {

        changeColor = NO;
		topCell = NO;
        defaultWitdh = theDefaultWidth;
        defaultHeight = theDefaultHeight;
		
		// Add labels for the three cells
        labelsArray = [[NSMutableArray alloc] init];
        for (int x = 0; x<theNumberOfColumns; x++) {
            UITextView *label = [[UITextView alloc] initWithFrame:CGRectMake(defaultWitdh*x, 0, defaultWitdh, defaultHeight)];
            label.textAlignment = NSTextAlignmentCenter;
            label.editable = NO;
            //label.lineBreakMode = NSLineBreakByWordWrapping;
            [label setFont:[UIFont systemFontOfSize:fontSize]];
            label.backgroundColor = [UIColor clearColor];
            label.tag = x;
            label.text = [NSString stringWithFormat:@"%d",x+1];
            [labelsArray addObject:label];
            [self addSubview:label];
        }

    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)drawRect:(CGRect)rect
{
	CGContextRef context = UIGraphicsGetCurrentContext();
    
    if (topCell) {
        
        CGContextMoveToPoint(context, rect.origin.x, rect.origin.y);
        CGContextAddLineToPoint(context, rect.size.width, rect.origin.y);
        CGContextAddLineToPoint(context, rect.size.width, rect.size.height);
        CGContextAddLineToPoint(context, rect.origin.x, rect.size.height);
        CGContextAddLineToPoint(context, rect.origin.x, rect.origin.y);
        
        CGContextSetRGBFillColor(context, 0.109, 0.458, 0.737, 1);
        self.contentView.backgroundColor = [UIColor colorWithRed:0.109 green:0.458 blue:0.737 alpha:1];
        CGContextFillPath(context);
    }else{
        if (changeColor) {
           
            CGContextMoveToPoint(context, rect.origin.x, rect.origin.y);
            CGContextAddLineToPoint(context, rect.size.width, rect.origin.y);
            CGContextAddLineToPoint(context, rect.size.width, rect.size.height);
            CGContextAddLineToPoint(context, rect.origin.x, rect.size.height);
            CGContextAddLineToPoint(context, rect.origin.x, rect.origin.y);
            
            CGContextSetRGBFillColor(context, 0.396, 0.603, 0.823, 1);
            self.contentView.backgroundColor = [UIColor colorWithRed:0.396 green:0.603 blue:0.823 alpha:1];
            CGContextFillPath(context);
        }else{
            CGContextMoveToPoint(context, rect.origin.x, rect.origin.y);
            CGContextAddLineToPoint(context, rect.size.width, rect.origin.y);
            CGContextAddLineToPoint(context, rect.size.width, rect.size.height);
            CGContextAddLineToPoint(context, rect.origin.x, rect.size.height);
            CGContextAddLineToPoint(context, rect.origin.x, rect.origin.y);
            
            CGContextSetRGBFillColor(context, 1.0, 1.0, 1.0, 1);
            self.contentView.backgroundColor = [UIColor colorWithRed:1.0 green:1.0 blue:1.0 alpha:1];
            CGContextFillPath(context);
        }
    }
    
    
    
    CGContextSetRGBStrokeColor(context, 0.905, 0.901, 0.898, 1);
	
	// CGContextSetLineWidth: The default line width is 1 unit. When stroked, the line straddles the path, with half of the total width on either side.
	// Therefore, a 1 pixel vertical line will not draw crisply unless it is offest by 0.5. This problem does not seem to affect horizontal lines.
	CGContextSetLineWidth(context, 1.0);

	// Add the vertical lines
    for (int x = 0; x<=labelsArray.count; x++) {
        if (x==0) {
            CGContextMoveToPoint(context, +0.5, 0);
            CGContextAddLineToPoint(context, +0.5, rect.size.height);
        }else{
            CGContextMoveToPoint(context, (defaultWitdh * x)+0.5, 0);
            CGContextAddLineToPoint(context, (defaultWitdh * x)+0.5, rect.size.height);
        }
    }

	// Add bottom line
	CGContextMoveToPoint(context, 0, rect.size.height);
    CGContextAddLineToPoint(context, (defaultWitdh * labelsArray.count), rect.size.height-0.5);
	//CGContextAddLineToPoint(context, rect.size.width, rect.size.height-0.5);
	
	// If this is the topmost cell in the table, draw the line on top
	if (topCell)
	{
        CGContextMoveToPoint(context, +0.5, 0);
        CGContextAddLineToPoint(context, +0.5, rect.size.height);
        
		CGContextMoveToPoint(context, 0, 0);
		CGContextAddLineToPoint(context, (defaultWitdh * labelsArray.count), 0);
        
        CGContextMoveToPoint(context, (defaultWitdh * labelsArray.count)-0.5, 0);
        CGContextAddLineToPoint(context, (defaultWitdh * labelsArray.count)-0.5, rect.size.height);
	}
	
	// Draw the lines
	CGContextStrokePath(context); 
}

- (void)setTopCell:(BOOL)newTopCell
{
	topCell = newTopCell;
	[self setNeedsDisplay];
}

- (void)setLabelsForCell:(NSString*)firstLabelText, ... {
    NSMutableArray *Strings = [NSMutableArray array];
    va_list args;
    va_start(args, firstLabelText);
    
    //processing logic
    for (NSString* arg = firstLabelText; arg != nil; arg = va_arg(args, NSString*)) {
        [Strings addObject:arg];
    }
    
    va_end(args);
    
    for (int x = 0; x<labelsArray.count; x++) {
        UILabel *tmpLabel = [labelsArray objectAtIndex:x];
        if (Strings.count > x) {
            if (topCell) {
                tmpLabel.textColor = [UIColor whiteColor];
            }else{
                tmpLabel.textColor = [UIColor blackColor];
            }
            NSString *tmpString = [Strings objectAtIndex:x];
            tmpLabel.text = tmpString;
        }
    }
}

@end
