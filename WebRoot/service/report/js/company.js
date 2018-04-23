$(function(){
	var div = '<div class="com_box">'+
					'<table class="com_box_head">'+
						'<thead>'+
							'<tr>'+
								'<td>门店编码</td>'+
								'<td>门店名称</td>'+
								'<td>拼音码</td>'+
							'</tr>'+
						'</thead>'+
					'</table>'+
					'<div class="com_box_list_div">'+
					'<table class="com_box_list">'+					
							'<tbody>'+
							'</tbody>'+					
						'</table>'+
					'</div>'+
				'</div>';
	var obj = $(div);
	$(".search_company").after(obj);
	obj.css("margin-left",'-'+($(".search_company").width()+5)+'px')
	obj.css("margin-top",($(".search_company").height()+5)+'px')
	
	$(".search_company").bind('keyup',function(){
		search_queryCom();
	})
	$(document.body).on("click",".com_box_list .list_item",function(){
		  $(".search_company").val($(this).children(":eq(1)").text());
		  $(".search_company").attr('companyCode',$(this).children(":eq(0)").text())
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
})

/*******门店搜索相关******/
function search_queryCom(){
	var obj = $(".search_company");
	var robj = $('.com_box_list tbody');
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