package com.esportzoo.esport.interceptor;

import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.constants.BizSystem;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.manager.CachedManager;
import com.esportzoo.esport.vo.MemberSession;
import com.esportzoo.esport.vo.UserConsumerQueryOption;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserAuthInterceptor extends HandlerInterceptorAdapter {

    private final Logger logger = LoggerFactory.getLogger("request");
    private static final String logPrefix = "用户实名认证拦截_";

    @Autowired
    @Qualifier("cachedManager")
    private CachedManager cachedManager;
    @Autowired
    private UserConsumerServiceClient userConsumerServiceClient;
    @Autowired
    private RedisClient redisClient;
    private static final String USER_AUTH_KEY = "user_auth_key_";
    private static final int CACHE_TIME = 2 * 24 * 60 * 60;//缓存两天
//    private static final int CACHE_TIME_WARN = 2 * 60 * 60;//缓存2小时


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String biz = request.getParameter("biz");
        String clientType = request.getParameter("clientType");
        biz = StringUtils.isNumeric(biz) ? biz : BizSystem.LOCAL.getIndex() + "";//以防前端传入的值部分异常
        String sid = cachedManager.getUserSid(request, biz, clientType);
        logger.info(logPrefix + "biz={},clientType={},sid={}", biz, clientType, sid);
        if (StringUtils.isBlank(sid)) {
            logger.info(logPrefix + "登录未通过,sid为空");
            response.setStatus(ResponseConstant.USR_NOT_LOGIN_CODE);
            return false;
        }

        MemberSession memberSession = cachedManager.getCachedMemberSession(sid);
        if (memberSession != null && memberSession.getMember() != null) {
            UserConsumer loginUser = memberSession.getMember();
            String cacheKey = USER_AUTH_KEY + "_" + loginUser.getId();
//            warnCachedKey = USER_AUTH_KEY + "warn_" + loginUser.getId();
            if (StringUtils.isNotBlank(redisClient.getObj(cacheKey))) {
                logger.info(logPrefix + "【实名认证数据库缓存中取出已通过】,获取登陆用户id={},nickName={}", loginUser.getId(), loginUser.getNickName());
                return true;
            }
            //判断用户是否实名认证
            if (StringUtils.isNotBlank(loginUser.getCertNo()) && StringUtils.isNotBlank(loginUser.getTrueName())) {
                //校验数据库用户
                ModelResult<UserConsumer> modelResult = userConsumerServiceClient.queryConsumerById(loginUser.getId(), new UserConsumerQueryOption());
                if (modelResult.isSuccess() && null != modelResult.getModel()) {
                    UserConsumer dbUser = modelResult.getModel();
                    if (StringUtils.isNotBlank(dbUser.getCertNo()) && StringUtils.isNotBlank(dbUser.getTrueName())) {
                        redisClient.setObj(cacheKey, CACHE_TIME, "1");
                        logger.info(logPrefix + "【实名认证已通过】,获取登陆用户id={},nickName={},实名认证姓名:{}", loginUser.getId(), loginUser.getNickName(), loginUser.getTrueName());
                        return true;
                    }
//                    else{
//                        //新增 按频率提醒用户
//                        if (StringUtils.isNotBlank(redisClient.getObj(warnCachedKey))) {
//                            logger.info(logPrefix + "【用户登录态中有数据,实名认证已提醒用户,直接通过】,获取登陆用户id={},nickName={}", loginUser.getId(), loginUser.getNickName());
//                            return true;
//                        }
//                        redisClient.setObj(warnCachedKey, CACHE_TIME_WARN, "1");
//                        logger.info(logPrefix + "【实名认证未通过,用户登录态中有数据,但是数据库中没有】,获取登陆用户id={},nickName={}", loginUser.getId(), loginUser.getNickName());
//                        response.setStatus(ResponseConstant.USR_NOT_AUTH_CODE);
//                        return false;
//                    }
                } else {
                    logger.info(logPrefix + "【实名认证查数据未通过】,用户id={},nickName={}", loginUser.getId(), loginUser.getNickName());
                    response.setStatus(ResponseConstant.USR_NOT_AUTH_CODE);
                    return false;
                }
            } else {
                //新增 按频率提醒用户
//                if (StringUtils.isNotBlank(redisClient.getObj(warnCachedKey))) {
//                    logger.info(logPrefix + "【实名认证已提醒用户,直接通过】,获取登陆用户id={},nickName={}", loginUser.getId(), loginUser.getNickName());
//                    return true;
//                }
//                redisClient.setObj(warnCachedKey, CACHE_TIME_WARN, "1");
                logger.info(logPrefix + "【实名认证未通过】,获取登陆用户id={},nickName={}", loginUser.getId(), loginUser.getNickName());
                response.setStatus(ResponseConstant.USR_NOT_AUTH_CODE);
                return false;
            }
        }
        logger.info(logPrefix + "【实名认证未通过】,已走完拦截,当前sid={}", sid);
        response.setStatus(ResponseConstant.USR_NOT_AUTH_CODE);
        return false;
    }

}
