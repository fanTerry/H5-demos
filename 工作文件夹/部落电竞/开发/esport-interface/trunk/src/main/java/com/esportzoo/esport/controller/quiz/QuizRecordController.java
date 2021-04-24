package com.esportzoo.esport.controller.quiz;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.domain.result.PageResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.common.SysConfigPropertyServiceClient;
import com.esportzoo.esport.client.service.quiz.QuizExchangeGoodsServiceClient;
import com.esportzoo.esport.client.service.quiz.QuizPlanFollowServiceClient;
import com.esportzoo.esport.client.service.quiz.QuizPlanServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.quiz.QuizRecordPageRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.quiz.QuizRecordResponse;
import com.esportzoo.esport.constants.AdPicPlaceType;
import com.esportzoo.esport.constants.SysConfigPropertyKey;
import com.esportzoo.esport.constants.quiz.*;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.ClientAdPic;
import com.esportzoo.esport.domain.SysConfigProperty;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.manager.CommonManager;
import com.esportzoo.esport.vo.quiz.*;
import com.esportzoo.quiz.client.service.quiz.QuizMatchGameServiceClient;
import com.esportzoo.quiz.constants.QuizMatchStatus;
import com.esportzoo.quiz.constants.QuizOption;
import com.esportzoo.quiz.constants.SpInfoResult;
import com.esportzoo.quiz.domain.quiz.QuizMatchGameInfo;
import com.esportzoo.quiz.domain.quiz.QuizPlayDefine;
import com.esportzoo.quiz.util.CatchPlayDefineUtil;
import com.esportzoo.quiz.util.SpUtils;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * 查询用户竞猜记录
 *
 * @Author lixisheng
 * @Date 2019/10/24 19:36
 */
@Controller
@RequestMapping("/quiz/record")
public class QuizRecordController extends BaseController {
	private transient final Logger logger = LoggerFactory.getLogger(getClass());
	private static String loggerPre = "查询用户竞猜记录_";


	@Autowired
	private CatchPlayDefineUtil playDefineUtil;
	@Autowired
	private QuizExchangeGoodsServiceClient exchangeGoodsServiceClient;
	@Autowired
	private RedisClient redisClient;


	@Autowired
	private QuizPlanServiceClient quizPlanServiceClient;

	@Autowired
	private QuizPlanFollowServiceClient quizPlanFollowServiceClient;

	@Autowired
	private QuizMatchGameServiceClient quizMatchGameServiceClient;

	@Autowired
	private  SysConfigPropertyServiceClient sysConfigPropertyServiceClient;
	@Autowired
	private CommonManager commonManager;
	/* 中奖用户列表 跑马灯展示 PrizeHandle */
	private final static String WIN_PRIZE_USER_LIST = "quiz_win_prize_user_list";

	private final static BigDecimal MIN_COST=new BigDecimal(1000);
	private final static BigDecimal MIN_BETSP=new BigDecimal(1.3);

