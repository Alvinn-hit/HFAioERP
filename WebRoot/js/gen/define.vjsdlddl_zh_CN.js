var tabObj ;
$(document).ready(function(){
 tabObj= new TabObj("input_type"); 
 
 $("#saveButDIV").each(function(){
 if($(this).find(".d-more a").size()==1){
 var clk = $(this).find(".d-more a").attr("onClick");
 if(clk == undefined || clk == "" ){
 clk = $(this).find(".d-more a").attr("href");
 }
 var cid = $(this).find(".d-more a").attr("id");
 var cname = $(this).find(".d-more a").attr("name");
 var ctitle = $(this).find(".d-more a").attr("title");
 var newli = '<span class="hBtns" id="'+ (cid=='' || cid == undefined ?'':cid) +
 '" name="'+(cname=='' || cname == undefined ?'':cname) +
 '" title="'+ (ctitle=='' || ctitle == undefined ?'':ctitle) +'" onClick="'+clk+'">'+
 $(this).find(".d-more a").html()+
 '</span>';
 $(this).parents("li").html(newli);
 }
 });
 
 $(".h-child-btn").each(function(){
 if($(this).find(".d-more a").size()==0){ 
 $(this).parents("li").hide();
 }
 });
 $(".h-child-btn").mouseover(function(){
 $(this).addClass("h-height").find(".d-more").show().siblings(".br-link").show();
 }).mouseout(function(){
 $(this).removeClass("h-height").find(".d-more").hide().siblings(".br-link").hide();
 });
 
 $(document).mouseup( function() { 
 if(curDragObj != null){
 
 var dobjs = document.getElementsByName(curDragObj.attr("name"));
 sl = curDragStartLine;
 el = curDragEndLine;
 if(el==-1){
 el=sl; 
 }
 if(sl>el){
 sl = curDragEndLine;
 el = curDragStartLine;
 }
 for(i=sl;i<=el;i++){
 dobjs[i].value= curDragObj.val();
 $(dobjs[i]).css("border","");
 
 }
 execStat();
 curDragObj = null;
 curchildOpObj=null;
 curDragStartLine = -1;
 curDragEndLine = -1;
 
 if(window.k_interval != undefined)
 {
 clearInterval(window.k_interval);
 window.k_interval = undefined;
 }
 }
 } ); 
 $(document).mousemove( function() { 
 if(curDragObj != null){
 
 dirObj = window.event.srcElement;
 
 var dragobjs = document.getElementsByName(curDragObj.attr("name"));
 
 var myDiv = jQuery("#"+curGridRowNum.curKey+"Table_tableData");
 if(event.clientY < myDiv[0].getBoundingClientRect().top+10)
 {
 if(curDragEndLine == -1) curDragEndLine = curDragStartLine;
 if(window.k_interval==undefined)
 {
 window.k_interval = setInterval(function(){
 myDiv.scrollTop(myDiv.scrollTop()-25); 
 curDragEndLine--; 
 $(dragobjs[curDragEndLine]).css("border","1px solid #ff00ff");
 },300);
 } 
 
 }else if(event.clientY > myDiv[0].getBoundingClientRect().bottom-10)
 {
 if(curDragEndLine == -1) curDragEndLine = curDragStartLine;
 if(window.k_interval==undefined)
 {
 window.k_interval = setInterval(function(){
 myDiv.scrollTop(myDiv.scrollTop()+25); 
 curDragEndLine++; 
 $(dragobjs[curDragEndLine]).css("border","1px solid #ff00ff");
 },300);
 }
 
 }else
 {
 if(window.k_interval != undefined)
 {
 clearInterval(window.k_interval);
 window.k_interval = undefined;
 }
 
 } 
 
 var dirLine = -1;
 var objs = document.getElementsByName(dirObj.name);
 if(objs.length<2)return; 
 for(i=0;i<objs.length;i++){
 if(dirObj == objs[i]){ 
 dirLine = i;
 }
 }
 if(dirLine == -1 || curDragEndLine == dirLine){ return; }
 curDragEndLine = dirLine;
 var sl = curDragStartLine;
 var el = dirLine;
 if(sl>el){
 sl = dirLine;
 el = curDragStartLine;
 }
 
 for(var i=0;i<dragobjs.length;i++){
 if(i<sl||i>el){
 $(dragobjs[i]).css("border","");
 }else{
 $(dragobjs[i]).css("border","1px solid #ff00ff");
 }
 }
 }
 } ); 
});


var childOpDiv=null;
var curchildOpObj=null;
var curDragObj=null; 
var curDragStartLine=-1; 
var curDragEndLine=-1;
function childFocus(tobj){
 if(childOpDiv == null){
 $(document.body).append("<div id='childOpDiv'>"+
 "<b id='childOpDivBt' class='f-icon16'></b></div>"+
 "<div id='childOpDivPanal' >"+
 "</div>");
 childOpDiv = $("#childOpDiv");
 $("#childOpDivBt").mouseover( function() { 
 ctop =$(this).offset().top+$(this).height();
 cleft =$(this).offset().left;
 $("#childOpDivPanal").css("left",cleft+"px"); 
 $("#childOpDivPanal").css("top",ctop+"px"); 
 $("#childOpDivPanal").show();
 } ).mouseout(function() { $("#childOpDivPanal").hide(); }); 
 $("#childOpDivPanal").mouseover( function() { $(this).show();}).mouseout(function() { $(this).hide(); });
 }
 if(curchildOpObj ==null || curchildOpObj[0] != tobj[0]){ 
 curchildOpObj = tobj; 
 ftop =tobj.offset().top+1;
 fleft =tobj.offset().left+tobj.width()-15;
 childOpDiv.css("left",fleft+"px"); 
 childOpDiv.css("top",ftop+"px"); 
 
 inStr = "";
 if(tobj.attr("ondblclick") != null){ 
 inStr += "<li id='c_dbClick' class='f-icon16' title='选择'></li>";
 }
 if(tobj.attr("num") == "true"){ 
 inStr += "<li id='c_calc' class='f-icon16' title='计算器'></li>";
 }
 inStr += "<li id='c_addline' class='f-icon16' title='插入一行'></li>";
 inStr += "<li id='c_delline' class='f-icon16' title='删除本行'></li>";
 inStr += "<li id='c_upline' class='f-icon16' title='上移'></li>";
 inStr += "<li id='c_downline' class='f-icon16' title='下移'></li>";
 inStr += "<li id='c_stickline' class='f-icon16' title='复制整行'></li>";
 if(tobj.attr("ondblclick") == null){
 inStr += "<li id='c_dragcel' class='f-icon16' title='拖选复制单元格'></li>";
 }

 
 $("#childOpDivPanal").html(inStr);
 
 $("#c_upline").click(function(){
 tabn = curchildOpObj.attr("name");
 tabn = tabn.substring(0,tabn.indexOf("_"));
 $("#"+tabn+"TableDIV")[0].fix.moveRow(curGridRowNum.get(tabn),true);
 $("#childOpDivPanal").hide();
 childOpDiv.hide(); 
 curchildOpObj=null;
 });
 $("#c_downline").click(function(){
 tabn = curchildOpObj.attr("name");
 tabn = tabn.substring(0,tabn.indexOf("_"));
 $("#"+tabn+"TableDIV")[0].fix.moveRow(curGridRowNum.get(tabn),false);
 $("#childOpDivPanal").hide();
 childOpDiv.hide(); 
 curchildOpObj=null;
 });
 $("#c_dragcel").click(function(){
 curDragObj = tobj;
 $("#childOpDivPanal").hide();
 childOpDiv.hide(); 
 $(tobj).css("border","1px solid #ff00ff");
 curDragStartLine = curGridRowNum.curLine; 
 });
 $("#c_stickline").click(function(){
 asyncbox.prompt('复制整行','小提示:请输入插入的行号，默认复制到表格最后一行','','text',function(action,val){
 　　　if(action == 'ok'){
 line = 0;
 if(line != ""){
 　　　　　 if(!isInt(val)){ alert('请输入整数'); return;}
 line = parseInt(val); 
 }
 tabn = curchildOpObj.attr("name");
 tabn = tabn.substring(0,tabn.indexOf("_"));
 $("#"+tabn+"TableDIV")[0].fix.copyRow(curGridRowNum.get(tabn),line-1);
 　　　}
 　});
 $("#childOpDivPanal").hide();
 childOpDiv.hide(); 
 curchildOpObj=null;

 });
 $("#c_addline").click(function(){
 tabn = curchildOpObj.attr("name");
 tabn = tabn.substring(0,tabn.indexOf("_"));
 insertRowClick(tabn+"Table",curGridRowNum.get(tabn)+1); 
 $("#childOpDivPanal").hide();
 childOpDiv.hide(); 
 curchildOpObj=null;
 });
 
 $("#c_delline").click(function(){
 deleditGridRow(tobj[0]);
 $("#childOpDivPanal").hide(); 
 childOpDiv.hide(); 
 curchildOpObj=null; 
 });
 if(tobj.attr("ondblclick") != null){ 
 $("#c_dbClick").click(function(){
 if (document.createEvent) {
 evt = document.createEvent("MouseEvents"); 
 evt.initMouseEvent("dblclick", true, true, window, 
 0, 0, 0, 0, 0, false, false, false, false, 0, null); 
 tobj[0].dispatchEvent(evt); 
 } else if (tobj[0].fireEvent) {
 tobj[0].fireEvent('ondblclick'); 
 } 
 });
 }
 if(tobj.attr("num") == "true"){ 
 $("#c_calc").click(function(){
 var urls = "/common/calc.jsp"; 
 asyncbox.open({
 id : 'PopCalcdiv',
 title : '计算器',
 url : urls,
 width : 210,
 height : 260,
 top: 25 ,
 　　 btnsbar : jQuery.btn.OKCANCEL, 
 　　 callback : function(action,iframe){
 　　　　　if(action == 'ok'){
 　　　　　　　if(curchildOpObj != null && typeof(curchildOpObj) != "undefined"){
 val = iframe.document.getElementById("lcd").value;
 if(val.substring(val.length-1)=="."){
 val = val.substring(0,val.length-1);
 } 
 for(i=0;i<fieldDigit.length;i++){
 if(fieldDigit[i].name ==curchildOpObj.attr("name")){
 val=f(val,fieldDigit[i].digit);
 } 
 } 
 curchildOpObj.val(val);
 }
 　　　　　}
 　　　}
 
 　 }); 
 });
 }
 }
 
 childOpDiv.show();
}

function childDbClick(){
 alert(tobj);
}
function childBlur(){
 if(childOpDiv != null){
 childOpDiv.hide();
 }
}

function cFireEvent(fn,defaultValue){
 var z=n(fn); 
 for(var i = 0;i<z.length;i++){ 
 if(z[i].value.length>0){ 
 if((defaultValue.length>0&&z[i].value!=defaultValue) ||defaultValue.length==0){
 $(z[i]).change();
 }
 }
 }
}

function loadFireEvent()
{ 
 }

function auditing(){
 var varReturn = window.showModalDialog("/OAFileWorkFlowAction.do?operation=94&type=submit&tableName=dlddl&workFlowNode="+workFlowNode,
 winObj,"dialogWidth:600px;dialogHeight:400px;center:yes;help:no;resizable:no;status:no;scroll:no;");
 if(typeof(varReturn)=="undefined")return false ;
 var strFile = varReturn.split("||") ;
 m("varWakeUp").value =strFile[0] ;
 m("varNextNode").value =strFile[1] ;
 m("varCheckPersons").value =strFile[2] ;
 m("varAttitude").value =strFile[3] ;
 m("operation").value ="79" ;
 m("optionType").value ="auditing" ;
 return true ;
}
function overFlow(){
 var varReturn = window.showModalDialog("/vm/oa/fileflow/repealFile.jsp?type=over&&wakeup="+strWakeUp,
 winObj,"dialogWidth:600px;dialogHeight:400px;center:yes;help:no;resizable:no;status:no;scroll:no;");
 if(typeof(varReturn)=="undefined")return false ;
 var strFile = varReturn.split("||") ;
 m("varWakeUp").value =strFile[0] ;
 m("varAttitude").value =strFile[1] ;
 m("operation").value ="79" ;
 m("optionType").value ="overFlow" ;
}

