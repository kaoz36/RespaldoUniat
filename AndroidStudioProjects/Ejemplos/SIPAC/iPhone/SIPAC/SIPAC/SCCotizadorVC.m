//
//  SCCotizadorVC.m
//  SIPAC
//
//  Created by Jaguar3 on 29/05/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import "SCCotizadorVC.h"
#import "SCDatabaseManager.h"
#import "SCPopoverCotizadorVC.h"
#import "SCManager.h"
#import "SCCotizador.h"
#import "SCCobertura.h"
#import "SCCotizacionVC.h"

@interface SCCotizadorVC (){
    
    double sumaAseguradaBas;
    double sumaAseguradaTextField;
    double primaExedenteAnualValue;
    int countTipoCotizacion;
}

@property (strong) SCCotizador *cotizador;
@property (nonatomic, assign) BOOL esNuevaPoliza;
@property (strong)  SCCotizadorVC *cotizadorVC;
@property (strong)  SCCotizacionVC *cotizacionVC;
@property (nonatomic, strong) UIAlertView *alertaInsuficientes;
@property (nonatomic, strong) UIAlertView *alertaSinSolucion;
@property (nonatomic, strong) UIAlertView *alertaExcede;

@end

@implementation SCCotizadorVC

- (void)viewDidLoad
{
    [super viewDidLoad];
   
    if ([[[UIDevice currentDevice] systemVersion] integerValue] == 7) {
        self.edgesForExtendedLayout = UIRectEdgeNone;
    }
    
    [self initCotizador];
    self.realAge.keyboardType = UIKeyboardTypeNumberPad;
    self.txtSelectEdadConyuge.keyboardType = UIKeyboardTypeNumberPad;
    countTipoCotizacion = 0;
    self.pago = @"Anual";
    self.calculo = @"Por suma asegurada";
    self.txtSelectEdadConyuge.text = @"15";
    self.txtSelectAgeCoberturaComplementaria.text = @"15";
    self.txtSelectAgeCancerPLus1.text = @"15";
    self.txtSelectAgeCancerPLus2.text = @"15";
    self.txtSelectAgeCancerPLus3.text = @"15";
    self.lblSexo.text=@"F";
    self.popUpPrima.hidden = YES;
    self.btnEnableBeneficiosConyuge.hidden = YES;
    self.extraPrima.enabled = NO;
    self.popUpDatos.hidden = YES;
    self.primaTotalAnual.enabled = NO;
    self.estimateAge.enabled = NO;
    self.producto.enabled = NO;
    self.btnTitular.selected = YES;
    self.conDevolucion.selected = YES;
    self.popUpDatos.hidden = YES;
    self.txtSelectSGFH.enabled = NO;
    self.nuevaPoliza.selected = YES;
    self.esNuevaPoliza = YES;
    self.btnSelectBas.selected = YES;
    self.btnSelectBas.userInteractionEnabled = NO;
    self.hombre.selected = YES;
    self.fuma.selected = YES;
    self.primaExedenteAnual.delegate = self;
    NSArray *dataProfession = [self.cotizador loadProfesions];
    NSMutableArray *dataGenero = [[NSMutableArray alloc]initWithObjects:@"F",@"M", nil];
    NSMutableArray *dataFormaDePago= [[NSMutableArray alloc]initWithObjects:@"Anual",@"Mensual",@"Quincenal",nil];
    NSMutableArray *dataFormaDeCalculo= [[NSMutableArray alloc]initWithObjects:@"Por suma asegurada",@"Prima",nil];
    NSMutableArray *dataHijos= [[NSMutableArray alloc]initWithObjects:@"Sin Hijos",@"GFH: 1 Hijo",@"GFH: 2 Hijos",@"GFH: 3 Hijos",@"GFH: 4 Hijos",@"GFH: 5 Hijos",@"GFH: 6 Hijos",@"GFH: 7 Hijos",@"GFH: 8 Hijos",@"GFH: 9 Hijos",@"GFH: 10 Hijos",nil];
    _comboHijos = [[SCComboBox alloc] initWithButton:self.cmbGFH isTableUp:NO visibleOptionLimit:4 TableDelegate:self onView:self.coberturaH dataSource:dataHijos];
    [self.cmbGFH addTarget:self action:@selector(btnTouched:) forControlEvents:UIControlEventTouchUpInside];
    self.comboFormaDeCalculo = [[SCComboBox alloc] initWithButton:self.cmbFormaDeCalculo isTableUp:NO visibleOptionLimit:4 TableDelegate:self onView:self.titular dataSource:dataFormaDeCalculo];
    [self.cmbFormaDeCalculo addTarget:self action:@selector(btnTouched:) forControlEvents:UIControlEventTouchUpInside];
    self.comboCoberturaComplementaria = [[SCComboBox alloc] initWithButton:self.cbbCoberturaComplementaria isTableUp:NO visibleOptionLimit:4 TableDelegate:self onView:self.coberturaH dataSource:dataGenero];
    [self.cbbCoberturaComplementaria addTarget:self action:@selector(btnTouched:) forControlEvents:UIControlEventTouchUpInside];
    self.comboCancerPlus1 = [[SCComboBox alloc] initWithButton:self.cbbCancerPlus1 isTableUp:NO visibleOptionLimit:4 TableDelegate:self onView:self.coberturaH dataSource:dataGenero];
    [self.cbbCancerPlus1 addTarget:self action:@selector(btnTouched:) forControlEvents:UIControlEventTouchUpInside];
    self.comboCancerPlus2 = [[SCComboBox alloc] initWithButton:self.cbbCancerPlus2 isTableUp:NO visibleOptionLimit:4 TableDelegate:self onView:self.coberturaH dataSource:dataGenero];
    [self.cbbCancerPlus2 addTarget:self action:@selector(btnTouched:) forControlEvents:UIControlEventTouchUpInside];
    self.comboCancerPlus3 = [[SCComboBox alloc] initWithButton:self.cbbCancerPlus3 isTableUp:NO visibleOptionLimit:4 TableDelegate:self onView:self.coberturaH dataSource:dataGenero];
    [self.cbbCancerPlus3 addTarget:self action:@selector(btnTouched:) forControlEvents:UIControlEventTouchUpInside];
    self.comboFormaDePago = [[SCComboBox alloc] initWithButton:self.cmbFormaDePago isTableUp:NO visibleOptionLimit:4 TableDelegate:self onView:self.titular dataSource:dataFormaDePago];
    [self.cmbFormaDePago addTarget:self action:@selector(btnTouched:) forControlEvents:UIControlEventTouchUpInside];
    if([UIDevice currentDevice].userInterfaceIdiom == UIUserInterfaceIdiomPhone) {
        self.primaTotalAnual.backgroundColor = [UIColor colorWithRed:(228/255.0) green:(237/255.0) blue:(191/255.0) alpha:0.9];
        self.theScroller.contentSize=CGSizeMake(320,590);
        CGRect screenBounds = [[UIScreen mainScreen] bounds];
        if (screenBounds.size.height == 568) {
            self.theScroller.contentSize=CGSizeMake(320,540);
            [self.theScroller setFrame:CGRectMake(0, 120, 320, 420)];
            self.comboProfessions = [[SCComboBox alloc] initWithButton:self.cmbProfession isTableUp:NO visibleOptionLimit:3 TableDelegate:self onView:self.popUpDatos dataSource:dataProfession];
            [self.cmbProfession addTarget:self action:@selector(btnTouched:) forControlEvents:UIControlEventTouchUpInside];
        } else {
            self.comboProfessions = [[SCComboBox alloc] initWithButton:self.cmbProfession isTableUp:YES visibleOptionLimit:3 TableDelegate:self onView:self.popUpDatos dataSource:dataProfession];
            [self.cmbProfession addTarget:self action:@selector(btnTouched:) forControlEvents:UIControlEventTouchUpInside];
        }
    } else {
        self.comboProfessions = [[SCComboBox alloc] initWithButton:self.cmbProfession isTableUp:NO visibleOptionLimit:4 TableDelegate:self onView:self.view dataSource:dataProfession];
        [self.cmbProfession addTarget:self action:@selector(btnTouched:) forControlEvents:UIControlEventTouchUpInside];
        self.prima.hidden = YES;
    }
  
        self.navigationController.navigationBar.topItem.title = @"Cotizador";
        UIImage *btnImgAlert = [UIImage imageNamed:@"btn_sidemenu_unpressed"];
        UIImage *btnImgAlertPressed = [UIImage imageNamed:@"btn_sidemenu_pressed"];
        UIButton *myleftButton = [UIButton buttonWithType:UIButtonTypeCustom];
        [myleftButton setImage:btnImgAlert forState:UIControlStateNormal];
        [myleftButton setImage:btnImgAlertPressed forState:UIControlStateHighlighted];
        myleftButton.frame = CGRectMake(200.0, 10.0, btnImgAlert.size.width, btnImgAlert.size.height);
        [myleftButton addTarget:self action:@selector(paneMenuTapped:) forControlEvents:UIControlEventTouchUpInside];
        UIBarButtonItem *leftButton = [[UIBarButtonItem alloc]initWithCustomView:myleftButton];
        self.navigationItem.leftBarButtonItem = leftButton;

    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWasShown:)
                                                 name:UIKeyboardDidShowNotification
                                               object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWasHidden:)
                                                 name:UIKeyboardWillHideNotification
                                               object:nil];
    
    self.conDevolucion.selected = YES;
    self.sinDevolucion.selected = NO;
    [self calculateAge];
    self.cotizador.esConDevolucion = YES;
    self.comboHijos.index = 0;
    [self initCotizador];
}


#pragma mark - IBActions ComboBox Touched *************************************************************

- (void)btnTouched:(UIButton*)sender {
    if (sender == self.cmbGFH) {
        [self.comboHijos showOrHideCombo];
        [self.comboHijos setGFH:YES];
    }else if (sender == self.cmbProfession){
        [self.comboProfessions showOrHideCombo];
        [self.comboProfessions setOcupacion:YES];
    }else if (sender == self.cbbCoberturaComplementaria){
        [self.comboCoberturaComplementaria showOrHideCombo];
    }else if (sender == self.cmbFormaDeCalculo){
        [self.comboFormaDeCalculo showOrHideCombo];
        [self.comboFormaDeCalculo setCalculo:YES];
    }else if (sender == self.cmbFormaDePago){
        [self.comboFormaDePago showOrHideCombo];
        [self.comboFormaDePago setPago:YES];
    }
    if (sender == self.cbbCancerPlus1){
        [self.comboCancerPlus1 showOrHideCombo];
        [self.comboCancerPlus1 setCP1:YES];
    }
    if (sender == self.cbbCancerPlus2){
        [self.comboCancerPlus2 showOrHideCombo];
        [self.comboCancerPlus2 setCP2:YES];
    }
    if (sender == self.cbbCancerPlus3){
        [self.comboCancerPlus3 showOrHideCombo];
        [self.comboCancerPlus3 setCP3:YES];
    }
}

#pragma mark - IBActions Options Sections *************************************************************

-(void)calculateAge {
    self.edad = [self.realAge.text integerValue];
    int realAge = [self.realAge.text integerValue];
    
    if (self.mujer.selected == YES) {
        self.edad = self.edad - 3;
    }
    if (self.noFuma.selected == YES) {
        self.edad = self.edad - 2;
    }
    if (self.edad <=15) {
        self.edad = 15;
        self.realAge.text = @"15";
    }
    
    NSString *realAgeString = [NSString stringWithFormat:@"%d",realAge];
    self.realAge.text = realAgeString;
    
    if ([self.realAge.text doubleValue]<=15) {
        self.edad = 15;
        self.realAge.text = @"15";
    }
    if ([self.realAge.text doubleValue] >=70) {
        self.edad = 70;
        self.realAge.text = @"70";
        if (self.mujer.selected == YES) {
            self.edad = self.edad - 3;
        }
        if (self.noFuma.selected == YES) {
            self.edad = self.edad - 2;
        }
    }
    
    self.realAge.textColor = [UIColor blackColor];
    NSString *estimulateAgeString = [NSString stringWithFormat:@"%d", self.edad];
    self.estimateAge.text = estimulateAgeString;
    [self selectOcupacion];
    [self initCotizador];
    [self recalculoConBotonSeleccionado:YES];
    if (self.btnSelectCC.selected == YES) {
        [self recalculoConBotonSeleccionado:NO];
        [self recalculoConBotonSeleccionado:YES];
    }
    
}

-(void)selectPago {
    self.pago = self.cmbFormaDePago.titleLabel.text;
    if (self.cotizador.esPrima) {
        double Prima = [self.prima.text doubleValue];
        if ([self.pago isEqualToString:@"Anual"]) {
            Prima = Prima * 1;
        } else if ([self.pago isEqualToString:@"Mensual"]) {
            Prima = Prima * 12;
        } else if ([self.pago isEqualToString:@"Quincenal"]) {
            Prima = Prima * 24;
        }
        self.lblprimaAnualTotal.text = @"Prima Total Anual";
        NSString *PrimaAnualString = [NSString stringWithFormat:@"%.2f",Prima];
        self.primaTotalAnual.text = PrimaAnualString;
        self.cotizador.primaTotalCotizadorPrima = Prima;
    } else {
        [self calculateAge];
        if ([self.pago isEqualToString:@"Anual"]) {
            self.lblprimaAnualTotal.text=@"Prima Total Anual";
        } else if ([self.pago isEqualToString:@"Mensual"]) {
            self.lblprimaAnualTotal.text=@"Prima Total Mensual";
        } else if ([self.pago isEqualToString:@"Quincenal"]) {
            self.lblprimaAnualTotal.text=@"Prima Total Quincenal";
        }
        [self cotizador:self.cotizador didGetSumaAseguradaTotal:self.cotizador.resultadoSumasAseguradas];
    }
}

