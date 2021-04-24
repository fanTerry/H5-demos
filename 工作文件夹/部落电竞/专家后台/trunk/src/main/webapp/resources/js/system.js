/*
* @Author: wenbo.liu
* @Date:   2016-04-20 18:10:40
* @Last Modified by:   wenbo.liu
* @Last Modified time: 2016-04-22 15:33:36
*/

$(document).ready(function(){
  	$(".Jq_gm_b").click(function() {
        $(this).toggleClass("gm_bg2")
    });

    $(".Jq_icon_bottom").click(function(){
    	$(this).parents('.Jq_top_content').siblings('ul').toggle(200);
        $(this).toggleClass('icon_bottom2');
    })
    
    $(document).on("click", ".Jq_i_bottom", function(event){
    	$(this).parents('tbody').toggleClass('hide');
    });
    
    $(document).on("mouseover mouseout", ".competition", function(event){
    	if(event.type == "mouseover"){
    		 $('.saiDivCo').show();
    	}else if(event.type == "mouseout"){
    		 $('.saiDivCo').hide();
    	}
    });
    
    $(document).on("click", "#cbxAll", function(){
    	$(".BMTable tbody:gt(0)").removeClass("hide");
    	
    	$(".saiButt a.on").removeClass("on");
    	$(this).addClass("on");
    	$(".saiLis input:checkbox").prop("checked", true);
    	$(".BMTable tbody:gt(0) tr[id^=tr_match]").css({"display": ''});
    	$("#matchHideCount").text($(".BMTable tbody:gt(0) tr[id^=tr_match]:hidden").length);
    });
    
    $(document).on("click", "#cbxReverse", function(){
    	$(".saiButt a.on").removeClass("on");
    	$(this).addClass("on");
    	
    	$("div.saiLis input:checkbox").each(function(){
    		$(this).click();
    	});
    });
    
    //五大联赛
    $(document).on("click", "#cbx5", function(){
    	var match5 = ['英超', '西甲', '德甲', '意甲', '法甲'];
    	
    	$(".saiButt a.on").removeClass("on");
    	$(this).addClass("on");
    	
    	$(".saiLis input:checkbox:checked").prop("checked", false);
    	$(".saiLis input:checkbox").each(function(){
    		if(match5.indexOf($(this).data("name")) >= 0){
    			$(this).prop("checked", true);
    		}
    	});
    	
    	$(".BMTable tbody:gt(0) tr[id^=tr_match]").each(function(){
    		if(match5.indexOf($(this).data("leaguename")) >= 0){
    			$(this).css({"display": ''});
    		}else{
    			$(this).hide();
    		}
    	});
    	
    	$("#matchHideCount").text($(".BMTable tbody:gt(0) tr[id^=tr_match]:hidden").length);
    });
    
    //赛事选择
    $(document).on("change", "div.saiLis input:checkbox", function(){
    	if($(this).is(":checked")){
    		$(".BMTable tbody:gt(0) tr[data-leaguename=" + $(this).data("name") + "]").css({"display": ''});
    	}else{
    		$(".BMTable tbody:gt(0) tr[data-leaguename=" + $(this).data("name") + "]").hide();
    	}
    	
    	$("#matchHideCount").text($(".BMTable tbody:gt(0) tr[id^=tr_match]:hidden").length);
    });

    $("#Jq_match_all").click(function () { 
        $(".saiLis :checkbox").attr("checked", true);   
    });
    $("#Jq_match_none").click(function () {   
        $(".saiLis :checkbox").attr("checked", false);
    });
    $(".saiButt a").click(function(event) {
        $(this).siblings('a').removeClass('on');  
        $(this).addClass('on');  
    });
})
'use strict';