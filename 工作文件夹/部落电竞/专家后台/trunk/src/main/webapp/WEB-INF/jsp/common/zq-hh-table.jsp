<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!-- 混合过关 -->
<input type="hidden" id="jq_play_type" value="hh">
<input type="hidden" id="jq_improv_gameId" value="4075">
<table id="jczq_hh_table" class="BMTable">
	<tr style="background-color: #f5f5f5" class="th12">
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
		<th>主队</th>
		<th>&nbsp;</th>
		<th>客队</th>
		<th>数据</th>
		<th>平均欧赔</th>
		<th style="border-right: 1px solid #eeeeee;">&nbsp;</th>
	</tr>
	
	<c:if test="${empty groupVoList}">
		<tbody>
			<tr>
				<td colspan="12">暂时没有数据</td>
			</tr>
		</tbody>
	</c:if>
	<c:forEach items="${groupVoList}" var="groupVo" varStatus="gst">
		<tbody>
			<tr>
				<td colspan="12" class="game_time">
					<span> <span class="time_marr">${groupVo.groupName}</span>${groupVo.dayOfWeek}(12:00-次日12:00)</span>
					<i class="sprite Jq_i_bottom"></i>
				</td>
			</tr>
			
			<c:forEach items="${groupVo.matchVoList}" var="matchVo" varStatus="mst">
				<c:set var="spf_3" value="${fn:split(matchVo.spSPF, '-')[0]}"/>
				<c:set var="spf_1" value="${fn:split(matchVo.spSPF, '-')[1]}"/>
				<c:set var="spf_0" value="${fn:split(matchVo.spSPF, '-')[2]}"/>
				<c:set var="rqspf_handicap" value="${fn:split(matchVo.spRQSPF, '|')[0]}"/>
				<c:set var="rqspf_3" value="${fn:split(fn:split(matchVo.spRQSPF, '|')[1], '-')[0]}"/>
				<c:set var="rqspf_1" value="${fn:split(fn:split(matchVo.spRQSPF, '|')[1], '-')[1]}"/>
				<c:set var="rqspf_0" value="${fn:split(fn:split(matchVo.spRQSPF, '|')[1], '-')[2]}"/>
				<c:set var="yp_handicap" value="${fn:split(matchVo.spYP, '|')[0]}"/>
				<c:set var="yp_3" value="${fn:split(fn:split(matchVo.spYP, '|')[1], '-')[0]}"/>
				<c:set var="yp_0" value="${fn:split(fn:split(matchVo.spYP, '|')[1], '-')[1]}"/>
				<c:set var="dxq_handicap" value="${fn:split(matchVo.spDXQ, '|')[0]}"/>
				<c:set var="dxq_3" value="${fn:split(fn:split(matchVo.spDXQ, '|')[1], '-')[0]}"/>
				<c:set var="dxq_0" value="${fn:split(fn:split(matchVo.spDXQ, '|')[1], '-')[1]}"/>
				<c:set var="eroup_3" value="${fn:split(matchVo.avgEroupSp, '-')[0]}"/>
				<c:set var="eroup_1" value="${fn:split(matchVo.avgEroupSp, '-')[1]}"/>
				<c:set var="eroup_0" value="${fn:split(matchVo.avgEroupSp, '-')[2]}"/>
			
				<tr data-leaguename="${matchVo.leagueName}" class="Jq_line_match" style="line-height: 40px;">
					<td class="game_number">${matchVo.matchNo}</td>
					<td class="game_match"><span style="background-color: #005bd8;">${matchVo.leagueName}</span></td>
					<td class="game_start"><fmt:formatDate value="${matchVo.matchTime.time}" pattern="HH:mm"/></td>
					<td class="game_host">${matchVo.homeTeamName}</td>
					<td class="game_score">VS</td>
					<td class="game_guest">${matchVo.guestTeamName}</td>
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
					
					<c:choose>
						<c:when test="${gst.count == 1 and mst.count == 1}"><td class="game_xztj"><span class="tj_choose co_pan">收起推荐</span></td></c:when>
						<c:otherwise><td class="game_xztj"><span class="tj_choose">选择推荐</span></td></c:otherwise>
					</c:choose>
				</tr>
								
				<tr data-leaguename="${matchVo.leagueName}" class="Jq_line_sp" bgcolor="#f8f8f8" style="display: ${gst.count == 1 and mst.count == 1 ? '' : 'none'};">
					<td colspan="10">
						<table data-matchid="${matchVo.matchId}" data-fxid="${matchVo.fxId}" data-matchno="${matchVo.uniqueMatchNo}" class="unfold">
						  <c:if test="${not empty matchVo.spSPF && matchVo.isOfficial == true}">
							<tr data-gametype="4071">
								<td class="c_f0f0f0">胜平负</td>
								<td class="Jq_sp" data-index="0" data-sp="${empty spf_3 ? null : '3-'.concat(spf_3)}">主胜${spf_3}</td>
								<td class="Jq_sp" data-index="1" data-sp="${empty spf_1 ? null : '1-'.concat(spf_1)}">
									平 ${empty spf_1 ? '-' : spf_1}
								</td>
								<td class="Jq_sp" data-index="2" data-sp="${empty spf_0 ? null : '0-'.concat(spf_0)}">
									客胜${empty spf_0 ? '-' : spf_0}
								</td>
							</tr>
						  </c:if>
						  <c:if test="${not empty matchVo.spRQSPF && matchVo.isOfficial == true}">
							<tr data-gametype="4076">
								<td data-handicap="${rqspf_handicap}" class="c_f0f0f0">
									让球胜平负<span style="color: #ff0000">（${rqspf_handicap}）</span>
								</td>
								<td class="Jq_sp" data-index="0" data-sp="3-${rqspf_3}">主胜 ${rqspf_3}</td>
								<td class="Jq_sp" data-index="1" data-sp="1-${rqspf_1}">平 ${rqspf_1}</td>
								<td class="Jq_sp" data-index="2" data-sp="0-${rqspf_0}">客胜 ${rqspf_0}</td>
							</tr>
						  </c:if>
						  <c:if test="${not empty matchVo.spDXQ}">
							<tr data-gametype="4079">
								<td data-handicap="${dxq_handicap}" class="c_f0f0f0">
									大小球<span style="color: #ff0000">（${dxq_handicap}）</span>
								</td>
								<td class="Jq_sp" data-index="0" data-sp="3-${dxq_3}">大球 ${dxq_3}</td>
								<td class="Jq_sp" data-index="1" data-sp="0-${dxq_0}" colspan="2">小球 ${dxq_0}</td>
							</tr>
						  </c:if>
						  <c:if test="${not empty matchVo.spYP}">
							<tr data-gametype="4078">
								<td data-handicap="${yp_handicap}" class="c_f0f0f0">
									亚盘<span style="color: #ff0000">（${yp_handicap}）</span>
								</td>
								<!-- Jq_yp_s上盘 Jq_yp_x 下盘 -->
								<td class="Jq_sp" data-index="0" data-sp="3-${yp_3}">主胜 ${yp_3}</td>
								<td class="Jq_sp" data-index="1" data-sp="0-${yp_0}" colspan="2">客胜 ${yp_0}</td>
							</tr>
						  </c:if>
						</table>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</c:forEach>
</table>