-(void)selectCalculo
{    
    self.calculo = self.cmbFormaDeCalculo.titleLabel.text;
    BOOL porPrima = NO;
    if ([self.calculo isEqualToString:@"Por suma asegurada"]) {
        self.prima.hidden = YES;
    } else if ([self.calculo isEqualToString:@"Prima"]) {
        self.prima.hidden = NO;
        porPrima = YES;
    }
    [self configurarTituloPrimaPorPrima:porPrima];
}

-(void) configurarTituloPrimaPorPrima:(BOOL)porPrima
{
    self.pago = self.cmbFormaDePago.titleLabel.text;
    if (porPrima) {
        self.lblprimaAnualTotal.text = @"Prima Total Anual";
    } else {
        if ([self.pago isEqualToString:@"Anual"]) {
            self.lblprimaAnualTotal.text=@"Prima Total Anual";
        } else if ([self.pago isEqualToString:@"Mensual"]) {
            self.lblprimaAnualTotal.text=@"Prima Total Mensual";
        } else if ([self.pago isEqualToString:@"Quincenal"]) {
            self.lblprimaAnualTotal.text=@"Prima Total Quincenal";
        }
    }
}

-(void)selectOcupacion{
    
    [self.cotizador selectOcupacion:self.comboProfessions.index];
    
}

-(IBAction)limpiarCotizador:(id)sender{
    
    //    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
    //        //Device is ipad
    //        self.cotizadorVC = [[SCCotizadorVC alloc] initWithNibName:@"SCCotizadorVC" bundle:nil];
    //    }else{
    //        //Device is iphone
    //        self.cotizadorVC = [[SCCotizadorVC alloc] initWithNibName:@"SCCotizadorVCphone" bundle:nil];
    //    }
    //
    //    [self.view addSubview:self.cotizadorVC.view];
    
    [[SCManager sharedInstance]goToQuotation];
    
}

#pragma mark - KeyBoard Methods **********************************************************************

- (void)keyboardWasShown:(NSNotification *)aNotification {
    
    sumaAseguradaBas   =  [self.txtSelectBas.text doubleValue];
    
    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPhone) {
        
        if ([self.realAge isFirstResponder] || [self.txtSelectAgeCoberturaComplementaria isFirstResponder] || [self.txtSelectcoberturaConyuge isFirstResponder]) {
            CGRect tmpFrame = self.view.frame;
            tmpFrame.origin = CGPointMake(0, -135);
            
            [UIView animateWithDuration:0.5 animations:^{
                self.view.frame = tmpFrame;
            }];
            
        }else if ([self.txtSelectAgeCancerPLus1 isFirstResponder] || [self.txtSelectAgeCancerPLus2 isFirstResponder] || [self.txtSelectAgeCancerPLus3 isFirstResponder]) {
            
            CGRect tmpFrame = self.view.frame;
            tmpFrame.origin = CGPointMake(0, -200);
            
            [UIView animateWithDuration:0.5 animations:^{
                self.view.frame = tmpFrame;
            }];
            
        }else if ([self.salarioMinimo isFirstResponder]) {
            
            CGRect tmpFrame = self.view.frame;
            tmpFrame.origin = CGPointMake(0, -210);
            
            [UIView animateWithDuration:0.5 animations:^{
                self.view.frame = tmpFrame;
            }];
            
        } else if ([self.txtSelectBas isFirstResponder] || [self.txtSelectBit isFirstResponder] || [self.txtSelectSGFH isFirstResponder] || [self.txtSelectEdadConyuge isFirstResponder]) {
            
            CGRect tmpFrame = self.view.frame;
            tmpFrame.origin = CGPointMake(0, -90);
            [UIView animateWithDuration:0.5 animations:^{
                self.view.frame = tmpFrame;
            }];
        }   else if ([self.txtSelectCii isFirstResponder] || [self.txtSelectCma isFirstResponder] || [self.txtSelectTima isFirstResponder] || [self.txtSelectCat isFirstResponder] || [self.txtSelectGfa isFirstResponder] || [self.txtSelectGe isFirstResponder]  || [self.txtSelectGFC isFirstResponder] || [self.txtSelectCancerPlus isFirstResponder] || [self.txtSelectCoberturaComplementaria isFirstResponder] || [self.txtSelectCancerPLus1 isFirstResponder] || [self.txtSelectCancerPLus2 isFirstResponder] || [self.txtSelectCancerPLus3 isFirstResponder]) {
            CGRect tmpFrame = self.view.frame;
            tmpFrame.origin = CGPointMake(0, -210);
            [UIView animateWithDuration:0.5 animations:^{
                self.view.frame = tmpFrame;
            }];
        }
    } else {
        
        if ([[[UIDevice currentDevice] systemVersion] integerValue] == 7) {
        }else{
            CGRect tmpFrame = self.view.frame;
            tmpFrame.origin = CGPointMake(0, 0);
            [UIView animateWithDuration:0.5 animations:^{
                self.view.frame = tmpFrame;
            }];
            
            if ([self.primaExedenteAnual isFirstResponder] || [self.nombreTitular isFirstResponder]) {
                
                CGRect tmpFrame = self.view.frame;
                tmpFrame.origin = CGPointMake(0, 0);
                [UIView animateWithDuration:0.5 animations:^{
                    self.view.frame = tmpFrame;
                }];
            }
            
        }
        if ([self.txtSelectCat isFirstResponder] || [self.txtSelectGfa isFirstResponder] || [self.txtSelectGe isFirstResponder]) {
            CGRect tmpFrame = self.view.frame;
            tmpFrame.origin = CGPointMake(0, -180);
            [UIView animateWithDuration:0.5 animations:^{
                self.view.frame = tmpFrame;
            }];
        }
      
    }
}

- (void)keyboardWasHidden:(NSNotification *)aNotification {
    
    
    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPhone) {
        
        if ([[[UIDevice currentDevice] systemVersion] integerValue] == 7) {
        
            CGRect tmpFrame = self.view.frame;
            tmpFrame.origin = CGPointMake(0, 63);
            [UIView animateWithDuration:0.5 animations:^{
                self.view.frame = tmpFrame;
            }];
            
        }else{
            
            CGRect tmpFrame = self.view.frame;
            tmpFrame.origin = CGPointMake(0, 0);
            [UIView animateWithDuration:0.5 animations:^{
                self.view.frame = tmpFrame;
            }];
        }
        
        
    }else{
        
        
        if ([[[UIDevice currentDevice] systemVersion] integerValue] == 7) {
            if ([self.txtSelectCat isFirstResponder] || [self.txtSelectGfa isFirstResponder] || [self.txtSelectGe isFirstResponder]) {
                CGRect tmpFrame = self.view.frame;
                tmpFrame.origin = CGPointMake(0, 65);
                [UIView animateWithDuration:0.5 animations:^{
                    self.view.frame = tmpFrame;
                }];
            }
        }else{
            CGRect tmpFrame = self.view.frame;
            tmpFrame.origin = CGPointMake(0, 0);
            [UIView animateWithDuration:0.5 animations:^{
                self.view.frame = tmpFrame;
            }];
        }
        
    }
   
}

-(void)textFieldDidEndEditing:(UITextField *)textField {
    
    if (textField.tag >= 2 && textField.tag <= 17) {
        double sumaAsegurada = [textField.text doubleValue];
        if (sumaAsegurada > [self.txtSelectBas.text doubleValue]) {
            [self.cotizador reloadSuma];
            [self.cotizador evaluateChangeInSumaAsegurada:sumaAsegurada index:textField.tag-2 editandoTextField:NO];
            [self.cotizador reloadSuma];
        }else{
            [self.cotizador evaluateChangeInSumaAsegurada:sumaAsegurada index:textField.tag-2 editandoTextField:NO];
        }
        [self.cotizador calcularCoberturas];
        [self.cotizador sumaAseguradaGastosFunerarios];
    }
    if (textField.tag == 2) {
        double sumaAsegurada = [textField.text doubleValue];
        sumaAseguradaTextField = sumaAsegurada;
        [self.cotizador reloadSumaAsegurada];
        if (sumaAseguradaBas > sumaAseguradaTextField) {
            [self recalculoConBotonSeleccionado:YES];
        }
        [self initCotizadorSinBacy];
    }
    if ([textField isEqual:self.realAge]) {
        [self calculateAge];
    } else if ([textField isEqual:self.prima]) {
        [self selectPago];
    } else if ([textField isEqual:self.txtSelectAgeCancerPLus1]){
        if ([self.txtSelectAgeCancerPLus1.text integerValue] > 65) {
            self.txtSelectAgeCancerPLus1.text = @"65";
            self.txtSelectAgeCancerPLus1.textColor = [UIColor blackColor];
        }
        if ([self.txtSelectAgeCancerPLus1.text integerValue] < 15) {
            self.txtSelectAgeCancerPLus1.text = @"15";
        }
        [self initCotizador];
    } else if ([textField isEqual:self.txtSelectAgeCancerPLus2]){
        if ([self.txtSelectAgeCancerPLus2.text integerValue] > 65) {
            self.txtSelectAgeCancerPLus2.text = @"65";
            self.txtSelectAgeCancerPLus2.textColor = [UIColor blackColor];
        }
        if ([self.txtSelectAgeCancerPLus2.text integerValue] < 15) {
            self.txtSelectAgeCancerPLus2.text = @"15";
        }
        [self initCotizador];
    } else if ([textField isEqual:self.txtSelectAgeCancerPLus3]){
        if ([self.txtSelectAgeCancerPLus3.text integerValue] > 65) {
            self.txtSelectAgeCancerPLus3.text = @"65";
            self.txtSelectAgeCancerPLus3.textColor = [UIColor blackColor];
        }
        if ([self.txtSelectAgeCancerPLus3.text integerValue] < 15) {
            self.txtSelectAgeCancerPLus3.text = @"15";
        }
        [self initCotizador];
    }else if ([textField isEqual:self.txtSelectAgeCoberturaComplementaria]){
        if ([self.txtSelectAgeCoberturaComplementaria.text integerValue] > 70) {
            self.txtSelectAgeCoberturaComplementaria.text = @"70";
            self.txtSelectAgeCoberturaComplementaria.textColor = [UIColor blackColor];
        }
        if ([self.txtSelectAgeCoberturaComplementaria.text integerValue] < 15) {
            self.txtSelectAgeCoberturaComplementaria.text = @"15";
        }
        [self initCotizador];
    } else if ([textField isEqual:self.txtSelectEdadConyuge]) {
        if ([self.txtSelectEdadConyuge.text integerValue] < 15) {
             self.txtSelectEdadConyuge.text = @"15";
        }
        [self initCotizador];
    }
   
    if (textField == self.primaExedenteAnual) {
        if (!self.cotizador.esPrima) {
              primaExedenteAnualValue = [textField.text doubleValue];
        }
        [self initCotizador];
    }
    
    if (textField == self.prima) {
        [self initCotizador];
    }
    
    if (textField.tag >= 2 && textField.tag <= 17) {
        SCCobertura *cobertura = [self.cotizador.coberturas objectAtIndex:textField.tag - 2];
        [self.cotizador calcularCobertura:cobertura];
    }
    
    [self cancerValidation];
}

-(IBAction)returnKeyBoard:(id)sender {
    
    [self.realAge resignFirstResponder];
    [self.prima resignFirstResponder];
    [self.salarioMinimo resignFirstResponder];
    [self.txtSelectBas resignFirstResponder];
    [self.txtSelectBit resignFirstResponder];
    [self.txtSelectCii resignFirstResponder];
    [self.txtSelectCma resignFirstResponder];
    [self.txtSelectTima resignFirstResponder];
    [self.txtSelectCat resignFirstResponder];
    [self.txtSelectGfa resignFirstResponder];
    [self.txtSelectGe resignFirstResponder];
    [self.txtSelectcoberturaConyuge resignFirstResponder];
    [self.txtSelectGFC resignFirstResponder];
    [self.txtSelectCancerPlus resignFirstResponder];
    [self.txtSelectEdadConyuge resignFirstResponder];
    [self.txtSelectSGFH resignFirstResponder];
    [self.txtSelectCoberturaComplementaria resignFirstResponder];
    [self.txtSelectAgeCoberturaComplementaria resignFirstResponder];
    [self.txtSelectAgeCancerPLus1 resignFirstResponder];
    [self.txtSelectAgeCancerPLus2 resignFirstResponder];
    [self.txtSelectAgeCancerPLus3 resignFirstResponder];
    [self.txtSelectCancerPLus1 resignFirstResponder];
    [self.txtSelectCancerPLus2 resignFirstResponder];
    [self.txtSelectCancerPLus3 resignFirstResponder];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}

