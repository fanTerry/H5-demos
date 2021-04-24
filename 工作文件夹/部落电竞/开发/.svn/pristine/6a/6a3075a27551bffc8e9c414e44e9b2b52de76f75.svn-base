package com.esportzoo.esport.controller.payment;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.esport.client.service.charge.UserChargeOrderServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.payment.H5UserChargeRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.charge.H5ChargeV2Response;
import com.esportzoo.esport.connect.response.payment.ChargeMoneyConfigVo;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.constants.*;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.SysConfigProperty;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.charge.UserThirdOrder;
import com.esportzoo.esport.hd.constants.HdCode;
import com.esportzoo.esport.util.RequestUtil;
import com.esportzoo.esport.vo.ChargePayRequest;
import com.esportzoo.esport.vo.ReceiptValidateVo;
import com.esportzoo.esport.vo.UsrChargeReturnLog;
import com.esportzoo.esport.vo.charge.IapChargeVo;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 新版H5充值相关接口
 *
 * @author wujing
 * @date 2019年8月15日 上午11:03:25
 */
@Controller
@RequestMapping("h5charge")
@Api(value = "新版H5充值相关接口", tags = { "新版H5充值相关接口" })
public class H5ChargeController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(H5ChargeController.class);
	@Autowired
	private UserChargeOrderServiceClient userChargeOrderServiceClient;

	@Value("${interface.app.domain}")
	private String interfaceAppDomain;

	@Value("${touch.app.domain}")
	private String touchAppDomain;

	@Autowired
	RedisClient redisClient;

	@RequestMapping(value = "/tocharge", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "h5充值", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "充值", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<H5ChargeV2Response> charge(H5UserChargeRequest userChargeReq, HttpServletRequest request) {//渠道改为用户注册时渠道
		String logPrefix = "h5充值请求_";
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			//防用户频繁重复提交请求
			if (redisClient.get("prevent_repeated_request"+userConsumer.getId())!=null){
				return CommonResponse.withErrorResp("请不要频繁重复请求");
			}
			redisClient.set("prevent_repeated_request"+userConsumer.getId(),"1",3);

			logger.info(logPrefix + "请求参数chargeRequst={}", JSON.toJSONString(userChargeReq));
			if (userChargeReq.getBiz() == null || !BizSystem.getAllList().contains(BizSystem.valueOf(userChargeReq.getBiz().intValue()))) {
				return CommonResponse.withErrorResp("业务系统参数错误");
			}
			if (userChargeReq.getClientType() == null || !ClientType.getAllList().contains(ClientType.valueOf(userChargeReq.getClientType().intValue()))) {
				return CommonResponse.withErrorResp("客户端类型参数错误");
			}
			if (userChargeReq.getAgentId() == null) {
				return CommonResponse.withErrorResp("渠道号参数错误");
			}
			String chargeAmountStr = userChargeReq.getChargeAmount();
			if (chargeAmountStr.isEmpty()) {
				return CommonResponse.withErrorResp("充值金额不能为空");
			}
			UserThirdOrder order = new UserThirdOrder();
			if (userChargeReq.getClientType()==ClientType.WEB.getIndex()) {
				//扫码支付充值
				SysConfigProperty configProperty = getSysConfigByKey(SysConfigPropertyKey.CHARGE_MONEY_CONFIG,userChargeReq.getClientType(),userChargeReq.getAgentId());
				List<ChargeMoneyConfigVo> amountList=JSON.parseArray(configProperty.getValue(), ChargeMoneyConfigVo.class);
				boolean isExistAmount=Boolean.FALSE;
				ChargeMoneyConfigVo chargeMoneyConfigVo = null;
				for (ChargeMoneyConfigVo vo : amountList) {
					if (chargeAmountStr.equals(vo.getMoney())) {
						isExistAmount=Boolean.TRUE;
						chargeMoneyConfigVo = vo;
						break;
					}
				}
				if (!isExistAmount) {
					return CommonResponse.withErrorResp("充值金额不在充值列表项");
				}
				//针对充值有赠送星星的订单，对应活动103
				if (chargeMoneyConfigVo.getSendStar()>0){
					logger.info(logPrefix+"用户：【{}】，扫码充值的赠送前端传递参数 【{}】",userConsumer.getId(),JSON.toJSONString(chargeMoneyConfigVo));
					order.setupFeature("hdcode", HdCode.HD_103.getIndex());
				}
			} else {
				List<String> chargeAmountList = Lists.newArrayList();
				if (userChargeReq.getClientType().intValue() == ClientType.IOS.getIndex()) {// ios渠道
					SysConfigProperty sysConfigProperty = getSysConfigByKey(SysConfigPropertyKey.IAP_CHARGE_AMOUNT_LIST,userChargeReq.getClientType(),userChargeReq.getAgentId());
					List<IapChargeVo> chargeResList = JSON.parseArray(sysConfigProperty.getValue(), IapChargeVo.class);
					chargeAmountList = chargeResList.stream().map(c -> c.getChargeAmount()).collect(Collectors.toList());
				} else {
					SysConfigProperty sysConfigProperty = getSysConfigByKey(SysConfigPropertyKey.WXXCY_CHARGE_AMOUNT_LIST,userChargeReq.getClientType(),userChargeReq.getAgentId());
					chargeAmountList = Arrays.asList(sysConfigProperty.getValue().split(";"));
				}
				if (!chargeAmountList.contains(chargeAmountStr)) {
					return CommonResponse.withErrorResp("充值金额不在充值列表项");
				}
			}
			if (userChargeReq.getChargeWay() == null || !EsportPayway.getAllList().contains(EsportPayway.valueOf(userChargeReq.getChargeWay().intValue()))) {
				return CommonResponse.withErrorResp("充值方式参数错误");
			}
			int userChargeWay = userChargeReq.getChargeWay().intValue();

			//标记充值赠送
			order.setupFeature("hdcode", HdCode.HD_103.getIndex());
			order.setUserId(userConsumer.getId());
			order.setUserName(userConsumer.getNickName());
			order.setupFeature("propName", "充值" + chargeAmountStr + "元");
			order.setAmount(new BigDecimal(chargeAmountStr).multiply(new BigDecimal(100)).intValue());
			order.setBizSystem(userChargeReq.getBiz().intValue());
			order.setChannelIndex(EsportPayway.valueOf(userChargeWay).getChannelProxy().getIndex());
			order.setChargeWayIndex(userChargeWay);
			logger.info(logPrefix + "调用接口参数order={}", JSON.toJSONString(order));
			UserOperationParam userOperationParm = new UserOperationParam();
			userOperationParm.setSellChannel(userConsumer.getChannelNo());
			//处理充值回调地址
			String redirectUrl = buildRedirectUrl(userChargeWay, userChargeReq.getRedirectUrl());
			userOperationParm.setWapUrl(redirectUrl);
			userOperationParm.setClientType(userChargeReq.getClientType().intValue());
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
            H5ChargeV2Response h5ChargeResponse = new H5ChargeV2Response();
            BeanUtils.copyProperties(modelResult.getModel(), h5ChargeResponse);
            if (userChargeWay == EsportPayway.Ali_H5_PAY.getIndex()) {
            	if (StringUtils.isNotEmpty(modelResult.getModel().getForm())){
					String form = modelResult.getModel().getForm();
					int length = form.length();
					String temp= form.substring(0,length - 44);//<script>document.forms[0].submit();</script>
//					logger.info(logPrefix + "截取后={}",temp);
					h5ChargeResponse.setRequestUrl(temp);
				}
            }
            h5ChargeResponse.setChargeWay(userChargeWay);
            h5ChargeResponse.setSuccessFlag(true);
            //记录跳转地址
			recordRedirectUrl(modelResult.getModel().getPayNo(), userChargeReq.getRedirectUrl());
            return CommonResponse.withSuccessResp(h5ChargeResponse);
        } catch (Exception e) {
            logger.info(logPrefix + "发生异常,exception={}", e.getMessage(), e);
            return CommonResponse.withErrorResp(e.getMessage());
        }
    }

	@RequestMapping(value = "/applePayCallBack", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "applePay充值回调", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "applePay充值回调", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<Boolean> applePayCallBack(ReceiptValidateVo receiptValidateVo, BaseRequest baseRequest, HttpServletRequest request) {
		String logPrefix = "applePay充值回调请求_";
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			if (StringUtils.isBlank(receiptValidateVo.getPayNo())) {
				return CommonResponse.withErrorResp("订单编号不能为空");
			}
			if (StringUtils.isBlank(receiptValidateVo.getReceiptData())) {
				return CommonResponse.withErrorResp("必要参数不能为空");
			}
			ModelResult<UsrChargeReturnLog> modelResult = userChargeOrderServiceClient.validateReceiptWithAppStore(receiptValidateVo);
			logger.info(logPrefix + "用户id={},用户昵称={},请求参数payNo:{},返回结果modelResult={}", userConsumer.getId(), userConsumer.getNickName(), receiptValidateVo.getPayNo(), JSON.toJSONString(modelResult));
			if (!modelResult.isSuccess() && null == modelResult.getModel()) {
				return CommonResponse.withErrorResp("充值回调接口异常");
			}
			return CommonResponse.withSuccessResp(Boolean.TRUE);
		} catch (Exception e) {
			logger.info(logPrefix + "发生异常,exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	/*@RequestMapping(value = "/toJSAPICharge", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "h5充值", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "充值", response = CommonResponse.class)
	@ResponseBody
	public @ResponseBody CommonResponse<Map<String, String>> toWxCharge(HttpServletRequest request, H5UserChargeRequest chargeRequst) {
		try {
			String logPrefix = "公众号微信充值_";
			logger.info(logPrefix + "请求参数chargeRequst={}", JSON.toJSONString(chargeRequst));
			if (chargeRequst.getBiz() == null || !BizSystem.getAllList().contains(BizSystem.valueOf(chargeRequst.getBiz().intValue()))) {
				return CommonResponse.withErrorResp("业务系统参数错误");
			}
			if (chargeRequst.getClientType() == null || !ClientType.getAllList().contains(ClientType.valueOf(chargeRequst.getClientType().intValue()))) {
				return CommonResponse.withErrorResp("客户端类型参数错误");
			}
			if (chargeRequst.getAgentId() == null) {
				return CommonResponse.withErrorResp("渠道号参数错误");
			}
			String chargeAmountStr = chargeRequst.getChargeAmount();
			if (chargeAmountStr.isEmpty()) {
				return CommonResponse.withErrorResp("充值金额不能为空");
			}
			SysConfigProperty sysConfigProperty = getSysConfigByKey(SysConfigPropertyKey.WXXCY_CHARGE_AMOUNT_LIST,chargeRequst.getClientType());
			List<String> chargeAmountList = Arrays.asList(sysConfigProperty.getValue().split(";"));
			if (!chargeAmountList.contains(chargeAmountStr)) {
				return CommonResponse.withErrorResp("充值金额不在充值列表项");
			}
			if (chargeRequst.getChargeWay() == null || !EsportPayway.getAllList().contains(EsportPayway.valueOf(chargeRequst.getChargeWay().intValue()))) {
				return CommonResponse.withErrorResp("充值方式参数错误");
			}
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			// 组装参数，创建订单发起充值支付
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
			if (!modelResult.isSuccess() || modelResult.getModel() == null) {
				return CommonResponse.withErrorResp("创建订单异常");
			}
			ChargePayRequest chargePayReq = modelResult.getModel();
			Map<String, String> params = new HashMap<>();
			params.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
			params.put("appId", wxpayEnvBean.getAppId());
			params.put("nonceStr", WxpayUtil.getRandomNonceStr());
			params.put("package", "prepay_id=" + chargePayReq.getPrepayId());
			params.put("signType", "MD5");
			String secretKey = wxpayEnvBean.getKey();
			params.put("paySign", WxpayUtil.createMd5Sign(params, secretKey));
			return CommonResponse.withSuccessResp(params);
		} catch (BusinessException bizEx) {
			return CommonResponse.withErrorResp(bizEx.getMessage());
		} catch (Exception e) {
			return CommonResponse.withErrorResp("系统异常，请稍后再试");
		}
	}
*/

	/**
	 * 记录跳转地址
	 * @param payNo  充值订单流水号
	 * @param redirectUrl  充值完成后的跳转地址
	 */
	private void recordRedirectUrl(String payNo, String redirectUrl){
		if (StringUtils.isBlank(payNo) || StringUtils.isBlank(redirectUrl)) {
			logger.warn("记录跳转地址参数为空，payNo={},redirectUrl={}", payNo, redirectUrl);
			return;
		}
		try {
			String key = StrUtil.format(CacheKeyConstant.CHARGE_REDIRECT_URL_KEY, payNo);
			redisClient.set(key, redirectUrl, CacheKeyConstant.CHARGE_REDIRECT_URL_EXPIRE);
		} catch (Exception e) {
			logger.error("记录跳转地址异常，payWay={},redirectUrl={}", payNo, redirectUrl);
			e.printStackTrace();
		}
	}

	/**
	 * 处理充值回跳地址
	 * @param payWay  @see EsportPayway
	 * @param redirectUrl 前端传过来的跳转地址
	 * @return
	 */
	private String buildRedirectUrl(Integer payWay, String redirectUrl){
		String returnUrl = null;
		//兼容其他支付方式
		if (StringUtils.isNotBlank(redirectUrl)) {
			returnUrl = redirectUrl;
		}
		//TODO 先处理银联的跳转地址 2020-05-29 11:35 linzheng
		if (EsportPayway.UMS_H5_PAY.getIndex() == payWay.intValue()) {
			returnUrl = interfaceAppDomain + "/charge/thirdPayCallBack/" + payWay.toString();
		}
		//默认钱包流水页面地址
		if (StringUtils.isBlank(returnUrl)) {
			returnUrl = touchAppDomain + "/wallet";
		}
		logger.info("充值方式[{}],跳转地址[{}],最终跳转地址[{}]",payWay, redirectUrl, returnUrl);
		return returnUrl;
	}
}
