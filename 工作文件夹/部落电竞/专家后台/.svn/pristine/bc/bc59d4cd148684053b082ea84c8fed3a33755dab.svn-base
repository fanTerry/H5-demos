<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!doctype html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-cache"/>
<title>部落(海南)电竞-计划查询</title>
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
            <div class="wrap1080 ppd70 fz14">
                <p class="pu_title bor_bot0">发布记录</p>
                <div class="clearfix fb_title ml30">
                    <div class="fl pr pdr100">
                        <span class="fl">发布时间</span>
                        <input type="text" id="jq_begin_time" class="input01" placeholder="起始时间" >
                            <i>—</i>
                        <input type="text" id="jq_end_time" class="input01 input02" placeholder="结束时间" >
                        
                        <span>计划名称</span>
                        <select id="jq_plan_id">
                       		<option value="">全部</option>
                       		<c:forEach items="${profitList}" var="profitPlan">
                       			<option value="${profitPlan.id}">${profitPlan.title}</option>
                       		</c:forEach>
                       	</select>
                        
                        <span>计划状态</span>
                        <select id="jq_plan_status">
                       		<option value="">全部</option>
                       		<c:forEach items="${planStatus}" var="status">
                       			<option value="${status.index}">${status.description}</option>
                       		</c:forEach>
                       	</select>
                       	                       
                    </div>
                    <a href="javascript:;" id="jq_query" >查询</a>
                    <a href="/profit/addProfitPlanIndex"  >新增</a>
                </div>
              
                <!-- 方案列表 -->
                <div class="pl30 mt30 fb_table">
                    <table id="listContainer" width="100%">
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
		<tr style="background-color:#dddddd">
			 <td width="25%">发布时间</td>
                            <td>盈利计划类型</td>
                            <td>盈利计划名称</td>
                            <td>价格</td>
                            <td>计划状态</td>
                            <td>操作</td>
		</tr>
	</thead>
	<tbody>
		{{? it.dataList.length < 1}}
			<tr><td align="center" colspan="12">暂无数据</td></tr>
		{{?}}
		{{~it.dataList :item:index}}
			<tr>
				<td>
					{{=item.createTime}}
				</td>
				<td>{{? item.playType == 1}} 2串1 {{??}} 亚盘{{?}} </td>
				<td>
					{{=item.title}}
				</td>
				<td>{{=item.price}}</td>
				<td>
					{{? item.status == 1}} 
						正常
					{{?? item.status == 2}} 
						禁用
					{{?? item.status == 3}} 
						待审核
					{{?? item.status == 4}} 
						<a href="/profit/toEditProfitPlan?profitPlanId={{=item.id}}" style="color: #0082ff;" title="审核驳回">审核驳回</a>
					{{??}}
					{{?}}
				</td>
				<td>
					<a href="/profit/planDetail/{{=item.id}}" style="color: #0082ff;">查看</a>
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
	url: "/profit/planRecordList",
	data: {
		status: 111,
		profitPlanId: $("#jq_plan_id").val()
	}
});

jieduPage.render();
var queryJieDu = function(){
	var _status = 111;
	if($("#jq_plan_status").val() == "") {
		_status = 111;
	} else {
		_status = $("#jq_plan_status").val();
	}
	jieduPage.render({
		data : {
			startTime: $("#jq_begin_time").val(),
			endTime: $("#jq_end_time").val(),
			status: _status,
			profitPlanId: $("#jq_plan_id").val()
		}
	});
}

$(document).ready(function(){
	$( "#jq_begin_time, #jq_end_time").datepicker();
	$("#jq_query").click(function() {
		queryJieDu();
	})
})
</script>
</body>
</html>
