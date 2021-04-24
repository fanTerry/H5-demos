package com.esportzoo.esport.manager;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.redisclient.core.JedisClusterClientImpl;
import com.esportzoo.common.util.CookieUtils;
import com.esportzoo.esport.client.service.common.ClientChannelInviteCodeServiceClient;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.client.service.consumer.UserThirdLoginServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.constant.CachedKeyAndTimeLong;
import com.esportzoo.esport.constants.*;
import com.esportzoo.esport.constants.sms.SendSmsEnum;
import com.esportzoo.esport.constants.user.MemberConstants;
import com.esportzoo.esport.constants.user.SexType;
import com.esportzoo.esport.domain.RegisterBindingParam;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.UserThirdLogin;
import com.esportzoo.esport.domain.operate.ClientChannelInviteCode;
import com.esportzoo.esport.exception.CommonExceptionCode;
import com.esportzoo.esport.manager.qqconnect.QqLoginManager;
import com.esportzoo.esport.manager.sms.SmsManager;
import com.esportzoo.esport.service.exception.BusinessException;
import com.esportzoo.esport.service.exception.errorcode.UserConsumerErrorTable;
import com.esportzoo.esport.util.RegisterValidUtil;
import com.esportzoo.esport.util.RequestUtil;
import com.esportzoo.esport.vo.ThirdLoginVo;
import com.esportzoo.esport.vo.operate.ClientChannelInviteCodeQueryVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author jiajing.he
 * @date 2020/2/25 15:26
 */
@Component
public class LoginManager {

    @Autowired
    private SmsManager smsManager;
    @Autowired
    private JedisClusterClientImpl redisClient;
    @Autowired
    @Qualifier("cachedManager")
    private CachedManager cachedManager;
    @Autowired
    private UserConsumerServiceClient userConsumerServiceClient;
    @Autowired
    private UserThirdLoginServiceClient userThirdLoginServiceClient;
    @Autowired
    @Qualifier("clientChannelInviteCodeServiceClient")
    private ClientChannelInviteCodeServiceClient clientChannelInviteCodeService;

    public static final String CACHE_USER_INFO_KEY="cache_qquser_info_";

    /**
     * 限制并发请求注册
     */
    private final String RESTRICT_REGISTER_KEY="esport_interface_restrict_register_";

    private final SendSmsEnum sendSmsEnum=SendSmsEnum.BINDING_REGISTER_CODE;

    private Logger logger= LoggerFactory.getLogger(LoginManager.class);

