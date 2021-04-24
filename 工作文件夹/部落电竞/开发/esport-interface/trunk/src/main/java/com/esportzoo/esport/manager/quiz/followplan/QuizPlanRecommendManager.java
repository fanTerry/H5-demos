package com.esportzoo.esport.manager.quiz.followplan;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.domain.result.PageResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.client.service.quiz.QuizPlanRecommendServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.quiz.QuizSubmitBetRequest;
import com.esportzoo.esport.connect.request.quiz.followplan.PlanRecommedIndexRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.quiz.QuizSubmitBetResponse;
import com.esportzoo.esport.connect.response.quiz.followplan.QuizSubmitPlanRecommendResponse;
import com.esportzoo.esport.constants.EsportPayway;
import com.esportzoo.esport.constants.SysConfigPropertyKey;
import com.esportzoo.esport.constants.cms.FollowStatus;
import com.esportzoo.esport.constants.quiz.QuizAcceptBetterSpType;
import com.esportzoo.esport.constants.quiz.QuizPayStatus;
import com.esportzoo.esport.constants.quiz.follow.RecommendQueryEnum;
import com.esportzoo.esport.constants.quiz.follow.RecommendStatus;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.quiz.QuizOrder;
import com.esportzoo.esport.domain.quiz.QuizPlan;
import com.esportzoo.esport.domain.quiz.QuizPlanRecommend;
import com.esportzoo.esport.service.common.SysConfigPropertyService;
import com.esportzoo.esport.service.quiz.QuizOrderService;
import com.esportzoo.esport.service.quiz.QuizPlanService;
import com.esportzoo.esport.vo.quiz.QuizOrderQueryVo;
import com.esportzoo.esport.vo.quiz.QuizPlanRecommendDetail;
import com.esportzoo.esport.vo.quiz.QuizPlanRecommendQueryVo;
import com.esportzoo.esport.vo.quiz.SubmitOrderParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @description: 大神推荐单相关
 * @author: Haitao.Li
 * @create: 2020-06-09 10:12
 **/
@Component
public class QuizPlanRecommendManager {

    private transient final Logger logger = LoggerFactory.getLogger(getClass());
    public static final String logPrefix = "大神推荐单相关：";
    private static final String DUPLICATION_CHECK = "esport_interface_duplication_planrecommend_userid{}_matchgameid{}";
    /**当个用户默认推单次数 */
    private final int SINGLE_USER_RECOMMEND_ORDER_DEFAULT_NUM = 5;

    @Autowired
    @Qualifier("quizPlanRecommendServiceClient")
    QuizPlanRecommendServiceClient quizPlanRecommendServiceClient;
    @Autowired
    @Qualifier("userConsumerServiceClient")
    UserConsumerServiceClient userConsumerServiceClient;
    @Autowired
    private QuizOrderService quizOrderService;
    @Autowired
    private QuizPlanService quizPlanService;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private SysConfigPropertyService sysConfigPropertyService;

