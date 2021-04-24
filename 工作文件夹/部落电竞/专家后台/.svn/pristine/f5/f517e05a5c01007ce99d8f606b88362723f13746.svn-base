/**
 * 
 */
$(document).ready(function() {
	var member_id = $("#jq_member_id").val();
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
		queryJieDu();
	});
	
	$("#jq_show").mouseover(function(){
		$("#jq_show").addClass("ad_spancolor");
		$("#jq_show ul").show();
	}).mouseout(function(){
		$("#jq_show").removeClass("ad_spancolor");
		$("#jq_show ul").css("display", "none");
	});
	
	$("#jq_reply").mouseover(function(){
		$("#jq_reply").addClass("ad_spancolor");
		$("#jq_reply ul").show();
	}).mouseout(function(){
		$("#jq_reply").removeClass("ad_spancolor");
		$("#jq_reply ul").css("display", "none");
	});
	
	$("#jq_show ul li").click(function(){
		//clearQueryData();
		$("#jq_show").removeClass("ad_spancolor");
		$("#jq_show ul").css("display", "none");
		
		$(this).siblings(".act").removeClass();
		$(this).addClass("act");
		$("#jq_show span").text($(this).text());
		$("#jq_show").val($("#jq_show ul li.act").data("value"));
		queryJieDu();
	});
	
	$("#jq_reply ul li").click(function(){
		//clearQueryData();
		$("#jq_reply").removeClass("ad_spancolor");
		$("#jq_reply ul").css("display", "none");
		
		$(this).siblings(".act").removeClass();
		$(this).addClass("act");
		$("#jq_reply span").text($(this).text());
		$("#jq_reply").val($("#jq_reply ul li.act").data("value"));
		queryJieDu();
	});
	
	$("#jq_query_list").click(function() {
		queryJieDu();
	});
	
	$("#jq_query_clear").click(function() {
		clearQueryData();
		queryJieDu();
	})
	var commentPage, interpretationId;
	interpretationId = $("#jq_interpretation_id").val().trim();
	if(interpretationId != null) {
		commentPage = $.fn.JieDuPage({
			url: "/comment/list"
		});
		commentPage.render({
			data: {interpretationId: interpretationId}
		});
	} else {
		commentPage = $.fn.JieDuPage({
			url: "/comment/list"
		});
		commentPage.render({});
	}
	
	var queryJieDu = function(){
		console.log($("#beginTime").val());
		console.log($("#beginTime").val());
		console.log($("#jq_show").val());
		console.log($("#jq_reply").val());
		console.log($("#jq_search").val());
		commentPage.render({
			data : {
				pageSize:1,
				pageNo:1,
				startTime: $("#beginTime").val(),
				endTime: $("#endTime").val(),
				isShow: $("#jq_show").val(),
				isRelied: $("#jq_reply").val(),
				searchStr: $("#jq_search").val()
			}
		});
	};
	
	$("body").on("change", ".test", function() {
		var _this = $(this);
		var display = _this.val();
		var note_id = _this.attr("data-select");
		
		
		$.post("/comment/changeDisplay",
				{
					memberId: member_id,
					noteId: note_id,
					displayType: display
				},
				function(result){
					if(result.isSuccess) {
						alert("修改成功！");
					} else {
						alert("修改失败！");
						return;
					}
				}
		);
	});
	
	$("body").on("mouseover", ".jq_reply_show", function(event) {
		var _this = $(this);
		var xx = event.pageX;  
	    var yy = event.pageY -25;
		webAlert({left:xx,top: yy,lock: false , width: "200px" , title:false, content:_this.attr("data-content")});
	});
	$("body").on("mouseout", ".jq_reply_show", function(event) {
		closeWebdialog();
	});
	
	$("body").on("mouseover", ".jq_reply_content", function(event) {
		var _this = $(this);
		var xx = event.pageX;  
	    var yy = event.pageY-25;
		webAlert({left:xx,top: yy,lock: false, width: "200px", title:false, content:_this.attr("data-content")});
	});
	$("body").on("mouseout", ".jq_reply_content", function(event) {
		closeWebdialog();
	});
	
	
	$("body").on("click", ".test2", function() {
		var _this = $(this);
		var note_id = _this.attr("data-edit");
		$("#jq_edit_note_id").val(note_id);
		webAlert({title:"评论回复", content:$("#dialog-alert")[0]});
	});
	
	$("#jq_edit_save").on("click", function() {
		var note_id = $("#jq_edit_note_id").val();
		var content = $("#jq_eidt_content").val();
		if(content.length <=0) {
			alert("回复内容不能为空。");
			return;
		} else if(content.length >= 200){
			alert("回复内容超过200字限制，请精简回复内容。");
			return;
		}
		$.post("/comment/saveReply",
				{
					replyMemberId: member_id,
					noteId: note_id,
					content: content
				},
				function(result){
					if(result.isSuccess) {
						closeWebdialog();
						window.location.href = "/comment/index";
					} else {
						alert(result.msg );
						closeWebdialog();
						return;
					}
				}
		);
	});
	
	function closeWebdialog () { 	//关闭页面弹层.
		if(webAlert&&webAlert.list){
		  for(var id in webAlert.list){
			  webAlert.list[id].close();
		  }
		 } 
	};
	
	function clearQueryData() {
		$("#beginTime").val('');
	 	$("#endTime").val('');
		$("#jq_show").val("1");
		$("#jq_reply").val("1");
		$("#jq_search").val("");
	}
});