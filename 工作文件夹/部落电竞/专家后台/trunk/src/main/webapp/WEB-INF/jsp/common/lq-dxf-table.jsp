<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- 大小分 -->
<input type="hidden" id="jq_play_type" value="dxf">
<input type="hidden" id="jq_improv_gameId" value="4064">
<table id="jclq_dxf_table" class="BMTable">
	<tr style="background-color:#f5f5f5" class="th12">
		<th>编号</th>
        <th class="competition">
			<div>
				<div>
					<span>赛事</span><i class="sprite"></i>
				</div>
				<jsp:include page="publish-common-league.jsp"/>
			</div>
		</th>
        <th>比赛时间</th>
        <th>客队</th>
        <th>&nbsp;</th>
        <th>主队</th>
        <th>数据</th>
        <th>平均欧赔</th>
        <th>预设总分</th>
         <th>大分</th>
         <th style="border-right:1px solid #eeeeee;">小分</th>
	</tr>
		
	<c:if test="${empty groupVoList}">
		<tbody>
			<tr>
				<td colspan="12">暂时没有数据</td>
			</tr>
		</tbody>
	</c:if>

	<c:forEach items="${groupVoList}" var="groupVo">
	    <tbody>
	        <tr>
	            <td colspan="12" class="game_time">
	            	<span> <span class="time_marr">${groupVo.groupName}</span>${groupVo.dayOfWeek}(12:00-次日12:00)</span>
	            	<i class="sprite Jq_i_bottom"></i>
	            </td>
	        </tr>
	        
	        <c:forEach items="${groupVo.matchVoList}" var="matchVo">
	       	 	<c:set var="eroup_1" value="${fn:split(matchVo.avgEroupSp, '-')[0]}"/>
	        	<c:set var="eroup_2" value="${fn:split(matchVo.avgEroupSp, '-')[1]}"/>
	        	
	        	<c:set var="dxf_handicap" value="${fn:split(matchVo.spDXF, '|')[0]}"/>
	        	<c:set var="dxf_1" value="${fn:split(fn:split(matchVo.spDXF, '|')[1], '-')[0]}"/>
	        	<c:set var="dxf_2" value="${fn:split(fn:split(matchVo.spDXF, '|')[1], '-')[1]}"/>
	                       
		        <tr data-gametype="4064" data-leaguename="${matchVo.leagueName}" data-fxid="${matchVo.fxId}" data-matchid="${matchVo.matchId}" data-matchno="${matchVo.uniqueMatchNo}" class="Jq_line_match" style="line-height: 40px;">
					<td class="game_number">${groupVo.dayOfWeek}${matchVo.matchNo}</td>
                    <td class="game_match"><span style="background-color: #005bd8;">${matchVo.leagueName}</span></td>
                    <td class="game_start"><fmt:formatDate value="${matchVo.matchTime.time}" pattern="HH:mm"/></td>
	            	<td class="game_guest">${matchVo.guestTeamName}</td>
	            	<td class="game_score">VS</td>
	            	<td class="game_host">${matchVo.homeTeamName}</td>
	            	<td class="game_trend">
		            	<em><a href="http://live.aicai.com/lc/xyo_${matchVo.fxId}.html" target="_blank">析</a></em>
		            	<em><a href="http://live.aicai.com/lc/xyo_${matchVo.fxId}_0_ouzhi.html" target="_blank">欧</a></em>
		            	<em><a href="http://live.aicai.com/lc/xyo_${matchVo.fxId}_rfpl.html" target="_blank">让</a></em>
		            </td>
		            <td class="game_odds">
		            	<em class="ml0">${empty eroup_2 ? '-' : eroup_2}</em>
		            	<em>${empty eroup_1 ? '-' : eroup_1}</em>
		            </td>
                    <td data-handicap="${empty dxf_handicap ? null : dxf_handicap}"  class="Jq_handicap" style="color:#ff0000">
                    	${empty dxf_handicap ? '-' : dxf_handicap}
                    </td>
                    <td data-sp="${empty dxf_1 ? null : '1-'.concat(dxf_1)}"  data-index="0" class="bgp_sp1 Jq_sp">
                    	${empty dxf_1 ? '-' : dxf_1}
                    </td>
                    <td data-sp="${empty dxf_2 ? null : '2-'.concat(dxf_2)}"  data-index="1" class="bgp_sp1 Jq_sp">
                    	${empty dxf_2 ? '-' : dxf_2}
                    </td>
				</tr>
	        </c:forEach>
	   </tbody>
	</c:forEach>
</table>

