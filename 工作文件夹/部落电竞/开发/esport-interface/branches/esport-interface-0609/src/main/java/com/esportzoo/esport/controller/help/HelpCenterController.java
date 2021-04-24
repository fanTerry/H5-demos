package com.esportzoo.esport.controller.help;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.domain.result.PageResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.esport.client.service.consumer.UserFeedbackServiceClient;
import com.esportzoo.esport.connect.request.BasePageRequest;
import com.esportzoo.esport.connect.request.helpcenter.UserFeedBackSubmitRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.helpcenter.UserFeedbackResponse;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.constants.user.UserFeedbackStatus;
import com.esportzoo.esport.constants.user.UserFeedbackType;
import com.esportzoo.esport.constants.user.UserQuestionType;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.UserFeedback;
import com.esportzoo.esport.manager.CommonManager;
import com.esportzoo.esport.vo.MemberSession;
import com.esportzoo.esport.vo.UserFeedbackQueryVo;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("helpcenter")
@Api(value = "帮助中心", tags = {"帮助中心相关接口"})
public class HelpCenterController extends BaseController {

	public static final String logPrefix = "帮助中心相关接口_";
	@Value("${interface.upload.root}feedback")
	private String uploadRoot;
	@Value("${interface.upload.url}feedback")
	private String uploadURL;
	@Autowired
	private CommonManager commonManager;
	@Autowired
	@Qualifier("userFeedbackServiceClient")
	private UserFeedbackServiceClient userFeedbackServiceClient;

	@Autowired
	RedisClient redisClient;

	@ApiOperation(value = "意见反馈问题类型列表", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "", response = CommonResponse.class)
	@RequestMapping(value = "/feedbackTypeList", method = RequestMethod.POST)
	@ResponseBody
	public CommonResponse<JSONObject> queryFeedbackList(HttpServletRequest request) {
		try {
			JSONObject jsonObject = new JSONObject();
			List<UserQuestionType> questionTypes = UserQuestionType.getAll();
			jsonObject.put("questionTypes", questionTypes);
			return CommonResponse.withSuccessResp(jsonObject);
		} catch (Exception e) {
			logger.info("{}意见反馈问题类型列表接口,发生异常,exception={}", logPrefix, e.getMessage(), e);
			return CommonResponse.withErrorResp(e.getMessage());
		}
	}

