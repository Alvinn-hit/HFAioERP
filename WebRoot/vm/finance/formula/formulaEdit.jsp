
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link type="text/css" href="/style/css/base_button.css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/jquery-ui.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/financereport.js"></script>

</head>
<style>	
	.f_bar{margin:10px 0 10px 10px;}
	.f_bar .f_val{width:50%;}
	.f_head thead{text-align:center;background:#5fa3e7;background-image:-webkit-linear-gradient(top,#5fa3e7,#428bca);background-image:linear-gradient(top,#5fa3e7,#428bca);}
	.f_head{border-collapse:collapse;border-spacing:0;border-left:1px solid #c2c2c2;border-bottom:1px solid #c2c2c2;}
	.f_head td{border-right:1px solid #c2c2c2;border-top:1px solid #c2c2c2;border-spacing:0;}			
	.f_head tbody td{background:#d7ecff;}
	
	.f_divH,.f_divB{table-layout:fixed;}
	.f_head,.f_sheet{line-height:1.5em;}
	.f_cell{height:100%;width:100%;border:none;}
	.f_selected{border-bottom:1px solid #000;}
	.f_bar span{margin:0 10px 0 0;}
	.f_bar .f_cur{width:50px;border:1px solid #c2c2c2;}
	.f_addRow{margin:0 10px;}
	
	.f_funBox{position:absolute;left:30%;top:20%;display:none;z-index:99;background:#fff;}
	
	.f_funBox .f_fun_tab{margin:10px 10px;}
	.f_funBox .f_funTitle{text-align:center;background:#5fa3e7;background-image:-webkit-linear-gradient(top,#5fa3e7,#428bca);background-image:linear-gradient(top,#5fa3e7,#428bca);}
	.f_funBox .f_fun_row{margin:10px 10px;}
	.f_funBox {border-radius:3px;background:#eee;border:1px #bbb solid;width:450px;}
	.f_funBox .f_fun_tab{display:table;width:100%;}
	.f_funBox .f_fun_tab .f_fun_row{display:table-row;line-height:2.5em;}
	.f_funBox .f_fun_tab .f_fun_row .f_fun_cell{display:table-cell;}
	.f_funBox .f_fun_tab .f_fun_row .f_fun_cell span{margin:10px 0;}
	.f_save{float:right;}
	.f_btn_cancel{margin-left:10px;}
	.f_btn_sure,.f_btn_cancel{margin:10px 10px;}
	.f_flushCell:hover,.f_saveToCell:hover,.f_funEdit:hover{cursor:pointer;}
	.f_searchIcon {cursor:pointer;background-position:-32px 0;display:inline-block;width:16px;height:16px;background-image:url(/style/images/client/icon16.png);background-repeat:no-repeat;}
</style>

<script type="text/javascript">
	$(function(){
		$(".f_head").width($(".f_sheet").width()+2);
		$(".f_funBox").draggable();
		/*******按钮事件绑定********/
		$(".f_save").bind("click",function(){
			commit();
		});
		$(".f_btn_sure").bind("click",function(){
			var formula = '';
			if($(".f_accStart").val() != ""){
				formula = $(".f_accStart").val();
				if($(".f_accOver").val() != ""){
					formula += ":"+$(".f_accOver").val();
				}
				formula = '"'+formula+'"';
			} else{
				formula = '""';
			}
			
			formula += ','+'"'+$(".f_dataType option:selected").val()+'"';
			formula += ',"'+$(".f_money").val()+'"';
			formula += ','+$(".f_accYear").val();
			formula += ','+$(".f_accBeg").val();
			formula += ','+$(".f_accEnd").val();
			formula += ',""';
			formula = "ACCT("+formula+")";
			var val = $(".f_val").val().trim();
			if(val == ""){
				$(".f_val").val("="+formula);
			} else{
				$(".f_val").val(val+formula);	
			}
						
			
			$(".f_funBox").hide();
		});
		$(".f_btn_cancel").bind("click",function(){
			$(".f_funBox").hide();
		});
		$(".f_flushCell").bind("click",function(){
			$(".f_val").val("");
			$(".f_selected").val("");
		});
		
		$(".f_saveToCell").bind("click",function(){
			var fun = $(".f_val").val().trim();
			if(fun.indexOf('=')==0){
				$(".f_selected").val(fun).attr("fun",1);
			} else{
				$(".f_selected").val(fun);
			}			
		});
		
		$(".f_funEdit").bind("click",function(){
			$(".f_funBox").show();
		});
		/*******cell事件绑定*******/
		$(document.body).on("click",".f_cell",function(){
			$(".f_val").val($(this).val());
			var tr = $(this).parents("tr");
			$(".f_cell").removeClass("f_selected");
			$(this).addClass("f_selected");
			$(".f_cur").val($(this).attr("cell"));
			if(tr.next("tr").length == 0){
				var clone = tr.clone();
				var rows = $(".f_row").length;
				clone.find(".f_serial").text($(".f_row").length+1);
				clone.find(".f_cell").each(function(){
					var cell = $(this).attr("cell");
					if(cell != undefined){
						$(this).attr("cell",cell.substr(0,1)+(rows+1));	
					}
					
				});
				tr.after(clone);
				if($(".f_head").width() != $(".f_sheet").width()+2){
					$(".f_head").width($(".f_sheet").width()+2);
				}
				
			}
		})
		.on("keyup",".f_cell",function(event){
			var v = $(this).val();
			if(v.indexOf("=") == 0){
				$(".f_selected").attr("fun",1);
			} else{
				$(".f_selected").attr("fun",0);
			}
			if($(".f_selected").length > 0){
				$(".f_val").val($(this).val());
			}
		})
		.on("keyup",".f_val",function(event){			
			var v = $(this).val();
			if(v.indexOf("=") ==0){
				$(".f_selected").attr("fun",1);
				//*******进入公式编辑模式*******//
				
				//***********end**********//
			} else{
				$(".f_selected").attr("fun",0);
			}			
			if($(".f_selected").length > 0){
				$(".f_selected").val($(this).val());
			}
		})
		.on("click",".f_delRow",function(){			
			var obj = $(this).parents(".f_row").nextAll(".f_row");			
			$(this).parents(".f_row").remove();
			flushCell();			
		})
		.on("click",".f_addRow",function(){					
			var clone = $(".f_row:last").clone();			
			$(this).parents(".f_row").after(clone);			
			flushCell();
		});
				
		/***********end*********/
	});
	//**********重置表格cell**********//
	function flushCell(){		
		$(".f_row").each(function(index,e){
			$(this).find(".f_serial").text(index+1);
			$(this).find(".f_cell").each(function(){
				var cell = $(this).attr("cell");
				$(this).attr("cell",cell.substr(0,1)+(index+1));
			});
		});
		$(".f_cur").val($(".f_selected").attr("cell"));
	}
	//*********生成总账公式*********//
	function commit(){
		var list = [];
		var index = 0;
		//******整理数据******//
		$(".f_cell").each(function(){
			var item = {};
			item.name = $(this).attr("cell");
			item.value = $(this).val();
			item.fun = $(this).attr("fun");
			list[index] = item;
			index++;			
		});
		//*******end*******//
		var callBack = {success:function(data){
				
				if(data.code=="OK"){
					alert("保存成功");
				} else{
					alert("保存失败");
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				alert(textStatus);
			}
		};
		var data = {};
		data.cells =JSON.stringify(list);
		data.rows = $(".f_row").length-1;
		data.cols = 8;
		data.operation = "save"
		data.id  = "$!id"
		var url = "/FormulaAction.do";
		
		//******保存公式及表格布局*****//
		commitData(url,data,callBack);
		//**********end**********//
	}
	
	/**
	 *  ajax调用 
	 * @param url
	 * @param obj
	 * @param callbackfun
	 * @param type
	 * @param dataType
	 */
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
				prompt("操作异常");				
			};
		}
		jQuery.ajax({
			url : url,
			dataType : dataType,
			type : type,
			data : obj,
			traditional:true,
			success : callbackfun.success,
			error:callbackfun.error,
			complete:callbackfun.complete
		});
	} 
</script>
<body>

	<div class="f_bar">
		<span class=""><input type="text" readonly=true class="f_cur" /></span><span class="f_flushCell">✘</span><span class="f_saveToCell">✔</span><span class="f_funEdit">fun </span><span><input type="text" class="f_val" /></span>
		<span class="btn f_save">保存</span>
	</div>	
	<div class="f_container" style="overflow: hidden; height: 560px;width:100%;box-sizing:border-box;">
	<div class="f_box" style="width:100%;height:100%;overflow:hidden;position:relative;">		
		<div class="f_divH" style="margin-left:10px;z-index: 55; width: 100%; overflow: hidden; position: absolute; top: 0px; left: 0px;">
				<table class="f_head"  cellpadding="0" cellspacing="0" border="1" borderColor="#808080" style="overflow:auto;border-collapse:collapse;table-layout:fixed;width:100%;">
					<thead>
						<td width=20>-</td>
						<td>资产</td>
						<td>行次</td>
						<td>年初数</td>
						<td>期末数</td>
						<td>负债和股东权益</td>
						<td>行次</td>
						<td>年初数</td>
						<td>期末数</td>					
						<td>-</td>
					</thead>
				</table>
		</div>
				<div class="f_divB" class="rt_table3" style="margin-left:10px;z-index: 40; width: 100%; overflow: auto; position: absolute; top: 0px; left: 0px; height: 303px;">
					<table class="f_sheet" cellpadding="0" cellspacing="0" border="1" borderColor="#808080" style="overflow:auto;border-collapse:collapse;table-layout:fixed;width:100%;">
					<thead>
						<td width=20>-</td>
						<td>资产</td>
						<td>行次</td>
						<td>年初数</td>
						<td>期末数</td>
						<td>负债和股东权益</td>
						<td>行次</td>
						<td>年初数</td>
						<td>期末数</td>					
						<td>-</td>
					</thead>
						<tbody>
							#set($r = 1)
							#foreach($r in [1..$rows])				
								<tr class="f_row">
								<td><span class="f_serial">$r</span></td>
								<td><input type="text" cell="A$r" class="f_cell f_label" value='$!cells.get("A$r").value' /></td>						
								<td><input type="text" cell="B$r" class="f_cell f_serial" value='$!cells.get("B$r").value' /></td>
								<td><input type="text" cell="C$r" class="f_cell f_beg" value='$!cells.get("C$r").value' /></td>
								<td><input type="text" cell="D$r" class="f_cell f_end" value='$!cells.get("D$r").value' /></td>
								
								<td><input type="text" cell="E$r" class="f_cell E$r f_label" value='$!cells.get("E$r").value' /></td>
								<td><input type="text" cell="F$r" class="f_cell F$r f_serial" value='$!cells.get("F$r").value' /></td>
								<td><input type="text" cell="G$r" class="f_cell G$r f_beg" value='$!cells.get("G$r").value' /></td>
								<td><input type="text" cell="H$r" class="f_cell f_end" value='$!cells.get("H$r").value' /></td>
								<td><span class="f_addRow">＋</span><span class="f_delRow">×</span></td>
								</tr>
								#set($r = $r+1)
							#end
								#set($rows = $rows + 1)
								<tr class="f_row">
								<td><span class="f_serial">$rows</span></td>
								<td><input type="text" cell="A$rows" class="f_cell f_label" value='' /></td>
								<td><input type="text" cell="B$rows" class="f_cell f_serial" value='' /></td>
								<td><input type="text" cell="C$rows" class="f_cell f_beg" value='' /></td>
								<td><input type="text" cell="D$rows" class="f_cell f_end" value='' /></td>
								
								<td><input type="text" cell="E$rows" class="f_cell f_label" value='' /></td>
								<td><input type="text" cell="F$rows" class="f_cell f_serial" value='' /></td>
								<td><input type="text" cell="G$rows" class="f_cell f_beg" value='' /></td>
								<td><input type="text" cell="H$rows" class="f_cell f_end" value='' /></td>
								<td><span class="f_addRow">＋</span><span class="f_delRow">×</span></td>
								</tr>
						</tbody>
					</table>
				</div>
		</div>
	</div>
	<div class="f_funBox">
		<div class="f_funTitle">
			总账科目取数
		</div>
		<div class="f_fun_tab">
			<div class="f_fun_row">
				<div class="f_fun_cell"><span>科目：</span></div><div class="f_fun_cell"><span><input type="text" class="f_accStart" name ="f_accStart" id="f_accStart" /></span><div class="f_searchIcon" onclick="selectCode('f_accStart')"></div></div>
			</div>			
			<div class="f_fun_row">
				<div class="f_fun_cell"><span>至：</span></div><div class="f_fun_cell"><span><input type="text" class="f_accOver" name ="f_accOver" id="f_accOver" /></span><div class="f_searchIcon" onclick="selectCode('f_accOver')"></div></div>
			</div>
			<div class="f_fun_row">
				<div class="f_fun_cell"><span>取数类型：</span></div>
				<div class="f_fun_cell">
				<span>
					<select class="f_dataType">
						<option value="C">C-期初余额</option>
						<option value="JC">JC-借方期初余额</option>
						<option value="DC">DC-贷方期初余额</option>
						<option value="AC">AC-期初绝对余额</option>
						<option value="Y">Y-期末余额</option>
						<option value="JY">JY-借方期末余额</option>
						<option value="DY">DY-贷方期末余额</option>
						<option value="AY">AY-期末绝对余额</option>
						<option value="JF">JF-借方发生额</option>
						<option value="DF">JF-贷方发生额</option>
						<option value="JL">JL-借方本年累计发生额</option>
						<option value="DL">DL-贷方本年累计发生额</option>
						<option value="SY">SY-利率表本期实际发生额</option>
						<option value="SL">SL-利率表本年实际发生额</option>
						<option value="BG">BG-取科目本年最高预算余额</option>
						<option value="BD">BD-取科目本年最低预算余额</option>
						<option value="BJG">BJG-本期最高预算借方发生额</option>
						<option value="BDG">BDG-本期最高预算贷方发生额</option>
					</select>
				</span>
				</div>
			</div>			
			<div class="f_fun_row">
				<div class="f_fun_cell"><span>货币：</span></div><div class="f_fun_cell"><span><input type="text" class="f_money" /></span></div>
			</div>				
			<div class="f_fun_row">
				<div class="f_fun_cell"><span>年度：</span></div><div class="f_fun_cell"><span><input type="text" class="f_accYear" /></span></div>
			</div>
			<div class="f_fun_row">
				<div class="f_fun_cell"><span>起始期间：</span></div><div class="f_fun_cell"><span><input type="text" class="f_accBeg" /></span></div>
			</div>
			<div class="f_fun_row">
				<div class="f_fun_cell"><span>结束期间：</span></div><div class="f_fun_cell"><span><input type="text" class="f_accEnd" /></span></div>
			</div>
		</div>
		<div class="f_button"><span class="f_btn_sure btn">确定</span><span class="f_btn_cancel btn">取消</span></div>		
	</div>
</body>
</html>