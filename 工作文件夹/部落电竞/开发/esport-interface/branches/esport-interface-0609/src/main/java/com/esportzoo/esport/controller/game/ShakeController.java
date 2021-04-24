package com.esportzoo.esport.controller.game;

import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.annotation.EsportMethodLog;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.quiz.QuizExchangeGoodsServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.game.ShakePlayRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.game.ShakeRoomDataResponse;
import com.esportzoo.esport.constants.game.GameConstants;
import com.esportzoo.esport.constants.quiz.QuizExchangeGoodsStatus;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.manager.game.ShakeManager;
import com.esportzoo.esport.vo.game.ShakePlayResponse;
import com.esportzoo.esport.vo.quiz.QuizBroadcastInfo;
import com.esportzoo.esport.vo.quiz.QuizExchangeGoodsQueryVo;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * 欢乐摇一摇相关接口
 *
 * @author jing.wu
 * @version 创建时间：2020年4月4日 上午10:23:37
 */
@Controller
@RequestMapping("/game/shake")
@Api(value = "欢乐摇一摇相关接口", tags = { "欢乐摇一摇" })
public class ShakeController extends BaseController {

	public static final String LOGGER_PREFIX = "调用欢乐摇一摇接口:";

	@Autowired
	ShakeManager shakeManager;
	@Autowired
	RedisClient redisClient;
	@Autowired
	private QuizExchangeGoodsServiceClient exchangeGoodsServiceClient;

	@RequestMapping(value = "/roomData", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "房间信息", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "房间信息", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<ShakeRoomDataResponse> roomData(HttpServletRequest request, String roomNo, BaseRequest baseRequest) {
		logger.info("{}房间信息 ,房间编号={}", LOGGER_PREFIX, roomNo);
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("用户未登录");
			}
			return shakeManager.getRoomData(userConsumer.getId(), roomNo, baseRequest);
		} catch (Exception e) {
			logger.info(LOGGER_PREFIX + "查询房间信息发生异常,exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	@RequestMapping(value = "/startPlay", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "摇一摇", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "摇一摇", response = CommonResponse.class)
	@ResponseBody
	@EsportMethodLog(paramLog = false)
	public CommonResponse<ShakePlayResponse> startPlay(HttpServletRequest request, ShakePlayRequest shakePlayRequest) {
		logger.info("{}摇一摇 ,参数baseRequest={}", LOGGER_PREFIX, JSONObject.toJSONString(shakePlayRequest));
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("用户未登录");
			}

			logger.info(LOGGER_PREFIX + "确定摇一摇,用户id={},参数={}", userConsumer.getId(), JSONObject.toJSONString(shakePlayRequest));
			// 调用游戏接口
			CommonResponse<ShakePlayResponse> commonResponse = shakeManager.startPlay(request, shakePlayRequest, userConsumer);
			logger.info(LOGGER_PREFIX + "确定摇一摇,调用接口处理成功,用户id={},参数={},处理结果={}",
					userConsumer.getId(), JSONObject.toJSONString(shakePlayRequest), JSONObject.toJSONString(commonResponse));
			return commonResponse;
		} catch (Exception e) {
			logger.info(LOGGER_PREFIX + "确定摇一摇发生异常,exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	@RequestMapping(value = "/getUserWalletLog", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "用户钱包流水", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "用户钱包流水", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<JSONObject> getUserWalletLog(HttpServletRequest request, Integer pageNo, Integer pageSize) {

		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("用户未登录");
			}
			logger.info(LOGGER_PREFIX + "查询用户钱包流水记录，用户【{}】,pageNo={},pageSize={}", userConsumer.getId(), pageNo, pageSize);
			CommonResponse<JSONObject> commonResponse = shakeManager.getUserWalletLog(userConsumer, pageNo, pageSize);
			logger.info(LOGGER_PREFIX + "查询用户钱包流水记录处理成功,用户id={}", userConsumer.getId());
			return commonResponse;
		} catch (Exception e) {
			logger.info(LOGGER_PREFIX + "查询用户钱包流水记录发生异常,exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}


	@RequestMapping(value = "/broadcastList", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "欢乐摇一摇播报", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "欢乐摇一摇播报", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<QuizBroadcastInfo>> broadcastList(HttpServletRequest request, BaseRequest baseRequest) {
		List<QuizBroadcastInfo> resultList = Lists.newArrayList();
		try {
			/** 取出缓存中的中奖信息 */
			List<QuizBroadcastInfo> winInfoList = redisClient.getObj(GameConstants.GAME_WIN_BROADCAST_LIST_KEY);
			if (null != winInfoList) {
				Collections.reverse(winInfoList);
				resultList.addAll(winInfoList);
			}
			/** 取出商品兑换信息 */
			QuizExchangeGoodsQueryVo queryVo = new QuizExchangeGoodsQueryVo();
			queryVo.setStartCreateTime(DateUtil.getCurBeforeOrAferHours(-72));
			queryVo.setStatus(QuizExchangeGoodsStatus.VALID.getIndex());
			ModelResult<List<QuizBroadcastInfo>> listModelResult = exchangeGoodsServiceClient.indexRadioList(queryVo);
			if (listModelResult.isSuccess() && null != listModelResult.getModel()) {
				resultList.addAll(listModelResult.getModel());			}
			ArrayList<QuizBroadcastInfo> quizBroadcastInfoArrayList = resultList.stream()
					.collect(collectingAndThen(toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getUserId()))),ArrayList::new));
			List<QuizBroadcastInfo> collect = quizBroadcastInfoArrayList.stream().sorted(Comparator.comparing(QuizBroadcastInfo::getCreateTime,
					Comparator.nullsLast((o1, o2) -> o1.compareTo(o2))).reversed()).collect(Collectors.toList());
			return CommonResponse.withSuccessResp(collect);
		} catch (Exception e) {
			logger.error("获取跑马灯缓存异常:{}", e.getMessage(), e);
			return CommonResponse.withErrorResp("获取播报数据异常！");
		}
	}
}
