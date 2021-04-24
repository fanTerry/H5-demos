<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!doctype html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-cache"/>
<title>部落(海南)电竞</title>
<link href="" rel="shortcut icon" type="image/x-icon" />
<jsp:include page="/WEB-INF/commons/common-file.jsp"/>
<style type="text/css">
.sub_table_border_color  {border:1px solid #e5e5e5;margin: 0 auto;width:96%;}
.sub_table_border_color  td{border-bottom:1px solid #e5e5e5; border-right: 1px solid #e5e5e5;}
.sub_table_border_color thead tr td{color: #999999;}
</style>
<body class="admin_bg">
    <!-- 头部 -->
	<jsp:include page="/WEB-INF/commons/header.jsp"/>
	
    <div class="content clearfix">
        <!-- 左侧菜单 -->
		<jsp:include page="/WEB-INF/commons/profit-left.jsp">
			<jsp:param name="menu" value="publish_record"/>
		</jsp:include>
		<div class="aside">
            <p class="aside_word"><span>发布记录</span> > <span>发布推荐详情页</span><span>：${vo.planTjNo}</span>
            <span style="padding-left:450px">发布时间：<fmt:formatDate value="${vo.planTj.createTime.time}" pattern="yyyy-MM-dd HH:mm"/></span></p>
            <div class="wrap1080">
                <p class="pub1">商品信息</p>
                <table class="table_border_color">
                    <tr>
                        <td>商品名称</td>
                        <td width="20%">${profitPlan.title }</td>
                    </tr>
                </table>
                <p class="pub1">推荐单信息</p>
                <table class="table_border_color table_bottom_border">
                    <tr>
                        <td>推荐编号</td>
                        <td width="20%">
                        	${vo.planTjNo}
                        </td>
                        <td>赛事类型</td>
                        <td width="20%">
                        	<c:choose>
                          		<c:when test="${ tjPlan.raceType == 1}">
                          			竞彩足球
                          		</c:when>
                          		<c:when test="${ tjPlan.raceType == 2}">
                          			竞彩篮球
                          		</c:when>
                          	</c:choose>
                        </td>
                        <td>玩法/过关类型</td>
                        <td>亚盘</td>
                        <td>返奖率</td>
                        <td>${tjPlan.masterPrize/tjPlan.masterCost *100 }% </td>
                    </tr>
                </table>
               	<table class="sub_table_border_color" style="margin-top:15px;">
               		<thead>
               			<tr style="background-color:#f9f9f9;">
               				<td>赛事编号</td>
               				<td>联赛名称</td>
               				<td>开赛时间</td>
               				<td>对阵</td>
               				<td>比赛状态</td>
               				<td>完场比分</td>
               				<td>开奖结果</td>
               				<td>命中状态</td>
               				<td>推荐结果</td>
               			</tr>
               		</thead>
               		<tbody>
               			 <c:forEach items="${detailResultList}" var="item">
               				<tr>
	               				<td>${item.dayOfWeek}${item.matchOrder}</td>
	               				<td>${item.matchName} </td>
	               				<td><fmt:formatDate value="${item.matchTime}" pattern="yyyy-MM-dd HH:mm"/></td>
	               				<td>${item.matchVS}</td>
	               				<td>
	               					${item.matchStaus }
	               					<%-- <c:forEach items="${allRaceStatus}" var="t" varStatus="status">
										<c:if test="${t.index == tjPlan.raceStatus}"> ${t.description} </c:if>
									</c:forEach> --%>
								</td>
	               				<td>${item.wholeScore }</td>
	               				<td>${item.openResult }</td>
	               				<td>
	               					${item.winStatus}
	               					<%-- <c:choose>
		                          		<c:when test="${item.winStatus == 0}">
		                          			待开奖
		                          		</c:when>
		                          		<c:when test="${item.winStatus == 1}">
		                          			未中
		                          		</c:when>
		                          		<c:when test="${item.winStatus == 2}">
		                          			命中
		                          		</c:when>
		                          		<c:when test="${item.winStatus == 3}">
		                          			取消
		                          		</c:when>
		                          	</c:choose> --%>
		                         </td>
		                         <td>${item.tjResult}</td>
	               			</tr>
               			</c:forEach>
               		</tbody>
               	</table>
               	<c:if test="${tjPlan.describe != null && tjPlan.describe != ''}">
               		<div class="pub1">推荐说明：</div>
	                <div class="reason">
	                	${tjPlan.describe}  
	                </div>
               	</c:if>
            </div>
        </div>        
    </div>
    <!-- 尾部 -->
	<jsp:include page="/WEB-INF/commons/footer.jsp"/>
</body>
</html>
