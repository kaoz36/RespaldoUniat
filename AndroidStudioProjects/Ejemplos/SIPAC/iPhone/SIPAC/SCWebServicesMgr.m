//
//  SCWebServicesMgr.m
//  SIPAC
//
//  Created by Jaguar3 on 04/04/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import "SCWebServicesMgr.h"
#import "SCDatabaseManager.h"
#import "SCManager.h"

#define WSPoliza @"promotorialocationcontroller/backupPolizas/%@/%d"
#define WSCoberturas @"promotorialocationcontroller/backupCoberturas/%@/%d"
#define WSProspeccion @"promotorialocationcontroller/backupProspecciones/%@/%d"
#define WSAllAged @"cotizadorcontroller/getAllEdadesCotizador"
#define WSAllProfesions @"cotizadorcontroller/getAllProfesionesCotizador"
#define WSServicios @"promotorialocationcontroller/backupServicios/%@/%d"
#define WSServiciosVenta @"promotorialocationcontroller/backupServiciosAfectacion/%@/%d"
#define WSServiciosInterno @"promotorialocationcontroller/backupServiciosInternos/%@/%d"


@interface SCWebServicesMgr (){
    CLLocationManager *locationMgr;
    id theLocationDelegate;
    int paginateGoal;
    int paginateValue;
    bool onRequest;
    NSMutableArray *downloadQueue;
    int theCurrent;
    int theTotal;
    NSTimer *timer;
    
    int currSendLocationType;
    BOOL locationSent;
}

@end

@implementation SCWebServicesMgr

+ (SCWebServicesMgr *)sharedInstance
{
    static dispatch_once_t pred;
    static SCWebServicesMgr *shared = nil;
    dispatch_once(&pred, ^{
        shared = [[SCWebServicesMgr alloc] init];
    });
    return shared;
}

+ (void)WSSetLogin:(NSString*)userName ThePassword:(NSString*)pass Delegate:(id)theDelegate{
    
    NSDictionary *post = @{@"username": userName,@"password":pass, @"deviceId" : [[SCDatabaseManager sharedInstance] getUniqueGeneratedIdentifier]};
    //NSDictionary *post = @{@"username": userName,@"password":pass};
    (void)[[BWConnectionManager alloc] initWithJSONPost:post withUrl:WSURLClient inWS:@"usuariocontroller/userLogin" delegate:theDelegate message:@"Iniciando Sesión..." withHud:YES requestWithTag:kConnectionTagLogIn ShowResponse:YES Resting3g:NO];
}

+ (void)WSGetLocationsWithDelegate:(id)theDelegate{
    NSDictionary *profile = [[SCDatabaseManager sharedInstance] GetProfile];
    NSDictionary *post = @{@"promotoria_id": [profile objectForKey:@"promotoria"]};
    (void)[[BWConnectionManager alloc] initWithJSONPost:post withUrl:WSURLClient inWS:@"promotorialocationcontroller/getAllPromotorias" delegate:theDelegate message:@"Obteniendo ubicaciones..." withHud:YES requestWithTag:kConnectionTagLocations ShowResponse:YES Resting3g:NO];
    
} 

+ (void)WSSetNewLocation:(NSDictionary*)location Delegate:(id)theDelegate{
//  {"nombre":"","retenedor":"","upago":"","prospectos":"","altitude":"","latitude":""}
    (void)[[BWConnectionManager alloc] initWithJSONPost:location withUrl:WSURLClient inWS:@"promotorialocationcontroller/registerPromotoria" delegate:theDelegate message:@"Guardando ubicación..." withHud:YES requestWithTag:kConnectionTagNewLocations ShowResponse:YES Resting3g:NO];
    
}

- (void)SWSaveLocationWithSendLocationType:(sendLocationType)locationType AndDelegate:(id)theDelegate{
    currSendLocationType = locationType;
    locationMgr = [[CLLocationManager alloc] init];
    locationMgr.delegate = self;
    [locationMgr startUpdatingLocation];
    [locationMgr startMonitoringSignificantLocationChanges];

    theLocationDelegate = theDelegate;
    locationSent = NO;
}

