 
function getValueById(value){
 return document.getElementById(value) ;
}



var fieldNames;
var fieldNIds;

function deptPop(popname,fieldName,fieldNameIds,flag){
 var urls = "/Accredit.do?popname=" + popname + "&inputType=checkbox";
 var titles = "请选择部门";
 if(popname == "userGroup"){
 titles = "请选择个人"
 }
 fieldNames = fieldName;
 fieldNIds = fieldNameIds;
 asyncbox.open({
 id : 'Popdiv',
 title : titles,
 url : urls,
 width : 755,
 height : 435,
 top: 5,
 btnsbar : jQuery.btn.OKCANCEL,
 callback : function(action,opener){
　　　 if(action == 'ok'){
 var employees = opener.strData;
 newOpenSelect(employees,fieldName,fieldNameIds,flag)
　　　　　 }
　　　 }
　});
}


function newOpenSelect(employees,fieldName,fieldNameIds,flag){
 var employees = employees.split("|") ;
 for(var j=0;j<employees.length;j++){
 var field = employees[j].split(";") ;
 if(field!=""){
 var existOption = getValueById(fieldName).options;
 var length = existOption.length;
 var talg = false ;
 for(var i=0;i<length;i++){
 if(existOption[i].value==field[0]){
 talg = true;
 }
 }
 if(!talg){
 getValueById(fieldName).options.add(new Option(field[1],field[0]));
 getValueById(fieldNameIds).value+=field[0]+";";
 }
 }
 }
}


function deleteOpation(fileName,popedomId){
 var index = $("#"+fileName+" option:selected").size();
 if(index==0){
 alert("请选择要移除的项!");
 return false;
 }
 $("#"+fileName+" option:selected").remove();
 getShowContent(fileName,popedomId);
}


function getShowContent(showOption,param){
 var showContent="";
 $("#"+showOption+" option").each(function(){
 showContent+= this.value+";";
 });
 $("#"+param).attr("value",showContent);
}


var deptIds;
var titleIds;
function deptPopSingle(popname,fieldName,fieldNameIds,flag,deptId,titleId){
 var urls = "/Accredit.do?popname=" + popname + "&inputType=radio";
 var titles = "请选择部门";
 if(popname == "userGroup"){
 titles = "请选择个人";
 }else if(popname=="CrmClickGroup"){
 titles = "请选中客户";
 }
 titleIds = titleId;
 deptIds = deptId;
 fieldNames = fieldName;
 fieldNIds = fieldNameIds;
 asyncbox.open({
 id : 'Popdiv',
 title : titles,
 url : urls,
 width : 755,
 height : 435,
 top: 5,
 btnsbar :[{text:'清空',action:'remove'}].concat(jQuery.btn.OKCANCEL),
 callback : function(action,opener){
 　　　　　 if(action == 'ok'){
 var employees = opener.strData;
 newOpenSelectSearchSingle(employees,fieldName,fieldNameIds,deptId,titleId);
 　　　　　 }
 if(action == "remove"){ 
 removeData(fieldName,fieldNameIds);
 }
　　　 }
　 });
}


function removeData(fieldName,fieldNameIds){
 $("#"+fieldName).val("");
 $("#"+fieldNameIds).val("");
}

function newOpenSelectSearchSingle(str,fieldName,fieldNameIds,deptId,titleId){
 var employees = str.split("|") ;
 for(var j=0;j<employees.length;j++){
 var field = employees[j].split(";") ;
 if(field!=""){
 
 if(typeof(deptId)!="undefined" || typeof(titleId)!="undefined"){ 
 $("#"+fieldName).val(field[1]);
 $("#"+fieldNameIds).val(field[0])
 if(typeof(deptId)!="undefined" && deptId!="notEmp" && deptId.length>0){ 
 var hidDeptId=deptId;
 var showDeptId="popedomDeptIds_"+deptId;
 $("#"+hidDeptId).val(field[2]);
 $("#"+showDeptId).val(field[3]);
 }
 if(typeof(titleIds)!="undefined" && titleIds!="notTitle" && titleIds.length>0){ 
 var titleId=titleIds;
 $("#"+titleId).val(field[4]);
 }
 }else{ 
 
 var existOption = getValueById(fieldName).options;
 var length = existOption.length;
 var talg = false ;
 for(var i=0;i<length;i++){
 if(existOption[i].value==field[0]){
 talg = true;
 }
 }
 if(!talg){
 getValueById(fieldName).options.add(new Option(field[1],field[0]));
 $("#"+fieldNameIds).val($("#"+fieldNameIds).val()+field[0]+";")
 }
 }
 }
 }
}