	@RequestMapping(value = "/recordPage", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "竞猜记录", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "竞猜记录信息", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<QuizRecordResponse>> queryRecordPage(QuizRecordPageRequest param, HttpServletRequest request) {
		UserConsumer userConsumer = getLoginUsr(request);
		try {
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("用户未登录");
			}
			QuizRecordQueryVo quizRecordQueryVo = new QuizRecordQueryVo();
			quizRecordQueryVo.setUserId(userConsumer.getId());// 用户ID
			quizRecordQueryVo.setWinStatus(param.getWinStatus());// 当前方案赛事中奖状态，0、待开奖，2、已中奖，1、未中奖
			// 0、待开奖时，只展示已经投注成功的方案
			if (param.getWinStatus() != null && param.getWinStatus().intValue() == QuizPlanWinStatus.NOT_OPEN.getIndex()) {
				quizRecordQueryVo.setStatus(QuizPlanStatus.SUCCESS.getIndex());
			} else {
				quizRecordQueryVo.setPlanNoShowList(QuizPlanStatus.getNoShowList());
			}
			DataPage<QuizRecordResult> dataPage = new DataPage<>();
			dataPage.setPageNo(param.getPageNo());
			dataPage.setPageSize(param.getPageSize());
			PageResult<QuizRecordResult> pageResult = quizPlanServiceClient.queryRecordPage(quizRecordQueryVo, dataPage);
			if (!pageResult.isSuccess()) {
				logger.info(loggerPre + "分页查询用户竞猜记录订单接口,调用接口返回错误,errorMsg={},param={}", pageResult.getErrorMsg(), JSON.toJSONString(param));
				return CommonResponse.withErrorResp(pageResult.getErrorMsg());
			}
			List<QuizRecordResult> dataList = pageResult.getPage().getDataList();
			ArrayList<QuizRecordResponse> quizRecordResponseList = new ArrayList<>();
			if (CollectionUtil.isNotEmpty(dataList)) {
				List<Long> matchGameIdList = dataList.stream().map(QuizRecordResult::getMatchGameId).distinct().collect(Collectors.toList());
				ModelResult<List<QuizMatchGameInfo>> modelResult = quizMatchGameServiceClient.queryGameInfoByIdList(matchGameIdList);
				if (!modelResult.isSuccess()) {
					logger.info(loggerPre + "根据id查询赛事,调用接口返回错误,errorMsg={}", modelResult.getErrorMsg());
					return CommonResponse.withErrorResp(pageResult.getErrorMsg());
				}
				List<QuizMatchGameInfo> quizMatchGameInfoList = modelResult.getModel();
				Map<Long, QuizMatchGameInfo> collect = quizMatchGameInfoList.stream().collect(Collectors.toMap(k -> k.getId(), v -> v));
				Map<Integer, QuizPlayDefine> playNoThirdNameMap = playDefineUtil.getPlayNoMap();
				for (QuizRecordResult quizRecordResult : dataList) {
					QuizRecordResponse quizRecordResponse = new QuizRecordResponse();
					Long matchGameId = quizRecordResult.getMatchGameId();
					QuizMatchGameInfo matchGameInfo = collect.get(matchGameId);
					if (matchGameInfo == null) {
						logger.info(loggerPre + "赛事为空,赛事matchGameId={}", matchGameId);
						continue;
					}
					if (quizRecordResult.getPlanType() == QuizPlanType.FOLLOW.getIndex()) {//是跟单方案
						//判断订单状态，未开奖不展示比赛相关信息
						if (quizRecordResult.getWinStatus()==QuizPlanWinStatus.NOT_OPEN.getIndex()){//未开奖
							quizRecordResponse.setRecommendAccount(quizRecordResult.getRecommendAccount());
							quizRecordResponse.setFollowUserNickName(quizRecordResult.getFollowUserNickName());
							quizRecordResponse.setCreateTime(quizRecordResult.getCreateTime());
							quizRecordResponse.setCost(quizRecordResult.getCost());
							quizRecordResponse.setWinStatus(quizRecordResult.getWinStatus());
							quizRecordResponse.setStatus(quizRecordResult.getStatus());
							quizRecordResponse.setQuizOrderStatus(quizRecordResult.getQuizOrderStatus());
							quizRecordResponse.setPlanNo(quizRecordResult.getPlanNo());
							quizRecordResponse.setVideoGameId(quizRecordResult.getVideoGameId());
						}else {
							getRecordInfo(playNoThirdNameMap, quizRecordResult, quizRecordResponse, matchGameInfo);
						}
						// 是普通方案 判断是否符合推单条件
                    } else if (quizRecordResult.getPlanType() == QuizPlanType.DEFAULT.getIndex()) {
                        getRecordInfo(playNoThirdNameMap, quizRecordResult, quizRecordResponse, matchGameInfo);
                        if (quizRecordResponse.getWinStatus() == QuizPlanWinStatus.NOT_OPEN.getIndex()
                                && quizRecordResponse.getStatus() == QuizPlanStatus.SUCCESS.getIndex()
                                && matchGameInfo.getMatchStatus() == QuizMatchStatus.NOTSTART.getIndex()
                                && quizRecordResponse.getBetSp().compareTo(getSysTemRealBetOddsMin()) > -1
                                && quizRecordResponse.getCost().compareTo(getSysTemRealRecommedBetAmountMin()) > -1) {
                            quizRecordResponse.setRecommendType(Boolean.TRUE);
                        }
                    } else {
                        getRecordInfo(playNoThirdNameMap, quizRecordResult, quizRecordResponse, matchGameInfo);
                    }
                    quizRecordResponseList.add(quizRecordResponse);
				}
			}
			return CommonResponse.withSuccessResp(quizRecordResponseList);
		} catch (Exception e) {
			logger.error(loggerPre + "查询用户竞猜记录出现异常，用户id:{}", userConsumer.getId(), e);
			return CommonResponse.withErrorResp("查询用户竞猜记录失败");
		}
	}