- (void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event {
	[self.view endEditing:YES];
	[super touchesBegan:touches withEvent:event];
	
}


- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
    
    if (self.nombreTitular == textField || self.txtSelectNameCoberturaComplementaria == textField || self.txtSelectNameCancerPLus1 == textField || self.txtSelectNameCancerPLus2 == textField || self.txtSelectNameCancerPLus3 == textField) {
        return YES;
    }
    
    NSCharacterSet *numbers = [NSCharacterSet characterSetWithCharactersInString:@"0123456789."];
    NSRange NumCharRange;
    NumCharRange = [string rangeOfCharacterFromSet:[numbers invertedSet]];
    if (NumCharRange.location != NSNotFound) {
        textField.text = [textField.text stringByReplacingCharactersInRange:range withString:@""];
        return NO;
    }
    
    NSString * searchStr = [textField.text stringByReplacingCharactersInRange:range withString:string];
    int edad = [searchStr integerValue];
    if (textField.tag == 1) {
        [self evaluateTitularRangeAge:edad];
    }else if (textField.tag >= 2 && textField.tag <= 17) {
        double sumaAsegurada = [searchStr doubleValue];
        [self.cotizador evaluateChangeInSumaAsegurada:sumaAsegurada index:textField.tag-2 editandoTextField:YES];
    }else if (textField.tag == 18){
        [self evaluateCP1RangeAge:edad];
    }else if (textField.tag == 19){
        [self evaluateCP2RangeAge:edad];
    }else if (textField.tag == 20){
        [self evaluateCP3RangeAge:edad];
    }else if (textField.tag == 21){
        [self evaluateConyugeRangeAge:edad];
    }else if (textField.tag == 22){
        [self evaluateCCRangeAge:edad];
    }
    
    return YES;
}

#pragma mark - IBActions Sections Cotizador *************************************************************

-(IBAction)tapTitular:(id)sender{
    
    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPhone) {
        self.theScroller.contentSize=CGSizeMake(320,590);
        
        CGRect screenBounds = [[UIScreen mainScreen] bounds];
        if (screenBounds.size.height == 568) {
            self.theScroller.contentSize=CGSizeMake(320,540);
        }
    }
    self.titular.hidden = NO;
    self.coberturaC.hidden = YES;
    self.coberturaH.hidden = YES;
    self.coberturaT.hidden = YES;
    
    self.btnTitular.selected = YES;
    self.btnCoberturaC.selected = NO;
    self.btnCoberturaH.selected = NO;
    self.btnCoberturaT.selected = NO;
   
    
}
-(IBAction)tapCoberturaT:(id)sender{
    
    [self validationGenero];
    //[self cotizadoresValidation];
    
    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPhone) {
        self.theScroller.contentSize=CGSizeMake(320,570);
        
        CGRect screenBounds = [[UIScreen mainScreen] bounds];
        if (screenBounds.size.height == 568) {
            self.theScroller.contentSize=CGSizeMake(320,530);
        }
    }
    self.titular.hidden = YES;
    self.coberturaC.hidden = YES;
    self.coberturaH.hidden = YES;
    self.coberturaT.hidden = NO;
    
    self.btnTitular.selected = NO;
    self.btnCoberturaC.selected = NO;
    self.btnCoberturaH.selected = NO;
    self.btnCoberturaT.selected = YES;
    countTipoCotizacion = 1;
    
}
-(IBAction)tapCoberturaC:(id)sender{
    
    [self validationGenero];
    
    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPhone) {
        self.theScroller.contentSize=CGSizeMake(320,0);
    }
    self.titular.hidden = YES;
    self.coberturaC.hidden = NO;
    self.coberturaH.hidden = YES;
    self.coberturaT.hidden = YES;
    
    self.btnTitular.selected = NO;
    self.btnCoberturaC.selected = YES;
    self.btnCoberturaH.selected = NO;
    self.btnCoberturaT.selected = NO;
    
}
-(IBAction)tapCoberturaH:(id)sender{
    
    [self validationGenero];
    
    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPhone) {
        self.theScroller.contentSize=CGSizeMake(320,640);
        
        CGRect screenBounds = [[UIScreen mainScreen] bounds];
        if (screenBounds.size.height == 568) {
            self.theScroller.contentSize=CGSizeMake(320,530);
        }
    }
    
    self.titular.hidden = YES;
    self.coberturaC.hidden = YES;
    self.coberturaH.hidden = NO;
    self.coberturaT.hidden = YES;
    
    self.btnTitular.selected = NO;
    self.btnCoberturaC.selected = NO;
    self.btnCoberturaH.selected = YES;
    self.btnCoberturaT.selected = NO;
    
}

#pragma mark - IBActions Titular Section *************************************************************

-(IBAction)nuevaPolizaSelected:(id)sender {
    if (self.nuevaPoliza.selected == NO) {
        self.nuevaPoliza.selected = YES;
        self.incrusiones.selected = NO;
        self.esNuevaPoliza = YES;
        self.btnSelectBas.selected = YES;
        self.btnSelectBas.userInteractionEnabled = NO;
        if (![self.cmbFormaDeCalculo.titleLabel.text isEqualToString:@"Prima"]) {
            //[self.cotizador validationSumaAseguradaOpcionNuevaPoliza];
        }
         [self initCotizador];
         [self.cotizador calcularCoberturas];
         [self.cotizador reloadSuma];
         [self recalculoConBotonSeleccionado:YES];
//          if ( [self.cmbFormaDeCalculo.titleLabel.text isEqualToString:@"Prima"]) {
//              
//          }
    }
}
-(IBAction)inclusionesSelected:(id)sender {
    
    if (countTipoCotizacion == 0) {
        self.btnSelectBas.selected = NO;
    }
    
    if (self.incrusiones.selected == NO) {
        self.incrusiones.selected = YES;
        self.nuevaPoliza.selected = NO;
        self.esNuevaPoliza = NO;
        self.btnSelectBas.userInteractionEnabled = YES;
        [self initCotizador];
        [self.cotizador calcularCoberturas];
        [self.cotizador reloadSuma];
        [self recalculoConBotonSeleccionado:YES];
//        if ( [self.cmbFormaDeCalculo.titleLabel.text isEqualToString:@"Prima"]) {
//            
//        }
        if (![self.cmbFormaDeCalculo.titleLabel.text isEqualToString:@"Prima"]) {
            [self.cotizador validationSumaAseguradaOpcionInclusiones];
        }
    }
}

-(IBAction)conDevolucionSelected:(id)sender {
    if (self.conDevolucion.selected == NO) {
        self.conDevolucion.selected = YES;
        self.sinDevolucion.selected = NO;
        [self calculateAge];
        self.cotizador.esConDevolucion = YES;
        [self initCotizador];
        [self.cotizador calcularCoberturas];
        [self.cotizador reloadSuma];
        [self recalculoConBotonSeleccionado:YES];
//        if ( [self.cmbFormaDeCalculo.titleLabel.text isEqualToString:@"Prima"]) {
//            
//        }
    }
}
-(IBAction)sinDevolcionSelected:(id)sender {
    if (self.sinDevolucion.selected == NO) {
        self.sinDevolucion.selected = YES;
        self.conDevolucion.selected = NO;
        [self calculateAge];
        self.cotizador.esConDevolucion = NO;
        [self initCotizador];
        [self.cotizador calcularCoberturas];
        [self.cotizador reloadSuma];
        [self recalculoConBotonSeleccionado:YES];
//        if ( [self.cmbFormaDeCalculo.titleLabel.text isEqualToString:@"Prima"]) {
//            
//        }
    }
}

-(IBAction)fumaSelected:(id)sender {
    if (self.fuma.selected == NO) {
        self.fuma.selected = YES;
        self.noFuma.selected = NO;
    }
    [self optionsSelected];
}

-(IBAction)noFumaSelected:(id)sender {
    if (self.noFuma.selected == NO) {
        self.noFuma.selected = YES;
        self.fuma.selected = NO;
    } 
    [self optionsSelected];
}

-(IBAction)hombreSelected:(id)sender {
    if (self.hombre.selected == NO) {
        self.mujer.selected = NO;
        self.hombre.selected = YES;
        self.lblSexo.text=@"F";
    } 
    [self optionsSelected];
}

-(IBAction)mujerSelected:(id)sender
{
    if(self.mujer.selected == NO) {
        self.hombre.selected = NO;
        self.mujer.selected = YES;
        self.lblSexo.text=@"M";
    } 
    [self optionsSelected];
}

-(void)optionsSelected{
    
    [self calculateAge];
    [self selectOcupacion];
    [self initCotizador];
    
}

#pragma mark - datos Show Method ******************************************************************

-(IBAction)tapDatos:(id)sender {
    self.popUpDatos.hidden = NO;
    [self pulse:self.popUpDatos];
}

-(IBAction)tapPrima:(id)sender{
    self.popUpPrima.hidden = NO;
    [self pulse:self.popUpPrima];
}

-(IBAction)exitPopUpPrima:(id)sender{
    self.popUpPrima.hidden = YES;
     [self.prima resignFirstResponder];
}

-(IBAction)exitPopUp:(id)sender {
    self.popUpDatos.hidden = YES;
}

- (void)paneMenuTapped:(UIButton*)sender{
    [[SCManager sharedInstance] showPaneMenu];
}


#pragma mark - Beneficios Selected Methods ****************************************************************

-(IBAction)BASSelected:(id)sender {
    [self metodoGenericoParaBoton:sender];
}

-(IBAction)BITSelected:(id)sender{
    
    if (self.btnSelectBas.selected == YES || self.btnSelectGfa.selected == YES || self.SelectCP.selected == YES) {
        [self metodoGenericoParaBoton:sender];
    }
}

-(IBAction)CIISelected:(id)sender{
    [self metodoGenericoParaBoton:sender];
    [self.cotizador CII_Validation];
 
    
}
-(IBAction)CMASelected:(id)sender{
    if (![(UIButton *)sender isSelected])
        self.btnSelectTima.selected = NO;
    [self metodoGenericoParaBoton:sender];
    [self.cotizador CMA_Validation];
   
}

-(IBAction)TIBASelected:(id)sender {
    if (![(UIButton *)sender isSelected])
        self.btnSelectCma.selected = NO;
    [self metodoGenericoParaBoton:sender];
    [self.cotizador TIBA_Validation];
 
}

-(IBAction)CATSelected:(id)sender {
    [self metodoGenericoParaBoton:sender];
    [self.cotizador CAT_Validation];

    
}
-(IBAction)GFASelected:(id)sender {
    
    [self metodoGenericoParaBoton:sender];
    [self.cotizador gfaTouched];
   
}
-(IBAction)GESelected:(id)sender {
    [self metodoGenericoParaBoton:sender];
    if ([[self.cotizador.coberturas objectAtIndex:12] isSelected] && ![[self.cotizador.coberturas objectAtIndex:7] isSelected]) {
        [self recalculoConBotonSeleccionado:YES];
        [self recalculoConBotonSeleccionado:NO];
    }
}

-(IBAction)enableBeneficiosConyuge:(id)sender {
    UIButton *button = sender;
    [button setSelected:!button.isSelected];
}

-(IBAction)BACYSelected:(id)sender {
    [self metodoGenericoParaBoton:sender];
    if (!self.cotizador.esPrima) {
        [self.cotizador evaluateChangeInSumaAsegurada:[[self.cotizador.coberturas objectAtIndex:8] sumaAsegurada] index:8 editandoTextField:NO];
    }
    [self.cotizador bacySelected];
}

-(IBAction)GFCSelected:(id)sender {
    [self metodoGenericoParaBoton:sender];
    [self.cotizador gfcTouched];
}

-(IBAction)CPSelected:(id)sender {
    [self metodoGenericoParaBoton:sender];
     [self.cotizador CP_Validation];
}

-(void)selectGFH {
    self.txtSelectSGFH.enabled = YES;
    [self recalculoConBotonSeleccionado:self.comboHijos.index != 0];
    [self.cotizador gfhTouched];
}


-(IBAction)CCSelected:(id)sender {
    [self metodoGenericoParaBoton:sender];
    if (![[self.cotizador.coberturas objectAtIndex:12] isSelected] && [[self.cotizador.coberturas objectAtIndex:7] isSelected]) {
        [self recalculoConBotonSeleccionado:YES];
        [self recalculoConBotonSeleccionado:NO];
    }
}

-(IBAction)cancerPlus1:(id)sender {
    [self metodoGenericoParaBoton:sender];
     [self.cotizador CP1_Validation];
}

-(IBAction)cancerPlus2:(id)sender {
    [self metodoGenericoParaBoton:sender];
         [self.cotizador CP2_Validation];
}
-(IBAction)cancerPlus3:(id)sender {
    [self metodoGenericoParaBoton:sender];
         [self.cotizador CP3_Validation];
}

- (void)metodoGenericoParaBoton:(id)sender
{
    UIButton *button = sender;
    [button setSelected:!button.isSelected];
    [self recalculoConBotonSeleccionado:button.isSelected];
    
}

- (void)recalculoConBotonSeleccionado:(BOOL)seleccionado
{
    [self initCotizador];
    [self edadConyugueValidation];
    if (self.incrusiones.selected == YES) {
        if (!seleccionado)
        {
            [self.cotizador validationSumaAseguradaOpcionInclusionesSeleccionBeneficio];
        }
    }
    if (!self.cotizador.esPrima) {
        [self.cotizador evaluateChangeInSumaAsegurada:[[self.cotizador.coberturas objectAtIndex:8] sumaAsegurada] index:8 editandoTextField:NO];
    }
    
    [self.cotizador reloadSumaAseguradaSinGastosFunerarios];
    [self.cotizador validations];
    [self cancerValidation];
   
}

-(void)edadConyugueValidation{
    
    if ([self.txtSelectEdadConyuge.text integerValue] > 65){
        self.txtSelectCancerPlus.enabled = NO;
        self.SelectCP.enabled =  NO;
        
        if ([self.txtSelectEdadConyuge.text integerValue] > 70){
            self.txtSelectEdadConyuge.text = @"70";
            self.txtSelectEdadConyuge.textColor = [UIColor blackColor];
        }
    } else {
        self.txtSelectCancerPlus.enabled = YES;
        self.SelectCP.enabled =  YES;
    }
}

#pragma mark - Change coberturas ***********************************************************************

