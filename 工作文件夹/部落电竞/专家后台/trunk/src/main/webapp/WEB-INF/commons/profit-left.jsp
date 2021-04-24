<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="nav">
	<div class="pl50 pr lh42 <c:if test="${param.menu == 'profit_index'}">c_aadd bg_3bbb79</c:if>">
    	<i class="fb_icon fbwz <c:if test="${param.menu == 'profit_index'}">fbwz_ch</c:if>"></i>
    	<a href="/profit/index">发布推荐</a>
    </div>
    <div class="pl50 pr lh42 <c:if test="${param.menu == 'publish_record'}">c_aadd bg_3bbb79</c:if>">
    	<i class="fb_icon fbjl <c:if test="${param.menu == 'publish_record'}">fbjl_ch</c:if>"></i>
    	<a href="/profit/publishRecord">发布推荐记录</a>
    </div>
    <div class="pl50 pr lh42 <c:if test="${param.menu == 'publish_period'}">c_aadd bg_3bbb79</c:if>">
    	<i class="fb_icon fbjl <c:if test="${param.menu == 'publish_period'}">fbjl_ch</c:if>"></i>
    	<a href="/profit/periodRecord">发布期数记录</a>
    </div>
    <div class="pl50 pr lh42 <c:if test="${param.menu == 'order-record'}">c_aadd bg_3bbb79</c:if>">
    	<i class="fb_icon fbjl <c:if test="${param.menu == 'order-record'}">fbjl_ch</c:if>"></i>
    	<a href="/profit/orderRecord">订购记录</a>
    </div>    
   	<div class="pl50 pr lh42 <c:if test="${param.menu == 'my_income'}">c_aadd bg_3bbb79</c:if>">
   		<i class="fb_icon lygl <c:if test="${param.menu == 'my_income'}">lygl_ch</c:if>"></i>
   		<a href="/income">我的收益</a>
   	</div>
   	<div class="pl50 pr lh42 <c:if test="${param.menu == 'plan_record'}">c_aadd bg_3bbb79</c:if>">
   		<i class="fb_icon lygl <c:if test="${param.menu == 'plan_record'}">lygl_ch</c:if>"></i>
   		<a href="/profit/planRecord">计划管理</a>
   	</div>	
   	<div class="pl50 pr lh42 <c:if test="${param.menu == 'reply'}">c_aadd bg_3bbb79</c:if>">
    	<i class="fb_icon fbwz <c:if test="${param.menu == 'reply'}">fbwz_ch</c:if>"></i>
    	<a href="/replyForProfPlan">盈利计划评论管理</a>
    </div>
</div>
		