function repelFile(){
 var varReturn = window.showModalDialog("/vm/oa/fileflow/repealFile.jsp?wakeup="+strWakeUp,
 winObj,"dialogWidth:600px;dialogHeight:400px;center:yes;help:no;resizable:no;status:no;scroll:no;");
 if(typeof(varReturn)=="undefined")return false ;
 var strFile = varReturn.split("||") ;
 m("varWakeUp").value =strFile[0] ;
 m("varAttitude").value =strFile[1] ;
 m("operation").value ="79" ;
 m("optionType").value ="repeal" ;
}

function backAuditing(){
 var varReturn = window.showModalDialog("/OAFileWorkFlowAction.do?operation=94&type=back&keyId="+valueId+"&tableName=dlddl&workFlowNode="+workFlowNode,
 winObj,"dialogWidth:600px;dialogHeight:400px;center:yes;help:no;resizable:no;status:no;scroll:no;");
 if(typeof(varReturn)=="undefined")return false ;
 var strFile = varReturn.split("||") ;
 m("varWakeUp").value = strFile[0] ;
 m("varNextNode").value = strFile[1] ;
 m("varCheckPersons").value = strFile[2] ;
 m("varAttitude").value = strFile[3] ;
 m("operation").value = "79" ;
 m("optionType").value = "returnAuditing" ;
}

function auditLogList(){
 var file = window.showModalDialog("/UserFunctionAction.do?tableName=dlddl&keyId="+valueId+"&checkTab=Y&optionType=Auditing&type=audtiting&operation=5&fromPage=yes",
 winObj,"dialogWidth:800px;dialogHeight:400px;center:yes;help:no;resizable:no;status:no;scroll:no;");
}
function selectDispense(){
 var varPerson = window.showModalDialog("/vm/oa/fileflow/selectReadPerson.jsp?keyId="+valueId+"&type=dispense&wakeup="+strWakeUp,
 winObj,"dialogWidth:700px;dialogHeight:300px;center:yes;help:no;resizable:no;status:no;scroll:no;") ;
}
function selectReadPerson(){
 var varPerson = window.showModalDialog("/vm/oa/fileflow/selectReadPerson.jsp?keyId="+valueId+"&tableName=dlddl&wakeup="+strWakeUp,
 winObj,"dialogWidth:700px;dialogHeight:300px;center:yes;help:no;resizable:no;status:no;scroll:no;") ;
}
function newSave()
{ 
 form.id.value="";
 operation=1 ;
 if(beforSubmit(document.form)) {window.save=true; document.form.submit(); }
}

function draftSave()
{ 
 operation=1;
 if(beforSubmit(document.form)) {
 form.button.value="quoteDraft"; 
 window.save=true;
 form.operation.value=1 ;
 document.form.submit(); 
 }
}

function copyAdd(){ 
 if(beforSubmit(document.form)) {
 form.id.value="";
 form.button.value="copyAdd" ;
 window.save=true; 
 form.operation.value=1 ;
 document.form.submit(); 
 }

}

var gridDatas = new Array();
var gridDataNum = 0;







function getTotalCount(){
 var total_count = 0;
 var qtyInputType = m("QtyInputType") ;
 var varTableName = document.getElementById("tableName").value ;
 if(varTableName == "tblSalesOutStock" && qtyInputType!=null && qtyInputType.value=="Total"){
 }else{
 }
 return total_count;
}
function setTotalCount(count){
 var flag=false;
 
 if(event!=null && event.srcElement!=null && typeof(event.srcElement)!="undefined"){
 }
 var qtyInputType = m("QtyInputType") ;
 if(flag && (typeof(qtyInputType)=="undefined" || qtyInputType.value=="Detail")){
 }
}

function setRowTotal(objTd){
 var qtyInputType = m("QtyInputType") ;
 var headCount = 1 ;
 if(objTd.parentElement.parentElement.parentElement.parentElement.tHead.rows.length>1){
 headCount = 2 ;
 }
 }
function totalA(obj)
{
 setTotalCount(getTotalCount());
 setRowTotal(obj); 
 copyFields(null,null,null,obj);
}
function totalB(obj)
{
 setTotalCount(getTotalCount());
 setRowTotal(obj);
}
function initCountCal(){
}


function copyFields2(index,order,tableName,copyAll){
 if(index==0)return ;
 var copyFields;
 }

function copyFields(trigType,order,copyAll,obj){

var index = curGridRowNum.curLine ;

if(trigType=="dbClick" && index>0){
 var copyFields;
 }
}


var goodsCodeLogicValidate =new Array();




function setSeqQtyReadOnly(tableN,row,checked){
}


var __statKey =0;
function execStat(){
 __statKey ++;
 setTimeout(function(){
 if(__statKey==1){ 
 if("detail" == detail) 
 execDetailStat2(); 
 else execStat2(); 
 } __statKey -= 1;},200);
}
function execStatOne(fn,digit){
 var statmi = n(fn); 
 var totalstat = 0;
 for(i=0;i<statmi.length;i++){
 if(isFloat(statmi[i].value)){
 totalstat = totalstat + Number(statmi[i].value);
 }
 }
 var mistat = m(fn+'_total'); 
 if(mistat!=undefined){
 mistat.innerHTML = f(totalstat,digit);
 }
}

function execStat2(){
setTotalCount(getTotalCount());
}
function execDetStatOne(fn,digit){
 var mistat = m(fn+'_total'); 
 if(mistat!=undefined){
 sumc = mistat.getAttribute("sum");
 if(sumc != null && typeof(sumc) != "undefined"){
 mistat.innerHTML = f(Number(sumc),digit);
 }
 }
}
function execDetailStat2(){
}

var urlstr;
var obj ;
var field;
function openSelect(urlstr,obj,field){
 urlstr +="&isMain=true";
 width =800;
 height=470;
 if(width > document.documentElement.clientWidth -50){
 width = document.documentElement.clientWidth -50;
 }
 if(height > document.documentElement.clientHeight -50){
 height = document.documentElement.clientHeight -50;
 }

 window.urlstr = urlstr;
 window.obj = obj;
 window.field = field;
 if(typeof(window.parent.$("#bottomFrame").attr("id"))!="undefined"){
 asyncbox = parent.asyncbox;
 }
 if(urlstr.indexOf("&url=@URL:")==-1){
 urlstr = encodeURI(urlstr) ;
 }
 urlstr = encodeURI(urlstr) ;
 if(urlstr.indexOf("+")!=-1){
 urlstr = urlstr.replaceAll("\\+",encodeURIComponent("+")) ;
 }
 urlstr = urlstr.replaceAll("#","%23") ;
 urlstr += "&src=menu&MOID=86ed1cd2_1312221558520651057&MOOP=add&popupWin=MainPopup";
 asyncbox.open({id:'MainPopup',title:'',url:urlstr,width:width,height:height});
 
 $("#MainPopup_content").height($("#MainPopup").height()-25); 
}

var thisField;
var current;

function openChildSelect(urlstr,obj,field,thisField,current){
 if(typeof(window.parent.$("#bottomFrame").attr("id"))!="undefined"){
 asyncbox = parent.asyncbox;
 }
 urlstr +="&isMain=false";
 window.obj = obj;
 window.urlstr = urlstr;
 window.field = field; 
 window.thisField = thisField;
 window.current = current;
 
 obj.t = "t"; 
 var id = 0; 
 var mi = n(thisField); 
 for(i=0;i<mi.length;i++){
 if(mi[i].t == "t"){
 id = i;
 obj.t = ""; 
 } 
 }
 var url2=urlstr.substr(urlstr.indexOf('?')+1);
 url2='&src=menu&MOID=86ed1cd2_1312221558520651057&MOOP=add&'+url2;
 urlstr=urlstr.substring(0,urlstr.indexOf('?')+1)+url2;
 
 url2='UtilServlet?operation=savePopURL&'+url2;
 url2=encodeURI(url2);
 url2=encodeURI(url2);
 var flag=false;
 if("tblSalesOutStockDet_SQty1"==field || "tblAllotDet_Qty1"==field || "tblAllotChangeDet_Qty1"==field || "tblBuyOutStockDet_Qty1"==field || "tblOtherOutDet_Qty1"==field || "tblAdjustPriceDet_Qty1"==field || "tblProDrawManysDet_Qty1"==field || "tblProCheckManysDet_Qty1"==field || "tblClearBackMaterialDet_Qty1"==field || "tblInProductBPDet_Qty1"==field || "tblOutMaterialsBPDet_Qty1"==field || "tblAddMaterialsDet_Qty1"==field || "tblEntrustGoodsDet_Qty1"==field || "tblRetCheckDet_Qty1"==field){
 urlstr += "&popType=sqty" ;
 }
 if(urlstr.indexOf("#")!=-1){
 urlstr=urlstr.replaceAll("#","%23") ;
 }else{
 urlstr=encodeURI(urlstr);
 }
 if(urlstr.indexOf("+")!=-1){
 urlstr = urlstr.replaceAll("\\+",encodeURIComponent("+")) ;
 }
 urlstr += "&popupWin=ChildPopup";
 if(url2.indexOf("#")!=-1){
 url2=url2.replaceAll("#","%23") ;
 }
 AjaxRequest(url2+"&time="+(new Date()).getTime()) ;
 
 asyncbox.open({id:'ChildPopup',title:'弹出窗口',url:urlstr,width:1000,height:490}); 
 
 
}

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
 uploadDiv.style.cssText = "position:absolute;top:10px;left:30px;width:600px;height:300px;display:block;z-index:100";
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
 var imgstr = "<li style='background:url();' id='uploadfile_"+str+"'><input type=hidden name="+hiddenName+" value='"+str+"'><div class='hImage'><a href=\"/ReadFile?fileName="+str+"&realName="+encodeURI(str)+"&tempFile=true&type=PIC\" target=\"_blank\"><img src=\"/ReadFile.jpg?fileName="+encodeURI(str)+"&realName="+encodeURI(str)+"&tempFile=true&type=PIC\" width=\"150\" border=\"0\"></a></div><div><em>"+str+"</em><a href='javascript:void(0);' style=\"cursor:pointer;\" onclick='deleteupload(\""+str+"\",\"true\",\"dlddl\",\"PIC\");'>删除</a></div></li>";
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
 var imgstr = "<li style='background:url();' id='uploadfile_"+str+"'><input type=hidden name="+hiddenName+" value='"+str+"'><div class='showAffix'>"+str+"</div><a class='showAffixDel' href='javascript:deleteupload(\""+str+"\",\"true\",\"dlddl\",\"AFFIX\");'>删除</a><a class='showAffixDown' href=\"/ReadFile?fileName="+encodeURI(str)+"&realName="+encodeURI(str)+"&tempFile=true&type=AFFIX\" target=_blank>下载</a></li>";
 ul.append(imgstr);
 }
 } 
 var attachUpload = document.getElementById("attachUpload");
 attachUpload.style.display="none";
}

function linkPIC(btnId){
 var buttonId = "picuploadul";
 var hiddenName = "uploadpic";
 if(btnId!="undefined" && btnId!=null){
 buttonId = "picuploadul_"+btnId;
 hiddenName = btnId;
 }
 var ul=$('#'+buttonId);
 
 asyncbox.prompt('请输入图片链接','','http://','text',function(action,val){
 　　　if(action == 'ok'){
 　　　　　str = val;
 var imgstr = "<li style='background:url();' id='uploadfile_"+str+"'><input type=hidden name="+hiddenName+" value='"+str+"'><div><a href=\""+str+"\" target=\"_blank\"><img src=\""+str+"\" width=\"150\" border=\"0\"></a></div><div><em>"+str+"</em><a style=\"cursor:pointer;\" href='javascript:void(0);' onclick='deleteupload(\""+str+"\",\"false\",\"dlddl\",\"PIC\");'>删除</a></div></li>";
 ul.append(imgstr);
 　　　}
 }); 
}

