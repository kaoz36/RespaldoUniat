//
//  SCSearchResultVC.m
//  SIPAC
//
//  Created by Jaguar3 on 19/03/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import "SCSearchResultVC.h"
#import "GridTableViewCell.h"
#import "SCDetailVC.h"
#import "SCDatabaseManager.h"

@interface SCSearchResultVC ()
@end

@implementation SCSearchResultVC

@synthesize searchResult;

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
    
    if ([[[UIDevice currentDevice] systemVersion] integerValue] == 7){
         self.edgesForExtendedLayout = UIRectEdgeNone;
    }
    //searchResult = [[SCDatabaseManager sharedInstance] GetPoliza:@"AAR073"];
    //DebugLog(@"searchResult : %@",searchResult);
    
        UIImage *myImage = [UIImage imageNamed:@"btn_back_unpressed"];
        UIImage *myImage2 = [UIImage imageNamed:@"btn_back_pressed"];
        UIButton *myLeftButton = [UIButton buttonWithType:UIButtonTypeCustom];
        [myLeftButton setImage:myImage forState:UIControlStateNormal];
        [myLeftButton setImage:myImage2 forState:UIControlStateHighlighted];
        myLeftButton.frame = CGRectMake(0.0, 5.0, myImage.size.width, myImage.size.height);
        [myLeftButton addTarget:self action:@selector(backTapped:) forControlEvents:UIControlEventTouchUpInside];
        UIBarButtonItem *leftButton = [[UIBarButtonItem alloc] initWithCustomView:myLeftButton];
        self.navigationItem.leftBarButtonItem = leftButton;
        
        self.title = @"Resultados";
    
    
    //self.LblResult.text = [NSString stringWithFormat:@"Resultados para : %@",@"AAR073"];
        
}

- (void)backTapped:(id)sender{
    [self.navigationController popViewControllerAnimated:YES];
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
    return searchResult.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = (UITableViewCell*)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if (cell == nil) {
        
        NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"TableViewCell" owner:self options:nil];
        if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
            //Device is ipad
            cell = [topLevelObjects objectAtIndex:0];
        }else{
            //Device is iphone
            cell = [topLevelObjects objectAtIndex:1];
        }
        
        
    }
    
   
    
    
    NSDictionary *dic = [searchResult objectAtIndex:indexPath.row];
    
    UILabel *tmpLabelPoliza = (UILabel*)[cell viewWithTag:1];
    tmpLabelPoliza.text = [dic objectForKey:@"id_poliza"];
    
    UILabel *tmpLabelNombre = (UILabel*)[cell viewWithTag:2];
    tmpLabelNombre.text = [dic objectForKey:@"nombre_poliza"];
    
    UILabel *tmpLabelUnidadPago = (UILabel*)[cell viewWithTag:3];
    tmpLabelUnidadPago.text = [dic objectForKey:@"emp_poliza"];
    
    UILabel *tmpLabelRFC = (UILabel*)[cell viewWithTag:4];
    tmpLabelRFC.text = [dic objectForKey:@"rfc_poliza"];
    
    UILabel *tmpLabelPrima = (UILabel*)[cell viewWithTag:5];
    tmpLabelPrima.text = [dic objectForKey:@"prima_poliza"];
    
    UILabel *tmpLabelRetenedor = (UILabel*)[cell viewWithTag:6];
    tmpLabelRetenedor.text = [dic objectForKey:@"ret_poliza"];
    
    UILabel *tmpLabelUltQuin = (UILabel*)[cell viewWithTag:7];
    tmpLabelUltQuin.text = [dic objectForKey:@"ult_pago_poliza"];
    
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
        //Device is ipad
        return 91.0;
    }else{
        //Device is iphone
        return 148.0;
    }
    
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
   
    SCDetailVC *detailVC = nil;
    
    if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
        //Device is ipad
        detailVC = [[SCDetailVC alloc] initWithNibName:@"SCDetailVC" bundle:nil];
    }else{
        //Device is iphone
        detailVC = [[SCDetailVC alloc] initWithNibName:@"SCDetailVCIphone" bundle:nil];
    }
    
    NSDictionary *dict = [searchResult objectAtIndex:indexPath.row];
    detailVC.Poliza = [dict objectForKey:@"id_poliza"];
    [self.navigationController pushViewController:detailVC animated:YES];
    
}



@end
