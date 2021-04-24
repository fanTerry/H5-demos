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
<script type="text/javascript" src="/resources/plugins/doT/doT.min.js"></script>
<script type="text/javascript" src="/resources/plugins/wangeditor/wangEditor.min.js"></script>

<body class="admin_bg">
	<!-- 头部 -->
	<jsp:include page="/WEB-INF/commons/header.jsp"/>

    <div class="content clearfix">
        <!-- 左侧菜单 -->
		<jsp:include page="/WEB-INF/commons/left.jsp">
			<jsp:param name="menu" value="publish_article"/>
		</jsp:include>

        <div class="aside">
            <div class="wrap1080 ppd70">
                <input type="hidden" id="expertId" value="${expertId }">
                <input type="hidden" id="isValidExpert" value="${isValidExpert }">
                <%-- <div class="jd_zhaiyao ml40 pt30">
                	<span class="fl fs14" style="display:block; width:142px;">专栏选择：</span>
               		<c:if test="${!empty columnList }">
                		<c:forEach items="${columnList }" var="column" varStatus="indexStatus">
                			<input type="radio" name="columnId" value="${column.id }" <c:if test="${indexStatus.index == 0}">checked="checked"</c:if> />${column.title }</span>
                		</c:forEach>
               		</c:if>
               	</div> --%>
				<div class='change_title'>发布文章</div>
                <p class="jd_title"><span class='title_tips'>标题：</span><input type="text" id="title" name="title" maxlength="100" placeholder="最多输入50个汉字或100个字符"></p>
                <!-- <div class="jd_zhaiyao ml40 pt30">
                	<span class="fl fs14" style="display:block; width:110px;">摘要(必填)：</span>
                	<textarea id="articleDesc" name="articleDesc" rows="3" cols="20" placeholder="请控制在20-250个字"></textarea>
                </div> -->
               	<div class="game_select clearfix"> 
					<span class='title_tips'>游戏选择：</span>
               		<c:if test="${!empty gameList }">
	               		<c:forEach items="${gameList }" var="game" varStatus="indexStatus">
							<div class='choose_tab <c:if test="${indexStatus.index == 0}">cur</c:if>'>
								<input type='radio' name="gameId" value="${game.index }"/>${game.description }
							</div>
						</c:forEach>
              		</c:if>
               	</div>
               	<div class="add_lh">
                    <!-- <h5 class="fl fz14">请选择至少1场比赛</h5> -->
                    <h5><span id="matchDisplay">展开赛事</span><i class='triangle_icon'></i></h5>
                </div>
                <div id="matchListDiv" class="text_nocenter pub_list_jczq" style="display: none;">
                </div>
               	<div class="jd_zhaiyao mt16">
                	<span class="title_tips">推荐买点：</span>
                	<textarea id="buyPoints" rows="8" cols="20" placeholder="1 一个买点不含赔率：买点 &#10;   2 一个买点包含赔率：买点|赔率(注意是英文分隔符)&#10;   3 两个买点不含赔率：买点1;买点2&#10;   4 两个买点包含赔率：买点1|赔率1;买点2|赔率2&#10;   5 三个及其以上原则同两个的&#10; "></textarea>
                </div>
                <!-- <div class="jd_zhaiyao ml40 pt30">
                	<span class="fl fs14" style="display:block; width:142px;">基本面分析：</span>
                	<textarea id="baseFaceAnalysis" rows="3" cols="20" placeholder="多个基本面用|分隔  eg: 基本面1|基本面2"></textarea>
                </div> -->
                <div class="jd_zhaiyao pt30">
                	<span class="title_tips">内容：</span>
					<div class='comment_con'>
						<div id="viewPointDivMenu" style=" width:900px;border: 1px solid #ccc;">
						</div>
						<div id="viewPointDivText" style="width:900px;height:400px; border: 1px solid #ccc;">
						</div>
					</div>
                </div>
                <c:choose>
                	<c:when test="${ canCharge }">
                		<div class="game_select clearfix">
							<span class="title_tips">是否免费：</span>
							<div class='choose_tab'>
								<input type="radio" name="isFree" value="0" checked="checked"/>不是
							</div>
							<div class='choose_tab'>
								<input type="radio" name="isFree" value="1"/>是
							</div>
		               	</div>
		               	<div class="game_select clearfix" id="priceContainer">
							<span class='title_tips'>单价：</span>
		               		<input class='price_input' type="numer" id="price" maxlength="100" placeholder="必须是两位小数" style="width: 150px">
		               	</div>
                	</c:when>
                	<c:otherwise>
                		<p class="">
							<span>是否免费：</span>
               				<input type="radio" name="isFree" value="1" class="ml40" checked="checked"/>是
               			</p>
                	</c:otherwise>
                </c:choose>
               	<div class="clearfix" id="publish_div">
                	<a class="confirm_push confirm_push2" id="btnNowSubmit">立即发布</a>
                </div>
			</div>
			<!-- 尾部 -->
			<jsp:include page="/WEB-INF/commons/footer.jsp"/>
        </div>
    </div>

	

	<!-- 发布成功 -->
    <div id="dialog-success" class="pub_success" style="display:none">
        <p class="sprite success_close" data-mark="x"></p>
        <p class="sprite success_logo"></p>
        <p class="success_word">恭喜你发布成功</p>
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
        <p class="success_word success_word2">**</p>
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

