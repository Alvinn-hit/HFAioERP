if(typeof(top.jblockUI)!="undefined")top.junblockUI();

function openInputDate(obj)
{
 WdatePicker({lang:'zh_CN'});
}

function delajax(){
 var url=form.action;
 var data = $("[name='form']").serializeArray();
 jQuery.ajax({
 type: "POST", 
 url: url,
 data:data,
 success: function(msg){
 if(msg=="ok"){
 window.location.reload();
 }
 }
 }); 
}

function sureDel(itemName){

 if(!isChecked(itemName)){
 asyncbox.tips("请选择至少一条记录",'alert',1500);
 return false;
 } 
 if(groupId==2){
 asyncbox.confirm('此草稿将被彻底删除，确定要删除？','提示',function(action){
 if(action == 'ok'){
 form.operation.value=3;
 delajax();
 }
 });
 }else{
 form.operation.value=3;
 asyncbox.confirm('确定删除吗','提示',function(action){
 if(action == 'ok'){
 delajax();
 }else{
 form.operation.value = 4;
 cancelSelected("input");
 }
 });
 }
}
function sureDelChe(itemName){
 if(!isChecked(itemName)){
 asyncbox.tips("请选择至少一条记录",'alert',1500);
 return false;
 }
 if(groupId==2 && !confirm('此草稿将被彻底删除，确定要删除？')){
 return false;
 }
 form.operation.value=3;
 asyncbox.confirm('彻底删除后邮件将无法恢复，您确定要删除吗？','提示',function(action){
 if(action == 'ok'){
 form.deleteReal.value="true";
 delajax();
 }else{
 form.operation.value = 4;
 cancelSelected("input");
 }
 });
}




function sign(){ 
 if(!isChecked("keyId")){
 asyncbox.tips("请选择至少一条记录",'alert',1500);
 return false;
 }
 form.operation.value=2; 
 form.submit();
}

function move()
{ 
 if(document.form.moveGroup.value == "-1"){
 return;
 }
 if(!isChecked("keyId")){
 asyncbox.tips("请选择至少一条记录",'alert',1500);
 return false;
 }
 form.operation.value=2; 
 form.submit();
 
}
function movetotrash(){ 
 if(!isChecked("keyId")){
 asyncbox.tips("请选择至少一条记录",'alert',1500);
 return false;
 }
 asyncbox.confirm('确定这是垃圾邮件?','提示',function(action){
 if(action == 'ok'){
 document.form.moveGroup.value = "4";
 form.operation.value=2; 
 form.submit();
 }
 }); 
}
function openSelect1(urlstr,displayName,obj){

 displayName=encodeURI(displayName) ;
 var str = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=add&LinkType=@URL:&displayName="+displayName,winObj,"dialogWidth=730px;dialogHeight=450px"); 

 if(typeof(str)!="undefined"){
 var mutli=str.split("|"); 
 hid="";
 dis="";
 if(str.length>0){
 var len=mutli.length;
 if(len>1){len=len-1}
 for(j=0;j<len;j++){ 
 fs=mutli[j].split(";");
 dis=fs[4];
 hid=fs[1];
 if(hid.indexOf("@Sess:")>=0){
 document.getElementById(obj).value="";
 }else{
 document.getElementById(obj).value=dis;
 
 }
 }
 }else{
 document.getElementById(obj).value="";
 }
 }
}

function selectEmployee(o)
{
 var urlstr = '/UserFunctionAction.do?operation=22&tableName=tblEmployee&selectName=SelectAllEmployee' ;
 var str = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=query","","dialogWidth=730px;dialogHeight=450px"); 
 var content = str.split(";") ;
 o.value=content[0] ;
}

function ch(){
 document.getElementById("readOuter").disabled=true;
 var account = document.getElementById("account") ;
 var value = account.options[account.selectedIndex].value;
 window.location ='/OAInnersMail.do?type=readOuterEmail&operation=4&account='+value;
 if(typeof(top.jblockUI)!="undefined"){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 }
} 

