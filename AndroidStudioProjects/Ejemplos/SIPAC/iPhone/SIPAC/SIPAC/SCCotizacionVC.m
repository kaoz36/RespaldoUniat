//
//  SCCotizacionVC.m
//  SIPAC
//
//  Created by MacBook Pro de Hugo on 21/08/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import "SCCotizacionVC.h"

@interface SCCotizacionVC ()

@property (strong) NSDictionary *cotizacion;
@property (strong) IBOutlet UIWebView *webView;
@property (strong) IBOutlet UITextField *txtInteresGarantizado;
@property (strong) IBOutlet UITextField *txtCostoAdministrativo;

-(IBAction)exitToWebView:(id)sender;

@end

@implementation SCCotizacionVC

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil cotizacion:(NSDictionary *)verCotizacion suma:(double)suma primaExcedente:(double)primaExcedente tienePrima:(BOOL)tienePrimaExcedente
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.cotizacion = verCotizacion;
        self.primaExcedente = primaExcedente;
        self.sumaMayor = suma;
        self.tienePrimaExcedente = tienePrimaExcedente;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    self.interesGarantizado = 0.06;
    self.costoAdministracion = 0.005;
    
    self.txtInteresGarantizado.text = @"0.06";
    self.txtCostoAdministrativo.text = @"0.005";
    if([UIDevice currentDevice].userInterfaceIdiom == UIUserInterfaceIdiomPhone) {
         CGRect screenBounds = [[UIScreen mainScreen] bounds];
        if (screenBounds.size.height == 568) {
            self.webView.frame = CGRectMake(0, 43, 320, 500);
        }
    }
    
    [self configurarHTMLView];
}

- (void)configurarHTMLView
{
    NSArray *proyeccion = nil;
    if (self.tienePrimaExcedente) {
        proyeccion = [self calcularProyeccionConInteresGarantizado:self.interesGarantizado costoDeAdministracion:self.costoAdministracion primaExedente:self.primaExcedente periodos:20 sumaMayor:self.sumaMayor];
    }
    [self htmlStringWithStructure: [self stringTitularWithDictionary:self.cotizacion] stringHijos: [self stringHijosWithDictionary:self.cotizacion] stringConyugue:[self stringConyugueWithDictionary:self.cotizacion] stringCC:[self stringComplementariosWithDictionary:self.cotizacion] proyeccion: [self stringCotizacion2OaniosWithDictionary:self.cotizacion array:proyeccion]];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)textFieldDidEndEditing:(UITextField *)textField {
    double value = [textField.text doubleValue];
    if ([textField isEqual:self.txtInteresGarantizado]) {
        self.interesGarantizado = value;
    }else{
        self.costoAdministracion = value;
    }
    [self configurarHTMLView];
}


#pragma mark - Methods for cotizacion.html

- (NSArray *) calcularProyeccionConInteresGarantizado:(double)interesGarantizado  costoDeAdministracion:(double)costoAdministracion  primaExedente:(double)excedente periodos:(int)periodos sumaMayor:(double)sumaMayor {
    double arrayMontoAnual[periodos + 1];
    double interesAcumulado;
    NSMutableArray *arrayProyeccionesAnuales = [[NSMutableArray alloc] init];
    for( int contador = 1; contador <= periodos; contador++) {
        interesAcumulado = 0;
        if( contador != 0) {
            interesAcumulado = arrayMontoAnual[contador - 1] * pow( (interesGarantizado / 12 ) + 1, 12);
            interesAcumulado += (excedente * pow( 1 - costoAdministracion, 2) / 12) * [self sumatoria:interesGarantizado];
        }
        arrayMontoAnual[contador] = interesAcumulado;
        double sumaPorFallecimiento = sumaMayor + interesAcumulado;
        NSDictionary *dictionarioProyeccion = [NSDictionary dictionaryWithObjectsAndKeys:[NSString stringWithFormat:@"%i", contador], @"anio", [self stringFromDouble:interesAcumulado], @"fondoInversion", [self stringFromDouble:sumaPorFallecimiento], @"sumaFallecimiento", nil];
        [arrayProyeccionesAnuales addObject:dictionarioProyeccion];
    }
    return arrayProyeccionesAnuales;
}

