<!DOCTYPE html> 
<html>
<head>
<meta name="renderer" content="webkit">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />

<link type="text/css" rel="stylesheet" href="/js/skins/ZCMS/asyncbox.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=an1Z1onwGuydYUVCI0sTOylfTEhsKyIp">

<script type="text/javascript" src="/js/AsyncBox.v1.4.5.js"></script>

<title>区域分布</title>
<style>
#container{height:600px;width:100%;}
.loading{		
		display:none;
		min-width:250px;
		min-height: 100px;		
	    z-index: 99;	   
	    filter:alpha(opacity=100);
	    opacity:1;
	    position: absolute;
	    top: 30%;
	    left: 40%;	   
	    display: none;
	   
	}
.loading .loading_msg{
	text-align:center;
	vertical-align:middle;
	line-height:25px;
}
</style>
<script>
  var map ;
  var myGeo;
  var index = 0;
  var adds = [];
  var opts = {
			width : 250,     // 信息窗口宽度
			height: 80,     // 信息窗口高度
			title : "" , // 信息窗口标题
			enableMessage:true//设置允许信息窗发送短息
		   };
  $(function(){	 	
	  map = new BMap.Map("container"); // 创建地图实例
	  map.enableScrollWheelZoom();
	  var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
	  var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
	  map.addControl(top_left_control); //添加缩放，放大按钮（地图左上角）
	  map.addControl(top_left_navigation); //添加平移按钮（地图左上角）
	  myGeo = new BMap.Geocoder();
	 
	  var point = new BMap.Point(116.404, 39.915);  // 创建点坐标  
	  map.centerAndZoom(point, 5);	
		//*****获取数据信息******//		
		showDiv('.loading');
		jQuery.post("/hf/HFMapApi.jsp",{op:'GetHTInf'},function(ret){ 			
			if(ret.code==0){
    	    	alert('加载失败');
    	    } else{
    	    	var obj;  
	            obj = eval(ret); 
	            dealResult(ret.data);
    	    }
			closeDiv(".loading");
		},"json" ); 
  });
  
  function dealResult(data){
	var htm = "";	
	for(var i = 0;i<data.length;i++){			
		var str = '';		
		var name = data[i].ComFullName;
		if(data[i].ComAddress != undefined){
			adds[i] = {};
			adds[i].ComAddress = data[i].ComAddress;
			adds[i].ht = data[i].ht;
			/*
			myGeo.getPoint(data[i].ComAddress, function(point){
				if (point) {					
					var address = new BMap.Point(point.lng, point.lat);				
					addMarker(address,new BMap.Label(i+":"+name,{offset:new BMap.Size(20,-10)}));
				}
			}, "北京市");*/
		}else{
			adds[i] = '';
		}		
	}
	//*****描点*****//
	bdGEO();
  }
  
  /* 显示选择科目div */
	function showDiv(obj){
		$(obj).show();
		var bWidth=parseInt(document.documentElement.scrollWidth);
		var bHeight=parseInt(document.documentElement.scrollHeight);
		var back=document.createElement("div");
		back.id="back";
		var styleStr="top:0px;left:0px;position:absolute;background:#666;width:"+bWidth+"px;height:"+bHeight+"px;";
		styleStr+= "opacity:0;";
		back.style.cssText=styleStr;
		document.body.appendChild(back);
		showBackground(back,50);
	}
  
	/* 隐藏div */
	function closeDiv(obj){
		$(obj).hide();
		if(document.getElementById('back')!=null){
			document.getElementById('back').parentNode.removeChild(document.getElementById('back'));
		}
	}
	
	//让背景渐渐变暗
	function showBackground(obj,endInt){
			var al=parseFloat(obj.style.opacity);al+=0.05;
			obj.style.opacity=al;
			if(al<(endInt/100)){
				setTimeout(function(){showBackground(obj,endInt)},1);}		
	}
	
	function openInfo(content,e){
		var p = e.target;
		var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
		var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
		map.openInfoWindow(infoWindow,point); //开启信息窗口
	}
	
	// 编写自定义函数,创建标注
	function addMarker(point,label){		
		var marker;
		if(label.ht == undefined || label.ht ==''){
			marker = new BMap.Marker(point);
			map.addOverlay(marker);	
		} else{
			var icon = new BMap.Icon("/style/images/hf_icon_blue.jpg", new BMap.Size(32,37));
			marker = new BMap.Marker(point,{icon:icon});
			map.addOverlay(marker);	
		}
		
		//marker.setLabel(label);
		marker.addEventListener("mouseover",function(e){
			openInfo(label.ComAddress,e)
		});		
	}
	
	function bdGEO(){
		var add = adds[index];
		geocodeSearch(add);
		index++;
	}
	function geocodeSearch(add){
		if(index < adds.length){
			setTimeout(window.bdGEO,50);
		} 
		if(add.ComAddress != undefined && add.ComAddress != ''){
			myGeo.getPoint(add.ComAddress, function(point){
				if (point) {				
					var address = new BMap.Point(point.lng, point.lat);
					addMarker(address,add);
					//addMarker(address,new BMap.Label(add,{offset:new BMap.Size(20,-10)}));
				}
			}, "北京市");	
		}		
	}
</script>
</head>
<body>
<div class="loading">
		<div class="loading_title"></div>
		<div class="loading_msg"><span class="loading_txt"></span><span><img src="/js/loading.gif" /></span></div>
		<div class="loading_foot"></div>
</div>
<div id="container">
</div>
</body>
</html>