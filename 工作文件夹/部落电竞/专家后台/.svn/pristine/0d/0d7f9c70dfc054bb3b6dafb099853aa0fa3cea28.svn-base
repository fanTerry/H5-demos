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
                <p class="pu_title bor_bot0">盈球公告</p>
                <div class="pl30 fb_table">
                    <table id="listContainer" width="100%" style="border:0">          
                    </table>
                </div>
                
                <div id="pageContainer"  class="paging clearfix">              
                </div>             
            </div>        
        </div>
    </div>
           
    <!-- 尾部 -->
	<jsp:include page="/WEB-INF/commons/footer.jsp"/>

<jsp:include page="/WEB-INF/dot/dot-page.jsp"/>

<script type="text/template" id="dot-list" charset="utf-8">
	<thead>
	</thead>
	<tbody>
		{{? it.dataList.length < 1}}
			<tr><td align="center" colspan="12">暂无数据</td></tr>
		{{?}}
		{{~it.dataList :item:index}}
			<tr>
				<td align="left">
					<div class="pu_title pad200">
                    	<p><span class="fs14 c_999">{{=item.createTime}}</span>&nbsp&nbsp
                    	<a href="news/detail?id={{=item.id}}" class="xh_title"><span class="fs18 c_333 fwb">{{=item.title}}</span></p></a>
                    	<p class="c_666 fs14">{{=item.summary}}</p>
                    	<a href="news/detail?id={{=item.id}}" class="look_p">查看全文  >></a>
                	</div>
				</td>			
			</tr>
		{{~}}
	</tbody>
</script>	

<script type="text/javascript" src="/resources/plugins/jquery.ui/1.11.4/jquery-ui.min.js"></script>
<script type="text/javascript" src="/resources/plugins/doT/doT.min.js"></script>
<script type="text/javascript" src="/resources/js/jiedu.page.js"></script>
<script type="text/javascript">
var jieduPage = $.fn.JieDuPage({
	url: "/news/list"
});
jieduPage.render();
var queryJieDu = function(){
	jieduPage.render({
		data : {
			pageNo: 1
		}
	});
}

$(document).ready(function(){

})
</script>
</body>
</html>