- (void)locationManager:(CLLocationManager *)manager didUpdateToLocation:(CLLocation *)newLocation fromLocation:(CLLocation *)oldLocation{
    
    if (!locationSent) {
        if (currSendLocationType == sendLocationTypeHistory) {
            NSDictionary *profile = [[SCDatabaseManager sharedInstance] GetProfile];
            NSDateFormatter *formater = [[NSDateFormatter alloc] init];
            [formater setTimeZone:[NSTimeZone systemTimeZone]];
            [formater setDateFormat:@"YYYY-MM-DD"];
            
            NSDictionary *post = @{@"id_usuario":[profile objectForKey:@"id_user"],
                                   @"latitude":[NSString stringWithFormat:@"%f",newLocation.coordinate.latitude],
                                   @"longitude":[NSString stringWithFormat:@"%f", newLocation.coordinate.longitude],
                                   @"fecha": [formater stringFromDate:[NSDate date]]};
            (void)[[BWConnectionManager alloc] initWithJSONPost:post withUrl:WSURLClient inWS:@"usuariocontroller/registerHistory" delegate:theLocationDelegate message:@"" withHud:NO requestWithTag:kConnectionTagSaveConsultLocation ShowResponse:YES Resting3g:NO];
        }else{
            NSDictionary *profile = [[SCDatabaseManager sharedInstance] GetProfile];
            
            NSDictionary *post = @{@"agente_id":[profile objectForKey:@"id_user"],
                                   @"latitude":[NSString stringWithFormat:@"%f",newLocation.coordinate.latitude],
                                   @"longitude":[NSString stringWithFormat:@"%f", newLocation.coordinate.longitude],
                                   @"promotoria_id": [profile objectForKey:@"promotoria"]};
            (void)[[BWConnectionManager alloc] initWithJSONPost:post withUrl:WSURLClient inWS:@"promotorialocationcontroller/saveLocation" delegate:theLocationDelegate message:@"" withHud:NO requestWithTag:kConnectionTagSaveConsultLocation ShowResponse:YES Resting3g:NO];
        }
        locationSent = YES;
    }
    
    
    
    [locationMgr stopMonitoringSignificantLocationChanges];
    [locationMgr stopUpdatingLocation];
}

+ (void)WSMakeCheckInLatidude:(float)latitude Longitude:(float)longitude PlaceId:(int)placeId Delegate:(id)theDelegate{
    NSDictionary *profile = [[SCDatabaseManager sharedInstance] GetProfile];
    //"agente_id":"32","promotoria_id":"mp","latitude":"50.5","longitude":"-6.34","ubicacion_id":"30"
    NSDictionary *post = @{@"agente_id":[profile objectForKey:@"id_user"],
                           @"promotoria_id":[profile objectForKey:@"promotoria"],
                           @"latitude":[NSString stringWithFormat:@"%f", latitude],
                           @"longitude":[NSString stringWithFormat:@"%f", longitude],
                           @"ubicacion_id":[NSString stringWithFormat:@"%d",placeId]};
    (void)[[BWConnectionManager alloc] initWithJSONPost:post withUrl:WSURLClient inWS:@"promotorialocationcontroller/saveChekin" delegate:theDelegate message:@"Haciendo Checkin..." withHud:YES requestWithTag:kConnectionTagMakeCheckIn ShowResponse:YES Resting3g:NO];
}


#pragma mark Parse Methods

