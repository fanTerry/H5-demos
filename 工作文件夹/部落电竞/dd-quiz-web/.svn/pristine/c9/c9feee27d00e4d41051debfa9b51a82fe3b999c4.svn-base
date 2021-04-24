package com.esportzoo.esport.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.xerces.impl.dv.util.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.client.service.consumer.UserThirdLoginServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.LoginRequest;
import com.esportzoo.esport.connect.request.WxDecodePhoneRequest;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.constants.BizSystem;
import com.esportzoo.esport.constants.RegisterType;
import com.esportzoo.esport.constants.ThirdType;
import com.esportzoo.esport.constants.UserConsumerStatus;
import com.esportzoo.esport.constants.UserOperationParam;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.UserThirdLogin;
import com.esportzoo.esport.domain.WeMiniUserVo;
import com.esportzoo.esport.service.exception.BusinessException;
import com.esportzoo.esport.util.RequestUtil;
import com.esportzoo.esport.util.SHA1Util;
import com.esportzoo.esport.vo.ThirdLoginVo;
import com.esportzoo.esport.vo.user.WxDecodePhoneResponse;

@Component
public class WeChatManager {

	private transient static final Logger logger = LoggerFactory.getLogger(WeChatManager.class);
	private static String WECHATSESSIONKEY = "WECHATSESSIONKEY_";

	@Value("${esport.weixin.mini.appid}")
	private String appid;
	@Value("${esport.weixin.mini.appsecret}")
	private String appSecret;

	@Autowired
	@Qualifier("userConsumerServiceClient")
	private UserConsumerServiceClient userConsumerServiceClient;
	@Autowired
	private UserThirdLoginServiceClient userThirdLoginServiceClient;
	@Autowired
	private RedisClientManager redisClientManager;
	@Autowired
	private UserManager userManager;
	
