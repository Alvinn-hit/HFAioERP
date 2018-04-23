<!DOCTYPE html>
<head>
	<title>数据导入</title>
	<link type="text/css" rel="stylesheet" href="/style/css/base_button.css">
	<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
	
	<script type="text/javascript" src="/js/jquery.js"></script>
	<script type="text/javascript" src="/js/jquery-ui.js"></script>
	<script type="text/javascript" src="/vm/finance/cashier/CashierUtil.js"></script>		
</head>
<script>
function impData(obj){
	  var formData = new FormData($("#form")[0]);	 
	  window.parent.showDiv('.loading');
	  jQuery.ajax({
		  url:"/CashierAction.do?operation=102",
		  data:formData,
		  type:"post",
		  dataType: "json",
		  processData:false,
		  contentType:false,
		  success : function(data) {				
				
			  	if(data.code==0){
					alert("导入失败");	
				}else{
					window.parent.importAfter(data);
				}		
			},
			error : function(error) {
				alert("上传数据异常。");
			},
			complete:function(){
				$(obj).val('');
				$("#form")[0].reset();
				window.parent.closeDiv('.loading');
			}
	  });
}

</script>
<body>
 	<form method="post" scope="request" id="form" name="form" action="/CashierAction.do?operation=102" enctype="multipart/form-data">        	
    	<input type="file" onchange="impData(this)" name="fileName" id="file" style="width:250px;height:24px; vertical-align:bottom" class="text">   		
  	</form>
</body>