//
//  SCProspectionVC.m
//  SIPAC
//
//  Created by Jaguar3 on 27/05/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import "SCProspectionVC.h"
#import "SCManager.h"
#import "SCComboBox.h"
#import "SCDatabaseManager.h"
#import "GridTableViewCell.h"
#import "MBProgressHUD.h"

@interface SCProspectionVC (){
    SCComboBox *comboEstructura;
    SCComboBox *comboZona;
    SCComboBox *comboSexo;
    SCComboBox *comboEstadoCivil;
    SCComboBox *comboFuma;
    SCComboBox *comboEdad;
    SCComboBox *comboPrima;
    SCComboBox *comboSumaBasica;
    SCComboBox *comboFondo;
    
    NSMutableArray *resultsFromQuery;
    
    UIButton *search;
    UIButton *invisibleButton;
    
    UIWebView *resultsProspect;
    
    UIActionSheet *actionSheet;
    UITextField *activeTextField;
    UIDatePicker *activeDatePicker;
    int currentDateTxt;
    UIPopoverController *popover;
}
@property (weak, nonatomic) IBOutlet UIImageView *imgLogo;

@property (weak, nonatomic) IBOutlet UIScrollView *scrollIphone;
@property (weak, nonatomic) IBOutlet UIScrollView *iphoneScroll;

//ComboBox
@property (weak, nonatomic) IBOutlet UIButton *btnEstructura;
@property (weak, nonatomic) IBOutlet UIButton *btnZona;
@property (weak, nonatomic) IBOutlet UIButton *btnSexo;
@property (weak, nonatomic) IBOutlet UIButton *btnFuma;
@property (weak, nonatomic) IBOutlet UIButton *btnPrima;
@property (weak, nonatomic) IBOutlet UIButton *btnReservaInversion;
@property (weak, nonatomic) IBOutlet UIButton *btnEstadoCivil;
@property (weak, nonatomic) IBOutlet UIButton *btnEdad;
@property (weak, nonatomic) IBOutlet UIButton *btnSumaBasica;

//TextFields
@property (weak, nonatomic) IBOutlet UITextField *txtRetenedor;
@property (weak, nonatomic) IBOutlet UITextField *txtUnidadPago;
@property (weak, nonatomic) IBOutlet UITextField *txtMunicipioEmpresa;
@property (weak, nonatomic) IBOutlet UITextField *txtUltimaVenta;
@property (weak, nonatomic) IBOutlet UITextField *txtDe;
@property (weak, nonatomic) IBOutlet UITextField *txtA;
@property (weak, nonatomic) IBOutlet UITextField *txtEdad;
@property (weak, nonatomic) IBOutlet UITextField *txtPrima;
@property (weak, nonatomic) IBOutlet UITextField *txtSumaBasica;
@property (weak, nonatomic) IBOutlet UITextField *txtFondoReserva;
@property (weak, nonatomic) IBOutlet UITextField *noResults;
@property (weak, nonatomic) IBOutlet UITextField *txtUltimoRetiro;

//CheckBox
@property (weak, nonatomic) IBOutlet UIButton *checkCMA;
@property (weak, nonatomic) IBOutlet UIButton *checkTIBA;
@property (weak, nonatomic) IBOutlet UIButton *checkCII;
@property (weak, nonatomic) IBOutlet UIButton *checkBAC;
@property (weak, nonatomic) IBOutlet UIButton *checkBACY;
@property (weak, nonatomic) IBOutlet UIButton *checkGFA;
@property (weak, nonatomic) IBOutlet UIButton *checkBIT;
@property (weak, nonatomic) IBOutlet UIButton *checkCAT;
@property (weak, nonatomic) IBOutlet UIButton *checkCATPLUS;
@property (weak, nonatomic) IBOutlet UIButton *checkEXC;
@property (weak, nonatomic) IBOutlet UIButton *checkGFH;
@property (weak, nonatomic) IBOutlet UIButton *checkGFC;
@property (weak, nonatomic) IBOutlet UIButton *checkGE;
@property (weak, nonatomic) IBOutlet UIButton *checkUltimaVentaMeses;
@property (weak, nonatomic) IBOutlet UIButton *checkUltimaVenteDeA;
@property (weak, nonatomic) IBOutlet UIButton *checkUltimoRetiro;



- (IBAction)btnCheckTouched:(UIButton*)sender;
- (IBAction)MakeQuery:(id)sender;
@property (weak, nonatomic) IBOutlet UITableView *resultTableView;

@end

