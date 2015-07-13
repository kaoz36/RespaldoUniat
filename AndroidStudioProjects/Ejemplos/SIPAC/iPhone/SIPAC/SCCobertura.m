//
//  SCCoberturas.m
//  SIPAC
//
//  Created by MacBook Pro de Hugo on 05/08/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import "SCCobertura.h"
#import "SCDatabaseManager.h"
#import "SCCotizador.h"

@implementation SCCobertura
{
    double _sumaAsegurada;
    double _primaAnual;
}

@dynamic sumaAsegurada;
@dynamic primaAnual;

- (void)setSumaAsegurada:(double)sumaAsegurada {
    _sumaAsegurada = sumaAsegurada;
    self.stringSumaAsegurada = [NSString stringWithFormat:@"%.2f", _sumaAsegurada];
}

- (double)sumaAsegurada {
    return _sumaAsegurada;
}

- (void)setPrimaAnual:(double)primaAnual {
    _primaAnual = primaAnual;
    self.stringPrimaAnual = [NSString stringWithFormat:@"%.2f", _primaAnual];
}

- (double)primaAnual {
    return _primaAnual;
}

-(id)initWithType:(SCCoberturaType)newType
{
    self = [super init];
    if (self) {
        
        self.type = newType;
    }
    return self;
}


