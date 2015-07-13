//
//  SCCustomPin.m
//  SIPAC
//
//  Created by Jaguar3 on 08/05/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import "SCCustomPin.h"


@interface SCCustomPin (){
   
}

@property (nonatomic, copy) NSString *placeId;
@property (nonatomic, copy) NSString *name;
@property (nonatomic, copy) NSString *address;
@property (nonatomic, assign) CLLocationCoordinate2D theCoordinate;

@end

@implementation SCCustomPin

- (id)initWithName:(NSString*)name address:(NSString*)address placeId:(NSString*)placeId coordinate:(CLLocationCoordinate2D)coordinate{
    if ((self = [super init])) {
        
        if ([name isKindOfClass:[NSString class]]) {
            self.name = name;
        } else {
            self.name = @"Unknown";
        }

        self.placeId = placeId;
        self.address = address;
        self.theCoordinate = coordinate;
        
    }
    return self;
}

- (NSString *)title {
    return _name;
}

- (NSString *)subtitle {
    return _address;
}

- (CLLocationCoordinate2D)coordinate {
    return _theCoordinate;
}

- (void)makeCheckIn{
    [SCWebServicesMgr WSMakeCheckInLatidude:self.theCoordinate.latitude Longitude:self.theCoordinate.longitude PlaceId:[self.placeId integerValue] Delegate:self];
}

- (void)didGetResponse:(id)response inConnectionTag:(int)tag{
    UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Aviso" message:@"El checkin se realizó exitosamente." delegate:Nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
    [alert show];
}

@end
