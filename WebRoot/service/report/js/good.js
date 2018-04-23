$(function(){
	var div = '<div class="goods_box">'+
					'<table class="goods_box_head">'+
					'<thead>'+
					'<tr>'+
					'<td>商品编号</td>'+
					'<td>商品名称</td>'+							
					'</tr>'+
					'</thead>'+
					'</table>'+
					'<div class="goods_box_list_div">'+
						'<table class="goods_box_list">'+					
							'<tbody>'+
							'</tbody>'+					
						'</table>'+
					'</div>'+
				'</div>';
	var obj = $(div);
	$(".search_good").after(obj);
	obj.css("margin-left",'-'+($(".search_good").width()+5)+'px')
	obj.css("margin-top",($(".search_good").height()+5)+'px')
	$(".search_good").bind('keyup',function(){
		search_queryGood();
	})
	
	$(document.body).on("click",".goods_box_list .list_item",function(){
		  $(".search_good").val($(this).children(":eq(1)").text());
		  $(".search_good").attr("goodCode",$(this).children(":eq(0)").text());	  	  
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
        $('.goods_box').css('display','none'); 
    });	
})
/*******商品搜索相关******/
function search_queryGood(){		
	var obj = $(".search_good");
	var robj = $('.goods_box_list tbody');
	var callBack={				
		success:function(data){				
			if(data.code=='OK'){
				var htm = '';
				//****拼装商品列表
				var list = data.data;
				for(var o in list){						
					htm += '<tr class="list_item">';
					htm += '<td>'+list[o].goodsNumber+'</td><td>'+list[o].GoodsFullName+'</td>';
					htm += '</tr>';
				}		
				robj.html(htm);
			} else{						
				
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