- (NSString *)stringFromDouble:(double)dato
{
    return [NSString stringWithFormat:@"%.2f", dato];
}

- (double)sumatoria:(double)interes {
    double acumulador = 0;
    for(int contador = 0; contador < 12; contador++){
        acumulador += pow ((interes / 12) + 1, 12 - contador);
    }
    return acumulador;
}



-(void)htmlStringWithStructure:(NSString *)stringTitular stringHijos:(NSString *)stringHijos stringConyugue:(NSString *)stringConyugue stringCC:(NSString *)stringCC proyeccion:(NSString *)proyeccion{
    
    NSString *edadCalculo = [[self.cotizacion objectForKey:@"datosTitular"]objectForKey:@"edadCalculo"];
    NSString *edadReal = [[self.cotizacion objectForKey:@"datosTitular"]objectForKey:@"edadReal"];
    NSString *habito = [[self.cotizacion objectForKey:@"datosTitular"]objectForKey:@"habito"];
    NSString *ocupacion = [[self.cotizacion objectForKey:@"datosTitular"]objectForKey:@"ocupacion"];
    NSString *nombre = [[self.cotizacion objectForKey:@"datosTitular"]objectForKey:@"nombre"];
    NSString *sexo = [[self.cotizacion objectForKey:@"datosTitular"]objectForKey:@"sexo"];
    
    NSString *html = [NSString stringWithFormat:
                      @"<html>\
                      <head><meta charset=\"utf-8\">\
                      <title>CotizaciÛn</title>\
                      <meta name=\"description\" content=\"\">\
                      <meta name=\"viewport\" content=\"width=device-width\">\
                      <style type=\"text/css\">\
                      body {\
                      background-color: #FFF;margin: 0;\
                      font-family: sans-serif;font-size: 16px;\
                      font-size: 100%%;\
                      }\
                      h4 {\
                      font-size: 1em;\
                      font-weight: bold;}\
                      div {width: 100%%;}\
                      div.blu {\
                      background-color: #2d73b5;padding: 5px 2%%;\
                      width: 96%%;\
                      color: #FFF;\
                      margin: 0 auto 1px;\
                      }\
                      .blu h4, .blu p {\
                      display: inline;\
                      padding: 0 4px 0 0;\
                      }\
                      table {\
                      border-collapse: collapse;\
                      border-spacing: 0;\
                      width: 100%%;\
                      margin: 0 auto 40px;\
                      }\
                      table th {border-right:1px solid #FFF;\
                      font-size: 0.7em;\
                      text-transform: uppercase;\
                      color: #FFF;\
                      background-color: #2d73b5;\
                      padding: 5px 2px 2px;\
                      }\
                      table td {\
                      padding: 4px 2px 2px;\
                      border-bottom: 1px solid #CCC;\
                      }\
                      td {text-align: center;}\
                      td.left {text-align: left;}\
                      </style>\
                      </head>\
                      <body>\
                      <div class=\"blu\">\
                      <h4>Titular:</h4>\
                      <p>%@</p>\
                      </div>\
                      <div class=\"blu\">\
                      <h4>Habito:</h4>\
                      <p>%@</p>\
                      <h4>Sexo:</h4>\
                      <p>%@</p>\
                      </div>\
                      <div class=\"blu\">\
                      <h4>Edad real:</h4>\
                      <p>%@</p>\
                      <h4>Edad Cálculo:</h4>\
                      <p>%@</p>\
                      </div>\
                      <div class=\"blu\">\
                      <h4>Ocupación:</h4>\
                      <p>%@</p>\
                      </div>\
                      <table>\
                      <thead>\
                      <tr>\
                      <th>Coberturas</th>\
                      <th>Suma Asegurada</th>\
                      <th>Prima A. Total</th>\
                      </tr>\
                      </thead>\
                      </body>\
                      <tbody>\
                      %@\
                      </tbody>\
                      </table>\
                      %@\
                      %@\
                      %@\
                      %@\
                      </body>\
                      </html>", nombre, habito, sexo, edadReal, edadCalculo, ocupacion,stringTitular,stringHijos,stringConyugue,stringCC,proyeccion];
    
    NSString *myHTML =html;
    [self.webView loadHTMLString:myHTML baseURL:nil];
    
}

