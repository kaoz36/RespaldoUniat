//
//  SCContentForPageViewVC.h
//  SIPAC
//
//  Created by Jaguar3 on 25/03/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import <UIKit/UIKit.h>

/**
 class that contains the information in pages
 */
@interface SCContentForPageViewVC : UIViewController <UITableViewDelegate,UITableViewDataSource>

@property (strong, nonatomic) NSString *labelContents;
@property (strong, nonatomic) NSString *Poliza;

/**
 General View Outlets
 */
@property (weak, nonatomic) IBOutlet UIView *GeneralView;
@property (weak, nonatomic) IBOutlet UITextField *TxtEstadoCivil;

@property (weak, nonatomic) IBOutlet UITextField *TxtSexo;
@property (weak, nonatomic) IBOutlet UITextField *TxtFuma;
@property (weak, nonatomic) IBOutlet UITextField *TxtFechaNac;
@property (weak, nonatomic) IBOutlet UITextField *TxtEdad;
@property (weak, nonatomic) IBOutlet UITextField *TxtCalle;
@property (weak, nonatomic) IBOutlet UITextField *TxtNoExt;
@property (weak, nonatomic) IBOutlet UITextField *TxtNoInt;
@property (weak, nonatomic) IBOutlet UITextField *TxtCP;
@property (weak, nonatomic) IBOutlet UITextField *TxtPoblacion;
@property (weak, nonatomic) IBOutlet UITextField *TxtColonia;
@property (weak, nonatomic) IBOutlet UITextField *TxtTelefono;
@property (weak, nonatomic) IBOutlet UITextField *TxtMail;

@property (weak, nonatomic) IBOutlet UITextField *TxtMontoPrimaPagada;
@property (weak, nonatomic) IBOutlet UITextField *TxtMontoReserva;
@property (weak, nonatomic) IBOutlet UITextField *TxtMontoFondoInv;
@property (weak, nonatomic) IBOutlet UITextField *TxtPrimaPendiente;
@property (weak, nonatomic) IBOutlet UITextField *TxtRecibosPendientes;
@property (weak, nonatomic) IBOutlet UITextField *TxtUltimoPago;
@property (weak, nonatomic) IBOutlet UITextField *TxtPrimaEmitida;
@property (weak, nonatomic) IBOutlet UITextField *RecibosQuincenal;

@property (weak, nonatomic) IBOutlet UITextField *TxtFechaInicialVig;
@property (weak, nonatomic) IBOutlet UITextField *TxtFechaUltimaMod;
@property (weak, nonatomic) IBOutlet UITextField *TxtFechaUltimoRet;
@property (weak, nonatomic) IBOutlet UITextField *TxtOrdenPagoCheque;
@property (weak, nonatomic) IBOutlet UITextField *TxtAdicional;
@property (weak, nonatomic) IBOutlet UITextField *TxtMonto;

@property (weak, nonatomic) IBOutlet UITableView *tableViewCobertura;


//Sales View Outlets
@property (weak, nonatomic) IBOutlet UIView *Ventas;

@property (weak, nonatomic) IBOutlet UITableView *tableViewServicioVenta;
@property (weak, nonatomic) IBOutlet UITableView *tableViewServicioInterno;

//Services View Outlets
@property (weak, nonatomic) IBOutlet UIView *Servicios;

@property (weak, nonatomic) IBOutlet UITableView *tableViewServicios;

//Cobranza View Outlets
@property (weak, nonatomic) IBOutlet UIView *Cobranza;

@property (weak, nonatomic) IBOutlet UITextField *TxtRefCobBan;
@property (weak, nonatomic) IBOutlet UITextField *TxtRefAlf;
@property (weak, nonatomic) IBOutlet UITextField *TxtPrima;
@property (weak, nonatomic) IBOutlet UITextField *TxtPriEsp;
@property (weak, nonatomic) IBOutlet UITextField *TxtPriDesc;
@property (weak, nonatomic) IBOutlet UITextField *TxtConcepto;
@property (weak, nonatomic) IBOutlet UITextField *TxtSubRet;
@property (weak, nonatomic) IBOutlet UITextField *TxtCveCob;
@property (weak, nonatomic) IBOutlet UITextField *TxtUniPag;
@property (weak, nonatomic) IBOutlet UITextField *TxtUltPag;
@property (weak, nonatomic) IBOutlet UITextField *TxtQuiMvto;
@property (weak, nonatomic) IBOutlet UITextField *TxtTipMov;



@property (strong, nonatomic) IBOutlet UIView *genericTableView;
@property (weak, nonatomic) UIViewController *parentViewVC;

@end