@implementation SCProspectionVC

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    [self.imgLogo setImage:[[SCManager sharedInstance] imageFromFile]];
    
    if ([[[UIDevice currentDevice] systemVersion] integerValue] == 7) {
        self.edgesForExtendedLayout = UIRectEdgeNone;
    }
  
        self.navigationController.navigationBar.topItem.title = @"Prospección";
        
        UIImage *btnImgAlert = [UIImage imageNamed:@"btn_sidemenu_unpressed"];
        UIImage *btnImgAlertPressed = [UIImage imageNamed:@"btn_sidemenu_pressed"];
        UIButton *myleftButton = [UIButton buttonWithType:UIButtonTypeCustom];
        [myleftButton setImage:btnImgAlert forState:UIControlStateNormal];
        [myleftButton setImage:btnImgAlertPressed forState:UIControlStateHighlighted];
        myleftButton.frame = CGRectMake(200.0, 10.0, btnImgAlert.size.width, btnImgAlert.size.height);
        [myleftButton addTarget:self action:@selector(paneMenuTapped:) forControlEvents:UIControlEventTouchUpInside];
        UIBarButtonItem *leftButton = [[UIBarButtonItem alloc]initWithCustomView:myleftButton];
        self.navigationItem.leftBarButtonItem = leftButton;

    
    NSArray *dataEstructura = @[@"KU",@"KU/COLIMA",@"KU/JALISCO",@"KU/JALISCO/CD.GUZMAN",@"KU/JALISCO/GUADALAJARA",@"KU/JALISCO/PUERTO VALLARTA",@"KU/JALISCO/TEPATITLAN",@"KU/JALISCO/ZAPOPAN"];
    NSArray *dataZona = [[SCDatabaseManager sharedInstance] getZones];;//@[@"Todas",@"00OF",@"01AB",@"01AC",@"01AD",@"01AE",@"01AG",@"01AH",@"01AI",@"01AK",@"01AL",@"01AM",@"01AO",@"01AP",@"01AS",@"01AU",@"01B1",@"01B2",@"01B3",@"01B4",@"01B5",@"01BP",@"01CA",@"01CB",@"01CD",@"01CE",@"01CF",@"01CH",@"01CI",@"01CL",@"01CM",@"01CP",@"01CR",@"01CS",@"01CT",@"01CU",@"01CU-A",@"01CV",@"01DB",@"01DG",@"01DM",@"01DP",@"01DT",@"01EC",@"01EF",@"01ES",@"01GB",@"01GD",@"01GM",@"01HA",@"01HB",@"01HH",@"01HJ",@"01HM",@"01HN",@"01HT",@"01HV",@"01IE",@"01IT",@"01J1",@"01J2",@"01J3",@"01J4",@"01J5",@"01J6",@"01J7",@"01JM",@"01LB",@"01LO",@"01ML",@"01MU",@"01NT",@"01OM",@"01OP",@"01OR",@"01PC",@"01PD",@"01PL",@"01PM",@"01PMG",@"01PO",@"01PQ",@"01PS",@"01PT",@"01RC",@"01RE",@"01RR",@"01RS",@"01RX",@"01SA",@"01SC",@"01SG",@"01TM",@"01TR",@"01UC",@"01UR",@"01US",@"01VV",@"01Z1",@"01Z2",@"01Z3",@"01Z4",@"01Z5",@"01Z6",@"01ZB",@"01ZC",@"01ZF",@"01ZS",@"02AA",@"02AC",@"02AM",@"02AP",@"02AT",@"02AZ",@"02B2",@"02B3",@"02B4",@"02B5",@"02BB",@"02BC",@"02BG",@"02BM",@"02BZ",@"02CA",@"02CB",@"02CC",@"02CD",@"02CF",@"02CG",@"02CN",@"02CS",@"02CT",@"02CU",@"02CV",@"02DC",@"02DE",@"02DG",@"02DO",@"02EE",@"02EM",@"02ER",@"02EV",@"02GC",@"02GP",@"02HG",@"02HJ",@"02HN",@"02IA",@"02IM",@"02JG",@"02JV",@"02JZ",@"02KC",@"02KT",@"02MD",@"02MT",@"02OC",@"02PC",@"02PD",@"02PE",@"02PI",@"02PL",@"02PM",@"02PQ",@"02PR",@"02PT",@"02PV",@"02PZ",@"02RC",@"02RR",@"02RT",@"02S1",@"02S2",@"02S3",@"02S4",@"02SM",@"02TA",@"02TC",@"02TM",@"02TN",@"02TZ",@"02UA",@"02UL",@"02UR",@"02VV",@"02XT",@"03AD",@"03CD",@"03CP",@"03OP",@"04CG",@"04SA",@"05CL",@"05ZA",@"06TC",@"07TZ",@"08CC",@"09CM",@"10ZO",@"11DF",@"12DA",@"12DE",@"12DF",@"12GU",@"12OF",@"13PS",@"14UTS",@"15PC",@"16TP",@"17FD",@"18CO",@"1P10",@"22HC",@"99DR",@"METGDL"];
    NSArray *dataSexo = @[@"Ambos",@"Masculino",@"Femenino"];
    NSArray *dataEstadoCivil = @[@"Todos",@"Soltero(a)",@"Casado(a)",@"Divorciado(a)",@"Union Libre",@"Viudo(a)"];
    NSArray *dataFuma = @[@"No",@"Si"];
    NSArray *dataComparacion = @[@">=",@"<=",@"=="];
    
    if(!([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad)) {
        self.scrollIphone.contentSize = CGSizeMake(320, 1000);
        comboEstructura = [[SCComboBox alloc] initWithButton:self.btnEstructura isTableUp:NO visibleOptionLimit:3 TableDelegate:self onView:self.iphoneScroll dataSource:dataEstructura];
        comboZona = [[SCComboBox alloc] initWithButton:self.btnZona isTableUp:YES visibleOptionLimit:3 TableDelegate:self onView:self.iphoneScroll dataSource:dataZona];
        comboEstadoCivil = [[SCComboBox alloc] initWithButton:self.btnEstadoCivil isTableUp:NO visibleOptionLimit:3 TableDelegate:self onView:self.iphoneScroll dataSource:dataEstadoCivil];
        comboFuma = [[SCComboBox alloc] initWithButton:self.btnFuma isTableUp:NO visibleOptionLimit:2 TableDelegate:self onView:self.iphoneScroll dataSource:dataFuma];
        comboSexo = [[SCComboBox alloc] initWithButton:self.btnSexo isTableUp:NO visibleOptionLimit:3 TableDelegate:self onView:self.iphoneScroll dataSource:dataSexo];
        comboPrima = [[SCComboBox alloc] initWithButton:self.btnPrima isTableUp:NO visibleOptionLimit:3 TableDelegate:self onView:self.iphoneScroll dataSource:dataComparacion];
        comboSumaBasica = [[SCComboBox alloc] initWithButton:self.btnSumaBasica isTableUp:NO visibleOptionLimit:3 TableDelegate:self onView:self.iphoneScroll dataSource:dataComparacion];
        comboEdad = [[SCComboBox alloc] initWithButton:self.btnEdad isTableUp:NO visibleOptionLimit:3 TableDelegate:self onView:self.iphoneScroll dataSource:dataComparacion];
        comboFondo = [[SCComboBox alloc] initWithButton:self.btnReservaInversion isTableUp:YES visibleOptionLimit:3 TableDelegate:self onView:self.iphoneScroll dataSource:dataComparacion];
    }else{
        comboEstructura = [[SCComboBox alloc] initWithButton:self.btnEstructura isTableUp:NO visibleOptionLimit:5 TableDelegate:self onView:self.view dataSource:dataEstructura];
        comboZona = [[SCComboBox alloc] initWithButton:self.btnZona isTableUp:NO visibleOptionLimit:5 TableDelegate:self onView:self.view dataSource:dataZona];
        comboEstadoCivil = [[SCComboBox alloc] initWithButton:self.btnEstadoCivil isTableUp:NO visibleOptionLimit:5 TableDelegate:self onView:self.view dataSource:dataEstadoCivil];
        comboFuma = [[SCComboBox alloc] initWithButton:self.btnFuma isTableUp:NO visibleOptionLimit:2 TableDelegate:self onView:self.view dataSource:dataFuma];
        comboSexo = [[SCComboBox alloc] initWithButton:self.btnSexo isTableUp:NO visibleOptionLimit:3 TableDelegate:self onView:self.view dataSource:dataSexo];
        comboPrima = [[SCComboBox alloc] initWithButton:self.btnPrima isTableUp:NO visibleOptionLimit:3 TableDelegate:self onView:self.view dataSource:dataComparacion];
        comboSumaBasica = [[SCComboBox alloc] initWithButton:self.btnSumaBasica isTableUp:NO visibleOptionLimit:3 TableDelegate:self onView:self.view dataSource:dataComparacion];
        comboEdad = [[SCComboBox alloc] initWithButton:self.btnEdad isTableUp:NO visibleOptionLimit:3 TableDelegate:self onView:self.view dataSource:dataComparacion];
        comboFondo = [[SCComboBox alloc] initWithButton:self.btnReservaInversion isTableUp:YES visibleOptionLimit:3 TableDelegate:self onView:self.view dataSource:dataComparacion];
    }
    
    
    
    
    [self.btnEstructura setTitle:@"Escoge una opcion.." forState:UIControlStateNormal];
    [self.btnZona setTitle:@"Todas" forState:UIControlStateNormal];
    [self.btnEstadoCivil setTitle:@"Todos" forState:UIControlStateNormal];
    [self.btnFuma setTitle:@"No" forState:UIControlStateNormal];
    [self.btnSexo setTitle:@"Ambos" forState:UIControlStateNormal];
    [self.btnPrima setTitle:@">=" forState:UIControlStateNormal];
    [self.btnSumaBasica setTitle:@">=" forState:UIControlStateNormal];
    [self.btnEdad setTitle:@">=" forState:UIControlStateNormal];
    [self.btnReservaInversion setTitle:@">=" forState:UIControlStateNormal];
    
    [self.btnEstructura addTarget:self action:@selector(btnTouched:) forControlEvents:UIControlEventTouchUpInside];
    [self.btnZona addTarget:self action:@selector(btnTouched:) forControlEvents:UIControlEventTouchUpInside];
    [self.btnEstadoCivil addTarget:self action:@selector(btnTouched:) forControlEvents:UIControlEventTouchUpInside];
    [self.btnFuma addTarget:self action:@selector(btnTouched:) forControlEvents:UIControlEventTouchUpInside];
    [self.btnSexo addTarget:self action:@selector(btnTouched:) forControlEvents:UIControlEventTouchUpInside];
    [self.btnPrima addTarget:self action:@selector(btnTouched:) forControlEvents:UIControlEventTouchUpInside];
    [self.btnSumaBasica addTarget:self action:@selector(btnTouched:) forControlEvents:UIControlEventTouchUpInside];
    [self.btnEdad addTarget:self action:@selector(btnTouched:) forControlEvents:UIControlEventTouchUpInside];
    [self.btnReservaInversion addTarget:self action:@selector(btnTouched:) forControlEvents:UIControlEventTouchUpInside];
    
    if (([UIDevice currentDevice].userInterfaceIdiom!=UIUserInterfaceIdiomPad)) {
    [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(keyboardWasShown:)
                                                     name:UIKeyboardDidShowNotification
                                                   object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(keyboardWasHidden:)
                                                     name:UIKeyboardDidHideNotification
                                                   object:nil];
    }
    
}

