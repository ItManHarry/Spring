$(function(){
	//根据模块ID加载授权菜单
	$.ajax({
		url:'/web/system/menu/mms',
		type:"get",
		data:{module:module},
		dataType:'json',
		success:function(r){
			var urls = ''
			for(var i = 0; i < r.data.length; i++){
				if(i != r.data.length - 1)
					urls += "<a href = '"+r.data[i].url+"?module="+module+"' class = 'btn btn-link text-secondary'><i class = 'fa fa-angle-double-right'></i>&nbsp;&nbsp;"+r.data[i].name+"</a><br>"
				else
					urls += "<a href = '"+r.data[i].url+"?module="+module+"' class = 'btn btn-link text-secondary'><i class = 'fa fa-angle-double-right'></i>&nbsp;&nbsp;"+r.data[i].name+"</a>"
			}
			$("#more").popover({
				trigger:"focus",
				html:true,
				placement:"bottom",
				container:"body",
				content:urls
			});
		},
		error:function(){
			
		}
	})
})