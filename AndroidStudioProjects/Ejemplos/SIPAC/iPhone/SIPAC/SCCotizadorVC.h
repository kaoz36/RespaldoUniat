//
//  SCCotizadorVC.h
//  SIPAC
//
//  Created by Jaguar3 on 29/05/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SCComboBox.h"
#import "SCCotizador.h"
/**
 Class representing a budget planning in the Screen.
 */
@interface SCCotizadorVC : UIViewController<UITextFieldDelegate, UIScrollViewDelegate, SCCoberturaDelegate>

@property (strong) SCComboBox *comboProfessions;
@property (strong) SCComboBox *comboFormaDePago,*comboFormaDeCalculo;
@property (strong) SCComboBox *comboHijos;
@property (strong) SCComboBox *comboCoberturaComplementaria,*comboCancerPlus1,*comboCancerPlus2,*comboCancerPlus3;

@property (strong) NSDictionary  *dataAllAge,*dataAllAgeConyuge;
@property (strong) IBOutlet UITextField *primaExedenteAnual;


@property int edad;

@property (strong, nonatomic)  NSString *pago,*calculo;

#pragma mark - iphoneOutlets

@property (weak, nonatomic) IBOutlet UIScrollView *theScroller;
@property (weak, nonatomic) IBOutlet UIView *popUpDatos;
@property (weak, nonatomic) IBOutlet UIView *popUpPrima;


#pragma mark - info

@property (weak, nonatomic) IBOutlet UITextField *producto;
@property (weak, nonatomic) IBOutlet UITextField *primaTotalAnual;
@property (weak, nonatomic) IBOutlet UITextField *salarioMinimo;
@property (weak, nonatomic) IBOutlet UITextField *nombreTitular;
@property (weak, nonatomic) IBOutlet UILabel     *lblprimaAnualTotal;
@property (weak, nonatomic) IBOutlet UITextField *prima;


#pragma mark - comboBox

@property (weak, nonatomic) IBOutlet UIButton *cmbProfession;
@property (weak, nonatomic) IBOutlet UIButton *cmbFormaDeCalculo;
@property (weak, nonatomic) IBOutlet UIButton *cmbFormaDePago;
@property (weak, nonatomic) IBOutlet UIButton *cmbGFH;

@property (weak, nonatomic) IBOutlet UIButton *cbbCoberturaComplementaria;
@property (weak, nonatomic) IBOutlet UIButton *cbbCancerPlus1;
@property (weak, nonatomic) IBOutlet UIButton *cbbCancerPlus2;
@property (weak, nonatomic) IBOutlet UIButton *cbbCancerPlus3;


#pragma mark - buttonsSelected


@property (weak, nonatomic) IBOutlet UIButton *nuevaPoliza;
@property (weak, nonatomic) IBOutlet UIButton *conDevolucion;
@property (weak, nonatomic) IBOutlet UIButton *incrusiones;
@property (weak, nonatomic) IBOutlet UIButton *sinDevolucion;

@property (weak, nonatomic) IBOutlet UIButton *hombre;
@property (weak, nonatomic) IBOutlet UIButton *mujer;
@property (weak, nonatomic) IBOutlet UIButton *fuma;
@property (weak, nonatomic) IBOutlet UIButton *noFuma;


@property (weak, nonatomic) IBOutlet UIButton *btnTitular;
@property (weak, nonatomic) IBOutlet UIButton *btnCoberturaT;
@property (weak, nonatomic) IBOutlet UIButton *btnCoberturaC;
@property (weak, nonatomic) IBOutlet UIButton *btnCoberturaH;


#pragma mark - Cobertura T

@property (weak,nonatomic) IBOutlet UIButton *btnSelectBas;
@property (weak,nonatomic) IBOutlet UIButton *btnSelectBit;
@property (weak,nonatomic) IBOutlet UIButton *btnSelectCii;
@property (weak,nonatomic) IBOutlet UIButton *btnSelectCma;
@property (weak,nonatomic) IBOutlet UIButton *btnSelectTima;
@property (weak,nonatomic) IBOutlet UIButton *btnSelectCat;
@property (weak,nonatomic) IBOutlet UIButton *btnSelectGfa;
@property (weak,nonatomic) IBOutlet UIButton *btnSelectGe;

@property (weak,nonatomic) IBOutlet UITextField *txtSelectBas;
@property (weak,nonatomic) IBOutlet UITextField *txtSelectBit;
@property (weak,nonatomic) IBOutlet UITextField *txtSelectCii;
@property (weak,nonatomic) IBOutlet UITextField *txtSelectCma;
@property (weak,nonatomic) IBOutlet UITextField *txtSelectTima;
@property (weak,nonatomic) IBOutlet UITextField *txtSelectCat;
@property (weak,nonatomic) IBOutlet UITextField *txtSelectGfa;
@property (weak,nonatomic) IBOutlet UITextField *txtSelectGe;

@property (weak,nonatomic) IBOutlet UILabel *lblSelectBas;
@property (weak,nonatomic) IBOutlet UILabel *lblSelectBit;
@property (weak,nonatomic) IBOutlet UILabel *lblSelectCii;
@property (weak,nonatomic) IBOutlet UILabel *lblSelectCma;
@property (weak,nonatomic) IBOutlet UILabel *lblSelectTima;
@property (weak,nonatomic) IBOutlet UILabel *lblSelectCat;
@property (weak,nonatomic) IBOutlet UILabel *lblSelectGfa;
@property (weak,nonatomic) IBOutlet UILabel *lblSelectGe;


-(IBAction)BASSelected:(id)sender;
-(IBAction)BITSelected:(id)sender;
-(IBAction)CIISelected:(id)sender;
-(IBAction)CMASelected:(id)sender;
-(IBAction)TIBASelected:(id)sender;
-(IBAction)CATSelected:(id)sender;
-(IBAction)GFASelected:(id)sender;
-(IBAction)GESelected:(id)sender;

