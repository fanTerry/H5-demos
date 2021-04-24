package com.esportzoo.esport.controller.hd;

import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.hd.HdSignResponse;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.hd.constants.HdCode;
import com.esportzoo.esport.hd.constants.HdSignGiftStatus;
import com.esportzoo.esport.hd.constants.HdSignStatus;
import com.esportzoo.esport.hd.entity.HdGift;
import com.esportzoo.esport.hd.entity.HdInfo;
import com.esportzoo.esport.hd.entity.HdSign;
import com.esportzoo.esport.hd.gift.HdGiftServiceClient;
import com.esportzoo.esport.hd.gift.HdUserLogServiceClient;
import com.esportzoo.esport.hd.sign.HdSignLogServiceClient;
import com.esportzoo.esport.hd.sign.HdSignServiceClient;
import com.esportzoo.esport.hd.vo.JoinActParam;
import com.esportzoo.esport.hd.vo.JoinActResult;
import com.esportzoo.esport.manager.UserWalletManager;
import com.esportzoo.esport.manager.hd.HdCommonManager;
import com.esportzoo.esport.manager.hd.HdSignManager;
import com.esportzoo.esport.util.RequestUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("hdsign")
@Api(value = "签到活动相关接口", tags = {"签到活动相关接口"})
public class HdSignController extends BaseController {
	private transient final Logger logger = LoggerFactory.getLogger(getClass());
	public static final String logPrefix = "签到活动[102]相关接口-";

	@Autowired
	@Qualifier("hdSignServiceClient")
	HdSignServiceClient hdSignServiceClient;

	@Autowired
	@Qualifier("hdSignLogServiceClient")
	HdSignLogServiceClient hdSignLogServiceClient;

	@Autowired
	@Qualifier("hdGiftServiceClient")
	HdGiftServiceClient hdGiftServiceClient;

	@Autowired
	@Qualifier("hdUserLogServiceClient")
	HdUserLogServiceClient hdUserLogServiceClient;

	@Autowired
	RedisClient redisClient;

	@Autowired
	private HdCommonManager hdCommonManager;

	@Autowired
	private HdSignManager hdSignManager;

	@Autowired
	UserWalletManager userWalletManager;

