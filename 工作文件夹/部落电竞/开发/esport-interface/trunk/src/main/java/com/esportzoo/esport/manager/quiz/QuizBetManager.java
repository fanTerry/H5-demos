package com.esportzoo.esport.manager.quiz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.esport.client.service.common.SysConfigPropertyServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.quiz.QuizSubmitBetRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.quiz.QuizSubmitBetResponse;
import com.esportzoo.esport.constants.EsportPayway;
import com.esportzoo.esport.constants.SysConfigPropertyKey;
import com.esportzoo.esport.constants.quiz.QuizAcceptBetterSpType;
import com.esportzoo.esport.constants.quiz.QuizPayStatus;
import com.esportzoo.esport.constants.quiz.QuizPlanStatus;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.quiz.QuizOrder;
import com.esportzoo.esport.domain.quiz.QuizPlan;
import com.esportzoo.esport.manager.hd.Hd104Manager;
import com.esportzoo.esport.service.exception.errorcode.QuizOrderErrorTable;
import com.esportzoo.esport.service.quiz.QuizOrderService;
import com.esportzoo.esport.service.quiz.QuizPlanService;
import com.esportzoo.esport.vo.quiz.QuizOrderQueryVo;
import com.esportzoo.esport.vo.quiz.SubmitOrderParam;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 竞猜投注相关:主要处理竞猜投注和投注记录
 * @author jing.wu
 * @version 创建时间：2019年10月24日 下午4:12:58
 */
@Component
public class QuizBetManager {
	private transient final Logger logger = LoggerFactory.getLogger(getClass());
	public static final String logPrefix = "竞猜投注相关";
	@Autowired
	private QuizOrderService quizOrderService;

	@Autowired
	private SysConfigPropertyServiceClient sysConfigPropertyServiceClient;

	@Autowired
	private Hd104Manager hd104Manager;

	@Autowired
	private RedisClient redisClient;
	@Autowired
	private QuizPlanService quizPlanService;

	public static final String FIRST_GUESS_KEY = "esport_first_time_guess_key";
	private static final String DUPLICATION_CHECK = "esport_interface_duplication_userid{}_matchgameid{}";

