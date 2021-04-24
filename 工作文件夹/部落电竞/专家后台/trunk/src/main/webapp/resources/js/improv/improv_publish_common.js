var publish = {};
publish.extract = {};

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
	JJ_LC_HH		: 4065		//竞彩蓝球-串关
}

publish.regex = function(content){
	return content.match(/\([^\)]+\)/g);
}

Array.prototype.remove = function(val) { var index = this.indexOf(val); if (index > -1) { this.splice(index, 1); } };

/**
 * 获得发布解读的所有参数
 */
publish.getPostData = function(gameCode, playCode){
	var content = $("#content").val();
	var inpterpreId = $("#jq_interp_id").val();
	
	var gameType;
	var issueNo;
	var itemList = [];
	var passType = 1;
	var betCount = 0;
	
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
			if(itemList.length <= 1) {
				return jiedu.dialog.alert("请选择2场比赛！");
			}
			if(itemList.length > 2) {
				return jiedu.dialog.alert("只能选择2场比赛！");
			}
			
			var fixIds = itemList[0].fixId +","+itemList[1].fixId;
			//console.log(fixIds);
			var checked = false;
			$.ajax({
				type: "post",
				async : false,
				url: "/publish/check2c1Published",
				data: {fixIdStr: fixIds},
				success : function(result){
					if(result.isSuccess) {
						checked = true;	
					} else {
						jiedu.dialog.reload(result.msg);
					}
				}
			});
			if(!checked) {
				return;
			}
			
			if((itemList[0].gameType ==itemList[1].gameType && itemList[0].gameType == publish.gameType.JJ_LC_HH ) 
					|| itemList[0].gameType != itemList[1].gameType ) {
				gameType = publish.gameType.JJ_LC_HH;
				var itemCount1 = itemList[0].content.split("~").length;
				var itemCount2 = itemList[1].content.split("~").length;
				if(itemList[0].content.indexOf(",") > 0) {
					itemCount1 = itemCount1 + itemList[0].content.split(",").length -1;
				}
				if(itemList[1].content.indexOf(",") > 0) {
					itemCount2 = itemCount2 + itemList[1].content.split(",").length -1;
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
	}else if("r9c" == gameCode || "r9" == gameCode){
		gameType = publish.gameType.JJ_R9C;
		issueNo = $("#issueNo").val();
		itemList = publish.extract.sfc_or_r9c(publish.gameType.JJ_R9C);
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
	
	if(gameCode == "jclq" && playCode == "2c1" && spData.lqCgProfitRatio) {
		var _prize = $("#jq_prize_rate").text();
		var _lqcgrate = spData.lqCgProfitRatio;
		if(_prize < _lqcgrate) {
			return jiedu.dialog.alert("2串1方案最低返奖率须≥"+_lqcgrate+"%");
		}
	}
	
	
	if(itemList.length == 0){
		return jiedu.dialog.alert("请至少择一场比赛的结果");
	}else if(content.length == 0){
		return jiedu.dialog.alert("请输入解读内容");
	}else if(jiedu.getLength(content) > 60000){
		return jiedu.dialog.alert("您的解读内容超过最大长度");
	}

	if(content.indexOf("退款") >=0 || content.indexOf("返还") >=0 || content.indexOf("不中") >=0) {
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
	
	var voiceUrl = $("#voiceUrl").val();
	var voiceFreeSecond = $("#voiceFreeSecond").val();
	var freeSecond = $("input[name='freeSecond']:checked").val();
	var voiceSecond = $("#voiceSecond").val();
	
	return {
		id: inpterpreId,
		gameType: gameType,
		passType: passType,
		betCount: betCount,
		issueNo: issueNo,
		content: content,
		itemList : itemList,
		voiceUrl : voiceUrl,
		voiceFreeSecond : voiceFreeSecond,
		freeSecond : freeSecond,
		voiceSecond : voiceSecond
	};
};