-(void)cancerValidation {
    
    if ([self.cmbProfession.titleLabel.text isEqualToString:@"Actor"]) {
    }else{
        if ([self.txtSelectEdadConyuge.text integerValue] > 65) {
            self.txtSelectCancerPlus.enabled = NO;
            if (self.SelectCP.isSelected) {
                self.SelectCP.selected =  NO;
            }
            self.SelectCP.enabled =  NO;
            if ([self.txtSelectEdadConyuge.text integerValue] > 70) {
                self.txtSelectEdadConyuge.text = @"70";
                self.txtSelectEdadConyuge.textColor = [UIColor blackColor];
            }
        } else {
            self.txtSelectCancerPlus.enabled = YES;
            self.SelectCP.enabled = !(self.btnCancerPLus1.selected && self.btnCancerPlus2.selected && self.btnCancerPlus3.selected);
        }
        self.btnCancerPlus3.enabled = !(self.SelectCP.selected && self.btnCancerPLus1.selected && self.btnCancerPlus2.selected);
        self.btnCancerPlus2.enabled = !(self.SelectCP.selected && self.btnCancerPLus1.selected && self.btnCancerPlus3.selected);
        self.btnCancerPLus1.enabled = !(self.SelectCP.selected && self.btnCancerPlus2.selected && self.btnCancerPlus3.selected);
    }
}