#pragma mark - Cobertura C


@property (weak,nonatomic) IBOutlet UIButton *btnEnableBeneficiosConyuge;
@property (weak,nonatomic) IBOutlet UIButton *btnSelectBacy;
@property (weak,nonatomic) IBOutlet UIButton *SelectGFC;
@property (weak,nonatomic) IBOutlet UIButton *SelectCP;

@property (weak,nonatomic) IBOutlet UITextField *txtSelectNombreConyuge;
@property (weak,nonatomic) IBOutlet UITextField *txtSelectcoberturaConyuge;
@property (weak,nonatomic) IBOutlet UITextField *txtSelectGFC;
@property (weak,nonatomic) IBOutlet UITextField *txtSelectCancerPlus;
@property (weak,nonatomic) IBOutlet UITextField *txtSelectEdadConyuge;

@property (weak,nonatomic) IBOutlet UILabel *lblSelectcoberturaConyuge;
@property (weak,nonatomic) IBOutlet UILabel *lblSelectGFC;
@property (weak,nonatomic) IBOutlet UILabel *lblSelectCancerPlus;
@property (weak,nonatomic) IBOutlet UILabel *lblSexo;


-(IBAction)enableBeneficiosConyuge:(id)sender;
-(IBAction)BACYSelected:(id)sender;
-(IBAction)GFCSelected:(id)sender;
-(IBAction)CPSelected:(id)sender;



#pragma mark - Cobertura H

@property (weak,nonatomic) IBOutlet UIButton *btnSelectCC;
@property (weak,nonatomic) IBOutlet UIButton *btnCancerPLus1;
@property (weak,nonatomic) IBOutlet UIButton *btnCancerPlus2;
@property (weak,nonatomic) IBOutlet UIButton *btnCancerPlus3;

@property (weak,nonatomic) IBOutlet UITextField *txtSelectSGFH;
@property (weak,nonatomic) IBOutlet UITextField *txtSelectCoberturaComplementaria;
@property (weak,nonatomic) IBOutlet UITextField *txtSelectCancerPLus1;
@property (weak,nonatomic) IBOutlet UITextField *txtSelectCancerPLus2;
@property (weak,nonatomic) IBOutlet UITextField *txtSelectCancerPLus3;


@property (weak,nonatomic) IBOutlet UITextField *txtSelectNameCoberturaComplementaria;
@property (weak,nonatomic) IBOutlet UITextField *txtSelectNameCancerPLus1;
@property (weak,nonatomic) IBOutlet UITextField *txtSelectNameCancerPLus2;
@property (weak,nonatomic) IBOutlet UITextField *txtSelectNameCancerPLus3;

@property (weak,nonatomic) IBOutlet UILabel *lblSGFH;
@property (weak,nonatomic) IBOutlet UILabel *lblSelectCoberturaComplementaria;
@property (weak,nonatomic) IBOutlet UILabel *lblSelectCancerPLus1;
@property (weak,nonatomic) IBOutlet UILabel *lblSelectCancerPLus2;
@property (weak,nonatomic) IBOutlet UILabel *lblSelectCancerPLus3;

@property (weak,nonatomic) IBOutlet UITextField *txtSelectAgeCoberturaComplementaria;
@property (weak,nonatomic) IBOutlet UITextField *txtSelectAgeCancerPLus1;
@property (weak,nonatomic) IBOutlet UITextField *txtSelectAgeCancerPLus2;
@property (weak,nonatomic) IBOutlet UITextField *txtSelectAgeCancerPLus3;

-(void)selectGFH;
-(IBAction)CCSelected:(id)sender;
-(IBAction)cancerPlus1:(id)sender;
-(IBAction)cancerPlus2:(id)sender;
-(IBAction)cancerPlus3:(id)sender;


#pragma mark - textFields Age

@property (weak, nonatomic) IBOutlet UITextField *realAge;
@property (weak, nonatomic) IBOutlet UITextField *estimateAge;
@property (weak, nonatomic) IBOutlet UITextField *extraPrima;
@property (weak, nonatomic) IBOutlet UITextView *extraPrimaIphone;


#pragma mark - Views coberturas


@property (weak, nonatomic) IBOutlet UIView *titular;
@property (weak, nonatomic) IBOutlet UIView *coberturaT;
@property (weak, nonatomic) IBOutlet UIView *coberturaC;
@property (weak, nonatomic) IBOutlet UIView *coberturaH;


#pragma mark - IBActions Selected Buttons
-(IBAction)tapPrima:(id)sender;
-(IBAction)tapDatos:(id)sender;
-(IBAction)exitPopUp:(id)sender;
-(IBAction)exitPopUpPrima:(id)sender;

-(IBAction)returnKeyBoard:(id)sender;

-(IBAction)fumaSelected:(id)sender;
-(IBAction)noFumaSelected:(id)sender;
-(IBAction)hombreSelected:(id)sender;
-(IBAction)mujerSelected:(id)sender;

-(IBAction)nuevaPolizaSelected:(id)sender;
-(IBAction)inclusionesSelected:(id)sender;
-(IBAction)conDevolucionSelected:(id)sender;
-(IBAction)sinDevolcionSelected:(id)sender;

-(IBAction)tapTitular:(id)sender;
-(IBAction)tapCoberturaT:(id)sender;
-(IBAction)tapCoberturaC:(id)sender;
-(IBAction)tapCoberturaH:(id)sender;

-(IBAction)limpiarCotizador:(id)sender;

-(void)selectOcupacion;
-(void)selectPago;
-(void)selectCalculoWithIndex:(NSInteger)index;
-(void)initCotizador;

@end
