var c = function (id) {
    return "string" == typeof id ? document.getElementById(id) : id;
};

var Class = {
  create: function() {
    return function() {
      this.initialize.apply(this, arguments);
    }
  }
}

var Extend = function(destination, source) {
    for (var property in source) {
        destination[property] = source[property];
    }
    return destination;
}

function changeColor(obj){
	var tags = document.getElementsByTagName("a") ;
	for(var i=0;i<tags.length;i++){
		tags[i].parentNode.className = "" ;
	}
	obj.parentNode.className = "onToday" ;
}
var Calendar = Class.create();
Calendar.prototype = {
  initialize: function(container, options) {
	this.Container = c(container);//容器(table结构)
	this.Days = [];//日期对象列表
	
	this.SetOptions(options);
	
	this.Year = this.options.Year || new Date().getFullYear();
	this.Month = this.options.Month || new Date().getMonth() + 1;
	this.SelectDay = this.options.SelectDay ? new Date(this.options.SelectDay) : null;
	this.onSelectDay = this.options.onSelectDay;
	this.onToday = this.options.onToday;
	this.onFinish = this.options.onFinish;	
	
	this.Draw();
  },
  //设置默认属性
  SetOptions: function(options) {
	this.options = {//默认值		Year:			0,//显示年		Month:			0,//显示月		SelectDay:		null,//选择日期
		onSelectDay:	function(){},//在选择日期触发
		onToday:		function(){},//在当天日期触发		onFinish:		function(){}//日历画完后触发	};
	Extend(this.options, options || {});
  },
  //当前月

  NowMonth: function() {
	this.PreDraw(new Date());
  },
  //上一月

  PreMonth: function() {
	this.PreDraw(new Date(this.Year, this.Month - 2, 1));
  },
  //下一月

  NextMonth: function() {
	this.PreDraw(new Date(this.Year, this.Month, 1));
  },
  //上一年

  PreYear: function() {
	this.PreDraw(new Date(this.Year - 1, this.Month - 1, 1));
  },
  //下一年

  NextYear: function() {
	this.PreDraw(new Date(this.Year + 1, this.Month - 1, 1));
  },
  //根据日期画日历

  PreDraw: function(date) {
	//再设置属性	this.Year = date.getFullYear(); this.Month = date.getMonth() + 1;
	//重新画日历	this.Draw();
  },
  //画日历
  Draw: function() {
	//用来保存日期列表
	var arr = [];
	//用当月第一天在一周中的日期值作为当月离第一天的天数
	for(var i = 1, firstDay = new Date(this.Year, this.Month - 1, 1).getDay(); i <= firstDay; i++){ arr.push(0); }
	//用当月最后一天在一个月中的日期值作为当月的天数
	for(var i = 1, monthDay = new Date(this.Year, this.Month, 0).getDate(); i <= monthDay; i++){ arr.push(i); }
	//清空原来的日期对象列表	this.Days = [];
	//插入日期
	var frag = document.createDocumentFragment();
	if(arr.length>35){
		$(".Calendar").css("background","url(/style/images/client/webDateBg2.jpg)").css("height","264") ;
	}else{
		$(".Calendar").css("background","url(/style/images/client/webDateBg1.jpg)").css("height","230") ;
	}
	var dHeight = $(".Calendar").height() ;
	var sHeight = parent.document.documentElement.clientHeight-dHeight-160;
	$("#treeNode").css("height",sHeight+"px");
	var weekMonday = "";
	var week= 1;
	while(arr.length){
		//每个星期插入一个tr
		var row = document.createElement("tr");
		//每个星期有7天		for(var i = 1; i <= 7; i++){
			var cell = document.createElement("td"); cell.innerHTML = "&nbsp;";
			if(arr.length){
				var d = arr.shift();
				if(i==2){
					weekMonday = this.Year+"-"+this.Month+"-"+d ;
				}
				if(d){
					var varMonth = "" ;
					var varDay = "" ;
					if(this.Month<10){
						varMonth = "0"+this.Month ;
					}else{
						varMonth = this.Month ;
					}
					if(d<10){
						varDay = "0"+d ;
					}else{
						varDay = d ;
					}
					cell.innerHTML = "<a id='"+this.Year+"-"+varMonth+"-"+varDay+"' href='javascript:void(0)' onclick='changeColor(this);showWeekReport(this)'>"+d+"</a><span id='b"+varDay+"'></span>";
					this.Days[d] = cell;
					var on = new Date(this.Year, this.Month - 1, d);
					//判断是否今日
					this.IsSame(on, new Date()) && this.onToday(cell);
					//判断是否选择日期
					this.SelectDay && this.IsSame(on, this.SelectDay) && this.onSelectDay(cell);
				}
			}
			row.appendChild(cell);
		}
		var cell = document.createElement("td");
		$(cell).attr("class","showWeek");
		cell.innerHTML = "&nbsp;";
		cell.innerHTML = "<a href='javascript:void(0)' style='width:130px;color: #0066FF;font-size: 13px;' onclick='showWeekReport(this)'>第 "+week+" 周</a>";
		week++;
		row.appendChild(cell);
		frag.appendChild(row);
	}
	//先清空内容再插入(ie的table不能用innerHTML)
	while(this.Container.hasChildNodes()){ this.Container.removeChild(this.Container.firstChild); }
	this.Container.appendChild(frag);
	//附加程序
	this.onFinish();
  },
  //判断是否同一日  IsSame: function(d1, d2) {
	return (d1.getFullYear() == d2.getFullYear() && d1.getMonth() == d2.getMonth() && d1.getDate() == d2.getDate());
  } 
}

function openDate(){
	var cale = new Calendar("idCalendar", {
		//SelectDay: new Date().setDate(10),
		onSelectDay: function(o){ o.className = "onSelect"; },
		onToday: function(o){ o.className = "onToday"; },
		onFinish: function(){
			c("idCalendarYear").innerHTML = this.Year; 
			c("idCalendarMonth").innerHTML = this.Month;
		}
	});
	c("idCalendarPre").onclick = function(){ cale.PreMonth(); }
	c("idCalendarNext").onclick = function(){ cale.NextMonth(); }
	
	//$("idCalendarPreYear").onclick = function(){ cale.PreYear(); }
	//$("idCalendarNextYear").onclick = function(){ cale.NextYear(); }

}