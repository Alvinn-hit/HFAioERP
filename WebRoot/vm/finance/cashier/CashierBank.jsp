<!DOCTYPE html>
<head>
	<title>银行日记账</title>
	<link type="text/css" rel="stylesheet" href="/style/css/base_button.css">
	<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
	<script type="text/javascript" src="/js/jquery.js"></script>
	<script type="text/javascript" src="/js/jquery-ui.js"></script>
	<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
	<script type="text/javascript" src="/vm/finance/cashier/CashierUtil.js"></script>		
</head>

<style>
	
	ul{margin: 0;padding: 0;}
	ul li{list-style: none;}
	.add_content li{margin:15px auto;}
	.add_content li input{width:120px;}
	.add_content{margin:5px;}
	.add_foot{clear:both;}
	.add_content li span{display:inline-block;width:120px;}
	
	.edit_content li{margin:5px auto;}
	.edit_content li input{width:120px;}
	.edit_content{margin:5px;}	
	.edit_content li span{display:inline-block;width:120px;}
	
	.data_op{text-align:center;}
	.imp_footer{margin:5px 5px;}
	.imp_footer button{float:right; margin: 5px 5px;}
	.imp_list{max-height:300px;overflow-y:auto;}
	.imp_content{margin:5px 5px;}
	.imp_row{margin:5px auto;}
	.importData,.impStatus{display:none;}
	.data_selected{background: #e7fca9;}
	.data_row:hover{background:#4BC6D1;}	
	
	.loading{		
		display:none;
		min-width:250px;
		min-height: 100px;		
	    z-index: 99;	   
	    filter:alpha(opacity=100);
	    opacity:1;
	    position: absolute;
	    top: 30%;
	    left: 40%;	   
	    display: none;
	   
	}
	.loading .loading_msg{
		text-align:center;
		vertical-align:middle;
		line-height:25px;
	}
	.stBtn{
		position: absolute;
	    background-position: -32px 0;
	    background-color: #fff;
	    cursor: pointer;
	    margin-left: -15px;    		
	}
	.icon16{
		width: 16px;
	    height: 16px;
	    display: inline-block;
	    background-image: url(/style/images/client/icon16.png);
	    background-repeat: no-repeat;
	}
			
	.bar{
		margin:10px 0;
	}
	.tip{
		float:right
	}
	.data_top table,.data_body table,.quote_Dialog table{
		font-size: 12px;
    	border-collapse: collapse;
    	border-spacing: 0;
    	border-left: 1px solid #c2c2c2;
    	border-bottom: 1px solid #c2c2c2;
	}
	
	.data_top table thead,.data_body table thead,.quote_Dialog table thead{
		background: #5fa3e7;
   		background-image: -webkit-linear-gradient(top,#5fa3e7,#428bca);
	}
	.data_top table thead th,.data_body table thead th{
		color: #fff;
	}
	.data_top table td,.data_body table td,.quote_Dialog table td{
		border-right: 1px solid #c2c2c2;
	    border-top: 1px solid #c2c2c2;
	    border-spacing: 0;
	    padding: 0 5px;
	}
	
	.impStatus,.search_Dialog,.add_Dialog,.quote_Dialog,.setting_Dialog,.edit_Dialog{
		display:none;
		width: 460px;
	    z-index: 99;
	    margin: 0 0 0 -220px;
	    border: #808080 1px solid;
	    float: left;
	    
	    position: absolute;
	    top: 2%;
	    left: 50%;
	    background: #fff;
	    display: none;
	    -webkit-box-shadow: #666 0px 0px 10px;
	    -moz-box-shadow: #666 0px 0px 10px;
	    box-shadow: #666 0px 0px 10px;
	}
	
	.impStatus{
		width: 750px;
		left: 35%;
	}
	
	.search_Dialog .search_title,.add_Dialog .add_title,.quote_Dialog .quote_title,.setting_Dialog .setting_title,.edit_Dialog .edit_title{
		cursor:move;
		height: 28px;
    	background: #428bca;
	}
		
	.setting_Dialog .setting_content{margin:7px 7px;}
	.setting_Dialog .setting_foot{margin:10px 5px;}
	.quote_Dialog{width:500px;}
	.quote_Dialog .quote_content{margin:5px 5px;}
	.quote_Dialog .quote_voucherScope div{margin:5px 0;}
	.quote_Dialog .quote_foot{margin:10px 5px;}
	.quote_Dialog table{width:100%;}
	.add_Dialog,.edit_Dialog{width:600px;}
	.add_Dialog input,.edit_Dialog input{width:25%;}
	.add_Dialog select,.edit_Dialog select{width:15%;}
	.edit_Dialog .edit_orderNo{width:5%;}
	.add_Dialog .add_foot,.edit_Dialog .edit_foot{padding:0 9px;margin:9px 0;}
	
	.search_Dialog div{
		line-height: 28px;
		padding: 0 9px;
	}
	.search_Dialog div,.add_Dialog div,.edit_Dialog div{	  
	    background: url(../images/oaimages/pop-up_tite.gif);
	}

	.search_Dialog .search_period{
		width:100px;
	}
	.data_top{		    
	    z-index: 55;
	    width: 100%;
	    overflow: hidden;
	    position: absolute;
	    top: 0px;
	    left: 0px;
	}
	.data_top table,.data_body table{
		width:100%;
	}
	.data_top,.data_body{
		margin-left: 10px;
	}
	.data_container{
		overflow: hidden;	    
	    min-height:500px;
	    width: 100%;
	    box-sizing: border-box;
	    margin:0 auto;
	}
	.data_container .data_box{
		width: 100%;
	    height: 100%;
	    overflow: hidden;
	    position: relative;
	}
	.data_container table{
		table-layout: fixed;
    	overflow: auto;
    	border-collapse: collapse;
	}
	.data_container table td,.data_container table th{
		padding: 2px 5px;
	}
	.data_body{
	    z-index: 45;
	    width: 100%;	   
	    position: absolute;
	    top: 0px;
	    left: 0px;
	    overflow: auto;
	}
	.search_foot{
		margin:5px 0;
	}
	.search_foot button{
		margin:5px;
	}
	
	.search_div2{
		display:none;
	}
	.edit_remark_div,.edit_handler_div,.edit_refAcc_div,.add_remark_div,.add_handler_div,.add_refAcc_div{
		display:inline-block;
		position:relative;
	}
	.stBtn{
		top:2px;
		right:2px;
	}
</style>


<script type="text/javascript">
	$(function(){
		#if(!$!accCode)
			showDiv('.search_Dialog');
		#end
		//$(".data_container").css("height",$(document.body).height()*0.9);
		$(".data_container").css("height",document.documentElement.clientHeight-100+'px');
		$(".data_top table").css("width",$(".data_body table").width()-20);
		$(".data_body table").css("width",$(".data_body table").width()-20);
		$(".data_body").height(document.documentElement.clientHeight-100+'px');
		$(".btn_open").on("click",function(){
			showDiv('.search_Dialog');
		});
		$(".edit_Dialog").draggable();
		$(".search_Dialog").draggable();
		$(".add_Dialog").draggable();
		$(".quote_Dialog").draggable();
		$(".setting_Dialog").draggable();
		$(".impStatus").draggable();
		$(document.body).on("click",".data_row",function(){
			$(".data_row").removeClass("data_selected");
			$(this).addClass("data_selected");
			$(".btn_edit").removeAttr("disabled");
			if($(this).find(".checkItem:checked").length==0){
				$(this).find(".checkItem").attr("checked","checked");
			}else{
				$(this).find(".checkItem").removeAttr("checked");
			}
		});
		$(document.body).on("dblclick",".data_row",function(){
			$(".data_row").removeClass("data_selected");
			$(this).addClass("data_selected");
			$(".btn_edit").removeAttr("disabled");
			edit_ShowDiv();
		});
	});
	
	/* 隐藏div */
	function closeDiv(obj){
		$(obj).hide();
		if(document.getElementById('back')!=null){
			document.getElementById('back').parentNode.removeChild(document.getElementById('back'));
		}
	}
	
	/* 显示选择科目div */
	function showDiv(obj){
		$(obj).show();
		var bWidth=parseInt(document.documentElement.scrollWidth);
		var bHeight=parseInt(document.documentElement.scrollHeight);
		var back=document.createElement("div");
		back.id="back";
		var styleStr="top:0px;left:0px;position:absolute;background:#666;width:"+bWidth+"px;height:"+bHeight+"px;";
		styleStr+= "opacity:0;";
		back.style.cssText=styleStr;
		document.body.appendChild(back);
		showBackground(back,50);
	}
	
	//让背景渐渐变暗
	function showBackground(obj,endInt){
			var al=parseFloat(obj.style.opacity);al+=0.05;
			obj.style.opacity=al;
			if(al<(endInt/100)){
				setTimeout(function(){showBackground(obj,endInt)},1);}		
	}
	
	//转为日期对象
	function parseDate(date) {  
	    var t = Date.parse(date);  
	    if (!isNaN(t)) {  
	        return new Date(Date.parse(date.replace(/-/g, "/")));  
	    } else {  
	        return new Date();  
	    }  
	}
	
	//打开对应科目日记账
	var global_search ;
	function search(){
		//****验证参数
		var flag = true;
		if($("input[name=period]:checked").attr("value")=='period'){
			
			var reg = /^[1-9]+?[0-9]*$/;
			$(".search_period").each(function(){
				var val = $(this).attr("value");
				if(!reg.test(val)){
					flag = false;
				}				
			});
			if(!flag){
				return alert('请输入正确的会计期间');
			}
		} else{
			if($(".search_date[value='']").length>0){
				flag = false;
			}
			if(!flag){
				return alert('请输入日期');
			}
		}
		//****end
		//***回填提示栏***//
		$(".tip_acccode").text($(".search_acc option:selected").attr("value"));
		$(".tip_currency").text($(".search_currency option:selected").text());
		if($("input[name=period]:checked").attr("value")=='period'){
			$(".tip_period").text($(".search_periodBegYear").val()+'年'+$(".search_periodBegMonth").val()+'期-'+$(".search_periodEndYear").val()+'年'+$(".search_periodEndMonth").val()+'期');
		}else{
			var txt = '';
			var beg = parseDate($(".search_begD").val());
			var end = parseDate($(".search_endD").val());
			var txt =  beg.getFullYear()+'年'+(beg.getMonth() + 1)+'期-'+beg.getFullYear()+'年'+(beg.getMonth() + 1)+'期';
			$(".tip_period").text(txt);
		}
		$(".tip_period").text();
		//****end****//
		
		var data = {};
		data.operation = 122;
		data.accCode = $(".search_acc option:selected").attr("value");
		data.currency = $(".search_currency option:selected").attr("value");
		data.showDisable = $(".search_showDisable").attr("checked") =="checked" ? 1:0;
		data.qPeriod = $("input[name=period]:checked").attr("value");
		data.qBeginYear = $(".search_periodBegYear").val();
		data.qBeginMonth = $(".search_periodBegMonth").val();
		data.qEndYear = $(".search_periodEndYear").val();
		data.qEndMonth = $(".search_periodEndMonth").val();
		data.qBegD = $(".search_begD").val();
		data.qEndD = $(".search_endD").val();
		data.curOption = $("input[name=search_curoption]").val();
		data.initBala = $(".search_showInitialBalance:checked").length>0 ? 1 :0 ;
		data.showDet = $(".search_showDet:checked").length>0?1:0;
		data.todayAccount = $(".search_showTodayAccount:checked").length>0?1:0;
		data.curPeriodAccount = $(".search_showCurPeriodAccount:checked").length>0?1:0;
		data.accuTotal = $(".search_showAccumulativeTotal:checked").length>0?1:0;
		data.total =  $(".search_showTotal:checked").length>0?1:0;
		global_search = data;
		jQuery.post("/CashierAction.do?",data,  
	        function (data) {  
	            var obj; 
	            if(data.code == 0){
	            	alert('获取失败！');
	            } else{
	            	obj = eval(data);  
		            dealResult(data.data);	
	            }
	            closeDiv('.search_Dialog');
	         },"json"); 
	}
	
	function dealResult(data){
		var htm = "";
		for(var i = 0;i<data.length;i++){						
			var str = '';
			if(data[i].others == undefined){						
				str += '<tr class="data_row" val="'+data[i].id+'">';
				str += '<td>'+(data[i].row==undefined?'':data[i].row)+'</td>';
				#if($hasAudit == 1)
					str += '';
				#end
				str += '<td class="data_op"><input type="checkbox" class="checkItem" /></td>';
			}else{				
				str += '<tr class="data_other" val="">';
				str += '<td>'+(data[i].row==undefined?'':data[i].row)+'</td>';
				#if($hasAudit == 1)
					str += '';
				#end	
				str += '<td class=""></td>';
			}			
			str += '<td>'+(data[i].workFlowNodeName==undefined?'':data[i].workFlowNodeName)+'</td>';
			str += '<td>'+(data[i].hasPosted==undefined?'':data[i].hasPosted)+'</td>';
			str += '<td>'+(data[i].BillDate==undefined?'':data[i].BillDate)+'</td>';		
			str += '<td>'+(data[i].No==undefined?'':data[i].No)+'</td>';
			str += '<td>'+(data[i].CredTypeID==undefined?'':data[i].CredTypeID)+'</td>';
			str += '<td>'+(data[i].Period==undefined?'':data[i].Period)+'</td>';
			str += '<td>'+(data[i].isAudited == 'finish' ? '已审核':'')+'</td>';
			str += '<td>'+(data[i].isPosted == 'finish' ? '已过账':'')+'</td>';
			str += '<td>'+(data[i].RecordComment==undefined?'':data[i].RecordComment)+'</td>';
			str += '<td>'+(data[i].RefAcc==undefined?'':data[i].RefAcc)+'</td>';
			str += '<td>'+(data[i].DebitAmount==undefined?'':formatNumRgx(data[i].DebitAmount))+'</td>';
			str += '<td>'+(data[i].LendAmount==undefined?'':formatNumRgx(data[i].LendAmount)) +'</td>';
			str += '<td>'+(data[i].Amount == undefined ? '':formatNumRgx(data[i].Amount)) +'</td>';
			str += '<td>'+(data[i].handler==undefined?'':data[i].handler)+'</td>';
			str += '<td>'+(data[i].creator==undefined?'':data[i].creator)+'</td>';
			
			htm  += '<tr class="data_row" val="'+data[i].id+'">'+str+'</tr>';
		}
		$(".data_body table tbody").html(htm);
	}
			
	function search_ShowDiv(div1,div2){
		$("."+div1).show();
		$("."+div2).hide();
	}
	
	//****选项设置****//
	function setting_ShowDiv(){
		showDiv(".setting_Dialog");
	}
	
	//****选项窗口关闭****//
	function setting_close(){
		closeDiv(".setting_Dialog");
	}
	
	
	//****引入按钮****//
	function quote_ShowDiv(){
		showDiv(".quote_Dialog");
	}
	
	//****引入凭证****//
	function quote_add(){		
		var data = {};
		data.operation = 124;
		data.periodYear = $(".quote_periodYear").val();
		data.periodMonth = $(".quote_periodMonth").val();
		var accCodes  = '';
		$(".quote_checkAcc:checked").each(function(){
			accCodes += (accCodes==''?'':';')+$(this).attr("val");
		});
		data.accCodes = accCodes;
		data.way = $("input[name=quote_way]").val();
		data.date = $("input[name=quote_date]").val();
		data.model = $("input[name=quote_model]:checked").val();
		data.word = $(".quote_word option:selected").attr('value');
		data.creator = $(".quote_creator option:selected").attr('value');
		data.begOrderNo = $(".quote_orderNoBeg").val().trim()==''?0:$(".quote_orderNoBeg").val().trim();
		data.endOrderNo = $(".quote_orderNoEnd").val().trim()==''?0:$(".quote_orderNoEnd").val().trim();
		data.status = $("input[name=quote_status]:checked").val();
		data.post  = $("input[name=quote_post]:checked").val();
		
		jQuery.post("/CashierAction.do?",data,  
		        function (ret) {  
		    	    if(ret.code==0){
		    	    	alert('引入失败');
		    	    } else{
		    	    	var obj;  
			            obj = eval(ret);  
			            if(ret.msg != undefined){
			            	alert(ret.msg);
			            }
			            reload();	
		    	    }
		    	    closeDiv('.quote_Dialog');
		         },"json"); 
		
	}
	
	//****关闭引入对话框quote_Dialog****//
	function quote_close(){
		closeDiv(".quote_Dialog");
	}
	
	//****添加按钮****//
	function add_ShowDiv(){		
		$(".add_Dialog input[class^=add_]").val('').attr("val",'');
		$(".add_Dialog .add_orderNo").val("1");
		$(".add_Dialog .add_date").val(new Date().format("yyyy-MM-dd"));
		$(".add_accYear").val(new Date().getFullYear());		
		$(".add_accMonth").val(new Date().getMonth()+1);
		showDiv(".add_Dialog");
	}
	
	//****添加记账****//
	function add_save(){
		var data ={};
		data.operation = 123;
		data.accCode = $(".add_accCode option:selected").val();
		data.currency = $(".add_currency option:selected").val();
		data.BillDate = $(".add_date").val();
		data.period = $(".add_period").val();
		data.No = $(".add_orderNo").val();
		data.accYear = $(".add_accYear").val();
		data.accMonth = $(".add_accMonth").val();
		data.accWord= $(".add_accWord option:selected").val();
		data.orderNo ="0";
		data.remark = $(".add_remark").val();
		data.handler = $(".add_handler").val().trim() !='' ? $(".add_handler").attr("val"):'';
		data.refCode = $(".add_refAcc").val().trim() != '' ? $(".add_refAcc").attr("val"):'';
				
		data.debitAmt = $(".add_debitAmt").val();
		data.lendAmt = $(".add_lendAmt").val();
		data.comment = $(".add_comment").val();
		
		if(data.debitAmt.trim() == '' && data.lendAmt.trim() == ''){
			return alert('请输入借贷金额');
		} else if(data.debitAmt != '' && data.lendAmt != ''){
			return alert('只能输入借贷一方金额');
		}
		
		
		jQuery.post("/CashierAction.do?",data,  
		        function (ret) {  
		    	    if(ret.code==0){
		    	    	alert('添加失败');
		    	    } else{
		    	    	var obj;  
			            obj = eval(ret);  
			            reload();	
		    	    }
		    	    closeDiv('.add_Dialog');
		         },"json"); 
	}
	
	//***修改按钮***//
	function edit_ShowDiv(){
		//****加载条目****//
		var data = {};
		data.operation = 127;		
		data.id = $(".data_selected").attr("val");
		jQuery.post("/CashierAction.do?",data,  
		        function (ret) {  
		    	    if(ret.code==0){
		    	    	if(ret.msg != undefined && ret.msg != ''){
		    	    		alert(ret.msg);
		    	    	}else{
		    	    		alert('获取数据失败');	
		    	    	}
		    	    } else{
		    	    	var obj;  
			            obj = eval(ret.data);  
			            edit_loadDet(obj);	
		    	    }		    	  
		         },"json"); 		
	}
	
	//***修改日记账***//
	function edit_save(){
		if($(".data_selected").length ==0){
			return alert('请选择明细。');
		}
		var data ={};
		data.operation = 128;
		data.id = $(".data_selected").attr("val");
		data.accCode = $(".edit_accCode option:selected").val();
		data.currency = $(".edit_currency option:selected").val();
		data.BillDate = $(".edit_date").val();
		data.period = $(".edit_period").val();
		data.No = $(".edit_orderNo").val();
		data.accYear = $(".edit_accYear").val();
		data.accMonth = $(".edit_accMonth").val();
		data.accWord= $(".edit_accWord option:selected").val();
		data.orderNo = $(".edit_accNo").val();
		data.remark = $(".edit_remark").val();
		data.handler = $(".edit_handler").val().trim() !='' ? $(".edit_handler").attr("val"):'';
		data.refCode = $(".edit_refAcc").val().trim() != '' ? $(".edit_refAcc").attr("val"):'';
		data.debitAmt = $(".edit_debitAmt").val();
		data.lendAmt = $(".edit_lendAmt").val();
		data.comment = $(".edit_comment").val();
		
		jQuery.post("/CashierAction.do?",data,  
		        function (ret) {  
		    	    if(ret.code==0){
		    	    	alert('修改失败');
		    	    } else{
		    	    	var obj;  
			            obj = eval(ret);  
			            reload();	
		    	    }
		    	    closeDiv('.edit_Dialog');
		         },"json"); 
	}
	
	function edit_close(){
		closeDiv(".edit_Dialog");
	}
	
	//***重载数据***//
	function edit_loadDet(data){
		$(".edit_Dialog").attr("val",data.id);
		$(".edit_accCode option[value="+data.AccCode+"]").attr("selected","selected");
		$(".edit_date").val(data.BillDate);
		$(".edit_period").val(data.Period);
		$(".edit_orderNo").val(data.No);
		$(".edit_accYear").val(data.PeriodYear);
		$(".edit_accMonth").val(data.PeriodMonth);
		$(".edit_accWord option[value="+data.CredType+"]").attr("selected","selected");
		$(".edit_accNo").val(data.CredNo);
		$(".edit_refAcc").val(data.RefAcc!=undefined && data.RefAcc!=''?data.RefAcc+data.RefAccName:'').attr("val",data.RefAcc);		
		$(".edit_handler").val(data.handlerName).attr("val",data.handler);
		$(".edit_remark").val(data.RecordComment);
		$(".edit_debitAmt").val(data.DebitAmount==0 ? '':data.DebitAmount);
		$(".edit_lendAmt").val(data.LendAmount==0 ?'':data.LendAmount);
		showDiv(".edit_Dialog");
	}
	
	//***重载现金记账条目****//
	function reload(){
		if(global_search == undefined){
			return;
		}
		global_search.operation = 122;
		jQuery.post("/CashierAction.do?",global_search,  
		        function (data) {  
		            var obj; 
		            if(data.code == 0){
		            	alert('获取失败！');
		            } else{
		            	obj = eval(data);  
			            dealResult(data.data);	
		            }
		           
		         },"json"); 
	}
	
	//****关闭添加记账按钮****//
	function add_close(){
		closeDiv('.add_Dialog');
	}
	
	//****按单生成凭证
	function genBySingle(){
		var data ={};		
		data = global_search;
		if(global_search==undefined){
			alert("请选择筛选条件");			
		}
		data = global_search;
		data.operation = 125;
		data.accWord = $(".setting_accWord option:selected").val();
		data.exception = $(".setting_voucher:checked").val();	
		showDiv('.loading');		
		jQuery.post("/CashierAction.do?",global_search,  
		        function (data) {  		            
					var obj; 
		            if(data.code == 0){		            	
		            	if(data.msg != undefined){
		            		alert(data.msg);
		            	} else{
		            		alert('生成凭证失败');
		            	}		            	
		            } else{
		            	//setMsg('.loading','生成完毕');
		            }
		            closeDiv(".loading");
		         },"json"); 
	}
	
	//****汇总生成凭证
	function genBySummary(){
		var data ={};
		if(global_search==undefined){
			alert("请选择筛选条件")
		}
		data = global_search;
		data.operation = 126;
		data.accWord = $(".setting_accWord option:selected").val();
		data.mergeAcc = $(".setting_mergeAcc:checked").length;
		data.mergeRefAcc = $(".setting_mergeRefAcc:checked").length;
		showDiv('.loading');		
		jQuery.post("/CashierAction.do?",global_search,  
		        function (data) {  		            
					var obj; 
					closeDiv(".loading");
					if(data.code == 0){		            	
		            	if(data.msg != undefined){
		            		alert(data.msg);
		            	} else{
		            		alert('生成凭证失败');
		            	}		            	
		            } else{
		            	alert('生成完毕');
		            }
		           
		         },"json"); 
	}
	
	//*****设置说明文字*****//
	function setMsg(obj,txt){
		$(obj).text(txt);
	}
	//*****删除日记账条目****//
	function delete_Item(){
		if($(".checkItem:checked").length == 0){
			return alert("请选择条目。");
		}
		var data = {};	
		data.ids = '';
		$(".checkItem:checked").each(function(){
			data.ids += (data.ids == ''?'':';')+$(this).parents('.data_row').attr("val");	
		});
			
		data.operation = 129;
		showDiv('.loading');		
		jQuery.post("/CashierAction.do?",data,  
		        function (data) {  		            
					closeDiv(".loading");	
					var obj; 
		            if(data.code == 0){		            	
		            	if(data.msg != undefined){
		            		alert(data.msg);
		            	} else{
		            		alert('删除失败');
		            	}		            	
		            } else{
		            	
		            }
		            reload();		           
		         },"json"); 
	}
	
	function importData(){
		$(".importData").contents().find("#file").click();
	}
	function exportData(){
		if(global_search==undefined){
			alert("请选择筛选条件");			
		}
		data = global_search;
		data.operation = 114;
		data.accWord = $(".setting_accWord option:selected").val();
		data.exception = $(".setting_voucher:checked").val();	
		showDiv('.loading');		
		jQuery.post("/CashierAction.do?",global_search,  
		        function (data) {  		            
					var obj; 
		            if(data.code == 0){		            	
		            	if(data.msg != undefined){
		            		alert(data.msg);
		            	} else{
		            		alert('导出excel失败');
		            	}		            	
		            } else{
		            	$(".iframe").attr("src","/CashierAction.do?operation=132&filename="+data.data);
		            	//$(".iframe").attr("src","/vm/finance/cashier/tmp/"+data.data);
		            }
		            closeDiv(".loading");
		         },"json"); 
	}
	#if($hasPosted == 1)
		function postDets(){
			if($(".checkItem:checked").length<=0){
				return alert('请选择出纳明细！');
			}
			var dets = '';
			$(".checkItem:checked").parents(".data_row").each(function(){
				dets += dets==''?$(this).attr('val'):';'+$(this).attr('val');
			});
			var data ={};
			if(global_search==undefined){
				alert("请选择筛选条件")
			}
			data = global_search;
			data.operation = 139;
			data.accWord = $(".setting_accWord option:selected").val();
			data.mergeAcc = $(".setting_mergeAcc:checked").length;
			data.mergeRefAcc = $(".setting_mergeRefAcc:checked").length;
			data.dets = dets;
			showDiv('.loading');		
			jQuery.post("/CashierAction.do?",global_search,  
			        function (data) {  		            
						var obj; 
						closeDiv(".loading");
						if(data.code == 0){		            	
			            	if(data.msg != undefined){
			            		alert(data.msg);
			            	} else{
			            		alert('出纳明细过账失败');
			            	}		            	
			            } else{
			            	alert('已过账');
			            }
						reload();
			         },"json"); 
		}		
	#end
	#if($hasRePosted == 1)
		function repostDets(){	
			if($(".checkItem:checked").length<=0){
				return alert('请选择出纳明细！');
			}
			var dets = '';
			$(".checkItem:checked").parents(".data_row").each(function(){
				dets += dets==''?$(this).attr('val'):';'+$(this).attr('val');
			});
			var data ={};
			if(global_search==undefined){
				alert("请选择筛选条件")
			}
			data = global_search;
			data.operation = 140;
			data.accWord = $(".setting_accWord option:selected").val();
			data.mergeAcc = $(".setting_mergeAcc:checked").length;
			data.mergeRefAcc = $(".setting_mergeRefAcc:checked").length;
			data.dets = dets;
			showDiv('.loading');		
			jQuery.post("/CashierAction.do?",global_search,  
			        function (data) {  		            
						var obj; 
						closeDiv(".loading");
						if(data.code == 0){		            	
			            	if(data.msg != undefined){
			            		alert(data.msg);
			            	} else{
			            		alert('出纳明细反过账失败');
			            	}		            	
			            } else{
			            	alert('已反过账');
			            }
						reload();
			         },"json"); 	
		}		
	#end
</script>
<body>
	<iframe src="" class="iframe" style="display:none;"></iframe>
	<div class="impStatus">
		<div class="imp_content">
			<div class="imp_row">
				<span>导入总行数：</span><span class="impCount"></span>
			</div>
			<div class="imp_row">
				<span>导入成功：</span><span class="impSuccess"></span>
			</div>
			<div class="imp_row">
				<span>导入失败：</span><span class="impFailed"></span>
			</div>
			<div class="imp_row">
				<fieldset>
					<legend>
						导入失败：
					</legend>
					<div class="imp_list">
					<table class="impFailedList">
						<thead>
							<tr>
								<th>日期</th>	
								<th>序号</th>
								<th>凭证字</th>
								<th>凭证号</th>
								<th>摘要</th>
								<th>科目</th>
								<th>对方科目</th>
								<th>借方金额</th>
								<th>贷方金额</th>
								<th>经手人</th>
								<th>制单人</th>
							</tr>
						</thead>
						<tbody>
							
						</tbody>
					</table>
				 </div>
				</fieldset>
			</div>
		</div>
		<div class="imp_footer">
			<span><button class="btn btn-small" onclick="closeDiv('.impStatus')">确定</button></span>
		</div>
	</div>
	<iframe class="importData" src="/CashierAction.do?operation=101"></iframe>
	<div class="loading">
		<div class="loading_title"></div>
		<div class="loading_msg"><span class="loading_txt"></span><span><img src="/js/loading.gif" /></span></div>
		<div class="loading_foot"></div>
	</div>
	<!-- 修改记账明细对话框 -->
	<div class="edit_Dialog">
		<div class="edit_title">
			<span class="ico_4"></span>			
			现金日记账-修改
		</div>
		<ul class="edit_content">
			<li class="edit_row">
				<span>科 目：</span>
				<select class="edit_accCode">
					#foreach($row in $AccCodes)
							<option value="$!row.AccNumber">$!row.AccName</option>
					#end					
				</select>
				<span>币 别：</span>
				<select class="edit_currency">
					<option value='RMB'>人民币</option>
				</select>
			</li>
			<li class="edit_row">
				<span>日 期：</span>
				<span><input type="date" class="edit_date" onchange="Cashier_changeDate(this,'.edit_period','edit')" /></span>
				<span>期  间：</span>
				<span><input type="text" class="edit_period" disabled /></span>
				<span>序号：</span>
				<span><input type="text" class="edit_orderNo" value='1' readonly /></span>				
			</li>
			<li class="edit_row">
				<span>凭 证 年：</span>
				<span><input type="number" class="edit_accYear" /></span>
				<span>凭证期间：</span>
				<span><input type="number" class="edit_accMonth" /></span>				
			</li>
			<li class="edit_row">
				<span>凭 证 字：</span>
				<select class="edit_accWord">
					<option value="记" >记</option>
				</select>
				<span>凭 证 号：</span>
				<span><input type="text" class="edit_accNo" /></span>
			</li>
			<li class="edit_row">
				<span>摘 要：</span>
				<div class="edit_remark_div">
					<input type="text" class="edit_remark" />
					<b class="stBtn icon16" onclick="Cashier_record('.edit_remark')"></b>
				</div>
			</li>
			<li class="edit_row">
				<span>经 手 人：</span>
				<div class="edit_handler_div">
					<input type="text" class="edit_handler" />		
					<b class="stBtn icon16" onclick="Cashier_selectPops(this,'SelectAccEmployee','edit_handler','','选择经手人')"></b>
				</div>
			</li>
			<li class="edit_row">
				<span>对方科目：</span>
				<div class="edit_refAcc_div">
					<input type="text" class="edit_refAcc" />
					<b class="stBtn icon16" onclick="Cashier_AccCode('.edit_refAcc','edit','true')"></b>
				</div>
			</li>
			<li class="edit_row">
				<span>借方金额：</span>
				<span><input type="text" class="edit_debitAmt" /></span>
				<!-- 
				<span>汇率（乘）：</span>
				<span><input type="text" class="add_rateMult" /></span>
				-->
				
				<span>本位币额：</span>
				<span><input type="text" class="edit_curAmt" /></span>
			</li>
			<li class="edit_row">
				<span>贷方金额：</span>
				<span><input type="text" class="edit_lendAmt" /></span>
				<!-- 
				<span>汇率（乘）：</span>
				<span><input type="text" class="edit_rateMult" /></span>
				 -->
				 <span>本位币额：</span>
				 <span><input type="text" class="edit_curAmt" /></span>
			</li>
			<li class="edit_row">
				<span>备 注：</span>
				<span><input type="text" class="edit_comment" /></span>
			</li>		
		</ul>
		<div class="edit_foot">
			<span><button class="edit_save" onclick="edit_save()">保存</button></span>
			<span><button class="edit_close" onclick="edit_close()">关闭</button></span>
		</div>			
	</div>
	<!-- 生成凭证选项对话框 -->
	<div class="setting_Dialog">
		<div class="setting_title">
			<span class="ico_4"></span>
			生成凭证选项
		</div>
		<div class="setting_content">
			<div class="setting_row">
				<fieldset>
					<legend>
						默认值
					</legend>
					<div>
						<span>凭证字：</span>
						<span><select class="setting_accWord"><option value="记">记</option></select></span>
						<span><input type="checkbox" class="setting_editAccDate" /></span>
						<span>手工编辑凭证日期</span>
					</div>				
				</fieldset>
			</div>
			<div class="setting_row">
				<fieldset>
					<legend>
						按单异常处理
					</legend>
					<div>
						<span><input type="radio" name="setting_voucher" value="editVoucher" /></span><span>编辑该凭证</span>
						<span><input type="radio" name="setting_voucher" value="passVoucher" checked /></span><span>跳过该凭证</span>
						<span><input type="radio" name="setting_voucher" value="stopVoucher" /></span><span>停止生成凭证</span>
					</div>
				</fieldset>
			</div>
			<div class="setting_row">
				<fieldset>
					<legend>
						汇总生成凭证选项
					</legend>
					<div>
						<span><input type="checkbox"  value="setting_mergeAcc" /></span>
						<span>日记账中科目相同合并</span>						
					</div>
					<div>
						<span><input type="checkbox"  value="setting_mergeRefAcc" /></span>
						<span>日记账中对方科目相同合并</span>						
					</div>
				</fieldset>
			</div>
		</div>
		<div class="setting_foot">
			<span><button class="btn btn-small">确定</button></span>
			<span><button class="btn btn-small" onclick="setting_close()">取消</button></span>
		</div>
	</div>
	
	<!-- 引入凭证科目对话框 -->
	<div class="quote_Dialog">
		<div class="quote_title">
			<span class="ico_4"></span>
			引入日记账
		</div>
		<div class="quote_content">
			<div class="quote_row">
				<fieldset>
				<legend>会计期间：</legend>
				<span>期间：</span>
				<span><input type="number" class="quote_periodYear" /> 年</span>
				<span><input type="number" class="quote_periodMonth" /> 期</span>
				</fieldset>		
			</div>
			<div class="quote_row">
				<fieldset>
					<legend>银行日记账</legend>
					<table>
						<thead>
							<tr>
							<th>选择</th>
							<th>科目</th>
							<th>状态</th>
							</tr>
						</thead>
						<tbody>
							#foreach($row in $AccCodes)
							<tr>
								<td><input type="checkbox" class="quote_checkAcc" val="$!row.AccNumber" /></td>
								<td>$!row.AccName</td>
								<td></td>
							</tr>							
							#end							
						</tbody>
					</table>
				</fieldset>
				<button class="btn btn-small" onclick="quote_all()">全选</button>
				<button class="btn btn-small" onclick="quote_clearAll()">全清</button>
			</div>
			<div class="quote_row">
				<fieldSet>
					<legend>引入方式</legend>
					<div>
						<span>
						<input type="radio" name="quote_way" value="cashItem" checked />
						按现金科目
						</span>
						<span>
						<input type="radio" name="quote_way" value="refItem" />
						按对方科目
						</span>
					</div>					
				</fieldSet>
				<fieldSet>
					<legend>日期</legend>
					<div>
						<span>
							<input type="radio" name="quote_date" value="accDate" checked />
							使用凭证日期
						</span>
						<span>
						<input type="radio" name="quote_date" value="sysDate" />
						使用系统日期
						</span>
					</div>					
				</fieldSet>
			</div>
			<div class="quote_row">
				<fieldSet>
					<legend>期间模式</legend>
					<div>
						<span>
						<input type="radio" name="quote_model" value="quote_today" checked />
						只引入本日凭证
						</span>
						<span>
						<input type="radio" name="quote_model" value="quote_curPeriod" />
						引入本期所有凭证
						</span>
					</div>					
				</fieldSet>
			</div>
			<div class="quote_row quote_voucherScope">
				<fieldset>
					<legend>
						凭证范围
					</legend>
					<div>
						<span>凭证字：</span><span><select class="quote_word"><option value="all">所有凭证</option></select></span>
						<span>制单人：</span><span><select class="quote_creator"><option value="all">全部</option></select></span>
					</div>
					<div>
						<span>凭证号：</span><span><input type="number" class="quote_orderNoBeg" /></span> 至 <span><input type="number" class="quote_orderNoEnd" /></span> 
					</div>
					<div>
						<span>审核状态：</span><span><input type="radio" name="quote_status" value="checked" />已审核</span>
						<span><input type="radio" name="quote_status" value="unchecked" />未审核</span>
						<span><input type="radio" name="quote_status" value="all" checked />全部</span>
					</div>
					<div>
						<span>过账状态：</span><span><input type="radio" name="quote_post" value="posted" />已过账</span>
						<span><input type="radio" name="quote_post" value="unposted" />未过账</span>
						<span><input type="radio" name="quote_post" value="all" checked />全部</span>
					</div>
				</fieldset>
			</div>			
		</div>
		<div class="quote_foot">
			<button class="btn btn-small" onclick="quote_add()">引入</button>
			<button class="btn btn-small" onclick="quote_close()">关闭</button>
		</div>
	</div>
	
	<!-- 添加记账明细对话框 -->
	<div class="add_Dialog">
		<div class="add_title">
			<span class="ico_4"></span>			
			银行日记账-新增
		</div>
		<ul class="add_content">
			<li class="add_row">
				<span>科 目：</span>
				<select class="add_accCode">					
					#foreach($row in $AccCodes)
							<option value="$!row.AccNumber">$!row.AccName</option>
						#end
				</select>
				<span>币 别：</span>
				<select class="add_currency">
					<option value='RMB'>人民币</option>
				</select>
			</li>
			<li class="add_row">
				<span>日 期：</span>
				<span><input type="date" class="add_date" onchange="Cashier_changeDate(this,'.add_period','add')" /></span>
				<span>期  间：</span>
				<span><input type="text" class="add_period" disabled /></span>
				<span>序号：</span>
				<span><input type="text" class="add_orderNo" value='1' readonly /></span>				
			</li>
			<li class="add_row" style="display:none;">
				<span>凭 证 年：</span>
				<span><input type="number" class="add_accYear" /></span>
				<span>凭证期间：</span>
				<span><input type="number" class="add_accMonth" /></span>				
			</li>
			<li class="add_row" style="display:none;">
				<span>凭 证 字：</span>
				<select class="add_accWord">
					<option value="记"  >记</option>
				</select>
				<span>凭 证 号：</span>
				<span><input type="text" class="add_accNo" value=0 /></span>
			</li>
			<li class="add_row">
				<span>摘 要：</span>
				<div class="add_remark_div">
					<input type="text" class="add_remark" />
					<b class="stBtn icon16" onclick="Cashier_record('.add_remark')"></b>
				</div>
			</li>
			<li class="add_row">
				<span>经 手 人：</span>
				<div class="add_handler_div">
					<input type="text" class="add_handler" />
					<b class="stBtn icon16" onclick="Cashier_selectPops(this,'SelectAccEmployee','add_handler','','选择经手人')"></b>
				</div>		
			</li>
			<li class="add_row">
				<span>对方科目：</span>
				<div class="add_refAcc_div">
					<input type="text" class="add_refAcc" />
					<b class="stBtn icon16" onclick="Cashier_AccCode('.add_refAcc','add','true')"></b>
				</div>
			</li>
			<li class="add_row">
				<span>借方金额：</span>
				<span><input type="text" class="add_debitAmt" /></span>
				<!-- 
				<span>汇率（乘）：</span>
				<span><input type="text" class="add_rateMult" /></span>
				-->
				
				<span>本位币额：</span>
				<span><input type="text" class="add_curAmt" /></span>
			</li>
			<li class="add_row">
				<span>贷方金额：</span>
				<span><input type="text" class="add_lendAmt" /></span>
				<!-- 
				<span>汇率（乘）：</span>
				<span><input type="text" class="add_rateMult" /></span>
				 -->
				 <span>本位币额：</span>
				 <span><input type="text" class="add_curAmt" /></span>
			</li>
			<li class="add_row">
				<span>备 注：</span>
				<span><input type="text" class="add_comment" /></span>
			</li>		
		</ul>
		<div class="add_foot">
			<span><button class="add_save" onclick="add_save()">保存</button></span>
			<span><button class="add_close" onclick="add_close()">关闭</button></span>
		</div>			
	</div>
 	<!-- 打开查询科目对话框 -->
	<div class="search_Dialog">
		<div class="search_title">
			<span class="ico_4"></span>
			条件查询
		</div>
		<div class="search_content">
			<fieldSet>
				<legend>科目及币别</legend>
				<div class="search_row1">
					<span>科目：</span>
					<select class="search_acc">						
						#foreach($row in $AccCodes)
							<option value="$!row.AccNumber">$!row.AccName</option>
						#end
					</select>
				</div>
				<div class="search_row2">
				 	<span>币别：</span>
				 	<select class="search_currency"><option value="RMB">人民币</option></select>
				 	<input type="checkbox" class="search_showDisable" />显示禁用科目
				</div>				
			</fieldSet>
			<fieldSet>
				<legend>期间</legend>
				<div class="search_row3">
					<span><input type="radio" name="period" value="period" onclick="search_ShowDiv('search_div1','search_div2')" checked />按期间查询</span>
					<span><input type="radio" name="period" value="date" onclick="search_ShowDiv('search_div2','search_div1')" />按日期查询</span>
					
				</div>
				<div class="search_div1">
					<div class="search_row4">
						<span>会计期间：<input type="number" class="search_period search_periodBegYear" value="$!PeriodYear" />年</span> <span><input type="number" class="search_period search_periodBegMonth" value="$!PeriodMonth" /> 期</span>
						
					</div>
					<div class="search_row5">
						<span>&nbsp;&nbsp;&nbsp;至：<input type="number" class="search_period search_periodEndYear" value="$!PeriodYear" />年</span> <span><input type="number" class="search_period search_periodEndMonth" value="$!PeriodMonth" /> 期</span></span>
						
					</div>
				</div>
				<div class="search_div2">
					<span>从 <input type="date" class="search_date search_begD" /></span><span> 至 <input type="date" class="search_date search_endD" /></span>
				</div>
				<div class="search_row6">
					<!--<span><button class="search_today">本日</button></span>-->
					<!-- <span><button class="search_curPeriod">本期</button></span> -->
					<!-- <span><button class="search_curYear">本年</button></span> -->
					<!-- <span><button class="search_allPeriod">所有区间</button></span> -->
				</div>
			</fieldSet>
			<fieldSet>
				<legend>币别选项</legend>
				<div class="search_row7">
					<span><input type="radio" name="search_curoption" value="origin" checked />显示原币</span>
					<span><input type="radio" name="search_curoption" value="standard" disabled />显示本位币</span>
					<span><input type="radio" name="search_curoption" value="all" disabled />显示原币和本位币</span>
				</div>
			</fieldSet>
			<div class="search_row8">
				<span><input type="checkbox" class="search_showInitialBalance" checked />显示期初余额</span>
				<span><input type="checkbox" class="search_showCurPeriodAccount" checked />显示本期合计</span>
				<!--<span><input type="checkbox" class="search_showDet" />显示明细记录</span>-->
			</div>
			<div class="search_row9">
				<!--<span><input type="checkbox" class="search_showTodayAccount" />显示本日合计</span>-->				
			</div>
			<div class="search_row10">
				<span><input type="checkbox" class="search_showAccumulativeTotal" />显示本年累计</span>
				<span><input type="checkbox" class="search_showTotal" checked />显示总计</span>
			</div>
			
		</div>
		<div class="search_foot">
				<span><button onclick="search()">确定</button><button onclick="closeDiv('.search_Dialog')">取消</button></span>
		</div>
	</div>
	
	<div class="bar">
		<span class="btn btn-small btn_open">打开</span>
		<span class="btn btn-small btn_add" onclick="add_ShowDiv()">新增</span>		
		<span class="btn btn-small btn_quote" onclick="quote_ShowDiv()">引入</span>
		<span class="btn btn-small btn_edit" onclick="edit_ShowDiv()" disabled>修改</span>
		<span class="btn btn-small btn_delete" onclick="delete_Item()">删除</span>
		<!-- <span class="btn btn-small btn_setting" onclick="setting_ShowDiv()">选项</span>-->
		<!-- <span class="btn btn-small btn_genBySingle" onclick="genBySingle()">导出</span>		
		<span class="btn btn-small btn_genBySingle" onclick="genBySingle()">按单</span>
		<span class="btn btn-small btn_genBySummary" onclick="genBySummary()">汇总</span>-->
		<span class="btn btn-small btn_import" onclick="importData()">导入</span>
		
		#if($hasAudit == 1)		
		<span class="btn btn-small btn_check" onclick="checkDet(1)">审核</span>
		#end
		#if($hasReAudit==1)
			<span class="btn btn-small btn_recheck" onclick="checkDet(0)">反审核</span>
		#end
		#if($hasPosted == 1)
			<span class="btn btn-small btn_posted" onclick="postDets()">过账</span>
		#end
		#if($hasRePosted == 1)
			<span class="btn btn-small btn_reposted" onclick="repostDets()">反过账</span>
		#end
		<span class="btn btn-small btn_export" onclick="exportData()">导出</span>
		<!-- <span class="btn btn-small btn_back">返回</span>-->
	</div>
	<div class="tip">
		<span>科目：</span><span class="tip_acccode"></span><span> 币别：</span><span class="tip_currency"></span><span> 期间：</span><span class="tip_period"></span>
	</div>
	<div class="data_container">
		<div class="data_box">
		<div class="data_top">
			<table>
				<thead>
					<tr>
						<th width=20>No</th>
						<th width=16><input type="checkbox" class="checkSelected" onclick="checkDets(this)" /></th>
						<th width=15>审核</th>
						<th width=15>过账</th>
						<th>日期</th>
						<th>序号</th>
						<th>凭证字号</th>
						<th>凭证期间</th>
						<th>凭证审核</th>
						<th>过账标志</th>
						<th>摘要</th>
						<th>对方科目</th>
						<th>借方金额</th>
						<th>贷方金额</th>
						<th>余额</th>
						<th>经手人</th>
						<th>制单人</th>
					</tr>
				</thead>
			</table>
		</div>
		<div class="data_body">
			<table>
				<thead>
					<th width=20>No</th>
					<th width=16><input type="checkbox" class="checkSelected" onclick="checkDets(this)" /></th>
					<th width=15>审核</th>
					<th width=15>过账</th>
					<th>日期</th>
					<!-- <th>序号</th>-->
					<th>凭证字号</th>
					<th>凭证期间</th>
					<th>凭证审核</th>
					<th>过账标志</th>
					<th>摘要</th>
					<th>对方科目</th>
					<th>借方金额</th>
					<th>贷方金额</th>
					<th>余额</th>
					<th>经手人</th>
					<th>制单人</th>
				</thead>
				<tbody>
				
				</tbody>
			</table>
		</div>
		</div>
	</div>
</body>
</html>