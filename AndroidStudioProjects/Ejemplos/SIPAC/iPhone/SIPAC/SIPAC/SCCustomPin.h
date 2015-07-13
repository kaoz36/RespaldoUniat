//
//  SCCustomPin.h
//  SIPAC
//
//  Created by Jaguar3 on 08/05/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>
#import "SCWebServicesMgr.h"

@interface SCCustomPin : MKAnnotationView <MKAnnotation,BWConnectionManagerDelegate>

- (id)initWithName:(NSString*)name address:(NSString*)address placeId:(NSString*)placeId coordinate:(CLLocationCoordinate2D)coordinate;

- (void)makeCheckIn;

@end
