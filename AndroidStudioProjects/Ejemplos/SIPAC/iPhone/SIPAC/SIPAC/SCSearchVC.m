//
//  SCSearchVC.m
//  SIPAC
//
//  Created by Jaguar3 on 19/03/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import "SCSearchVC.h"
#import "SCSearchResultVC.h"
#import "SCDatabaseManager.h"
#import <QuartzCore/QuartzCore.h>

#import "SCManager.h"

@interface SCSearchVC (){
    UITableView *theTableView;
    
    NSMutableArray *results;
    NSTimer *theTimer;
    int lastCount;
    CGRect originalFrame;
    
    UIButton *invisibleButton;
}

@end

@implementation SCSearchVC

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
    if ([[[UIDevice currentDevice] systemVersion] integerValue] < 7) {
        
        [[self.searchBar.subviews objectAtIndex:0] removeFromSuperview];
        [[UINavigationBar appearance] setBackgroundImage:[UIImage imageNamed:@"img_navBar.png"] forBarMetrics:UIBarMetricsDefault];
    }else{
       [self.searchBar setSearchFieldBackgroundImage:[UIImage imageNamed:@"bg_search"] forState:UIControlStateNormal];
        self.searchBar.barTintColor = [UIColor whiteColor];
        self.edgesForExtendedLayout = UIRectEdgeNone;
    }
    
    self.navigationController.navigationBar.topItem.title = @"Consulta de cartera";
    self.searchBar.backgroundColor = [UIColor clearColor];
    self.searchBar.delegate = self;
    originalFrame = CGRectMake(self.searchBar.frame.origin.x+20, self.searchBar.frame.origin.y+7, self.searchBar.frame.size.width-40, 0);
    
    results = [[NSMutableArray alloc] init];
    
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
                                                 name:UIKeyboardDidHideNotification
                                               object:nil];
}

