<!DOCTYPE html>
<head>
	<title>银行对账</title>
	<link type="text/css" rel="stylesheet" href="/style/css/base_button.css">
	<script type="text/javascript" src="/js/jquery.js"></script>
	<script type="text/javascript" src="/js/jquery-ui.js"></script>
</head>

<style>
	.search_Dialog{
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
	.search_Dialog .search_title{
		cursor:move;
		height: 28px;
    	background: #428bca;
	}
	.search_Dialog div{		
	    line-height: 28px;
	    padding:0 9px;
	    background: url(../images/oaimages/pop-up_tite.gif);
	}
	.search_Dialog .search_period{
		width:100px;
	}
	.search_foot{
		margin:5px 0;
	}
	.search_foot button{
		margin:5px;
	}
	
	.bar{
		margin:10px 0;
	}
	.tip{		
		margin-left:10px;
	}
	.data_container1,.data_container2{
		margin:10px 0px;
	}
	
	.data_top table,.data_body table{
		font-size: 12px;
    	border-collapse: collapse;
    	border-spacing: 0;
    	border-left: 1px solid #c2c2c2;
    	border-bottom: 1px solid #c2c2c2;
	}
	
	.data_top table thead,.data_body table thead{
		background: #5fa3e7;
   		background-image: -webkit-linear-gradient(top,#5fa3e7,#428bca);
	}
	.data_top table thead th,.data_body table thead th{
		color: #fff;
	}
	.data_top table td,.data_body table td{
		border-right: 1px solid #c2c2c2;
	    border-top: 1px solid #c2c2c2;
	    border-spacing: 0;
	    padding: 0 5px;
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
	    width: 70%;	   
	    position: absolute;
	    top: 0px;
	    left: 0px;
	    overflow: auto;
	}
</style>
<script type="text/javascript">
	$(function(){		
		#if(!$!accCode)
			showDiv('.search_Dialog');
		#end
		$(".search_Dialog").draggable();
		$(".btn_open").on("click",function(){
			showDiv('.search_Dialog');
		});
		$(".data_container").css("height",document.documentElement.clientHeight-100+'px');
		$(".data_top table").css("width",$(".data_body table").width()-20);
		$(".data_body table").css("width",$(".data_body table").width()-20);
		$(".data_body").height(document.documentElement.clientHeight-100+'px');
	});
	
	//打开对应科目日记账
	var global_search ;
	function search(){
		//****验证参数
		var flag = true;					
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
		//****end
		//***回填提示栏***//		
		$(".tip_period").text($(".search_periodYear").val()+'年'+$(".search_periodMonth").val()+'期');		
		$(".tip_period").text();
		//****end****//
		
		var data = {};
		data.operation = 120;
		data.periodYear = $(".search_periodYear").val();
		data.periodMonth = $(".search_periodMonth").val();
		global_search = data;
		jQuery.post("/CashierAction.do?",data,  
	        function (data) {  
	            var obj; 
	            if(data.code == 0){
	            	alert('查询失败！');
	            } else{
	            	obj = eval(data);  
		            dealResult(data.data);	
	            }
	            closeDiv('.search_Dialog');
	         },"json"); 
	}
	
	function dealResult(data){
		var htm = '';
		var htm = "";
		for(var i = 0;i<data.length;i++){
			var tr = '';
			tr += '<td>'+data[i].project+'</td>';
			tr += '<td>'+data[i].cashier+'</td>';
			tr += '<td>'+data[i].finance+'</td>';
			tr += '<td>'+data[i].diff+'</td>';
			tr = "<tr>"+tr+"</tr>";
			htm += tr;
		}
		$(".data_table tbody").html(htm);
	}
	
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
		var bHeight=parseInt(document.documentElement.clientHeight);
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
</script>
<body>
	<!-- 打开查询科目对话框 -->
	<div class="search_Dialog">
		<div class="search_title">
			<span class="ico_4"></span>
			条件查询
		</div>
		<div class="search_content">			
			<fieldSet>
				<legend>期间</legend>				
				<div class="search_div1">
					<div class="search_row">
						<span>会计期间：<input type="number" class="search_period search_periodYear" />年</span> <span><input type="number" class="search_period search_periodMonth" /> 期</span>						
					</div>					
				</div>					
			</fieldSet>				
		</div>
		<div class="search_foot">
				<span><button onclick="search()">确定</button><button onclick="closeDiv('.search_Dialog')">取消</button></span>
		</div>
	</div>
	
	<div class="bar">
		<span class="btn btn-small btn_open">打开</span>	
		<span class="btn btn-small btn_back">返回</span>
	</div>
	
	<div class="tip">
		<span> 期间：</span><span class="tip_period"></span>
	</div>
	
	<div class="data_container">
		<div class="data_box">
		<div class="data_top">
			<table>
				<thead>
					<tr>
						<th>项目</th>
						<th>出纳管理系统</th>
						<th>总账系统</th>
						<th>差额</th>						
					</tr>
				</thead>
			</table>
		</div>
		<div class="data_body">
			<table class="data_table">
				<thead>
					<th>项目</th>
						<th>出纳管理系统</th>
						<th>总账系统</th>
						<th>差额</th>
				</thead>
				<tbody>
					
				</tbody>
			</table>
		</div>
		</div>
	</div>		
</body>
</html>