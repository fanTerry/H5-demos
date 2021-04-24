package com.esportzoo.esport.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.client.service.msg.MsgWxPushFormidServiceClient;
import com.esportzoo.esport.connect.request.FormRequest;
import com.esportzoo.esport.connect.request.WxDecodePhoneRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.constants.ThirdType;
import com.esportzoo.esport.constants.UserOperationParam;
import com.esportzoo.esport.constants.msg.weixin.FormIdStatus;
import com.esportzoo.esport.domain.MsgWxPushFormid;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.manager.UserManager;
import com.esportzoo.esport.manager.WeChatManager;
import com.esportzoo.esport.vo.user.WxDecodePhoneResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("wxapp")
public class WeChatController {

	private transient final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private WeChatManager weChatManager;
	@Autowired
	private UserManager userManager;
	@Autowired
	private UserConsumerServiceClient userConsumerServiceClient;

	@Autowired
	private MsgWxPushFormidServiceClient msgWxPushFormidServiceClient;

	@RequestMapping(value = "decodephone", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<WxDecodePhoneResponse> doLogin(WxDecodePhoneRequest wxDecodePhoneRequest, HttpServletRequest request) {
		logger.info("微信解码手机请求,wxDecodePhoneRequest={}", JSONObject.toJSONString(wxDecodePhoneRequest));
		try {
			WxDecodePhoneResponse wxDecodePhoneResponse = weChatManager.wxDecodePhone(wxDecodePhoneRequest);
			if (wxDecodePhoneResponse == null) {
				return CommonResponse.withErrorResp("wxDecodePhoneResponse == null");
			}
			UserConsumer user = userManager.getUserConsumer(wxDecodePhoneResponse.getOpenid(), ThirdType.WECHAT_MINI.getIndex());
			if (user == null) {
				return CommonResponse.withErrorResp("user == null");
			}
			logger.info("微信解码手机请求, userId={}, wxDecodePhoneResponse={}", user.getId(), JSON.toJSONString(wxDecodePhoneResponse));
			ModelResult<Boolean> modelResult = userConsumerServiceClient.updateConsumerPhone(user.getId(), wxDecodePhoneResponse.getPhoneNumber(), new UserOperationParam());
			if (!modelResult.isSuccess()) {
				logger.info("微信解码手机请求, 更新手机号返回错误，errCode={}, errMsg={}", modelResult.getErrorCode(), modelResult.getErrorMsg());
				return CommonResponse.withErrorResp("!modelResult.isSuccess()");
			}
			if (modelResult.getModel()==null || !modelResult.getModel()) {
				return CommonResponse.withErrorResp("同步失败");
			}
			return CommonResponse.withSuccessResp(wxDecodePhoneResponse);
		} catch (Exception e) {
			logger.info("微信解码手机请求,发生异常exception={}",e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}


	}


	@RequestMapping(value = "saveFormid", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<JSONObject> saveFormid(HttpServletRequest request, FormRequest formRequest) {
		if (StringUtils.isEmpty(formRequest.getFormId())) {
			return CommonResponse.withErrorResp("formid不能为空");
		}
		if (formRequest.getUserId() == null) {
			return CommonResponse.withErrorResp("UserId不能为空");
		}
		if (StringUtils.isEmpty(formRequest.getFormId())) {
			return CommonResponse.withErrorResp("SourceType不能为空");
		}
		if (formRequest.getFormId().contains("formId")){
			logger.info("the formId is a mock one 不进行入库处理，参数【{}】",JSON.toJSONString(formRequest));
			return CommonResponse.withSuccessResp("不进行入库处理");
		}
		try {
			MsgWxPushFormid msgWxPushFormid = new MsgWxPushFormid();
			msgWxPushFormid.setUserId(formRequest.getUserId());
			msgWxPushFormid.setFormId(formRequest.getFormId());
			//1 小程序商城兑换收集
			msgWxPushFormid.setSource(formRequest.getSourceType());
			msgWxPushFormid.setStatus(FormIdStatus.NO_USE.getIndex());
			ModelResult<Integer> modelResult = msgWxPushFormidServiceClient.save(msgWxPushFormid);
			if (modelResult != null && modelResult.isSuccess() && modelResult.getModel() != null) {
				return CommonResponse.withSuccessResp("保存成功");
			}
		} catch (Exception e) {
			logger.error("保存微信表单ID发生异常，参数【{}】,异常信息：{}", JSONObject.toJSONString(formRequest), e.getMessage());
			e.printStackTrace();
			return CommonResponse.withSuccessResp("保存失败");

		}
		return CommonResponse.withSuccessResp("保存成功");
	}


}
