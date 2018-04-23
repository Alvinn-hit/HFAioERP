<!DOCTYPE html >
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>进销存</title>
<link rel="stylesheet" href="/style/css/log.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>  
<style type="text/css">
  input{border-radius:3px;border: 1px solid #ccc;}  
  
  table{table-layout:fixed;font-size:12px;border-collapse:collapse;border-spacing:0;border-left:1px solid #c2c2c2;border-bottom:1px solid #c2c2c2;}
  thead{
  	text-align:center;background:#5fa3e7;background-image:-webkit-linear-gradient(top,#5fa3e7,#428bca);background-image:linear-gradient(top,#5fa3e7,#428bca);
  }
  table td{border-right:1px solid #c2c2c2;border-top:1px solid #c2c2c2;border-spacing:0;padding:2px 5px;}
  
  .btn:hover{ color: #333333;
  text-decoration: none;
  background-position: 0 -15px;
  -webkit-transition: background-position 0.1s linear;
  -moz-transition: background-position 0.1s linear;
  -o-transition: background-position 0.1s linear;
  transition: background-position 0.1s linear;}
  
  .btn {
  display: inline-block;
  *display: inline;
  padding: 4px 12px;
  margin-bottom: 0;
  *margin-left: .3em;
  font-size: 14px;
  line-height: 20px;
  color: #333333;
  text-align: center;
  text-shadow: 0 1px 1px rgba(255, 255, 255, 0.75);
  vertical-align: middle;
  cursor: pointer;
  background-color: #f5f5f5;
  *background-color: #e6e6e6;
  background-image: -webkit-gradient(linear, 0 0, 0 100%, from(#ffffff), to(#e6e6e6));
  background-image: -webkit-linear-gradient(top, #ffffff, #e6e6e6);
  background-image: linear-gradient(to bottom, #ffffff, #e6e6e6);
  background-repeat: repeat-x;
  border: 1px solid #cccccc;
  *border: 0;
  border-color: #e6e6e6 #e6e6e6 #bfbfbf;
  border-color: rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.25);
  border-bottom-color: #b3b3b3;
  -webkit-border-radius: 4px;
  -moz-border-radius: 4px;
  border-radius: 4px;
  filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffffffff', endColorstr='#ffe6e6e6', GradientType=0);
  filter: progid:DXImageTransform.Microsoft.gradient(enabled=false);
  *zoom: 1;
  -webkit-box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2), 0 1px 2px rgba(0, 0, 0, 0.05);
  -moz-box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2), 0 1px 2px rgba(0, 0, 0, 0.05);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2), 0 1px 2px rgba(0, 0, 0, 0.05);
}
  .goods_btns li,.coms_btns li{float:left;} 
  .goods_btns .btn,.coms_btns .btn{float:left;}
  
  .goods_btns,.coms_btns{float:right;}
  
  .goods_header,.coms_header{width:96%;}
  .goods_body,.coms_body{width:100%;}
  
  
  .goods_select{background:#fff;position:absolute;top:20%;left:20%;display:none;border:1px solid #eee;border-radius:2px;padding:5px;width:30%;}  
  .coms_select{background:#fff;position:absolute;top:20%;left:20%;display:none;border:1px solid #eee;border-radius:2px;padding:5px;width:30%;}
  
  .stBtn{position:absolute;background-position:-32px 0;background-color:#fff;cursor:pointer;margin-top:6px;margin-left:-20px;}
  .icon16{width:16px;height:16px;display:inline-block;background-image:url(/style/images/client/icon16.png);background-repeat:no-repeat;}
  
  .list{table-layout:fixed;}
  .report,.report_head{width:75%;}  
  .items{width:75%;table-layout:fixed;max-height:400px;overflow-y:auto;overflow-x:hidden;}
  .report,.report_items{
  	table-layout:fixed;
  }
  .report,.report_items{
      width:100%;
	}    
  .report td,.report_items td{
  	white-space:normal;
  }
  .report_items td{border-collapse:collapse;border-right:1px solid #c2c2c2;border-top:1px solid #c2c2c2;border-spacing:0;padding:5px 5px;} 
  .report_items{width:100%;font-size:12px;border-collapse:collapse;border-spacing:0;border-left:1px solid #c2c2c2;border-bottom:1px solid #c2c2c2;} 
  .report_items tr:hover{background:#4BC6D1;}  
  .report td:first-child,.report_items td:first-child{
  	width:200;
  }
  .spaceRow{background: #d7ecff;}
  .content{
  	padding:0 0 0 10px;
  }
  .list{
  	margin:10px 5px;
  }
  .search,.download{
  	border-radius:3px 3px 3px 3px;
  	background-color:#5fa3e7;
  	color:#fff;
  	padding:3px 10px;
  	height:27px;
  	line-height:27px;
  	cursor:pointer;
  } 
  .search:hover,.download:hover{
  	background-color:#4086ce;
  }
  .com_box,.goods_box{
	display:none;
	position:absolute;
	background:#fff;
	width:25%;
	height:450px;
	overflow-y:hidden;
  }
  .goods_box{width:20%;}
  .goods_box_head,.goods_box_list{table-layout:fixed;}
  .com_box_head,.goods_box_head{width:100%;}
  .com_box_head,.com_box_list{table-layout:fixed;}
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
  .report tr td{border-right:1px solid #c2c2c2;border-top:1px solid #c2c2c2;border-spacing:0;padding:2px 5px;}			

  .iframe,.hidden{display:none;}  
</style>
<style>
	.coms_select_body,.goods_select_body{
		max-height:350px;
		overflow-y:auto;
	}
</style>
<script type="text/javascript">
	$(function(){		
		var _w = $(".companyCode").width();
		$(".goods_box").css("left",_w);
		//*****设置当前日期*****//
		var today = new Date();   
		var day = today.getDate();   
		var month = today.getMonth() + 1;   
		var year = today.getFullYear();    		
		var date = year + "-" + (month<10?"0":"")+ month + "-" + (day<10?"0":"") + day ;
		$(".date").attr("value",date);
		//********end*******//
		$(document.body).on("click",".list_item",function(){
			$(".companyCode").val($(this).children(":eq(1)").text()).attr("code",$(this).children(":eq(0)").text());			  
		});
		$(document.body).on("click",".goods_item",function(){
			$(".good").val($(this).children(":eq(1)").text()).attr("code",$(this).children(":eq(0)").text());
		});
		$(document).bind('click',function(e){
	         var e = e || window.event;
	         var elem = e.target || e.srcElement;
	         while (elem) {
	             if(elem.className && elem.className.indexOf('com_list')>-1) {
	                 return;
	             }
	             if(elem.className && elem.className.indexOf('goods_box')>-1) {
	                 return;
	             }
	             elem = elem.parentNode;
	         }
	         $('.com_box').css('display','none'); 
	         $('.goods_box').css('display','none'); 
	     });			
	});
	
	/*******提交门店********/
	function commitComSelect(){
		var coms = '',names = '';		
		$(".com_check:checked").each(function(){
			coms += (coms==''?'':',')+$(this).parents("tr:eq(0)").find("td:eq(1)").text();
			names += (names==''?'':',')+$(this).parents("tr:eq(0)").find("td:eq(2)").text();
		});
		$(".companyCode").val(names).attr('code',coms);
		closeComSelect();
	}
	/********关闭门店选择*****/
	function closeComSelect(){
		$(".coms_select").css("display","none");
	}
	/*******提交选择商品******/
	function commitGoodSelect(){
		var goods = '',names='';
		$(".good_check:checked").each(function(){
			goods += (goods==''?'':',')+$(this).parents("tr:eq(0)").find("td:eq(1)").text();
			names += (names==''?'':',')+$(this).parents("tr:eq(0)").find("td:eq(2)").text();
		});
		$(".good").val(names).attr('code',goods);
		closeGoodSelect();
	}
	/********关闭门店选择*****/
	function closeGoodSelect(){
		$(".goods_select").css("display","none");
	}
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
						htm += '<td>'+list[o].classCode+'</td><td>'+list[o].ComFullName+'</td>';
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
	
	/*******显示门店列表（多选）********/
	function openComSelect(){
		var callBack={				
			success:function(data){				
				if(data.code=='OK'){
					var htm = '';
					//****拼装门店列表
					var list = data.data;
					for(var o in list){						
						htm += '<tr class="com_item">';
						htm += '<td><input type="checkbox" class="com_check" /></td><td>'+list[o].classCode+'</td><td>'+list[o].ComFullName+'</td>';
						htm += '</tr>';
					}
					$(".coms_body tbody").html(htm);
				} else{						
					alert("查询失败！");
				}
				
			},
			complete:function(){
			}
		}
		var url = "ReportApi.jsp";
		var data ={
			op:'queryCom',
			keyword:''
		};
		commitData(url,data,callBack);
		$(".coms_select").css("display","block");		
	}	
	
	/*******显示商品列表（多选）*******/
	function openGoodSelect(){
		var callBack = {				
			success:function(data){				
				if(data.code=='OK'){
					var htm = '';
					//****拼装门店列表
					var list = data.data;
					for(var o in list){						
						htm += '<tr class="good_item">';
						htm += '<td><input type="checkbox" class="good_check" /></td><td>'+list[o].goodsNumber+'</td><td>'+list[o].GoodsFullName+'</td>';
						htm += '</tr>';
					}		
					$(".goods_body tbody").html(htm);
				} else{						
					alert("查询失败！");
				}				
			},
			complete:function(){				
			}
		}
		var url = "ReportApi.jsp";
		var data ={
			op:'queryGood',
			keyword:'',		
		};
		commitData(url,data,callBack);
		$(".goods_select").css("display","block"); 
	}
	
	/*******商品搜索相关******/
	function queryGoods(obj,robj){		
		var callBack={				
			success:function(data){				
				if(data.code=='OK'){
					var htm = '';
					//****拼装门店列表
					var list = data.data;
					for(var o in list){						
						htm += '<tr class="goods_item">';
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
	
	/**********选择公司******/
	function selectComs(obj){		
		if($(obj).attr("checked")== undefined){
			$(".com_check:checked").removeAttr("checked");
		} else{
			$(".com_check").attr("checked","checked");
		}
	}
	
	/*******选择商品******/
	function selectGoods(obj){
		if($(obj).attr("checked")== undefined){
			$(".good_check:checked").removeAttr("checked");
		} else{
			$(".good_check").attr("checked","checked");
		}
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
			complete: callbackfun.complete,
			error:callbackfun.error
		});
	}
</script>	 	 
<script type="text/javascript">
  $(function(){	  
	  $(document.body).on("click",".download",function(){
		  var callBack={				
					success:function(data){						
						if(data.code=='OK'){
							$(".iframe").attr("src","./tmp/"+data.data);
						} else{						
							alert(data.description);
						}						
					},
					complete:function(){
						$(".loading").addClass("hidden");
						$(".download").removeClass("hidden");
					}
				};
		 	var url = "ReportApi.jsp";
			var data ={
				op:'genPackageRankExcel',
				companyCode:$(".companyCode").val()!=""?$(".companyCode").attr('code'):'',
				begD:$(".begD").val(),
				endD:$(".endD").val(),
				GoodsCode:$(".good").val()!=""? $(".good").attr("code"):''
			};
			$(".download").addClass("hidden");
			$(".loading").removeClass("hidden");
			commitData(url,data,callBack);
	  });
	  $(document.body).on("click",".search",function(){
		  var callBack={				
					success:function(data){						
						if(data.code=='OK'){
							var htm = '';							
							var list = data.data;
							var flag = true;							
							for(var o in list){
								if(flag){
									htm += "<tr class='spaceRow'>";
								}else{
									htm += "<tr>";
								}																
								htm += "<td>"+list[o].no+"</td><td colspan='3'>"+list[o].ComFullName+"</td><td>"+(list[o].year != undefined ? list[o].year:"")+"</td>"+
									   "<td>"+(list[o].month != undefined ? list[o].month:"")+"</td><td>"+(list[o].day != undefined ? list[o].day:"")+"</td>"+
									   "</td><td colspan='2'>"+(list[o].name != undefined ? list[o].name:"")+"</td>"+									   
									   "<td>"+(list[o].salesCount != undefined ? list[o].salesCount:"")+"</td>"+
									   "<td>"+(list[o].salesAmount != undefined ? list[o].salesAmount :"")+"</td>";									 
								htm += "</tr>";
								flag = !flag;
							}
							//*****表格body特殊处理****//							
							if(list.length>=14){
								$(".items").css("width","76.2%");
							}
							$(".report_items tbody").html(htm);							
						} else{						
							$(".report_items tbody").html(data.description);
						}						
					},
					complete:function(){
						
					}
				}
				var url = "ReportApi.jsp";
				var data ={
					op:'getPackageRank',
					companyCode:$(".companyCode").val()!=""?$(".companyCode").attr('code'):'',
					begD:$(".begD").val(),
					endD:$(".endD").val(),
					GoodsCode:$(".good").val()!=""? $(".good").attr("code"):''
				};
				$(".report_items tbody").html("查询中...");
				commitData(url,data,callBack);
	  });	  	 
  });  		
</script>
</head> 
  <body class="html">
  	<!----------商品选择框------------->
  	<div class="goods_select">
  		<div class="goods_select_bar"><ul class="goods_btns"><li><span class="btn" onclick="commitGoodSelect()">确定</span></li><li><span class="btn" onclick="closeGoodSelect()">关闭</span></li></ul></div>
  		<div style="clear:both;"></div>
  		<div class="goods_select_content">
  			<div class="goods_select_header">
  				<table class="goods_header">
  					<thead><tr><td><input type="checkbox" onchange="selectGoods(this)" /></td><td>编号</td><td>名称</td></tr></thead>
  				</table>
  			</div>
  			<div class="goods_select_body">
  				<table class="goods_body">
  					<tbody></tbody>
  				</table>
  			</div>  		
  		</div>
  	</div>
    <!-- end -->
  	<!----------门店选择框------------->
  	<div class="coms_select">
  		<div class="coms_select_bar"><ul class="coms_btns"><li><span class="btn" onclick="commitComSelect()">确定</span></li><li><span class="btn" onclick="closeComSelect()">关闭</span></li></ul></div>
  		<div style="clear:both;"></div>
  		<div class="coms_content">
  			<div class="coms_select_header">
  				<table class= "coms_header">
  					<thead><tr><td><input type="checkbox" onchange="selectComs(this)" /></td><td>门店编号</td><td>名称</td></tr></thead>
  				</table>
  			</div>
  			<div class="coms_select_body">
  				<table class="coms_body">
  					<tbody></tbody>
  				</table>
  			</div>
  		</div>
  	</div>
    <!-- end -->
    <div class="top"></div>
    <div class="content">
    	<div class="searchBar">
    		<span>门店：</span><span><input type="text" class="companyCode" onkeyup="queryCom(this,$('.com_box_list tbody'))" /><b class="stBtn icon16" onclick="openComSelect();"></b></span>
    		<div class="com_box">
				<table class="com_box_head">
					<thead>
						<tr>
							<td>门店编码</td>
							<td>门店名称</td>							
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
    		<span>产品：</span><span><input type="text" class="good" onkeyup="queryGoods(this,$('.goods_box_list tbody'))" /><b class="stBtn icon16" onclick="openGoodSelect();"></b></span>
    		<div class="goods_box">
				<table class="goods_box_head">
					<thead>
						<tr>
							<td>商品编号</td>
							<td>名称</td>							
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
    		<span>开始日期：</span><span><input type="date" class="date begD" value="" /></span>
    		<span>结束日期：</span><span><input type="date" class="date endD" value="" /></span>
    		
    		<span class="search">查询</span> 
    		
    		<span class="download">导出</span>
    		<span class="loading hidden">正在生成...</span>
    		<iframe src="" class="iframe"></iframe>
    	</div>
    	<div class="list">
    	<div class="report_head">
    		<table class="report" cellspacing="0">
    			<thead>
    				<tr>
    					<td>序号</td>
    					<td colspan="3">门店名称</td>
    					<td>年度</td>
    					<td>月份</td>
    					<td>日</td>    					
    					<td colspan='2'>产品名称</td>    					
    					<td>销量</td>
    					<td>销额</td>    					
    				</tr>
    			</thead>
    			<tbody>
    			
    			</tbody>
    		</table>
    	</div>
    	<div class="items">
    		<table class="report_items">
    			<tbody></tbody>
    		</table>
    	</div>  	    	
    </div>
    </div>
    <div class="foot"></div>
  </body>
</html>