	@RequestMapping(value = "/submitFeedback", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "提交意见反馈", httpMethod = "POST", consumes = "multipart/form-data", produces = "application/json")
	@ApiResponse(code = 200, message = "提交意见反馈", response = CommonResponse.class)
	public @ResponseBody
	CommonResponse<JSONObject> submitFeedback(HttpServletRequest request,
			@RequestParam(value = "files", required = false) CommonsMultipartFile[] files, UserFeedBackSubmitRequest submitRequest) {
		String logInnerPrefix = logPrefix + "提交意见反馈接口";
		try {
			logger.info("{},基础参数[{}]", logInnerPrefix, JSONObject.toJSONString(submitRequest));
			JSONObject jsonObject = new JSONObject();
			UserConsumer userConsumer = getLoginUsr(request);
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("用户未登录");
			}
			Integer questionType = submitRequest.getQuestionType();
			if (null == questionType || null == UserQuestionType.valueOf(questionType)) {
				return CommonResponse.withErrorResp("提交意见反馈的类型不对");
			}
			if (StringUtils.isBlank(submitRequest.getContent())) {
				return CommonResponse.withErrorResp("请输入要反馈的内容");
			}
			String uploadImage = "";
			if (files != null && files.length > 0) {
				logger.info("{},上传图片个数=[{}]", logInnerPrefix, files.length);
				uploadImage = commonManager.uploadImage(files, uploadRoot, uploadURL, null);
				if (StringUtils.isEmpty(uploadImage)) {
					return CommonResponse.withErrorResp("上传图片异常,请稍后重试");
				}
				logger.info("{}上传成功后的地址[{}]", logInnerPrefix, uploadImage);
			}
			ModelResult<UserFeedback> modelResult = saveUserFeedBack(submitRequest, userConsumer, uploadImage);
			if (null == modelResult || !modelResult.isSuccess() || null == modelResult.getModel()) {
				return CommonResponse.withErrorResp("提交意见反馈异常");
			}
			jsonObject.put("successFlag", Boolean.TRUE);
			return CommonResponse.withSuccessResp(jsonObject);
		} catch (Exception e) {
			logger.info("{}出现异常,异常信息:{}", logInnerPrefix, e.getMessage(), e);
			return CommonResponse.withResp(ResponseConstant.SYSTEM_ERROR_CODE, ResponseConstant.SYSTEM_ERROR_MESG);

		}
	}

	@RequestMapping("/myFeedbackList")
	@ResponseBody
	public CommonResponse<DataPage<UserFeedbackResponse>> myFeedbackList(HttpServletRequest request, BasePageRequest pageRequest) {
		//用户登录
		try {
			MemberSession memberSession = getMemberSession(request);
			UserConsumer userConsumer = memberSession.getMember();
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("用户未登录");
			}
			UserFeedbackQueryVo queryVo = new UserFeedbackQueryVo();
			queryVo.setUserId(userConsumer.getId());
			queryVo.setFeedbackType(UserFeedbackType.PARENT_ISSUE.getIndex());
			DataPage<UserFeedback> dataPage = new DataPage<UserFeedback>(pageRequest.getPageNo(), pageRequest.getPageSize());
			PageResult<UserFeedback> pageResult = userFeedbackServiceClient.queryPage(queryVo, dataPage);
			if (pageResult.isSuccess()) {
				List<UserFeedback> userFeedbackList = pageResult.getPage().getDataList();
				List<UserFeedbackResponse> responseList = Lists.newArrayList();
				for (UserFeedback userFeedback : userFeedbackList) {
					UserFeedbackResponse feedbackResponse = new UserFeedbackResponse();
					BeanUtil.copyProperties(userFeedback, feedbackResponse);
					UserQuestionType questionType = UserQuestionType.valueOf(userFeedback.getQuestionType());
					if (questionType != null) {
						feedbackResponse.setQuestionTypeName(questionType.getDescription());
					}
					//查询用户反馈回复数
					UserFeedbackQueryVo condition = new UserFeedbackQueryVo();
					condition.setParentId(userFeedback.getId());
					ModelResult<Integer> result = userFeedbackServiceClient.queryCount(condition);
					feedbackResponse.setReplyNum(result.getModel() == null ? 0 : result.getModel());
					responseList.add(feedbackResponse);
				}
				DataPage<UserFeedbackResponse> responseDataPage = new DataPage<>();
				BeanUtil.copyProperties(pageResult.getPage(), responseDataPage, "dataList");
				responseDataPage.setDataList(responseList);
				return CommonResponse.withSuccessResp(responseDataPage);
			} else {
				logger.error(logPrefix + "用户：【{}】，参数param 【{}】,异常：{}", userConsumer.getId(), JSON.toJSONString(pageRequest),
						pageResult.getErrorMsg());
				return CommonResponse.withErrorResp(pageResult.getErrorMsg());

			}
		} catch (Exception e) {
			logger.error(logPrefix + "查询我的反馈列表出现异常，信息：{}", e.getMessage());
			e.printStackTrace();
			return CommonResponse.withErrorResp("系统异常，请稍后再试");
		}
	}

	@RequestMapping("/detailFeedback")
	@ResponseBody
	public CommonResponse<List<UserFeedbackResponse>> detailFeedback(HttpServletRequest request, BasePageRequest pageRequest, Long quetionId) {
		//用户登录
		try {
			MemberSession memberSession = getMemberSession(request);
			UserConsumer userConsumer = memberSession.getMember();
			if (userConsumer == null) {
				return CommonResponse.withErrorResp("用户未登录");
			}
			if (quetionId == null) {
				return CommonResponse.withErrorResp("quetionId.is.null");
			}
			List<UserFeedback> userFeedbackList = Lists.newArrayList();
			ModelResult<UserFeedback> result = userFeedbackServiceClient.queryById(quetionId);
			if (!result.isSuccess() || result.getModel() == null) {
				logger.info(logPrefix + "用户：【{}】，quetionId 【{}】", userConsumer.getId(), quetionId);
				return CommonResponse.withErrorResp("未查询到相应反馈问题");
			}
			UserFeedback userFeedbackQuetion = result.getModel();
			userFeedbackList.add(userFeedbackQuetion);
			//查询用户反馈回复记录
			UserFeedbackQueryVo condition = new UserFeedbackQueryVo();
			condition.setParentId(userFeedbackQuetion.getId());
			ModelResult<List<UserFeedback>> listModelResult = userFeedbackServiceClient.queryList(condition);
			if (listModelResult != null && listModelResult.isSuccess() && listModelResult.getModel() != null) {
				List<UserFeedback> listTemp = listModelResult.getModel();
				if (listTemp.size() > 0) {
					listTemp = CollUtil.sort(listTemp, Comparator.comparing(UserFeedback::getCreateTime));
				}

				userFeedbackList.addAll(listTemp);
			}

			List<UserFeedbackResponse> responseList = Lists.newArrayList();
			for (UserFeedback userFeedback : userFeedbackList) {
				UserFeedbackResponse feedbackResponse = new UserFeedbackResponse();
				BeanUtil.copyProperties(userFeedback, feedbackResponse);
				feedbackResponse.setQuestionTypeName(UserQuestionType.valueOf(userFeedback.getQuestionType()).getDescription());
				responseList.add(feedbackResponse);
			}
			//发布时间最新的答复倒序，第一条是问题
			List<UserFeedbackResponse> reverse = CollUtil.reverse(responseList);
			UserFeedbackResponse remove = responseList.remove(responseList.size() - 1);
			responseList.add(0,remove);
			//CollUtil.a
			return CommonResponse.withSuccessResp(reverse);
		} catch (Exception e) {
			logger.error(logPrefix + "异常：{}", e.getMessage());
			e.printStackTrace();
			return CommonResponse.withErrorResp("查询问题反馈详情异常");
		}

	}

	@RequestMapping("/userSendReply")
	@ResponseBody
	public CommonResponse<UserFeedbackResponse> detailFeedback(HttpServletRequest request, BasePageRequest pageRequest, Long quetionId,
			String content) {

		MemberSession memberSession = getMemberSession(request);
		UserConsumer userConsumer = memberSession.getMember();
		if (userConsumer == null) {
			return CommonResponse.withErrorResp("用户未登录");
		}

		if (quetionId == null) {
			return CommonResponse.withErrorResp("回复问题ID为空");
		}
		if (StringUtils.isEmpty(content)) {
			return CommonResponse.withErrorResp("回复内容不能为空");
		}

		if (!redisClient.setNX("userSendReply_" + userConsumer.getId() + "_" + quetionId, "1", 3)) {
			return CommonResponse.withErrorResp("请勿频繁发送");
		}
		//查询当前回复的问题ID是否存在
		ModelResult<UserFeedback> result = userFeedbackServiceClient.queryById(quetionId);
		if (!result.isSuccess() || result.getModel() == null) {
			logger.info(logPrefix + "用户：【{}】，quetionId 【{}】", userConsumer.getId(), quetionId);
			return CommonResponse.withErrorResp("回复问题不存在");
		}
		UserFeedback currFeedback = result.getModel();
		currFeedback.setStatus(UserFeedbackStatus.UNREAD.getIndex());
		userFeedbackServiceClient.update(currFeedback);
		//插入用户回复内容
		UserFeedback userFeedback = new UserFeedback();
		userFeedback.setContent(content);
		userFeedback.setQuestionType(currFeedback.getQuestionType());
		userFeedback.setUserId(userConsumer.getId());
		userFeedback.setParentId(quetionId);
		userFeedback.setAccount(userConsumer.getAccount());
		userFeedback.setFeedbackType(UserFeedbackType.CONSUMER.getIndex());
		userFeedback.setStatus(UserFeedbackStatus.UNREAD.getIndex());
		userFeedback.setClientType(pageRequest.getClientType());
		userFeedback.setChannelNo(pageRequest.getAgentId().intValue());
		ModelResult<UserFeedback> modelResult = userFeedbackServiceClient.insert(userFeedback);
		UserFeedback feedback = modelResult.getModel();
		ModelResult<UserFeedback> feedbackModelResult = userFeedbackServiceClient.queryById(feedback.getId());
		if (!modelResult.isSuccess() || modelResult.getModel() == null) {
			logger.error(logPrefix + "用户：【{}】，参数param 【{}】，异常信息：{}", userConsumer.getId(), modelResult.getErrorMsg());
			return CommonResponse.withErrorResp("发送回复异常，请稍后再试");
		}


		UserFeedbackResponse feedbackResponse = new UserFeedbackResponse();
		UserFeedback myRely = feedbackModelResult.getModel();
		BeanUtil.copyProperties(myRely, feedbackResponse);
		feedbackResponse.setQuestionTypeName(UserQuestionType.valueOf(userFeedback.getQuestionType()).getDescription());
		return CommonResponse.withSuccessResp(feedbackResponse);
	}


	private ModelResult<UserFeedback> saveUserFeedBack(UserFeedBackSubmitRequest submitRequest, UserConsumer userConsumer, String uploadImage) {
		UserFeedback userFeedback = new UserFeedback();
		userFeedback.setContent(submitRequest.getContent());
		userFeedback.setPhone(submitRequest.getPhone());
		userFeedback.setQuestionType(submitRequest.getQuestionType());
		userFeedback.setImageUrl(uploadImage);
		userFeedback.setUserId(userConsumer.getId());
		userFeedback.setAccount(userConsumer.getAccount());
		userFeedback.setFeedbackType(UserFeedbackType.PARENT_ISSUE.getIndex());
		userFeedback.setStatus(UserFeedbackStatus.UNREAD.getIndex());
		userFeedback.setClientType(submitRequest.getClientType());
		userFeedback.setChannelNo(submitRequest.getAgentId().intValue());
		ModelResult<UserFeedback> modelResult = userFeedbackServiceClient.insert(userFeedback);
		return modelResult;
	}

}