    /*目前没支持微信*/
    public CommonResponse<UserConsumer> registerBinding(RegisterBindingParam param){
        if (BeanUtil.hasNullField(param)){
            throw new BusinessException(CommonExceptionCode.PARAM_IS_NULL);
        }
        String cacheKey = param.getCacheKey();
        String code = param.getCode();
        String phone = param.getPhone();
        Integer thirdTypeIndex = param.getThirdType();
        HttpServletRequest request = param.getRequest();
        String restrictKey=RESTRICT_REGISTER_KEY+ phone;
        if (!redisClient.setNX(restrictKey,"1",5)){
            throw new BusinessException(CommonExceptionCode.RESTRICT_EXCEPTION);
        }
        ThirdType thirdType = ThirdType.valueOf(thirdTypeIndex);
        if (null==thirdType){
            throw new BusinessException(CommonExceptionCode.THIRD_TYPE_EXCEPTION);
        }
        Boolean checkCode = smsManager.checkCode(phone, code, sendSmsEnum);
        if (!checkCode){
            return CommonResponse.withErrorResp("验证码错误!");
        }
        //第三方登录获取到的用户信息
        UserConsumer thirdUser= redisClient.getObj(cacheKey);
        if (null==thirdUser){
            throw new BusinessException(CommonExceptionCode.INVALID_CACHE_KEY_EXCEPTION);
        }
        //step 1:检查手机号是否存在db
        String openid = thirdUser.getFeature(QqLoginManager.QQ_USER_OPENID_FEATURE_KEY);
        if (StrUtil.isBlank(openid)){
            throw new BusinessException(CommonExceptionCode.THIRDID_IS_NULL_EXCEPTION);
        }
        Boolean checkPhoneIsUsable = smsManager.checkPhoneIsUsable(phone, sendSmsEnum.getLogPrefix());
        if (checkPhoneIsUsable){
            //不存在  注册绑定第三方登录记录
            thirdUser.setPhone(phone);
            UserOperationParam userOperationParam = new UserOperationParam(RequestUtil.getClientIp(request), ClientType.QQ.getIndex(), param.getAgentId(), param.getVersion());
            ThirdLoginVo thirdLoginVo = new ThirdLoginVo(openid,thirdType.getIndex());
            ModelResult<UserConsumer> userConsumerModelResult = userConsumerServiceClient.registerBindingThirdLogin(thirdUser, userOperationParam, thirdLoginVo);
            UserConsumer model = userConsumerModelResult.getModel();
            if (!userConsumerModelResult.isSuccess() || null==model){
                throw new BusinessException(CommonExceptionCode.RPC_EXCEPTION);
            }
            smsManager.deleteCacheCode(phone, sendSmsEnum);
            return CommonResponse.withSuccessResp(model);
        }

        //查询是否存在对应的第三方记录
        UserConsumer user = getUserByPhone(phone);
        List<UserThirdLogin> thirdList = getThirdListByUserId(user.getId(), thirdType.getIndex());
        if (CollectionUtil.isNotEmpty(thirdList)){
            //存在 不能再进行绑定
            smsManager.deleteCacheCode(phone, sendSmsEnum);
            redisClient.del(restrictKey);
            return CommonResponse.withErrorResp("该手机号已被绑定!");
        }else {
            //绑定第三方登录信息
            ThirdLoginVo thirdLoginVo = new ThirdLoginVo(openid,thirdType.getIndex(),user.getAccount());
            ModelResult<UserThirdLogin> insertUserThirdLogin = userConsumerServiceClient.insertUserThirdLogin(thirdLoginVo, user.getId());
            if (!insertUserThirdLogin.isSuccess()){
                throw new BusinessException(CommonExceptionCode.RPC_EXCEPTION);
            }
            smsManager.deleteCacheCode(phone, sendSmsEnum);
            return CommonResponse.withSuccessResp(user);
        }
    }



    private UserConsumer getUserByPhone(String phone){
        ModelResult<UserConsumer> userConsumerModel = userConsumerServiceClient.queryConsumerByPhone(phone);
        UserConsumer userConsumer = userConsumerModel.getModel();
        if (!userConsumerModel.isSuccess() || null==userConsumer){
            throw new BusinessException(CommonExceptionCode.RPC_EXCEPTION);
        }
        return userConsumer;
    }

    private List<UserThirdLogin> getThirdListByUserId(Long userId,Integer thirdType){
        ModelResult<List<UserThirdLogin>> thirdModelResult = userThirdLoginServiceClient.queryByConsumerIdAndTypeForList(userId, thirdType);
        List<UserThirdLogin> thirdLogins = thirdModelResult.getModel();
        if (!thirdModelResult.isSuccess()) {
            throw new BusinessException(CommonExceptionCode.RPC_EXCEPTION);
        }
        return thirdLogins;
    }


    /**
     * @param request
     * @param response
     * @param userConsumer
     * @param cookieSid  {@link MemberConstants} 客户端cookie Key
     */
    public void loginAndUpdateLastLoginTime(HttpServletRequest request, HttpServletResponse response,UserConsumer userConsumer,String cookieSid){
       if (userConsumer.getStatus().intValue()==UserConsumerStatus.VALID.getIndex()){
           String sid = UUID.randomUUID().toString();// uuid生成唯一key
           CookieUtils.setCookie(request, response, cookieSid, sid,
                   CachedKeyAndTimeLong.MEMBER_MEMCACHE_EXP);
           cachedManager.cachedMemberSession(userConsumer, sid);
           userConsumerServiceClient.updateUserConsumerLastLoginTime(userConsumer);
           logger.info("cookieSid:[{}],更新用户最新登陆时间，userId={},ninkName={}", cookieSid,userConsumer.getId(),
                   userConsumer.getNickName());
           return;
       }
       logger.info("用户[{}]状态无效,禁止登录!", userConsumer.getId());
    }


