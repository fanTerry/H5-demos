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
			<jsp:param name="menu" value="article_list"/>
		</jsp:include>
        <div class="aside">
            <div class="wrap1080">
                <p class="pub1">文章信息</p>
                <table class="table_border_color table_bottom_border">
                    <tr>
                    	<td width="100px">文章编号</td>
                        <td align="left" style="padding-left:50px">${article.articleNo}</td>
                    </tr>
                    <tr>
                        <td style="width: 90px">发布时间</td>
                        <td align="left" style="padding-left:50px"><fmt:formatDate value="${article.createTime.time}" pattern="yyyy-MM-dd HH:mm"/></td>
                    </tr>
                	<tr>
                        <td style="width: 90px">文章标题</td>
                        <td align="left" style="padding-left:50px">${article.title}</td>
                    </tr>
                    <%-- <tr>
                        <td style="width: 90px">文章简介</td>
                        <td align="left" style="padding-left:50px">${article.articleDesc}</td>
                    </tr> --%>
                    <tr>
                        <td style="width: 90px">推荐买点</td>
                        <td align="left" style="padding-left:50px">
	                        <c:if test="${!empty cObj.buyPoints}">
	                        	<c:forEach items="${cObj.buyPoints}" var="buyPoint">  
								     <c:if test="${!empty buyPoint.playType}">${buyPoint.playType}</c:if>
								     <c:if test="${!empty buyPoint.sp}">-${buyPoint.sp}</c:if>
								     <br/>
								</c:forEach>
	                        </c:if>
                        </td>
                    </tr>
                    <%-- <tr>
                        <td style="width: 90px">基本面分析</td>
                        <td align="left" style="padding-left:50px">
	                        <c:if test="${!empty cObj.baseFaceAnalysis}">
	                        	<c:forEach items="${cObj.baseFaceAnalysis}" var="baseFaceAnaly">  
								   ${baseFaceAnaly}<br/>
								</c:forEach>
	                        </c:if>
                        </td>
                    </tr> --%>
                    <tr>
                        <td style="width: 90px">内容</td>
                        <td align="left" style="padding-left:50px">${cObj.viewPoint}</td>
                    </tr>
                </table>
            </div>
        </div>        
    </div>
  	<!-- 尾部 -->
	<jsp:include page="/WEB-INF/commons/footer.jsp"/>
</body>
</html>