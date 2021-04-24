package com.esportzoo.esport.controller.quiz;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.quiz.QuizSubmitBetRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.quiz.QuizSubmitBetResponse;
import com.esportzoo.esport.constant.ResponseConstant;
import com.esportzoo.esport.constants.quiz.QuizOrderStatus;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.quiz.QuizOrder;
import com.esportzoo.esport.domain.quiz.QuizPlan;
import com.esportzoo.esport.manager.quiz.QuizBetManager;
import com.esportzoo.esport.manager.quiz.followplan.QuizPlanRecommendManager;
import com.esportzoo.esport.service.quiz.QuizOrderService;
import com.esportzoo.esport.service.quiz.QuizPlanService;
import com.esportzoo.quiz.domain.quiz.QuizMatchGame;
import com.esportzoo.quiz.service.quiz.QuizMatchGameService;
import com.esportzoo.quiz.util.CyksUtil;
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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 竞猜投注相关:主要处理竞猜投注和投注记录
 *
 * @author jing.wu
 * @version 创建时间：2019年10月21日 下午6:30:08
 */
@Controller
@RequestMapping("/quiz/bet")
public class QuizBetController extends BaseController {
    private transient final Logger logger = LoggerFactory.getLogger(getClass());
    public static final String logPrefix = "竞猜投注相关";
    @Autowired
    private QuizBetManager quizBetManager;
    @Autowired
    private QuizMatchGameService quizMatchGameService;
    @Autowired
	private QuizPlanService quizPlanService;
    @Autowired
    private QuizOrderService quizOrderService;
    @Autowired
    private CyksUtil cyksUtil;
    @Autowired
    private QuizPlanRecommendManager quizPlanRecommendManager;