-(void)validationGenero {
    if (self.hombre.selected == NO && self.mujer.selected == NO) {
        UIAlertView *alert = [[UIAlertView alloc]initWithTitle:@"Advertencia" message:@"Favor de seleccionar un genero" delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        [alert show];
    }
}

- (void)evaluateTitularRangeAge:(int)age {
    BOOL valid = YES;
    if (age < 15 || age > 70) {
        valid = NO;
    }
    [self changeTextColorOfTextField:self.realAge valid:valid];
}

- (void)evaluateCP1RangeAge:(int)age{
    [self evaluateAge:age limit:65 textField:self.txtSelectAgeCancerPLus1];
}

- (void)evaluateCP2RangeAge:(int)age {
    [self evaluateAge:age limit:65 textField:self.txtSelectAgeCancerPLus2];
}

- (void)evaluateCP3RangeAge:(int)age {
    [self evaluateAge:age limit:65 textField:self.txtSelectAgeCancerPLus3];
}

- (void)evaluateCCRangeAge:(int)age {
    [self evaluateAge:age limit:70 textField:self.txtSelectAgeCoberturaComplementaria];
}

- (void)evaluateConyugeRangeAge:(int)age {
    [self evaluateAge:age limit:70 textField:self.txtSelectEdadConyuge];
}

- (void)evaluateAge:(int)age limit:(int)limit textField:(UITextField *)textField
{
    [self changeTextColorOfTextField:textField valid:age <= limit];
}


-(void)initCotizadorSinBacy{
    
        BOOL esMenorEdad = [self.realAge.text doubleValue] < 18;
        
        SCCobertura *coberturaBAS = [[SCCobertura alloc]initWithType:SCCoberturaTypeBAS];
        coberturaBAS.min = !self.esNuevaPoliza ? 0 : 30000;
        coberturaBAS.maxMenores = 3000000;
        coberturaBAS.maxMayores = 100000000;
        coberturaBAS.edad = [self.estimateAge.text intValue];
        coberturaBAS.edadReal = [self.realAge.text intValue];
        coberturaBAS.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
        coberturaBAS.sumaAsegurada = [self.txtSelectBas.text doubleValue];
        coberturaBAS.selected = self.btnSelectBas.isSelected;
        if (self.esNuevaPoliza) {
            coberturaBAS.selected = YES;
            coberturaBAS.enabled = NO;
        }
        coberturaBAS.limiteSumaAseguradaParaCotizadorPorPrima = -1;
        coberturaBAS.clasificacion = SCClasificacionCoberturaTitular;
        
        SCCobertura *coberturaBIT = [[SCCobertura alloc]initWithType:SCCoberturaTypeBIT];
        coberturaBIT.edad = [self.estimateAge.text intValue];
        coberturaBIT.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
        coberturaBIT.sumaAsegurada = [self.txtSelectBit.text doubleValue];
        coberturaBIT.selected = self.btnSelectBit.isSelected;
        coberturaBIT.limiteSumaAseguradaParaCotizadorPorPrima = -1;
        coberturaBIT.clasificacion = SCClasificacionCoberturaTitular;
        
        double valorBAS = coberturaBAS.sumaAsegurada;
        BOOL considerarBAS = self.esNuevaPoliza ? YES : NO;
        
        SCCobertura *coberturaCII = [[SCCobertura alloc]initWithType:SCCoberturaTypeCII];
        coberturaCII.min = !self.esNuevaPoliza ? 0 : 30000;
        coberturaCII.maxMenores = 3000000;
        coberturaCII.maxMayores = 6000000;
        coberturaCII.edad = [self.estimateAge.text intValue];
        coberturaCII.edadReal = [self.realAge.text intValue];
        coberturaCII.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
        coberturaCII.sumaAsegurada = [self.txtSelectCii.text doubleValue];
        coberturaCII.selected = self.btnSelectCii.isSelected;
        coberturaCII.valorLimiteConsiderandoBAS = valorBAS;
        coberturaCII.considerarBAS = considerarBAS;
        coberturaCII.limiteSumaAseguradaParaCotizadorPorPrima = esMenorEdad ? 3000000 : 6000000;
        coberturaCII.clasificacion = SCClasificacionCoberturaTitular;
        
        SCCobertura *coberturaCMA = [[SCCobertura alloc]initWithType:SCCoberturaTypeCMA];
        coberturaCMA.min = !self.esNuevaPoliza ? 0 : 30000;
        coberturaCMA.maxMenores = 3000000;
        coberturaCMA.maxMayores = 6000000;
        coberturaCMA.edad = [self.estimateAge.text intValue];
        coberturaCMA.edadReal = [self.realAge.text intValue];
        coberturaCMA.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
        coberturaCMA.sumaAsegurada = [self.txtSelectCma.text doubleValue];
        coberturaCMA.selected = self.btnSelectCma.isSelected;
        coberturaCMA.valorLimiteConsiderandoBAS = valorBAS;
        coberturaCMA.considerarBAS = considerarBAS;
        coberturaCMA.limiteSumaAseguradaParaCotizadorPorPrima = esMenorEdad ? 3000000 : 6000000;
        coberturaCMA.clasificacion = SCClasificacionCoberturaTitular;
        
        SCCobertura *coberturaTIBA = [[SCCobertura alloc]initWithType:SCCoberturaTypeTIBA];
        coberturaTIBA.min = !self.esNuevaPoliza ? 0 : 30000;
        coberturaTIBA.maxMenores = 3000000;
        coberturaTIBA.maxMayores = 6000000;
        coberturaTIBA.edad = [self.estimateAge.text intValue];
        coberturaTIBA.edadReal = [self.realAge.text intValue];
        coberturaTIBA.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
        coberturaTIBA.sumaAsegurada = [self.txtSelectTima.text doubleValue];
        coberturaTIBA.selected = self.btnSelectTima.isSelected;
        coberturaTIBA.valorLimiteConsiderandoBAS = valorBAS;
        coberturaTIBA.considerarBAS = considerarBAS;
        coberturaTIBA.limiteSumaAseguradaParaCotizadorPorPrima = esMenorEdad ? 3000000 : 6000000;
        coberturaTIBA.clasificacion = SCClasificacionCoberturaTitular;
        
        SCCobertura *coberturaCAT = [[SCCobertura alloc]initWithType:SCCoberturaTypeCAT];
        coberturaCAT.min = !self.esNuevaPoliza ? 0 : 30000;
        coberturaCAT.maxMenores = 1000000;
        coberturaCAT.maxMayores = 1000000;
        coberturaCAT.edad = [self.realAge.text intValue];
        coberturaCAT.edadReal = [self.realAge.text intValue];
        coberturaCAT.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
        coberturaCAT.sumaAsegurada = [self.txtSelectCat.text doubleValue];
        coberturaCAT.selected = self.btnSelectCat.isSelected;
        coberturaCAT.valorLimiteConsiderandoBAS = valorBAS;
        coberturaCAT.considerarBAS = considerarBAS;
        coberturaCAT.limiteSumaAseguradaParaCotizadorPorPrima = 1000000;
        coberturaCAT.clasificacion = SCClasificacionCoberturaTitular;
        
        SCCobertura *coberturaGFA = [[SCCobertura alloc]initWithType:SCCoberturaTypeGFA];
        coberturaGFA.min = !self.esNuevaPoliza ? 0 : 34476;
        coberturaGFA.maxMenores = 180000;
        coberturaGFA.maxMayores = 180000;
        coberturaGFA.edad = [self.estimateAge.text intValue];
        coberturaGFA.edadReal = [self.realAge.text intValue];
        coberturaGFA.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
        coberturaGFA.sumaAsegurada = [self.txtSelectGfa.text doubleValue];
        coberturaGFA.selected = self.btnSelectGfa.isSelected;
        coberturaGFA.limiteSumaAseguradaParaCotizadorPorPrima = 180000;
        coberturaGFA.clasificacion = SCClasificacionCoberturaTitular;
        
        SCCobertura *coberturaGE = [[SCCobertura alloc]initWithType:SCCoberturaTypeGE];
        coberturaGE.min = !self.esNuevaPoliza ? 0 : 10000;
        coberturaGE.maxMenores = considerarBAS ? valorBAS : 3000000;
        coberturaGE.maxMayores = considerarBAS ? valorBAS : 100000000;
        coberturaGE.edad = [self.estimateAge.text intValue];
        coberturaGE.edadReal = [self.realAge.text intValue];
        coberturaGE.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
        coberturaGE.sumaAsegurada = [self.txtSelectGe.text doubleValue];
        coberturaGE.selected = self.btnSelectGe.isSelected;
        coberturaGE.valorLimiteConsiderandoBAS = valorBAS;
        coberturaGE.considerarBAS = considerarBAS;
        coberturaGE.limiteSumaAseguradaParaCotizadorPorPrima = esMenorEdad ? 3000000 : 100000000;
        coberturaGE.clasificacion = SCClasificacionCoberturaTitular;
        
        SCCobertura *coberturaBACY = [[SCCobertura alloc]initWithType:SCCoberturaTypeBACY];
        coberturaBACY.min = !self.esNuevaPoliza ? 0 : 30000;
        coberturaBACY.maxMenores = 344760;
        coberturaBACY.maxMayores = 344760;
        coberturaBACY.edad = [self.txtSelectEdadConyuge.text intValue];
        coberturaBACY.edadReal = [self.realAge.text intValue];
        coberturaBACY.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
        coberturaBACY.sumaAsegurada = [self.txtSelectcoberturaConyuge.text doubleValue];
        coberturaBACY.selected = self.btnSelectBacy.isSelected;
        coberturaBACY.valorLimiteConsiderandoBAS = 344760;
        coberturaBACY.considerarBAS = considerarBAS;
        coberturaBACY.limiteSumaAseguradaParaCotizadorPorPrima = 344760;
        coberturaBACY.clasificacion = SCClasificacionCoberturaConyugue;
        
        SCCobertura *coberturaGFC = [[SCCobertura alloc]initWithType:SCCoberturaTypeGFC];
        coberturaGFC.min = !self.esNuevaPoliza ? 0 : 34476;
        coberturaGFC.maxMenores = 180000;
        coberturaGFC.maxMayores = 180000;
        coberturaGFC.edad = [self.txtSelectEdadConyuge.text intValue];
        coberturaGFC.edadReal = [self.realAge.text intValue];
        coberturaGFC.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
        coberturaGFC.sumaAsegurada = [self.txtSelectGFC.text doubleValue];
        coberturaGFC.selected = self.SelectGFC.isSelected;
        coberturaGFC.limiteSumaAseguradaParaCotizadorPorPrima = 180000;
        coberturaGFC.clasificacion = SCClasificacionCoberturaConyugue;
        
        SCCobertura *coberturaCP = [[SCCobertura alloc]initWithType:SCCoberturaTypeCP];
        coberturaCP.min = !self.esNuevaPoliza ? 0 : 30000;
        coberturaCP.maxMenores = 1000000;
        coberturaCP.maxMayores = 1000000;
        coberturaCP.edad = [self.txtSelectEdadConyuge.text intValue];
        coberturaCP.edadReal = [self.realAge.text intValue];
        coberturaCP.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
        coberturaCP.sumaAsegurada = [self.txtSelectCancerPlus.text doubleValue];
        coberturaCP.selected = self.SelectCP.isSelected;
        coberturaCP.valorLimiteConsiderandoBAS = valorBAS;
        coberturaCP.considerarBAS = considerarBAS;
        coberturaCP.limiteSumaAseguradaParaCotizadorPorPrima = 1000000;
        coberturaCP.clasificacion = SCClasificacionCoberturaConyugue;
        
        SCCobertura *coberturaGFH = [[SCCobertura alloc]initWithType:SCCoberturaTypeGFH];
        coberturaGFH.min = !self.esNuevaPoliza ? 0 : 34476;
        coberturaGFH.maxMenores = 180000;
        coberturaGFH.maxMayores = 180000;
        coberturaGFH.edad = [self.estimateAge.text intValue];
        coberturaGFH.edadReal = [self.realAge.text intValue];
        coberturaGFH.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
        coberturaGFH.sumaAsegurada = [self.txtSelectSGFH.text doubleValue];
        coberturaGFH.selected = self.comboHijos.index == 0 ? NO : YES;
        coberturaGFH.GFHDescription = self.cmbGFH.titleLabel.text;
        coberturaGFH.limiteSumaAseguradaParaCotizadorPorPrima = 180000;
        coberturaGFH.clasificacion = SCClasificacionCoberturaHijos;
        
        SCCobertura *coberturaCC = [[SCCobertura alloc]initWithType:SCCoberturaTypeCC];
        if (self.esNuevaPoliza) {
            coberturaCC.min =  30000;
            coberturaCC.edadReal = [self.txtSelectAgeCoberturaComplementaria.text intValue];
            coberturaCC.edad = [self.txtSelectAgeCoberturaComplementaria.text intValue];
            coberturaCC.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
            coberturaCC.sumaAsegurada = [self.txtSelectCoberturaComplementaria.text doubleValue];
            coberturaCC.selected = self.btnSelectCC.isSelected;
            coberturaCC.valorLimiteConsiderandoBAS = valorBAS;
            coberturaCC.considerarBAS = YES;
            
            if (coberturaCC.edadReal < 18) {
                coberturaCC.limiteSumaAseguradaParaCotizadorPorPrima = 3000000;
                coberturaCC.maxMenores =  3000000;
                coberturaCC.maxMayores = 3000000;
                
            } else {
                coberturaCC.limiteSumaAseguradaParaCotizadorPorPrima = 100000000;
                coberturaCC.maxMenores =  100000000;
                coberturaCC.maxMayores =  100000000;
                
            }
            coberturaCC.clasificacion = SCClasificacionCoberturaComplementaria;
        }else{
            coberturaCC.min = !self.esNuevaPoliza ? 0 : 30000;
            coberturaCC.maxMenores = considerarBAS ? valorBAS : 3000000;
            coberturaCC.maxMayores = considerarBAS ? valorBAS : 100000000;
            coberturaCC.edad = [self.txtSelectAgeCoberturaComplementaria.text intValue];
            coberturaCC.edadReal = [self.txtSelectAgeCoberturaComplementaria.text intValue];
            coberturaCC.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
            coberturaCC.sumaAsegurada = [self.txtSelectCoberturaComplementaria.text doubleValue];
            coberturaCC.selected = self.btnSelectCC.isSelected;
            coberturaCC.valorLimiteConsiderandoBAS = valorBAS;
            coberturaCC.considerarBAS = considerarBAS;
            if (coberturaCC.edadReal < 18) {
                coberturaCC.limiteSumaAseguradaParaCotizadorPorPrima = 3000000;
            } else {
                coberturaCC.limiteSumaAseguradaParaCotizadorPorPrima = 100000000;
            }
            coberturaCC.clasificacion = SCClasificacionCoberturaComplementaria;
            
        }
        SCCobertura *coberturaCP1 = [[SCCobertura alloc]initWithType:SCCoberturaTypeCP1];
        coberturaCP1.min = !self.esNuevaPoliza ? 0 : 30000;
        coberturaCP1.maxMenores = 1000000;
        coberturaCP1.maxMayores = 1000000;
        coberturaCP1.edad = [self.txtSelectAgeCancerPLus1.text intValue];
        coberturaCP1.edadReal = [self.realAge.text intValue];
        coberturaCP1.sexCP = self.cbbCancerPlus1.titleLabel.text;
        coberturaCP1.sumaAsegurada = [self.txtSelectCancerPLus1.text doubleValue];
        coberturaCP1.selected = self.btnCancerPLus1.isSelected;
        coberturaCP1.valorLimiteConsiderandoBAS = valorBAS;
        coberturaCP1.considerarBAS = considerarBAS;
        coberturaCP1.limiteSumaAseguradaParaCotizadorPorPrima = 1000000;
        coberturaCP1.clasificacion = SCClasificacionCancerPlus1;
        
        SCCobertura *coberturaCP2 = [[SCCobertura alloc]initWithType:SCCoberturaTypeCP2];
        coberturaCP2.min = !self.esNuevaPoliza ? 0 : 30000;
        coberturaCP2.maxMenores = 1000000;
        coberturaCP2.maxMayores = 1000000;
        coberturaCP2.edad = [self.txtSelectAgeCancerPLus2.text intValue];
        coberturaCP2.edadReal = [self.realAge.text intValue];
        coberturaCP2.sexCP = self.cbbCancerPlus2.titleLabel.text;
        coberturaCP2.sumaAsegurada =  [self.txtSelectCancerPLus2.text doubleValue];
        coberturaCP2.selected = self.btnCancerPlus2.isSelected;
        coberturaCP2.valorLimiteConsiderandoBAS = valorBAS;
        coberturaCP2.considerarBAS = considerarBAS;
        coberturaCP2.limiteSumaAseguradaParaCotizadorPorPrima = 1000000;
        coberturaCP2.clasificacion = SCClasificacionCancerPlus2;
        
        SCCobertura *coberturaCP3 = [[SCCobertura alloc]initWithType:SCCoberturaTypeCP3];
        coberturaCP3.min = !self.esNuevaPoliza ? 0 : 30000;
        coberturaCP3.maxMenores = 1000000;
        coberturaCP3.maxMayores = 1000000;
        coberturaCP3.edad = [self.txtSelectAgeCancerPLus3.text intValue];
        coberturaCP3.edadReal = [self.realAge.text intValue];
        coberturaCP3.sexCP =  self.cbbCancerPlus3.titleLabel.text;
        coberturaCP3.sumaAsegurada =  [self.txtSelectCancerPLus3.text doubleValue];
        coberturaCP3.selected = self.btnCancerPlus3.isSelected;
        coberturaCP3.valorLimiteConsiderandoBAS = valorBAS;
        coberturaCP3.considerarBAS = considerarBAS;
        coberturaCP3.limiteSumaAseguradaParaCotizadorPorPrima = 1000000;
        coberturaCP3.clasificacion = SCClasificacionCancerPlus3;
        
        NSArray *coberturas = @[coberturaBAS,
                                coberturaBIT,
                                coberturaCII,
                                coberturaCMA,
                                coberturaTIBA,
                                coberturaCAT,
                                coberturaGFA,
                                coberturaGE,
                                coberturaBACY,
                                coberturaGFC,
                                coberturaCP,
                                coberturaGFH,
                                coberturaCC,
                                coberturaCP1,
                                coberturaCP2,
                                coberturaCP3];
        
        BOOL isPrima;
        
        if ( [self.cmbFormaDeCalculo.titleLabel.text isEqualToString:@"Prima"]) {
            isPrima = YES;
        } else {
            isPrima = NO;
        }
        self.cotizador = [[SCCotizador alloc]initWithCoberturas:coberturas prima:isPrima];
        self.cotizador.delegate = self;
        self.cotizador.calculo = self.cmbFormaDeCalculo.titleLabel.text;
        self.cotizador.pago = self.pago;
        self.cotizador.esConDevolucion = self.conDevolucion.selected;
        self.cotizador.primaExcedenteAnualPorPrima = [self.primaExedenteAnual.text doubleValue];
        if (isPrima) {
            double Prima = [self.prima.text doubleValue];
            if ([self.pago isEqualToString:@"Anual"]) {
                Prima = Prima * 1;
            } else if ([self.pago isEqualToString:@"Mensual"]) {
                Prima = Prima * 12;
            } else if ([self.pago isEqualToString:@"Quincenal"]) {
                Prima = Prima * 24;
            }
            self.cotizador.primaTotalCotizadorPrima = Prima;
            if (self.cotizador.primaTotalCotizadorPrima == 0) {
                self.cotizador.primaTotalCotizadorPrima = 12000;
                self.prima.text = @"12000.00";
            }
            [self cotizador:nil didGetSumaAseguradaTotal:self.cotizador.primaTotalCotizadorPrima];
        }
        
        self.cotizador.esNuevaPoliza = self.esNuevaPoliza;
        self.cotizador.estimateAge = [self.estimateAge.text intValue];
        self.cotizador.realAge = [self.realAge.text intValue];
        [self selectOcupacion];
        
        [self.cotizador calcularCoberturas];
        
        if (!isPrima) {[self.cotizador GastosFunerariosEnabledValidation];
            [self.cotizador validateGastosFunerariosSumaAsegurada];
            [self.cotizador calcularBIT];
            [self cotizador:self.cotizador didUpdateCobertura:[self.cotizador.coberturas objectAtIndex:1]];
            [self cancerValidation];
            if (sumaAseguradaBas > sumaAseguradaTextField) {
                [self.cotizador reloadSumaAsegurada];
            }
            
        }
    }

-(void)initCotizador {
    
    BOOL esMenorEdad = [self.realAge.text doubleValue] < 18;
    
    SCCobertura *coberturaBAS = [[SCCobertura alloc]initWithType:SCCoberturaTypeBAS];
    coberturaBAS.min = !self.esNuevaPoliza ? 0 : 30000;
    coberturaBAS.maxMenores = 3000000;
    coberturaBAS.maxMayores = 100000000;
    coberturaBAS.edad = [self.estimateAge.text intValue];
    coberturaBAS.edadReal = [self.realAge.text intValue];
    coberturaBAS.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
    coberturaBAS.sumaAsegurada = [self.txtSelectBas.text doubleValue];
    coberturaBAS.selected = self.btnSelectBas.isSelected;
    if (self.esNuevaPoliza) {
        coberturaBAS.selected = YES;
        coberturaBAS.enabled = NO;
    }
    coberturaBAS.limiteSumaAseguradaParaCotizadorPorPrima = -1;
    coberturaBAS.clasificacion = SCClasificacionCoberturaTitular;
    
    SCCobertura *coberturaBIT = [[SCCobertura alloc]initWithType:SCCoberturaTypeBIT];
    coberturaBIT.edad = [self.estimateAge.text intValue];
    coberturaBIT.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
    coberturaBIT.sumaAsegurada = [self.txtSelectBit.text doubleValue];
    coberturaBIT.selected = self.btnSelectBit.isSelected;
    coberturaBIT.limiteSumaAseguradaParaCotizadorPorPrima = -1;
    coberturaBIT.clasificacion = SCClasificacionCoberturaTitular;
    
    double valorBAS = coberturaBAS.sumaAsegurada;
    BOOL considerarBAS = self.esNuevaPoliza ? YES : NO;
    
    SCCobertura *coberturaCII = [[SCCobertura alloc]initWithType:SCCoberturaTypeCII];
    coberturaCII.min = !self.esNuevaPoliza ? 0 : 30000;
    coberturaCII.maxMenores = 3000000;
    coberturaCII.maxMayores = 6000000;
    coberturaCII.edad = [self.estimateAge.text intValue];
    coberturaCII.edadReal = [self.realAge.text intValue];
    coberturaCII.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
    coberturaCII.sumaAsegurada = [self.txtSelectCii.text doubleValue];
    coberturaCII.selected = self.btnSelectCii.isSelected;
    coberturaCII.valorLimiteConsiderandoBAS = valorBAS;
    coberturaCII.considerarBAS = considerarBAS;
    coberturaCII.limiteSumaAseguradaParaCotizadorPorPrima = esMenorEdad ? 3000000 : 6000000;
    coberturaCII.clasificacion = SCClasificacionCoberturaTitular;
    
    SCCobertura *coberturaCMA = [[SCCobertura alloc]initWithType:SCCoberturaTypeCMA];
    coberturaCMA.min = !self.esNuevaPoliza ? 0 : 30000;
    coberturaCMA.maxMenores = 3000000;
    coberturaCMA.maxMayores = 6000000;
    coberturaCMA.edad = [self.estimateAge.text intValue];
    coberturaCMA.edadReal = [self.realAge.text intValue];
    coberturaCMA.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
    coberturaCMA.sumaAsegurada = [self.txtSelectCma.text doubleValue];
    coberturaCMA.selected = self.btnSelectCma.isSelected;
    coberturaCMA.valorLimiteConsiderandoBAS = valorBAS;
    coberturaCMA.considerarBAS = considerarBAS;
    coberturaCMA.limiteSumaAseguradaParaCotizadorPorPrima = esMenorEdad ? 3000000 : 6000000;
    coberturaCMA.clasificacion = SCClasificacionCoberturaTitular;
    
    SCCobertura *coberturaTIBA = [[SCCobertura alloc]initWithType:SCCoberturaTypeTIBA];
    coberturaTIBA.min = !self.esNuevaPoliza ? 0 : 30000;
    coberturaTIBA.maxMenores = 3000000;
    coberturaTIBA.maxMayores = 6000000;
    coberturaTIBA.edad = [self.estimateAge.text intValue];
    coberturaTIBA.edadReal = [self.realAge.text intValue];
    coberturaTIBA.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
    coberturaTIBA.sumaAsegurada = [self.txtSelectTima.text doubleValue];
    coberturaTIBA.selected = self.btnSelectTima.isSelected;
    coberturaTIBA.valorLimiteConsiderandoBAS = valorBAS;
    coberturaTIBA.considerarBAS = considerarBAS;
    coberturaTIBA.limiteSumaAseguradaParaCotizadorPorPrima = esMenorEdad ? 3000000 : 6000000;
    coberturaTIBA.clasificacion = SCClasificacionCoberturaTitular;
    
    SCCobertura *coberturaCAT = [[SCCobertura alloc]initWithType:SCCoberturaTypeCAT];
    coberturaCAT.min = !self.esNuevaPoliza ? 0 : 30000;
    coberturaCAT.maxMenores = 1000000;
    coberturaCAT.maxMayores = 1000000;
    coberturaCAT.edad = [self.realAge.text intValue];
    coberturaCAT.edadReal = [self.realAge.text intValue];
    coberturaCAT.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
    coberturaCAT.sumaAsegurada = [self.txtSelectCat.text doubleValue];
    coberturaCAT.selected = self.btnSelectCat.isSelected;
    coberturaCAT.valorLimiteConsiderandoBAS = valorBAS;
    coberturaCAT.considerarBAS = considerarBAS;
    coberturaCAT.limiteSumaAseguradaParaCotizadorPorPrima = 1000000;
    coberturaCAT.clasificacion = SCClasificacionCoberturaTitular;
    
    SCCobertura *coberturaGFA = [[SCCobertura alloc]initWithType:SCCoberturaTypeGFA];
    coberturaGFA.min = !self.esNuevaPoliza ? 0 : 34476;
    coberturaGFA.maxMenores = 180000;
    coberturaGFA.maxMayores = 180000;
    coberturaGFA.edad = [self.estimateAge.text intValue];
    coberturaGFA.edadReal = [self.realAge.text intValue];
    coberturaGFA.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
    coberturaGFA.sumaAsegurada = [self.txtSelectGfa.text doubleValue];
    coberturaGFA.selected = self.btnSelectGfa.isSelected;
    coberturaGFA.limiteSumaAseguradaParaCotizadorPorPrima = 180000;
    coberturaGFA.clasificacion = SCClasificacionCoberturaTitular;
    
    SCCobertura *coberturaGE = [[SCCobertura alloc]initWithType:SCCoberturaTypeGE];
    coberturaGE.min = !self.esNuevaPoliza ? 0 : 10000;
    coberturaGE.maxMenores = considerarBAS ? valorBAS : 3000000;
    coberturaGE.maxMayores = considerarBAS ? valorBAS : 100000000;
    coberturaGE.edad = [self.estimateAge.text intValue];
    coberturaGE.edadReal = [self.realAge.text intValue];
    coberturaGE.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
    coberturaGE.sumaAsegurada = [self.txtSelectGe.text doubleValue];
    coberturaGE.selected = self.btnSelectGe.isSelected;
    coberturaGE.valorLimiteConsiderandoBAS = valorBAS;
    coberturaGE.considerarBAS = considerarBAS;
    coberturaGE.limiteSumaAseguradaParaCotizadorPorPrima = esMenorEdad ? 3000000 : 100000000;
    coberturaGE.clasificacion = SCClasificacionCoberturaTitular;
    
    SCCobertura *coberturaBACY = [[SCCobertura alloc]initWithType:SCCoberturaTypeBACY];
    coberturaBACY.min = !self.esNuevaPoliza ? 0 : 30000;
    coberturaBACY.maxMenores = 344760;
    coberturaBACY.maxMayores = 344760;
    coberturaBACY.edad = [self.txtSelectEdadConyuge.text intValue];
    coberturaBACY.edadReal = [self.realAge.text intValue];
    coberturaBACY.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
    coberturaBACY.sumaAsegurada = [self.txtSelectcoberturaConyuge.text doubleValue];
    coberturaBACY.selected = self.btnSelectBacy.isSelected;
    coberturaBACY.valorLimiteConsiderandoBAS = 344760;
    coberturaBACY.considerarBAS = considerarBAS;
    coberturaBACY.limiteSumaAseguradaParaCotizadorPorPrima = 344760;
    coberturaBACY.clasificacion = SCClasificacionCoberturaConyugue;
    
    SCCobertura *coberturaGFC = [[SCCobertura alloc]initWithType:SCCoberturaTypeGFC];
    coberturaGFC.min = !self.esNuevaPoliza ? 0 : 34476;
    coberturaGFC.maxMenores = 180000;
    coberturaGFC.maxMayores = 180000;
    coberturaGFC.edad = [self.txtSelectEdadConyuge.text intValue];
    coberturaGFC.edadReal = [self.realAge.text intValue];
    coberturaGFC.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
    coberturaGFC.sumaAsegurada = [self.txtSelectGFC.text doubleValue];
    coberturaGFC.selected = self.SelectGFC.isSelected;
    coberturaGFC.limiteSumaAseguradaParaCotizadorPorPrima = 180000;
    coberturaGFC.clasificacion = SCClasificacionCoberturaConyugue;
    
    SCCobertura *coberturaCP = [[SCCobertura alloc]initWithType:SCCoberturaTypeCP];
    coberturaCP.min = !self.esNuevaPoliza ? 0 : 30000;
    coberturaCP.maxMenores = 1000000;
    coberturaCP.maxMayores = 1000000;
    coberturaCP.edad = [self.txtSelectEdadConyuge.text intValue];
    coberturaCP.edadReal = [self.realAge.text intValue];
    coberturaCP.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
    coberturaCP.sumaAsegurada = [self.txtSelectCancerPlus.text doubleValue];
    coberturaCP.selected = self.SelectCP.isSelected;
    coberturaCP.valorLimiteConsiderandoBAS = valorBAS;
    coberturaCP.considerarBAS = considerarBAS;
    coberturaCP.limiteSumaAseguradaParaCotizadorPorPrima = 1000000;
    coberturaCP.clasificacion = SCClasificacionCoberturaConyugue;
    
    SCCobertura *coberturaGFH = [[SCCobertura alloc]initWithType:SCCoberturaTypeGFH];
    coberturaGFH.min = !self.esNuevaPoliza ? 0 : 34476;
    coberturaGFH.maxMenores = 180000;
    coberturaGFH.maxMayores = 180000;
    coberturaGFH.edad = [self.estimateAge.text intValue];
    coberturaGFH.edadReal = [self.realAge.text intValue];
    coberturaGFH.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
    coberturaGFH.sumaAsegurada = [self.txtSelectSGFH.text doubleValue];
    coberturaGFH.selected = self.comboHijos.index == 0 ? NO : YES;
    coberturaGFH.GFHDescription = self.cmbGFH.titleLabel.text;
    coberturaGFH.limiteSumaAseguradaParaCotizadorPorPrima = 180000;
    coberturaGFH.clasificacion = SCClasificacionCoberturaHijos;
    
    SCCobertura *coberturaCC = [[SCCobertura alloc]initWithType:SCCoberturaTypeCC];
    if (self.esNuevaPoliza) {
        coberturaCC.min =  30000;
        coberturaCC.edadReal = [self.txtSelectAgeCoberturaComplementaria.text intValue];
        coberturaCC.edad = [self.txtSelectAgeCoberturaComplementaria.text intValue];
        coberturaCC.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
        coberturaCC.sumaAsegurada = [self.txtSelectCoberturaComplementaria.text doubleValue];
        coberturaCC.selected = self.btnSelectCC.isSelected;
        coberturaCC.valorLimiteConsiderandoBAS = valorBAS;
        coberturaCC.considerarBAS = YES;
        
        if (coberturaCC.edadReal < 18) {
            coberturaCC.limiteSumaAseguradaParaCotizadorPorPrima = 3000000;
            coberturaCC.maxMenores =  3000000;
            coberturaCC.maxMayores = 3000000;
            
        } else {
            coberturaCC.limiteSumaAseguradaParaCotizadorPorPrima = 100000000;
            coberturaCC.maxMenores =  100000000;
            coberturaCC.maxMayores =  100000000;
            
        }
        coberturaCC.clasificacion = SCClasificacionCoberturaComplementaria;
    }else{
        coberturaCC.min = !self.esNuevaPoliza ? 0 : 30000;
        coberturaCC.maxMenores = considerarBAS ? valorBAS : 3000000;
        coberturaCC.maxMayores = considerarBAS ? valorBAS : 100000000;
        coberturaCC.edad = [self.txtSelectAgeCoberturaComplementaria.text intValue];
        coberturaCC.edadReal = [self.txtSelectAgeCoberturaComplementaria.text intValue];
        coberturaCC.sexo = self.hombre.selected ? SCSexHombre: SCSexMujer;
        coberturaCC.sumaAsegurada = [self.txtSelectCoberturaComplementaria.text doubleValue];
        coberturaCC.selected = self.btnSelectCC.isSelected;
        coberturaCC.valorLimiteConsiderandoBAS = valorBAS;
        coberturaCC.considerarBAS = considerarBAS;
        if (coberturaCC.edadReal < 18) {
            coberturaCC.limiteSumaAseguradaParaCotizadorPorPrima = 3000000;
        } else {
            coberturaCC.limiteSumaAseguradaParaCotizadorPorPrima = 100000000;
        }
        coberturaCC.clasificacion = SCClasificacionCoberturaComplementaria;
        
    }
    SCCobertura *coberturaCP1 = [[SCCobertura alloc]initWithType:SCCoberturaTypeCP1];
    coberturaCP1.min = !self.esNuevaPoliza ? 0 : 30000;
    coberturaCP1.maxMenores = 1000000;
    coberturaCP1.maxMayores = 1000000;
    coberturaCP1.edad = [self.txtSelectAgeCancerPLus1.text intValue];
    coberturaCP1.edadReal = [self.realAge.text intValue];
    coberturaCP1.sexCP = self.cbbCancerPlus1.titleLabel.text;
    coberturaCP1.sumaAsegurada = [self.txtSelectCancerPLus1.text doubleValue];
    coberturaCP1.selected = self.btnCancerPLus1.isSelected;
    coberturaCP1.valorLimiteConsiderandoBAS = valorBAS;
    coberturaCP1.considerarBAS = considerarBAS;
    coberturaCP1.limiteSumaAseguradaParaCotizadorPorPrima = 1000000;
    coberturaCP1.clasificacion = SCClasificacionCancerPlus1;
    
    SCCobertura *coberturaCP2 = [[SCCobertura alloc]initWithType:SCCoberturaTypeCP2];
    coberturaCP2.min = !self.esNuevaPoliza ? 0 : 30000;
    coberturaCP2.maxMenores = 1000000;
    coberturaCP2.maxMayores = 1000000;
    coberturaCP2.edad = [self.txtSelectAgeCancerPLus2.text intValue];
    coberturaCP2.edadReal = [self.realAge.text intValue];
    coberturaCP2.sexCP = self.cbbCancerPlus2.titleLabel.text;
    coberturaCP2.sumaAsegurada =  [self.txtSelectCancerPLus2.text doubleValue];
    coberturaCP2.selected = self.btnCancerPlus2.isSelected;
    coberturaCP2.valorLimiteConsiderandoBAS = valorBAS;
    coberturaCP2.considerarBAS = considerarBAS;
    coberturaCP2.limiteSumaAseguradaParaCotizadorPorPrima = 1000000;
    coberturaCP2.clasificacion = SCClasificacionCancerPlus2;
    
    SCCobertura *coberturaCP3 = [[SCCobertura alloc]initWithType:SCCoberturaTypeCP3];
    coberturaCP3.min = !self.esNuevaPoliza ? 0 : 30000;
    coberturaCP3.maxMenores = 1000000;
    coberturaCP3.maxMayores = 1000000;
    coberturaCP3.edad = [self.txtSelectAgeCancerPLus3.text intValue];
    coberturaCP3.edadReal = [self.realAge.text intValue];
    coberturaCP3.sexCP =  self.cbbCancerPlus3.titleLabel.text;
    coberturaCP3.sumaAsegurada =  [self.txtSelectCancerPLus3.text doubleValue];
    coberturaCP3.selected = self.btnCancerPlus3.isSelected;
    coberturaCP3.valorLimiteConsiderandoBAS = valorBAS;
    coberturaCP3.considerarBAS = considerarBAS;
    coberturaCP3.limiteSumaAseguradaParaCotizadorPorPrima = 1000000;
    coberturaCP3.clasificacion = SCClasificacionCancerPlus3;
    
    NSArray *coberturas = @[coberturaBAS,
                            coberturaBIT,
                            coberturaCII,
                            coberturaCMA,
                            coberturaTIBA,
                            coberturaCAT,
                            coberturaGFA,
                            coberturaGE,
                            coberturaBACY,
                            coberturaGFC,
                            coberturaCP,
                            coberturaGFH,
                            coberturaCC,
                            coberturaCP1,
                            coberturaCP2,
                            coberturaCP3];
    
    BOOL isPrima;
    
    if ( [self.cmbFormaDeCalculo.titleLabel.text isEqualToString:@"Prima"]) {
        isPrima = YES;
    } else {
        isPrima = NO;
    }
    self.cotizador = [[SCCotizador alloc]initWithCoberturas:coberturas prima:isPrima];
    self.cotizador.delegate = self;
    self.cotizador.calculo = self.cmbFormaDeCalculo.titleLabel.text;
    self.cotizador.pago = self.pago;
    self.cotizador.esConDevolucion = self.conDevolucion.selected;
    self.cotizador.primaExcedenteAnualPorPrima = [self.primaExedenteAnual.text doubleValue];
    if (isPrima) {
        double Prima = [self.prima.text doubleValue];
        if ([self.pago isEqualToString:@"Anual"]) {
            Prima = Prima * 1;
        } else if ([self.pago isEqualToString:@"Mensual"]) {
            Prima = Prima * 12;
        } else if ([self.pago isEqualToString:@"Quincenal"]) {
            Prima = Prima * 24;
        }
        self.cotizador.primaTotalCotizadorPrima = Prima;
        if (self.cotizador.primaTotalCotizadorPrima == 0) {
            self.cotizador.primaTotalCotizadorPrima = 12000;
            self.prima.text = @"12000.00";
        }
        [self cotizador:nil didGetSumaAseguradaTotal:self.cotizador.primaTotalCotizadorPrima];
    }
    
    self.cotizador.esNuevaPoliza = self.esNuevaPoliza;
    self.cotizador.estimateAge = [self.estimateAge.text intValue];
    self.cotizador.realAge = [self.realAge.text intValue];
    [self selectOcupacion];
    
    [self.cotizador calcularCoberturas];
    
    if (!isPrima) {[self.cotizador GastosFunerariosEnabledValidation];
    [self.cotizador validateGastosFunerariosSumaAsegurada];
    
    [self.cotizador sumaAseguradaBacy];
        [self.cotizador calcularBIT];
        [self cotizador:self.cotizador didUpdateCobertura:[self.cotizador.coberturas objectAtIndex:1]];
    [self cancerValidation];
    if (sumaAseguradaBas > sumaAseguradaTextField) {
        [self.cotizador reloadSumaAsegurada];
    }
        
    }
}

-(void)changeTextColorOfTextField:(UITextField *)textField valid:(BOOL)valid
{
    if (valid) {
        textField.textColor = [UIColor blackColor];
    } else {
        textField.textColor = [UIColor redColor];
    }
}

- (UITextField *)textFieldParaTipoCobertura:(SCCoberturaType)type
{
    UITextField *textFieldReturn = nil;
    switch (type) {
        case SCCoberturaTypeBAS: textFieldReturn = self.txtSelectBas; break;
        case SCCoberturaTypeBIT: textFieldReturn = self.txtSelectBit; break;
        case SCCoberturaTypeCII: textFieldReturn = self.txtSelectCii; break;
        case SCCoberturaTypeCMA: textFieldReturn = self.txtSelectCma; break;
        case SCCoberturaTypeTIBA: textFieldReturn = self.txtSelectTima; break;
        case SCCoberturaTypeCAT: textFieldReturn = self.txtSelectCat; break;
        case SCCoberturaTypeGFA: textFieldReturn = self.txtSelectGfa; break;
        case SCCoberturaTypeGE: textFieldReturn = self.txtSelectGe; break;
        case SCCoberturaTypeBACY: textFieldReturn = self.txtSelectcoberturaConyuge; break;
        case SCCoberturaTypeGFC: textFieldReturn = self.txtSelectGFC; break;
        case SCCoberturaTypeCP: textFieldReturn = self.txtSelectCancerPlus; break;
        case SCCoberturaTypeGFH: textFieldReturn = self.txtSelectSGFH; break;
        case SCCoberturaTypeCC: textFieldReturn = self.txtSelectCoberturaComplementaria; break;
        case SCCoberturaTypeCP1: textFieldReturn = self.txtSelectCancerPLus1; break;
        case SCCoberturaTypeCP2: textFieldReturn = self.txtSelectCancerPLus2; break;
        case SCCoberturaTypeCP3: textFieldReturn = self.txtSelectCancerPLus3; break;
        default: break;
    }
    return textFieldReturn;
}

#pragma mark - CotizadorDelegate

- (int)ocupacionActualParaCotizador:(SCCotizador *)cotizador
{
    return self.comboProfessions.index;
}

- (void)cotizador:(SCCotizador *)aCotizador validarColorCobertura:(SCCobertura *)cobertura
{
    [self changeTextColorOfTextField:[self textFieldParaTipoCobertura:cobertura.type] valid:cobertura.esValida];
}

- (void)cotizador:(SCCotizador *)aCotizador nuevaSumaAseguradaEnCobertura:(SCCobertura *)cobertura
{
    UITextField *textField = [self textFieldParaTipoCobertura:cobertura.type];
    textField.text = [NSString stringWithFormat:@"%.2f", cobertura.sumaAsegurada];
    textField.textColor = [UIColor blackColor];
}

- (void)cotizador:(SCCotizador *)aCotizador didUpdateCobertura:(SCCobertura *)aCobertura {
    if (!aCobertura) {
        return;
    }
    [[self textFieldParaTipoCobertura:aCobertura.type] setEnabled:!aCotizador.esPrima];
    NSString *stringNoSeleccionado = @"Excluído";
    if ([aCobertura.stringPrimaAnual isEqualToString:@"Insuficiente"]) {
        stringNoSeleccionado = @"Insuficiente";
    }
    switch (aCobertura.type) {
        case SCCoberturaTypeBAS:{
            if (aCobertura.isSelected){
                self.lblSelectBas.text = aCobertura.stringPrimaAnual;
            } else {
                self.lblSelectBas.text = stringNoSeleccionado;
            }
            self.txtSelectBas.text = aCobertura.stringSumaAsegurada;
            self.btnSelectBas.selected = aCobertura.isSelected;
            self.btnSelectBas.enabled = aCobertura.isEnabled;
        }
            break;
        case SCCoberturaTypeBIT:{
            if (aCobertura.isSelected){
                self.lblSelectBit.text = aCobertura.stringPrimaAnual;
                if (![aCobertura.stringSumaAsegurada isEqualToString:@"Insuficiente"]) {
                    self.txtSelectBit.text = @"Cubierto";
                } else {
                    self.txtSelectBit.text = @"Insuficiente";
                }
            } else {
                if (![aCobertura.stringSumaAsegurada isEqualToString:@"Insuficiente"]) {
                    self.txtSelectBit.text = stringNoSeleccionado;
                    self.lblSelectBit.text = @"0.00";
                } else {
                    self.txtSelectBit.text = @"Insuficiente";
                    self.lblSelectBit.text = @"Insuficiente";
                }
            }
            self.btnSelectBit.selected = aCobertura.isSelected;
            self.btnSelectBit.enabled = aCobertura.isEnabled;
        }
            break;
        case SCCoberturaTypeCII:{
            if (aCobertura.isSelected){
                self.lblSelectCii.text = aCobertura.stringPrimaAnual;
            } else {
                self.lblSelectCii.text = stringNoSeleccionado;
            }
            self.txtSelectCii.text = aCobertura.stringSumaAsegurada;
            self.btnSelectCii.selected = aCobertura.isSelected;
            self.btnSelectCii.enabled = aCobertura.isEnabled;
        }
            break;
        case SCCoberturaTypeCMA:{
            if (aCobertura.isSelected){
                self.lblSelectCma.text = aCobertura.stringPrimaAnual;
            } else {
                self.lblSelectCma.text = stringNoSeleccionado;
            }
            self.txtSelectCma.text = aCobertura.stringSumaAsegurada;
            self.btnSelectCma.selected = aCobertura.isSelected;
            self.btnSelectCma.enabled = aCobertura.isEnabled;
        }
            break;
        case SCCoberturaTypeTIBA:{
            if (aCobertura.isSelected){
                self.lblSelectTima.text = aCobertura.stringPrimaAnual;
            } else {
                self.lblSelectTima.text = stringNoSeleccionado;
            }
            self.txtSelectTima.text = aCobertura.stringSumaAsegurada;
            self.btnSelectTima.selected = aCobertura.isSelected;
            self.btnSelectTima.enabled = aCobertura.isEnabled;
        }
            break;
        case SCCoberturaTypeCAT:{
            if (aCobertura.isSelected){
                self.lblSelectCat.text = aCobertura.stringPrimaAnual;
            } else {
                self.lblSelectCat.text = stringNoSeleccionado;
            }
            self.txtSelectCat.text = aCobertura.stringSumaAsegurada;
            self.btnSelectCat.selected = aCobertura.isSelected;
            self.btnSelectCat.enabled = aCobertura.isEnabled;
        }
            break;
        case SCCoberturaTypeGFA:{
            if (aCobertura.isSelected){
                self.lblSelectGfa.text = aCobertura.stringPrimaAnual;
            }else{
                self.lblSelectGfa.text = stringNoSeleccionado;
            }
            self.txtSelectGfa.text = aCobertura.stringSumaAsegurada;
            self.btnSelectGfa.selected = aCobertura.isSelected;
            self.btnSelectGfa.enabled = aCobertura.isEnabled;
        }
            break;
        case SCCoberturaTypeGE:{
            if (aCobertura.isSelected){
                self.lblSelectGe.text = aCobertura.stringPrimaAnual;
            }else{
                self.lblSelectGe.text = stringNoSeleccionado;
            }
            self.txtSelectGe.text = aCobertura.stringSumaAsegurada;
            self.btnSelectGe.selected = aCobertura.isSelected;
            self.btnSelectGe.enabled = aCobertura.isEnabled;
        }
            break;
        case SCCoberturaTypeBACY:{
            if (aCobertura.isSelected){
                self.lblSelectcoberturaConyuge.text = aCobertura.stringPrimaAnual;
            }else{
                self.lblSelectcoberturaConyuge.text = stringNoSeleccionado;
            }
            self.txtSelectcoberturaConyuge.text = aCobertura.stringSumaAsegurada;
            self.btnSelectBacy.selected = aCobertura.isSelected;
            self.btnSelectBacy.enabled = aCobertura.isEnabled;
        }
            break;
        case SCCoberturaTypeGFC: {
            if (aCobertura.isSelected) {
                self.lblSelectGFC.text = aCobertura.stringPrimaAnual;
            }else{
                self.lblSelectGFC.text = stringNoSeleccionado;
            }
            self.txtSelectGFC.text = aCobertura.stringSumaAsegurada;
            self.SelectGFC.selected = aCobertura.isSelected;
            self.SelectGFC.enabled = aCobertura.isEnabled;
        }
            break;
        case SCCoberturaTypeCP: {
            if (aCobertura.isSelected) {
                self.lblSelectCancerPlus.text = aCobertura.stringPrimaAnual;
            } else {
                self.lblSelectCancerPlus.text = stringNoSeleccionado;
            }
            self.txtSelectCancerPlus.text = aCobertura.stringSumaAsegurada;
            self.SelectCP.selected = aCobertura.isSelected;
            self.SelectCP.enabled = aCobertura.isEnabled;
        }
            break;
        case SCCoberturaTypeGFH: {
            if (self.cmbGFH.enabled == YES) {
                self.lblSGFH.text = aCobertura.stringPrimaAnual;
            }else{
                self.lblSGFH.text = stringNoSeleccionado;
            }
            //self.lblSGFH.text = aCobertura.stringPrimaAnual;
            self.txtSelectSGFH.text = aCobertura.stringSumaAsegurada;
            self.cmbGFH.enabled = aCobertura.isEnabled;
        }
            break;
        case SCCoberturaTypeCC: {
            if (aCobertura.isSelected) {
                self.lblSelectCoberturaComplementaria.text = aCobertura.stringPrimaAnual;
            }else{
                self.lblSelectCoberturaComplementaria.text = stringNoSeleccionado;
            }
            self.txtSelectCoberturaComplementaria.text = aCobertura.stringSumaAsegurada;
            self.btnSelectCC.selected = aCobertura.isSelected;
            self.btnSelectCC.enabled = aCobertura.isEnabled;
        }
            break;
        case SCCoberturaTypeCP1: {
            if (aCobertura.isSelected) {
                self.lblSelectCancerPLus1.text = aCobertura.stringPrimaAnual;
            } else {
                self.lblSelectCancerPLus1.text = stringNoSeleccionado;
            }
            
            self.txtSelectCancerPLus1.text = aCobertura.stringSumaAsegurada;
            self.btnCancerPLus1.selected = aCobertura.isSelected;
            self.btnCancerPLus1.enabled = aCobertura.isEnabled;
        }
            break;
        case SCCoberturaTypeCP2: {
            if (aCobertura.isSelected) {
                self.lblSelectCancerPLus2.text = aCobertura.stringPrimaAnual;
            }else{
                self.lblSelectCancerPLus2.text = stringNoSeleccionado;
            }
            self.txtSelectCancerPLus2.text = aCobertura.stringSumaAsegurada;
            self.btnCancerPlus2.selected = aCobertura.isSelected; 
            self.btnCancerPlus2.enabled = aCobertura.isEnabled;
        }
            break;
        case SCCoberturaTypeCP3: {
            if (aCobertura.isSelected) {
                self.lblSelectCancerPLus3.text = aCobertura.stringPrimaAnual;
            } else {
                self.lblSelectCancerPLus3.text = stringNoSeleccionado;
            }
            self.txtSelectCancerPLus3.text = aCobertura.stringSumaAsegurada;
            self.btnCancerPlus3.selected = aCobertura.isSelected;
            self.btnCancerPlus3.enabled = aCobertura.isEnabled;
        }
            break;
        default:
            break;
    }
}

- (void)cotizador:(SCCotizador *)aCotizador didGetSumaAseguradaTotal:(double)nuevaSumaAseguradaTotal {
    if (!self.cotizador.esPrima) {
        int indicePago = self.comboFormaDePago.index;
        double factorMultAumento = 1;
        switch (indicePago) {
            case 1:
                factorMultAumento = 1.0/12.0;
                break;
            case 2:
                factorMultAumento = 1.0/24.0;
            default:
                break;
        }
        nuevaSumaAseguradaTotal = primaExedenteAnualValue*factorMultAumento + nuevaSumaAseguradaTotal*factorMultAumento;
    }
    self.primaTotalAnual.text = [NSString stringWithFormat:@"%.2f",nuevaSumaAseguradaTotal];
}

- (void)cotizador:(SCCotizador *)aCotizador didGetExtraPrimaText:(NSString *)nuevaExtraPrima {
    self.extraPrima.text = nuevaExtraPrima;
}

#pragma mark - Animation Methods **********************************************************************

- (void) pulse:(UIView*)theView {
    theView.transform = CGAffineTransformMakeScale(0.6, 0.6);
	[UIView animateWithDuration: 0.2
					 animations: ^{
						 theView.transform = CGAffineTransformMakeScale(1.1, 1.1);
					 }
					 completion: ^(BOOL finished){
						 [UIView animateWithDuration:1.0/15.0
										  animations: ^{
											  theView.transform = CGAffineTransformMakeScale(0.9, 0.9);
										  }
										  completion: ^(BOOL finished){
											  [UIView animateWithDuration:1.0/7.5
															   animations: ^{
																   theView.transform = CGAffineTransformIdentity;
															   }];
										  }];
					 }];
	
}

- (double)sumatoria:(double)interes {
    double acumulador = 0;
    for(int contador = 0; contador < 12; contador++){
        acumulador += pow ((interes / 12) + 1, 12 - contador);
    }
    return acumulador;
}


- (IBAction)verCotizacion:(id)sender
{
    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
        //Device is ipad
        self.cotizacionVC = [[SCCotizacionVC alloc] initWithNibName:@"SCCotizacionVC" bundle:nil cotizacion:[self estructuraDatosCotizacion] suma:[self obtenerSumaMayor] primaExcedente:[self.primaExedenteAnual.text doubleValue] tienePrima:([self.primaExedenteAnual.text doubleValue] > 0)];
    }else{
        //Device is iphone
        self.cotizacionVC = [[SCCotizacionVC alloc] initWithNibName:@"SCCotizacion_iphoneVC" bundle:nil cotizacion:[self estructuraDatosCotizacion] suma:[self obtenerSumaMayor] primaExcedente:[self.primaExedenteAnual.text doubleValue] tienePrima:([self.primaExedenteAnual.text doubleValue] > 0)];
    }
    [self.view addSubview:self.cotizacionVC.view];
}

