<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="text_nocenter pub_list_jczq" style="display: block;">
	<input type="hidden" id="jq_play_type" value="2c1">
	<input type="hidden" id="jq_improv_gameId" value="4071">
	<table id="jczq_2c1_table" class="BMTable">
		<tr style="background-color: #f5f5f5" class="th12">
			<th>编号</th>
			<th class="competition">
				<div>
					<div>
						<span>赛事</span><i class="sprite"></i>
					</div>
					<jsp:include page="/WEB-INF/jsp/common/publish-common-league.jsp"/>
				</div>
			</th>
			<th>比赛时间</th>
			<th>主队</th>
			<th>比分</th>
			<th>客队</th>
			<th>让球</th>
			<th>胜</th>
			<th>平</th>
			<th>负</th>
			<th>数据</th>
			<th>平均欧赔</th>
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
					<c:if test="${matchVo.isOfficial == true}">
					
					<c:set var="spf_3" value="${fn:split(matchVo.spSPF, '-')[0]}"/>
					<c:set var="spf_1" value="${fn:split(matchVo.spSPF, '-')[1]}"/>
					<c:set var="spf_0" value="${fn:split(matchVo.spSPF, '-')[2]}"/>
					<c:set var="rqspf_handicap" value="${fn:split(matchVo.spRQSPF, '|')[0]}"/>
					<c:set var="rqspf_3" value="${fn:split(fn:split(matchVo.spRQSPF, '|')[1], '-')[0]}"/>
					<c:set var="rqspf_1" value="${fn:split(fn:split(matchVo.spRQSPF, '|')[1], '-')[1]}"/>
					<c:set var="rqspf_0" value="${fn:split(fn:split(matchVo.spRQSPF, '|')[1], '-')[2]}"/>
					<c:set var="eroup_3" value="${fn:split(matchVo.avgEroupSp, '-')[0]}"/>
					<c:set var="eroup_1" value="${fn:split(matchVo.avgEroupSp, '-')[1]}"/>
					<c:set var="eroup_0" value="${fn:split(matchVo.avgEroupSp, '-')[2]}"/>
						<tr data-leaguename="${matchVo.leagueName}" data-raceid="${matchVo.raceId }" data-leagueindex="${matchVo.leagueIndex}" data-fxid="${matchVo.fxId}" data-matchid="${matchVo.matchId}" data-matchno="${matchVo.uniqueMatchNo}" class="Jq_line_match">
							<td class="game_number">${matchVo.matchNo}</td>
							<td class="game_match"><span style="background-color: #005bd8;">${matchVo.leagueName}</span></td>
							<td class="game_start"><fmt:formatDate value="${matchVo.matchTime.time}" pattern="HH:mm"/></td>
							<td class="game_host">${matchVo.homeTeamName}</td>
							<td class="game_score">VS</td>
							<td class="game_guest">${matchVo.guestTeamName}</td>
							<td class="game_cede gm_bgp Jq_handicap">
								<p>0</p>
		                		<p data-handicap="${rqspf_handicap}">${empty rqspf_handicap ? '-' : rqspf_handicap}</p>
		                	</td>
							<td class="game_sheng gm_bgp Jq_sp">
								<p data-index="0" data-gametype="4071" data-sp="${empty spf_3 ? null : '3-'.concat(spf_3)}">
									${empty spf_3 ? '-' : spf_3}
								</p>
		                		<p data-index="0" data-gametype="4076" data-sp="${empty rqspf_3 ? null : '3-'.concat(rqspf_3)}">
									${empty rqspf_3 ? '-' : rqspf_3}
								</p>
							</td>
							<td class="game_ping gm_bgp Jq_sp">
								<p data-index="1" data-gametype="4071" data-sp="${empty spf_1 ? null : '1-'.concat(spf_1)}">
									${empty spf_1 ? '-' : spf_1}
								</p>
		                		<p data-index="1" data-gametype="4076" data-sp="${empty rqspf_1 ? null : '1-'.concat(rqspf_1)}">
									${empty rqspf_1 ? '-' : rqspf_1}
								</p>
							</td>
							<td class="game_fu gm_bgp Jq_sp">
								<p data-index="2" data-gametype="4071" data-sp="${empty spf_0 ? null : '0-'.concat(spf_0)}">
									${empty spf_0 ? '-' : spf_0}
								</p>
		                		<p data-index="2" data-gametype="4076" data-sp="${empty rqspf_0 ? null : '0-'.concat(rqspf_0)}">
									${empty rqspf_0 ? '-' : rqspf_0}
								</p>
							</td>
							<td class="game_trend">
								<em><a href="http://live.aicai.com/zc/xyo_${matchVo.fxId}_407.html" target="_blank">析</a></em>
		          	    		<em><a href="http://www.aicai.com/lotnew/jc/zqchart/${matchVo.matchId}.htm" target="_blank">势</a></em>
		              			<em><a href="https://yq.aicai.com/matchPlanDetail/${matchVo.fxId}-1/matchPlanIntelligence?showHeader=1" target="_blank">情</a></em>
							</td>
							<td class="game_odds">
								<em class="ml0">${empty eroup_3 ? '-' : eroup_3}</em>
			      				<em>${empty eroup_1 ? '-' : eroup_1}</em>
			      				<em>${empty eroup_0 ? '-' : eroup_0}</em>
							</td>
						</tr>
					</c:if>
					
				</c:forEach>
			</tbody>
		</c:forEach>
	</table>
	<div id="jq_return_rate" >方案最低返奖率须≥<fmt:formatNumber type="number" value="${zqCgProfitRatio*100}" maxFractionDigits="0"/>%,当前推荐单最低返奖率:<span id="jq_prize_rate" style="color:red">0</span>%</div>
</div>              
                

