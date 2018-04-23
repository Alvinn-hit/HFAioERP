<!DOCTYPE html >
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>单品进销存</title>
<link rel="stylesheet" href="/style/css/log.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<style type="text/css">
  .com_box,.goods_box{
	display:none;
	position:absolute;
	background:#fff;
	width:55%;
	height:450px;
	overflow-y:hidden;
  }
  
  .goods_box{	
	width:40%;	
  }
  
  .com_box_head,.goods_box_head{width:100%;}
  .com_box_list,.goods_box_list{
  	width:100%;  	
  }
  .com_box_list_div,.goods_box_list_div{
  	height:400px;
  	overflow-y:auto;
  }
  .report{font-size:12px;border-collapse:collapse;border-spacing:0;border-left:1px solid #c2c2c2;border-bottom:1px solid #c2c2c2;}
  .report thead td{
  	color:#fff;
  }
  .report thead{text-align:center;background:#5fa3e7;background-image:-webkit-linear-gradient(top,#5fa3e7,#428bca);background-image:linear-gradient(top,#5fa3e7,#428bca);}
  .report tr td{border-right:1px solid #c2c2c2;border-top:1px solid #c2c2c2;border-spacing:0;padding:0 5px;}			
</style>
<script type="text/javascript">
	$(function(){
		$(document.body).on("click",".com_box_list .list_item",function(){
			  $(".companyCode").val($(this).children(":eq(0)").text());
		  });
		 $(document).bind('click',function(e){
	         var e = e || window.event;
	         var elem = e.target || e.srcElement;
	         while (elem) {
	             if (elem.className && elem.className.indexOf('com_list')>-1) {
	                 return;
	             }
	             elem = elem.parentNode;
	         }
	         $('.com_box').css('display','none'); 
	     });
	
		 $(document).bind('click',function(e){
	         var e = e || window.event;
	         var elem = e.target || e.srcElement;
	         while (elem) {
	             if (elem.className && elem.className.indexOf('com_list')>-1) {
	                 return;
	             }
	             elem = elem.parentNode;
	         }
	         $('.goods_box').css('display','none'); 
	     });
	});
	/*******门店搜索相关******/
	function queryCom(obj,robj){		
		var callBack={				
			success:function(data){				
				if(data.code=='OK'){
					var htm = '';
					//****拼装门店列表
					var list = data.data;
					for(var o in list){						
						htm += '<tr class="list_item">';
						htm += '<td>'+list[o].classCode+'</td><td>'+list[o].ComFullName+'</td><td>'+list[o].ComFullNamePYM+'</td>';
						htm += '</tr>';
					}		
					robj.html(htm);
				} else{						
					
				}
				$(".com_box").css("display","inherit");
			},
			complete:function(){				
				$(".com_box").css("display","inherit");
			}
		}
		var url = "ReportApi.jsp";
		var data ={
			op:'queryCom',
			keyword:$(obj).val(),		
		};
		commitData(url,data,callBack);
	}
	
	/*******商品搜索相关******/
	function queryGood(obj,robj){		
		var callBack={				
			success:function(data){				
				if(data.code=='OK'){
					var htm = '';
					//****拼装商品列表
					var list = data.data;
					for(var o in list){						
						htm += '<tr class="list_item">';
						htm += '<td>'+list[o].goodsNumber+'</td><td>'+list[o].GoodsFullName+'</td>';
						htm += '</tr>';
					}		
					robj.html(htm);
				} else{						
					alert("查询失败！");
				}
				$(".goods_box").css("display","inherit");
			},
			complete:function(){				
				$(".goods_box").css("display","inherit");
			}
		}
		var url = "ReportApi.jsp";
		var data ={
			op:'queryGood',
			keyword:$(obj).val(),		
		};
		commitData(url,data,callBack);
	}
	
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
</script>	 	 
<script type="text/javascript">
  $(function(){
	  $(document.body).on("click",".goods_box_list .list_item",function(){
		  $(".goodCode").val($(this).children(":eq(1)").text());
		  $(".goodCode").attr("goodCode",$(this).children(":eq(0)").text());	  	  
	  });
	  
	  $(document.body).on("click",".search",function(){
		  var callBack={				
					success:function(data){						
						if(data.code=='OK'){
							var htm = '';
							var list = data.data;
							for(var o in list){
								htm += "<tr>";
								htm += "<td>"+list[o].date+"</td><td>"+list[o].yesterdayLevCount+list[o].BaseUnit+"</td>"+
									   "<td>"+list[o].todayInCount+list[o].BaseUnit+"</td><td>"+list[o].todayTransferCount+"</td>"+
									   "<td>"+list[o].materialLossCount+list[o].BaseUnit+"</td><td>"+list[o].productLossCount+list[o].BaseUnit+"</td>"+
									   "<td>"+list[o].posUseCount+list[o].BaseUnit+"</td><td>"+list[o].todayInvtCount+"</td>"+
									   "<td>"+list[o].theoryInvtCount+"</td><td>"+list[o].difference+"</td>"+
									   "<td>"+list[o].diffAmount+"</td><td>"+list[o].calTurnover+"</td>"+
									   "<td>"+list[o].relTurnover+"</td><td>"+list[o].diffTurnover+"</td>";
								htm += "</tr>";
							}
							
							$(".report tbody").html(htm);
						} else{						
							
						}						
					},
					complete:function(){
						
					}
				}
				var url = "ReportApi.jsp";
				var data ={
					op:'getSingleInvt',
					goodCode:$(".goodCode").attr("goodCode"),
					companyCode:$(".companyCode").val(),
					begD:$(".begD").val(),
					endD:$(".endD").val()
				};
				commitData(url,data,callBack);
	  });	  	 
  });  
	
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
</script>
</head> 
  <body class="html">
    <div class="top"></div>
    <div class="content">
    	<div class="searchBar">
    		<span>单品：</span><span><input type="text" class="goodCode" onkeyup="queryGood(this,$('.goods_box_list tbody'))" /></span>
    		<div class="goods_box">
				<table class="goods_box_head">
					<thead>
						<tr>
							<td>商品编号</td>
							<td>商品名称</td>							
						</tr>
					</thead>
				</table>
				<div class="goods_box_list_div">
					<table class="goods_box_list">					
						<tbody>
						</tbody>					
					</table>
				</div>
			</div>  
    		<span>门店：</span><span><input type="text" class="companyCode" onkeyup="queryCom(this,$('.com_box_list tbody'))" /></span>
    		<div class="com_box">
				<table class="com_box_head">
					<thead>
						<tr>
							<td>门店编码</td>
							<td>门店名称</td>
							<td>拼音码</td>
						</tr>
					</thead>
				</table>
				<div class="com_box_list_div">
					<table class="com_box_list">					
						<tbody>
						</tbody>					
					</table>
				</div>
			</div>
    		<span>开始日期：</span><span><input type="date" class="begD" /></span>
    		<span>结束日期：</span><span><input type="date" class="endD" /></span>    		  	
    		<span class="search">查询</span>
    	</div>
    	<div class="list">
    		<table class="report">
    			<thead>
    				<tr>    				
    					<td>日期</td>
    					<td>昨天库存量</td>
    					<td>进货</td>
    					<td>调货</td>
    					<td>原料损耗</td>
    					<td>成品损耗</td>
    					<td>POS机用量</td>
    					<td>今天盘点库存量</td>
    					<td>POS理论存量</td>
    					<td>差异</td>
    					<td>差异金额</td>
    					<td>预估营业额</td>
    					<td>实际营业额</td>
    					<td>差异</td>
    				</tr>
    			</thead>
    			<tbody>
    			
    			</tbody>
    		</table>
    	</div>
    </div>
    <div class="foot"></div>
  </body>
</html>