function fillData(datas){
 newOpenSelectSearchSingle(datas,fieldNames,fieldNIds,deptIds,titleIds);
 jQuery.close('Popdiv');
}



var globalfieldName;
var globalhidfieldName;
var globalType;
function openPopup(tableName,selectName,fieldName,hidfieldName,type){
 var displayName="" ;
 var urlstr = "/UserFunctionAction.do?operation=22&tableName="+tableName+"&selectName="+selectName+"&popupWin=Popdiv&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName ;
 asyncbox.open({
 id:'Popdiv',
 title:displayName,
 url:urlstr,
 width:750,
 height:470
 });
 globalfieldName=fieldName;
 globalhidfieldName=hidfieldName;
 globalType=type;
}
function exePopdiv(returnValue){
 var strList = returnValue.split("|") ;
 for(var j=0;j<strList.length;j++){
 var field = strList[j].split(";") ;
 if(field!=""){
 if(globalType=="false"){ 

 $("#"+globalhidfieldName).val(field[0]);
 $("#"+globalfieldName).val(field[1]);
 }else{ 

 getValueById(globalfieldName).options.add(new Option(field[1],field[0]));
 getValueById(globalhidfieldName).value+=field[0]+";";
 }
 }
 }
}

function m(str){return document.getElementById(str)}

function deleteupload(fileName,tempFile,tableName,type){
 if(!confirm('确定删除吗')){
 return;
 }
 if("true" == tempFile){
 var str="/UploadDelAction.do?NOTTOKEN=NOTTOKEN&operation=3&fileName="+encodeURIComponent(fileName)+"&tempFile="+tempFile+"&tableName="+tableName+"&type="+type;
 AjaxRequest(str);
 var value = response; 
 if(value=="no response text" && value.length==0){
 return;
 } 
 }
 var li=m('uploadfile_'+fileName);
 if("false" == tempFile && type == "AFFIX"){
 li.outerHTML = "<input type=hidden name=uploadDelAffix value='"+fileName+"'>";
 }else if("false" == tempFile && type == "PIC"){
 li.outerHTML = "<input type=hidden name=uploadDelPic value='"+fileName+"'>";
 }else{ 
 li.outerHTML = "";
 }
}
var curUploadType = "";
function openAttachUpload(type,btnId){
 curUploadType = type;
 var filter = "";
 if(type == "PIC"){
 filter = "Image";
 }
 
 var attachUpload = document.getElementById("attachUpload");
 if(attachUpload == null){
 uploadDiv = document.createElement("div"); 
 uploadDiv.id = "attachUpload" ;
 uploadDiv.style.cssText = "position:absolute; top:10px;left:30px; width=600px;height:300px; display:block";
 document.body.appendChild(uploadDiv);
 attachUpload = document.getElementById("attachUpload");
 }
 var clientHeight = document.documentElement.clientHeight;
 if(clientHeight==0) {
 clientHeight = document.body.clientHeight ;
 }
 attachUpload.style.left= ((document.body.clientWidth - 500) /2) +"px";
 attachUpload.style.top= ((clientHeight - 250) /2) +"px";
 attachUpload.style.display="block";
 attachUpload.innerHTML='<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" id="fileUpload" width="500" height="250" codebase="http:/'+'/fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">'+
 ' <param name="movie" value="/flash/FileUpload.swf" /><param name="quality" value="high" /><param name="bgcolor" value="#869ca7" /><param name="flashvars" value="maxSize=0&uploadUrl=/UploadServlet?path=/temp/@amp;fileType='+type+'&filter='+filter+'&btnId='+btnId+'" />'+
 ' <param name="allowScriptAccess" value="sameDomain" /><embed src="/flash/FileUpload.swf" quality="high" bgcolor="#869ca7" width="500" height="250" name="column" align="middle" play="true" loop="false"'+
 ' flashvars="maxSize=0&uploadUrl=/UploadServlet?path=/temp/@amp;fileType='+type+'&filter='+filter+'&btnId='+btnId+'" quality="high" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http/'+'/www.adobe.com/go/getflashplayer"></embed></object>';
} 
function onCompleteUpload(retstr,btnId){ 
 retstr = decodeURIComponent(retstr); 
 if(curUploadType == 'PIC'){
 var buttonId = "picuploadul";
 var hiddenName = "uploadpic";
 if(btnId!="undefined" && btnId!=null){
 buttonId = "picuploadul_"+btnId;
 hiddenName = btnId;
 }
 mstrs = retstr.split(";");
 var ul=$('#'+buttonId);
 for(i=0;i<mstrs.length;i++){
 str = mstrs[i];
 if(str == "") continue;
 var imgstr = "<li style='float:left;margin-left:15px;list-style:none;' id='uploadfile_"+str+"'><input type=hidden name="+hiddenName+" value='"+str+"'><div style=\"float:left;\"><a href=\"/ReadFile?fileName="+str+"&realName="+encodeURI(str)+"&tempFile=true&type=PIC\" target=\"_blank\"><img src=\"/ReadFile.jpg?fileName="+encodeURI(str)+"&realName="+encodeURI(str)+"&tempFile=true&type=PIC\" width=\"150\" border=\"0\"></a></div><div style=\"float:left;\"><img src='/style1/images/delete_button.gif' id='empDel_field_1' onclick='deleteupload(\""+str+"\",\"true\",\"$tableName\",\"PIC\");' alt='删除' title='删除' class='search'></div></li>";
 ul.append(imgstr);
 }
 }else if(curUploadType == 'AFFIX'){
 var buttonId = "affixuploadul";
 var hiddenName = "uploadaffix";
 if(btnId!="undefined" && btnId!=null){
 buttonId = "affixuploadul_"+btnId;
 hiddenName = btnId;
 }
 mstrs = retstr.split(";");
 for(i=0;i<mstrs.length;i++){
 str = mstrs[i];
 if(str == "") continue;
 var ul=$('#'+buttonId);
 var imgstr = "<li style='background:url();' id='uploadfile_"+str+"'><input type=hidden name="+hiddenName+" value='"+str+"'><div class='showAffix'>"+str+"</div>&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:deleteupload(\""+str+"\",\"true\",\"$tableName\",\"AFFIX\");'>删除</a>&nbsp;&nbsp;&nbsp;<a href=\"/ReadFile?fileName="+encodeURI(str)+"&realName="+encodeURI(str)+"&tempFile=true&type=AFFIX\" target=_blank>下载</a></li>";
 ul.append(imgstr);
 }
 } 
 var attachUpload = document.getElementById("attachUpload");
 attachUpload.style.display="none";
}

