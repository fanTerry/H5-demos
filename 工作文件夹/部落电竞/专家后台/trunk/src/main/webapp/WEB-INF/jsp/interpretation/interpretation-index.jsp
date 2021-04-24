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
			<jsp:param name="menu" value="my_interpretation"/>
		</jsp:include>
		
        <div class="aside">
            <div class="wrap1080 ppd70 fz14">
                <p class="pu_title bor_bot0">发布记录</p>
                <div class="pub_list clearfix mt30">
                    <ul class="rec_li clearfix">
                        <li class="li_rect"><a href="/interpretation">解读</a></li>
                        <li><a href="/article">文章</a></li>
                    </ul>
                </div>
                <!-- 解读 -->
                <div class="clearfix fb_title ml30">
                    <div class="fl pr pdr110">
                        <span class="fl">发布时间:</span>
                         <input type="text" id="jq_begin_time" class="input01" style="margin-left:10px;" placeholder="起始时间" >
                            <i>—</i>
                        <input type="text" id="jq_end_time" class="input01 input02" placeholder="结束时间" >
                        
                        <span>全部解读</span>
                        <select id="jq_race_type">
                       		<option value="">全部</option>
                       		<c:forEach items="${raceTypeList}" var="raceType">
                       			<option value="${raceType.index}">${raceType.description}</option>
                       		</c:forEach>
                       	</select>                       
                        
                        <span>全部状态</span>
                        <select id="jq_race_status">
                       		<option value="">全部</option>
                       		<c:forEach items="${raceStatusList}" var="raceStatus">
                       			<option value="${raceStatus.index}">${raceStatus.description}</option>
                       		</c:forEach>
                       	</select>
                       	<span>发布状态</span>
                        <select id="jq_improv_status">
                       		<option value="">全部</option>
                       		<option value="1">未发布</option>
                       		<option value="2">已发布</option>
                       		<option value="3">已撤销</option>
                       	</select>
                       	<span>销售类别</span>
                        <select id="jq_publish_type">
                       		<option value="">全部</option>
                       		<option value="1">实时解读</option>
                       		<option value="2">临场解读</option>
                       	</select>
                    </div>
                    
                    <a href="javascript:;" id="jq_query" class="fr add_bgeb">查询</a>
                </div>
              
                <!-- 解读表 -->
                <div class="pl30 mt30 fb_table">
                    <table id="listContainer" width="100%">
                        <tr bgcolor="#f5f5f5" style="color:#545454;">
                            <td width="25%">发布时间</td>
                            <td style="text-align: left;">标题</td>
                            <td>解读类型</td>
                            <td>过关类型</td>
                            <td>价格</td>
                            <td>购买人数</td>
                            <td>比赛状态</td>
                            <td width="15%">操作1</td>
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


	<div class="user_list" style="display: none" id="lookerPage">
        <div class="search_number">查看用户列表（<span>${tjInterpretation.lookerCount}</span>人）<a href="javascript:void(0)" class="sprite close" data-mark="x"></a></div>
        <table class="table_border" id="listContainer-looker">
        </table>
        <div class="list_btn" id="pageContainer-looker">
        </div>       
    </div>


