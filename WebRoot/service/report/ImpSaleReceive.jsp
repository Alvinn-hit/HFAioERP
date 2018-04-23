<!DOCTYPE html >
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>门店收款单</title>
<link rel="stylesheet" href="/style/css/log.css" type="text/css" />
<script type="text/javascript" src="/js/jquery.js"></script>  
	 	 
<script type="text/javascript">
  function impData(){
	  var formData = new FormData($("#form")[0]);
	  $(".result").html("正在导入...");
	  jQuery.ajax({
		  url:"/impExcelData",
		  data:formData,
		  type:"post",
		  processData:false,
		  contentType:false,
		  success : function(data) {
				alert('导入完成');
				console.log(data.sNum);
				console.log(data.fNum);
				var div = '<div>导入成功：'+data.sNum+'</div>';
					div +='<div>导入失败：'+data.fNum+'</div>';
				if(data.fNum > 0){
				//if(data.sNum > 0){	
					div +='<div>失败单据：</div>';
					for(var i = 0 ;i<data.fRec.length;i++){
						var item = data.fRec[i];
						div += '<div>单据日期：'+item.BillDate;
						div += ';客户名称：'+item.ComFullName;
						div += ';备注：'+item.Remark;
						div += '</div>';
					}				
				}
				$(".result").html(div);
				
			},
			error : function(error) {
				alert("上传数据异常。");
			}
	  });
  }
</script>
</head> 
  <body class="html">
  	<div class="body">
    	<form method="post" scope="request" id="form" name="form" action="/impDataAction.do" enctype="multipart/form-data">
	    	<input type="hidden" name="tableName" value="SaleReceive" />
	    	上传xls格式文件：
	    	<input type="file" name="fileName" id="fileName" style="width:250px;height:24px; vertical-align:bottom" class="text">
	   		<button type="button" onclick="impData()" class="bu_02">导入</button>
   		</form>
    </div>
    <div class="result"></div>
    <div class="foot"></div>
  </body>
</html>
