//
//  SCCotizador.m
//  SIPAC
//
//  Created by MacBook Pro de Hugo on 05/08/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import "SCCotizador.h"
#import "SCDatabaseManager.h"

@interface SCCotizador ()

@end

@implementation SCCotizador

-(id)initWithCoberturas:(NSArray*)nuevasCoberturas prima:(BOOL)isPrima{
    self = [super init];
    if (self) {
        self.coberturas = nuevasCoberturas;
        self.esPrima = isPrima;
        for (SCCobertura *cobertura in nuevasCoberturas) {
            cobertura.esMenorEdad = cobertura.edadReal < 18 ? YES : NO;
            cobertura.tipoCalculo = self.calculo;
            cobertura.tipoPago = self.pago;
            cobertura.esConDevolucion = self.esConDevolucion;
            cobertura.esNuevaPoliza = self.esNuevaPoliza;
            cobertura.esValida = YES;
            cobertura.enabled = YES;
            
        }
    }
    
    return self;
}

- (void)calcularCoberturas {
    for (SCCobertura *cobertura in self.coberturas) {
        cobertura.esMenorEdad = cobertura.edadReal < 18 ? YES : NO;
        cobertura.esConDevolucion = self.esConDevolucion;
        cobertura.esNuevaPoliza = self.esNuevaPoliza;
        cobertura.tipoPago = self.pago;
    }
    if (self.esPrima) {
        [self calcularCoberturasPorPrima];
    } else {
        double primaAnualTotal = 0;
        [self chooseOcupacion:[self.delegate ocupacionActualParaCotizador:self]];
        for (SCCobertura *cobertura in self.coberturas) {
            if (cobertura != [self coberturaForType:SCCoberturaTypeBIT]) {
                [cobertura calcular:millar invalidez:invalidez accidente:accidente];
                if (!self.esPrima) {
                    [self.delegate cotizador:self didUpdateCobertura:cobertura];
                }
            }
            if (cobertura.isSelected || cobertura.type == SCCoberturaTypeGFH) {
                primaAnualTotal += cobertura.primaAnual;
            }
        }
        self.resultadoSumasAseguradas = primaAnualTotal;
        if (self.esNuevaPoliza) {
            [self validacion1y3];
        } else {
            [self validacion2y4];
        }
        [self calcularBIT];
        if ([self coberturaForType:SCCoberturaTypeBIT].isSelected) {
            self.resultadoSumasAseguradas += [self coberturaForType:SCCoberturaTypeBIT].primaAnual;
        }
        [self.delegate cotizador:self didGetSumaAseguradaTotal:self.resultadoSumasAseguradas];
        NSInteger cont = 0;
        for (SCCobertura *cobertura in self.coberturas) {
            [self evaluateChangeInSumaAsegurada:cobertura.sumaAsegurada index:cont editandoTextField:NO];
            cont ++;
        }
    }
}

-(void)calcularCobertura:(SCCobertura *)cobertura{
    
    double primaAnualTotal = 0;

    [cobertura calcular:millar invalidez:invalidez accidente:accidente];
    if (!self.esPrima) {
        [self.delegate cotizador:self didUpdateCobertura:cobertura];
    }
    
    if (cobertura.isSelected || cobertura.type == SCCoberturaTypeGFH) {
        primaAnualTotal += cobertura.primaAnual;
    }
    self.resultadoSumasAseguradas = primaAnualTotal;
    
    if (self.esNuevaPoliza) {
        [self validacion1y3];
    } else {
        [self validacion2y4];
    }
    
    [self calcularBIT];
    if ([self coberturaForType:SCCoberturaTypeCII] || [self coberturaForType:SCCoberturaTypeCMA] || [self coberturaForType:SCCoberturaTypeTIBA]) {
    }else{
        if ([self coberturaForType:SCCoberturaTypeBIT].isSelected) {
            [self coberturaForType:SCCoberturaTypeBIT].primaAnual = 0;
            self.resultadoSumasAseguradas += [self coberturaForType:SCCoberturaTypeBIT].primaAnual;
        }
    }
    [self.delegate cotizador:self didGetSumaAseguradaTotal:self.resultadoSumasAseguradas];
    
}

- (void)limitarCoberturasUsandoBit:(BOOL)doBit numerador:(float)numerador denominador:(float)denominador
{
    for (SCCobertura *cobertura in self.coberturas) {
        if (cobertura.type != SCCoberturaTypeBIT) {
            if (!cobertura.sumaCalculoCorregida) {
                cobertura.sumaAseguradaDeCalculo = (numerador/denominador)*[self factorMultiplicacionParaCobertura:cobertura];
                if (cobertura.isOverflowed) {
                    numerador -= [cobertura calcularFactoresBeneficios:millar invalidez:invalidez accidente:accidente pCalculandoFactorBit:NO] * cobertura.limiteSumaAseguradaParaCotizadorPorPrima;
                    denominador -= [cobertura calcularFactoresBeneficios:millar invalidez:invalidez accidente:accidente pCalculandoFactorBit:NO] * [self factorMultiplicacionParaCobertura:cobertura];
                    if (doBit) {
                        numerador -= [cobertura calcularFactoresBeneficios:millar invalidez:invalidez accidente:accidente pCalculandoFactorBit:YES] * [self calcularFactorBIT] * cobertura.limiteSumaAseguradaParaCotizadorPorPrima;
                        denominador -= [cobertura calcularFactoresBeneficios:millar invalidez:invalidez accidente:accidente pCalculandoFactorBit:YES] * [self calcularFactorBIT] * [self factorMultiplicacionParaCobertura:cobertura];
                    }
                    cobertura.sumaAseguradaDeCalculo = cobertura.limiteSumaAseguradaParaCotizadorPorPrima;
                    cobertura.sumaCalculoCorregida = YES;
                    [self limitarCoberturasUsandoBit:doBit numerador:numerador denominador:denominador];
                    break;
                } else if (cobertura.isUnderLimit && [self factorMultiplicacionParaCobertura:cobertura] == 1) {
                    if (cobertura.min < [[self.coberturas objectAtIndex:0] sumaAsegurada] * [self factorMultiplicacionParaCobertura:cobertura]) {
                        numerador -= [cobertura calcularFactoresBeneficios:millar invalidez:invalidez accidente:accidente pCalculandoFactorBit:NO] * cobertura.min;
                        denominador -= [cobertura calcularFactoresBeneficios:millar invalidez:invalidez accidente:accidente pCalculandoFactorBit:NO] * [self factorMultiplicacionParaCobertura:cobertura];
                        if (doBit) {
                            numerador -= [cobertura calcularFactoresBeneficios:millar invalidez:invalidez accidente:accidente pCalculandoFactorBit:YES] * [self calcularFactorBIT] * cobertura.min;
                            denominador -= [cobertura calcularFactoresBeneficios:millar invalidez:invalidez accidente:accidente pCalculandoFactorBit:YES] * [self calcularFactorBIT] * [self factorMultiplicacionParaCobertura:cobertura];
                        }
                        cobertura.sumaAseguradaDeCalculo = cobertura.min;
                        cobertura.sumaCalculoCorregida = YES;
                        [self limitarCoberturasUsandoBit:doBit numerador:numerador denominador:denominador];
                        break;
                    }
                }
            }
        }
    }
}