-(NSString *)stringTitularWithDictionary:(NSDictionary *)dictionary{
    
    if (![dictionary objectForKey:@"datosTitular"]) {
        return @"";
    }else{
        
        NSArray *arrayCoberturas = [[dictionary objectForKey:@"datosTitular"]objectForKey:@"arrayCoberturas"];
        
        NSString *coberturasTitularString = @"";
        
        for (NSDictionary *coberturas in arrayCoberturas) {
            
            NSString *nombreCobertura = [coberturas objectForKey:@"nombreCobertura"];
            NSString *sumaAsegurada = [coberturas objectForKey:@"sumaAsegurada"];
            NSString *primaAnual = [coberturas objectForKey:@"primaAnual"];
            
            NSString *nuevaCoberturaTitularString = [NSString stringWithFormat:@"<tr><td>%@</td><td>%@</td><td>%@</td></tr>",nombreCobertura,sumaAsegurada,primaAnual];
            
            coberturasTitularString = [coberturasTitularString stringByAppendingString:nuevaCoberturaTitularString];
            
        }
        return coberturasTitularString;
    }
}

-(NSString *)stringHijosWithDictionary:(NSDictionary *)dictionary{
    
    if (![dictionary objectForKey:@"datosHijos"]) {
        return @"";
    }else{
        NSString *hijos = [[dictionary objectForKey:@"datosHijos"]objectForKey:@"hijos"];
        NSArray *arrayCoberturas = [[dictionary objectForKey:@"datosHijos"]objectForKey:@"arrayCoberturas"];
        NSString *nombreCobertura = [[arrayCoberturas objectAtIndex:0]objectForKey:@"nombreCobertura"];
        NSString *primaAnual = [[arrayCoberturas objectAtIndex:0]objectForKey:@"primaAnual"];
        NSString *sumaAsegurada = [[arrayCoberturas objectAtIndex:0]objectForKey:@"sumaAsegurada"];
        
        NSString *hijosString = [NSString stringWithFormat:
                                 @"<div class=\"blu\">\
                                 <h4>Hijos a Asegurar:</h4>\
                                 <p>%@</p>\
                                 </div>\
                                 <table>\
                                 <thead>\
                                 <tr>\
                                 <th>Cobertura</th>\
                                 <th>Suma Asegurada</th>\
                                 <th>Prima A. Total</th>\
                                 </tr>\
                                 </thead>\
                                 <tbody>\
                                 <tr>\
                                 <td>%@</td>\
                                 <td>%@</td>\
                                 <td>%@</td>\
                                 </tr>\
                                 </tbody>\
                                 </table>",hijos,nombreCobertura,sumaAsegurada,primaAnual];
        
        return hijosString;
    }
}

