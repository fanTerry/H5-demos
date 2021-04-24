package com.esportzoo.esport.manager.hd;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.hd.SubjectRequest;
import com.esportzoo.esport.connect.request.hd.UserContinueWayRequest;
import com.esportzoo.esport.connect.response.BaseResponse;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.hd.*;
import com.esportzoo.esport.constant.Hd101CodeConsant;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.constants.ClientType;
import com.esportzoo.esport.constants.FeatureKey;
import com.esportzoo.esport.constants.SysConfigPropertyKey;
import com.esportzoo.esport.constants.UserOperationParam;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.hd.constants.*;
import com.esportzoo.esport.hd.entity.*;
import com.esportzoo.esport.hd.gift.HdGiftServiceClient;
import com.esportzoo.esport.hd.gift.HdUserGiftServiceClient;
import com.esportzoo.esport.hd.gift.HdUserLogServiceClient;
import com.esportzoo.esport.hd.info.HdInfoServiceClient;
import com.esportzoo.esport.hd.service.HdUserShareLogService;
import com.esportzoo.esport.hd.service.HdUserShareService;
import com.esportzoo.esport.hd.subject.HdSubjectLogServiceClient;
import com.esportzoo.esport.hd.subject.HdSubjectOptionLogServiceClient;
import com.esportzoo.esport.hd.subject.HdSubjectServiceClient;
import com.esportzoo.esport.hd.vo.*;
import com.esportzoo.esport.hd.wallet.HdUserWalletLogServiceClient;
import com.esportzoo.esport.hd.wallet.HdUserWalletServiceClient;
import com.esportzoo.esport.hd.wallet.HdUserWithdrawServiceClient;
import com.esportzoo.esport.service.common.SysConfigPropertyService;
import com.esportzoo.esport.service.exception.BusinessException;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description:
 *
 * @author: Haitao.Li
 *
 * @create: 2019-09-16 16:32
 **/
@Component
public class SubjectManager {

	private static final Logger logger = LoggerFactory.getLogger(SubjectManager.class);

	@Autowired
	@Qualifier("hdInfoServiceClient")
	HdInfoServiceClient hdInfoServiceClient;

	@Autowired
	@Qualifier("hdUserLogServiceClient")
	HdUserLogServiceClient hdUserLogServiceClient;

	@Autowired
	@Qualifier("hdSubjectLogServiceClient")
	HdSubjectLogServiceClient hdSubjectLogServiceClient;

	@Autowired
	HdUserShareLogService hdUserShareLogService;

	@Autowired
	HdUserShareService hdUserShareService;

	@Autowired
	@Qualifier("hdSubjectServiceClient")
	HdSubjectServiceClient hdSubjectServiceClient;

	@Autowired
	@Qualifier("hdGiftServiceClient")
	HdGiftServiceClient hdGiftServiceClient;

	@Autowired
	@Qualifier("hdUserGiftServiceClient")
	HdUserGiftServiceClient hdUserGiftServiceClient;

	@Autowired
	@Qualifier("userConsumerServiceClient")
	UserConsumerServiceClient userConsumerServiceClient;

	@Autowired
	@Qualifier("hdUserWalletLogServiceClient")
	HdUserWalletLogServiceClient hdUserWalletLogServiceClient;

	@Autowired
	@Qualifier("hdSubjectOptionLogServiceClient")
	HdSubjectOptionLogServiceClient hdSubjectOptionLogServiceClient;
	@Autowired
	private Hd101Manager hd101Manager;

	@Autowired
	@Qualifier("taskExecutor")
	ThreadPoolTaskExecutor taskExecutor;

	@Autowired
	private SysConfigPropertyService sysConfigPropertyService;

	@Autowired
	HdUserWithdrawServiceClient hdUserWithdrawServiceClient;

	@Autowired
	HdUserWalletServiceClient hdUserWalletServiceClient;

	@Autowired
	RedisClient redisClient;

	//礼品奖项列表
	private final String SUBJECT_GIFT_LIST = "hdsubject101_gift_list";

	@Value("${environ}")
	private String environ;

	private static final Long hdCode = 101L;
	// 存放题目列表
	public static final String SUBJECT_LIST_USER = "subject_list-hdId:{hdCode}-user:{userId}-UserLog{joinId}";
	// 存放题当前答题题目
	public static final String CURRENT_SUBJECT_INFO = "current_subject_info-hdId:{hdCode}-user:{userId}-UserLog:{joinId}";
	// 当前答题的分享码
	public static final String CURR_SHARE_CODE = "curr_share_code";

	public static final String CURR_CONTINUE_MARK = "curr_continue_mark_user:{userId}_UserLog:{joinId}";
	//用户历史营收列表
	private final String SUBJECT_LIST_USER_HISTORY_INC = "subject_list_user_history_inc";

