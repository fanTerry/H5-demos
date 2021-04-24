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
<link rel="stylesheet" href="/resources/js/datetimepicker/bootstrap-datetimepicker.min.css"/>

<jsp:include page="/WEB-INF/commons/common-file.jsp"/>
<script type="text/javascript" src="/resources/plugins/jquery.cookie/1.4.1/jquery.cookie.js"></script>
<script type="text/javascript" src="/resources/js/uploadImg/ajaxfileupload.js"></script>
<script type="text/javascript">
var GLOBAL_GAME_CODE = '${gameCode}';
var GLOBAL_COMMISSION = ${empty tjConfigResult.prorataCongfigDo.expert_profit_multiply_ratio ? 0 : tjConfigResult.prorataCongfigDo.expert_profit_multiply_ratio};
var GLOBAL_ZQ_SINGLE_SP = '${zqSingleSp}';
var GLOBAL_ZQ_DOUBLE_SP = '${zqDoubleSp}';
var GLOBAL_LQ_SINGLE_SP = '${lqSingleSp}';
var G_ZQ_2C1_PROFIT_RATE = '${zqCgProfitRatio}';
var G_LQ_2C1_PROFIT_RATE = '${lqCgProfitRatio}';
</script>

<body class="admin_bg">
	<!-- 头部 -->
	<jsp:include page="/WEB-INF/commons/header.jsp"/>

    <div class="content clearfix">
        <jsp:include page="/WEB-INF/commons/left.jsp">
			<jsp:param name="menu" value="publish_jczq"/>
		</jsp:include>
        <!-- 左侧菜单 -->
        <div class="aside">        
            <div class="wrap1080 ppd70">
                <p class="pu_title">
                	发布竞彩足球二串一
                </p>
                <input type="hidden" id="jq_play_type" value="2c1">
                <input type="hidden" id="jq_pass_type" value="2">
                <input type="hidden" id="jq_race_type" value="1">
                <input type="hidden" id="jq_improv_gameId" value="4071">
                <input type="hidden" id=jq_member_id value="${memId }">
                
                <p class="jd_title">标题：<input type="text" id="title" name="title" maxlength="80" placeholder="最多输入40个汉字或80个字符"></p>
                <div class="jd_zhaiyao ml40 pt30"><span class="fl fs14" style="display:block; width:142px;">摘要（必填）：</span><textarea id="jq_summary" name="summary" rows="3" cols="20" placeholder="未付费时，用户只能查看摘要，请控制在20-200个字"></textarea></div>
                <!-- 文件上传开始 -->
                <jsp:include page="/WEB-INF/jsp/common/voice-upload.jsp"/>
                <!-- 文件上传结束 -->
                <div class="jd_title clearfix add_lh">
                    <h5 class="fl">选择赛事</h5>
                    <ul class="tab_play">
                        <li class="play_cho"><a href="/publish/jczq/2c1" >二串一</a></li>
                        <li><a href="/publish/jczq?playCode=dg">单关</a></li>
                        <%--<li><a href="/publish/jczq?playCode=yp">亚盘</a></li>
                        <li><a href="/publish/jczq?playCode=dxq">大小球</a></li>
                        <li><a href="/publish/jczq?playCode=hh">混合推荐</a></li>--%>
                    </ul>
                    <span id="matchDisplay" class="sqsh sqsh2">收起赛事</span>
                </div>
               	<div class="ml40 pt30">
               		<input type="radio" name="publishType" value="1" checked="checked" />普通解读<span style="color:#999;">（选比赛+结果）</span>
               		<input type="radio" name="publishType" value="2" />临场解读<span style="color:#999;">（当前不选比赛和结果，临场发布推荐结果）</span>
               	</div>
                <!-- 赛事选择 -->
                <jsp:include page="/WEB-INF/jsp/common/zq-2c1-table.jsp"></jsp:include> 
            	
            	<div id="jq_improv_time" style="display:none;">
            		<p class="ml40 pt20" style="font-size: 14px;">预计发布结果时间: 
            			<input type="text" id="jq_publish_time" name="publishTime" placeholder="请于预设时间前发布比赛和结果，否则自动撤单" style="margin-left: 60px;height: 20px;text-indent: 1em; width: 400px;">
            		</p>
            	</div>
            	<div class="game_reason">
                    <div>提示：编辑内容含有图片时，请在编辑区手工上传。</div>
                    <div id="jq_content_area" class="intro_reason intro_reason2" >
	                	<span>解读内容：</span>
	                	<div style="margin-left:80px;margin-top:-20px">
	                		<textarea id="content" name="content" placeholder="请输入专家解读内容，分析越透彻，内容越详细，查看的用户越多，您的收益也越多！（注：内容不得含非法、虚假内容或联系方式）"></textarea>
						</div>
	                </div>
	                <div id="jq_improv_desc_area" class="intro_reason intro_reason2" style="display:none;" > <!-- style="display:none;" -->
	                	<span>解读宣传：</span>
	                	<div style="margin-left:80px;margin-top:-20px">
	                		<textarea id="jq_improv_desc" name="improvDesc" placeholder="请输入专家解读内容，分析越透彻，内容越详细，查看的用户越多，您的收益也越多！（注：内容不得含非法、虚假内容或联系方式）"></textarea>
						</div>
	                </div>
             		 <!-- 文件上传开始 -->
                	<jsp:include page="/WEB-INF/jsp/common/improv-voice-upload.jsp"/>
                	<!-- 文件上传结束 -->
                    <div>
                        <span  style="float: left; font-size: 14px; padding-top: 10px;">查看价格:</span>
                        <div class="huoyan">
	                        <ul id="priceList" class="clearfix" style="padding-left: 80px;">
	                            <c:forEach items="${priceConfig2c1.amountCongfigDo.amountList}" var="amount">
	                            	<c:if test="${ amount <= 0}">
	                            		<li data-price="${amount}" >免费</li>
	                            	</c:if>
	                            	<c:if test="${ amount > 0}">
	                            		<li data-price="${amount}">${amount}金币</li>
	                            	</c:if>
	                            </c:forEach>
	                        </ul>
                        </div>
                        
                        <p class="shouyi shouyi2" style="margin-left:80px;">该解读被查看<em>1</em>次,您可获得<span id="commission">0</span>元收益</p>
	                    <%-- <c:if test="${not empty tjConfigResult.prorataCongfigDo.expert_profit_multiply_ratio && tjConfigResult.prorataCongfigDo.expert_profit_multiply_ratio > 0}">
	                    	
	                    </c:if>	 --%>
	                    <div id="jq_free_type_refund" class="pt10" style="margin-left: 80px;display:none;" >
	                    	<input type="checkbox" name="refundNotWin" value="1	"/><span style="color:red">不中退款 </span><span>(解读未命中，全额退还金币，每周限<span id="jq_limit_count">3</span>次)</span></span>
	                    </div>
	                    
	                    <div id="jq_free_type_list" style="margin-left: 80px; display:none;">
	                    	<div class="pt10">该解读以下用户免费查看</div>
	                    	<div class="pt10">
		                    	<input type="radio" name="freeType" checked="checked" value="2" >所有人
		                    	<input type="radio" name="freeType" value="3" class="ml40">我的粉丝
		                    	<input type="radio" name="freeType" value="4" class="ml40">买过我解读的人
		                    </div>
	                    </div>
                    </div>
					
                    <div class="clearfix" id="jq_publish_div">
                        <a class="confirm_push confirm_push2" id="btnSubmit">提交推荐</a>
                        <a class="confirm_push confirm_push3" id="btnPreview">发布预览</a>
                    </div>
                    <div class="clearfix" style="display:none;" id="jq_improv_publish">
                        <a class="confirm_push confirm_push2" id="btnImprovSubmit">提交临场推荐</a>
                    </div>
                    <p class="pl100 mt20 c666">
                    	<input type="checkbox" id="btnAgree" checked>&nbsp;
                    	<a href="/publish/agree" style="color:#444444" target="_blank">我已阅读并同意《部落(海南)电竞专家解读和推荐服务协议》</a>
                    </p>                  
                </div>  
        	</div>
    	</div>
    </div>
	<!-- 尾部 -->
	<jsp:include page="/WEB-INF/commons/footer.jsp"/>
    
	<form id="previewForm" action="preview" method="post" target="_blank">
		<input type="hidden" name="jsonString" value="">
	</form>
    
	<!-- 发布推荐成功 -->
    <div class="bg_rgba" style="display:none"></div>
    <div id="dialog-success" class="pub_success" style="display:none">
        <p class="sprite success_close" data-mark="x"></p>
        <p class="sprite success_logo"></p>
        <p class="success_word">恭喜你发布2串1方案成功</p>
        <div class="success_btn">
            <a href="javascript:void(0)" class="success_look">查看2串1方案</a>
            <a href="/publish/jczq/2c1" class="go_pub">继续发布</a>        
        </div>
    </div>
	<!-- 发布失败 -->
    <div id="dialog-error" class="pub_success" style="display:none">
        <p class="sprite success_close" data-mark="x"></p>
        <p class="sprite fail_logo"></p>
        <p class="fail_word">抱歉,发布未成功</p>
        <p class="return_word">请返回重新发布</p>
        <a href="javascript:void(0)" class="return_btn" data-mark="x">返回</a>
    </div>
	<!-- 无logo -->
    <div id="dialog-alert" class="pub_success" style="display:none">
        <p class="sprite success_close" data-mark="x"></p>
        <p class="success_word success_word2">至少选择一场比赛</p>
        <a href="javascript:void(0)" class="return_btn" data-mark="x">确定</a>
    </div>
    
    <div id="dialog-reload" class="pub_success" style="display:none">
        <p class="sprite success_close" data-mark="x"></p>
        <p class="success_word success_word2">xx</p>
        <a href="javascript:void(0)" class="return_btn" id="jq_reload">确定</a>
    </div>
    
    <div id="dialog-tip" class="pub_success"  style="display:none"> <!-- style="width:300px;" -->
        <!-- <p class="sprite success_close" data-mark="x"></p> -->
        <p class="fs20">您确认发布该解读吗？</p>
        <p class="mt10 fz16 red" color="red">解读发布后不可修改，请确保内容正确无误</p>
         <div class="success_btn">
            <a href="javascript:void(0)" class="return_btn mr20" data-mark="x" >取消</a>
            <a href="javascript:void(0)" class="return_btn" id="btnSubmitNew" >确认</a>        
        </div>
    </div>

