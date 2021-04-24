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
			<jsp:param name="menu" value="order-record"/>
		</jsp:include>
		
        <div class="aside">
            <div class="wrap1080 ppd70 fz14">
                <p class="pu_title bor_bot0">订购购记录</p>
                <div class="clearfix fb_title ml30" style="height: auto;">
                    <div class="fl pr" style="width:80%;">
                        <span class="fl">发布时间</span>
                        <input type="text" id="jq_begin_time" class="input01" placeholder="起始时间" readonly>
                            <i>—</i>
                        <input type="text" id="jq_end_time" class="input01 input02" placeholder="结束时间" readonly>
                        
                        <span class="ml30">计划名称</span>
                        <select id="jq_plan_id" class="ml10">
                       		<option value="">全部</option>
                       		<c:forEach items="${profitList}" var="profitPlan">
                       			<option value="${profitPlan.id}">${profitPlan.title}</option>
                       		</c:forEach>
                       	</select>
                        
                        <span >期状态</span>
                        <select id="jq_period_status" >
                        	<option value="">全部</option>
                        	<option value="1">计划中</option>
                        	<option value="2">已终止</option>
                        </select>
                        
                       <br> 
                        <span>返金币</span>
                        <select id="jq_refund" class="ml10">
                        	<option value="">全部</option>
                        	<option value="1">是</option>
                        	<option value="2">否</option>
                        </select>
                        
                        <span class="ml30">订购类型</span>
                        <select id="jq_free_type" class="ml10">
                        	<option value="">全部</option>
                        	<option value="3">金币订购</option>
                        	<option value="5">免费体验</option>
                        </select>
                    </div>
                    <a href="javascript:;" id="jq_query" class="mt20 ">查询</a>
                    <a href="javascript:;" id="jq_reset" class="mt20 ">重置</a>
                </div>
              
                <!-- 方案列表 -->
                <div class="pl30 mt30 fb_table">
                    <table id="listContainer" width="100%">
                        <tr bgcolor="#f5f5f5" style="color:#545454;">
                            <td width="25%">订购时间</td>
                            <td style="text-align: left;">订购人昵称</td>
                            <td>计划类型</td>
                            <td>计划名称</td>
                            <td>订购期数</td>
                            <td>价格</td>
                            <td>累计盈利SP/净胜场次</td>
                            <td>返金币</td>
                            <td>状态</td>
                        </tr>
                    </table>
                    <div class="get_income">累计返还金币：<strong style="color:#e60012" id="inSumAmount">0</strong>金币
            		<span class="put_money">累计返还人数：<strong style="color: #2eb670;" id="outSumAmount">0</strong>人  </span></div>
            		<input type="hidden" id="jq_periodId" value="${periodId}">
            		<input type="hidden" id="jq_free_status" value="${freeStatus}">
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
			<th>订购时间</th>
			<th>订购人昵称</th>
            <th>计划类型</th>
			<th>计划名称</th>
			<th>订购期数</th>
			<th>价格</th>
			<th>累计盈利SP/净胜场次</th>  <th>返金币</th> <th>状态</th>
		</tr>
	</thead>
	<tbody>
		{{? it.dataList == null || it.dataList.length < 1}}
			<tr><td align="center" colspan="12">暂无数据</td></tr>
		{{?}}
		{{~it.dataList :item:index}}
			<tr>
				<td>{{=item.orderInfo.createTime}}</td>
				<td>{{=item.jcobMember.formatNickName}}</td>
				<td>{{? item.profitPlan.playType == 1}} 2串1 {{??}} 亚盘{{?}} </td>
				<td>{{=item.profitPlan.title}}</td>
				<td>第{{=item.profitPeriod.periodIndex}}期</td>
				<td>
					{{=item.profitPeriod.currentPrice}}
				<td>
					{{? item.profitPeriod.earnSP == null}} 
						等待赚取中
					{{?? item.profitPlan.playType == 1}} 
						{{=item.profitPeriod.earnSP}}sp
					{{?? item.profitPlan.playType == 2}} 
						{{=item.profitPeriod.earnSP}}场	
					{{??}}
					{{?}}		
				</td>
				<td>
					{{? item.refundAcmount == null}}					
						--
					{{??}}
					 	{{= item.refundAcmount}}
					{{?}}
				</td>
				<td>		
					{{?item.profitPeriod.periodStatus == 1}} 
						未发单
					{{??item.profitPeriod.periodStatus == 2}}
					   进行中
					{{??item.profitPeriod.periodStatus == 3}}
					  亏损返金币
					{{??item.profitPeriod.periodStatus == 4}}
					   赚退出
					{{??item.profitPeriod.periodStatus == 5}}
					  已退款赚取退出
					{{??item.profitPeriod.periodStatus == 6}}
					   人工退出
					{{??}}
					{{?}}
				</td>

			


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
	url: "/profit/orderRecordList",
	data: {
		freeStatus: $("#jq_free_status").val(),
		periodId: $("#jq_periodId").val()
	}
});
jieduPage.render();
var queryJieDu = function(){
	jieduPage.render({
		data : {
			startTime: $("#jq_begin_time").val(),
			endTime: $("#jq_end_time").val(),
			profitPlanId: $("#jq_plan_id").val(),
			periodStatus: $("#jq_period_status").val(),
			periodId: $("#jq_periodId").val(),
			freeStatus: $("#jq_free_status").val(),
			isRefund: $("#jq_free_type").val()
		}
	});
}

$(document).ready(function(){
	$( "#jq_begin_time, #jq_end_time").datepicker();
	
	$("#jq_query").click(function(){
		queryJieDu();
	});
	$("#jq_reset").click(function() {
		$("#jq_begin_time").val("");
		$("#jq_end_time").val("");
		$("#jq_plan_id").val("");
		$("#jq_period_status").val("");
		$("#jq_refund").val("");
		$("#jq_free_type").val("");
		queryJieDu();
	});
})
</script>
<script type="text/javascript">
var lookerPage = $.fn.JieDuPage({
	dotList : {containerId : "listContainer-looker", dotId : "dot-list-looker"},
	dotPage : {containerId : "pageContainer-looker", dotId : "dot-page-looker"},	
	pageSize: 10
});

</script>
</body>
</html>
