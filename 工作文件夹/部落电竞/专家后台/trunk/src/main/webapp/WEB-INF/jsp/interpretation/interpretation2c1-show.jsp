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
<jsp:include page="/WEB-INF/commons/common-file.jsp"/>
<body class="admin_bg">
     <!-- 头部 -->
	<jsp:include page="/WEB-INF/commons/header.jsp"/>
	
    <div class="content clearfix">
        <!-- 左侧菜单 -->
		<jsp:include page="/WEB-INF/commons/left.jsp">
			<jsp:param name="menu" value="interpretation"/>
		</jsp:include>
        
        <div class="aside">
            <p class="aside_word"><span>我的解读</span> > <span>解读编号</span><span>：${tjInterpretation.tjInterpretationNo}</span>
            <span style="padding-left:450px">发布时间：<fmt:formatDate value="${tjInterpretation.createTime.time}" pattern="yyyy-MM-dd HH:mm"/></span></p>
            <div class="wrap1080">
                <p class="pub1">查看信息</p>
                <table class="table_border_color">
                    <tr>
                        <td>查看金币</td>
                        <td width="20%"><fmt:formatNumber value="${tjInterpretation.price}" pattern="#"/>个</td>
                        <td>查看人数</td>
                        <td width="20%">${tjInterpretation.lookerCount}人</td>
                    </tr>
                </table>
                <p class="pub1">解读详情</p>
                <table class="table_border_color table_bottom_border">
                    <tr>
                        <td>彩种</td>
                        <td width="20%">
                        	<c:forEach items="${raceTypeList}" var="raceType">
								<c:if test="${raceType.index == tjInterpretation.raceType}">${raceType.description}</c:if>                          
                          	</c:forEach>
                        </td>
                        <td>解读状态</td>
                        <td width="20%">
                        	<c:forEach items="${raceStatusList}" var="raceStatus">
								<c:if test="${raceStatus.index == tjInterpretation.raceStatus}">${raceStatus.description}</c:if>                          
                          	</c:forEach>
                        </td>
                        <td>开赛时间</td>
                        <td><fmt:formatDate value="${tjInterpretation.minRaceMatchTime.time}" pattern="yyyy-MM-dd HH:mm"/></td>
                    </tr>
                    <tr>
                        <td style="width: 90px">解读标题</td>
                        <td colspan="5" align="left" style="padding-left:50px">${tjInterpretation.title}</td>
                    </tr>
                </table>
                
               	<table class="table_bottom_border ad_tjcont">
                       <tr bgcolor="#f9f9f9" >
                           <td rowspan="3" width="14%">推荐内容</td> <!-- rowspan="${fn:length(matchVoList) + 1}" -->
                           <td>玩法</td>
                           <td>联赛名称</td>
                           <td>赛事编号</td>
                           <td>比赛对阵</td>
                           <td>开赛时间</td>
                           <td>推荐结果</td>
                           <td>完场比分</td>
                           <td>开奖结果</td>
                           <td>命中状态</td>
                           <td>返奖率</td>
                       </tr>
                       <tr>
                      		<td>2串1</td>
                      		<td>${detailResult0.matchName}</td>
                      		<td>${detailResult0.dayOfWeek}${detailResult0.matchOrder}</td>
                      		<td>${detailResult0.matchVS}</td>
                      		<td><fmt:formatDate value="${detailResult0.matchTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                      		<td>${detailResult0.tjResult}</td>
                      		<td>${detailResult0.wholeScore}</td>
                      		<td>${detailResult0.openResult }</td>
                      		<td>
                       		<c:choose>
                          		<c:when test="${ tjInterpretation.winStatus == 0}">
                          			待开奖
                          		</c:when>
                          		<c:when test="${ tjInterpretation.winStatus == 1}">
                          			未中
                          		</c:when>
                          		<c:when test="${ tjInterpretation.winStatus == 2}">
                          			命中
                          		</c:when>
                          	</c:choose>
                         	</td>
                         	<td>${tjInterpretation.prize/tjInterpretation.cost *100 }% </td>
                       </tr>
                       <tr>
                      		<td>2串1</td>
                      		<td>${detailResult1.matchName}</td>
                      		<td>${detailResult1.dayOfWeek}${detailResult1.matchOrder}</td>
                      		<td>${detailResult1.matchVS}</td>
                      		<td><fmt:formatDate value="${detailResult1.matchTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                      		<td>${detailResult1.tjResult}</td>
                      		<td>${detailResult1.wholeScore}</td>
                      		<td>${detailResult1.openResult }</td>
                      		<td>
                       		<c:choose>
                          		<c:when test="${ tjInterpretation.winStatus == 0}">
                          			待开奖
                          		</c:when>
                          		<c:when test="${ tjInterpretation.winStatus == 1}">
                          			未中
                          		</c:when>
                          		<c:when test="${ tjInterpretation.winStatus == 2}">
                          			命中
                          		</c:when>
                          	</c:choose>
                         	</td>
                         	<td>${tjInterpretation.prize/tjInterpretation.cost *100 }% </td>
                       </tr>
               	</table>
               	<div class="pub1">解读摘要</div>
                <div class="reason">${tjInterpretation.shortDesc}</div>
                <div class="pub1">解读理由</div>
                <div class="reason">${tjInterpretation.content}</div>
            </div>
        </div>        
    </div>
    
    <!-- 尾部 -->
	<jsp:include page="/WEB-INF/commons/footer.jsp"/>
<script>
$(document).ready(function(){
    $("#Jq_look_number").bind("click",function(){
        webAlert({
            title:false,
            content:$("#Jq_user_list")[0],
            padding:"0 0 20px 0",
          })
    })
})
</script>
</body>
</html>