function upload(type,btnId){
 openAttachUpload(type,btnId);
 return; 
}
function openInputDate(obj)
{
 WdatePicker();
}
function openInputTime(obj)
{
 WdatePicker({lang:'zh_CN',dateFmt:'yyyy-MM-dd HH:mm:ss'});
}

function delFireEventOne(rowId,tableName,fn,ctn){
 var z=n(fn); 
 if(z.length>0 && tableName==ctn+"Table"){
 $(z[rowId]).change();
 }
}

function delFireEvent(rowId,tableName)
{
 }

var fieldDigit = new Array();
 
function beforSubmit(form){ 

 for(i=0;i<fieldDigit.length;i++){
 if(!validateDigit(fieldDigit[i].name,fieldDigit[i].dis,fieldDigit[i].digit,fieldDigit[i].isMain))return false; 
 }

 if(typeof(flowNodeid)!="undefined" && flowNodeid!=""){
 var checkPerson = document.getElementById("auditingCheckPersonIds"+flowNodeid).value ;
 if(checkPerson.length==0){
 alert(flowNodedisplay+"的审核人不能为空.") ;
 return false ;
 }
 } 
 
 submitBefore = false ;
 if(!validate(form))return false; 
 disableForm(form);
 if(existFlow=="exist" && detailFlow !="detailFlow"&&form.button.value!='deliverTo'){
 if("false"==form.OAWorkFlow.value){
 var strReturn = window.showModalDialog("/OAFileWorkFlowAction.do?operation=94&type=add&tableName=dlddl",
 winObj,"dialogWidth:600px;dialogHeight:350px;center:yes;help:no;resizable:no;status:no;scroll:no;");
 if(typeof(strReturn)=="undefined")return false ;
 var strFile = strReturn.split("||") ;
 m("varWakeUp").value=strFile[0] ;
 m("varNextNode").value=strFile[1] ;
 m("varCheckPersons").value=strFile[2] ;
 m("varAttitude").value=strFile[3] ;
 }
 }
 if(typeof(fromCRM)!="undefined" && fromCRM=="service"){
 var strContent = m("content") ;
 if(strContent.value.length==0){
 alert("分配内容不能为空");
 strContent.focus() ;
 return false ;
 }
 }
 if(typeof(top.jblockUI)!="undefined"){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 }
 if(typeof(operation) != "undefined"){
 form.operation.value= operation;
 }
 return true;
}
var varTableName;
function quoteDraft(tableName,moduleType){
 window.varTableName = tableName ;
 var urlstr = "/UserFunctionQueryAction.do?tableName="+tableName+"&src=menu&moduleType="+moduleType+"&parentTableName=&draftQuery=draftPop&popupWin=QuoteDraft";
 asyncbox.open({id:'QuoteDraft',title:'引用草稿',url:urlstr,width:870,height:470});
}

function exeQuoteDraft(varStr,moduleType){
 if(typeof(varStr)=="undefined")return ;
 window.location.href="/UserFunctionAction.do?tableName="+varTableName+"&operation=7&moduleType="+moduleType+"&parentTableName=&f_brother=&keyId="+varStr+"&saveDraft=quoteDraft&winCurIndex="+winCurIndex ; 
}

function subAdd(){
 form.button.value="saveAdd"; 
 if(beforSubmit(document.form)) {
 window.save=true; 
 document.form.submit();
 }
 return false ;
}
function saveDraft2(){
 operation=1;
 form.button.value="saveDraft"; 
 if(beforSubmit(document.form)) {
 window.save=true; 
 document.form.submit();
 }
 return false ;
}
function updateDraft2(){
 operation=2;
 form.button.value="saveDraft"; 
 if(beforSubmit(document.form)) {
 window.save=true; 
 document.form.submit();
 }
 return false ;
}

function printSave(){
 form.button.value="printSave";
 if(beforSubmit(document.form)) {
 window.save=true; 
 document.form.submit();
 }
 return false ; 
 
}
function printData(billId,reportNumber){ 
 if(printRight){
 if(confirm("是否打印单据?")){
 var strUrl = "/UserFunctionQueryAction.do?tableName=dlddl&reportNumber="+reportNumber+"&moduleType="+moduleType+"&operation=18&BillID="+billId+"&parentTableName=&printType=savePrint&winCurIndex="+winCurIndex ;
 asyncbox.open({id:'printPopup',title:'打印控件',url:strUrl,width:300,height:280,
 callback : function(action){
 if(action=="close"){
 location.href='/common/message.jsp';
 }
 }});
 enableForm(form);
 return ;
 }else{
 location.href='/common/message.jsp';
 }
 }
}
var reportNumber2;
var billId2;
function prints(billId,reportNumber){
 reportNumber2=reportNumber;
 billId2=billId
 var strUrl = "/UserFunctionQueryAction.do?tableName=dlddl&reportNumber="+reportNumber+"&moduleType="+moduleType+"&operation=18&BillID="+billId+"&parentTableName=&winCurIndex="+winCurIndex;
 asyncbox.open({id:'printPopup',title:'打印控件',url:strUrl,width:300,height:280});
 enableForm(form);
}

jQuery(window).keydown(function(event){
 if(event.ctrlKey && event.which == 83){
 event.preventDefault();
 return false;
 }else if(event.ctrlKey && event.which == 68){
 event.preventDefault();
 return false;
 }else{
 return true;
 }
});

function down(){
 if(event.ctrlKey&&event.keyCode==83){
 form.copySave.focus() ;
 $(form.copySave).click();
 event.keyCode=9;
 }else if(event.ctrlKey&&event.keyCode==68){
 if(typeof(form.saveAdd)=="undefined")return;
 form.saveAdd.focus() ;
 $(form.saveAdd).click();
 event.keyCode=9;
 }else if(event.ctrlKey&&event.keyCode==90){
 $(form.backList).click();
 event.keyCode=9;
 }else if(event.ctrlKey&&event.keyCode==187){
 var ele=document.activeElement;
 var trObj=ele.parentNode.parentNode.parentNode.parentNode.tHead.rows[0];
 $(trObj.cells[1].childNodes[0]).click();
 }else if(event.ctrlKey&&event.keyCode==189){
 var ele=document.activeElement;
 var trObj=ele.parentNode.parentNode;
 var trIndex=parseInt(trObj.rowIndex);
 var tbody=ele.parentNode.parentNode.parentNode;
 tbody.rows[trIndex].cells[1].firstChild.focus();
 $(trObj.cells[1]).click(); 
 }else if(event.ctrlKey&&event.keyCode==77){ 
 } 
}


function mainSelect2(urlstr,openUrl,obj,field,isChild)
{
 var dropdown=obj.dropdown;
 

 if(dropdown==undefined)
 {
 var tmpUrl = urlstr.substring(urlstr.indexOf("?")+1);
 var temp_par = tmpUrl.split("&");
 var data = {
 operation:"DropdownPopup",
 MOID:"86ed1cd2_1312221558520651057",
 MOOP:"add",
 selectField:obj.name,
 selectValue:obj.value
 };
 
 for(var i = 0; i<temp_par.length ;i++)
 {
 var temp_item =temp_par[i].split("=");
 jQuery(data).attr(temp_item[0],temp_item[1]);
 }
 
 dropdown = new dropDownSelect("t_"+obj.id+new Date().getTime(),data,obj);
 if(isChild == undefined || isChild == false)
 {
 dropdown.retFun2 = k_fillMainField;
 }else
 dropdown.retFun2 = k_fillChildField;
 dropdown.showData();
 return ;
 }
 
 if(event.keyCode == 38)
 {
 if(dropdown!=undefined)
 {
 dropdown.selectUp();
 }
 return ;
 }else if(event.keyCode==40)
 {
 if(dropdown!=undefined)
 {
 dropdown.selectDown();
 }
 return ;
 }else if(event.keyCode==13)
 {
 if(dropdown!=undefined)
 {
 dropdown.curValue();
 }
 return ;
 }else if(event.keyCode==27)
 {
 if(dropdown!=undefined)
 {
 dropdown.close();
 }
 return ;
 }


 dropdown.showData();
}

function k_fillMainField(a,b)
{
 dataBackIn("true",a,b,window);
}

function k_fillChildField(a,b)
{
 dataBackIn("false",a,b,window);
}

function dyGetPYM(url,obj,fileName){
 var tempUrl = url + "&chinese="+encodeURIComponent(obj.value);
 AjaxRequest(tempUrl);
 var pym = m(fileName+"PYM");
 pym.value = response;
}


function isCheckField(obj,fieldName){
 var name = m("tbl_"+obj.name).value; 
 if(obj.value != name){
 resetSubmit(fieldName); 
 }
}

function resetSubmit(fieldName){
 }
function moreLanguage(len,moreLanguagefield){
 moreLanguagefield.t = "t"; 
 var id = 0; 
 var mi = n(moreLanguagefield.name); 
 for(i=0;i<mi.length;i++){
 if(mi[i].t == "t"){
 id = i;
 moreLanguagefield.t = ""; 
 } 
 }
 var flan=moreLanguagefield.name.substr(0,moreLanguagefield.name.lastIndexOf("_"));
 var cf=n(flan);
 var url = "/common/moreLanguage.jsp?len="+len+"&str="+encodeURI(cf[id].value) ;
 url = url.replaceAll("#","%23") ;
 var str = window.showModalDialog(url,winObj,"dialogWidth=730px;dialogHeight=450px");
 if(typeof(str)=="undefined") return ;
 var strs=str.split(";");
 for(var i=0;i<strs.length;i++){
 var lanstr=strs[i].split(":");
 if(lanstr[0]=='zh_CN'){
 moreLanguagefield.value=lanstr[1];break;
 }
 }
 cf[id].value = str;
}

var moreLanguagefield;
function mainMoreLanguage(len,moreLanguagefield){
 window.moreLanguagefield = moreLanguagefield;
 var mlf = n(moreLanguagefield);
 var url = "/common/moreLanguage.jsp?popupWin=Language&len="+len+"&str="+encodeURI(mlf[0].value) ;
 url = url.replaceAll("#","%23") ;
 asyncbox.open({id:'Language',title:'多语言',url:url,width:530,height:300});
}

function fillLanguage(str){
 if(typeof(str)=="undefined"){return };
 var mlf = n(moreLanguagefield);
 mlf[0].value = str;
 var mf=n(moreLanguagefield+"_lan");
 if(str.length==0){
 mf[0].value= "" ;
 }else{
 var strs=str.split(";");
 for(var i=0;i<strs.length;i++){
 var lanstr=strs[i].split(":");
 if(lanstr[0]=='zh_CN'){
 mf[0].value=lanstr[1];break;
 }
 }
 }
}

function initSeqQtySet(){
 if(false){
 }
 }

function delOpation(fileName,popedomId){
 var formatName=m(fileName);
 if(formatName.selectedIndex==-1)
 {
 alert("请选择要移除的项.");
 }
 if(formatName.selectedIndex<0)return false ;
 var value = formatName.options[formatName.selectedIndex].value;
 var appcers = m(popedomId).value;
 appcers = appcers.replace(value+",","");
 m(popedomId).value =appcers
 formatName.options.remove(formatName.selectedIndex);
}

function showUserGroup(fieldName,fieldNameIds){
 var displayName=encodeURI("职员分组") ;
 var urlstr = '/UserFunctionAction.do?operation=22&src=menu&tableName=tblEmpGroup&selectName=SelectEmpGroup' ;
 var str = window.showModalDialog(urlstr+"&MOID=86ed1cd2_1312221558520651057&MOOP=add&LinkType=@URL:&displayName="+displayName,winObj,"dialogWidth=730px;dialogHeight=450px"); 
 if(typeof(str)=="undefined") return ;
 var employees = str.split("|") ;
 for(var j=0;j<employees.length;j++){
 var field = employees[j].split(";") ;
 if(field!=""){
 var existOption = m(fieldName).options;
 var length = existOption.length;
 var talg = false ;
 for(var i=0;i<length;i++)
 {
 if(existOption[i].value==field[0])
 {
 talg = true;
 }
 }
 if(!talg){
 m(fieldName).options.add(new Option(field[1],field[0]));
 m(fieldNameIds).value+=field[0]+",";
 }
 }
 }
}

