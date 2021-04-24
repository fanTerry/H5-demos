package com.esportzoo.esport.manager.qqconnect;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.esport.constant.QqConnectConstants;
import com.esportzoo.esport.constants.*;
import com.esportzoo.esport.domain.*;
import com.esportzoo.esport.exception.CommonExceptionCode;
import com.esportzoo.esport.manager.LoginManager;
import com.esportzoo.esport.service.consumer.UserConsumerService;
import com.esportzoo.esport.service.exception.BusinessException;
import com.esportzoo.esport.vo.UserConsumerQueryOption;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author jiajing.he
 * @date 2020/2/21 15:56
 */
@Component
public class QqLoginManager {

    private Logger logger = LoggerFactory.getLogger(QqLoginManager.class);

    @Value("${qq.connect.appid}")
    private String appId;

//    @Value("${app.qq.connect.appid}")
//    private String appAppId;

    @Value("${qq.connect.url}")
    private String redirectUrl;

    @Value("${qq.connect.appkey}")
    private String appKey;

//    @Value("${app.qq.connect.appkey}")
//    private String appAppKey;

    @Autowired
    UserConsumerService userConsumerService;

    public static final String QQ_USER_OPENID_FEATURE_KEY="qqUserOpenId";


    /**
     * 获取AccessToken
     * @param code
     * @return
     */
    public String getAccessToken( String code)  {
        HashMap<String, Object> param = new HashMap<>();
        param.put("grant_type","authorization_code");
        param.put("client_id", appId);
        param.put("client_secret", appKey);
        param.put("code", code);
        /**传入的uri不需要URLEncoder.encode，HttpUtil.get源码里面做了处理 */
        param.put("redirect_uri",redirectUrl);
        String result = HttpUtil.get(QqConnectConstants.accessTokenURL, param, 3000);
        String token = null;
        /*获取失败*/
        if (result.isEmpty()) {
            logger.info("获取accessToken失败  返回信息result=[{}]", result);
            throw new BusinessException(CommonExceptionCode.GET_QQACCESSTOKEN_EXCEPTION);
        }
        if(result.indexOf("access_token") >= 0){
            String[] array = result.split("&");
            for (String str : array){
                if(str.indexOf("access_token") >= 0){
                    token = str.substring(str.indexOf("=") + 1);
                    break;
                }
            }
        }
        return token;
    }
    /**
     * 获取openid
     * @param accessToken
     */
    public String getOpenId(String accessToken) {
        HashMap<String, Object> openIDParam = new HashMap<>();
        openIDParam.put("access_token", accessToken);
        String openIdResult = HttpUtil.get(QqConnectConstants.getOpenIDURL, openIDParam, 3000);
        /*获取失败*/
        if (openIdResult.isEmpty()) {
            logger.info("获取openIdResult失败  返回信息openIdResult=[{}]", openIdResult);
            throw new BusinessException(CommonExceptionCode.GET_QQOPENID_EXCEPTION);
        }
        logger.info("qq登录  获得openIdResult={}", openIdResult);
        int startIndex = openIdResult.indexOf("(");
        int endIndex = openIdResult.lastIndexOf(")");
        String json = openIdResult.substring(startIndex + 1,endIndex);
        JSONObject jsonObject= JSONObject.parseObject(json);
        String openid = jsonObject.getString("openid");
        logger.info("qq登录  openid={}", openid);
        return openid;
    }