	private void getRecordInfo(Map<Integer, QuizPlayDefine> playNoThirdNameMap, QuizRecordResult quizRecordResult, QuizRecordResponse quizRecordResponse, QuizMatchGameInfo matchGameInfo) {
		String content = quizRecordResult.getContent();
		quizRecordResponse.setShowMatch(Boolean.TRUE);
		BeanUtil.copyProperties(quizRecordResult,quizRecordResponse, CopyOptions.create().ignoreNullValue());
		BeanUtil.copyProperties(matchGameInfo,quizRecordResponse, CopyOptions.create().ignoreNullValue());
		if (content.contains("FSGL")) {
			String index = content.substring(content.indexOf("|") + 1, content.indexOf("@"));
			int index1 = content.lastIndexOf("(");
			String odd;
			if (index1 > 0) {
				odd = content.substring(content.lastIndexOf("(") + 1, content.length() - 1);
			} else {
				odd = content.substring(content.lastIndexOf("@") + 1);
			}
			quizRecordResponse.setBetSp(new BigDecimal(odd));
			List<QuizOption> quizOptionList = SpUtils.spToQuizOptionList(quizRecordResponse.getSp(), quizRecordResponse.getPlayNo(),
					new String[]{quizRecordResponse.getHomeTeamName(), quizRecordResponse.getAwayTeamName()});
			Map<String, QuizOption> indexAndQuizOptionMap = quizOptionList.stream().collect(Collectors.toMap(key -> key.getIndex(), value -> value));

			if (quizRecordResponse.getAwardResult() != null) {
				QuizOption answerOption = indexAndQuizOptionMap.get(quizRecordResponse.getAwardResult());
				if (answerOption != null) {
					quizRecordResponse.setAnswer((answerOption.getTeamName() == null ? "" : answerOption.getTeamName() + " ") + answerOption.getName()); // 赢盘的项名字
				} else {
					logger.info("比赛结果与解析sp的值不匹配：比赛结果:{}", quizRecordResponse.getAwardResult());
				}
			}
			QuizOption option = indexAndQuizOptionMap.get(index);
			quizRecordResponse.setOption((option.getTeamName() == null ? "" : option.getTeamName() + " ") + option.getName());// 用户选择的投注项
			// 名字
			/* 设置玩法题目 */
			// 获得解析的sp对象
			List<SpInfoResult> spInfoResults = SpUtils.resolverSp(quizRecordResponse.getSp());
			// 题目中内容取第一个选项的附加值
			QuizPlayDefine quizPlayDefine = playNoThirdNameMap.get(quizRecordResponse.getPlayNo());
			if (quizPlayDefine != null && spInfoResults != null) {
				String playSubject = SpUtils.getPlaySubject(quizPlayDefine, spInfoResults.get(0).getAttach()
						, quizRecordResponse.getVideoGameId(), quizRecordResponse.getHomeTeamName());
				quizRecordResponse.setPlayName(playSubject);
			}
		}
	}
	/**
	 * 获取系统配置中 发起推单的最低投注金额
	 *
	 * @return
	 */
	private BigDecimal getSysTemRealRecommedBetAmountMin() {
		try {
			String configValueByKey = sysConfigPropertyServiceClient.getConfigValueByKey(SysConfigPropertyKey.RECOMMEND_ORDER_BET_MONEY_MIN);
			if (StrUtil.isNotBlank(configValueByKey)) {
				return new BigDecimal(configValueByKey);
			}
		} catch (Exception e) {
			logger.info("获取系统配置中发起推单的最低投注金额异常,将启用默认值:1000", e);
		}
		return MIN_COST;
	}

	/**
	 * 获取系统配置中 发起推单的最低赔率
	 *
	 * @return
	 */
	private BigDecimal getSysTemRealBetOddsMin() {
		try {
			String configValueByKey = sysConfigPropertyServiceClient.getConfigValueByKey(SysConfigPropertyKey.RECOMMEND_ORDER_BET_ODDS_MIN);
			if (StrUtil.isNotBlank(configValueByKey)) {
				return new BigDecimal(configValueByKey);
			}
		} catch (Exception e) {
			logger.info("获取系统配置中发起推单的最低赔率异常,将启用默认值:1.3", e);
		}
		return MIN_BETSP;
	}

