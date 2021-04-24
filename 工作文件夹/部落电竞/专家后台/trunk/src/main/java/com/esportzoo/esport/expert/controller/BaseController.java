package com.esportzoo.esport.expert.controller;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.esport.client.service.expert.RecExpertServiceClient;
import com.esportzoo.esport.constants.ExpertStatus;
import com.esportzoo.esport.domain.RecExpert;
import com.esportzoo.esport.expert.constant.CookieConstant;
import com.esportzoo.esport.expert.interceptor.CalendarEditor;
import com.esportzoo.esport.expert.interceptor.DateEditor;
import com.esportzoo.esport.expert.tool.LoginUtils;

public abstract class BaseController {
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	protected RecExpertServiceClient recExpertServiceClient;
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Calendar.class, new CalendarEditor());
		binder.registerCustomEditor(Date.class, new DateEditor());
	}
	
	public RecExpert getLoginExpert(HttpServletRequest request) {
		String cookieValue = LoginUtils.getCookie(request, CookieConstant.LOGIN_COOKIE_NAME);
		if (StringUtils.isBlank(cookieValue)) {
			return null;
		}
		Long expertId = Long.valueOf(cookieValue.split("\\|")[2]);
		if (expertId == null) {
			return null;
		}
		ModelResult<RecExpert> modelResult = recExpertServiceClient.queryById(expertId);
		if (!modelResult.isSuccess()) {
			return null;
		}
		RecExpert recExpert = modelResult.getModel();
		if (recExpert == null) {
			return null;
		}
		if (recExpert.getStatus()==null || recExpert.getStatus().intValue()==ExpertStatus.INVALID.getIndex()) {
			return null;
		}
		return recExpert;
	}	
	
}