- (NSDictionary *)estructuraDatosCotizacion
{
    NSMutableDictionary *diccionarioDatosCotizacion = [[NSMutableDictionary alloc] init];
    
    [diccionarioDatosCotizacion setObject:[self datosTitular] forKey:@"datosTitular"];
    
    if ([[self.cotizador.coberturas objectAtIndex:8] isSelected] || [[self.cotizador.coberturas objectAtIndex:9] isSelected] || [[self.cotizador.coberturas objectAtIndex:10] isSelected]) {
        [diccionarioDatosCotizacion setObject:[self datosConyugue] forKey:@"datosConyugue"];
    }
    if ([[self.cotizador.coberturas objectAtIndex:12] isSelected]) {
        [diccionarioDatosCotizacion setObject:[self datosComplementariosConTipo:SCClasificacionCoberturaComplementaria] forKey:@"datosCC"];
    }
    if ([[self.cotizador.coberturas objectAtIndex:13] isSelected]) {
        [diccionarioDatosCotizacion setObject:[self datosComplementariosConTipo:SCClasificacionCancerPlus1] forKey:@"datosC1"];
    }
    if ([[self.cotizador.coberturas objectAtIndex:14] isSelected]) {
        [diccionarioDatosCotizacion setObject:[self datosComplementariosConTipo:SCClasificacionCancerPlus2] forKey:@"datosC2"];
        
    }
    if ([[self.cotizador.coberturas objectAtIndex:15] isSelected]) {
        [diccionarioDatosCotizacion setObject:[self datosComplementariosConTipo:SCClasificacionCancerPlus3] forKey:@"datosC3"];
    }
    if ([[self.cotizador.coberturas objectAtIndex:11] isSelected]) {
        [diccionarioDatosCotizacion setObject:[self datosHijos] forKey:@"datosHijos"];
    }
    [diccionarioDatosCotizacion setObject:[self datosProyeccionFinanciera] forKey:@"datosProyeccionFinanciera"];
    
    return diccionarioDatosCotizacion;
}

