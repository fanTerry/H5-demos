package com.esportzoo.esport.controller.qqconnect;


import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.redisclient.core.JedisClusterClientImpl;
import com.esportzoo.common.util.CookieUtils;
import com.esportzoo.esport.constant.CachedKeyAndTimeLong;
import com.esportzoo.esport.constant.QqConnectConstants;
import com.esportzoo.esport.constants.ClientType;
import com.esportzoo.esport.constants.user.MemberConstants;
import com.esportzoo.esport.domain.QqConnectUserInfo;
import com.esportzoo.esport.domain.SynchronizeRes;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.exception.CommonExceptionCode;
import com.esportzoo.esport.manager.CachedManager;
import com.esportzoo.esport.manager.qqconnect.QqLoginManager;
import com.esportzoo.esport.service.consumer.UserConsumerService;

import com.esportzoo.esport.service.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * @author jiajing.he
 * @date 2020/2/21 15:12
 */
@Controller
@RequestMapping("qqlogin")
public class QqLoginController {

    private Logger logger = LoggerFactory.getLogger(QqLoginController.class);
    private static String loggerPre = "QQ授权登录_";


    @Autowired
    QqLoginManager qqLoginManager;
    @Autowired
    @Qualifier("cachedManager")
    private CachedManager cachedManager;
    @Autowired
    UserConsumerService userConsumerService;
    @Autowired
    private JedisClusterClientImpl redisClient;

    @Value("${qq.connect.appid}")
    private String appId;

//    @Value("${app.qq.connect.appid}")
//    private String appAppId;

    @Value("${qq.connect.url}")
    private String redirectUrl;

//    @Value("${app.qq.connect.url}")
//    private String appRedirectUrl;

    /**
     * 发起请求
     *
     * @param
     * @return
     */
    @RequestMapping("/toqqlogin")
    public ModelAndView toqqLogin( HttpSession session,HttpServletRequest request) {
        String format = "";
        try {
            //用于第三方应用防止CSRF攻击
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            session.setAttribute("state", uuid);
            String redirect = request.getParameter("redirect");
            logger.info(loggerPre+"登录成功后的跳转连接redirect={}",redirect);
            if (StringUtils.isBlank(redirect)) {
                logger.info(loggerPre+"redirect参数为空！");
                return null;
            }
            try {
                redirect = URLDecoder.decode(redirect, "utf-8");
            } catch (UnsupportedEncodingException e) {
                logger.info(loggerPre+"回调地址解码异常");
            }
//            String clientType = getParameter(redirect,"clientType");
            /**Step1：获取Authorization Code*/
            String backUrl =redirectUrl+"?redirect="+redirect;
            String urlEncode = URLEncoder.encode(backUrl, "utf-8");
            format = String.format(QqConnectConstants.toqqLoginURL, appId, urlEncode, uuid);
            logger.info("拉起QQ的URL：{}", format);
            return new ModelAndView(new RedirectView(format));
        } catch (IOException e) {
            logger.info(loggerPre+"跳转授权页出现异常[{}]", e.getMessage(), e);
            return null;
        }
    }

    /**
     * QQ回调
     *
     * @param request
     * @return
     */
    @RequestMapping("/index")
    @CrossOrigin
    public ModelAndView qqcallback(HttpServletRequest request, HttpServletResponse response)  {
        String redirect ="";
        try {
            HttpSession session = request.getSession();
            //qq返回的信息
            String code = request.getParameter("code");
            String state = request.getParameter("state");
            redirect = request.getParameter("redirect");
            logger.info(loggerPre+"回调拿到参数 【code:{}】 【重定向地址：{}】", code, redirect);
            String uuid = (String) session.getAttribute("state");
            if (uuid != null && !uuid.equals(state)) {
                logger.info(loggerPre+"回调state出错");
                throw new BusinessException(CommonExceptionCode.SYSTEM_EXCEPTION);
            }
            if (!code.isEmpty()&& !redirect.isEmpty()) {
                String clientType = getParameter(redirect,"clientType");
                /**Step2：通过Authorization Code获取Access Token*/
                String accessToken = qqLoginManager.getAccessToken(code);
                /**Step3: 获取回调后的 openid 值*/
                redirect = setUserInfo(request, response, redirect, clientType, accessToken);
            }
            RedirectView redirectView = new RedirectView(redirect);
            logger.info(loggerPre+"页面{}",JSONObject.toJSONString(redirectView));
            return new ModelAndView(new RedirectView(redirect));
            //返回用户的信息
        } catch (Exception e) {
            logger.info(loggerPre+"回调出现异常 【{}】", e.getMessage(), e);
            return null;
        }
    }

