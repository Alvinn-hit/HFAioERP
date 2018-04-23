<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link rel="stylesheet" href="/$globals.getStylePath()/css/ListRange.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="/style1/css/workflow2.css" />
<link rel="stylesheet" type="text/css" href="/style/css/client.css"/>
<link rel="stylesheet" type="text/css" href="/style/css/clientModule.css" />
<link rel="stylesheet" type="text/css" href="/js/skins/ZCMS/asyncbox.css"/>
<script language="javascript" src="/js/jquery.js"></script>
<script language="javascript" src="/js/public_utils.js"></script>
<script language="javascript" src="/js/AsyncBox.v1.4.5.js" ></script>
<script type="text/javascript" src="/js/function.js"></script>  
<style type="text/css">


</style>
<script language="javascript">
	$(document).ready(function(){
		if($("#showRsDiv").val() == "true"){
			$("#showRs").show();
		}
		
		if($("#downUrl").val() !=""){
			setTimeout(downLoad($("#downUrl").val()),100);
		}
	
		if($("#isExistModule").val() == "true"){
			alert("此视图不存在,请检查视图的表名或重新下载视图");
		}
	})
	
	
function downLoad(url){
	window.location.href = url;
}

if(typeof(top.junblockUI)!="undefined"){	
	top.junblockUI();
}



function Refresh() {
	var ready = true ;
    path="/UtilServlet?operation=importInfo&importName=$!importName"+"&time="+(new Date()).getTime();;
	if(top.cancelImport){
		path="/UtilServlet?operation=cancelImport&importName=$!importName"+"&time="+(new Date()).getTime();;
		
	}
	var xmlHttp;
	if (window.ActiveXObject) {
		xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
	} 
	else if (window.XMLHttpRequest) {
		xmlHttp = new XMLHttpRequest();
	}
	xmlHttp.onreadystatechange = function(){
		if(xmlHttp.readyState == 4) {
			if(xmlHttp.status == 200) {
				response = xmlHttp.responseText;
				if(response == "nullObject"){
					ready = false ;
				}else if(response != "" && response != "ok"){
					var is = response.split("|");
					document.getElementById("imTotal").innerHTML=is[0];
					document.getElementById("imSuccess").innerHTML=is[1];
					document.getElementById("imError").innerHTML=is[2];					
					pspan = document.getElementById("progressSpan");
					pross = (Number(is[1])+Number(is[2])) /Number(is[0]);
					pross = pross*100;
					pross = Math.round(pross);
					
					//alert("to:"+is[0]+"su:"+is[1]+"er:"+is[2]+"pr"+pross);
					
					pspan.style.width=pross+"%";
					if(pross>0){
						pspan.innerHTML=pross+"%";						
					}
					
				}else if(response == "ok"){					
					document.getElementById("imCancel").innerHTML="$text.get('suspend.import.process')";
					top.cancelImport = true;
				}
			}else{
				response = "no response text" ;
			}
		}
	};
	xmlHttp.open("get", path, false);
	xmlHttp.send();
	var hit ;
	if(ready){
		hit =document.getElementById('ask_load').outerHTML;
	}else{
		hit = document.getElementById('ask_load2').outerHTML;
	}
	
	top.jblockUI({ message: hit, css: { left:'35%', top:'30%', width: '460px',height:'auto' } });
}


function getExcel()
{
 var path=document.getElementById("fileName").value;
   if(path.length>0)
   {
   
        var pos=path.lastIndexOf(".");
		var lastName=path.substring(pos,path.length);
		if(lastName.toLowerCase()!=".xls")
		{
			alert('$text.get("excel.format.error")');
			return false;
		}else
		{
			var starIndex = path.lastIndexOf("\\");
			var endIndex = path.lastIndexOf(".");
			var fileName = path.substr(starIndex+1,(endIndex-starIndex-1));
			
			document.form.submit();
			top.cancelImport= false;
			setInterval("Refresh()",1000);		
			
		}
	}else
   {
     alert('$text.get("excel.up.select")');
	 return false;
   }
}

function downloadExample(fileName){
	window.location.href="/ReadFile?tempFile=example&fileName="+fileName;
}
function download(keyId){
 	window.location.href="/UtilServlet?operation=importTemplete&tableName="+keyId+"&winCurIndex=3";
}

function downView(viewId){
	var url = "/CRMClientAction.do?operation=92&ModuleId=$moduleBean.id&isTemplet=true&viewId=" + viewId;
	jQuery.ajax({
	   type: "POST",
	   url: url,
	   success: function(msg){
	     window.location.href=msg;
	   }
	});
}

