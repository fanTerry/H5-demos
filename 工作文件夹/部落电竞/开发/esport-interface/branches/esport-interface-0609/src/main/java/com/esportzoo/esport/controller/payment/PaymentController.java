package com.esportzoo.esport.controller.payment;

import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.esport.client.service.charge.UserChargeOrderServiceClient;
import com.esportzoo.esport.client.service.charge.UserChargeWayServiceClient;
import com.esportzoo.esport.client.service.charge.UserThirdOrderServiceClient;
import com.esportzoo.esport.client.service.common.SysConfigPropertyServiceClient;
import com.esportzoo.esport.client.service.wallet.UserWalletRecServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.payment.UserPayStatusRequest;
import com.esportzoo.esport.connect.request.payment.UserPayWayRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.payment.ChargeMoneyResponse;
import com.esportzoo.esport.connect.response.payment.PayOrderStatusResponse;
import com.esportzoo.esport.connect.response.payment.UserPayWayResponse;
import com.esportzoo.esport.constants.ClientType;
import com.esportzoo.esport.constants.EsportPayway;
import com.esportzoo.esport.constants.SysConfigPropertyKey;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.SysConfigProperty;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.charge.UserThirdOrder;
import com.esportzoo.esport.manager.UserManager;
import com.esportzoo.esport.manager.UserWalletManager;
import com.esportzoo.esport.vo.ThirdOrderQueryVo;
import com.esportzoo.esport.vo.charge.UserChargeWayDTO;
import com.esportzoo.esport.vo.charge.UserChargeWayListQueryVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * 新版支付相关接口
 * 
 * @author wujing
 * @date 2019年8月15日 上午11:03:25
 */