- (void)calcularCoberturasPorPrima
{
    if (self.realAge > 55) {
        [[self.coberturas objectAtIndex:1] setEnabled:NO];
        [[self.coberturas objectAtIndex:2] setEnabled:NO];
        
        [[self.coberturas objectAtIndex:1] setSelected:NO];
        [[self.coberturas objectAtIndex:2] setSelected:NO];
    }
    if (self.realAge > 65) {
        [[self.coberturas objectAtIndex:3] setEnabled:NO];
        [[self.coberturas objectAtIndex:4] setEnabled:NO];
        [[self.coberturas objectAtIndex:5] setEnabled:NO];
        
        [[self.coberturas objectAtIndex:3] setSelected:NO];
        [[self.coberturas objectAtIndex:4] setSelected:NO];
        [[self.coberturas objectAtIndex:5] setSelected:NO];
    }
    if (accidente == -1) {
        [[self.coberturas objectAtIndex:3] setEnabled:NO];
        [[self.coberturas objectAtIndex:4] setEnabled:NO];
        
        [[self.coberturas objectAtIndex:3] setSelected:NO];
        [[self.coberturas objectAtIndex:4] setSelected:NO];
    }
    if (invalidez == -1) {
        [[self.coberturas objectAtIndex:1] setEnabled:NO];
        [[self.coberturas objectAtIndex:2] setEnabled:NO];
        
        [[self.coberturas objectAtIndex:1] setSelected:NO];
        [[self.coberturas objectAtIndex:2] setSelected:NO];
    }
    if (millar == -1 || millar == -100.000000) {
        for (SCCobertura *cobertura in self.coberturas) {
            [cobertura setEnabled:NO];
            [cobertura setSelected:NO];
            [self.delegate cotizador:self didUpdateCobertura:cobertura];
        }
        [self.delegate cotizador:self didGetSumaAseguradaTotal:0];
        return;
    }
    if (0 == millar && 0 == invalidez && 0 == accidente) {
        millar = 0;
        invalidez = 1;
        accidente = 1;
    }
    double sumaFactores = 0;
    double sumaFactoresParaBit = 0;
    BOOL tieneSolucion = YES;
    BOOL doBit = [[self.coberturas objectAtIndex:1] isSelected];
    for (SCCobertura *cobertura in self.coberturas) {
        cobertura.sumaCalculoCorregida = NO;
        double factorMultiplicacion = [self factorMultiplicacionParaCobertura:cobertura];
        sumaFactores += factorMultiplicacion * [cobertura calcularFactoresBeneficios:millar invalidez:invalidez accidente:accidente pCalculandoFactorBit:NO];
        if (doBit) {
            sumaFactoresParaBit += factorMultiplicacion * [cobertura calcularFactoresBeneficios:millar invalidez:invalidez accidente:accidente pCalculandoFactorBit:YES] * [self calcularFactorBIT];
        }
    }
    double numerador = (self.primaTotalCotizadorPrima - self.primaExcedenteAnualPorPrima) * 100000;
    double denominador = sumaFactoresParaBit + sumaFactores;
    [self limitarCoberturasUsandoBit:doBit numerador:numerador denominador:denominador];
    if((self.realAge < 18 && [[self.coberturas objectAtIndex:0] sumaAseguradaDeCalculo] > 3000000) || (self.realAge >= 18 && [[self.coberturas objectAtIndex:0] sumaAseguradaDeCalculo] > 100000000))
    {
        tieneSolucion = NO;
    }
    if (!tieneSolucion) {
        [self.delegate cotizadorPorPrimaSinSolucion:self];
        for (SCCobertura *cobertura in self.coberturas) {
            if (cobertura.isSelected && cobertura.type != SCCoberturaTypeBIT) {
                if (cobertura.sumaAseguradaDeCalculo > cobertura.limiteSumaAseguradaParaCotizadorPorPrima) {
                    cobertura.sumaAseguradaDeCalculo = cobertura.limiteSumaAseguradaParaCotizadorPorPrima;
                    if (cobertura.sumaAseguradaDeCalculo < 0) {
                        cobertura.sumaAseguradaDeCalculo = 0;
                    }
                }
            }
        }
    } else if (self.realAge < 18) {
        double cantidad1 = [[self.coberturas objectAtIndex:0] isSelected] ? [[self.coberturas objectAtIndex:0] sumaAseguradaDeCalculo] : 0;
        double cantidad2 = [[self.coberturas objectAtIndex:6] isSelected] ? [[self.coberturas objectAtIndex:6] sumaAseguradaDeCalculo] : 0;
        double cantidad3 = [[self.coberturas objectAtIndex:7] isSelected] ? [[self.coberturas objectAtIndex:7] sumaAseguradaDeCalculo] : 0;
        if (cantidad1 + cantidad2 + cantidad3 > 3000000) {
            [self.delegate cotizadorPorPrimaExcede:self];
            tieneSolucion = NO;
        }
    }
    double sumaPrimaAnualCoberturas = 0;
    for (SCCobertura *cobertura in self.coberturas) {
        double primaBeneficio = 0;
        if (cobertura.type == SCCoberturaTypeBIT && cobertura.isSelected) {
            for (SCCobertura *coberturaEvaluar in self.coberturas) {
                if (coberturaEvaluar.type != SCCoberturaTypeBIT) {
                    double factorCoberturaConBIT = [coberturaEvaluar calcularFactoresBeneficios:millar invalidez:invalidez accidente:accidente pCalculandoFactorBit:YES] * coberturaEvaluar.sumaAseguradaDeCalculo;
                    primaBeneficio += factorCoberturaConBIT;
                }
            }
            primaBeneficio *= [self calcularFactorBIT];
            primaBeneficio /= 100000;
        } else {
            primaBeneficio = [self calcularPrimaParaCobertura:cobertura sumaAsegurada:cobertura.sumaAseguradaDeCalculo];
        }
        if (cobertura.isSelected) {
            cobertura.sumaAsegurada = cobertura.sumaAseguradaDeCalculo;
            cobertura.primaAnual = primaBeneficio;
            sumaPrimaAnualCoberturas += cobertura.primaAnual;
            if (cobertura.type == SCCoberturaTypeBIT) {
                cobertura.stringSumaAsegurada = @"Cubierto";
            } else if (cobertura.type == SCCoberturaTypeBAS && !tieneSolucion) {
                cobertura.stringSumaAsegurada = @"Sin solución";
            }
        } else {
            cobertura.sumaAsegurada = 0;
            cobertura.primaAnual = 0;
            if (cobertura.type == SCCoberturaTypeBIT) {
                cobertura.stringSumaAsegurada = @"No cubierto";
            } else {
                cobertura.stringSumaAsegurada = @"Excluída";
            }
        }
        [self.delegate cotizador:self didUpdateCobertura:cobertura];
    }
    
    
    
    BOOL hayCoberturasAbajoLimite = NO;
    for (SCCobertura *cobertura in self.coberturas) {
        if (cobertura.isSelected && cobertura.type != SCCoberturaTypeBIT && cobertura.sumaAsegurada < cobertura.min) {
            hayCoberturasAbajoLimite = YES;
            break;
        }
    }
    if (hayCoberturasAbajoLimite && tieneSolucion) {
        [self mostrarCotizadorInsuficiente];
        return;
    }
    if (sumaPrimaAnualCoberturas > (self.primaTotalCotizadorPrima  - self.primaExcedenteAnualPorPrima + 5)) {
        [self mostrarCotizadorInsuficiente];
    } else {
        [self.delegate cotizador:self didGetSumaAseguradaTotal:self.primaTotalCotizadorPrima];
    }
}

- (void)mostrarCotizadorInsuficiente
{
    [self.delegate cotizadorPorPrimaInsuficiente:self];
    for (SCCobertura *cobertura in self.coberturas) {
        cobertura.stringPrimaAnual = @"Insuficiente";
        cobertura.stringSumaAsegurada = @"Insuficiente";
        [self.delegate cotizador:self didUpdateCobertura:cobertura];
    }
}

