var curpicattType= "ATT";
var isrefresh = false;
var notbefor="";
var fileCount=0;
function openAttachUpload(path,type,refresh){
 var filter = "";
 if(type == "PIC"){
 filter = "Image";
 curpicattType = "PIC";
 }
 if(refresh ){
 isrefresh = true;
 }
 
 var attachUpload = document.getElementById("attachUpload");
 if(attachUpload == null){
 uploadDiv = document.createElement("div"); 
 uploadDiv.id = "attachUpload" ;
 uploadDiv.style.cssText = "position:absolute;top:10px;left:30px;width:600px;height:0px; display:block;z-index:100;";
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
 ' <param name="movie" value="/flash/FileUpload.swf" /><param name="quality" value="high" /><param name="bgcolor" value="#869ca7" /><param name="flashvars" value="maxSize=0&uploadUrl=/UploadServlet;jsessionid=$session.id?path='+path+'&filter='+filter+'" />'+
 ' <param name="allowScriptAccess" value="sameDomain" /><embed src="/flash/FileUpload.swf" quality="high" bgcolor="#869ca7" width="500" height="250" name="column" align="middle" play="true" loop="false"'+
 ' flashvars="maxSize=0&uploadUrl=/UploadServlet;jsessionid=$session.id?path='+path+'&filter='+filter+'" quality="high" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http/'+'/www.adobe.com/go/getflashplayer"></embed></object>';
} 

function onCompleteUpload(retstr){ 
 retstr = decodeURIComponent(retstr); 
 var strs = retstr.split(";");
 for(i=0;i<strs.length;i++){ 
 if(strs[i] != ""){ 
 insertNextFile(strs[i]);
 }
 } 
 var attachUpload = document.getElementById("attachUpload");
 attachUpload.style.display="none";
 
 if(isrefresh){
 window.location.href = window.location.href;
 }
}
 
function insertNextFile(str) 
{ fileCount=fileCount+1;
 if(curpicattType == "PIC"){
 var filevalue = document.form.picFiles.value;
 if(typeof(document.form.picFiles) == "undefined"){
 return;
 }
 if(filevalue.indexOf(str) == -1){ 
 var fileHtml = '';
 fileHtml += '<div id ="'+str+'" style =" color:#ff0000;float:left;vertical-align:top">'; 
 fileHtml += '<a href="/ReadFile.jpg?type=PIC&tempFile=path&path=/news/&fileName='+str+'" target="_blank"><img src="/ReadFile.jpg?type=PIC&tempFile=path&path=/news/&fileName='+str+'" width="150" height="115" border="0"></a>';
 fileHtml += '<div style="top:0px; left:0px; float:right;"><a href="javascript:;" onclick="removeFile(\'' + str + '\',\'PIC\');"><div style="display:block;"><img src="/style/images/del.gif"></a>&nbsp;&nbsp;&nbsp;&nbsp;</div></div></div>';
 var fileElement = document.getElementById("files_preview");
 fileElement.innerHTML = fileElement.innerHTML + fileHtml; 
 document.form.picFiles.value = filevalue+str+";";
 }
 }else{
 if(typeof(document.form.attachFiles) == "undefined"){
 return;
 }
 var filevalue = document.form.attachFiles.value;
 
 if(filevalue.indexOf(str) == -1){ 
 var fileHtml = '';
 fileHtml += '<div id ="'+str+'" style ="height:18px; color:#ff0000;">'; 
 fileHtml += '<a href="javascript:;" onclick="removeFile(\'' + str + '\');"><img src="/style/images/del.gif"></a>&nbsp;&nbsp;'; 
 fileHtml +='<i>'+str+'</i><input type=hidden name="attachFile" value="'+str+'"/></div>'; 
 var fileElement = document.getElementById("files_preview");
 fileElement.innerHTML = fileElement.innerHTML + fileHtml; 
 document.form.attachFiles.value = filevalue+str+";";
 }
 }
}
function removeFile(file,picType) 
{
 notbefor="not";
 fileCount=fileCount-1;
 if(picType == "PIC"){
 document.getElementById("files_preview").removeChild(document.getElementById(file)); 
 var filevalue = document.form.picFiles.value;
 document.form.picFiles.value = filevalue.substr(0,filevalue.indexOf(file))+ filevalue.substr(filevalue.indexOf(file)+file.length+1);
 document.form.delPicFiles.value = document.form.delPicFiles.value+file+";";
 }else{
 document.getElementById("files_preview").removeChild(document.getElementById(file)); 
 var filevalue = document.form.attachFiles.value;
 document.form.attachFiles.value = filevalue.substr(0,filevalue.indexOf(file))+ filevalue.substr(filevalue.indexOf(file)+file.length+1);
 document.form.delFiles.value = document.form.delFiles.value+file+";";
 }
}