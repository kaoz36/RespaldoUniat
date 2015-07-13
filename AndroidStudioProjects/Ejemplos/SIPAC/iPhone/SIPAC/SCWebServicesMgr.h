//
//  SCWebServicesMgr.h
//  SIPAC
//
//  Created by Jaguar3 on 04/04/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BWConnectionManager.h"
#import <CoreLocation/CoreLocation.h>

typedef enum {
    sendLocationTypeHistory,
    sendLocationTypeRegister
}sendLocationType;

/**
 Development URL
 */
//#define WSURL @"http://bajiomedia.com/sipac/"
/**
 Production URL
 */
#define WSURLClient @"http://sipac-app.com.mx/index.php/"

enum connectionTags {
    kConnectionTagLogIn,
    kConnectionTagLocations,
    kConnectionTagNewLocations,
    kConnectionTagMakeCheckIn,
    kConnectionTagAllProfessions,
    kConnectionTagAllAges,
    kConnectionTagGetAllProspection,
    kConnectionTagSaveConsultLocation,
    kConnectionTagGetAllPolizas,
    kConnectionTagGetAllCoberturas,
    kConnectionTagGetAllServicios,
    kConnectionTagGetAllServiciosVenta,
    kConnectionTagGetAllServiciosInterno
    };


@interface SCWebServicesMgr : NSObject <CLLocationManagerDelegate,BWConnectionManagerDelegate>

+ (SCWebServicesMgr *)sharedInstance;

+ (void)WSSetLogin:(NSString*)userName ThePassword:(NSString*)pass Delegate:(id)theDelegate;
+ (void)WSGetLocationsWithDelegate:(id)theDelegate;
+ (void)WSSetNewLocation:(NSDictionary*)location Delegate:(id)theDelegate;
+ (void)WSMakeCheckInLatidude:(float)latitude Longitude:(float)longitude PlaceId:(int)placeId Delegate:(id)theDelegate;
- (void)SWSaveLocationWithSendLocationType:(sendLocationType)locationType AndDelegate:(id)theDelegate;

+ (void)parseLogin:(NSDictionary*)dictionary userName:(NSString*)userName userPass:(NSString*)userPass;

- (void)download;

@end