function showEmployee(fieldName,fieldNameIds){
 var displayName=encodeURI("审核人") ;
 var urlstr = '/UserFunctionAction.do?operation=22&selectName=SelectSMSEmployee' ;
 var str = window.showModalDialog(urlstr+"&MOID=86ed1cd2_1312221558520651057&MOOP=query&LinkType=@URL:&displayName="+displayName,winObj,"dialogWidth=830px;dialogHeight=450px"); 
 if(typeof(str)=="undefined") return ;
 var employees = str.split("|") ;
 for(var j=0;j<employees.length;j++){
 var field = employees[j].split(";") ;
 if(field!=""){
 var existOption = m(fieldName).options;
 var length = existOption.length;
 var talg = false ;
 for(var i=0;i<length;i++)
 {
 if(existOption[i].value==field[0])
 {
 talg = true;
 }
 }
 if(!talg){
 m(fieldName).options.add(new Option(field[1],field[0]));
 m(fieldNameIds).value+=field[0]+",";
 }
 }
 }
}

function changeMoreLanguage(strName,strValue){
 var localeValue = m(strName).value ;
 var lanValue = m(strValue).value ;
 var locale = "zh_CN" ;
 var moreValue = "" ;
 if(lanValue.indexOf(localeValue)==-1){
 if(lanValue.indexOf(locale)!=-1){
 moreValue = lanValue.substring(0,lanValue.indexOf(locale))+lanValue.substring(lanValue.indexOf(";",lanValue.indexOf(locale))+1) ;
 }
 moreValue = moreValue+locale+":"+localeValue+";";
 m(strValue).value=moreValue ;
 }
}

function mdiwin(url,title){
 top.showreModule(title,url);
}

function openCRMSelect(selectName,displayName){
 displayName=encodeURI(displayName) ;
 var str = window.showModalDialog("/UserFunctionAction.do?selectName="+selectName+"&operation=22&MOID=86ed1cd2_1312221558520651057&MOOP=add&LinkType=@URL:&displayName="+displayName,"","dialogWidth=730px;dialogHeight=450px"); 
 if(typeof(str)!="undefined"){
 var mutli=str.split(";"); 
 m("taskPersonName").value = mutli[3] ;
 m("taskPerson").value = mutli[0] ;
 }
}
function dyGetCalculate(url,obj){
 if(obj.value.match(/^\s*$/)){
 if(event.keyCode==13){event.keyCode=9;};
 }else{
 var fieldName = "" ;
 if(typeof(obj.name)!="undefined" && obj.name.indexOf("_")!=-1){
 fieldName = obj.name ;
 }else{
 fieldName = "dlddl"+"_"+obj.name ;
 }
 var tempUrl = url + "&fieldName="+fieldName+"&value="+encodeURIComponent(obj.value);
 AjaxRequest(tempUrl);
 if(response=="error"){
 alert("表达式输入有误!只能输入数字,+,-,*(乘法),/(除法)及()运算符，请重试!");
 }else{
 obj.value = response;
 $(obj).change();
 if(event.keyCode==13){event.keyCode=9;};
 }
 }
}


function sendBillMsg(action){
 var popupUserIds = m("popedomUserIds").value ;
 var popedomDeptIds = m("popedomDeptIds").value ;
 var otherEmail = m("otherEmail").value ;
 var otherSMS = m("otherSMS").value ;
 var strurl = "/vm/classFunction/sendBillMsg.jsp?tableName=dlddl&action="+action
 +"&userIds="+encodeURI(popupUserIds)+"&deptIds="+encodeURI(popedomDeptIds)+"&otherEmail="+otherEmail+"&otherSMS="+otherSMS;
 asyncbox.open({id:'billMgsId',url:strurl,title:'发送消息',width:800,height:400,btnsbar:jQuery.btn.OKCANCEL, 
　　　 callback:function(action,opener){
　　　　　 if(action == 'ok'){
　　　　　　　 var returnValue = opener.sendBillMsg();
 if(typeof(returnValue)!="undefined" && returnValue!="false"){
 var strValue = returnValue.split("@koron@") ;
 m("wakeUpMode").value = strValue[0] ;
 m("wakeUpContent").value = strValue[1] ;
 m("popedomUserIds").value = strValue[2] ;
 m("popedomDeptIds").value = strValue[3] ;
 
 
 m("otherEmail").value = strValue[4] ;
 m("otherSMS").value = strValue[5] ;
 }
 m("smsType").value = "sms" ;
　　　　　 }
　　　 }
　 });
}


function telCall()
{
 var str = window.showModalDialog("/TelAction.do?operator=callTel&from=dlddl&keyId="+valueId,"","dialogWidth=600px;dialogHeight=350px"); 
}

function checkKeyword(fieldName,definedId){
 document.getElementById("ButtonType").value = "keyWord" ;
 var fieldValue = document.getElementById(fieldName).value ;
 if(fieldValue.length==0){
 alert("简称不能为空") ;
 return ;
 }
 form.checkFieldName.value = fieldValue;
 form.defineName.value=definedId;
 form.operation.value=25;
 form.submit();
}


function m(value){
 return document.getElementById(value) ;
}
function n(value){
 if(document.getElementsByName(value).length==0){
 return document.getElementsByName("conver");
 }
 return document.getElementsByName(value) ;
}
function nn(value,row){ 
 if(document.getElementsByName(value).length==0){ 
 return document.getElementsByName("conver"); 
 }
 var eob = document.getElementsByName(value) ;
 if(eob.length <=row){ 
 alert("公式错误，rn行号大于行数");
 }else{ 
 return Number(eob[row].value);
 }
}
function r(num,fieldName){
 var value= tblName[num]+"_"+fieldName;
 return document.getElementsByName(value)
}
function rn(num,fieldName,row){
 var value= tblName[num]+"_"+fieldName;
 var eob = document.getElementsByName(value);
 if(eob.length <=row){
 alert("公式错误，rn行号大于行数"+value+":"+row);
 }else{
 return Number(eob[row].value);
 }
}
function v(num,fieldName,pos){
 var value= tblName[num]+"_"+fieldName;
 return document.getElementsByName(value)[pos].value;
}
function o(num,fieldName,pos){
 var value= tblName[num]+"_"+fieldName;
 return document.getElementsByName(value)[pos];
}

function sm(fieldName){
 var st=n(fieldName); 
 var retv=0;
 for(i = 0;i<st.length;i++){ 
 if(st[i].value.length>0 && isFloat(st[i].value) ){ 
 retv += parseFloat(st[i].value); 
 }
 }
 return retv;
}

function autoBalance(type){
 
 if(type=="RefReceive"){
 var CompanyCode = m('CompanyCode').value;
 var ExeBalAmt = m('ExeBalAmt').value;
 var AcceptTypeID = m('AcceptTypeID').value;
 if(CompanyCode.length==0){
 alert('请输入客户'); 
 return;
 }
 if(isNaN(ExeBalAmt)){
 alert('请输入收款金额且收款金额必须大于0');
 return;
 }
 if(ExeBalAmt<=0){
 alert('请输入收款金额且收款金额必须大于0');

 return;
 }
 var billNo = m("BillNo").value;
 var str="/RefPay?type=receive&CompanyCode="+CompanyCode+"&money="+ExeBalAmt+"&AcceptTypeID="+AcceptTypeID+"&billNo="+billNo;
 AjaxRequest(str);
 var value = response;
 
 if(value!="no response text" && value.length>0){
 for(var l=0;l<gridDatas.length;l++){
 var checkItem = gridDatas[l].cols[0];
 var items = document.getElementsByName(checkItem.name);
 var table=checkItem.name.substring(0,checkItem.name.indexOf("_"));
 var fieldName = checkItem.name.substring(checkItem.name.indexOf("_")+1);
 if(table == "tblSaleReceiveDet"){
 var saveField2=n("tblSaleReceiveDet_RefbillNo");
 $(saveField2[items.length-1]).focus();
 $(saveField2[items.length-1]).click();
 var leng = items.length;
 for(var j =0;j<leng;j++){
 var saveField=n("tblSaleReceiveDet_RefbillNo");
 $(saveField[0]).focus();
 $(saveField[0]).click();
 var ele=document.activeElement;
 var trObj=ele.parentNode.parentNode;
 var trIndex=parseInt(trObj.rowIndex);
 
 var tbody=ele.parentNode.parentNode.parentNode;
 $(tbody.rows[trIndex].cells[1].firstChild).focus();
 $(trObj.cells[1]).click(); 
 }
 }
 }
 
 var vs = value.split('|');
 var inde = 0;
 for(var i = 0;i<vs.length;i++){
 var v =vs[i];
 var vo = v.split(',');
 var saveField=n("tblSaleReceiveDet_RefbillNo"); saveField[i].value = vo[0]; 
 var saveField=n("tblSaleReceiveDet_BillAmt"); saveField[i].value = vo[1]; 
 var saveField=n("tblSaleReceiveDet_SettledAmt");saveField[i].value = vo[2]; 
 var saveField=n("tblSaleReceiveDet_BackAmt"); saveField[i].value = vo[3]; 
 var saveField=n("tblSaleReceiveDet_WexeBalAmt");saveField[i].value = vo[4]; 
 var saveField=n("tblSaleReceiveDet_RefbillID"); saveField[i].value = vo[5]; 
 var saveField=n("tblSaleReceiveDet_SalesOrderNo");saveField[i].value = vo[6]; 
 var saveField=n("tblSaleReceiveDet_ReceiveBillType");saveField[i].value = vo[7]; 
 var saveField=n("tblSaleReceiveDet_BillName"); saveField[i].value = vo[8]; 
 var saveField=n("tblSaleReceiveDet_SalesOrderID");saveField[i].value = vo[9]; 
 var saveField=n("tblSaleReceiveDet_ExeBalAmt"); saveField[i].value = vo[10];
 var saveField=n("tblSaleReceiveDet_Remark"); saveField[i].value = vo[11];
 $(saveField[i]).focus();
 $(saveField[i]).click();
 inde=i;
 }
 inde++;
 
 var saveField=n("tblSaleReceiveDet_ExeBalAmt");
 $(saveField[inde]).focus();
 $(saveField[inde]).click();
 
 var ele=document.activeElement;
 var trObj=ele.parentNode.parentNode;
 var trIndex=parseInt(trObj.rowIndex);
 
 var tbody=ele.parentNode.parentNode.parentNode;
 tbody.rows[trIndex].cells[1].firstChild.focus();
 $(trObj.cells[1]).click(); 
 
 }
 }else if(type=="RefPay"){
 var ViewCompanyTotal_ComFullName = m('ViewCompanyTotal_ComFullName').value;
 var CompanyCode = m('CompanyCode').value;
 var SettleAmt = m('SettleAmt').value;
 var PaytypeID = m('PaytypeID').value;
 if(ViewCompanyTotal_ComFullName.length==0){
 alert('请输入客户');
 return;
 }
 if(isNaN(SettleAmt)){
 alert('请输入付款金额且付款金额必须大于0');
 return;
 }
 if(SettleAmt<=0){
 alert('请输入付款金额且付款金额必须大于0');
 return;
 }
 var billNo = m("BillNo").value;
 var str="/RefPay?type=pay&companyCode="+CompanyCode+"&ViewCompanyTotal_ComFullName="+ViewCompanyTotal_ComFullName+"&money="+SettleAmt+"&PaytypeID="+PaytypeID+"&billNo="+billNo;
 AjaxRequest(str);
 var value = response;
 
 if(value!="no response text" && value.length>0){
 for(var l=0;l<gridDatas.length;l++){
 var checkItem = gridDatas[l].cols[0];
 var items = document.getElementsByName(checkItem.name);
 var table=checkItem.name.substring(0,checkItem.name.indexOf("_"));
 var fieldName = checkItem.name.substring(checkItem.name.indexOf("_")+1);
 if(table == "tblPaydet"){
 var saveField2=n("tblPayDet_RefbillNo");
 $(saveField2[items.length-1]).focus();
 $(saveField2[items.length-1]).click();
 var leng = items.length;
 for(var j =0;j<leng;j++){
 var saveField=n("tblPayDet_RefbillNo");
 $(saveField[0]).focus();
 $(saveField[0]).click();
 var ele=document.activeElement;
 var trObj=ele.parentNode.parentNode;
 var trIndex=(trObj.rowIndex);
 
 var tbody=ele.parentNode.parentNode.parentNode;
 $(tbody.rows[trIndex].cells[1].firstChild).focus();
 $(trObj.cells[1]).click(); 
 }
 }
 }
 
 var vs = value.split('|');
 var inde = 0;
 for(var i = 0;i<vs.length;i++){
 var v =vs[i];
 var vo = v.split(',');
 var saveField=n("tblPayDet_RefbillNo"); saveField[i].value = vo[0]; 
 var saveField=n("tblPayDet_BillAmt"); saveField[i].value = vo[1]; 
 var saveField=n("tblPayDet_SettledAmt");saveField[i].value = vo[2]; 
 var saveField=n("tblPayDet_BackAmt"); saveField[i].value = vo[3]; 
 var saveField=n("tblPayDet_WexeBalAmt");saveField[i].value = vo[4]; 
 var saveField=n("tblPayDet_RefbillID"); saveField[i].value = vo[5]; 
 var saveField=n("tblPayDet_BuyOrderNo");saveField[i].value = vo[6]; 
 var saveField=n("tblPayDet_PayBillType");saveField[i].value = vo[7]; 
 var saveField=n("tblPayDet_BillName"); saveField[i].value = vo[8]; 
 var saveField=n("tblPayDet_BuyOrderID");saveField[i].value = vo[9]; 
 var saveField=n("tblPaydet_ExeBalAmt"); saveField[i].value = vo[10];
 var saveField=n("tblPaydet_Remark"); saveField[i].value = vo[11];
 $(saveField[i]).focus();
 $(saveField[i]).click();
 inde=i;
 }
 inde++;
 var saveField=n("tblPaydet_ExeBalAmt");
 $(saveField[inde]).focus();
 $(saveField[inde]).click();
 
 var ele=document.activeElement;
 var trObj=ele.parentNode.parentNode;
 var trIndex=parseInt(trObj.rowIndex);
 
 var tbody=ele.parentNode.parentNode.parentNode;
 tbody.rows[trIndex].cells[1].firstChild.focus();
 $(trObj.cells[1]).click(); 
 
 }
 }
}



