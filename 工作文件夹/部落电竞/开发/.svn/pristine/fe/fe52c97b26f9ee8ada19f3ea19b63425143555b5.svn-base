package com.esportzoo.esport.controller.sms;

import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.esport.constants.sms.ShortMessageContentType;
import com.esportzoo.esport.service.sms.AliYunShortMessageService;
import com.esportzoo.esport.vo.sms.AliYunSendSmsParam;
import com.esportzoo.esport.vo.sms.AliYunSmQueryParam;
import com.esportzoo.esport.vo.sms.AliYunSmsDetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tingting.shen
 * @date 2019/07/10
 */
@Controller
@RequestMapping("sms")
public class SmsController {
	
	@Autowired
	private AliYunShortMessageService aliYunShortMessageService;
	
	@RequestMapping(value="/send")
	@ResponseBody
	public String sendSms(String phone) {
		AliYunSendSmsParam param = new AliYunSendSmsParam();
		param.setPhone(phone);
		param.setSignName("橘子电竞");
		param.setTemplateCode("SMS_170050386");
		param.setContentType(ShortMessageContentType.REGISTER_VALID_CODE);
		Map<String, String> templateParam = new HashMap<String, String>();
		templateParam.put("code", "1234");
		param.setTemplateParam(templateParam);
		aliYunShortMessageService.sendSms(param);
		return "success";
	}
	
	@RequestMapping(value="/query")
	@ResponseBody
	public String query(AliYunSmQueryParam param) {
		ModelResult<AliYunSmsDetailResponse> modelResult = aliYunShortMessageService.querySendDetails(param);
		return JSON.toJSONString(modelResult);
	}
	
}