-(NSString *)stringConyugueWithDictionary:(NSDictionary *)dictionary{
    
    NSString *conyugueString=@"";
    
    if (![dictionary objectForKey:@"datosConyugue"]) {
        return @"";
    }else{
        
        NSString *edad = [[dictionary objectForKey:@"datosConyugue"]objectForKey:@"edad"];
        NSString *nombre = [[dictionary objectForKey:@"datosConyugue"]objectForKey:@"nombre"];
        NSString *sexo = [[dictionary objectForKey:@"datosConyugue"]objectForKey:@"sexo"];
        NSArray *arrayCoberturas = [[dictionary objectForKey:@"datosConyugue"]objectForKey:@"arrayCoberturas"];
        
        NSString *coberturasConyugalesString = @"";
        
        for (NSDictionary *coberturas in arrayCoberturas) {
            
            NSString *nombreCobertura = [coberturas objectForKey:@"nombreCobertura"];
            NSString *sumaAsegurada = [coberturas objectForKey:@"sumaAsegurada"];
            NSString *primaAnual = [coberturas objectForKey:@"primaAnual"];
            NSString *nuevaCoberturaConyugalString = [NSString stringWithFormat:@"<tr><td>%@</td><td>%@</td><td>%@</td></tr>",nombreCobertura,sumaAsegurada,primaAnual];
            
            coberturasConyugalesString = [coberturasConyugalesString stringByAppendingString:nuevaCoberturaConyugalString];
            
            
            
            conyugueString = [NSString stringWithFormat:
                              @"<div class=\"blu\">\
                              <h4>Cónyuge:</h4>\
                              <p>%@</p>\
                              <br/>\
                              <h4>Sexo:</h4>\
                              <p>%@</p>\
                              <h4>Edad:</h4>\
                              <p>%@</p>\
                              </div>\
                              <table>\
                              <thead>\
                              <tr>\
                              <th>Cobertura</th>\
                              <th>Suma Asegurada</th>\
                              <th>Prima A. Total</th>\
                              </tr>\
                              </thead>\
                              %@\
                              </table>",nombre,sexo,edad,coberturasConyugalesString];
            
        }
        return conyugueString;
    }
    
}


