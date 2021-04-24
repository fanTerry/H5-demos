<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- 胜分差 -->
<input type="hidden" id="jq_play_type" value="sfc">
<input type="hidden" id="jq_improv_gameId" value="4063">
<table id="jclq_sfc_table" class="BMTable">
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
        <th style="border-right:1px solid #eeeeee;" class="th_bor" width="45%">
			<table>
            	<tr><td colspan="7" bgcolor="#ffffff">赛果选择区</td></tr>
          		<tr bgcolor="#f5f5f5">
                	<td style="color:#999999;">主客</td>
                	<td style="color:#999999;">1-5分</td>
                	<td style="color:#999999;">6-10分</td>
                	<td style="color:#999999;">11-15分</td>
                	<td style="color:#999999;">16-20分</td>
                	<td style="color:#999999;">21-25分</td>
                	<td style="color:#999999;">26分＋</td>
            	</tr>
			</table>                              
        </th>
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
	        	<c:set var="sfc_11"  value="${fn:split(matchVo.spSFC, '-')[0]}"/>
	        	<c:set var="sfc_01"  value="${fn:split(matchVo.spSFC, '-')[1]}"/>
	        	<c:set var="sfc_12"  value="${fn:split(matchVo.spSFC, '-')[2]}"/>
	        	<c:set var="sfc_02"  value="${fn:split(matchVo.spSFC, '-')[3]}"/>
	        	<c:set var="sfc_13"  value="${fn:split(matchVo.spSFC, '-')[4]}"/>
	        	<c:set var="sfc_03"  value="${fn:split(matchVo.spSFC, '-')[5]}"/>
	        	<c:set var="sfc_14"  value="${fn:split(matchVo.spSFC, '-')[6]}"/>
	        	<c:set var="sfc_04"  value="${fn:split(matchVo.spSFC, '-')[7]}"/>
	        	<c:set var="sfc_15"  value="${fn:split(matchVo.spSFC, '-')[8]}"/>
	        	<c:set var="sfc_05"  value="${fn:split(matchVo.spSFC, '-')[9]}"/>
	        	<c:set var="sfc_16"  value="${fn:split(matchVo.spSFC, '-')[10]}"/>
	        	<c:set var="sfc_06"  value="${fn:split(matchVo.spSFC, '-')[11]}"/>
	        	
	       	 	<tr  data-gametype="lqsfc" data-leaguename="${matchVo.leagueName}" data-fxid="${matchVo.fxId}" data-matchid="${matchVo.matchId}" data-matchno="${matchVo.uniqueMatchNo}" class="Jq_line_match" style="line-height: 80px;">
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
                    <td>
                    	<table>
	                        <tr class="bor_ri" data-gametype="4063_s">
	                            <td>客胜</td>
	                            <td data-sp="${empty sfc_11 ? null : '11-'.concat(sfc_11)}" data-index="0" class="Jq_sp">
	                            	${empty sfc_11 ? '-' : sfc_11}
	                            </td>
	                            <td data-sp="${empty sfc_12 ? null : '12-'.concat(sfc_12)}" data-index="1" class="Jq_sp">
	                            	${empty sfc_12 ? '-' : sfc_12}
	                            </td>
	                            <td data-sp="${empty sfc_13 ? null : '13-'.concat(sfc_13)}" data-index="2" class="Jq_sp">
	                            	${empty sfc_13 ? '-' : sfc_13}
	                            </td>
	                            <td data-sp="${empty sfc_14 ? null : '14-'.concat(sfc_14)}" data-index="3" class="Jq_sp">
	                            	${empty sfc_14 ? '-' : sfc_14}
	                            </td>
	                            <td data-sp="${empty sfc_15 ? null : '15-'.concat(sfc_15)}" data-index="4" class="Jq_sp">
	                            	${empty sfc_15 ? '-' : sfc_15}
	                            </td>
	                            <td data-sp="${empty sfc_16 ? null : '16-'.concat(sfc_16)}" data-index="5" class="Jq_sp">
	                            	${empty sfc_16 ? '-' : sfc_16}
	                            </td>
	                        </tr>
	                        <tr class="bor_ri" data-gametype="4063_f">
	                            <td>主胜</td>
	                            <td data-sp="${empty sfc_01 ? null : '01-'.concat(sfc_01)}" data-index="0" class="Jq_sp">
	                            	${empty sfc_01 ? '-' : sfc_01}
	                            </td>
	                            <td data-sp="${empty sfc_02 ? null : '02-'.concat(sfc_02)}" data-index="1" class="Jq_sp">
	                            	${empty sfc_02 ? '-' : sfc_02}
	                            </td>
	                            <td data-sp="${empty sfc_03 ? null : '03-'.concat(sfc_03)}" data-index="2" class="Jq_sp">
	                            	${empty sfc_03 ? '-' : sfc_03}
	                            </td>
	                            <td data-sp="${empty sfc_04 ? null : '04-'.concat(sfc_04)}" data-index="3" class="Jq_sp">
	                            	${empty sfc_04 ? '-' : sfc_04}
	                            </td>
	                            <td data-sp="${empty sfc_05 ? null : '05-'.concat(sfc_05)}" data-index="4" class="Jq_sp">
	                            	${empty sfc_05 ? '-' : sfc_05}
	                            </td>
	                            <td data-sp="${empty sfc_06 ? null : '06-'.concat(sfc_06)}" data-index="5" class="Jq_sp">
	                            	${empty sfc_06 ? '-' : sfc_06}
	                            </td>
	                        </tr>
	                    </table>
                    </td>
                </tr>
	        </c:forEach>
	   </tbody>
	</c:forEach>
</table>