function upload(type,btnId){
 openAttachUpload(type,btnId);
 return; 
}

function openInputDate(obj){
 WdatePicker();
}

function openInputTime(obj){
 WdatePicker({lang:'zh_CN',dateFmt:'yyyy-MM-dd HH:mm:ss'});
}

$(document).ready(function(){
 $("img").each(function(){
 if(typeof($(this).attr("imgtype"))!="undefined" && $(this).attr("imgtype")!="pic"){
 
 $(this).css({cursor:'pointer'});
 var imgType=$(this).attr("imgtype");
 var Id=$(this).attr("id");
 
 var isMutiplse=""; 
 if(imgType!="affixAndPic" && imgType!="employee" && imgType!="dept" && imgType!="client" && imgType!="count" && imgType!="popup" ){
 $(this).before("<input type='text' class='changeInput' id='"+Id+"' name='"+Id+"' />");
 }else{
 isMutiplse=$(this).attr("ismultiple");
 }
 
 if("longdate"==imgType || "shortdate"==imgType || "count"==imgType){
 var imgSrc=$(this).attr("src");
 $("input[name='"+Id+"']").css({background: "#fff url('"+imgSrc+"') no-repeat right "});
 }
 
 if("employee"==imgType){ 

 $(this).before("<input type='hidden' id='"+Id+"' name='"+Id+"' value='' />");
 var sizeVal=jQuery.trim($(this).attr("size"));
 if(isMutiplse=="false"){ 
 
 var widthVal="";
 if(sizeVal!=""){
 widthVal="width:"+sizeVal+"px;";
 } 
 $(this).wrap('<span style="display:inline-block;"></span>');
 if(sizeVal==""){
 $(this).before("<input type='text' class='borderInput' id='popedomUserIds_"+Id+"' name='popedomUserIds_"+Id+"' />");
 }else{
 $(this).before("<input type='text' class='borderInput2' style='"+widthVal+"' id='popedomUserIds_"+Id+"' name='popedomUserIds_"+Id+"' />");
 }
 var relateDept=$(this).attr("relateDept");
 var deptId="";
 if((jQuery.trim(relateDept)).length>0){
 deptId= $("img[title='"+relateDept+"']").attr("id");
 if(typeof(deptId)=="undefined"){
 deptId= $("img[newtitle='"+relateDept+"']").attr("id");
 }
 }
 var titleId="";
 var relateTitle=$(this).attr("relatePosition");
 if((jQuery.trim(relateTitle)).length>0){
 titleId=$("input[title='"+relateTitle+"'][inputtype='txt']").attr("id");
 
 }
 if(typeof($(this).attr("isDefault"))!="undefined" && $(this).attr("isDefault")=="true"){
 $("input[name='popedomUserIds_"+Id+"']").val(sys_empNameValue);
 $("input[name='"+Id+"']").val(hiddenempId);
 
 if((jQuery.trim(relateTitle)).length>0){
 $("#"+titleId).val(sys_empTitleValue);
 }
 if((jQuery.trim(relateDept)).length>0){
 if($("input[name='popedomDeptIds_"+deptId+"']").size()==0){
 var deptsizeVal=jQuery.trim($("img[id='"+deptId+"']").attr("size"));
 createDeptElement(deptsizeVal,deptId);
 }
 $("input[name='"+deptId+"']").val(hiddendeptId);
 $("input[name='popedomDeptIds_"+deptId+"']").val(sys_deptValue);
 
 }
 }
 $("input[name='popedomUserIds_"+Id+"']").attr("ondblclick","deptPopSingle('userGroup','popedomUserIds_"+Id+"','"+Id+"','1','"+deptId+"','"+titleId+"');");
 $("input[name='popedomUserIds_"+Id+"']").attr("onclick","deptPopSingle('userGroup','popedomUserIds_"+Id+"','"+Id+"','1','"+deptId+"','"+titleId+"');");
 $("img[name='"+Id+"']").attr("onclick","deptPopSingle('userGroup','popedomUserIds_"+Id+"','"+Id+"','1','"+deptId+"','"+titleId+"');");
 $("img[name='"+Id+"']").attr("class","imgInput");
 }else{
 var empNames="empNames"+Id;
 var empDiv="emp_context"+Id;
 var empImg="empImg_"+Id;
 var empA="empA_"+Id;
 var empDel="empDel_"+Id;
 
 var widthVal="width:110px;";
 if(sizeVal!=""){
 widthVal="width:"+sizeVal+"px;";
 } 
 
 var html='<div class="oa_signDocument1" id="'+empDiv+'" > '
 +' <div class="oa_signDocument_3"> '
 +'<img id="'+empImg+'" src="/style1/images/St.gif" onClick="deptPop(\'userGroup\',\''+empNames+'\',\''+Id+'\',\'1\');" alt="选择个人" class="search" /> '
 +' &nbsp;<a href="javascript:void(0)" id="'+empA+'" onClick="deptPop(\'userGroup\',\''+empNames+'\',\''+Id+'\',\'1\');" title="选择个人"> '
 +' 选择个人</a> '
 +' </div><select name="'+empNames+'" id="'+empNames+'" multiple="multiple" style="'+widthVal+'"></select></div><div class="oa_signDocument2">'
 +' <img src="/style1/images/delete_button.gif" id="'+empDel+'" onClick="deleteOpation(\''+empNames+'\',\''+Id+'\')" '
 +' alt="移除" title="移除" class="search" />'
 +'</div>';
 
 $(this).before(html);
 
 if(typeof($(this).attr("isDefault"))!="undefined" && $(this).attr("isDefault")=="true"){
 var loginOpt='<option value="'+hiddenempId+'">'+sys_empNameValue+'</option>';
 $("select[name='"+empNames+"']").prepend(loginOpt);
 $("input[name='"+Id+"']").val(hiddenempId+";");
 }
 $(this).remove();
 }
 }else if("dept"==imgType){

 var sizeVal=jQuery.trim($(this).attr("size"));
 
 if(isMutiplse=="false"){ 
 
 if($("input[name='popedomDeptIds_"+Id+"']").size()==0){
 createDeptElement(sizeVal,Id);
 $(this).attr("newTitle",$(this).attr("title"));
 }
 }else{
 $(this).before("<input type='hidden' id='"+Id+"' name='"+Id+"' value='' />");
 var deptNames="deptNames"+Id;
 var deptDiv="dept_context"+Id;
 var deptImg="deptImg_"+Id;
 var deptA="deptA_"+Id;
 var deptDel="deptDel_"+Id;
 
 var widthVal="width:110px;";
 if(sizeVal!=""){
 widthVal="width:"+sizeVal+"px;";
 } 
 
 var html='<div class="oa_signDocument1" id="'+deptDiv+'" > '
 +' <div class="oa_signDocument_3"> '
 +'<img id="'+deptImg+'" src="/style1/images/St.gif" onClick="deptPop(\'deptGroup\',\''+deptNames+'\',\''+Id+'\',\'1\');" alt="选择部门" class="search" /> '
 +' &nbsp;<a href="javascript:void(0)" id="'+deptA+'" onClick="deptPop(\'deptGroup\',\''+deptNames+'\',\''+Id+'\',\'1\');" title="选择部门"> '
 +' 选择部门</a>'
 +' </div><select name="'+deptNames+'" id="'+deptNames+'" multiple="multiple" style="'+widthVal+'"></select></div><div class="oa_signDocument2">'
 +' <img src="/style1/images/delete_button.gif" id="'+deptDel+'" onClick="deleteOpation(\''+deptNames+'\',\''+Id+'\')" '
 +' alt="移除" title="移除" class="search" />'
 +'</div>';
 
 $(this).before(html);
 $(this).remove();
 }
 }else if("client"==imgType){

 var sizeVal=jQuery.trim($(this).attr("size"));
 $(this).before("<input type='hidden' id='"+Id+"' name='"+Id+"' value='' />");
 if(isMutiplse=="false"){ 

 var widthVal="";
 if(sizeVal!=""){
 widthVal="width:"+sizeVal+"px;";
 } 
 $(this).wrap('<span style="display:inline-block;"></span>');
 if(sizeVal==""){
 $(this).before("<input type='text' class='borderInput' id='popedomClientId_"+Id+"' name='popedomClientId_"+Id+"' />");
 }else{
 $(this).before("<input type='text' class='borderInput2' style='"+widthVal+"' id='popedomClientId_"+Id+"' name='popedomClientId_"+Id+"' />");
 }
 
 $("input[name='popedomClientId_"+Id+"']").attr("ondblclick","deptPopSingle('CrmClickGroup','popedomClientId_"+Id+"','"+Id+"','1','notEmp','notTitle')");
 $("input[name='popedomClientId_"+Id+"']").attr("onclick","deptPopSingle('CrmClickGroup','popedomClientId_"+Id+"','"+Id+"','1','notEmp','notTitle')");
 $("img[name='"+Id+"']").attr("onclick","deptPopSingle('CrmClickGroup','popedomClientId_"+Id+"','"+Id+"','1','notEmp','notTitle')");
 $("img[name='"+Id+"']").attr("class","imgInput");
 }else{
 var clientNames="clientNames"+Id;
 var clientDiv="client_context"+Id;
 var clientImg="clientImg_"+Id;
 var clientA="clientA_"+Id;
 var clientDel="clientDel_"+Id;
 
 var widthVal="width:110px;";
 if(sizeVal!=""){
 widthVal="width:"+sizeVal+"px;";
 } 
 
 var html='<div class="oa_signDocument1" id="'+clientDiv+'" > '
 +' <div class="oa_signDocument_3"> '
 +' <img id="'+clientImg+'" src="/style1/images/St.gif" alt="选择客户" onClick="deptPop(\'CrmClickGroup\',\''+clientNames+'\',\''+Id+'\',\'1\');" class="search" /> '
 +' &nbsp;<a href="javascript:void(0)" id="'+clientA+'" onClick="deptPop(\'CrmClickGroup\',\''+clientNames+'\',\''+Id+'\',\'1\');" title="选择客户"> '
 +' 选择客户</a>'
 +' </div><select name="'+clientNames+'" id="'+clientNames+'" multiple="multiple" style="'+widthVal+'"></select></div><div class="oa_signDocument2">'
 +' <img src="/style1/images/delete_button.gif" id="'+clientDel+'" onClick="deleteOpation(\''+clientNames+'\',\''+Id+'\')" '
 +' alt="移除" title="移除" class="search" />'
 +'</div>';
 
 $(this).before(html);
 $(this).remove();
 }
 }else if("popup"==imgType){ 

 var sizeVal=jQuery.trim($(this).attr("size"));
 $(this).before("<input type='hidden' id='"+Id+"' name='"+Id+"' value='' />");
 
 if(isMutiplse=="false"){ 

 var widthVal="";
 if(sizeVal!=""){
 widthVal="width:"+sizeVal+"px;";
 } 
 
 $(this).wrap('<span style="display:inline-block;"></span>'); 
 if(sizeVal==""){
 $(this).before("<input type='text' class='borderInput' id='popup_"+Id+"' name='popup_"+Id+"' />");
 }else{
 $(this).before("<input type='text' class='borderInput2' style='"+widthVal+"' id='popup_"+Id+"' name='popup_"+Id+"' />");
 }
 
 var imgUrl="if(popMainInput(''))openPopup('Flow_huodongjingfeishiyongshenqingbiao','"+$(this).attr('value')+"','popup_"+Id+"','"+Id+"','"+isMutiplse+"')";
 $("input[name='popup_"+Id+"']").attr("ondblclick",imgUrl);
 $("input[name='popup_"+Id+"']").attr("onclick",imgUrl); 
 $("img[name='"+Id+"']").attr("onclick",imgUrl);
 $("img[name='"+Id+"']").attr("class","imgInput");
 }else{
 var popupNames="popupNames"+Id;
 var popupDiv="popup_context"+Id;
 var popupImg="popupImg_"+Id;
 var popupA="popupA_"+Id;
 var popupDel="popupDel_"+Id;
 
 var widthVal="width:110px;";
 if(sizeVal!=""){
 widthVal="width:"+sizeVal+"px;";
 } 
 
 var imgUrl="if(popMainInput(''))openPopup('Flow_huodongjingfeishiyongshenqingbiao','"+$(this).attr('value')+"','"+popupNames+"','"+Id+"','"+isMutiplse+"')";
 var html='<div class="oa_signDocument1" id="'+popupDiv+'" > '
 +' <div class="oa_signDocument_3"> '
 +' <img id="'+popupImg+'" src="/style1/images/St.gif" onClick="'+imgUrl+'" alt="选择'+$(this).attr("title")+'" class="search" /> '
 +' &nbsp;<a href="javascript:void(0)" id="'+popupA+'" onClick="'+imgUrl+'" title="选择'+$(this).attr("title")+'"> '
 +' 选择'+$(this).attr("title")+'</a>'
 +' </div><select name="'+popupNames+'" id="'+popupNames+'" multiple="multiple" style="'+widthVal+'"></select></div><div class="oa_signDocument2">'
 +' <img src="/style1/images/delete_button.gif" id="'+popupDel+'" onClick="deleteOpation(\''+popupNames+'\',\''+Id+'\')" '
 +' alt="移除" title="移除" class="search" />'
 +'</div>';
 
 $(this).before(html);
 $(this).remove();
 }
 
 }else if("longdate"==imgType){ 

 var varField = jQuery("input[name='"+Id+"']") ;
 varField.click(function(){
 WdatePicker({lang:'zh_CN',dateFmt:'yyyy-MM-dd HH:mm:ss'});
 }) ;
 jQuery("input[name='"+Id+"']").width(135);
 $(this).remove();
 }else if("shortdate"==imgType){ 

 var varField = jQuery("input[name='"+Id+"']") ;
 varField.click(function(){
 WdatePicker({lang:'zh_CN'});
 });
 jQuery("input[name='"+Id+"']").width(90)
 $(this).remove();
 }
 var fieldName=$(this).attr("name");
 if("affixAndPic"==imgType){ 

 if($(this).attr("systype")=="affix") { 
 var affixStr="affixbuttonthe"+fieldName;
 var affixUpload="affixuploadul_"+fieldName;
 var url="<span id='"+affixStr+"' name='"+affixStr+"' style='cursor:pointer;' onClick='upload(\"AFFIX\",\""+fieldName+"\");'><img src='/style1/images/oaimages/uploading.gif' />附件上传</span><ul id='"+affixUpload+"'></ul>";
 $(this).before(url);
 }else{
 var picStr="picbuttonthe"+fieldName;
 var picUpload="picuploadul_"+fieldName;
 $(this).before("<input type='hidden' name='picFiles' id='pic_"+Id+"'/>");
 var url="<span id='"+picStr+"' name='"+picStr+"' style='cursor:pointer;' onClick='upload(\"PIC\",\""+fieldName+"\");'><img src='/style1/images/oaimages/uploading.gif' />图片上传</span><ul id='"+picUpload+"'></ul>";
 $(this).before(url);
 }
 $(this).remove();
 }
 
 if("count"==imgType){ 
 $(this).wrap('<span style="display:inline-block;"></span>'); 
 $(this).before("<input type='text' class='borderInput' id='"+Id+"' name='"+Id+"' />");
 $(this).attr("id","");
 $(this).click(function(){
 jQuery('#'+Id).val(countVal(Id));
 });
 $(this).attr("class","imgInput");
 }
 
 if("hong"==imgType){

 var sizeVal=jQuery.trim($(this).attr("size"));
 var widthVal="110px";
 if(sizeVal!=""){
 widthVal=sizeVal+"px";
 } 
 var hongType=$(this).attr("systype");
 var hongVal="";
 if(hongType=="sys_date"){ 
 hongVal = sys_dateValue;
 }else if(hongType=="sys_time"){
 hongVal = sys_timeValue;
 }else if(hongType=="sys_datetime"){

 hongVal = sys_datetimeValue;
 }else if(hongType=="sys_empName"){

 hongVal = sys_empNameValue;
 }else if(hongType=="sys_empTitle"){
 hongVal = sys_empTitleValue;
 }else if(hongType=="sys_dept"){
 hongVal = sys_deptValue;
 }else if(hongType=="sys_startdate"){
 hongVal = sys_startdateValue;
 }else if(hongType=="sys_starttime"){
 hongVal = sys_starttimeValue;
 }
 $("input[name='"+Id+"']").val(hongVal);
 $("input[name='"+Id+"']").attr("readonly","readonly");
 $("input[name='"+Id+"']").css({width:widthVal});
 $(this).remove();
 }
 $(this).removeAttr("title");
 $(this).remove("id");
 }
 });
 $("input").each(function(){
 if(typeof($(this).attr("title"))!="undefined"){
 
 $(this).removeAttr("title");
 var inputType=$(this).attr("type");
 if(inputType=="text" || typeof($(this).attr("inputtype"))!="undefined"){
 $(this).attr("class","changeInput2");
 
 if(typeof($(this).attr("inputtype"))!="undefined" && $(this).attr("inputtype")=="BillNo"){
 $(this).attr("readonly","readonly");
 }
 }else{
 if(inputType=="radio"){ 
 var radioList=$(this).attr("dioname").split(";");
 var radioSelect=$(this).val();
 var radioId="radio"+$(this).attr("name");
 $(this).wrap("<div id='"+radioId+"' style='display: inline-block;'></div>");
 for(var i=0;i<radioList.length;i++){
 if(radioList[i].length>0){
 var checked = "";
 if(radioList[i]==radioSelect){
 checked = "checked";
 }
 var addInput="<input type='radio' value='"+radioList[i]+"' name='"+$(this).attr("name")+"' "+checked+" /> "+radioList[i]+" ";
 $(this).before(addInput);
 }
 }
 }
 
 if(inputType=="checkbox"){ 
 var chkList=$(this).attr("chkname").split(";");
 var chkSelect=$(this).val();
 var checkId="checkbox"+$(this).attr("name");
 $(this).wrap("<div id='"+checkId+"' style='display: inline-block;'></div>");
 for(var i=0;i<chkList.length;i++){
 if(chkList[i].length>0){
 var checked = "" ;
 if(chkSelect.indexOf(chkList[i]+";")>-1){
 checked = "checked";
 }
 var addInput="<input type='checkbox' value='"+chkList[i]+"' name='"+$(this).attr("name")+"' "+checked+"/>"+chkList[i]+" ";
 $(this).before(addInput);
 }
 }
 }
 $(this).remove();
 }
 }
 });
 
 $("select").each(function(){
 if(typeof($(this).attr("title"))!="undefined"){
 $(this).removeAttr("title");
 }
 if($(this).attr("selectval")==""){
 var addOption='<option value="" selected="selected"></option>';
 $(this).prepend(addOption);
 }
 });
});

