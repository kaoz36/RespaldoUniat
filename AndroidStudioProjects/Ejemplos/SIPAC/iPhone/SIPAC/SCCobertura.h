#import <Foundation/Foundation.h>

@class SCCotizador;
/**
 An enumeration of coberturas types
 */
typedef enum {
    SCCoberturaTypeBAS,/**< A BAS cobertura */
    SCCoberturaTypeBIT,/**< A BIT cobertura */
    SCCoberturaTypeCII,/**< A CII cobertura */
    SCCoberturaTypeCMA,/**< A CMA cobertura */
    SCCoberturaTypeTIBA,/**< A TIBA cobertura */
    SCCoberturaTypeCAT,/**< A CAT cobertura */
    SCCoberturaTypeGFA,/**< A GFA cobertura */
    SCCoberturaTypeGE,/**< A GE cobertura */
    SCCoberturaTypeBACY,/**< A BACY cobertura */
    SCCoberturaTypeGFC,/**< A GFC cobertura */
    SCCoberturaTypeCP,/**< A CP cobertura */
    SCCoberturaTypeGFH,/**< A GFH cobertura */
    SCCoberturaTypeCC,/**< A CC cobertura */
    SCCoberturaTypeCP1,/**< A CP1 cobertura */
    SCCoberturaTypeCP2,/**< A CP2 cobertura */
    SCCoberturaTypeCP3/**< A CP3 cobertura */
    
}SCCoberturaType;
/**
 An enumeration of sex type
 */
typedef enum {
    SCSexHombre,/**< A man */
    SCSexMujer/**< A woman */
}
SCSex;
/**
 An enumeration of coberturas clasification 
 */
typedef enum {
    SCClasificacionCoberturaTitular = 1,/**< A clasification of cobertura titular */
    SCClasificacionCoberturaConyugue,/**< A clasification of cobertura conyugue */
    SCClasificacionCoberturaHijos,/**< A clasification of cobertura hijos */
    SCClasificacionCoberturaComplementaria,/**< A clasification of cobertura complementaria */
    SCClasificacionCancerPlus1,/**< A clasification of cancer plus1 */
    SCClasificacionCancerPlus2,/**< A clasification of cancer plus2 */
    SCClasificacionCancerPlus3/**< A clasification of cancer plus3 */
} SCClasificacionCobertura;
/**
This class calculate the  coberturas.
 */
@interface SCCobertura : NSObject{
    /**
     The diferent types of coberturas
     */
    double BAS, BASN, CAT, GE,GEN,BACY, GFC, CII, CIIN;
    
}
/**
 calculate extra primas
 @param millar the factor millar
 @param invalidez the factor invalidez
 @param accidente the factor accidente
 @returns void.
 */
-(void)calcular:(double)pMillar invalidez:(double)pInvalidez accidente:(double)pAccidente;
/**
 calculate the factor of beneficios
 @param millar the factor millar
 @param invalidez the factor invalidez
 @param accidente the factor accidente
 @param CalculandoFactorBit the factor bit
 @returns the factor.
 */
-(double)calcularFactoresBeneficios:(double)pMillar invalidez:(double)pInvalidez accidente:(double)pAccidente pCalculandoFactorBit:(BOOL)pCalculandoFactorBit;
/**
 Initialize with cobertura object
 @param newType The type of cobertura
 @returns a newly initialized object
 */
-(id)initWithType:(SCCoberturaType)newType;
/**
 test to see if is overFlowes
 @returns return YES if overFlowed, otherwise returns NO.
 */
- (BOOL)isOverflowed;
- (BOOL)isUnderLimit;

@property (nonatomic, assign) SCClasificacionCobertura clasificacion;

@property double min;
@property double maxMenores;
@property double maxMayores;
@property int edad;
@property int edadReal;
@property SCSex sexo;
@property (getter=isSelected) BOOL selected;
@property (getter=isEnabled) BOOL enabled;

//@property (strong) NSString *sexCP;
//@property (strong) NSString *GFHDescription;
@property (nonatomic, assign) NSInteger indexSeleccionGFH;
@property (nonatomic, assign) NSInteger sexIndex;

@property double sumaAsegurada;
@property (strong) NSString *stringSumaAsegurada;
@property double primaAnual;
@property (strong) NSString *stringPrimaAnual;

@property SCCoberturaType type;

@property (nonatomic, strong) NSString *tipoPago;
@property (nonatomic, strong) NSString *tipoCalculo;
@property (nonatomic, assign) double valorLimiteConsiderandoBAS;
@property (nonatomic, assign) BOOL considerarBAS;
@property (nonatomic, assign) BOOL esNuevaPoliza;
@property (nonatomic, assign) BOOL esConDevolucion;
@property (nonatomic, assign) BOOL esMenorEdad;

@property (nonatomic, strong) SCCotizador *cotizador;

@property (nonatomic, assign) BOOL esValida;

@property (nonatomic, assign) double limiteSumaAseguradaParaCotizadorPorPrima;
@property (nonatomic, assign) double sumaAseguradaDeCalculo;
@property (nonatomic, assign) BOOL sumaCalculoCorregida;

@end