function toStockDistributing(reportName){
 
 var goodsFullName = "";
 
 for(var l=0;l<gridDatas.length;l++){
 for(var i=0;i<gridDatas[l].cols.length;i++){
 var checkItem = gridDatas[l].cols[i];
 var items = document.getElementsByName(checkItem.name);
 var table=checkItem.name.substring(0,checkItem.name.indexOf("_"));
 var fieldName = checkItem.name.substring(checkItem.name.indexOf("_")+1);
 if(fieldName.indexOf("_" != -1)){
 fieldName = fieldName.substring(fieldName.indexOf("_")+1);
 }
 if(fieldName=='GoodsFullName'){
 for(var j =0;j<items.length;j++){
 if(items[j].value.length > 0){
 if(goodsFullName!=""){
 goodsFullName = goodsFullName+",";
 }
 goodsFullName = goodsFullName + items[j].value;
 } 
 }
 }
 }
 }
 goodsFullName =encodeURI(goodsFullName).replaceAll("#","%23") ;
 window.open("/ReportDataAction.do?reportNumber="+reportName+"&src=menu&LinkType=@URL&selectType=endClass&query=true&GoodsFullName="+goodsFullName);
}



function cut(){
 var billTableName = m('billTableName').value;
 var str = "";
 if(billTableName == ""){
 alert('请先选择表');
 return;
 }else{
 str = window.showModalDialog("/common/fieldSelectSMS.jsp?tableName="+billTableName);
 }
 if(str == undefined || str == ""){
 return;
 }
 m('ModelContent').innerHTML = m('ModelContent').innerHTML + "["+str+"]";
}

function initMainCaculate(){
 
}

function getrp(obj,field){
 obj.t = "t"; 
 var p = 0; 
 var mi = n(field); 
 for(i=0;i<mi.length;i++){
 if(mi[i].t == "t"){
 p = i;
 obj.t = ""; 
 } 
 } 
 return p;
}

function initCalculate(){
}

var childCount=0;
var minValue = -9999999999;
var maxValue = 9999999999;
 
 
function sendSMS(){
 var keyIds = m("id").value ;
 var left = 250; 
 var top = screen.height/2 - 200;
 var str = window.open("/ClientAction.do?type=sendPrepare&msgType=sms&keyId="+keyIds,null,'menubar=no,toolbar=no,scrollbars=no,loaction=no,status=yes,resizable=no,width=750,height=400,left='+left+',top='+top);
}

function sendEmail(){
 var keyIds = m("id").value ;
 var left = 250; 
 var top = screen.height/2 - 200 ;
 var str = window.open("/ClientAction.do?type=sendPrepare&msgType=email&keyId="+keyIds,null,'menubar=no,toolbar=no,scrollbars=no,loaction=no,status=yes,resizable=no,width=750,height=400,left='+left+',top='+top);
}

function G(id){
 return document.getElementById(id);
}

function showDivCustomSetTable(){
 var moduleType = document.getElementById("moduleType").value ;
 var strUrl = "/UserFunctionAction.do?tableName=dlddl&moduleType="+moduleType+"&winCurIndex="+winCurIndex+"&f_brother=&noback="+noback+"&parentCode=&operation=6&parentTableName=&configType=colConfig" ;
 asyncbox.open({id:'customPopup',title:'列配置',url:strUrl,width:700,height:430});
}

function fillColConfig(returnValue){
 if(typeof(returnValue)!="undefined"){
 var strType = returnValue.split("@@") ;
 if("colConfig"==strType[0]){
 var tables = "dlddl,".split(",") ;
 var strCols = strType[1] ;
 for(var i=0;i<tables.length;i++){
 if(tables[i].length>0){
 strCols = strCols.replaceAll(tables[i]+":","a"+i+":") ;
 }
 }
 var strURL = "/UtilServlet?operation=colconfig&tableName=dlddl&allTableName=dlddl,&colType=bill&colSelect="+ strCols;
 }else if("defaultConfig"==strType[0]){
 var strURL = "/UtilServlet?operation=defaultConfig&tableName=dlddl&allTableName=&colType=bill" ;
 }else if("colWidth"==strType[0]){
 var strURL = "/UtilServlet?operation=colconfig&colType=colWidth&colSelect="+strType[1] ;
 }else if("defaultWidth"==strType[0]){
 var strURL = "/UtilServlet?operation=defaultConfig&tableName=dlddl&allTableName=&colType=colWidth" ;
 }else if(strType[0]=="colNameSet"){
 mdiwin(strType[1],'列名设置') ;
 return ;
 }
 AjaxRequest(strURL);
 document.location.href = document.location.href ;
 }
}

function showDept(){
 var urlstr = '/UserFunctionAction.do?operation=22&tableName=tblDepartment&fieldName=DeptFullName&selectName=SelectDepartmentMes' ;
 var str = window.showModalDialog(urlstr+"&MOID=86ed1cd2_1312221558520651057&MOOP=add",winObj,"dialogWidth=750px;dialogHeight=450px"); 
 if(typeof(str)=="undefined") return ;
 var depts = str.split("|") ;
 for(var j=0;j<depts.length;j++){
 var field = depts[j].split(";") ;
 var existOption = m("formatDeptName").options;
 var length = existOption.length;
 var talg = false ;
 for(var i=0;i<length;i++)
 {
 if(existOption[i].value==field[0])
 {
 talg = true;
 }
 }
 if(!talg){
 m("formatDeptName").options.add(new Option(field[1],field[0]));
 m("popedomDeptIds").value+=field[0]+",";
 }
 }
}

function showEmployee(fieldName,fieldNameIds){
 var urlstr = "/UserFunctionAction.do?operation=22&tableName=tblEmployee&selectName=SelectSMSEmployee" ;
 var str = window.showModalDialog(urlstr+"&MOID=86ed1cd2_1312221558520651057&MOOP=query&LinkType=@URL:",winObj,"dialogWidth=750px;dialogHeight=450px"); 
 if(typeof(str)=="undefined") return ;
 var employees = str.split("|") ;
 for(var j=0;j<employees.length;j++){
 var field = employees[j].split(";") ;
 if(field!=""){
 var existOption = m(fieldName).options;
 var length = existOption.length;
 var talg = false ;
 for(var i=0;i<length;i++)
 {
 if(existOption[i].value==field[0])
 {
 talg = true;
 }
 }
 if(!talg){
 m(fieldName).options.add(new Option(field[1],field[0]));
 m(fieldNameIds).value+=field[0]+",";
 }
 }
 }
}

function delOpation(fileName,popedomId){
 var formatName=m(fileName);
 if(formatName.selectedIndex==-1){
 alert("请选择要移除的项.");
 }
 if(formatName.selectedIndex<0)return false ;
 var value = formatName.options[formatName.selectedIndex].value;
 var appcers = m(popedomId).value;
 appcers = appcers.replace(value+",","");
 m(popedomId).value =appcers
 formatName.options.remove(formatName.selectedIndex);
}

function compareDate(startDatetime,hour,minute){
 var sdStr = startDatetime.split("-") ;
 var sd = new Date(sdStr[0],sdStr[1]-1,sdStr[2], hour, minute,"00");
 var diff = sd - new Date() ;
 if (diff < 0){
 alert("提醒时间不能少于当前系统时间");
 return false;
 }
 return true;
}

function checkAlertSet(){
 if(m("alertDivId")==null)return true;
 var alertDiv = m("alertDivId").style.display ;
 if("none"==alertDiv){return true ;}
 var alertType = n("alertType") ;
 var hasSelect = false ;
 for(var i=0;i<alertType.length;i++){
 if(alertType[i].checked){
 hasSelect = true ;
 break ;
 }
 }
 if(!hasSelect){
 alert("请选择至少一种提醒方式") ;
 return false ;
 }
 var alertDate = m("alertDate").value ;
 if(alertDate.length==0){
 alert("提醒时间不能为空") ;
 return false ;
 }
 var alertHour = m("alertHour").value ;
 var alertMinute = m("alertMinute").value ;
 if(!compareDate(alertDate,alertHour,alertMinute)){
 return false;
 }
 var alertContent = m("alertContent").value ;
 if(alertContent.length==0){
 alert("提醒内容不能为空") ;
 return false ;
 }
 var popedomUserIds = m("popedomUserIds") ;
 var popedomDeptIds = m("popedomDeptIds") ;

 if(typeof(popedomUserIds)!="undefined" && popedomUserIds!=null 
 && popedomUserIds.value.length==0 && popedomDeptIds.value.length==0){
 alert("请选择提醒对象") ;
 return false ;
 }
 var loopTime = m("loopTime").value ;
 if(!isInt(loopTime)){
 alert("每隔多少(天,周,月,年)提醒一次必须是整数") ;
 return false ;
 }
 var hasEndDate = m("endDateId") ;
 if(hasEndDate.checked){
 var endDate = m("endDate").value ;
 if(endDate.length==0){
 alert("结束日期不能为空") ;
 return false ;
 }
 }
 return true ;
}

function attention(attention,tableName,empId,keyId){
 if(attention=="attention"){
 document.location.href="/UserFunctionQueryAction.do?tableName="+tableName+"&keyId="+keyId+"&operation=4&optionType="+attention+"&empId="+empId;
 }else{
 document.location.href="/UserFunctionQueryAction.do?tableName="+tableName+"&empId="+empId+"&keyId="+keyId+"&operation=4&optionType="+attention;
 }
}