	/**
	 * 推荐单分页条件查询，含金额榜，赛事、关注等条件
	 * @param indexRequest
	 * @return
	 */
	public CommonResponse<DataPage<QuizPlanRecommendDetail>> pageQueryPlanRecommend( PlanRecommedIndexRequest indexRequest) {

		Long userId = indexRequest.getUserId();
        if (userId == null) {
            logger.error(logPrefix + "用户ID为空");
            return CommonResponse.withErrorResp("用户未登录");
        }

        if (indexRequest.getFollowQueryType() == null) {
            logger.error(logPrefix + "分页查询类型为空");
            return CommonResponse.withErrorResp("查询类型为空");
        }

        RecommendQueryEnum queryEnum = RecommendQueryEnum.valueOf(indexRequest.getFollowQueryType());
        if (queryEnum == null) {
            logger.error(logPrefix + "查询类型不存在，类型={}", indexRequest.getFollowQueryType());
            return CommonResponse.withErrorResp("查询类型不存在");
        }
        QuizPlanRecommendQueryVo queryCondition = getQueryCondition(queryEnum, userId,indexRequest.getMatchId());
        if (queryCondition == null) {
            logger.error(logPrefix + "获取查询条件错误,followType={},userId={}", queryEnum.getIndex(), userId);
            return CommonResponse.withErrorResp("获取查询条件错误");
        }
        logger.info(logPrefix + "分页查询参数param 【{}】", JSON.toJSONString(queryCondition));

        try {
			DataPage<QuizPlanRecommendDetail> queryPage = new DataPage();
			queryPage.setPageNo( indexRequest.getPageNo() );
			queryPage.setPageSize( indexRequest.getPageSize() );
			PageResult<QuizPlanRecommendDetail> recommendPageResult = quizPlanRecommendServiceClient.queryRecommendPage( queryCondition, queryPage );
			if (recommendPageResult != null && recommendPageResult.isSuccess()) {
				return CommonResponse.withSuccessResp( recommendPageResult.getPage() );
			} else {
				logger.error( logPrefix + "用户：【{}】，查询参数={},分页查询失败原因：{}", userId, JSON.toJSONString( queryCondition ),
						recommendPageResult.getErrorMsg() );
				return CommonResponse.withErrorResp( "分页查询失败" );
			}



        } catch (Exception e) {
            logger.error(logPrefix + "分页查询出现异常，用户ID={},查询参数={}", userId, JSON.toJSONString(queryCondition));
            e.printStackTrace();
            return CommonResponse.withErrorResp("查询出现异常");
        }


    }

	/**
	 * 获取推荐榜单分页查询条件
	 * @param queryEnum
	 * @param userId
	 * @param matchId
	 * @return
	 */
    private QuizPlanRecommendQueryVo getQueryCondition(RecommendQueryEnum queryEnum, Long userId,Long matchId) {
        QuizPlanRecommendQueryVo queryVo = new QuizPlanRecommendQueryVo();

        queryVo.setFollowQueryType(queryEnum.getIndex());
		queryVo.setStatus(RecommendStatus.RECOMMEND.getIndex());
        if (queryEnum.equals(RecommendQueryEnum.MY_RECOMMEND)) {
            //用户发单列表发单
			queryVo.setStatus(null);
            queryVo.setUserId(userId);
            return queryVo;
        } else if (queryEnum.equals(RecommendQueryEnum.MY_FOLLOW_USER)) {
            //我关注的
            queryVo.setUserId(userId);
            queryVo.setUserFollowStatus(FollowStatus.FOLLOW.getIndex());
            queryVo.setStartRecommendEndTime(new Date() );
            queryVo.setStatus(RecommendStatus.RECOMMEND.getIndex());
            return queryVo;
        } else if (queryEnum.equals(RecommendQueryEnum.FOLLOW_COST_RANKINGS) || queryEnum.equals(RecommendQueryEnum.HOT_RANKINGS) || queryEnum
                .equals(RecommendQueryEnum.WIN_RANKINGS)) {
            //命中榜，金额榜，人气榜
            queryVo.setStartRecommendEndTime(new Date());

            return queryVo;
        }else if (queryEnum.equals(RecommendQueryEnum.MATCH_RECOMMEND)){
        	//查询赛事下面的推荐单
			queryVo.setMatchId( matchId );
			return queryVo;
		}

        return null;
    }

