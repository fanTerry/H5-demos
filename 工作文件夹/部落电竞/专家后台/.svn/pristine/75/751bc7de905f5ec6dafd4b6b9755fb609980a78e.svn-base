$(function(){
	$(document).on("click", "#jczq_2c1_table td.Jq_sp p:even" ,function() {
		var _this = $(this);
		checkZqCommon2(_this,2, 4071);
	});
	
	$(document).on("click", "#jczq_2c1_table td.Jq_sp p:odd" ,function() {
		var _this = $(this);
		checkZqCommon2(_this,2, 4076);
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

var zqCommonData = {};
var spData = {
	zqDoubleSp : GLOBAL_ZQ_DOUBLE_SP == undefined ? 200 : Number(GLOBAL_ZQ_DOUBLE_SP*100),
	zqCgProfitRatio : G_ZQ_2C1_PROFIT_RATE == undefined ? 120 : Number(G_ZQ_2C1_PROFIT_RATE*100),
};
function checkZqCommon2(bindObj, isHh, zqDgFlag) {
	var publishType = $("input[name='publishType']:checked").val();
	if(publishType == 2) {
		jiedu.dialog.alert("临场解读无须选择比赛和结果，在预设时间发布推荐结果即可！");
		return;
	}
	var gameType,sp,gameId,matchid,dataIndex;
	if(zqDgFlag == 4071) {
		gameId = 4071;
	}else if(zqDgFlag == 4076) {
		gameId = 4076;
	} 
	dataIndex = bindObj.data("index");
	if(gameId == 4071) {
		gameType = "spf";
	} else if(gameId == 4076) {
		gameType = "rqspf";
	} 
	sp = bindObj.data("sp");
	matchId = bindObj.parents(".Jq_line_match").data("fxid");
	
	if(sp == "-") {
		jiedu.dialog.alert("目前赛事不支持推荐。");
		return;
	} else if(sp.split("-").length == 2) {
		sp = sp.split("-")[1];
	}
	
	// 本场赛事第一次选取
	if(!zqCommonData[matchId]) { 
		var typeObj = {'spf': [0,0,0], 'rqspf': [0,0,0]};
		typeObj[gameType][dataIndex] = sp; //为数组指定位置上存值
		zqCommonData[matchId] = typeObj;
		if(countObjProperty(zqCommonData) >= 3) {
			zqCommonData[matchId][gameType][dataIndex] = 0;
			delete zqCommonData[matchId];
			jiedu.dialog.alert("只能选取2场比赛");
			return;
		}
		predictMinPrize(zqCommonData);
		bindObj.toggleClass("bgp_sp2");
		return;
	}
	
	if(zqCommonData[matchId][gameType][dataIndex] == sp) {
		zqCommonData[matchId][gameType][dataIndex] = 0;
		var spfSum = sumSp(zqCommonData[matchId].spf);
		var rqspfSum =  sumSp(zqCommonData[matchId].rqspf);
		var spSum = Number(spfSum) + Number(rqspfSum);
		if(spSum == 0) {
			delete zqCommonData[matchId];
		}
		predictMinPrize(zqCommonData);
		bindObj.toggleClass("bgp_sp2");
		return;
	}
	zqCommonData[matchId][gameType][dataIndex] = sp;
	
	//足彩单项判断 start
	if(gameType == 'spf') {
		if(getSpCount(zqCommonData[matchId].spf)== 3) {
			zqCommonData[matchId][gameType][dataIndex] = 0;
			jiedu.dialog.alert("一场比赛推荐选项不能同时全选");
			return;
		}
	}
	if(gameType == 'rqspf') {
		if(getSpCount(zqCommonData[matchId].rqspf) == 3) {
			zqCommonData[matchId][gameType][dataIndex] = 0;
			jiedu.dialog.alert("一场比赛推荐选项不能同时全选");
			return;
		}
	}
	predictMinPrize(zqCommonData);
	
	bindObj.toggleClass("bgp_sp2");
	
}


function predictMinPrize(zqCommonDataObj) {
	//选2场
	if(countObjProperty(zqCommonDataObj) <= 1) {
		$("#jq_prize_rate").text(0);
		return;
	}
	var minArray = [],countArray = [];
	for(var pro in zqCommonDataObj) {
		 //每一场 对应到编号
		minArray.push(minOfMatch(zqCommonDataObj[pro]));
		countArray.push(zhuCount(zqCommonDataObj[pro]));
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
function zhuCount(matchData) {
	var spf = matchData.spf, rqspf = matchData.rqspf;
	return getSpCount(spf)+getSpCount(rqspf);
}

//取每一场比赛里面最小值 非零
function minOfMatch(matchData) {
	var spf = matchData.spf, rqspf = matchData.rqspf,min;
	if(sumSp(spf) != 0) {
		min = minOfArray(spf);
	}
	if(sumSp(rqspf) != 0) {
		var rqMin =minOfArray(rqspf);
		min = (min < rqMin ? min : rqMin);
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

function getSpCount(arr) {
	var count = 0,i = 0;
	for(;i<=arr.length;i++) {
		if(arr[i] > 0) count++;
	}
	return count;
}


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
	JJ_LC_DXF		: 4064		//竞彩蓝球-大小分
}

/**
 * 获得发布解读的所有参数
 */
publish.getPostData = function(){
	var content = $("#content").val();
	var inpterpreId = $("#jq_interp_id").val();
	var gameType;
	var betCount;
	var itemList = [];
	if(content.indexOf("退款") >=0 || content.indexOf("返还") >=0 || content.indexOf("不中") >=0) {
		jiedu.dialog.alert("发布解读的标题/摘要/内容不能包含 退款/返还/不中 字样");
		return false;
	}

	itemList = publish.extract.jczq_2c1();
	
	if(itemList.length <= 1) {
		return jiedu.dialog.alert("需要筛选2场赛事");
	}
	
	if(content.length == 0){
		return jiedu.dialog.alert("请输入解读内容");
	}else if(jiedu.getLength(content) > 60000){
		return jiedu.dialog.alert("您的解读内容超过最大长度");
	}
	
	if(!validImprovVoiceUpload())
	{
		return jiedu.dialog.alert("您还未上传语音文件,或者不启用语音内容");
	}
	
	var fixIds = itemList[0].fixId +","+itemList[1].fixId;
	var checked = false;
	console.log(fixIds);
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
	
	if(isNaN(itemList[0].betCount) || isNaN(itemList[1].betCount) || 
			itemList[0].betCount <= 0 || itemList[1].betCount <= 0) {
		alert("注数计算错误");
		return true;
	}
	if(spData.zqCgProfitRatio) {
		var _prize = $("#jq_prize_rate").text();
		var _zqcgrate = spData.zqCgProfitRatio;
		if(_prize < _zqcgrate) {
			return jiedu.dialog.alert("2串1方案最低返奖率须≥"+_zqcgrate+"%");
		}
	}
	
	betCount = itemList[0].betCount * itemList[1].betCount;
	if(itemList[0].gameType == 4071 && itemList[1].gameType == 4071) {
		gameType = publish.gameType.JJ_ZC_SPF;
	}else if(itemList[0].gameType == 4076 && itemList[1].gameType == 4076) {
		gameType = publish.gameType.JJ_ZC_RQ_SPF;
	}else {
		gameType = publish.gameType.JJ_ZC_RQ_SPF_HH;
	}
	var improvVoiceUrl = $("#improvVoiceUrl").val();
	var improvVoiceSecond = $("#improvVoiceSecond").val();
	
	return {
		passType : 2,
		content : content,
		id : inpterpreId,
		gameType: gameType,
		betCount: betCount,
		itemList : itemList,
		improvVoiceUrl: improvVoiceUrl,
		improvVoiceSecond : improvVoiceSecond
	};
};


publish.regex = function(content){
	return content.match(/\([^\)]+\)/g);
}

publish.extract = {};
//竞彩足球-2串1
publish.extract.jczq_2c1 = function(){
	var itemList = [];
	$("#jczq_2c1_table tbody:gt(0) tr:nth-child(n+2)").each(function(){
		var matchId = $(this).data("matchid");
		var fixId = $(this).data("fxid");
		var raceId = $(this).data("raceid");
		var uniqueMatchNo = $(this).data("matchno");
		var matchNo = $(this).data("matchno");
		var leagueIndex = $(this).data("leagueindex");
		var spfSpContent = "", spfContent = "", rqspfSpContent = "", rqspfContent="", spContent="",content="",spfCount = 0, rqspfCount = 0;
		if($(this).find("td.Jq_sp p:first-child.bgp_sp2").length > 0){
			var allSp = [];
			var chsSp = [];
			$(this).find("td.Jq_sp p:first-child").each(function(){
				allSp.push($(this).data("sp"));
				if($(this).hasClass("bgp_sp2")){
					chsSp.push($(this).data("sp"));
				}
			});
			if(chsSp.length > 0) {
				spfCount = chsSp.length;
				spfContent = matchNo + "(" + chsSp.join(",") + "){4071}";
				spfSpContent = matchNo + "(" + allSp.join(",") + "){4071}";
			}
		}
		
		if($(this).find("td.Jq_sp p:last-child.bgp_sp2").length > 0){
			var allSp = [];
			var chsSp = [];
			$(this).find("td.Jq_sp p:last-child").each(function(){
				allSp.push($(this).data("sp"));
				if($(this).hasClass("bgp_sp2")){
					chsSp.push($(this).data("sp"));
				}
			});
			if(chsSp.length > 0) {
				var handicap = $(this).find("td.Jq_handicap p:last-child").data("handicap");
				rqspfCount = chsSp.length;
				rqspfContent = matchNo + "[" + handicap + "](" + chsSp.join(",") + "){4076}";
				rqspfSpContent = matchNo + "[" + handicap + "](" + allSp.join(",") + "){4076}";
			}
		}
		
		if(spfContent == "" && rqspfContent == "") {
			return ; //continue
		}
		if(spfContent != "" && rqspfContent != "") {
			content = spfContent+"~"+rqspfContent;
			spContent = spfSpContent+"~"+rqspfSpContent;
			gameType = publish.gameType.JJ_ZC_RQ_SPF_HH;
		}else if(spfSpContent != "") {
			spfContent = spfContent.replace("{4071}", "");
			content = spfContent;
			spContent = spfSpContent;
			gameType = publish.gameType.JJ_ZC_SPF;
		} else {
			rqspfContent = rqspfContent.replace("{4076}", "");
			content = rqspfContent;
			spContent = rqspfSpContent;
			gameType = publish.gameType.JJ_ZC_RQ_SPF;
		}
		
		var item = {
			raceId: raceId,
			matchId: matchId,
			fixId: fixId,
			uniqueMatchNo: uniqueMatchNo,
			betCount: spfCount + rqspfCount,
			gameType: gameType,
			leagueIndex: leagueIndex,
			content:  content,
			spContent: spContent
		}
			
		itemList.push(item);
	});
	
	return itemList;
};


Array.prototype.remove = function(val) { var index = this.indexOf(val); if (index > -1) { this.splice(index, 1); } };
//
//$(document).ready(function(){
//	//发布解读
//	$("#btnSubmit").click(function() {
//		if(!$("#btnAgree").is(":checked")){
//			return jiedu.dialog.alert("请阅读并同意部落(海南)电竞专家解读和推荐服务协议");
//		}
//		return webAlert({title:false, content:$("#dialog-tip")[0]});
//	});
//	$("#btnSubmitNew").click(function(){
//		var _this = this;
//		if($(_this).data('isLock')){return;};
//		$(_this).data('isLock', 1);
//		setTimeout(function(){
//			$(_this).data('isLock', 0);
//		}, 15000); 
//		
//		var data = publish.getPostData();
//		//console.log(JSON.stringify(data));
//		if($.type(data) === "object"){
//			console.log(JSON.stringify(data));
//			$.ajax({
//				contentType : 'application/json',
//				type: "POST",
//	         	dataType: "json",
//				url: "/improv/publishWithItem",
//				data: JSON.stringify(data),
//				success: function(json){
//					$(_this).data('isLock', 0);
//					
//					if(json.isSuccess){
//						var uri = "";
//						if(!json.model) {
//							uri = "/interpretation";
//						} else {
//							uri = "/interpretation/show/" + json.model;
//						}
//						return jiedu.dialog.success(uri);
//					}else if(json.code == "101"){
//						return alert(json.msg);
//					}else if(json.code == "-1"){
//						return jiedu.dialog.error();
//					}else{
//						return jiedu.dialog.alert(json.msg);
//					}
//				}
//			});
//		} 
//	});
//	
//});
