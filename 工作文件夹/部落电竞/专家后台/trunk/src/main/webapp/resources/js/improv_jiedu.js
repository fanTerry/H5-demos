$(document).ready(function(){
	$("input[name='publishType']").on("click", function(){
		var _this = $(this);
		if(_this.val() == 1) {
			$("#jq_return_rate").show();
			$("#jq_content_area").show();
			$("#enableVoiceDiv").show();
			
			$("#jq_improv_time").hide();
			$("#jq_improv_desc_area").hide();
			$("#jq_improv_publish").hide();
			$("#improVoiceDiv").hide();
			$("#jq_publish_div").show();
		}else{
			$("#jq_return_rate").toggle();
			$("#jq_content_area").toggle();
			$("#jq_improv_time").toggle();
			$("#jq_improv_desc_area").toggle();
			$("#jq_improv_publish").toggle();
			$("#jq_publish_div").toggle();
			$("#improVoiceDiv").toggle();
			$("#voiceUrlDiv").hide();
			$("#enableVoiceDiv").hide();
			clearSelectedItem.clear();
		}
	});
	
	//临场解读语音上传
	$("#summaryFileUpload").click(function() {
		$("#summaryAudioFile").click();
	});
  	
	$('#summaryAudioFile').live('change',function(){
		ajaxSummaryFileUpload("summaryAudioFile");
	})

  	$("input[name='enableSummaryVoice']").click(function(e){
		var option = $(this).val();
		if(0 == option){
			$("#summaryDiv").hide(); 
		} else {
			$("#summaryDiv").show();
			$("#improvVoiceUrl").val(''); 
			$("#improvVoiceSecond").val('');
		}
	});
  	
  	/** fileUpload为file的id* */
  	function ajaxSummaryFileUpload(fileUpload) {  		
  		var file = $("#" + fileUpload).val();
  		if (file == "") {
  			alert("请选择上传的语音文件");
  			return;
  		}
  		var reg = /\.mp3$/i;

  		if (!reg.test(file)) {
  			alert('请上传mp3格式');
  			return;
  		}
  		beginSimmaryUpload();
  		$.ajaxFileUpload({
  			url : '/publish/uploadImprovVoiceFile',
  			secureuri : false,
  			fileElementId : fileUpload,
  			dataType : 'text',
  			success : function(data) {
  				var json = getJsonData(data);
  				if(json.isSuccess) {
  					var model = json.model;
  					successSummaryUpload(model.filePath,model.playerUrl,model.fileSize,model.fileName,model.voiceLength);
  				}else {
  					$("#uploadSummaryMsg").text(json.msg);
  					alert(json.msg);
  				}
				$("#summaryFileUpload").text("重新上传");
  			}
  		});
  		return false;
  	}

  	function beginSimmaryUpload()
  	{
  		$("#uploadSummaryMsg").text('开始上传...');
  		$("#uploadSummaryMsg").fadeIn();
  	}

  	function getJsonData(data)
  	{
  		var dataStr = data.toString();
  		if(dataStr != '') {
  			dataStr = dataStr.replace('<pre style="word-wrap: break-word; white-space: pre-wrap;">','');
  			dataStr = dataStr.replace('</pre>','');
  		}
  		return eval('(' + dataStr + ')'); 
  	}
  	
  	function successSummaryUpload(filePath,playerUrl,fileSize,fileName,voiceLength)
  	{
  		$("#uploadSummaryMsg").text('上传成功!');	
  		$("#summaryFileInfo").fadeIn();
  		$("#summaryFileInfo").html(fileName+"&nbsp;&nbsp;"+fileSize);
  		$("#summaryPlayer").attr("src",playerUrl);
  		
  		$("#improvVoiceUrl").val(filePath);
  		if(voiceLength==0)
  		{
  			setAudioLength($("#summaryPlayer"),$("#improvVoiceSecond"));
  			return;
  		}
  		$("#improvVoiceSecond").val(voiceLength);
  	}
  	
  	function setAudioLength(obj,target)
  	{
  		obj.load(function(){
  		    var time = obj.get(0).duration;
  		    target.val(time);
  		});
  	}
  	
	//普通解读语音上传
	$("#fileUpload").click(function() {
		$("#audioFile").click();
	});
  	
	$('#audioFile').live('change',function(){
		ajaxFileUpload("audioFile");
	})
  	
  	/** fileUpload为file的id* */
  	function ajaxFileUpload(fileUpload) {  		
  		var file = $("#" + fileUpload).val();
  		if (file == "") {
  			alert("请选择上传的语音文件");
  			return;
  		}
  		var reg = /\.mp3/i;

  		if (!reg.test(file)) {
  			alert('请上传mp3格式');
  			return;
  		}
  		beginUpload();
  		$.ajaxFileUpload({
  			url : '/publish/uploadVoiceFile',
  			secureuri : false,
  			fileElementId : fileUpload,
  			dataType : 'text',
  			success : function(data) {
  				var json = getJsonData(data);
  				if(json.isSuccess) {
  					var model = json.model;
  					successUpload(model.filePath,model.playerUrl,model.fileSize,model.fileName,model.voiceLength);
  				}else {
  					$("#uploadMsg").text(json.msg);
  					alert(json.msg);
  				}
				$("#fileUpload").text("重新上传");
  			}
  		});
  		return false;
  	}

  	function beginUpload()
  	{
  		$("#uploadMsg").text('开始上传...');
  		$("#uploadMsg").fadeIn();
  	}

  	function successUpload(filePath,playerUrl,fileSize,fileName,voiceLength)
  	{
  		$("#uploadMsg").text('上传成功!');	
  		$("#fileInfo").fadeIn();
  		$("#fileInfo").html(fileName+"&nbsp;&nbsp;"+fileSize);
  		$("#player").attr("src",playerUrl);
  		
  		$("#voiceUrl").val(filePath);
  		if(voiceLength==0)
  		{
  			setAudioLength($("#player"),$("#voiceSecond"));
  			return;
  		}
  		$("#voiceSecond").val(voiceLength);
  		
  		//设置试听时长
  		var freeSecond = $("input[name='freeSecond']:checked").val();
  		if(freeSecond==1)
  		{
  	  	    $('#voiceFreeSecond').val(voiceLength); 
  		}else
  			{
  			$("#voiceFreeSecond").val($('#setFreeSecond').val());
  			}
  	}
  
  	
  	$("input[name='enableVoice']").click(function(e){
		var option = $(this).val();
		if(option == 1){
			$("#voiceUrlDiv").show(); 
		} else {
			$("#voiceUrlDiv").hide();
			$("#voiceUrl").val(''); 
			$("#voiceSecond").val('');
			$("#fileInfo").html(''); 
		}
	});
  	
  	
  	$("input[name='freeSecond']").click(function(e){
		var option = $(this).val();
		if(option == 1){
			//整条均可免费听  让 voiceFreeSecond = voiceSecond
			var voiceSecond = $("#voiceSecond").val(); 
			if(voiceSecond=='')
			{
				return;
			}
				$("#voiceFreeSecond").val(voiceSecond);
		}else
			{
				$("#voiceFreeSecond").val($('#setFreeSecond').val());
			}
	});
  	
  	//监听值的变化
  	$('#setFreeSecond').bind('input propertychange', function() { 
  		var freeSecond = $("input[name='freeSecond']:checked").val();
  		if(freeSecond==0)
  		{
  	  	    $('#voiceFreeSecond').val($(this).val()); 
  		} 
  	}); 
  	
	//选择查看价格效果
	$("#priceList li").on("click", function(){
		if($(this).hasClass("press")){return;}
		
		$(this).siblings('li').removeClass('press');
		$(this).toggleClass("press");
		
		var memberId = $("#jq_member_id").val();
		var race_type = $("#jq_race_type").val();
		var value = parseInt($(this).data("price")) * GLOBAL_COMMISSION;
		$("#commission").text(value ? value.toFixed(2) : 0);
		if($(this).data("price") == 0) {
			$("#jq_free_type_refund").hide();
			$("#jq_free_type_list").show();
			$(".shouyi shouyi2").hide();
		} else {
			$("#jq_free_type_list").hide();
			if(race_type == 3 || race_type == 4){ //14场 任9隐藏
				$("#jq_free_type_refund").hide();
				return;
			}
			$("#jq_free_type_refund").show();
			$.ajax({
				type: "post",
				async : false,
				url: "/publish/checkRefundNotWin",
				data: {memberId: memberId},
				success : function(result){
					if(result.isSuccess) {
						$("#jq_limit_count").text(result.model);
						$("#jq_free_type_refund").show();
					} else {
						$("#jq_free_type_refund").hide();
					}
				}
			});
		}
	});
	
	$("#jq_publish_time").click(function() {
		$("#jq_publish_time").show();
	});
	
	if($("#jq_publish_time").length>0){
		$("#jq_publish_time").datetimepicker({format: 'yyyy-mm-dd hh:ii'});
	}
	
	$("#btnImprovSubmit").on("click", function() {
		var summary = $("#jq_summary").val();
		var title = $.trim($("#title").val());
		var improvDesc = $("#jq_improv_desc").val();
		var price = parseInt($("#priceList li[class=press]").data("price"));
		var passType = $("#jq_pass_type").val();
		var raceType = $("#jq_race_type").val();
		var improvGameId = $("#jq_improv_gameId").val();
		var publishTime = $("#jq_publish_time").val();
		var issueNo = $("#issueNo").val();
		var improvVoiceUrl = $("#improvVoiceUrl").val();
		var improvVoiceSecond = $("#improvVoiceSecond").val();
		
		if(!publishTime) {
			return jiedu.dialog.alert("请设置预发布时间");
		}
		var freeType;
		var refund;
		$("#priceList li").each(function() {
			if($(this).hasClass("press")) {
				if($(this).data("price") == 0) {
					freeType = $('input:radio[name="freeType"]:checked').val();
				} else {
					freeType = 1;
					refund = $('input:checkbox[name="refundNotWin"]:checked').val();
				}
			}
		});
		
		var data = {};
		if(summary.length < 20 || summary.length>200){
			return jiedu.dialog.alert("请输入摘要且最多输入20-200个字符");
		}
		if(title.length == 0 || jiedu.getLength(title) > 80){
			return jiedu.dialog.alert("请输入标题且最多输入40个汉字或80个字符");
		}else if(improvDesc.length == 0){
			return jiedu.dialog.alert("请输入解读内容");
		}else if(jiedu.getLength(improvDesc) > 60000){
			return jiedu.dialog.alert("您的解读内容超过最大长度");
		}else if(isNaN(price)){
			return jiedu.dialog.alert("请选择查看价格");
		}
		if(!validImprovVoiceUpload())
		{
			return jiedu.dialog.alert("您还未上传语音文件,或者不启用语音内容");
		}
		data.title = title;
		data.summary = summary;
		data.improvDesc = improvDesc;
		data.price = price;
		data.freeType = freeType;
		data.raceType = raceType;
		data.content = "敬请期待";
		data.passType = passType;
		data.improvGameId = improvGameId;
		data.publishTime = publishTime;
		data.refundNotWin = refund;
		data.improvVoiceUrl = improvVoiceUrl;
		data.improvVoiceSecond = improvVoiceSecond;
		if(issueNo) {
			data.issueNo = issueNo;
		}
		console.log(data);
		$.ajax({
			contentType : 'application/json',
			type: "POST",
         	dataType: "json",
			url: "/improv/prePublish",
			data: JSON.stringify(data),
			success: function(json){
				//$(_this).data('isLock', 0);
				if(json.isSuccess){
					var uri = "";
					if(!json.model) {
						uri = "/interpretation";
					} else {
						uri = "/interpretation/show/" + json.model;
					}
					return jiedu.dialog.success(uri);
				}else if(json.code == "101"){
					return alert(json.msg);
				}else if(json.code == "-1"){
					return jiedu.dialog.error();
				}else{
					return jiedu.dialog.alert(json.msg);
				}
			}
		});

	})
});

function validImprovVoiceUpload()
{
	if($("input[name='enableSummaryVoice']").length>0){
		var enableVoice = $("input[name='enableSummaryVoice']:checked").val();
		var improvVoiceUrl = $("#improvVoiceUrl").val();
		if(enableVoice==1&& improvVoiceUrl=='')
		{
			return false;
		}
	}
	return true;
}

function validVoiceUpload()
{
	if($("input[name='enableVoice']").length>0){
		var enableVoice = $("input[name='enableVoice']:checked").val();
		var voiceUrl = $("#voiceUrl").val();
		if(enableVoice==1&& voiceUrl=='')
		{
			return false;
		}
	}
	return true;
}
