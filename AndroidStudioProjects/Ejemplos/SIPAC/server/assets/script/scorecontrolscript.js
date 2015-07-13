var scoreControl = (function(){
	var scores = [];
	return {
			registerScore: function(data,pCallBack,pFault){
									$.ajax({type:'POST',
												url:globalVars.controlBaseURL+'scorecontroller/registerScore',
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