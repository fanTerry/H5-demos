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
<link href="https://r.aicai.com/v2/styles/common/??global-2014.css?v=20151016100" rel="stylesheet" type="text/css">
<link href="/resources/css/style.css" rel="stylesheet" type="text/css">
<script src="/resources/plugins/jquery/jquery-1.8.3.min.js"></script>
</head>
<body class="admin_bg login_bg">
	<!-- 头部 -->
	<div class='logo_header'><span class='logo'></span>电竞专家文章发布系统</div>
    <div class="wrap480">
        <div class='welcome'>欢迎登录</div>
        <div class="log_content">
        	<form action="/login" method="post" id="do-login">
            	<div class="login_form">
            		<p class="error_txt red" style="visibility: hidden;">占位不显示</p>
                	<p class="error_txt red" id="error_msg">${msg}</p>
               		<div class="inputBox">
               			<span class="tit user_icon"></span>
               			<input type="text" id="nickName" name="nickName" placeholder="输入您的昵称" maxlength="30" />
                	</div>
                	<div class="inputBox">
                		<span class="tit pwd_icon"></span>
                		<input type="password" id="password" name="password" placeholder="请输入您的密码" maxlength="32" />
                	</div>
               		 <div class="inputBox">
               		 	<span class="tit yzm_icon"></span>
               		 	<input type="text" id="code" name="code" placeholder="请输入验证码" maxlength="4" style="width:135px;" />
                        <a href="javascript:void(0)" class="yzm"><img src="/captcha?nocache="+new Date().getTime() id="refreshCaptha"></a>
                	</div>
                	<a href="javascript:void(0)" class="submit" id="submit">立即登录</a>
            	</div>
           	</form>
        </div> 
    </div>
    <div class="footer">部落(海南)电竞科技有限公司</div>
<script>
$(document).ready(function(){
	
	$("#refreshCaptha").click(function(){
		$(this).attr({src : "/captcha?nocache=" +new Date().getTime()});
	});
	
	$("#submit").click(function(){
		var $nickName = $("#nickName");
		var $password = $("#password");
		var $code = $("#code");
		
		if($nickName.val() == '' || $nickName.val() == null){
			$("#error_msg").text("请输入昵称");
			$nickName.focus();
			return;
		}
		if($password.val() == '' || $password.val() == null){
			$("#error_msg").text("请输入密码");
			$password.focus();
			return;
		}
		if($code.val() == '' || $code.val() == null){
			$("#error_msg").text("请输入验证码");
			$code.focus();
			return;
		}
		
		if($nickName.val().length > 22){
			$("#error_msg").text("昵称过长");
			$account.focus();
			return;
		}
		if($password.val().length > 32){
			$("#error_msg").text("密码过长");
			$password.focus();
			return;
		}
		if($code.val().length != 4){
			$("#error_msg").text("验证码必须是4位");
			$code.focus();
			return;
		}
		$("#do-login").submit();
		
	});
	
})
</script>
</body>
</html>
