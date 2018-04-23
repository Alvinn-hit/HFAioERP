<!DOCTYPE html >
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>称重异常流水</title>
<link rel="stylesheet" href="/style/css/log.css" type="text/css" />
<link rel="stylesheet" href="/service/report/css/company.css" type="text/css" />
<link rel="stylesheet" href="/service/report/css/good.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/service/report/js/common.js"></script>
<script type="text/javascript" src="/service/report/js/company.js"></script>
<script type="text/javascript" src="/service/report/js/good.js"></script>
<script type="text/javascript">
$(function(){	
	$(".search_begD").attr('value',CurentDate());
	$(".search_endD").attr('value',CurentDate());
	$(document.body).on("click",".commit",function(){
		 var callBack = {
			success:function(data){
				if(data.code=='OK'){
					var htm = '';
					var list = data.data;
					var flag = true;
					for(var o in list){
						if(flag){
							htm += "<tr class='spaceRow'>";
						} else{
							htm += "<tr>";	
						}
						flag = !flag;
						htm += "<td>"+list[o].id+"</td>"+
							   "<td>"+(list[o].comFullName != undefined ? list[o].comFullName:"")+"</td>"+
							   "<td>"+(list[o].syjNo != undefined ? list[o].syjNo:"")+"</td>"+
							   "<td>"+(list[o].type != undefined ? list[o].type:"")+"</td>"+
							   "<td>"+(list[o].goodsNo != undefined ? list[o].goodsNo :"")+"</td>"+
							   "<td>"+(list[o].goodsName != undefined ? list[o].goodsName :"")+"</td>"+
							   "<td>"+(list[o].unit != undefined ? list[o].unit :"")+"</td>"+
							   "<td>"+(list[o].price != undefined ? list[o].price :"")+"</td>"+
							   "<td>"+(list[o].qty != undefined ? list[o].qty :"")+"</td>"+
							   "<td>"+(list[o].createTime != undefined ? list[o].createTime :"")+"</td>";
						htm += "</tr>";
					}					
					$(".tab_content tbody").html(htm);
				} else{
					$(".tab_content tbody").html(htm);
				}
			}
		 }
		 var url = "ReportApi.jsp";
		 var data={
			op:'getLogWeight',
			goodNo:$(".search_good").attr('goodcode')==undefined?$(".search_good").val():$(".search_good").attr('goodcode'),
			companyCode:$(".search_company").attr('companycode')==undefined?'':$(".search_company").attr('companycode'),
			begD:$(".search_begD").val(),
			endD:$(".search_endD").val(),
			syjNo:$(".search_no").val(),
			status:$(".search_status option:selected").attr("value")					
		 };
		 commitData(url,data,callBack);
	 });
});

function CurentDate()
{ 
    var now = new Date();
   
    var year = now.getFullYear();       //年
    var month = now.getMonth() + 1;     //月
    var day = now.getDate();            //日
   
    var hh = now.getHours();            //时
    var mm = now.getMinutes();          //分
   
    var clock = year + "-";
   
    if(month < 10)
        clock += "0";
   
    clock += month + "-";
   
    if(day < 10)
        clock += "0";
       
    clock += day;
      
    return clock; 
}
</script>
<style>
	.tab_head,.tab_content{table-layout:fixed;}
	.head{width:60%;}
	.tab_head{width:98%;}
	.tab_head thead tr{background:#5fa3e7;text-align:center;}
	.tab_head thead tr td{color:#fff;border-right:1px solid #c2c2c2;border-top:1px solid #c2c2c2;border-spacing:0;padding:0 5px;}
	.tab_content{width:100%;}
	.tab_content tbody td{border-right:1px solid #c2c2c2;border-top:1px solid #c2c2c2;border-spacing:0;padding:0 5px;}
	.tab_content tr:hover{background:#4BC6D1;}
	.tab_content .spaceRow{background:#d7ecff;}
	.content{height:500px;width:60%;overflow-y:auto;}
	input[class^="search"]:hover,select[class^="search"]:hover{border: 1px solid #999;}
	input[class^="search"],select[class^="search"]{border-radius:3px;border:1px solid #ccc}
	.commit{background-color:#5fa3e7;color:#fff;padding:3px 10px;height:27px;line-height:27px;border-radius:3px;cursor:pointer;}
	.commit:hover{background-color:#4086ce;}
</style>
</head>
<body>
	<div class="search_bar">
		<span>门店：</span><span><input type="text" class="search_company" /></span>		
		<span>商品：</span><span><input type="text" class="search_good" /></span>
		<span>终端号：</span><span><input type="text" class="search_no" /></span>
		<span>状态：</span>
		<span>
			<select class="search_status">
				<option value="1">全部</option>
				<option value="2">异常</option>
				<option value="3">删除</option>
				<option value="4">取消</option>
			</select>
		</span>
		<span>
		 发生时间 从
		</span><span><input type="date" class="search_begD" /></span>
		<span><input type="date" class="search_endD" /></span>
		<span class="commit">查询</span>
	</div>	
		<div class="head">
			<table class="tab_head">
				<thead>
					<tr>
						<td>序号</td>
						<td>门店</td>
						<td>终端号</td>
						<td>状态</td>
						<td>商品编号</td>
						<td>商品名称</td>
						<td>单位</td>
						<td>零售价</td>
						<td>数量</td>
						<td>发生时间</td>
					</tr>
				</thead>
			</table>
		</div>	
	<div class="content">
		<table class="tab_content">
			<tbody>
				
			</tbody>
		</table>
	</div>
</body>
</html>