-(NSDictionary *)datosProyeccionFinanciera{
    
    NSString *primaExedente = [NSString stringWithFormat:@"%.2f", [self.primaExedenteAnual.text doubleValue]];
    if (!primaExedente) {
        primaExedente = @"";
    }
    double primaTotal = [self.primaTotalAnual.text doubleValue];
    if (!self.cotizador.esPrima) {
        if ([self.pago isEqualToString:@"Mensual"]) {
            primaTotal *= 12;
        } else if ([self.pago isEqualToString:@"Quincenal"]) {
            primaTotal *= 24;
        }
    }
    NSString *primaTotalAnual = [NSString stringWithFormat:@"%.2f", primaTotal];
    
    NSString *primaDiaria = [NSString stringWithFormat:@"%.2f", primaTotal/360];
    NSString *formaDePago = self.cmbFormaDePago.titleLabel.text;
    
    NSDictionary *dicc = [NSDictionary dictionaryWithObjectsAndKeys:primaExedente, @"primaExedente",primaTotalAnual, @"primaTotalAnual", primaDiaria, @"primaDiaria", formaDePago, @"formaDePago", nil];
    
    return dicc;
    
}

- (NSDictionary *)datosTitular
{
    NSString *nombre = self.nombreTitular.text;
    if (!nombre) {
        nombre = @"";
    }
    NSString *edadReal = [NSString stringWithFormat:@"%i", self.cotizador.realAge];
    NSString *edadCalculo = [NSString stringWithFormat:@"%i", self.cotizador.estimateAge];
    NSString *habito = self.fuma.isSelected ? @"Fumador" : @"No Fumador";
    NSString *sexo = self.hombre.isSelected ? @"Hombre" : @"Mujer";
    NSString *ocupacion = self.cmbProfession.titleLabel.text;
    if (!ocupacion) {
        ocupacion = @"";
    }
    NSDictionary *dicc = [NSDictionary dictionaryWithObjectsAndKeys:nombre, @"nombre", edadReal, @"edadReal", edadCalculo, @"edadCalculo", habito, @"habito", sexo, @"sexo", ocupacion, @"ocupacion", [self arrayPrimasConClasificacion:SCClasificacionCoberturaTitular], @"arrayCoberturas", nil];
    return dicc;
}

