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
		<jsp:include page="/WEB-INF/commons/left.jsp">
			<jsp:param name="menu" value="news"/>
		</jsp:include>
		
        <div class="aside">
            <div class="wrap1080 ppd70 fz14">
                <p class="xqy_adv">盈球公告  >  详情页</p>
                <div class="pu_title pad40100 fs14 c_333">
                    <h2 class="tac fs32">${title }</h2>
                    <div class="tac c_999 mt10"><fmt:formatDate value="${publishTime}" type="both" timeStyle="default" pattern="yyyy-MM-dd HH:mm "/></div>
                    <div class="fs14 c_333 lh30">
                        ${divContent}                      
                     </div>
                    <div class="tac mt40">
                   		<!--  表格 -->               
                    </div>
                </div>
                  
            </div>
        </div>
    </div>
           
    <!-- 尾部 -->
	<jsp:include page="/WEB-INF/commons/footer.jsp"/>


</body>
</html>