	/**
	 * 参与答题活动
	 *
	 * @param subjectRequest
	 * @return
	 */
	public CommonResponse<SubjectResponse> joinSubject(SubjectRequest subjectRequest) {
		CommonResponse<BaseResponse> ret = CommonResponse.newCommonResponse();

		try {

			String subjectJoinLimit = sysConfigPropertyService
					.getValueByCondition(SysConfigPropertyKey.HD_101_DO_SUBJECT_JOIN_LIMIT, subjectRequest.getClientType(),
							subjectRequest.getAgentId());
			if (StringUtils.isBlank(subjectJoinLimit)) {
				subjectJoinLimit = "2";//默认2次
			}
			Integer joinNum = Integer.valueOf(subjectJoinLimit);
			ModelResult<HdInfo> hdInfoModelResult = hdInfoServiceClient.queryByCode(HdCode.HD_101.getIndex());
			HdInfo hdInfo = hdInfoModelResult.getModel();
			/** 查看今天是否有免费机会 */
			HdSubjectLogQueryParam queryParam = new HdSubjectLogQueryParam();
			queryParam.setUserId(subjectRequest.getConsumerId());
			queryParam.setHdId(hdInfo.getId().longValue());
			if (subjectRequest.getClientType() == ClientType.WXXCY.getIndex()) {
				queryParam.setClientType(subjectRequest.getClientType());
			}
			ModelResult<List<HdSubjectLog>> result = hdSubjectLogServiceClient.queryToDaySubjectLog(queryParam);

			if (result != null && result.isSuccess() && result.getModel() != null) {
				List<HdSubjectLog> hdSubjectLogs = result.getModel();
				logger.info("用户：【{}】，参与答题,参与参数{}，参与次数：{}", subjectRequest.getConsumerId(), JSONObject.toJSONString(subjectRequest), joinNum);
				/** 客户端是小程序时*/
				if (subjectRequest.getClientType() == ClientType.WXXCY.getIndex()) {
					if (hdSubjectLogs.size() != 0 && hdSubjectLogs.size() < joinNum && subjectRequest.getWatchAd() != true) {
						logger.info("当天已有参与记录，看视频，再次挑战,用户：【{}】，参与答题", subjectRequest.getConsumerId());
						return CommonResponse.withResp(Hd101CodeConsant.WATCH_VOIDE_CONTIBUE_GAME, "看视频，再次挑战");
					}
					/** 当系统配置的次数<=参加的次数时*/
					else if (joinNum <= hdSubjectLogs.size()) {
						logger.info("当天已满参与，今天没有参与资格了,用户：【{}】", subjectRequest.getConsumerId());
						return CommonResponse.withResp(Hd101CodeConsant.NO_JOIN_CHANCE_TODAY, Hd101CodeConsant.NO_JOIN_CHANCE_TODAY_MESG);
					}
					/** 客户端是H5*/
				} else {
					if (hdSubjectLogs.size() == joinNum - 1) {
						logger.info("当天录已有参与记，返回支付三毛弹窗,用户：【{}】，参与答题", subjectRequest.getConsumerId());
						// 当天已有参与记录，返回支付三毛弹窗
						return CommonResponse.withResp(Hd101CodeConsant.PAY_CONTIBUE_GAME, "支付三毛，再次挑战");
					} else if (hdSubjectLogs.size() >= joinNum) {
						// 查看是否有付费
						boolean payFlag = false;
						if (null != subjectRequest.getJoinType() && subjectRequest.getJoinType().intValue() == 1) {
							logger.info("用户：【{}】，参与答题，当前是付费参与", subjectRequest.getConsumerId());
							for (HdSubjectLog hdSubjectLog : hdSubjectLogs) {
								// 单存在2记录，且是付费创建，则参与活动
								if (hdSubjectLog.getStatus().intValue() == HdSubjectLogStatus.INTO.getIndex()) {
									payFlag = true;
								}
							}
						}
						if (!payFlag) {
							return CommonResponse.withResp(Hd101CodeConsant.NO_JOIN_CHANCE_TODAY, Hd101CodeConsant.NO_JOIN_CHANCE_TODAY_MESG);
						}

					}
				}

			}

			JoinActParam param = new JoinActParam();
			param.setCustomerId(subjectRequest.getConsumerId());
			param.setHdCode(hdCode.longValue());
			param.setSellClient(subjectRequest.getClientType());
			param.setSellChannel(subjectRequest.getAgentId());
			param.setBizSystem(subjectRequest.getBiz());
			Map<String, Object> extParam = Maps.newHashMap();
			// 默认是10 ，后续改成读取后台配置
			extParam.put("subjectNum", 20);
			if (subjectRequest.getJoinType() == null) {
				subjectRequest.setJoinType(HdJoinType.HD_FREE.getIndex());
			}
			//区分免费和付费参与
			extParam.put("joinType", subjectRequest.getJoinType());
			if (subjectRequest.getJoinType() == HdJoinType.HD_PAY.getIndex()) {
				//非免费参与
				if (subjectRequest.getThirdOrderId() == null) {
					logger.error("用户：【{}】，参与答题发生异常,ThirdOrderId.is.null", subjectRequest.getConsumerId());
					return CommonResponse.withErrorResp("参与答题发生异常,ThirdOrderId.is.null");
				}
				if (subjectRequest.getSubjectLogId() == null) {
					logger.error("用户：【{}】，参与答题发生异常,SubjectLogId.is.null", subjectRequest.getConsumerId());
					return CommonResponse.withErrorResp("参与答题发生异常,SubjectLogId.is.null");
				}
				extParam.put("thirdOrderId", subjectRequest.getThirdOrderId());
				extParam.put("subjectLogId", subjectRequest.getSubjectLogId());
			}
			param.setExtParamMap(extParam);

			JoinActResult joinActResult = hdUserLogServiceClient.joinActivity(param);
			if (joinActResult != null && joinActResult.isSuccess()) {
				logger.info("用户【{}】参加答题活动", subjectRequest.getConsumerId());
				HdUserLog hdUserLog = joinActResult.getHdUserLog();

				Long subjectLogId = Long.valueOf(hdUserLog.getFeature("hdSubjectLogId"));
				List<HdSubjectVo> hdSubjectVos = getSubjectList(subjectRequest.getConsumerId(), hdUserLog.getId());
				// 将第一道题目放入第一道题目
				HdSubjectVo hdSubjectVo = hdSubjectVos.get(0);

				SubjectResponse subjectResponse = converToSubjectResponse(hdSubjectVo);
				subjectResponse.setHdUserLogId(hdUserLog.getId());
				// 设置第一道题目ID
				subjectResponse.setSubjectId(hdSubjectVo.getId());
				subjectResponse.setSubjectLogId(subjectLogId);
				return CommonResponse.withSuccessResp(subjectResponse);
			} else {
				logger.error("用户【{}】参加答题活动失败，失败原因:code:{} msg:{}", subjectRequest.getConsumerId(), joinActResult.getErrorCode(),
						joinActResult.getErrorMsg());
				return CommonResponse.withErrorResp(joinActResult.getErrorMsg());
			}
		} catch (Exception e) {
			logger.error("用户【{}】参加答题活动发生异常，异常原因:{}", subjectRequest.getConsumerId(), e);
			e.printStackTrace();
			return CommonResponse.withErrorResp("参与答题发生异常");
		}

	}

	public Hd101GiftResponse getWxxcxGomeGift(BaseRequest baseRequest) {
		ModelResult<HdInfo> hdInfoModelResult = hdInfoServiceClient.queryByCode(HdCode.HD_101.getIndex());
		HdInfo hdInfo = hdInfoModelResult.getModel();
		Hd101GiftResponse hd101GiftResponse = new Hd101GiftResponse();
		if (!hdInfoModelResult.isSuccess() || null == hdInfo) {
			logger.info("查询答题首页中奖列表出现异常 {}", hdInfoModelResult.getErrorMsg());
			return hd101GiftResponse;
		}
		HdGiftQueryVo hdGiftQueryVo = new HdGiftQueryVo();
		hdGiftQueryVo.setHdId(hdInfo.getId().intValue());
		hdGiftQueryVo.setStatus(HdGiftStatus.valid.getIndex());
		ModelResult<List<HdGift>> result = hdGiftServiceClient.queryByOption(hdGiftQueryVo);
		if (result != null && result.isSuccess() && result.getModel() != null) {
			List<HdGift> hdGiftList = result.getModel();
			// 查找头奖礼品信息
			for (HdGift hdGift : hdGiftList) {
				if (StringUtils.isNotEmpty(hdGift.getGiftProp())) {
					JSONObject giftPropJo = hdGift.getGiftPropJo();
					if (null == giftPropJo) {
						logger.info("该礼物属性为空 " + JSONObject.toJSONString(hdGift));
						continue;
					}
					Boolean fristPrize = giftPropJo.getBoolean("subjectFristPrize");
					Integer integer = giftPropJo.getInteger(FeatureKey.HD_GIFT_CLIENT_TYPE);
					if (fristPrize != null && fristPrize && null != integer && integer.intValue() == ClientType.WXXCY.getIndex()) {
						hd101GiftResponse.setHdGiftId(hdGift.getId());
						hd101GiftResponse.setGiftRemainder(hdGift.getGiftRemainder());
						hd101GiftResponse.setHdGiftName(hdGift.getGiftName());
					}
				}

			}

			List<Map> winnerList = Lists.newArrayList();
			List<AnnounceResponse> announceResponses = hd101Manager.showUserGiftInfo(null, Long.valueOf(hdInfo.getId()), baseRequest.getClientType());
			for (AnnounceResponse announceRespons : announceResponses) {
				HashMap<String, Object> map = Maps.newHashMap();
				map.put("userName", announceRespons.getUserName());
				map.put("showType", ShowType.GAININFO.getIndex());
				map.put("giftNum", announceRespons.getAmount());
				map.put("giftName", announceRespons.getGiftName());

				winnerList.add(map);
			}
			hd101GiftResponse.setGiftWinnerList(winnerList);

			hd101GiftResponse.setPlayTime(DateUtil.getDateString(new Date()));
			return hd101GiftResponse;
		} else {
			logger.warn("请添加活动【{}】礼品列表", HdCode.HD_101.getDescription());
		}
		return hd101GiftResponse;
	}