- (NSDictionary *)datosConyugue
{
    NSString *nombre = self.txtSelectNombreConyuge.text;
    if (!nombre) {
        nombre = @"";
    }
    NSString *edad = self.txtSelectEdadConyuge.text;
    NSString *sexo = self.hombre.isSelected ? @"Mujer" : @"Hombre";
    NSDictionary *dicc = [NSDictionary dictionaryWithObjectsAndKeys:nombre, @"nombre", edad, @"edad", sexo, @"sexo", [self arrayPrimasConClasificacion:SCClasificacionCoberturaConyugue], @"arrayCoberturas", nil];
    return dicc;
}

- (NSDictionary *)datosComplementariosConTipo:(SCClasificacionCobertura)clasificacion
{
    if (clasificacion == (SCClasificacionCoberturaConyugue | SCClasificacionCoberturaTitular | SCClasificacionCoberturaHijos)) {
        return nil;
    }
    
    UITextField *textFieldNombre = nil;
    UITextField *textFieldEdad = nil;
    SCComboBox *comboEdad = nil;
    switch (clasificacion) {
        case SCClasificacionCancerPlus1:
            textFieldNombre = self.txtSelectNameCancerPLus1;
            textFieldEdad = self.txtSelectAgeCancerPLus1;
            comboEdad = self.comboCancerPlus1;
            break;
        case SCClasificacionCancerPlus2:
            textFieldNombre = self.txtSelectNameCancerPLus2;
            textFieldEdad = self.txtSelectAgeCancerPLus2;
            comboEdad = self.comboCancerPlus2;
            break;
        case SCClasificacionCancerPlus3:
            textFieldNombre = self.txtSelectNameCancerPLus3;
            textFieldEdad = self.txtSelectAgeCancerPLus3;
            comboEdad = self.comboCancerPlus3;
            break;
        case SCClasificacionCoberturaComplementaria:
            textFieldNombre = self.txtSelectNameCoberturaComplementaria;
            textFieldEdad = self.txtSelectAgeCoberturaComplementaria;
            comboEdad = self.comboCoberturaComplementaria;
            break;
        default:
            break;
    }
   
    NSString *sexo = comboEdad.index == 0 ? @"Mujer" : @"Hombre";
    NSString *nombre = textFieldNombre.text;
    if (!nombre) {
        nombre = @"";
    }
    NSDictionary  *dicc = [NSDictionary dictionaryWithObjectsAndKeys:nombre, @"nombre", textFieldEdad.text, @"edad", sexo, @"sexo", [self arrayPrimasConClasificacion:clasificacion], @"arrayCoberturas", nil];
    return dicc;
}

- (NSDictionary *)datosHijos
{
    SCComboBox *hijosCmb = self.comboHijos;
    NSString *hijos = [NSString stringWithFormat:@"%i", hijosCmb.index];
    NSDictionary *dicc = [NSDictionary dictionaryWithObjectsAndKeys:hijos, @"hijos", [self arrayPrimasConClasificacion:SCClasificacionCoberturaHijos], @"arrayCoberturas", nil];
    return dicc;
}

- (NSArray *)arrayPrimasConClasificacion:(SCClasificacionCobertura)clasificacion
{
    NSMutableArray *coberturas = [[NSMutableArray alloc] init];
    
    for (SCCobertura *cobertura in self.cotizador.coberturas) {
        if (cobertura.isSelected) {
            if (cobertura.clasificacion == clasificacion) {
                NSString *nombreCobertura = [self nombreCoberturaDeTipo:cobertura.type];
                NSString *sumaAsegurada = [self stringFromDouble:cobertura.sumaAsegurada];
                NSString *primaAnual = [self stringFromDouble:cobertura.primaAnual];
                if (cobertura.type == SCCoberturaTypeBIT) {
                    sumaAsegurada = @"Cubierto";
                }
                [coberturas addObject:[NSDictionary dictionaryWithObjectsAndKeys:nombreCobertura, @"nombreCobertura", sumaAsegurada, @"sumaAsegurada", primaAnual, @"primaAnual", nil]];
            }
        }
    }
    if (clasificacion == SCClasificacionCoberturaTitular) {
        
        
        
        [coberturas addObject:[NSDictionary dictionaryWithObjectsAndKeys:@"ET", @"nombreCobertura", self.cotizador.esNuevaPoliza ? @"Otogado" : @"No otorgado", @"sumaAsegurada", @"Sin Costo", @"primaAnual", nil]];
    }
    return coberturas;
}

- (double)obtenerSumaMayor
{
    double sumaMayor = 0;
    for (SCCobertura *cobertura in self.cotizador.coberturas) {
        if (cobertura.isSelected && cobertura.type != SCCoberturaTypeBIT) {
            if (cobertura.sumaAsegurada > sumaMayor) {
                sumaMayor = cobertura.sumaAsegurada;
            }
        }
    }
    return sumaMayor;
}

- (NSString *)nombreCoberturaDeTipo:(SCCoberturaType)tipo
{
    NSString *nombre = nil;
    switch (tipo) {
        case SCCoberturaTypeBAS: nombre = @"BAS"; break;
        case SCCoberturaTypeBIT: nombre = @"BIT"; break;
        case SCCoberturaTypeCII: nombre = @"CII"; break;
        case SCCoberturaTypeCMA: nombre = @"CMA"; break;
        case SCCoberturaTypeTIBA: nombre = @"TIBA"; break;
        case SCCoberturaTypeCAT: nombre = @"CAT"; break;
        case SCCoberturaTypeGFA: nombre = @"GFA"; break;
        case SCCoberturaTypeGE: nombre = @"GE"; break;
        case SCCoberturaTypeBACY: nombre = @"BACY"; break;
        case SCCoberturaTypeGFC: nombre = @"GFC"; break;
        case SCCoberturaTypeCP: nombre = @"CP"; break;
        case SCCoberturaTypeGFH: nombre = @"GFH"; break;
        case SCCoberturaTypeCC: nombre = @"Cobertura complementaria"; break;
        case SCCoberturaTypeCP1: nombre = @"Cáncer plus"; break;
        case SCCoberturaTypeCP2: nombre = @"Cáncer plus"; break;
        case SCCoberturaTypeCP3: nombre = @"Cáncer plus"; break;
        default: break;
    }
    return nombre;
}

- (NSString *)stringFromDouble:(double)dato
{
    
    NSNumberFormatter *numberFormatter = [[NSNumberFormatter alloc] init];
    [numberFormatter setNumberStyle: NSNumberFormatterCurrencyStyle];
    return [numberFormatter stringFromNumber:[NSNumber numberWithFloat:dato]];
    
}

- (void)cotizadorPorPrimaSinSolucion:(SCCotizador *)cotizador {
    if (!self.alertaSinSolucion) {
        self.alertaSinSolucion = [[UIAlertView alloc] initWithTitle:@"Sin solución" message:@"No se puede encontrar una solución que cumpla con los límites de suma asegurada." delegate:nil cancelButtonTitle:@"Aceptar" otherButtonTitles:nil];
    }
    if (!self.alertaSinSolucion.isVisible) {
        [self.alertaSinSolucion show];
    }
}

- (void)cotizadorPorPrimaExcede:(SCCotizador *)cotizador
{
    if (!self.alertaExcede) {
        self.alertaExcede = [[UIAlertView alloc] initWithTitle:@"Sin solución" message:@"La suma de BAS, GFA y GE no puede exceder la cantidad de 3,000,000." delegate:nil cancelButtonTitle:@"Aceptar" otherButtonTitles:nil];
    }
    if (!self.alertaExcede.isVisible) {
        [self.alertaExcede show];
    }
}

- (void)cotizadorPorPrimaInsuficiente:(SCCotizador *)cotizador {
    if (!self.alertaInsuficientes) {
        self.alertaInsuficientes = [[UIAlertView alloc] initWithTitle:nil message:@"La prima es insuficiente para esta combinación, por favor intente con otra" delegate:nil cancelButtonTitle:@"Aceptar" otherButtonTitles:nil];
    }
    if (!self.alertaInsuficientes.isVisible) {
        [self.alertaInsuficientes show];
    }
}

@end
