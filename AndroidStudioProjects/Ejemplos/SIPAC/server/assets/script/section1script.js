/*El script para cada seccion es similar  se componen principalmete de  3 partes:

En la primera parte se definen funciones y variables globales para utilizarlas en la seccion, aqui es importante 
cuidar no sobreescribir las variables del portal principal*/

var faultHandler = function(result){
	alert (result.error);
}

/*En la siguiente parte se definen los eventos de control los cuales son los que se envian a traves de jquery en el ejemplo de abajo 
se definen dos eventos de control adicionales que se utilizaran een el portal , el primero sirve para guardar un score. Esta
 funcion  utiliza objetos definidos en  los scripst principales del portal*/
var controlEvents={registerScore:function(event){
												scoreControl.registerScore({usuarioId:userControl.getUserInfo().id,
																			juegoId:1,
																		    score:(Math.round(1000*Math.random()))},
																		    function(result){eventControl.eventDispatcher().trigger('getUserScores');},
																		    faultHandler);
											},
												getUserScores:function(event){
																			alert ('Score guardado exitosamente');
											}
};


/*La tercera parte se encarga de definir los eventos graficos, estos eventos son  por ejemplo el click de un boton o 
al  escribir con el teclado en algun capo de texto*/
var uiEvents = [{selector:'#registerScoreButton',
				 eventType:'click',
				 handler:function(event){
				 	eventControl.eventDispatcher().trigger('registerScore');
				 }}];



/*En la ultima seccion  se registran en el control principal los eventos de control y graficos para que funcionen de manera correcta*/
eventControl.addEvents(controlEvents);
eventControl.addUIEvents(uiEvents);