	@RequestMapping(value = "/exchangeRecord", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "兑奖播报", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "兑奖播报信息", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<QuizBroadcastInfo>> queryRecordPage(BaseRequest request) {
		List<QuizBroadcastInfo> responseList = new ArrayList<>();
		try {
			QuizExchangeGoodsQueryVo queryVo = new QuizExchangeGoodsQueryVo();
			queryVo.setStartCreateTime(DateUtil.getCurBeforeOrAferHours(-48));
			queryVo.setStatus(QuizExchangeGoodsStatus.VALID.getIndex());
			ModelResult<List<QuizBroadcastInfo>> listModelResult = exchangeGoodsServiceClient.indexRadioList(queryVo);
			responseList = listModelResult.getModel();
			if (!listModelResult.isSuccess() || responseList == null) {
				return CommonResponse.withErrorResp("暂无兑奖记录");
			}
		} catch (Exception e) {
			logger.error("查询首页兑奖播报记录异常", e);
		}
		return CommonResponse.withSuccessResp(responseList);
	}

	@RequestMapping(value = "/broadcastList", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "竞猜首页播报", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "竞猜首页播报", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<QuizBroadcastInfo>> broadcastList(HttpServletRequest request, BaseRequest baseRequest) {
		List<QuizBroadcastInfo> resultList = Lists.newArrayList();
		try {
			resultList = getBroadcastInfos(baseRequest);
			return CommonResponse.withSuccessResp(resultList);
		} catch (Exception e) {
			logger.error("获取跑马灯缓存异常:{}", e.getMessage(), e);
			return CommonResponse.withErrorResp("获取播报数据异常！");
		}
	}



	/** 获取播报信息 */
	private List<QuizBroadcastInfo> getBroadcastInfos(BaseRequest baseRequest) {
		List<QuizBroadcastInfo> resultList = Lists.newArrayList();
		/** 取出公告信息 */
		List<QuizBroadcastInfo> noticeList = Lists.newArrayList();
		boolean topFlag = getNoticeList(baseRequest, noticeList);
		if (topFlag) {// 有置顶公告
			return noticeList;
		}
		/** 取出缓存中的中奖信息 */
		List<QuizBroadcastInfo> winInfoList = redisClient.getObj(WIN_PRIZE_USER_LIST);
		if (null != winInfoList) {
			Collections.reverse(winInfoList);
			resultList.addAll(winInfoList);
		}
		/** 取出商品兑换信息 */
		QuizExchangeGoodsQueryVo queryVo = new QuizExchangeGoodsQueryVo();
		queryVo.setStartCreateTime(DateUtil.getCurBeforeOrAferHours(-24));
		queryVo.setStatus(QuizExchangeGoodsStatus.VALID.getIndex());
		ModelResult<List<QuizBroadcastInfo>> listModelResult = exchangeGoodsServiceClient.indexRadioList(queryVo);
		if (listModelResult.isSuccess() && null != listModelResult.getModel()) {
			resultList.addAll(listModelResult.getModel());
		}
		/** 取出后台系统配置 */
		SysConfigProperty configProperty = getSysConfigByKey(SysConfigPropertyKey.QUIZ_BROADCAST_HANDLE_INPUT_INFO, baseRequest.getClientType(), baseRequest.getAgentId());
		if (null != configProperty && StringUtils.isNotBlank(configProperty.getValue())) {
			List<QuizBroadcastInfo> haldleInfos = JSON.parseArray(configProperty.getValue(), QuizBroadcastInfo.class);
			resultList.addAll(haldleInfos);
		}

		// 统一按时间排序
		//		resultList = resultList.stream().sorted(Comparator.comparing(QuizBroadcastInfo::getCreateTime))
		//				.collect(Collectors.toList());
		ArrayList<QuizBroadcastInfo> quizBroadcastInfoArrayList = resultList.stream()
				.collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getUserName()))), ArrayList::new));
		resultList = quizBroadcastInfoArrayList.stream().sorted(Comparator.comparing(QuizBroadcastInfo::getCreateTime, Comparator.nullsLast((o1, o2) -> o1.compareTo(o2))))
				.collect(Collectors.toList());
		if (!noticeList.isEmpty()) {
			resultList.addAll(0, noticeList);
		}
		return resultList;
	}

	/** 取出公告信息 */
	private boolean getNoticeList(BaseRequest baseRequest, List<QuizBroadcastInfo> noticeList) {
		boolean topFlag = false;
		List<ClientAdPic> noticeDbList = commonManager.getClientAdPic(AdPicPlaceType.NOTICE_INFO.getIndex(), baseRequest.getClientType(), true);
		if (null != noticeDbList && noticeDbList.size() > 0) {
			for (ClientAdPic item : noticeDbList) {
				QuizBroadcastInfo temp = new QuizBroadcastInfo();
				if (null != item.getCreateTime()) {
					temp.setCreateTime(item.getCreateTime().getTime());
				}
				temp.setNoticeDesc(item.getTitle());
				temp.setType(QuizBroadcastType.NOTICE_INFO.getIndex());
				temp.setNoticeUrl(item.getLocation());
				noticeList.add(temp);
				if (item.getOrderNumber().intValue() == 0) {// 说明公告有置顶内容
					topFlag = true;
				}
			}
		}
		return topFlag;
	}

}
