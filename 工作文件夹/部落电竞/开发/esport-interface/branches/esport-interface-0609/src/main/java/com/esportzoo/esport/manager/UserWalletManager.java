package com.esportzoo.esport.manager;

import cn.hutool.core.map.MapUtil;

import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.domain.result.PageResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.partner.UboxPartnerServiceClient;
import com.esportzoo.esport.client.service.wallet.UserWalletLogRecServiceClient;
import com.esportzoo.esport.client.service.wallet.UserWalletRecServiceClient;
import com.esportzoo.esport.connect.request.user.WalletLogReqeust;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.WalletLogResponse;
import com.esportzoo.esport.constants.SysConfigPropertyKey;
import com.esportzoo.esport.constants.wallet.BizType;
import com.esportzoo.esport.domain.SysConfigProperty;
import com.esportzoo.esport.domain.UserWalletLogRec;
import com.esportzoo.esport.domain.UserWalletRec;
import com.esportzoo.esport.vo.partner.UboxBalanceVo;
import com.esportzoo.esport.vo.wallet.WalletLogQueryVo;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @description: 用户钱包相关
 *
 * @author: Haitao.Li
 *
 * @create: 2019-05-20 14:57
 **/

@Component
public class UserWalletManager {

	private transient static final Logger logger = LoggerFactory.getLogger(UserWalletManager.class);

	@Autowired
	@Qualifier("userWalletLogRecServiceClient")
	UserWalletLogRecServiceClient userWalletLogServiceClient;

	@Autowired
	@Qualifier("userWalletRecServiceClient")
	UserWalletRecServiceClient userWalletServiceClient;
	
	@Autowired
	@Qualifier("uboxPartnerServiceClient")
	UboxPartnerServiceClient uboxPartnerServiceClient;
	@Autowired
	private CommonManager commonManager;
	
	public CommonResponse<JSONObject> getPageUserWalletLog(Long userId, DataPage<UserWalletLogRec> page, WalletLogReqeust walletLogReqeust) {
		if (userId == null) {
			logger.error("查询用户钱包流水,用户ID为空");
			return CommonResponse.withErrorResp("用户ID不为空");
		}
		if (page == null) {
			logger.error("查询用户钱包流水,page参数为空");
			return CommonResponse.withErrorResp("page参数为空");
		}

		JSONObject jsonObject = new JSONObject();
		try {
			/** 忽略查询钱包 */
			if (walletLogReqeust.getType() != null && walletLogReqeust.getType() == 0) {
				ModelResult<UserWalletRec> walletModelResult = userWalletServiceClient.queryWalletByUserId(userId);
				if (!walletModelResult.isSuccess()) {
					logger.error("查询用户钱包出错,异常信息：{}", walletModelResult.getErrorMsg());
					return CommonResponse.withErrorResp("查询用户钱包出错");
				}
				UserWalletRec userWallet = walletModelResult.getModel();
				jsonObject.put("balance", userWallet.getAbleRecScore());
				jsonObject.put("freezedbalance", userWallet.getFreezedRecScore());
			}

			WalletLogQueryVo queryVo = new WalletLogQueryVo();
			queryVo.setUserId(Integer.parseInt(userId.toString()));
			//按照id倒序
			queryVo.setOrderBy("id DESC");
			PageResult<UserWalletLogRec> pageResult = userWalletLogServiceClient.queryPage(queryVo, page);

			if (pageResult.isSuccess()) {
				List<UserWalletLogRec> userWalletLogs = pageResult.getPage().getDataList();
				List<WalletLogResponse> list = Lists.newArrayList();
				for (UserWalletLogRec userWalletLog : userWalletLogs) {
					WalletLogResponse walletLogResponse = new WalletLogResponse();
					walletLogResponse.setCreateTime(DateUtil.getDateString2(userWalletLog.getCreateTime()));
					walletLogResponse.setSortColum(DateUtil.getDateStringByZdGs(userWalletLog.getCreateTime(),"yyyy-MM"));
					walletLogResponse.setRecEndAbleScore(userWalletLog.getRecScoreBalance().add(userWalletLog.getGiftRecScoreBalance()).toString());
					walletLogResponse.setRecFreezedScore(null==userWalletLog.getFreezedRecScorebalance()?"0.00":userWalletLog.getFreezedRecScorebalance().toString());
					BigDecimal currentScore = userWalletLog.getRecScore().add(userWalletLog.getGiftRecScore());
					if(userWalletLog.getBizType().intValue()==BizType.FREEZE_REC_SCORE_CONSUME.getIndex()) {
						walletLogResponse.setRecHappenScore(null==userWalletLog.getFreezedRecScore()?"0.00":userWalletLog.getFreezedRecScore().toString());
					}else {
						walletLogResponse.setRecHappenScore(currentScore.toString());
					}
					//备注信息
					if(userWalletLog.getBizType().intValue()==BizType.LOSS_ADD_REC.getIndex()){
						walletLogResponse.setRemark("星星充值");
					}else{
						walletLogResponse.setRemark(userWalletLog.getRemarks());
					}
					walletLogResponse.setWalletOprType(userWalletLog.getWalletOperType());
					list.add(walletLogResponse);
				}
				/*是否有下一页*/
				jsonObject.put("hasNextPage", true);
				if (pageResult.getPage().getNextPage() == page.getPageNo()){
					jsonObject.put("hasNextPage", false);
				}

				Map<String, List<WalletLogResponse>> listMap = list.stream().collect(Collectors.groupingBy(WalletLogResponse::getSortColum));
				TreeMap<String, List<WalletLogResponse>> sortMap = MapUtil.sort(listMap);
				NavigableMap<String, List<WalletLogResponse>> map1 = sortMap.descendingMap();
				jsonObject.put("listMap",map1);
				SysConfigProperty sysConfigProperty = commonManager.getSysConfigByKey(SysConfigPropertyKey.CHARGE_SHOW_SWITCH,walletLogReqeust.getClientType(),walletLogReqeust.getAgentId());
				String value = sysConfigProperty.getValue();
				if (StringUtils.isNotBlank(value) && value.trim().equals("1")) {// 充值是关闭的
					jsonObject.put("chargeFlag", true);
				} else {
					jsonObject.put("chargeFlag", false);
				}
				return CommonResponse.withSuccessResp(jsonObject);
			}
			logger.error("查询用户钱包流水出错,异常信息：{}", pageResult.getErrorMsg());
		} catch (Exception e) {
			logger.error("查询用户钱包流水出错,异常信息：{}", e);
			return CommonResponse.withErrorResp("查询用户钱包流水出错");
		}

		return CommonResponse.withErrorResp("查询用户钱包流水出错");

	}

	// 获取友宝用户余额
	public UboxBalanceVo getUboxBalance(Long userId) {
		UboxBalanceVo uboxBalanceVo = null;
		ModelResult<UboxBalanceVo> modelResult = uboxPartnerServiceClient.queryUboxBalance(userId);
		if (modelResult.isSuccess() && null != modelResult.getModel()) {
			uboxBalanceVo = modelResult.getModel();
		}
		return uboxBalanceVo;
	}

	// 获取本站用户钱包
	public UserWalletRec getUserWalletRec(Long userId) {
		UserWalletRec userWalletRec = null;
		// 查询支付方式的余额
		ModelResult<UserWalletRec> walletModelResult = userWalletServiceClient.queryWalletByUserId(userId);
		if (walletModelResult.isSuccess() && null != walletModelResult.getModel()) {
			userWalletRec = walletModelResult.getModel();
		}
		return userWalletRec;
	}
}