- (void)keyboardWasShown:(id)notification{
    invisibleButton = [[UIButton alloc] initWithFrame:[UIApplication sharedApplication].keyWindow.bounds];
    [invisibleButton addTarget:self action:@selector(hiddenBtnTouched:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:invisibleButton];
    
    
    if (([UIDevice currentDevice].userInterfaceIdiom!=UIUserInterfaceIdiomPad)) {
    for (UITextField *txt in self.iphoneScroll.subviews) {
        if ([txt class] == [UITextField class]) {
            if (txt.tag == 1) {
                if ([txt isFirstResponder]) {
                    CGRect tmpFrame = self.iphoneScroll.frame;
                    tmpFrame.origin = CGPointMake(0, -135);
                    [UIView animateWithDuration:0.5 animations:^{
                        self.iphoneScroll.frame = tmpFrame;
                    }];
                }
            }
            
            if (txt.tag == 2) {
                if ([txt isFirstResponder]) {
                    CGRect tmpFrame = self.iphoneScroll.frame;
                    tmpFrame.origin = CGPointMake(0, -35);
                    [UIView animateWithDuration:0.5 animations:^{
                        self.iphoneScroll.frame = tmpFrame;
                    }];
                }
            }
            
            if (txt.tag == 3) {
                if ([txt isFirstResponder]) {
                    CGRect tmpFrame = self.iphoneScroll.frame;
                    tmpFrame.origin = CGPointMake(0, -165);
                    [UIView animateWithDuration:0.5 animations:^{
                        self.iphoneScroll.frame = tmpFrame;
                    }];
                }
            }
        }
    }
    }
    
}

- (void)keyboardWasHidden:(id)notification{
    UIView *view = nil;
    if (([UIDevice currentDevice].userInterfaceIdiom!=UIUserInterfaceIdiomPad)) {
        view = self.iphoneScroll;
    }else{
        view = self.view;
    }
    CGRect tmpFrame = view.frame;
    tmpFrame.origin = CGPointMake(0, 0);
    [UIView animateWithDuration:0.5 animations:^{
        view.frame = tmpFrame;
    }];
    
    [invisibleButton removeFromSuperview];
}

- (void)hiddenBtnTouched:(id)sender{
    UIView *view = nil;
    if (([UIDevice currentDevice].userInterfaceIdiom!=UIUserInterfaceIdiomPad)) {
        view = self.iphoneScroll;
    }else{
        view = self.view;
    }
    for (UITextField *txt in view.subviews) {
        if ([txt class] == [UITextField class]) {
            [txt resignFirstResponder];
        }
    }
}

- (void)btnTouched:(UIButton*)button{
    switch (button.tag) {
        case 1:
            [comboEstructura showOrHideCombo];
            break;
            
        case 2:
            [comboZona showOrHideCombo];
            break;
            
        case 3:
            //sexo
            [comboSexo showOrHideCombo];
            break;
            
        case 4:
            //estado civil
            [comboEstadoCivil showOrHideCombo];
            break;
            
        case 5:
            //fuma
            [comboFuma showOrHideCombo];
            break;
            
        case 6:
            //edad
            [comboEdad showOrHideCombo];
            break;
            
        case 7:
            //prima
            [comboPrima showOrHideCombo];
            break;
            
        case 8:
            //sumabasica
            [comboSumaBasica showOrHideCombo];
            break;
            
        case 9:
            //fondo
            [comboFondo showOrHideCombo];
            break;
            
        default:
            break;
    }
    
    
}

-(BOOL)textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    return YES;
}

