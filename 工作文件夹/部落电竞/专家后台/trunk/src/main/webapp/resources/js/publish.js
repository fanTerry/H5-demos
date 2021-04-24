$(function(){
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
	
	// 玩法选项卡切换
    $("ul.tab_play li").click(function(){
    	 $(".tab_play li").removeClass("play_cho");
    	 $(this).addClass("play_cho");
    	
    	$("div.pub_list_jczq").empty();
    	lqCommonData= {}; //数据对象初始化
    	zqCommonData= {};
    	$("div.pub_list_jczq").load("/publish/page/" + GLOBAL_GAME_CODE + "/" + $(this).data("playcode"));	
    });
  	
  	
    //选择查看价格效果
	$("#priceList li").click(function(){
		if($(this).hasClass("press")){return;}
		
		$(this).siblings('li').removeClass('press');  
		$(this).toggleClass("press");
		
		var value = parseInt($(this).data("price")) * GLOBAL_COMMISSION;
		$("#commission").text(value ? value.toFixed(2) : 0);
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

var publish = {};

publish.gameType = {
	JJ_ZC			: 407,		//竞彩足球
	JJ_ZC_SPF		: 4071,		//竞彩足球-胜平负
	JJ_ZC_RQ_SPF 	: 4076,		//竞彩足球-让球胜平负
	JJ_ZC_RQ_SPF_HH : 4077, 	//竞彩足球-单关（胜平负/让球胜平负）
	JJ_ZC_YP		: 4078,		//竞彩足球-亚盘
	JJ_ZC_WW_DXQ	: 4079,		//竞彩足球-大小球
	JJ_14SFC		: 401,		//胜负彩
	JJ_R9C			: 402,		//任选9场
	JJ_LC			: 406, 		//竞彩蓝球
	JJ_LC_SF		: 4061,		//竞彩蓝球-胜负
	JJ_LC_RFSF		: 4062,		//竞彩蓝球-让分胜负
	JJ_LC_SFC		: 4063,		//竞彩蓝球-胜分差
	JJ_LC_DXF		: 4064,		//竞彩蓝球-大小分
	JJ_LC_HH		: 4065,		//竞彩蓝球-串关
	BJDC_SPF         :4051  //北京单场-胜平负
}

/**
 * 获得发布解读的所有参数
 */
publish.getPostData = function(gameCode, playCode){
	var title = $.trim($("#title").val());
	var content = $("#content").val();
	var price = parseInt($("#priceList li[class=press]").data("price"));
	var summary = $("#jq_summary").val();
	
	var gameType;
	var issueNo;
	var itemList = [];
	var passType = 1;
	var betCount = 0;
	/*var summary_lenght = $("#jq_sum_length").val();
	if(summary_lenght == undefined) {
		summary_lenght = 40;
	}*/
	
	if("jczq" == gameCode){
		if("dg" == playCode){
			gameType = publish.gameType.JJ_ZC_RQ_SPF_HH;
			itemList = publish.extract.jczq_dg();
		}else if("yp" == playCode){
			gameType = publish.gameType.JJ_ZC_YP;
			itemList = publish.extract.jczq_yp();
		}else if("dxq" == playCode){
			gameType = publish.gameType.JJ_ZC_WW_DXQ;
			itemList = publish.extract.jczq_dxq();
		}else if("hh" == playCode){
			gameType = publish.gameType.JJ_ZC;
			itemList = publish.extract.jczq_hh();
		}
	}else if("jclq" == gameCode){
		if("sf" == playCode){
			gameType = publish.gameType.JJ_LC_SF;
			itemList = publish.extract.jclq_sf();
		}else if("rfsf" == playCode){
			gameType = publish.gameType.JJ_LC_RFSF;
			itemList = publish.extract.jclq_rfsf();
		}else if("sfc" == playCode){
			gameType = publish.gameType.JJ_LC_SFC;
			itemList = publish.extract.jclq_sfc();
		}else if("dxf" == playCode){
			gameType = publish.gameType.JJ_LC_DXF;
			itemList = publish.extract.jclq_dxf();
		}else if("hh" == playCode){
			gameType = publish.gameType.JJ_LC;
			itemList = publish.extract.jclq_hh();
		} else if("2c1" == playCode) {
			itemList = publish.extract.jclq_2c1_hh();
			
			if((itemList[0].gameType ==itemList[0].gameType && itemList[0].gameType == publish.gameType.JJ_LC_HH ) 
					|| itemList[0].gameType != itemList[1].gameType ) {
				gameType = publish.gameType.JJ_LC_HH;
				var itemCount1 = itemList[0].content.split("~").length;
				var itemCount2 = itemList[1].content.split("~").length;
				if(itemList[0].content.indexOf(",") > 0) {
					itemCount1++;
				}
				if(itemList[1].content.indexOf(",") > 0) {
					itemCount2++;
				}
				betCount = itemCount1 * itemCount2;
			} else if(itemList[0].gameType == publish.gameType.JJ_LC_SF) {
				gameType = publish.gameType.JJ_LC_SF;
				betCount  = 1;
			}else if(itemList[0].gameType == publish.gameType.JJ_LC_SFC) {
				gameType = publish.gameType.JJ_LC_SFC;
				var itemCount1 =1, itemCount2 = 1;
				if(itemList[0].content.indexOf(",") > 0) {
					itemCount1++;
				}
				if(itemList[1].content.indexOf(",") > 0) {
					itemCount2++;
				}
				betCount = itemCount1 * itemCount2;
			}else if(itemList[0].gameType == publish.gameType.JJ_LC_RFSF) {
				gameType = publish.gameType.JJ_LC_RFSF;
				betCount  = 1;
			}else if(itemList[0].gameType == publish.gameType.JJ_LC_DXF) {
				gameType = publish.gameType.JJ_LC_DXF;
				betCount  = 1;
			}
			passType = 2;
		}
	}else if("sfc" == gameCode){
		gameType = publish.gameType.JJ_14SFC;
		issueNo = $("#issueNo").val();
		itemList = publish.extract.sfc_or_r9c(publish.gameType.JJ_14SFC);
	}else if("r9c" == gameCode){
		gameType = publish.gameType.JJ_R9C;
		issueNo = $("#issueNo").val();
		itemList = publish.extract.sfc_or_r9c(publish.gameType.JJ_R9C);
	} else if ("bjdc" == gameCode) {
        gameType = publish.gameType.BJDC_SPF;
        itemList = publish.extract.bjdc_spf();
    }
	
	//胜负彩选中的赛事数量
	var fnSfcMatchCount = function(){
		var resultList = itemList[0].content.split("|");
		var resultCount = 0;
		for(var i=0; i < resultList.length; i++){
			if(resultList[i] != '_'){
				resultCount++;
			}
		}
		
		return resultCount;
	};
	
	if(summary.length < 20 || summary.length>200){
		return jiedu.dialog.alert("请输入摘要且最多输入20-200个字符");
	}
	
	if(title.length == 0 || jiedu.getLength(title) > 80){
		return jiedu.dialog.alert("请输入标题且最多输入40个汉字或80个字符");
	}else if(itemList.length == 0){
		return jiedu.dialog.alert("请至少择一场比赛的结果");
	}else if(content.length == 0){
		return jiedu.dialog.alert("请输入解读内容");
	}else if(jiedu.getLength(content) > 60000){
		return jiedu.dialog.alert("您的解读内容超过最大长度");
	}else if(isNaN(price)){
		return jiedu.dialog.alert("请选择查看价格");
	}

	if(title.indexOf("退款") >=0 || summary.indexOf("退款") >=0 || content.indexOf("退款") >=0 ||
		title.indexOf("不中") >=0 || summary.indexOf("不中") >=0 || content.indexOf("不中") >=0 ||
		title.indexOf("返还") >=0 || summary.indexOf("返还") >=0 || content.indexOf("返还") >=0 ) {
		jiedu.dialog.alert("发布解读的标题/摘要/内容不能包含 退款/返还/不中 字样");
		return false;
	}


	if(gameType == publish.gameType.JJ_14SFC && fnSfcMatchCount() < 14){
		return jiedu.dialog.alert("请至少择14场比赛的结果");
	}else if(gameType == publish.gameType.JJ_R9C && fnSfcMatchCount() < 9){
		return jiedu.dialog.alert("请至少择9场比赛的结果");
	}else if(gameType == publish.gameType.JJ_LC || gameType == publish.gameType.JJ_LC_SFC){
		for(var i in itemList){
			if(publish.gameType.JJ_LC_SFC == itemList[i].gameType && itemList[i].content.match(/\([^\)]+\)/g)[0].split(",").length > 2){
				return jiedu.dialog.alert("同一场比赛最多选择2个胜分差结果");
			}
		}
	}
	
	return {
		gameType: gameType,
		passType: passType,
		betCount: betCount,
		issueNo: issueNo,
		title: title,
		content: content,
		price : price,
		itemList : itemList,
		summary: summary
	};
};

publish.regex = function(content){
	return content.match(/\([^\)]+\)/g);
}

publish.extract = {};
//竞彩足球-单关
publish.extract.jczq_dg = function(){
	var itemList = [];
	$("#jczq_dg_table tbody:gt(0) tr:nth-child(n+2)").each(function(){
		var matchId = $(this).data("matchid");
		var matchNo = $(this).data("matchno");
		
		//胜平负
		if($(this).find("td.Jq_sp p:first-child.bgp_sp2").length > 0){
			var allSp = [];
			var chsSp = [];
			$(this).find("td.Jq_sp p:first-child").each(function(){
				allSp.push($(this).data("sp"));
				if($(this).hasClass("bgp_sp2")){
					chsSp.push($(this).data("sp"));
				}
			});
			
			var item = {
  				matchId: matchId,
				gameType: publish.gameType.JJ_ZC_SPF,
				content:  matchNo + "(" + chsSp.join(",") + ")",
				spContent: matchNo + "(" + allSp.join(",") + ")"
  			}
			
			itemList.push(item);
		}
		
		//让球胜平负 
		if($(this).find("td.Jq_sp p:last-child.bgp_sp2").length > 0){
			var handicap = $(this).find("td.Jq_handicap p:last-child").data("handicap");
			var allSp = [];
			var chsSp = [];
			$(this).find("td.Jq_sp p:last-child").each(function(){
				allSp.push($(this).data("sp"));
				if($(this).hasClass("bgp_sp2")){
				chsSp.push($(this).data("sp"));
			}
			});
			
			var item = {
  				matchId: matchId,
				gameType: publish.gameType.JJ_ZC_RQ_SPF,
				content:  matchNo + "[" + handicap + "](" + chsSp.join(",") + ")",
				spContent: matchNo + "[" + handicap + "](" + allSp.join(",") + ")"
  			}
			
			itemList.push(item);
		}
	});
	
	return itemList;
};

publish.extract.bjdc_spf = function(){
    var itemList = [];
    $("#bjdc_table tbody:gt(0) tr:nth-child(n+2)").each(function(){
        var matchId = $(this).data("matchid");
        var matchNo = $(this).data("matchno");

        //胜平负
        if($(this).find("td.Jq_sp p:first-child.bgp_sp2").length > 0){
            var allSp = [];
            var chsSp = [];
            $(this).find("td.Jq_sp p:first-child").each(function(){
                allSp.push($(this).data("sp"));
                if($(this).hasClass("bgp_sp2")){
                    chsSp.push($(this).data("sp"));
                }
            });

            var item = {
                matchId: matchId,
                gameType: publish.gameType.JJ_ZC_SPF,
                content:  matchNo + "(" + chsSp.join(",") + ")",
                spContent: matchNo + "(" + allSp.join(",") + ")"
            }

            itemList.push(item);
        }

        //让球胜平负
        if($(this).find("td.Jq_sp p:last-child.bgp_sp2").length > 0){
            var handicap = $(this).find("td.Jq_handicap p:last-child").data("handicap");
            var allSp = [];
            var chsSp = [];
            $(this).find("td.Jq_sp p:last-child").each(function(){
                allSp.push($(this).data("sp"));
                if($(this).hasClass("bgp_sp2")){
                    chsSp.push($(this).data("sp"));
                }
            });

            var item = {
                matchId: matchId,
                gameType: publish.gameType.BJDC_SPF,
                content:  matchNo + "[" + handicap + "](" + chsSp.join(",") + ")",
                spContent: matchNo + "[" + handicap + "](" + allSp.join(",") + ")"
            }

            itemList.push(item);
        }
    });

    return itemList;
};

//竞彩足球-亚盘
publish.extract.jczq_yp = function(){
	var itemList = [];
	
	$("#jczq_yp_table tbody:gt(0) tr:nth-child(n+2)").each(function(){
		var matchId = $(this).data("matchid");
		var matchNo = $(this).data("matchno");
		var handicap  = $(this).find("td.game_pankou").data("handicap");
		
		if($(this).find("td.Jq_sp.gm_bg2").length > 0){
			var allSp = [];
			var chsSp = [];
			$(this).find("td.Jq_sp").each(function(){
				allSp.push($(this).data("sp"));
					if($(this).hasClass("gm_bg2")){
					chsSp.push($(this).data("sp"));
				}
			});
			
			var item = {
				matchId: matchId,
				gameType: publish.gameType.JJ_ZC_YP,
				content:  matchNo + "[" + handicap  + "](" + chsSp.join(",") + ")",
				spContent: matchNo + "[" + handicap  + "](" + allSp.join(",") + ")"
			}
					
			itemList.push(item);
		}
	});
	
	return itemList;
}

//竞彩足球-大小球
publish.extract.jczq_dxq = function(){
	var itemList = [];
	
	$("#jczq_dxq_table tbody:gt(0) tr:nth-child(n+2)").each(function(){
		var matchId = $(this).data("matchid");
		var matchNo = $(this).data("matchno");
		var handicap  = $(this).find("td.game_pankou").data("handicap");
		
		if($(this).find("td.Jq_sp.gm_bg2").length > 0){
			var allSp = [];
				var chsSp = [];
			$(this).find("td.Jq_sp").each(function(){
				allSp.push($(this).data("sp"));
					if($(this).hasClass("gm_bg2")){
					chsSp.push($(this).data("sp"));
				}
			});
			
			var item = {
				matchId: matchId,
				gameType: publish.gameType.JJ_ZC_WW_DXQ,
				content:  matchNo + "[" + handicap  + "](" + chsSp.join(",") + ")",
				spContent: matchNo + "[" + handicap  + "](" + allSp.join(",") + ")"
			}
					
			itemList.push(item);
		}
	});	
	
	return itemList;
}

//竞彩足球-混合
publish.extract.jczq_hh = function(){
	var itemList = [];
	
	$("#jczq_hh_table table.unfold").each(function(){
   		var matchId = $(this).data("matchid");
   		var matchNo = $(this).data("matchno");
   		
		$(this).find("tr").each(function(){
       		//当前行是否有选项被选中
			if($(this).find("td:nth-child(n+2).chos_td").length > 0){
				var gameType = $(this).data("gametype");
				
				if(publish.gameType.JJ_ZC_SPF == gameType){	//胜平负
					var allSp = [];
					var chsSp = [];
					$(this).find("td:nth-child(n+2)").each(function(){
						allSp.push($(this).data("sp"));
						if($(this).hasClass("chos_td")){
							chsSp.push($(this).data("sp"));
						}
					});
					
					var item = {
						matchId: matchId,
						gameType: gameType,
						content:  matchNo + "(" + chsSp.join(",") + ")",
						spContent: matchNo + "(" + allSp.join(",") + ")"
					}
					
					itemList.push(item);
				}else if(publish.gameType.JJ_ZC_RQ_SPF == gameType){	//让球胜平负
					var handicap = $(this).find("td:first").data("handicap");
					var allSp = [];
					var chsSp = [];
					$(this).find("td:nth-child(n+2)").each(function(){
						allSp.push($(this).data("sp"));
						if($(this).hasClass("chos_td")){
							chsSp.push($(this).data("sp"));
						}
					});
					
					var item = {
						matchId: matchId,
						gameType: gameType,
						content:  matchNo + "[" + handicap + "](" + chsSp.join(",") + ")",
						spContent: matchNo + "[" + handicap + "](" + allSp.join(",") + ")"
					}
					
					itemList.push(item);
				}else if(publish.gameType.JJ_ZC_YP == gameType){	//亚盘
					var handicap  = $(this).find("td:first").data("handicap");
					var allSp = [];
					var chsSp = [];
					$(this).find("td:nth-child(n+2)").each(function(){
						allSp.push($(this).data("sp"));
						if($(this).hasClass("chos_td")){
							chsSp.push($(this).data("sp"));
						}
					});
					
					var item = {
						matchId: matchId,
						gameType: gameType,
						content:  matchNo + "[" + handicap  + "](" + chsSp.join(",") + ")",
						spContent: matchNo + "[" + handicap  + "](" + allSp.join(",") + ")"
					}
					
					itemList.push(item);
				}else if(publish.gameType.JJ_ZC_WW_DXQ == gameType){	//大小球
					var handicap  = $(this).find("td:first").data("handicap");
					var allSp = [];
					var chsSp = [];
					$(this).find("td:nth-child(n+2)").each(function(){
						allSp.push($(this).data("sp"));
						if($(this).hasClass("chos_td")){
							chsSp.push($(this).data("sp"));
						}
					});
					
					var item = {
						matchId: matchId,
						gameType: gameType,
						content:  matchNo + "[" + handicap  + "](" + chsSp.join(",") + ")",
						spContent: matchNo + "[" + handicap  + "](" + allSp.join(",") + ")"
					}
					
					itemList.push(item);
				}
			}	
		});
   	});
	
	return itemList;
}

//竞彩篮球-胜负
publish.extract.jclq_sf = function(){
	var itemList = [];
	
	$("#jclq_sf_table tbody:gt(0) tr:nth-child(n+2)").each(function(){
		//当前行是否有选项被选中
		if($(this).find("td:nth-child(n+2).bgp_sp2").length > 0){
			var matchId = $(this).data("matchid");
			var matchNo = $(this).data("matchno");
			
			var allSp = [];
			var chsSp = [];
			$(this).find("td.Jq_sp").each(function(){
				allSp.push($(this).data("sp"));
				if($(this).hasClass("bgp_sp2")){
					chsSp.push($(this).data("sp"));
				}
			});
			
			var item = {
				matchId: matchId,
				gameType: publish.gameType.JJ_LC_SF,
				content:  matchNo + "(" + chsSp.join(",") + ")",
				spContent: matchNo + "(" + allSp.join(",") + ")"
			}
					
			itemList.push(item);
		}
	});
		
	return itemList;
}

//竞彩篮球-让分胜负
publish.extract.jclq_rfsf = function(){
	var itemList = [];
	
	$("#jclq_rfsf_table tbody:gt(0) tr:nth-child(n+2)").each(function(){
		//当前行是否有选项被选中
		if($(this).find("td:nth-child(n+2).bgp_sp2").length > 0){
			var matchId = $(this).data("matchid");
			var matchNo = $(this).data("matchno");
			var handicap  = $(this).find("td.Jq_handicap").data("handicap");
			var allSp = [];
			var chsSp = [];
			$(this).find("td.Jq_sp").each(function(){
				allSp.push($(this).data("sp"));
				if($(this).hasClass("bgp_sp2")){
					chsSp.push($(this).data("sp"));
				}
			});
			
			var item = {
				matchId: matchId,
				gameType: publish.gameType.JJ_LC_RFSF,
				content:  matchNo + "[" + handicap + "](" + chsSp.join(",") + ")",
				spContent: matchNo + "[" + handicap + "](" + allSp.join(",") + ")"
			}
					
			itemList.push(item);
		}
	});
		
	return itemList;
}

//竞彩篮球-胜分差
publish.extract.jclq_sfc = function(){
	var itemList = [];
	
	$("#jclq_sfc_table tbody:gt(0) tr:nth-child(n+2)").each(function(){
		//当前行是否有选项被选中
		if($(this).find("table td.bgp_sp2").length > 0){
			var matchId = $(this).data("matchid");
			var matchNo = $(this).data("matchno");
			var allSp = [];
			var chsSp = [];
			$(this).find("table td.Jq_sp").each(function(){
				allSp.push($(this).data("sp"));
				if($(this).hasClass("bgp_sp2")){
					chsSp.push($(this).data("sp"));
				}
			});
			
			var item = {
				matchId: matchId,
				gameType: publish.gameType.JJ_LC_SFC,
				content:  matchNo + "(" + chsSp.join(",") + ")",
				spContent: matchNo + "(" + allSp.join(",") + ")"
			}
					
			itemList.push(item);
		}
	});
	
	return itemList;
}

//竞彩篮球-大小分
publish.extract.jclq_dxf = function(){
	var itemList = [];
	
	$("#jclq_dxf_table tbody:gt(0) tr:nth-child(n+2)").each(function(){
		//当前行是否有选项被选中
		if($(this).find("td:nth-child(n+2).bgp_sp2").length > 0){
			var matchId = $(this).data("matchid");
			var matchNo = $(this).data("matchno");
			var handicap  = $(this).find("td.Jq_handicap").data("handicap");
			var allSp = [];
			var chsSp = [];
			$(this).find("td.Jq_sp").each(function(){
				allSp.push($(this).data("sp"));
				if($(this).hasClass("bgp_sp2")){
					chsSp.push($(this).data("sp"));
				}
			});
			
			var item = {
				matchId: matchId,
				gameType: publish.gameType.JJ_LC_DXF,
				content:  matchNo + "[" + handicap + "](" + chsSp.join(",") + ")",
				spContent: matchNo + "[" + handicap + "](" + allSp.join(",") + ")"
			}
					
			itemList.push(item);
		}
	});
		
	return itemList;
}

//竞彩篮球-混合
publish.extract.jclq_hh = function(){
	var itemList = [];
	
	$("#jclq_hh_table table.unfold").each(function(){
   		var matchId = $(this).data("matchid");
   		var matchNo = $(this).data("matchno");
   		
   		$(this).find("tr").each(function(){
   			//当前行是否有选项被选中
			if($(this).find("td:nth-child(n+2).chos_td").length > 0){
				var gameType = $(this).data("gametype");
				
				if(publish.gameType.JJ_LC_RFSF == gameType){	//让分胜负
					var handicap = $(this).find("td:first").data("handicap");
					var allSp = [];
					var chsSp = [];
					$(this).find("td:nth-child(n+2)").each(function(){
						allSp.push($(this).data("sp"));
						if($(this).hasClass("chos_td")){
							chsSp.push($(this).data("sp"));
						}
					});
					
					var item = {
						matchId: matchId,
						gameType: gameType,
						content:  matchNo + "[" + handicap + "](" + chsSp.join(",") + ")",
						spContent: matchNo + "[" + handicap + "](" + allSp.join(",") + ")"
					}
					
					itemList.push(item);
				}else if(publish.gameType.JJ_LC_DXF == gameType){	//大小分
					var handicap = $(this).find("td:first").data("handicap");
					var allSp = [];
					var chsSp = [];
					$(this).find("td:nth-child(n+2)").each(function(){
						allSp.push($(this).data("sp"));
						if($(this).hasClass("chos_td")){
							chsSp.push($(this).data("sp"));
						}
					});
					
					var item = {
						matchId: matchId,
						gameType: gameType,
						content:  matchNo + "[" + handicap + "](" + chsSp.join(",") + ")",
						spContent: matchNo + "[" + handicap + "](" + allSp.join(",") + ")"
					}
					
					itemList.push(item);
				}else if(publish.gameType.JJ_LC_SF == gameType){	//胜负
					var allSp = [];
					var chsSp = [];
					$(this).find("td:nth-child(n+2)").each(function(){
						allSp.push($(this).data("sp"));
						if($(this).hasClass("chos_td")){
							chsSp.push($(this).data("sp"));
						}
					});
					
					var item = {
						matchId: matchId,
						gameType: gameType,
						content:  matchNo + "(" + chsSp.join(",") + ")",
						spContent: matchNo + "(" + allSp.join(",") + ")"
					}
					
					itemList.push(item);
				}else if(publish.gameType.JJ_LC_SFC == gameType){	//胜分差
					var allSp = [];
					var chsSp = [];
					$(this).find("td.Jq_sp").each(function(){
						allSp.push($(this).data("sp"));
						if($(this).hasClass("chos_td")){
							chsSp.push($(this).data("sp"));
						}
					});
					
					var item = {
						matchId: matchId,
						gameType: gameType,
						content:  matchNo + "(" + chsSp.join(",") + ")",
						spContent: matchNo + "(" + allSp.join(",") + ")"
					}
							
					itemList.push(item);
				}
			}
   		});
	});
   		
	return itemList;
}


Array.prototype.remove = function(val) { var index = this.indexOf(val); if (index > -1) { this.splice(index, 1); } };

//胜负彩、任9
publish.extract.sfc_or_r9c = function(gameType){
	var itemList = [];
	
	if($("#sfc_table tr:nth-child(n+2) td.Jq_sp p.gm_bg2").length > 0){
		var allSp = [];
		var chsSp = [];
		
		$("#sfc_table tr:nth-child(n+2)").each(function(){
			var sp1 = [];
			var sp2 = [];
			$(this).find("td.Jq_sp p").each(function(){
				var sp = $(this).data("sp");
				sp1.push(sp);
				if($(this).hasClass("gm_bg2")){
					sp2.push(sp);
				}
			});
			
			allSp.push(sp1.join(","));
			chsSp.push(sp2.length == 0 ? "_" : sp2.join(","));
		});
		
		var item = {
			gameType: gameType,
			content: chsSp.join("|")
		}
		
		itemList.push(item);
	}
	return itemList;
}


publish.extract.jclq_2c1_hh = function(){
	var itemList = [];
	$("#jclq_hh_table table.unfold").each(function(){//展开的table遍历
		var matchId = $(this).data("matchid");
   		var matchNo = $(this).data("matchno");
   		var rfsfContent,rfsfSpContent,dxfContent,dxfSpContent,sfContent,sfcContent,sfSpContent,sfcSpContent,content,spContent;
   		$(this).find("tr").each(function(){
   			if($(this).find("td:nth-child(n+2).chos_td").length > 0){ //在tr中找选中的td
   				var gameType = $(this).data("gametype");
   				if(publish.gameType.JJ_LC_RFSF == gameType || publish.gameType.JJ_LC_DXF == gameType || publish.gameType.JJ_LC_SF == gameType){
   					var handicap = $(this).find("td:first").data("handicap");
   					var allSp = [];
					var chsSp = [];
					$(this).find("td:nth-child(n+2)").each(function(){
						allSp.push($(this).data("sp"));
						if($(this).hasClass("chos_td")){
							chsSp.push($(this).data("sp"));
						}
						if(publish.gameType.JJ_LC_RFSF == gameType && chsSp.length > 0) {
							rfsfContent = matchNo + "[" + handicap + "](" + chsSp.join(",") + "){4062}";
							rfsfSpContent = matchNo + "[" + handicap + "](" + allSp.join(",") + "){4062}";
						}
						if(publish.gameType.JJ_LC_DXF == gameType && chsSp.length > 0) {
							dxfContent = matchNo + "[" + handicap + "](" + chsSp.join(",") + "){4064}";
							dxfSpContent = matchNo + "[" + handicap + "](" + allSp.join(",") + "){4064}";
						}
						if(publish.gameType.JJ_LC_SF == gameType && chsSp.length > 0) {
							sfContent = matchNo +"(" + chsSp.join(",") + "){4061}";
							sfSpContent = matchNo + "(" + allSp.join(",") + "){4061}";
						}
					});
   				}else if(publish.gameType.JJ_LC_SFC == gameType){	//胜分差
   					var allSp = [];
					var chsSp = [];
					$(this).find("td.Jq_sp").each(function(){
						allSp.push($(this).data("sp"));
						if($(this).hasClass("chos_td")){
							chsSp.push($(this).data("sp"));
						}
						sfcContent = matchNo + "(" + chsSp.join(",") + "){4063}";
						sfcSpContent = matchNo + "(" + allSp.join(",") + "){4063}";
					});
   				}
   			}
   		});

   		
   		if((rfsfContent != undefined && dxfContent != undefined) || (rfsfContent != undefined && sfContent != undefined) || (rfsfContent != undefined && sfcContent != undefined)
   				||(dxfContent != undefined && sfContent != undefined) ||(dxfContent != undefined && sfcContent != undefined)
   				|| (sfContent != undefined && sfcContent != undefined)) {
   			gameType = publish.gameType.JJ_LC_HH;
   			content = (rfsfContent == undefined ?"": "~"+rfsfContent) +(dxfContent == undefined ?"": "~"+dxfContent) +
				  (sfContent == undefined ?"": "~"+sfContent) +(sfcContent == undefined ?"": "~"+sfcContent);
   			content = content.substring(1);
   			spContent = (rfsfSpContent == undefined ?"": "~"+rfsfSpContent) +(dxfSpContent == undefined ?"": "~"+dxfSpContent) +
   				(sfSpContent == undefined ?"": "~"+sfSpContent) +(sfcSpContent == undefined ?"": "~"+sfcSpContent);
   			spContent = spContent.substring(1);
   		} else if(sfContent != undefined) {
   			gameType = publish.gameType.JJ_LC_SF;
   			content = sfContent.replace("{4061}","");
			spContent = sfSpContent.replace("{4061}","");
   		}  else if(rfsfContent != undefined) {
   			gameType = publish.gameType.JJ_LC_RFSF;
   			content = rfsfContent.replace("{4062}","");
			spContent = rfsfSpContent.replace("{4062}","");
   		} else if(sfcContent != undefined) {
   			gameType = publish.gameType.JJ_LC_SFC;
   			content = sfcContent.replace("{4063}","");
			spContent = sfcSpContent.replace("{4063}","");
   		} else if(dxfContent != undefined) {
   			gameType = publish.gameType.JJ_LC_DXF;
   			content = dxfContent.replace("{4064}","");
			spContent = dxfSpContent.replace("{4064}","");
   		} 
   		
   		if(content == undefined || spContent == undefined) {
   			return;
   		}
   		var item = {
			matchId: matchId,
			gameType: gameType,
			content:  content,
			spContent: spContent
		}
   		itemList.push(item);
	});
	return itemList;
}