- (double)factorMultiplicacionParaCobertura:(SCCobertura *)cobertura
{
    double factorMultiplicacion = 1;
    double factorGFs = 1;
    if (self.esNuevaPoliza) {
        factorGFs = 0.35;
    }
    switch (cobertura.type) {
        case SCCoberturaTypeGFA:
            factorMultiplicacion = factorGFs;
            break;
        case SCCoberturaTypeGFC:
            factorMultiplicacion = 0.35;
            break;
        case SCCoberturaTypeGFH:
            factorMultiplicacion = 0.35;
            break;
        case SCCoberturaTypeBACY:
            if ([[self.coberturas objectAtIndex:9] isSelected] && [[self.coberturas objectAtIndex:8] isSelected]) {
                factorMultiplicacion = 0.65;
            }
            break;
        default:
            break;
    }
    return factorMultiplicacion;
}

- (double)calcularPrimaParaCobertura:(SCCobertura *)cobertura sumaAsegurada:(double)sumaAsegurada
{
    double primaBeneficio = 0;
    primaBeneficio = [cobertura calcularFactoresBeneficios:millar invalidez:invalidez accidente:accidente pCalculandoFactorBit:NO];
    primaBeneficio *= sumaAsegurada/100000;
    return primaBeneficio;
}

- (void)reglaGFA_GE_BASTresMillones
{
    /*Se aplica la regla de que la suma de BAS + GE + GFA, no pueden exceder los 3,000,000 en menores de edad
     si solo seleccionamos BAS y GE;   y la suma de BAS + GE y sobrepasan los 3 millones
     GE y BAS  se reparten el valor y lo dividen entre dos, esto es 1,500,000
     
     Si seleccionamos BAS + GFA + GE y sobrepasan los 3,000,000 en  menores de edad
     GFA, toma el valor máximo permitido (180,000)
     
     El valor que queda de la resta  de 3,000,000 menos  GFA
     (    3,000,000 -   180,000) / 2  , se reparte en partes iguales entre BAS Y GE,
     BAS = GE = 1,410,000
     
     Si seleccionamos solo GE y GFA y sobrepasan los 3,000,000 en  menores de edad
     GFA toma el valor de 180,000
     GE toma el valor de 1,410.00
     */

    SCCobertura *coberturaBAS = [self coberturaForType:SCCoberturaTypeBAS];
    SCCobertura *coberturaGFA = [self coberturaForType:SCCoberturaTypeGFA];
    SCCobertura *coberturaGE = [self coberturaForType:SCCoberturaTypeGE];
    
    if (self.realAge < 18) {
        if (coberturaGFA.isSelected && coberturaGE.isSelected && coberturaBAS.isSelected) {
            
            if (coberturaBAS.sumaAsegurada + coberturaGE.sumaAsegurada + coberturaGFA.sumaAsegurada > 3000000) {
                
                double resta = 3000000 - coberturaGFA.sumaAsegurada;
                
                double division = resta/2;
                coberturaBAS.sumaAsegurada = division;
                coberturaGE.sumaAsegurada = division;
                
//                if (self.esNuevaPoliza) {
//                    
//                    if ([self coberturaForType:SCCoberturaTypeTIBA].isSelected) {
//                        [self coberturaForType:SCCoberturaTypeTIBA].sumaAsegurada = coberturaBAS.sumaAsegurada;
//                    }
//                    if ([self coberturaForType:SCCoberturaTypeCII].isSelected) {
//                        [self coberturaForType:SCCoberturaTypeCII].sumaAsegurada = coberturaBAS.sumaAsegurada;
//                    }
//                    if ([self coberturaForType:SCCoberturaTypeCMA].isSelected) {
//                        [self coberturaForType:SCCoberturaTypeCMA].sumaAsegurada = coberturaBAS.sumaAsegurada;
//                    }
//                }
            }
        } else if (coberturaGFA.isSelected && coberturaBAS.isSelected) {
            
            if (coberturaBAS.sumaAsegurada + coberturaGFA.sumaAsegurada > 3000000) {
                
                coberturaBAS.sumaAsegurada = 3000000-coberturaGFA.sumaAsegurada;
                
//                if (self.esNuevaPoliza) {
//                    
//                    if ([self coberturaForType:SCCoberturaTypeTIBA].isSelected) {
//                        [self coberturaForType:SCCoberturaTypeTIBA].sumaAsegurada = coberturaBAS.sumaAsegurada;
//                    }
//                    if ([self coberturaForType:SCCoberturaTypeCII].isSelected) {
//                        [self coberturaForType:SCCoberturaTypeCII].sumaAsegurada = coberturaBAS.sumaAsegurada;
//                    }
//                    if ([self coberturaForType:SCCoberturaTypeCMA].isSelected) {
//                        [self coberturaForType:SCCoberturaTypeCMA].sumaAsegurada = coberturaBAS.sumaAsegurada;
//                    }
//                }
                
            }
        } else if (coberturaGE.isSelected && coberturaBAS.isSelected) {
            
            if (coberturaBAS.sumaAsegurada + coberturaGE.sumaAsegurada > 3000000) {
                
                coberturaBAS.sumaAsegurada = 1500000;
                coberturaGE.sumaAsegurada = 1500000;
                
//                if (self.esNuevaPoliza) {
//                    
//                    if ([self coberturaForType:SCCoberturaTypeTIBA].isSelected) {
//                        [self coberturaForType:SCCoberturaTypeTIBA].sumaAsegurada = coberturaBAS.sumaAsegurada;
//                    }
//                    if ([self coberturaForType:SCCoberturaTypeCII].isSelected) {
//                        [self coberturaForType:SCCoberturaTypeCII].sumaAsegurada = coberturaBAS.sumaAsegurada;
//                    }
//                    if ([self coberturaForType:SCCoberturaTypeCMA].isSelected) {
//                        [self coberturaForType:SCCoberturaTypeCMA].sumaAsegurada = coberturaBAS.sumaAsegurada;
//                    }
//                }
            }
        } else if (coberturaGE.isSelected && coberturaGFA.isSelected) {
            
            if (coberturaGFA.sumaAsegurada + coberturaGE.sumaAsegurada > 3000000) {
                
                coberturaGE.sumaAsegurada = 3000000 - coberturaGFA.sumaAsegurada;
                
//                if (self.esNuevaPoliza) {
//                    
//                    if ([self coberturaForType:SCCoberturaTypeTIBA].isSelected) {
//                        [self coberturaForType:SCCoberturaTypeTIBA].sumaAsegurada = coberturaBAS.sumaAsegurada;
//                    }
//                    if ([self coberturaForType:SCCoberturaTypeCII].isSelected) {
//                        [self coberturaForType:SCCoberturaTypeCII].sumaAsegurada = coberturaBAS.sumaAsegurada;
//                    }
//                    if ([self coberturaForType:SCCoberturaTypeCMA].isSelected) {
//                        [self coberturaForType:SCCoberturaTypeCMA].sumaAsegurada = coberturaBAS.sumaAsegurada;
//                    }
//                }
            }
        }
    }
    
    [self reloadSuma];
    double primaAnualTotal = 0;
    [self chooseOcupacion:[self.delegate ocupacionActualParaCotizador:self]];
    for (SCCobertura *cobertura in self.coberturas) {
        if (cobertura != [self coberturaForType:SCCoberturaTypeBIT]) {
            [cobertura calcular:millar invalidez:invalidez accidente:accidente];
            if (!self.esPrima) {
                [self.delegate cotizador:self didUpdateCobertura:cobertura];
            }
        }
        if (cobertura.isSelected) {
            primaAnualTotal += cobertura.primaAnual;
        }
        self.resultadoSumasAseguradas = primaAnualTotal;
    }
    
    [self.delegate cotizador:self didGetSumaAseguradaTotal:self.resultadoSumasAseguradas];
}


