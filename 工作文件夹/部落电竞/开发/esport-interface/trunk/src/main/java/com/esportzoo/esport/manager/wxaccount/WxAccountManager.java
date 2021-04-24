package com.esportzoo.esport.manager.wxaccount;

import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.constants.*;
import com.esportzoo.esport.constants.wx.AppIdAndSecret;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.UserThirdLogin;
import com.esportzoo.esport.domain.WxAccountUserInfo;
import com.esportzoo.esport.service.consumer.UserConsumerService;
import com.esportzoo.esport.service.consumer.UserThirdLoginService;
import com.esportzoo.esport.service.exception.BusinessException;
import com.esportzoo.esport.util.AccessTokenUtil;
import com.esportzoo.esport.util.RequestUtil;
import com.esportzoo.esport.util.WeChatUtil;
import com.esportzoo.esport.vo.ThirdLoginVo;
import com.esportzoo.esport.vo.UserConsumerQueryOption;
import com.esportzoo.esport.vo.user.WeixinUserVO;
import com.google.common.collect.Lists;
import com.google.gson.Gson;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import java.io.InputStream;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class WxAccountManager {

    private transient static final Logger logger = LoggerFactory.getLogger("request");

    @Value("${esport.weixin.account.appid}")
    private String Appid;
    @Value("${esport.weixin.account.appsecret}")
    private String AppSecret;
    @Value("${esport.api.base.url}")
    private String esportBaseUrl;
    @Value("${ddquiz.api.base.url}")
    private String ddBaseUrl;
    @Autowired
    private RedisClient redisClient;
    private final static String WXACCOUNTKEY = "access_token_";
    private final static String JSTICKETKEY ="jsapi_ticket";
    private static final Gson gson = new Gson();
    @Autowired
    private UserConsumerService userConsumerService;

    @Autowired
    private UserThirdLoginService userThirdLoginService;
    @Autowired
    private AccessTokenUtil accessTokenUtil;


    /*跳转到微信服务端让用户授权获取code*/
    public String toAuth(String param,String backUrl) {
        String weixinOauth2AuthBaseUrl=null;
        //dd竞猜
        if (backUrl.contains("dd")){
            weixinOauth2AuthBaseUrl = WeChatUtil.getWeixinOauth2AuthBaseUrl(Appid, param, backUrl, ddBaseUrl);
        }else {
            //橘子电竞
            weixinOauth2AuthBaseUrl = WeChatUtil.getWeixinOauth2AuthBaseUrl(Appid, param,backUrl,esportBaseUrl);
        }
        return weixinOauth2AuthBaseUrl;
    }

    /**
     * 公众号通过授权获取的code获取  openId
     *
     * @param code
     * @return
     */
    public WeixinUserVO getUserOpenIdForCode(String code) {
        WeixinUserVO vo = WeChatUtil.getUserAccessToken(code, Appid, AppSecret);
        return vo;
    }

    public WxAccountUserInfo getUserInfoByOpenId(String accessToken, String openId) {
        WxAccountUserInfo userInfoByUserToken = WeChatUtil.getUserInfoByUserToken(accessToken, openId);
        return userInfoByUserToken;
    }
    
    public WxAccountUserInfo getScanUserInfoByOpenId(String accessToken, String openId) {
        WxAccountUserInfo userInfoByUserToken = WeChatUtil.getScanUserInfoByUserToken(accessToken, openId);
        return userInfoByUserToken;
    }
    
    /**
     * 同步注册微信公众号的用户信息
     *
     * @return
     */
    public UserConsumer synchronizeUserInfo(WxAccountUserInfo wxAccountUserInfo, BaseRequest baseRequest, HttpServletRequest request) {
        String logPrefix = "微信公众号同步用户信息_";
        /*用户unionid*/
        String unionid = wxAccountUserInfo.getUnionid();
        /*用户openid*/
        String openid = wxAccountUserInfo.getOpenid();
        UserConsumer userConsumer = null;
        /*没有unionid*/
        if (StringUtils.isBlank(unionid)) {
            /**/
            userConsumer = checkBind(openid, wxAccountUserInfo, baseRequest, request);
        }


        /*存在unionid*/
        ModelResult<UserThirdLogin> thirdLogin=null;
        Long userId=null;
        if (StringUtils.isNotBlank(unionid)) {
            //List<UserThirdLogin> userThird = userThirdLoginService.queryByUnionId(unionid).getModel();
            ModelResult<List<UserThirdLogin>> listModelResult = userThirdLoginService.queryByUnionId(unionid);
            if (!listModelResult.isSuccess()){
                logger.error(logPrefix+"查询第三方登录列表出错 【{}】",listModelResult.getErrorMsg());
                return null;
            }
            List<UserThirdLogin> userThird = listModelResult.getModel();
            logger.info(logPrefix+"根据第unionId查到 【{}】条 第三方登录信息",userThird.size());
            if (null == userThird || 0>=userThird.size()) {
                userConsumer = checkBind(openid, wxAccountUserInfo, baseRequest, request);
            } else {
                 userId = userThird.get(0).getUserId();
                /*如果第三方登录中存在unionid 则看存在三方类型是不是微信公众号*/
                List<UserThirdLogin> collect = userThird.stream().filter(userThirdLogin -> {
                    return ThirdType.WECHAT_ACCOUNT.getIndex() == userThirdLogin.getThirdType().intValue();
                }).collect(Collectors.toList());
                if (0 < collect.size()){
                    userId=collect.get(0).getUserId();
                }
                if (0 >= collect.size()) {
                    /*不存在公众号登录 但有用户信息  绑定条三方登录信息*/
                    ThirdLoginVo thirdLoginVo = new ThirdLoginVo(wxAccountUserInfo.getUnionid(), wxAccountUserInfo.getOpenid(), ThirdType.WECHAT_ACCOUNT.getIndex());
                    thirdLogin = userConsumerService.insertUserThirdLogin(thirdLoginVo, userId);
                }
                userConsumer = userConsumerService.queryConsumerById(userId, new UserConsumerQueryOption()).getModel();
            }
        }

        if (null!=thirdLogin){
            if (!thirdLogin.isSuccess()) {
                logger.info("微信公众号openId[{}],本站用户[{}], 绑定关系失败,原因[{}]", wxAccountUserInfo.getOpenid(), userConsumer.getAccount(), thirdLogin.getErrorMsg());
                throw new BusinessException(ResponseConstant.SYSTEM_ERROR_CODE, thirdLogin.getErrorMsg());
            }

            logger.info("微信公众号 openId[{}] 注册并绑定本站用户[{}]成功", wxAccountUserInfo.getOpenid(), userConsumer.getAccount());

        }

        //由于后面昵称修添加了唯一有索引为避免报错，昵称和头像单独更新
        if (null!=userConsumer) {
            if (StringUtils.isNotBlank(wxAccountUserInfo.getNickname()) && !userConsumer.getNickName().contains(wxAccountUserInfo.getNickname())) {
                userConsumer.setNickName(wxAccountUserInfo.getNickname());
                userConsumerService.updateConsumerNickname(userConsumer.getId(), wxAccountUserInfo.getNickname());
            }
            if (StringUtils.isNotBlank(wxAccountUserInfo.getHeadimgurl()) && !wxAccountUserInfo.getHeadimgurl().equals(userConsumer.getIcon())) {
                userConsumer.setIcon(wxAccountUserInfo.getHeadimgurl());
                userConsumerService.updateConsumerIcon(userConsumer.getId(), wxAccountUserInfo.getHeadimgurl());
            }
        }


        return userConsumer;
    }

    private UserConsumer checkBind(String openid, WxAccountUserInfo wxAccountUserInfo, BaseRequest baseRequest, HttpServletRequest request) {
        String logPrefix = "微信公众号同步用户信息_";
        //UserThirdLogin model = userConsumerService.queryUsrThirdByIdAndType(openid, ThirdType.WECHAT_ACCOUNT.getIndex()).getModel();
        ModelResult<UserThirdLogin> userThirdLoginModelResult = userConsumerService.queryUsrThirdByIdAndType(openid, ThirdType.WECHAT_ACCOUNT.getIndex());
        if (!userThirdLoginModelResult.isSuccess()){
            logger.info(logPrefix+"根据微信公众号登录类型和openid查询出现异常 【{}】",userThirdLoginModelResult.getErrorMsg());
            return null;
        }
        UserThirdLogin model = userThirdLoginModelResult.getModel();
        /*新用户未绑定*/
        if (null == model) {
            /*注册并绑定用户*/
            UserConsumer userConsumer = new UserConsumer();
            userConsumer.setNickName(wxAccountUserInfo.getNickname());
            userConsumer.setStatus(UserConsumerStatus.VALID.getIndex());
            userConsumer.setClientVersion("");
            userConsumer.setRegisterType(RegisterType.third_register.getIndex());
            userConsumer.setIcon(wxAccountUserInfo.getHeadimgurl());
            userConsumer.setGender(wxAccountUserInfo.getSex());
            userConsumer.setProvince(wxAccountUserInfo.getProvince());
            userConsumer.setCity(wxAccountUserInfo.getCity());
            userConsumer.setUserType(UserType.GENERAL_USR.getIndex());
            userConsumer.setBizSystem(BizSystem.LOCAL.getIndex());
            userConsumer.setRegisterTime(new Date());
            userConsumer.setLastLoginTime(new Date());
            userConsumer.setUserThirdLogins(Lists.newArrayList());
            userConsumer.setThirdId(wxAccountUserInfo.getOpenid());
            userConsumer.setCreateTime(Calendar.getInstance());
            userConsumer.setUpdateTime(Calendar.getInstance());

			//根据分享码邀请注册
			if (StringUtils.isNotEmpty(baseRequest.getShareCode())){
				userConsumer.setupFeature("shareCode", baseRequest.getShareCode());
			}
            UserOperationParam userOperationParam = new UserOperationParam(RequestUtil.getClientIp(request), ClientType.WXGZH.getIndex(), baseRequest.getAgentId(), baseRequest.getVersion());
            ModelResult<UserConsumer> modelResult = userConsumerService.register(userConsumer, userOperationParam);
            if (!modelResult.isSuccess()) {
                logger.error("微信公众号 openId[{}]注册失败,失败原因[{}]", wxAccountUserInfo.getOpenid(), modelResult.getErrorMsg());
                throw new BusinessException(ResponseConstant.RESP_PARAM_ERROR_CODE, modelResult.getErrorMsg());
            }

            UserConsumer member = modelResult.getModel();
            // 绑定关系
            ThirdLoginVo thirdLoginVo = new ThirdLoginVo(wxAccountUserInfo.getUnionid(), wxAccountUserInfo.getOpenid(), ThirdType.WECHAT_ACCOUNT.getIndex());
            ModelResult<UserThirdLogin> thirdLogin = userConsumerService.insertUserThirdLogin(thirdLoginVo, member.getId());
            if (!thirdLogin.isSuccess()) {
                logger.info("微信公众号openId[{}],本站用户[{}], 绑定关系失败,原因[{}]", wxAccountUserInfo.getOpenid(), member.getAccount(), thirdLogin.getErrorMsg());
                throw new BusinessException(ResponseConstant.SYSTEM_ERROR_CODE, thirdLogin.getErrorMsg());
            }

            logger.info("微信公众号 openId[{}] 注册并绑定本站用户[{}]成功", wxAccountUserInfo.getOpenid(), member.getAccount());
            return member;
        }
        if (null != model) {
            Long userId = model.getUserId();
            UserConsumer model1 = userConsumerService.queryConsumerById(userId, new UserConsumerQueryOption()).getModel();
            if (null == model1) {
                logger.info(logPrefix + "根据第三方绑定获取到无效的用户id[{}]",userId);
            }
            return model1;
        }
        return null;
    }


    public Map<String, Object> getWxConfig(HttpServletRequest request) {
        Map<String, Object> ret = new HashMap<String, Object>();
        String requestUrl = request.getRequestURL().toString();
        String jsTicket = "";
        String timestamp = Long.toString(System.currentTimeMillis() / 1000); // 必填，生成签名的时间戳
        String nonceStr = UUID.randomUUID().toString(); // 必填，生成签名的随机串

        jsTicket = getJsApiTicket();
        String signature = "";
        // 注意这里参数名必须全部小写，且必须有序
        String sign = "jsapi_ticket=" + jsTicket + "&noncestr=" + nonceStr + "×tamp=" + timestamp + "&url=" + requestUrl;
		logger.info("sign：【{}】",sign);
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(sign.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (Exception e) {
           logger.info("加密签名的时候 出现异常 【{}】",e.getMessage(),e);
        }
        logger.info("jsApiTicket 签名生成【{}】",signature);
        ret.put("appId", Appid);
        ret.put("timestamp", timestamp);
        ret.put("nonceStr", nonceStr);
        ret.put("signature", signature);
        return ret;
    }

	/**
	 * js验证时所需的签名
	 * @param request
	 * @param reqUrl
	 * @return
	 */
	public Map<String, Object> getWxConfig(HttpServletRequest request,String reqUrl) {
		Map<String, Object> ret = new HashMap<String, Object>();
		String signStr="";
		try {

			reqUrl = URLDecoder.decode(reqUrl,"utf-8");
			logger.info("传入的要分享的地址:【{}】",reqUrl);
			String ticket = getJsApiTicket();
			String timestamp = Long.toString(System.currentTimeMillis() / 1000); // 必填，生成签名的时间戳
			String nonceStr = UUID.randomUUID().toString(); // 必填，生成签名的随机串
			 signStr = MessageFormatter
					.arrayFormat("jsapi_ticket={}&noncestr={}&timestamp={}&url={}", new Object[]{ticket,nonceStr,timestamp,reqUrl}).getMessage();
			logger.info("用户签名信息：【{}】",signStr);
			String signature = sha1(signStr);
			logger.info("用户获取的签名：【{}",signature);
			ret.put("appId", Appid);
			ret.put("timestamp", timestamp);
			ret.put("nonceStr", nonceStr);
			ret.put("signature", signature);

		} catch (Exception e) {
			logger.info("获取用户签名信息出错，签名：【{}】，返货参数param 【{}】，异常信息:{}",signStr, JSON.toJSONString(ret),e);
			e.printStackTrace();
			return ret;
		}
		return ret;

	}

	public Map<String, Object> getWxShareConfig(HttpServletRequest request) {
		Map<String, Object> ret = new HashMap<String, Object>();
		String requestUrl = request.getRequestURL().toString();
		String jsTicket = "";
		String timestamp = Long.toString(System.currentTimeMillis() / 1000); // 必填，生成签名的时间戳
		String nonceStr = UUID.randomUUID().toString(); // 必填，生成签名的随机串

		jsTicket = getJsApiTicket();
		String signature = "";
		// 注意这里参数名必须全部小写，且必须有序
		String sign = "jsapi_ticket=" + jsTicket + "&noncestr=" + nonceStr + "×tamp=" + timestamp + "&url=" + requestUrl;

		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(sign.getBytes("UTF-8"));
			signature = byteToHex(crypt.digest());
		} catch (Exception e) {
			logger.info("加密签名的时候 出现异常 【{}】",e.getMessage(),e);
		}
		logger.info("jsApiTicket 签名生成【{}】",signature);
		ret.put("appId", Appid);
		ret.put("timestamp", timestamp);
		ret.put("nonceStr", nonceStr);
		ret.put("signature", signature);
		return ret;
	}


    /**
     * 方法名：byteToHex</br>
     * 详述：字符串加密辅助方法 </br>
     * @param hash
     * @return 说明返回值含义
     * @throws 说明发生此异常的条件
     */
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;

    }



    public String getAppid() {
        return Appid;
    }

    public String getAppSecret() {
        return AppSecret;
    }

    public String getJsApiTicket() {
        if (StringUtils.isBlank(redisClient.get(JSTICKETKEY))) {
            synchronized (WxAccountManager.class) {
                if (StringUtils.isBlank(redisClient.get(JSTICKETKEY))) {
                    String accessToken = accessTokenUtil.getAccessToken(AppIdAndSecret.wxService.getIndex());
                    String jsApiTicketkey = WeChatUtil.getJsTicket(accessToken);
                    redisClient.set(JSTICKETKEY, jsApiTicketkey, 7200);
                    return jsApiTicketkey;
                } else {
                    return redisClient.get(JSTICKETKEY);
                }
            }
        } else {
            return redisClient.get(JSTICKETKEY);
        }
    }

	/**
	 * 微信JS sha1签名
	 * @param signStr
	 * @return
	 * @throws Exception
	 */
	public  String sha1(String signStr) throws Exception{
		MessageDigest crypt = MessageDigest.getInstance("SHA-1");
		crypt.reset();
		crypt.update(signStr.getBytes("UTF-8"));

		Formatter formatter = new Formatter();
		for (byte b : crypt.digest()){
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	/***
	 * httpClient-Get请求
	 * 
	 * @param url 请求地址
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> httpClientGet(String url) throws Exception {
		HttpClient client = new HttpClient();
		client.getParams().setContentCharset("UTF-8");
		GetMethod httpGet = new GetMethod(url);
		try {
			client.executeMethod(httpGet);
			String response = httpGet.getResponseBodyAsString();
			Map<String, Object> map = gson.fromJson(response, Map.class);
			return map;
		} catch (Exception e) {
			throw e;
		} finally {
			httpGet.releaseConnection();
		}
	}

	/***
	 * httpClient-Post请求
	 * 
	 * @param url    请求地址
	 * @param params post参数
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> httpClientPost(String url, String params) throws Exception {
		HttpClient client = new HttpClient();
		client.getParams().setContentCharset("UTF-8");
		PostMethod httpPost = new PostMethod(url);
		try {
			RequestEntity requestEntity = new ByteArrayRequestEntity(params.getBytes("utf-8"));
			httpPost.setRequestEntity(requestEntity);
			client.executeMethod(httpPost);
			String response = httpPost.getResponseBodyAsString();
			Map<String, Object> map = gson.fromJson(response, Map.class);
			return map;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			httpPost.releaseConnection();
		}
	}
	
	// xml转为map
	public Map<String, String> xmlToMap(HttpServletRequest httpServletRequest) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			InputStream inputStream = httpServletRequest.getInputStream();
			SAXReader reader = new SAXReader(); // 读取输入流
			Document document = reader.read(inputStream);
			Element root = document.getRootElement(); // 得到xml根元素
			List<Element> elementList = root.elements(); // 得到根元素的所有子节点
			// 遍历所有子节点
			for (Element e : elementList)
				map.put(e.getName(), e.getText());
			// 释放资源
			inputStream.close();
			inputStream = null;
			return map;
		} catch (Exception e) {
			e.getMessage();
		}
		return null;
	}
	
	//扫码入参
	public String sacnReqParams(String sceneStr) {
		String params = "{\"expire_seconds\":600, \"action_name\":\"QR_STR_SCENE\", \"action_info\":{\"scene\":{\"scene_str\":\""
				+ sceneStr + "\"}}}";
		return params;
	}
}
