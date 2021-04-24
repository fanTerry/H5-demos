package com.esportzoo.esport.controller.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.common.redisclient.core.JedisClusterClientImpl;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.common.util.StringUtil;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.client.service.expert.RecExpertApplyServiceClient;
import com.esportzoo.esport.client.service.expert.RecExpertServiceClient;
import com.esportzoo.esport.client.service.quiz.QuizUserScoreServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.UserInfoRequest;
import com.esportzoo.esport.connect.request.sms.SendALiYunSmsParam;
import com.esportzoo.esport.connect.request.user.FollowedUserRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.UserIDCard;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.constants.SysConfigPropertyKey;
import com.esportzoo.esport.constants.sms.SendSmsEnum;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.controller.ws.server.UsrConsumerCacheManager;
import com.esportzoo.esport.domain.RecExpert;
import com.esportzoo.esport.domain.SysConfigProperty;
import com.esportzoo.esport.domain.UserCenterInfoVo;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.quiz.QuizUserScore;
import com.esportzoo.esport.exception.CommonExceptionCode;
import com.esportzoo.esport.manager.LoginManager;
import com.esportzoo.esport.manager.sms.SmsManager;
import com.esportzoo.esport.service.exception.BusinessException;
import com.esportzoo.esport.util.HttpClientUtils;
import com.esportzoo.esport.util.RegisterValidUtil;
import com.esportzoo.esport.vo.MemberSession;
import com.esportzoo.esport.vo.UserConsumerQueryOption;
import com.esportzoo.esport.vo.cms.FollowUserParam;
import com.esportzoo.esport.vo.cms.FollowedUserPageQueryParam;
import com.esportzoo.esport.vo.cms.FollowedUserPageQueryResult;
import com.esportzoo.esport.vo.cms.FollowedUserVo;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("user")
@Api(value = "用户相关接口", tags={"用户相关接口"})
public class UserController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserConsumerServiceClient userConsumerServiceClient;
	@Autowired
	private RecExpertServiceClient recExpertServiceClient;
	@Autowired
	RecExpertApplyServiceClient recExpertApplyServiceClient;
	@Autowired
	private JedisClusterClientImpl redisClientManager;
	@Autowired
	private LoginManager loginManager;
	@Autowired
	UsrConsumerCacheManager usrConsumerCacheManager;
	@Autowired
	QuizUserScoreServiceClient quizUserScoreServiceClient;

	@Autowired
	private RedisClient redisClient;
	@Autowired
	private SmsManager smsManager;
	@Value("${avatarImage.upload.path}")
	private String uploadPath;

	@Value("${avatarImage.res.host}")
	private String resPath;

	@Value("${idCard.appcode}")
	private String appcode;

	@Value("${idCard.host}")
	private String host;
	@Value("${idCard.path}")
	private String path;
	@Value("${idCard.method}")
	private String method;
	@Value("${environ}")
	public String currentEnviron;

	public static final String logPrefix = "用户相关接口-";

	//实名认证每天的次数
	public static final Integer limit=3;

	private static final String VERIFY_LIMIT = "day_verify_limit_";

	@RequestMapping(value = "/follow", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "关注或取消关注接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "关注或取消关注接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<Boolean> followOrCancel(@ApiParam(required = true, value = "1=关注 0=取消关注") int type, @ApiParam(required = true, value = "用户id") Long usrId, HttpServletRequest request) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("关注或取消关注接口,未获取到登录用户信息，type={}，usrId={}", type, usrId);
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			FollowUserParam param = new FollowUserParam();
			param.setUserId(userConsumer.getId());
			param.setType(type);
			param.setFollowUserId(usrId);
			ModelResult<Boolean> modelResult = userConsumerServiceClient.followUser(param);
			if (!modelResult.isSuccess()) {
				logger.info("关注或取消关注接口，调用接口返回错误，type={}，usrId={}, errMsg={}", type, usrId, modelResult.getErrorMsg());
				return CommonResponse.withErrorResp(modelResult.getErrorMsg());
			}
			return CommonResponse.withSuccessResp(modelResult.getModel());
		} catch (Exception e) {
			logger.info("关注或取消关注接口，发生异常，type={}，usrId={}, exception={}", type, usrId, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	@RequestMapping(value = "/pageFollowedUser", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "分页查询关注的用户接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "分页查询关注的用户接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<FollowedUserPageQueryResult> pageFollowedUser(FollowedUserRequest followedUserRequest, HttpServletRequest request) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("分页查询关注的用户接口,未获取到登录用户信息,");
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			FollowedUserPageQueryParam param = new FollowedUserPageQueryParam();
			param.setUserId(userConsumer.getId());
			param.setPageNo(followedUserRequest.getPageNo());
			param.setPageSize(followedUserRequest.getPageSize());
			param.setFollowType(followedUserRequest.getFollowType());
			ModelResult<FollowedUserPageQueryResult> modelResult = userConsumerServiceClient.pageQueryFollowedUser(param);
			if (!modelResult.isSuccess()) {
				logger.info("分页查询关注的用户接口,调用接口返回错误,errorMsg={}", modelResult.getErrorMsg());
				return CommonResponse.withErrorResp("调用接口返回错误,errorMsg=" + modelResult.getErrorMsg());
			}
			FollowedUserPageQueryResult model = modelResult.getModel();
			List<FollowedUserVo> followedUserList = model.getFollowedUserList();


			if (CollUtil.isEmpty( followedUserList )) {
				followedUserList = Lists.newArrayList();
			}

			for (FollowedUserVo followedUserVo : followedUserList) {
				ModelResult<RecExpert> recExpertModelResult = recExpertServiceClient.queryByUserId( followedUserVo.getUserId() );
				//专家用户，使用专家名替换用户名
				if (recExpertModelResult != null && recExpertModelResult.isSuccess() && recExpertModelResult.getModel() != null) {
					RecExpert recExpert = recExpertModelResult.getModel();
					followedUserVo.setUserNickName( recExpert.getNickName() );
				}
				//关联大神跟单战绩
				ModelResult<QuizUserScore> userScoreModelResult = quizUserScoreServiceClient.queryByUserId( followedUserVo.getUserId() );
				if (userScoreModelResult != null && userScoreModelResult.isSuccess() && userScoreModelResult.getModel() != null) {
					QuizUserScore quizUserScore = userScoreModelResult.getModel();
					//胜场数,有10场战绩才显示
					if (StrUtil.isNotBlank( quizUserScore.getTenScore() ) && quizUserScore.getTenScore().length()>=10) {
						int winNum = StringUtil.appearNumber( quizUserScore.getTenScore(), "1" );
						followedUserVo.setTenWinNum( "近10中" + winNum );
					}
					if (quizUserScore.getTenReturnRate() != null) {
						followedUserVo.setTenWinProfit( quizUserScore.getTenReturnRate().stripTrailingZeros().toPlainString() + "%" );
					}
					//默认为0，大神填写 1
					followedUserVo.setUserType( 1 );
				}
			}


			model.setFollowedUserList(followedUserList);
			return CommonResponse.withSuccessResp(model);
		} catch (Exception e) {
			logger.info("分页查询关注的用户接口,发生异常,exception={}", e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	@RequestMapping(value = "/allFollowedUser", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "查询全部的关注用户接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "查询全部的关注用户接口", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<FollowedUserVo>> allFollowedUser(HttpServletRequest request) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info("查询全部的关注用户接口,未获取到登录用户信息,");
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			ModelResult<List<FollowedUserVo>> modelResult = userConsumerServiceClient.queryAllFollowedUser(userConsumer.getId());
			if (!modelResult.isSuccess()) {
				logger.info("查询全部的关注用户接口,调用接口返回错误,errorMsg={}", modelResult.getErrorMsg());
				return CommonResponse.withErrorResp("调用接口返回错误,errorMsg=" + modelResult.getErrorMsg());
			}
			return CommonResponse.withSuccessResp(modelResult.getModel());
		} catch (Exception e) {
			logger.info("查询全部的关注用户接口,发生异常,exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	@RequestMapping(value = "/updateUserInfo", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "修改用户个人信息", httpMethod = "POST", consumes = "multipart/form-data", produces = "application/json")
	@ApiResponse(code = 200, message = "修改用户个人信息", response = CommonResponse.class)
	@ResponseBody
    public CommonResponse<UserCenterInfoVo> updateUserInfo(HttpServletRequest request, MultipartFile[] files, UserInfoRequest userInfoRequest) {
        try {
            UserConsumer userConsumer = getLoginUsr(request);
            if (userConsumer == null) {
                logger.info("查询全部的关注用户接口,未获取到登录用户信息,");
                return CommonResponse.withErrorResp("未获取到登录用户信息");
            }
            if (userInfoRequest == null) {
                return CommonResponse.withErrorResp("参数为空");
            }
            if (files != null && files.length > 0) {
                if (files[0].getSize() > 5242880) {
                    return CommonResponse.withErrorResp("修改失败，图片不能超过5M");
                }
                String uploadImage = uploadImage(files, userConsumer.getId());
                if (StringUtils.isEmpty(uploadImage)) {
                    return CommonResponse.withErrorResp("头像修改失败");
                }
                userConsumer.setIcon(uploadImage);
                //修改头像
                ModelResult<Boolean> updateIconResult = userConsumerServiceClient.updateConsumerIcon(userConsumer.getId(), uploadImage);
                if (updateIconResult.isSuccess() && updateIconResult.getModel()) {
                    UserCenterInfoVo infoVo = updateSuccess(request, userInfoRequest, userConsumer);
                    return CommonResponse.withSuccessResp(infoVo);
                }
                //修改昵称
            } else if (StringUtils.isNotBlank(userInfoRequest.getNickName())) {
                if (!userInfoRequest.getNickName().equals(userConsumer.getNickName())) {
                    if (4 > userInfoRequest.getNickName().length() || 14 < userInfoRequest.getNickName().length()) {
                        return CommonResponse.withErrorResp("昵称长度不合格");
                    }
                    if (!ReUtil.isMatch("^[\\u4e00-\\u9fa5A-Za-z0-9-_]*$", userInfoRequest.getNickName())) {
                        return CommonResponse.withErrorResp("昵称不合格");
                    }
                    ModelResult<Boolean> updateNicknameResult = userConsumerServiceClient.updateConsumerNickname(userConsumer.getId(), userInfoRequest.getNickName());
                    if (updateNicknameResult.isSuccess() && updateNicknameResult.getModel()) {
                        UserCenterInfoVo infoVo = updateSuccess(request, userInfoRequest, userConsumer);
                        return CommonResponse.withSuccessResp(infoVo);
                    }
                } else {
                    return CommonResponse.withErrorResp("昵称与修改前一致");
                }
                //修改简介
            } else if (StringUtils.isNotBlank(userInfoRequest.getIntro())) {
                ModelResult<Boolean> updateIntroResult = userConsumerServiceClient.updateConsumerIntro(userConsumer.getId(), userInfoRequest.getIntro());
                if (updateIntroResult.isSuccess() && updateIntroResult.getModel()) {
                    UserCenterInfoVo infoVo = updateSuccess(request, userInfoRequest, userConsumer);
                    return CommonResponse.withSuccessResp(infoVo);
                }
            }
        } catch (Exception e) {
            logger.info("用户信息修改失败,发生异常,exception={}", e.getMessage(), e);
            return CommonResponse.withErrorResp(e.getMessage());
        }
        return CommonResponse.withErrorResp("用户信息修改失败");
    }

    private UserCenterInfoVo updateSuccess(HttpServletRequest request, UserInfoRequest userInfoRequest, UserConsumer userConsumer) {
        UserCenterInfoVo infoVo = new UserCenterInfoVo();
        /** 更新成功后，刷新session缓存的用户信息 */
        MemberSession memberSession = updateMemberSession(request);
        if (memberSession == null) {
            logger.warn("修改用户【{}】信息,更新用户session信息失败", userConsumer.getId());
            BeanUtil.copyProperties(userInfoRequest, userConsumer, CopyOptions.create().setIgnoreNullValue(true));
            BeanUtils.copyProperties(userConsumer, infoVo);
        } else {
            UserConsumer member = memberSession.getMember();
            BeanUtils.copyProperties(member, infoVo);
        }
        return infoVo;
    }


	@RequestMapping(value = "/checkBindingPhone", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "检查用户是否绑定手机", httpMethod = "POST", consumes = "multipart/form-data", produces = "application/json")
	@ApiResponse(code = 200, message = "检查用户是否绑定手机", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse checkBindingPhone(HttpServletRequest request) {
		UserConsumer loginUsr = getLoginUsr(request);
		if (null == loginUsr) {
			return CommonResponse.withSuccessResp("请先登录！");
		}
		Long id = loginUsr.getId();
		ModelResult<UserConsumer> userConsumerModelResult = userConsumerServiceClient.queryConsumerById(id, null);
		UserConsumer model = userConsumerModelResult.getModel();
		if (!userConsumerModelResult.isSuccess() || null == model) {
			logger.info("查询用户是否绑定手机,errorMsg={}", userConsumerModelResult.getErrorMsg());
			return CommonResponse.withErrorResp("调用接口返回错误,errorMsg=" + userConsumerModelResult.getErrorMsg());
		}
		if (StrUtil.isNotBlank(model.getPhone())) {
			return CommonResponse.withSuccessResp(Boolean.TRUE);
		}
		return CommonResponse.withSuccessResp(Boolean.FALSE);
	}


	//发送手机验证码
	@RequestMapping(value = "/sendPhoneCode", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse sendPhoneCode(String phone, Integer smsType, HttpServletRequest request, HttpServletResponse response, BaseRequest baseRequest) {
		SendSmsEnum sendSmsEnum = SendSmsEnum.getSendSmsEnumByType(smsType);
		String logPrefix = phone + "发送手机验证码,";
		if (null == sendSmsEnum) {
			return CommonResponse.withErrorResp("错误的短信类型！");
		}
		try {
			logPrefix += sendSmsEnum.getLogPrefix();
			if (smsType.intValue() == SendSmsEnum.LOG_IN_CODE.getType().intValue()) {//如果是手机号登录(包含了注册)
				CommonResponse judgeResponse = judgeUserCanRegist(phone, baseRequest);
				if (!judgeResponse.getCode().equals(ResponseConstant.RESP_SUCC_CODE)) {
					logger.info(logPrefix + "判断新用户是否可以注册,【校验未通过】，phone={},baseRequest={}", phone, JSON.toJSONString(baseRequest));
					return judgeResponse;
				}
			}
			UserConsumer userConsumer = getLoginUsr(request);
			if (smsType.intValue() == SendSmsEnum.REAL_NAME_AUTHENTICATION.getType().intValue()) {//如果是实名认证
				logger.info(logPrefix + "用户实名认证获取验证码(必须使用注册时手机号)，phone={},baseRequest={},缓存中用户手机号:{}", phone, JSON.toJSONString(baseRequest), userConsumer.getPhone());
				if (!phone.equals(userConsumer.getPhone())) {
					return CommonResponse.withErrorResp("请使用注册时的手机号获取验证码！");
				}
			}
			Map<String, String> m = smsManager.checkUserIpSendSms(phone, request, response, logPrefix);
			CommonResponse commonResponse = smsManager.checkSmsCondition(sendSmsEnum, userConsumer, phone);
			if (commonResponse.getCode().equals(ResponseConstant.RESP_ERROR_CODE)) {
				return commonResponse;
			}
			String cacheKey = StrUtil.format(sendSmsEnum.getCacheKey(), phone);
			if (redisClientManager.exists(cacheKey)) {
				//重复获取短信验证码
				logger.info(logPrefix + "5分钟之内请勿重复获取验证码，phone={}", phone);
				return CommonResponse.withErrorResp("请勿频繁获取验证码");
			}
			smsManager.increaseValidCodeCount(m.get("ip"), m.get("ua"));
			smsManager.increaseValidCodeCount(m.get("ip"));
			smsManager.increaseValidCodeCookieCount(request, response);
			SendALiYunSmsParam sendALiYunSmsParam = new SendALiYunSmsParam();
			sendALiYunSmsParam.setPhone(phone);
			sendALiYunSmsParam.setLogPrefix(logPrefix);
			sendALiYunSmsParam.setSendSmsEnum(sendSmsEnum);
			String code = String.valueOf((new Random().nextInt(8999) + 1000));
			logger.info(logPrefix + "获得验证码，phone={}，code={}", phone, code);
			sendALiYunSmsParam.setCode(code);
			sendALiYunSmsParam.setCacheCodeKey(cacheKey);
			CommonResponse sendRes = smsManager.sendALiyunSms(sendALiYunSmsParam, request);
			return sendRes;
		} catch (RuntimeException e) {
			//logger.info(logPrefix + "发生异常exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		} catch (Exception e) {
			logger.info(logPrefix + "发生异常exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	private String uploadImage(MultipartFile[] files, Long userId) {
		String imageUrl = "";
		try {
			for (int i = 0; i < files.length; i++) {
				String fileName = files[i].getOriginalFilename();
				if (StringUtils.isNotBlank(fileName)) {
					//创建输出文件对象
					String suffix = "";
					if (fileName.contains(".jpeg")) {
						suffix = fileName.substring(fileName.length() - 5, fileName.length());
					} else {
						suffix = fileName.substring(fileName.length() - 4, fileName.length());
					}

					File outFile = FileUtils.getFile(
							uploadPath + File.separator + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10) + "_" + userId + suffix);
					//拷贝文件到输出文件对象
					FileUtils.copyInputStreamToFile(files[0].getInputStream(), outFile);
					imageUrl = outFile.getPath().replace(uploadPath, resPath);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("上传个人头像图片出错", e.getMessage());
		}
		return imageUrl;


	}


	@RequestMapping(value = "/realNameCheck", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "实名认证用户个人信息", httpMethod = "POST", consumes = "multipart/form-data", produces = "application/json")
	@ApiResponse(code = 200, message = "实名认证用户个人信息", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<UserCenterInfoVo> realNameCheck(HttpServletRequest request, UserInfoRequest userInfoRequest, String code) {
		String prefix = "用户提交实名认证信息_";
		try {
			logger.info(prefix + "基础参数:{},验证码={}", JSON.toJSONString(userInfoRequest), code);
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				logger.info(prefix + "未获取到登录用户信息");
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}
			String phone = userInfoRequest.getPhone(),certNo = userInfoRequest.getCertNo(), trueName = userInfoRequest.getTrueName();
			if (StrUtil.isBlank(certNo) || StrUtil.isBlank(trueName) || StrUtil.isBlank(phone) || StrUtil.isBlank(code)) {
				return CommonResponse.withErrorResp("必要信息不能为空~");
			}
			certNo = certNo.toUpperCase();//适配身份证末尾x->X
			// 检查手机和验证码
			RegisterValidUtil.validPhone(phone);
			String cacheKey = StrUtil.format(SendSmsEnum.REAL_NAME_AUTHENTICATION.getCacheKey(), phone);
			String s = redisClientManager.get(cacheKey);
			if (StrUtil.isBlank(s)) {
				logger.info(prefix + "缓存中获取验证码为空,phone={},用户id={}", phone, userConsumer.getId());
				return CommonResponse.withErrorResp("验证码已失效！");
			}
			if (!code.equalsIgnoreCase(s)) {
				logger.info(prefix + "填写的验证码错误,phone={},用户id={}", phone, userConsumer.getId());
				return CommonResponse.withErrorResp("验证码错误！");
			}
			//工具类判断用户身份证号码
			boolean cardBoolean = IdcardUtil.isValidCard(certNo);
			logger.info(prefix + "用户id:【{}】,工具类校验身份证号码验证是否通过：{}", userConsumer.getId(), cardBoolean);
			if (!cardBoolean) {
				return CommonResponse.withErrorResp("身份证号有误,请核对~");
			}
			int age = IdcardUtil.getAgeByIdCard(certNo);
			if (age < 18) {
				logger.info(prefix + "【{}】用户未满18岁,年龄age：{}", userConsumer.getId(), age);
				return CommonResponse.withErrorResp("您还未成年,不能进行此操作~");
			}
			//从阿里云接口拉取实名认证信息
			if (StringUtils.isNotBlank(currentEnviron) && !currentEnviron.equals("daily")) {
				String dayVerifyLimit = redisClient.getObj(VERIFY_LIMIT + userConsumer.getId());
				logger.info(prefix + "【{}】用户今日已验证次数：【{}】,共能验证次数:{}", userConsumer.getId(), dayVerifyLimit, limit);
				int num = 0;
				if (StringUtils.isNotBlank(dayVerifyLimit)) {
					num = Integer.parseInt(dayVerifyLimit);
					//身份证验证
					if (num >= limit) {
						return CommonResponse.withErrorResp("今日身份验证次数已超过三次~");
					}
				}
				UserIDCard userIDCard = verifyIDCard(userConsumer.getId(), certNo, trueName);
				redisClient.setObj(VERIFY_LIMIT + userConsumer.getId(), DateUtil.getCurrentDayRemainderSeconds(), (num + 1) + "");

				if (userIDCard == null) {
					return CommonResponse.withErrorResp("身份信息验证发生异常");
				}
				if (userIDCard.getStatus() != 1) {//1为验证通过
					logger.info(prefix + "【{}】身份证验证状态status:【{}】,msg:【{}】", userConsumer.getId(), userIDCard.getStatus(), userIDCard.getMsg());
					return CommonResponse.withErrorResp("身份信息验证不通过");
				}
			} else {
				logger.info(prefix + "测试环境不走阿里云校验身份证信息,用户昵称【{}】", userConsumer.getNickName());
			}
			ModelResult<Integer> modelResult = userConsumerServiceClient.updateAuthUserInfo(userConsumer.getId(),trueName,certNo);
			if (modelResult != null && modelResult.isSuccess() && modelResult.getModel() != null) {
				UserCenterInfoVo infoVo = new UserCenterInfoVo();
				BeanUtils.copyProperties(userConsumer, infoVo);
				/** 更新成功后，刷新session缓存的用户信息 */
				MemberSession memberSession = updateMemberSession(request);
				if (memberSession == null) {
					logger.warn(prefix + "修改用户【{}】信息,更新用户session信息失败", userConsumer.getId());
				}
				return CommonResponse.withSuccessResp(infoVo);
			}
			logger.error(prefix + "【{}】实名认证用户个人信息，失败原因：{}", userConsumer.getId(), modelResult.getErrorMsg());
			return CommonResponse.withErrorResp("修改失败");
		} catch (Exception e) {
			logger.info(prefix + "发生异常,exception={}", e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	@RequestMapping(value = "/queryInviteCode", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "检查用户是否为邀请码用户", httpMethod = "POST", consumes = "multipart/form-data", produces = "application/json")
	@ApiResponse(code = 200, message = "检查用户是否为邀请码用户", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<Boolean> queryInviteCode(HttpServletRequest request) {
		boolean inviteCodeType = false;
		UserConsumer loginUsr = getLoginUsr(request);
		// logger.info("用户个人信息{}",loginUsr.getInviteCode());
		if (null == loginUsr) {
			return CommonResponse.withErrorResp("请先登录！");
		}
		if (StringUtils.isNotEmpty(loginUsr.getInviteCode())) {
			inviteCodeType = true;
		} else {
			ModelResult<UserConsumer> modelResult = userConsumerServiceClient.queryConsumerById(loginUsr.getId(), new UserConsumerQueryOption());
			if (modelResult != null && modelResult.isSuccess() && modelResult.getModel() != null) {
				if (StringUtils.isNotEmpty(modelResult.getModel().getInviteCode())) {
					logger.info("用户邀请码：{}", modelResult.getModel().getInviteCode());
					inviteCodeType = true;
				}
			}
		}
		return CommonResponse.withSuccessResp(inviteCodeType);
	}


	public CommonResponse judgeUserCanRegist(String phone, BaseRequest baseRequest) {
		ModelResult<UserConsumer> consumerByPhone = userConsumerServiceClient.queryConsumerByPhone(phone);
		// 手机号绑定用户 是一对一的关系
		if (!consumerByPhone.isSuccess()) {
			return CommonResponse.withErrorResp("服务器异常！");
		}
		UserConsumer userConsumer = consumerByPhone.getModel();
		if (null == userConsumer) {
			// 判断用户注册登录开关
			SysConfigProperty sysConfigProperty = getSysConfigByKey(SysConfigPropertyKey.USER_REGISTER_SWITCH, baseRequest.getClientType(), baseRequest.getAgentId());
			if (sysConfigProperty != null) {
				String value = sysConfigProperty.getValue();
				if (StringUtils.isNotBlank(value) && value.trim().equals("0")) {
					return CommonResponse.withErrorResp("注册功能未开放~");
				}
			}
			// 判断链接上邀请码是否合法
			if (!loginManager.judgeInviteCode(baseRequest)) {
				return CommonResponse.withErrorResp("非法注册来源,注册失败~");
			}
		}
		return CommonResponse.withSuccessResp("可以发送验证码");
	}

	@ApiOperation(value = "获取用户websocket连接", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "getUserWebsocket", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<JSONObject> getUserWebsocket(HttpServletRequest request, BaseRequest baseRequest) {
		try {
			UserConsumer userConsumer = getLoginUsr(request);
			JSONObject jsonObject = new JSONObject();
			if (userConsumer == null) {
				logger.info("根据id获取赛事详情,未获取到登录用户信息");
				return CommonResponse.withErrorResp("未获取到登录用户信息");
			}

			String wsurl = usrConsumerCacheManager.buildWSURL();
			jsonObject.put("socketUrl",wsurl);
			jsonObject.put( "userId", userConsumer.getId() + "_" + IdUtil.fastUUID().substring( 0, 8 ) );
			return CommonResponse.withSuccessResp(jsonObject);
		} catch (Exception e) {
			logger.error("获取用户websocket连接，发生异常exception={}",  e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	public UserIDCard verifyIDCard(Long userId, String idCard, String name) {
		Map<String, String> headers = new HashMap<String, String>();
		UserIDCard userIDCard = new UserIDCard();
		headers.put("Authorization", "APPCODE " + appcode);
		Map<String, String> querys = new HashMap<String, String>();
		querys.put("idCard", idCard);
		querys.put("name", name);
		try {
			HttpResponse response = HttpClientUtils.doGet(host, path, method, headers, querys);
			if (response.getStatusLine() != null) {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode != 200) {//状态码: 200 正常；400 URL无效；401 appCode错误； 403 次数用完； 500 API网管错误
					logger.error("阿里云身份证实名认证,【{}】身份证验证出现异常statusCode:【{}】, 400 URL无效；401 appCode错误； 403 次数用完； 500 API网管错误", userId, statusCode);
					throw new BusinessException(CommonExceptionCode.VERIFY_IDCARD);
				}
				String entity = EntityUtils.toString(response.getEntity());
				JSONObject jsonObject = JSONObject.parseObject(entity);
				userIDCard = JSONObject.toJavaObject(jsonObject, UserIDCard.class);
				logger.info("阿里云身份证实名认证,【{}】身份证验证:【{}】", userId, JSON.toJSONString(userIDCard));
				return userIDCard;
			}
		} catch (Exception e) {
			logger.error("阿里云身份证实名认证,发生异常exception={}", e.getMessage(), e);
		}
		return null;
	}
}
