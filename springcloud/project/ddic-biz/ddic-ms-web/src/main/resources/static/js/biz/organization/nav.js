var urlHtml = "<a href = '/web/biz/company/list' class = 'btn btn-link text-secondary'><i class = 'fa fa-angle-double-right'></i>&nbsp;&nbsp;公司信息</a><br>"
	+"<a href = '/web/biz/department/list' class = 'btn btn-link text-secondary'><i class = 'fa fa-angle-double-right'></i>&nbsp;&nbsp;部门信息</a><br>" 
	+"<a href = '#' class = 'btn btn-link text-secondary'><i class = 'fa fa-angle-double-right'></i>&nbsp;&nbsp;雇员信息</a>" 
$(function(){
	$("#more").popover({
		trigger:"focus",
		html:true,
		placement:"bottom",
		container:"body",
		content:urlHtml
	});
})