+ (void)parseLogin:(NSDictionary*)dictionary userName:(NSString*)userName userPass:(NSString*)userPass{
    
    NSDictionary *profile = [[SCDatabaseManager sharedInstance] GetProfile];
    if (![profile[@"promotoria"] isEqualToString:dictionary[@"promotoria"][@"promot"] ]) {
        [[SCDatabaseManager sharedInstance] deleteTables];
    }
    
    NSDictionary *Dic = @{@"id_user":dictionary[@"usuario"][@"Agente"],@"email":dictionary[@"usuario"][@"correo"], @"full_name_user":dictionary[@"usuario"][@"Nombre"], @"rfc":dictionary[@"usuario"][@"RFC"], @"fecha_nacimiento":@"", @"fecha_expiracion_user":@"", @"monedero_user":@"" , @"logged_user":@"1",@"promotoria":dictionary[@"promotoria"][@"promot"], @"expiracion":dictionary[@"configuracion"][@"expiracion"], @"id_configuracion":dictionary[@"promotoria"][@"mostrar_agte"], @"salario":dictionary[@"configuracion"][@"salario"], @"loginDate":[NSString stringWithFormat:@"%f",[[NSDate date] timeIntervalSince1970]],@"user_name":userName, @"user_pass":userPass,@"interes_garantizado":dictionary[@"configuracion"][@"interes_garantizado"]};
    
    [[SCDatabaseManager sharedInstance] saveProfile:Dic];
    
    [[SCManager sharedInstance] saveImageFromUrl:dictionary[@"promotoria"][@"logourl"]];
}

#pragma mark paginatedMethods

- (void)download{
    downloadQueue = [[NSMutableArray alloc] init];
    onRequest = NO;
    
    NSDictionary *profile = [[SCDatabaseManager sharedInstance] GetProfile];
    
    if (([profile[@"Poliza"]integerValue] > [profile[@"PolizaAvance"]integerValue]) || ([profile[@"Poliza"]integerValue] == 0)) {
        [downloadQueue addObject:[NSNumber numberWithInt:kConnectionTagGetAllPolizas]];
    }
    if (([profile[@"Cobertura"]integerValue] > [profile[@"CoberturaAvance"]integerValue]) || ([profile[@"Cobertura"]integerValue] == 0)) {
        [downloadQueue addObject:[NSNumber numberWithInt:kConnectionTagGetAllCoberturas]];
    }
    if (([profile[@"ServicioGeneral"]integerValue] > [profile[@"ServicioGeneralAvance"]integerValue]) || ([profile[@"ServicioGeneral"]integerValue] == 0)) {
        [downloadQueue addObject:[NSNumber numberWithInt:kConnectionTagGetAllServicios]];
    }
    if (([profile[@"VentaInterno"]integerValue] > [profile[@"VentaInternoAvance"]integerValue]) || ([profile[@"VentaInterno"]integerValue] == 0)) {
        [downloadQueue addObject:[NSNumber numberWithInt:kConnectionTagGetAllServiciosInterno]];
    }
    if (([profile[@"ServicioVenta"]integerValue] > [profile[@"ServicioVentaAvance"]integerValue]) || ([profile[@"ServicioVenta"]integerValue] == 0)) {
        [downloadQueue addObject:[NSNumber numberWithInt:kConnectionTagGetAllServiciosVenta]];
    }
    if (([profile[@"AllProspect"]integerValue] > [profile[@"AllProspectAvance"]integerValue]) || ([profile[@"AllProspect"]integerValue] == 0)) {
        [downloadQueue addObject:[NSNumber numberWithInt:kConnectionTagGetAllProspection]];
    }
    if ([profile[@"AllAges"]integerValue] == 0) {
        [downloadQueue addObject:[NSNumber numberWithInt:kConnectionTagAllAges]];
    }
    if ([profile[@"AllProfesions"]integerValue] == 0) {
         [downloadQueue addObject:[NSNumber numberWithInt:kConnectionTagAllProfessions]];
    }
    
    if (timer != NULL) {
        [timer invalidate];
        timer = NULL;
    }
    if (downloadQueue.count != 0) {
        timer = [NSTimer scheduledTimerWithTimeInterval:1.0 target:self selector:@selector(dispachQueue) userInfo:nil repeats:YES];
        theCurrent = 0;
        theTotal = downloadQueue.count;
    }
    
}

