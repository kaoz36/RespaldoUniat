//
//  SCCotizador.h
//  SIPAC
//
//  Created by MacBook Pro de Hugo on 05/08/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SCCobertura.h"

@class SCCotizadorVC;
/**
 Set of methods to be implemented to act as plannig budget
 */
@protocol SCCoberturaDelegate;
/**
 An enumeration of payments type
 */
typedef enum {
    
    FormaDePagoTypeAnual,
    FormaDePagoTypeMensual,
    FormaDePagoTypeQuincenal,
    
}FormaDePagoType;
/**
 Class representing the calculates of cotizador.
 */
@interface SCCotizador : NSObject{
    
    double millar, invalidez, accidente;
    double coberturaGFAValidations, coberturaGFCValidations, coberturaGFHValidations;
}
/**
 Initialize with array of coberturas 
 @param prima if the budget has prima
 @returns a newly initialized object
 */
-(id)initWithCoberturas:(NSArray*)nuevasCoberturas prima:(BOOL)isPrima;


@property (nonatomic, assign) float valor20Salarios;
@property (nonatomic, assign) float valor200Salarios;

@property (nonatomic, assign) double primaTotalCotizadorPrima;
@property (nonatomic, assign) double primaExcedenteAnualPorPrima;

@property double resultadoBIT;
@property double resultadoSumasAseguradas;
@property double sumaAseguradaPorPrima;
@property FormaDePagoType formaPago;

@property (strong) NSArray *coberturas;
@property (weak) id <SCCoberturaDelegate> delegate;
@property (strong) NSString *calculo;
@property (strong) NSString *pago;

@property BOOL esConDevolucion;
@property BOOL esNuevaPoliza;
@property BOOL esPrima;

@property int estimateAge;
@property int realAge;
@property (copy) NSMutableArray *professions;


- (void)calcularCoberturas;
- (double)sumaAseguradaActualForCoberturaType:(SCCoberturaType)theType;
- (void)selectOcupacion:(int)newOcupation;
- (void)refreshAllCoberturas;
- (void)CII_Validation;
- (void)CMA_Validation;
- (void)TIBA_Validation;
- (void)CAT_Validation;
- (void)CP_Validation;
- (void)CP1_Validation;
- (void)CP2_Validation;
- (void)CP3_Validation;
- (void)reloadSumaAseguradaSinGastosFunerarios;

- (void)gfaTouched;
- (void)gfcTouched;
- (void)gfhTouched;


/**
 evaluate the range of sumasAseguradas each cobertura
 @returns void
 */
- (void)evaluateChangeInSumaAsegurada:(double)sumaAsegurada index:(NSInteger)index editandoTextField:(BOOL)editando;

- (void)reloadSumaAsegurada;
- (double)calcularBIT;
- (void)GastosFunerariosEnabledValidation;
- (void)validateGastosFunerariosSumaAsegurada;
- (void)validationSumaAseguradaOpcionInclusiones;
- (void)validationSumaAseguradaOpcionNuevaPoliza;
- (void)validationSumaAseguradaOpcionInclusionesSeleccionBeneficio;
- (void)sumaAseguradaBacy;
- (void)sumaAseguradaBacy:(double)sumaAseguradaBAS;
- (void)CII_CMA_TIBA_Validation;
- (void)reloadSuma;
- (void)bacySelected;
- (void)calcularCobertura:(SCCobertura *)cobertura;
- (void)sumaAseguradaGastosFunerarios;
- (void)validations;
/**
 load the professions 
 @returns returns an array of professions
 */
- (NSArray *)loadProfesions;

@end


@protocol SCCoberturaDelegate <NSObject>

- (void)cotizador:(SCCotizador *)aCotizador didUpdateCobertura:(SCCobertura *)aCobertura;
- (void)cotizador:(SCCotizador *)aCotizador didGetSumaAseguradaTotal:(double)nuevaSumaAseguradaTotal;
- (void)cotizador:(SCCotizador *)aCotizador validarColorCobertura:(SCCobertura *)cobertura;
- (void)cotizador:(SCCotizador *)aCotizador nuevaSumaAseguradaEnCobertura:(SCCobertura *)cobertura;

- (void)cotizador:(SCCotizador *)aCotizador didGetExtraPrimaText:(NSString *)nuevaExtraPrima;
- (int)ocupacionActualParaCotizador:(SCCotizador *)cotizador;
- (void)cotizadorPorPrimaSinSolucion:(SCCotizador *)cotizador;
- (void)cotizadorPorPrimaInsuficiente:(SCCotizador *)cotizador;
- (void)cotizadorPorPrimaExcede:(SCCotizador *)cotizador;

@end