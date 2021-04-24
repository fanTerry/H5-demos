<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!doctype html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="Cache-Control" content="no-cache" />
    <meta name=”robots” content=”noindex,nofollow” />
    <title>部落(海南)电竞-实名认证</title>
    <link href="" rel="shortcut icon" type="image/x-icon" />

    <link rel="stylesheet" href="/resources/plugins/kindeditor/4.1.11/themes/default/default.css" />
    <link rel="stylesheet" href="/resources/js/datetimepicker/bootstrap-datetimepicker.min.css" />
    <jsp:include page="/WEB-INF/commons/common-file.jsp" />
    <script type="text/javascript" src="/resources/plugins/jquery.cookie/1.4.1/jquery.cookie.js"></script>
    <script src="/resources/js/jiedu.js"></script>
<body class="admin_bg">
<!-- 头部 -->
<jsp:include page="/WEB-INF/commons/header.jsp" />

<div class="content clearfix">
    <!-- 左侧菜单 -->
    <jsp:include page="/WEB-INF/commons/left.jsp">
        <jsp:param name="menu" value="" />
    </jsp:include>
    <style>
        .login_form .inputBox{ margin: 0 auto 10px;}
        .login_form .inputBox .tit{ width: 65px;}
        .addTipTxt{ text-align: left; padding-left: 100px; margin-top: 30px;}
        .addTipTxt dt{ font-size: 16px; color: #666; margin-bottom: 10px;}
        .addTipTxt dd{ font-size: 14px; color: #999; margin-bottom: 10px;}
        .login_form .inputBox input{ width: 185px;}
        .login_form .inputBox .tit{ width: 90px;}
        .addYzm{position: absolute; padding: 0 10px;  color: red;  top: 0;  right: 0;  height: 100%;  line-height: 40px;}
        .addYzm.gray{ color: #999;}
    </style>
    <div class="aside">
        <div class="wrap1080 ppd70">
            <div class="fs30 c333 pl40 pt30">实名认证：</div>
            <form action="/uc/savepwd" method="post" id="do-login">
            <div class="login_form tac pt40">
                <p class="error_txt red mb10" id="error_msg"></p>
                <input type="hidden" id="memId" name="memId" value="${member.id}">
                <div class="inputBox">
                    <span class="tit">姓名：</span>
                    <input id="realName" name="realName" value="${member.realName}" placeholder="提交后不可修改" maxlength="30">
                </div>
                <div class="inputBox">
                    <span class="tit">身份证号码：</span>
                    <input  id="certNo" name="certNo" value="${member.certNo}" placeholder="15或18位，提交后不可修改" maxlength="40">
                </div>
                <div class="inputBox">
                    <span class="tit">手机号：</span>
                    <input  id="phone" name="phone" value="${member.phone}" placeholder="请输入手机号码" maxlength="30">
                </div>
                <div class="inputBox">
                    <span class="tit">短信验证码：</span>
                    <input id="code" name="code" placeholder="请输入验证码" maxlength="30">
                    <button href="javascript:void(0);" id="jq_sendcode" class="addYzm" >获取验证码</button>
                </div>
                <a href="javascript:void(0)" class="submit" id="submit">提交</a>
                <dl class="addTipTxt">
                    <dt>安全提示：</dt>
                    <dd>为保障您的资金安全，请尽快进行先实名认证！</dd>
                    <dd>请务必提供真实有效的身份信息，提交后不可修改</dd>
                </dl>
            </div>
           </form>
        </div>
    </div>
</div>
<div id="dialog-alert" class="pub_success" style="display:none">
    <p class="sprite success_close" data-mark="x"></p>
    <p class="success_word success_word2">xxx,xxxxx!</p>
    <a href="javascript:void(0)" class="return_btn" data-mark="x">确定</a>
</div>
<script>


$(document).ready(function(){
    $("#realName,#certNo,#phone,#code").focus(function () {
        $("#error_msg").text('');
    });
    $("#jq_sendcode").click(function () {
        if($("#phone").val() == "") {
            $("#error_msg").text("请输入手机号");
            return;
        }
        var data = {};
        data.memId = $("#memId").val();
        data.phone = $("#phone").val();
        $("#jq_sendcode").attr("disabled",true);
        $.ajax({
            url: "/uc/sendCode" ,
            type: "POST",
            dataType:"json",
            data: data,
            success:function(jsonMsg){
                if(jsonMsg.code=="200"){
                    msgCount.count(); //发短信验证码读数
                }else{
                    $('#error_msg').html(jsonMsg.msg);
                    $("#jq_sendcode").attr("disabled",false);
                }
            }
        });
    });

    var msgCount = {
        seconds: 180,
        msgtip: "${scd}s后重发",
        count: function(){
            window.timer = window.setInterval(this.b, 1000);
        },
        b: function(){
            if(parseInt(msgCount.seconds) <= 0) {
                window.clearInterval(timer);
                $('#jq_sendcode').removeClass().addClass('addYzm');
                $("#jq_sendcode").html("获取验证码");
                msgCount.seconds = 180;
                $("#jq_sendcode").attr("disabled",false);
                return;
            }
            $('#jq_sendcode').removeClass().addClass('addYzm gray');
            $("#jq_sendcode").html(msgCount.msgtip.replace("${scd}",parseInt(msgCount.seconds)));
            msgCount.seconds = parseInt(msgCount.seconds) - 1;
        }
    };

    $("#submit").click(function(){
		var _memId = $("#memId").val();
		var _realName = $("#realName").val();
		var _certNo = $("#certNo").val();
		var _phone = $("#phone").val();
		var _code = $("#code").val();

		if(_realName == '' || _realName == null){
			$("#error_msg").text("请输入真实姓名");
            $("#realName").focus();
			return;
		}
		if(_certNo == '' || _certNo == null){
			$("#error_msg").text("请输入新密码");
            $("#certNo").focus();
			return;
		}
		if(_phone == '' || _phone == null){
			$("#error_msg").text("请输入手机号码");
            $("#phone").focus();
			return;
		}
        if(_code == '' || _code == null){
            $("#error_msg").text("请输入验证码");
            $("#code").focus();
            return;
        }

        var data = {};
        data.memId = _memId;
        data.realName = _realName;
        data.certNO = _certNo;
        data.phone = _phone;
        data.code = _code;
        $.ajax({
            type: "POST",
            dataType: "json",
            data: data,
            url: "/uc/saveAuthCertNo",
            success: function (result) {
                if(result.code == 200){
                    jiedu.dialog.alert("恭喜您，实名认证成功");
                    window.location.reload();
                } else{
                    $("#error_msg").text(result.msg);
                }
            }
        });
		
	});
	
})
</script>
</body>
</html>