var A_detailCount = 0;
var A_rowNum = -1;
var A_detailQty = 0;

function inputDetail(tableName){
 var goodsCode = n(tableName+"_GoodsCode") ;
 var hasGoods = false ;
 for(var a=0;typeof(goodsCode)!="undefined" && a<goodsCode.length;a++){
 if(goodsCode[a].value.length>0){
 hasGoods = true ;
 break ;
 }
 }
 if(!hasGoods){
 alert("商品不能为空！") ;
 return ;
 }
 A_detailQty = m("detailQty").value ;
 A_detailCount = m("detailCount").value ;
 if(A_detailCount.length==0 || A_detailQty.length==0){
 alert('定码匹数和定码数量不能为空');
 return false ;
 }
 if(A_detailCount.trim().length>0){
 if(!isInt2(A_detailCount.trim())){
 alert("定码匹数必须是大于零的整数！") ;
 m("detailCount").focus();
 return ;
 }
 }
 var digit = 0;
 if(A_detailQty.trim().length>0){
 if(!gtZero(A_detailQty.trim())){
 alert("定码数量必须是大于零！") ;
 m("detailQty").focus();
 return ;
 }
 if(!validateDigit('detailQty','定码数量',digit,true)){
 m("detailQty").focus();
 return ;
 }
 }
 
 A_rowNum = -1 ;
 var sqty1 = n(tableName+"_Qty1") ;
 for(var j=0;j<sqty1.length;j++){
 if(sqty1[j].value.length==0){
 A_rowNum = j ;
 var trObj = sqty1[A_rowNum].parentNode.parentNode.parentNode.parentNode.tHead.rows[0];
 $(trObj.cells[1].childNodes[0]).click();
 break;
 }
 }
 if(A_rowNum==-1){
 A_rowNum = sqty1.length ;
 var trObj = sqty1[parseInt(sqty1.length-1)].parentNode.parentNode.parentNode.parentNode.tHead.rows[0];
 $(trObj.cells[1].childNodes[0]).click();
 }
 if(typeof(top.jblockUI)!="undefined"){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 } 
 recInputDetail(tableName);
}


function recInputDetail(tableName){
 var seqNum = 0 ;
 for(var i=0;i<10 && i<A_detailCount;i++){
 seqNum++ ; 
 
 n(tableName+"_Qty"+seqNum)[A_rowNum].value = A_detailQty ; 
 $(n(tableName+"_Qty"+seqNum)[A_rowNum]).change();
 }
 $(n(tableName+"_Qty1")[A_rowNum]).change();
 $(n(tableName+"_Qty1")[A_rowNum]).blur();
 copyFields2(A_rowNum,0,tableName,"copyAll");
 if(seqNum==10){
 var trObj = n(tableName+"_Qty1")[A_rowNum].parentNode.parentNode.parentNode.parentNode.tHead.rows[0];
 $(trObj.cells[1].childNodes[0]).click(); 
 A_rowNum++;
 seqNum=0;
 }
 
 if(A_detailCount >10){
 A_detailCount = A_detailCount -10;
 setTimeout("recInputDetail('"+tableName+"')",10);
 }else{
 m("detailQty").value = "" ;
 m("detailCount").value = "" ;
 m("detailQty").focus();
 top.junblockUI();
 }
}

function seqInput(){
 var obj = m("FunctionField") ;
 if(obj == null){
 alert("序列号扫描字段的名字必须为FunctionField") ;
 return ;
 }
 if(obj.value.trim().length==0){
 alert("序列号不能为空，请输入序列号！") ;
 return ;
 }
 var cTabName = "dlddlDet";
 var stockCode="";
 
 var stockCodeObj=m("StockCode"); 
 stockCode = stockCodeObj.value ;
 if(stockCode==""){
 alert("仓库不能为空，请选择合适的仓库！") ;
 return ;
 }
 var detStock=false;
 
 
 for(var i=1;i<11;i++){
 var seqs = n(cTabName+"_SEQ_Qty"+i) ;
 for(var j=0;j<seqs.length;j++){
 if(seqs[j].value==obj.value&& n(cTabName+"_Qty"+i)[j].value != "0"){
 alert("序列号已经在第"+(j+1)+"行，数"+i+"存在！") ;
 obj.value = "";
 return ;
 }
 }
 }
 
 
 ;
 ;
 var popBeanName = "";
 if(popBeanName==""){
 alert("未取到数量弹出窗名称");
 return;
 }
 var urls="/UtilServlet?operation=validateBPSeq&MOID=86ed1cd2_1312221558520651057&MOOP=add&StockCode="+stockCode+"&Seq="+obj.value+"&popupBean="+popBeanName;
 AjaxRequest(urls) ;
 
 if(response == "noSeq"){
 alert("此序列号在当前仓库中不存在，请核对后再录入！");
 obj.value = "";
 return;
 }
 var returnValue = response.split("@@") ;
 var goodsCode = n(cTabName+"_GoodsCode") ;
 var rowNum = -1 ;
 
 
 for(var i=goodsCode.length-1;i>=0;i--){
 if(goodsCode[i].value.length>0){
 var compareField = "" ;
 if(detStock){ 
 if(returnValue[0]==compareField && returnValue[3]==n(cTabName+"_GoodsCode")[i].value && returnValue[2]==n(cTabName+"_StockCode")[i].value){
 rowNum = i ;
 break;
 }
 }else{
 if(returnValue[0]==compareField && returnValue[3]==n(cTabName+"_GoodsCode")[i].value){
 rowNum = i ;
 break;
 }
 }
 }
 }
 
 
 var seqNum = -1 ;
 if(rowNum!=-1){ 
 for(var i=1;i<11;i++){
 var seqs = n(cTabName+"_Qty"+i) ;
 if(seqs[rowNum].value == '' || seqs[rowNum].value=="0"){
 seqNum = i ;
 break ;
 }
 }
 } 
 
 if(seqNum==-1){
 
 var countNum = 0 ;
 for(var i=0;i<goodsCode.length;i++){
 if(goodsCode[i].value.length>0){ 
 countNum++ ;
 }
 }
 if(n(cTabName+"_tblGoods_GoodsNumber").length<=countNum){
 $(n(cTabName+"_tblGoods_GoodsNumber")[countNum-1]).focus() ;
 }
 $(n(cTabName+"_tblGoods_GoodsNumber")[countNum]).focus();
 
 if(detStock){ 
 StockFullName = returnValue[4]; 
 if(StockFullName!=""){ 
 var StockFullNameObj = n(cTabName+"_tblStocks_StockFullNameDet") ;
 StockFullNameObj[countNum].value=StockFullName;
 
 if(document.createEvent){
 var evt = document.createEvent("HTMLEvents"); 
 evt.initEvent("keydown",false,false);
 evt.keyCode = 13;
 StockFullNameObj[countNum].dispatchEvent(evt); 
 }else{
 var goodnewEvt = document.createEventObject();
 goodnewEvt.keyCode = 13;
 StockFullNameObj[countNum].fireEvent('onKeyDown',goodnewEvt);
 }
 if(n(cTabName+"_StockCode")[countNum].value==""){
 return;
 }
 }
 }
 
 GoodsNumber = returnValue[5];
 
 var GoodsNumberObj = n(cTabName+"_tblGoods_GoodsNumber") ;
 GoodsNumberObj[countNum].value=GoodsNumber;
 if(document.createEvent){
 var evt = document.createEvent("HTMLEvents"); 
 evt.initEvent("keydown",false,false);
 evt.keyCode = 13;
 GoodsNumberObj[countNum].dispatchEvent(evt); 
 }else{
 var goodnewEvt = document.createEventObject();
 goodnewEvt.keyCode = 13;
 GoodsNumberObj[countNum].fireEvent('onKeyDown',goodnewEvt);
 }
 
 if(n(cTabName+"_GoodsCode")[countNum].value==""){
 return;
 }
 
 var qtys = returnValue[1].split(";") ;
 }else{
 var qtys = returnValue[1].split(";") ;
 }
 
 obj.value = "" ;
 obj.focus();
}


function RecAutoSettlement(){
 var tableName=document.getElementById("tableName").value;
 var companyCode=document.getElementById("CompanyCode").value;
 var BillDate=document.getElementById("BillDate").value;
 
 
 var AcceptTypeIDStr='AcceptTypeID';if(tableName=="tblPay")AcceptTypeIDStr='PaytypeID';
 var AcceptTypeID=document.getElementById(AcceptTypeIDStr).value;
 
 var ExeBalAmtStr='FactIncome';if(tableName=="tblPay")ExeBalAmtStr='FactOutcome';
 var ExeBalAmt=document.getElementById(ExeBalAmtStr).value;
 
 if(companyCode.length==0){
 if(tableName=="tblPay"){
 alert("请选择供应商");
 }else{
 alert("请选择客户");
 }
 return;
 }
 
 if(isNaN(ExeBalAmt)||Number(ExeBalAmt)<=0){
 if(tableName=="tblPay"){
 alert("请在付款账户明细表中输入付款金额");
 }else{
 alert("请在收款账户明细表中输入收款金额");
 }
 return;
 }
 
 window.thisField = 'tblSaleReceiveDet_RefbillNo';
 if(tableName=='tblPay')window.thisField = 'tblPaydet_RefBillNo';
 
 window.field = window.thisField;
 window.obj=document.getElementsByName(window.thisField)[0];
 var url="";
 if(tableName=='tblPay'){
 var url ="UtilServlet?keyId=&tableName=tblPaydet&fieldName=RefBillNo";
 }else{ 
 var url ="UtilServlet?keyId=&tableName=tblSaleReceiveDet&fieldName=RefbillNo";
 }
 var url=url+"&operation=RecAutoSettlement&CompanyCode="+companyCode+"&"+AcceptTypeIDStr+"="+AcceptTypeID+"&BillDate="+BillDate+"&MOID=86ed1cd2_1312221558520651057&MOOP=add&selectField=&selectValue=";

 url = encodeURI(url) ;
 if(url.indexOf("+")!=-1){
 url = url.replaceAll("\\+",encodeURIComponent("+")) ;
 }
 AjaxRequest(url) ; 
 
 var str=response;
 var newStr='';

 var strArray=str.split('|');
 for(var i=0;i<strArray.length;i++){
 if(ExeBalAmt>0&&strArray[i].length>0){
 var fieldValArray=strArray[i].split(';');
 var hanExeBalAmt=fieldValArray[7];

 if(Number(ExeBalAmt)<Number(hanExeBalAmt)){
 hanExeBalAmt=ExeBalAmt
 }
 fieldValArray[7]=hanExeBalAmt;
 
 ExeBalAmt=f(ExeBalAmt-hanExeBalAmt,2);
 var fieldVal='';
 for(var j=0;j<fieldValArray.length;j++){
 fieldVal=fieldVal+fieldValArray[j]+';';
 }
 newStr=newStr+fieldVal+'|';
 }else{
 break;
 }
 }
 if(newStr.length==0){
 alert("没有需要结算的单据");
 }
 var colNames='';
 if(tableName=='tblPay'){
 curGridRowNum.curKey='tblPaydet';
 var colNames='@TABLENAME.RefBillDate;@TABLENAME.RefBillNo;@TABLENAME.BillAmt;@TABLENAME.SettledAmt;@TABLENAME.BackAmt;@TABLENAME.WexeBalAmt;@TABLENAME.BillName;@TABLENAME.ExeBalAmt;@TABLENAME.RefbillID;@TABLENAME.BuyOrderNo;@TABLENAME.PayBillType;@TABLENAME.BuyOrderID;@TABLENAME.CompanyCode;@TABLENAME.Account;';
 }else{
 curGridRowNum.curKey='tblSaleReceiveDet';
 var colNames='@TABLENAME.RefbillNo;@TABLENAME.BillAmt;@TABLENAME.SettledAmt;@TABLENAME.BackAmt;@TABLENAME.WexeBalAmt;@TABLENAME.BillName;@TABLENAME.RefBillDate;@TABLENAME.ExeBalAmt;@TABLENAME.RefbillID;@TABLENAME.SalesOrderNo;@TABLENAME.ReceiveBillType;@TABLENAME.SalesOrderID;@TABLENAME.CompanyCode;@TABLENAME.SubCode;';
 }
 
 gridTable =document.getElementById(curGridRowNum.curKey+"Table");
 tableDiv = gridTable.parentNode.parentNode.parentNode;
 for(var i=0;i<document.getElementsByName(window.thisField).length-1;i++){
 tableDiv.fix.deleteRow(i+1);
 }
 
 var divTitle =document.getElementById(curGridRowNum.curKey+"TableDIVTitle");
 changeTags(divTitle);
 dataBackIn(false,colNames,newStr,window)
}