function openSelect3(to){
 displayName=encodeURI("发件人") ;
 urlstr = '/UserFunctionAction.do?selectName=Communication_Select2&operation=22&LinkType=@URL:&displayName='+displayName;
 var str = window.showModalDialog(urlstr+"&MOID=$MOID&MOOP=add","","dialogWidth=700px;dialogHeight=450px");
 if(typeof(str)=="undefined")return ;
 if(str.indexOf("@Sess:")>=0||str.length<=2){
 document.getElementById(to).value="";
 return ;
 }
 var varStr = str.split("|"); 
 var varBbc = document.getElementById(to).value ;
 for(var i=0;i<varStr.length;i++){
 var varUser = varStr[i].split(";");
 if(varUser[0]!=""){
 var isExist = varBbc.indexOf(varUser[0]+",") ;
 if(isExist==-1){
 varBbc = varUser[0] ;
 }
 }
 }
 document.getElementById(to).value =varBbc;
}

function receiver(){
 parent.leftFrame.receiver();
 
}
function submits(submitType){
 var keyIds=document.getElementsByName("keyId");
 var keyId="";
 var flag=false;
 var mt ="";
 var mr="";
 for(var i=0;i<keyIds.length;i++){
 if(keyIds[i].checked){
 if(flag){
 asyncbox.tips("转发或者回复最多选择一条记录",'alert',1500);
 return false;
 }else{
 flag=true;
 keyId=keyIds[i].value;
 mt =keyIds[i].getAttribute("emailType");
 mr =keyIds[i].getAttribute("moreMail");
 }
 }
 }
 if(!flag){
 asyncbox.tips("请选择至少一条记录",'alert',1500);
 return false;
 }
 window.location.href="/EMailAction.do?operation=6&keyId="+keyId+"&type="+submitType+"&emailType="+mt+"&moreMail="+mr+"&groupId="+groupId;
}

var mailInfoId;
function showPopMenu(mailid){
 mailInfoId = mailid;
 
 var pop=window.createPopup();
 

 pop.document.body.innerHTML=menuDiv.innerHTML; 
 var rowObjs=pop.document.body.all[0].rows;
 

 var rowCount=rowObjs.length;
 

 pop.document.oncontextmenu=function()
 {
 return false;
 }
 

 pop.document.onclick=function()
 {
 pop.hide();
 }
 
 pop.show(event.clientX-1,event.clientY,100,rowCount*25,document.body);
 event.returnValue=false;
 event.cancelBubble=true;
 return false;
}
function setLabel(){
 labelId = document.form.setLabel.value;
 if(labelId == "-1"){
 return;
 }
 if(!isChecked("keyId")){
 asyncbox.tips("请选择至少一条记录",'alert',1500);
 return false;
 }
 form.operation.value=2; 
 form.submit();
}

function isdescimgclick(){
 if(document.form.isdesc.value=="false"){
 document.form.isdesc.value = "true";
 document.getElementById("isdescimg").src="/style/images/mail/desc.gif"; 
 }else{
 document.form.isdesc.value = "false";
 document.getElementById("isdescimg").src="/style/images/mail/asc.gif";
 }
 document.form.submit();
}

function orderByChange(){
 document.form.submit();
}
function viewChange(){
 document.form.submit();
}
function clickMail(mailId,mailTitle,createTime){
 window.location.href="/EMailAction.do?operation=5&groupId="+groupId+"&emailType="+emailType+"&iframe=true&keyId="+mailId+"&createTime="+createTime;

}
function update(mailId){

 var keyIds=document.getElementsByName("keyId");
 var keyId="";
 var flag=false;
 var mt ="";
 var mr="";
 for(var i=0;i<keyIds.length;i++){
 if(keyIds[i].checked){
 if(flag){
 asyncbox.tips("转发或者回复最多选择一条记录",'alert',1500);
 return false;
 }else{
 flag=true;
 keyId=keyIds[i].value;
 mt =keyIds[i].getAttribute("emailType");
 mr =keyIds[i].getAttribute("moreMail");
 }
 }
 }
 if(!flag){
 asyncbox.tips("请选择至少一条记录",'alert',1500);
 return false;
 }
 
 window.location.href="/EMailAction.do?operation=5&keyId="+keyId+"&emailType="+mt+"&moreMail="+mr+"&groupId="+groupId;
}
function prenext(isNext,totalPage){
 inp = null;
 if(isNext){
 inp = $("#CK_"+curMailId).parent().parent().prev().find("input"); 
 if(inp.length==0){
 if(Number(document.form.pageNo.value) > 1){
 document.form.pageNo.value = Number(document.form.pageNo.value) -1;
 document.form.isprev.value="true";
 document.form.submit();
 }else{
 asyncbox.tips("已经是第一封邮件了.",'alert',1500);
 }
 }
 }else{
 inp = $("#CK_"+curMailId).parent().parent().next().find("input");
 if(inp.length==0){
 if(Number(document.form.pageNo.value) != totalPage){
 document.form.pageNo.value = Number(document.form.pageNo.value) +1;
 document.form.submit();
 }else{
 asyncbox.tips("已经是最后一封邮件了.",'alert',1500);
 }
 }
 } 
 inp.focus(); 
 if(inp.length > 0){
 clickMail(inp.val());
 }
}