	/**
	 * 获取首页展示的头奖礼品信息
	 *
	 * @return
	 */
	public Hd101GiftResponse getHomeGift() {
		ModelResult<HdInfo> hdInfoModelResult = hdInfoServiceClient.queryByCode(HdCode.HD_101.getIndex());
		HdInfo hdInfo = hdInfoModelResult.getModel();
		Hd101GiftResponse hd101GiftResponse = new Hd101GiftResponse();
		if (!hdInfoModelResult.isSuccess() || null == hdInfo) {
			logger.info("查询答题首页中奖列表出现异常 {}", hdInfoModelResult.getErrorMsg());
			return hd101GiftResponse;
		}
		HdGiftQueryVo hdGiftQueryVo = new HdGiftQueryVo();
		hdGiftQueryVo.setHdId(hdInfo.getId().intValue());
		hdGiftQueryVo.setStatus(HdGiftStatus.valid.getIndex());
		ModelResult<List<HdGift>> result = hdGiftServiceClient.queryByOption(hdGiftQueryVo);
		if (result != null && result.isSuccess() && result.getModel() != null) {
			List<HdGift> hdGiftList = result.getModel();
			// 查找头奖礼品信息
			for (HdGift hdGift : hdGiftList) {
				if (StringUtils.isNotEmpty(hdGift.getGiftProp())) {
					JSONObject giftPropJo = hdGift.getGiftPropJo();
					Boolean fristPrize = giftPropJo.getBoolean("subjectFristPrize");
					if (fristPrize != null && fristPrize) {
						hd101GiftResponse.setHdGiftId(hdGift.getId());
						hd101GiftResponse.setGiftRemainder(hdGift.getGiftRemainder());
						hd101GiftResponse.setHdGiftName(hdGift.getGiftName());
					}
				}
			}
			List<Map> winnerList = Lists.newArrayList();
			List<AnnounceResponse> announceResponses = hd101Manager.showUserHistoryInc();
			for (AnnounceResponse announceRespons : announceResponses) {
				HashMap<String, Object> map = Maps.newHashMap();
				map.put("userName", announceRespons.getUserName());
				map.put("showType", ShowType.GAININFO.getIndex());
				map.put("giftNum", announceRespons.getAmount());
				winnerList.add(map);
			}
			hd101GiftResponse.setGiftWinnerList(winnerList);

			hd101GiftResponse.setPlayTime(DateUtil.getDateString(new Date()));
			return hd101GiftResponse;
		} else {
			logger.warn("请添加活动【{}】礼品列表", HdCode.HD_101.getDescription());
		}
		return null;

	}

	public static void main(String[] args) {
		String s = "{\"rule\":15,\"subjectFristPrize\":false,\"invalid\":\"0\",\"clientType\":\"5\"}";

		JSONObject jsonObject = JSONObject.parseObject(s);

		System.out.println(jsonObject.getInteger("clientType"));
	}

