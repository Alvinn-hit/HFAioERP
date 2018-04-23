<!DOCTYPE html >
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>进销存</title>
<link rel="stylesheet" href="/style/css/log.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript">
$(function(){
	$(document.body).on("click",".list_item",function(){
			  $(".companyCode").val($(this).children(":eq(1)").text());
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
     $(document.body).on("click",".search",function(){
     	var callBack={				
					success:function(data){						
						if(data.code=='OK'){
							var htm = '';
							var list = data.data;
							for(var o in list){
								htm += "<tr>";
								htm += "<td>"+list[o].ID+"</td>"+
									   "<td>"+(list[o].project != undefined ? list[o].project:"")+"</td>"+									  									 
									   "<td>"+(list[o].value != undefined ? list[o].value :"")+"</td>";
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
					op:'getDailyBussiness',
					companyCode:$(".companyCode").val(),
					date:$(".date").val()
				};
				commitData(url,data,callBack);
     });
});
/*******门店搜索相关******/
function queryCom(obj){		
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
				$(".com_box_list").html(htm);
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
<style type="text/css">
.search{border-right:1px #4086ce solid;background-color:#5fa3e7;color:#fff;padding:0 10px;height:27px;line-height:27px;border-radius:3px 0 0 3px;cursor:pointer;}
.search:hover{background-color:#4086ce;}
.com_box{
	display:none;
	width:25%;
	position:absolute;
	background:#fff;	
	height:450px;
	overflow-y:hidden;
  }
 ．com_box_head,.com_box_list{
 	 margin: 0 auto;
 }
 .com_box_head{width:100%;}
 .com_box_head thead tr{background:#5fa3e7;}
  .com_box_list{
  	width:100%;  	
  }
  .com_box_list,.com_box_head{table-layout:fixed;}
  .com_box_list_div{
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
</head>
<body>
	<div class="content">
		<div class="searchBar">
			<span>门店：</span><span><input type="text" class="companyCode" onkeyup="queryCom(this)" /></span>
		
		<div class="com_box">
				<table class="com_box_head">
					<thead>
						<tr>
							<td>门店编码</td>
							<td>门店名称</td>							
							<td>拼音码</td>
						</tr>
					</thead>
					<tbody>						
					
					</tbody>
				</table>
				<div class="com_box_list_div">
					<table class="com_box_list">					
						<tbody>
						</tbody>					
					</table>
				</div>
		</div>
		<span>日期：</span><span><input type="date" class="date" /></span>
		<span class="search">查询</span>
	</div>
	<div class="list">
		<table class="report">
			<thead>
				<tr>
					<td>序号</td>
					<td>项目</td>
					<td>金额</td>
				</tr>
			</thead>
			<tbody>
			
			</tbody>
		</table>
	</div>
</body>
</html>
