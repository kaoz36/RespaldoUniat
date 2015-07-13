//
//  SCDatabaseManager.m
//  SIPAC
//
//  Created by Jaguar3 on 19/03/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import "SCDatabaseManager.h"

@implementation SCDatabaseManager

#pragma mark - Public Methods

+ (SCDatabaseManager *)sharedInstance
{
    static dispatch_once_t pred;
    static SCDatabaseManager *shared = nil;
    dispatch_once(&pred, ^{
        shared = [[SCDatabaseManager alloc] init];
    });
    return shared;
}

-(void)deleteDataBase{
    [db closeDB];
    
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *documentsDirectory = [paths objectAtIndex:0];
    NSString *filePath =  [documentsDirectory stringByAppendingPathComponent:DatabaseName];
    
    if([[NSFileManager defaultManager] fileExistsAtPath:filePath]){
        [[NSFileManager defaultManager] removeItemAtPath:filePath error:nil];
    }
    
}

- (void)deleteTables{
    [db doQuery:[NSString stringWithFormat:@"drop table if exists %@",PolizaTableName]];
    [db doQuery:[NSString stringWithFormat:@"drop table if exists %@",CoberturaTableName]];
    [db doQuery:[NSString stringWithFormat:@"drop table if exists %@",VentaInternoTableName]];
    [db doQuery:[NSString stringWithFormat:@"drop table if exists %@",ServicioVentaTableName]];
    [db doQuery:[NSString stringWithFormat:@"drop table if exists %@",ServicioGeneralTableName]];
    [db doQuery:[NSString stringWithFormat:@"drop table if exists %@",AllProspectTable]];
    [db doQuery:[NSString stringWithFormat:@"drop table if exists %@",AllAgesTableName]];
    [db doQuery:[NSString stringWithFormat:@"drop table if exists %@",ProfesionsTableName]];
    
    NSDictionary *upProfile = @{@"Poliza":@"0", @"PolizaAvance":@"0", @"Cobertura":@"0", @"CoberturaAvance":@"0", @"ServicioVenta":@"0", @"ServicioVentaAvance":@"0", @"VentaInterno":@"0", @"VentaInternoAvance":@"0", @"ServicioGeneral":@"0", @"ServicioGeneralAvance":@"0", @"AllProspect":@"0", @"AllProspectAvance":@"0", @"AllAges":@"0", @"AllProfesions":@"0"};
    [self saveProfile:upProfile];
}

#pragma mark - Private Methods

-(id)init{
    
    self = [super init];
    if (self) {
        // Custom initialization
        //Init Database and create tables if necesary
        if ([db = [BWDB alloc] initWithDBFilename:DatabaseName andTableName:PolizaTableName]) {
            
            if (![db checkIfTableExists:ProfileTableName]) {
                [self CreateProfileTable];
                [self DisplayinLogContentofTable:ProfileTableName];
            }
            if (![db checkIfTableExists:ProfesionsTableName]) {
                [self CreateProfessionsTable];
                [self DisplayinLogContentofTable:ProfesionsTableName];
            }
            if (![db checkIfTableExists:AllAgesTableName]) {
                [self CreateAllAgesTable];
                [self DisplayinLogContentofTable:AllAgesTableName];
            }
            
        }
    }
    return self;
    
}

-(void)DisplayinLogContentofTable:(NSString*)TableName{
    NSLog(@"All Values For Table %@",TableName);
    [db setTableName:TableName];
    NSArray *theArray = [db getAllRowsFromTable];
    for (NSDictionary *row in theArray) {
        NSLog(@"%@",row);
    }
}

#pragma mark - CreateSearchTables

-(void)CreateProfileTable{
    [db setTableName:ProfileTableName];
    
    [db doQuery:[NSString stringWithFormat:@"drop table if exists %@",ProfileTableName]];
    [db doQuery:[NSString stringWithFormat:@"CREATE TABLE %@ (id INTEGER PRIMARY KEY, id_user, user_name, user_pass, full_name_user, email, rfc, fecha_nacimiento, fecha_expiracion_user, monedero_user, logged_user, promotoria, expiracion, id_configuracion, salario, loginDate, Poliza, PolizaAvance, Cobertura, CoberturaAvance, ServicioVenta, ServicioVentaAvance, VentaInterno, VentaInternoAvance, ServicioGeneral, ServicioGeneralAvance, AllProspect, AllProspectAvance, AllAges, AllProfesions)",ProfileTableName]];
    
    NSDictionary *Dic = @{@"id_user":@"", @"user_name":@"", @"user_pass":@"", @"full_name_user":@"", @"email":@"", @"rfc":@"", @"fecha_nacimiento":@"", @"fecha_expiracion_user":@"", @"monedero_user":@"" , @"logged_user":@"0",@"promotoria":@"", @"expiracion":@"", @"id_configuracion":@"", @"salario":@"",@"loginDate":@"0",@"Poliza":@"0", @"PolizaAvance":@"0", @"Cobertura":@"0", @"CoberturaAvance":@"0", @"ServicioVenta":@"0", @"ServicioVentaAvance":@"0", @"VentaInterno":@"0", @"VentaInternoAvance":@"0", @"ServicioGeneral":@"0", @"ServicioGeneralAvance":@"0", @"AllProspect":@"0", @"AllProspectAvance":@"0", @"AllAges":@"0", @"AllProfesions":@"0"};
    [db insertRow:Dic];
}