	/**
	 * 返回下一道题目,或者技术答题
	 *
	 * @param subjectRequest
	 * @return
	 */
	public CommonResponse<SubjectResponse> nextOrOverSubject(SubjectRequest subjectRequest, boolean isRight) {
		CommonResponse<SubjectResponse> commonResponse = CommonResponse.newCommonResponse();
		SubjectResponse subjectResponse = new SubjectResponse();
		try {
			ModelResult<HdSubjectLog> subjectLogModelResult = hdSubjectLogServiceClient.queryById(subjectRequest.getSubjectLogId());
			if (!subjectLogModelResult.isSuccess() || subjectLogModelResult.getModel() == null) {
				logger.error("nextSubject - 用户：【{}】，SubjectLogId 【{}】查询答题流水出错", subjectRequest.getConsumerId(), subjectRequest.getSubjectLogId());
				return CommonResponse.withErrorResp("答题校验答案异常");
			}
			UserContinueWayRequest conWay = new UserContinueWayRequest();
			conWay.setHdSubjectLogId(subjectRequest.getSubjectLogId());
			conWay.setClientType(subjectRequest.getClientType());
			// 1.判断是否可以续命-wj
			boolean canContinue = false;
			CommonResponse<List<UserContinueWayResponse>> wayResponse = hd101Manager.getContinueWay(conWay, subjectRequest.getConsumerId());
			if (null != wayResponse && StringUtils.isNotBlank(wayResponse.getCode()) && wayResponse.getCode()
					.equals(ResponseConstant.RESP_SUCC_CODE)) {
				List<UserContinueWayResponse> list = wayResponse.getData();
				for (UserContinueWayResponse way : list) {
					if (way.canActive) {
						canContinue = true;
						break;
					}
				}
			}
			logger.info("nextOrOverSubject,用户：【{}】是否可以续命 【{}】,是否可以续命响应结果:{}", subjectRequest.getConsumerId(), canContinue,
					JSONObject.toJSONString(wayResponse));
			if (!isRight && canContinue) {
				return CommonResponse.withResp(Hd101CodeConsant.CAN_CONTINUE, "答案错误,可以续命");
			}
			HdSubjectLog hdSubjectLog = subjectLogModelResult.getModel();
			HdSubjectQueryParam queryParam = new HdSubjectQueryParam();
			queryParam.setHdId(hdSubjectLog.getHdId().longValue());
			queryParam.setSubjectLogId(hdSubjectLog.getId());
			queryParam.setUserId(subjectRequest.getConsumerId());
			queryParam.setUserLogId(subjectRequest.getHdUserLogId());
			queryParam.setBizSystem(subjectRequest.getBiz());
			queryParam.setChannelNo(subjectRequest.getClientType());
			queryParam.setClientType(subjectRequest.getClientType());
			// 2.无续命机会,结束答题-wj
			if (!isRight) {

				ModelResult<SubjectCloseVo> subjectCloseVoModelResult = hdSubjectServiceClient.subjectClose(queryParam);
				SubjectCloseVo subjectCloseVo = subjectCloseVoModelResult.getModel();
				logger.info("结束答题，用户：【{}】返回结果 【{}】", subjectRequest.getConsumerId(), JSON.toJSONString(subjectCloseVo));
				// 判断当天是否可以再参与
				boolean joinFlag = canJoinSubjectHd(subjectRequest.getConsumerId(), subjectCloseVo.getNum(), subjectRequest.getClientType(),
						subjectRequest.getAgentId());
				subjectResponse.setCanJoinSubject(joinFlag);
				subjectResponse.setRightAnswer(subjectCloseVo.getAmount());
				if (subjectCloseVo.getGift() == null) {
					//a.答题结束没有礼品
					commonResponse.setData(subjectResponse);
					commonResponse.setCode(Hd101CodeConsant.GAME_OVER_BY_ANSWER_WRONG);
					commonResponse.setMessage("结束答题，没有礼品");
					return commonResponse;
				}
				/** 答案出错，结束并返回礼品 */
				commonResponse.setCode(Hd101CodeConsant.GAME_OVER_BY_ANSWER_WRONG);
				commonResponse.setMessage("恭喜答对" + subjectCloseVo.getAmount() + "所有题目");
				subjectResponse.setHdGift(subjectCloseVo.getGift());
				subjectResponse.setStartPrizeNum(subjectCloseVo.getGift().getAmount());
				subjectResponse.setRightAnswer(subjectCloseVo.getAmount());
				subjectResponse.setGameChanceNum(subjectCloseVo.getNum());
				subjectResponse.setUserGiftLogId(subjectCloseVo.getUserGiftId());

				commonResponse.setData(subjectResponse);

				return commonResponse;
			}


			/** 3. 答案正确，去获取下一道题目 */
			String subjectKey = SUBJECT_LIST_USER.replace("{hdCode}", hdCode.toString())
					.replace("{userId}", subjectRequest.getConsumerId().toString()).replace("{joinId}", subjectRequest.getHdUserLogId().toString());
			logger.info("用户题目缓存key: 【{}】", subjectKey);
			String sub = redisClient.get(subjectKey);
			if (StringUtils.isEmpty(sub)) {
				logger.info("用户：【{}】，当前题目列表已过期 ，答题流水ID【{}】", subjectRequest.getConsumerId(), subjectRequest.getSubjectLogId());
				return CommonResponse.withErrorResp("当前题目列表已过期");
			}
			List<HdSubjectVo> hdSubjectVoList = JSONArray.parseArray(sub, HdSubjectVo.class);
			//			logger.info("用户：【{}】，参数param 【{}】", subjectRequest.getConsumerId(), JSON.toJSONString(hdSubjectVoList));

			String currKey = CURRENT_SUBJECT_INFO.replace("{hdCode}", hdCode.toString())
					.replace("{userId}", subjectRequest.getConsumerId().toString()).replace("{joinId}", subjectRequest.getHdUserLogId().toString());

			String s = redisClient.get(currKey);
			if (StringUtils.isEmpty(s)) {
				logger.info("用户：【{}】，当前题目回答时间过期 ，题目ID【{}】", subjectRequest.getConsumerId(), subjectRequest.getSubjectId());
				return CommonResponse.withErrorResp("当前题目回答时间过期");
			}
			HdSubject currSubject = JSONObject.parseObject(s, HdSubject.class);

			for (int i = 0; i < hdSubjectVoList.size(); i++) {

				if (hdSubjectVoList.get(i).getId().equals(currSubject.getId())) {

					// 查找下一道题目
					int next = i + 1;
					if (next < hdSubjectVoList.size()) {

						HdSubjectVo hdSubjectVo = hdSubjectVoList.get(next);

						redisClient.set(currKey, JSONObject.toJSONString(hdSubjectVo), 11);
						subjectResponse = converToSubjectResponse(hdSubjectVo);
						subjectResponse.setHdUserLogId(subjectRequest.getHdUserLogId());
						// 设置下一道题目ID
						subjectResponse.setSubjectId(hdSubjectVo.getId());
						// 返回下一题目
						return CommonResponse.withSuccessResp(subjectResponse);
					}
				}

			}
			/** 4. 所有题目都答完，没有下一道 ，校验答对题目数是否全对 */
			Integer rightAnswers = hdSubjectLog.getRightAnswers();
			//		SysConfigProperty configPropertyByKey = sysConfigPropertyServiceClient
			//				.getSysConfigPropertyByKey(SysConfigPropertyKey.HD_101_DO_SUBJECT_NUMBER);
			//		Integer allRightNum = Integer.valueOf(configPropertyByKey.getValue());
			/** 同后台配置的正确题目数一致 ，结束答题并返回礼品 */
			logger.info("题目数【{}】", rightAnswers);
			if (rightAnswers.intValue() == 20) {
				// 该用户答对所有题目
				logger.info("用户：【{}】答对所有题目数，答题流水ID 【{}】", subjectRequest.getConsumerId(), subjectRequest.getSubjectLogId());
				ModelResult<SubjectCloseVo> subjectCloseVoModelResult = hdSubjectServiceClient.subjectClose(queryParam);
				logger.info("用户：【{}】，参数param 【{}】,调用hdSubjectServiceClient.subjectClose，返回信息：{}", subjectRequest.getConsumerId(),
						JSON.toJSONString(subjectCloseVoModelResult));
				if (!subjectCloseVoModelResult.isSuccess() || subjectCloseVoModelResult.getModel() == null) {
					logger.error("用户：【{}】，参数param 【{}】,调用hdSubjectServiceClient.subjectClose，关闭答题出错，出错信息：{}", subjectRequest.getConsumerId(),
							JSON.toJSONString(queryParam), subjectCloseVoModelResult.getErrorMsg());
					return CommonResponse.withErrorResp("答题结束出现异常");
				}
				SubjectCloseVo subjectCloseVo = subjectCloseVoModelResult.getModel();
				subjectResponse.setHdGift(subjectCloseVo.getGift());
				subjectResponse.setRightAnswer(subjectCloseVo.getAmount());
				subjectResponse.setGameChanceNum(subjectCloseVo.getNum());
				if (subjectCloseVo.getGift() == null) {
					logger.error("答对20道题，今日奖品已领取完，用户：【{}】，参数param 【{}】", subjectRequest.getConsumerId(), JSON.toJSONString(subjectCloseVo));
					commonResponse.setData(subjectResponse);
					commonResponse.setCode(Hd101CodeConsant.GAME_OVER_BY_ANSWER_WRONG);
					commonResponse.setMessage("结束答题，没有礼品");
					return commonResponse;
				} else {
					subjectResponse.setStartPrizeNum(subjectCloseVo.getGift().getAmount());
				}
				subjectResponse.setUserGiftLogId(subjectCloseVo.getUserGiftId());
				// 判断当天是否可以再参与
				boolean joinFlag = canJoinSubjectHd(subjectRequest.getConsumerId(), subjectCloseVo.getNum(), subjectRequest.getClientType(),
						subjectRequest.getAgentId());
				subjectResponse.setCanJoinSubject(joinFlag);
				/** 不一定获得头奖 */
				if (subjectCloseVo.getSubjectFristPrize()) {
					// a.用户获得今天头奖
					commonResponse.setCode(Hd101CodeConsant.GAME_WIN_BY_FIRST_PRIZE);
					commonResponse.setMessage("恭喜答对" + subjectCloseVo.getAmount() + "所有题目");

					commonResponse.setData(subjectResponse);
				} else {
					//b.用户题目全答对，没有获得头奖
					commonResponse.setCode(Hd101CodeConsant.GAME_WIN_BYNO_NO_FIRST_PRIZE);
					commonResponse.setMessage("恭喜答对" + subjectCloseVo.getAmount() + "所有题目");
					commonResponse.setData(subjectResponse);

				}

				return commonResponse;

			}

		} catch (Exception e) {
			logger.error("nextOrOverSubject-用户：【{}】，参数param 【{}】异常信息:{}", subjectRequest.getConsumerId(), JSON.toJSONString(subjectRequest), e);
			e.printStackTrace();
			return CommonResponse.withErrorResp("答题校验答案异常");

		}
		logger.error("用户：【{}】，参数param 【{}】", subjectRequest.getConsumerId(), JSON.toJSONString(subjectRequest));
		return CommonResponse.withErrorResp("答题校验答案异常");
	}

	/**
	 * 获取当前答题开始的第一道题目
	 *
	 * @param subjectRequest
	 * @return
	 */
	public CommonResponse<SubjectResponse> getFirstSubject(SubjectRequest subjectRequest) {
		ModelResult<HdSubjectLog> subjectLogModelResult = hdSubjectLogServiceClient.queryById(subjectRequest.getSubjectLogId());
		if (!subjectLogModelResult.isSuccess() || subjectLogModelResult.getModel() == null) {
			logger.error("用户：【{}】，答题记录不存在", subjectRequest.getConsumerId());
			return CommonResponse.withErrorResp("答题记录不存在");
		}
		HdSubjectLog hdSubjectLog = subjectLogModelResult.getModel();
		if (hdSubjectLog.getStatus() != HdSubjectLogStatus.INTO.getIndex()) {
			logger.error("用户：【{}】,当前答题状态非参与中,参数param 【{}】", subjectRequest.getConsumerId(), JSON.toJSONString(hdSubjectLog));
			return CommonResponse.withErrorResp("答题状态不是参与中");
		}

		List<HdSubjectVo> hdSubjectVoList = this.getSubjectList(subjectRequest.getConsumerId(), subjectRequest.getHdUserLogId());
		if (hdSubjectVoList == null) {
			return CommonResponse.withErrorResp("无法获取第一道题目，答题时间已经过期");
		}
		HdSubjectVo hdSubjectVo = hdSubjectVoList.get(0);

		SubjectResponse subjectResponse = converToSubjectResponse(hdSubjectVo);
		subjectResponse.setHdUserLogId(subjectRequest.getHdUserLogId());
		// 设置题目ID
		subjectResponse.setSubjectId(hdSubjectVo.getId());
		subjectResponse.setShareCode(hdSubjectLog.getShareCode());
		String currKey = CURRENT_SUBJECT_INFO.replace("{hdCode}", hdCode.toString()).replace("{userId}", subjectRequest.getConsumerId().toString())
				.replace("{joinId}", subjectRequest.getHdUserLogId().toString());
		// 防止网络延迟，前后端时间不同步，加多1秒时间
		redisClient.set(currKey, JSONObject.toJSONString(hdSubjectVo), 11);
		return CommonResponse.withSuccessResp(subjectResponse);
	}