function extendSubmit(vars,selected)
{
 if(vars == "RecAutoSettlement"){
 RecAutoSettlement();
 return;
 }else if(vars == "custom"){
 document.getElementById("ButtonType").value = 'custom';
 }else if(vars == "handover"){
 var varValue = window.showModalDialog("/vm/classFunction/selectCRMCustomer.jsp","","dialogWidth=465px;dialogHeight=260px") ;
 if(typeof(varValue)=="undefined")return ;
 var arrValue = varValue.split("|") ;
 document.getElementById("newCreateBy").value=arrValue[0] ;
 document.getElementById("wakeUp").value=arrValue[1] ;
 document.getElementById("ButtonType").value = "handover" ;
 }else if(vars == "sendEmail"){
 document.getElementById("ButtonType").value = "sendEmail" ;
 if(window.parent.document.getElementById("bottomFrame")){
 var varMin = window.parent.document.getElementById("moddiFrame").contentWindow.document.getElementById("div_min");
 if(typeof(varMin)!="undefined"){
 varMin.click() ;
 }
 }
 }else if(vars == "sendSMS"){
 var varValue = window.showModalDialog("/vm/classFunction/sendMessage.jsp","","dialogWidth=645px;dialogHeight=350px") ;
 if(typeof(varValue)=="undefined")return ;
 document.getElementById("sendMessage").value = varValue ;
 document.getElementById("ButtonType").value = "sendSMS" ;
 }else if(vars == "folderRight"){
 var listObj = document.getElementById("functionListObj");
 var keyIdStr=listObj.getCBoxValue("hidden");
 

 AjaxRequest('/UtilServlet?operation=folderRight&type=getData&tableName='+form.tableName.value+'&keyId='+keyIdStr);
 var varValue = window.showModalDialog("/vm/oa/oaPublicMsg/folderRight.jsp?tableName=dlddl&keyId="+keyIdStr,"","dialogWidth=565px;dialogHeight=350px") ;
 if(typeof(varValue)=="undefined")return ;
 }else if(vars == "dataMove"){
 displayName = encodeURI("分级目录") ;
 var selectName = document.getElementById("selectName").value ;
 var moduleType = document.form.moduleType;
 var strURL = "/UserFunctionAction.do?&selectName="+selectName+"&isRoot=isRoot&operation=22" ;
 if(typeof(moduleType)!="undefined"){
 strURL = strURL+"&moduleType="+moduleType.value ; 
 }
 var str = window.showModalDialog(strURL+"&isQuote=1&popDataType=dataMove&MOID="+MOID+"&MOOP=query&LinkType=@URL:&displayName="+displayName,"",
 "dialogWidth=750px;dialogHeight=430px");
 if(typeof(str)=="undefined")return false ;
 var fieldValue=str.split(";"); 
 
 document.getElementById("classCode").value=fieldValue[0];
 document.getElementById("ButtonType").value = "dataMove" ;
 if(typeof(top.jblockUI)!="undefined"){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 }
 }else if(vars=="billExport"){
 if(!confirm("确定导出数据吗?"))return
 document.getElementById("ButtonType").value = "billExport" ;
 if(typeof(top.jblockUI)!="undefined"){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 }
 }else if(vars=="StopValue_BaseInfo_tblEmployee"){
 var chks = document.getElementsByName("keyId");
 for(var i=0;i<chks.length;i++){
 if(chks[i].checked){
 var v = chks[i].value;
 if(v=="1"){
 alert("admin是超级管理员，不能被停用");
 return false;
 }
 }
 }
 document.getElementById("ButtonType").value = "execDefine" ;
 form.defineName.value=vars;
 }else if(vars == "on" || vars == "down" || vars == "all"){
 var opType = "update" ;
 var updateImg = "" ;
 if("all"==vars){
 if(!confirm("同步电子商务:就是把电子商务网站上所有商品全部删除后重新上架商品。\r\r这一过程可能需要一段时间，你确定要同步电子商务网站？")){
 return ;
 }
 if(confirm("上传图片可能影响上传速度,是否要上传图片？\n\n注：如果启用商品图片根据商品编号规则自动读取,则可以直接复制图片到\n服务器AIOSHop\\ShopImage相应目录下，可能更快些.")){
 updateImg = "yes" ;
 }
 }else{
 if("down"==vars){
 if(confirm("是否删除网店上的商品资料")){
 opType = "delete" ;
 }
 }else{
 if(confirm("是否更新图片")){
 updateImg = "yes" ;
 }
 }
 }
 document.getElementById("ButtonType").value = "shelf" ;
 document.getElementById("opType").value = opType ;
 document.getElementById("updateImg").value = updateImg ;
 document.getElementById("shelfType").value = vars ;
 if("down"==vars){
 if(typeof(top.jblockUI)!="undefined"){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 }
 }else{
 setInterval("Refresh()",1000);
 }
 }else if(vars=="ImageCatalog"){ 
 document.getElementById("ButtonType").value = "ImageCatalog" ;
 alert("生成图片目录：在启用商品图片根据商品编号规则自动读取下,用户不需要手工上传图片\n\n只需要复制商品图片到服务器AIO\\fileServer_gm\\ShopPic相应的目录下.") ;
 }else{
 document.getElementById("ButtonType").value = "execDefine" ;
 form.defineName.value=vars;
 }
 form.operation.value=25;
 form.submit();
}


function initDownSelect(){ 
 
} 


function FillGoods(gcode){
 var code ='';
 var codeAmount = n("BarCodeQty")[0].value;
 if(Object.prototype.toString.call(gcode) == '[object Array]')
 {
 code = gcode[0];
 codeAmount = gcode[1];
 }else
 {
 code = gcode;
 }
 
 
 if(code.trim().length==0){
 alert("条形码不能为空，请输入条形码！") ;
 return ;
 }
 
 
 var cTabName = "dlddlDet";
 var goodsCode=cTabName+'_GoodsCode';
 var goodsNumber=cTabName+'_tblGoods_GoodsNumber';
 
 var goodsCodeObj=n(cTabName+'_GoodsCode');
 var goodsObj=n(cTabName+'_tblGoods_GoodsNumber');
 
 var countNum = curGridRowNum.curLine;
 if(countNum ==-1)
 {
 countNum =0;
 curGridRowNum.curLine = 0;
 }
 if(n(cTabName+"_tblGoods_GoodsNumber").length<=countNum){
 countNum = n(cTabName+"_tblGoods_GoodsNumber").length-1;
 }
 
 goodsObj[countNum].value=code.trim();
 $(goodsObj[countNum]).select();
 var tmpe = jQuery.Event("keyup");
 tmpe.keyCode = 13;
 $(goodsObj[countNum]).trigger(tmpe);
 
 var qty=n(cTabName+"_Qty")[countNum];
 qty.value=codeAmount;
 $(qty).focus();
 $(qty).change();
 
 curGridRowNum.curLine = countNum +1;
 
}

function billQuery(value,dis,width,height){
 while(value.indexOf("@ValueofDB:") >-1){
 pos = value.indexOf("@ValueofDB:");
 epos = value.indexOf("&",pos);
 dpos = value.indexOf(";",pos);
 if(dpos > 0 && dpos<epos){
 epos = dpos;
 }
 if(epos==-1){
 epos = value.length;
 }
 
 name = value.substring(pos+11,epos); 
 obj=null; 
 nv = "";
 if(name.indexOf("_")== -1){ 
 obj = document.getElementsByName(name); 
 nv = obj[0].value;
 if(nv == ""){
 asyncbox.alert('无'+dis,'提示');
 return;
 }
 }else{ 
 ct_name = name.substring(0,name.indexOf("_"));
 if(curGridRowNum.curKey != ct_name){
 ctTitle=$("#"+ct_name+"TableDIVTitle").text();
 asyncbox.alert("请选择"+ctTitle+"中明细行",'提示');
 return;
 }
 if(detail== "detail"){
 gdata = null;
 for(i=0;i<gridDataNum;i++){
 if(childData[i] == ct_name){
 gdata = gridDatas[i];
 }
 }
 nv = gdata.rows[curGridRowNum.curLine-1].get(name.substring(name.indexOf("_")+1));
 
 if(nv == ""){
 asyncbox.alert('您选择的当前行无'+dis,'提示');
 return;
 }
 }else{
 obj = document.getElementsByName(name);
 if(curGridRowNum.curLine>=obj.length||curGridRowNum.curLine<0){
 asyncbox.alert('请选择要查询的明细行','提示');
 return;
 }
 nv = obj[curGridRowNum.get(ct_name)].value;
 if(nv == ""){
 asyncbox.alert('您选择的当前行无'+dis,'提示');
 return;
 }
 }
 }
 
 value = value.substring(0,pos)+encodeURIComponent(nv)+value.substring(epos);
 }
 if(width=='' || width>document.documentElement.clientWidth-100){
 width=document.documentElement.clientWidth-100;
 }
 if(height=='' || height > document.documentElement.clientHeight-50){
 height=document.documentElement.clientHeight-50;
 }
 value= value+"&LinkType=@URL:&NoButton=true";
 openPop('PopQuerydiv',dis,value,width,height,false,false);
}