    private String setUserInfo(HttpServletRequest request, HttpServletResponse response, String redirect, String clientType, String accessToken) throws UnsupportedEncodingException, MalformedURLException {
        String sid =null;
        UserConsumer userConsumer=null;
        String openid = qqLoginManager.getOpenId(accessToken);
        /**Step4：获取QQ用户信息*/
        QqConnectUserInfo qquserInfo = qqLoginManager.getUserInfo(openid, accessToken);
        /**Step5: 绑定用户*/
        SynchronizeRes synchronizeUserInfo = qqLoginManager.synchronizeUserInfo(qquserInfo);
        logger.info(loggerPre+"synchronizeUserInfo={}", JSONObject.toJSONString(synchronizeUserInfo));
        //已注册
        if (!synchronizeUserInfo.getNewUser()) {
            sid = UUID.randomUUID().toString();
            CookieUtils.setCookie(request, response, MemberConstants.H5_LOGIN_COOKIE_SID, sid,
                    CachedKeyAndTimeLong.MEMBER_MEMCACHE_EXP);
            userConsumer = synchronizeUserInfo.getUserConsumer();
            cachedManager.cachedMemberSession(userConsumer, sid);
        } else {
            //未注册，重定向到手机号注册页
            sid = synchronizeUserInfo.getKey();
            userConsumer = synchronizeUserInfo.getUserConsumer();
            redisClient.setObj(sid, CachedKeyAndTimeLong.setMinutes(3), userConsumer);
            redirect = URLDecoder.decode(redirect, "utf-8");
            logger.info(loggerPre+"redirect ={}"+redirect);
            URL url=new URL(redirect);
            redirect= url.getProtocol()+"://"+url.getAuthority()+"/register"+"?clientType=" + clientType +"&key="+sid+"&newUserType="+synchronizeUserInfo.getNewUser();
            logger.info(loggerPre+"跳转到手机注册页backUrl={}",redirect);
        }
        return redirect;
    }

//    /**
//     * 测试App内的回调
//     * @param request
//     * @param response
//     * @return
//     */
//    @RequestMapping("/appIndex")
//    @CrossOrigin
//    public ModelAndView appqqCallBack(HttpServletRequest request, HttpServletResponse response)  {
//        String redirect ="";
//        try {
//            HttpSession session = request.getSession();
//            //qq返回的信息
//            String accessToken = request.getParameter("access_token");
//            String state = request.getParameter("state");
//            redirect = request.getParameter("redirect");
//            logger.info(loggerPre+"APP回调参数accessToken={},state={},redirect={}",accessToken,state,redirect);
//            String uuid = (String) session.getAttribute("state");
//            if (uuid != null && !uuid.equals(state)) {
//                logger.info(loggerPre+"APP回调state出错");
//                throw new BusinessException(CommonExceptionCode.SYSTEM_EXCEPTION);
//            }
//            if (!redirect.isEmpty()) {
//                String clientType = getParameter(redirect,"clientType");
//                redirect = setUserInfo(request, response, redirect, clientType, accessToken);
//            }
//            RedirectView redirectView = new RedirectView(redirect);
//            logger.info(loggerPre+"页面{}",JSONObject.toJSONString(redirectView));
//            return new ModelAndView(new RedirectView(redirect));
//            //返回用户的信息
//        } catch (Exception e) {
//            logger.info(loggerPre+"APP回调出现异常 【{}】", e.getMessage(), e);
//            return null;
//        }
//    }

    /** 获取url参数的方法*/
    private String getParameter(String url,String neededParameter) throws MalformedURLException {
        logger.info("获取url的参数 url={},neededParameter={}",url,neededParameter);
        URL geturl = new URL(url);
        String parameter=null;
        String urlRef=null;
        //获取#后面的内容
        if (url.contains("#")){
            urlRef = geturl.getRef();
        }else {
            urlRef= geturl.getQuery();
        }
        if(urlRef.indexOf(neededParameter) >= 0){
            String[] array = urlRef.split("&");
            for (String str : array){
                if(str.indexOf(neededParameter) >= 0){
                    parameter = str.substring(str.indexOf("=") + 1);
                    break;
                }
            }
        }
        logger.info("获得的参数 parameter={}",parameter);
        return parameter;
    }




}