function startPrint(obj)
{ 

 x = window.screen.availWidth /2-300;
 y = window.screen.availHeight /2-300;
 var oWin=window.open("","_blank",'height=470px,width=768px,top='+y+',left='+x+',toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no');
 oWin.document.open();
 oWin.document.write(obj.innerHTML);
 
 this.focus();
 oWin.document.close();
 oWin.print();
 oWin.close(); 
 
}


$(document).ready(function() {
 $(".toolbar").find("li").hover(function() {
 $(this).addClass("tollbarover");
 }, function() {
 $(this).removeClass("tollbarover");
 });
 
 var isDrag=false;
 var dragObj;
 $("#moveId").mousedown( function (){ 
 isDrag=true;
 dragObj = event.srcElement;
 dragObj.setCapture();
 });
 $("#moveId").mouseup( function (){
 isDrag=false;
 dragObj.releaseCapture();
 dragObj = null;
 });
 $("#moveId").mousemove( function (){
 if(isDrag){ 
 if(event.x >20){
 $("#listMailDiv").css("width",event.x-8); 
 } 
 }
 });
});

function Do_newOpen(){ 
 window.open("/EMailAction.do?operation=5&emailType="+emailType+"&noback=true&keyId="+curParam+"&newOpen=true"); 
}
function Do_signRead(){ 
 document.form.sign.value="1";
 sign();
}
function Do_signNoread(){ 
 document.form.sign.value="0";
 sign();
}
function popsubmits(submitType){
 keyObj = document.getElementById("CK_"+curParam); 
 window.location.href="/EMailAction.do?operation=6&keyId="+curParam+"&type="+submitType+"&emailType="+keyObj.getAttribute("emailType")+"&moreMail="+keyObj.getAttribute("moreMail");
}
function Do_replay(){ 
 popsubmits('revert');
}

function Do_replayAll(){ 
 popsubmits('revertAll');
}
function Do_transmit(){ 
 popsubmits('transmit');
}

function Do_del(){ 
 form.operation.value=3;
 asyncbox.confirm('确定删除吗','提示',function(action){
 if(action == 'ok'){
 document.getElementById("selAll").checked=false;
 isAllSelectSelected = false;
 items = document.getElementsByName('keyId');
 for(var i=0;i<items.length;i++){
 items[i].checked =false;
 } 
 curMailId = curParam;
 document.getElementById("CK_"+curParam).checked=true;
 form.submit();
 }else{
 form.operation.value = 4;
 cancelSelected("input");
 }
 }); 
}
function Do_clear(){ 
 form.operation.value=3;
 asyncbox.confirm('确定删除吗','提示',function(action){
 if(action == 'ok'){
 document.getElementById("selAll").checked=false;
 isAllSelectSelected = false;
 items = document.getElementsByName('keyId');
 for(var i=0;i<items.length;i++){
 items[i].checked =false;
 } 
 curMailId = curParam;
 document.getElementById("CK_"+curParam).checked=true;
 form.deleteReal.value="true";
 form.submit();
 }else{
 form.operation.value = 4;
 cancelSelected("input");
 }
 }); 
}
function Do_contact(){
 ckkey = document.getElementById("CK_"+curParam);
 detail_contact(curParam,ckkey.getAttribute("contactId"),ckkey.getAttribute("eName"),ckkey.getAttribute("email")); 
}
function detail_contact(mailId,contactId,conName,email){
 if(contactId == ""){
 var wo =window.open("/OACommunication.do?operation=4&type=Communication&optiontype=addPrepare&conName="+encodeURIComponent(conName)+"&emailAddress="+email+"&noback=true");
 if (window.addEventListener){
 wo.addEventListener('unload', mailunload, false);
 } else if (window.attachEvent){
 wo.attachEvent('onunload', mailunload); 
 }
 }else{
 var wo =window.open("/OACommunicationQuery.do?operation=4&optiontype=detail&keyWord=&groupId=&pageNo=1&keyId="+contactId+"&noback=true"); 
 } 
}
function Do_black(){
 ckkey = document.getElementById("CK_"+curParam);
 jQuery.get("/EMailBlackAction.do?operation=1&email="+ckkey.getAttribute("email"),function(data){ alert('加入黑名单成功'); }); 

}
var curMailTitle="";
var curMailId="";
function mailunload(){
 
 if(typeof(document.form.curMailId) == "undefined"){
 if(typeof(parent.document.form.curMailId) != "undefined"){
 
 
 window.location.reload();
 }
 if(typeof(opener.document.form.curMailId) != "undefined"){
 opener.document.form.curMailId.value = curMailId;
 opener.document.form.submit();
 }
 }else{
 document.form.curMailId.value = curMailId;
 document.form.submit();
 }

}