function keyDown(e) { 
	var iekey=event.keyCode; 
	if(iekey==13){
		alert("请选择要导入的文件!");
		return false;
	}
}
document.onkeydown = keyDown; 
</script>
</head>
<body>
<form  method="post" scope="request" id="form" name="form" action="/importDataAction.do" enctype="multipart/form-data" >
<input type="hidden" name="operation" value="$globals.getOP("OP_IMPORT")"/>
<input type="hidden" name="winCurIndex" value="$winCurIndex"/>
<input type="hidden" name="NoBack" value="$!NoBack"/>
<input type="hidden" name="isTemplet" id="isTemplet" value="true" />
<input type="hidden" name="isCrmImport" id="isCrmImport" value="true" />
<input type="hidden" name="importName" id="importName" value='$globals.get($moduleBean.tableInfo.split(":"),0)' />
<input type="hidden" name="downUrl" id="downUrl" value="$!downUrl" />
<input type="hidden" name="showRsDiv" id="showRsDiv" value="$!showRsDiv" />
<input type="hidden" name="moduleId" id="moduleId" value="$moduleBean.id" />
<input type="hidden" name="isExistModule" id="isExistModule" value="$!isExistModule" />
		<table cellpadding="0" cellspacing="0" class="framework">
			<tr>
				<td>
					<div class="TopTitle">
						<span>当前位置:客户导入 </span>
					</div>
					<div id="cc" class="data" style="width:100%; overflow:hidden;overflow-y:auto;width:100%;">
						<script type="text/javascript">
							var oDiv=document.getElementById("cc");
							var sHeight=document.documentElement.clientHeight-55;
							oDiv.style.height=sHeight+"px";
						</script>
						<div class="boxbg2 subbox_w700">
							<div class="subbox cf">
								<div class="inputbox">
									<div>
										<ul>
											<span style="line-height: 40px;"><span class="num_1"></span><font>获取execl导入视图，并填写内容</font></span>
											<div style="width: 100%;">
												<div style="float: left;margin-left: 40px;margin-right:5px; padding-top:2px; vertical-align: middle;">选择下载视图: </div>
												#foreach ($view in $viewList)
													<div class="lp"  id="$view.id"  style="float: left;"  onclick="downView(this.id)"><span class="a"></span>$view.viewName<span class="c"></span></div>
							        		 	#end
											</div>
											<li style="list-style-type: none;"><font color="red">*</font>注：导出视图后请勿改动视图中表名称，避免导入错误</li>
										</ul>
									</div>
									<div>
										<ul>
											<span style="line-height: 40px;"><span class="num_2"></span><font>另外保存为xls格式文件</font></span>
											<li>客户的内容输入和整理完成后，在excel中，选中菜单"文件" - "另存为..."</li>
											<li>在另存为的窗口，底部"保存类型"中选择 "xls(逗号分隔)"</li>
											<li>忽略兼容性提示，点击是，保存</li>
										</ul>
									</div>
									<div>
										<ul>
											<span style="line-height: 40px;"><span class="num_3"></span><font>上传xls格式文件</font></span>
											<li>将上一步生成的xls文件上传到服务器</li>
											<li style="list-style: none;">
												<input type="file" name="fileName"  id="fileName" style="width:250px;height:24px; vertical-align:bottom" class="text" />
												<button type="button" onclick="return getExcel();" class="bu_02">$text.get("excel.lb.upload")</button>&nbsp;
											</li>
											
										</ul>
									</div>
									<div>
										<ul>
											<span style="line-height: 40px;"><span class="num_4"></span><font>检查导入结果</font></span>
										</ul>
										<ul id="showRs" style="display: none;">		
											<li>
												<span>$text.get("aio.import.total"):<font style="color: red;">$!totalimport</font></span>					
											</li>
											<li>
												<span>$text.get("import.succees.number"):<font style="color: red;">$!successimport</font></span>					
											</li>
											<li>
												<span>$text.get("import.failure.number"):<font style="color: red;">$!errorimport</font></span>				
											</li>
											#if("$!errorimport" != "0")
											<li style="width: 200px;margin-left: 75px;">
												<a href="/UtilServlet?operation=readErrorExcel&fileName=$!fileName"><font style="color: red;">$text.get("download.error.report")</font></a>				
											</li>
											#end
										</ul>
									</div>
									<br />
								</div>
							</div>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
