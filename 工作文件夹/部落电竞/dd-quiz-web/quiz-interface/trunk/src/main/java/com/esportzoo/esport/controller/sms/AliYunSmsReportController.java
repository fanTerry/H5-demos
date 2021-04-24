package com.esportzoo.esport.controller.sms;

import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.esport.client.service.sms.SmsLogServiceClient;
import com.esportzoo.esport.constants.sms.SmsLogStatus;
import com.esportzoo.esport.domain.SmsLog;
import com.esportzoo.esport.vo.sms.AliYunSmsReportReceive;
import com.esportzoo.esport.vo.sms.AliYunSmsReportResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.List;

/**
 * @author tingting.shen
 * @date 2019/07/11
 */
@Controller
@RequestMapping("aliyunsms")
public class AliYunSmsReportController {
	
	private transient final Logger logger = LoggerFactory.getLogger(getClass());
	private static String logPrefix= "处理阿里云短信报告_";
	
	@Autowired
	private SmsLogServiceClient smsLogServiceClient;
	
	@RequestMapping(value="/processNotify")
	@ResponseBody
	public AliYunSmsReportResponse processNotify(HttpServletRequest request, HttpServletResponse response) {
		try {
			logger.info(logPrefix + "开始");
			String notifyStr = getNotifyStr(request);
			if (StringUtils.isBlank(notifyStr)) {
				return new AliYunSmsReportResponse(-1, "没有收到通知内容");
			}
			List<AliYunSmsReportReceive> list = JSON.parseArray(notifyStr, AliYunSmsReportReceive.class);
			if (list == null) {
				logger.info(logPrefix + "转换后集合为空");
				return new AliYunSmsReportResponse(-2, "转换后集合为空");
			} 
			logger.info(logPrefix + "list.size={}", list.size());
			if (list.size() <= 0) {
				return new AliYunSmsReportResponse(-3, "转换后集合为空");
			}
			for (AliYunSmsReportReceive aliYunSmsReportReceive : list) {
				logger.info(logPrefix + "aliYunSmsReportReceive={}", JSON.toJSONString(aliYunSmsReportReceive));
				processEach(aliYunSmsReportReceive);
			}
			return new AliYunSmsReportResponse(0, "接收成功");
		} catch (Exception e) {
			logger.info(logPrefix + "发生异常e={}", e.getMessage(), e);
			return new AliYunSmsReportResponse(-100, "处理异常");
		}
	}
	
	private void processEach(AliYunSmsReportReceive aliYunSmsReportReceive) {
		try {
			String out_id = aliYunSmsReportReceive.getOut_id();
			if (StringUtils.isBlank(out_id)) {
				return;
			}
			ModelResult<SmsLog> mr = smsLogServiceClient.queryById(Long.parseLong(out_id));
			if (!mr.isSuccess() || mr.getModel()==null) {
				return;
			}
			SmsLog smsLog = mr.getModel();
			if (smsLog.getStatus().intValue() != SmsLogStatus.INIT.getIndex()) {
				return;
			}
			smsLog.setStatus(aliYunSmsReportReceive.getSuccess().booleanValue()?SmsLogStatus.SUCCESS.getIndex():SmsLogStatus.FAIL.getIndex());
			String failReason = aliYunSmsReportReceive.getErr_code() + "|" + aliYunSmsReportReceive.getErr_msg();
			failReason = failReason.length()>200 ? failReason.substring(0, 200) : failReason;
			smsLog.setFailReason(failReason);
			smsLog.setChargeNum(Integer.parseInt(aliYunSmsReportReceive.getSms_size()));
			smsLog.setupFeature("biz_id", aliYunSmsReportReceive.getBiz_id());
			smsLog.setupFeature("update_source", "interface_reveiver");
			smsLog.setUpdateTime(Calendar.getInstance());
			smsLogServiceClient.update(smsLog);
			logger.info(logPrefix + "修改状态成功smsLogId={}", out_id);
		} catch (Exception e) {
			return;
		}
	}
	
	/**得到通知字符串*/
	private String getNotifyStr(HttpServletRequest request) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream(), "utf-8"));
			StringBuffer sb = new StringBuffer("");
			String temp;
			while ((temp = br.readLine()) != null) { 
			  sb.append(temp);
			}
			br.close();
			logger.info(logPrefix + "得到通知字符串={}", sb.toString());
			return sb.toString();
		} catch (Exception e) {
			logger.info(logPrefix + "得到通知字符串发生异常，e={}", e.getMessage());
			return null;
		}
	}
	
}