- (void)dispachQueue{
    if (!onRequest) {
        theCurrent = theCurrent + 1;
        NSNumber *tag = downloadQueue[0];
        NSDictionary *profile = [[SCDatabaseManager sharedInstance] GetProfile];
        switch ([tag integerValue]) {
            case kConnectionTagGetAllPolizas:
                paginateValue = [profile[@"PolizaAvance"]integerValue];
                paginateGoal = [profile[@"Poliza"]integerValue];
                [self getAllValuesWithPaginatedCompletition:paginateValue withURL:WSURLClient inWS:WSPoliza Tag:kConnectionTagGetAllPolizas Current:theCurrent Total:theTotal withWSCompletition:YES];
                break;
                
            case kConnectionTagGetAllCoberturas:
                paginateValue = [profile[@"CoberturaAvance"]integerValue];
                paginateGoal = [profile[@"Cobertura"]integerValue];
                [self getAllValuesWithPaginatedCompletition:paginateValue withURL:WSURLClient inWS:WSCoberturas Tag:kConnectionTagGetAllCoberturas Current:theCurrent Total:theTotal withWSCompletition:YES];
                break;
                
            case kConnectionTagGetAllServicios:
                paginateValue = [profile[@"ServicioGeneralAvance"]integerValue];
                paginateGoal = [profile[@"ServicioGeneral"]integerValue];
                [self getAllValuesWithPaginatedCompletition:paginateValue withURL:WSURLClient inWS:WSServicios Tag:kConnectionTagGetAllServicios Current:theCurrent Total:theTotal withWSCompletition:YES];
                break;
                
            case kConnectionTagGetAllServiciosVenta:
                paginateValue = [profile[@"ServicioVentaAvance"]integerValue];
                paginateGoal = [profile[@"ServicioVenta"]integerValue];
                [self getAllValuesWithPaginatedCompletition:paginateValue withURL:WSURLClient inWS:WSServiciosVenta Tag:kConnectionTagGetAllServiciosVenta Current:theCurrent Total:theTotal withWSCompletition:YES];
                break;
                
            case kConnectionTagGetAllServiciosInterno:
                paginateValue = [profile[@"VentaInternoAvance"]integerValue];
                paginateGoal = [profile[@"VentaInterno"]integerValue];
                [self getAllValuesWithPaginatedCompletition:paginateValue withURL:WSURLClient inWS:WSServiciosInterno Tag:kConnectionTagGetAllServiciosInterno Current:theCurrent Total:theTotal withWSCompletition:YES];
                break;
                
            case kConnectionTagGetAllProspection:
                paginateValue = [profile[@"AllProspectAvance"]integerValue];
                paginateGoal = [profile[@"AllProspect"]integerValue];
                [self getAllValuesWithPaginatedCompletition:paginateValue withURL:WSURLClient inWS:WSProspeccion Tag:kConnectionTagGetAllProspection Current:theCurrent Total:theTotal withWSCompletition:YES];
                break;
                
            case kConnectionTagAllAges:
                [self getAllValuesWithPaginatedCompletition:0 withURL:WSURLClient inWS:WSAllAged Tag:kConnectionTagAllAges Current:theCurrent Total:theTotal withWSCompletition:NO];
                break;
                
            case kConnectionTagAllProfessions:
                [self getAllValuesWithPaginatedCompletition:0 withURL:WSURLClient inWS:WSAllProfesions Tag:kConnectionTagAllProfessions Current:theCurrent Total:theTotal withWSCompletition:NO];
                break;
                
            default:
                break;
        }
        NSMutableArray *tmpArray = [[NSMutableArray alloc] init];
        for (int i = 1; i < downloadQueue.count; i++) {
            [tmpArray addObject:downloadQueue[i]];
        }
        downloadQueue = tmpArray;
    }
    if (downloadQueue.count == 0) {
        [timer invalidate];
        timer = NULL;
    }
}


