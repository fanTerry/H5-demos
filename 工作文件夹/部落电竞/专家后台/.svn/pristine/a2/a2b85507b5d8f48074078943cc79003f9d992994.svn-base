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
			<jsp:param name="menu" value="publish_period"/>
		</jsp:include>
		
        <div class="aside">
            <div class="wrap1080 ppd70 fz14">
                <p class="pu_title bor_bot0">发布记录</p>
                <div class="clearfix fb_title ml30">
                    <div class="fl pr pdr100">
                        <span class="fl">发布时间</span>
                        <input type="text" id="jq_begin_time" class="input01" placeholder="起始时间" readonly>
                            <i>—</i>
                        <input type="text" id="jq_end_time" class="input01 input02" placeholder="结束时间" readonly>
                        
                        <span class="ml20">计划名称</span>
                        <select id="jq_plan_id">
                       		<option value="">全部</option>
                       		<c:forEach items="${profitList}" var="profitPlan">
                       			<option value="${profitPlan.id}">${profitPlan.title}</option>
                       		</c:forEach>
                       	</select>
                        
                        <span class="ml20">期状态</span>
                        <select id="jq_period_status">
                        	<option value="">全部</option>
                        	<option value="1">计划中</option>
                        	<option value="2">已终止</option>
                        </select>
                        
                        <span class="ml20">返金币</span>
                        <select id="jq_refund">
                        	<option value="">全部</option>
                        	<option value="1">是</option>
                        	<option value="2">否</option>
                        </select>
                        
                    </div>
                    <a href="javascript:;" id="jq_query" style="float:rigth;">查询</a>
                </div>
              
                <!-- 方案列表 -->
                <div class="pl30 mt30 fb_table">
                    <table id="listContainer" width="100%">
                        <tr bgcolor="#f5f5f5" style="color:#545454;">
                            <td width="25%">发布时间</td>
                            <td style="text-align: left;">推荐编号</td>
                            <td>盈利计划类型</td>
                            <td>盈利计划名称</td>
                            <td>开启期数</td> <td>订购人数</td> <td>免费体验人数</td> 
                            <td>价格</td>
                            <td>累计盈利SP/场次</td>
                            <td>返金币</td>
                            <td width="15%">期状态</td>
                        </tr>
                    </table>
                    <div class="get_income">累计返还金币：<strong style="color:#e60012" id="inSumAmount">xx</strong>金币
            		<span class="put_money">累计返还人数：<strong style="color: #2eb670;" id="outSumAmount">0</strong>人  </span></div>
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
			<td>盈利计划类型</td>
			<td>盈利计划名称</td>
			<td>开启期数</td> 
			<td>订购人数</td> 
			<td>免费体验人数</td> 
            <td>价格</td>
            <td>累计盈利SP/场次</td>
			<td>返金币</td>
 			<td width="15%">期状态</td>
		</tr>
	</thead>
	<tbody>
		{{? it.dataList == null || it.dataList.length < 1}}
			<tr><td align="center" colspan="12">暂无数据</td></tr>
		{{?}}
		{{~it.dataList :item:index}}
			{{? item.profitPlan != null}} 
				<tr>
				<td>{{=item.profitPeriod.createTime}}</td>
				<td>
					{{=item.profitPlan.playType}}
				</td>
				<td>
					{{=item.profitPlan.title}}
				</td>
				<td>第{{=item.profitPeriod.periodIndex}}期 </td>
				<td><a href="/profit/orderRecord?periodId={{=item.profitPeriod.id}}&freeStatus=5" style="color: #0082ff;" title="订单详情"> {{= item.orderNum }} 人</a></td>
				<td><a href="/profit/orderRecord?periodId={{=item.profitPeriod.id}}&freeStatus=5" style="color: #0082ff;" title="订单详情">{{=item.freeOrderNum}}人</a></td>
				<td>{{=item.profitPeriod.currentPrice}}</td>
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
					{{??item.raceStatus == 2}}
						进行中
					{{??item.raceStatus == 3}}
						亏损返金币
					{{??item.raceStatus == 4}}
						赚退出
					{{??item.raceStatus == 5}}
						已退款赚取退出
					{{??item.raceStatus == 6}}
						人工退出
					{{??}}
						--
					{{?}}
				</td>
			</tr>
{{?}}
		{{~}}
	</tbody>
</script>	

<script type="text/javascript" src="/resources/plugins/jquery.ui/1.11.4/jquery-ui.min.js"></script>
<script type="text/javascript" src="/resources/plugins/doT/doT.min.js"></script>
<script type="text/javascript" src="/resources/js/jiedu.page.js"></script>
<script type="text/javascript">
var jieduPage = $.fn.JieDuPage({
	url: "/profit/periodRecordList",
	afterSuccess: function(json){
		$("#inSumAmount").text(json.amountAndMembers.totalRefundAmount);
		$("#outSumAmount").text(json.amountAndMembers.totalRefundCount);
		console.log(json.amountAndMembers.totalRefundAmount);
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
			isRefund: $("#jq_refund").val()
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
		queryJieDu();
	});
})
</script>

</body>
</html>
