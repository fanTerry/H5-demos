<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!doctype html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-cache" />
<meta name=”robots” content=”noindex,nofollow” />
<title>部落(海南)电竞-编辑专栏</title>
<link href="" rel="shortcut icon" type="image/x-icon" />

<link rel="stylesheet" href="/resources/plugins/kindeditor/4.1.11/themes/default/default.css" />
<link rel="stylesheet" href="/resources/js/datetimepicker/bootstrap-datetimepicker.min.css" />
<jsp:include page="/WEB-INF/commons/common-file.jsp" />
<script type="text/javascript" src="/resources/plugins/jquery.cookie/1.4.1/jquery.cookie.js"></script>

<script type="text/javascript">
	
</script>
<body class="admin_bg">
	<!-- 头部 -->
	<jsp:include page="/WEB-INF/commons/header.jsp" />

	<div class="content clearfix">
		<!-- 左侧菜单 -->
		<jsp:include page="/WEB-INF/commons/left.jsp">
			<jsp:param name="menu" value="column_edit" />
		</jsp:include>

		<div class="aside">
			<div class="pub_details">
                <div class="income_ta">
                    <span>我的收益:</span>
                    <table> 
                         <tr class="inc_tr2">
                            <td>总收益</td>
                            <td>今日收益</td>
                            <td>待结算收益</td>
                            <td>总订阅用户</td>
                            <td>今日订阅用户</td>
                        </tr>
                        <tr class="inc_tr">
                            <td>${orderResult.totalExpertProfit}</td>
                            <td>${orderResult.todayExpertProfit}</td>
                            <td>${orderResult.waitSettleAmount}</td>
                            <td>${orderResult.totalOrderCount}</td>
                            <td>${orderResult.todayOrderCount}</td>
                        </tr>
                    </table>
                </div>
            </div>
			<div class="wrap1080 ppd70">
				<p class="pu_title">编辑专栏</p>
				<p class="jd_title">
					&nbsp;&nbsp;&nbsp;标题：<input type="text" id="title" name="title" maxlength="80" value="${tjExpertColumn.title}" placeholder="最多50个汉字">
				</p>
				<p class="jd_title">
					副标题：<input type="text" id="subtitle" name="subtitle" maxlength="80" value="${tjExpertColumn.subtitle}" placeholder="最多50个汉字">
				</p>
				<p class="jd_title">
					开始销售时间：<input type="text" id="saleStartTime" name="saleStartTime" style="width: 150px;"
						value="<fmt:formatDate value="${tjExpertColumn.saleStartTime}" pattern="yyyy-MM-dd HH:mm" />">
					<span class="red">从开始销售时间起，请保证您的每月发布文章数量</span>
				</p>
				<p class="jd_title">
					停止更新时间：<input type="text" id="saleEndTime" name="saleEndTime" style="width: 150px;"
						value="<fmt:formatDate value="${tjExpertColumn.saleEndTime}" pattern="yyyy-MM-dd HH:mm" />">
					<span class="red">设置了停止更新时间后，下个自然月就不销售，已经发布的文章继续销售</span>
				</p>
				<p class="jd_title">
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;每月售价：
					<input type="text" value="<fmt:formatNumber value="${tjExpertColumn.monthPrice}" pattern="#" />" style="width: 50px;" readonly>
					个金币&nbsp;&nbsp;&nbsp;&nbsp;<span class="red">如需修改，请联系管理员</span>
				</p>
				<p class="jd_title">每月至少发布：<input type="text" value="${tjExpertColumn.promiseCount}" style="width: 50px;" readonly>篇文章
				&nbsp;&nbsp;&nbsp;&nbsp;<span class="red">本篇数不含试读内容，若自然月内未达承诺，系统将为用户全额退款，如需修改，请联系管理员</span></p>
				<div class="jd_zhaiyao ml40 pt30">
					<span class="fl fs14" style="display: block; width: 142px;">专栏简介：</span>
					<textarea id="describe" name="describe" rows="2" cols="20" placeholder="250个字以内" style="height:60px">${tjExpertColumn.describe}</textarea>
				</div>
				<div class="jd_zhaiyao ml40 pt30">
					<span class="fl fs14" style="display: block; width: 142px;">适宜人群：</span>
					<textarea id="crowds" name="crowds" rows="2" cols="20" placeholder="250个字以内" style="height:60px">${tjExpertColumn.crowds}</textarea>
				</div>
				<div class="jd_zhaiyao ml40 pt30">
					<span class="fl fs14" style="display: block; width: 142px;">订阅须知：</span>
					<textarea id="instruction" name="instruction" rows="2" cols="20" placeholder="250个字以内" style="height:60px">${tjExpertColumn.instruction}</textarea>
				</div>

			</div>
			<div class="game_reason">
				<div class="clearfix" id="jq_publish_div">
					<a class="confirm_push confirm_push2" id="btnNowSubmit">保存</a>
				</div>
				<p class="pl100 mt20 c666">
					<input type="checkbox" id="btnAgree" checked>&nbsp; <a href="/publish/agree" style="color: #444444"
						target="_blank">我已阅读并同意《部落(海南)电竞专家解读和推荐服务协议》</a>
				</p>
			</div>
		</div>
	</div>
	<!-- 尾部 -->
	<jsp:include page="/WEB-INF/commons/footer.jsp" />

	<!-- 保存成功 -->
	<div class="bg_rgba" style="display: none"></div>
	<div id="dialog-success" class="pub_success" style="display: none">
		<p class="sprite success_close" data-mark="x"></p>
		<p class="sprite success_logo"></p>
		<p class="success_word">保存成功</p>
	</div>
	<!-- 保存失败 -->
	<div id="dialog-error" class="pub_success" style="display: none">
		<p class="sprite success_close" data-mark="x"></p>
		<p class="sprite fail_logo"></p>
		<p class="fail_word">抱歉,发布未成功</p>
		<p class="return_word">请返回</p>
		<a href="javascript:void(0)" class="return_btn" data-mark="x">返回</a>
	</div>
	
	<div id="dialog-alert" class="pub_success" style="display: none">
		<p class="sprite success_close" data-mark="x"></p>
		<p class="success_word success_word2"></p>
		<a href="javascript:void(0)" class="return_btn" data-mark="x">确定</a>
	</div>

	<script src="/resources/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
	<script src="/resources/js/jiedu.js"></script>

	<script type="text/javascript">
		function validLength(value,length,msg)
		{
			if(value.length == 0)
			{
				 jiedu.dialog.alert("请输入"+msg);
				 return false;
			}
			if(jiedu.getLength(value) > 2*length)
			{
				jiedu.dialog.alert(msg+"最多"+length+"个汉字");
				 return false;
			}
			return true;
		}
		
		$(document).ready(function() {
			//时间控件
			$("#saleStartTime").datetimepicker({
				format : 'yyyy-mm-dd hh:ii'
			});
			$("#saleEndTime").datetimepicker({
				format : 'yyyy-mm-dd hh:ii'
			});
			//提交数据
			$("#btnNowSubmit").on("click", function() {
				var title = $("#title").val();
				var subtitle = $.trim($("#subtitle").val());
				var saleStartTime = $("#saleStartTime").val();
				var saleEndTime = $("#saleEndTime").val();
				var describe = $.trim($("#describe").val());
				var instruction = $.trim($("#instruction").val());
				var crowds = $.trim($("#crowds").val());
				var data = {};
				
				if(!validLength(title,50,'标题'))
				{
					return;
				}
				if(!validLength(subtitle,50,'副标题'))
				{
					return;
				}
				if(!validLength(describe,250,'专栏简介'))
				{
					return;
				}
				if(!validLength(crowds,250,'适宜人群'))
				{
					return;
				}
				if(!validLength(instruction,250,'订阅须知'))
				{
					return;
				}
				data.title = title;
				data.subtitle = subtitle;
				data.saleStartTime = saleStartTime;
				data.saleEndTime = saleEndTime;
				data.instruction = instruction;
				data.describe = describe;
				data.crowds = crowds;
				//console.log(data);
				$.ajax({
					contentType : 'application/json',
					type: "POST",
		         	dataType: "json",
					url: "/column/updateColumn",
					data: JSON.stringify(data),
					success: function(json){
						if(json.isSuccess){
							var uri = "/column/editColumn";
							return jiedu.dialog.success(uri);
						}else if(json.code == "100"){
							return jiedu.dialog.alert(json.msg);
						}else{
							return jiedu.dialog.error(json.msg);
						}
					}
				});

			})
		});
	</script>
</body>
</html>