- (void)paneMenuTapped:(UIButton*)sender{
    [[SCManager sharedInstance] showPaneMenu];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewDidUnload {
    [self setScrollIphone:nil];
    [self setBtnEstructura:nil];
    [self setBtnZona:nil];
    [self setIphoneScroll:nil];
    [self setBtnSexo:nil];
    [self setBtnFuma:nil];
    [self setBtnPrima:nil];
    [self setBtnReservaInversion:nil];
    [self setBtnEstadoCivil:nil];
    [self setBtnEdad:nil];
    [self setBtnSumaBasica:nil];
    [self setTxtRetenedor:nil];
    [self setTxtUnidadPago:nil];
    [self setTxtMunicipioEmpresa:nil];
    [self setTxtUltimaVenta:nil];
    [self setTxtDe:nil];
    [self setTxtA:nil];
    [self setTxtEdad:nil];
    [self setTxtPrima:nil];
    [self setTxtSumaBasica:nil];
    [self setTxtFondoReserva:nil];
    [self setCheckCMA:nil];
    [self setCheckTIBA:nil];
    [self setCheckCII:nil];
    [self setCheckBAC:nil];
    [self setCheckBACY:nil];
    [self setCheckGFA:nil];
    [self setCheckBIT:nil];
    [self setCheckCAT:nil];
    [self setCheckCATPLUS:nil];
    [self setCheckEXC:nil];
    [self setCheckGFH:nil];
    [self setCheckGFC:nil];
    [self setCheckGE:nil];
    [self setCheckUltimaVentaMeses:nil];
    [self setCheckUltimaVenteDeA:nil];
    [self setCheckUltimoRetiro:nil];
    [self setResultTableView:nil];
    [self setNoResults:nil];
    [super viewDidUnload];
}

- (IBAction)btnCheckTouched:(UIButton*)sender {
    if (sender.isSelected) {
        [sender setSelected:NO];
    }else{
        [sender setSelected:YES];
    }
}

- (IBAction)MakeQuery:(UIButton*)sender {
    resultsFromQuery = [[NSMutableArray alloc] init];
    [self doQuery];
    if (resultsFromQuery.count != 0) {
        search = sender;
        search.enabled = NO;
        [self loadWebView];
        UIImage *btnImgAlert = [UIImage imageNamed:@"btn_gotoSearch_unpressed"];
        UIImage *btnImgAlertPressed = [UIImage imageNamed:@"btn_gotoSearch_pressed"];
        UIButton *myRightButton = [UIButton buttonWithType:UIButtonTypeCustom];
        [myRightButton setImage:btnImgAlert forState:UIControlStateNormal];
        [myRightButton setImage:btnImgAlertPressed forState:UIControlStateHighlighted];
        myRightButton.frame = CGRectMake(200.0, 5.0, btnImgAlert.size.width, btnImgAlert.size.height);
        [myRightButton addTarget:self action:@selector(returnTapped:) forControlEvents:UIControlEventTouchUpInside];
        UIBarButtonItem *rightButton = [[UIBarButtonItem alloc]initWithCustomView:myRightButton];
        self.navigationItem.rightBarButtonItem = rightButton;
    }else{
        [MBProgressHUD hideHUDForView:self.view animated:YES];
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Advertencia" message:@"La busqueda no dio ningun resultado." delegate:Nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
        [alert show];
    }
    
}

- (void)returnTapped:(id)sender{
    [resultsProspect removeFromSuperview];
    resultsProspect = nil;
    self.navigationItem.rightBarButtonItem = Nil;
    search.enabled = YES;
}

- (void)doQuery{
    
    //resultsFromQuery = [[SCDatabaseManager sharedInstance] GetArrayFromQuery:[NSString stringWithFormat:@"SELECT * FROM %@",AllProspectTable]];
    //return;
    
    NSString *fields = @"nombre, ";
    NSString *values = @"";
    NSString *query = @"";
    bool prevalue = NO;
    int limit = 100;
    
    if (self.noResults.text.length != 0) {
        limit = [self.noResults.text integerValue];
    }
    
    //    if (self.btnEstructura.titleLabel.text != Nil) {
    //        fields = [fields stringByAppendingString:@"estructura, "];
    //        values = [values stringByAppendingString:[NSString stringWithFormat:@"estuctura like('%%%@%%') ",self.btnEstructura.titleLabel.text]];
    //    }
    //
        if (self.txtRetenedor.text.length != 0) {
            fields = [fields stringByAppendingString:@"ret, "];
            values = [values stringByAppendingString:[NSString stringWithFormat:@"ret = '%@' ",self.txtRetenedor.text]];
            prevalue = YES;
        }
    
        if (self.txtUnidadPago.text.length != 0) {
            if (prevalue) {
                fields = [fields stringByAppendingString:@"unidadPago, "];
                values = [values stringByAppendingString:[NSString stringWithFormat:@"and cast(subcartera as integer) = %d ",[self.txtUnidadPago.text integerValue]]];
            }else{
                fields = [fields stringByAppendingString:@"unidadPago, "];
                values = [values stringByAppendingString:[NSString stringWithFormat:@"cast(subcartera as integer) = %d ",[self.txtUnidadPago.text integerValue]]];
                prevalue = YES;
            }
        }
    
    //Prima
    if ([self.txtPrima.text length]>0) {
        if (prevalue) {
            fields = [fields stringByAppendingString:@"prima_emi, "];
            values = [values stringByAppendingString:[NSString stringWithFormat:@"and cast(prima_emi as integer) %@ %d ",self.btnPrima.titleLabel.text,[self.txtPrima.text integerValue]]];
        }else{
            fields = [fields stringByAppendingString:@"prima_emi, "];
            values = [values stringByAppendingString:[NSString stringWithFormat:@"cast(prima_emi as integer) %@ %d ",self.btnPrima.titleLabel.text,[self.txtPrima.text integerValue]]];
            prevalue = YES;
        }
        
    }
    
    //BAS
    
    if ([self.txtSumaBasica.text length]>0) {
        if (prevalue) {
            fields = [fields stringByAppendingString:@"sabas, "];
            values = [values stringByAppendingString:[NSString stringWithFormat:@"and cast(sabas as integer) %@ %d ",self.btnSumaBasica.titleLabel.text,[self.txtSumaBasica.text integerValue]]];
        }else{
            fields = [fields stringByAppendingString:@"sabas, "];
            values = [values stringByAppendingString:[NSString stringWithFormat:@"cast(sabas as integer) %@ %d ",self.btnSumaBasica.titleLabel.text,[self.txtSumaBasica.text integerValue]]];
            prevalue = YES;
        }
    }
    
    //Fondo Reserva Inversion
    
    if ([self.txtFondoReserva.text length]>0) {
        if (prevalue) {
            fields = [fields stringByAppendingString:@"reserva, "];
            values = [values stringByAppendingString:[NSString stringWithFormat:@"and cast(reserva as integer) %@ %d ",self.btnReservaInversion.titleLabel.text,[self.txtFondoReserva.text integerValue]]];
        }else{
            fields = [fields stringByAppendingString:@"reserva, "];
            values = [values stringByAppendingString:[NSString stringWithFormat:@"cast(reserva as integer) %@ %d ",self.btnReservaInversion.titleLabel.text,[self.txtFondoReserva.text integerValue]]];
            prevalue = YES;
        }
    }
    
    //Zona
    
    if (![self.btnZona.titleLabel.text isEqualToString:@"Todas"]) {
        if (prevalue) {
            values = [values stringByAppendingString:[NSString stringWithFormat:@"and zona_prom like('%@') ",self.btnZona.titleLabel.text]];
        }else{
            values = [values stringByAppendingString:[NSString stringWithFormat:@"zona_prom like('%@') ",self.btnZona.titleLabel.text]];
            prevalue = YES;
        }
    }
    
//    //Edad
//    
//    if ([self.txtEdad.text length]>0) {
//        fields = [fields stringByAppendingString:@"edad, "];
//        values = [values stringByAppendingString:[NSString stringWithFormat:@"edad %@ %d ",self.btnEdad.titleLabel.text,[self.txtEdad.text integerValue]]];
//    }else{
//        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Aviso" message:@"Por favor ingresa una edad." delegate:Nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
//        [alert show];
//        return;
//    }
    
    //CheckBoxes
    
    if (self.checkCMA.isSelected) {
        if (prevalue) {
            values = [values stringByAppendingString:[NSString stringWithFormat:@"and cast(cma as integer) = %d ",0]];
        }else{
            values = [values stringByAppendingString:[NSString stringWithFormat:@"cast(cma as integer) = %d ",0]];
            prevalue = YES;
        }
    }
    
    if (self.checkTIBA.isSelected) {
        if (prevalue) {
            values = [values stringByAppendingString:[NSString stringWithFormat:@"and cast(tiba as integer) = %d ",0]];
        }else{
            values = [values stringByAppendingString:[NSString stringWithFormat:@"cast(tiba as integer) = %d ",0]];
            prevalue = YES;
        }
    }
    
    if (self.checkCII.isSelected) {
        if (prevalue) {
            values = [values stringByAppendingString:[NSString stringWithFormat:@"and cast(cii as integer) = %d ",0]];
        }else{
            values = [values stringByAppendingString:[NSString stringWithFormat:@"cast(cii as integer) = %d ",0]];
            prevalue = YES;
        }
    }

    if (self.checkBAC.isSelected) {
        if (prevalue) {
            values = [values stringByAppendingString:[NSString stringWithFormat:@"and cast(bac as integer) = %d ",0]];
        }else{
            values = [values stringByAppendingString:[NSString stringWithFormat:@"cast(bac as integer) = %d ",0]];
            prevalue = YES;
        }
    }

    if (self.checkBACY.isSelected) {
        if (prevalue) {
            values = [values stringByAppendingString:[NSString stringWithFormat:@"and cast(bacy as integer) = %d ",0]];
        }else{
            values = [values stringByAppendingString:[NSString stringWithFormat:@"cast(bacy as integer) = %d ",0]];
            prevalue = YES;
        }
    }

    if (self.checkGFA.isSelected) {
        if (prevalue) {
            values = [values stringByAppendingString:[NSString stringWithFormat:@"and cast(gfa as integer) = %d ",0]];
        }else{
            values = [values stringByAppendingString:[NSString stringWithFormat:@"cast(gfa as integer) = %d ",0]];
            prevalue = YES;
        }
    }
    
    if (self.checkBIT.isSelected) {
        if (prevalue) {
            values = [values stringByAppendingString:[NSString stringWithFormat:@"and cast(bit as integer) = %d ",0]];
        }else{
            values = [values stringByAppendingString:[NSString stringWithFormat:@"cast(bit as integer) = %d ",0]];
            prevalue = YES;
        }
    }
    
    if (self.checkCAT.isSelected) {
        if (prevalue) {
            values = [values stringByAppendingString:[NSString stringWithFormat:@"and cast(bcat as integer) = %d ",0]];
        }else{
            values = [values stringByAppendingString:[NSString stringWithFormat:@"cast(bcat as integer) = %d ",0]];
            prevalue = YES;
        }
    }
    
    if (self.checkCATPLUS.isSelected) {
        if (prevalue) {
            values = [values stringByAppendingString:[NSString stringWithFormat:@"and cast(bcatplus as integer) = %d ",0]];
        }else{
            values = [values stringByAppendingString:[NSString stringWithFormat:@"cast(bcatplus as integer) = %d ",0]];
            prevalue = YES;
        }
    }
    
    if (self.checkEXC.isSelected) {
        if (prevalue) {
            values = [values stringByAppendingString:[NSString stringWithFormat:@"and cast(exc as integer) = %d ",0]];
        }else{
            values = [values stringByAppendingString:[NSString stringWithFormat:@"cast(exc as integer) = %d ",0]];
            prevalue = YES;
        }
    }
    
    if (self.checkGFH.isSelected) {
        if (prevalue) {
            values = [values stringByAppendingString:[NSString stringWithFormat:@"and cast(gfh as integer) = %d ",0]];
        }else{
            values = [values stringByAppendingString:[NSString stringWithFormat:@"cast(gfh as integer) = %d ",0]];
            prevalue = YES;
        }
    }
    
    if (self.checkGFC.isSelected) {
        if (prevalue) {
            values = [values stringByAppendingString:[NSString stringWithFormat:@"and cast(gfc as integer) = %d ",0]];
        }else{
            values = [values stringByAppendingString:[NSString stringWithFormat:@"cast(gfc as integer) = %d ",0]];
            prevalue = YES;
        }
    }
    
    if (self.checkGE.isSelected) {
        if (prevalue) {
            values = [values stringByAppendingString:[NSString stringWithFormat:@"and cast(ge as integer) = %d ",0]];
        }else{
            values = [values stringByAppendingString:[NSString stringWithFormat:@"cast(ge as integer) = %d ",0]];
            prevalue = YES;
        }
    }
    
    //Sexo

    if ([self.btnSexo.titleLabel.text isEqualToString:@"Masculino"]) {
        if (prevalue) {
            values = [values stringByAppendingString:@"and sexo like 'M' "];
        }else{
            values = [values stringByAppendingString:@"sexo like 'M' "];
            prevalue = YES;
        }
    }
    
    if ([self.btnSexo.titleLabel.text isEqualToString:@"Femenino"]) {
        if (prevalue) {
            values = [values stringByAppendingString:@"and sexo like 'F' "];
        }else{
            values = [values stringByAppendingString:@"sexo like 'F' "];
            prevalue = YES;
        }
    }
    
    if (![self.btnEstadoCivil.titleLabel.text isEqualToString:@"Todos"]) {
        if (prevalue) {
            values = [values stringByAppendingString:[NSString stringWithFormat:@"and estado_civil like '%@' ",[self.btnEstadoCivil.titleLabel.text substringToIndex:1]]];
        }else{
            values = [values stringByAppendingString:[NSString stringWithFormat:@"estado_civil like '%@' ",[self.btnEstadoCivil.titleLabel.text substringToIndex:1]]];
            prevalue = YES;
        }
    }
    
    if (self.txtUltimoRetiro.text.length != 0) {
        if (prevalue) {
            values = [values stringByAppendingString:[NSString stringWithFormat:@"and Datetime(fecha_ret_reserv) <= Datetime('%@') and Datetime(fecha_ret_inv) <=  Datetime('%@')",self.txtUltimoRetiro.text,self.txtUltimoRetiro.text]];
        }else{
            values = [values stringByAppendingString:[NSString stringWithFormat:@"Datetime(fecha_ret_reserv) <= Datetime('%@') and Datetime(fecha_ret_inv) <=  Datetime('%@')",self.txtUltimoRetiro.text,self.txtUltimoRetiro.text]];
        }
    }
    
    if (self.txtUltimaVenta.text.length != 0) {
        if (prevalue) {
            values = [values stringByAppendingString:[NSString stringWithFormat:@"and Datetime(ultimo_incr) < Datetime('%@')",self.txtUltimaVenta.text]];
        }else{
            values = [values stringByAppendingString:[NSString stringWithFormat:@"Datetime(ultimo_incr) < Datetime('%@')",self.txtUltimaVenta.text]];
        }
    }
    
    // Datetime(fecha_ret_reserv)<Datetime(%@) and Datetime(fecha_ret_inv)<Datetime(%@)
    // Datetime(ultimo_incr)<Datetime(%@)
    
//
//    if ([self.btnSexo.titleLabel.text isEqualToString:@"Femenino"]) {
//        values = [values stringByAppendingString:@"and sexo = 0 "];
//    }
    
//    //Fuma
//    
//    if ([self.btnSexo.titleLabel.text isEqualToString:@"No"]) {
//        values = [values stringByAppendingString:@"and fuma = 0 "];
//    }
//    
//    if ([self.btnSexo.titleLabel.text isEqualToString:@"Si"]) {
//        values = [values stringByAppendingString:@"and fuma = 1 "];
//    }
    
//    //Municipio
//    
//    if ([self.txtMunicipioEmpresa.text length] > 0) {
//        fields = [fields stringByAppendingString:@"municipio "];
//        values = [values stringByAppendingString:[NSString stringWithFormat:@"and municipio like('%%%@%%') ",self.txtMunicipioEmpresa.text]];
//    }else{
//        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Aviso" message:@"Por favor ingresa un municipio." delegate:Nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
//        [alert show];
//        return;
//    }
    
    
    if (prevalue) {
        query = [NSString stringWithFormat:@"SELECT * FROM %@ WHERE %@ limit 0,%d",AllProspectTable,values,limit];
    }else{
        query = [NSString stringWithFormat:@"SELECT * FROM %@ limit 0,%d",AllProspectTable,limit];
    }
    
    [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    NSLog(@"The query is : %@",query);
    
    resultsFromQuery = [[SCDatabaseManager sharedInstance] GetArrayFromQuery:query];
    
    NSLog(@"Tabla prospeccion : %@",[[SCDatabaseManager sharedInstance] GetArrayFromQuery:[NSString stringWithFormat:@"Select * from %@ limit 0,1",AllProspectTable]]);
    
    NSLog(@"results : %@",resultsFromQuery);
}

#pragma mark webView result Methods

- (void)loadWebView{
    NSString *htmlHead = @"<html>"
    @"<head><style>*{padding:0;margin:0}table{border-collapse:collapse;width:3000px;}table,th, td{border:1px solid black;}th{color:#FFF;background-color: #546E9E;}"
    @"td{padding:10px;text-align: center;vertical-align:middle;}tr.odd {background-color: #EEEEED;}tr.even {background-color: #849ECE;}</style>"
    @"</head>"
    @"<body><table>"
    @"<tr>"
    @"<th>PLAN</th>"
    @"<th>POLIZA</th>"
    @"<th>N. EMPLEADO</th>"
    @"<th>RFC</th>"
    @"<th>NOMBRE</th>"
    @"<th>F. NACIMIENTO</th>"
    @"<th>FUMA</th>"
    @"<th>SEXO</th>"
    @"<th>ESTADO CIVIL</th>"
    @"<th>PRIMA M.</th>"
    @"<th>S.A.BAS</th>"
    @"<th>CMA</th>"
    @"<th>TIBA</th>"
    @"<th>CII </th>"
    @"<th>BAC</th>"
    @"<th>CAT</th>"
    @"<th>BACY</th>"
    @"<th>GFA</th>"
    @"<th>BIT</th>"
    @"<th>EXC</th>"
    @"<th>GFC</th>"
    @"<th>GFH</th>"
    @"<th>GE</th>"
    @"<th>CATP</th>"
    @"<th>ZONA</th>"
    @"<th>CONCEPTO</th>"
    @"<th>SUB-CARTERA</th>"
    @"<th>RETENEDOR</th>"
    @"<th>FORMA PAGO</th>"
    @"<th>SIGNO RESERVA</th>"
    @"<th>RESERVA</th>"
    @"<th>ULTIMO INCREMENTO</th>"
    @"<th>F. EMI.</th>"
    @"<th>F. U. PAGO</th>"
    @"<th>F. RET. RESERVA</th>"
    @"<th>IMPORTE DE RETENCION</th>"
    @"<th>SIGNO INVERSION</th>"
    @"<th>INVERSION</th>"
    @"<th>Incre20</th>"
    @"</tr>";
    
    
    NSString *htmlTail =	@"</table></body>"
    @"</html>";
    
    NSMutableString *html = [[NSMutableString alloc] initWithString:htmlHead];
    for(int x = 0 ; resultsFromQuery.count > x ; x++ ){
        [html appendString:[self parseRowToHtml:resultsFromQuery[x] isEven:((x%2 == 0)? YES:NO)]];
        //html = [html stringByAppendingString:[self parseRowToHtml:resultsFromQuery[x] isEven:((x%2 == 0)? YES:NO)]];
    }
    //html = [html stringByAppendingString:htmlTail];
    [html appendString:htmlTail];
    
    CGRect frame = CGRectZero;
    if (IS_iOS7) {
        frame = CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height - 5);
    }else{
        frame = CGRectMake(self.view.frame.origin.x, self.view.frame.origin.y + 5, self.view.frame.size.width, self.view.frame.size.height - 5);
    }
    
    resultsProspect = [[UIWebView alloc] initWithFrame:frame];
    NSLog(@"resulsProspect.frame : %@ original frame : %@",NSStringFromCGRect(resultsProspect.frame), NSStringFromCGRect(self.view.frame));
    [resultsProspect loadHTMLString:html baseURL:nil];
    resultsProspect.delegate = self;
    resultsProspect.scalesPageToFit = YES;
    
}

-(void)webViewDidFinishLoad:(UIWebView *)webView{
    [MBProgressHUD hideHUDForView:self.view animated:YES];
    [self.view addSubview:resultsProspect];
}

- (NSString*)parseRowToHtml:(NSDictionary*)row isEven:(bool)even{
    NSString *evenString =  even ? @"odd":@"even";
    //plan, Poliza, Nombre, prima_emi, fe_emision, reserva, ret, bas, sabas, cma, tiba, cii, bac, bcat, bcatplus, bacy, gfa, gfc, gfh, bit, ge, exc, inversion
    return [NSString stringWithFormat:@"<tr class=%@><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td><td>%@</td></tr>",evenString,
            row[@"plan"],
            row[@"Poliza"],
            row[@"No_empleado"],
            row[@"RFC"],
            row[@"Nombre"],
            row[@"fecha_nac"],
            row[@"fuma"],
            row[@"sexo"],
            row[@"estado_civil"],
            row[@"prima_emi"],
            row[@"sabas"],
            [row[@"cma"] integerValue] ? @"CMA" : @" ",
            [row[@"tiba"] integerValue] ? @"TIBA" : @" ",
            [row[@"cii"] integerValue] ? @"CII" : @" ",
            [row[@"bac"] integerValue] ? @"BAC" : @" ",
            [row[@"bcat"] integerValue] ? @"CAT" : @" ",
            [row[@"bacy"] integerValue] ? @"BACY" : @" ",
            [row[@"gfa"] integerValue] ? @"GFA" : @" ",
            [row[@"bit"] integerValue] ? @"BIT" : @" ",
            row[@"exc"],
            [row[@"gfc"] integerValue] ? @"GFC" : @" ",
            [row[@"gfh"] integerValue] ? @"GFH" : @" ",
            [row[@"ge"] integerValue] ? @"GE" : @" ",
            [row[@"bcatplus"] integerValue] ? @"CATP" : @" ",
            row[@"zona_prom"],
            row[@"concepto"],
            row[@"subcartera"],
            row[@"ret"],
            row[@"forma_pago"],
            row[@"signo_reserva"],
            row[@"reserva"],
            row[@"ultimo_incr"],
            row[@"fe_emision"],
            row[@"ultimo_pago"],
            row[@"fecha_ret_reserv"],
            row[@"importe_ret_inv"],
            row[@"signo_inversion"],
            row[@"inversion"],
            row[@"incre20"]];
}

#pragma mark -
#pragma mark Date Picker Methods

- (void)textFieldDidBeginEditing:(UITextField *)textField
{
    activeTextField = textField;
    
    // If the date field has focus, display a date picker instead of keyboard.
    // Set the text to the date currently displayed by the picker.
    if (textField.tag == 5)
    {
        [self showPickerWithTextField:self.txtUltimoRetiro];
        [textField resignFirstResponder];
    }
    
    if (textField.tag == 6)
    {
        [self showPickerWithTextField:self.txtUltimaVenta];
        [textField resignFirstResponder];
    }
    
    
}

- (void)showPickerWithTextField:(UITextField*)txtField{
    actionSheet = [[UIActionSheet alloc] initWithTitle:nil
                                              delegate:nil
                                     cancelButtonTitle:nil
                                destructiveButtonTitle:nil
                                     otherButtonTitles:nil];
    
    [actionSheet setActionSheetStyle:UIActionSheetStyleBlackTranslucent];
    
    CGRect pickerFrame = CGRectMake(0, 40, 0, 0);
    
    // Create a date picker for the date field.
    UIDatePicker *datePicker = [[UIDatePicker alloc]initWithFrame:pickerFrame];
    datePicker.datePickerMode = UIDatePickerModeDate;
    datePicker.maximumDate = [NSDate date];
    [datePicker setDate:[NSDate date]];
    [datePicker addTarget:self action:@selector(updateDateField:) forControlEvents:UIControlEventValueChanged];
    
    UIToolbar* numberToolbar = [[UIToolbar alloc]initWithFrame:CGRectMake(0, 0, 320, 50)];
    numberToolbar.barStyle = UIBarStyleBlackTranslucent;
    numberToolbar.items = [NSArray arrayWithObjects:
                           [[UIBarButtonItem alloc]initWithTitle:@"Limpiar" style:UIBarButtonItemStyleBordered target:self action:@selector(dismissActionSheet:)],
                           [[UIBarButtonItem alloc]initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil],
                           [[UIBarButtonItem alloc]initWithTitle:@"Listo" style:UIBarButtonItemStyleDone target:self action:@selector(doneActionSheet:)],
                           nil];
    [numberToolbar sizeToFit];
    
    
    
    if ([UIDevice currentDevice].userInterfaceIdiom != UIUserInterfaceIdiomPad) {
        [actionSheet addSubview:datePicker];
        [actionSheet addSubview:numberToolbar];
        [actionSheet showInView:self.view];
        [actionSheet setBounds:CGRectMake(0, 0, 320, 485)];
    }
    else {
        UIView *view = [[UIView alloc] init];
        
        [view addSubview:numberToolbar];
        [view addSubview:datePicker];
        
        UIViewController *vc = [[UIViewController alloc] init];
        [vc setView:view];
        [vc setContentSizeForViewInPopover:CGSizeMake(320, 285)];
        
        popover = [[UIPopoverController alloc] initWithContentViewController:vc];
        //popover.delegate = self;
        
        
        
        [popover presentPopoverFromRect:txtField.frame inView:self.view
               permittedArrowDirections:UIPopoverArrowDirectionAny animated:YES];
        
        
    }
    
    activeDatePicker = datePicker;
    if (txtField.text.length != 0) {
        [datePicker setDate:[self getDateFromString:txtField.text]];
    }else{
        txtField.text = [self formatDate:datePicker.date];
    }
    
}

- (void)dismissActionSheet:(UIBarButtonItem*)sender{
    activeTextField.text = @"";
}

- (void)doneActionSheet:(UIBarButtonItem*)sender{
    [actionSheet dismissWithClickedButtonIndex:0 animated:YES];
    [activeTextField resignFirstResponder];
}


// Called when the date picker changes.
- (void)updateDateField:(id)sender
{
    if (activeTextField.tag == 5) {
        self.txtUltimoRetiro.text = [self formatDate:activeDatePicker.date];
    }
    if (activeTextField.tag == 6) {
        self.txtUltimaVenta.text = [self formatDate:activeDatePicker.date];
    }
}


// Formats the date chosen with the date picker.
- (NSString *)formatDate:(NSDate *)date
{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateStyle:NSDateFormatterShortStyle];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    NSString *formattedDate = [dateFormatter stringFromDate:date];
    return formattedDate;
}

- (NSDate *)getDateFromString:(NSString *)date
{
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateStyle:NSDateFormatterShortStyle];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    NSDate *formattedDate = [dateFormatter dateFromString:date];
    return formattedDate;
}

@end
