package com.esportzoo.esport.manager.hd;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.domain.result.PageResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.esport.client.service.charge.UserThirdOrderServiceClient;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.client.service.payment.PayGatewayServiceClient;
import com.esportzoo.esport.connect.request.hd.SubjectPayRequest;
import com.esportzoo.esport.connect.request.hd.UserContinueWayRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.hd.AnnounceResponse;
import com.esportzoo.esport.connect.response.hd.UserContinueWayResponse;
import com.esportzoo.esport.connect.response.hd.UserHdWalletInfoResponse;
import com.esportzoo.esport.connect.response.payment.PayOrderResponse;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.constants.*;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.charge.UserThirdOrder;
import com.esportzoo.esport.hd.constants.*;
import com.esportzoo.esport.hd.entity.*;
import com.esportzoo.esport.hd.gift.HdUserGiftServiceClient;
import com.esportzoo.esport.hd.share.HdUserShareServiceClient;
import com.esportzoo.esport.hd.subject.HdSubjectLogServiceClient;
import com.esportzoo.esport.hd.vo.HdSubjectLogVo;
import com.esportzoo.esport.hd.vo.HdUserGiftQueryVo;
import com.esportzoo.esport.hd.vo.HdUserShareQueryParam;
import com.esportzoo.esport.hd.vo.HdUserWalletLogQueryVo;
import com.esportzoo.esport.hd.vo.subject.DoSubjectParam;
import com.esportzoo.esport.hd.wallet.HdUserWalletLogServiceClient;
import com.esportzoo.esport.hd.wallet.HdUserWalletServiceClient;
import com.esportzoo.esport.hd.wallet.HdUserWithdrawServiceClient;
import com.esportzoo.esport.service.common.SysConfigPropertyService;
import com.esportzoo.esport.service.exception.BusinessException;
import com.esportzoo.esport.util.RequestUtil;
import com.esportzoo.esport.vo.ChargePayRequest;
import com.esportzoo.esport.vo.PayParam;
import com.esportzoo.esport.vo.ThirdOrderQueryVo;
import com.google.common.collect.Lists;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 答题活动manager
 * 
 * @author jing.wu
 * @version 创建时间：2019年9月20日 下午12:04:33
 */
@Component
public class Hd101Manager {

	private static final Logger logger = LoggerFactory.getLogger(Hd101Manager.class);

	@Autowired
	private HdCommonManager hdCommonManager;

	@Autowired
	private HdSubjectLogServiceClient hdSubjectLogClient;
	@Autowired
	private PayGatewayServiceClient payGatewayService;
	@Autowired
	private HdUserShareServiceClient hdUserShareClient;
	@Autowired
	private UserThirdOrderServiceClient userThirdOrderServiceClient;
	@Autowired
	private SysConfigPropertyService sysConfigPropertyService;
	@Autowired
	private UserConsumerServiceClient userConsumerServiceClient;
	@Autowired
	private HdUserGiftServiceClient hdUserGiftServiceClient;
	@Autowired
	private HdUserWalletLogServiceClient hdUserWalletLogServiceClient;
	@Autowired
	private HdUserWalletServiceClient hdUserWalletServiceClient;
	@Autowired
	private HdUserWithdrawServiceClient hdUserWithdrawServiceClient;
	@Autowired
	private RedisClient redisClient;
	private final String SUBJECTKEY = "hdsubject101_usergift_list";

	public String USERSUBJECTKEY = RedisKey.USERSUBJECTKEY;
	// 活动每个用户可提现最大值
	private final String GETMONEYKEY = "hd101_user_Withdraw_money_max";

	// 用户历史营收列表
	private final String SUBJECT_LIST_USER_HISTORY_INC = "subject_list_user_history_inc";