<script src="/resources/plugins/kindeditor/4.1.11/kindeditor-all.js" charset="utf-8"></script>
<script src="/resources/js/jiedu.js"></script>

<script src="/resources/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
<script src="/resources/js/zq_2c1_publish.js"></script>
<script src="/resources/js/publish/clear_selected_item.js"></script>
<script src="/resources/js/improv_jiedu.js"></script>

<script>
$(document).ready(function(){
	KindEditor.ready(function(K) {
		editor = K.create('textarea[name="content"]', {
			width:'100%',
			height:400,
			items: [
			        'source', '|', 'undo', 'redo', '|', 'preview', 'cut', 'copy', 'paste',
			        'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
			        'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 
			        'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
			        'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
			        'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image',
			        'table', 'hr', 'pagebreak',
			        'anchor', 'link', 'unlink'
			],
			resizeType : 1,
			allowImageRemote: false,	//禁止网络图片
			allowPreviewEmoticons : false,
			allowImageUpload : true,
			formatUploadUrl : false,
			uploadJson : "/image/upload",
			afterBlur : function(){
	            this.sync();
			}
		});
	});
	
  	$("input[name='enableSummaryVoice']").click(function(e){
		var option = $(this).val();
		if(0 == option){
			$("#summaryDiv").hide(); 
		} else {
			$("#summaryDiv").show();
		}
	});
  	
	//预览
	$("#btnPreview").click(function(){
		var data = publish.getPostData(GLOBAL_GAME_CODE,  $("ul.tab_play li.play_cho").data("playcode"));
		if($.type(data) === "object"){
			console.log(JSON.stringify(data));
			$("#previewForm input:hidden[name=jsonString]").val(JSON.stringify(data))
			$("#previewForm").submit();
		}
	});
	
	$("#jq_reload").click(function() {
		jiedu.dialog.closeWebdialog();
		window.location.reload();
	});
	
	$("#btnSubmit").click(function() {
		if(!$("#btnAgree").is(":checked")){
			return jiedu.dialog.alert("请阅读并同意部落(海南)电竞专家解读和推荐服务协议");
		}
		return webAlert({title:false, content:$("#dialog-tip")[0]});
	});
  	//发布解读
	$("#btnSubmitNew").click(function(){
		var _this = this;
		if($(_this).data('isLock')){return;};
		$(_this).data('isLock', 1);
		setTimeout(function(){
			$(_this).data('isLock', 0);
		}, 15000); 
		
		var data = publish.getPostData();
		//console.log(JSON.stringify(data));
		if($.type(data) === "object"){
			console.log(JSON.stringify(data));
			$.ajax({
				contentType : 'application/json',
				type: "POST",
	         	dataType: "json",
				url: "/publish/save",
				data: JSON.stringify(data),
				success: function(json){
					$(_this).data('isLock', 0);
					
					if(json.isSuccess){
						var uri = "";
						if(!json.model) {
							uri = "/interpretation";
						} else {
							uri = "/interpretation/show/" + json.model;
						}
						return jiedu.dialog.success(uri);
					}else if(json.code == "101"){
						return alert(json.msg);
					}else if(json.code == "-1"){
						return jiedu.dialog.error();
					}else{
						return jiedu.dialog.alert(json.msg);
					}
				}
			});
		} 
    });
});

</script>
</body>
</html>