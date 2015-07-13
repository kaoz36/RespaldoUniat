//
//  SCContentForPageViewVC.m
//  SIPAC
//
//  Created by Jaguar3 on 25/03/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import "SCContentForPageViewVC.h"
#import "SCDatabaseManager.h"
#import "GridTableViewCell.h"

@interface SCContentForPageViewVC (){
    NSMutableArray *SourceCobertura;
    NSMutableArray *SourceServicioVenta;
    NSMutableArray *SourceServicioInterno;
    NSMutableArray *SourceServicioGeneral;
    UIViewController *landscapeVC;
}

- (IBAction)tableButtonTouched:(id)sender;
- (IBAction)dismissTable:(id)sender;

@end

@implementation SCContentForPageViewVC

@synthesize labelContents,Poliza,parentViewVC;

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
    
    //, id_poliza, status_poliza, ret_poliza, nombre_poliza, rfc_poliza, emp_poliza, sexo_poliza, fuma_poliza, nac_poliza, calle_poliza, ext_poliza, int_poliza, cp_poliza, tel_poliza, pob_poliza, col_poliza, email_poliza, prima_pag_poliza, res_poliza, inv_poliza, prima_pen_poliza, rec_pen_poliza, ult_pago_poliza, prima_emi_poliza, prima_quin_poliza, fec_ini_vig_poliza, fec_ult_mod_poliza, fec_ult_ret_poliza, orden_pago_poliza, Monto_poliza, adicional_poliza, referencia_poliza, prima_poliza, prima_esp_poliza, prima_desc_poliza, cpto_poliza, sub_ret_poliza, cve_cob_poliza, uni_pago_poliza, ult_pago2_poliza, quin_poliza, tipo_mov_poliza
    
    NSMutableArray *result = [[SCDatabaseManager sharedInstance] GetPoliza:Poliza];
    NSDictionary *theInfo = [result objectAtIndex:0];
    
    self.GeneralView.hidden = YES;
    self.Ventas.hidden = YES;
    self.Servicios.hidden = YES;
    self.Cobranza.hidden = YES;
    
    
    if ([labelContents isEqualToString:@"1"]) {
        [self.Servicios removeFromSuperview];
        [self.Ventas removeFromSuperview];
        [self.Cobranza removeFromSuperview];
        self.GeneralView.hidden = NO;
        self.TxtSexo.text = [theInfo objectForKey:@"sexo_poliza"];
        self.TxtFuma.text = [theInfo objectForKey:@"fuma_poliza"];
        NSString *forma = @"";
        switch ([theInfo[@"forma_poliza"] integerValue]) {
            case 1:
                forma = @"Mensual";
                break;
                
            case 3:
                forma = @"Trimestral";
                break;
                
            case 6:
                forma = @"Semestral";
                break;
                
            case 12:
                forma = @"Anual";
                break;
                
            default:
                break;
        }
        self.TxtEstadoCivil.text = forma;//[theInfo objectForKey:@"estado_civil_poliza"];
        self.TxtFechaNac.text = [theInfo objectForKey:@"nac_poliza"];
        
        //"nac_poliza" = "1973-08-22";
        NSString *dateString = theInfo[@"nac_poliza"];
        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
        [dateFormatter setDateStyle:NSDateFormatterShortStyle];
        [dateFormatter setDateFormat:@"yyyy-MM-dd"];
        NSDate *serviceDate = [dateFormatter dateFromString:dateString];
        
        NSCalendar *calendar = [NSCalendar currentCalendar];
        NSUInteger unitFlags = NSYearCalendarUnit;
        NSDateComponents *dateComponents = [calendar components:unitFlags fromDate:serviceDate toDate:[NSDate date] options:0];
        
        self.TxtEdad.text = [NSString stringWithFormat:@"%d",[dateComponents year]];
        self.TxtCalle.text = [theInfo objectForKey:@"domicilio_poliza"];
        self.TxtNoExt.text = [theInfo objectForKey:@"ext_poliza"];
        self.TxtNoInt.text = [theInfo objectForKey:@"int_poliza"];
        self.TxtCP.text = [theInfo objectForKey:@"cp_poliza"];
        self.TxtPoblacion.text = [theInfo objectForKey:@"pob_poliza"];
        self.TxtColonia.text = [theInfo objectForKey:@"estado_civil_poliza"];
        self.TxtTelefono.text = [theInfo objectForKey:@"tel_poliza"];
        self.TxtMail.text = [theInfo objectForKey:@"email_poliza"];
        
        self.TxtMontoPrimaPagada.text = [theInfo objectForKey:@"prima_pag_poliza"];
        NSString *signoReserva = @"";
        if ([theInfo[@"signo_reserva"] isEqualToString:@"P"]) {
            signoReserva = @"+";
        }
        if ([theInfo[@"signo_reserva"] isEqualToString:@"N"]) {
            signoReserva = @"-";
        }
        self.TxtMontoReserva.text = [NSString stringWithFormat:@"(%@)%@", signoReserva,[theInfo objectForKey:@"res_poliza"]];
        
        NSString *signoFondoi = @"";
        if ([theInfo[@"signo_fondoi"] isEqualToString:@"P"]) {
            signoFondoi = @"+";
        }
        if ([theInfo[@"signo_fondoi"] isEqualToString:@"N"]) {
            signoFondoi = @"-";
        }
        self.TxtMontoFondoInv.text = [NSString stringWithFormat:@"(%@)%@", signoFondoi,[theInfo objectForKey:@"inv_poliza"]];
        self.TxtPrimaPendiente.text = [theInfo objectForKey:@"prima_pen_poliza"];
        self.TxtRecibosPendientes.text = [theInfo objectForKey:@"rec_pen_poliza"];
        self.TxtUltimoPago.text = [theInfo objectForKey:@"ult_pago_poliza"];
        self.TxtPrimaEmitida.text = [theInfo objectForKey:@"prima_emi_poliza"];
        self.RecibosQuincenal.text = [theInfo objectForKey:@"prima_quin_poliza"];
        
        self.TxtFechaInicialVig.text = [theInfo objectForKey:@"fec_ini_vig_poliza"];
        self.TxtFechaUltimaMod.text = [theInfo objectForKey:@"fec_ult_mod_poliza"];
        self.TxtFechaUltimoRet.text = [theInfo objectForKey:@"fec_ult_ret_poliza"];
        self.TxtOrdenPagoCheque.text = [theInfo objectForKey:@"orden_pago_poliza"];
        self.TxtAdicional.text = [theInfo objectForKey:@"adicional_poliza"];
        self.TxtMonto.text = [theInfo objectForKey:@"Monto_poliza"];
        
        self.tableViewCobertura.tag = 1;
        self.tableViewCobertura.separatorColor = [UIColor clearColor];
        SourceCobertura = [[SCDatabaseManager sharedInstance] GetCoberturaTableForPoliza:Poliza];
        
        for (UITextField *txtView in self.GeneralView.subviews) {
            if ([txtView isKindOfClass:[UITextField class]]) {
                txtView.textColor = [UIColor colorWithRed:0.47 green:0.47 blue:0.48 alpha:1.0];
            }
        }
    }
    if ([labelContents isEqualToString:@"2"]) {
        [self.GeneralView removeFromSuperview];
        [self.Servicios removeFromSuperview];
        [self.Cobranza removeFromSuperview];
        self.Ventas.hidden = NO;
        
        self.tableViewServicioVenta.tag = 2;
        self.tableViewServicioInterno.tag = 3;
        self.tableViewServicioVenta.separatorColor = [UIColor clearColor];
        self.tableViewServicioInterno.separatorColor = [UIColor clearColor];
        
        SourceServicioVenta = [[SCDatabaseManager sharedInstance] GetServicioVentaTableForPoliza:Poliza];
        SourceServicioInterno = [[SCDatabaseManager sharedInstance] GetVentaInternoTableForPoliza:Poliza];
        
        for (UITextField *txtView in self.Ventas.subviews) {
            if ([txtView isKindOfClass:[UITextField class]]) {
                txtView.textColor = [UIColor colorWithRed:0.47 green:0.47 blue:0.48 alpha:1.0];
            }
        }
    }
    if ([labelContents isEqualToString:@"3"]) {
        [self.GeneralView removeFromSuperview];
        [self.Ventas removeFromSuperview];
        [self.Cobranza removeFromSuperview];
        self.Servicios.hidden = NO;
        
        self.tableViewServicios.tag = 4;
        self.tableViewServicios.separatorColor = [UIColor clearColor];
        
        SourceServicioGeneral = [[SCDatabaseManager sharedInstance] GetServicioGeneralTableForPoliza:Poliza];
        
        for (UITextField *txtView in self.Servicios.subviews) {
            if ([txtView isKindOfClass:[UITextField class]]) {
                txtView.textColor = [UIColor colorWithRed:0.47 green:0.47 blue:0.48 alpha:1.0];
            }
        }
    }
    if ([labelContents isEqualToString:@"4"]) {
        [self.GeneralView removeFromSuperview];
        [self.Ventas removeFromSuperview];
        [self.Servicios removeFromSuperview];
        self.Cobranza.hidden = NO;
        
        self.TxtRefCobBan.text = [theInfo objectForKey:@"referencia_poliza"];
        self.TxtRefAlf.text = [theInfo objectForKey:@"ref_alfa_poliza"];
        self.TxtPrima.text = [theInfo objectForKey:@"prima_poliza"];
        self.TxtPriEsp.text = [theInfo objectForKey:@"prima_esp_poliza"];
        self.TxtPriDesc.text = [theInfo objectForKey:@"prima_desc_poliza"];
        self.TxtConcepto.text = [theInfo objectForKey:@"cpto_poliza"];
        self.TxtSubRet.text = [theInfo objectForKey:@"sub_ret_poliza"];
        self.TxtCveCob.text = [theInfo objectForKey:@"cve_cob_poliza"];
        self.TxtUniPag.text = [theInfo objectForKey:@"uni_pago_poliza"];
        self.TxtUltPag.text = [theInfo objectForKey:@"ult_pago2_poliza"];
        self.TxtQuiMvto.text = [theInfo objectForKey:@"quin_poliza"];
        self.TxtTipMov.text = [theInfo objectForKey:@"tipo_mov_poliza"];
        
        
        for (UITextField *txtView in self.Cobranza.subviews) {
            if ([txtView isKindOfClass:[UITextField class]]) {
                txtView.textColor = [UIColor colorWithRed:0.47 green:0.47 blue:0.48 alpha:1.0];
            }
        }
    }
    
    
    
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark UITableViewDataSourceDelegate

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    switch (tableView.tag) {
        case 1:
            return SourceCobertura.count+1;
            break;
        
        case 2:
            return SourceServicioVenta.count+1;
            break;
            
        case 3:
            return SourceServicioInterno.count+1;
            break;
            
        case 4:
            return SourceServicioGeneral.count+1;
            break;
            
        default:
            return 1;
            break;
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    static NSString *CellIdentifier = @"Cell";
    static NSString *CellIdentifier2 = @"Cell2";
    
    
    GridTableViewCell *cell;
    if (tableView.tag != 3) {
        cell = (GridTableViewCell*)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    }else{
        cell = (GridTableViewCell*)[tableView dequeueReusableCellWithIdentifier:CellIdentifier2];
    }
     
    
    switch (tableView.tag) {
        case 1:
            if (cell == nil) {
                
                cell = [[GridTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier defaultWitdh:tableView.frame.size.width/4 defaultHeight:80 numberOfColumns:4 FontSize:14];
                cell.lineColor = [UIColor blackColor];
            }
            if (indexPath.row == 0){
                cell.topCell = YES;
                [cell setLabelsForCell:@"Cobertura",@"Prima ",@"Prima extra",@"Suma asegurada",nil];
            }else{
                cell.topCell = NO;
                NSDictionary *dic = [SourceCobertura objectAtIndex:indexPath.row-1];
                //id_poliza, id_cob, prima_cob, prima_ext_cob, suma_cob
                //"\"id_poliza\",\"prima_cob\",\"prima_ext_cob\",\"suma_cob\",
                [cell setLabelsForCell:dic[@"clave_cob"],[dic objectForKey:@"prima_cob"],[dic objectForKey:@"prima_ext_cob"],[dic objectForKey:@"suma_cob"],nil];
            }
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            break;
            
        case 2:
            if (cell == nil) {
                cell = [[GridTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier defaultWitdh:tableView.frame.size.width/5 defaultHeight:80 numberOfColumns:5 FontSize:14];
                
                cell.lineColor = [UIColor blackColor];
            }
            if (indexPath.row == 0){
                cell.topCell = YES;
                [cell setLabelsForCell:@"Servicio",@"+/-",@"Prima",@"Fecha de emision",@"Plan",nil];
            }else{
                cell.topCell = NO;
                NSDictionary *dic = [SourceServicioVenta objectAtIndex:indexPath.row-1];
                // id_serv, mas_men_serv, prima_serv, fec_emi_serv, plan_serv
                //"\"id_poliza\",\"id_serv\",\"mas_men_serv\",\"prima_serv\",\"fec_emi_serv\",\"plan_serv\"
                [cell setLabelsForCell:[dic objectForKey:@"id_serv"],[dic objectForKey:@"mas_men_serv"],[dic objectForKey:@"prima_serv"],[dic objectForKey:@"fec_emi_serv"],[dic objectForKey:@"plan_serv"],nil];
            }
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            break;
            
        case 3:
            if (cell == nil) {
                NSDictionary *profile = [[SCDatabaseManager sharedInstance] GetProfile];
                
                if ([profile[@"id_configuracion"] isEqualToString:@"1"]) {
                    cell = [[GridTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier2 defaultWitdh:tableView.frame.size.width/10 defaultHeight:80 numberOfColumns:10 FontSize:12];
                }else{
                    cell = [[GridTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier2 defaultWitdh:tableView.frame.size.width/9 defaultHeight:80 numberOfColumns:9 FontSize:12];
                }
                
                cell.lineColor = [UIColor blackColor];
            }
            if (indexPath.row == 0){
                cell.topCell = YES;
                NSDictionary *profile = [[SCDatabaseManager sharedInstance] GetProfile];
                if ([profile[@"id_configuracion"] isEqualToString:@"1"]) {
                    [cell setLabelsForCell:@"Agente",@"Tipo Negocio",@"Fecha de envio",@"Fecha emi.",@"Fecha ent.",@"Fecha acept.",@"Prima ini.",@"Prima emi.",@"Prima tot.",@"No. Serv.",nil];
                }else{
                    [cell setLabelsForCell:@"Tipo Negocio",@"Fecha de envio",@"Fecha emi.",@"Fecha ent.",@"Fecha acept.",@"Prima ini.",@"Prima emi.",@"Prima tot.",@"No. Serv.",nil];
                }
            }else{
                cell.topCell = NO;
                NSDictionary *dic = [SourceServicioInterno objectAtIndex:indexPath.row-1];
                //id_poliza, id_agente_venta, fec_env_venta, fec_emi_venta, fec_ent_venta, fec_acc_venta, prima_ini_venta, prima_emi_venta, prima_tot_venta, serv_venta
                //\"id_poliza\",\"id_agente_venta\",\"fec_env_venta\",\"fec_emi_venta\",\"fec_ent_venta\",\"fec_acc_venta\",\"prima_ini_venta\",\"prima_emi_venta\",\"prima_tot_venta\",\"serv_venta\"
                NSDictionary *profile = [[SCDatabaseManager sharedInstance] GetProfile];
                if ([profile[@"id_configuracion"] isEqualToString:@"1"]) {
                    [cell setLabelsForCell:[dic objectForKey:@"id_agente_venta"], [dic objectForKey:@"tipo_negocio"],[dic objectForKey:@"fec_env_venta"],[dic objectForKey:@"fec_emi_venta"],[dic objectForKey:@"fec_ent_venta"],[dic objectForKey:@"fec_acc_venta"],[dic objectForKey:@"prima_ini_venta"],[dic objectForKey:@"prima_emi_venta"],[dic objectForKey:@"prima_tot_venta"],[dic objectForKey:@"serv_venta"],nil];
                }else{
                    [cell setLabelsForCell:[dic objectForKey:@"tipo_negocio"],[dic objectForKey:@"fec_env_venta"],[dic objectForKey:@"fec_emi_venta"],[dic objectForKey:@"fec_ent_venta"],[dic objectForKey:@"fec_acc_venta"],[dic objectForKey:@"prima_ini_venta"],[dic objectForKey:@"prima_emi_venta"],[dic objectForKey:@"prima_tot_venta"],[dic objectForKey:@"serv_venta"],nil];
                }
                
            }
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            break;
            
        case 4:
            if (cell == nil) {
                int defaultHeight = 80;
                if(!([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad)) {
                    defaultHeight = 90;
                }
                
                cell = [[GridTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier defaultWitdh:tableView.frame.size.width/6 defaultHeight:defaultHeight numberOfColumns:6 FontSize:14];
                
                cell.lineColor = [UIColor blackColor];
            }
            if (indexPath.row == 0){
                cell.topCell = YES;
                [cell setLabelsForCell:@"Agente",@"Servicio",@"Descripción",@"Orden de Pago", @"Monto",@"Fecha captura",nil];
            }else{
                cell.topCell = NO;
                NSDictionary *dic = [SourceServicioGeneral objectAtIndex:indexPath.row-1];
                //, id_poliza, agente_serv, id_serv, desc_serv, fecha_serv
                [cell setLabelsForCell:[dic objectForKey:@"agente_serv"],[dic objectForKey:@"id_serv"],[dic objectForKey:@"desc_serv"], dic[@"orden_pago"],dic[@"monto"],[dic objectForKey:@"fecha_serv"],nil];
            }
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
            break;
            
        default:
            if (cell == nil) {
                cell = [[GridTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier defaultWitdh:tableView.frame.size.width/4 defaultHeight:80 numberOfColumns:4 FontSize:[UIFont systemFontSize]];
                cell.lineColor = [UIColor blackColor];
            }
            break;
    }
	// Since we are drawing the lines ourself, we need to know which cell is the top cell in the table so that
	// we can draw the line on the top
    
    if (indexPath.row%2 == 0) {
        [cell setChangeColor:YES];
    }else{
        [cell setChangeColor:NO];
    }
    
    //[cell setLabelsForCell:@"Columna 1",@"Columna 2",@"Columna 3",@"Columna4",nil];
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    int defaultHeight = 80;
    if (tableView.tag == 4) {
        if(!([UIDevice currentDevice].userInterfaceIdiom == UIUserInterfaceIdiomPad)) {
            defaultHeight = 90;
        }
    }
    return defaultHeight;
}


- (void)viewDidUnload {
    [self setGenericTableView:nil];
    [super viewDidUnload];
}



- (IBAction)tableButtonTouched:(id)sender {
    UIButton *senderButton = (UIButton*)sender;
    //self.genericTableView.frame = CGRectMake(0, 0, self.genericTableView.frame.size.width, self.genericTableView.frame.size.height);
    for (UIView *subview in self.genericTableView.subviews) {
        if ([subview isKindOfClass:[UITableView class]]) {
            subview.tag = senderButton.tag;
            [(UITableView*)subview reloadData];
        }
    }
    
    
    landscapeVC = [[UIViewController alloc] init];
    landscapeVC.view = self.genericTableView;
    [self.parentViewVC.navigationController presentModalViewController:landscapeVC animated:YES];
    landscapeVC.view.frame = CGRectMake(0, 0, [UIApplication sharedApplication].keyWindow.frame.size.height, [UIApplication sharedApplication].keyWindow.frame.size.width);
    landscapeVC.view.transform = CGAffineTransformMakeRotation(M_PI/2);
    landscapeVC.view.frame = CGRectMake(0,0, landscapeVC.view.frame.size.width, landscapeVC.view.frame.size.height);
    [[UIApplication sharedApplication] setStatusBarHidden:YES withAnimation:UIStatusBarAnimationFade];
    
}

- (IBAction)dismissTable:(id)sender {
    [[UIApplication sharedApplication] setStatusBarHidden:NO withAnimation:UIStatusBarAnimationFade];
    [landscapeVC dismissModalViewControllerAnimated:YES];
}
@end
