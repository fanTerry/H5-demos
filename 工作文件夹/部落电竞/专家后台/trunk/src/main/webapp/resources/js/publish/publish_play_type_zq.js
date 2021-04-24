	var zqCommonData = {};
	//@param isHh 说明 1(hh) chos_td 2(spf/rfspf) bgp_sp2 3(yp) gm_bg2 
	function checkZqCommon2(bindObj, isHh, zqDgFlag) {

		var publishType = $("input[name='publishType']:checked").val();
		if(publishType == 2) {
			jiedu.dialog.alert("临场解读无须选择比赛和结果，在预设时间发布推荐结果即可！");
			return;
		}
		var gameType,sp,gameId,matchid,dataIndex,isAllowed = false;
		if(zqDgFlag == 4071) {
			gameId = 4071;
		}else if(zqDgFlag == 4076) {
			gameId = 4076;
		} else if (zqDgFlag == 4051) {
            gameId = 4051;
        } else {
            gameId = bindObj.parent().data("gametype");
        }
		dataIndex = bindObj.data("index");
		if(gameId == 4071) {
			gameType = "spf";
		} else if(gameId == 4076) {
			gameType = "rqspf";
		} else if(gameId == 4079) {
			gameType = "dxq";
		} else if(gameId == "4078") {
			gameType = "yp";
		} else if (gameId == 4051) {
            gameType = "bjdc";
        }
		
		if((isHh !=1) &&(gameType == "spf" || gameType == "rqspf")) {
			sp = bindObj.data("sp");
			matchId = bindObj.parents(".Jq_line_match").data("fxid");
		} else if((isHh !=1)&& (gameType == "yp" || gameType == "dxq")) {
			sp = bindObj.html().trim();
			matchId = bindObj.parent().data("fxid");
		} else if (isHh !=1 && gameType == "bjdc" ) {
            sp = bindObj.data("sp");
            matchId = bindObj.parents(".Jq_line_match").data("fxid");
        } else {
            sp = bindObj.data("sp");
            matchId = bindObj.parents(".unfold").data("fxid");
        }
		
		if(sp == "" || sp == undefined || sp == "-") {
			jiedu.dialog.alert("无法选取本场比赛，未获取到比赛sp值！");
			return;
		}
		if(typeof(isImprov) == "undefined") {
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
			if(sp == "") {jiedu.dialog.alert("目前赛事sp不支持推荐。");  return;}
		}
		
		if((gameType=="spf" || gameType == "rqspf") && (Number(sp*100) <= spData.zqSingeSp)) {
			jiedu.dialog.alert("请选择大于"+Number(spData.zqSingeSp/100)+"的赔率");
			return;
		}
        if (gameType == "bjdc" && (Number(sp * 100) <= spData.bdSingeSp)) {
            jiedu.dialog.alert("请选择大于" + Number(spData.bdSingeSp / 100) + "的赔率");
            return;
        }
		
		// 本场赛事第一次选取
		if(!zqCommonData[matchId]) {
			var typeObj = {'spf': [0,0,0], 'rqspf': [0,0,0], 'yp': [0,0], 'dxq': [0,0],'bjdc':[0,0,0]};
            typeObj[gameType][dataIndex] = sp; //为数组指定位置上存值
            zqCommonData[matchId] = typeObj;
				if(checkMatch(zqCommonData) > 1) {
                typeObj[gameType][dataIndex] = 0;
                jiedu.dialog.alert("一个方案最多只支持1场赛事");
                return;
            }
			if(isHh == 1) {
				bindObj.toggleClass("chos_td");
			} else if(isHh == 2){
				bindObj.toggleClass("bgp_sp2");
			} else if(isHh == 3){
				bindObj.toggleClass("gm_bg2");
			}
			return;
		}

		if(zqCommonData[matchId][gameType][dataIndex] == sp) {
			zqCommonData[matchId][gameType][dataIndex] = 0;
			if(isHh == 1) {
				bindObj.toggleClass("chos_td");
			} else if(isHh == 2){
				bindObj.toggleClass("bgp_sp2");
			} else if(isHh == 3){
				bindObj.toggleClass("gm_bg2");
			}
			return;
		}
		zqCommonData[matchId][gameType][dataIndex] = sp;
		if(gameType == 'bjdc' && countEffectBetSp(zqCommonData[matchId].bjdc) > 1) {
			zqCommonData[matchId][gameType][dataIndex] = 0;
			jiedu.dialog.alert("北京单场一场比赛只能选1个结果");
			return;
		}
		if(gameType == 'bjdc' && checkMatch(zqCommonData) > 1) {
			jiedu.dialog.alert("一个方案最多只支持1场赛事");
			zqCommonData[matchId][gameType][dataIndex] = 0;
			return;
		}
		if(checkMatch(zqCommonData) > 1) {
			jiedu.dialog.alert("一个方案最多只支持2场赛事");
			zqCommonData[matchId][gameType][dataIndex] = 0;
			return;
		}
		//足彩单项判断 start
		if(checkGameType(zqCommonData[matchId]) == false) {
			jiedu.dialog.alert("单场赛事限一个玩法,胜平负/让球胜平负算一个");
			zqCommonData[matchId][gameType][dataIndex] = 0;
			return;
		}
		if(gameType == 'yp' && countEffectBetSp(zqCommonData[matchId].yp) > 1) {
			zqCommonData[matchId][gameType][dataIndex] = 0;
			jiedu.dialog.alert("亚盘只能选1个结果");
			return;
		}
		if(gameType == 'dxq' && countEffectBetSp(zqCommonData[matchId].dxq) > 1) {
			zqCommonData[matchId][gameType][dataIndex] = 0;
			jiedu.dialog.alert("大小球只能选1个结果");
			return;
		}
        if (gameType == 'spf') {
            var spfCount;
            //sp值低于2.0统计
            spfCount = countSpfSp(zqCommonData[matchId].spf);

            if (countEffectBetSp(zqCommonData[matchId].spf) == 3) {
                zqCommonData[matchId][gameType][dataIndex] = 0;
                jiedu.dialog.alert("一场比赛推荐选项不能同时全选");
                return;
            }
            if (spfCount >= 1 && countEffectBetSp(zqCommonData[matchId].spf) >= 2) {
                zqCommonData[matchId][gameType][dataIndex] = 0;
                jiedu.dialog.alert("双选时两个结果的赔率都必须>" + Number(spData.zqDoubleSp / 100));
                return;
            }

        }
        if (gameType == 'bjdc') {
            //sp值低于2.0统计
            var bjdcCount =  countBjdcSp(zqCommonData[matchId].bjdc);
            if (countEffectBetSp(zqCommonData[matchId].bjdc) == 3) {
                zqCommonData[matchId][gameType][dataIndex] = 0;
                jiedu.dialog.alert("一场比赛推荐选项不能同时全选");
                return;
            }
            if (bjdcCount >= 1 && countEffectBetSp(zqCommonData[matchId].bjdc) >= 2) {
                zqCommonData[matchId][gameType][dataIndex] = 0;
                jiedu.dialog.alert("双选时两个结果的赔率都必须>" + Number(spData.bdDoubleSp / 100));
                return;
            }
        }
		if(gameType == 'rqspf') {
			var rqspfCount;
			rqspfCount = countSpfSp(zqCommonData[matchId].rqspf);
			if(countEffectBetSp(zqCommonData[matchId].rqspf) == 3) {
				zqCommonData[matchId][gameType][dataIndex] = 0;
				jiedu.dialog.alert("一场比赛推荐选项不能同时全选");
				return;
			}
			if(rqspfCount >= 1 && countEffectBetSp(zqCommonData[matchId].rqspf) >= 2) {
				zqCommonData[matchId][gameType][dataIndex] = 0;
				jiedu.dialog.alert("双选时两个结果的赔率都必须>"+Number(spData.zqDoubleSp/100));
				return;
			}
		}
		var jcCount = countEffectBetSp(zqCommonData[matchId].spf)+countEffectBetSp(zqCommonData[matchId].rqspf);
		if(jcCount == 2){
			var spfLessSpCount = countSpfSp(zqCommonData[matchId].spf);
			var rqspfLessSpCount = countSpfSp(zqCommonData[matchId].rqspf);
			if((spfLessSpCount+rqspfLessSpCount)>=1){
				zqCommonData[matchId][gameType][dataIndex] = 0;
				jiedu.dialog.alert("双选时两个结果的赔率都必须>"+Number(spData.zqDoubleSp/100));
				return;
			}
		}
		if(jcCount > 2){
			zqCommonData[matchId][gameType][dataIndex] = 0;
			jiedu.dialog.alert("竞彩胜平负/让球胜平负最多选择2个");
			return;
		}
		if ((zqCommonData[matchId].spf[0] > 0 || zqCommonData[matchId].rqspf[0] > 0) && zqCommonData[matchId].yp[1] > 0) {
			zqCommonData[matchId][gameType][dataIndex] = 0;
			jiedu.dialog.alert("竞彩主胜和亚盘客胜，只能选择一个");
			return;
		}
		if ((zqCommonData[matchId].spf[2] > 0 || zqCommonData[matchId].rqspf[2] > 0) && zqCommonData[matchId].yp[0] > 0) {
			zqCommonData[matchId][gameType][dataIndex] = 0;
			jiedu.dialog.alert("竞彩客胜和亚盘主胜，只能选择一个");
			return;
		}
		
		if(isHh == 1) {
			bindObj.toggleClass("chos_td");
		} else if(isHh == 2){
			bindObj.toggleClass("bgp_sp2");
		} else if(isHh == 3){
			bindObj.toggleClass("gm_bg2");
		}
	}