- (void)getAllValuesWithPaginatedCompletition:(int)paginate withURL:(NSString*)url inWS:(NSString*)webS Tag:(int)tag Current:(int)current Total:(int)total withWSCompletition:(BOOL)YESNO{
    
    [UIApplication sharedApplication].networkActivityIndicatorVisible = YES;
    [UIApplication sharedApplication].idleTimerDisabled = YES;
    
    onRequest = YES;
    if (paginate == 0) {
        paginateGoal = 0;
    }
    paginateValue = paginate;
    NSString *wsCompletition = webS;
    if (YESNO) {
       wsCompletition = [NSString stringWithFormat:webS,[[SCDatabaseManager sharedInstance] GetProfile][@"promotoria"],paginateValue];
    }
     
    BWConnectionManager *connection = [[BWConnectionManager alloc] initWithJSONPost:@{@"":@""} withUrl:url inWS:wsCompletition delegate:self message:[NSString stringWithFormat:@"Descargando datos... (%d de 8)",current + (8 - total)] withHud:YES requestWithTag:tag ShowResponse:NO Resting3g:YES];
    
    if (YESNO) {
        if (paginateGoal != 0) {
            float value = (float)paginateValue/paginateGoal;
            [connection progress:value];
        }else{
            float value = 0.0;
            [connection progress:value];
        }
    }
    
}