<jsp:include page="/WEB-INF/jsp/common/kind-editor.jsp"></jsp:include>
<jsp:include page="/WEB-INF/dot/dot-matchlist.jsp"/>

<script src="/resources/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
<script src="/resources/js/jiedu.js"></script>

<script type="text/javascript">
function calcHeight(){
	var conHeight = $('.aside').height() + 76;
	$('.nav').height(conHeight);
}
$(document).ready(function(){
	
	var E = window.wangEditor
    var editor = new E('#viewPointDivMenu', '#viewPointDivText')
 	// 将图片大小限制为 10M
    editor.customConfig.uploadImgMaxSize = 10 * 1024 * 1024
 	// 限制一次最多上传 5 张图片
    editor.customConfig.uploadImgMaxLength = 5
    editor.customConfig.uploadImgServer = '/fileupload/uploadImage'
    // editor.customConfig.showLinkImg = false   // 隐藏“网络图片”tab
    // 自定义菜单配置
    editor.customConfig.menus = [
        'head',  // 标题
        'bold',  // 粗体
        'fontSize',  // 字号
        //'fontName',  // 字体
        //'italic',  // 斜体
        'underline',  // 下划线
        'strikeThrough',  // 删除线
        'foreColor',  // 文字颜色
        'backColor',  // 背景颜色
        //'link',  // 插入链接
        'list',  // 列表
        'justify',  // 对齐方式
        //'quote',  // 引用
        'emoticon',  // 表情
        'image',  // 插入图片
        'table',  // 表格
        //'video',  // 插入视频
        //'code',  // 插入代码
        'undo',  // 撤销
        'redo'  // 重复
    ]
    
    editor.create()
	
	getMatchList();
	
	//calcHeight();
	//setInterval("calcHeight()",100);
	
	
	$(".choose_tab").click(function(){
		$(this).addClass('cur').siblings().removeClass('cur');
	})
	
	$("#matchDisplay").click(function(){
		$(this).siblings('.triangle_icon').toggleClass('active');
		var t = $("#matchDisplay").text();
		if (t == "收起赛事") {
			$("#matchListDiv").hide();
			$("#matchDisplay").text("展开赛事");
		} else {
			$("#matchListDiv").show();
			$("#matchDisplay").text("收起赛事");
		}
	});
	
	$("input[name=isFree]").click(function(){
		var val = $('input[name="isFree"]:checked').val(); 
		if (val == 0) {//不免费
			$("#priceContainer").css("visibility","initial");
		} else if (val == 1) {//免费
			$("#priceContainer").css("visibility","hidden");
		}
	});
	
	$("input[name=gameId]").click(function(){
		getMatchList();
	});
	
	function getMatchList() {
		var gameId = $('input[name="gameId"]:checked').val();
		$.ajax({
			url: "/publish/getMatchList",
			type: "post",
			data: {gameId: gameId},
			success : function(result){
				if(result.isSuccess) {
					var matchList = result.model;
					var tmpltxt = doT.template(document.getElementById("dot-matchlist").innerHTML);//生成模板方法
					document.getElementById("matchListDiv").innerHTML=tmpltxt(matchList);//数据渲染
				} else {
					jiedu.dialog.reload(result.msg);
				}
			}
		});
	}
	
	function getChoosedMatchId() {
		var matchIds = [];
		$("input[name='matchId']").each(function () {
	        if ($(this).is(":checked")) {
	        	matchIds.push($(this).context.value);
	        }
	    });
		return matchIds;
	}
	
	function isEmpty(str){
	    if(typeof str == "undefined" || str == null || str == ""){
	        return true;
	    }else{
	        return false;
	    }
	}
	
	$("#btnNowSubmit").click(function(){
		var expertId = $("#expertId").val();
		if(isEmpty(expertId)){
			jiedu.dialog.alert("请先登录");
			return;
		}
		var isValidExpert = $("#isValidExpert").val();
		if (isEmpty(isValidExpert) || isValidExpert=="false") {
			jiedu.dialog.alert("专家状态无效，不能发布文章");
			return;
		}
		/* var columnId = $('input[name="columnId"]:checked').val(); 
		if (isEmpty(columnId)) {
			jiedu.dialog.alert("您沒有专栏，不能发布文章");
			return;
		} */
		var title = $("#title").val();
		if (isEmpty(title)) {
			jiedu.dialog.alert("标题不能为空");
			return;
		}
		if (title.length >= 100) {
			jiedu.dialog.alert("标题长度大于100了，请精简");
			return;
		}
		/* var articleDesc = $("#articleDesc").val();
		if (isEmpty(articleDesc)) {
			jiedu.dialog.alert("摘要不能为空");
			return;
		}
		if (articleDesc.length >= 500) {
			jiedu.dialog.alert("摘要长度大于500了，请精简");
			return;
		} */
		var matchIds = getChoosedMatchId();
		/* if (matchIds.length <= 0) {
			jiedu.dialog.alert("请至少选择一场比赛");
			return;
		} */
		var matchIdStr = matchIds.join(",");
		var buyPoints = $("#buyPoints").val();
		if (isEmpty(buyPoints)) {
			jiedu.dialog.alert("推荐买点不能为空");
			return;
		}
		/* var baseFaceAnalysis = $("#baseFaceAnalysis").val();
		if (isEmpty(baseFaceAnalysis)) {
			jiedu.dialog.alert("基本面分析不能为空");
			return;
		} */
		var viewPoint = editor.txt.html();
		console.log("viewPoint=" + viewPoint);
		if (isEmpty(viewPoint)) {
			jiedu.dialog.alert("内容不能为空");
			return;
		}
		var isFree = parseInt($('input[name="isFree"]:checked').val()); 
		var price = $("#price").val();
		
		if (isFree == 0) {//不免费
			if (isEmpty(price)) {
				jiedu.dialog.alert("不免费时请输入单价");
				return;
			}
			var reg = /^(([1-9]{1}\d*)|(0{1}))(\.\d{2})$/;
			if (!reg.test(price)) {
				jiedu.dialog.alert("单价格式不对，必须是两位小数");
				return;
			}
			if (parseFloat(price) > 1000) {
				jiedu.dialog.alert("单价不能超过1000");
				return;
			}
		} 
		
		$.ajax({
			url: "/publish/save",
			type: "post",
			data: {
				expertId: expertId,
				//columnId: columnId,
				title: title,
				//articleDesc: articleDesc,
				articleDesc: "默认摘要",
				matchIdList: matchIdStr,
				isFree: isFree,
				price: price,
				viewPoint: viewPoint,
				//baseFaceAnalysisStr: baseFaceAnalysis,
				buyPointsStr: buyPoints,
				gameId: $('input[name="gameId"]:checked').val()
			},
			success : function(result){
				if(result.isSuccess) {
					jiedu.dialog.alert("发布成功");
					setTimeout(function(){
						window.location.href = '/article/list';
					},1000);
				} else {
					jiedu.dialog.alert(result.msg);
				}
			}
		});
		
	});
	
	
	
	
});
</script>
</body>
</html>