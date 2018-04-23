<script type="text/javascript">
	$(function(){
		$(document.body).on("click",".list_item",function(){
			  $(".companyCode").val($(this).children(":eq(0)").text());
		  });
		 $(document).bind('click',function(e){
	         var e = e || window.event;
	         var elem = e.target || e.srcElement;
	         while (elem) {
	             if (elem.className && elem.className.indexOf('com_list')>-1) {
	                 return;
	             }
	             elem = elem.parentNode;
	         }
	         $('.com_box').css('display','none'); 
	     });
	
		 $(document).bind('click',function(e){
	         var e = e || window.event;
	         var elem = e.target || e.srcElement;
	         while (elem) {
	             if (elem.className && elem.className.indexOf('com_list')>-1) {
	                 return;
	             }
	             elem = elem.parentNode;
	         }
	         $('.com_box').css('display','none'); 
	     });
	});
	/*******门店搜索相关******/
	function queryCom(obj,robj){		
		var callBack={				
			success:function(data){				
				if(data.code=='OK'){
					var htm = '';
					//****拼装门店列表
					var list = data.data;
					for(var o in list){						
						htm += '<tr class="list_item">';
						htm += '<td>'+list[o].classCode+'</td><td>'+list[o].ComFullName+'</td><td>'+list[o].ComFullNamePYM+'</td>';
						htm += '</tr>';
					}		
					robj.html(htm);
				} else{						
					
				}
				$(".com_box").css("display","inherit");
			},
			complete:function(){				
				$(".com_box").css("display","inherit");
			}
		}
		var url = "ReportApi.jsp";
		var data ={
			op:'queryCom',
			keyword:$(obj).val(),		
		};
		commitData(url,data,callBack);
	}
	
	/*******商品搜索相关******/
	function queryGood(obj,robj){		
		var callBack={				
			success:function(data){				
				if(data.code=='OK'){
					var htm = '';
					//****拼装门店列表
					var list = data.data;
					for(var o in list){						
						htm += '<tr class="list_item">';
						htm += '<td>'+list[o].classCode+'</td><td>'+list[o].ComFullName+'</td><td>'+list[o].ComFullNamePYM+'</td>';
						htm += '</tr>';
					}		
					robj.html(htm);
				} else{						
					alert("查询失败！");
				}
				$(".goods_box").css("display","inherit");
			},
			complete:function(){				
				$(".goods_box").css("display","inherit");
			}
		}
		var url = "ReportApi.jsp";
		var data ={
			op:'queryGood',
			keyword:$(obj).val(),		
		};
		commitData(url,data,callBack);
	}
	
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
				alert("操作异常");				
			};
		}
		jQuery.ajax({
			url : url,
			dataType : dataType,
			type : type,
			data : obj,
			traditional:true,
			success : callbackfun.success,
			error:callbackfun.error
		});
	}
</script>	 	 