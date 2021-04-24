<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!doctype html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-cache"/>
<title>盈球专栏-留言管理</title>
<link href="" rel="shortcut icon" type="image/x-icon" />
<jsp:include page="/WEB-INF/commons/common-file.jsp"/>

<body class="admin_bg">
    <!-- 头部 -->
	<jsp:include page="/WEB-INF/commons/header.jsp"/>
	
    <div class="content clearfix">
        <!-- 左侧菜单 -->
		<jsp:include page="/WEB-INF/commons/left.jsp">
			<jsp:param name="menu" value="column_reply"/>
		</jsp:include>
		
        <div class="aside">
            <div class="wrap1080 ppd70 fz14">
                <p class="pu_title bor_bot0">留言管理</p>
                <!-- 查询 -->
                <div class="clearfix fb_title ml30">
                    <div class="fl pr pdr100">
                        <span class="fl">留言时间</span>
                        <input type="text" id="beginTime" data-for="boxBeginTime" class="input01" placeholder="起始时间" readonly>
                            <i>—</i>
                        <input type="text" id="endTime" data-for="boxEndTime" class="input01 input02" placeholder="结束时间" readonly>
                    	<a href="javascript:;" id="btnSearch" class="red_confire">搜索</a>
                    </div>
                     <a href="javascript:;" id="btnReset" class="fr add_bgeb">重置</a>
                </div>
              
                <!-- 文章表 -->
                <div class="pl30 mt30 fb_table">
                    <table id="listContainer" width="100%">
<!--                     
                        <tr bgcolor="#f5f5f5" style="color:#545454;">
                            <td width="25%">发布时间</td>
                            <td style="text-align: left;">标题</td>
                            <td width="15%">操作</td>
                        </tr>
                        <tr>
                            <td style="color:#999999;">2016-01-04 15:32</td>
                            <td style="color:#444444;text-align: left;">胜负彩第24938期解读...</td>
                            <td style="color: #0082ff;">查看留言</td>
                        </tr>
                        <tr>
                            <td style="color:#999999;">2016-01-04 15:32</td>
                            <td style="color:#444444;text-align: left;">胜负彩第24938期解读...</td>
                            <td style="color: #0082ff;">查看留言</td>
                        </tr>
 -->                       
                    </table>
                </div>
                
                <div id="pageContainer"  class="paging clearfix">
<!-- 					
					<div class="list_number">共<span>25</span>条，每页<span>15</span>条</div>
                        <ul class="clearfix">
                           <li class="pre_li">上一页</li>
                           <li class="paging_border">1</li>
                           <li>2</li>
                           <li>3</li>
                           <li class="paging_border">...</li>
                           <li>20</li>
                           <li class="pre_li">下一页</li>
                        </ul>
                        <div class="paging_total">共<em>20</em>页  到第<input type="text" placeholder="1">页</div>
                        <a href="" class="confirm">确定</a>
 -->                        
                </div>          
            </div>        
        </div>
    </div>
           
    <!-- 尾部 -->
	<jsp:include page="/WEB-INF/commons/footer.jsp"/>

<jsp:include page="/WEB-INF/dot/dot-page.jsp"/>
<script type="text/template" id="dot-list" charset="utf-8">
	<thead>
		<tr bgcolor="#f5f5f5" style="color:#545454;">
			<td width="120px">编号</td>
			<td width="150px">留言时间</td>
			<td>留言人昵称</td>
			<td>文章ID</td>
			<td>留言内容</td>
			<td>留言点赞</td>
			<td>操作</td>
		</tr>
	</thead>
	<tbody>
		{{? it.dataList.length < 1}}
			<tr><td align="center" colspan="6">暂无留言</td></tr>
		{{?}}
		{{~it.dataList :item:index}}
			<tr>
				<td>{{=item.id}}</td>
			 	<td>{{=item.createTime}}</td>
				<td>{{=item.nickName}}</td>
				<td><a href="/column/showArticle/{{=item.sourceId}}" style="color: #0082ff;" target="blank">{{=item.sourceId}}</a></td>
				<td>{{=item.content}}</td>
				<td>{{=item.likeCount}}</td>
				<td>
					<a href="/column/replyDetail/{{=item.id}}" style="color: #0082ff;">回复</a>
					{{? item.isShow ==1}}| <a href="javascript:void(0)" id="btnSetShow" data-id="{{=item.id}}" data-show="0" style="color: #0082ff;">隐藏</a> {{?}}
					{{? item.isShow ==0}}| <a href="javascript:void(0)" id="btnSetShow" data-id="{{=item.id}}" data-show="1" style="color: #0082ff;">显示</a> {{?}}
					| <a href="javascript:void(0)" id="btnDelete" data-id="{{=item.id}}" style="color: #0082ff;">删除</a>
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
	url: "/column/listReply"
});
jieduPage.render();
var queryJieDu = function(){
	jieduPage.render({
		data : {
			pageNo: 1,
			createBeginTime: $("#beginTime").val(),
			createEndTime: $("#endTime").val()
		}
	});
}

$(document).ready(function(){
	$( "#beginTime, #endTime").datepicker();
	
	$("#btnSearch").click(function(){
		queryJieDu();
	});
	
	//重置表单
	$("#btnReset").click(function(){
		$("div.fb_title input").val("");
		queryJieDu();
	});
	
	//设置试读
	$(document).on("click", "#listContainer #btnSetShow", function(){
		var _this = this;
		var show = $(_this).data("show");
		var confirmMsg = '确定要隐藏吗？';
		if(show==1)
		{
			confirmMsg = '确定要显示吗？';
		}
	    var sure =confirm(confirmMsg);
	    if(!sure) return;
		if($(_this).data('isLock')){return;};
		$(_this).data('isLock', 1);
		
		var id = $(_this).data("id");
		var freeVar = show==1?'true':'false';
		$.getJSON("/column/showReply/" + id+"/"+freeVar, function(json){
			$(_this).data('isLock', 0);		
			webAlert({title:'提示', content:json.msg, width:250, time:2});
			if(json.isSuccess){	
				queryJieDu();
			}
		});
	});
	
	//删除文章
	$(document).on("click", "#listContainer #btnDelete", function(){
		var _this = this;
	    var sure =confirm("确定要删除吗？");
	    if(!sure) return;
		if($(_this).data('isLock')){return;};
		$(_this).data('isLock', 1);
		
		var id = $(_this).data("id");
		
		$.getJSON("/column/deleteReply/" + id, function(json){
			$(_this).data('isLock', 0);		
			
			webAlert({title:'提示', content:json.msg, width:250, time:2});
			
			if(json.isSuccess){	
				queryJieDu();
			}
		});
	});
})
</script>
</body>
</html>
