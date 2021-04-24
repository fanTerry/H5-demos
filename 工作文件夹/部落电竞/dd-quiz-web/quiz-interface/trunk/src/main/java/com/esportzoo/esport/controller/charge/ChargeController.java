package com.esportzoo.esport.controller.charge;

import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.esport.client.service.charge.UserChargeOrderServiceClient;
import com.esportzoo.esport.client.service.wallet.UserWalletRecServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.charge.ChargeRequst;
import com.esportzoo.esport.connect.request.charge.ChargeRequstExpand;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.charge.ChargePageResponse;
import com.esportzoo.esport.connect.response.charge.ChargeResponse;
import com.esportzoo.esport.connect.response.charge.H5ChargeResponse;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.constants.*;
import com.esportzoo.esport.constants.cms.expert.H5ChargeWay;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.SysConfigProperty;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.UserWalletRec;
import com.esportzoo.esport.domain.charge.UserThirdOrder;
import com.esportzoo.esport.manager.UserManager;
import com.esportzoo.esport.manager.UserWalletManager;
import com.esportzoo.esport.util.RequestUtil;
import com.esportzoo.esport.util.WxpayUtil;
import com.esportzoo.esport.vo.ChargePayRequest;
import com.esportzoo.esport.vo.charge.IapChargeVo;
import com.esportzoo.esport.vo.partner.UboxBalanceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author tingting.shen
 * @date 2019/05/18
 */
@Controller
@RequestMapping("charge")
@Api(value = "充值相关接口", tags = { "充值相关接口" })
public class ChargeController extends BaseController {
	
	@Autowired
	private UserManager userManager;
	@Autowired
	private UserWalletManager userWalletManager;
	@Autowired
	private UserChargeOrderServiceClient userChargeOrderServiceClient;
	@Autowired
	private UserWalletRecServiceClient userWalletServiceClient;
	@Value("${appId}")
    private String appId;
	@Value("${signKey}")
    private String signKey;
	
