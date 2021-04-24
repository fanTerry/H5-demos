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
<link rel="stylesheet" href="/resources/plugins/kindeditor/4.1.11/themes/default/default.css"/>
<jsp:include page="/WEB-INF/commons/common-file.jsp"/>
<!-- <script src="/resources/plugins/jquery.cookie/1.4.1/jquery.cookie.js"></script> -->
<body class="admin_bg">
	<!-- 头部 -->
	<jsp:include page="/WEB-INF/commons/header.jsp"/>

    <div class="content clearfix">
        <!-- 左侧菜单 -->
		<jsp:include page="/WEB-INF/commons/left.jsp">
			<jsp:param name="menu" value="comment"/>
		</jsp:include>
        <div class="aside">        
           <div class="wrap1080 ppd70 fz14">
                <!-- 解读 -->
                <div class="clearfix fb_title ml30">
                    <div class="fl pr pdr100">
                        <span class="fl">发布时间</span>
                        <input type="text" id="beginTime" data-for="boxBeginTime" class="input01" placeholder="起始时间" readonly>
                            <i>—</i>
                        <input type="text" id="endTime" data-for="boxEndTime" class="input01 input02" placeholder="结束时间" readonly>
                        <div class="fl al_btnbox" style="display: none;">
                            <input type="text" id="boxBeginTime" placeholder="起始时间" class="input01 input_ad">
                            <i>—</i>
                            <input type="text" id="boxEndTime" placeholder="结束时间" class="input01 input02">
                            <a href="javascript:;" id="btnTime" class="red_confire">确定</a>
                        </div>
                        <span class="f_span1" id="jq_show">
							展示<span>全部</span>
                        	<!-- 展开加class   -->
                            <ul class="pull_down jd_pdown" style="display: none;">
                                    <li data-value="1">全部</li>
                                    <li data-value="2">是</li>
                                    <li class="act" data-value="3">否</li>
                            </ul>
                        </span>
                        <span class="f_span1" id="jq_reply">
                        	回复<span>全部</span>
                            <ul class="pull_down jd_pdown" style="display: none;">
                                    <li data-value="1">全部</li>
                                    <li data-value="2">是</li>
                                    <li class="act" data-value="3">否</li>
                            </ul>
                        </span>
                        <span class="f_span2">原解读/文章标题</span>
                        <input type="text" id="jq_search" placeholder="模糊匹配">
                    </div>
                    <div>                    
	                    <a href="" class="fr add_bgeb" id="jq_query_clear">重置</a>
	                    <a id="jq_query_list" href="javascript:void(0)" class="fr add_bgeb">查询</a>
                    </div>
                </div>
                
                
           <div class="pl30 mt30 fb_table">
                <table id="listContainer" width="100%">
								
                </table>
	           <div class="paging clearfix" id="pageContainer">             
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
	   	<tr bgcolor="#f5f5f5" style="color:#545454;">
	       	 <td width="8%">id</td>
             <td style="text-align: center;width:20%;text">留言内容</td>
             <td>作者</td>
             <td>时间</td>
             <td>原解读/文章</td>
             <td>是否展示</td>
             <td width="15%">回复</td>
	   	</tr>
	</thead>
	<tbody>
		{{? it.dataList.length < 1}}
			<tr><td align="center" colspan="7">暂无数据</td></tr>
		{{?}}
		{{~it.dataList :item:index}}
	   		<tr>
				<td style="color:#999999;">{{=item.id}}</td>
	       		<td  style="color:#444444;text-align: left;line-height:20px;" >
					<span class="jq_reply_content" data-content="{{=item.content}}"> {{=(item.content.length<=20 ? item.content : item.content.substr(0,20) +"...")}} </span>
				</td>
				<td>{{=item.filterAccount}}</td>
	       		<td>{{=(item.createTime.substr(5,item.createTime.length-1))}}</td>
	       		<td style="color: #0082ff;cursor:pointer;" >
					<a href="/interpretation/show/{{=item.interpretationId}}" style="color: #0082ff;">
						{{=(item.interpretationTitle.length<=8 ? item.interpretationTitle : item.interpretationTitle.substr(0,8) +"...")}}
					</a>
				</td>
	       		<td style="color:#ff0000;">
					<select class="test" data-select="{{=item.id}}">
	 						<option {{?item.display ==0 }}selected="selected" {{?}} value="0">不显示</option>
	  						<option {{?item.display ==1 }}selected="selected" {{?}} value="1">所有人可见</option>
							<option {{?item.display ==2 }}selected="selected" {{?}} value="2">回复人可见</option>
					</select>
				</td>
				{{?item.reply == 1}}
					<td class="jq_reply" style="color: #333;cursor:pointer;line-height:20px;" >
						<span class="jq_reply_show" data-content="{{=item.replyMessage}}"> {{=(item.replyMessage.length<=20 ? item.replyMessage : item.replyMessage.substr(0,20) +"...")}}</span>						
					</td> 
				{{??}}
					<td style="color: #0082ff;cursor:pointer;" >
						<a href="javascript:void(0)" class="test2" data-edit="{{=item.id}}" style="color: #0082ff;">编辑回复</a>
					</td>
				{{?}}
	   		</tr>
		{{~}}
	</tbody>
</script>

<!-- {{=item.display}} -->
<input type="hidden" id="jq_member_id" value="${loginMemberId}"/>
<input type="hidden" id="jq_interpretation_id" value="${interpretationId}"/>
<div id="dialog-alert"  style="display:none">
    <p class="success_word success_word2">请输入回复内容（200字以内）</p>
    <textarea id="jq_eidt_content" rows="15" cols="100"></textarea>
    <input id="jq_edit_note_id" type="hidden"/>
    <div>
    	<a id="jq_edit_save" class="return_btn" href="javascript:void(0)" style="text-align:center;background-color: #2880e0;">提交</a>
    	<!--  class="return_btn" data-mark="x" -->
    </div>
</div>
<!-- <script src="/resources/plugins/kindeditor/4.1.11/kindeditor-all.js" charset="utf-8"></script>-->
<script type="text/javascript" src="/resources/plugins/jquery.ui/1.11.4/jquery-ui.min.js"></script>
<script type="text/javascript" src="/resources/plugins/doT/doT.min.js"></script>
<script type="text/javascript" src="/resources/js/jiedu.page.js"></script>
<script type="text/javascript" src="/resources/js/jiedu.js"></script>
<script type="text/javascript" src="/resources/js/comment/comment.js"></script>
</body>
</html>