	/**
	 * 结束游戏
	 * @param
	 * @param
	 * @return
	 */
	public CommonResponse<SubjectResponse> endGame(SubjectRequest subjectRequest) {

		SubjectResponse subjectResponse = new SubjectResponse();
		logger.info("用户【{}】结束答题，参数param 【{}】", subjectRequest.getConsumerId(), JSON.toJSONString(subjectRequest));
		try {
			CommonResponse<SubjectResponse> commonResponse = CommonResponse.newCommonResponse();
			ModelResult<HdInfo> hdInfoModelResult = hdInfoServiceClient.queryByCode(HdCode.HD_101.getIndex());
			HdInfo hdInfo = hdInfoModelResult.getModel();
			HdSubjectQueryParam queryParam = new HdSubjectQueryParam();
			queryParam.setHdId(hdInfo.getId().longValue());
			queryParam.setSubjectLogId(subjectRequest.getSubjectLogId());
			queryParam.setUserId(subjectRequest.getConsumerId());
			queryParam.setUserLogId(subjectRequest.getHdUserLogId());
			queryParam.setBizSystem(subjectRequest.getBiz());
			queryParam.setChannelNo(subjectRequest.getAgentId().intValue());
			queryParam.setClientType(subjectRequest.getClientType());
			ModelResult<SubjectCloseVo> subjectCloseVoModelResult = hdSubjectServiceClient.subjectClose(queryParam);
			logger.info("结束答题，用户：【{}】返回结果 【{}】", subjectRequest.getConsumerId(), JSON.toJSONString(subjectCloseVoModelResult));
			SubjectCloseVo subjectCloseVo = subjectCloseVoModelResult.getModel();
			logger.info("用户：【{}】，结束答题参数，参数param 【{}】", subjectRequest.getConsumerId(), JSON.toJSONString(subjectCloseVo));
			/** 答案出错，结束并返回礼品 */
			commonResponse.setCode(Hd101CodeConsant.GAME_OVER_BY_ANSWER_WRONG);
			commonResponse.setMessage("恭喜答对" + subjectCloseVo.getAmount() + "题目");
			subjectResponse.setHdGift(subjectCloseVo.getGift());
			if (null != subjectCloseVo.getGift()) {
				subjectResponse.setStartPrizeNum(subjectCloseVo.getGift().getAmount());
			}
			subjectResponse.setRightAnswer(subjectCloseVo.getAmount());

			// 判断当天是否可以再参与
			if (null != subjectCloseVo.getNum()) {
				boolean joinFlag = canJoinSubjectHd(subjectRequest.getConsumerId(), subjectCloseVo.getNum(), subjectRequest.getClientType(),
						subjectRequest.getAgentId());
				subjectResponse.setGameChanceNum(subjectCloseVo.getNum());
				subjectResponse.setCanJoinSubject(joinFlag);
			}
			subjectResponse.setUserGiftLogId(subjectCloseVo.getUserGiftId());
			commonResponse.setData(subjectResponse);
			return commonResponse;

		} catch (Exception e) {
			logger.info("startGame出现异常，用户id：{},异常信息:{}", subjectRequest.getConsumerId(), e);
			e.printStackTrace();
			return CommonResponse.withResp(ResponseConstant.SYSTEM_ERROR_CODE, ResponseConstant.SYSTEM_ERROR_MESG);
		}
	}


	/**
	 * 校验用户选择的答案
	 *
	 * @param subjectRequest
	 * @return
	 */
	public boolean verifyAnswer(SubjectRequest subjectRequest) {


		try {
			ModelResult<HdSubjectLog> subjectLogModelResult = hdSubjectLogServiceClient.queryById(subjectRequest.getSubjectLogId());
			if (!subjectLogModelResult.isSuccess() || subjectLogModelResult.getModel() == null) {
				logger.info("用户：【{}】，题目答案校验失败，当前答题记录不存在  HdUserLogId【{}】", subjectRequest.getConsumerId(), subjectRequest.getHdUserLogId());
				return false;
			}
			HdSubjectLog hdSubjectLog = subjectLogModelResult.getModel();
			if (hdSubjectLog.getStatus() != HdSubjectLogStatus.INTO.getIndex()) {
				logger.info("用户：【{}】，题目答案校验失败，当前答题状态非参与中  HdSubjectLogStatus【{}】", subjectRequest.getConsumerId(), hdSubjectLog.getStatus());
				return false;
			}

			// List<HdSubjectVo> hdSubjectVoList =
			// getSubjectList(subjectRequest.getConsumerId(),
			// subjectRequest.getHdUserLogId());
			String currKey = CURRENT_SUBJECT_INFO.replace("{hdCode}", hdCode.toString())
					.replace("{userId}", subjectRequest.getConsumerId().toString()).replace("{joinId}", subjectRequest.getHdUserLogId().toString());
			String s = redisClient.get(currKey);
			if (StringUtils.isEmpty(s)) {
				logger.info("用户：【{}】，题目答案校验失败，当前答题题目时间过期 ，题目ID【{}】", subjectRequest.getConsumerId(), subjectRequest.getSubjectId());
				return false;
			}
			HdSubjectVo currHdSubjectVo = JSONObject.parseObject(s, HdSubjectVo.class);

			// 比对题目答案
			if (StringUtils.isEmpty(subjectRequest.getUserOptionIdList())) {
				return false;
			}

			logger.info("用户：【{}】，校验答案参数param 【{}】", subjectRequest.getConsumerId(), JSON.toJSONString(subjectRequest.getUserOptionIdList()));
			List<String> answerArray = Arrays.asList(subjectRequest.getUserOptionIdList().split(","));
			List<Long> objects = Lists.newArrayList();
			for (String s1 : answerArray) {
				if (StringUtils.isEmpty(s1)) {
					continue;
				}
				String num = s1.trim();
				if (!NumberUtil.isLong(num)) {
					logger.error("用户：【{}】，答案参数非法 【{}】", subjectRequest.getConsumerId(), num);
					return false;
				} else {
					objects.add(Long.valueOf(num));
				}

			}
			List<HdSubjectOption> hdSubjectOptionList = currHdSubjectVo.getHdSubjectOptions();
			for (Long userOptionId : objects) {
				for (HdSubjectOption hdSubjectOption : hdSubjectOptionList) {
					if (hdSubjectOption.getId().equals(userOptionId)) {

						Long userId = subjectRequest.getConsumerId();
//						HdSubjectOptionLogVo optionLogVo = new HdSubjectOptionLogVo();
//						optionLogVo.setSubjectLogId(subjectRequest.getSubjectLogId());
//						optionLogVo.setSubjectId(hdSubjectOption.getSubjectId());
//						ModelResult<List<HdSubjectOptionLog>> result = hdSubjectOptionLogServiceClient.queryList(optionLogVo);
//						if (result != null && result.isSuccess() && result.getModel().size() > 0) {
//							logger.info("用户：【{}】，参数param 【{}】,当前答题轮次，答题题目已存在记录", userId, JSON.toJSONString(optionLogVo));
//							continue;
//						}
						HdSubjectOptionLog hdSubjectOptionLog = new HdSubjectOptionLog();
						hdSubjectOptionLog.setSubjectLogId(subjectRequest.getSubjectLogId());
						hdSubjectOptionLog.setClientType(subjectRequest.getClientType());
						hdSubjectOptionLog.setChannelNo(subjectRequest.getAgentId().intValue());
						hdSubjectOptionLog.setBizSystem(subjectRequest.getBiz());
						hdSubjectOptionLog.setUserId(subjectRequest.getConsumerId().intValue());
						hdSubjectOptionLog.setOptionId(userOptionId.intValue());
						hdSubjectOptionLog.setOptionName(hdSubjectOption.getName());
						hdSubjectOptionLog.setSubjectName(hdSubjectOption.getSubjectName());
						hdSubjectOptionLog.setOptionId(hdSubjectOption.getId().intValue());
						hdSubjectOptionLog.setSubjectId(hdSubjectOption.getSubjectId());
						hdSubjectOptionLog.setAnswer(Joiner.on(",").join(currHdSubjectVo.getKey()));
						hdSubjectOptionLog.setStatus(HdSubjectOptionLogStatus.UNVALID.getIndex());
						hdSubjectOptionLog.setIsRight(HdSubjectOptionLogIsRight.NO.getIndex());
						if (currHdSubjectVo.getKey().contains(userOptionId)) {
							hdSubjectOptionLog.setIsRight(HdSubjectOptionLogIsRight.YES.getIndex());
						}
						hdSubjectOptionLog.setCreateTime(new Date());
						hdSubjectOptionLog.setAccount("");
						hdSubjectOptionLog.setUpdateTime(hdSubjectOptionLog.getCreateTime());
						if (HdSubjectOptionLogIsRight.NO.getIndex() == hdSubjectOptionLog.getIsRight().intValue()) {
							String prefix = Hd101Constants.SUBJECT_PAY_60S_LIMIT_KEY;

							Long subjectLogId = subjectRequest.getSubjectLogId();
							String key = prefix + userId.toString() + "_" + subjectLogId.toString();
							Date date = new Date();
							DateTime endTime = cn.hutool.core.date.DateUtil.offsetMinute(date, 1);
							boolean isSuccess = redisClient.setObj(key, 61, endTime);
							logger.info("用户【{}】 当前时间【{}】答错题目【{}】 添加缓存【key{},val{},是否添加成功：{}】", userId,
									cn.hutool.core.date.DateUtil.formatDateTime(date), subjectRequest.getSubjectId(), key, endTime, isSuccess);

						}
						ModelResult<Integer> integerModelResult = hdSubjectOptionLogServiceClient.add(hdSubjectOptionLog);
					}
				}
			}
			// 匹配所有答案正确
			if (currHdSubjectVo.getKey().containsAll(objects) && objects.containsAll(currHdSubjectVo.getKey())) {
				return true;
			}
			return false;
		} catch (NumberFormatException e) {
			logger.error("校验答案出错，用户：【{},异常信息：{}", subjectRequest.getConsumerId(), e);
			e.printStackTrace();
			return false;
		}


	}

