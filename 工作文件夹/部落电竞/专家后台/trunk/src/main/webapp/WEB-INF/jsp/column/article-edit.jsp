<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-cache" />
<title><c:if test="${!empty tjExpertColumnArticle.id}">编辑</c:if><c:if test="${empty tjExpertColumnArticle.voiceUrl}">发布</c:if>专栏文章</title>
<link href="" rel="shortcut icon" type="image/x-icon" />

<link rel="stylesheet" href="/resources/plugins/kindeditor/4.1.11/themes/default/default.css" />
<jsp:include page="/WEB-INF/commons/common-file.jsp" />
<body class="admin_bg">
	<!-- 头部 -->
	<jsp:include page="/WEB-INF/commons/header.jsp" />

	<div class="content clearfix">
		<!-- 左侧菜单 -->
		<c:if test="${!empty tjExpertColumnArticle.id}">
		<jsp:include page="/WEB-INF/commons/left.jsp">
			<jsp:param name="menu" value="column_article" />
		</jsp:include>
		</c:if>
		
		<c:if test="${empty tjExpertColumnArticle.id}">
		<jsp:include page="/WEB-INF/commons/left.jsp">
			<jsp:param name="menu" value="column_article_add" />
		</jsp:include>
		</c:if>

		<div class="aside">
			<div class="wrap1080 ppd70">
				<input type="hidden" id="id" name="id" value="${tjExpertColumnArticle.id}" />
				<input type="hidden" id="columnId" name="columnId"  value="${tjExpertColumnArticle.columnId}" />
				<input type="hidden" id="voiceUrl" name="voiceUrl" value="${tjExpertColumnArticle.voiceUrl}" />
				<input type="hidden" id="voiceLength" name="voiceLength" value="${tjExpertColumnArticle.voiceLength}" />
				<input type="hidden" id="fileSize" name="fileSize" value="${tjExpertColumnArticle.fileSize}" />
				<input type="hidden" id="fileName" name="fileName" value="${tjExpertColumnArticle.fileName}" />
				<p class="jd_title">
						文章标题：<input type="text" id="title" name="title" value="${tjExpertColumnArticle.title}"
						placeholder="不超过40个字" maxlength="80">
				</p>
				<div class="jd_zhaiyao ml40 pt30">
					<span class="fl fs14" style="display:block; width:170px;">文章摘要：</span>
					<textarea id="describe" name="describe" rows="3" cols="20" placeholder="不超过250个字">${tjExpertColumnArticle.describe}</textarea>
				</div>
				<div class="mt30">
					<p class="jd_title" style="display: inline-block; padding-top: 0; vertical-align: middle;">语音内容：</p>
					<div class="profit_price" style="display: inline-block; vertical-align: middle; margin-left: 100px;">
						<input type="radio" name="enableVoice" value="false" <c:if test="${empty tjExpertColumnArticle.voiceUrl}">checked</c:if> /> 不启用
						<input type="radio" name="enableVoice" value="true" <c:if test="${!empty tjExpertColumnArticle.voiceUrl}">checked</c:if> /> 启用
					</div>
				</div>
				<div class="mt30" id="chooseFileDiv" <c:if test="${empty tjExpertColumnArticle.voiceUrl}">style="display:none"</c:if>>
					<div class="profit_price" style="margin-left: 220px;padding-bottom:10px">
					<input type="file" id="audioFile" style="display:none;" name="audioFile" />
					<span id="uploadMsg" style="display:none;">开始上传</span>
            		<button id="fileUpload" style="width:80px; height:30px; margin-left:20px; font-size:12;" >选择文件</button>&nbsp;&nbsp;&nbsp;&nbsp;格式支持mp3，文件大小不超过20M，语音时长不超过20分钟
					</div>
					<div class="profit_price" id="playAudio" style="float:left;width:100%;margin-left: 220px;vertical-align: middle;">
						<video id="player" controls="controls" controls="controls" style="height:50px;width:400px" type="video/mp4" <c:if test="${!empty tjExpertColumnArticle.voiceUrl}">src="${baseUrl}${tjExpertColumnArticle.voiceUrl}"</c:if> ></video>
						<c:if test="${empty tjExpertColumnArticle.voiceUrl}">
						<span id="fileInfo" style="display:none;"></span>
						</c:if>
						<c:if test="${!empty tjExpertColumnArticle.voiceUrl}">
						<span id="fileInfo">${tjExpertColumnArticle.fileSize}&nbsp;&nbsp;${tjExpertColumnArticle.fileName}</span>
						</c:if>
					</div>
				</div>
			
				<div class="game_reason">
					<div class="intro_reason intro_reason2">
						<span>文章内容：</span>
						<div style="margin-left: 80px; margin-top: -20px">
							<textarea id="content">${tjExpertColumnArticle.content}</textarea>
						</div>
					</div>
					<p class="pl100 mt20 c666">
						<input type="checkbox" name="isFree" id="isFree" value="1" <c:if test="${tjExpertColumnArticle.isFree==1}">checked</c:if> />本篇可以试读
					</p>
					<p class="pl100 mt20 c666">
                    	<input type="checkbox" id="btnAgree" checked>&nbsp;
                    	<a href="/publish/agree" style="color:#444444" target="_blank">我已阅读并同意《部落(海南)电竞专家解读和推荐服务协议》</a>
                    </p> 
					<div class="clearfix">
						<a class="confirm_push confirm_push2" id="btnSubmit">确定</a>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 尾部 -->
	<jsp:include page="/WEB-INF/commons/footer.jsp" />

	<!-- 发布成功 -->
	<div class="bg_rgba" style="display: none"></div>
	<div id="dialog-success" class="pub_success" style="display: none">
		<p class="sprite success_close" data-mark="x"></p>
		<p class="sprite success_logo"></p>
		<p class="success_word">文章保存成功</p>
		<div class="success_btn">
			<a href="javascript:void(0)" class="success_look">文章查看</a> <a href="/column/newArticle" class="go_pub">继续发布</a>
		</div>
	</div>
	<!-- 发布失败 -->
	<div id="dialog-error" class="pub_success" style="display: none">
		<p class="sprite success_close" data-mark="x"></p>
		<p class="sprite fail_logo"></p>
		<p class="fail_word">抱歉,发布未成功</p>
		<p class="return_word">请返回重新发布</p>
		<a href="javascript:void(0)" class="return_btn" data-mark="x">返回</a>
	</div>
	<!-- 无logo -->
	<div id="dialog-alert" class="pub_success" style="display: none">
		<p class="sprite success_close" data-mark="x"></p>
		<p class="success_word success_word2">请输入内容</p>
		<a href="javascript:void(0)" class="return_btn" data-mark="x">提交</a>
	</div>

	<script src="/resources/plugins/kindeditor/4.1.11/kindeditor-all.js" charset="utf-8"></script>
	<script src="/resources/js/system.js"></script>
	<script src="/resources/js/jiedu.js"></script>
	<script type="text/javascript" src="/resources/js/uploadImg/ajaxfileupload.js"></script>
	<script>
