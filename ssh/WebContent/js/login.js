$(function(){
	alert("aaaa");
	$("#email").focus().select();
	$("#email").blur(function(){
		$.post("userAction_regEmail.action",null,function(){
			
		});
	})
});