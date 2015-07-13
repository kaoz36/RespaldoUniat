/*En este objeto se administran los servicios relacionados al control de items */

var itemControl = (function(){
	var items = [];
	return {
			registerItem: function(data,pCallBack,pFault){
									$.ajax({type:'POST',
												url:globalVars.controlBaseURL+'itemcontroller/addItem',
												dataType:'json',
												data:data,
												error:defaultErrorHandler,
												success:function(result,status){
																	if(result.length > 0)
																	{
																		pCallBack(result);
																		return;
																	}
																	pFault(result);
																}
													});
			}
	};

}());