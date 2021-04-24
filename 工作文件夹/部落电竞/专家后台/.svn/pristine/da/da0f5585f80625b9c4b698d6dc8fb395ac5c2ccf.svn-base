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
            <p class="aside_word"><span>我的解读</span> > <span>解读编号</span><span>：${fn:escapeXml(tjInterpretation.tjInterpretationNo)}</span>
            <span style="padding-left:450px">发布时间：<fmt:formatDate value="${tjInterpretation.createTime.time}" pattern="yyyy-MM-dd HH:mm"/></span></p>
            <div class="wrap1080">
                <p class="pub1">查看信息</p>
                <table class="table_border_color">
                    <tr>
                        <td>查看金币</td>
                        <td width="20%"><fmt:formatNumber value="${fn:escapeXml(tjInterpretation.price)}" pattern="#"/>个</td>
                        <td>查看人数</td>
                        <td width="20%">${fn:escapeXml(tjInterpretation.lookerCount)}人</td>
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
                        <c:choose>
                        	<c:when test="${tjInterpretation.publishType == 2 }">
                        		<td>预计发布时间</td>
                       	 		<td><fmt:formatDate value="${tjInterpretation.publishTime.time}" pattern="yyyy-MM-dd HH:mm"/></td>
                        	</c:when>
                        	<c:otherwise>
                        		<td>开赛时间</td>
                       	 		<td><fmt:formatDate value="${tjInterpretation.minRaceMatchTime.time}" pattern="yyyy-MM-dd HH:mm"/></td>
                        	</c:otherwise>
                        </c:choose>
                    </tr>
                    <tr>
                        <td>销售类型</td>
                        <td width="20%">
                        	<c:if test="${tjInterpretation.publishType == 1}">
                        		普通解读
                        	</c:if>
                        	<c:if test="${tjInterpretation.publishType == 2}">
                        		临场解读
                        	</c:if>
                        </td>
                        <td>发布结果状态</td>
                        <td width="20%">
                        	<c:if test="${ tjInterpretation.publishType ==1 }">
                        		已发布
                        	</c:if>
                        	<c:if test="${ tjInterpretation.publishType ==2 && tjInterpretation.improvStatus ==1 }">
                        		未发布 <a href="/improv/publishIndex?interId=${tjInterpretation.id }" style="color: #0082ff;">&nbsp;&nbsp;发布结果</a>
                        	</c:if>
                        	<c:if test="${ tjInterpretation.publishType ==2 && tjInterpretation.improvStatus ==2 }">
                        		已发布
                        	</c:if>
                        	<c:if test="${ tjInterpretation.publishType ==2 && tjInterpretation.improvStatus ==3 }">
                        		过期撤单
                        	</c:if>
                        </td>
                        <td>是否实单</td>
                        <td>
                        	<c:choose>
                        		<c:when test="${fn:escapeXml(tjInterpretation.ownBuyAmount) > 0}">
                        			是（购买${fn:escapeXml( tjInterpretation.ownBuyAmount)}元
                        		</c:when>
                        		<c:otherwise>否</c:otherwise>
                        	</c:choose>
                        </td>
                    </tr>
                    <tr>
                        <td>不中退款</td>
                        <td width="20%">
                        	<c:choose>
                        		<c:when test="${isRefundNotWin == true}"> 是 </c:when>
                        		<c:otherwise>
                        			否
                        		</c:otherwise>
                        	</c:choose>
                        </td>
                        <td>免费解读查看条件</td>
                        <td width="20%">
                        	<c:forEach items="${freeTypeList}" var="freeType">
                        		<c:if test="${freeType.index == tjInterpretation.freeType }"> ${ freeType.description } </c:if>
                        	</c:forEach>
                        </td>
                        <td>临场语音</td>
                        <td>
                        <c:if test="${!empty tjInterpretation.improvVoiceUrl}"><video src="${baseUrl}${tjInterpretation.improvVoiceUrl}" controls="controls" style="height:50px;width:200px"></video></c:if></td>
                    </tr>
                    <tr>
                        <td>解读标题</td>
                        <td colspan="3" align="left" style="padding-left:50px">${tjInterpretation.title}</td>
                         <td>解读语音</td>
                        <td width="20%">
                        	<c:if test="${!empty tjInterpretation.voiceUrl}"><video src="${baseUrl}${tjInterpretation.voiceUrl}" controls="controls" style="height:50px;width:200px" >
                        	</video></c:if>
                        </td>
                    </tr>
                </table>
                
                <c:if test="${not empty matchVoList}">
	                <c:choose>
	                	<c:when test="${tjInterpretation.raceType == 3 || tjInterpretation.raceType == 4}">
		                	<table class="table_bottom_border ad_tjcont">
		                        <tr bgcolor="#f9f9f9" >
		                            <td rowspan="4" width="14%">推荐内容</td>
		                            <td>1</td>
		                            <td>2</td>
		                            <td>3</td>
		                            <td>4</td>
		                            <td>5</td>
		                            <td>6</td>
		                            <td>7</td>
		                            <td>8</td>
		                            <td>9</td>
		                            <td>10</td>
		                            <td>11</td>
		                            <td>12</td>
		                            <td>13</td>
		                            <td>14</td>
		                        </tr>
		                        <tr>
		                        	<c:forEach items="${matchVoList}" var="matchVo">
		                        		<td>${matchVo.homeTeamName}</td>
		                        	</c:forEach>
		                        </tr>
		                        <tr>
		                            <c:forEach items="${matchVoList}" var="matchVo">
		                        		<td>${empty matchVo.result ? '-' : fn:replace(matchVo.result, ',', '')}</td>
		                        	</c:forEach>
		                        </tr>
		                        <tr>
									<c:forEach items="${matchVoList}" var="matchVo">
		                        		<td>${matchVo.guestTeamName}</td>
		                        	</c:forEach>
		                        </tr>
		                	</table>
		                </c:when>
		                <c:when test="${fn:escapeXml(tjInterpretation.passType) == 2}">
		                	<table class="table_bottom_border ad_tjcont">
		                        <tr bgcolor="#f9f9f9" >
		                            <td rowspan="${fn:length(matchVoList) + 1}" width="14%">推荐内容</td>
		                            <td>玩法</td>
		                            <td>比赛</td>
		                            <td>推荐</td>
		                        </tr>
		                        
		                        <c:forEach items="${matchVoList}" var="matchVo" varStatus="itemFlag">
                        			<tr>
			                            <c:if test="${itemFlag.index == 0}"><td rowspan="${fn:length(matchVoList) + 1}">2串1</td></c:if>
			                            
			                            <td>${matchVo.homeTeamName} VS ${matchVo.guestTeamName}</td>
			                            <td>
			                            	${fn:replace(matchVo.result, "||", "<br>")}
			                            </td>
			                        </tr>
		                        </c:forEach>
		                	</table>
		                </c:when>
		                <c:otherwise>
		                	<table class="table_bottom_border ad_tjcont">
		                        <tr bgcolor="#f9f9f9" >
		                            <td rowspan="${fn:length(matchVoList) + 1}" width="14%">推荐内容</td>
		                            <td>玩法</td>
		                            <td>比赛</td>
		                            <td>推荐</td>
		                        </tr>
		                        
		                        <c:forEach items="${matchVoList}" var="matchVo">
		                        	<c:choose>
										<c:when test="${matchVo.gameType == 4051}">
											<tr>
												<td>胜平负</td>
												<td>${matchVo.homeTeamName} VS ${matchVo.guestTeamName}</td>
												<td>
													<c:if test="${fn:contains(matchVo.result, '3')}">胜</c:if>
													<c:if test="${fn:contains(matchVo.result, '1')}">平</c:if>
													<c:if test="${fn:contains(matchVo.result, '0')}">负</c:if>
												</td>
											</tr>
										</c:when>
		                        		<c:when test="${matchVo.gameType == 4071}">
		                        			<tr>
					                            <td>胜平负</td>
					                            <td>${matchVo.homeTeamName} VS ${matchVo.guestTeamName}</td>
					                            <td>
					                            	<c:if test="${fn:contains(matchVo.result, '3')}">胜</c:if>
					                            	<c:if test="${fn:contains(matchVo.result, '1')}">平</c:if>
					                            	<c:if test="${fn:contains(matchVo.result, '0')}">负</c:if>
					                            </td>
					                        </tr>
		                        		</c:when>
		                        		<c:when test="${matchVo.gameType == 4076}">
		                        			<tr>
					                            <td>让球胜平负(${matchVo.handicap})</td>
					                            <td>${matchVo.homeTeamName} VS ${matchVo.guestTeamName}</td>
					                            <td>
					                            	<c:if test="${fn:contains(matchVo.result, '3')}">胜</c:if>
					                            	<c:if test="${fn:contains(matchVo.result, '1')}">平</c:if>
					                            	<c:if test="${fn:contains(matchVo.result, '0')}">负</c:if>
					                            </td>
					                        </tr>
		                        		</c:when>
		                        		<c:when test="${matchVo.gameType == 4078}">
		                        			<tr>
					                            <td>亚盘(${matchVo.handicap})</td>
					                            <td>${matchVo.homeTeamName} VS ${matchVo.guestTeamName}</td>
					                            <td>
					                            	<c:if test="${fn:contains(matchVo.result, '3')}">主胜</c:if>
					                            	<c:if test="${fn:contains(matchVo.result, '0')}">客胜</c:if>
					                            </td>
					                        </tr>
		                        		</c:when>
		                        		<c:when test="${matchVo.gameType == 4079}">
		                        			<tr>
					                            <td>大小球(${matchVo.handicap})</td>
					                            <td>${matchVo.homeTeamName} VS ${matchVo.guestTeamName}</td>
					                            <td>
					                            	<c:if test="${fn:contains(matchVo.result, '3')}">大球</c:if>
					                            	<c:if test="${fn:contains(matchVo.result, '0')}">小球</c:if>
					                            </td>
					                        </tr>
		                        		</c:when>
		                        		<c:when test="${matchVo.gameType == 4061}">
		                        			<tr>
					                            <td>胜负</td>
					                            <td>${matchVo.guestTeamName} VS ${matchVo.homeTeamName}</td>
					                            <td>
					                            	<c:if test="${fn:contains(matchVo.result, '2')}">主负</c:if>
					                            	<c:if test="${fn:contains(matchVo.result, '1')}">主胜</c:if>
					                            </td>
					                        </tr>
		                        		</c:when>
		                        		<c:when test="${matchVo.gameType == 4062}">
		                        			<tr>
					                            <td>让分胜负(${matchVo.handicap})</td>
					                            <td>${matchVo.guestTeamName} VS ${matchVo.homeTeamName}</td>
					                            <td>
					                            	<c:if test="${fn:contains(matchVo.result, '2')}">主负</c:if>
					                            	<c:if test="${fn:contains(matchVo.result, '1')}">主胜</c:if>
					                            </td>
					                        </tr>
		                        		</c:when>
		                        		<c:when test="${matchVo.gameType == 4063}">
		                        			<tr>
					                            <td>胜分差</td>
					                            <td>${matchVo.guestTeamName} VS ${matchVo.homeTeamName}</td>
					                            <td>
					                            	<c:forEach items="${fn:split(matchVo.result, '-')}" var="rs">
					                            		<c:choose>
					                            			<c:when test="${rs == '01'}">主胜(1-5)</c:when>
					                            			<c:when test="${rs == '02'}">主胜(6-10)</c:when>
					                            			<c:when test="${rs == '03'}">主胜(11-15)</c:when>
					                            			<c:when test="${rs == '04'}">主胜(16-20)</c:when>
					                            			<c:when test="${rs == '05'}">主胜(21-25)</c:when>
					                            			<c:when test="${rs == '06'}">主胜(26+)</c:when>
					                            			<c:when test="${rs == '11'}">客胜(1-5)</c:when>
					                            			<c:when test="${rs == '12'}">客胜(6-10)</c:when>
					                            			<c:when test="${rs == '13'}">客胜(11-15)</c:when>
					                            			<c:when test="${rs == '14'}">客胜(16-20)</c:when>
					                            			<c:when test="${rs == '15'}">客胜(21-25)</c:when>
					                            			<c:when test="${rs == '16'}">客胜(26+)</c:when>
														</c:choose>
					                            	</c:forEach>
					                            </td>
					                        </tr>
		                        		</c:when>
		                        		<c:when test="${matchVo.gameType == 4064}">
		                        			<tr>
					                            <td>大小分(${matchVo.handicap})</td>
					                            <td>${matchVo.guestTeamName} VS ${matchVo.homeTeamName}</td>
					                            <td>
					                            	<c:if test="${fn:contains(matchVo.result, '1')}">大分</c:if>
					                            	<c:if test="${fn:contains(matchVo.result, '2')}">小分</c:if>
					                            </td>
					                        </tr>
		                        		</c:when>
		                        		
		                        	</c:choose>
		                        </c:forEach>
		                	</table>
		                </c:otherwise>
		            </c:choose>
               	</c:if>
               	
               	<div class="pub1">解读摘要</div>
                <div class="reason">${fn:escapeXml(tjInterpretation.shortDesc)}</div>
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