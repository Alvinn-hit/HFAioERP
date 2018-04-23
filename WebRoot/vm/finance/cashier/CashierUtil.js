//下拉框回填项目
//下拉弹窗回填科目
function setPodDivAcc(cols,vals){
	 
	var col = cols.split(";");
	var val = vals.split("#;#");
	var acccode='';
	var accfullname='';
	for(var i=0;i<col.length;i++){
		var nm = col[i].replace(".","_");		
		if(nm=='tblAccTypeInfo_AccFullName'){
			accfullname=val[i];
		}else if(nm=='tblAccDetail_AccCode'){
			acccode=val[i];
		}
	}	
	$("#"+curAcc).val(acccode+' '+accfullname);
	$("#"+curAcc).attr("val",acccode);
}

//以下是下拉弹出窗功能。
function Cashier_refAcc(inp,selectName,fieldName,display){
	curAcc = $(inp).attr("id");
	objectPop = inp;
	var dropdown=inp.dropdown;
	if(dropdown==undefined)
	{
    	var data = {
    	selectName:selectName,
    	operation:"DropdownPopup",
    	MOID:moduleId,
    	MOOP:"query",
    	selectField:fieldName,
    	};
		dropdown = new dropDownSelect("t_"+inp.id,data,inp,selectName);
		if(fieldName=="AccCodeName"){
			dropdown.retFun2 = setPodDivAcc; //科目的回填
		}else{
			dropdown.retFun = setFieldValue;
		}
		dropdown.showData();
		return ;
	}
	if(event.keyCode == 38)
	{
		if(dropdown!=undefined)
		{
			dropdown.selectUp();
		}
		return ;
	}else if(event.keyCode==40)
	{
		if(dropdown!=undefined)
		{
			dropdown.selectDown();
		}
		return ;
	}else if(event.keyCode==13)
	{
		if(dropdown!=undefined)
		{
			dropdown.curValue();
		}
		return ;
	}else if(event.keyCode==27)
	{
		if(dropdown!=undefined)
		{
			dropdown.close();
		}
		return ;
	}	
	dropdown.showData();
} 

var showvalues = "";
var popnames = "";
var hidevalues = "";
var objectPop = null;
/* 弹出框选择*/
//selectPops(this,'SelectAccEmployee','IsPersonalName','EmployeeIDs','选择个人')
function Cashier_selectPops(object,popname,showvalue,hidevalue,name){
	var displayName=encodeURI(name) ;
	//var urlstr = '/UserFunctionAction.do?operation=22&popupWin=Popdiv&tableName=&selectName='+popname+"&MOID="+moduleId+"&MOOP=add&LinkType=@URL:&displayName="+displayName;
	var urlstr = '/UserFunctionAction.do?operation=22&popupWin=Popdiv&tableName=&selectName='+popname+"&MOID=2e9767a1_0811051341599530017&MOOP=add";
	/*
	if(linkStr != ""){
		urlstr+=encodeURI(linkStr);
	}*/
	if(showvalue == "IsPersonalName" || showvalue == "EmployeeName"){
			
		if(showvalue == "IsPersonalName"){
			deptname= $(object).parent().parent().parent().find("#IsDeptName").val();
		}else if(showvalue == "EmployeeName"){
			deptname= $(object).parent().parent().find("#DepartmentName").val();
		}
		if(deptname!=undefined && deptname!=""){
			deptname = encodeURI(encodeURI(deptname));
			urlstr += "&tblDepartment_DeptFullName="+deptname;
		}
	}else if(showvalue == "IsProjectName"){
		//核算项目时，要根据往来单位进行处理，传往来单位的编号进行过滤
		var companyCode = current_tr.find("#CompanyCode").val();
		if(companyCode!=undefined && companyCode!=""){
			urlstr += "&CompanyCode="+companyCode;
		}
	}
	asyncbox.open({id:'Popdiv',title:name,url:urlstr,width:750,height:470})
	showvalues = showvalue;
	objectPop = object;
	hidevalues = hidevalue;
}

/* 弹出窗返回数据 */
function exePopdiv(returnValue){	
	if(typeof(returnValue)=="undefined") return ;
	var note = returnValue.split("#;#") ;
		
	//其他
	/*
	$(objectPop).parent().find("#"+showvalues).val(note[2]);
	current_tr.find("#"+hidevalues).val(note[0]);
	current_tr.find("#"+(showvalues.substring(2))).val($(objectPop).parent().find("#"+showvalues).val());
	*/
	$("."+showvalues).attr("val",note[0]).val(note[4]);	
	
}

/* 在摘要库中选择摘要*/
var recordobject = null;
function Cashier_record(object){
	var value = $(object).val();
	var url = '/VoucherManage.do?optype=queryRecordComment&searchValue='+value;
	url = encodeURI(encodeURI(url));
	asyncbox.open({
		id : 'dealdiv',
　　		url : url,
	    title : '摘要库',
	　　    width : 570,
	　　 	height : 370,
    	btnsbar : jQuery.btn.CANCEL,
	    callback : function(action,opener){
　　　　　  		//判断 action 值。

	　　	　	}
　		});
		recordobject = object;
}