<jsp:include page="/WEB-INF/dot/dot-page.jsp"/>
<script type="text/template" id="dot-list" charset="utf-8">
	<thead>
		<tr style="background-color:#dddddd">
			<th>ID</th>
			<th>发布时间</th>
			<th>解读标题</th>
            <th>解读类型</th>
			<td>过关类型</td>
			<th>查看价格</th>
			<th>购买人数</th>
			<th>比赛状态</th> 
			<th>销售类型</th>
			<th>发布结果状态</th>
			<th>留言数</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		{{? it.dataList.length < 1}}
			<tr><td align="center" colspan="12">暂无数据</td></tr>
		{{?}}
		{{~it.dataList :item:index}}
			<tr>
				<td>{{=item.id}}</td>
				<td>{{=item.createTime}}</td>
				<td>{{=item.title}}</td>
				<td>
					{{?item.raceType == 1}}
						竞彩足球
					{{??item.raceType == 2}}
						竞彩篮球
					{{??item.raceType == 3}}
						胜负彩
					{{??item.raceType == 4}}
						任选9场
					{{??item.raceType == 5}}
						北京单场
					{{??}}
						------
					{{?}}
				</td>
				<td>
					{{?item.passType == 1}}
						单关
					{{??item.passType == 2}}
						2串1
					{{??item.passType == 3}}
						3串1
					{{??}}
						单关
					{{?}}
				</td>
				<td>{{=item.price}}</td>
				<td name="lookerCount" data-id="{{=item.id}}" style="cursor:pointer;color: #0082ff;">{{=item.lookerCount}}</td>
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
					{{?item.publishType == 1}}
						普通解读
					{{?? item.publishType == 2}}
						临场解读
					{{?}}
				</td>
				<td>
					{{? item.publishType == 1 }}
						实时发布					
					{{?? item.improvStatus == 1 }}
						未发布
					{{?? item.improvStatus == 2}}
						已发布
					{{?? item.improvStatus == 3}}
						过期撤单
					{{?}}
				</td>
				<td>{{=item.interpNoteCounts}}</td>
				<td><a href="/interpretation/show/{{=item.id}}" style="color: #0082ff;">详情 </a>
					{{? item.publishType == 2 && item.improvStatus == 1}}
						 | <a href="/improv/publishIndex?interId={{=item.id}}" style="color: #0082ff;">发布结果</a>
					{{?}}
					{{? item.interpNoteCounts > 0}}|<a href="/comment/index?interpretationId={{=item.id}}" style="color: #0082ff;">查看留言</a>{{?}}</td>
			</tr>
		{{~}}
	</tbody>
</script>	

<script type="text/template" id="dot-list-looker" charset="utf-8">		
	<thead>
		<tr>
			<th>用户昵称</th>
			<th>用户身份</th>
            <th>查看时间</th>
		</tr>
	</thead>
	<tbody>
		{{? it.dataList.length < 1}}
			<tr><td align="center" colspan="3">暂无数据</td></tr>
		{{?}}
		{{~it.dataList :item:index}}
			<tr>
				<td>{{=item.nickName}}</td>
           		<td>
					{{?item.level == 0}}普通用户
					{{??item.level == 1}}实验室用户
					{{??item.level == 2}}战绩红人
					{{??item.level == 4}}名人专家
					{{??item.level == 7}}消费代理
					{{??}}------
					{{?}}
				</td>
            	<td><span>{{=item.lookedTime.substr(5,5)}}</span> <span>{{=item.lookedTime.substr(11,5)}}</span></td>
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
	url: "/interpretation/list"
});
jieduPage.render();
var queryJieDu = function(){
	jieduPage.render({
		data : {
			beginTime: $("#jq_begin_time").val(),
			endTime: $("#jq_end_time").val(),
			raceType: $("#jq_race_type").val(),
			raceStatus: $("#jq_race_status").val(),
			publishType: $("#jq_publish_type").val(),
			improvStatus: $("#jq_improv_status").val()
		}
	});
}

$(document).ready(function(){
	$("#jq_begin_time, #jq_end_time").datepicker();
	$("#jq_query").click(function() {
		queryJieDu();
	})
	
	//重置表单
	$("#btnReset").click(function(){
		$("div.fb_title input").val("");
		$("div.fb_title span.f_span1").each(function(){
			var li_first = $(this).find("li:first");
			li_first.siblings(".act").removeClass();
			li_first.addClass("act");
			$(this).find("span").text(li_first.text());
		});
		
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

$(function(){
	//查看购买人数
	$(document).on("click", "td[name=lookerCount]", function(){
		var id = $(this).data("id");
		
		lookerPage.render({
			url: "/interpretation/looker/" + id,
		});
		
		webAlert({
			title:false,
            content:$("#lookerPage")[0],
            padding:"0 0 20px 0",
		})
	});
});
</script>
</body>
</html>