-(NSString *)stringComplementariosWithDictionary:(NSDictionary *)dictionary{
    
    NSString *stringComplementarios = @"";
    
    if (![dictionary objectForKey:@"datosCC"]) {
    }else{
        
        NSString *edad = [[dictionary objectForKey:@"datosCC"]objectForKey:@"edad"];
        NSString *nombre = [[dictionary objectForKey:@"datosCC"]objectForKey:@"nombre"];
        NSString *sexo = [[dictionary objectForKey:@"datosCC"]objectForKey:@"sexo"];
        NSArray *arrayCoberturas = [[dictionary objectForKey:@"datosCC"]objectForKey:@"arrayCoberturas"];
        NSString *nombreCobertura = [[arrayCoberturas objectAtIndex:0]objectForKey:@"nombreCobertura"];
        NSString *primaAnual = [[arrayCoberturas objectAtIndex:0]objectForKey:@"primaAnual"];
        NSString *sumaAsegurada = [[arrayCoberturas objectAtIndex:0]objectForKey:@"sumaAsegurada"];
        
        NSString *ccString = [NSString stringWithFormat:
                              @"<div class=\"blu\">\
                              <h4>Complementario:</h4>\
                              <p>%@</p>\
                              <br/>\
                              <h4>Sexo:</h4>\
                              <p>%@</p>\
                              <h4>Edad:</h4>\
                              <p>%@</p>\
                              </div>\
                              <table>\
                              <thead>\
                              <tr>\
                              <th>Cobertura</th>\
                              <th>Suma Asegurada</th>\
                              <th>Prima A. Total</th>\
                              </tr>\
                              </thead>\
                              <tbody>\
                              <tr>\
                              <td>%@</td>\
                              <td>%@</td>\
                              <td>%@</td>\
                              </tr>\
                              </tbody>\
                              </table>",nombre,sexo,edad,nombreCobertura,sumaAsegurada,primaAnual];
        stringComplementarios = [stringComplementarios stringByAppendingString:ccString];
    }
    if (![dictionary objectForKey:@"datosC1"]) {
    }else{
        NSString *edad = [[dictionary objectForKey:@"datosC1"]objectForKey:@"edad"];
        NSString *nombre = [[dictionary objectForKey:@"datosC1"]objectForKey:@"nombre"];
        NSString *sexo = [[dictionary objectForKey:@"datosC1"]objectForKey:@"sexo"];
        NSArray *arrayCoberturas = [[dictionary objectForKey:@"datosC1"]objectForKey:@"arrayCoberturas"];
        NSString *nombreCobertura = [[arrayCoberturas objectAtIndex:0]objectForKey:@"nombreCobertura"];
        NSString *primaAnual = [[arrayCoberturas objectAtIndex:0]objectForKey:@"primaAnual"];
        NSString *sumaAsegurada = [[arrayCoberturas objectAtIndex:0]objectForKey:@"sumaAsegurada"];
        
        NSString *c1String = [NSString stringWithFormat:
                              @"<div class=\"blu\">\
                              <h4>Cancer Plus:</h4>\
                              <p>%@</p>\
                              <br/>\
                              <h4>Sexo:</h4>\
                              <p>%@</p>\
                              <h4>Edad:</h4>\
                              <p>%@</p>\
                              </div>\
                              <table>\
                              <thead>\
                              <tr>\
                              <th>Cobertura</th>\
                              <th>Suma Asegurada</th>\
                              <th>Prima A. Total</th>\
                              </tr>\
                              </thead>\
                              <tbody>\
                              <tr>\
                              <td>%@</td>\
                              <td>%@</td>\
                              <td>%@</td>\
                              </tr>\
                              </tbody>\
                              </table>",nombre,sexo,edad,nombreCobertura,sumaAsegurada,primaAnual];
        
        stringComplementarios = [stringComplementarios stringByAppendingString:c1String];
    }
    if (![dictionary objectForKey:@"datosC2"]) {
    }else{
        NSString *edad = [[dictionary objectForKey:@"datosC2"]objectForKey:@"edad"];
        NSString *nombre = [[dictionary objectForKey:@"datosC2"]objectForKey:@"nombre"];
        NSString *sexo = [[dictionary objectForKey:@"datosC2"]objectForKey:@"sexo"];
        NSArray *arrayCoberturas = [[dictionary objectForKey:@"datosC2"]objectForKey:@"arrayCoberturas"];
        NSString *nombreCobertura = [[arrayCoberturas objectAtIndex:0]objectForKey:@"nombreCobertura"];
        NSString *primaAnual = [[arrayCoberturas objectAtIndex:0]objectForKey:@"primaAnual"];
        NSString *sumaAsegurada = [[arrayCoberturas objectAtIndex:0]objectForKey:@"sumaAsegurada"];
        
        NSString *c2String = [NSString stringWithFormat:
                              @"<div class=\"blu\">\
                              <h4>Cancer Plus:</h4>\
                              <p>%@</p>\
                              <br/>\
                              <h4>Sexo:</h4>\
                              <p>%@</p>\
                              <h4>Edad:</h4>\
                              <p>%@</p>\
                              </div>\
                              <table>\
                              <thead>\
                              <tr>\
                              <th>Cobertura</th>\
                              <th>Suma Asegurada</th>\
                              <th>Prima A. Total</th>\
                              </tr>\
                              </thead>\
                              <tbody>\
                              <tr>\
                              <td>%@</td>\
                              <td>%@</td>\
                              <td>%@</td>\
                              </tr>\
                              </tbody>\
                              </table>",nombre,sexo,edad,nombreCobertura,sumaAsegurada,primaAnual];
        stringComplementarios = [stringComplementarios stringByAppendingString:c2String];
    }
    if (![dictionary objectForKey:@"datosC3"]) {
    }else{
        NSString *edad = [[dictionary objectForKey:@"datosC3"]objectForKey:@"edad"];
        NSString *nombre = [[dictionary objectForKey:@"datosC3"]objectForKey:@"nombre"];
        NSString *sexo = [[dictionary objectForKey:@"datosC3"]objectForKey:@"sexo"];
        NSArray *arrayCoberturas = [[dictionary objectForKey:@"datosC3"]objectForKey:@"arrayCoberturas"];
        NSString *nombreCobertura = [[arrayCoberturas objectAtIndex:0]objectForKey:@"nombreCobertura"];
        NSString *primaAnual = [[arrayCoberturas objectAtIndex:0]objectForKey:@"primaAnual"];
        NSString *sumaAsegurada = [[arrayCoberturas objectAtIndex:0]objectForKey:@"sumaAsegurada"];
        
        NSString *c3String = [NSString stringWithFormat:
                              @"<div class=\"blu\">\
                              <h4>Cancer Plus:</h4>\
                              <p>%@</p>\
                              <br/>\
                              <h4>Sexo:</h4>\
                              <p>%@</p>\
                              <h4>Edad:</h4>\
                              <p>%@</p>\
                              </div>\
                              <table>\
                              <thead>\
                              <tr>\
                              <th>Cobertura</th>\
                              <th>Suma Asegurada</th>\
                              <th>Prima A. Total</th>\
                              </tr>\
                              </thead>\
                              <tbody>\
                              <tr>\
                              <td>%@</td>\
                              <td>%@</td>\
                              <td>%@</td>\
                              </tr>\
                              </tbody>\
                              </table>",nombre,sexo,edad,nombreCobertura,sumaAsegurada,primaAnual];
        
        stringComplementarios = [stringComplementarios stringByAppendingString:c3String];
    }
    return stringComplementarios;
}