- (void)bacySelected{
    //que regrese el valor a 100 000 cuando selecciono de nuevo bacy
    
    if ([self coberturaForType:SCCoberturaTypeGFC].isSelected == NO && [self coberturaForType:SCCoberturaTypeBACY].isSelected == YES){
        
        if (self.esNuevaPoliza) {
            if ([self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada > self.valor200Salarios){
                [self coberturaForType:SCCoberturaTypeBACY].sumaAsegurada = self.valor200Salarios;
            }else{
                [self coberturaForType:SCCoberturaTypeBACY].sumaAsegurada = [self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada;
            }
        }
        
        [[self coberturaForType:SCCoberturaTypeBACY] calcular:millar invalidez:invalidez accidente:accidente];
        [self.delegate cotizador:self didUpdateCobertura:[self coberturaForType:SCCoberturaTypeBACY]];
        [self calcularBIT];
        double  primaAnualTotal = 0;
        for (SCCobertura *cobertura in self.coberturas) {
            if (cobertura.isSelected) {
                primaAnualTotal += cobertura.primaAnual;
            }
        }
        self.resultadoSumasAseguradas = primaAnualTotal;
        [self.delegate cotizador:self didGetSumaAseguradaTotal:self.resultadoSumasAseguradas];
    }
}

-(void)sumaAseguradaBacy
{ //Modificado
    if (self.esNuevaPoliza) {
        if ([self coberturaForType:SCCoberturaTypeGFC].isSelected == YES && [self coberturaForType:SCCoberturaTypeBACY].isSelected == YES){
            
            if ([self coberturaForType:SCCoberturaTypeBACY].sumaAsegurada != self.valor200Salarios) {
                [self coberturaForType:SCCoberturaTypeBACY].sumaAsegurada = [self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada * 0.65;
            }
            if (self.valor200Salarios > [self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada * 0.65) {
                [self coberturaForType:SCCoberturaTypeBACY].sumaAsegurada = [self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada * 0.65;
            }
        }
        
        [[self coberturaForType:SCCoberturaTypeBACY] calcular:millar invalidez:invalidez accidente:accidente];
        [self.delegate cotizador:self didUpdateCobertura:[self coberturaForType:SCCoberturaTypeBACY]];
        [self calcularBIT];
        double  primaAnualTotal = 0;
        for (SCCobertura *cobertura in self.coberturas) {
            if (cobertura.isSelected) {
                primaAnualTotal += cobertura.primaAnual;
            }
        }
        self.resultadoSumasAseguradas = primaAnualTotal;
        [self.delegate cotizador:self didGetSumaAseguradaTotal:self.resultadoSumasAseguradas];
    }
}
// checar cuando modificas el textfield de bas en textendediting
-(void)sumaAseguradaBacy:(double)sumaAseguradaBAS
{ //Modificado
//    //NSLog(@"**** %f",sumaAseguradaBAS);
//    if (self.esNuevaPoliza) {
//        
//        if ([self coberturaForType:SCCoberturaTypeGFC].isSelected == YES && [self coberturaForType:SCCoberturaTypeBACY].isSelected == YES){
//            if ([self coberturaForType:SCCoberturaTypeBACY].sumaAsegurada != self.valor200Salarios) {
//                [self coberturaForType:SCCoberturaTypeBACY].sumaAsegurada = [self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada * 0.65;
//            }
//            if (self.valor200Salarios > [self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada * 0.65) {
//                [self coberturaForType:SCCoberturaTypeBACY].sumaAsegurada = [self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada * 0.65;
//            }
//        }
//  
////        if ([self coberturaForType:SCCoberturaTypeGFC].sumaAsegurada < self.valor20Salarios){
////            
////            if ([self coberturaForType:SCCoberturaTypeBACY].sumaAsegurada < [self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada) {
////                [self coberturaForType:SCCoberturaTypeBACY].sumaAsegurada = sumaAseguradaBAS * 0.65;
////            }else{
////                [self coberturaForType:SCCoberturaTypeBACY].sumaAsegurada = [self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada;
////            }
////        }
//        [[self coberturaForType:SCCoberturaTypeBACY] calcular:millar invalidez:invalidez accidente:accidente];
//        [self.delegate cotizador:self didUpdateCobertura:[self coberturaForType:SCCoberturaTypeBACY]];
//        [self calcularBIT];
//        double  primaAnualTotal = 0;
//        for (SCCobertura *cobertura in self.coberturas) {
//            if (cobertura.isSelected) {
//                primaAnualTotal += cobertura.primaAnual;
//            }
//        }
//        self.resultadoSumasAseguradas = primaAnualTotal;
//        [self.delegate cotizador:self didGetSumaAseguradaTotal:self.resultadoSumasAseguradas];
//    }
}

- (void)validacion1y3
{
    double primaAnualTotal = 0;
    SCCobertura *coberturaBACY = [self coberturaForType:SCCoberturaTypeBACY];
    SCCobertura *coberturaGFC = [self coberturaForType:SCCoberturaTypeGFC];
    SCCobertura *coberturaBAS = [self coberturaForType:SCCoberturaTypeBAS];
    double primeroBACY = 0;
    if (coberturaBACY.isSelected && coberturaGFC.isSelected) {
        primeroBACY = coberturaBACY.primaAnual;
    }
    for (SCCobertura *cobertura in self.coberturas) {
        if (cobertura == [self coberturaForType:SCCoberturaTypeBACY] || cobertura == [self coberturaForType:SCCoberturaTypeGFC]) {
            [cobertura calcular:millar invalidez:invalidez accidente:accidente];
            [self.delegate cotizador:self didUpdateCobertura:cobertura];
            if (cobertura.isSelected) {
                primaAnualTotal += cobertura.primaAnual;
            }
        }
    }
    /*
     Si se quieren seleccionar los beneficios de BACY Y GFC al mismo tiempo, la suma de ambas sumas aseguradas no puede exceder la suma segurada de BAS entonces:
     Si ( BACY + GFC) >    BAS;     y   GFC > 35% de BAS entonces GFC = 180,000  ( GFC toma su valor máximo permitido)     y   BACY =  BAS - 180,000  ; pero si la resta de BAS - 180,000, excede el valor maximo permitido para BACY, BACY toma su valor maximo permitido de 344,760
     */
    if ([self coberturaForType:SCCoberturaTypeGFC].isSelected == YES && [self coberturaForType:SCCoberturaTypeBACY].isSelected == YES) {
        double sumaAmbas = coberturaBACY.sumaAsegurada + coberturaGFC.sumaAsegurada;
        if (sumaAmbas > coberturaBAS.sumaAsegurada) {
            if (coberturaGFC.sumaAsegurada*0.35 > coberturaBAS.sumaAsegurada) {
                coberturaGFC.sumaAsegurada = 180000;
                coberturaBACY.sumaAsegurada = coberturaBAS.sumaAsegurada - 180000;
                double maxBACY = coberturaBACY.esMenorEdad ? coberturaBACY.maxMenores : coberturaBACY.maxMayores;
                if (coberturaBACY.sumaAsegurada > maxBACY) {
                    coberturaBACY.sumaAsegurada = maxBACY;
                }
            }
        }
    }
    //Se aplica la regla de que la suma de BAS + GE + GFA, no pueden exceder los 3,000,000 en menores de edad
    [self reglaGFA_GE_BASTresMillones];
    double segundoBACY = 0;
    if (coberturaBACY.isSelected && coberturaGFC.isSelected) {
        segundoBACY = coberturaBACY.primaAnual;
    }
    self.resultadoSumasAseguradas -= primeroBACY - segundoBACY;
}



-(void)validationSumaAseguradaOpcionInclusionesSeleccionBeneficio {
    
    for (SCCobertura *cobertura in self.coberturas) {
        if (cobertura.type != SCCoberturaTypeBIT) {
            if (!cobertura.isSelected) {
                cobertura.sumaAsegurada = 10000;
                [self.delegate cotizador:self nuevaSumaAseguradaEnCobertura:cobertura];
                [cobertura calcular:millar invalidez:invalidez accidente:accidente];
                [self.delegate cotizador:self didUpdateCobertura:cobertura];
            }
        }
    }
}
-(void)validationSumaAseguradaOpcionInclusiones{
    
    //El valor que toman por default las Sumas aseguradas es de 10,000 (incluyendo los GF), su valor debe tomar el valor proporcional a esa suma asegurada
    
    for (SCCobertura *cobertura in self.coberturas) {
        if (cobertura.isSelected){
        }else{
            cobertura.sumaAsegurada = 10000;
            [self.delegate cotizador:self nuevaSumaAseguradaEnCobertura:cobertura];
        }
    }
}

-(void)validationSumaAseguradaOpcionNuevaPoliza{
    for (SCCobertura *cobertura in self.coberturas) {
        cobertura.sumaAsegurada = 100000;
        [self.delegate cotizador:self nuevaSumaAseguradaEnCobertura:cobertura];
    }
    
}

- (void)validacion2y4 {
    
    //GFA , GFC, GFH pueden tomar cualquier valor de suma asegurada, hasta su máximo sin importar el valor de BAS NOTA:realizado en initCotizador (VC).
    
    //Se aplica la regla de que la suma de BAS + GE + GFA, no pueden exceder los 3,000,000 en menores de edad
    [self reglaGFA_GE_BASTresMillones];
}

- (double)calcularBIT {

    NSDictionary *getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.estimateAge];
    double BIT = 0;
    if (self.esConDevolucion) {
        BIT = [[getValue objectForKey:@"BIT"]doubleValue];
    }else{
        BIT = [[getValue objectForKey:@"BITN"]doubleValue];
    }
    
    double bitSuma = 0;
    
    for (SCCobertura *aCobertura in self.coberturas) {
        
        if (aCobertura.isSelected == YES &&
            aCobertura.type != SCCoberturaTypeBIT &&
            aCobertura.type != SCCoberturaTypeCII &&
            aCobertura.type != SCCoberturaTypeCMA &&
            aCobertura.type != SCCoberturaTypeTIBA) {
            
            bitSuma += aCobertura.primaAnual;
        }
    }
    double Bitresult =  ((bitSuma * BIT) * invalidez)/100;
    self.resultadoBIT = Bitresult;
    SCCobertura *coberturaBit = [self coberturaForType:SCCoberturaTypeBIT];
    coberturaBit.primaAnual = Bitresult;
    [self.delegate cotizador:self didUpdateCobertura:coberturaBit];
    
    return Bitresult;
}

- (double)calcularFactorBIT {
    NSDictionary *getValue = [[SCDatabaseManager sharedInstance]GetAgeInfo:self.estimateAge];
    double BIT = 0;
    if (self.esConDevolucion) {
        BIT = [[getValue objectForKey:@"BIT"]doubleValue];
    }else{
        BIT = [[getValue objectForKey:@"BITN"]doubleValue];
    }
    double factor = (BIT * invalidez)/100;
    return factor;
}

- (SCCobertura *)coberturaForType:(SCCoberturaType)aType {
    if (!self.esPrima) {
        int index = [self.coberturas indexOfObjectPassingTest:^BOOL(id obj, NSUInteger idx, BOOL *stop) {
            if ([(SCCobertura *)obj type] == aType) {
                return YES;
            }
            else {
                return NO;
            }
        }];
        return [self.coberturas objectAtIndex:index];
    }
    return nil;
}



- (double)sumaAseguradaActualForCoberturaType:(SCCoberturaType)theType {
    if (!self.esPrima) {
        return [[self coberturaForType:theType] sumaAsegurada];
    }
    return 0;
}

- (void)chooseOcupacion:(int)newOcupation {
    
    //    NSDictionary *tmpDict = @{@"accidente": @"1", @"id":@"0",@"invalidez":@"1",@"millar":@"0",@"nombre":@"Sin Ocupación"};
    //    self.professions = [[SCDatabaseManager sharedInstance] GetProfessions];
    //    NSMutableArray *tmp = [self.professions mutableCopy];
    //    [tmp insertObject:tmpDict atIndex:0];
    //    self.professions = tmp;
    self.professions = [[SCDatabaseManager sharedInstance] GetProfessions];
    if (newOcupation < 0 || newOcupation >= self.professions.count) {
        
        if (millar == 0 && invalidez == 0 && accidente == 0) {
            millar = 0;
            invalidez = 1;
            accidente = 1;
        }
        return;
    }
    millar =[[[self.professions objectAtIndex:newOcupation]objectForKey:@"millar"]doubleValue]* 100;
    invalidez =[[[self.professions objectAtIndex:newOcupation]objectForKey:@"invalidez"]doubleValue];
    accidente =[[[self.professions objectAtIndex:newOcupation]objectForKey:@"accidente"]doubleValue];
    
    
    double millarNormal = (millar / 100);
    NSString *millarString = [NSString stringWithFormat:@"%.2f",millarNormal];
    NSString *invalidezString = [NSString stringWithFormat:@"%.2f",invalidez];
    NSString *accidenteString = [NSString stringWithFormat:@"%.2f",accidente];
    
    if ([accidenteString isEqualToString:@"0.0"]) {
        [self.delegate cotizador:self didGetExtraPrimaText:@""];
        return;
    }
    if (millar == 0 && invalidez == 0 && accidente == 0) {
        millar = 0;
        invalidez =1;
        accidente =1;
    }
    
    NSString *extraPrimas = [NSString stringWithFormat:@"vida: %@ al millar, Accidentes: %@ tantos, Invalidez: %@ tantos",millarString,accidenteString,invalidezString];
    if (invalidez == -1.000000) {
        extraPrimas = [NSString stringWithFormat:@"vida: %@ al millar, Accidentes: %@ tantos, Invalidez: Rechazo",millarString,accidenteString];
    }
    if (accidente == -1.000000) {
        extraPrimas = [NSString stringWithFormat:@"vida: %@ al millar, Accidentes: %@, Invalidez: %@ tantos",millarString,@"Rechazo",invalidezString];
    }
    if (accidente == -1.000000 && invalidez == -1.000000) {
        extraPrimas = [NSString stringWithFormat:@"vida: %@ al millar, Accidentes: %@, Invalidez: %@",millarString,@"Rechazo",@"Rechazo"];
    }
    if (millar == -100.000000) {
        extraPrimas = @"Rechazo";
        for (SCCobertura *aCobertura in self.coberturas) {
            aCobertura.enabled = NO;
            aCobertura.sumaAsegurada = 0;
        }
    }
    if (millar == 0 && invalidez == 1 && accidente == 1) {
        extraPrimas = @"";
        
    }
    [self.delegate cotizador:self didGetExtraPrimaText:extraPrimas];
    
    if (!self.esPrima) {
        if (invalidez == -1) {
            invalidez = 0;
            [self coberturaForType:SCCoberturaTypeCII].enabled = NO;
            [self coberturaForType:SCCoberturaTypeBIT].enabled = NO;
        }
        if (accidente == -1) {
            accidente = 0;
            [self coberturaForType:SCCoberturaTypeCMA].enabled = NO;
            [self coberturaForType:SCCoberturaTypeTIBA].enabled = NO;
        }
        if (millar == -1) {
            millar = 0;
            for (SCCobertura *aCobertura in self.coberturas) {
                aCobertura.enabled = NO;
            }
        }
        
        if (millar == 0 && invalidez == 0 && accidente == 0) {
            millar = 0;
            invalidez = 1;
            accidente = 1;
        }
    }
}

- (NSArray *)loadProfesions {
    
    //     NSDictionary *tmpDict = @{@"accidente": @"1", @"id":@"0",@"invalidez":@"1",@"millar":@"0",@"nombre":@"Sin Ocupación"};
    //    self.professions = [[SCDatabaseManager sharedInstance] GetProfessions];
    //    NSMutableArray *tmp = [self.professions mutableCopy];
    //    [tmp insertObject:tmpDict atIndex:0];
    //    self.professions = tmp;
    self.professions = [[SCDatabaseManager sharedInstance] GetProfessions];
    NSMutableArray *dataProfession = [[NSMutableArray alloc] init];
    for (int x = 0;x<self.professions.count; x++) {
        NSDictionary *dict = [self.professions objectAtIndex:x];
        [dataProfession addObject:[dict objectForKey:@"nombre"]];
        
    }
    return dataProfession;
}

- (void)selectOcupacion:(int)newOcupation {
    
    self.professions = [[SCDatabaseManager sharedInstance] GetProfessions];
    for (SCCobertura *aCobertura in self.coberturas) {
        aCobertura.enabled = YES;
        [self GastosFunerariosEnabledValidation];
    }
    [self limitAge];
    if (newOcupation < 0 || newOcupation >= self.professions.count) {
        return;
    }
    
    [self chooseOcupacion:newOcupation];
    [self extraPrimasValue];
}

-(void)limitAge {
    if (self.realAge > 55) {
        [self coberturaForType:SCCoberturaTypeCII].enabled = NO;
        [self coberturaForType:SCCoberturaTypeCII].selected = NO;
        [self coberturaForType:SCCoberturaTypeBIT].enabled = NO;
        [self coberturaForType:SCCoberturaTypeBIT].selected = NO;
    }
    if (self.realAge > 65) {
        [self coberturaForType:SCCoberturaTypeCII].enabled = NO;
        [self coberturaForType:SCCoberturaTypeCII].selected = NO;
        [self coberturaForType:SCCoberturaTypeBIT].enabled = NO;
        [self coberturaForType:SCCoberturaTypeBIT].selected = NO;
        [self coberturaForType:SCCoberturaTypeCMA].enabled = NO;
        [self coberturaForType:SCCoberturaTypeCMA].selected = NO;
        [self coberturaForType:SCCoberturaTypeTIBA].enabled = NO;
        [self coberturaForType:SCCoberturaTypeTIBA].selected = NO;
        [self coberturaForType:SCCoberturaTypeCAT].enabled = NO;
        [self coberturaForType:SCCoberturaTypeCAT].selected = NO;
    }
    [self refreshAllCoberturas];
}


-(void)reloadSuma{
    
    if (self.esNuevaPoliza == YES) {
        for (SCCobertura *aCobertura in self.coberturas) {
            if (aCobertura.isSelected &&  aCobertura.sumaAsegurada < [self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada){
            }else{
                if (aCobertura.type != SCCoberturaTypeGFA &&
                    aCobertura.type != SCCoberturaTypeGFC &&
                    aCobertura.type != SCCoberturaTypeGFH) {
                    aCobertura.sumaAsegurada = [self sumaAseguradaActualForCoberturaType:SCCoberturaTypeBAS];
                }
            }
            [self refreshAllCoberturas];
        }
    }
}

-(void)reloadSumaAsegurada {
    if (self.esPrima) {
        return;
    }
    if (self.esNuevaPoliza == YES) {
        for (SCCobertura *aCobertura in self.coberturas) {
            if (aCobertura.isSelected &&  aCobertura.sumaAsegurada < [self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada){
            }else{
                if (aCobertura.type != SCCoberturaTypeGFA &&
                    aCobertura.type != SCCoberturaTypeGFC &&
                    aCobertura.type != SCCoberturaTypeGFH) {
                    aCobertura.sumaAsegurada = [self sumaAseguradaActualForCoberturaType:SCCoberturaTypeBAS];
                    if (aCobertura.type == SCCoberturaTypeBACY){
                        if (aCobertura.sumaAsegurada > self.valor200Salarios) {
                            aCobertura.sumaAsegurada = self.valor200Salarios;
                        }
                    }
                }
            }
            if (aCobertura.isSelected){
            }else{
                if (aCobertura.type == SCCoberturaTypeGFA ||
                    aCobertura.type == SCCoberturaTypeGFC ||
                    aCobertura.type == SCCoberturaTypeGFH) {
                    aCobertura.sumaAsegurada = [self sumaAseguradaActualForCoberturaType:SCCoberturaTypeBAS]*.35;
                    
                    if (aCobertura.sumaAsegurada > 180000) {
                        aCobertura.sumaAsegurada = 180000;
                    }
                }
            }
            if (aCobertura.type == SCCoberturaTypeCAT ||
                aCobertura.type == SCCoberturaTypeCP ||
                aCobertura.type == SCCoberturaTypeCP1 ||
                aCobertura.type == SCCoberturaTypeCP2 ||
                aCobertura.type == SCCoberturaTypeCP3) {
                if (aCobertura.sumaAsegurada > 1000000) {
                    aCobertura.sumaAsegurada = 1000000;
                }
            }

        }
        [self validateGastosFunerariosSumaAsegurada];
        [self GastosFunerariosEnabledValidation];
        [self canceresValidations];
        [self CII_CMA_TIBA_Validation];
        [self refreshAllCoberturas];
        SCCobertura *CC = [self.coberturas objectAtIndex:12];
        if (CC.edadReal < 18) {
            if (CC.sumaAsegurada > 3000000) {
                CC.sumaAsegurada = 3000000;
            }
        }
    }
}

-(void)reloadSumaAseguradaSinGastosFunerarios{
    if (self.esPrima) {
        return;
    }
    if (self.esNuevaPoliza == YES) {
        for (SCCobertura *aCobertura in self.coberturas) {
            if (aCobertura.isSelected &&  aCobertura.sumaAsegurada < [self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada){
            }else{
                if (aCobertura.type != SCCoberturaTypeGFA &&
                    aCobertura.type != SCCoberturaTypeGFC &&
                    aCobertura.type != SCCoberturaTypeGFH) {
                    aCobertura.sumaAsegurada = [self sumaAseguradaActualForCoberturaType:SCCoberturaTypeBAS];
                    if (aCobertura.type == SCCoberturaTypeBACY){
                        if (aCobertura.sumaAsegurada > self.valor200Salarios) {
                            aCobertura.sumaAsegurada = self.valor200Salarios;
                        }
                    }
                }
            }
        
            if (aCobertura.type == SCCoberturaTypeCAT ||
                aCobertura.type == SCCoberturaTypeCP ||
                aCobertura.type == SCCoberturaTypeCP1 ||
                aCobertura.type == SCCoberturaTypeCP2 ||
                aCobertura.type == SCCoberturaTypeCP3) {
                if (aCobertura.sumaAsegurada > 1000000) {
                    aCobertura.sumaAsegurada = 1000000;
                }
            }
            
        }
        [self validateGastosFunerariosSumaAsegurada];
        [self GastosFunerariosEnabledValidation];
        [self canceresValidations];
        [self CII_CMA_TIBA_Validation];
        [self refreshAllCoberturas];
        SCCobertura *CC = [self.coberturas objectAtIndex:12];
        if (CC.edadReal < 18) {
            if (CC.sumaAsegurada > 3000000) {
                CC.sumaAsegurada = 3000000;
            }
        }
    }
}

-(void)validations{
    
    [self GastosFunerariosEnabledValidationPerButtonSelected];
    [self refreshAllCoberturas];
}


-(void)gfaTouched{
    
    if (self.esNuevaPoliza) {
        [self coberturaForType:SCCoberturaTypeGFA].sumaAsegurada = [self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada *.35;
        if ([self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada *.35 > 180000) {
            [self coberturaForType:SCCoberturaTypeGFA].sumaAsegurada =  180000;
        }
        [self refreshAllCoberturas];
    }

}

-(void)gfcTouched{
    
    if (self.esNuevaPoliza) {
        [self coberturaForType:SCCoberturaTypeGFC].sumaAsegurada = [self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada *.35;
        if ([self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada *.35 > 180000) {
            [self coberturaForType:SCCoberturaTypeGFC].sumaAsegurada =  180000;
        }
        [self refreshAllCoberturas];
    }

}


-(void)gfhTouched{
    
    if (self.esNuevaPoliza) {
        //[self coberturaForType:SCCoberturaTypeGFH].sumaAsegurada = [self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada *.35;
        if ([self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada *.35 > 180000) {
               [self coberturaForType:SCCoberturaTypeGFH].sumaAsegurada =  180000;
        }
        [self refreshAllCoberturas];
    }

}

-(void)GastosFunerariosEnabledValidationPerButtonSelected{
    
    if (self.esPrima) {
    } else {
        if (self.esNuevaPoliza == YES) {
            
            if ([self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada *0.35 < self.valor20Salarios) {
                
                [self coberturaForType:SCCoberturaTypeGFA].enabled = NO;
                [self coberturaForType:SCCoberturaTypeGFA].selected = NO;
                [self coberturaForType:SCCoberturaTypeGFC].enabled = NO;
                [self coberturaForType:SCCoberturaTypeGFC].selected = NO;
                [self coberturaForType:SCCoberturaTypeGFH].enabled = NO;
                [self coberturaForType:SCCoberturaTypeGFH].selected = NO;
                
                if ([self coberturaForType:SCCoberturaTypeGFA].isSelected) {
                    [self coberturaForType:SCCoberturaTypeGFA].sumaAsegurada =  self.valor20Salarios;
                }
                if ([self coberturaForType:SCCoberturaTypeGFC].isSelected) {
                    [self coberturaForType:SCCoberturaTypeGFC].sumaAsegurada =  self.valor20Salarios;
                }
                //if ([self coberturaForType:SCCoberturaTypeGFH].isSelected) {
                    [self coberturaForType:SCCoberturaTypeGFH].sumaAsegurada =  self.valor20Salarios;
                
                //}
                
            } else {
                if (millar == -100.000000) {
                    
                } else {
                    [self coberturaForType:SCCoberturaTypeGFA].enabled = YES;
                    [self coberturaForType:SCCoberturaTypeGFC].enabled = YES;
                    [self coberturaForType:SCCoberturaTypeGFH].enabled = YES;
                }
            }
        }
    }
}


-(void)canceresValidations{
    
    if (self.esPrima) {
    } else {
        if (self.esNuevaPoliza == YES) {
            if ([self coberturaForType:SCCoberturaTypeCAT].sumaAsegurada > 1000000) {
                [self coberturaForType:SCCoberturaTypeCAT].sumaAsegurada =   1000000;
                [self coberturaForType:SCCoberturaTypeCP].sumaAsegurada =   1000000;
                [self coberturaForType:SCCoberturaTypeCP1].sumaAsegurada =  1000000;
                [self coberturaForType:SCCoberturaTypeCP2].sumaAsegurada =  1000000;
                [self coberturaForType:SCCoberturaTypeCP3].sumaAsegurada =  1000000;
                
            }
        }
    }
}


-(void)CII_Validation{
  
    if (self.esPrima) {
    } else {
        if (self.esNuevaPoliza == YES) {
            if ([self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada > 6000000) {
                if ([self coberturaForType:SCCoberturaTypeCII].isSelected) {
                    [self coberturaForType:SCCoberturaTypeCII].sumaAsegurada =   6000000;
                    [self calcularCobertura:[self coberturaForType:SCCoberturaTypeCII]];
                }
            }
        }
    }
    [self refreshAllCoberturas];
}

-(void)CMA_Validation{

    if (self.esPrima) {
    } else {
        if (self.esNuevaPoliza == YES) {
            
            if ([self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada > 6000000) {
                
                if ([self coberturaForType:SCCoberturaTypeCMA].isSelected){
                    [self coberturaForType:SCCoberturaTypeCMA].sumaAsegurada =   6000000;
                    [self calcularCobertura:[self coberturaForType:SCCoberturaTypeCMA]];
                }
            }
        }
    }
    
     [self refreshAllCoberturas];
}

-(void)TIBA_Validation{
    
    if (self.esPrima) {
    } else {
        if (self.esNuevaPoliza == YES) {
            if ([self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada > 6000000) {
                if ([self coberturaForType:SCCoberturaTypeTIBA].isSelected){
                    [self coberturaForType:SCCoberturaTypeTIBA].sumaAsegurada =  6000000;
                    [self calcularCobertura:[self coberturaForType:SCCoberturaTypeTIBA]];
                }
            }
            
        }
        
    }
    
     [self refreshAllCoberturas];
}

-(void)CII_CMA_TIBA_Validation{
    
    if (self.esPrima) {
    } else {
        if (self.esNuevaPoliza == YES) {
            
            if ([self coberturaForType:SCCoberturaTypeCII].sumaAsegurada > 6000000) {
                [self coberturaForType:SCCoberturaTypeCII].sumaAsegurada =   6000000;
                [self coberturaForType:SCCoberturaTypeCMA].sumaAsegurada =   6000000;
                [self coberturaForType:SCCoberturaTypeTIBA].sumaAsegurada =  6000000;
            }
        }
    }
}

-(void)CAT_Validation{
    
    if (self.esPrima) {
    } else {
        if (self.esNuevaPoliza == YES) {
            
            if ([self coberturaForType:SCCoberturaTypeCAT].sumaAsegurada >= 1000000) {
                
                if ([self coberturaForType:SCCoberturaTypeCAT].isSelected){
                    
                    [self coberturaForType:SCCoberturaTypeCAT].sumaAsegurada =  1000000;
                    [self calcularCobertura:[self coberturaForType:SCCoberturaTypeCAT]];
                }
            }
        }
    }
    
}


-(void)CP_Validation{
    if (self.esPrima) {
    } else {
        if (self.esNuevaPoliza == YES) {
            
            if ([self coberturaForType:SCCoberturaTypeCP].sumaAsegurada >= 1000000) {
                
                if ([self coberturaForType:SCCoberturaTypeCP].isSelected){
                    
                    [self coberturaForType:SCCoberturaTypeCP].sumaAsegurada =  1000000;
                    
                }
            }
        }
    }
    
}


-(void)CP1_Validation{
    
    if (self.esPrima) {
    } else {
        if (self.esNuevaPoliza == YES) {
            
            if ([self coberturaForType:SCCoberturaTypeCP1].sumaAsegurada >= 1000000) {
                
                if ([self coberturaForType:SCCoberturaTypeCP1].isSelected){
                    
                    [self coberturaForType:SCCoberturaTypeCP1].sumaAsegurada =  1000000;
                    
                }
            }
        }
    }
}


-(void)CP2_Validation{
    
    if (self.esPrima) {
    } else {
        if (self.esNuevaPoliza == YES) {
            
            if ([self coberturaForType:SCCoberturaTypeCP2].sumaAsegurada >= 1000000) {
                
                if ([self coberturaForType:SCCoberturaTypeCP2].isSelected){
                    
                    [self coberturaForType:SCCoberturaTypeCP2].sumaAsegurada =  1000000;
                    
                }
            }
        }
    }
}


-(void)CP3_Validation{
    
    if (self.esPrima) {
    } else {
        if (self.esNuevaPoliza == YES) {
            
            if ([self coberturaForType:SCCoberturaTypeCP3].sumaAsegurada >= 1000000) {
                
                if ([self coberturaForType:SCCoberturaTypeCP3].isSelected){
                    
                    [self coberturaForType:SCCoberturaTypeCP3].sumaAsegurada =  1000000;
                    
                }
            }
        }
    }
}

-(void)sumaAseguradaGastosFunerarios{
    
    if (self.esNuevaPoliza) {
        
        if ([self coberturaForType:SCCoberturaTypeGFA].sumaAsegurada > [self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada *.35) {
            
            [self coberturaForType:SCCoberturaTypeGFA].sumaAsegurada = ([self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada *.35);
        }
        if ([self coberturaForType:SCCoberturaTypeGFC].sumaAsegurada > [self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada *.35) {
            
            [self coberturaForType:SCCoberturaTypeGFC].sumaAsegurada = ([self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada *.35);
        }
        if ([self coberturaForType:SCCoberturaTypeGFH].sumaAsegurada > [self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada *.35) {
            
            [self coberturaForType:SCCoberturaTypeGFH].sumaAsegurada = ([self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada *.35);
        }
    }
    
}

#pragma mark - gastos funerarios Validations

-(void)validateGastosFunerariosSumaAsegurada{
    
//    if (self.esNuevaPoliza) {
//        
//        [self coberturaForType:SCCoberturaTypeGFA].sumaAsegurada = ([self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada *.35);
//        [self coberturaForType:SCCoberturaTypeGFC].sumaAsegurada = ([self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada *.35);
//        [self coberturaForType:SCCoberturaTypeGFH].sumaAsegurada = ([self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada *.35);
//        
//    }else{
//        
//        [self coberturaForType:SCCoberturaTypeGFA].sumaAsegurada = [self coberturaForType:SCCoberturaTypeGFA].sumaAsegurada;
//        [self coberturaForType:SCCoberturaTypeGFC].sumaAsegurada = [self coberturaForType:SCCoberturaTypeGFC].sumaAsegurada;
//        [self coberturaForType:SCCoberturaTypeGFH].sumaAsegurada = [self coberturaForType:SCCoberturaTypeGFH].sumaAsegurada;
//    }
}

-(void)GastosFunerariosEnabledValidation {
    
    if (self.esPrima) {
    } else {
        if (self.esNuevaPoliza == YES) {
            if ([self coberturaForType:SCCoberturaTypeBAS].sumaAsegurada *0.35 < self.valor20Salarios) {
                [self coberturaForType:SCCoberturaTypeGFA].enabled = NO;
                [self coberturaForType:SCCoberturaTypeGFC].enabled = NO;
                [self coberturaForType:SCCoberturaTypeGFH].enabled = NO;
                
                [self coberturaForType:SCCoberturaTypeGFA].selected = NO;
                [self coberturaForType:SCCoberturaTypeGFC].selected = NO;
                [self coberturaForType:SCCoberturaTypeGFH].selected = NO;
                
                [self coberturaForType:SCCoberturaTypeGFA].sumaAsegurada =  self.valor20Salarios;
                [self coberturaForType:SCCoberturaTypeGFC].sumaAsegurada =  self.valor20Salarios;
                [self coberturaForType:SCCoberturaTypeGFH].sumaAsegurada =  self.valor20Salarios;
            } else {
                if (millar == -100.000000) {
                } else {
                    [self coberturaForType:SCCoberturaTypeGFA].enabled = YES;
                    [self coberturaForType:SCCoberturaTypeGFC].enabled = YES;
                    [self coberturaForType:SCCoberturaTypeGFH].enabled = YES;
                }
            }

        }
    }
}

- (void)refreshAllCoberturas {
    
    if (!self.esPrima) {
        for (SCCobertura *aCobertura in self.coberturas) {
            [self.delegate cotizador:self didUpdateCobertura:aCobertura];
        }
    }
}

-(void)extraPrimasValue {
    
    double millarNormal = (millar / 100);
    NSString *millarString = [NSString stringWithFormat:@"%.2f",millarNormal];
    NSString *invalidezString = [NSString stringWithFormat:@"%.2f",invalidez];
    NSString *accidenteString = [NSString stringWithFormat:@"%.2f",accidente];
  
    if ([accidenteString isEqualToString:@"0.0"]) {
        [self.delegate cotizador:self didGetExtraPrimaText:@""];
        return;
    }
    if (millar == 0 && invalidez == 0 && accidente == 0) {
        millar = 0;
        invalidez =1;
        accidente =1;
    }
    
    
    NSString *extraPrimas = [NSString stringWithFormat:@"vida: %@ al millar, Accidentes: %@ tantos, Invalidez: %@ tantos",millarString,accidenteString,invalidezString];
    if (invalidez == -1.000000) {
        extraPrimas = [NSString stringWithFormat:@"vida: %@ al millar, Accidentes: %@ tantos, Invalidez: Rechazo",millarString,accidenteString];
    }
    if (accidente == -1.000000) {
        extraPrimas = [NSString stringWithFormat:@"vida: %@ al millar, Accidentes: %@, Invalidez: %@ tantos",millarString,@"Rechazo",invalidezString];
    }
    if (accidente == -1.000000 && invalidez == -1.000000) {
        extraPrimas = [NSString stringWithFormat:@"vida: %@ al millar, Accidentes: %@, Invalidez: %@",millarString,@"Rechazo",@"Rechazo"];
    }
    if (millar == -100.000000) {
        extraPrimas = @"Rechazo";
        for (SCCobertura *aCobertura in self.coberturas) {
            aCobertura.enabled = NO;
        }
    }
    if (millar == 0 && invalidez == 1 && accidente == 1) {
        extraPrimas = @"";
        
    }
    [self.delegate cotizador:self didGetExtraPrimaText:extraPrimas];
}

- (BOOL)esValidaCobertura:(SCCobertura *)cobertura {
    double max = cobertura.esMenorEdad ? cobertura.maxMenores : cobertura.maxMayores;
    if (cobertura.considerarBAS) {
        max = cobertura.valorLimiteConsiderandoBAS < max ? cobertura.valorLimiteConsiderandoBAS : max;
    }
    return (cobertura.sumaAsegurada < cobertura.min || cobertura.sumaAsegurada > max) ? NO : YES;
}

- (void)evaluateChangeInSumaAsegurada:(double)sumaAsegurada index:(NSInteger)index editandoTextField:(BOOL)editando {
    
    SCCobertura *cobertura = [self.coberturas objectAtIndex:index];
    if (cobertura.type != SCCoberturaTypeBIT) {
        cobertura.sumaAsegurada = sumaAsegurada;
        BOOL esValida = cobertura.esValida;
        cobertura.esValida = [self esValidaCobertura:cobertura];
        if (editando) {
            if (cobertura.esValida != esValida) {
                [self.delegate cotizador:self validarColorCobertura:cobertura];
            }
        } else {
            
            if (!cobertura.esValida) {
                cobertura.sumaAsegurada = cobertura.sumaAsegurada < cobertura.min ? cobertura.min : cobertura.esMenorEdad ? cobertura.maxMenores : cobertura.maxMayores;
                [self.delegate cotizador:self nuevaSumaAseguradaEnCobertura:cobertura];
            }
        }
        
    } else {
        if (cobertura.isSelected) {
            cobertura.stringSumaAsegurada = @"Cubierto";
        } else {
            cobertura.stringSumaAsegurada = @"Excluído";
        }
        [self.delegate cotizador:self didUpdateCobertura:cobertura];
    }
}



@end