	private static final String wechat_mini_request_url = "https://api.weixin.qq.com/sns/jscode2session";
	
	
	public UserConsumer synchronizeUserInfo(LoginRequest loginRequest, BaseRequest baseRequest, HttpServletRequest request, String sid) {
		String logPrefix = "微信小程序同步用户信息_";
		try {
			UserConsumer userConsumer = null;
			
			//获取微信小程序用户信息-start
			JSONObject sessionKeyOpenId = getSessionKeyOrOpenId(loginRequest.getCode());
			String openid = sessionKeyOpenId.getString("openid");
			String sessionKey = sessionKeyOpenId.getString("session_key");
			logger.info(logPrefix + "当前用户openId={},session_key={}", openid, sessionKey);
			
			String cacheValue = sessionKey + "|" + openid;
			redisClientManager.set(WECHATSESSIONKEY + sid, cacheValue);
			logger.info(logPrefix + "当前sid={}, 缓存值={}", sid, cacheValue);
			
			String caledSign = SHA1Util.sha1Encrypted(loginRequest.getRawData() + sessionKey);
			logger.info(logPrefix + "caledSign={}", caledSign);
			if (!loginRequest.getSignature().equals(caledSign)) {
				logger.info(logPrefix + "验签不通过");
				return null;
			}
			JSONObject userInfoData = getUserInfo(loginRequest.getEncrypteData(), sessionKey, loginRequest.getIv());
			logger.info(logPrefix + "根据解密算法获取的小程序用户信息:{}",userInfoData);
			WeMiniUserVo userVo = WeMiniUserVo.convertByJson(userInfoData);
			//获取微信小程序用户信息-end
			
			String unionId = userVo.getUnionId();
			logger.info(logPrefix + "unionId={}", unionId);
			//List<UserThirdLogin> unionIdBindList =  userThirdLoginServiceClient.queryByUnionId(unionId).getModel();
			ModelResult<List<UserThirdLogin>> listModelResult = userThirdLoginServiceClient.queryByUnionId(unionId);
			if (!listModelResult.isSuccess()){
			    logger.info(logPrefix+"根据unionid查询出现异常 unionid：【{}】 error：【{}】",unionId,listModelResult.getErrorMsg());
				return null;
			}
			List<UserThirdLogin> unionIdBindList = listModelResult.getModel();
			//根据unionid没有找到三方登录信息
			if (unionIdBindList==null || unionIdBindList.size()<=0) {
				UserThirdLogin openIdBind = userManager.getUserThirdLogin(userVo.getOpenId(), ThirdType.WECHAT_MINI.getIndex());
				if (openIdBind==null) {
					//unionid 和openid 三方登录类型都没有查询到信息  新用户 注册绑定
					userConsumer = weChatMiniUserRegister(userVo, baseRequest, request);
				} else {
					userConsumer = userConsumerServiceClient.queryConsumerById(openIdBind.getUserId(), null).getModel();
					if (StringUtils.isBlank(openIdBind.getUnionId())) {
						userThirdLoginServiceClient.updateUnionId(openIdBind.getId(), unionId);
					}
				}
			} else {
				UserThirdLogin unionIdBind = unionIdBindList.get(0);
				Long userId = unionIdBind.getUserId();
				logger.info("用户【{}】 根据unionid查询到【{}】条记录",userId,unionIdBindList.size());
				//是否存在unionid中有微信小程序登录的三方登录记录
				List<UserThirdLogin> collect = unionIdBindList.stream().filter(x -> {
					return ThirdType.WECHAT_MINI.getIndex() == x.getThirdType().intValue();
				}).collect(Collectors.toList());
				if (0<collect.size()){
				    userId=collect.get(0).getUserId();
					logger.info("查询到小程序三方登录 userid【{}】",userId);
				}
				userConsumer = userConsumerServiceClient.queryConsumerById(userId, null).getModel();
				//该三方登录信息是否存在  TODO 问题记录 这里查询次数可能过多
				UserThirdLogin openIdBind = userManager.getUserThirdLogin(userVo.getOpenId(), ThirdType.WECHAT_MINI.getIndex());
				//不存在则绑定一条
				if (openIdBind==null) {
					ThirdLoginVo thirdLoginVo = new ThirdLoginVo(userVo.getUnionId(), userVo.getOpenId(), ThirdType.WECHAT_MINI.getIndex());
					userConsumerServiceClient.insertUserThirdLogin(thirdLoginVo, userConsumer.getId());
				} else {
					if (StringUtils.isBlank(openIdBind.getUnionId())) {
						userThirdLoginServiceClient.updateUnionId(openIdBind.getId(), unionId);
					}
				}
			}
			
			//由于后面昵称修添加了唯一有索引为避免报错，昵称和头像单独更新
			//if (StringUtils.isNotBlank(userVo.getNickName()) && !userVo.getNickName().equals(userConsumer.getNickName())) {
			if (StringUtils.isNotBlank(userVo.getNickName()) && !userConsumer.getNickName().contains(userVo.getNickName())) {
				userConsumerServiceClient.updateConsumerNickname(userConsumer.getId(), userVo.getNickName());
				userConsumer.setNickName(userVo.getNickName());
			}
			if (StringUtils.isNotBlank(userVo.getAvatarUrl()) && !userVo.getAvatarUrl().equals(userConsumer.getIcon())) {
				userConsumerServiceClient.updateConsumerIcon(userConsumer.getId(), userVo.getAvatarUrl());
				userConsumer.setIcon(userVo.getAvatarUrl());
			}

			return userConsumer;
		} catch (Exception e) {
			logger.info(logPrefix + "发生异常exception={}", e.getMessage(), e);
			return null;
		}
	}


	/** 小程序用户是否绑定过 **/
	public UserThirdLogin isBindWeChatMini(String openId) {
		ModelResult<UserThirdLogin> modelResult = userConsumerServiceClient.queryUsrThirdByIdAndType(openId, ThirdType.WECHAT_MINI.getIndex());
		if (!modelResult.isSuccess()) {
			throw new BusinessException(modelResult.getErrorCode(), modelResult.getErrorMsg());
		}

		return modelResult.getModel();
	}
	
	/**根据第三方id查询用户*/
	public UserConsumer queryUsrById(Long id) {
		ModelResult<UserConsumer> modelResult = userConsumerServiceClient.queryConsumerById(id, null);
		if (!modelResult.isSuccess()) {
			throw new BusinessException(modelResult.getErrorCode(), modelResult.getErrorMsg());
		}
		return modelResult.getModel();
	}
	
