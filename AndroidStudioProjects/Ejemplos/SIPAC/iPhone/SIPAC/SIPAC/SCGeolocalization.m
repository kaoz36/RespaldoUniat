//
//  SCGeolocalization.m
//  SIPAC
//
//  Created by Jaguar3 on 08/05/13.
//  Copyright (c) 2013 Jaguar3. All rights reserved.
//

#import "SCGeolocalization.h"
#import "SCManager.h"
#import <QuartzCore/QuartzCore.h>
#import "SCDatabaseManager.h"

#import "SCCustomPin.h"

@interface SCGeolocalization (){
    CLLocationCoordinate2D touchMapCoordinate;
    UIButton *invisibleButton;
    bool located;
    BOOL iphoneListPresented;
    
    NSMutableArray *locationArray;
    SCPopoverVC *pop;
    NSMutableArray *pinsArray;
}

@property (weak, nonatomic) IBOutlet UIImageView *imgTuto;
@property (weak, nonatomic) IBOutlet MKMapView *theMapView;

@property (weak, nonatomic) IBOutlet UITextField *txtName;
@property (weak, nonatomic) IBOutlet UITextField *txtRetenedor;
@property (weak, nonatomic) IBOutlet UITextField *txtUpago;
@property (weak, nonatomic) IBOutlet UITextField *txtProspectos;
@property (weak, nonatomic) IBOutlet UITextField *txtZona;
@property (weak, nonatomic) IBOutlet UITextField *txtDireccion;

@property (strong, nonatomic) UIPopoverController* popover;

@property (strong, nonatomic) IBOutlet UIView *LocationView;

- (IBAction)accept:(id)sender;
- (IBAction)Cancel:(id)sender;

@end

@interface MKMapView (UIGestureRecognizer)

// this tells the compiler that MKMapView actually implements this method
- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch;

@end

@implementation SCGeolocalization

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
    self.theMapView.showsUserLocation = YES;
  
        UIImage *btnImgAlert = [UIImage imageNamed:@"btn_sidemenu_unpressed"];
        UIImage *btnImgAlertPressed = [UIImage imageNamed:@"btn_sidemenu_pressed"];
        UIButton *myleftButton = [UIButton buttonWithType:UIButtonTypeCustom];
        [myleftButton setImage:btnImgAlert forState:UIControlStateNormal];
        [myleftButton setImage:btnImgAlertPressed forState:UIControlStateHighlighted];
        myleftButton.frame = CGRectMake(200.0, 10.0, btnImgAlert.size.width, btnImgAlert.size.height);
        [myleftButton addTarget:self action:@selector(paneMenuTapped:) forControlEvents:UIControlEventTouchUpInside];
        UIBarButtonItem *leftButton = [[UIBarButtonItem alloc]initWithCustomView:myleftButton];
        self.navigationItem.leftBarButtonItem = leftButton;
        
        UIImage *btnImgGeoList = [UIImage imageNamed:@"btn_geolist_unpressed"];
        UIImage *btnImgGeoListPressed = [UIImage imageNamed:@"btn_geolist_pressed"];
        UIButton *myRightButton = [UIButton buttonWithType:UIButtonTypeCustom];
        [myRightButton setImage:btnImgGeoList forState:UIControlStateNormal];
        [myRightButton setImage:btnImgGeoListPressed forState:UIControlStateHighlighted];
        myRightButton.frame = CGRectMake(200.0, 5.0, btnImgGeoList.size.width, btnImgGeoList.size.height);
        [myRightButton addTarget:self action:@selector(geoListTapped:) forControlEvents:UIControlEventTouchUpInside];
        UIBarButtonItem *rightButton = [[UIBarButtonItem alloc]initWithCustomView:myRightButton];
        self.navigationItem.rightBarButtonItem = rightButton;
        
        self.title = @"Geolocalización";
    
    [SCWebServicesMgr WSGetLocationsWithDelegate:self];
    
    
    UILongPressGestureRecognizer *lpgr = [[UILongPressGestureRecognizer alloc]
                                          initWithTarget:self action:@selector(handleLongPress:)];
    lpgr.minimumPressDuration = 1.5; //user needs to press for 2 seconds
    [_theMapView addGestureRecognizer:lpgr];
    
    
    located = NO;
    if ([[[UIDevice currentDevice] systemVersion] integerValue] == 7) {
    self.edgesForExtendedLayout = UIRectEdgeNone;
    }
}

