//
//  SCCotizacionVC.h
//  SIPAC
//
//  Created by MacBook Pro de Hugo on 21/08/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import <UIKit/UIKit.h>
/**
Class representing the result of budget planning in a webView.
*/
@interface SCCotizacionVC : UIViewController<UIWebViewDelegate,UITextFieldDelegate>
/**
 Initialize a new cotizacion object
 @param cotizacion The structure that contains all the information budget
 @param suma the the calculation of the 20-year projection
 @param primaExcedente the primaExedente
 @param tienePrima if have primaExedente
 @returns a newly initialized object
 */
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil cotizacion:(NSDictionary *)verCotizacion suma:(double)suma primaExcedente:(double)primaExcedente tienePrima:(BOOL)tienePrimaExcedente;
/**
 the value of interesGarantizado
 */
@property  double interesGarantizado;
/**
 the value of costoAdministracion
 */
@property  double costoAdministracion;
/**
 the calculation of the 20-year projection
 */
@property (nonatomic, assign) double sumaMayor;
/**
 the value of prima exedente
 */
@property (nonatomic, assign) double primaExcedente;
/**
 if have prima exedente
 */
@property (nonatomic, assign) BOOL tienePrimaExcedente;

@end