    /**
     * 点击立即竞猜逻辑
     */
    @RequestMapping(value = "/doSubmit", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ApiOperation(value = "立即竞猜", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    @ApiResponse(code = 200, message = "立即竞猜接口", response = CommonResponse.class)
    @ResponseBody
    public CommonResponse<QuizSubmitBetResponse> doBet(HttpServletRequest request, QuizSubmitBetRequest quizDoBetRequest) {
        try {
            UserConsumer userConsumer = getLoginUsr(request);
            if (userConsumer == null) {
                return CommonResponse.withErrorResp("未获取到登录用户信息");
            }
            return quizBetManager.doBet(request, quizDoBetRequest, userConsumer);
        } catch (Exception e) {
            logger.error(logPrefix + "立即竞猜接口异常, 参数【{}】 异常【{}】", JSONObject.toJSONString(quizDoBetRequest), e.getMessage(), e);
            return CommonResponse.withErrorResp("投注异常");
        }
    }

    /**
     * 根据余额获取投注金额列表
     */
    @RequestMapping(value = "/getBetNumList", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ApiOperation(value = "获取投注金额列表", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    @ApiResponse(code = 200, message = "获取投注金额列表接口", response = CommonResponse.class)
    @ResponseBody
    public CommonResponse<List<Integer>> getBetNumList(HttpServletRequest request, BaseRequest baseRequest, BigDecimal balance) {
        try {
            logger.info(logPrefix + "获取投注金额列表接口, 参数【{}】 ,余额【{}】", JSONObject.toJSONString(baseRequest), balance);
            UserConsumer userConsumer = getLoginUsr(request);
            if (userConsumer == null) {
                return CommonResponse.withErrorResp("未获取到登录用户信息");
            }
            List<Integer> betNumList = quizBetManager.getBetNumByBalance(baseRequest, balance);
            logger.info(logPrefix + "获取投注金额列表接口, 参数【{}】 ,余额【{}】,返回值:{}", JSONObject.toJSONString(baseRequest), balance, betNumList);
            return CommonResponse.withSuccessResp(betNumList);
        } catch (Exception e) {
            logger.error(logPrefix + "获取投注金额列表接口异常, 参数【{}】 异常【{}】", JSONObject.toJSONString(baseRequest), e.getMessage(), e);
            return CommonResponse.withErrorResp("获取投注金额列表异常");
        }
    }


    @RequestMapping(value = "/getPreBetting", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ApiOperation(value = "获取某个玩法预测投注上限", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    @ApiResponse(code = 200, message = "获取某个玩法预测投注上限", response = CommonResponse.class)
    @ResponseBody
    public CommonResponse<Integer> getPreBetting(HttpServletRequest request, Long matchGameId,Long recommendPlanId,String optIndex) {
        if (null == matchGameId && null == recommendPlanId) {
            return CommonResponse.withErrorResp("无效的玩法!");
        }
        if(null!=recommendPlanId){
            QuizPlan quizPlan = quizPlanRecommendManager.getQuizPlanByRecommendId(recommendPlanId);
            if(null == quizPlan){
                return CommonResponse.withErrorResp("无效的推荐单!");
            }
            matchGameId = quizPlan.getMatchGameId();
            optIndex = quizPlan.getBetOption();
        }
        try {
            UserConsumer userConsumer = getLoginUsr(request);
            if (userConsumer == null) {
                return CommonResponse.withErrorResp("未获取到登录用户信息");
            }
            ModelResult<QuizMatchGame> modelResult = quizMatchGameService.queryById(matchGameId);
            QuizMatchGame matchGame = modelResult.getModel();
            if (!modelResult.isSuccess() || null == matchGame) {
                return CommonResponse.withErrorResp("无效的玩法!");
            }
            Map<String, Integer> stringIntegerMap = cyksUtil.preBetting(matchGame, userConsumer.getId());
            logger.info("获取玩法MatchGameId[{}]预测投注上限[{}]", matchGameId, JSONObject.toJSONString(stringIntegerMap));
            if (null == stringIntegerMap || stringIntegerMap.isEmpty()) {
                return CommonResponse.withSuccessResp(null);
            }
            return CommonResponse.withSuccessResp(stringIntegerMap.get(optIndex));
        } catch (Exception e) {
            logger.error(logPrefix + "获取下单金额上线异常, 参数[matchGameId:{}] 异常[{}]", matchGameId, e.getMessage(), e);
        }
        return CommonResponse.withErrorResp("获取下单金额上线异常!");
    }

    @RequestMapping(value = "/getOrderApplyInfo", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    @ApiOperation(value = "加载订单确认信息", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    @ApiResponse(code = 200, message = "加载订单确认信息", response = CommonResponse.class)
    @ResponseBody
    public CommonResponse getOrderApplyInfo(HttpServletRequest request,Long orderId) {
		UserConsumer loginUsr = getLoginUsr(request);
		if (null == orderId && null==loginUsr) {
			return CommonResponse.withErrorResp(ResponseConstant.PARAM_NOT_NULL);
		}
		try {
			ModelResult<QuizOrder> quizOrderModelResult = quizOrderService.queryById(orderId);
			QuizOrder order = quizOrderModelResult.getModel();
			logger.info("加载订单确认信息,用户id:{},订单id:{},订单状态:{}",loginUsr.getId(),order.getId(),order.getStatus());
			if (!quizOrderModelResult.isSuccess() || null==order){
			    return CommonResponse.withErrorResp(ResponseConstant.SYSTEM_ERROR_MESG);
			}
			//订单与用户要对应
			if (!order.getUserId().equals(loginUsr.getId())){
			    return CommonResponse.withErrorResp(ResponseConstant.WRONG_REQUEST);
			}
			if (QuizOrderStatus.CAST_DEFEAT.getIndex() == order.getStatus().intValue()){
				//TODO 目前一对一
				List<QuizPlan> quizPlans = quizPlanService.queryByOrderIds(Arrays.asList(orderId)).getModel();
				if (CollectionUtil.isNotEmpty(quizPlans)){
					QuizPlan quizPlan = quizPlans.get(0);
					return CommonResponse.withSuccessResp(quizPlan.getRealRejectDesc()).withData(order.getStatus());
				}
			}
			return CommonResponse.withSuccessResp(order.getStatus());
		} catch (Exception e) {
			logger.error("加载订单[{}]信息出现异常[{}]",orderId,e.getMessage(),e);
		}
		return CommonResponse.withErrorResp(ResponseConstant.SYSTEM_ERROR_MESG);
	}

}