function openNewClient(wo){ 
 if (window.addEventListener){
 wo.addEventListener('unload', mailunload, false);
 } else if (window.attachEvent){
 wo.attachEvent('onunload', mailunload); 
 }

}
function Do_client(){
 
 var ck = jQuery("#CK_"+curParam);
 detail_client(curParam,ck.attr("clientId"),ck.attr("eName"),ck.attr("email"),ck.attr("clientName")); 
}
function detail_client(mailId,clientId,cName,email,clientName){
 if(/.*[\u4e00-\u9fa5]+.*$/.test(email)) 
 { 
 alert("邮箱地址含有中文,无法关联客户！");
 asyncbox.tips("邮箱地址含有中文,无法关联客户！",'alert',1500); 
 return false; 
 } 
 
 if(!isMail(email)){
 asyncbox.tips("邮箱地址不正确,无法关联客户！",'alert',1500);
 return false; 
 }
 
 if(clientId == ""){
 var height = 540;
 var winHeight = 450;
 if(typeof(jQuery("#oaMailDetail").css("height")) != "undefined"){
 winHeight = parseInt(jQuery("#oaMailDetail").css("height").substring(0,jQuery("#oaMailDetail").css("height").length-2)) + jQuery("#oaMailDetail").position().top;
 }
 if(height>winHeight){
 height = winHeight;
 }
 var url = "/CRMClientAction.do?operation=6&tableName=CRMClientInfo&contactTableName=CRMClientInfoDet&moduleId=1&nowHead=2&email=" + email;
 asyncbox.open({
 id:'crmOpDiv',url:url,title:'添加客户',width:840,height:height,cache:false,modal:true,btnsbar:jQuery.btn.OKCANCEL,
 callback : function(action,opener){
 if(action == 'ok'){
 opener.beforeSubmit();
 return false;
 }
 　　 　 }
 　 });
 }else{
 mdiwin('/CRMClientAction.do?operation=5&type=detailNew&keyId='+clientId,clientName);
 }
}


function crmOpDeal(){
 jQuery.close("crmOpDiv"); 
 window.location.reload();
}


function myImport(type){
 window.location.href="/EMailImportAction.do?operation=6&importMail=prepare&emailType="+emailType+"&groupId="+groupId+"&importType="+type; 
}
function myExport(itemName,type){
 if(!isChecked(itemName)){
 asyncbox.tips("请选择至少一条记录",'alert',1500);
 return false;
 }
 
 if(type == "stadexport" && (form.emailType.value=="" || form.emailType.value=="inner")){
 asyncbox.tips("标准邮件导出，不能导出内部邮件",'alert',1500);
 return false;
 }
 asyncbox.confirm('确定导出吗','提示',function(action){
 if(action == 'ok'){
 form.operation.value=99;
 form.exportType.value=type;
 if(typeof(top.jblockUI)!="undefined"){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 }
 form.submit();
 }else{
 form.operation.value = 4;
 cancelSelected("input");
 }
 }); 
}


var shu=0;
var divh=0;
function moreOperation(){
 opid = document.form.moreOperation.value;
 if(opid == "-1"){
 return;
 }
 if(opid == "export"){
 myExport('keyId','export');
 }else if(opid == "import"){
 myImport('backup');
 }else if(opid == "stadImport"){
 myImport('stadImport');
 }else if(opid == "stadexport"){
 myExport('keyId','stadexport');
 } else if(opid == "movetotrash"){
 movetotrash();
 } else if(opid == "startPrint"){
 
 
 
 
 mailprint();
 }
}


function xinghou(){
 document.getElementById("oaMailDetail").style.height=divh;
 window.frames['mailDetailFrame'].location.reload();
}