	/**修改用户信息*/
	public void modifyConsumerInfo(UserConsumer consumer) {
		ModelResult<Integer> result = userConsumerServiceClient.updateConsumerInfo(consumer, null);
		if(!result.isSuccess()){
			logger.error("用户id[{}]修改 用户信息时失败，错误信息[{}]", consumer.getId(), result.getErrorMsg());
			return;
		}
		logger.info("用户id[{}]修改 用户信息 完成 updateCount[{}]", consumer.getId(), null==result.getModel()?0:result.getModel());
	}

	public UserConsumer weChatMiniUserRegister(WeMiniUserVo userVo, BaseRequest baseRequest, HttpServletRequest request) {
		if (StringUtils.isBlank(userVo.getOpenId())) {
			logger.error("微信小程序用户 openId[{}]注册失败,失败原因openId参数异常", userVo.getOpenId());
			throw new BusinessException(ResponseConstant.RESP_PARAM_ERROR_CODE, "openId参数异常");
		}
		// 注册
		UserOperationParam usrOperationParm = new UserOperationParam(RequestUtil.getClientIp(request), baseRequest.getDefaultClientType().getIndex(),
				baseRequest.getAgentId(), baseRequest.getVersion());
		Date now = new Date();
		UserConsumer reqMember = new UserConsumer();
		if (StringUtils.isBlank(userVo.getNickName())) {
			userVo.setNickName("mini" + DateUtil.dateToString(new Date(), "yyyyMMdd") + RandomStringUtils.random(6, false, true));
		}
		reqMember.setNickName(userVo.getNickName());
		// 头像同步微信小程序
		if (StringUtils.isBlank(userVo.getAvatarUrl())) {
			// userVo.setAvatarLarge(host + "/image/icon/default.png");
		}
		reqMember.setIcon(userVo.getAvatarUrl());
		reqMember.setStatus(UserConsumerStatus.VALID.getIndex());
		reqMember.setRegisterTime(now);
		reqMember.setLastLoginTime(now);
		reqMember.setCity(userVo.getCity());
		reqMember.setProvince(userVo.getProvince());
		reqMember.setGender(userVo.getGender());
		reqMember.setRegisterType(RegisterType.third_register.getIndex());
		reqMember.setBizSystem(BizSystem.LOCAL.getIndex());
		ModelResult<UserConsumer> modelResult = userConsumerServiceClient.register(reqMember, usrOperationParm);
		if (!modelResult.isSuccess()) {
			logger.error("微信小程序用户 openId[{}]注册失败,失败原因[{}]", userVo.getOpenId(), modelResult.getErrorMsg());
			throw new BusinessException(ResponseConstant.RESP_PARAM_ERROR_CODE, modelResult.getErrorMsg());
		}

		UserConsumer member = modelResult.getModel();
		// 绑定关系
		ThirdLoginVo thirdLoginVo = new ThirdLoginVo(userVo.getUnionId(), userVo.getOpenId(), ThirdType.WECHAT_MINI.getIndex());
		ModelResult<UserThirdLogin> thirdLogin = userConsumerServiceClient.insertUserThirdLogin(thirdLoginVo, member.getId());
		if (!thirdLogin.isSuccess()) {
			logger.info("微信小程序用户openId[{}],本站用户[{}], 绑定关系失败,原因[{}]", userVo.getOpenId(), member.getAccount(), thirdLogin.getErrorMsg());
			throw new BusinessException(ResponseConstant.SYSTEM_ERROR_CODE, thirdLogin.getErrorMsg());
		}

		logger.info("微信小程序用户 openId[{}] 注册并绑定本站用户[{}]成功", userVo.getOpenId(), member.getAccount());

		return member;
	}

	public JSONObject getSessionKeyOrOpenId(String code) {
		Map<String, String> requestUrlParam = new HashMap<String, String>();
		requestUrlParam.put("appid", getAppid());// 小程序appId
		requestUrlParam.put("secret", getAppSecret());
		requestUrlParam.put("js_code", code);// 小程序端返回的code
		requestUrlParam.put("grant_type", "authorization_code");// 默认参数
		// 发送post请求读取调用微信接口获取openid用户唯一标识
		JSONObject jsonObject = JSON.parseObject(sendPost(wechat_mini_request_url, requestUrlParam));
		logger.info("微信登录，通过临时登录凭证得到的返回结果为={}", jsonObject.toJSONString());
		return jsonObject;
	}

