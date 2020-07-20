$(function(){	
	//$('[data-toggle="tooltip"]').tooltip();
	$('#logout').popover({
		trigger:"focus",
		html:true,
		title:"You want to ?"
	});	
})
/**
 * 显示关闭模式窗体
 * @param id		模式窗体id
 * @param flag		1：显示 0：关闭
 * @returns
 */
function modalSwitch(id, flag){
	if(flag == 1)
		$("#"+id).modal('show');
	else
		$("#"+id).modal('hide');
}
/**
 * 关闭所有的弹出框
 * @returns
 */
function hidePops(){
	$("[data-toggle='popover']").popover("hide");	
}
function test(){
	alert('For test')
}