    /**
     * 判断是否可以推荐
     * @param baseRequest
     * @param orderId
     * @return
     */
    public Boolean judgeCanRecommend(BaseRequest baseRequest, Long orderId) {
        ModelResult<List<QuizPlan>> quizPlanModelResult = quizPlanService.queryByOrderIds(Arrays.asList(orderId));
        List<QuizPlan> quizPlans = quizPlanModelResult.getModel();
        if (!quizPlanModelResult.isSuccess() || null == quizPlans) {
            return false;
        }
        if (CollectionUtil.isNotEmpty(quizPlans)) {
            QuizPlan quizPlan = quizPlans.get(0);
            ModelResult<Boolean> modelResult = quizOrderService.checkRecommendPlanCondition(quizPlan.getId());
            logger.info("判断是否可以推荐接口manager,参数planId:{},返回结果:{}", quizPlan.getId(),JSONObject.toJSONString(modelResult));
            if (!modelResult.isSuccess() || null == modelResult.getModel()) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 处理推单逻辑
     *
     * @param baseRequest
     * @param orderId quiz_order表id
     * @return
     */
    public CommonResponse<QuizSubmitPlanRecommendResponse> doSubmitPlanRecommend(BaseRequest baseRequest, Long orderId) {
        ModelResult<List<QuizPlan>> quizPlanModelResult = quizPlanService.queryByOrderIds(Arrays.asList(orderId));
        List<QuizPlan> quizPlans = quizPlanModelResult.getModel();
        if (!quizPlanModelResult.isSuccess() || null == quizPlans) {
            logger.info("处理推单逻辑manager,参数orderId:{},对应quizPlan为空", orderId);
            return CommonResponse.withErrorResp("推单查询方案异常");
        }
        if (CollectionUtil.isNotEmpty(quizPlans)) {
            QuizPlan quizPlan = quizPlans.get(0);
            ModelResult<Long> modelResult = quizOrderService.createRecommendPlan(quizPlan.getId());
            logger.info("处理推单接口manager,参数planId:{},返回结果:{}", quizPlan.getId(), JSONObject.toJSONString(modelResult));
            if (!modelResult.isSuccess() || null == modelResult.getModel()) {
                return CommonResponse.withErrorResp(modelResult.getErrorMsg());
            }
            QuizSubmitPlanRecommendResponse response = new QuizSubmitPlanRecommendResponse();
            response.setPlanRecommendId(modelResult.getModel());
            response.setRemainderTimes(getUserRemainderTimes(quizPlan.getUserId()));
            response.setPlanId(quizPlan.getId());
            response.setUserId(quizPlan.getUserId());
            return CommonResponse.withSuccessResp(response);
        }
        return CommonResponse.withErrorResp("推单异常");
    }

    /**
     * 获取用户可发单次数
     *
     * @param userId
     * @return
     */
    public Integer getUserRemainderTimes(Long userId) {
        String limitNumStr = sysConfigPropertyService.getConfigValueByKey(SysConfigPropertyKey.TODAY_USER_ENABLE_RECOMMEND_ORDER_COUNT);
        Integer limitRecNum = SINGLE_USER_RECOMMEND_ORDER_DEFAULT_NUM, res = 0;
        if (StrUtil.isNotBlank(limitNumStr)) {
            limitRecNum = Integer.parseInt(limitNumStr.trim());
        }
        QuizPlanRecommendQueryVo param = new QuizPlanRecommendQueryVo();
        param.setStartCreateTime(DateUtil.getTodayStartTime());
        param.setEndCreateTime(DateUtil.getTodayEndTime());
        param.setUserId(userId);
        param.setStatusList(Arrays.asList(RecommendStatus.RECOMMEND.getIndex(), RecommendStatus.OVER.getIndex()));
        ModelResult<List<QuizPlanRecommend>> modelResult = quizPlanRecommendServiceClient.queryList(param);
        if (!modelResult.isSuccess()) {
            return res;
        }
        List<QuizPlanRecommend> recommends = modelResult.getModel();
        logger.info(logPrefix + "获取用户[{}]今日推单记录[{}]", userId, CollectionUtil.isEmpty(recommends) ? "无推单记录" : recommends.size());
        res = CollectionUtil.isNotEmpty(recommends) ? limitRecNum - recommends.size() : limitRecNum;
        return res;
    }

    /**
     * 用户立即跟单接口
     *
     * @param request
     * @param quizSubmitBetRequest
     * @param userConsumer
     * @return
     */
    public CommonResponse<QuizSubmitBetResponse> doSubmitPlanFollow(HttpServletRequest request, QuizSubmitBetRequest quizSubmitBetRequest, UserConsumer userConsumer) {
        logger.info(logPrefix + "用户id={},立即跟单接口参数:{}", userConsumer.getId(), JSONObject.toJSONString(quizSubmitBetRequest));
        //防重点击
        String key = StrUtil.format(DUPLICATION_CHECK, userConsumer.getId(), quizSubmitBetRequest.getMatchGameId());
        if (!redisClient.setNX(key, "1", 3)) {
            logger.error("3秒内只能提交一次投注，用户ID【{}】,MatchGameId【{}】", userConsumer.getId(), quizSubmitBetRequest.getMatchGameId());
            return CommonResponse.withErrorResp("请勿重复提交");
        }
        try {
            // 校验请求参数
            if (null == quizSubmitBetRequest || null == quizSubmitBetRequest.getRecommendPlanId() || null == quizSubmitBetRequest.getOrderType()) {
                logger.info(logPrefix + "用户id={},必要参数为空,传入接口参数:{}", userConsumer.getId(), JSONObject.toJSONString(quizSubmitBetRequest));
                return CommonResponse.withErrorResp("必要参数为空");
            }
            ModelResult<QuizPlanRecommend> recModelResult = quizPlanRecommendServiceClient.queryById(quizSubmitBetRequest.getRecommendPlanId());
            if (!recModelResult.isSuccess() || null == recModelResult.getModel()) {
                return CommonResponse.withErrorResp(recModelResult.getErrorMsg());
            }
            ModelResult<QuizPlan> quizPlanModelResult = quizPlanService.queryById(recModelResult.getModel().getPlanId());
            if (!quizPlanModelResult.isSuccess() || null == quizPlanModelResult.getModel()) {
                return CommonResponse.withErrorResp(quizPlanModelResult.getErrorMsg());
            }
            QuizPlan quizPlan = quizPlanModelResult.getModel();
            SubmitOrderParam submitOrderParam = new SubmitOrderParam();
            submitOrderParam.setUserId(userConsumer.getId());
            submitOrderParam.setMatchGameId(quizPlan.getMatchGameId());
            submitOrderParam.setMatchNo(quizPlan.getMatchNo());
            submitOrderParam.setPlayNo(quizPlan.getPlayNo());
            submitOrderParam.setAmount(quizSubmitBetRequest.getUserBetNum());
            submitOrderParam.setOption(quizPlan.getBetOption());
            submitOrderParam.setPayType(EsportPayway.REC_PAY.getIndex());
            submitOrderParam.setBizSystem(quizSubmitBetRequest.getBiz());
            submitOrderParam.setClientType(quizSubmitBetRequest.getClientType());
            submitOrderParam.setChannelNo(userConsumer.getChannelNo().intValue());
            submitOrderParam.setAcceptBetterSp(QuizAcceptBetterSpType.TRUE.getIndex());
            submitOrderParam.setOrderType(quizSubmitBetRequest.getOrderType());
            submitOrderParam.setPlanId(quizPlan.getId());

            ModelResult<Long> modelResult = quizOrderService.submitOrder(submitOrderParam);
            logger.info(logPrefix + "用户id={},立即跟单接口参数:{},调用submitOrder接口返回值:{}", userConsumer.getId(), JSONObject.toJSONString(quizSubmitBetRequest),
                    JSONObject.toJSONString(modelResult));
            if (null == modelResult) {
                return CommonResponse.withErrorResp("调用接口异常");
            }
            if (!modelResult.isSuccess()) {
                return CommonResponse.withResp(modelResult.getErrorCode(), modelResult.getErrorMsg());
            }
            QuizSubmitBetResponse response = new QuizSubmitBetResponse();
            response.setBetOrderId(modelResult.getModel());
            return CommonResponse.withSuccessResp(response);
        } catch (Exception e) {
            logger.error(logPrefix + "立即跟单接口异常, 参数【{}】 异常【{}】", JSONObject.toJSONString(quizSubmitBetRequest), e.getMessage(), e);
            return CommonResponse.withErrorResp("立即跟单异常");
        }
    }

    /**
     * 根据推荐id 拿quizplan信息
     * @param recommendPlanId
     * @return
     */
    public QuizPlan getQuizPlanByRecommendId(Long recommendPlanId) {
        ModelResult<QuizPlanRecommend> recModelResult = quizPlanRecommendServiceClient.queryById(recommendPlanId);
        if (!recModelResult.isSuccess() || null == recModelResult.getModel()) {
            return null;
        }
        ModelResult<QuizPlan> quizPlanModelResult = quizPlanService.queryById(recModelResult.getModel().getPlanId());
        if (!quizPlanModelResult.isSuccess() || null == quizPlanModelResult.getModel()) {
            return null;
        }else{
            return quizPlanModelResult.getModel();
        }
    }

}
