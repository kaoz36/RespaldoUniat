//
//  SCDatabaseManager.h
//  SIPAC
//
//  Created by Jaguar3 on 19/03/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BWDB.h"

#define DatabaseName @"DB.db"


/**
 DinamicTables
 */
#define PolizaTableName @"Poliza"
#define CoberturaTableName @"Cobertura"
#define ServicioVentaTableName @"ServicioVenta"
#define VentaInternoTableName @"VentaInterno"
#define ServicioGeneralTableName @"ServicioGeneral"
#define AllProspectTable @"AllProspect"

/**
 StaticTables
 */
#define ProfileTableName @"Profile"
#define ProfesionsTableName @"Professions"
#define AllAgesTableName @"AllAges"


@interface SCDatabaseManager : NSObject {
    BWDB *db;
}


+ (SCDatabaseManager *)sharedInstance;

-(void)deleteDataBase;
-(void)deleteTables;

-(NSMutableArray*)GetPoliza:(NSString*)poliza;
-(NSMutableArray*)GetCoberturaTableForPoliza:(NSString*)poliza;
-(NSMutableArray*)GetServicioVentaTableForPoliza:(NSString*)poliza;
-(NSMutableArray*)GetVentaInternoTableForPoliza:(NSString*)poliza;
-(NSMutableArray*)GetServicioGeneralTableForPoliza:(NSString*)poliza;

-(NSMutableArray*)GetArrayFromQuery:(NSString*)query;

- (void)saveProfile:(NSDictionary*)profile;
- (NSString*)getUniqueGeneratedIdentifier;
- (NSDictionary*)GetProfile;
- (float)getMinimunSalary;

- (void)CreateProfileTable;

- (void)SaveProfessions:(NSDictionary*)dict;
- (NSMutableArray*)GetProfessions;
- (void)SaveAllAges:(NSDictionary*)dict;
- (NSDictionary*)GetAgeInfo:(int)age;

- (NSMutableArray*)getZones;

- (void)checkIfTableExistAndCreateIfItDoesnt:(NSString*)TableName withFields:(NSString*)fields;
- (void)doPaginateInsertionWithFields:(NSString*)fields values:(NSString*)values forTable:(NSString*)TableName;
- (void)doPaginateInsertionWithFields:(NSString*)fields andValues:(NSArray*)values forTable:(NSString*)TableName;
- (void)DisplayinLogContentofTable:(NSString*)TableName;
- (bool)hasAllData;

@end
