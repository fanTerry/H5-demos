package com.esportzoo.esport.expert.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.client.service.expert.RecExpertServiceClient;
import com.esportzoo.esport.constants.ExpertStatus;
import com.esportzoo.esport.domain.RecExpert;
import com.esportzoo.esport.expert.constant.CookieConstant;
import com.esportzoo.esport.expert.tool.LoginUtils;
import com.esportzoo.esport.util.MD5;


public class LoginInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
	private static String prefix = "登录拦截器-";
	
	@Autowired
	private RecExpertServiceClient recExpertServiceClient;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String cookieValue = LoginUtils.getCookie(request, CookieConstant.LOGIN_COOKIE_NAME);
		logger.info(prefix + "拦截请求={}", request.getServletPath());
		if (StringUtils.isBlank(cookieValue)) {
			logger.info(prefix + "登录cookie值为空");
			response.sendRedirect(request.getContextPath()+"/login");
			return false;
		}
		String[] loginCookie = cookieValue.split("\\|");
		if (loginCookie.length != 3) {
			logger.info(prefix + "登录cookie值长度不正确");
			response.sendRedirect(request.getContextPath()+"/login");
			return false;
		}
		String encryedStr = loginCookie[0];
		String userNickName = loginCookie[1];
		String expertId = loginCookie[2];
		if (StringUtils.isBlank(encryedStr) || StringUtils.isBlank(userNickName) || StringUtils.isBlank(expertId)) {
			logger.info(prefix + "登录cookie值其中一项为空");
			response.sendRedirect(request.getContextPath()+"/login");
			return false;
		}
		String calEncryStr = MD5.md5Encode(expertId + CookieConstant.LOING_COOKIE_MD5);
		if (!calEncryStr.equals(encryedStr)) {
			logger.info(prefix + "登录cookie值加密值错误");
			response.sendRedirect(request.getContextPath()+"/login");
			return false;
		}
		ModelResult<RecExpert> modelResult = recExpertServiceClient.queryById(Long.parseLong(expertId));
		if (!modelResult.isSuccess()) {
			logger.info(prefix + "根据专家id查询专家，接口返回错误={}", modelResult.getErrorMsg());
			response.sendRedirect(request.getContextPath()+"/login");
			return false;
		}
		RecExpert recExpert = modelResult.getModel();
		if (recExpert == null) {
			logger.info(prefix + "根据专家id查询专家，为空");
			response.sendRedirect(request.getContextPath()+"/login");
			return false;
		}
		if (recExpert.getStatus()==null || recExpert.getStatus().intValue()!=ExpertStatus.VALID.getIndex()) {
			logger.info(prefix + "根据专家id查询专家状态无效，expertId={}", expertId);
			response.sendRedirect(request.getContextPath()+"/login");
			return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		System.out.println(MD5.md5Encode("000000"));
		System.out.println("ES20190429155910100001".length());
		System.out.println("670b14728ad9902aecba32e22fa4f6bd".length());
	}
	
}