-(void)didGetResponse:(id)response inConnectionTag:(int)tag{
    NSDictionary *dic = response;
    NSArray *dataArray = [[NSArray alloc] init];
    NSString *dataString = @"";
    NSDictionary *upProfile = nil;
    [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
    [UIApplication sharedApplication].idleTimerDisabled = NO;
    switch (tag) {
        case kConnectionTagGetAllPolizas:
            
            paginateGoal = [dic[@"total"] integerValue];
            dataString = dic[@"data"];
            dataString = [dataString stringByReplacingOccurrencesOfString:@",|" withString:@"|"];
            //dataString = [dataString stringByReplacingOccurrencesOfString:@"\"" withString:@""];
            dataArray = [dataString componentsSeparatedByString:@"|"];
            
            [[SCDatabaseManager sharedInstance] checkIfTableExistAndCreateIfItDoesnt:PolizaTableName withFields:dataArray[0]];
            [[SCDatabaseManager sharedInstance] doPaginateInsertionWithFields:dataArray[0] andValues:dataArray forTable:PolizaTableName];
//            for (int x = 1; x < dataArray.count; x++) {
//                [[SCDatabaseManager sharedInstance] doPaginateInsertionWithFields:dataArray[0] values:dataArray[x] forTable:PolizaTableName];
//            }
            
            upProfile = @{@"Poliza":[NSNumber numberWithInt:paginateGoal], @"PolizaAvance":[NSNumber numberWithInt:(paginateValue+1000)]};
            [[SCDatabaseManager sharedInstance] saveProfile:upProfile];
            //[[SCDatabaseManager sharedInstance] DisplayinLogContentofTable:PolizaTableName];
            
            if (paginateGoal > (paginateValue+1000)) {
                [self getAllValuesWithPaginatedCompletition:paginateValue+1000 withURL:WSURLClient inWS:WSPoliza Tag:kConnectionTagGetAllPolizas Current:theCurrent Total:theTotal withWSCompletition:YES];
            }else{
                onRequest = NO;
            }
            
            break;
            
        case kConnectionTagGetAllCoberturas:
            
            paginateGoal = [dic[@"total"] integerValue];
            dataString = dic[@"data"];
            dataString = [dataString stringByReplacingOccurrencesOfString:@",|" withString:@"|"];
            //dataString = [dataString stringByReplacingOccurrencesOfString:@"\"" withString:@""];
            dataArray = [dataString componentsSeparatedByString:@"|"];
            
            [[SCDatabaseManager sharedInstance] checkIfTableExistAndCreateIfItDoesnt:CoberturaTableName withFields:dataArray[0]];
            //for (int x = 1; x < dataArray.count; x++) {
                [[SCDatabaseManager sharedInstance] doPaginateInsertionWithFields:dataArray[0] andValues:dataArray forTable:CoberturaTableName];
            //}
            
            upProfile = @{@"Cobertura":[NSNumber numberWithInt:paginateGoal], @"CoberturaAvance":[NSNumber numberWithInt:(paginateValue+1000)]};
            [[SCDatabaseManager sharedInstance] saveProfile:upProfile];
            
            if (paginateGoal > (paginateValue+1000)) {
               [self getAllValuesWithPaginatedCompletition:paginateValue+1000 withURL:WSURLClient inWS:WSCoberturas Tag:kConnectionTagGetAllCoberturas Current:theCurrent Total:theTotal withWSCompletition:YES];
            }else{
                onRequest = NO;
            }
            
            
            break;
            
        case kConnectionTagGetAllServicios:
            
            paginateGoal = [dic[@"total"] integerValue];
            dataString = dic[@"data"];
            dataString = [dataString stringByReplacingOccurrencesOfString:@",|" withString:@"|"];
            //dataString = [dataString stringByReplacingOccurrencesOfString:@"\"" withString:@""];
            dataArray = [dataString componentsSeparatedByString:@"|"];
            
            [[SCDatabaseManager sharedInstance] checkIfTableExistAndCreateIfItDoesnt:ServicioGeneralTableName withFields:dataArray[0]];
            //for (int x = 1; x < dataArray.count; x++) {
                [[SCDatabaseManager sharedInstance] doPaginateInsertionWithFields:dataArray[0] andValues:dataArray forTable:ServicioGeneralTableName];
            //}
            
            upProfile = @{@"ServicioGeneral":[NSNumber numberWithInt:paginateGoal], @"ServicioGeneralAvance":[NSNumber numberWithInt:(paginateValue+1000)]};
            [[SCDatabaseManager sharedInstance] saveProfile:upProfile];
            
            
            if (paginateGoal > (paginateValue+1000)) {
                [self getAllValuesWithPaginatedCompletition:paginateValue+1000 withURL:WSURLClient inWS:WSServicios Tag:kConnectionTagGetAllServicios Current:theCurrent Total:theTotal withWSCompletition:YES];
            }else{
                onRequest = NO;
            }
            
            
            break;
            
        case kConnectionTagGetAllServiciosVenta:
            
            paginateGoal = [dic[@"total"] integerValue];
            dataString = dic[@"data"];
            dataString = [dataString stringByReplacingOccurrencesOfString:@",|" withString:@"|"];
            //dataString = [dataString stringByReplacingOccurrencesOfString:@"\"" withString:@""];
            dataArray = [dataString componentsSeparatedByString:@"|"];
            
            [[SCDatabaseManager sharedInstance] checkIfTableExistAndCreateIfItDoesnt:ServicioVentaTableName withFields:dataArray[0]];
            //for (int x = 1; x < dataArray.count; x++) {
                [[SCDatabaseManager sharedInstance] doPaginateInsertionWithFields:dataArray[0] andValues:dataArray forTable:ServicioVentaTableName];
            //}
            
            upProfile = @{@"ServicioVenta":[NSNumber numberWithInt:paginateGoal], @"ServicioVentaAvance":[NSNumber numberWithInt:(paginateValue+1000)]};
            [[SCDatabaseManager sharedInstance] saveProfile:upProfile];
            
            
            if (paginateGoal > (paginateValue+1000)) {
                [self getAllValuesWithPaginatedCompletition:paginateValue+1000 withURL:WSURLClient inWS:WSServiciosVenta Tag:kConnectionTagGetAllServiciosVenta Current:theCurrent Total:theTotal withWSCompletition:YES];
            }else{
                onRequest = NO;
            }
            
            
            break;
            
        case kConnectionTagGetAllServiciosInterno:
            
            paginateGoal = [dic[@"total"] integerValue];
            dataString = dic[@"data"];
            dataString = [dataString stringByReplacingOccurrencesOfString:@",|" withString:@"|"];
            //dataString = [dataString stringByReplacingOccurrencesOfString:@"\"" withString:@""];
            dataArray = [dataString componentsSeparatedByString:@"|"];
            
            [[SCDatabaseManager sharedInstance] checkIfTableExistAndCreateIfItDoesnt:VentaInternoTableName withFields:dataArray[0]];
            //for (int x = 1; x < dataArray.count; x++) {
                [[SCDatabaseManager sharedInstance] doPaginateInsertionWithFields:dataArray[0] andValues:dataArray forTable:VentaInternoTableName];
            //}
            
            upProfile = @{@"VentaInterno":[NSNumber numberWithInt:paginateGoal], @"VentaInternoAvance":[NSNumber numberWithInt:(paginateValue+1000)]};
            [[SCDatabaseManager sharedInstance] saveProfile:upProfile];
            
            
            if (paginateGoal > (paginateValue+1000)) {
                [self getAllValuesWithPaginatedCompletition:paginateValue+1000 withURL:WSURLClient inWS:WSServiciosInterno Tag:kConnectionTagGetAllServiciosInterno Current:theCurrent Total:theTotal withWSCompletition:YES];
            }else{
                onRequest = NO;
            }
            
            
            break;
            
        case kConnectionTagGetAllProspection:
            
            paginateGoal = [dic[@"total"] integerValue];
            dataString = dic[@"data"];
            dataString = [dataString stringByReplacingOccurrencesOfString:@",|" withString:@"|"];
            //dataString = [dataString stringByReplacingOccurrencesOfString:@"\"" withString:@""];
            dataArray = [dataString componentsSeparatedByString:@"|"];
            
            [[SCDatabaseManager sharedInstance] checkIfTableExistAndCreateIfItDoesnt:AllProspectTable withFields:dataArray[0]];
            //for (int x = 1; x < dataArray.count; x++) {
                [[SCDatabaseManager sharedInstance] doPaginateInsertionWithFields:dataArray[0] andValues:dataArray forTable:AllProspectTable];
            //}
            
            upProfile = @{@"AllProspect":[NSNumber numberWithInt:paginateGoal], @"AllProspectAvance":[NSNumber numberWithInt:(paginateValue+1000)]};
            [[SCDatabaseManager sharedInstance] saveProfile:upProfile];
            
            if (paginateGoal > (paginateValue+1000)) {
                [self getAllValuesWithPaginatedCompletition:paginateValue+1000 withURL:WSURLClient inWS:WSProspeccion Tag:kConnectionTagGetAllProspection Current:theCurrent Total:theTotal withWSCompletition:YES];
            }else{
                onRequest = NO;
            }
            
            
            break;
            
        case kConnectionTagAllProfessions:
            
            [[SCDatabaseManager sharedInstance] SaveProfessions:response];
            
            upProfile = @{@"AllProfesions":[NSNumber numberWithInt:1]};
            [[SCDatabaseManager sharedInstance] saveProfile:upProfile];
            onRequest = NO;
            
            break;
            
        case kConnectionTagAllAges:
            
            [[SCDatabaseManager sharedInstance] SaveAllAges:response];
            
            upProfile = @{@"AllAges":[NSNumber numberWithInt:1]};
            [[SCDatabaseManager sharedInstance] saveProfile:upProfile];
            onRequest = NO;
            
            break;
            
        default:
            onRequest = NO;
            break;
    }

}

- (void)didFailResponseInConnectionTag:(int)tag{
    [UIApplication sharedApplication].networkActivityIndicatorVisible = NO;
    [UIApplication sharedApplication].idleTimerDisabled = NO;
    onRequest = NO;
    if (timer != NULL) {
        [timer invalidate];
        timer = NULL;
    }
}


@end
