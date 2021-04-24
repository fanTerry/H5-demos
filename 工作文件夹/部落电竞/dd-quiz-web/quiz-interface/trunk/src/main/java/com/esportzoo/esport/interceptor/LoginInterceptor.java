package com.esportzoo.esport.interceptor;

import com.esportzoo.common.util.CookieUtils;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.constants.BizSystem;
import com.esportzoo.esport.constants.ClientType;
import com.esportzoo.esport.constants.user.MemberConstants;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.manager.CachedManager;
import com.esportzoo.esport.vo.MemberSession;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final String logPrefix = "登陆拦截_";

	@Autowired
	@Qualifier("cachedManager")
	private CachedManager cachedManager;
	@Autowired
	private UserConsumerServiceClient userConsumerServiceClient;

	@Value("${environ}")
	private String environ;


	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String sid = "";
		String biz = request.getParameter("biz");
		String clientType = request.getParameter("clientType");
		logger.info(logPrefix + "biz={},clientType={}", biz, clientType);
		if (StringUtils.isNotBlank(biz) && Integer.parseInt(biz)==BizSystem.UBOX.getIndex()) {
			sid = CookieUtils.getCookieValue(request, MemberConstants.UBOX_LOGIN_COOKIE_SID);
			logger.info(logPrefix + "业务系统是友宝, sid={}", sid);
		} else {
			if (StringUtils.isNotBlank(clientType) && ClientType.getH5ClientTypeIndexList().contains(Integer.parseInt(clientType))) {
				sid = CookieUtils.getCookieValue(request, MemberConstants.H5_LOGIN_COOKIE_SID);
				logger.info(logPrefix + "客户端类型是h5, sid={}", sid);
			} else if (StringUtils.isNotBlank(clientType) && (Integer.parseInt(clientType) == ClientType.WXGZH.getIndex()||Integer.parseInt(clientType) == ClientType.WEB.getIndex())) {
				sid = CookieUtils.getCookieValue(request, MemberConstants.WX_ACCOUNT_LOGIN_COOKIE_SID);
				logger.info(logPrefix + "客户端类型是微信公众号, sid={}", sid);
			} else if (StringUtils.isNotBlank(clientType) && Integer.parseInt(clientType) == ClientType.WXXCY.getIndex()) {
				sid = request.getParameter("sid");
				logger.info(logPrefix + "客户端类型是微信小程序, sid={}", sid);
			} else {
				sid = request.getParameter("sid");
				logger.info(logPrefix + "默认是微信小程序，sid={}", sid);
			}
		}

		//用于测试环境免登陆
		if (StringUtils.isNotEmpty(environ) && environ.equals("daily") && request.getRequestURL().toString().contains("quiz.esportzoo.com")) {
			return true;
		}

		if (StringUtils.isBlank(sid)) {
			logger.info(logPrefix + "登录未通过,sid为空");
			response.setStatus(ResponseConstant.USR_NOT_LOGIN_CODE);
			return false;
		}
		
		MemberSession memberSession = cachedManager.getCachedMemberSession(sid);
		if (memberSession != null && memberSession.getMember() != null) {
			UserConsumer loginUser = memberSession.getMember();
			loginUser.setsId(sid);
			Integer clientTypeInt = Integer.valueOf(clientType);
			if (clientTypeInt==ClientType.ANDROID.getIndex() || clientTypeInt==ClientType.IOS.getIndex() ){
				cachedManager.cachedMemberSessionByDay(memberSession.getMember(),sid);
			}else {
				cachedManager.cachedMemberSession(memberSession.getMember(),sid);
			}
			cachedManager.cachedMemberSession(loginUser, sid);
			userConsumerServiceClient.updateUserConsumerLastLoginTime(loginUser);
			logger.info(logPrefix + "已通过,获取登陆用户id={},nickName={},并且更新了最后登陆时间", loginUser.getId(), loginUser.getNickName());
			return true;
		}
		logger.info(logPrefix + "未通过,当前sid={},redis缓存已经失效",sid);
		response.setStatus(ResponseConstant.USR_NOT_LOGIN_CODE);
		return false;
	}
}
