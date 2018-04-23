<!DOCTYPE html>
<head>
	<title>出纳反月结</title>
	<script type="text/javascript" src="/js/jquery.js"></script>
	<script type="text/javascript" src="/js/jquery-ui.js"></script>
</head>
<style>
	.data_msg{
		color:red;
	}
	.div_row{
		margin:20px 0;
	}
	.div_container{
		text-align:center;
	}
	
	.b_5{
	    width: 16px;
	    height: 16px;
	    display: inline-block;
	    margin:0 auto;
	    background: url(/style/images/body/i_017.gif) no-repeat 0 0;	   
	}	
	
	.div_ts{
		width: 120px;
    	border: 2px aliceblue solid;
   		border-radius: 7px;
    	cursor: pointer;
    	text-align: center;
    	margin:0 auto;
	}
	.div_content,.tab_content{
		margin:0 auto;
	}
	.div_ts:hover{
		border: 2px lightblue solid;
	}
</style>
<script type="text/javascript">
	$(function(){
		$(".div_ts").on("click",function(){
			if(!window.confirm("确定执行日结账反月结？")){
				return;
			}
			showDiv('.loading');
			var data = {};
			data.operation = 136;
			jQuery.post("/CashierAction.do?",data,  
	        function (data) {  
	            var obj; 
	            if(data.code == 0){	            	
					if(data.msg != undefined){
						alert(data.msg);
					}
					if(data.data!= undefined){
						
					}
	            } else{
	            	//反月结成功
	            	$(".data_msg").text("反月结完成");
	            	setTimeout(function(){window.location.reload();},2000);	
	            }
	            closeDiv('.search_Dialog');
	         },"json"); 
		});
	});

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

	/* 隐藏div */
	function closeDiv(obj){
		$(obj).hide();
		if(document.getElementById('back')!=null){
			document.getElementById('back').parentNode.removeChild(document.getElementById('back'));
		}
	}

	//让背景渐渐变暗
	function showBackground(obj,endInt){
		var al=parseFloat(obj.style.opacity);al+=0.05;
		obj.style.opacity=al;
		if(al<(endInt/100)){
			setTimeout(function(){showBackground(obj,endInt)},1);
		}		
	}
</script>

<body>		
		<div class="div_container">
			<div class="div_row">
				<div class="data_msg">
					
				</div>
			</div>
			<div class="div_row">
				<div class="div_ts div_ts_bcolor1" show="sysAcc" tg="5">
					<div><img src="/style/images/flow/war5.gif"></div>
					<div><label>反月结</label></div>
				</div>
			</div>
			
			<div class="div_row">
				<div class="div_head">
					<b class="b_5"></b>
				</div>
				<div class="div_content" style="display: block;" id="div5">								
					<table border="0" cellpadding="0" cellspacing="0" class="tab_content acc">
						<tbody>
						<tr class="acctr">
							<td width="100">期间年</td>
							<td width="100">期间</td>
							<td width="100" class="td1" align="center" rowspan="2">反月结至</td>
							<td width="100">期间年</td>
							<td width="100">期间</td>
						</tr>
						<tr>
							<td width="100">$!CashierYear</td>
							<td width="100">$!CashierMonth</td>
							<td width="100">$!CashierYearPre</td>
							<td width="100">$!CashierMonthPre</td>
						</tr>
						</tbody>
					</table>
					
				</div>
			</div>
		</div>
</body>
</html>