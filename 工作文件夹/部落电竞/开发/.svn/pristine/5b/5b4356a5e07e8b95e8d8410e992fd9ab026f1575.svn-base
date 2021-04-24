package com.esportzoo.esport.manager.sms;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.redisclient.core.JedisClusterClientImpl;
import com.esportzoo.common.util.CookieUtils;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.client.service.sms.SmsLogServiceClient;
import com.esportzoo.esport.connect.request.sms.SendALiYunSmsParam;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.constants.sms.SendSmsEnum;
import com.esportzoo.esport.constants.sms.ShortMessageServiceProvider;
import com.esportzoo.esport.constants.sms.SmsLogStatus;
import com.esportzoo.esport.constants.sms.ToSendSmsExceptionType;
import com.esportzoo.esport.constants.user.MemberConstants;
import com.esportzoo.esport.domain.SmsLog;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.service.exception.errorcode.UserConsumerErrorTable;
import com.esportzoo.esport.service.sms.AliYunShortMessageService;
import com.esportzoo.esport.util.RegisterValidUtil;
import com.esportzoo.esport.util.RequestUtil;
import com.esportzoo.esport.vo.UserConsumerQueryVo;
import com.esportzoo.esport.vo.sms.AliYunSendSmsParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author jiajing.he
 * @date 2019/12/13 18:21
 */
@Component
public class SmsManager {
    private transient final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String VALID_CODE_COUNT_IP_UA = "valid_code_count_ip_ua_";
    private static final String VALID_CODE_COUNT_IP = "valid_code_count_ip_";
    private static final String VALID_CODE_COUNT_COOKIE_KEY = "valid_code_count_cookie_key";
    private static final String PHONE_REGISTER_VALID_CODE = "phone_register_valid_code_";
    private static final String RESET_PASSWORD_VALID_CODE_ = "reset_password_valid_code_";
    /**
     * 用户绑定手机号
     */
    private static final String USER_BINDING_PHONE_VALID_CODE = "user_binding_phone_valid_code_";

    /**
     * 用户手机验证码登录
     */
    private static final String USER_PHONE_LOGIN_VALID_CODE = "user_phone_login_valid_code_";

    @Autowired
    private JedisClusterClientImpl redisClientManager;
    @Autowired
    private UserConsumerServiceClient userConsumerServiceClient;
    @Autowired
    private AliYunShortMessageService aliYunShortMessageService;
    @Autowired
    private SmsLogServiceClient smsLogServiceClient;


    /** 每增加一个短信类型 {@link SendSmsEnum} 与该方法都要对应的增加判断*/
    public CommonResponse checkSmsCondition(SendSmsEnum sendSmsEnum,UserConsumer userConsumer,String phone){
        if (null== sendSmsEnum || StrUtil.isBlank(phone)){
           throw new RuntimeException("checkSmsCondition 参数为空！");
        }

        if (sendSmsEnum.equals(SendSmsEnum.BINDING_CODE)){
            if (null==userConsumer){
                return CommonResponse.withErrorResp("请先登录!");
            }
            if (StrUtil.isNotBlank(userConsumer.getPhone())){
                logger.info("用户[{}] 已经绑定手机[{}] 不能再次绑定！", userConsumer.getId(),userConsumer.getPhone());
                return CommonResponse.withErrorResp("已绑定手机!");
            }
            //检查手机是否绑定
            Boolean aBoolean = checkPhoneValid(phone, sendSmsEnum.getLogPrefix());
            if (!aBoolean){
                return CommonResponse.withErrorResp("该手机号已被绑定！");
            }
        }

        if (sendSmsEnum.equals(SendSmsEnum.LOG_IN_CODE)){
            //检查手机是否存在db
            Boolean aBoolean = checkPhoneValid(phone, sendSmsEnum.getLogPrefix());
            if (aBoolean){
                return CommonResponse.withErrorResp("该手机号不存在！");
            }
        }

        if (sendSmsEnum.equals(SendSmsEnum.REAL_NAME_AUTHENTICATION)){
            //实名认证的条件暂无
        }
        return CommonResponse.withSuccessResp(Boolean.TRUE);
    }



