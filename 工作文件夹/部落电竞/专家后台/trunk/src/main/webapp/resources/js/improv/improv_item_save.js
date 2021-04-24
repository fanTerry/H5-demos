$(document).ready(function(){	
	$("#jq_reload").click(function() {
		jiedu.dialog.closeWebdialog();
		window.location.reload();
	});
	
	$("#btnNowSubmit").click(function() {
		if(!$("#btnAgree").is(":checked")){
			return jiedu.dialog.alert("请阅读并同意部落(海南)电竞专家解读和推荐服务协议");
		}
		webAlert({title:false, content:$("#dialog-tip")[0]});
	});
  	//发布解读
	$("#btnSubmitNew").click(function(){
		jiedu.dialog.closeWebdialog();
		var _this = this;
		if($(_this).data('isLock')){return;};
		$(_this).data('isLock', 1);
		setTimeout(function(){
			$(_this).data('isLock', 0);
		}, 15000);
		
		var gameCode = $("#jq_game_code").val();
		var playCode = $("#jq_play_code").val();
		
		var voiceUrl = $("#voiceUrl").val();
		var voiceFreeSecond = $("#voiceFreeSecond").val();
		var freeSecond = $("input[name='freeSecond']:checked").val();
		var voiceSecond = $("#voiceSecond").val();
		
		var data = publish.getPostData(gameCode, playCode);
		
		data.voiceUrl=voiceUrl;
		data.voiceFreeSecond = voiceFreeSecond;
		data.freeSecond =freeSecond;
		data.voiceSecond=voiceSecond;
		
		console.log(data);
		if($.type(data) === "object"){
			$.ajax({
				contentType : 'application/json',
				type: "POST",
	         	dataType: "json",
				url: "/improv/publishWithItem",
				data: JSON.stringify(data),
				success: function(json){
					$(_this).data('isLock', 0);
					
					if(json.isSuccess){
						return jiedu.dialog.success("/interpretation/show/" + json.model);
					}else if(json.code == "101"){
						return alert(json.msg);
					}else if(json.code == "-1"){
						return jiedu.dialog.error();
					}else{
						return jiedu.dialog.alert(json.msg);
					}
				}
			});
		}
    });
 });
