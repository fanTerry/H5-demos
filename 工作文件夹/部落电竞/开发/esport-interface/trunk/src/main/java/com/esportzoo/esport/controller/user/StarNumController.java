package com.esportzoo.esport.controller.user;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.wallet.UserWalletRecServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.quiz.QuizUserInfoResponse;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.UserWalletRec;
import com.esportzoo.esport.manager.UserManager;
import com.esportzoo.esport.service.wallet.UserWalletLogRecService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * 查询用户星星数量
 * 
 * @Author lixisheng
 * @Date 2019/10/19 16:24
 */
@Controller
@RequestMapping("starNum")
public class StarNumController extends BaseController {

	private transient final Logger logger = LoggerFactory.getLogger(getClass());
	public static final String logPrefix = "查询用户星星的相关接口-";

	@Autowired
	private UserWalletRecServiceClient userWalletRecServiceClient;

	@Autowired
	private UserManager userManager;
	@Autowired
	private UserWalletLogRecService userWalletLogRecService;
	@Autowired
	RedisClient redisClient;
	public static final String SIGN_FIRST_BY_DAY = "sign_first_by_day_";
	public static final String EXCHANGE_SCORE_NUM_KEY = "exchange_score_num_key_{}";
	/**
	 * 查询用户星星数量
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryStarNum", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse queryStarNum(HttpServletRequest request, BaseRequest baseRequest) {
		QuizUserInfoResponse quizUserInfoResponse = new QuizUserInfoResponse();
		UserConsumer userConsumer = getLoginUsr(request);
		try {
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("用户未登录");
			}
			ModelResult<UserWalletRec> userWalletRecModelResult = userWalletRecServiceClient.queryWalletByUserId(userConsumer.getId());
			if (userWalletRecModelResult == null || !userWalletRecModelResult.isSuccess() || userWalletRecModelResult.getModel() == null) {
				logger.info(logPrefix + "用户id={},获取查询用户星星数量异常:{}", userConsumer.getId(), JSONObject.toJSONString(userWalletRecModelResult));
				return CommonResponse.withErrorResp("获取查询用户星星数量异常");
			}
			UserWalletRec userWalletRec = userWalletRecModelResult.getModel();
			quizUserInfoResponse.setRecScore(userWalletRec.getRecScore());
			quizUserInfoResponse.setGiftRecScore(userWalletRec.getGiftRecScore());
			quizUserInfoResponse.setAbleRecScore(userWalletRec.getAbleRecScore());
			// 更新用户昵称和头像缓存
			quizUserInfoResponse.setNickName(userConsumer.getNickName());
			quizUserInfoResponse.setIcon(userConsumer.getIcon());

			return CommonResponse.withSuccessResp(quizUserInfoResponse);

		} catch (Exception e) {
			logger.error("查询用户星星出现异常，用户id:{},异常信息:{}", userConsumer.getId(), e.getMessage());
			return CommonResponse.withErrorResp("查询用户星星失败");
		}
	}

	@RequestMapping(value = "/queryWinPrize", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse queryWinPrizeByLoginTime(HttpServletRequest request, BaseRequest baseRequest) {
		QuizUserInfoResponse quizUserInfoResponse = new QuizUserInfoResponse();
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("用户未登录");
			}
			quizUserInfoResponse = userManager.queryWinPrizeByLoginTime(userConsumer.getId(), baseRequest.getAgentId().intValue());
			// 查询签到是否今天有弹出过
			if (StringUtils.isEmpty(redisClient.get(SIGN_FIRST_BY_DAY + userConsumer.getId()))) {
				quizUserInfoResponse.setSignFirstByDay(true);
				redisClient.set(SIGN_FIRST_BY_DAY + userConsumer.getId(), "1", DateUtil.getCurrentDayRemainderSeconds());
			} else {
				quizUserInfoResponse.setSignFirstByDay(false);
			}
		} catch (Exception e) {
			logger.info("中奖弹窗 发生异常[{}]", e.getMessage(), e);
		}

		return CommonResponse.withSuccessResp(quizUserInfoResponse);
	}

	@RequestMapping(value = "/queryExchangeScore", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<BigDecimal> queryExchangeScore(HttpServletRequest request, BaseRequest baseRequest) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("用户未登录");
			}
			BigDecimal remainderAward = null;
			String key = StrUtil.format(EXCHANGE_SCORE_NUM_KEY, userConsumer.getId());
			remainderAward = redisClient.getObj(key);
			if (remainderAward != null) {
				logger.info(logPrefix + "用户id={},查询用户可以兑换星星数量[命中缓存]:{}", userConsumer.getId(), remainderAward);
				return CommonResponse.withSuccessResp(remainderAward);
			}

			ModelResult<BigDecimal> modelResult = userWalletLogRecService.getRemainderAwardAmount(userConsumer.getId());
			if (modelResult != null && modelResult.isSuccess() && modelResult.getModel() != null) {
				remainderAward = modelResult.getModel();
				redisClient.setObj(key, 1 * 60, modelResult.getModel());
				logger.info(logPrefix + "用户id={},查询用户可以兑换星星数量[从数据库取]:{}", userConsumer.getId(), remainderAward);
				return CommonResponse.withSuccessResp(remainderAward);
			}
		} catch (Exception e) {
			logger.info("查询可兑换积分余额发生异常[{}]", e.getMessage(), e);
		}
		return CommonResponse.withErrorResp("查询兑换余额发生异常");
	}
}