//文本框选择会计科目
var cur_Obj = '';
function Cashier_AccCode(object,strtype,currencysetting,searchValue){
	objectinput = object;
	strtypes = strtype;
	currencysettings = currencysetting;
	cur_Obj = object;
	var urlstr = '/PopupAction.do?popupName=popAccTypeInfo&chooseType=chooseChild&inputType=checkbox&isCheckItem=true&returnName=Cashier_exePopdivAcc&';
	if(searchValue!=null && searchValue!=""){
		urlstr += "&selectType=keyword&selectValue="+searchValue;
	}
	urlstr = encodeURI(encodeURI(urlstr));
	asyncbox.open({
		id:'Popdiv',
		title:'会计科目',
		url:urlstr,
		width:750,
		height:470,
		btnsbar : [{
	     text    : '清&nbsp;&nbsp;&nbsp;空',
	      action  : 'clearbtn'
	  	}].concat(jQuery.btn.OKCANCEL),
		callback : function(action,opener){			
			if(action == 'ok'){
				var values = opener.datas();				
				Cashier_exePopdivAcc(values);				
			}else if(action == 'clearbtn'){
				Cashier_dealStrDate(object,'');				
			}
		}
	});
}

/* 回填数据 */
function Cashier_exePopdivAcc(returnValue){
	Cashier_dealStrDate(returnValue);
}	

function Cashier_dealStrDate(str){
	console.log(str);
	if(typeof(str)=="undefined")return ;
	var obj  = cur_Obj;
	if(str == ""){
		
	}
	var nateStr = str.split("#|#");
	for(var j=0;j<nateStr.length;j++){
		if(nateStr[j]!=""){
			var note = nateStr[j].split("#;#")
			if(j==0){
				$(obj).val(note[0]+" - "+note[1]);							
				$(obj).attr("val",note[0]);
			}else{				
				
			}
		}
	}
}

/*更改日期，回填期间*/
function Cashier_changeDate(obj,refObj,op){
	var v = $(obj).val();
	var period = new Date(v);
	var str = '';
	str = period.getFullYear()+'年'+(period.getMonth()+1)+'期';
	$(refObj).val(str);	
	if(op =="add"){
		$(".add_accYear").val(period.getFullYear());
		$(".add_accMonth").val(period.getMonth()+1);
	}else if(op=="edit"){
		$(".edit_accYear").val(period.getFullYear());
		$(".edit_accMonth").val(period.getMonth()+1);
	}
}

/**
 *  ajax调用 
 * @param url
 * @param obj
 * @param callbackfun
 * @param type
 * @param dataType
 */
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
			prompt("操作异常");
			console.log(obj);
		};
	}
	$.ajax({
		url : url,
		dataType : dataType,
		type : type,
		data : obj,
		traditional:true,
		success : callbackfun.success,
		error:callbackfun.error,
		complete:callbackfun.complete
	});
}

//****执行导入动作后
function importAfter(data){
	
	if(data.exts != undefined){
		var htm = '';
		for(var i = 0; i< data.exts.length;i++){
			var tr = '';
			tr +='<td>'+data.exts[i].BillDate+'</td>';
			tr +='<td>'+data.exts[i].No+'</td>';
			tr +='<td>'+data.exts[i].accWord+'</td>';
			tr +='<td>'+data.exts[i].orderNo+'</td>';
			tr +='<td>'+data.exts[i].comment+'</td>';
			tr +='<td>'+data.exts[i].accCode+'</td>';
			tr +='<td>'+data.exts[i].refCode+'</td>';
			tr +='<td>'+data.exts[i].debitAmt+'</td>';
			tr +='<td>'+data.exts[i].lendAmt+'</td>';
			tr +='<td>'+data.exts[i].handlerName+'</td>';
			tr +='<td>'+data.exts[i].createByName+'</td>';
			htm += '<tr>'+tr+'</tr>';
		}
		$(".impFailedList tbody").html(htm);
	}

	$(".impCount").text(data.count);
	$(".impSuccess").text(data.successCount);
	$(".impFailed").text(data.failCount);
	showDiv('.impStatus');
	reload();
}

//审核/反审核 出纳日记账
function checkDet(flag){
	if(global_search == undefined){
		alert("请选择明细。");
	}
	var data ={};
	data.dets = '';
	$(".checkItem:checked").each(function(){
		data.dets += (data.dets ==''?'':';')+$(this).parents(".data_row").attr("val");
	});
	if(flag == 1){
		data.operation = 99;
	}else{
		data.operation = 100;
	}
	showDiv('.loading');		
	jQuery.post("/CashierAction.do?",data,  
	        function (data) {  		            
				var obj; 
	            if(data.code == 0){		            	
	            	if(data.msg != undefined){
	            		alert(data.msg);
	            	} else{
	            		alert('操作失败');
	            	}		            	
	            } else{
	            	if(data.msg != undefined){
	            		alert(data.msg);
	            	}
	            }
	            closeDiv(".loading");
	            reload();
	         },"json"); 
	
}

//***选择/反选明细
function checkDets(obj){
	 if ($(obj).is(':checked')) {
         $(".checkItem").attr("checked","checked");         
     }else{
    	 $(".checkItem:checked").removeAttr("checked");
     }
}

//***数字添加千分位
function formatNumRgx(num) {  
	  var parts = num.toString().split(".");  
	  parts[0] = parts[0].replace(/\B(?=(\d{3})+(?!\d))/g, ",");  
	  return parts.join(".");  
};  


Date.prototype.format = function(fmt) { 
    var o = { 
       "M+" : this.getMonth()+1,                 //月份 
       "d+" : this.getDate(),                    //日 
       "h+" : this.getHours(),                   //小时 
       "m+" : this.getMinutes(),                 //分 
       "s+" : this.getSeconds(),                 //秒 
       "q+" : Math.floor((this.getMonth()+3)/3), //季度 
       "S"  : this.getMilliseconds()             //毫秒 
   }; 
   if(/(y+)/.test(fmt)) {
           fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
   }
    for(var k in o) {
       if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
   return fmt; 
}    
