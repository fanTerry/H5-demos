$(function(){
	$(document).on("click", "#jczq_2c1_table td.Jq_sp p:even" ,function() {
		var _this = $(this);
		checkZqCommon2(_this,2, 4071);
	});
	
	$(document).on("click", "#jczq_2c1_table td.Jq_sp p:odd" ,function() {
		var _this = $(this);
		checkZqCommon2(_this,2, 4076);
	});
	
	$(document).on("click", "#jclq_2c1_table table.unfold td.Jq_sp", function() {
		var _this = $(this);
		checkLqCommon2(_this, 2);
	});
	
	$(document).on("click", "#jclq_hh_table table.unfold td.Jq_sp", function() {
		var _this = $(this);
		checkLqCommon2(_this, 1);
	});

	//篮球 胜负 让分胜负 大小分
	$(document).on("click", "#jclq_sf_table td.Jq_sp, #jclq_rfsf_table td.Jq_sp, #jclq_dxf_table td.Jq_sp, #jclq_sfc_table td.Jq_sp", function() {
		var _this = $(this);
		checkLqCommon2(_this, 0);
	});
	
	$(document).on("click", "#jczq_hh_table table td.Jq_sp", function() {
		var _this = $(this);
		checkZqCommon2(_this, 1);
	});
	
	$(document).on("click", "#jczq_dg_table td.Jq_sp p:even" ,function() {
		var _this = $(this);
		checkZqCommon2(_this,2, 4071);
	});
	
	$(document).on("click", "#jczq_dg_table td.Jq_sp p:odd" ,function() {
		var _this = $(this);
		checkZqCommon2(_this,2, 4076);
	});
	
	$(document).on("click", "#jczq_yp_table td.Jq_sp:not([data-sp=''])", function(){
		var _this = $(this);
		checkZqCommon2(_this,3);
	});
	
	$(document).on("click", "#jczq_dxq_table td.Jq_sp:not([data-sp=''])", function(){
		var _this = $(this);
		checkZqCommon2(_this,3);
	});

    $(document).on("click", "#bjdc_table td.Jq_sp p" ,function() {
        var _this = $(this);
        checkZqCommon2(_this,2,4051);
    });


	
	// 玩法选项卡切换
    $("ul.tab_play li").click(function(){
    	if($(this).data("playcode") == "2c1") {
    		return;
    	}
    	 $(".tab_play li").removeClass("play_cho");
    	 $(this).addClass("play_cho");
    	
    	$("div.pub_list_jczq").empty();
    	lqCommonData= {}; //数据对象初始化
    	zqCommonData= {};
    	$("div.pub_list_jczq").load("/publish/page/" + GLOBAL_GAME_CODE + "/" + $(this).data("playcode"));	
    });

    // 北京单场 赛事数据加载
    $("#bjdcDiv").click(function () {
        $("div.pub_list_jczq").empty();
        $("div.pub_list_jczq").load("/publish/page/" + GLOBAL_GAME_CODE + "/" + $(this).data("playcode"));
    });

  	
	//胜负彩/任九选择彩期加载赛事
	$(document).on("change", "#issueNo", function(){
		if($(this).find("option:selected").length == 0){
			$(this).find("option:first").prop("selected", "selected");
		}
		
		$("span.stop_time").text($(this).find("option:selected").data("endtime"));
		$("div.pub_list_jczq").load("/publish/page/" + GLOBAL_GAME_CODE + "/" + $(this).val());
	});
	
	//赛事过滤
	$(document).on("mouseover mouseout", ".competition", function(event){
    	if(event.type == "mouseover"){
    		 $('.saiDivCo').show();
    	}else if(event.type == "mouseout"){
    		 $('.saiDivCo').hide();
    	}
    });
	
	//单个联赛过滤
	$(document).on("change", "div.saiLis input:checkbox", function(event){
		var leagueName = $(this).val();
		
		if($(this).is(":checked")){
			$("table.BMTable tbody:gt(0) tr[data-leaguename=" + leagueName + "]").show();
    	}else{
    		$("table.BMTable tbody:gt(0) tr[data-leaguename=" + leagueName + "]").hide();
    	}
		
		$("#matchHideCount").text($("table.BMTable tbody:gt(0) tr:gt(0).Jq_line_match:hidden").length);
	});
	
	//全选所有联赛
	$(document).on("click", "#cbxLeagueAll", function(event){
		$("div.saiButt a.on").removeClass("on");
		$(this).addClass("on");
		$("div.saiLis input:checkbox").prop("checked", true);
		
		$("table.BMTable tbody:gt(0) tr").show();
		$("#matchHideCount").text($("table.BMTable tbody:gt(0) tr:gt(0).Jq_line_match:hidden").length);
	});
	
	//反选所有联赛
	$(document).on("click", "#cbxLeagueReverse", function(event){
		$("div.saiButt a.on").removeClass("on");
		$(this).addClass("on");
		
		$("div.saiLis input:checkbox").each(function(){
    		$(this).click();
    	});
	});
	
	//选中五大联赛
	$(document).on("click", "#cbxLeague5", function(){
    	var league5 = ['英超', '西甲', '德甲', '意甲', '法甲'];
    	
    	$("div.saiButt a.on").removeClass("on");
    	$(this).addClass("on");
    	
    	$("div.saiLis input:checkbox:checked").prop("checked", false);
    	$("div.saiLis input:checkbox").each(function(){
    		if(league5.indexOf($(this).data("name")) >= 0){
    			$(this).prop("checked", true);
    		}
    	});
    	
    	$("table.BMTable tbody:gt(0) tr:nth-child(n+2)").each(function(){
    		if(league5.indexOf($(this).data("leaguename")) >= 0){
    			$(this).show();
    		}else{
    			$(this).hide();
    		}
    	});
    	
    	$("#matchHideCount").text($("table.BMTable tbody:gt(0) tr:gt(0).Jq_line_match:hidden").length);
    });
	
	//胜负彩/任9玩法选中
	$(document).on("click", "#sfc_table td.Jq_sp p", function(){
		var publishType = $("input[name='publishType']:checked").val();
		if(publishType == 2) {
			jiedu.dialog.alert("临场解读无须选择比赛和结果，在预设时间发布推荐结果即可！");
			return;
		}
		$(this).toggleClass("gm_bg2");
	});
	
	//显示/隐藏指定某天的赛事
	$(document).on("click", "i.Jq_i_bottom", function(){
		$(this).parents('tbody').toggleClass('hide');
	});
	
	//收起/选择推荐
	$(document).on("click", "td.game_xztj span.tj_choose", function(){
		if($(this).hasClass("co_pan")){
			$(this).parent("td").parent("tr").next().hide();
			$(this).text("选择推荐");
			$(this).removeClass("co_pan");
		}else{
			$(this).parent("td").parent("tr").next().show();
			$(this).text("收起推荐");
			$(this).addClass("co_pan");
		}
	});
	
	//收起/展开所有赛事
	$("#matchDisplay").click(function(){
		if($(this).text() == '收起赛事'){
			$(this).text("展开赛事");
			$("div.pub_list_jczq").hide();
		}else{
			$(this).text("收起赛事");
			$("div.pub_list_jczq").show();
		}
	});
});
