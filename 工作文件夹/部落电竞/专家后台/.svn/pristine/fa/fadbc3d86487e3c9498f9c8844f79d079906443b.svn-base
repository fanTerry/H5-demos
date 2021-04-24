<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="domainUrl" value="${pageContext.request.contextPath}" />  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>盈利计划评论管理</title>	
	
	<style type="text/css">
		.reply_push {
	    width: 65px;
	    line-height: 27px;
	    border-radius: 3px;
	    background-color: #2eb670;
	    font-size: 16px;
	    cursor: pointer;
	    float: left;
		}
		.delete_push {
		    width: 65px;
		    line-height: 27px;
		    border-radius: 3px;
		    background-color: rgb(245, 87, 87);
		    font-size: 16px;
		    cursor: pointer;
		    float: left;
		}
	</style>	 
</head>

<link href="" rel="shortcut icon" type="image/x-icon" />
<jsp:include page="/WEB-INF/commons/common-file.jsp"/>
<body>
	
	<!-- 头部 -->
	<jsp:include page="/WEB-INF/commons/header.jsp"/>
	
	
	
	 <div class="content clearfix">
        <!-- 左侧菜单 -->
		<jsp:include page="/WEB-INF/commons/profit-left.jsp">
			<jsp:param name="menu" value="reply"/>
		</jsp:include>

	 <div class="aside">
			<div class="wrap1080 ppd70 fz14">
	<span style="font-weight: bold; font-size: 20px">评论信息</span>
		<div class="panel panel-default">
			<div class="panel-body form-inline">
			 	<table border="1">
			 		<tr>
						<th height="38px" width="160px">评论时间:</th><th height="38px" width="160px"><fmt:formatDate value="${snsMsgReply.reply.createTime.time}" pattern="yyyy-MM-dd"/> </td>
						<th height="38px" width="160px">盈利计划:</th><th height="38px" width="160px">${name }</th>
						<th height="38px" width="160px">评论者昵称:</th><th height="38px" width="160px"><a style="color: blue;" href="/snsmember?account=${snsMsgReply.reply.author}">${snsMsgReply.reply.author }</a></th>
						<th height="38px" width="160px">回复数:</th><th height="38px" width="160px">${snsMsgReply.subReplyCount }</th>
					</tr>
			 	</table>
			</div>
		</div>
		
		<br/>
		<span style="font-weight: bold; font-size: 20px">评论的回复</span>
		<div class="panel panel-default">
		<div class="row" style="padding-left: 15px; padding-right: 15px;">
			<table  align="center">
			 		<tr>
						<th width="200px" align="left">${snsMsgReply.reply.author } :</th>
						<td width="800px" align="left">
							<div> <p> ${snsMsgReply.reply.content } </p> </div> 
						</td>
						<td width="320px"></td>
						<td width="500px">
				             	<a class="reply_push" href="#" onclick="showReplyFrame('replyFrame');">回复</a>
				        </td>
					</tr>
					<tr>
						<td width="80px"></td>
						<td width="320px"> </td>
						<td width="80px"></td> 
						<td width="500px">
							<div id="replyFrame" style="display:none;" > 
								<textarea type="text" id="${snsMsgReply.reply.id}_content" placeholder="回复${snsMsgReply.reply.author }（注：内容不得含非法、虚假内容或联系方式）" name="content" style="width: 320px;height: 80px">
								</textarea><span id="errorTips_${snsMsgReply.reply.id}" style="color: red;" ></span>
								<button type="button" class="btn btn-primary" onclick="closeReplyFrame('replyFrame');">取消</button>
								<button type="button" class="btn btn-primary"  onclick="submitReply(${snsMsgReply.reply.id});" >确认</button>
							</div>
						</td>
					</tr>
					<c:forEach items="${secondSnsMsgList }" var="subSnsMsg" varStatus="status">
						<c:if test="${subSnsMsg.reply.operLevel == 31 }">
							<tr>
								<td width="80px"></td>
								<th width="80px" align="left">${subSnsMsg.reply.author }:</th>
								<td width="800px" align="left">
									<div>
											<p>${subSnsMsg.reply.content }</p>
									</div> 
								</td>
								<td width="500px">
						               <a class="reply_push" href="javascript:void(0)" onclick="showReplyFrame('replyFrame_${status.index }');" >回复</a>
						                <a class="delete_push" href="javascript:void(0)" onclick="deleteReply(${subSnsMsg.reply.id},'${subSnsMsg.reply.author}');" >删除</a>
					       		 </td>
							</tr>
							<tr>
								<td width="80px"></td>
								<td width="320px"> </td>
								<td width="80px"></td> 
								<td width="500px">
									<div id="replyFrame_${status.index }"  style="display:none;" > 
										<textarea type="text" id="${subSnsMsg.reply.id}_content" name="content" placeholder="回复${subSnsMsg.reply.author }（注：内容不得含非法、虚假内容或联系方式）" style="width: 320px;height: 80px">
										</textarea><span id="errorTips_${subSnsMsg.reply.id}" style="color: red;" ></span>
										<button type="button" class="btn btn-primary" onclick="closeReplyFrame('replyFrame_${status.index }');">取消</button>
										<button type="button" class="btn btn-primary" onclick="submitReply(${subSnsMsg.reply.id});">确认</button>
									</div>
								</td>
						    </tr>
						</c:if>
						
						<c:if test="${subSnsMsg.subReply != null && subSnsMsg.subReply.size() > 0 }">
								<c:forEach items="${subSnsMsg.subReply }" var="thridReplyMsg" varStatus="statusThrid">
									<tr>
												<td width="80px"></td>
												<td align="left">
													<c:out value="${thridReplyMsg.author }" /> 回复
													<font style="font-weight: bold">
														<c:out value="${thridReplyMsg.features }"/>:
													</font>
												</td>
												<td  width="800px" align="left">
													<p> <c:out value="${thridReplyMsg.content }"/></p>
												</td>
												<td width="680px">
										               <a class="reply_push" href="javascript:void(0)" onclick="showReplyFrame('replyThridFrame_${statusThrid.index }_${subSnsMsg.reply.id}');" >回复</a>
										                <a class="delete_push" href="javascript:void(0)" onclick="deleteReply(${thridReplyMsg.id},'${thridReplyMsg.author }');" >删除</a>
								       		 	</td>
									</tr>
									<tr>
										<td width="80px"></td>
										<td width="320px"> </td>
										<td width="80px"></td>
										<td width="500px">
											<div id="replyThridFrame_${statusThrid.index }_${subSnsMsg.reply.id}"  style="display:none;" > 
													<textarea type="text" id="${thridReplyMsg.id}_content" placeholder="回复${thridReplyMsg.author }（注：内容不得含非法、虚假内容或联系方式）" name="content" style="width: 320px;height: 80px">
													</textarea><span id="errorTips_${thridReplyMsg.id}" style="color: red;" ></span>
													<button type="button" class="btn btn-primary" onclick="closeReplyFrame('replyThridFrame_${statusThrid.index }_${subSnsMsg.reply.id}');">取消</button>
													<button type="button" class="btn btn-primary" onclick="submitReply(${thridReplyMsg.id});">确认</button>
											</div>
										</td>
								    </tr>
								</c:forEach>
						</c:if>
					</c:forEach>
					
					<c:if test="${snsMsgReply.subReplyCount  > 10}">
						<tr>
							<td width="180px"></td>
							<td width="80px"></td>
							<th align="left">
								<a style="color: blue;"> 共<c:out value="${snsMsgReply.subReplyCount }"></c:out>条回复 </a>
							</th>
						</tr>
					</c:if>
			</table>
		</div>
		</div>
		 
	</div>
	</div>
	</div>
	 <!-- 尾部 -->
	<jsp:include page="/WEB-INF/commons/footer.jsp"/>
	
	
	
	<script type="text/template" id="dot_foul" charset="utf-8">
		<div class="form-horizontal" id="foul-dialog">
			<c:forEach items="${foulTypeList}" var="foulType">
				<div class="radio">
					<label><input type="radio" name="foulType" value="<c:out value="${foulType.index}"/>"/><c:out value="${foulType.description}"/></label>
				</div>
			</c:forEach>
		</div>
	</script>
	
	<script type="text/javascript">
		function showReplyFrame(frameName){
			console.log(frameName);
			 $("#"+frameName).attr("style","display: block;");
		}
		function closeReplyFrame(frameName){
			 $("#"+frameName).attr("style","display: none;");
		}
		function submitReply(id){
			//TODO 参数校验 更改为ajax msgId应该可以删除
			var content = $("#"+id+"_content").val();
			if(content.trim() == ""){
				$("#errorTips_"+id).html("回复内容不能为空！");
				return;
			}
			var url = "/replyForProfPlan/replyDetailsMsg?id="+id+"&content="+content;
			$.ajax({
				url:url,
				success: function (responceModel) { 
					if(responceModel.code == 200){
						alert(responceModel.msg);
						window.location.reload();
					} else{
						alert(responceModel.msg);
						window.location.reload();
					}
				}
	    	});
			
		}
		function deleteReply(id,author){
			var isDel = confirm("确定要删除"+ author +"的评论吗？");
			
			if(isDel){
				$.ajax({ url:"/replyForProfPlan/deleteReplyMsg?ids="+id ,async:false});
				window.location.reload();
			}
		}
	</script>
</body>

<footer-scripts> 
<script type="text/javascript" src="/resources/plugins/jquery.ui/1.11.4/jquery-ui.min.js"></script>
<script type="text/javascript" src="/resources/plugins/moment/moment-with-locales-2.9.0.min.js"></script>
<script type="text/javascript" src="/resources/plugins/doT/doT.min.js"></script> 
<script src="/resources/plugins/jquery.form/3.51/jquery.form.js"></script>
<script src="/resources/plugins/ajaxfileupload/ajaxfileupload.js"></script> 
</footer-scripts>
</html>