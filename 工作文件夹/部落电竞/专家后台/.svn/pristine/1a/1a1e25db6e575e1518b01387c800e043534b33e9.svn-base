<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!doctype html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-cache"/>
<title>盈利计划评论管理</title>
<link href="" rel="shortcut icon" type="image/x-icon" />
<jsp:include page="/WEB-INF/commons/common-file.jsp"/>

<body class="admin_bg">
    <!-- 头部 -->
	<jsp:include page="/WEB-INF/commons/header.jsp"/>
	
    <div class="content clearfix">
        <!-- 左侧菜单 -->
		<jsp:include page="/WEB-INF/commons/profit-left.jsp">
			<jsp:param name="menu" value="reply"/>
		</jsp:include>


		<div class="aside">
			<div class="wrap1080 ppd70 fz14">
				<!-- <p class="pu_title bor_bot0">盈利计划评论管理</p> -->
				<div class="clearfix fb_title ml30">
					<div class="fl pr pdr100">
						<span class="fl">评论时间</span> <input type="text" id="beginTime"
							data-for="boxBeginTime" class="input01" placeholder="起始时间"
							readonly> <i>—</i> <input type="text" id="endTime"
							data-for="boxEndTime" class="input01 input02" placeholder="结束时间"
							readonly>
						<div class="fl al_btnbox" style="display: none;">
							<input type="text" name="startCreateTime" id="boxBeginTime"
								placeholder="起始时间" class="input01 input_ad"> <i>—</i> <input
								type="text" name="endCreateTime" id="boxEndTime"
								placeholder="结束时间" class="input01 input02">
							<!--  <a href="javascript:;" id="btnTime" class="red_confire">确定</a> -->
						</div>


						<span class="f_span1" id="btnProfPlan"> <span>盈利计划名称：</span>
							<ul class="pull_down jd_pdown">
								<li style="" data-value="" class="act">全部盈利计划</li>
								<c:forEach items="${profPlanTitleList }" var="profPlanName">
									<li value="${profPlanName }">${profPlanName }</li>
								</c:forEach>
							</ul>
						</span> <span class="f_span1"> <span
							style="padding-right: 25px;">作者</span>
							 <input id="btnAuthor" type="text" class="form-control" name="author" placeholder="评论者昵称"/>
						</span>
					</div>
					<a type="button" class="fr add_bgeb" id="btnQueryProfPlan"> <!-- <span class="glyphicon glyphicon-search"></span>&nbsp; -->查找
					</a>
				</div>
				
				<div class="pl30 mt30 fb_table">
                    <table id="listContainer" width="100%"> </table>
					<div id="pageContainer"  class="paging clearfix">
					
					</div>
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
						<th>评论时间</th>
						<th>盈利计划名称</th>
						<th>评论者昵称</th>
						<th>评论内容</th>
						<th>回复数</th>
						<th>操作</th>
					</tr>	
				</thead>
		<tbody>
			{{? it.dataList.length < 1}}
				<tr><td align="center" colspan="10">暂无数据</td></tr>
			{{?}}
			{{~it.dataList :item:index}}
					<tr>
						<td>{{=item.reply.createTime}}</td>
						<td>{{=item.msgName}}</td>
						<td>{{=item.reply.author}}</td>
						<td>
							<div data-toggle="tooltip" data-placement="bottom" title="{{=item.reply.content}}">
								{{=item.reply.content }}
							</div>
						</td>
						<td>{{=item.subReplyCount}}</td>
						<td>
							<div aria-label="optional" role="group" class="btn-group btn-group-xs">
								<a id="btnDetails" data-id="{{=item.reply.id}}" href = "/replyForProfPlan/replyDetails?replyId={{=item.reply.id}}" style="color: #0082ff;">详情</a>
								&nbsp;|&nbsp;<a href="#" onclick="deleteReply({{=item.reply.id}},'{{=item.reply.author}}');" style="color: #0082ff;">删除</a>	
							</div>
						</td>
					</tr>
			{{~ }}
		</tbody>
	</script>
<script type="text/x-dot-template" id="dot-page-looker">
					
</script>
 
 
 
 
<script type="text/javascript" src="/resources/plugins/jquery.ui/1.11.4/jquery-ui.min.js"></script>
<script type="text/javascript" src="/resources/plugins/doT/doT.min.js"></script>
<script type="text/javascript" src="/resources/js/jiedu.page.js"></script>

 
<script type="text/javascript">
 var jieduPage = $.fn.JieDuPage({
	url: "/replyForProfPlan/list",
	 
 }); 
  jieduPage.render(); 
var queryJieDu = function(){
	jieduPage.render({
		data : {
			beginTime: $("#boxBeginTime").val(),
			endTime: $("#boxEndTime").val(),
			profPlanName: $("#btnProfPlan ul li.act").text(),
			author: $("#btnAuthor").val()
		}
	});
}

function deleteReply(id,author){
	var isDel = confirm("确定要删除"+ author +"的评论吗？");
	if(isDel){
		$.ajax({ url:"/replyForProfPlan/deleteReplyMsg?ids="+id ,async:false});
		$("#btnQueryProfPlan").click();
	}else{
		return;
	}
}

$(document).ready(function(){
	$( "#boxBeginTime, #boxEndTime").datepicker();
	//发布时间
	$("#beginTime, #endTime").click(function(){
		$("div.al_btnbox input:text").removeClass("input_ad");
		$("div.al_btnbox").show();
		var bindId = $(this).data("for");
		$("#" + bindId).addClass("input_ad").focus();
	});
	$("#btnTime").click(function(){
		$("#beginTime").val($("#boxBeginTime").val());
		$("#endTime").val($("#boxEndTime").val());
		$("div.al_btnbox").hide();
	});
	//全部盈利计划
	$("#btnProfPlan").mouseover(function(){
		$("#btnProfPlan").addClass("ad_spancolor");
		$("#btnProfPlan ul").show();
	}).mouseout(function(){
		$("#btnProfPlan").removeClass("ad_spancolor");
		$("#btnProfPlan ul").css("display", "none");
	}); 
	
	$("#btnProfPlan ul li").click(function(){
		$("#btnProfPlan").removeClass("ad_spancolor");
		$("#btnProfPlan ul").css("display", "none");
		
		$(this).siblings(".act").removeClass();
		$(this).addClass("act");
		$("#btnProfPlan span").text($(this).text());
		/*  queryJieDu();   */
	});
	//查询
	$("#btnQueryProfPlan").click(function(){
		queryJieDu();
	});
})
</script>
 
</body>
</html>