    /**
     * 获取用户信息
     * @param openId
     * @param token
     * @return
     */
    public QqConnectUserInfo getUserInfo(String openId, String token) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("openid", openId);
        param.put("oauth_consumer_key", appId);
        param.put("access_token", token);
        String s = HttpUtil.get(QqConnectConstants.getUserInfoURL, param, 3000);
        logger.info("qq登录 openid[{}] 获取用户信息[{}]", openId, s);
        JSONObject jsonObject = JSONObject.parseObject(s);
        QqConnectUserInfo userInfo = new QqConnectUserInfo(jsonObject);
        /*获取失败*/
        if (0 != userInfo.getRet().intValue()) {
            logger.info("根据 openid[{}] 获取用户信息失败 返回码[{}] 返回信息[{}]", openId, userInfo.getRet(), userInfo.getMsg());
            throw new BusinessException(CommonExceptionCode.GET_USERINFO_EXCEPTION);
        }
        userInfo.setOpenId(openId);
        return userInfo;
    }

    /**
     * 同步注册qq的用户信息
     * qq用户的唯一标识 就是 openid
     *
     * @return
     */
    public SynchronizeRes synchronizeUserInfo(QqConnectUserInfo qqConnectUserInfo) {

        String logPrefix = "QQ同步用户信息_";
        SynchronizeRes synchronizeRes = new SynchronizeRes();
        synchronizeRes.setNewUser(Boolean.FALSE);
        ModelResult<UserThirdLogin> userThirdLoginModelResult = userConsumerService.queryUsrThirdByIdAndType(qqConnectUserInfo.getOpenId(), ThirdType.QQ_CONNECT.getIndex());
        if (!userThirdLoginModelResult.isSuccess()) {
            logger.info(logPrefix + "根据QQ登录类型和openid查询出现异常 【{}】", userThirdLoginModelResult.getErrorMsg());
            throw new BusinessException(CommonExceptionCode.RPC_EXCEPTION);
        }
        UserThirdLogin thirdLogin = userThirdLoginModelResult.getModel();
        /*新用户*/
        if (null == thirdLogin) {
            UserConsumer userConsumer = userInfoPackag(qqConnectUserInfo);
            String key = LoginManager.CACHE_USER_INFO_KEY + UUID.randomUUID().toString();
            synchronizeRes.setNewUser(Boolean.TRUE);
            synchronizeRes.setKey(key);
            synchronizeRes.setUserConsumer(userConsumer);
        } else {
            Long userId = thirdLogin.getUserId();
            UserConsumer userConsumer = userConsumerService.queryConsumerById(userId, new UserConsumerQueryOption()).getModel();
            if (null == userConsumer) {
                logger.info(logPrefix + "根据第三方绑定获取到无效的用户id[{}]", userId);
                throw new BusinessException(CommonExceptionCode.GET_USERINFO_EXCEPTION);
            } else {
                userConsumerService.updateUserConsumerLastLoginTime(userConsumer);
                logger.info("QQ登陆接口,更新用户最新登陆时间，userId={},ninkName={}", userConsumer.getId(),
                        userConsumer.getNickName());
                synchronizeRes.setUserConsumer(userConsumer);
            }
        }
        return synchronizeRes;
    }

    private UserConsumer userInfoPackag(QqConnectUserInfo qqConnectUserInfo) {
        UserConsumer userConsumer = new UserConsumer();
        String openId = qqConnectUserInfo.getOpenId();
        userConsumer.setNickName(qqConnectUserInfo.getNickName());
        userConsumer.setStatus(UserConsumerStatus.VALID.getIndex());
        userConsumer.setClientVersion("");
        userConsumer.setRegisterType(RegisterType.third_register.getIndex());
        userConsumer.setIcon(qqConnectUserInfo.getIcon());
        userConsumer.setGender(qqConnectUserInfo.getGender());
        userConsumer.setProvince(qqConnectUserInfo.getProvince());
        userConsumer.setCity(qqConnectUserInfo.getCity());
        userConsumer.setUserType(UserType.GENERAL_USR.getIndex());
        userConsumer.setBizSystem(BizSystem.LOCAL.getIndex());
        userConsumer.setRegisterTime(new Date());
        userConsumer.setLastLoginTime(new Date());
        userConsumer.setUserThirdLogins(Lists.newArrayList());
        userConsumer.setThirdId(qqConnectUserInfo.getOpenId());
        userConsumer.setCreateTime(Calendar.getInstance());
        userConsumer.setUpdateTime(Calendar.getInstance());
        userConsumer.setClientType(ClientType.QQ.getIndex());
        userConsumer.setupFeature(QQ_USER_OPENID_FEATURE_KEY, openId);
        return userConsumer;
    }

}