	public CommonResponse<QuizSubmitBetResponse> doBet(HttpServletRequest request, QuizSubmitBetRequest quizDoBetRequest, UserConsumer userConsumer) {//渠道改为用户注册时渠道
		logger.info(logPrefix + "用户id={},立即竞猜接口参数:{}", userConsumer.getId(), JSONObject.toJSONString(quizDoBetRequest));
		//防重点击
		String key = StrUtil.format(DUPLICATION_CHECK, userConsumer.getId(), quizDoBetRequest.getMatchGameId());
		if (!redisClient.setNX(key, "1", 3)) {
			logger.error("3秒内只能提交一次投注，用户ID【{}】,MatchGameId【{}】", userConsumer.getId(), quizDoBetRequest.getMatchGameId());
			return CommonResponse.withErrorResp("请勿要重复提交");
		}
		try {


			// 校验请求参数
			if (null == quizDoBetRequest || StringUtils.isBlank(quizDoBetRequest.getMatchNo()) || null == quizDoBetRequest.getPlayNo()
					|| null == quizDoBetRequest.getBetSp() || null == quizDoBetRequest.getUserBetNum()) {
				logger.info(logPrefix + "用户id={},必要参数为空,传入接口参数:{}", userConsumer.getId(), JSONObject.toJSONString(quizDoBetRequest));
				return CommonResponse.withErrorResp("必要参数为空");
			}

			SubmitOrderParam submitOrderParam = new SubmitOrderParam();
			submitOrderParam.setUserId(userConsumer.getId());
			submitOrderParam.setMatchGameId(quizDoBetRequest.getMatchGameId());
			submitOrderParam.setMatchNo(quizDoBetRequest.getMatchNo());
			submitOrderParam.setPlayNo(quizDoBetRequest.getPlayNo());
			submitOrderParam.setAmount(quizDoBetRequest.getUserBetNum());
			submitOrderParam.setOption(quizDoBetRequest.getOptIndex());
//			submitOrderParam.setOdds(quizDoBetRequest.getBetSp()); wj0619不需要传,改成从数据库查
			submitOrderParam.setPayType(EsportPayway.REC_PAY.getIndex());
			submitOrderParam.setBizSystem(quizDoBetRequest.getBiz());
			submitOrderParam.setClientType(quizDoBetRequest.getClientType());
			submitOrderParam.setChannelNo(userConsumer.getChannelNo().intValue());
			submitOrderParam.setAcceptBetterSp(QuizAcceptBetterSpType.TRUE.getIndex());

			ModelResult<Long> modelResult = quizOrderService.submitOrder(submitOrderParam);
			logger.info(logPrefix + "用户id={},立即竞猜接口参数:{},调用submitOrder接口返回值:{}", userConsumer.getId(), JSONObject.toJSONString(quizDoBetRequest),
					JSONObject.toJSONString(modelResult));
			if (null == modelResult) {
				return CommonResponse.withErrorResp("调用接口异常");
			}
			if (!modelResult.isSuccess()) {
				return CommonResponse.withResp(modelResult.getErrorCode(), modelResult.getErrorMsg());
			}
			QuizSubmitBetResponse response = new QuizSubmitBetResponse();
			response.setBetOrderId(modelResult.getModel());
			//根据quiz_orderId 查询quiz_plan 状态
			ModelResult<List<QuizPlan>> quizPlanModelResult = quizPlanService.queryByOrderIds(Arrays.asList(modelResult.getModel()));
			List<QuizPlan> quizPlans = quizPlanModelResult.getModel();
			if (!quizPlanModelResult.isSuccess() || null == quizPlans) {
				logger.info("处理推单逻辑manager,参数orderId:{},对应quizPlan为空", modelResult.getModel());
				return CommonResponse.withErrorResp("投注查询方案异常");
			}
			if (CollectionUtil.isNotEmpty(quizPlans)) {
				QuizPlan quizPlan = quizPlans.get(0);
				response.setQuizPlanStatus(quizPlan.getStatus());
			}
			/** 首次投注，弹出公众号扫码标记弹出 */
			if (StringUtils.isEmpty(redisClient.get(FIRST_GUESS_KEY + userConsumer.getId()))) {
				QuizOrderQueryVo quizOrderQueryVo = new QuizOrderQueryVo();
				quizOrderQueryVo.setUserId(userConsumer.getId());
				quizOrderQueryVo.setPayStatus(QuizPayStatus.PAY_SUCCESS.getIndex());
				//quizOrderQueryVo.setStartCreateTime(DateUtil.getTodayStartTime());
				ModelResult<List<QuizOrder>> recordResult = quizOrderService.queryList(quizOrderQueryVo);
				if (recordResult != null && recordResult.isSuccess() && recordResult.getModel() != null) {
					if (recordResult.getModel().size() == 1) {
						response.setFirstGuess(true);
						redisClient.set(FIRST_GUESS_KEY + userConsumer.getId(), "1", 2 * 3600);
						//出现首次投注行为，并且携带邀请码的用户，当投注成功，检查是否参与助力送星星活动有关的
						if (StringUtils.isNotEmpty(quizDoBetRequest.getShareCode())) {
							logger.info(logPrefix+"用户首次投注：【{}】，携带邀请分享码 【{}】",userConsumer.getId(), quizDoBetRequest.getShareCode());
							hd104Manager.sendUserHdGift(userConsumer.getId(), quizDoBetRequest.getShareCode(), quizDoBetRequest);
						}

					}

				}
			}


			return CommonResponse.withSuccessResp(response);
		} catch (Exception e) {
			logger.error(logPrefix + "立即竞猜接口异常, 参数【{}】 异常【{}】", JSONObject.toJSONString(quizDoBetRequest), e.getMessage(), e);
			return CommonResponse.withErrorResp("投注异常");
		}
	}


	/** 根据余额获取投注金额列表 */
	public List<Integer> getBetNumByBalance(BaseRequest baseRequest, BigDecimal balance) {
		List<Integer> res = Lists.newArrayList();
		String configValue = sysConfigPropertyServiceClient
				.getValueByCondition(SysConfigPropertyKey.BET_NUM_LIST_AREA, baseRequest.getClientType(), baseRequest.getAgentId());
		if (StringUtils.isNotBlank(configValue)) {
			JSONObject jsonObject = JSONObject.parseObject(configValue);
			// 生成用户余额区间
			String balanceStr = jsonObject.getString("balanceList");
			List<Integer> balanceList = Arrays.asList(balanceStr.split(",")).stream().map(s -> (Integer.parseInt(s))).collect(Collectors.toList());
			List<List<Integer>> balanceAreaList = new ArrayList<>();
			for (int i = 0; i < balanceList.size(); i++) {
				List<Integer> temp = new ArrayList<>();
				if (i <= balanceList.size() - 2) {
					temp.add(balanceList.get(i));
					temp.add(balanceList.get(i + 1));
					balanceAreaList.add(temp);
				}
			}
			// 生成对应余额区间的投注列表
			String betNumStr = jsonObject.getString("betNumList");
			List<String> areaList = Arrays.asList(betNumStr.split("],")).stream().map(s -> s.trim().replace("[", "")).collect(Collectors.toList());
			List<List<Integer>> betNumAreaList = new ArrayList<>();
			for (String str : areaList) {
				List<Integer> betNumList = Arrays.asList(str.split(",")).stream().map(s -> (Integer.parseInt(s))).collect(Collectors.toList());
				betNumAreaList.add(betNumList);
			}

			//余额对应的区间,取出投注列表
			for (int i = 0; i < balanceAreaList.size(); i++) {
				int min = balanceAreaList.get(i).get(0);
				int max = balanceAreaList.get(i).get(1);
				if (balance.intValue() > min && balance.intValue() <= max) {
					res = betNumAreaList.get(i);
					break;
				}
			}
		}
		return res;
	}
}