function billTool(value,dis,width,height){
 while(value.indexOf("@ValueofMustDB:") >-1){
 pos = value.indexOf("@ValueofMustDB:");
 epos = value.indexOf("&",pos);
 dpos = value.indexOf(";",pos);
 if(dpos > 0 && dpos<epos){
 epos = dpos;
 }
 if(epos==-1){
 epos = value.length;
 }
 
 name = value.substring(pos+15,epos);
 if(name.indexOf("@TABLENAME") ==0){
 if(curGridRowNum.curKey == ""){
 alert("请选择要查询的明细行");
 return;
 }
 name = curGridRowNum.curKey+name.substring(10);
 }
 obj=null;
 if(name.indexOf("|")>0){ 

 ns = name.split("|");
 obj = document.getElementsByName(ns[0]);
 if(obj == null||obj.length==0){
 obj = document.getElementsByName(ns[1]);
 }
 }else{
 obj = document.getElementsByName(name); 
 }
 
 nv = "";
 if(obj == null||obj.length==0){
 
 }else if(obj.length ==1){ 
 nv = obj[0].value;
 }else{ 
 if(curGridRowNum.curLine>=obj.length||curGridRowNum.curLine<0){
 alert("请选择要查询的明细行"); 
 return;
 }
 tb = name.substring(0,name.indexOf("_"));
 nv = obj[curGridRowNum.get(tb)].value;
 }
 if(nv==""){
 if(name.indexOf("|")>0){
 name = name.substring(0,name.indexOf("|"));
 }
 if(name.indexOf("_")>0){
 for(var i=0;i<childCheckList.length;i++){
 if(childCheckList[i].name == name)
 {
 alert("请先输入当前选择明细行的"+childCheckList[i].title); 
 return;
 }
 }
 alert("请先输入"+dis+":"+name); 
 return; 
 }else{
 for(var i=0;i<checkList.length;i++){
 if(checkList[i].name == name)
 {
 asyncbox.alert("请先输入"+checkList[i].title,'提示');
 return;
 }
 }
 alert("请先输入"+dis+":"+name); 
 return; 
 } 
 } 
 value = value.substring(0,pos)+encodeURIComponent(nv)+value.substring(epos); 
 }
 while(value.indexOf("@ValueofDB:") >-1){
 pos = value.indexOf("@ValueofDB:");
 epos = value.indexOf("&",pos);
 dpos = value.indexOf(";",pos);
 if(dpos > 0 && dpos<epos){
 epos = dpos;
 }
 if(epos==-1){
 epos = value.length;
 }
 
 name = value.substring(pos+11,epos);
 if(name.indexOf("@TABLENAME") ==0){
 if(curGridRowNum.curKey == ""){
 alert("请选择要查询的明细行");
 return;
 }
 name = curGridRowNum.curKey+name.substring(10);
 }
 obj=null;
 if(name.indexOf("|")>0){ 
 ns = name.split("|");
 obj = document.getElementsByName(ns[0]);
 if(obj == null||obj.length==0){
 obj = document.getElementsByName(ns[1]);
 }
 }else{
 obj = document.getElementsByName(name); 
 }
 
 nv = "";
 if(obj == null||obj.length==0){
 
 }else if(obj.length ==1){ 
 nv = obj[0].value;
 }else{ 
 if(curGridRowNum.curLine>=obj.length||curGridRowNum.curLine<0){
 alert("请选择要查询的明细行"); 
 return;
 }
 tb = name.substring(0,name.indexOf("_"));
 nv = obj[curGridRowNum.get(tb)].value;
 }
 
 value = value.substring(0,pos)+encodeURIComponent(nv)+value.substring(epos); 
 }
 
 
 if(width=='' || width>document.documentElement.clientWidth-100){
 width=document.documentElement.clientWidth-100;
 }
 if(height=='' || height > document.documentElement.clientHeight-50){
 height=document.documentElement.clientHeight-50;
 }
 value= value+"&LinkType=@URL:&NoButton=true";
 
 openPop('PopTooldiv',dis,value,width,height,false,false);
}
function closePopWin(){
 if(jQuery.exist('Popdiv')){ 
 jQuery.close('Popdiv'); 
 }
}

var curpushUser;
var curpushdetId;
function billPush(destTableName,tableDisplay){
 asyncbox.open({
 id : 'Popdiv',
 title : '推单['+tableDisplay+']',
 　　 html : '<span style="padding:20px 0 10px 20px;display:block">请选择推送职员，默认为自己</span><input type=hidden id=pushUser name=pushUser>'+
 '<input type=text id=pushUserName name=pushUserName readOnly style="width:100px;margin-left:20px">'+
 '<input type=button name=pbt onclick="selectpushUser()" value="选择职员"/>',
 　　 width : 300,
 　　 height : 180,
 btnsbar : [{text : '整单推送',action : 'allPush' },
 
 {text : '取消',action : 'cancel' }],
 callback : function(action){
 if(action != 'allPush' && action != 'partPush'){
 return;
 }
 curpushUser = $("#pushUser").val()
 　　　　　if(action == 'allPush'){
 
 　　　　　}
 　　　　　if(action == 'partPush'){ 
 
 url = "/UtilServlet?operation=pushBill&tableName="+$("#tableName").val()+"&destTableName="+destTableName+"&getFirstChildTable=true";
 alert(url);
 ftable = jQuery.ajax({
 url: url , 
 async: false 
 }).responseText; 
 openSelectPushDet(ftable); 
 　　　　　　　return;
 　　　　　}
 
 jQuery.post("/UtilServlet?operation=pushBill",
 { tableName: $("#tableName").val(), 
 destTableName: destTableName, 
 userId: curpushUser,
 varKeyIds: $("#varKeyIds").val(),
 detId :'' },
 function(data){ 
 if(data == "HASPUSH"){
 
 asyncbox.confirm('此单已进行过推单['+tableDisplay+'],是否继续?','推单确认',function(action){
 　　　if(action == 'ok'){
 　　　　　jQuery.post("/UtilServlet?operation=pushBill",
 { tableName: $("#tableName").val(), 
 destTableName: destTableName, 
 userId: curpushUser,
 varKeyIds: $("#varKeyIds").val(),
 isContinue: 'true',
 detId :'' },
 function(data){ 
 alert(data);
 } 
 );
 　　　}
 　}); 
 }else{
 alert(data);
 }
 } 
 );
 
 　　　} 
　 });
}
function openSelectPushDet(ftable){

 asyncbox.open({
 id : 'PopSelBilldiv',
 title : '选择明细',
 　　 html : '<div name="tblBuyOrderDetTableDIV" id="tblBuyOrderDetTableDIV" ah="tblBuyOrderDetTable" style="">'+$("#tblBuyOrderDetTableDIV").html()+'</div>'
 });
 return;
}
 
function selectpushUser(){
 condition=''
 popname="userGroup";
 var urls = "/Accredit.do?popname=" + popname + "&inputType=radio&condition="+condition; 
 titles = "请选择职员"
 
 asyncbox.open({
 id : 'PopUserdiv',
 title : titles,
 url : urls,
 width : 755,
 height : 435,
 top: 5,
 btnsbar : jQuery.btn.OKCANCEL,
 callback : function(action,opener){
　　　　　 if(action == 'ok'){
 var employees = opener.strData;
 emps = employees.split(";");
 $("#pushUser").val(emps[0]);
 $("#pushUserName").val(emps[1]);
　　　　　 }
　　　 }
　 }); 
}


function fillData(datas){
 var employees = datas;
 emps = employees.split(";");
 $("#pushUser").val(emps[0]);
 $("#pushUserName").val(emps[1]);
 jQuery.close('PopUserdiv');
}

function calc(){
 var urls = "/common/calc.jsp"; 
 asyncbox.open({
 id : 'PopCalcdiv',
 title : '计算器',
 url : urls,
 width : 210,
 height : 220,
 top: 25
　 }); 
}


function changePos(type){
 curId = $("#varKeyIds").val();
 curid =curId.split(";")[0];
 if(type=="PRE"){
 obj = parent.window.$("input[type=checkbox][name=keyId][value*="+curId+"]").parents("tr").prev();
 if(obj.size()==0){
 alert('本页没有上一条数据了');
 return;
 }else{
 vid =obj.find("input[name=keyId]").val();
 vid = vid.split(";")[0];
 href = window.location.href;
 pos = href.indexOf("keyId=")+6;
 href = href.substring(0,pos)+vid+href.substring(href.indexOf("&",pos));
 window.location.href=href;
 }
 }else{
 obj = parent.window.$("input[type=checkbox][name=keyId][value*="+curId+"]").parents("tr").next();
 if(obj.size()==0){
 alert('本页没有下一条数据了');
 return;
 }else{
 vid =obj.find("input[name=keyId]").val();
 if(vid==null){
 alert('本页没有下一条数据了');
 return;
 }
 vid = vid.split(";")[0];
 
 href = window.location.href;
 pos = href.indexOf("keyId=")+6;
 href = href.substring(0,pos)+vid+href.substring(href.indexOf("&",pos));
 window.location.href=href;
 }
 }
}
function flowDepict(applyType,keyId){ 
 var winWidth = 1024;
 if($(window).width()>1024){
 winWidth = 1150;
 }
 window.open("/OAMyWorkFlow.do?keyId="+keyId+"&operation=5&applyType="+applyType,null,"height=570, width="+winWidth+",top=50,left=100 ");
}
function changeTags(obj){
 $(obj).addClass("tagSel").siblings("span").removeClass("tagSel");
 $("#"+$(obj).attr("show")).show().siblings("div").hide();
}
function showGridTags(){
 var firstTag = $(".showGridTags .tags:eq(0)");
 firstTag.addClass("tagSel");
 $("#"+firstTag.attr("show")).show().siblings("div").hide();
}
 
function update(){
 location.href="/UserFunctionAction.do?tableName=dlddl&designId=&keyId="+$("#id").val()+
 "&f_brother="+f_brother+"&parentCode="+$("#parentCode").val()+"&operation=7&noback="+noback+"&winCurIndex="+winCurIndex+
 "&fromPage=detail&moduleType="+moduleType+"&parentTableName="+$("#parentTableName").val()+"&saveDraft="+$("#saveDraft").val(); 
}

function copyOp(keyId)
{ 
 form.target = ""; 
 form.action="/UserFunctionAction.do?&operation=17&tableName=dlddl&saveDraft=&&keyId="+keyId+
 "&parentTableName=&moduleType="+moduleType+"&f_brother="+f_brother+"&noback="+noback+"&winCurIndex="+winCurIndex;
 form.submit();
}

function draftAudit(){
 

 
 form.action = '/UserFunctionQueryAction.do?varKeyIds='+$("#id").val()+'|'+'&ButtonType=draftAudit';
 form.operation.value = 25 ;
 if(typeof(top.jblockUI)!="undefined"){
 top.jblockUI({ message: " <img src='/js/loading.gif'/>",css:{ background: 'transparent'}}); 
 }
 document.getElementById("form").target = "formFrame" ; 
 form.submit() ; 
}

function openPop(id,title,usrc,width,height,checkFormChange,noTitle){
 asyncbox.open({
 id : id, 
 title : title, 
 　　 html : '<div style="text-align: center;width: 100%; height: 100%;border:none">'+
 '<iframe id="'+id+'_Frame" frameborder="no" border="0" marginwidth="0" marginheight="0" style="width: 100%; height: 99%;top:0px;left:0px;z-index:-1;border:none;filter:Alpha(opacity=100);"></iframe>'+
 '<div id="'+id+'_Frame_1" style="position:absolute;top:'+(height/2-80)+'px;left:'+(width/2-80)+'px;text-align: center;width:128px;height:128px; background: url(/style1/images/ll.gif) no-repeat center;"></div></div>', 
 　　 width : width,
 　　 height : height,
 callback : function(action,opener){
 if(action == 'close' && checkFormChange){
 iframe1 = document.getElementById(id+"_Frame");
 if(iframe1 && iframe1.contentWindow.is_form_changed()){
 if(! confirm("您有未保存的数据，是否确定放弃保存并离开")){
 return false;
 }
 }
 }
 }
　 }); 
 if(noTitle){ 
 $("#"+id+" .b_t_m").parent().remove();
 }
 
 $("#"+id+"_content").height($("#"+id).height()-22);

 var iframe = document.getElementById(id+"_Frame");
 iframe.src=usrc+"&popWinName="+id; 
 
 if (iframe.attachEvent){ 
 iframe.attachEvent("onload", function(){ 
 $("#"+id+"_Frame_1").hide();
 }); 
 } else { 
 iframe.onload = function(){ 
 $("#"+id+"_Frame_1").hide();
 }; 
 }
}



function checkDialog(url){
 asyncbox.open({
 id:'deliverApproveTo',url:url,title:'审核',width:650,height:370,
 btnsbar:jQuery.btn.OKCANCEL,
 callback:function(action,opener){
 if(action == 'ok'){
 if(opener.beforSubmit(opener.form)){
 opener.form.submit();
 }
 return false;
 }
　　 }
　 });
}
function checkSave(str){ 
 var defi = "&defineInfo=";
 var cid = $("#id").val(); 
 if(str != null && str != ""){
 defi += str;
 } 
 jQuery.get("/UserFunctionQueryAction.do?tableName=dlddl&operation=25&ButtonType=saveCheckBill&varKeyIds="+cid+defi,function(data){
 if(data=="OK"){
 checkDialog("/OAMyWorkFlow.do?keyId="+cid+"&tableName=dlddl&operation=82&fromPage=erp");
 }else if(data.indexOf("defineInfo")==0){
 var ds = data.split(":");
 if(confirm(ds[4])){
 if(ds[2] != ""){
 checkSave(ds[1]+":"+ds[2]+";");
 }
 }else{
 if(ds[3] != ""){
 checkSave(ds[1]+":"+ds[3]+";");
 }
 }
 } else{
 alert(data);
 } 
 });
}

