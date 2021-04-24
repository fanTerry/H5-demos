package com.esportzoo.esport.controller;

import cn.hutool.core.util.NumberUtil;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.util.CookieUtils;
import com.esportzoo.esport.client.service.common.SysConfigPropertyServiceClient;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.constants.BizSystem;
import com.esportzoo.esport.constants.ClientType;
import com.esportzoo.esport.constants.SysConfigPropertyKey;
import com.esportzoo.esport.constants.user.MemberConstants;
import com.esportzoo.esport.domain.SysConfigProperty;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.manager.CachedManager;
import com.esportzoo.esport.vo.MemberSession;
import com.esportzoo.esport.vo.UserConsumerQueryOption;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public abstract class BaseController {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	private static final String logPrefix = "BaseController_";

	@Autowired
	public CachedManager cachedManager;
	@Autowired
	UserConsumerServiceClient userConsumerServiceClient;
	@Autowired
	private SysConfigPropertyServiceClient sysConfigPropertyServiceClient;

	/**
	 * 获取memberSession
	 * 
	 * @param request
	 * @return
	 */
	public MemberSession getMemberSession(HttpServletRequest request) {
		String sid = getSessionId(request);
		if (sid == null) {
			return null;
		}
		String clientTypesStr = request.getParameter("clientType");
		if (StringUtils.isEmpty(clientTypesStr)) {
			return null;
		}
		Integer clientType = Integer.valueOf(clientTypesStr);

		MemberSession memberSession = cachedManager.getCachedMemberSession(sid);
		if (memberSession != null && memberSession.getMember() != null) {
			if (clientType==ClientType.ANDROID.getIndex() || clientType==ClientType.IOS.getIndex() ){
				cachedManager.cachedMemberSessionByDay(memberSession.getMember(),sid);
			}else {
				cachedManager.cachedMemberSession(memberSession.getMember(),sid);
			}
			return memberSession;
		}
		return null;
	}

	/**
	 * 更新用户缓存session信息，当修改用户信息时需要使用
	 * 
	 * @param request
	 * @return
	 */
	public MemberSession updateMemberSession(HttpServletRequest request) {
		String sid = getSessionId(request);
		if (sid == null) {
			return null;
		}

		MemberSession memberSession = cachedManager.getCachedMemberSession(sid);
		if (memberSession != null && memberSession.getMember() != null) {
			ModelResult<UserConsumer> result = userConsumerServiceClient
					.queryConsumerById(memberSession.getMember().getId(), new UserConsumerQueryOption());
			if (result != null && result.isSuccess() && result.getModel() != null) {
				cachedManager.cachedMemberSession(result.getModel(), sid);
				memberSession = cachedManager.getCachedMemberSession(sid);
			} else {
				return null;
			}

			return memberSession;
		}
		return null;
	}

	private String getSessionId(HttpServletRequest request) {
		String sid = "";
		String biz = request.getParameter("biz");
		String clientType = request.getParameter("clientType");
		logger.info(logPrefix + "biz={},clientType={}", biz, clientType);

		if (StringUtils.isNotBlank(biz) && Integer.parseInt(biz) == BizSystem.UBOX.getIndex()) {
			sid = CookieUtils.getCookieValue(request, MemberConstants.UBOX_LOGIN_COOKIE_SID);
			logger.info(logPrefix + "业务系统是友宝, sid={}", sid);
		} else {
			if (StringUtils.isNotBlank(clientType) && ClientType.getH5ClientTypeIndexList().contains(Integer.parseInt(clientType))) {
				sid = CookieUtils.getCookieValue(request, MemberConstants.H5_LOGIN_COOKIE_SID);
				logger.info(logPrefix + "客户端类型是h5, sid={}", sid);
			} else if (StringUtils.isNotBlank(clientType) && (Integer.parseInt(clientType) == ClientType.WXGZH.getIndex() || Integer.parseInt(clientType) == ClientType.WEB.getIndex())) {
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

		if (StringUtils.isBlank(sid)) {
			return null;
		}
		return sid;
	}

	/** 获取登陆用户 */
	public UserConsumer getLoginUsr(HttpServletRequest request) {
		UserConsumer userConsumer = null;
		MemberSession mSession = getMemberSession(request);
		if (mSession != null) {
			userConsumer = mSession.getMember();
			if (StringUtils.isNotBlank(request.getParameter("sid"))) {
				userConsumer.setsId(request.getParameter("sid"));
			}
		} else {

			SysConfigProperty property = getSysConfigByKey(SysConfigPropertyKey.USER_LOGIN_FREE_FOR_TEST, ClientType.UNKNOW.getIndex(),null);
			if (StringUtils.isNotEmpty(property.getValue())) {
				//测试使用
				String value = property.getValue();
				if ("-".equals(value)){
					logger.info("获取用户信息失败，请重新登录");
					return userConsumer;
				}
				if (NumberUtil.isLong(value)){
					Long userId = Long.valueOf(value);
					ModelResult<UserConsumer> modelResult = userConsumerServiceClient.queryConsumerById(userId, new UserConsumerQueryOption());
					userConsumer = modelResult.getModel();
				}

			}else {
				logger.info("获取用户信息失败，请重新登录");
			}


		}
		return userConsumer;
	}

	/** 根据clientType,渠道号和key 取系统配置参数 */
	public SysConfigProperty getSysConfigByKey(String key, Integer clientType,Long agentId) {
		if (null == ClientType.valueOf(clientType)) {
			clientType = ClientType.UNKNOW.getIndex();
		}
		if(agentId == null){
			agentId = 0L;
		}
		SysConfigProperty sysConfigProperty = sysConfigPropertyServiceClient.getSysConfigPropertyByKey(key,clientType,agentId);
		return sysConfigProperty;
	}
}