-(void)calcular:(double)pMillar invalidez:(double)pInvalidez accidente:(double)pAccidente{
    NSDictionary *getValue = [[NSDictionary alloc] init];
    switch (self.type) {
            
        case SCCoberturaTypeBAS: {
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            double bas = 0;
            if (self.esConDevolucion) {// cotizador 1 y cotizador 2
                BAS = [[getValue objectForKey:@"BAS"]doubleValue]* 100;
                bas = pMillar + BAS;
            }else{                     //cotizador 3 y cotizador 4
                BASN = [[getValue objectForKey:@"BASFN"]doubleValue]* 100;
                bas = pMillar + BASN;
            }
            self.primaAnual = (bas * self.sumaAsegurada) / 100000;
            
        }
            break;
        case SCCoberturaTypeBIT:
            return;
            break;
        case SCCoberturaTypeCII:{
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            if (self.esConDevolucion) {// cotizador 1 y cotizador 2
                CII = [[getValue objectForKey:@"CII"]doubleValue]* 100;
                self.primaAnual = (pInvalidez *CII);
            }else{ //cotizador 3 y cotizador 4
                CIIN = [[getValue objectForKey:@"CIIN"]doubleValue]* 100;
                self.primaAnual = (pInvalidez *CIIN);
            }
            self.primaAnual = (self.primaAnual * self.sumaAsegurada) / 100000;
        }
            break;
        case SCCoberturaTypeCMA:{
            double cma = 0;
            if (self.esConDevolucion) {// cotizador 1 y cotizador 2
                cma = (pAccidente * 161);
            }else{                     //cotizador 3 y cotizador 4
                cma = (pAccidente * 159);
            }
            
            self.primaAnual = (cma * self.sumaAsegurada) / 100000;
            
        }
            break;
        case SCCoberturaTypeTIBA:{
            double  tiba = 0;
            if (self.esConDevolucion) {// cotizador 1 y cotizador 2
                tiba = (pAccidente * 199);
            }else{                     //cotizador 3 y cotizador 4
                tiba = (pAccidente * 197);
            }
            
            self.primaAnual = (tiba * self.sumaAsegurada) / 100000;
        }
            break;
        case SCCoberturaTypeCAT:{
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            if (self.sexo == SCSexMujer){
                if (self.esConDevolucion) {// cotizador 1 y cotizador 2
                    CAT = [[getValue objectForKey:@"CPCONY"]doubleValue]* 100;
                }else{//cotizador 3 y cotizador 4
                    CAT = [[getValue objectForKey:@"CPCONYN"]doubleValue]* 100;
                }
            }else{
                if (self.esConDevolucion) {// cotizador 1 y cotizador 2
                    CAT = [[getValue objectForKey:@"BCAT"]doubleValue]* 100;
                }else{//cotizador 3 y cotizador 4
                    CAT = [[getValue objectForKey:@"BCATN"]doubleValue]* 100;
                }
            }
            self.primaAnual = (CAT * self.sumaAsegurada) / 100000;
        }
            break;
        case SCCoberturaTypeGFA:{
            double gfa = 0;
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            if (self.esConDevolucion) {// cotizador 1 y cotizador 2
                BAS = [[getValue objectForKey:@"BASF"]doubleValue]* 100;
                gfa = BAS + pMillar;
            }else{                     //cotizador 3 y cotizador 4
                BASN = [[getValue objectForKey:@"BASN"]doubleValue]* 100;
                gfa = BASN + pMillar;
            }
            self.primaAnual = (gfa * self.sumaAsegurada) / 100000;
            
        }
            break;
        case SCCoberturaTypeGE:{
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            double geTotal = 0;
            if (self.esConDevolucion) {// cotizador 1 y cotizador 2
                GE = [[getValue objectForKey:@"GE"]doubleValue]* 100;
                geTotal = GE + pMillar ;
            }else{//cotizador 3 y cotizador 4
                GEN = [[getValue objectForKey:@"GEN"]doubleValue]* 100;
                geTotal = GEN + pMillar ;
            }
            self.primaAnual = (geTotal * self.sumaAsegurada) / 100000;
        }
            break;
        case SCCoberturaTypeBACY:{
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            if (self.esConDevolucion) {// cotizador 1 y cotizador 2
                BACY = [[getValue objectForKey:@"BASF"]doubleValue]* 100;
            }else{                     //cotizador 3 y cotizador 4
                BACY = [[getValue objectForKey:@"BASN"]doubleValue]* 100;
            }
            self.primaAnual = (BACY * self.sumaAsegurada) / 100000;
        }
            break;
        case SCCoberturaTypeGFC:{
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            if (self.esConDevolucion) {// cotizador 1 y cotizador 2
                GFC = [[getValue objectForKey:@"BASF"]doubleValue]* 100;
            }else{                     //cotizador 3 y cotizador 4
                GFC = [[getValue objectForKey:@"BASN"]doubleValue]* 100;
            }
            self.primaAnual = (GFC * self.sumaAsegurada) / 100000;
        }
            break;
            
            
        case SCCoberturaTypeCP:{
            
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            //conyuge se evalua inverso el sexo
            if (self.sexo == SCSexMujer){
                if (self.esConDevolucion) {// cotizador 1 y cotizador 2
                    CAT = [[getValue objectForKey:@"BCAT"]doubleValue]* 100;
                }else{//cotizador 3 y cotizador 4
                    CAT = [[getValue objectForKey:@"BCATN"]doubleValue]* 100;
                }
            }else{
                
                if (self.esConDevolucion) {// cotizador 1 y cotizador 2
                    CAT = [[getValue objectForKey:@"CPCONY"]doubleValue]* 100;
                }else{//cotizador 3 y cotizador 4
                    CAT = [[getValue objectForKey:@"CPCONYN"]doubleValue]* 100;
                }
            }
            
            self.primaAnual = (CAT * self.sumaAsegurada) / 100000;
            
        }
            break;
            
            
        case SCCoberturaTypeGFH:{
            int valorGFH = 0;
            
            if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPhone) {
                
                if (self.indexSeleccionGFH == 0) {
                    valorGFH = 0;
                }else if (self.indexSeleccionGFH == 1) {
                    valorGFH = 136;
                }else if (self.indexSeleccionGFH == 2){
                    valorGFH = 276;
                }else{
                    valorGFH = 409;
                }
                
            }else{
                
                if (self.indexSeleccionGFH == 0) {
                    valorGFH = 0;
                }else if (self.indexSeleccionGFH == 1) {
                    valorGFH = 136;
                }else if (self.indexSeleccionGFH == 2){
                    valorGFH = 276;
                }else{
                    valorGFH = 409;
                }
                
            }
            
            self.primaAnual = (valorGFH * self.sumaAsegurada) / 100000;
            
        }
            break;
            
        case SCCoberturaTypeCC:{
            
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            if (self.esConDevolucion) {// cotizador 1 y cotizador 2
                BAS = [[getValue objectForKey:@"BASF"]doubleValue]* 100;
            }else{                     //cotizador 3 y cotizador 4
                BAS = [[getValue objectForKey:@"BASN"]doubleValue]* 100;
            }
            self.primaAnual = (BAS * self.sumaAsegurada) / 100000;
            
        }
            break;
            
        case SCCoberturaTypeCP1:{
            
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            
            if (self.sexIndex == 1){
                if (self.esConDevolucion) {// cotizador 1 y cotizador 2
                    CAT = [[getValue objectForKey:@"BCAT"]doubleValue]* 100;
                }else{//cotizador 3 y cotizador 4
                    CAT = [[getValue objectForKey:@"BCATN"]doubleValue]* 100;
                }
            }else{
                if (self.esConDevolucion) {// cotizador 1 y cotizador 2
                    CAT = [[getValue objectForKey:@"CPCONY"]doubleValue]* 100;
                }else{//cotizador 3 y cotizador 4
                    CAT = [[getValue objectForKey:@"CPCONYN"]doubleValue]* 100;
                }
            }
            self.primaAnual = (CAT * self.sumaAsegurada) / 100000;
            
        }
            break;
            
        case SCCoberturaTypeCP2:{
            
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            
            if (self.sexIndex == 1) {
                if (self.esConDevolucion) {// cotizador 1 y cotizador 2
                    CAT = [[getValue objectForKey:@"BCAT"]doubleValue]* 100;
                }else{//cotizador 3 y cotizador 4
                    CAT = [[getValue objectForKey:@"BCATN"]doubleValue]* 100;
                }
            }else{
                if (self.esConDevolucion) {// cotizador 1 y cotizador 2
                    CAT = [[getValue objectForKey:@"CPCONY"]doubleValue]* 100;
                }else{//cotizador 3 y cotizador 4
                    CAT = [[getValue objectForKey:@"CPCONYN"]doubleValue]* 100;
                }
            }
            self.primaAnual = (CAT * self.sumaAsegurada) / 100000;
        }
            break;
            
        case SCCoberturaTypeCP3:{
            
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            
            if (self.sexIndex == 1){
                if (self.esConDevolucion) {// cotizador 1 y cotizador 2
                    CAT = [[getValue objectForKey:@"BCAT"]doubleValue]* 100;
                }else{//cotizador 3 y cotizador 4
                    CAT = [[getValue objectForKey:@"BCATN"]doubleValue]* 100;
                }
            }else{
                if (self.esConDevolucion) {// cotizador 1 y cotizador 2
                    CAT = [[getValue objectForKey:@"CPCONY"]doubleValue]* 100;
                }else{//cotizador 3 y cotizador 4
                    CAT = [[getValue objectForKey:@"CPCONYN"]doubleValue]* 100;
                }
            }
            
            self.primaAnual = (CAT * self.sumaAsegurada) / 100000;
        }
            break;
        default:
            break;
    }

    self.stringPrimaAnual = [NSString stringWithFormat:@"%.2f",self.primaAnual];
     
}

