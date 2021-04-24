/*
package com.esportzoo.esport.controller.payment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.esport.client.service.charge.UserChargeOrderServiceClient;
import com.esportzoo.esport.client.service.charge.UserThirdOrderServiceClient;
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
import com.esportzoo.esport.vo.UsrChargeReturnLog;
import com.esportzoo.esport.vo.charge.IapChargeVo;
import com.google.common.collect.Lists;
import com.yeepay.g3.sdk.yop.client.YopRequest;
import com.yeepay.g3.sdk.yop.client.YopResponse;
import com.yeepay.g3.sdk.yop.client.YopRsaClient;
import com.yeepay.g3.sdk.yop.encrypt.CertTypeEnum;
import com.yeepay.g3.sdk.yop.encrypt.DigestAlgEnum;
import com.yeepay.g3.sdk.yop.encrypt.DigitalSignatureDTO;
import com.yeepay.g3.sdk.yop.utils.DigitalEnvelopeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

*/
/**
 * @Description alipay调试
 * @Author zheng.lin
 * @Date 2020/4/10 12:23
 *//*

@RequestMapping("/pay")
@Controller
public class AlipayController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(AlipayController.class);

	@Autowired
	private UserChargeOrderServiceClient userChargeOrderServiceClient;

	@Autowired
	private UserThirdOrderServiceClient userThirdOrderServiceClient;

	@RequestMapping("/payReturn")
	public CommonResponse<UsrChargeReturnLog> payReturn(HttpServletRequest request, HttpServletResponse response){
		for(Object key:request.getParameterMap().keySet()){
			logger.info("{}={}",key,request.getParameter((String)key));
		}
		//String outTradeNo = request.getParameter("out_trade_no");
		//UserOperationParam userOperationParam = new UserOperationParam();
		//ModelResult<UsrChargeReturnLog> result = userThirdOrderServiceClient.queryThirdPay(outTradeNo, userOperationParam);
		//return CommonResponse.withSuccessResp(result.getModel());
		UsrChargeReturnLog usrChargeReturnLog = new UsrChargeReturnLog();
		return CommonResponse.withSuccessResp(usrChargeReturnLog);
	}

    @RequestMapping(value = "/webAndH5Pay", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
    public void webAndH5Alipay(HttpServletRequest request, HttpServletResponse response){
		String serverUrl = "https://openapi.alipay.com/gateway.do";
		String appId = "2021001157644347";
		String appPrivateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDPrqqTzc2CcSiX2VfJOtLuZWvRGYHCkjV+3TCBe6qhI9iBvAITct/jVAFiLRCh0iwiGprlWN/JWKIDQVU3m32aLNJaF0ByMSdWDEgHeTWdqNR867hUL62uOcYe8u9tgUyBPt+wl+mTFxT6jeQBE279qQrFN6j/4Zqb8BOelM8V5aVmFoA6nvT0wPLaO2KHz7Wz5FiWHC6BXFf4Lo2DDIQj0khg/uf30E7Z8bZT4hroIjonoyJY8fFT5x9X3F8zyV2bF1gSpereyKXbvi53NNoceMMWkJB+Vdg9rMYkcay8tU1BD26s4YDaKKCygWgoKQzM/i6McU82YEtFH5zU/EcvAgMBAAECggEAets320HGzeCFA/c1wDvFJfPnaQXuxhxZ+xd8J0zx7mtXG8ANdlXhbgh23Q4NpbzAENiuQvhlQiXliYlvkPqxva/ALD1PCX5cZ8HSJmGkvwRt1wxWua6Ozt8sfYh27M+tY1O/XicvrKzd4y5m7+AAK5yBW5OGrQBXxuGpqYkcTDXB6P00BkNeLFNn0IVrvBNuA5vxVNzDGG2p8gnUNCxgrKzoWHwPdMDielQ5IXTPopyfLc+a//kcyTlO+SQhQYIOvdjkqkJYCQBLOV4dbLe5jBva26HYnzer8uNJa8O2L1iEzh3wtZPhZtf9YzKVX9FrbB/9pLc109QIP+tiyHkuUQKBgQDm5jNWzzkzrZF9q/61BZj/y1oa/kt+vkGGoJ6YYFLNoyiVxe56vdrC74Say3gERXR9ji9a0+5EEV1M17OLTyUVEu90Hlj90Mg7WDQk4YrVm0+y0i5gME8JvtUH6q1kTnElt3iIj5VEDOdGHDe5akf/trRT6HY+NNkxJfNqBCpfJwKBgQDmQlnrpmUE51KcwRCFOBrRaRXGBEdpon46BlZy1Zv5GHyarE8f5ArhR9m21FGYs+lOQcGjnTUyJbpZFZicY49ein7q3DmhJFF9JMtHUATgRheNUnoNECH6TdoZL0Dn3W07Zv80U3rZh+ffRwQ01k5F9qtpVoNGIOnrKqh1sajcuQKBgHzdhmvuml+BAIn2pkJcqZXC8ZJhbJA48RSN7dY3WPANyNfd9w53d0Mt5gcT+25L5Sg06RT0QQicX1k72MOAZGxWFho0gymXa4D0PRaoxbyoRdxNX3jzBvsdMPUcCvKQnQ2pf0xSYYUweS1n+xaRDCiUNEagErmnOMEkZdMyN3vHAoGBAIaHaaYGl6rfxCKVoiNTfkevT+tbgKW1LG6WWVfKedB1gAeWeLw3LiwlIIxzTCEdrEfun0YKiZP8+v0lYv6sJI2l+TUa6gi+AWZOYv4NQGwQR55n+6s6K7/kUHK6av+NHU7BhIV76KDDiLOo+Eouk5jI36kxxIzqmgCt8bKh0/jxAoGAP3BfE5UTDCu5uV+e2rZKbougCE+wbchh7UPBrG2Vnylw0dsAhrfkfETAnqIxmpGqGL4NUwkjgznNUj/uJ9XUYQPbVhuNpXiGR9Ozrbe6+4q626pBg3Hq7gRZkqiqIh3HAb9xyyUHQyzzhEn57Fi3FJlueRyce7dvnfQhKG2H7Es=";
		String format = "json";
		String charset = "UTF-8";
		String alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAldtc/iT/uUUEqCOmFT9BnFdyAB2RmBmxsiFigYyJmsQBhoBQHY+qEWdPLAgJV+0Jo/xm3iEXUalYJBsNysskE9pFlAh5PAiSqY/0oyAcrLq+GvIY8xFKFjOQnt7FRi0ulF994ELRZet+shdtWU2xCIUsgrrwD83xJ1SmOc/eufylb2N+dat0rHRXwxjD/0MMg/yCQc/h+QxUYv0iCIzJjn/IGPHp6HTWB+JtoymaSjxq9S7P7qbWdzOC6+vkorHrViJTDrrD/Mi+soUrcz7VUU1RF/t8117jpqiqarlAT/IiEdtY6XU361f7J35nDxvgTHSlcY5cW4wj1D9ow3r3hQIDAQAB";
		String signType = "RSA2";

		AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, appId, appPrivateKey, format, charset, alipayPublicKey, signType); //获得初始化的AlipayClient
		AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
		alipayRequest.setReturnUrl("http://daily-api.esportzoo.com/alipay/payReturn");
		alipayRequest.setNotifyUrl("http://daily-paygateway.esportzoo.com/alipay/paynotify");//在公共参数中设置回跳和通知地址
		alipayRequest.setBizContent("{" +
				" \"out_trade_no\":\"20200320010101001\"," +
				" \"total_amount\":\"0.01\"," +
				" \"subject\":\"Iphone6 16G\"," +
				" \"product_code\":\"QUICK_WAP_PAY\"" +
				" }");//填充业务参数
		String form="";
		try {
			form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		response.setContentType("text/html;charset=" + charset);
		try {
			response.getWriter().write(form);//直接将完整的表单html输出到页面
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}

    }

	@RequestMapping(value = "/charge", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	public CommonResponse<H5ChargeV2Response> charge(H5UserChargeRequest userChargeReq, HttpServletRequest request,HttpServletResponse response) {
		String logPrefix = "h5充值请求_";
		try {
			//UserConsumer userConsumer = getLoginUsr(request);
			UserConsumer userConsumer = new UserConsumer();
			userConsumer.setId(145656L);
			userConsumer.setNickName("test2755");
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
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
			List<String> chargeAmountList = Lists.newArrayList();

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
			userOperationParm.setSellChannel(new Long(userChargeReq.getAgentId()));
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
			String form = modelResult.getModel().getForm();
			response.setContentType("text/html;charset=UTF-8");
			try {
				response.getWriter().write(form);//直接将完整的表单html输出到页面
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}


		} catch (Exception e) {
			logger.info(logPrefix + "发生异常,exception={}", e.getMessage(), e);
		}
		return null;
	}

	//==================================== yeepay =================================================
	//merchant_private_key商户私钥
	public static final String merchant_private_key="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCjmh71qnLPofhDW5cvQg/5TZPrkXNWCqeVEHATUcywzyXS6/cXOSPPTrbbYhoSs8+CUt95CxlncXj0icFch2/Nqjztvl/WSRR960zn3pjGn5ptwboLe9S+Ax22bUgArmZmNM2rOu9n6il9HoKJQMVeyRIZylJch6o3JcdQrbcYxvXNVFrpkJn12aOh06LoQlDD2BPJokPKTu3JIJGP0xbt2sc18TYlMm6brzUCfVkoKfff17W0FGwTQgp8+pRXTf8cjeku5MGZTmQc4rgDZ5fQh69qAiWypm7j/J/RvvFTlYp4zceLJx2sGu0aZteRKYVEHvxv1SuwUAheXa0k1zVdAgMBAAECggEADonIcjH1OutHSbZkFHXX6o0QyzBlN0JXdbZ8vH1v5aRlgD1U0KeqEtGJYVcclF3jNPm9VHR+i5k4qO5sPuyQLGQSPwREYEv6j11aEQl7D7zTdykYfyMVcCnhuY+I2aVQgd5JCowrBdPNX7TW38XgOYCVDwf80q6Bm0ihp33lv+9Fv5Fs2wMfzHuiJSYg0WrZ0Exa63QsRgAmevOGvPekqVDps5tDdClPJaGmEjN5Qs0Y3QfVS2aByRhSH4+5fJvorVzCut3FGqaTg7pGeIjqB3iq8VIGNmWM8KnuCItYzGLhN1ShycNIkEzGdGnG6/OAwiwtayOxTFbsAog+fNKWZwKBgQDgrxKfv4kHj1KpFA9XEJoley2mI/FM6qcmP51CVOsVTQjPdPMMJh42FCvqKSCuFCNN8/J65PUHebf/WT+Cz8PTcrzeUiI9mi8Y+lMDtBwqRX2WdlhLGeH68vNWAmtEUGjm810LczoKnNwOcx9Qreo+rJoNYxkr3qeWzPICThnh6wKBgQC6Z5eGQ6o3AjC8d2FojyY03Cgz2g4T8ZukB6QKwnJOFh3yfEgiW8QcF3ID/TqrQbJTDmStTN0n4gcIqUH6iE5l5RTBLU/EMT9jxFzdxvOTkFtg2DEOys0cwemWINJWoEUak+MTvdq62wYpND2DC8lUP1+ACUsiY9XskAZWangr1wKBgB0rHUPX3bY5iQWiMQuggCJ1h0uWRvExVVnF0GiYFZzCT25/RD5DhJCItdrEVBXFT4ADN2t05cp+psBjmbclucptjs9d4kwVBCA9/yDv9OgX9WHfIz88sPWfLK/xyHMp7TRuS5n874AJNRg7Icmbillwlt8+CjqDwUX2fUSHVxzZAoGBAIfGHOB3OOyIc+dTLWhznGi0U7QxCEAOpEd+KUaNC6VFBtsxG3mrTdUIXsv5D1rep31H5p00d4ItaOIuJxjNYA1oJw3ua0OEe5+3z8zatWCurWCFGHal0XE47WOk1JC0fKE3RxeCGmgwUPSCUKu7UhJDdtRT8J5ECfYdkgAv3QhRAoGBALt0WE1gzydJ72ItSI4AnotANem+jqNw6oWWvYK/QeAOmhIPOy0HqwtHChhLwBjJ8u/oHBo7VlsCyedtdJ7r1fQaoHs0+8V/kOQ6TYj7X1/r8n229mqpUVL/faQzlQyuUUaMQSNk0W/jh9WLiN6KV5b7UVHkpSuztughSwJmQDZ2";
	//appKey应用
	public static final String appKey="OPR:10022945106";
	public static final String[] CASHIER = {"merchantNo","token","timestamp","directPayType","cardType","userNo","userType","ext"};

	@RequestMapping(value = "/yeepay", produces = "application/json; charset=utf-8", method = RequestMethod.GET)
	public CommonResponse<String> yeepay(HttpServletRequest httpRequest, HttpServletResponse httpResponse){

		YopRequest request = new YopRequest(appKey,merchant_private_key);
		//g构造请求参数
		request.addParam("parentMerchantNo","10022945106");
		request.addParam("merchantNo","10022945106");
		request.addParam("orderId","TestNo2020051510062100001");
		request.addParam("orderAmount", "0.01");
		request.addParam("redirectUrl", "http://daily-api.esportzoo.com/pay/payReturn");
		request.addParam("notifyUrl", "http://daily-paygateway.esportzoo.com/yeepay/paynotify");
		request.addParam("goodsParamExt", "{\"goodsName\":\""+"充值测试"+"\",\"goodsDesc\":\""+"技术开发调试测试"+"\"}");
		YopResponse response = null;// 发送请求
		try {
			response = YopRsaClient.post("/rest/v1.0/std/trade/order", request);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(response.toString());//输出返回
		Map<String,String> result = new HashMap<>();
		//解析返回
		if("FAILURE".equals(response.getState())){
			if(response.getError() != null){
				result.put("code",response.getError().getCode());
				result.put("message",response.getError().getMessage());
			}
		}
		System.out.println("result=" + response.getStringResult());
		try {
			System.out.println("sign_result=" + response.isValidSign());
			JSONObject bizResult = JSON.parseObject(response.getStringResult());
			String parentMerchantNo = bizResult.getString("parentMerchantNo");
			String merchantNo = bizResult.getString("merchantNo");
			String orderId = bizResult.getString("orderId");
			String uniqueOrderNo = bizResult.getString("uniqueOrderNo");
			String token = bizResult.getString("token");
			System.out.println("parentMerchantNo=" + parentMerchantNo);
			System.out.println("merchantNo=" + merchantNo);
			System.out.println("orderId=" + orderId);
			System.out.println("uniqueOrderNo=" + uniqueOrderNo);
			System.out.println("token=" + token);

			//跳转收银台
			String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
			String directPayType = "";
			String cardType = "DEBIT";//限制交易的卡类型,枚举值：DEBIT：借记卡	CREDIT：信用卡
			String userNo = "1";//用户标识，必填，用户在商户的唯一标识。
			String userType	 = "USER_ID";//用户ID
			String ext	 = "";
			Map<String,String> params = new HashMap<>();
			//{"merchantNo","token","timestamp","directPayType","cardType","userNo","userType","ext"};
			params.put("merchantNo",merchantNo);
			params.put("token",token);
			params.put("timestamp",timestamp);
			params.put("directPayType",directPayType);
			params.put("cardType",cardType);
			params.put("userNo",userNo);
			params.put("userType",userType);
			params.put("ext",ext);
			String stdUrl = getStdUrl(params);
			System.out.println("stdUrl=" + stdUrl);
			httpResponse.sendRedirect(stdUrl);
			return null;
		}catch (Exception e){
			e.printStackTrace();
		}
		return CommonResponse.withSuccessResp(response.toString());
	}

 	private static String getStdUrl(Map<String,String> paramValues){
		StringBuffer url = new StringBuffer();
		url.append("https://cash.yeepay.com/cashier/std");
		StringBuilder stringBuilder = new StringBuilder();
		System.out.println(paramValues);
		for (int i = 0; i < CASHIER.length; i++) {
			String name = CASHIER[i];
			String value = paramValues.get(name);
			if(i != 0){
				stringBuilder.append("&");
			}
			stringBuilder.append(name+"=").append(value);
		}
		System.out.println("stringbuilder:"+stringBuilder);
		String sign = getSign(stringBuilder.toString());
		url.append("?sign="+sign+"&"+stringBuilder);
		return url.toString();
	}

	*/
/**
	 *
	 * @param stringBuilder 签名源串
	 * @return
	 *//*

	private static String getSign(String stringBuilder) {
		PrivateKey isvPrivateKey = getPrivateKey(merchant_private_key);
		DigitalSignatureDTO digitalSignatureDTO = new DigitalSignatureDTO();
		digitalSignatureDTO.setAppKey(appKey);
		digitalSignatureDTO.setCertType(CertTypeEnum.RSA2048);
		digitalSignatureDTO.setDigestAlg(DigestAlgEnum.SHA256);
		digitalSignatureDTO.setPlainText(stringBuilder);
		String sign = DigitalEnvelopeUtils.sign0(digitalSignatureDTO,isvPrivateKey);
		return sign;
	}

	//获取私钥对象
	private static PrivateKey getPrivateKey(String merchant_private_key) {
		PrivateKey privateKey = null;
		PKCS8EncodedKeySpec priPKCS8;
		try {
			priPKCS8 = new PKCS8EncodedKeySpec(new BASE64Decoder().decodeBuffer(merchant_private_key));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			privateKey = keyf.generatePrivate(priPKCS8);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return privateKey;
	}

}
*/
