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

publish.extract.bjdc_spf = function(){
    var itemList = [];
    $("#bjdc_table tbody:gt(0) tr:nth-child(n+2)").each(function(){
        var matchId = $(this).data("matchid");
        var matchNo = $(this).data("matchno");

        //胜平负
        if($(this).find("td.Jq_sp p:first-child.bgp_sp2").length > 0){
            var handicap = $(this).find("td.Jq_handicap p:first-child").data("handicap");
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
                gameType: publish.gameType.BJDC_SPF,
                content:  matchNo + "[" + handicap + "](" + chsSp.join(",") + ")",
                spContent: matchNo + "[" + handicap + "](" + allSp.join(",") + ")"
            }
            itemList.push(item);
        }
    });
    return itemList;
};

