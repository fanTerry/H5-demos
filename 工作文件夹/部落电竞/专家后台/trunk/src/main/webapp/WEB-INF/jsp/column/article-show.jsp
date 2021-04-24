<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-cache"/>
<title>部落(海南)电竞-专栏文章</title>
<link href="" rel="shortcut icon" type="image/x-icon" />
<jsp:include page="/WEB-INF/commons/common-file.jsp"/>

<body class="admin_bg">
    <!-- 头部 -->
	<jsp:include page="/WEB-INF/commons/header.jsp"/>
	
    <div class="content clearfix">
        <!-- 左侧菜单 -->
		<jsp:include page="/WEB-INF/commons/left.jsp">
			<jsp:param name="menu" value="column_article"/>
		</jsp:include>
		
        <div class="aside">
            <p class="aside_word"><span>我的专栏文章</span> > <span>详情页</span></p>
            <div class="wrap1080">
                <p class="pub1">文章信息</p>
                <table class="table_border_color table_bottom_border">
                    <tr>
                    <td width="100px">文章编号</td>
                        <td>${tjExpertColumnArticle.id}</td>
                         <td width="100px">允许试读</td>
                        <td><c:if test="${tjExpertColumnArticle.isFree==1}">是 </c:if><c:if test="${tjExpertColumnArticle.isFree==0}">否 </c:if></td>
                        <td width="100px">发布时间</td>
                        <td><fmt:formatDate value="${tjExpertColumnArticle.createTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                        <td width="100px">发布终端</td>
                        <td>web</td>
                    </tr>
                	<tr>
                        <td style="width: 90px">文章标题</td>
                        <td colspan="7" align="left" style="padding-left:50px">${tjExpertColumnArticle.title}</td>
                    </tr>
                    <c:if test="${!empty tjExpertColumnArticle.voiceUrl}">
                    <tr>
                        <td style="width: 90px">语音文件</td>
                        <td colspan="7" align="left" style="padding-left:50px">
                        <video id="player" src="${baseUrl}${tjExpertColumnArticle.voiceUrl}" controls="controls" style="height:50px"></video>
                        </td>
                    </tr>
                     </c:if>
                </table>
                <div class="pub1">文章内容</div>
                <div class="reason">${tjExpertColumnArticle.content}</div>
            </div>
        </div>        
    </div>

  	<!-- 尾部 -->
	<jsp:include page="/WEB-INF/commons/footer.jsp"/>
</body>
</html>