	@RequestMapping(value = "/pageData", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "充值页所需数据", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "充值页所需数据", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<ChargePageResponse> getChargePageData(HttpServletRequest request, BaseRequest baseRequest) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("充值页所需数据,未获取到登录用户信息");
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			ModelResult<UserWalletRec> modelResult = userWalletServiceClient.queryWalletByUserId(userConsumer.getId());
			if (!modelResult.isSuccess()) {
				logger.info("充值页所需数据,调用接口返回错误,errorMsg={}", modelResult.getErrorMsg());
				return CommonResponse.withErrorResp("调用接口返回错误,errorMsg=" + modelResult.getErrorMsg());
			}
			UserWalletRec userWallet = modelResult.getModel();
			if (userWallet==null ) {
				logger.info("充值页所需数据,userWallet为空");
				return CommonResponse.withErrorResp("充值页所需数据,userWallet为空");
			}
			ChargePageResponse chargePageResponse = new ChargePageResponse();
			chargePageResponse.setAbleRecScore(userWallet.getAbleRecScore().toString());
			
			SysConfigProperty sysConfigProperty = getSysConfigByKey(SysConfigPropertyKey.WXXCY_CHARGE_AMOUNT_LIST,baseRequest.getClientType(),baseRequest.getAgentId());
			chargePageResponse.setChargeAmountList(sysConfigProperty.getValue().split(";"));
			
			return CommonResponse.withSuccessResp(chargePageResponse);
		} catch (Exception e) {
			logger.info("充值页所需数据,发生异常,exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/iapAmountList", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "苹果应用内购买充值所需数据", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "苹果应用内购买充值所需数据", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<ChargePageResponse> getIapAmountList(HttpServletRequest request, BaseRequest baseRequest) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("苹果应用内购买充值所需数据,未获取到登录用户信息");
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			ModelResult<UserWalletRec> modelResult = userWalletServiceClient.queryWalletByUserId(userConsumer.getId());
			if (!modelResult.isSuccess()) {
				logger.info("苹果应用内购买充值所需数据,调用接口返回错误,errorMsg={}", modelResult.getErrorMsg());
				return CommonResponse.withErrorResp("调用接口返回错误,errorMsg=" + modelResult.getErrorMsg());
			}
			UserWalletRec userWallet = modelResult.getModel();
			if (userWallet == null) {
				logger.info("苹果应用内购买充值所需数据,userWallet为空");
				return CommonResponse.withErrorResp("苹果应用内购买充值所需数据,userWallet为空");
			}
			ChargePageResponse chargePageResponse = new ChargePageResponse();
			chargePageResponse.setAbleRecScore(userWallet.getAbleRecScore().toString());
			SysConfigProperty sysConfigProperty = getSysConfigByKey(SysConfigPropertyKey.IAP_CHARGE_AMOUNT_LIST, baseRequest.getClientType(),baseRequest.getAgentId());
			String chargeAmountStr = sysConfigProperty.getValue();
			List<IapChargeVo> iapChargeList = JSON.parseArray(chargeAmountStr, IapChargeVo.class);
			chargePageResponse.setIapChargeList(iapChargeList);
			return CommonResponse.withSuccessResp(chargePageResponse);
		} catch (Exception e) {
			logger.info("苹果应用内购买充值所需数据,发生异常,exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/chargeRecScore", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "小程序充值推荐币", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "充值推荐币", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<ChargeResponse> chargeRecScore(ChargeRequst chargeRequst, HttpServletRequest request) {
		try {
			logger.info("充值推荐币,接收到的参数chargeRequst={}", JSON.toJSONString(chargeRequst));
			String chargeAmountStr = chargeRequst.getChargeAmount();
			if (chargeAmountStr.isEmpty()) {
				return CommonResponse.withErrorResp("充值金额不能为空");
			}
			SysConfigProperty sysConfigProperty = getSysConfigByKey(SysConfigPropertyKey.WXXCY_CHARGE_AMOUNT_LIST, chargeRequst.getClientType(),chargeRequst.getAgentId());
			List<String> chargeAmountList = Arrays.asList(sysConfigProperty.getValue().split(";"));
			if (!chargeAmountList.contains(chargeAmountStr)) {
				return CommonResponse.withErrorResp("充值金额不在充值列表项");
			}
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("充值推荐币,未获取到登录用户信息");
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			UserThirdOrder order = new UserThirdOrder();
			order.setUserId(userConsumer.getId());
			order.setUserName(userConsumer.getNickName());
			order.setupFeature("propName", "充值" + chargeAmountStr + "元");
			order.setAmount(new BigDecimal(chargeAmountStr).multiply(new BigDecimal(100)).intValue());
			order.setBizSystem(BizSystem.LOCAL.getIndex());
			order.setChannelIndex(ChannelProxy.weixin_pay.getIndex());
			order.setChargeWayIndex(EsportPayway.WXXCX_PAY.getIndex());
			logger.info("充值推荐币,调用接口参数param={}", JSON.toJSONString(order));
			UserOperationParam userOperationParm = new UserOperationParam();
			userOperationParm.setSellChannel(new Long(chargeRequst.getAgentId()));
			userOperationParm.setClientType(ClientType.WXXCY.getIndex());
			userOperationParm.setOperIp(RequestUtil.getClientIp(request));
			ModelResult<ChargePayRequest> modelResult = userChargeOrderServiceClient.insertUsrChargeOrder(order, userOperationParm);
			if (!modelResult.isSuccess()) {
				logger.info("充值推荐币,调用接口返回错误,errorMsg={},param={}", modelResult.getErrorMsg(), JSON.toJSONString(order));
				return CommonResponse.withErrorResp("调用接口返回错误,errorMsg=" + modelResult.getErrorMsg());
			}
			ChargePayRequest chargePayRequest = modelResult.getModel();
			if (chargePayRequest == null) {
				logger.info("充值推荐币,调用接口返回错误,chargePayRequest == null");
				return CommonResponse.withErrorResp("调用接口返回错误,chargePayRequest == null");
			}
			logger.info("充值推荐币,调用接口返回值chargePayRequest={}", JSON.toJSONString(chargePayRequest));
			String prepayId = chargePayRequest.getPrepayId();
			if (StringUtils.isBlank(prepayId)) {
				logger.info("充值推荐币,调用接口返回错误,prepayId == null");
				return CommonResponse.withErrorResp("调用接口返回错误,prepayId == null");
			}
			
			String timeStamp = WxpayUtil.getTimeStampStr();
			String nonceStr = WxpayUtil.getRandomNonceStr(32);
			ChargeResponse chargeResponse = new ChargeResponse();
			chargeResponse.setTimeStamp(timeStamp);
			chargeResponse.setNonceStr(nonceStr);
			chargeResponse.setPrepayId(prepayId);
			chargeResponse.setPaySign(getSign(timeStamp, nonceStr, prepayId));
			return CommonResponse.withSuccessResp(chargeResponse);
		} catch (Exception e) {
			logger.info("充值推荐币,发生异常,exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/charge", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "h5充值", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "充值", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<H5ChargeResponse> charge(ChargeRequstExpand chargeRequst, HttpServletRequest request) {
		String logPrefix = "充值请求_";
		try {
			logger.info(logPrefix + "请求参数chargeRequst={}", JSON.toJSONString(chargeRequst));
			if (chargeRequst.getBiz()==null || !BizSystem.getAllList().contains(BizSystem.valueOf(chargeRequst.getBiz().intValue()))) {
				return CommonResponse.withErrorResp("业务系统参数错误");
			}
			if (chargeRequst.getClientType()==null || !ClientType.getAllList().contains(ClientType.valueOf(chargeRequst.getClientType().intValue()))) {
				return CommonResponse.withErrorResp("客户端类型参数错误");
			}
			if (chargeRequst.getAgentId() == null) {
				return CommonResponse.withErrorResp("渠道号参数错误");
			}
			String chargeAmountStr = chargeRequst.getChargeAmount();
			if (chargeAmountStr.isEmpty()) {
				return CommonResponse.withErrorResp("充值金额不能为空");
			}
			SysConfigProperty sysConfigProperty = getSysConfigByKey(SysConfigPropertyKey.WXXCY_CHARGE_AMOUNT_LIST,chargeRequst.getClientType(),new Long(chargeRequst.getAgentId()));
			List<String> chargeAmountList = Arrays.asList(sysConfigProperty.getValue().split(";"));
			if (!chargeAmountList.contains(chargeAmountStr)) {
				return CommonResponse.withErrorResp("充值金额不在充值列表项");
			}
			if (chargeRequst.getChargeWay()==null || !EsportPayway.getAllList().contains(EsportPayway.valueOf(chargeRequst.getChargeWay().intValue()))) {
				return CommonResponse.withErrorResp("充值方式参数错误");
			}
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			UserThirdOrder order = new UserThirdOrder();
			order.setUserId(userConsumer.getId());
			order.setUserName(userConsumer.getNickName());
			order.setupFeature("propName", "充值" + chargeAmountStr + "元");
			order.setAmount(new BigDecimal(chargeAmountStr).multiply(new BigDecimal(100)).intValue());
			order.setBizSystem(chargeRequst.getBiz().intValue());
			order.setChannelIndex(EsportPayway.valueOf(chargeRequst.getChargeWay().intValue()).getChannelProxy().getIndex());
			order.setChargeWayIndex(chargeRequst.getChargeWay().intValue());
			logger.info(logPrefix + "调用接口参数order={}", JSON.toJSONString(order));
			UserOperationParam userOperationParm = new UserOperationParam();
			userOperationParm.setSellChannel(new Long(chargeRequst.getAgentId()));
			userOperationParm.setClientType(chargeRequst.getClientType().intValue());
			userOperationParm.setOperIp(RequestUtil.getClientIp(request));
			ModelResult<ChargePayRequest> modelResult = userChargeOrderServiceClient.insertUsrChargeOrder(order, userOperationParm);
			logger.info(logPrefix + "用户id={},用户昵称={},调用充值返回结果modelResult={}", userConsumer.getId(), userConsumer.getNickName(), JSON.toJSONString(modelResult));
			if (!modelResult.isSuccess()) {
				logger.info(logPrefix + "调用接口返回错误,errorCode={},errorMsg={},order={}", modelResult.getErrorCode(), modelResult.getErrorMsg(), JSON.toJSONString(order));
				if (modelResult.getErrorCode().equals("400")) {
					return CommonResponse.withResp(ResponseConstant.BALANCE_NOT_ENOUGH + "", "余额不足");
				}
				return CommonResponse.withErrorResp("调用接口返回错误,errorMsg=" + modelResult.getErrorMsg());
			}
			String requestUrl = "";
			if (chargeRequst.getChargeWay().intValue() == EsportPayway.WXH5_PAY.getIndex()) {
				requestUrl = modelResult.getModel().getRequestUrl();
			}
			return CommonResponse.withSuccessResp(new H5ChargeResponse(true, requestUrl));
		} catch (Exception e) {
			logger.info(logPrefix + "发生异常,exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/h5chooseChargeWay", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "h5查询用户可以选择的充值方式", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "h5查询用户可以选择的充值方式", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<H5ChargeWay>> h5ChoosePayWay(HttpServletRequest request, BaseRequest baseRequest) {
		String logPrefix = "h5可以选择的充值方式_";
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info(logPrefix + "未获取到登录用户信息");
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			Boolean isWeiXinEnv = false;
			String userAgent = request.getHeader("user-agent");
			if (StringUtils.isNotBlank(userAgent) && userAgent.contains("MicroMessenger")) {
				isWeiXinEnv = true;
			}
			logger.info(logPrefix + "登陆用户id={},昵称={}，bizsystem={},当前是微信体系下[{}]", userConsumer.getId(), userConsumer.getNickName(), userConsumer.getBizSystem(), isWeiXinEnv);
			List<H5ChargeWay> resultList = new ArrayList<>();
			if (userManager.getBind(userConsumer.getId(), ThirdType.UBOX) != null) {
				SysConfigProperty sysConfigProperty = getSysConfigByKey(SysConfigPropertyKey.PAYMENT_CHANNEL_SWITCH_UBOX, baseRequest.getClientType(),baseRequest.getAgentId());
				if (sysConfigProperty != null) {
					String value = sysConfigProperty.getValue();
					if (StringUtils.isNotBlank(value) && value.trim().equals("1")) {
						H5ChargeWay uboxPay = H5ChargeWay.UBOX_CHARGE;
						UboxBalanceVo vo = userWalletManager.getUboxBalance(userConsumer.getId());
						if (null != vo) {
							BigDecimal bd = new BigDecimal(vo.getBalance()).divide(new BigDecimal(100));
							double s = bd.setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
							uboxPay.setBalance(s);
						}
						resultList.add(uboxPay);
					}
				}
			} else {
				logger.info(logPrefix + "该用户未绑定友宝，userId={}", userConsumer.getId());
			}
			// 不是微信体系,支持H5支付
			if (!isWeiXinEnv) {
				H5ChargeWay h5Pay = H5ChargeWay.WXH5_PAY;
				resultList.add(h5Pay);
			}
			logger.info(logPrefix + "resultList={}", JSON.toJSONString(resultList));
			return CommonResponse.withSuccessResp(resultList);
		} catch (Exception e) {
			logger.info(logPrefix + "发生异常exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}
	
	private String getSign(String timeStamp, String nonceStr, String prepayId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appId", appId);
		params.put("timeStamp", timeStamp);
		params.put("nonceStr", nonceStr);
		params.put("package", "prepay_id=" + prepayId);
		params.put("signType", "MD5");
		String sign = WxpayUtil.createMd5Sign(params, signKey);
		logger.info("唤起微信支付参数timeStamp={},nonceStr={},prepayId={},appId={},signKey={},sign={}",
				timeStamp, nonceStr, prepayId, appId, signKey, sign);
		return sign;
	}
	
	public static void main(String[] args) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appId", "wxd678efh567hg6787");
		params.put("timeStamp", "1490840662");
		params.put("nonceStr", "5K8264ILTKCH16CQ2502SI8ZNMTM67VS");
		params.put("package", "prepay_id=wx2017033010242291fcfe0db70013231072");
		params.put("signType", "MD5");
		String s1 =  WxpayUtil.createMd5Sign(params, "qazwsxedcrfvtgbyhnujmikolp111111");
		System.out.println(s1);
		System.out.println(s1.equals("22D9B4E54AB1950F51E0649E8810ACD6"));
	}
	
}