-(double)calcularFactoresBeneficios:(double)pMillar invalidez:(double)pInvalidez accidente:(double)pAccidente pCalculandoFactorBit:(BOOL)pCalculandoFactorBit {
    NSDictionary *getValue = [[NSDictionary alloc] init];
    double factor = 0;
    if (pCalculandoFactorBit) {
        if (self.type == SCCoberturaTypeBIT ||  self.type == SCCoberturaTypeCII ||  self.type == SCCoberturaTypeCMA ||self.type == SCCoberturaTypeTIBA){
            return factor;
        }
    }
    if (!self.isSelected) {
        return factor;
    }
    switch (self.type) {
        case SCCoberturaTypeBAS: {
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            double bas = 0;
            if (self.esConDevolucion) {// cotizador 5 y cotizador 7
                BAS = [[getValue objectForKey:@"BAS"]doubleValue]* 100;
                bas = pMillar + BAS;
            } else {                     //cotizador 6 y cotizador 8
                BASN = [[getValue objectForKey:@"BASFN"]doubleValue]* 100;
                bas = pMillar + BASN;
            }
            factor = bas;
        }
            break;
        case SCCoberturaTypeBIT:
            
            factor = 0;
            
            break;
        case SCCoberturaTypeCII:{
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            if (self.esConDevolucion) {// cotizador 5 y cotizador 7
                CII = [[getValue objectForKey:@"CII"]doubleValue]* 100;
                factor = (pInvalidez *CII);
            } else {  //cotizador 6 y cotizador 8
                CIIN = [[getValue objectForKey:@"CIIN"]doubleValue]* 100;
                factor = (pInvalidez *CIIN);
            }
        }
            break;
        case SCCoberturaTypeCMA:{
            double cma = 0;
            if (self.esConDevolucion) {// cotizador 5 y cotizador 7
                cma = (pAccidente * 161);
            }else{                     //cotizador 6 y cotizador 8
                cma = (pAccidente * 159);
            }
            factor = cma;
        }
            break;
        case SCCoberturaTypeTIBA:{
            double  tiba = 0;
            if (self.esConDevolucion) {// cotizador 5 y cotizador 7
                tiba = (pAccidente * 199);
            }else{                    //cotizador 6 y cotizador 8
                tiba = (pAccidente * 197);
            }
            factor = tiba;
        }
            break;
        case SCCoberturaTypeCAT:{
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            if (self.sexo == SCSexMujer){
                if (self.esConDevolucion) {// cotizador 5 y cotizador 7
                    CAT = [[getValue objectForKey:@"CPCONY"]doubleValue]* 100;
                }else{ //cotizador 6 y cotizador 8
                    CAT = [[getValue objectForKey:@"CPCONYN"]doubleValue]* 100;
                }
            }else{
                if (self.esConDevolucion) {// cotizador 5 y cotizador 7
                    CAT = [[getValue objectForKey:@"BCAT"]doubleValue]* 100;
                }else{ //cotizador 6 y cotizador 8
                    CAT = [[getValue objectForKey:@"BCATN"]doubleValue]* 100;
                }
            }
            factor = CAT;
        }
            break;
        case SCCoberturaTypeGFA:{
            double gfa = 0;
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            if (self.esConDevolucion) {// cotizador 5 y cotizador 7
                BAS = [[getValue objectForKey:@"BASF"]doubleValue]* 100;
                gfa = BAS + pMillar;
            }else{                      //cotizador 6 y cotizador 8
                BASN = [[getValue objectForKey:@"BASN"]doubleValue]* 100;
                gfa = BASN + pMillar;
            }
            factor = gfa;
        }
            break;
        case SCCoberturaTypeGE:{
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            double geTotal = 0;
            if (self.esConDevolucion) {// cotizador 5 y cotizador 7
                GE = [[getValue objectForKey:@"GE"]doubleValue]* 100;
                geTotal = GE + pMillar ;
            }else{ //cotizador 6 y cotizador 8
                GEN = [[getValue objectForKey:@"GEN"]doubleValue]* 100;
                geTotal = GEN + pMillar ;
            }
            factor = geTotal;
        }
            break;
        case SCCoberturaTypeBACY:{
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            if (self.esConDevolucion) {// cotizador 5 y cotizador 7
                BACY = [[getValue objectForKey:@"BASF"]doubleValue]* 100;
            }else{                     //cotizador 6 y cotizador 8
                BACY = [[getValue objectForKey:@"BASN"]doubleValue]* 100;
            }
            factor = BACY;
        }
            break;
        case SCCoberturaTypeGFC:{
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            if (self.esConDevolucion) {// cotizador 5 y cotizador 7
                GFC = [[getValue objectForKey:@"BASF"]doubleValue]* 100;
            }else{                      //cotizador 6 y cotizador 8
                GFC = [[getValue objectForKey:@"BASN"]doubleValue]* 100;
            }
            factor = GFC;
        }
            break;
            
            
        case SCCoberturaTypeCP:{
            
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            //conyuge se evalua inverso el sexo
            if (self.sexo == SCSexMujer){
                if (self.esConDevolucion) {// cotizador 5 y cotizador 7
                    CAT = [[getValue objectForKey:@"BCAT"]doubleValue]* 100;
                }else{ //cotizador 6 y cotizador 8
                    CAT = [[getValue objectForKey:@"BCATN"]doubleValue]* 100;
                }
            }else{
                
                if (self.esConDevolucion) {// cotizador 5 y cotizador 7
                    CAT = [[getValue objectForKey:@"CPCONY"]doubleValue]* 100;
                }else{ //cotizador 6 y cotizador 8
                    CAT = [[getValue objectForKey:@"CPCONYN"]doubleValue]* 100;
                }
            }
            factor = CAT;
            
        }
            break;
            
            
        case SCCoberturaTypeGFH:{
            int valorGFH = 0;
            
            if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPhone) {
                
                if (self.indexSeleccionGFH == 0) {
                    valorGFH = 0;
                }else if (self.indexSeleccionGFH == 1) {
                    valorGFH = 136;
                }else if (self.indexSeleccionGFH == 2){
                    valorGFH = 276;
                }else{
                    valorGFH = 409;
                }
                
            }else{
                
                if (self.indexSeleccionGFH == 0) {
                    valorGFH = 0;
                }else if (self.indexSeleccionGFH == 1) {
                    valorGFH = 136;
                }else if (self.indexSeleccionGFH == 2){
                    valorGFH = 276;
                }else{
                    valorGFH = 409;
                }
                
            }
            factor = valorGFH;
        }
            break;
            
        case SCCoberturaTypeCC:{
            
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            if (self.esConDevolucion) {// cotizador 5 y cotizador 7
                BAS = [[getValue objectForKey:@"BASF"]doubleValue]* 100;
            }else{                     //cotizador 3 y cotizador 4
                BAS = [[getValue objectForKey:@"BASN"]doubleValue]* 100;
            }
            factor = BAS;
        }
            break;
            
        case SCCoberturaTypeCP1:{
            
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            
            if (self.sexIndex == 1){
                if (self.esConDevolucion) {// cotizador 5 y cotizador 7
                    CAT = [[getValue objectForKey:@"BCAT"]doubleValue]* 100;
                }else{//cotizador 3 y cotizador 4
                    CAT = [[getValue objectForKey:@"BCATN"]doubleValue]* 100;
                }
            }else{
                if (self.esConDevolucion) {// cotizador 5 y cotizador 7
                    CAT = [[getValue objectForKey:@"CPCONY"]doubleValue]* 100;
                }else{ //cotizador 6 y cotizador 8
                    CAT = [[getValue objectForKey:@"CPCONYN"]doubleValue]* 100;
                }
            }
            factor = CAT;
        }
            break;
            
        case SCCoberturaTypeCP2:{
            
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            
            if (self.sexIndex == 1){
                if (self.esConDevolucion) {// cotizador 5 y cotizador 7
                    CAT = [[getValue objectForKey:@"BCAT"]doubleValue]* 100;
                }else{//cotizador 3 y cotizador 4
                    CAT = [[getValue objectForKey:@"BCATN"]doubleValue]* 100;
                }
            }else{
                if (self.esConDevolucion) {// cotizador 5 y cotizador 7
                    CAT = [[getValue objectForKey:@"CPCONY"]doubleValue]* 100;
                }else{ //cotizador 6 y cotizador 8
                    CAT = [[getValue objectForKey:@"CPCONYN"]doubleValue]* 100;
                }
            }
            factor = CAT;
        }
            break;
            
        case SCCoberturaTypeCP3:{
            
            getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.edad];
            
            if (self.sexIndex == 1){
                if (self.esConDevolucion) {// cotizador 5 y cotizador 7
                    CAT = [[getValue objectForKey:@"BCAT"]doubleValue]* 100;
                }else{ //cotizador 6 y cotizador 8
                    CAT = [[getValue objectForKey:@"BCATN"]doubleValue]* 100;
                }
            }else{
                if (self.esConDevolucion) {// cotizador 5 y cotizador 7
                    CAT = [[getValue objectForKey:@"CPCONY"]doubleValue]* 100;
                }else{ //cotizador 6 y cotizador 8
                    CAT = [[getValue objectForKey:@"CPCONYN"]doubleValue]* 100;
                }
            }
            factor = CAT;
        }
            break;
        default:
            break;
    }
    return factor;
}

- (BOOL)isOverflowed
{
    return (self.isSelected && self.sumaAseguradaDeCalculo > self.limiteSumaAseguradaParaCotizadorPorPrima && self.limiteSumaAseguradaParaCotizadorPorPrima > 0);
}

- (BOOL)isUnderLimit
{
    return (self.isSelected && self.sumaAseguradaDeCalculo < self.min);
}

@end