function createDeptElement(sizeVal,Id){
 $("img[id='"+Id+"']").before("<input type='hidden' id='"+Id+"' name='"+Id+"' value='' />");
 var widthVal="";
 if(sizeVal!=""){
 widthVal="width:"+sizeVal+"px;";
 } 
 $("img[id='"+Id+"']").wrap('<span style="display:inline-block;"></span>');
 if(sizeVal==""){
 $("img[id='"+Id+"']").before("<input type='text' class='borderInput' id='popedomDeptIds_"+Id+"' name='popedomDeptIds_"+Id+"' />");
 }else{
 $("img[id='"+Id+"']").before("<input type='text' class='borderInput2' style='"+widthVal+"' id='popedomDeptIds_"+Id+"' name='popedomDeptIds_"+Id+"' />");
 }
 $("input[name='popedomDeptIds_"+Id+"']").attr("ondblclick","deptPopSingle('deptGroup','popedomDeptIds_"+Id+"','"+Id+"','1','notEmp','notTitle')");
 $("input[name='popedomDeptIds_"+Id+"']").attr("onclick","deptPopSingle('deptGroup','popedomDeptIds_"+Id+"','"+Id+"','1','notEmp','notTitle')"); 
 $("img[name='"+Id+"']").attr("onclick","deptPopSingle('deptGroup','popedomDeptIds_"+Id+"','"+Id+"','1','notEmp','notTitle')");
 $("img[name='"+Id+"']").attr("class","imgInput");
}