	/**
	 * 根据用户id查询礼品状态信息
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/queryHdGiftInfo", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "查询签到礼品状态", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "查询签到礼品状态信息", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<JSONObject> queryHdGiftInfoByUserId(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		UserConsumer userConsumer = getLoginUsr(request);
		try {
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("用户未登录");
			}
			CommonResponse<HdInfo> hdResponse = hdCommonManager.getHdBaseInfo(HdCode.HD_102.getIndex());
			if (null == hdResponse || !StringUtils.isNotBlank(hdResponse.getCode()) || !hdResponse.getCode()
					.equals(ResponseConstant.RESP_SUCC_CODE)) {
				logger.info(logPrefix + "用户id={},活动状态异常:{}", userConsumer.getId(), JSONObject.toJSONString(hdResponse));
				return CommonResponse.withErrorResp("活动状态异常");
			}

			ModelResult<List<HdGift>> listModelResult = hdSignLogServiceClient.querySignGiftList();

			if (listModelResult == null || !listModelResult.isSuccess() || listModelResult.getModel() == null) {
				logger.info(logPrefix + "用户id={},获取签到奖励列表异常:{}", userConsumer.getId(), listModelResult.getErrorMsg());
				return CommonResponse.withErrorResp("获取签到奖励列表异常");
			}

			List<HdSignResponse> signGiftInfo = getSignGiftInfo(listModelResult.getModel(), userConsumer,jsonObject);
			jsonObject.put("signGift", signGiftInfo);
			ModelResult<HdSign> modelResult = hdSignServiceClient.queryHdSignInfoByUserId(userConsumer.getId().intValue());
			if (modelResult!=null && modelResult.isSuccess() && modelResult.getModel()!=null) {
				HdSign hdSign = modelResult.getModel();
				if (jsonObject.get("signNum") == null) {
					jsonObject.put("signNum", hdSign.getSignFlagBit());
				}

			}

			return CommonResponse.withSuccessResp(jsonObject);
		} catch (Exception e) {
			logger.error("查询用户签到状态出现异常，用户id:{},异常信息:{}", userConsumer.getId(), e);
			e.printStackTrace();
			return CommonResponse.withErrorResp("查询用户签到状态失败");
		}
	}

	private List<HdSignResponse> getSignGiftInfo(List<HdGift> hdGiftList, UserConsumer userConsumer,JSONObject jsonObject) {
		List<HdSignResponse> hdSignResponseList = Lists.newArrayList();
		ModelResult<HdSign> hdSignModelResult = hdSignServiceClient.queryHdSignInfoByUserId(userConsumer.getId().intValue());
		if (!hdSignModelResult.isSuccess() || hdSignModelResult.getModel() == null) {
//			return hdSignResponseList;
		}

		HdSign hdSignInfo = hdSignModelResult.getModel();
		//用户未进行过签到或这周未进行过签到,重置签到累计信息为0
		Calendar currentTime = Calendar.getInstance();
		Date firstDayOfWeek = DateUtil.getFirstDayOfWeek(currentTime.getTime());
		Integer signFlagBit = 0;

		if (hdSignInfo == null || StringUtils.isEmpty(hdSignInfo.getSignFlagBit())) {
			//未有签到记录
			signFlagBit = 0;
		} else if (hdSignInfo.getLatestSignTime() == null
				|| DateUtil.interval(DateUtil.dateToCalendar(firstDayOfWeek), DateUtil.dateToCalendar(hdSignInfo.getLatestSignTime())) > 0) {
			signFlagBit = 0;
		} else {
			signFlagBit = Integer.valueOf(hdSignInfo.getSignFlagBit());
		}
		boolean isSignToday = false;

		if (hdSignInfo != null && hdSignInfo.getLatestSignTime() != null
				&& DateUtil.interval(currentTime, DateUtil.dateToCalendar(hdSignInfo.getLatestSignTime())) == 0) {
			isSignToday = true;
		}
		for (HdGift hdGift : hdGiftList) {
			HdSignResponse signResponse = new HdSignResponse();
			if (hdGift.getIsFixed()==0){
				signResponse.setSevenFlag("随机");
			}else {
				signResponse.setHdGiftCount(hdGift.getGiftPropJo().getInteger("count"));
			}

			signResponse.setHdGiftName(hdGift.getGiftName());
			Integer giftflagBit = hdGift.getGiftPropJo().getInteger("signFlagBit");
			if (signFlagBit == 0 && giftflagBit == 1) {
				//首次签到或者首天
				signResponse.setReceiveStatus(HdSignGiftStatus.UNCLAIMED.getIndex());
				//本周首次签到，连续签到次数归0
				jsonObject.put("signNum", 0);
//				hdSignInfo.setSignFlagBit("0");
//				hdSignServiceClient.updateHdSign(hdSignInfo);
			} else if (giftflagBit <= signFlagBit) {
				signResponse.setReceiveStatus(HdSignGiftStatus.HAVE_RECEIVED.getIndex());

			} else {
				//当天未领取领取
				if (!isSignToday && (signFlagBit + 1 == giftflagBit)) {
					signResponse.setReceiveStatus(HdSignGiftStatus.UNCLAIMED.getIndex());
				} else {
					signResponse.setReceiveStatus(HdSignGiftStatus.UNABLE_TO_RECEIVE.getIndex());
				}


			}
			hdSignResponseList.add(signResponse);

		}
		return hdSignResponseList;
	}

	/**
	 * 签到
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/signIn", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "活动签到", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "活动签到", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<JSONObject> signIn(HttpServletRequest request, BaseRequest baseRequest) {
		JSONObject jsonObject = new JSONObject();
		UserConsumer userConsumer = getLoginUsr(request);
		try {
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("用户未登录");
			}
			String repeatClickKey = "sign_hd." + userConsumer.getId() + "." + HdCode.HD_102.getIndex();
			if (!redisClient.setNX(repeatClickKey, "1", 2)) {
				logger.error("用户id[{}],nickName=[{}],参与活动code[{}],请求频繁", userConsumer.getId(), userConsumer.getNickName(),
						HdCode.HD_102.getDescription());
				return CommonResponse.withResp("9999", "请勿重复点击");
			}
			Map<String, Object> modelMap = Maps.newHashMap();
			JoinActParam param = new JoinActParam();
			param.setCustomerId(userConsumer.getId());
			param.setHdCode((long) HdCode.HD_102.getIndex());
			param.setOperIp(RequestUtil.getClientIp(request));
			param.setSellClient(baseRequest.getClientType());
			param.setSellChannel(baseRequest.getAgentId());
			param.setBizSystem(baseRequest.getBiz());
			JoinActResult result = hdUserLogServiceClient.joinActivity(param);
			if (result.isSuccess()) {
				jsonObject.put("giftCount", result.getHdGiftLog().get(0).getHdGift().getAmount().stripTrailingZeros().toPlainString());
				jsonObject.put("giftRecScore", userWalletManager.getUserWalletRec(userConsumer.getId()).getAbleRecScore());
			} else {
				logger.error(logPrefix+"用户：【{}】参与签到，失败 【{}】",userConsumer.getId(), result.getErrorMsg());
				return CommonResponse.withErrorResp(result.getErrorMsg());
			}

		} catch (Exception e) {
			logger.error("用户签到出现异常，用户id:{},异常信息:{}", userConsumer.getId(), e.getMessage());
			return CommonResponse.withErrorResp("用户签到失败");
		}
		return CommonResponse.withSuccessResp(jsonObject);
	}

	/**
	 * 查询签到状态
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/querySignStatus", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "查询签到状态", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "查询用户当天签到状态", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<JSONObject> querySignStatus(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		UserConsumer userConsumer = getLoginUsr(request);
		try {
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("用户未登录");
			}
			CommonResponse<HdInfo> hdResponse = hdCommonManager.getHdBaseInfo(HdCode.HD_102.getIndex());
			if (null == hdResponse || !StringUtils.isNotBlank(hdResponse.getCode()) || !hdResponse.getCode()
					.equals(ResponseConstant.RESP_SUCC_CODE)) {
				logger.info(logPrefix + "用户id={},活动状态异常:{}", userConsumer.getId(), JSONObject.toJSONString(hdResponse));
				return CommonResponse.withErrorResp("活动状态异常");
			}
			ModelResult<HdSign> hdSignModelResult = hdSignServiceClient.queryHdSignInfoByUserId(userConsumer.getId().intValue());
			if (null == hdSignModelResult || !hdSignModelResult.isSuccess() || null == hdSignModelResult.getModel()) {
				jsonObject.put("signStatus", HdSignStatus.SIGNED_IN.getIndex());
				return CommonResponse.withSuccessResp(jsonObject);
			}
			HdSign hdSignInfo = hdSignModelResult.getModel();
			if (HdSignManager.isNow(hdSignInfo.getLatestSignTime())) {
				jsonObject.put("signStatus", HdSignStatus.UNSIGNED.getIndex());
			} else {
				jsonObject.put("signStatus", HdSignStatus.SIGNED_IN.getIndex());
			}
			return CommonResponse.withSuccessResp(jsonObject);
		} catch (Exception e) {
			logger.error("查询签到状态出现异常，用户id:{},异常信息:{}", userConsumer.getId(), e.getMessage());
			return CommonResponse.withErrorResp("查询签到状态失败");
		}
	}


}
