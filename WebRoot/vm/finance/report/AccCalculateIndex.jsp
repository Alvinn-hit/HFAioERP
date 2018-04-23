<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>核算项目分类总账</title>
<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css"  />
<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" href="/js/ztree/zTreeStyle/zTreeStyle.css" type="text/css"/>
<link type="text/css" rel="stylesheet" href="/style/css/jquery.contextmenu.css"/>
<link rel="stylesheet" href="/style1/css/financereport.css" type="text/css" />
<link type="text/css" rel="stylesheet" href="/style1/css/CRselectBoxList.css"/>
<link type="text/css" rel="stylesheet" href="/$globals.getStylePath()/css/listgrid.css" />
<script type="text/javascript" src="/js/k_listgrid.js"></script>
<script language="javascript" src="/js/function.js"></script>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript"  src="/js/aioselect.js"></script>
<script type="text/javascript" src="/js/ztree/jquery.ztree-2.6.js"></script>
<script type="text/javascript" src="/js/financereport.js"></script>
<script language="javascript" src="/js/datePicker/wdatePicker.js"></script>
<script type="text/javascript" src="/js/crm/jquery.contextmenu.js"></script>
<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>
<script type="text/javascript" src="/js/ztree/demoTools.js"></script>

<style type="text/css">
	.search_popup{height: 75%}
	.title1{width: auto;height: 50px;border-bottom: #d7dcdc 2px solid;}
	.tdleft{width:20%;height:98%;border-right: #d7dcdc 2px solid;border-bottom: #d7dcdc 2px solid;}
</style>

<script type="text/javascript">
var zTree1;
var setting;
var zNodes;
#if("$!folderTree"=="")
	zNodes = [];
#else
	zNodes=$!folderTree;
#end
setting = {
	checkable : true,
	expandSpeed : false,
	checkType : {"Y":"s", "N":"ps"}, 
	showLine: true,
	callback: {
		dblclick: zTreeOnDBlClick,
		click: zTreeOnClick
	}
};

var exportUrl = "";
$(document).ready(function(){
	//*****键盘上下键监听事件*****//
	$(document).keydown(function(event){	
		var curNode=zTree1.getSelectedNode();	
		if(event.keyCode == 38){//上一条
			if(curNode == null){
				return;
			}			
			
			var target = "";
			
			//***获取下一节点classCode*******//
			for(var i = 0 ;i<zNodes.length;i++){
				if(zNodes[i].classCode == curNode.classCode && i >0){
					target = zNodes[i-1].classCode;
					break;
				}
			}
			if(target != ""){				
				var targetNode = zTree1.getNodeByParam("classCode",target);  
				zTree1.cancelSelectedNode();
				zTree1.selectNode(targetNode);
					if(targetNode.open){
						zTree1.expandNode(targetNode, false, false);
					}else{
						zTree1.expandNode(targetNode, true, false);
					}
					var flag = false;
					
					zTreeOnDBlClick(event,"",targetNode);				
			}
			 
		} else if(event.keyCode == 40){ //下一条
			var target="" ;
			var curNode=zTree1.getSelectedNode();
			if(curNode  == null){
				//****选中第一个节点
				target = zNodes[0].classCode;
				//targetNode = zTree1.getNodeByParam("classCode",zNodes[0].classCode);				
			}else{
				//***获取下一节点classCode*******//
				for(var i = 0 ;i<zNodes.length;i++){
					if(zNodes[i].classCode == curNode.classCode && i < zNodes.length-1){
						target = zNodes[i+1].classCode;
						break;
					}
				}
			}
			if(target != undefined && target != ''){				
				var targetNode = zTree1.getNodeByParam("classCode",target);  
				zTree1.cancelSelectedNode();
				zTree1.selectNode(targetNode);
					if(targetNode.open){
						zTree1.expandNode(targetNode, false, false);
					}else{
						zTree1.expandNode(targetNode, true, false);
					}
					var flag = false;
					
					zTreeOnDBlClick(event,"",targetNode);				
			}
			
		}	
	});
	//**********end*********//
	reloadTree();
	#if("$!comeMode"=="")
		showSearchDiv();
		$("#periodYearStart").val("$!accPeriod.accYear");
		$("#periodYearEnd").val("$!accPeriod.accYear");
		$("#periodStart").val("$!accPeriod.accPeriod");
		$("#periodEnd").val("$!accPeriod.accPeriod");
		$("#accTypeNo").attr("checked","checked");
	#end
	
	var postdX;
	document.getElementById("center_td").onmousedown=function(e){
		if(!e) e = window.event;
	    posX = e.clientX - parseInt(8);
	    document.onmousemove = mousemove;           
	}

	document.onmouseup = function(){
	    document.onmousemove = null;
	}
	function mousemove(ev){
	    if(ev==null) ev = window.event;
	    if(event.clientX >200){
		    $("#left_td").css("width",(event.clientX-posX) + "px");
			$("#trees").css("width",(event.clientX-8) + "px");
			$("#right_td").css("width",document.body.clientWidth-event.clientX-25);	
		}
	}
});

function reloadTree(node) {
	var setting1 = clone(setting);
	setting1.treeNodeKey = "classCode";
	setting1.treeNodeParentKey = "pClassCode";
	setting1.isSimpleData = true;
	zNodes1 = clone(zNodes);
	setting1.showLine = true;
	zTree1 = jQuery("#treeDemo").zTree(setting1, zNodes1);
	var nodes = zTree1.getNodes();
	var url = "/FinanceReportAction.do?optype=ReporttblAccCalculate&type=detail";
	var oldCode = $("#oldCode").val();
	if(oldCode!="" && nodes!=""){
		url += "&classCode="+oldCode+"&$!conditions";
	}
	$("#firameMain").attr("src",url);
}

/* 单击事件 */
function zTreeOnClick(event, treeId, treeNode){
	if(treeNode!=null){
		var srcNode = zTree1.getSelectedNode();
		if (srcNode) {
			if(treeNode.open){
				zTree1.expandNode(srcNode, false, false);
			}else{
				zTree1.expandNode(srcNode, true, false);
			}
			var flag = false;
			if(treeNode.classCode != undefined && treeNode.classCode != ''){
				zTreeOnDBlClick(event,treeId,treeNode);
			}
		}
	}
}

/* 树双击事件 */
function zTreeOnDBlClick(event, treeId, treeNode) {
	if(treeNode!=null){
		$("#oldCode").val(treeNode.classCode);
		var url = "/FinanceReportAction.do?optype=ReporttblAccCalculate&type=detail&classCode="+treeNode.classCode+"&$!conditions";
		$("#firameMain").attr("src",url);
		frames["firameMain"].blockui();
	}
}

//查询
var beginYear = "$!globals.get($!beginPeriod,0)";
var beginPeriod = "$!globals.get($!beginPeriod,1)";
function onsubmits(type){
	var keyword = jQuery("#keyWord").val();
	if(type == "search"){
		jQuery("#keyWord").val('');
	}
	if(keyword == "关键字搜索"){
		jQuery("#keyWord").val('');
	}
	if(!isvalidate()){
		return false;
	}
	var levelValue = jQuery("#accCodeLevel").val();
	if(levelValue != ""){
		if(!(gtZero(levelValue) && isInt(levelValue))){
			alert("级别必须为正整数");
			jQuery("#accCodeLevel").focus();
			return false;
		}
	}
	blockUI();
	form.submit();
}


var itemObj = "";
var itemSorts = "";
/* 搜索的弹出框 */
function selectPops(itemName){
	var itemSort = $("#itemSort").val();
	itemSorts = itemSort;
	var name = "请选择";
	var urlstr = "/UserFunctionAction.do?operation=22&popupWin=Popdiv&tableName=&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName=&selectName=";
	if(itemSort == "DepartmentCode"){
		//部门弹出框



		urlstr += "SelectAccDepartment";
		name += "部门";
	}else if(itemSort == "EmployeeID"){
		//职员弹出框



		urlstr += "SelectAccEmployee";
		name += "职员";
	}else if(itemSort == "StockCode"){
		urlstr += "SelectAccStocks";
		name += "仓库";
	}else if(itemSort == "ClientCode"){
		urlstr += "SelectAccClient";
		name += "客户";
	}else if(itemSort == "SuplierCode"){
		urlstr += "SelectAccProvider";
		name += "供应商";
	}else if(itemSort == "ProjectCode"){
		urlstr += "SelectAccProject";
		name += "项目";
	}	
	itemObj = itemName;
	asyncbox.open({id:'Popdiv',title:name,url:urlstr,width:750,height:470})
}

/* 返回数据 */
function exePopdiv(returnValue){
	if(typeof(returnValue)=="undefined") return ;
	var note = returnValue.split("#;#") ;
	if(itemSorts == "EmployeeID"){
		$("#"+itemObj).val(note[3]);
	}else{
		$("#"+itemObj).val(note[1]);
	}
}

/* 改变项目类别时清空项目代码 */
function changecode(){
	$("#itemCodeStart").val('');
	$("#itemCodeEnd").val('');
}

/* 关键字查询 */
function insertkeyword(){
	if("$!comeMode"==""){
		alert("请先进行条件查询!");
		return false;
	}
	onsubmits('');
}

/* 清空 */
function cleaDate(){
	$("#highSearch").find("input[type=checkbox]").each(function (){
		$(this).removeAttr("checked");
	});
	$("#periodYearStart").val("$!accPeriod.accYear");
	$("#periodYearEnd").val("$!accPeriod.accYear");
	$("#periodStart").val("$!accPeriod.accPeriod");
	$("#periodEnd").val("$!accPeriod.accPeriod");
	$("#itemSort").val("DepartmentCode");
	$("#itemCodeStart").val('');
	$("#itemCodeEnd").val('');
	$("#accCodeStart").val('');
	$("#accCodeEnd").val('');
	$("#accCodeLevel").val('');
	$("#accTypeNo").attr("checked","checked");
}

/* 导出列表 */
function onexport(){
	window.frames["firameMain"].exports();
}

/* 打印 */
function onprint(){
	 //document.getElementById('firameMain').contentWindow.prints();
	 $("#firameMain")[0].contentWindow.prints(); 
	//document.getElementById('firameMain').prints();
	//window.frames["firameMain"].prints();
}

</script>
</head>
<iframe name="formFrame" style="display:none"></iframe>
<body>
<form method="post" scope="request" id="form" name="form" action="/FinanceReportAction.do?optype=ReporttblAccCalculate" target="">
<input type="hidden" name="comeMode" id="comeMode" value="false" />
<input type="hidden" name="oldCode" id="oldCode" value="$!conMap.oldCode"/>
<input type="hidden" id="org.apache.struts.taglib.html.TOKEN" name="org.apache.struts.taglib.html.TOKEN" value=""/>
<div class="title1">
	<div class="titlediv">
		<img src="/style1/images/workflow/ti_003.gif"/><span style="font-size: 14px;">核算项目分类总账</span>
	</div>
	<div style="float: left;padding-bottom: 2px;margin: 20px 10px 10px 100px;">
	#if("$!comeMode"!="")
		项目类别：



		#if("$!conMap.itemSort"=="DepartmentCode")部门
		#elseif("$conMap.itemSort"=="EmployeeID")职员
		#elseif("$conMap.itemSort"=="StockCode")仓库
		#elseif("$conMap.itemSort"=="ClientCode")客户
		#elseif("$conMap.itemSort"=="SuplierCode")供应商



		#elseif("$conMap.itemSort"=="ProjectCode")项目
		#end
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;期间：$!conMap.periodYearStart年第$!conMap.periodStart期至#if("$!conMap.periodYearStart"!="$!conMap.periodYearEnd")$!conMap.periodYearEnd年#end第$!conMap.periodEnd期



		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		#if($!globals.getSysSetting('currency')=="true")
			#if("$!conMap.currencyName"=="")
				币别：综合本位币
			#else
				#foreach($currency in $!currencyList)
					#if("$!conMap.currencyName"=="$!globals.get($!currency,0)")
						币别：$!globals.get($!currency,1)
					#end
				#end
			#end
		#end
	#end
	</div>
	<div class="btndiv">
		<div class="op sbtn63" onclick="showSearchDiv()">
			<span class="a"></span>
			<a href="javascript:void(0);">条件查询</a>
			<span class="c"></span>
		</div>
		<div class="op sbtn41" onclick="onsubmits('')">
			<span class="a"></span>
			<a href="javascript:void(0);">刷新</a>
			<span class="c"></span>
		</div>
		#if($LoginBean.operationMap.get("/FinanceReportAction.do?optype=ReporttblAccCalculate").print() || $!LoginBean.id=="1")
		<div class="op sbtn41" onclick="onprint()">
			<span class="a"></span>
			<a>打印</a>
			<span class="c"></span>
		</div>
		#end
		<div class="op sbtn41" onclick="onexport()">
			<span class="a"></span>
			<a href="javascript:void(0);">导出</a>
			<span class="c"></span>
		</div>
		<div class="op sbtn41" onclick="closePage()">
			<span class="a"></span>
			<a href="javascript:void(0);" >关闭</a>
			<span class="c"></span>
		</div>
	</div>
</div>
	<div style="width:99%;" id="data_list_id">
		<table style="width:100%;height:100%">
			<tr>
				<td class="tdleft" valign="top" id="left_td">
					<div style="padding:5px 0 0 30px; width:99%"><input type="text" id="keyWord" name="keyWord" class="search_text" #if("$!conMap.keyWord" != "") value="$!conMap.keyWord" #else value="关键字搜索" #end 
					onKeyDown="if(event.keyCode==13) insertkeyword();" onblur="if(this.value=='')this.value='关键字搜索';" 
					onfocus="if(this.value=='关键字搜索'){this.value='';}" /><input type="button" class="search_button" onclick="insertkeyword();"/>
					</div>
					<div style="padding:0 0 0 20px;height: 100%;width:100%;">
						<p class="leftMenu_bgB" style="margin:10px 0 0 0;">
							<span style="padding-left: 20px;"><img src="/vm/oa/directorySetting/images/fiI.gif" />&nbsp;核算项目目录</span>
						</p>
						<div style="overflow:auto;float:left;width:210px;padding-top: 5px;" id="trees">
							<script type="text/javascript"> 
								var oDiv=document.getElementById("trees");
								var sHeight=document.documentElement.clientHeight-130;
								oDiv.style.height=sHeight+"px";
							</script>
							<div class="zTreeDemoBackground">
								<ul id="treeDemo" class="tree"></ul>
								#if("$!comeMode"=="")
									<div style="margin:50px 0px 0px 20px">点击条件查询按钮，进行数据查询</div>
								#elseif("$!folderTree"=="[]")
									<div style="margin:50px 0px 0px 0px">很抱歉，没有找到与您条件匹配的核算项目!</div>
								#end
							</div>	
						</div>
					</div>
				</td>
				<td valign="top" id="center_td" style="width:5px;height:100%;cursor:e-resize;" >
				</td>
				<td valign="top" id="right_td">
					<iframe id="firameMain" frameborder="false" src="" name="firameMain" style="margin-top: 10px;" scrolling="no" width="100%">
					</iframe>
				</td>
			</tr>
		</table>
		<script type="text/javascript"> 
			var oDiv=document.getElementById("data_list_id");
			var sHeight=document.documentElement.clientHeight-80;
			oDiv.style.height=sHeight+"px";
			document.getElementById("firameMain").style.height= sHeight+"px";
		</script>
	</div>
		
		<!-- 条件查询 -->
		<div id="highSearch" class="search_popup" style="display:none;height: 360px;top: 220px;">
		<script type="text/javascript">
			var sLeft=document.body.offsetWidth/2
			$("#highSearch").css("left",sLeft);
		</script>
		<input type="hidden" name="queryType" id="queryType" value=""/>
		<div id="Divtitle" style="cursor: move;width:98%;">
			<span class="ico_4"></span>&nbsp;条件查询
		</div>
			<table style="width:100%;padding-top: 5px;" id="tableSearch">
				<tr>
					<td>
					<div><table cellpadding="0" cellspacing="0" width="100%">
						<thead>
						<tr id="periodtr"><td class="tdDes3" colspan="3" width="50%">会计期间:&nbsp;<input name="periodYearStart" id="periodYearStart" style="width:40px;" value="$!conMap.periodYearStart" size="2"/>&nbsp;年



							<input name="periodStart" id="periodStart" value="$!conMap.periodStart" style="width:40px;" size="2"/>&nbsp;期</td>
						<td class="tdDes2" colspan="3" width="50%">至&nbsp;<input name="periodYearEnd" id="periodYearEnd" value="$!conMap.periodYearEnd" style="width:40px;" size="2"/>&nbsp;年



							<input name="periodEnd" id="periodEnd" value="$!conMap.periodEnd" style="width:40px;" size="2"/>&nbsp;期</td>
						</tr>
						</thead>
					</table></div>
					<div id="condition1" style="margin-top: 10px;"><table cellpadding="0" cellspacing="0" width="100%">
						<thead>
						<tr>
							<td class="tdDes1">项目类别:&nbsp;</td>
							<td colspan="3">
								<select name="itemSort" id="itemSort" style="width:110px;" onchange="changecode()">
									<option value="DepartmentCode" #if("$conMap.itemSort"=="DepartmentCode")selected#end>部门</option>
									<option value="EmployeeID" #if("$conMap.itemSort"=="EmployeeID")selected#end>职员</option>
									<option value="StockCode" #if("$conMap.itemSort"=="StockCode")selected#end>仓库</option>
									#set($isHide = $!globals.hideViewRight('tblAccTypeInfo','','Customer','/FinanceReportAction.do?optype=ReporttblAccCalculate'))
									#if($isHide)
									<option value="ClientCode" #if("$conMap.itemSort"=="ClientCode")selected#end>客户</option>
									#end
									#if($!globals.getSysSetting('Project')=="true")
									<option value="ProjectCode" #if("$conMap.itemSort"=="ProjectCode")selected#end>项目</option>
									#end
									#set($isHide = $!globals.hideViewRight('tblAccTypeInfo','','Supplier','/FinanceReportAction.do?optype=ReporttblAccCalculate'))
									#if($isHide)
									<option value="SuplierCode" #if("$conMap.itemSort"=="SuplierCode")selected#end>供应商</option>
									#end
								</select>
							</td>
						</tr>
						<tr><td class="tdDes1">项目代码:&nbsp;</td><td style="white-space: nowrap;" class="searchinput"><input name="itemCodeStart" id="itemCodeStart" value="$!conMap.itemCodeStart" size="13" ondblclick="selectPops('itemCodeStart')" />
							<img src='/style1/images/search.gif' onClick="selectPops('itemCodeStart')" class='search' />
							</td><td class="tdDes1">至&nbsp;</td><td style="white-space: nowrap;" class="searchinput">
							<input name="itemCodeEnd" id="itemCodeEnd" value="$!conMap.itemCodeEnd" size="13" ondblclick="selectPops('itemCodeEnd')"/>
							<img src='/style1/images/search.gif' onClick="selectPops('itemCodeEnd')" class='search'/></td>
						</tr>
						<tr><td class="tdDes1">会计科目:&nbsp;</td><td style="white-space: nowrap;" class="searchinput"><input name="accCodeStart" id="accCodeStart" value="$!conMap.accCodeStart" size="13" ondblclick="selectCode('accCodeStart')" />
							<img src='/style1/images/search.gif' onClick="selectCode('accCodeStart')" class='search' />
							</td><td class="tdDes1">至&nbsp;</td><td style="white-space: nowrap;" class="searchinput">
							<input name="accCodeEnd" id="accCodeEnd" value="$!conMap.accCodeEnd" size="13" ondblclick="selectCode('accCodeEnd')"/>
							<img src='/style1/images/search.gif' onClick="selectCode('accCodeEnd')" class='search'/></td>
						</tr>
						<tr>
							<td class="tdDes1">科目级次:&nbsp;</td>
							<td colspan="3"><input name="accCodeLevel" id="accCodeLevel" value="$!conMap.accCodeLevel" style="width:40px;"/></td>
						</tr>
						#if($!globals.getSysSetting('currency')=="true")
						<tr>
							<td class="tdDes1">币别:&nbsp;</td>
							<td>
								<select name="currencyName" id="currencyName" style="width:140px;">
									#foreach($currency in $!currencyList)
										<option value="$!globals.get($!currency,0)" #if("$!conMap.currencyName"=="$!globals.get($!currency,0)")selected#end>$!globals.get($!currency,1)</option>
									#end
									<option value="" #if("$!conMap.currencyName"=="")selected#end><综合本位币></option>
								</select>
							</td>
						</tr>
						#end
						</thead>
						<tbody>
						<tr>
							<td class="tdDes1"><input name="accTypeNo" id="accTypeNo" value="1" type="checkbox" #if("$conMap.accTypeNo"=="1")checked#end/></td><td><label for="accTypeNo">项目无发生额不显示</label></td>
							#if("$!AccMainSetting.isAuditing"=="1")
								<td class="tdDes1"><input name="binderNo" id="binderNo" value="1" type="checkbox" #if("$conMap.binderNo"=="1")checked#end/></td><td><label for="binderNo">包括未过账凭证</label></td>
							#end
						</tr>
						<tr>
							<!-- <td class="tdDes1"><input name="takeBrowNo" id="takeBrowNo" value="1" type="checkbox" #if("$conMap.takeBrowNo"=="1")checked#end/></td><td><label for="takeBrowNo">科目无发生额不显示</label></td> -->
							<td class="tdDes1"><input name="showBanAccTypeInfo" id="showBanAccTypeInfo" value="1" type="checkbox" #if("$conMap.showBanAccTypeInfo"=="1")checked#end/></td><td><label for="showBanAccTypeInfo">显示禁用科目</label></td>
						</tr>
						</tbody>
					</table></div>
					</td>
				</tr>
			</table>
			<span class="search_popup_bu"><input type="button" 
					onclick="onsubmits('search');" class="bu_1"  />
				<input type="button" class="bu_3" value="" onclick="cleaDate()"/>
				<input type="button" onClick="closeSearch();" class="bu_2" />
			</span>
			<script language="javascript">
			var posX;
			var posY;       
			fdiv = document.getElementById("highSearch");         
			document.getElementById("Divtitle").onmousedown=function(e){
				if(!e) e = window.event;  //IE
			    posX = e.clientX - parseInt(fdiv.style.left);
			    posY = e.clientY - parseInt(fdiv.style.top);
			    document.onmousemove = mousemove;           
			}
 
			document.onmouseup = function(){
			    document.onmousemove = null;
			}
			function mousemove(ev){
			    if(ev==null) ev = window.event;//IE
			    fdiv.style.left = (ev.clientX - posX) + "px";
			    fdiv.style.top = (ev.clientY - posY) + "px";
			}
			</script>
	</div>
</form>
</body>
</html>