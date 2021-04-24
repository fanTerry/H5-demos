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

//篮球二串1

publish.extract.jclq_2c1_hh = function(){
	var itemList = [];
	$("#jclq_2c1_table table.unfold").each(function(){//展开的table遍历
		var matchId = $(this).data("matchid");
   		var matchNo = $(this).data("matchno");
   		var fixId = $(this).data("fxid");
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
			fixId: fixId,
			gameType: gameType,
			content:  content,
			spContent: spContent
		}
   		itemList.push(item);
	});
	return itemList;
}