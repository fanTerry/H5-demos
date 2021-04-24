<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!doctype html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta name=”robots” content=”noindex,nofollow” />
<title>部落(海南)电竞</title>
<link href="" rel="shortcut icon" type="image/x-icon" />

<link rel="stylesheet" href="/resources/plugins/kindeditor/4.1.11/themes/default/default.css"/>
<jsp:include page="/WEB-INF/commons/common-file.jsp"/>
<script type="text/javascript" src="/resources/plugins/jquery.cookie/1.4.1/jquery.cookie.js"></script>
<script type="text/javascript">
var GLOBAL_COMMISSION = ${empty tjConfigResult.prorataCongfigDo.expert_profit_multiply_ratio ? 0 : tjConfigResult.prorataCongfigDo.expert_profit_multiply_ratio};
var GLOBAL_ZQ_SINGLE_SP = '${zqSingleSp}';
var GLOBAL_ZQ_DOUBLE_SP = '${zqDoubleSp}';
var GLOBAL_LQ_SINGLE_SP = '${lqSingleSp}';
var G_ZQ_2C1_PROFIT_RATE = '${zqCgProfitRatio}';
var G_LQ_2C1_PROFIT_RATE = '${lqCgProfitRatio}';
var isImprov = true;
</script>
<body class="admin_bg">
	<!-- 头部 -->
	<jsp:include page="/WEB-INF/commons/header.jsp"/>
	<div class="content clearfix">
        <!-- 左侧菜单 -->
		<jsp:include page="/WEB-INF/commons/left.jsp">
			<jsp:param name="menu" value="publish_${gameCode}"/>
		</jsp:include>	
		<div class="aside">
			<div class="wrap1080 ppd70">
				<p class="pu_title">
                	<c:choose>
                		<c:when test="${playCode == '2c1'}">发布竞彩篮球二串一</c:when>
                		<c:when test="${playCode == 'sf'}">发布竞彩篮球胜负</c:when>
                		<c:when test="${playCode == 'rfsf'}">发布竞彩篮球让分胜负</c:when>
                		<c:when test="${playCode == 'sfc'}">发布竞彩篮球胜分差</c:when>
                		<c:when test="${playCode == 'dxf'}">发布竞彩篮球大小分</c:when>
                		<c:when test="${playCode == 'hh'}">发布竞彩篮球混合</c:when>
                	</c:choose>
                </p>
                <input type="hidden" id="jq_game_code" value="${gameCode}">
                <input type="hidden" id="jq_play_code" value="${playCode}">
                <input type="hidden" id="jq_interp_id" value="${interpId }">
                <!-- 2c1 -->
                <c:if  test="${playCode == '2c1' }" >
                	<jsp:include page="/WEB-INF/jsp/common/lq-2c1-table.jsp"/>
                </c:if>
                
              	<c:if test="${playCode == 'sf' }" >
                	<div class="text_nocenter pub_list_jczq" style="display: block;">
                		<jsp:include page="/WEB-INF/jsp/common/lq-sf-table.jsp"/>
                	</div>
              	</c:if>
              
              	<c:if  test="${playCode == 'rfsf' }" >   <!-- 让分胜负--> 
 					<div class="text_nocenter pub_list_jczq" style="display: block;">
 						<jsp:include page="/WEB-INF/jsp/common/lq-rfsf-table.jsp"/>
 					</div>
              	</c:if>
              
              	<c:if test="${playCode == 'sfc' }" > <!-- 胜分差 -->
	              	<div class="text_nocenter pub_list_jczq" style="display: block;">
	              		<jsp:include page="/WEB-INF/jsp/common/lq-sfc-table.jsp"/>
	              	</div>
              	</c:if>
              	
              	<c:if test="${playCode == 'dxf' }" > <!-- 大小分 -->
	              	<div class="text_nocenter pub_list_jczq" style="display: block;">
	              		<jsp:include page="/WEB-INF/jsp/common/lq-dxf-table.jsp"/>
	              	</div>
              	</c:if>
              	
              	<c:if test="${playCode == 'hh'}" > <!-- 混合 --> -->
	              	<div class="text_nocenter pub_list_jczq" style="display: block;">
	              		<jsp:include page="/WEB-INF/jsp/common/lq-hh-table.jsp"/>
	              	</div>
              </c:if>
                 <!-- 文件上传开始 -->
                <jsp:include page="/WEB-INF/jsp/common/voice-upload.jsp"/>
                <!-- 文件上传结束 -->
                <div class="game_reason">
                    <div class="intro_reason intro_reason2">
	                	<span>解读内容：</span>
	                	<div style="margin-top:-20px">
	                		<textarea id="content" name="content" placeholder="请输入专家解读内容，分析越透彻，内容越详细，查看的用户越多，您的收益也越多！（注：内容不得含非法、虚假内容或联系方式）"></textarea>
						</div>
	                </div>
                    <div class="clearfix">
                        <a class="confirm_push confirm_push2" id="btnNowSubmit">立即发布</a>
                    </div>
                    <p class="pl100 mt20 c666">
                    	<input type="checkbox" id="btnAgree" checked>&nbsp;
                    	<a href="/publish/agree" style="color:#444444" target="_blank">我已阅读并同意《部落(海南)电竞专家解读和推荐服务协议》</a>
                    </p>                  
                </div>
			</div>
		</div>
		<!-- 尾部 -->
	</div>
	<jsp:include page="/WEB-INF/jsp/common/publish-div.jsp"/>
	<jsp:include page="/WEB-INF/commons/footer.jsp"/>
	<script src="/resources/js/jiedu.js"></script>
	<script src="/resources/js/improv/improv_publish_common.js"></script>
	<script src="/resources/js/publish/publish_lq.js"></script>
	<script src="/resources/js/publish/publish_play_type_common.js"></script>
	<script src="/resources/js/publish/publish_play_type_lq.js"></script>
	<script src="/resources/js/publish/publish_event.js"></script>
	<script src="/resources/js/improv/improv_item_save.js"></script>
	<script src="/resources/js/improv_jiedu.js"></script>
	<script type="text/javascript" src="/resources/js/uploadImg/ajaxfileupload.js"></script>
</body>
</html>