	public JSONObject getUserInfo(String encryptedData, String sessionKey, String iv) {
		// 被加密的数据
		byte[] dataByte = Base64.decode(encryptedData);
		// 加密秘钥
		byte[] keyByte = Base64.decode(sessionKey);
		// 偏移量
		byte[] ivByte = Base64.decode(iv);
		try {
			// 如果密钥不足16位，那么就补足. 这个if 中的内容很重要
			int base = 16;
			if (keyByte.length % base != 0) {
				int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
				byte[] temp = new byte[groups * base];
				Arrays.fill(temp, (byte) 0);
				System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
				keyByte = temp;
			}
			// 初始化
			Security.addProvider(new BouncyCastleProvider());
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
			SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
			AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
			parameters.init(new IvParameterSpec(ivByte));
			cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
			byte[] resultByte = cipher.doFinal(dataByte);
			if (null != resultByte && resultByte.length > 0) {
				String result = new String(resultByte, "UTF-8");
				return JSON.parseObject(result);
			}
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
		} catch (NoSuchPaddingException e) {
			logger.error(e.getMessage(), e);
		} catch (InvalidParameterSpecException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalBlockSizeException e) {
			logger.error(e.getMessage(), e);
		} catch (BadPaddingException e) {
			logger.error(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			logger.error(e.getMessage(), e);
		} catch (InvalidAlgorithmParameterException e) {
			logger.error(e.getMessage(), e);
		} catch (NoSuchProviderException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	public WxDecodePhoneResponse wxDecodePhone (WxDecodePhoneRequest wxDecodePhoneRequest) {
		String logPrefix = "微信解码手机号_";
		WxDecodePhoneResponse wxDecodePhoneResponse = new WxDecodePhoneResponse();
		try {
			String sid = wxDecodePhoneRequest.getSid();
			String cacheValue = redisClientManager.get(WECHATSESSIONKEY + sid);
			logger.info(logPrefix + "cacheValue={}", cacheValue);
			if (StringUtils.isBlank(cacheValue)) {
				return null;
			}
			String[] strArr = cacheValue.split("\\|");
			String sessionKey = strArr[0];
			String openid = strArr[1];
			logger.info(logPrefix + "sessionKey={},openid={}", sessionKey, openid);
	        
			byte[] dataByte = Base64.decode(wxDecodePhoneRequest.getEncryptedData());
			byte[] keyByte = Base64.decode(sessionKey);
			byte[] ivByte = Base64.decode(wxDecodePhoneRequest.getIv());
			int base = 16;
			if (keyByte.length % base != 0) {
				int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
				byte[] temp = new byte[groups * base];
				Arrays.fill(temp, (byte) 0);
				System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
				keyByte = temp;
			}
			Security.addProvider(new BouncyCastleProvider());
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
			SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
			AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
			parameters.init(new IvParameterSpec(ivByte));
			cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
			byte[] resultByte = cipher.doFinal(dataByte);
			String resultString = new String(resultByte, "UTF-8");
	        
	        logger.info(logPrefix + "解密数据resultString={}", resultString);
	        wxDecodePhoneResponse = JSON.parseObject(resultString, WxDecodePhoneResponse.class);
	        wxDecodePhoneResponse.setOpenid(openid);
	        logger.info(logPrefix + "解密数据wxDecodePhoneResponse={}", JSON.toJSONString(wxDecodePhoneResponse));
	        return wxDecodePhoneResponse;
		} catch (Exception e) {
			logger.info(logPrefix + "发生异常exception={}", e.getMessage(), e);
			return null;
		}
	}

	public static String sendPost(String url, Map<String, ?> paramMap) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";

		String param = "";
		Iterator<String> it = paramMap.keySet().iterator();

		while (it.hasNext()) {
			String key = it.next();
			param += key + "=" + paramMap.get(key) + "&";
		}

		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Accept-Charset", "utf-8");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			// logger.error(e.getMessage(), e);
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public String getAppid() {
		return appid;
	}
	
	public String getAppSecret() {
		return appSecret;
	}
	
}