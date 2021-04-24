<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%-- 联赛过滤 --%>
<div class="saiDivCo">
	<div class="saiButt">
		<a id="cbxLeagueAll" href="javascript:void(0)" class="on">所有</a> 
		<a id="cbxLeagueReverse" href="javascript:void(0)">反选</a> 
		<a id="cbxLeague5" href="javascript:void(0)">五大联赛</a>
	</div>
	<div class="saiLis">
		<jsp:useBean id="leagueCountMap" class="java.util.HashMap"/>
		<c:forEach items="${groupVoList}" var="groupVo">
			<c:forEach items="${groupVo.matchVoList}" var="matchVo">
				<c:choose>
					<c:when test="${empty leagueCountMap[matchVo.leagueName]}">
						<c:set target="${leagueCountMap}" property="${matchVo.leagueName}" value="1"/>
					</c:when>
					<c:otherwise>
						<c:set target="${leagueCountMap}" property="${matchVo.leagueName}" value="${leagueCountMap[matchVo.leagueName] + 1}"/>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</c:forEach>
		<ul>
			<c:forEach items="${leagueCountMap}" var="entry">
				<li><input type="checkbox" value="${entry.key}" checked/>${entry.key}[${entry.value}]场</li>	
			</c:forEach>
		</ul>
	</div>
	<div class="yjzBo">
		<span style=""> <span class="yjzSpanBlur">已截至</span>
		</span> <span> 隐藏<em class="c_red" id="matchHideCount">0</em>场
		</span>
	</div>
</div>