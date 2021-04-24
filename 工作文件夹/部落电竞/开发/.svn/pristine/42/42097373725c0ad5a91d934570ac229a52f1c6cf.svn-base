package com.esportzoo.esport.controller.quiz.followplan;

import com.alibaba.fastjson.JSONObject;
import com.esportzoo.esport.client.service.common.SysConfigPropertyServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.quiz.QuizSubmitBetRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.quiz.QuizSubmitBetResponse;
import com.esportzoo.esport.connect.response.quiz.followplan.QuizSubmitPlanRecommendResponse;
import com.esportzoo.esport.constants.SysConfigPropertyKey;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.manager.quiz.followplan.QuizPlanRecommendManager;
import com.esportzoo.esport.service.quiz.QuizOrderService;
import com.esportzoo.esport.service.quiz.QuizPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理推单/跟单业务
 */
@Controller
@RequestMapping("followplan")
@Api(value = "处理推单/跟单业务接口", tags = {"处理推单/跟单业务接口"})
public class QuizFollowPlanBetController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(QuizFollowPlanBetController.class);
    public static final String logPrefix = "---处理推单/跟单业务数据---";
    @Autowired
    private QuizOrderService quizOrderService;
    @Autowired
    private QuizPlanService quizPlanService;
    @Autowired
    private QuizPlanRecommendManager quizPlanRecommendManager;
    @Autowired
    SysConfigPropertyServiceClient sysConfigPropertyServiceClient;

    /**
     * 推单接口
     */
    @RequestMapping(value = "/submitPlanRecommend", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ApiOperation(value = "大神推单", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    @ApiResponse(code = 200, message = "大神推单接口", response = CommonResponse.class)
    @ResponseBody
    public CommonResponse<QuizSubmitPlanRecommendResponse> submitPlanRecommend(HttpServletRequest request, BaseRequest baseRequest, Long orderId) {
        try {
            UserConsumer loginUsr = getLoginUsr(request);
            if (loginUsr == null) {
                return CommonResponse.withErrorResp("未获取到登录用户信息");
            }
            logger.info("大神推单接口,用户id:{},订单id:{},基础参数:{}", loginUsr.getId(), orderId, JSONObject.toJSONString(baseRequest));
            if (null == orderId) {
                return CommonResponse.withErrorResp("必要参数为空");
            }
            return quizPlanRecommendManager.doSubmitPlanRecommend(baseRequest, orderId);
        } catch (Exception e) {
            logger.error(logPrefix + "大神推单接口异常, 参数orderId=【{}】 异常【{}】", orderId, e.getMessage(), e);
            return CommonResponse.withErrorResp("推单失败");
        }
    }

    /**
     * 立即跟单接口
     */
    @RequestMapping(value = "/submitPlanFollow", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ApiOperation(value = "立即跟单接口", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    @ApiResponse(code = 200, message = "立即跟单接口", response = CommonResponse.class)
    @ResponseBody
    public CommonResponse<QuizSubmitBetResponse> submitPlanFollow(HttpServletRequest request, QuizSubmitBetRequest quizSubmitBetRequest) {
        try {
            UserConsumer userConsumer = getLoginUsr(request);
            if (userConsumer == null) {
                return CommonResponse.withErrorResp("未获取到登录用户信息");
            }
            logger.info("立即跟单接口,用户id:{},,基础参数:{}", userConsumer.getId(), JSONObject.toJSONString(quizSubmitBetRequest));
            return quizPlanRecommendManager.doSubmitPlanFollow(request, quizSubmitBetRequest, userConsumer);
        } catch (Exception e) {
            logger.error(logPrefix + "立即跟单接口异常, 参数=【{}】 异常【{}】", JSONObject.toJSONString(quizSubmitBetRequest), e.getMessage(), e);
            return CommonResponse.withErrorResp("跟单失败");
        }
    }

    /**
     * 是否可以推荐逻辑
     */
    @RequestMapping(value = "/judgeCanRecommend", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ApiOperation(value = "是否可以推荐", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    @ApiResponse(code = 200, message = "是否可以推荐接口", response = CommonResponse.class)
    @ResponseBody
    public CommonResponse<JSONObject> judgeCanRecommend(HttpServletRequest request, BaseRequest baseRequest, Long orderId) {
        try {
            UserConsumer userConsumer = getLoginUsr(request);
            if (userConsumer == null) {
                return CommonResponse.withErrorResp("未获取到登录用户信息");
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("canRecom",quizPlanRecommendManager.judgeCanRecommend(baseRequest, orderId));
            /** [大神跟单]发单人提成比例*/
            jsonObject.put( "commissionRate", sysConfigPropertyServiceClient.getConfigValueByKey(SysConfigPropertyKey.RECOMMEND_USER_COMMISSION_RATE));
            return CommonResponse.withSuccessResp(jsonObject);
        } catch (Exception e) {
            logger.error(logPrefix + "是否可以推荐接口异常, 参数orderId=【{}】 异常【{}】", orderId, e.getMessage(), e);
            return CommonResponse.withErrorResp("投注异常");
        }
    }

}