	// 续命支付
	public CommonResponse<PayOrderResponse> subjectPay(SubjectPayRequest subjectPayRequest, HttpServletRequest request, UserConsumer userConsumer) {
		String logPrefix = "答题付费请求_";
		try {
			logger.info(logPrefix + "请求参数subjectPayRequest={}", JSON.toJSONString(subjectPayRequest));
			if (subjectPayRequest.getBiz() == null || !BizSystem.getAllList().contains(BizSystem.valueOf(subjectPayRequest.getBiz().intValue()))) {
				return CommonResponse.withErrorResp("业务系统参数错误");
			}
			if (subjectPayRequest.getClientType() == null || !ClientType.getAllList().contains(ClientType.valueOf(subjectPayRequest.getClientType().intValue()))) {
				return CommonResponse.withErrorResp("客户端类型参数错误");
			}
			if (subjectPayRequest.getAgentId() == null) {
				return CommonResponse.withErrorResp("渠道号参数错误");
			}
			if (subjectPayRequest.getChoosedPayWay() == null || !EsportPayway.getAllList().contains(EsportPayway.valueOf(subjectPayRequest.getChoosedPayWay().intValue()))) {
				return CommonResponse.withErrorResp("支付方式参数错误");
			}
			Long hdSubjectLogId = subjectPayRequest.getHdSubjectLogId();
			if (null == hdSubjectLogId) {
				return CommonResponse.withErrorResp("必要参数为空");
			}

			ModelResult<HdSubjectLog> modelResult = hdSubjectLogClient.queryById(hdSubjectLogId);
			if (null == modelResult || !modelResult.isSuccess() || null == modelResult.getModel() || modelResult.getModel().getUserId().intValue() != userConsumer.getId().intValue()) {
				logger.info(logPrefix + "找不到当前答题记录,或者当前答题记录不属于该用户[{}]", JSONObject.toJSONString(subjectPayRequest));
				return CommonResponse.withErrorResp("答题记录异常");
			}
			HdSubjectLog hdSubjectLog = modelResult.getModel();
			int choosePayWay = subjectPayRequest.getChoosedPayWay().intValue();
			UserOperationParam userOperationParm = new UserOperationParam();
			userOperationParm.setSellChannel(new Long(subjectPayRequest.getAgentId()));
			userOperationParm.setClientType(subjectPayRequest.getClientType().intValue());
			userOperationParm.setOperIp(RequestUtil.getClientIp(request));

			PayParam payParam = new PayParam();
			payParam.setOrderId(hdSubjectLogId);
			payParam.setOrderType(ThirdOrderType.SUBJECT_CONTINUE_ORDER.getIndex());
			payParam.setPayWayIndex(choosePayWay);
			payParam.setUserId(userConsumer.getId());
			payParam.setOrderNo(hdSubjectLog.getLogNo());
			payParam.setAmount(Hd101Constants.SUBJECT_PAY_AMOUNT);
			if (StringUtils.isNotBlank(subjectPayRequest.getShareCode())) {
				Map<String, Object> extraParam = new HashMap<String, Object>();
				extraParam.put(FeatureKey.SHARE_CODE, subjectPayRequest.getShareCode());
				payParam.setExtraParam(extraParam);
			}
			ModelResult<ChargePayRequest> payModelResult = payGatewayService.requestPay(payParam, userOperationParm);
			logger.info(logPrefix + "用户id={},用户昵称={},调用答题续命支付,传入参数={},返回结果modelResult={}", userConsumer.getId(), userConsumer.getNickName(), JSON.toJSONString(payParam), JSON.toJSONString(payModelResult));
			if (payModelResult == null || !payModelResult.isSuccess() || null == payModelResult.getModel()) {
				return CommonResponse.withErrorResp("服务繁忙,请稍后再试~");
			}
			PayOrderResponse resp = new PayOrderResponse();
			BeanUtils.copyProperties(resp, payModelResult.getModel());
			resp.setSuccessFlag(true);
			resp.setChargeWay(choosePayWay);
			return CommonResponse.withSuccessResp(resp);
		} catch (Exception e) {
			logger.info(logPrefix + "发生异常,exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	// 获取续命方式
	public CommonResponse<List<UserContinueWayResponse>> getContinueWay(UserContinueWayRequest userContinueWayRequest, Long userId) {
		String logPrefix = "获取续命方式请求_";

		try {
			Long hdSubjectLogId = userContinueWayRequest.getHdSubjectLogId();
			if (null == hdSubjectLogId) {
				return CommonResponse.withErrorResp("必要参数为空");
			}
			CommonResponse<HdInfo> hdResponse = hdCommonManager.getHdBaseInfo(HdCode.HD_101.getIndex());
			if (null != hdResponse && StringUtils.isNotBlank(hdResponse.getCode()) && hdResponse.getCode().equals(ResponseConstant.RESP_SUCC_CODE)) {
				logger.info(logPrefix + "用户id={},活动101正常开启", userId);
			} else {
				logger.info(logPrefix + "用户id={},活动状态异常:{}", userId, JSONObject.toJSONString(hdResponse));
				return CommonResponse.withErrorResp("活动状态异常");
			}
			ModelResult<HdSubjectLog> modelResult = hdSubjectLogClient.queryById(hdSubjectLogId);
			if (null == modelResult || !modelResult.isSuccess() || null == modelResult.getModel() || modelResult.getModel().getUserId().intValue() != userId.intValue()) {
				logger.info(logPrefix + "找不到当前答题记录,或者当前答题记录不属于该用户[{}]", JSONObject.toJSONString(userContinueWayRequest));
				return CommonResponse.withErrorResp("答题记录异常");
			}
			List<UserContinueWayResponse> resultList = getUserWayList(userContinueWayRequest, modelResult.getModel(), hdResponse.getData().getId().longValue());
			return CommonResponse.withSuccessResp(resultList);
		} catch (Exception e) {
			logger.error(logPrefix + "获取用户续命方式异常, 参数【{}】 异常【{}】", JSONObject.toJSONString(userContinueWayRequest), e.getMessage(), e);
			return CommonResponse.withErrorResp("接口异常");
		}

	}

	// 获取续命方式列表
	public List<UserContinueWayResponse> getUserWayList(UserContinueWayRequest userContinueWayRequest, HdSubjectLog hdSubjectLog, Long hdId) {
		List<UserContinueWayResponse> resultList = new ArrayList<>();
		if (userContinueWayRequest.getClientType().intValue() == ClientType.WXXCY.getIndex()) {
			SubjectContinueWayEnum adEnum = SubjectContinueWayEnum.WATCHAD;
			SubjectContinueWayEnum shareEnum = SubjectContinueWayEnum.SHARE;
			// 是否可以看广告和分享 续命
			Long hdSubjectLogId = userContinueWayRequest.getHdSubjectLogId();
			ModelResult<HdSubjectLog> hdSubjectLogModelResult = hdSubjectLogClient.queryById(hdSubjectLogId);
			HdSubjectLog model = hdSubjectLogModelResult.getModel();
			if (!hdSubjectLogModelResult.isSuccess()||null==model){
			    logger.info("查询活动参与流水[{}] 是否可以看广告和分享续命异常 [{}]", hdSubjectLogId,hdSubjectLogModelResult.getErrorMsg());
			}else {
				String feature = model.getFeature(FeatureKey.HD_WATCH_AD);
				String share = model.getFeature(FeatureKey.HD_SHARE);
				if (StrUtil.isBlank(feature)){
					UserContinueWayResponse response = new UserContinueWayResponse(adEnum.getName(), true, adEnum.getOrderSort(), adEnum.getWayType());
					resultList.add(response);
				}
				if (StrUtil.isBlank(share)){
					UserContinueWayResponse response = new UserContinueWayResponse(shareEnum.getName(), true, shareEnum.getOrderSort(), shareEnum.getWayType());
					resultList.add(response);
				}

			}
		} else {
			HdUserShareQueryParam param = new HdUserShareQueryParam();
			param.setHdId(hdId);
			param.setShareCode(hdSubjectLog.getShareCode());
			param.setSubjectLogId(hdSubjectLog.getId());
			param.setUserId(hdSubjectLog.getUserId().longValue());
			// 是否可以分享续命
			ModelResult<Boolean> shareModelResult = hdUserShareClient.checkShareStatus(param);
			logger.info("查询是否可以分享,hdSubjectLog[{}],续命参数[{}] 返回结果 [{}]", JSONObject.toJSONString(hdSubjectLog),JSONObject.toJSONString(param), JSONObject.toJSONString(shareModelResult));
			if (null != shareModelResult && shareModelResult.isSuccess() && null != shareModelResult.getModel()) {
				SubjectContinueWayEnum shareEnum = SubjectContinueWayEnum.SHARE;
				UserContinueWayResponse response = new UserContinueWayResponse(shareEnum.getName(), shareModelResult.getModel(), shareEnum.getOrderSort(), shareEnum.getWayType());
				resultList.add(response);
			}

			// 是否可以支付续命(小程序不支持)
			ThirdOrderQueryVo queryVo = new ThirdOrderQueryVo();
			queryVo.setOrderNo(hdSubjectLog.getLogNo());
			queryVo.setOrderId(hdSubjectLog.getId());
			queryVo.setOrderType(ThirdOrderType.SUBJECT_CONTINUE_ORDER.getIndex());
			queryVo.setPayStatus(ThirdPayStatus.PAY_SUCCESS.getIndex());
			ModelResult<List<UserThirdOrder>> payModelResult = userThirdOrderServiceClient.queryListByCondition(queryVo);
			if (null != payModelResult && payModelResult.isSuccess()) {
				SubjectContinueWayEnum payEnum = SubjectContinueWayEnum.PAY;
				UserContinueWayResponse response = new UserContinueWayResponse(payEnum.getName(), true, payEnum.getOrderSort(), payEnum.getWayType());
				if (null != payModelResult.getModel() && payModelResult.getModel().size() > 0) {
					response.setCanActive(false);
				}
				resultList.add(response);
			}
		}

		return resultList;
	}

	// 答题首页参与付费
	public CommonResponse<PayOrderResponse> subjectJoinPay(SubjectPayRequest subjectPayRequest, HttpServletRequest request, UserConsumer userConsumer) {
		String logPrefix = "答题首页参与付费请求_";
		try {
			logger.info(logPrefix + "请求参数subjectPayRequest={}", JSON.toJSONString(subjectPayRequest));
			if (subjectPayRequest.getBiz() == null || !BizSystem.getAllList().contains(BizSystem.valueOf(subjectPayRequest.getBiz().intValue()))) {
				return CommonResponse.withErrorResp("业务系统参数错误");
			}
			if (subjectPayRequest.getClientType() == null || !ClientType.getAllList().contains(ClientType.valueOf(subjectPayRequest.getClientType().intValue()))) {
				return CommonResponse.withErrorResp("客户端类型参数错误");
			}
			if (subjectPayRequest.getAgentId() == null) {
				return CommonResponse.withErrorResp("渠道号参数错误");
			}
			if (subjectPayRequest.getChoosedPayWay() == null || !EsportPayway.getAllList().contains(EsportPayway.valueOf(subjectPayRequest.getChoosedPayWay().intValue()))) {
				return CommonResponse.withErrorResp("支付方式参数错误");
			}
			CommonResponse<HdInfo> hdResponse = hdCommonManager.getHdBaseInfo(HdCode.HD_101.getIndex());
			if (null != hdResponse && StringUtils.isNotBlank(hdResponse.getCode()) && hdResponse.getCode().equals(ResponseConstant.RESP_SUCC_CODE)) {
				logger.info(logPrefix + "用户id={},活动101正常开启", userConsumer.getId());
			} else {
				logger.info(logPrefix + "用户id={},活动状态异常:{}", userConsumer.getId(), JSONObject.toJSONString(hdResponse));
				return CommonResponse.withErrorResp("活动状态异常");
			}
			logger.info(logPrefix+"接收参数 用户[{}]  [{}]  ",userConsumer.getId(),JSONObject.toJSONString(subjectPayRequest));
			int choosePayWay = subjectPayRequest.getChoosedPayWay().intValue();
			UserOperationParam userOperationParm = new UserOperationParam();
			userOperationParm.setSellChannel(new Long(subjectPayRequest.getAgentId()));
			userOperationParm.setClientType(subjectPayRequest.getClientType().intValue());
			userOperationParm.setOperIp(RequestUtil.getClientIp(request));
			DoSubjectParam doSubjectParam = new DoSubjectParam();
			doSubjectParam.setUserId(userConsumer.getId());
			doSubjectParam.setHdId(hdResponse.getData().getId());
			doSubjectParam.setPayway(choosePayWay);
			if (StringUtils.isNotBlank(subjectPayRequest.getShareCode())) {
				doSubjectParam.setShareCode(subjectPayRequest.getShareCode());
			}
			ModelResult<ChargePayRequest> payModelResult = hdSubjectLogClient.doSubjectByThirdPay(doSubjectParam, userOperationParm);
			logger.info(logPrefix + "用户id={},用户昵称={},请求参数={},调用答题支付返回结果modelResult={}", userConsumer.getId(), userConsumer.getNickName(), JSON.toJSONString(doSubjectParam), JSON.toJSONString(payModelResult));
			if (payModelResult == null || !payModelResult.isSuccess() || null == payModelResult.getModel()) {
				return CommonResponse.withErrorResp("服务繁忙,请稍后再试~");
			}
			PayOrderResponse resp = new PayOrderResponse();
			BeanUtils.copyProperties(resp, payModelResult.getModel());
			resp.setSuccessFlag(true);
			return CommonResponse.withSuccessResp(resp);
		} catch (Exception e) {
			logger.info(logPrefix + "发生异常,exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	public List<AnnounceResponse> listUserGift(Long userId, Long hdId) {
		String logPrefix = "答题首页奖励榜单_";
		ArrayList<AnnounceResponse> data = new ArrayList();
		HdUserGiftQueryVo param = new HdUserGiftQueryVo();
		ArrayList<Integer> al = new ArrayList();
		al.add(HdUserGiftStatus.USED.getIndex());
		param.setStatus(al);
		param.setHdId(hdId);
		DataPage<HdUserGift> siz = new DataPage<>();
		DataPage<HdUserWalletLog> dataPage = new DataPage<>();
		dataPage.setPageSize(50);
		siz.setPageSize(50);
		try {
			if (null == userId) {
				redisClient.del(SUBJECTKEY);
				if (!redisClient.exists(SUBJECTKEY)) {
					// 2019-09-27 17:26:35 查询这个点以后的数据 这个点之前存在beta测试数据
					Long start = 1569576395044L;
					Date startTime = DateUtil.date(start).toJdkDate();
					List<AnnounceResponse> announceResponses = new ArrayList<>();
					param.setCreateStartTime(startTime);
					List<Integer> list = Lists.newArrayList(HdUserGiftStatus.USED.getIndex());
					param.setStatus(list);
					PageResult<HdUserGift> pageResult = hdUserGiftServiceClient.pageQueryHdUserGiftForAdmin(siz, param);
					/*
					 * HdUserWalletLogQueryVo hdUserWalletLogQueryVo = new
					 * HdUserWalletLogQueryVo();
					 * hdUserWalletLogQueryVo.setTransType
					 * (HdTransType.SUBJECT_HD_SHARE_INCOME.getIndex());
					 * hdUserWalletLogQueryVo.setHdId(hdId);
					 * hdUserWalletLogQueryVo.setStartTime(startTime);
					 * PageResult<HdUserWalletLog> hdUserWalletLogPageResult =
					 * hdUserWalletLogServiceClient.queryPage(dataPage,
					 * hdUserWalletLogQueryVo); List<HdUserWalletLog> model =
					 * hdUserWalletLogPageResult.getPage().getDataList();
					 * //分享营收信息 if (!CollectionUtil.isEmpty(model)){
					 * 
					 * for (HdUserWalletLog hdUserWalletLog : model) {
					 * AnnounceResponse announceResponse = new
					 * AnnounceResponse();
					 * announceResponse.setShowType(ShowType.
					 * GAININFO.getIndex()); ModelResult<UserConsumer>
					 * userConsumerModelResult =
					 * userConsumerServiceClient.queryConsumerById
					 * (hdUserWalletLog.getUserId(), null); UserConsumer
					 * userConsumer = userConsumerModelResult.getModel(); if
					 * (!userConsumerModelResult.isSuccess() || null ==
					 * userConsumer) { logger.info(logPrefix +
					 * "查询奖励用户[{}]信息异常！ 【{}】",
					 * hdUserWalletLog.getUserId(),userConsumerModelResult
					 * .getErrorMsg()); return null; }
					 * announceResponse.setAmount(hdUserWalletLog.getAmount());
					 * announceResponse.setIcon(userConsumer.getIcon());
					 * announceResponse.setUserName(userConsumer.getNickName());
					 * announceResponse
					 * .setCreateTime(hdUserWalletLog.getCreateTime());
					 * announceResponses.add(announceResponse); } }
					 */
					// 用户历史营收信息
					List<AnnounceResponse> historyInc = showUserHistoryInc();
					announceResponses.addAll(historyInc);
					// 中奖信息
					List<HdUserGift> dataList = pageResult.getPage().getDataList();
					if (!CollectionUtil.isEmpty(dataList)) {

						for (HdUserGift hdUserGift : dataList) {
							// 2019.10.10 运营调整 答题礼物改为现金 之前获得的星星不展示
							if (hdUserGift.getGiftType().intValue() != HdGiftType.BONUS.getIndex()) {
								continue;
							}
							AnnounceResponse announceResponse = new AnnounceResponse();
							announceResponse.setShowType(ShowType.GIFTINFO.getIndex());
							Long id = hdUserGift.getUserId();
							ModelResult<UserConsumer> userConsumerModelResult = userConsumerServiceClient.queryConsumerById(id, null);
							UserConsumer userConsumer = userConsumerModelResult.getModel();
							if (!userConsumerModelResult.isSuccess() || null == userConsumer) {
								logger.info(logPrefix + "查询奖励用户[{}]信息异常！ 【{}】", id, userConsumerModelResult.getErrorMsg());
								return null;
							}
							announceResponse.setAmount(hdUserGift.getAmount());
							announceResponse.setGiftName(hdUserGift.getGiftName());
							announceResponse.setGiftType(hdUserGift.getGiftType());
							announceResponse.setStatus(hdUserGift.getStatus());
							announceResponse.setIcon(userConsumer.getIcon());
							announceResponse.setUserName(userConsumer.getNickName());
							announceResponse.setCreateTime(hdUserGift.getCreateTime());
							announceResponse.setGiftProp(hdUserGift.getGiftProp());
							announceResponse.setNum(hdUserGift.getId().intValue());
							announceResponses.add(announceResponse);

						}
						data.addAll(announceResponses.stream().sorted(Comparator.comparing(AnnounceResponse::getCreateTime).reversed()).collect(Collectors.toList()).subList(0, announceResponses.size() > 50 ? 50 : announceResponses.size()));
						if (!CollectionUtil.isEmpty(data)) {
							redisClient.setObj(SUBJECTKEY, 180, data);
						}
					}
				}
				else {
					data.addAll(redisClient.getObj(SUBJECTKEY));
				}
				logger.info(logPrefix + "此次请求所有用户榜单数据 【{}】", JSON.toJSONString(data.size()));
			} else {
				String key = StrUtil.format(USERSUBJECTKEY, userId);
				logger.info(logPrefix + "此前请求获取用户榜单信息 参数【userid{},hdid{}】 获取缓存的key[{}]", userId, hdId, key);
				if (!redisClient.exists(key)) {
					param.setUserId(userId);
					List<Integer> list = Lists.newArrayList(HdUserGiftStatus.USED.getIndex());
					param.setStatus(list);
					PageResult<HdUserGift> hdUserGiftPageResult = hdUserGiftServiceClient.pageQueryHdUserGift(siz, param, null);
					List<HdUserGift> dataList = hdUserGiftPageResult.getPage().getDataList();
					logger.info("个人【{}】获奖信息展示  数据大小【{}】", userId, dataList.size());
					if (!CollectionUtil.isEmpty(dataList)) {
						for (HdUserGift hdUserGift : dataList) {
							HdSubjectLogVo hdSubjectLogVo = new HdSubjectLogVo();
							hdSubjectLogVo.setLogNo(hdUserGift.getOuterSerialNo());
							String logNo = hdUserGift.getOuterSerialNo();
							logger.info("当前用户【{}】礼品流水【{}】的logno【{}】", userId, hdUserGift.getId(), hdUserGift.getLogNo());
							HdSubjectLogVo logVo = new HdSubjectLogVo();
							logVo.setLogNo(logNo);
							// logno是唯一的
							ModelResult<List<HdSubjectLog>> listModelResult = hdSubjectLogClient.queryByCondition(logVo);
							List<HdSubjectLog> model = listModelResult.getModel();
							if (!listModelResult.isSuccess() || CollectionUtil.isEmpty(model)) {
								logger.info(logPrefix + "查询用户【{}】 答题流水失败！ 【{}】 答题流水可能为空", userId, listModelResult.getErrorMsg());
								continue;
							}
							AnnounceResponse announceResponse = new AnnounceResponse();
							HdSubjectLog hdSubjectLog = model.get(0);
							announceResponse.setNum(hdSubjectLog.getRightAnswers());
							announceResponse.setAmount(hdUserGift.getAmount());
							announceResponse.setGiftName(hdUserGift.getGiftName());
							announceResponse.setGiftType(hdUserGift.getGiftType());
							announceResponse.setStatus(hdUserGift.getStatus());
							announceResponse.setCreateTime(hdUserGift.getCreateTime());
							announceResponse.setGiftProp(hdUserGift.getGiftProp());
							data.add(announceResponse);
						}
						if (!CollectionUtil.isEmpty(data)) {
							redisClient.setObj(key, 180, data);
						}
					}
				} else {
					data.addAll(redisClient.getObj(key));
				}
				logger.info(logPrefix + "此次请求用户【{}】榜单数据 【{}】", userId, JSON.toJSONString(data.size()));
			}

			return data;
		} catch (Exception e) {
			logger.info(logPrefix + "答题榜单信息出现异常【{}】", e.getMessage(), e);
			return null;
		}
	}

	/**
	 * @param userId  为null则是查全部用户
	 * @param clientType 为null则是查全部客户端
	 * @return 中奖列表
	 */
	public List<AnnounceResponse> showUserGiftInfo(Long userId,Long hdId,Integer clientType){
		List<AnnounceResponse> winnerList = Lists.newArrayList();
		ArrayList<AnnounceResponse> announceResponses=new ArrayList<>();
		logger.info("showUserGiftInfo 查询中奖列表参数 userId{} hdid{} clientType{}",userId,hdId,clientType );
		String key = StrUtil.format(RedisKey.SUBJECT_LIST_USER_GIFT, userId, clientType);
		if (!redisClient.exists(key)) {
			DataPage<HdUserGift> page = new DataPage<>();
			page.setPageSize(50);
			page.setPageNo(1);
			HdUserGiftQueryVo param = new HdUserGiftQueryVo();
			ArrayList<Integer> al = new ArrayList();
			al.add(HdUserGiftStatus.USED.getIndex());
			param.setStatus(al);
			param.setHdId(hdId);
			param.setUserId(userId);
			param.setClientType(clientType);
			PageResult<HdUserGift> hdUserGiftPageResult = hdUserGiftServiceClient.pageQueryHdUserGiftForAdmin(page, param);
			// 中奖信息
			List<HdUserGift> dataList = hdUserGiftPageResult.getPage().getDataList();
			if (!CollectionUtil.isEmpty(dataList)) {
				for (HdUserGift hdUserGift : dataList) {
					AnnounceResponse announceResponse = new AnnounceResponse();
					announceResponse.setShowType(ShowType.GIFTINFO.getIndex());
					Long id = hdUserGift.getUserId();
					ModelResult<UserConsumer> userConsumerModelResult = userConsumerServiceClient.queryConsumerById(id, null);
					UserConsumer userConsumer = userConsumerModelResult.getModel();
					if (!userConsumerModelResult.isSuccess() || null == userConsumer) {
						logger.info("showUserGiftInfo 查询奖励用户[{}]信息异常！ 【{}】", id, userConsumerModelResult.getErrorMsg());
						return null;
					}
					String logNo = hdUserGift.getOuterSerialNo();
					logger.info("当前用户【{}】礼品流水【{}】的logno【{}】", userId, hdUserGift.getId(), hdUserGift.getLogNo());
					HdSubjectLogVo logVo = new HdSubjectLogVo();
					logVo.setLogNo(logNo);
					// logno是唯一的
					ModelResult<List<HdSubjectLog>> listModelResult = hdSubjectLogClient.queryByCondition(logVo);
					List<HdSubjectLog> model = listModelResult.getModel();
					if (!listModelResult.isSuccess() || CollectionUtil.isEmpty(model)) {
						logger.info( "查询用户【{}】 答题流水失败！ 【{}】 答题流水可能为空", userId, listModelResult.getErrorMsg());
						continue;
					}
					HdSubjectLog hdSubjectLog = model.get(0);
					announceResponse.setNum(hdSubjectLog.getRightAnswers());
					announceResponse.setAmount(hdUserGift.getAmount());
					announceResponse.setGiftName(hdUserGift.getGiftName());
					announceResponse.setGiftType(hdUserGift.getGiftType());
					announceResponse.setStatus(hdUserGift.getStatus());
					announceResponse.setIcon(userConsumer.getIcon());
					announceResponse.setUserName(userConsumer.getNickName());
					announceResponse.setCreateTime(hdUserGift.getCreateTime());
					announceResponse.setGiftProp(hdUserGift.getGiftProp());					
					winnerList.add(announceResponse);
				}
				announceResponses.addAll(winnerList.stream().sorted(Comparator.comparing(AnnounceResponse::getCreateTime)
						.reversed()).collect(Collectors.toList()).
						subList(0, winnerList.size() > 50 ? 50 : winnerList.size()));
				redisClient.setObj(key, 180, announceResponses);
				return announceResponses;
			}
		}else {
			return redisClient.getObj(key);
		}
		return winnerList;
	}


	public List<AnnounceResponse> showUserHistoryInc() {
		List<AnnounceResponse> winnerList = Lists.newArrayList();
		if (!redisClient.exists(SUBJECT_LIST_USER_HISTORY_INC)) {
			HdUserWalletLogQueryVo param = new HdUserWalletLogQueryVo();
			param.setTransType(HdTransType.SUBJECT_HD_SHARE_INCOME.getIndex());
			ModelResult<List<HdUserWalletLog>> resModel = hdUserWalletLogServiceClient.queryList(param);
			List<HdUserWalletLog> hdUserWalletLogs = resModel.getModel();
			if (!resModel.isSuccess() || null == hdUserWalletLogs) {
				logger.info("查询用户分享营收流水异常！【{}】", resModel.getErrorMsg());
				return null;
			}
			if (CollectionUtil.isNotEmpty(hdUserWalletLogs)) {
				Map<Long, BigDecimal> collect = hdUserWalletLogs.stream().collect(Collectors.toMap(HdUserWalletLog::getUserId, HdUserWalletLog::getAmount, (key1, key2) -> key1.add(key2)));
				Map<Long, Date> collect1 = hdUserWalletLogs.stream().collect(Collectors.toMap(HdUserWalletLog::getUserId, HdUserWalletLog::getCreateTime, (key1, key2) -> key1));
				Set<Long> userIds = collect.keySet();
				for (Long userId : userIds) {
					ModelResult<UserConsumer> userConsumerModelResult = userConsumerServiceClient.queryConsumerById(userId, null);
					UserConsumer userConsumer = userConsumerModelResult.getModel();
					if (!userConsumerModelResult.isSuccess() || null == userConsumer) {
						logger.info("查询用户信息失败！ param【userid {}】 【{}】 ", userId, userConsumerModelResult.getErrorMsg());
						continue;
					}
					AnnounceResponse announceResponse = new AnnounceResponse();
					announceResponse.setIcon(userConsumer.getIcon());
					announceResponse.setUserName(userConsumer.getNickName());
					announceResponse.setAmount(collect.get(userId));
					announceResponse.setShowType(ShowType.INCINFO.getIndex());
					announceResponse.setCreateTime(collect1.get(userId));
					winnerList.add(announceResponse);
				}
				redisClient.setObj(SUBJECT_LIST_USER_HISTORY_INC, 180, winnerList);
				return winnerList;
			}

		} else {
			return redisClient.getObj(SUBJECT_LIST_USER_HISTORY_INC);
		}
		return null;
	}

	public UserHdWalletInfoResponse showUserWalletInfo(UserConsumer userConsumer, Long hdId) {
		UserHdWalletInfoResponse res = new UserHdWalletInfoResponse();
		Long userId = userConsumer.getId();
		try {

			// 校验用户活动钱包
			ModelResult<HdUserWallet> userWalletModelResult = hdUserWalletServiceClient.queryByUserId(userConsumer.getId());
			if (!userWalletModelResult.isSuccess() || userWalletModelResult.getModel() == null) {
				logger.info("用户：【{}】没有钱包 ", userConsumer.getId());
				// 初始化钱包
				ModelResult<HdUserWallet> walletModelResult = hdUserWalletServiceClient.initHdUserWallet(userConsumer.getId());
				if (!walletModelResult.isSuccess() || walletModelResult.getModel() == null) {
					logger.error("用户：【{}】，参与活动101，初始化钱包失败 【{}】", userConsumer.getId());
					throw new BusinessException("initHdUserWallet.error", "初始化钱包失败");
				}
			}
			HdUserWalletLogQueryVo param = new HdUserWalletLogQueryVo();
			param.setUserId(userId);
			param.setHdId(hdId);
			param.setTransType(HdTransType.SUBJECT_HD_SHARE_INCOME.getIndex());
			// 今日收入
			logger.info("用户【{}】查询营收收入的参数 param【{}】", userId, JSONObject.toJSONString(param));
			ModelResult<List<HdUserWalletLog>> today = hdUserWalletLogServiceClient.queryTodayByCondition(param, true);
			// 历史收入
			ModelResult<List<HdUserWalletLog>> history = hdUserWalletLogServiceClient.queryTodayByCondition(param, false);
			// 钱包余额
			ModelResult<HdUserWallet> hdUserWalletModelResult = hdUserWalletServiceClient.queryByUserId(userId);
			HdUserWallet model = hdUserWalletModelResult.getModel();
			if (!hdUserWalletModelResult.isSuccess() || null == model) {
				logger.info("查询用户【{}】钱包 信息时候 发生异常【{}】", userId, hdUserWalletModelResult.getErrorMsg());
				return null;
			}
			List<HdUserWalletLog> todayHdUserWalletLogs = today.getModel();
			List<HdUserWalletLog> historyHdUserWalletLogs = history.getModel();
			if (!today.isSuccess() || null == todayHdUserWalletLogs) {
				logger.info("查询用户【{}】钱包 今日营收 发生异常【{}】", userId, today.getErrorMsg());
				return null;
			}
			if (!history.isSuccess() || null == historyHdUserWalletLogs) {
				logger.info("查询用户【{}】钱包 历史营收 发生异常【{}】", userId, history.getErrorMsg());
				return null;
			}

			BigDecimal todayAmount = null;
			BigDecimal historyAmount = null;
			if (CollectionUtil.isEmpty(todayHdUserWalletLogs)) {
				todayAmount = new BigDecimal(0);
			} else {
				todayAmount = todayHdUserWalletLogs.stream().map(HdUserWalletLog::getAmount).reduce(BigDecimal::add).get();
			}

			if (CollectionUtil.isEmpty(historyHdUserWalletLogs)) {
				historyAmount = new BigDecimal(0);
			} else {
				historyAmount = historyHdUserWalletLogs.stream().map(HdUserWalletLog::getAmount).reduce(BigDecimal::add).get();
			}

			logger.info("查询用户【{}】钱包信息【今日营收{},历史营收{},钱包额度{}】 ", userId, todayAmount.doubleValue(), historyAmount.doubleValue(), model.getAmount().doubleValue());
			res.setIcon(userConsumer.getIcon());
			res.setNickName(userConsumer.getNickName());
			res.setHistoryIncome(historyAmount.doubleValue());
			res.setTodayIncome(todayAmount.doubleValue());
			res.setTodayGetMoney(model.getAmount().doubleValue());
			return res;
		} catch (Exception e) {
			logger.info("查询用户【{}】钱包信息发生异常【{}】", userId, e.getMessage(), e);
			return null;
		}
	}

}
