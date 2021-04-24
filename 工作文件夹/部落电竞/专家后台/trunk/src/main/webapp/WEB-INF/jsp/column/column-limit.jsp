<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-cache" />
<title>我的专栏</title>
<link href="" rel="shortcut icon" type="image/x-icon" />
<jsp:include page="/WEB-INF/commons/common-file.jsp" />
<body class="admin_bg">
	<!-- 头部 -->
	<jsp:include page="/WEB-INF/commons/header.jsp" />

	<div class="content clearfix">
		<!-- 左侧菜单 -->
		<jsp:include page="/WEB-INF/commons/left.jsp">
			<jsp:param name="menu" value="column_edit" />
		</jsp:include>

		<div class="aside">
			<div class="wrap1080 ppd70">
				<div class="mt30" id="chooseFileDiv">
					<div class="profit_price" style="margin-left: 220px; padding-top: 200px; font-size: 18px">${msg}</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 尾部 -->
	<jsp:include page="/WEB-INF/commons/footer.jsp" />

</body>
</html>