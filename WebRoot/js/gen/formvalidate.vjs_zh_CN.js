function checkText(myText)
{
 var variable = myText.value;
 if(variable=="" || variable==null)
 {
 myText.value = "00";
 return false;
 }else
 {
 if(isNaN(variable))
 {
 myText.value = "00";
 return false;
 }else
 {
 return true;
 }
 }

}
function isNull(variable,message)
{
 if(variable=="" || variable==null)
 {
 alert(message+'不能为空.');
 return false;
 }else
 {
 return true;
 }
 
}
function isNum(variable,message)
{

 if(isNull(variable,message))
 {
 if(isNaN(variable))
 {
 alert(message+'必须是数字.');
 return false;
 }else
 {
 return true;
 } 
 }else
 {
 return false; 
 }

} 
function isSpecialCharacter(variable,message)
{
 if(isNull(variable,message))
 {
 if(variable.indexOf('|')>=0)
 {
 alert(message+'包括特殊字符'+"'|'");
 return false; 
 }else if(variable.indexOf('&')>=0)
 {
 alert(message+'包括特殊字符'+"'&'");
 return false; 
 }else if(variable.indexOf('%')>=0)
 {
 alert(message+'包括特殊字符'+"'%'");
 return false; 
 }else if(variable.indexOf('*')>=0)
 {
 alert(message+'包括特殊字符'+"'*'");
 return false; 
 }else if(variable.indexOf('(')>=0)
 {
 alert(message+'包括特殊字符'+"'('");
 return false; 
 }else if(variable.indexOf(')')>=0)
 {
 alert(message+'包括特殊字符'+"')'");
 return false; 
 }else if(variable.indexOf('!')>=0)
 {
 alert(message+'包括特殊字符'+"'!'");
 return false; 
 }else if(variable.indexOf('#')>=0)
 {
 alert(message+'包括特殊字符'+"'#'");
 return false; 
 }
 else
 {
 return true;
 } 
 }else
 {
 return false; 
 } 

}
function isEmail(variable,message)
{
 if(isNull(variable,message))
 {
 if(!isEmailAddress(variable))
 {
 alert(message+'不是合法邮箱地址.');
 return false;
 }else
 {
 return true;
 }
 
 }else
 {
 return false; 
 } 

}
function isEmailAddress(str)
{
 var result = false;
 if(str.indexOf('@')>0)
 {
 var a1 = str.split('@')[0];
 var a2 = str.split('@')[1];
 if(isW(a1) && a2.indexOf('.')>0)
 {
 if(isW(a2.split('.')[0]) && isW(a2.split('.')[1]))
 result = true;
 }
 }
 return result;
}
function isW(str)
{
 if(str.length==0)
 {
 return false;
 for(var i=0; i<str.length; i++)
 {
 var tmp = str.substring(i,i+1);
 if(!(tmp>='a'&&tmp<='z') && !(tmp>='A'&&tmp<='Z')&& !(tmp>='0'&&tmp<='9') && tmp!='_')
 {
 return false;
 }
 }
 }
 return true;
}
function scopeValiadte(variable,start,end,message)
{
 
 if(isNum(variable,message))
 {
 var value = parseInt(variable);
 if(value<start || value>end)
 {
 alert(message+'必须在'+start+'$text.get("oa.word.and")'+end+'之间');
 return false; 
 }else
 {
 return true;
 } 
 }else
 {
 return false; 
 } 
}


function maxValiadte(variable,num,message) 
{ 

 if(isNum(variable,message))
 {
 var value = parseInt(variable); 
 if(value>num) 
 
 { 
 alert(message+'必须小于'+num);
 return false; 
 }else 
 { 
 return true; 
 } 
 }else
 {
 return false; 
 } 
}


function minValiadte(variable,num,message) 
{ 
 if(isNum(variable,message))
 {
 var value = parseInt(variable); 
 if(value<num) 
 { 
 alert(message+'$text.get("oa.larger.than")'+num); 
 return false; 
 }else 
 { 
 return true; 
 } 
 }else
 {
 return false; 
 } 
}


function minLenghValiadte(variable,num,message) 
{ 
 
 var value = variable.length; 
 if(value<num) 
 { 
 alert(message+'不能少于'+num+'个字符.'); 
 return false; 
 }else 
 { 
 return true; 
 } 
} 


function maxLenghValiadte(variable,num,message) 
{ 
 
 var value = variable.length; 
 if(value>num) 
 { 
 alert(message+'不能超过'+num+'个字符.'); 
 return false; 
 }else 
 { 

 return true; 
 } 
}

function LenghScopeValiadte(variable,beginnumer,endnumber,message) 
{ 
 
 var value = variable.length; 
 if(value<beginnumer || value>endnumber) 
 { 
 alert(message+'的字符必须在'+beginnumer+'$text.get("oa.word.and")'+endnumber+'之间'); 
 return false; 
 }else 
 { 
 return true; 
 } 
}