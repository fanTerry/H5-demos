<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- 14场胜负彩、任选九场 -->
<div class="text_nocenter pub_list_jczq" style="display: block;">
<!-- 14场胜负彩、任选九场 -->
<c:if test="${gameCode == 'sfc' }">
	<input type="hidden" id="jq_improv_gameId" value="401">
</c:if>
<c:if test="${gameCode == 'r9c' }">
	<input type="hidden" id="jq_improv_gameId" value="402">
</c:if>
<table id="sfc_table">
    <tr style="background-color:#f5f5f5">
        <th>场次</th>
        <th>赛事</th>
        <th>对阵</th>
        <th>比赛时间</th>
        <th>数据</th>
       	<th>平均欧指</th>
        <th>胜</th>
        <th>平</th>
        <th>负</th>
    </tr>
    
    <c:if test="${empty matchVoList}">
		<tr>
			<td colspan="12" class="game_time">
				<span> <span class="time_marr">${groupVo.groupName}</span>${groupVo.dayOfWeek}(12:00-次日12:00)</span>
				<i class="sprite Jq_i_bottom"></i>
			</td>
		</tr>
    </c:if>
    <c:forEach items="${matchVoList}" var="matchVo" varStatus="st">
		<c:set var="eroup_3" value="${fn:split(matchVo.avgEroupSp, '-')[0]}"/>
		<c:set var="eroup_1" value="${fn:split(matchVo.avgEroupSp, '-')[1]}"/>
		<c:set var="eroup_0" value="${fn:split(matchVo.avgEroupSp, '-')[2]}"/>
    
    	<tr>
	        <td>${st.count}</td>
	        <td>${matchVo.leagueName}</td>
	        <td>${matchVo.homeTeamName}<span class="vs_span">VS</span>${matchVo.guestTeamName}</td>
	        <td>
	        	<span class="time_right"><fmt:formatDate value="${matchVo.matchTime.time}" pattern="MM-dd"/></span> 
	        	<fmt:formatDate value="${matchVo.matchTime.time}" pattern="HH:mm"/>
	        </td>
	        <td class="game_trend">
	        	<em><a href="http://live.aicai.com/zc/xyo_${matchVo.fxId}_${matchVo.isReverse ? '10' : '00'}.html" target="_blank">析</a></em>
	            <em><a href="http://live.aicai.com/zc/xyo_${matchVo.fxId}_${matchVo.isReverse ? '10' : '00'}_yazhi.html" target="_blank">亚</a></em>
	            <em><a href="http://live.aicai.com/zc/xyo_${matchVo.fxId}_${matchVo.isReverse ? '10' : '00'}_ouzhi.html" target="_blank">欧</a></em>
	        </td>
	        <td class="game_odds">
	        	<em class="ml0">${eroup_3}</em>
	      		<em>${eroup_1}</em>
	      		<em>${eroup_0}</em>
	        </td>
	        <td class="game_sheng gm_bgp Jq_sp"><p class="bor_bot0" data-sp="3">3</p></td>
	        <td class="game_ping gm_bgp Jq_sp"><p class="bor_bot0" data-sp="1">1</p></td>
	        <td class="game_fu gm_bgp Jq_sp"><p class="bor_bot0" data-sp="0">0</p></td>
	    </tr>
    </c:forEach>
</table>
</div>