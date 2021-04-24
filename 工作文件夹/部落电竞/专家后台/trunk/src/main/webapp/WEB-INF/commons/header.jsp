<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="header">
	<div class='wrapper'>
		<div class="wrap1310">
			<div class='logo_header'><span class='logo'></span>电竞专家文章发布系统</div>
			<div class="ststem_right clearfix">
				<img src='https://daily-rs.esportzoo.com/upload/interface/avatar/dccf8c6f23_145630.jpg'>
				<span>${nickName},欢迎您！</span>
				<c:if test="${!isLoginPage }">
					<a href="/user/editPwd">修改密码</a>
					<a href="/loginOut">退出登录</a>
				</c:if>
			</div>   
		</div>
	</div>
</div>