    public String sendALiyunSms(SendALiYunSmsParam sendALiYunSmsParam){
        if (BeanUtil.hasNullField(sendALiYunSmsParam)){
            logger.info("sendALiyunSms 重要参数为空！[{}]", JSONObject.toJSONString(sendALiYunSmsParam));
        }
        String phone = sendALiYunSmsParam.getPhone();
        String logPrefix = sendALiYunSmsParam.getLogPrefix();
        HttpServletRequest request = sendALiYunSmsParam.getRequest();
        SendSmsEnum sendSmsEnum = sendALiYunSmsParam.getSendSmsEnum();
        AliYunSendSmsParam param = new AliYunSendSmsParam();
        param.setPhone(phone);
        param.setSignName("橘子电竞");
        param.setContentType(sendSmsEnum.getShortMessageContentType());
        param.setTemplateCode(sendSmsEnum.getSmsTemplateCodeEnum().getCode());
        Map<String, String> templateParam = new HashMap<String, String>();
        String code = String.valueOf((new Random().nextInt(8999) + 1000));
        logger.info(logPrefix + "获得验证码，phone={}，code={}", phone, code);
        templateParam.put("code", code);
        param.setTemplateParam(templateParam);
        Integer flag = toSendSms(request, param, logPrefix);
        if (flag==ToSendSmsExceptionType.ALIYUN_SEND.getIndex()){
            throw new RuntimeException("调用阿里云短信发送接口返回错误");
        }else if (flag==ToSendSmsExceptionType.SMS_INFO_INSERT_DB.getIndex()){
            throw new RuntimeException("验证码入库错误");
        }
        return code;
    }

    /**
     * 检查这个手机号是否不存在db中
     * @param phone
     * @param prefix 日志前缀
     * @return
     */
    private Boolean checkPhoneValid(String phone,String prefix){
        UserConsumerQueryVo userConsumerQueryVo = new UserConsumerQueryVo();
        userConsumerQueryVo.setPhone(phone);
        ModelResult<List<UserConsumer>> listModelResult = userConsumerServiceClient.queryList(userConsumerQueryVo);
        List<UserConsumer> model = listModelResult.getModel();
        if (!listModelResult.isSuccess()|| null==model){
            logger.info(prefix+"根据手机号[{}]查询信息异常[{}]", phone,listModelResult.getErrorMsg());
            throw new RuntimeException("服务器异常！");
        }
        if (CollectionUtil.isNotEmpty(model)){
            /*new RuntimeException("该手机号已被绑定！");*/
            return false;
        }
        return true;
    }

    /**
     * 检查用户发送短信的限制
     * @param phone
     * @param request
     * @param logPrefix 日志前缀
     */
    public Map<String,String> checkUserIpSendSms(String phone, HttpServletRequest request, HttpServletResponse response, String logPrefix){
        //检查手机是否符合规范
        RegisterValidUtil.validPhone(phone);
        //短信发送限制
        String ip = RequestUtil.getClientIp(request);
        String ua = request.getHeader("User-Agent");
        logger.info(logPrefix + "客户端ip={},user-agent={}", ip, ua);
        if (getValidCodeCount(ip, ua) >= 5) {// 3分钟5次
            logger.info(logPrefix + "ipua统计值为={},超过3分钟5次，phone={}", getValidCodeCount(ip, ua), phone);
            throw new RuntimeException(MemberConstants.REPETITION_GET_CODE);
        }
        if (getValidCodeCount(ip) >= 10) {// 5分钟10次
            logger.info(logPrefix + "ipua统计值为={},超过3分钟5次，phone={}", getValidCodeCount(ip, ua), phone);
            throw new RuntimeException(MemberConstants.REPETITION_GET_CODE);
        }

        if (getValidCodeCookieCount(request, response) > 5) {
            logger.info(logPrefix + "cookie统计值超过5次，phone={}", phone);
            throw new RuntimeException(MemberConstants.REPETITION_GET_CODE);
        }
        HashMap<String, String> m = new HashMap<>();
        m.put("ip", ip);
        m.put("ua", ua);
        return m;
    }

    public int getValidCodeCount(String ip, String ua) {
        String cacheKey = VALID_CODE_COUNT_IP_UA + ip + "_" + ua;
        if (!redisClientManager.exists(cacheKey)) {
            return 0;
        }
        String countVal = redisClientManager.get(cacheKey);
        logger.info("ip ua统计,ip={}, ua={}, countVal={}", ip, ua, countVal);
        return Integer.parseInt(countVal);
    }

    public int getValidCodeCount(String ip) {
        String cacheKey = VALID_CODE_COUNT_IP + ip;
        if (!redisClientManager.exists(cacheKey)) {
            return 0;
        }
        String countVal = redisClientManager.get(cacheKey);
        logger.info("ip统计,ip={},countVal={}", ip, countVal);
        return Integer.parseInt(countVal);
    }

