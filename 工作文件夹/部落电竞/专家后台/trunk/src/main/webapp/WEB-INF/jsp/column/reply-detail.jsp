<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>留言详情</title>
<link href="" rel="shortcut icon" type="image/x-icon" />
<jsp:include page="/WEB-INF/commons/common-file.jsp" />
<body class="admin_bg">
	<!-- 头部 -->
	<jsp:include page="/WEB-INF/commons/header.jsp" />

	<div class="content clearfix">
		<!-- 左侧菜单 -->
		<jsp:include page="/WEB-INF/commons/left.jsp">
			<jsp:param name="menu" value="column_reply" />
		</jsp:include>

		<div class="aside">
			<div class="wrap1080 ppd70">
				<input type="hidden" id="replyId" name="replyId" value="${memberReply.id}" />
				<input type="hidden" id="sourceId" name="sourceId" value="${memberReply.sourceId}" />
				<div class="jd_zhaiyao ml40 pt30">
					<span class="fl fs14" style="display:block; width:170px;">用户昵称：</span>
					${memberReply.nickName}
				</div>
				<div class="jd_zhaiyao ml40 pt30">
					<span class="fl fs14" style="display:block; width:170px;">留言内容：</span>
					${memberReply.content}
				</div>

				<div class="jd_zhaiyao ml40 pt30">
					<span class="fl fs14" style="display: block; width: 170px;">我的回复：</span>
					<c:if test="${empty expertReply}">
					<textarea id="content" name="content" rows="3" cols="20" placeholder="不超过120个字"></textarea>
					</c:if>
					<c:if test="${!empty expertReply}">
					${expertReply.content}
					</c:if>
				</div>
				<c:if test="${empty expertReply}">
				<div class="game_reason">
					<div class="clearfix">
						<a class="confirm_push confirm_push2" id="btnSubmit">确定</a>
					</div>
				</div>
				</c:if>
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
		<p class="success_word">回复成功</p>
		<div class="success_btn">
			<a href="/column/replyDetail/${memberReply.id}" class="go_pub">查看回复</a>
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

	<script src="/resources/js/system.js"></script>
	<script src="/resources/js/jiedu.js"></script>
	<script>

$(document).ready(function(){

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
				url: "/column/expertReply",
				data: data,
				success: function(json){
					$(_this).data('isLock', 0);
					if(json.isSuccess){
						return jiedu.dialog.success("/column/listReply");
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
		var replyId = $.trim($("#replyId").val());
		var content = $.trim($("#content").val());
		var sourceId = $.trim($("#sourceId").val());
		if(content.length == 0 || jiedu.getLength(content) > 250){
			return jiedu.dialog.alert("请输入内容且不超过120个汉字");
		}
		return {
			replyId: replyId,
			sourceId: sourceId,
			content: content
		}
	}
});
</script>
</body>
</html>