function checkTable(allHidFields){
 var labelText=$("#field_1",document);
 if(typeof(labelText)!="string"){
 labelText = labelText.val();
 }
 
 if(typeof(labelText)!= "undefined" && labelText.length == 0 && !(allHidFields.indexOf("field_1")>-1)){ 
 alert("申请人"+"不能为空，请重新输入!");
 $("#field_1",document).focus();
 return false;
 }
 
 var labelText = $("input[name='field_10']",document);
 var inputType = labelText.attr("inputtype");
 if(typeof(inputType)!="undefined"){
 if(inputType == "integer"){
 if(labelText.val().length>0 && !isInt(labelText.val())){
 alert("费用预算"+"必须为整形数字，请重新输入!");
 labelText.focus();
 return false;
 }
 }else if(inputType=="float"){
 if(labelText.val().length>0 && !isFloat(labelText.val())){
 alert("费用预算"+"必须为小数，请重新输入!");
 labelText.focus();
 return false;
 }
 }
 }
 
 var labelText = $("input[name='field_7']",document);
 var inputType = labelText.attr("inputtype");
 if(typeof(inputType)!="undefined"){
 if(inputType == "integer"){
 if(labelText.val().length>0 && !isInt(labelText.val())){
 alert("参与人数"+"必须为整形数字，请重新输入!");
 labelText.focus();
 return false;
 }
 }else if(inputType=="float"){
 if(labelText.val().length>0 && !isFloat(labelText.val())){
 alert("参与人数"+"必须为小数，请重新输入!");
 labelText.focus();
 return false;
 }
 }
 }
 
 var labelText = $("input[name='field_8']",document);
 var inputType = labelText.attr("inputtype");
 if(typeof(inputType)!="undefined"){
 if(inputType == "integer"){
 if(labelText.val().length>0 && !isInt(labelText.val())){
 alert("活动名称"+"必须为整形数字，请重新输入!");
 labelText.focus();
 return false;
 }
 }else if(inputType=="float"){
 if(labelText.val().length>0 && !isFloat(labelText.val())){
 alert("活动名称"+"必须为小数，请重新输入!");
 labelText.focus();
 return false;
 }
 }
 }
 
 var labelText = $("input[name='field_9']",document);
 var inputType = labelText.attr("inputtype");
 if(typeof(inputType)!="undefined"){
 if(inputType == "integer"){
 if(labelText.val().length>0 && !isInt(labelText.val())){
 alert("活动地点"+"必须为整形数字，请重新输入!");
 labelText.focus();
 return false;
 }
 }else if(inputType=="float"){
 if(labelText.val().length>0 && !isFloat(labelText.val())){
 alert("活动地点"+"必须为小数，请重新输入!");
 labelText.focus();
 return false;
 }
 }
 }
 
 return true;
}

function countVal(Id){ 
 var MinMilli = 1000 * 60;
 var HrMilli = MinMilli * 60;
 var DyMilli = HrMilli * 24;
 var result;
 return Math.round(result*100)/100;
}

function isInt(str){
 var pattern = new RegExp("^[-]{0,1}[0-9]*$");
 ret = str.search(pattern);
 return ret>-1;
}


function isFloat(str){
 if(str == 'Infinity'||str=='-Infinity') return false;
 return !isNaN(str);
}