-(NSString *)stringCotizacion2OaniosWithDictionary:(NSDictionary *)dictionary array:(NSArray *)array {
    NSString *prospeccionString = @"";
    NSString *coberturasAnios = @"";
    
    NSArray *cotizacion = array;
    NSDictionary *datosProyeccionFinanciera = [dictionary objectForKey:@"datosProyeccionFinanciera"];
    NSString *formaDePago = [datosProyeccionFinanciera objectForKey:@"formaDePago"];
    NSString *primaDiaria = [datosProyeccionFinanciera objectForKey:@"primaDiaria"];
    NSString *primaExedente = [datosProyeccionFinanciera objectForKey:@"primaExedente"];
    NSString *primaTotalAnual = [datosProyeccionFinanciera objectForKey:@"primaTotalAnual"];
    for (NSDictionary *anios in cotizacion) {
        NSString *anio = [anios objectForKey:@"anio"];
        NSString *fondoInversion = [anios objectForKey:@"fondoInversion"];
        NSString *sumaFallecimiento = [anios objectForKey:@"sumaFallecimiento"];
        NSString *nuevaCoberturaPorAnio = [NSString stringWithFormat:@"<tr><td>%@</td><td>%@</td><td>%@</td></tr>",anio,fondoInversion,sumaFallecimiento];
        coberturasAnios = [coberturasAnios stringByAppendingString:nuevaCoberturaPorAnio];
    }
    NSString *estructuraTablaCotizacion = @"";
    if (cotizacion.count > 0) {
        estructuraTablaCotizacion = [NSString stringWithFormat:@"\
                                     <table>\
                                     <thead>\
                                     <tr>\
                                     <th>Año</th>\
                                     <th>Fondo de Inversión</th>\
                                     <th>SA x Fallecimiento</th>\
                                     </tr>\
                                     </thead>\
                                     <tbody>\
                                     %@\
                                     </tbody>\
                                     </table>", coberturasAnios];
    }
    prospeccionString = [NSString stringWithFormat:
                         @"<div class=\"blu\">\
                         <h4>Proyección financiera del valor en Efectivo</h4>\
                         </div>\
                         <table>\
                         <tr>\
                         <td>Prima Excedente Anual</td>\
                         <td>%@</td>\
                         </tr>\
                         <tr>\
                         <td>Prima Total Anual</td>\
                         <td>%@</td>\
                         </tr>\
                         <tr>\
                         <td>Prima Diaria</td>\
                         <td>%@</td>\
                         </tr>\
                         <tr>\
                         <td>Forma de Pago</td>\
                         <td>%@</td>\
                         </tr>\
                         </table>%@",primaExedente,primaTotalAnual,primaDiaria,formaDePago, estructuraTablaCotizacion];
    return prospeccionString;
}


#pragma mark - IBActions Methods

-(IBAction)exitToWebView:(id)sender{
    
    [self.view removeFromSuperview];
}

@end