- (void)keyboardWasShown:(id)notification{
    invisibleButton = [[UIButton alloc] initWithFrame:[UIApplication sharedApplication].keyWindow.bounds];
    [invisibleButton addTarget:self action:@selector(hiddenBtnTouched:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:invisibleButton];
    
    if (self.theSwitch.isOn) {
        [theTableView removeFromSuperview];
        [self.view addSubview:theTableView];
    }
}

- (void)keyboardWasHidden:(id)notification{
    [invisibleButton removeFromSuperview];
}

- (void)hiddenBtnTouched:(id)sender{
    [self.searchBar resignFirstResponder];
}

- (void)viewWillDisappear:(BOOL)animated{
    [super viewWillDisappear:YES];
    [theTimer invalidate];
}

- (void)paneMenuTapped:(UIButton*)sender{
    [[SCManager sharedInstance] showPaneMenu];
}

-(void)viewWillAppear:(BOOL)animated{
    self.searchBar.text = @"";
    [SCManager sharedInstance].paneVC.paneDraggingEnabled = YES;
    if (self.theSwitch.on) {
        theTimer = [NSTimer scheduledTimerWithTimeInterval:1 target:self selector:@selector(showSuggestions) userInfo:nil repeats:YES];
    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)SearchPressed:(id)sender {
    
    [self search];
    
    if (results.count == 0) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Aviso" message:@"La busqueda no produjo ningún resultado" delegate:nil cancelButtonTitle:@"Aceptar" otherButtonTitles:nil];
        [alert show];
    }else{
        [SCManager sharedInstance].paneVC.paneDraggingEnabled = NO;
        SCSearchResultVC *searchResultVC = [[SCSearchResultVC alloc] initWithNibName:@"SCSearchResultVC" bundle:nil];
        searchResultVC.searchResult = results;
        [self.navigationController pushViewController:searchResultVC animated:YES];
        searchResultVC.LblResult.text = [NSString stringWithFormat:@"Resultados para : %@",self.searchBar.text];
    }
    
}

- (IBAction)autoSuggest:(id)sender {
    UISwitch *theSwitch = (UISwitch*)sender;
    
    if (theSwitch.on) {
        theTableView = [[UITableView alloc] initWithFrame:originalFrame];
        theTableView.delegate = self;
        theTableView.dataSource = self;
        theTableView.layer.cornerRadius = 5;
        theTableView.layer.borderColor = [UIColor grayColor].CGColor;
        theTableView.layer.borderWidth = 1.0;
        
        [self.view addSubview:theTableView];
       theTimer = [NSTimer scheduledTimerWithTimeInterval:2 target:self selector:@selector(showSuggestions) userInfo:nil repeats:YES]; 
    }else{
        [theTimer invalidate];
        [UIView animateWithDuration:0.3 animations:^(void){
            
            theTableView.frame = originalFrame;
            
        } completion:^(BOOL finisehd){
            if (finisehd) {
                [theTableView removeFromSuperview];
            }
        }];
        lastCount = 0;
        
    }
}

- (void)showSuggestions{
   
    [self search];
    
    if (results.count != lastCount) {
        CGPoint origin = originalFrame.origin;
        CGSize size;
        int numberOfLines = 0;
        if([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
            //Device is ipad
            numberOfLines = 5;
        }else{
            //Device is iphone
            numberOfLines = 3;
        }
        if (results.count <= numberOfLines) {
            size = CGSizeMake(originalFrame.size.width, 44*results.count);
        }else{
            size = CGSizeMake(originalFrame.size.width, 44*numberOfLines);
        }
        
        CGRect newframe = CGRectMake(origin.x, origin.y-size.height, size.width, size.height);
        [UIView animateWithDuration:0.3 animations:^(void){
            
            theTableView.frame = newframe;
            
        }];
        lastCount = results.count;
        
        [theTableView reloadData];
    }else if(results.count == 0){
        
        [UIView animateWithDuration:0.3 animations:^(void){
            
            theTableView.frame = originalFrame;
            
        }];
        lastCount = results.count;
        
        [theTableView reloadData];
    }
    
    
}

- (void)search{
    NSString *searchText = [NSString stringWithFormat:@"%s",[[self normalizedString:self.searchBar.text] UTF8String]];
    
    
    if (![searchText isEqualToString:@""] && searchText.length != 1) {
        NSString *field2 = @"";
        NSString *sign = @"";
        if (([searchText length] < 14 && [searchText length] > 9 && !([searchText rangeOfString:@" "].location == NSNotFound)) == NO)
        { //$campo_rfc = "or rfc like('".$search."%')";
            field2 = @", rfc_poliza";
            sign = @" - ";
        }
        /*else
         { $campo = "nombre";}*/
        
        //$campo2 = ", rfc";
        //$signo = " - ";
        NSString *campo_no_empl = @"";
        if([self isNumeric:searchText]){
            if(searchText.length>0){
                campo_no_empl = [NSString stringWithFormat:@" or emp_poliza = '%@' ",searchText] ;
            }
        }
        
        NSString *search = [searchText stringByReplacingOccurrencesOfString:@" " withString:@"%"];
        NSString *campo_rfc = [NSString stringWithFormat:@"or rfc_poliza like('%%%@%%') ",search];
        NSString *campo_poliza = [NSString stringWithFormat:@"or id_poliza like('%%%@%%') ",searchText];
        
        //NSString *consulta = [NSString stringWithFormat:@"SELECT id_poliza, nombre_poliza %@ FROM Poliza WHERE (nombre_poliza like('%%%@%%') %@%@) limit 0,5",field2,search,campo_rfc,campo_no_empl];
        //consulta;
        
        NSString *consulta = [NSString stringWithFormat:@"SELECT id_poliza, nombre_poliza , rfc_poliza , emp_poliza, prima_poliza , ret_poliza , ult_pago_poliza FROM Poliza WHERE (nombre_poliza like('%%%@%%') %@%@%@) Order by nombre_poliza COLLATE NOCASE ASC limit 0,100",search,campo_rfc,campo_no_empl,campo_poliza];
        
        results = [[SCDatabaseManager sharedInstance] GetArrayFromQuery:[self normalizedString:consulta]];
        
    }else{

        results = [[NSMutableArray alloc] init];
    }
}

- (void)searchBarSearchButtonClicked:(UISearchBar *)searchBar{
    [self.searchBar resignFirstResponder];
    [self SearchPressed:Nil];
}

- (BOOL)isNumeric:(NSString *)code{
    
    NSScanner *ns = [NSScanner scannerWithString:code];
    float the_value;
    if ( [ns scanFloat:&the_value] )
    {
        return YES;
    }
    else {
        return NO;
    }
}

-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return results.count;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = (UITableViewCell*)[tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.textLabel.font = [UIFont systemFontOfSize:14];
    }
    
    NSDictionary *dict = [results objectAtIndex:indexPath.row];
    cell.textLabel.text = [NSString stringWithFormat:@"%@ - %@",[dict objectForKey:@"id_poliza"],[dict objectForKey:@"nombre_poliza"]];
    
    return cell;

}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    NSDictionary *dict = [results objectAtIndex:indexPath.row];
    
    NSString *consulta = [NSString stringWithFormat:@"SELECT id_poliza, nombre_poliza , rfc_poliza , emp_poliza, prima_poliza , ret_poliza , ult_pago_poliza FROM Poliza WHERE (id_poliza like('%%%@%%')) limit 0,1",[dict objectForKey:@"id_poliza"]];
    
    results = [[SCDatabaseManager sharedInstance] GetArrayFromQuery:consulta];
    SCSearchResultVC *searchResultVC = [[SCSearchResultVC alloc] initWithNibName:@"SCSearchResultVC" bundle:nil];
    searchResultVC.searchResult = results;
    [SCManager sharedInstance].paneVC.paneDraggingEnabled = NO;
    [self.navigationController pushViewController:searchResultVC animated:YES];
    searchResultVC.LblResult.text = [NSString stringWithFormat:@"Resultados para : %@",self.searchBar.text];
    
    [theTimer invalidate];
    [UIView animateWithDuration:0.3 animations:^(void){
        theTableView.frame = originalFrame;
    }];
    lastCount = 0;
    
    [theTableView reloadData];
}