    public UserConsumer loginRegister(HttpServletRequest request, BaseRequest baseResult, String phone){
        String prefix="手机号登录注册用户_";
        //检查手机是否符合规范
        RegisterValidUtil.validPhone(phone);
        logger.info(prefix+"所带参数[phone:{},baserequest:[{}]]",phone, JSON.toJSONString(baseResult));
        if (smsManager.checkPhoneIsUsable(phone, prefix)){
            UserConsumer userConsumer = userInfoPackageRegister(request, baseResult, phone);
            return userConsumer;
        }
        logger.info(prefix+"该手机号[{}]已经被注册!",phone);
        return null;
    }


    /**
     * 判断当前邀请码是否有效
     *
     * @param baseRequest
     * @return
     */
    public Boolean judgeInviteCode(BaseRequest baseRequest) {
        Boolean resFlag = false;
        try {
            if (StringUtils.isBlank(baseRequest.getInviteCode())) {
                return resFlag;
            }
            ClientChannelInviteCodeQueryVo queryVo = new ClientChannelInviteCodeQueryVo();
            queryVo.setInviteCode(baseRequest.getInviteCode());
            queryVo.setChannelNo(baseRequest.getAgentId());
            queryVo.setStatus(CommonStatus.EFFECTIVE.getIndex());
            ModelResult<List<ClientChannelInviteCode>> modelResult = clientChannelInviteCodeService.queryChannelInviteCode(queryVo);
            if (modelResult.isSuccess() && modelResult.getModel() != null && modelResult.getModel().size() == 1) {
                resFlag = true;
            } else {
                logger.info("judgeInviteCode判断邀请码是否有效,邀请码记录【为空或者有多条】,baseRequest:[{}],modelResult:[{}]", JSON.toJSONString(baseRequest),JSON.toJSONString(modelResult));
            }
            return resFlag;
        } catch (Exception e) {
            logger.info("judgeInviteCode判断邀请码是否有效发生异常 baseRequest[{}] [{}]", JSON.toJSONString(baseRequest), e.getMessage(), e);
            return resFlag;
        }
    }

    private UserConsumer userInfoPackageRegister(HttpServletRequest request, BaseRequest baseResult, String phone){
        UserConsumer userConsumer = new UserConsumer();
        userConsumer.setStatus(UserConsumerStatus.VALID.getIndex());
        userConsumer.setChannelNo(baseResult.getAgentId());
        userConsumer.setBizSystem(baseResult.getBiz());
        userConsumer.setClientType(baseResult.getClientType());
        userConsumer.setClientVersion(baseResult.getVersion());
        userConsumer.setRegisterType(RegisterType.phone_register.getIndex());
        userConsumer.setGender(SexType.UNKNOW.getIndex());
        userConsumer.setLastGuessTime(null);
        userConsumer.setUserType(UserType.GENERAL_USR.getIndex());
        userConsumer.setVipLevel(UserVipLevel.VIP_GENERAL.getIndex());
        userConsumer.setFollowers(0);
        userConsumer.setFans(0);
        userConsumer.setIssues(0);
        userConsumer.setTopics(0);
        userConsumer.setFavorites(0);
        userConsumer.setInviteCode(baseResult.getInviteCode());
        userConsumer.setViews(0);
        userConsumer.setPhone(phone);
        userConsumer.setRegisterTime(new Date());
        userConsumer.setRegisterEndTime(new Date());
        userConsumer.setLastLoginTime(new Date());
        userConsumer.setLastLoginEndTime(new Date());
        userConsumer.setWalletRec(new BigDecimal("0"));
        userConsumer.setCreateTime(Calendar.getInstance());
        userConsumer.setUpdateTime(Calendar.getInstance());

        UserOperationParam userOperationParam = new UserOperationParam();
        //ip地址
        userOperationParam.setOperIp(RequestUtil.getClientIp(request));
        userOperationParam.setChannelNo(baseResult.getAgentId());
        userOperationParam.setBizSystem(baseResult.getBiz());
        userOperationParam.setClientType(baseResult.getClientType());
        logger.info("注册用户所带参数[userconsumer[{}]]",JSON.toJSONString(userConsumer));
        ModelResult<UserConsumer> register = userConsumerServiceClient.register(userConsumer, userOperationParam);
        UserConsumer model = register.getModel();
        if (!register.isSuccess() || null==model){
            new BusinessException(UserConsumerErrorTable.REGISTER_EXCEPTION);
        }
        return model;
    }

}
