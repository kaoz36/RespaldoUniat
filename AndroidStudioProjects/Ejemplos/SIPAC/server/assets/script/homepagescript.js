/* Script principal  del portal, pirmero es necesariop definir variables globales para el portal , en este caso es un objeto de variables globales
  y una funcion  por default para manejar el fallo de un servicio*/

var globalVars = {},
	defaultErrorHandler = function(){eventControl.eventDispatcher().trigger('connectionError');};

	
/*Cada uno de los objetos que se utilizan  se implementan a traves de una closure de javascript
 este primer objeto sirve para administrar los eventos  genericos que se mandan a traves de jquery 
 para que la aplicacon realice las acciones correspondientes*/
var eventControl= (function(){
					/*dentro de este objeto se definen las acciones por evento a ser ejecutadas */
					var handlers = { facebookConnected: function(event,facebookInfo){
															userControl.clearUserInfo();
															userControl.getUserInfo()['id'] 	= facebookInfo.id;
															userControl.getUserInfo()['name'] 	= facebookInfo.first_name;
															userControl.getUserInfo()['email']	= facebookInfo.email;
															userControl.userLogin();
															},
										 facebookDisconnected: function (event){
																$('#loginLink').unbind('click').html('<img  class="horizontalItem" src="'+globalVars.controlBaseURL+'assets/img/facebook.png"></img><p class="horizontalItem" >Ingresa Aqui</p>').bind('click',facebookLogin);
																$('#idCard').html('<p class="horizontalItem" >Sitio Michel</p>');
																},
										 connectionError: function(event){alert("Error de Conexion");},

										 loginSuccess:function(event){
																$('#loginLink').unbind('click').html('<img  class="horizontalItem" src="'+globalVars.controlBaseURL+'assets/img/exit.png"></img><p class="horizontalItem" >Salir</p>').bind('click',facebookLogout);
																$('#idCard').html('<img  class="horizontalItem" src="https://graph.facebook.com/'+parseInt(userControl.getUserInfo()['id'],10)+'/picture"></img><p class="horizontalItem" >'+ userControl.getUserInfo()['name']+': $'+userControl.getUserInfo()['coins']+'</p>');
																}

										};
					
					/*en el return  se obtienen las funciones publicas que son accesibles dentro de cada parte del portal los scripts adicionales de las secciones
					 también tendran acceso a estos metodos*/
					return { addHandlers: function(pEventHandler){
												for(var i in handlers){$(pEventHandler).bind(i,handlers[i]);}
												this.appEventDispatcher = pEventHandler;
										},
							 eventDispatcher:function(){
							  				    return $(this.appEventDispatcher);
							  			},
							 addEvents:function(eventsObject){
							  					this.removeEvents(eventsObject);
							  					for(var i in eventsObject){
							  						this.eventDispatcher().bind(i,eventsObject[i]);
							  					}

							  			},
							 removeEvents:function(eventsObject){
							  				for(var i in eventsObject){
							  						this.eventDispatcher().unbind(i,eventsObject[i]);
							  					}
							  			},
							 addUIEvents:function(uiItems){
							  				this.removeUIEvents(uiItems);
							  				for(var i in uiItems){
							  						$(uiItems[i].selector).bind(uiItems[i].eventType,uiItems[i].handler);
							  					}
							  			},
							 removeUIEvents:function(uiItems){
							   				for(var i in uiItems){
							  						$(uiItems[i].selector).unbind(uiItems[i].eventType,uiItems[i].handler);
							  					}
							   			}				
						   };
				  }());

/*Este objeto  se encarga de manejar las operaciones  de Usuario  y de las llamadas a servidor relacionadas*/
var userControl=(function(){
				var userInfo = {};
				return {getUserInfo:function(){return userInfo;},
						clearUserInfo:function(){userInfo = {};},
						userLogin:function(){
											$.ajax({type:'POST',
												url:globalVars.controlBaseURL+'usuariocontroller/usuarioLogin',
												dataType:'json',
												data:userInfo,
												error:defaultErrorHandler,
												success:function(result,status){
																	if(result.length > 0)
																	{
																		userInfo = {}
																		userInfo['id'] = result[0].usuario_id;
																		userInfo['name'] = result[0].nombre_usuario;
																		userInfo['email'] = result[0].email_usuario;
																		userInfo['coins'] = result[0].usuario_coins;
																		eventControl.eventDispatcher().trigger('loginSuccess');
																	}
																}
													});

										}
						};
				}());

/* Este objeto se encarga de  manejar las secciones del menu, ademas de controlar las llamadas al servicio de secciones*/
var sectionControl=(function(){
			var sections,sectionsLoadedCallback,
				sectionsLoaded = function(result,status){
														
															var menuString = "";
															for (var it in result)
															{
																menuString += "<a class='horizontalItem iconButton menuButton' data='"+result[it]+"' ><p>"+it+"</p></a> ";
															}
															$('#sectionMenu').html(menuString);
															$('.menuButton').unbind('click').bind('click',function(event){
																												resultClosure.loadSectionPage($(this).attr("data"));
																										});
															resultClosure.loadSectionPage($($(".menuButton")[0]).attr("data"));
															sectionsLoadedCallback();
														},
				resultClosure={getSections:function(){return sections;},
								loadSections:function(pCallback){
												sectionsLoadedCallback = pCallback;
												$.ajax({type:'POST',
														url:globalVars.controlBaseURL+'sectioncontroller/getSections',
														dataType:'json',
														error:defaultErrorHandler,
														success:sectionsLoaded
														});
											},
								loadSectionPage:function(page){
												$.ajax({type:'POST',
														url:globalVars.controlBaseURL+'sectioncontroller/loadSection',
														dataType:'text',
														data:{section:page},
														error:defaultErrorHandler,
														success:function(result,status){
																	$("#mainContent").html(result);
																}
													});
											}
							};

				return resultClosure;	
				}());