function commitData(url,obj,callbackfun,type,dataType){
	if(type==null){
		//默认post方式
		type="post";
	}
	if(dataType==null){
		//默认json数据类型
		dataType="json";
	}
	if(callbackfun.error==null){
		//默认输出错误日志
		callbackfun.error=function(data){
			alert("操作异常");				
			};
		}
		jQuery.ajax({
			url : url,
			dataType : dataType,
			type : type,
			data : obj,
			traditional:true,
			success : callbackfun.success,
			error:callbackfun.error
		});
}