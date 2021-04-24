package com.esportzoo.esport.interceptor;

import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.constants.BizSystem;
import com.esportzoo.esport.constants.ClientType;
import com.esportzoo.esport.constants.sms.SendSmsEnum;
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

    private final Logger logger = LoggerFactory.getLogger("request");
    private static final String logPrefix = "登陆拦截_";
    private static final String bindingRegisterType = SendSmsEnum.BINDING_REGISTER_CODE.getType().toString();
    //UserController中sendPhoneCode 发送短信的方法
    private final String sendSmsMethodName = "sendPhoneCode";

    @Autowired
    @Qualifier("cachedManager")
    private CachedManager cachedManager;
    @Autowired
    private UserConsumerServiceClient userConsumerServiceClient;

    @Value("${environ}")
    private String environ;


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String biz = request.getParameter("biz");
        String clientType = request.getParameter("clientType");
        String userAgent = request.getHeader("User-Agent");
        String requestUrl = request.getRequestURL() + "";
        biz = StringUtils.isNumeric(biz) ? biz : BizSystem.LOCAL.getIndex() + "";//以防前端传入的值部分异常
        String sid = cachedManager.getUserSid(request, biz, clientType);
        logger.info(logPrefix + "biz={},clientType={},sid={},userAgent={},requestUrl={}", biz, clientType, sid, userAgent, requestUrl);
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
            if (clientTypeInt == ClientType.ANDROID.getIndex() || clientTypeInt == ClientType.IOS.getIndex()) {
                cachedManager.cachedMemberSessionByDay(memberSession.getMember(), sid);
            } else {
                cachedManager.cachedMemberSession(memberSession.getMember(), sid);
            }
            cachedManager.cachedMemberSession(loginUser, sid);
            userConsumerServiceClient.updateUserConsumerLastLoginTime(loginUser);
            logger.info(logPrefix + "已通过,获取登陆用户id={},nickName={},并且更新了最后登陆时间,当前用户userAgent={},requestUrl={}", loginUser.getId(), loginUser.getNickName(), userAgent, requestUrl);
            return true;
        }
        logger.info(logPrefix + "未通过,当前sid={},redis缓存已经失效", sid);
        response.setStatus(ResponseConstant.USR_NOT_LOGIN_CODE);
        return false;
    }
}