- (void)CreateProfessionsTable{
    [db setTableName:ProfesionsTableName];
    
    [db doQuery:[NSString stringWithFormat:@"drop table if exists %@",ProfesionsTableName]];
    [db doQuery:[NSString stringWithFormat:@"CREATE TABLE %@ (id INTEGER PRIMARY KEY, nombre, accidente, invalidez, millar)",ProfesionsTableName]];
}

- (void)CreateAllAgesTable{
    [db setTableName:AllAgesTableName];
    
    [db doQuery:[NSString stringWithFormat:@"drop table if exists %@",AllAgesTableName]];
    [db doQuery:[NSString stringWithFormat:@"CREATE TABLE %@ (id INTEGER PRIMARY KEY, edad, BAS, BCAT, CII, CMA, GE, GEN, GFA, TIBA, TIBAN, CMAN, BIT, BITN, BASN, BASF, BASFN, CPCONY, CPCONYN, BCATN, CIIN)",AllAgesTableName]];
}

- (void)doPaginateInsertionWithFields:(NSString*)fields values:(NSString*)values forTable:(NSString*)TableName{
    NSString *query = @"";
    if (![db checkIfTableExists:TableName]){
        query = [NSString stringWithFormat:@"CREATE TABLE %@ (id INTEGER PRIMARY KEY, %@)",TableName,fields];
        //NSLog(@"create Table query : %@",query);
        [db doQuery:query];
    }
    
    query = [NSString stringWithFormat:@"insert into %@ (%@) values (%@)",TableName,fields,values];
    //NSLog(@"insert query : %@",query);
    [db doQuery:query];
}

#pragma mark - Public Methods

-(NSMutableArray*)GetPoliza:(NSString*)poliza{
    [db setTableName:PolizaTableName];
    NSMutableArray *query = [db getRowsFromColumnName:@"id_poliza" andValue:poliza];
    return query;
}

-(NSMutableArray*)GetCoberturaTableForPoliza:(NSString*)poliza{
    [db setTableName:CoberturaTableName];
    NSMutableArray *query = [db getRowsFromColumnName:@"id_poliza" andValue:poliza];
    return query;
}

-(NSMutableArray*)GetServicioVentaTableForPoliza:(NSString*)poliza{
    [db setTableName:ServicioVentaTableName];
    NSMutableArray *query = [db getRowsFromColumnName:@"id_poliza" andValue:poliza];
    return query;
}

-(NSMutableArray*)GetVentaInternoTableForPoliza:(NSString*)poliza{
    [db setTableName:VentaInternoTableName];
    NSMutableArray *query = [db getRowsFromColumnName:@"id_poliza" andValue:poliza];
    return query;
}

-(NSMutableArray*)GetServicioGeneralTableForPoliza:(NSString*)poliza{
    [db setTableName:ServicioGeneralTableName];
    NSMutableArray *query = [db getRowsFromColumnName:@"id_poliza" andValue:poliza];
    return query;
}

-(NSMutableArray*)GetArrayFromQuery:(NSString*)query{
    return [db getRowsFromQuery:query];
}

-(void)saveProfile:(NSDictionary*)profile{
    [db setTableName:ProfileTableName];
    [db updateRow:profile rowID:@1];
    
    //[self DisplayinLogContentofTable:ProfileTableName];
}

-(NSDictionary*)GetProfile{
    [db setTableName:ProfileTableName];
    return [db getRow:@1];
}

-(void)SaveProfessions:(NSDictionary*)dict{
    [self CreateProfessionsTable];
    NSArray *Professions = [dict objectForKey:@"result"];
    for (int x = 0; x<Professions.count; x++) {
        NSDictionary *tmpRow = [Professions objectAtIndex:x];
        [db insertRow:tmpRow];
    }
    [self DisplayinLogContentofTable:ProfesionsTableName];
}

-(NSMutableArray*)GetProfessions{
    [db setTableName:ProfesionsTableName];
    return [db getAllRowsFromTable];
}

-(void)SaveAllAges:(NSDictionary*)dict{
    [self CreateAllAgesTable];
    NSArray *AllAges = [dict objectForKey:@"result"];
    for (int x = 0; x<AllAges.count; x++) {
        NSDictionary *tmpRow = [AllAges objectAtIndex:x];
        [db insertRow:tmpRow];
    }
    [self DisplayinLogContentofTable:AllAgesTableName];
}

-(NSDictionary*)GetAgeInfo:(int)age{
   
    [db setTableName:AllAgesTableName];
    NSMutableArray *theAge = [db getRowsFromColumnName:@"edad" andValue:[NSString stringWithFormat:@"%i",age]];
    NSDictionary *result;
    if (theAge.count != 0) {
        result = [theAge objectAtIndex:0];
    }
    return result;
}

- (bool)hasAllData{
    NSDictionary *profile = [self GetProfile];
    if (([profile[@"Poliza"]integerValue] <= [profile[@"PolizaAvance"]integerValue]) && ([profile[@"Cobertura"]integerValue] <= [profile[@"CoberturaAvance"]integerValue]) && ([profile[@"ServicioGeneral"]integerValue] <= [profile[@"ServicioGeneralAvance"]integerValue]) && ([profile[@"VentaInterno"]integerValue] <= [profile[@"VentaInternoAvance"]integerValue]) && ([profile[@"ServicioVenta"]integerValue] <= [profile[@"ServicioVentaAvance"]integerValue]) && ([profile[@"AllProspect"]integerValue] <= [profile[@"AllProspectAvance"]integerValue]) && [profile[@"AllAges"]integerValue] == 1 && [profile[@"AllProfesions"]integerValue] == 1) {
        return YES;
    }else{
        return NO;
    }
}

@end
