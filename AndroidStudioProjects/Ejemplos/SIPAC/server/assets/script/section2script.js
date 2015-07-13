var faultHandler = function(result){
	alert (result.error);
}

var controlEvents={registerItem:function(event){
												//debugger;
												//alert('click RegisterItem');
												itemControl.registerItem({tipo:$('#tipoInput').val(),
																					categoria:$('#categoriaInput').val(),
																					nombre:$('#nombreInput').val(),
																					precio:$('#precioInput').val(),
																					descripcion:$('#descripcionInput').val(),
																					asset:$('#assetInput').val()},
																					function(result){eventControl.eventDispatcher().trigger('getItems');},
																					faultHandler);
											},
							getItems:function(event){
														alert ('Item guardado exitosamente');
														$('#nombreInput').val('');
														$('#precioInput').val('');
														$('#descripcionInput').val('');
														$('#assetInput').val('');
													}
};

var uiEvents = [{selector:'#saveItemButton',
						 eventType:'click',
						 handler:function(event){
							
							eventControl.eventDispatcher().trigger('registerItem');
						 }}];



eventControl.addEvents(controlEvents);
eventControl.addUIEvents(uiEvents);