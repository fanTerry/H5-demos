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
			<jsp:param name="menu" value="publish_record"/>
		</jsp:include>
		
        <div class="aside">
            <div class="wrap1080 ppd70 fz14">
                <p class="pu_title bor_bot0">发布记录</p>
                <div class="clearfix fb_title ml30" style="height: auto;">
                    <div class="fl pr" style="width:80%;">
                        <span class="fl">发布时间</span>
                        <input type="text" id="jq_begin_time" class="input01" placeholder="起始时间" readonly>
                            <i>—</i>
                        <input type="text" id="jq_end_time" class="input01 input02" placeholder="结束时间" readonly>
                        
                        <span class="ml30">计划名称</span>
                        <select id="jq_plan_id">
                       		<option value="">全部</option>
                       		<c:forEach items="${profitList}" var="profitPlan">
                       			<option value="${profitPlan.id}">${profitPlan.title}</option>
                       		</c:forEach>
                       	</select>
                       	
                        <span class="ml30">赛事状态</span>
                        <select id="jq_race_status">
                       		<option value="">全部</option>
                       		<c:forEach items="${allRaceStatus}" var="raceType">
                       			<option value="${raceType.index}">${raceType.description}</option>
                       		</c:forEach>
                       	</select>    
                       	<br>              
                        <span >命中状态</span>
                        <select id="jq_win_status">
                       		<option value="">全部</option>
                       		<c:forEach items="${allWinStatus}" var="winStatus">
                       			<option value="${winStatus.index}">${winStatus.description}</option>
                       		</c:forEach>
                       	</select> 
                       	<span style="padding-right:25px;" class="ml30">推荐编号：<input id="jq_plan_tj_no"></span>
                    </div>
                    <a href="javascript:;" id="jq_query" class="mt20 mr10">查询</a>
                    <a href="javascript:;" id="jq_reset" class="mt20 mr10">重置</a>
                </div>
              
                <!-- 方案列表 -->
                <div class="pl30 mt30 fb_table">
                    <table id="listContainer" width="100%">
                        <tr bgcolor="#f5f5f5" style="color:#545454;">
                            <td width="25%">发布时间</td>
                            <td style="text-align: left;">推荐编号</td>
                            <td>盈利计划类型</td>
                            <td>盈利计划名称</td>
                            <td>开启期数</td>
                            <td>推荐状态</td>
                            <td>命中状态</td>
                            <td width="15%">操作</td>
                        </tr>
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
			<th>发布时间</th>
			<th>推荐编号</th>
			<td>盈利计划类型</td>
			<td>盈利计划名称</td>
			<td>开启期数</td>
			<th>推荐状态</th>
			<th>命中状态</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		{{? it.dataList == null || it.dataList.length < 1}}
			<tr><td align="center" colspan="12">暂无数据</td></tr>
		{{?}}
		{{~it.dataList :item:index}}
			<tr>
				<td>{{=item.createTime}}</td>
				<td>{{=item.planTjNo}}</td> 
				<td>{{? item.profitPlan.playType == 1}} 2串1 {{??}} 亚盘{{?}}</td>
				<td>
					{{=item.profitPlan.title}}
				</td>
				<td>第{{=item.tjIndex}}期 </td>
				<td>
					{{?item.raceStatus == 0}}
						未开赛
					{{??item.raceStatus == 1}}
						比赛中
					{{??item.raceStatus == 2}}
						已完场
					{{??item.raceStatus == 3}}
						已取消
					{{??item.raceStatus == 4}}
						未开奖
					{{??item.raceStatus == 5}}
						开奖中
					{{??item.raceStatus == 6}}
						已开奖
					{{??item.raceStatus == 7}}
						已停赛
					{{??item.raceStatus == 8}}
						已延期
					{{??}}
						------
					{{?}}
				</td>
				<td>
					{{?item.winStatus == 0}}
						未开
					{{??item.winStatus == 1}}
						未中
					{{??item.winStatus == 2}}
						已中
					{{?}}
				</td>
				<td><a href="/profit/publishRecordDetail/{{=item.planTjNo}}" style="color: #0082ff;" title="订单详情">详情</a></td>				
			</tr>
		{{~}}
	</tbody>
</script>	
<script type="text/x-dot-template" id="dot-page-looker">
	{{? it.dataList.length > 0}}
		<a href="javascript:void(0)" class="list_anniu next_btn" data-num={{=it.pageNo > 1 ? it.pageNo-1 : it.pageNo}}>上一页</a>
		<a href="javascript:void(0)" class="list_anniu next_btn" data-num={{=it.pageNo < it.totalPages ? it.pageNo+1 : it.pageNo}}>下一页</a>
	{{?}}
</script>

<script type="text/javascript" src="/resources/plugins/jquery.ui/1.11.4/jquery-ui.min.js"></script>
<script type="text/javascript" src="/resources/plugins/doT/doT.min.js"></script>
<script type="text/javascript" src="/resources/js/jiedu.page.js"></script>
<script type="text/javascript">
var jieduPage = $.fn.JieDuPage({
	url: "/profit/publishRecordList",
});
jieduPage.render();
var queryJieDu = function(){
	jieduPage.render({
		data : {
			startTime: $("#jq_begin_time").val(),
			endTime: $("#jq_end_time").val(),
			profitPlanId : $("#jq_plan_id").val(),
			raceStatus : $("#jq_race_status").val(),
			winStatus : $("#jq_win_status").val(),
			planTjNo: $("#jq_plan_tj_no").val()
		}
	});
}

$(document).ready(function(){
	$("#jq_begin_time, #jq_end_time").datepicker();
	$("#jq_query").click(function(){
		queryJieDu();
	});
	$("#jq_reset").click(function() {
		$("#jq_begin_time").val("");
		$("#jq_end_time").val("");
		$("#jq_plan_id").val("");
		$("#jq_race_status").val("");
		$("#jq_win_status").val("");
		$("#jq_plan_tj_no").val("");
		queryJieDu();
	});
})
</script>
<script type="text/javascript">
var lookerPage = $.fn.JieDuPage({
	dotList : {containerId : "listContainer-looker", dotId : "dot-list-looker"},
	dotPage : {containerId : "pageContainer-looker", dotId : "dot-page-looker"},	
	pageSize: 6
});

</script>
</body>
</html>