@Controller
@RequestMapping("payment")
@Api(value = "新版支付相关接口", tags = { "新版支付相关接口" })
public class PaymentController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
	@Autowired
	private UserManager userManager;
	@Autowired
	private UserWalletManager userWalletManager;
	@Autowired
	private UserChargeOrderServiceClient userChargeOrderServiceClient;
	@Autowired
	private UserWalletRecServiceClient userWalletServiceClient;
	@Autowired
	private UserChargeWayServiceClient userChargeWayServiceClient;
	@Autowired
	private UserThirdOrderServiceClient userThirdOrderServiceClient;
	
	@Autowired
	private SysConfigPropertyServiceClient sysConfigPropertyServiceClient;

	@RequestMapping(value = "/getPayWayList", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "查询用户支付方式列表", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "查询用户可以选择的支付方式", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<UserPayWayResponse>> getPayWayList(HttpServletRequest request, UserPayWayRequest userPayWayRequest) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			List<UserPayWayResponse> resultList = new ArrayList<UserPayWayResponse>();
			if (userConsumer == null) {
				logger.info("查询支付方式列表接口，未获取到登录用户信息");
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			if (null == userPayWayRequest.getNeedRecPay()) {// 默认都有星星支付
				userPayWayRequest.setNeedRecPay(true);
			}
			userPayWayRequest.setUserId(userConsumer.getId());
			UserChargeWayListQueryVo queryVo = new UserChargeWayListQueryVo();
			BeanUtils.copyProperties(userPayWayRequest, queryVo);
			logger.info("查询支付方式列表接口,请求参数,userPayWayRequest={}", JSONObject.toJSONString(userPayWayRequest));
			ModelResult<List<UserChargeWayDTO>> modelResult = userChargeWayServiceClient.queryChargeWayListByUserId(queryVo);
			logger.info("查询支付方式列表接口,接口返回值,用户id={},modelResult={}", userConsumer.getId(), JSONObject.toJSONString(modelResult));
			if (null == modelResult || !modelResult.isSuccess() || null == modelResult.getModel()) {
				logger.info("查询支付方式列表接口,调用queryChargeWayListByUserId接口异常,用户id:{},modelResult={}", userConsumer.getId(), JSONObject.toJSONString(modelResult));
				return CommonResponse.withErrorResp("服务繁忙,请稍后再试~");
			}
			List<UserChargeWayDTO> dtoList = modelResult.getModel();
			for (UserChargeWayDTO dto : dtoList) {
				if (dto.getPayIndex().intValue() == EsportPayway.WXH5_PAY.getIndex()) {
					String userAgent = request.getHeader("user-agent");
					if ((StringUtils.isNotBlank(userAgent) && userAgent.contains("MicroMessenger")) 
						|| userPayWayRequest.getClientType().intValue() == ClientType.IOS.getIndex()) {// 支付方式是微信H5且是微信体系下的,需要屏蔽12.30wj新增ios屏蔽
						continue;
					}
				}
				UserPayWayResponse resp = new UserPayWayResponse();
				BeanUtils.copyProperties(dto, resp);
				resultList.add(resp);
			}
			return CommonResponse.withSuccessResp(resultList);
		} catch (Exception e) {
			logger.info("查询支付方式列表接口，发生异常，exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}


	@RequestMapping(value = "/getChargeMoneyList", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "竞猜获取充值金额列表", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "竞猜获取充值金额列表", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<ChargeMoneyResponse>> getChargeMoneyList(HttpServletRequest request, BaseRequest baseRequest) {
		SysConfigProperty propertyByKey = sysConfigPropertyServiceClient.getSysConfigPropertyByKey(SysConfigPropertyKey.CHARGE_MONEY_CONFIG,baseRequest.getClientType(),baseRequest.getAgentId());
		String value = propertyByKey.getValue();
		if (StringUtils.isEmpty(value)){
			logger.error("没有配置充值金额列表");
			return CommonResponse.withErrorResp("系统繁忙");
		}
		List<ChargeMoneyResponse> responses = null;
		try {
			responses = JSONObject.parseArray(value, ChargeMoneyResponse.class);
		} catch (Exception e) {
			logger.error("没有配置充值金额列表，出现异常，异常参数:{}",value);
			e.printStackTrace();
		}
		return CommonResponse.withSuccessResp(responses);
	}

	@RequestMapping(value = "/getThirdPayStatus", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "查询第三方订单支付状态", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "查询第三方支付订单状态信息", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<PayOrderStatusResponse> getThirdPayStatus(HttpServletRequest request,
			UserPayStatusRequest userPayStatusRequest) {
		try {
			PayOrderStatusResponse payOrderStatusResponse = new PayOrderStatusResponse();
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("查询第三方订单支付状态接口，未获取到登录用户信息");
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			if (null == userPayStatusRequest.getPayNo()) {
				logger.info("查询第三方订单支付状态接口，未获取到第三方支付订单号");
				return CommonResponse.withErrorResp("未获取到第三方支付订单号");
			}
			logger.info("查询第三方订单支付状态接口,请求参数,payNo={}", userPayStatusRequest.getPayNo());
			ThirdOrderQueryVo queryVo = new ThirdOrderQueryVo();
			queryVo.setPayNo(userPayStatusRequest.getPayNo());
			queryVo.setUserId(userConsumer.getId());
			ModelResult<List<UserThirdOrder>> modelResult = userThirdOrderServiceClient.queryListByCondition(queryVo);
			if (!modelResult.isSuccess() ||modelResult.getModel()==null || modelResult.getModel().size()==0) {
				logger.info("查询第三方订单支付状态接口,没有查询到对应的订单,用户id:{},PayNo={}", userConsumer.getId(), userPayStatusRequest.getPayNo());
				payOrderStatusResponse.setStatus(3);
				return CommonResponse.withSuccessResp(payOrderStatusResponse);
			}
			List<UserThirdOrder> userThirdOrders = modelResult.getModel();
			UserThirdOrder userThirdOrder = userThirdOrders.get(0);
			payOrderStatusResponse.setStatus(userThirdOrder.getPayStatus());
			payOrderStatusResponse.setWalletRec(userWalletManager.getUserWalletRec(userConsumer.getId()).getAbleRecScore());
			return CommonResponse.withSuccessResp(payOrderStatusResponse);
		} catch (Exception e) {
			logger.info("查询第三方订单支付状态接口，发生异常，exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}
}
