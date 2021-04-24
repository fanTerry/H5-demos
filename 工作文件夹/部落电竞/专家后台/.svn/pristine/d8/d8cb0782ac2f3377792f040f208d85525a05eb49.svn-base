<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-cache"/>
<title>部落(海南)电竞</title>
<link href="" rel="shortcut icon" type="image/x-icon" />

<link rel="stylesheet" href="/resources/plugins/kindeditor/4.1.11/themes/default/default.css"/>
<jsp:include page="/WEB-INF/commons/common-file.jsp"/>

<body class="admin_bg">
    <!-- 头部 -->
	<jsp:include page="/WEB-INF/commons/header.jsp"/>
    
    <div class="content clearfix">
        <!-- 左侧菜单 -->
		<jsp:include page="/WEB-INF/commons/left.jsp">
			<jsp:param name="menu" value="publish_article"/>
		</jsp:include>
        
        <div class="aside">        
            <div class="wrap1080 ppd70"> 
            	<input type="hidden" id="id" value="${tjInterpretation.id}"/>
                <p class="jd_title">文章标题：<input type="text" id="title" value="${tjInterpretation.title}" placeholder="最多只能输入40个汉字或者80个字符" maxlength="80"></p>
               
	            <div class="game_reason">
	            	<div class="intro_reason intro_reason2">
	                	<span>文章内容：</span>
	                	<div style="margin-left:80px;margin-top:-20px">
	                		<textarea id="content" placeholder="请输入专家文章内容，分析越透彻，内容越详细，查看的用户越多，您的收益也越多！
（注：内容不得含非法、虚假内容或联系方式）">${tjInterpretation.content}</textarea>
						</div>
	                </div>
	                <div class="clearfix">
	                    <a class="confirm_push confirm_push2" id="btnSubmit">发布</a>
	                    <a class="confirm_push confirm_push3" id="btnPreview">预览</a>
	                </div>                   
	            </div>
        	</div>        
    	</div>
    </div>
    
    <!-- 尾部 -->
	<jsp:include page="/WEB-INF/commons/footer.jsp"/>
	
	
	<form id="previewForm" action="/article/preview" method="post" target="_blank">
		<input type="hidden" name="title"/>
		<input type="hidden" name="content"/>
	</form>
    
<!-- 发布推荐成功 -->
    <div class="bg_rgba" style="display:none"></div>
    <div id="dialog-success" class="pub_success" style="display:none">
        <p class="sprite success_close" data-mark="x"></p>
        <p class="sprite success_logo"></p>
        <p class="success_word">恭喜你发布文章成功</p>
        <div class="success_btn">
            <a href="javascript:void(0)" class="success_look">文章查看</a>
            <a href="/article/new" class="go_pub">继续发布</a>        
        </div>
    </div>
<!-- 发布失败 -->
    <div id="dialog-error" class="pub_success" style="display:none">
        <p class="sprite success_close" data-mark="x"></p>
        <p class="sprite fail_logo"></p>
        <p class="fail_word">抱歉,发布未成功</p>
        <p class="return_word">请返回重新发布</p>
        <a href="javascript:void(0)" class="return_btn" data-mark="x">返回</a>
    </div>
<!-- 无logo -->
    <div id="dialog-alert" class="pub_success" style="display:none">
        <p class="sprite success_close" data-mark="x"></p>
        <p class="success_word success_word2">请输入内容</p>
        <a href="javascript:void(0)" class="return_btn" data-mark="x">提交</a>
    </div>

<script src="/resources/plugins/kindeditor/4.1.11/kindeditor-all.js" charset="utf-8"></script>
<script src="/resources/js/system.js"></script>
<script src="/resources/js/jiedu.js"></script>
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
	
	$("#btnPreview").click(function(){
		$("input:hidden[name=title]").val($("#title").val());
		$("input:hidden[name=content]").val($("#content").val());
		$("#previewForm").submit();
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
				url: "/article/save",
				data: data,
				success: function(json){
					$(_this).data('isLock', 0);
					
					if(json.isSuccess){
						return jiedu.dialog.success("/article/show/" + json.model);
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
		var content = $("#content").val();
		
		if(title.length == 0 || jiedu.getLength(title) > 80){
			return jiedu.dialog.alert("请输入标题且不超过40个汉字");
		}else if(content.length == 0){
			return jiedu.dialog.alert("请输入内容");
		}

		return {
			id: $("#id").val(),
			title: title,
			content: content
		}
	}
});
</script>
</body>
</html>