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

<body class="admin_bg">
    <!-- 头部 -->
	<jsp:include page="/WEB-INF/commons/header.jsp"/>
	
    <div class="content clearfix">
        <!-- 左侧菜单 -->
		<jsp:include page="/WEB-INF/commons/profit-left.jsp">
			<jsp:param name="menu" value="plan_record"/>
		</jsp:include>
		<div class="aside">
            <p class="aside_word"><span>计划管理</span> > <span>盈利计划详情页</span>
            <div class="wrap1080">
                <p class="pub1">基本信息</p>
                <table class="table_border_color table_bottom_border">
                    <tr>
                        <td>盈利计划id</td>
                        <td>${detail.profitPlan.id }</td>
                        <td>发布时间</td>
                        <td><fmt:formatDate value="${detail.profitPlan.createTime.time}" pattern="yyyy-MM-dd HH:mm"/></td>
                        <td>盈利计划名称</td>
                        <td >${detail.profitPlan.title }</td>
                        <td>玩法类型</td>
                        <td >
                        	<c:forEach items="${palyTypeList}" var="playType">
                        		<c:if test="${playType.index == detail.profitPlan.playType }"> ${playType.description }</c:if>
                        	</c:forEach>
                        <td>风险类型</td>
                        <td>
                        <c:forEach items="${riskTypeList}" var="riskType">
                        		<c:if test="${riskType.index == detail.profitPlan.riskType }"> ${riskType.description }</c:if>
                        	</c:forEach>
                        <td>价格</td>
                        <td>${detail.profitPlan.price }</td>
                    </tr>
                    <tr>
                        <td>logo</td>
                        
                        <!-- http://r.aicaicdn.com/${detail.profitPlan.imgAdress } -->
                        <td colspan="11">
	                        <div style="text-align:left;">
		                        <img height="100" width="80" src="https://ur.ttyingqiu.com/${ fn:replace(detail.profitPlan.imgAdress, "http://m.aicaicdn.com", "") }"/>
	                        </div>
                        </td>
                    </tr>
                </table>
                <p class="pub1">详细信息</p>
                <table class="table_border_color table_bottom_border">
                    <tr>
                        <td>盈利计划特点</td>
                        <td width="35%">
                        	${detail.profitPlan.profitFeatures}
                        </td>
                        <td>出单频率</td>
                        <td width="35%">
                        	${detail.profitPlanDetails.sendFreq}
                        </td>
                    </tr>
                    <tr>
                        <td>中奖倍数区间</td>
                        <td width="35%">
                        	${detail.profitPlanDetails.winMultiple}
                        </td>
                        <td>单周发布方案个数</td>
                        <td width="35%">
                        	${detail.profitPlanDetails.weekSend}
                        </td>
                    </tr>
                    <tr>
                        <td>平均中奖频率</td>
                        <td width="35%">
                        	${detail.profitPlanDetails.avgWinFreq}
                        </td>
                        <td>单期最多方案个数</td>
                        <td width="35%">
                        	${detail.profitPlanDetails.predictMaxLost}
                        </td>
                    </tr>
                    <tr>
                        <td>适合用户</td>
                        <td width="35%">
                        	${detail.profitPlanDetails.suitUser}
                        </td>
                        <td></td>
                        <td width="35%">
                        </td>
                    </tr>
                </table>
               	
               	<div class="pub1">服务商介绍</div>
                <div class="reason">
                	${detail.profitPlan.provideDesc}  
                </div>
                
                <div class="pub1">盈利计划介绍</div>
                <div class="reason">
                	${detail.profitPlan.description}  
                </div>
            </div>
        </div>        
    </div>
    <!-- 尾部 -->
	<jsp:include page="/WEB-INF/commons/footer.jsp"/>
</body>
</html>