    public void increaseValidCodeCount(String ip, String ua) {
        int count = 1;
        long expireTime = 3 * 60 * 60;
        String cacheKey = VALID_CODE_COUNT_IP_UA + ip + "_" + ua;
        if (redisClientManager.exists(cacheKey)) {
            count = Integer.parseInt(redisClientManager.get(cacheKey)) + 1;
            expireTime = redisClientManager.getExpire(cacheKey);
        }
        logger.info("ip ua增长，ip={},ua={},count={},expireTime={}", ip, ua, count, expireTime);
		redisClientManager.setEx(cacheKey, expireTime, count + "");
//        redisClientManager.setEx(cacheKey, count + "", expireTime, TimeUnit.MILLISECONDS);
    }

    public void increaseValidCodeCount(String ip) {
        int count = 1;
        long expireTime = 5 * 60 * 60;
        String cacheKey = VALID_CODE_COUNT_IP + ip;
        if (redisClientManager.exists(cacheKey)) {
            count = Integer.parseInt(redisClientManager.get(cacheKey)) + 1;
            expireTime = redisClientManager.getExpire(cacheKey);
        }
        logger.info("ip增长，ip={},count={},expireTime={}", ip, count, expireTime);
		redisClientManager.setEx(cacheKey, expireTime, count + "");
//        redisClientManager.setEx(cacheKey, count + "", expireTime, TimeUnit.MILLISECONDS);
    }

    public int getValidCodeCookieCount(HttpServletRequest request, HttpServletResponse response) {
        int count = 0;
        String countStr = CookieUtils.getCookieValue(request, VALID_CODE_COUNT_COOKIE_KEY);
        if (StringUtils.isNotBlank(countStr)) {
            count = Integer.parseInt(countStr);
        }
        logger.info("验证码cookie统计，count={}", count);
        return count;
    }

    public void increaseValidCodeCookieCount(HttpServletRequest request, HttpServletResponse response) {
        int count = 1;
        String countStr = CookieUtils.getCookieValue(request, VALID_CODE_COUNT_COOKIE_KEY);
        if (StringUtils.isNotBlank(countStr)) {
            count = Integer.parseInt(countStr) + 1;
        }
        CookieUtils.setCookie(request, response, VALID_CODE_COUNT_COOKIE_KEY, String.valueOf(count), 60 * 60);
        logger.info("验证码cookie统计加值，count={}", count);
    }

    public static String getValidationCode() {
        return String.valueOf((new Random().nextInt(8999) + 1000));
    }

    //daily不发送
    public Integer toSendSms(HttpServletRequest request, AliYunSendSmsParam param, String logPrefix) {
        //获取请求的url
        StringBuffer url = request.getRequestURL();
        String contextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
        logger.info("获取到的contextUrl：{}",contextUrl);
        if (!contextUrl.contains("daily-api.esportzoo.com")) {
            ModelResult<Boolean> modelResult1 = aliYunShortMessageService.sendSms(param);
            if (UserConsumerErrorTable.SMS_SEND_PHONE_LIMIT.code.equals(modelResult1.getErrorCode())){
                logger.info(logPrefix + "调用阿里云短信发送接口次数超限，phone={}", param.getPhone());
                return ToSendSmsExceptionType.SMS_SEND_PHONE_LIMIT.getIndex();
            }
            if (!modelResult1.isSuccess()) {
                logger.info(logPrefix + "调用阿里云短信发送接口返回错误，phone={}", param.getPhone());
                return ToSendSmsExceptionType.ALIYUN_SEND.getIndex();
            }
            if (!modelResult1.getModel()) {
                logger.info(logPrefix + "调用阿里云短信发送接口返回错误1，phone={}", param.getPhone());
                return ToSendSmsExceptionType.ALIYUN_SEND.getIndex();
            }
        } else {
            SmsLog smsLog = new SmsLog();
            smsLog.setProvider(ShortMessageServiceProvider.ALIYUN.getIndex());
            smsLog.setPhone(param.getPhone());
            smsLog.setContentType(param.getContentType().getIndex());
            smsLog.setTemplateCode(param.getTemplateCode());
            String str1 = JSON.toJSONString(param.getTemplateParam());
            smsLog.setTempalteParam(str1.length() > 200 ? str1.substring(0, 200) : str1);
            smsLog.setCreateTime(Calendar.getInstance());
            smsLog.setUpdateTime(Calendar.getInstance());
            smsLog.setStatus(SmsLogStatus.SUCCESS.getIndex());
            ModelResult<Long> save = smsLogServiceClient.save(smsLog);
            if (!save.isSuccess()) {
                return ToSendSmsExceptionType.SMS_INFO_INSERT_DB.getIndex();
            }
        }
        return ToSendSmsExceptionType.SUCCESSFUL.getIndex();
    }
}
