	var lqCommonData = {};
	//篮球投注项选中校验
	function checkLqCommon2(bindObj, isHh) {
		var publishType = $("input[name='publishType']:checked").val();
		if(publishType == 2) {
			jiedu.dialog.alert("临场解读无须选择比赛和结果，在预设时间发布推荐结果即可！");
			return;
		}
		var sp,gameType,gameId,matchId,dataIndex,_this = bindObj;
		sp = _this.data("sp");
		if(sp == "" || sp == undefined || sp == "-") {
			jiedu.dialog.alert("无法选取本场比赛，未获取到比赛sp值！");
			return;
		}
		dataIndex = _this.data("index");
		gameId = _this.parent().data("gametype");
		if(isHh == 1 || isHh == 2) {
			matchId = _this.parents(".unfold").data("fxid");
		} else {
			matchId = _this.parents(".Jq_line_match").data("fxid");
		}
		if(gameId == 4061) {
			gameType = "lqsf";
		} else if(gameId == 4062) {
			gameType = "lqrfsf";
		} else if(gameId == 4064) {
			gameType = "lqdxf";
		} else if(gameId == "4063_s") {
			gameType = "lqsfc_s";
			gameId = 4063;
		} else if(gameId == "4063_f") {
			gameType = "lqsfc_f";
			gameId = 4063;
		}
		
		if(typeof(isImprov) == "undefined") {
			var isAllowed = false;
			$.ajax({
				type: "post",
				async : false,
				url: "/publish/checkPublished",
				data: {raceId: matchId,gameId: gameId},
				success : function(result){
					if(result.isSuccess) {
						isAllowed = true;
					} else {
						jiedu.dialog.alert(result.msg);
						isAllowed = false;
					}
				}
			});
			
			if(!isAllowed) return;
		}
		
		if(sp == "-") {
			jiedu.dialog.alert("目前赛事不支持推荐。");
			return;
		} else if(sp.split("-").length == 2) {
			sp = sp.split("-")[1];
		}
		
		//篮球胜负大于1.2
		if(gameType == "lqsf" && Number(sp*100) <= Number(spData.lqSingeSp)) {
			jiedu.dialog.alert("请选择大于"+Number(spData.lqSingeSp/100)+"的赔率");
			return;
		}
		
		// 本场赛事第一次选取
		if(!lqCommonData[matchId]) { 
			var typeObj = {'lqsf': [0,0], 'lqrfsf': [0,0], 'lqdxf': [0,0], 'lqsfc_s': [0,0,0,0,0,0], 'lqsfc_f': [0,0,0,0,0,0]};
			typeObj[gameType][dataIndex] = sp; //为数组指定位置上存值
			lqCommonData[matchId] = typeObj;
			if(checkMatchLq(lqCommonData) > 2) {
				typeObj[gameType][dataIndex] = 0;
				jiedu.dialog.alert("一个方案最多只支持2场赛事");
				return;
			}
			if(isHh == 1) {
				bindObj.toggleClass("chos_td");
			} else if(isHh == 2){
				if(countObjProperty(lqCommonData) >2) {
					jiedu.dialog.alert("2串1只能选2场赛事");
					return;
				}
				bindObj.toggleClass("chos_td");
				predictLqMinPrize(lqCommonData);
			} else {
				bindObj.toggleClass("bgp_sp2");
			}
			return;
		}
		
		if(lqCommonData[matchId][gameType][dataIndex] == sp) {
			lqCommonData[matchId][gameType][dataIndex] = 0;
			if(isHh == 1) {
				bindObj.toggleClass("chos_td");
			} else if(isHh == 2) {
				bindObj.toggleClass("chos_td");
				predictLqMinPrize(lqCommonData);
			} else {
				bindObj.toggleClass("bgp_sp2");
			}
			return;
		}
		lqCommonData[matchId][gameType][dataIndex] = sp;
		//篮彩单项判断 start
		if(checkMatchLq(lqCommonData) > 2) {
			jiedu.dialog.alert("一个方案最多只支持2场赛事");
			lqCommonData[matchId][gameType][dataIndex] = 0;
			return;
		}
		if(checkGameTypeLq(lqCommonData[matchId]) == false) {
			jiedu.dialog.alert("单场赛事限一个玩法");
			lqCommonData[matchId][gameType][dataIndex] = 0;
			return;
		}
		if(gameType == 'lqrfsf' && countEffectBetSp(lqCommonData[matchId].lqrfsf) > 1) {
			lqCommonData[matchId][gameType][dataIndex] = 0;
			jiedu.dialog.alert("让分胜负只能选1个结果");
			return;
		}
		if(gameType == 'lqsf' && countEffectBetSp(lqCommonData[matchId].lqsf) > 1) {
			lqCommonData[matchId][gameType][dataIndex] = 0;
			jiedu.dialog.alert("胜负只能选1个结果");
			return;
		}
		if(gameType == 'lqdxf' && countEffectBetSp(lqCommonData[matchId].lqdxf) > 1) {
			lqCommonData[matchId][gameType][dataIndex] = 0;
			jiedu.dialog.alert("大小分只能选1个结果");
			return;
		}
		if((gameType == 'lqsfc_s' || gameType == 'lqsfc_f') && isHh != 2) {
			var sfcCount = countEffectBetSp(lqCommonData[matchId].lqsfc_s) + countEffectBetSp(lqCommonData[matchId].lqsfc_f);
			if(sfcCount > 2) {
				lqCommonData[matchId][gameType][dataIndex] = 0;
				jiedu.dialog.alert("胜分差只能选1-2个结果");
				return;
			}
		}
		if(isHh == 1) {
			bindObj.toggleClass("chos_td");
		} else if(isHh == 2){
			bindObj.toggleClass("chos_td");
			predictLqMinPrize(lqCommonData);
		} else {
			bindObj.toggleClass("bgp_sp2");
		}
	};