KindEditor.ready(function(K) {
	editor = K.create('#content', {
		width:'100%',
		height:400,
		items: [
		        'source', '|', 'undo', 'redo', '|', 'preview', 'cut', 'copy', 'paste',
		        'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
		        'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 
		        'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
		        'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
		        'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image',
		        'table', 'hr', 'pagebreak',
		        'anchor', 'link', 'unlink'
		],
		resizeType : 1,
		allowImageRemote: false,	//禁止网络图片
		allowPreviewEmoticons : false,
		allowImageUpload : true,
		formatUploadUrl : false,
		uploadJson : "/image/upload",
		afterBlur : function(){
            this.sync();
		}
	});
});

$(document).ready(function(){
	
	$("#fileUpload").click(function() {
		$("#audioFile").click();
	});

	$('#audioFile').live('change',function(){
		ajaxFileUpload("audioFile");
	})
	
	$("input[name='enableVoice']").click(function() {
		if ($(this).val() == 'false') {
			$("#freeTypeDiv").hide();
			$("#chooseFileDiv").hide();
		}else
			{
			$("#freeTypeDiv").show();
			$("#chooseFileDiv").show();
			}
	});

	
	$("#btnSubmit").click(function(){
		var _this = this;
		if($(_this).data('isLock')){return;};
		$(_this).data('isLock', 1);
		
		var data = getPostData();
		if($.type(data) === "object"){
			$.ajax({
				traditional: true,
				type: "POST",
             	dataType: "json",
				url: "/column/saveArticle",
				data: data,
				success: function(json){
					$(_this).data('isLock', 0);
					if(json.isSuccess){
						return jiedu.dialog.success("/column/showArticle/" + json.model);
					}else{
						return jiedu.dialog.error();
					}
				}
			});
		}else{
			$(_this).data('isLock', 0);
		}
	});
	
	getPostData = function(){
		var title = $.trim($("#title").val());
		var describe = $.trim($("#describe").val());
		var content = $("#content").val();
		var voiceUrl = $("#voiceUrl").val();
		var voiceLength = $("#voiceLength").val();
		var fileSize = $("#fileSize").val();
		var fileName = $("#fileName").val();
		var isFree = 0;
		var enableVoice = $('input[name="enableVoice"]:checked').val();
		if ($('#isFree').attr('checked')) {
			isFree =1;
		}
		if(title.length == 0 || jiedu.getLength(title) > 80){
			return jiedu.dialog.alert("请输入标题且不超过40个汉字");
		}else if(describe.length == 0 || jiedu.getLength(describe) > 250){
			return jiedu.dialog.alert("请输入摘要且不超过250个汉字");
		}else if(content.length == 0){
			return jiedu.dialog.alert("请输入内容");
		}
		return {
			id: $("#id").val(),
			title: title,
			describe: describe,
			content: content,
			voiceUrl: voiceUrl,
			voiceLength: voiceLength,
			fileSize: fileSize,
			fileName: fileName,
			enableVoice: enableVoice,
			isFree: isFree
		}
	}
});

/** fileUpload为file的id* */
function ajaxFileUpload(fileUpload) {
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
	beginUpload();
	$.ajaxFileUpload({
		url : '/column/uploadVoiceFile',
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
		}
	});
	return false;
}

function beginUpload()
{
	$("#voiceUrl").val('');
	$("#voiceLength").val('');
	$("#fileSize").val('');
	$("#fileName").val('');
	$("#uploadMsg").text('开始上传...');
	$("#uploadMsg").fadeIn();
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

function successUpload(filePath,playerUrl,fileSize,fileName,voiceLength)
{
	$("#uploadMsg").text('上传成功!');	
	$("#fileInfo").fadeIn();
	$("#fileInfo").html(fileName+"&nbsp;&nbsp;"+fileSize);
	$("#player").attr("src",playerUrl);
	$("#voiceUrl").val(filePath);
	
	$("#voiceLength").val(voiceLength);
	$("#fileSize").val(fileSize);
	$("#fileName").val(fileName);
}
</script>
</body>
</html>