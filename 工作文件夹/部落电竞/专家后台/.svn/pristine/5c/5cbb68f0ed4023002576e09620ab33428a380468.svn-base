	var spData = {
		zqSingeSp : GLOBAL_ZQ_SINGLE_SP == undefined ? 135 : Number(GLOBAL_ZQ_SINGLE_SP*100),
		zqDoubleSp : GLOBAL_ZQ_DOUBLE_SP == undefined ? 200 : Number(GLOBAL_ZQ_DOUBLE_SP*100),
		lqSingeSp : GLOBAL_LQ_SINGLE_SP == undefined ? 135 : Number(GLOBAL_LQ_SINGLE_SP*100),
		zqCgProfitRatio : G_ZQ_2C1_PROFIT_RATE == undefined ? 120 : Number(G_ZQ_2C1_PROFIT_RATE*100),
		lqCgProfitRatio : G_LQ_2C1_PROFIT_RATE == undefined ? 120 : Number(G_LQ_2C1_PROFIT_RATE*100),
        bdSingeSp : GLOBAL_BD_SINGLE_SP == undefined ? 159 : Number(GLOBAL_BD_SINGLE_SP*100),
        bdDoubleSp : GLOBAL_BD_DOUBLE_SP == undefined ? 299 : Number(GLOBAL_BD_DOUBLE_SP*100)
	};
	//统计sp值低于2.0个数
	function countSpfSp(arr) {
		var count = 0,i = 0;
		for(;i<=arr.length;i++) {
			if(Number(arr[i]*100) <= Number(spData.zqDoubleSp) && Number(arr[i]*100) > 1) count++;
		}
		return count;
	}
    function countBjdcSp(arr) {
        var count = 0,i = 0;
        for(;i<=arr.length;i++) {
            if(Number(arr[i]*100) <= Number(spData.bdDoubleSp) && Number(arr[i]*100) > 1) count++;
        }
        return count;
    }
	
	//统计sp大于0的个数
	function countEffectBetSp(arr) {
		//getSpCount
		var count = 0,i = 0;
		for(;i<=arr.length;i++) {
			if(arr[i] > 0) count++;
		}
		return count;
	}

	function checkGameType(matchObj) {
		if((countEffectBetSp(matchObj.spf) >= 1 || countEffectBetSp(matchObj.rqspf) >= 1) && (countEffectBetSp(matchObj.yp) >= 1 || countEffectBetSp(matchObj.dxq) >= 1)
			|| (countEffectBetSp(matchObj.yp) >= 1 && countEffectBetSp(matchObj.dxq) >= 1) )
			{ return false;}
		return true;
	}

	function checkGameTypeLq(matchObj) {
		if((countEffectBetSp(matchObj.lqsf) >= 1 && countEffectBetSp(matchObj.lqrfsf) >= 1) ||
			(countEffectBetSp(matchObj.lqsf) >= 1 && countEffectBetSp(matchObj.lqsfc_s) >= 1) ||
			(countEffectBetSp(matchObj.lqsf) >= 1 && countEffectBetSp(matchObj.lqdxf) >= 1) ||
			(countEffectBetSp(matchObj.lqsf) >= 1 && countEffectBetSp(matchObj.lqsfc_f) >= 1) ||
			(countEffectBetSp(matchObj.lqrfsf) >= 1 && countEffectBetSp(matchObj.lqdxf) >= 1) ||
			(countEffectBetSp(matchObj.lqrfsf) >= 1 && countEffectBetSp(matchObj.lqsfc_s) >= 1) ||
			(countEffectBetSp(matchObj.lqrfsf) >= 1 && countEffectBetSp(matchObj.lqsfc_f) >= 1) ||
			(countEffectBetSp(matchObj.lqdxf) >= 1 && countEffectBetSp(matchObj.lqsfc_s) >= 1) ||
			(countEffectBetSp(matchObj.lqdxf) >= 1 && countEffectBetSp(matchObj.lqsfc_f) >= 1) ||
			(countEffectBetSp(matchObj.lqsfc_s) >= 1 && countEffectBetSp(matchObj.lqsfc_f) >= 1)
		)
		{ return false;}
		return true;
	}

	function checkMatch(matchObj) {
		var count=0, size =Object.keys(matchObj).length;
		var set = new Set();

		var keys = Object.keys(matchObj);
		for (var i = 0; i<keys.length; i++) {
			var match = matchObj[keys[i]];
			var no = keys[i];
			if(countEffectBetSp(match.spf) >= 1 || countEffectBetSp(match.rqspf) >= 1 ||
				countEffectBetSp(match.yp) >= 1 || countEffectBetSp(match.dxq) >= 1 ||  countEffectBetSp(match.bjdc)) {
				if(!set.has(no)) {
					set.add(no);
					count ++;
					continue;
				}
			}
		}
		return count;
	}

	function checkMatchLq(matchObj) {
		var count=0, size =Object.keys(matchObj).length;
		var set = new Set();

		var keys = Object.keys(matchObj);
		for (var i = 0; i<keys.length; i++) {
			var match = matchObj[keys[i]];
			var no = keys[i];
			if(countEffectBetSp(match.lqsf) >= 1 || countEffectBetSp(match.lqrfsf) >= 1 || countEffectBetSp(match.lqdxf) >= 1 ||
				countEffectBetSp(match.lqsfc_s) >= 1 || countEffectBetSp(match.lqsfc_f) >= 1) {
				if(!set.has(no)) {
					set.add(no);
					count ++;
					continue;
				}
			}
		}
		return count;
	}

	//篮球2串1最低预计奖金
	function predictLqMinPrize(lqCommonDataObj) {
		//选2场
		if(countObjProperty(lqCommonDataObj) <= 1) {
			$("#jq_prize_rate").text(0);
			return;
		}
		var minArray = [],countArray = [];
		for(var pro in lqCommonDataObj) {
			 //每一场 对应到编号
			minArray.push(minOfMatchLq(lqCommonDataObj[pro]));
			countArray.push(zhuCountLq(lqCommonDataObj[pro]));
		}
		if(minArray[0]== undefined || minArray[1]== undefined) {
			$("#jq_prize_rate").text("0");
			return;
		}
		var spMinSum = (minArray[0]*100)*(minArray[1]*100)/10000;
		var zhuSum = countArray[0]*countArray[1];
		var prizeRate = ((spMinSum/zhuSum) * 100).toFixed(0);
		console.log(prizeRate);
		$("#jq_prize_rate").text(prizeRate);
		//TODO 这里不用返回 直接修改元素
		//return minSp;
	}
	
	//计算每场选的个数
	function zhuCountLq(matchData) {
		var lqsf = matchData.lqsf, lqrfsf = matchData.lqrfsf, lqdxf= matchData.lqdxf, lqsfc_s = matchData.lqsfc_s, lqsfc_f = matchData.lqsfc_f;
		return countEffectBetSp(lqsf)+countEffectBetSp(lqrfsf)+countEffectBetSp(lqdxf)+countEffectBetSp(lqsfc_s)+countEffectBetSp(lqsfc_f);
	}

	//取每一场比赛里面最小值 非零 篮球专用
	function minOfMatchLq(matchData) {
		var lqsf = matchData.lqsf, lqrfsf = matchData.lqrfsf, lqdxf= matchData.lqdxf, lqsfc_s = matchData.lqsfc_s, lqsfc_f = matchData.lqsfc_f ,min;
		if(sumSp(lqsf) != 0) {
			min = minOfArray(lqsf);
		}
		if(sumSp(lqrfsf) != 0) {
			var lqrfMin =minOfArray(lqrfsf);
			min = (min < lqrfMin ? min : lqrfMin);
		}
		
		if(sumSp(lqdxf) != 0) {
			var lqdxfMin = minOfArray(lqdxf);
			min = (min < lqdxfMin ? min : lqdxfMin);
		}
		
		if(sumSp(lqsfc_s) != 0) {
			var lqsfc_sMin = minOfArray(lqsfc_s);
			min = (min < lqsfc_sMin ? min : lqsfc_sMin);
		}
		
		if(sumSp(lqsfc_f) != 0) {
			var lqsfc_fMin = minOfArray(lqsfc_f);
			min = (min < lqsfc_fMin ? min : lqsfc_fMin);
		}
		
		return min;
	}
	 
	//取数组中最小非零数字
	function minOfArray(arr) {
		var i=0,j=0,min = 0;
		for(;j<arr.length; j++) {
			if(arr[j] != 0) {
				min = arr[j];
				break;
			}
		}
		if(min == 0 ) return;
		for(;i<arr.length;i++) {
			if(arr[i] > 0 ) {
				if(arr[i] < min){
					min = arr[i];
				}
			}
		}
		return min;
	}


	function sumSp(o) {
		var sum = 0;
		for(var i in o) {
			if(isNaN(o[i])) {
				o[i] = 0;
			}
			sum = sum + Number(o[i]);
		}
		return sum ;
	}

	function countObjProperty(o){
	    var t = typeof o;
	    if(t == 'string'){
	            return o.length;
	    }else if(t == 'object'){
	            var n = 0;
	            for(var i in o){
	                    n++;
	            }
	            return n;
	    }
	    return false;
	}

	function countEffectBetSp(arr) {
		var count = 0,i = 0;
		for(;i<=arr.length;i++) {
			if(arr[i] > 0) count++;
		}
		return count;
	}
	
