//
//  SCGeolocalization.h
//  SIPAC
//
//  Created by Jaguar3 on 08/05/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import "SCWebServicesMgr.h"
#import "SCPopoverVC.h"

/**
 Class that present map that locates the location of the pins created
 */
@interface SCGeolocalization : UIViewController <MKMapViewDelegate,BWConnectionManagerDelegate,UITextFieldDelegate,SCPopoverVCDelegate>

@end