-(void)didGetResponse:(id)response inConnectionTag:(int)tag{
    NSDictionary *positions = response;
    if (positions != NULL) {
        [self addNewPin:positions];
    }
    
    self.imgTuto.hidden = NO;
    [NSTimer scheduledTimerWithTimeInterval:4.0 target:self selector:@selector(changeAlpha:) userInfo:nil repeats:NO];
}

- (void)changeAlpha:(id*)sender{
    [UIView animateWithDuration:2.0 animations:^(void){
        self.imgTuto.alpha = 0.0;
    } completion:^(BOOL finished){
        if (finished) {
            self.imgTuto.hidden = YES;
        }
    }];
   
}

- (void)paneMenuTapped:(UIButton*)sender{
    if ([self.popover isPopoverVisible]) {
        [self.popover dismissPopoverAnimated:YES];
    }
    [[SCManager sharedInstance] showPaneMenu];
}

- (void)geoListTapped:(UIButton*)sender{
    
    if (self.popover != NULL && [self.popover isPopoverVisible]) {
        [self.popover dismissPopoverAnimated:YES];
        self.popover = NULL;
    }else{
        if (iphoneListPresented) {
            
            for (UIView *view in self.view.subviews) {
                if (view.tag == 999) {
                    CGRect frame = view.frame;
                    frame.origin = CGPointMake(0, self.view.frame.size.height);
                    frame.origin = CGPointMake(0,self.view.frame.size.height);
                    [UIView animateWithDuration:0.3 animations:^(void){
                        view.frame = frame;
                    } completion:^(BOOL finished){
                        if (finished) {
                            [view removeFromSuperview];
                        }
                    }];
                    iphoneListPresented = NO;
                }
            }
        }else{
            pop = [[SCPopoverVC alloc] initWithLocationArray:locationArray];
            pop.view.tag = 999;
            pop.delegate=self;
            
            if ([UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
                //is ipad
                self.popover = [[UIPopoverController alloc]initWithContentViewController:pop];
                self.popover.popoverContentSize=pop.view.frame.size;
                [self.popover presentPopoverFromBarButtonItem:self.navigationItem.rightBarButtonItem permittedArrowDirections:UIPopoverArrowDirectionAny animated:YES];
            }else{
                //is iphone/ipod
                pop.view.frame = CGRectMake(0, self.view.frame.size.height, self.view.frame.size.width, self.view.frame.size.height);
                [self.view addSubview:pop.view];
                CGRect frame = pop.view.frame;
                frame.origin = CGPointMake(0,0);
                [UIView animateWithDuration:0.5 animations:^(void){
                    pop.view.frame = frame;
                }];
                iphoneListPresented = YES;
            }
        }
    }
    
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewDidUnload {
    [self setTheMapView:nil];
    [self setLocationView:nil];
    [self setTxtName:nil];
    [self setTxtRetenedor:nil];
    [self setTxtUpago:nil];
    [self setTxtProspectos:nil];
    [self setImgTuto:nil];
    [self setTxtZona:nil];
    [self setTxtDireccion:nil];
    [super viewDidUnload];
}

#pragma mark MapView Methods

- (void)addNewPin:(NSDictionary*)pinInfo{

//    {
//        altitude = "-103.379210606217";
//        direccion = "Las flores|123|45125|las granjas";
//        "id_promotoria" = 35;
//        latitude = "20.7415042717309";
//       --- nombre = "sucursal 3";
//       --- prospectos = 123;
//       --- retenedor = 1245;
//       --- upago = 1245;
//    },
    
    NSDictionary *data = [pinInfo objectForKey:@"result"];
    locationArray = [[NSMutableArray alloc] init];
    pinsArray = [[NSMutableArray alloc] init];
    
    for (NSDictionary *row in data) {
        NSNumber * latitude = [row objectForKey:@"latitude"];
        NSNumber * longitude = [row objectForKey:@"altitude"];
        NSString * Name = [NSString stringWithFormat:@"%@ (%@ prospectos)",row[@"nombre"],row[@"prospectos"]];
        NSString * address = [NSString stringWithFormat:@"Retenedor : %@, U. de pago : %@",row[@"retenedor"],row[@"upago"]];
        NSString * placeId = row[@"id_promotoria"];
        
        [locationArray addObject:row];
        
        CLLocationCoordinate2D coordinate;
        coordinate.latitude = latitude.doubleValue;
        coordinate.longitude = longitude.doubleValue;
        SCCustomPin *annotation = [[SCCustomPin alloc] initWithName:Name address:address placeId:placeId coordinate:coordinate] ;
        [_theMapView addAnnotation:annotation];
        
        [pinsArray addObject:annotation];
	}
    
}

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation {
    static NSString *identifier = @"MyLocation";
    if ([annotation isKindOfClass:[SCCustomPin class]]) {
        MKAnnotationView *annotationView = (MKAnnotationView *) [_theMapView dequeueReusableAnnotationViewWithIdentifier:identifier];
        if (annotationView == nil) {
            annotationView = [[MKAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:identifier];
            UIButton *disclosure = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
            UIButton *checkInButton = [[UIButton alloc] initWithFrame:disclosure.frame];
            [checkInButton setImage:[UIImage imageNamed:@"icon_checkin"] forState:UIControlStateNormal];
            annotationView.rightCalloutAccessoryView = checkInButton;
            annotationView.enabled = YES;
            annotationView.canShowCallout = YES;
            //[annotationView setPinColor:MKPinAnnotationColorGreen];
            //annotationView.animatesDrop = YES;

            annotationView.image = [UIImage imageNamed:@"img_pin_icon.png"];//here we use a nice image instead of the default pins
            annotationView.centerOffset = CGPointMake(-10, 40);
        } else {
            annotationView.annotation = annotation;
        }
        
        return annotationView;
    }
    
    
    return nil;
}

- (void)mapView:(MKMapView *)mapView didUpdateUserLocation:(MKUserLocation *)userLocation {
    if (userLocation.location.coordinate.latitude != 0.0 && userLocation.location.coordinate.longitude != 0.0) {
        if (!located) {
            CLLocationCoordinate2D coord1 = CLLocationCoordinate2DMake(userLocation.location.coordinate.latitude, userLocation.location.coordinate.longitude);
            [self centerMapPoint:coord1 span:0.06];
            
            located = YES;
        }
    }
}

-(void)mapView:(MKMapView *)mapView annotationView:(MKAnnotationView *)view calloutAccessoryControlTapped:(UIControl *)control{
    if ([view.annotation isKindOfClass:[SCCustomPin class]]) {
        SCCustomPin *customView = (SCCustomPin*)view.annotation;
        [customView makeCheckIn];
    }
}

- (void)centerMapPoint:(CLLocationCoordinate2D)coords span:(float)theSpan{
    MKCoordinateSpan span = {.latitudeDelta = theSpan, .longitudeDelta = theSpan};
    MKCoordinateRegion region = {coords, span};
    
    [self.theMapView setRegion:region animated:YES];
}

#pragma mark NewLocation Methods

- (void)handleLongPress:(UIGestureRecognizer *)gestureRecognizer
{
    
    if (gestureRecognizer.state != UIGestureRecognizerStateBegan)
        return;
    CGPoint touchPoint = [gestureRecognizer locationInView:_theMapView];
    touchMapCoordinate =
    [_theMapView convertPoint:touchPoint toCoordinateFromView:_theMapView];
    
    if(![UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
        //Device is iphone
        CGRect frame = self.LocationView.frame;
        frame.origin = CGPointMake(10, 15);
        self.LocationView.frame = frame;
    }
    
    self.LocationView.layer.cornerRadius = 5;
    [self.LocationView setHidden:NO];
    [self pulse:self.LocationView];
    
}

- (void) pulse:(UIView*)theView;
{
	// pulse animation thanks to:  http://delackner.com/blog/2009/12/mimicking-uialertviews-animated-transition/
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

- (IBAction)accept:(id)sender {
    
    if ([self.txtName.text isEqualToString:@""] || [self.txtProspectos.text isEqualToString:@""] || [self.txtRetenedor.text isEqualToString:@""] || [self.txtUpago.text isEqualToString:@""] || [self.txtZona.text isEqualToString:@""] || [self.txtDireccion.text isEqualToString:@""]) {
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"" message:@"Por favor, no dejes campos vacios." delegate:nil cancelButtonTitle:@"Aceptar" otherButtonTitles:nil];
        [alert show];
        return;
    }
    
    //{id_ubicacion:"",zona:"",nombre:"",retenedor:"",upago:"",prospectos:"",altitude:"",latitude:"",direccion:"",promotoria:""}
    NSDictionary *profile = [[SCDatabaseManager sharedInstance] GetProfile];
    NSDictionary *newLocation = @{@"nombre":self.txtName.text,@"retenedor":self.txtRetenedor.text,@"upago":self.txtUpago.text,@"zona":self.txtZona.text,@"direccion":self.txtDireccion.text,@"prospectos":self.txtProspectos.text,@"altitude":[NSString stringWithFormat:@"%f",touchMapCoordinate.longitude],@"latitude":[NSString stringWithFormat:@"%f",touchMapCoordinate.latitude],@"promotoria_id": [profile objectForKey:@"promotoria"]};
    [SCWebServicesMgr WSSetNewLocation:newLocation Delegate:nil];
    
    [locationArray addObject:newLocation];
    
    //NSString *address = [NSString stringWithFormat:@"%@, %@, %@",self.txtRetenedor.text,self.txtUpago.text,self.txtProspectos.text];
    SCCustomPin *annot = [[SCCustomPin alloc] initWithName:self.txtName.text address:self.txtDireccion.text placeId:@"" coordinate:touchMapCoordinate];
    [_theMapView addAnnotation:annot];
    
    [pinsArray addObject:annot];
    
    self.LocationView.hidden = YES;
    self.txtName.text = @"";
    self.txtProspectos.text = @"";
    self.txtRetenedor.text = @"";
    self.txtUpago.text = @"";
    self.txtDireccion.text = @"";
    self.txtZona.text = @"";
}

- (IBAction)Cancel:(id)sender {
    self.LocationView.hidden = YES;
    for (UITextField *textBox in self.LocationView.subviews) {
        if ([textBox isKindOfClass:[UITextField class]]) {
            [textBox resignFirstResponder];
            textBox.text = @"";
        }
    }

}

- (BOOL)textFieldShouldReturn:(UITextField *)textField{
    [textField resignFirstResponder];
    [invisibleButton removeFromSuperview];
    return YES;
}

- (BOOL)textFieldShouldBeginEditing:(UITextField *)textField{
    
    invisibleButton = [[UIButton alloc] initWithFrame:self.view.frame];
    [invisibleButton addTarget:self action:@selector(btnTouched:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:invisibleButton];
    
    if(![UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
        if (textField.tag == 1) {
            CGRect frame = self.LocationView.frame;
            frame.origin = CGPointMake(10, -40);
            [UIView animateWithDuration:0.2 animations:^(void){
                self.LocationView.frame = frame;
            }];
        }
        
        if (textField.tag == 2) {
            CGRect frame = self.LocationView.frame;
            frame.origin = CGPointMake(10, -60);
            [UIView animateWithDuration:0.2 animations:^(void){
                self.LocationView.frame = frame;
            }];
        }
        
        if (textField.tag == 3) {
            CGRect frame = self.LocationView.frame;
            frame.origin = CGPointMake(10, -120);
            [UIView animateWithDuration:0.2 animations:^(void){
                self.LocationView.frame = frame;
            }];
        }
    }
    
    return YES;
}

- (void)btnTouched:(UIButton*)sender{
    for (UITextField *textBox in self.LocationView.subviews) {
        if ([textBox isKindOfClass:[UITextField class]]) {
            [textBox resignFirstResponder];
        }
    }
    [invisibleButton removeFromSuperview];
    if(![UIDevice currentDevice].userInterfaceIdiom==UIUserInterfaceIdiomPad) {
        CGRect frame = self.LocationView.frame;
        frame.origin = CGPointMake(10, 15);
        [UIView animateWithDuration:0.2 animations:^(void){
            self.LocationView.frame = frame;
        }];
    }
}

#pragma mark SCPopoverVCDelegate

-(void)didSelectPopoverRowAtIndex:(NSInteger)index{
    
    [self geoListTapped:nil];
    
    NSDictionary *row = [locationArray objectAtIndex:index];
    
    
    NSNumber * latitude = [row objectForKey:@"latitude"];
    NSNumber * longitude = [row objectForKey:@"altitude"];
    
    CLLocationCoordinate2D coordinate;
    coordinate.latitude = latitude.doubleValue;
    coordinate.longitude = longitude.doubleValue;
    [self centerMapPoint:coordinate span:0.03];
    
    SCCustomPin *tmpCustomPin = (SCCustomPin*)[pinsArray objectAtIndex:index];
    
    [self.theMapView selectAnnotation:tmpCustomPin animated:YES];
}

@end
