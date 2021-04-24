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
<style>
 	.addInputUl li{ font-size: 14px;color: #333;margi n-left: 40px;padding-top: 30px;height: 42px; line-height: 42px;}
 	.addInputUl li input{ float:left; height: 40px;text-indent: 1em;width: 500px; border: 1px solid #e1e1e1;color: #999;}
 	.addInputUl li select{ height: 40px;}
 	.addInputUl li textarea {width: 690px; height: 200px;margin-left: 156px;text-indent: 2em;resize: none;}
 </style>
<body class="admin_bg">
    <!-- 头部 -->
	<jsp:include page="/WEB-INF/commons/header.jsp"/>
	
    <div class="content clearfix">
        <!-- 左侧菜单 -->
		<jsp:include page="/WEB-INF/commons/profit-left.jsp">
			<jsp:param name="menu" value="plan_record"/>
		</jsp:include>
		
        <div class="aside">        
            <div class="wrap1080 ppd70"> 
            	<form id="jq_form" method="post" action="/profit/addProfitPlan" onsubmit="return false;">
            	<ul class="addInputUl">
            		<li class="clearfix">
            			<div class="fl w200 ml40">
            				<span class="red">*</span>
            				盈利计划名称
            			</div>
            			<input type="text" id="jq_title" name="title">
            		</li>
            		<li class="clearfix">
            			<div class="fl w200 ml40">
            				<span class="red">*</span>
            				盈利计划logo
            			</div>
            			<input type="text" id="jq_logo" name="imgAdress" style="width:500px;" >
            			<input type="file" id="homeFileToUpload"
										class="qd_txt" style="display:none;" name="filename" /> 
            			 <button id="jq_upload" style="width:80px; height:30px; margin-left:20px; font-size:12;" >上传图片</button>
            		</li>
            		<li class="clearfix">
            			<div class="fl w200 ml40">
            				<span class="red">*</span>
            				盈利计划玩法类型
            			</div>
            			<select name="playType" id="jq_paly_type" class="w200">
                        	<option value="1" selected="">竞足2串1</option>
                        	<option value="2">亚盘</option>
                    	</select>
            		</li>
            		<li class="clearfix">
            			<div class="fl w200 ml40">
            				<span class="red">*</span>
            				盈利计划风险类型
            			</div>
            			<select name="riskType" id="jq_risk_type" class="w200">
                        	<option value="1" selected="">稳健型</option>
                        	<option value="2">博冷型</option>
                    	</select>
            		</li>
            	</ul>
            	<div class="mt30">
	                <p class="jd_title" style="display:inline-block;padding-top:0;vertical-align: middle;">
	                	<span class="red">*</span> 盈利计划价格
	                </p>
	                <ul id="jq_price" name="price" class="profit_price" style="display:inline-block;vertical-align: middle; margin-left:100px;">
	                    <input name="price" type="radio" value="58" /> 58金币 
	            		<input name="price" type="radio" value="68" /> 68金币 
	            		<input name="price" type="radio" checked value="88" /> 88金币
	            		<input name="price" type="radio" value="108" /> 108金币
	            		<input name="price" type="radio" value="128" /> 128金币
	                </ul>
	            </div>
	            <ul class="addInputUl">
            		<li class="clearfix">
            			<div class="fl w200 ml40">
            				<span class="red">*</span>
            				盈利计划特点
            			</div>
            			<input type="text" id="jq_features" name="profitFeatures"  placeholder="示例：平赔模型，3单中1单" maxlength="15"><span style="margin-left: 10px;color:red;">限15字以内</span>
            		</li>
            		<li class="clearfix">
            			<div class="fl w200 ml40">
            				<span class="red">*</span>
            				出单频率
            			</div>
            			<input type="text" id="jq_send_freq" name="sendFreq" placeholder="示例：一个方案结束后发布另一个方案" >
            		</li>
            		<li class="clearfix">
            			<div class="fl w200 ml40">
            				<span class="red">*</span>
            				中奖倍数区间
            			</div>
            			<input type="text" id="jq_win_multiple" name="winMultiple" placeholder="示例：2.5-5.0倍">
            		</li>
            		<li class="clearfix">
            			<div class="fl w200 ml40">
            				<span class="red">*</span>
            				单周发布方案个数
            			</div>
            			<input type="text" id="jq_week_send" name="weekSend" placeholder="示例：5单左右，主要集中在周5、6、7">
            		</li>
            		<li class="clearfix">
            			<div class="fl w200 ml40">
            				<span class="red">*</span>
            				平均中奖频率
            			</div>
            			<input type="text" id="jq_avg_win_freq" name="avgWinFreq" placeholder="示例：平均3单中1单" >
            		</li>
            		<li class="clearfix">
            			<div class="fl w200 ml40">
            				<span class="red">*</span>
            				单期最多方案个数
            			</div>
            				<input type="text" id="jq_predict_max_lost" name="predictMaxLost" style="width: 100px;"placeholder="示例：5"><span style="margin-left: 10px;color:red;">限输入数字</span>
            			 
            		</li>
            		<li class="clearfix">
            			<div class="fl w200 ml40">
            				<span class="red">*</span>
            				适合用户
            			</div>
            			<input type="text" id="jq_user_rank" name="suitUser"  placeholder="示例：有一定耐心，持续跟单一小段时间" >
            		</li>
            	</ul>
	            
	            <div class="game_reason">
	            	<div class="intro_reason intro_reason2">
	                	<span>服务商介绍：</span>
	                	<div style="margin-left:80px;margin-top:-20px">
	                		<textarea id="jq_provide_desc" name="provideDesc" placeholder="请输入服务商介绍！（注：内容不得含非法、虚假内容或联系方式）"></textarea>
						</div>
	                </div>
	                <div class="intro_reason intro_reason2">
	                	<span>盈利计划介绍：</span>
	                	<div style="margin-left:80px;margin-top:-20px">
	                		<textarea id="jq_description" name="description" placeholder="请输入盈利计划的详细介绍！（注：内容不得含非法、虚假内容或联系方式）"></textarea>
						</div>
	                </div>
	                
	                <div class="clearfix">
	                    <a class="confirm_push confirm_push2" id="btnSubmit">提交审核</a>
	                </div>                   
	            </div>
        	</div> 
        	</form>
    	</div>
    </div>
           
    <!-- 尾部 -->
	<jsp:include page="/WEB-INF/commons/footer.jsp"/>
	<div id="dialog-success" class="pub_success" style="display:none">
        <p class="sprite success_close" data-mark="x"></p>
        <p class="sprite success_logo"></p>
        <p class="success_word">盈利计划创建成功,进入审批中...</p>
        <div class="success_btn">
            <a href="javascript:void(0)" class="success_look">查看计划</a>
        </div>
    </div>
    
    <div id="dialog-alert" class="pub_success" style="display:none">
        <p class="sprite success_close" data-mark="x"></p>
        <p class="success_word success_word2">至少选择一场比赛</p>
        <a href="javascript:void(0)" class="return_btn" data-mark="x">确定</a>
    </div>
    <div id="dialog-error" class="pub_success" style="display:none">
        <p class="sprite success_close" data-mark="x"></p>
        <p class="sprite fail_logo"></p>
        <p class="fail_word">抱歉,发布未成功</p>
        <p class="return_word">请返回重新发布</p>
        <a href="javascript:void(0)" class="return_btn" data-mark="x">返回</a>
    </div>
<script src="/resources/js/jiedu.js"></script>
<script type="text/javascript" src="/resources/js/uploadImg/ajaxfileupload.js"></script>
<script type="text/javascript" src="/resources/js/uploadImg/gift_img_upload.js"></script>
<script type="text/javascript">
$("#jq_upload").click(function() {
	$("#homeFileToUpload").click();
});
$("#homeFileToUpload").change(function(){
	ajaxFileUpload("homeFileToUpload");
});
$("#btnSubmit").click(function() {
	if(validatePram()){
		//var data = $("#jq_form").submit();
		$.ajax({
			traditional: true,
			type: "POST",
			url: "/profit/addProfitPlan",
			data: $("#jq_form").serialize(),
			success: function(json){
				if(json.isSuccess){
					return jiedu.dialog.success("/profit/planRecord");
				}else{
					return jiedu.dialog.errorInfo(json.msg);
				}
			}
		});
	}
}	
);

function validatePram() {
	//console.log("jq_paly_type:"+$("#jq_paly_type").val());
	if($("#jq_title").val() == "") {
		jiedu.dialog.alert("请输入盈利计划名称");
		return false;
	}
	if($("#jq_logo").val() == "") {
		jiedu.dialog.alert("请输入盈利计划logo")
		return false;
	}
	if($("#jq_paly_type").val() == "") {
		jiedu.dialog.alert("请选择盈利计划玩法类型");
		return false;
	}
	if($("#jq_risk_type").val() == "") {
		jiedu.dialog.alert("请选择盈利计划风险类型");
		return false;
	}
	if($("input[name='price']:checked").val() == "" || $("input[name='price']:checked").val() == undefined) {
		jiedu.dialog.alert("请选择盈利计划价格");
		return false;
	}
	if($("#jq_features").val() == "") {
		jiedu.dialog.alert("请输入盈利计划特点");
		return false;
	}
	if($("#jq_send_freq").val() == "") {
		jiedu.dialog.alert("请输入出单频率");
		return false;
	}
	if($("#jq_win_multiple").val() == "") {
		jiedu.dialog.alert("请输入中奖倍数区间");
		return false;
	} 
	if($("#jq_send_freq").val() == "") {
		jiedu.dialog.alert("请输入出单频率");
		return false;
	}
	if($("#jq_win_multiple").val() == "") {
		jiedu.dialog.alert("请输入中奖倍数区间");
		return false;
	}
	if($("#jq_avg_win_freq").val() == "") {
		jiedu.dialog.alert("请输入平均中奖频率");
		return false;
	} 
	if($("#jq_predict_max_lost").val() == "") {
		jiedu.dialog.alert("请输入单期最多方案个数");
		return false;
	}
	if(isNaN($("#jq_predict_max_lost").val())) {
		jiedu.dialog.alert("单期最多方案个数格式不正确,请输入数字");
		return false;
	}
	if($("#jq_week_send").val() == "") {
		jiedu.dialog.alert("请输入单周发布方案个数");
		return false;
	}
	if($("#jq_user_rank").val() == "") {
		jiedu.dialog.alert("请输入适合用户");
		return false;
	}
	if($("#jq_provide_desc").val() == "") {
		jiedu.dialog.alert("请输入服务商介绍");
		return false;
	}
	if($("#jq_description").val() == "") {
		jiedu.dialog.alert("请输入盈利计划介绍");
		return false;
	}
	return true;
}
</script>
</body>
</html>