	/**
	 * 修改题目的状态
	 *
	 * @param subjectRequest
	 */
	public HdSubjectLog updateSubjectStatusByAnswer(SubjectRequest subjectRequest, boolean isRight) {
		try {
			ModelResult<HdSubjectLog> modelResult = hdSubjectLogServiceClient.queryById(subjectRequest.getSubjectLogId());
			if (modelResult != null && modelResult.isSuccess()) {
				HdSubjectLog hdSubjectLog = modelResult.getModel();
				// 当答案正确
				if (isRight) {
					/** 增加答对题目数 */
					if (redisClient
							.setNX("rightanswers_subjectid_" + subjectRequest.getSubjectId() + "subjectidLogId_" + subjectRequest.getSubjectLogId(),
									"1", 300)) {
						HdSubjectOptionLogVo optionLogVo = new HdSubjectOptionLogVo();
						optionLogVo.setSubjectLogId(subjectRequest.getSubjectLogId());
						optionLogVo.setIsRight(HdSubjectOptionLogIsRight.YES.getIndex());
						ModelResult<List<HdSubjectOptionLog>> result = hdSubjectOptionLogServiceClient.queryList(optionLogVo);
						List<HdSubjectOptionLog> optionLogs = result.getModel();
						hdSubjectLog.setRightAnswers(optionLogs.size());
//						hdSubjectLog.setRightAnswers(hdSubjectLog.getRightAnswers() + 1);
						hdSubjectLogServiceClient.updateById(hdSubjectLog);
					}

				} else {
					/** 修改题目的状态 */
					hdSubjectLog.setErrorSubjectId(subjectRequest.getSubjectId().intValue());
					hdSubjectLog.setErrorTime(new Date());

					//判断是否可以续命
					UserContinueWayRequest conWay = new UserContinueWayRequest();
					conWay.setHdSubjectLogId(subjectRequest.getSubjectLogId());
					boolean canContinue = false;
					CommonResponse<List<UserContinueWayResponse>> wayResponse = hd101Manager.getContinueWay(conWay, subjectRequest.getConsumerId());
					if (null != wayResponse && StringUtils.isNotBlank(wayResponse.getCode()) && wayResponse.getCode()
							.equals(ResponseConstant.RESP_SUCC_CODE)) {
						List<UserContinueWayResponse> list = wayResponse.getData();
						for (UserContinueWayResponse way : list) {
							if (way.canActive) {
								canContinue = true;
								break;
							}
						}
					}
					if (canContinue) {
						hdSubjectLog.setStatus(HdSubjectLogStatus.CONTINUE.getIndex());
						/** 修改为续命状态 */
						hdSubjectLogServiceClient.updateById(hdSubjectLog);
						return hdSubjectLog;
					}
					// 不能续命,答题结束
					hdSubjectLog.setStatus(HdSubjectLogStatus.OVER.getIndex());

					/** 修改为结束 */
					hdSubjectLogServiceClient.updateById(hdSubjectLog);


				}

				return hdSubjectLog;


			} else {
				logger.error("修改题目的状态,没有查询到对应的答题流水，用户：【{}】，参数param 【{}】", subjectRequest.getConsumerId(), JSON.toJSONString(subjectRequest));
				return null;
			}
		} catch (Exception e) {
			logger.error("updateSubjectStatusByAnswer-用户：【{}】，参数param 【{}】异常信息：{}", subjectRequest.getConsumerId(), JSON.toJSONString(subjectRequest),
					e);
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 领取礼品
	 * @param
	 * @param
	 * @return
	 * @throws Exception
	 */
	public CommonResponse<SubjectResponse> getGifts(SubjectRequest subjectRequest) {

		ModelResult<HdInfo> infoModelResult = hdInfoServiceClient.queryByCode(HdCode.HD_101.getIndex());
		HdInfo hdInfo = infoModelResult.getModel();
		GetGiftParam giftParam = new GetGiftParam();

		giftParam.setBizNo(subjectRequest.getSubjectLogId() + "");
		giftParam.setBizId(subjectRequest.getUserGiftLogId());
		giftParam.setBizSystem(subjectRequest.getBiz());
		giftParam.setChannelNo(subjectRequest.getAgentId());
		giftParam.setClientType(subjectRequest.getClientType());
		giftParam.setHdId(hdInfo.getId().longValue());
		giftParam.setUserId(subjectRequest.getConsumerId());
		ModelResult<Boolean> modelResult = hdUserGiftServiceClient.getGift(giftParam);
		if (modelResult != null && modelResult.isSuccess() && modelResult.getModel()) {
			return CommonResponse.withSuccessResp("领取礼品品成功");
		} else {
			logger.error("用户：【{}】领取礼品失败，参数param 【{}】,失败原因:{}", subjectRequest.getConsumerId(), JSON.toJSONString(giftParam),
					modelResult.getErrorMsg());
			return CommonResponse.withErrorResp("增送礼品失败");
		}

	}


	private List<HdSubjectVo> getSubjectList(Long userId, Long hdUserLogId) {
		// 返回题目给
		String subjectKey = SUBJECT_LIST_USER.replace("{hdCode}", hdCode.toString()).replace("{userId}", userId.toString())
				.replace("{joinId}", hdUserLogId.toString());
		String sub = redisClient.get(subjectKey);
		if (StringUtils.isEmpty(sub)) {
			return null;
		}
		return JSONArray.parseArray(sub, HdSubjectVo.class);
	}

	private SubjectResponse converToSubjectResponse(HdSubjectVo hdSubject) {
		SubjectResponse response = new SubjectResponse();
		if (hdSubject == null) {
			return response;
		}
		response.setId(hdSubject.getId());
		response.setCategory(hdSubject.getCategory());
		response.setName(hdSubject.getName());
		response.setType(hdSubject.getType());

		List<HdSubjectOption> options = hdSubject.getHdSubjectOptions();
		List<HdSubjectOption> optionList = Lists.newArrayList();
		for (HdSubjectOption option : options) {
			HdSubjectOption subjectOption = new HdSubjectOption();
			subjectOption.setId(option.getId());
			subjectOption.setName(option.getName());
			subjectOption.setSubjectId(option.getSubjectId());
			subjectOption.setSubjectName(option.getSubjectName());
			if (CollUtil.isEmpty(hdSubject.getKey())) {
				logger.error("返回下一道题目，缺少答案，题目ID【{}】", hdSubject.getId());
				throw new BusinessException("缺少答案");
			}
			//测试使用
			if (StringUtils.isNotEmpty(environ) && environ.equals("daily")) {
				Long aLong = hdSubject.getKey().get(0);
				if (aLong != null && aLong.equals(option.getId())) {
					subjectOption.setName(option.getName() + "-答案点我");
				}
			}


			optionList.add(subjectOption);
		}

		//标记答案用户测试，9-24

		response.setHdSubjectOptions(optionList);
		return response;

	}

	public void reflushErrorSubjectCache(Long userId, Long hdUserLogId, Long errorSubjectId) {
		logger.info("刷新错题缓存开始,用户id={},hdUserLogId={},errorSubjectId={}", userId, hdUserLogId, errorSubjectId);
		try {
			String subjectKey = SUBJECT_LIST_USER.replace("{hdCode}", hdCode.toString()).replace("{userId}", userId.toString())
					.replace("{joinId}", hdUserLogId.toString());
			String sub = redisClient.get(subjectKey);
			if (StringUtils.isEmpty(sub)) {
				logger.info("用户：【{}】，当前题目列表已过期 ，答题活动流水ID【{}】", userId, hdUserLogId);
				return;
			}
			List<HdSubjectVo> hdSubjectVoList = JSONArray.parseArray(sub, HdSubjectVo.class);
			Map<Long, HdSubjectVo> map = hdSubjectVoList.stream()
					.collect(Collectors.toMap(HdSubjectVo::getId, Function.identity(), (key1, key2) -> key2));
			String currKey = CURRENT_SUBJECT_INFO.replace("{hdCode}", hdCode.toString()).replace("{userId}", userId.toString())
					.replace("{joinId}", hdUserLogId.toString());
			HdSubjectVo hdSubjectVo = map.get(errorSubjectId);
			if (null != hdSubjectVo) {
				redisClient.set(currKey, JSONObject.toJSONString(hdSubjectVo), 11);
			}
			logger.info("刷新错题缓存成功,用户id={},hdUserLogId={},errorSubjectId={}", userId, hdUserLogId, errorSubjectId);
		} catch (Exception e) {
			logger.info("刷新错题缓存失败,用户id={},hdUserLogId={},errorSubjectId={}", userId, hdUserLogId, errorSubjectId, e);
		}
	}

	public CommonResponse<UserHdWalletInfoResponse> userWithdraw(UserConsumer userConsumer, SubjectRequest subjectRequest) {

		if (userConsumer == null) {
			return CommonResponse.withErrorResp("用户信息为空");
		}
		ModelResult<HdUserWallet> walletModelResult = hdUserWalletServiceClient.queryByUserId(userConsumer.getId());
		if (!walletModelResult.isSuccess() || walletModelResult.getModel() == null) {
			logger.error("用户提现失败：【{}】，查询不到用户钱包", userConsumer.getId());
			return CommonResponse.withErrorResp("查询不到用户钱包");
		}
		HdUserWallet hdUserWallet = walletModelResult.getModel();

		String configValueByKey = sysConfigPropertyService
				.getValueByCondition(SysConfigPropertyKey.HD101_USER_WITHDRAW_MONEY_MAX, subjectRequest.getClientType(), subjectRequest.getAgentId());
		if (StringUtils.isEmpty(configValueByKey)) {
			logger.error("钱包限额获取不到，用户：【{}】", userConsumer.getId());
			return CommonResponse.withErrorResp("钱包限额获取不到");
		}
		BigDecimal limitMoney = new BigDecimal(configValueByKey);
		BigDecimal userMoney = hdUserWallet.getAmount();
		BigDecimal withdrawMoney;

		//查询今天可提现金额
		HdUserWithdrawQueryVo queryVo = new HdUserWithdrawQueryVo();
		queryVo.setUserId(userConsumer.getId());
		queryVo.setEndTime(DateUtil.getTodayEndTime());
		queryVo.setStartTime(DateUtil.getTodayStartTime());
		ModelResult<List<HdUserWithdraw>> todayByCondition = hdUserWithdrawServiceClient.queryTodayByCondition(queryVo, true);
		if (todayByCondition != null && todayByCondition.isSuccess() && todayByCondition.getModel() != null) {
			if (todayByCondition.getModel().size() > 0) {
				CommonResponse<UserHdWalletInfoResponse> commonResponse = new CommonResponse<>();
				commonResponse.setCode("1999");
				commonResponse.setMessage("当天已经提现过");
				return commonResponse;
			}
		}

		if (userMoney.compareTo(limitMoney) == 1) {
			//钱包额度大于限制金额，只提现限制金额
			withdrawMoney = limitMoney;
		} else {
			//提现所有余额
			withdrawMoney = userMoney;
		}
		if (withdrawMoney.doubleValue() == 0.00) {
			logger.info("用户：【{}】，钱包提现余额为0 ", userConsumer.getId());
			return CommonResponse.withErrorResp("可提现金额0");
		}
		HdUserWithdrawQueryVo withdrawQueryVo = new HdUserWithdrawQueryVo();
		withdrawQueryVo.setUserId(userConsumer.getId());
		withdrawQueryVo.setAccount(userConsumer.getAccount());
		withdrawQueryVo.setAmount(withdrawMoney);
		withdrawQueryVo.setStatus(HdUserWithdrawStatus.CREATED.getIndex());
		withdrawQueryVo.setBizSystem(subjectRequest.getBiz());
		ModelResult<HdInfo> modelResult = hdInfoServiceClient.queryByCode(HdCode.HD_101.getIndex());
		HdInfo hdInfo = modelResult.getModel();
		withdrawQueryVo.setHdId(hdInfo.getId().longValue());
		UserOperationParam userOperationParam = new UserOperationParam();
		userOperationParam.setOpRemark("活动提现");
		userOperationParam.setSellChannel(subjectRequest.getAgentId());
		userOperationParam.setClientType(subjectRequest.getClientType());
		userOperationParam.setBizSystem(subjectRequest.getBiz());
		ModelResult<Integer> userWithdraw = hdUserWithdrawServiceClient.addHdUserWithdraw(withdrawQueryVo, userOperationParam);
		if (!userWithdraw.isSuccess() || userWithdraw.getModel() == null) {
			logger.error("用户提现失败：【{}】，失败金额：{} , 参数param 【{}】异常信息：{}", userConsumer.getId(), withdrawMoney.doubleValue(),
					JSON.toJSONString(withdrawQueryVo), userWithdraw.getErrorMsg());
			return CommonResponse.withErrorResp("用户提现失败");
		}
		UserHdWalletInfoResponse walletInfoResponse = new UserHdWalletInfoResponse();
		walletInfoResponse.setTodayGetMoney(withdrawMoney.doubleValue());
		return CommonResponse.withSuccessResp(walletInfoResponse);


	}

	// 查询是否可以参与答题(领取并支付有用到)
	public boolean canJoinSubjectHd(Long userId, Integer curJoinTimes, Integer clientType, Long channelNo) {
		String subjectJoinLimit = sysConfigPropertyService
				.getValueByCondition(SysConfigPropertyKey.HD_101_DO_SUBJECT_JOIN_LIMIT, clientType, channelNo);
		if (StringUtils.isBlank(subjectJoinLimit)) {
			subjectJoinLimit = "2";// 默认2次
		}
		logger.info("查询是否可以参与答题,用户id：{},当前用户参与次数:{},系统参数设置次数:{}", userId, curJoinTimes, subjectJoinLimit);
		Integer joinNum = Integer.valueOf(subjectJoinLimit);
		if (null != curJoinTimes) {
			if (curJoinTimes.intValue() < joinNum.intValue()) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public CommonResponse<UserHdWalletInfoResponse> checkWithdraw(UserConsumer userConsumer, SubjectRequest subjectRequest) {

		if (userConsumer == null) {
			return CommonResponse.withErrorResp("用户信息为空");
		}
		ModelResult<HdUserWallet> walletModelResult = hdUserWalletServiceClient.queryByUserId(userConsumer.getId());
		if (!walletModelResult.isSuccess() || walletModelResult.getModel() == null) {
			logger.error("查询用户提现失败：【{}】，查询不到用户钱包", userConsumer.getId());
			return CommonResponse.withErrorResp("查询不到用户钱包");
		}
		HdUserWallet hdUserWallet = walletModelResult.getModel();

		String configValueByKey = sysConfigPropertyService
				.getValueByCondition(SysConfigPropertyKey.HD101_USER_WITHDRAW_MONEY_MAX, subjectRequest.getClientType(), subjectRequest.getAgentId());
		if (StringUtils.isEmpty(configValueByKey)) {
			logger.error("钱包限额获取不到，用户：【{}】", userConsumer.getId());
			return CommonResponse.withErrorResp("钱包限额获取不到");
		}
		BigDecimal limitMoney = new BigDecimal(configValueByKey);
		BigDecimal userMoney = hdUserWallet.getAmount();
		BigDecimal withdrawMoney;

		//查询今天可提现金额
		HdUserWithdrawQueryVo queryVo = new HdUserWithdrawQueryVo();
		queryVo.setUserId(userConsumer.getId());
		queryVo.setEndTime(DateUtil.getTodayEndTime());
		queryVo.setStartTime(DateUtil.getTodayStartTime());
		ModelResult<List<HdUserWithdraw>> todayByCondition = hdUserWithdrawServiceClient.queryTodayByCondition(queryVo, true);
		if (todayByCondition != null && todayByCondition.isSuccess() && todayByCondition.getModel() != null) {
			if (todayByCondition.getModel().size() > 0) {
				CommonResponse<UserHdWalletInfoResponse> commonResponse = new CommonResponse<>();
				commonResponse.setCode("1999");
				commonResponse.setMessage("当天已经提现过");
				return commonResponse;
			}
		}

		if (userMoney.compareTo(limitMoney) == 1) {
			//钱包额度大于限制金额，只提现限制金额
			withdrawMoney = limitMoney;
		} else {
			//提现所有余额
			withdrawMoney = userMoney;
		}
		//满3毛才可以提现
		if (withdrawMoney.compareTo(new BigDecimal("0.3")) == -1) {
			CommonResponse<UserHdWalletInfoResponse> commonResponse = new CommonResponse<>();
			commonResponse.setCode("1998");
			commonResponse.setMessage("满3毛才可提现");
			return commonResponse;
		}
		//返回可提现金额
		UserHdWalletInfoResponse walletInfoResponse = new UserHdWalletInfoResponse();
		walletInfoResponse.setTodayGetMoney(withdrawMoney.doubleValue());
		return CommonResponse.withSuccessResp(walletInfoResponse);


	}

	public List<Hd101GiftResponse> getGift() {
		String logPrefix = "查询答题礼物列表_";
		List<Hd101GiftResponse> giftList = new ArrayList<>();
		try {
			if (!redisClient.exists(SUBJECT_GIFT_LIST)) {
				ModelResult<HdInfo> hdInfoModelResult = hdInfoServiceClient.queryByCode(HdCode.HD_101.getIndex());
				HdInfo hdInfo = hdInfoModelResult.getModel();
				if (hdInfoModelResult.isSuccess() && null != hdInfo) {
					HdGiftQueryVo hdGiftQueryVo = new HdGiftQueryVo();
					hdGiftQueryVo.setHdId(hdInfo.getId().intValue());
					hdGiftQueryVo.setStatus(HdGiftStatus.valid.getIndex());
					ModelResult<List<HdGift>> result = hdGiftServiceClient.queryByOption(hdGiftQueryVo);
					logger.info(logPrefix + "返回的结果{}： " + JSONObject.toJSONString(result.getModel().size()));
					if (result != null && result.isSuccess() && result.getModel() != null) {
						List<HdGift> hdGiftList = result.getModel();
						//剔除无效数据，按金额降序排序
						List<HdGift> lists = hdGiftList.stream()
								.filter(s -> (s.getGiftPropJo() != null && s.getGiftPropJo().getInteger(FeatureKey.HD_GIFT_CLIENT_TYPE) != null
										&& s.getGiftPropJo().getInteger(FeatureKey.HD_GIFT_CLIENT_TYPE) == ClientType.WXXCY.getIndex()))
								.sorted(Comparator.comparing(HdGift::getAmount).reversed()).collect(Collectors.toList());
						logger.info(logPrefix + "过滤后的结果：lists{} " + JSONObject.toJSONString(lists.size()));
						// 礼品信息
						for (int i = 0; i < lists.size(); i++) {
							Hd101GiftResponse hd101GiftResponse = new Hd101GiftResponse();
							hd101GiftResponse.setHdGiftId(lists.get(i).getId());
							hd101GiftResponse.setGiftRemainder(lists.get(i).getGiftRemainder());
							hd101GiftResponse.setHdGiftName(lists.get(i).getGiftName());
							hd101GiftResponse.setGiftDesc(lists.get(i).getGiftDesc());
							if (i > 0) {
								Integer startRule = lists.get(i).getGiftPropJo().getInteger("rule");
								Integer endRule = lists.get(i - 1).getGiftPropJo().getInteger("rule");
								if (startRule - endRule < -1) {
									hd101GiftResponse.setNeedAsweredNum(startRule + "~" + (endRule - 1));
								} else {
									hd101GiftResponse.setNeedAsweredNum(startRule + "");
								}
								giftList.add(hd101GiftResponse);
							}
						}
						if (!CollectionUtil.isEmpty(giftList)) {
							redisClient.setObj(SUBJECT_GIFT_LIST, 180, giftList);
						}
					}
				}
			} else {
				giftList.addAll(redisClient.getObj(SUBJECT_GIFT_LIST));
			}
			logger.info(logPrefix + "giftList{} " + JSONObject.toJSONString(giftList.size()));
			return giftList;
		} catch (Exception e) {
			logger.info(logPrefix + "礼物列表信息出现异常【{}】", e.getMessage(), e);
			return null;
		}
	}
}
