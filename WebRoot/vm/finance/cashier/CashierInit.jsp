<!DOCTYPE html>
<head>
	<title>出纳账套</title>
</head>
<script type="javascript/text">
	function beforeSubmit(){
		 //获取完整的日期
 		var date=new Date;
 		var year=date.getFullYear(); 
 		var month=date.getMonth()+1;
		var re = /^[0-9]*[1-9][0-9]*$/; 
		var periodYear = $(".PeriodYear").val();
		var periodMonth = $(".PeriodMonth").val();
		if(!re.test(periodYear) || !re.test(periodMonth){
		    alert("请输入正确的期间!");
			return;
		}		
		if(periodYear<year){
			alert("当前期间年为："+year);
			return;
		}
		if(periodYear==year && periodMonth <month){
			alert("当前期间月为："+month);
			return;
		}
		document.form.submit();	
	}

</script>
<body>
	<div class="bar">
		<form method="post" action="/CashierAction.do">
			<input type="hidden" name="operation" value="1"/>			
			<fieldset>
				<legend>出纳系统</legend>
				<div>
					出纳系统启用期间：<input type="number" name="PeriodYear" class="PeriodYear" />
					年 第 <input type="number" name="PeriodMonth" class="PeriodMonth" /> 期
				</div>
			</fieldset>
			<div>
				<button onclick="beforeSubmit()">确定</button>
				<button onclick="cancel()">取消</button>		
			</div>
		</form>
	</div>
</body>
</html>