- (NSString *)normalizedString:(NSString *)string {
    
    string = [string stringByReplacingOccurrencesOfString:@"à" withString:@"a"];
    string = [string stringByReplacingOccurrencesOfString:@"á" withString:@"a"];
    string = [string stringByReplacingOccurrencesOfString:@"â" withString:@"a"];
    string = [string stringByReplacingOccurrencesOfString:@"ã" withString:@"a"];
    string = [string stringByReplacingOccurrencesOfString:@"ä" withString:@"a"];
    string = [string stringByReplacingOccurrencesOfString:@"å" withString:@"a"];
    string = [string stringByReplacingOccurrencesOfString:@"ǎ" withString:@"a"];
    string = [string stringByReplacingOccurrencesOfString:@"ā" withString:@"a"];
    
    string = [string stringByReplacingOccurrencesOfString:@"è" withString:@"e"];
    string = [string stringByReplacingOccurrencesOfString:@"é" withString:@"e"];
    string = [string stringByReplacingOccurrencesOfString:@"ê" withString:@"e"];
    string = [string stringByReplacingOccurrencesOfString:@"ë" withString:@"e"];
    string = [string stringByReplacingOccurrencesOfString:@"ě" withString:@"e"];
    string = [string stringByReplacingOccurrencesOfString:@"ē" withString:@"e"];
    
    string = [string stringByReplacingOccurrencesOfString:@"ì" withString:@"i"];
    string = [string stringByReplacingOccurrencesOfString:@"í" withString:@"i"];
    string = [string stringByReplacingOccurrencesOfString:@"î" withString:@"i"];
    string = [string stringByReplacingOccurrencesOfString:@"ï" withString:@"i"];
    string = [string stringByReplacingOccurrencesOfString:@"ǐ" withString:@"i"];
    string = [string stringByReplacingOccurrencesOfString:@"ī" withString:@"i"];
    
    string = [string stringByReplacingOccurrencesOfString:@"ò" withString:@"o"];
    string = [string stringByReplacingOccurrencesOfString:@"ó" withString:@"o"];
    string = [string stringByReplacingOccurrencesOfString:@"ô" withString:@"o"];
    string = [string stringByReplacingOccurrencesOfString:@"õ" withString:@"o"];
    string = [string stringByReplacingOccurrencesOfString:@"ö" withString:@"o"];
    string = [string stringByReplacingOccurrencesOfString:@"ǒ" withString:@"o"];
    string = [string stringByReplacingOccurrencesOfString:@"ō" withString:@"o"];
    
    string = [string stringByReplacingOccurrencesOfString:@"ù" withString:@"u"];
    string = [string stringByReplacingOccurrencesOfString:@"ú" withString:@"u"];
    string = [string stringByReplacingOccurrencesOfString:@"û" withString:@"u"];
    string = [string stringByReplacingOccurrencesOfString:@"ü" withString:@"u"];
    string = [string stringByReplacingOccurrencesOfString:@"ǔ" withString:@"u"];
    string = [string stringByReplacingOccurrencesOfString:@"ū" withString:@"u"];
    
    //string = [string uppercaseString];
    
    return string;
}


@end
