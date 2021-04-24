package com.esportzoo.esport.controller.quiz.followplan;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.domain.result.PageResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.esport.cache.CacheConstant;
import com.esportzoo.esport.client.service.cms.UserFollowServiceClient;
import com.esportzoo.esport.client.service.consumer.UserConsumerServiceClient;
import com.esportzoo.esport.client.service.quiz.QuizPlanFollowServiceClient;
import com.esportzoo.esport.client.service.quiz.QuizPlanRecommendServiceClient;
import com.esportzoo.esport.client.service.quiz.QuizUserScoreServiceClient;
import com.esportzoo.esport.connect.request.quiz.followplan.PlanFollowPageRequest;
import com.esportzoo.esport.connect.request.quiz.followplan.PlanRecommedIndexRequest;
import com.esportzoo.esport.connect.request.quiz.followplan.RecommendDetailRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.quiz.QuizMatchResponse;
import com.esportzoo.esport.connect.response.quiz.followplan.PlanFollowPageResponse;
import com.esportzoo.esport.connect.response.quiz.followplan.RecommendDetailResponse;
import com.esportzoo.esport.connect.response.quiz.followplan.RecommendMatchResponse;
import com.esportzoo.esport.constant.CachedKeyAndTimeLong;
import com.esportzoo.esport.constants.CacheKeyConstant;
import com.esportzoo.esport.constants.cms.FollowObjectType;
import com.esportzoo.esport.constants.quiz.follow.FollowStatus;
import com.esportzoo.esport.constants.quiz.follow.RecommendQueryEnum;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.UserFollow;
import com.esportzoo.esport.domain.quiz.QuizPlanRecommend;
import com.esportzoo.esport.domain.quiz.QuizUserScore;
import com.esportzoo.esport.manager.quiz.followplan.QuizPlanRecommendManager;
import com.esportzoo.esport.vo.UserConsumerQueryOption;
import com.esportzoo.esport.vo.cms.UserFollowQueryVo;
import com.esportzoo.esport.vo.quiz.*;
import com.esportzoo.quiz.vo.quiz.QuizMatchGameDetail;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: 大神推荐单相关
 *
 * @author: Haitao.Li
 *
 * @create: 2020-06-04 17:47
 **/

@Controller
@RequestMapping("planRecommend")
@Api(value = "用户推荐单相关接口", tags = {"用户推荐单相关接口"})
public class QuizPlanRecommendController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger( QuizPlanRecommendController.class );

	public static final String logPrefix = "---用户推荐单相关数据---";

	@Autowired
	@Qualifier("quizPlanRecommendServiceClient")
	QuizPlanRecommendServiceClient quizPlanRecommendServiceClient;

	@Autowired
	@Qualifier("quizUserScoreServiceClient")
	QuizUserScoreServiceClient quizUserScoreServiceClient;

	@Autowired
	@Qualifier("quizPlanFollowServiceClient")
	QuizPlanFollowServiceClient quizPlanFollowServiceClient;

	@Autowired
	UserConsumerServiceClient userConsumerServiceClient;

	@Autowired
	QuizPlanRecommendManager quizPlanRecommendManager;

	@Autowired
	UserFollowServiceClient userFollowServiceClient;

	@Autowired
	RedisClient redisClient;

	@RequestMapping("getTenwinUserInfo")
	public @ResponseBody
	CommonResponse<QuizRecommendUserTenwinInfoVo> getTenwinUserInfo(HttpServletRequest request,Long userId) {

		UserConsumer userConsumer = getLoginUsr( request );
		if (userConsumer == null) {
			return CommonResponse.withErrorResp( "用户未登录" );
		}
		if (userId == null) {
			logger.error(logPrefix+"查询近10场数据，用户ID不能为空");
			return CommonResponse.withErrorResp( "用户ID不能为空" );
		}
		try {
			ModelResult<QuizRecommendUserTenwinInfoVo> modelResult = quizPlanRecommendServiceClient.getTenWinUserData( userId);
			if (!modelResult.isSuccess() ||modelResult.getModel()==null) {
				logger.error(logPrefix+"用户：【{}】，查询用户近10场信息失败 【{}】",userId,modelResult.getErrorMsg());
				return CommonResponse.withErrorResp( "查询用户近10场信息失败" );
			}
			QuizRecommendUserTenwinInfoVo userTenwinInfoVo = modelResult.getModel();

			//关联大神关注状态
			if (userConsumer.getId().equals( userId )) {
				userTenwinInfoVo.setFollowStatus( true );
			} else {
				UserFollowQueryVo userFollowQueryVo = new UserFollowQueryVo();
				userFollowQueryVo.setUserId( userConsumer.getId() );
				userFollowQueryVo.setObjectType( FollowObjectType.USER.getIndex() );
				userFollowQueryVo.setObjectId( userId ); //对象ID
				userFollowQueryVo.setStatus( com.esportzoo.esport.constants.cms.FollowStatus.FOLLOW.getIndex() );
				ModelResult<List<UserFollow>> listModelResult = userFollowServiceClient.queryList( userFollowQueryVo );
				if (listModelResult != null && listModelResult.isSuccess() && listModelResult.getModel() != null) {
					List<UserFollow> userFollowList = listModelResult.getModel();
					if (userFollowList != null && userFollowList.size() > 0) {
						//已关注
						userTenwinInfoVo.setFollowStatus( true );
					}
				}
			}
			if (userConsumer.getId().equals( userId )){
				userTenwinInfoVo.setCanEditIntro( true );
			}
			return CommonResponse.withSuccessResp( userTenwinInfoVo );
		} catch (Exception e) {
			logger.error(logPrefix+"用户：【{}】，查询用户近10场信息发生异常 【{}】",userId, e);
			e.printStackTrace();
			return CommonResponse.withErrorResp( "查询异常" );
		}

	}

	@RequestMapping("pageData")
	public @ResponseBody
	CommonResponse<DataPage<QuizPlanRecommendDetail>> pageData(HttpServletRequest request, PlanRecommedIndexRequest indexRequest) {

		UserConsumer userConsumer = getLoginUsr( request );
		if (userConsumer == null) {
			return CommonResponse.withErrorResp( "用户未登录" );
		}
		try {
			if (indexRequest.getUserId() == null) {
				indexRequest.setUserId( userConsumer.getId() );
			}
			logger.info( logPrefix + "用户：【{}】，参数param 【{}】", userConsumer.getId(), JSON.toJSONString( indexRequest ) );
			CommonResponse<DataPage<QuizPlanRecommendDetail>> pageCommonResponse = quizPlanRecommendManager.pageQueryPlanRecommend( indexRequest );
			return pageCommonResponse;
		} catch (Exception e) {
			logger.error( logPrefix + "用户：【{}】，分页查询推荐单异常 【{}】", userConsumer.getId(), e );
			e.printStackTrace();
			return CommonResponse.withErrorResp( "查询异常" );
		}

	}


	@RequestMapping("getHistoryRecommendData")
	public @ResponseBody
	CommonResponse<UserRecommendHistoryDataVo> getHistoryRecommendData(HttpServletRequest request) {

		UserConsumer userConsumer = getLoginUsr( request );
		if (userConsumer == null) {
			return CommonResponse.withErrorResp( "用户未登录" );
		}
		ModelResult<UserRecommendHistoryDataVo> modelResult = null;
		try {
			modelResult = quizPlanRecommendServiceClient.getUserHistoryRecommendData( userConsumer.getId() );

			if (!modelResult.isSuccess() || modelResult.getModel() == null) {
				logger.error( logPrefix + "用户：【{}】，查询发单历史统计数据失败 【{}】", userConsumer.getId(), modelResult.getErrorMsg() );
				return CommonResponse.withErrorResp( "查询发单历史统计数据失败" );
			}
			UserRecommendHistoryDataVo userRecommendHistoryDataVo = modelResult.getModel();
			return CommonResponse.withSuccessResp( userRecommendHistoryDataVo );
		} catch (Exception e) {
			logger.error(logPrefix+"用户：【{}】，查询发单历史统计数据发生异常【{}】",userConsumer.getId(),e);
			e.printStackTrace();
			return CommonResponse.withErrorResp( "查询异常" );
		}


	}

	@RequestMapping("getTenWinBand")
	public @ResponseBody
	CommonResponse<List> getTenWinBand(HttpServletRequest request) {

		List<Map<String, Object>> objectList ;
		try {
			objectList = redisClient.getObj( CacheConstant.RECOMMENDER_TEN_WIN_BAND );
			if (objectList != null) {
				return CommonResponse.withSuccessResp( objectList );
			}
			objectList = Lists.newArrayList();

			/** 查询按照近10场胜率前五的推荐大神 */
			List<QuizUserScore> quizUserScoreList;
			ModelResult<List<QuizUserScore>> tenWinRateBand = quizUserScoreServiceClient.getTenWinRateBand( 10 );
			if (tenWinRateBand != null && tenWinRateBand.isSuccess() && tenWinRateBand.getModel() != null) {
				quizUserScoreList = tenWinRateBand.getModel();
			} else {
				logger.warn( logPrefix + "查询近10场胜率榜单数据失败，失败信息:{]", tenWinRateBand.getErrorMsg() );
				return CommonResponse.withErrorResp( "查询榜单数据失败" );
			}

			for (QuizUserScore quizUserScore : quizUserScoreList) {
				if (quizUserScore.getTenReturnRate() == null || quizUserScore.getTenReturnRate().compareTo( BigDecimal.ZERO ) <= 0) {
					continue;
				}
				Map<String, Object> objectMap = Maps.newHashMap();
				ModelResult<UserConsumer> consumerModelResult = userConsumerServiceClient
						.queryConsumerById( quizUserScore.getUserId(), new UserConsumerQueryOption() );

				if (!consumerModelResult.isSuccess()) {
					logger.warn( logPrefix + "用户不存在：【{}】", quizUserScore.getId() );
					continue;
				}
				UserConsumer consumer = consumerModelResult.getModel();
				objectMap.put( "userId", consumer.getId() );
				objectMap.put( "nickName", consumer.getNickName() );
				objectMap.put( "icon", consumer.getIcon() );
				objectMap.put( "tenWinRate",  quizUserScore.getTenWinRate().stripTrailingZeros().toPlainString()+"%");
				objectList.add( objectMap );

			}
			//放置一分钟的缓存时间
			if (objectList.size() > 0) {
				redisClient.setObj( CacheKeyConstant.RECOMMENDER_TEN_WIN_BAND, CachedKeyAndTimeLong.setMinutes( 1 ), objectList );
			}
		} catch (Exception e) {
			logger.error( logPrefix + "查询近10场胜率前五的推荐大神,发生异常:{}", e );
			e.printStackTrace();
			return CommonResponse.withErrorResp( "查询前五的推荐大神,发生异常" );
		}
		return CommonResponse.withSuccessResp( objectList );

	}

	@RequestMapping("synData")
	public @ResponseBody
	CommonResponse<Dict> synRecomend(HttpServletRequest request, Long planRecommendId) {

		if (planRecommendId == null) {
			logger.error(logPrefix+"同步推荐单是数据，推荐单ID为空");
			return CommonResponse.withErrorResp( "推荐单ID为空" );
		}

		ModelResult<QuizPlanRecommend> modelResult = quizPlanRecommendServiceClient.queryById( planRecommendId );
		if (modelResult != null && modelResult.isSuccess() && modelResult.getModel() != null) {
			QuizPlanRecommend quizPlanRecommend = modelResult.getModel();
			Dict dict = Dict.create();
			dict.set( "followAmount", quizPlanRecommend.getFollowAmount() );
			dict.set( "followCount", quizPlanRecommend.getFollowCount() );
			return CommonResponse.withSuccessResp( dict );

		} else {
			logger.error( logPrefix + "同步推荐单是数据，推荐单ID【{}】推荐单不存在", planRecommendId );
			return CommonResponse.withErrorResp( "推荐单不存在" );
		}

	}

	@RequestMapping(value = "/planFollowPage", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "跟单详情", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "跟单详情", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<DataPage<PlanFollowPageResponse>> queryPlanFollowPage(PlanFollowPageRequest planFollowPageRequest, HttpServletRequest request) {
		UserConsumer userConsumer = getLoginUsr(request);
		if (userConsumer == null) {
			return CommonResponse.withErrorResp("用户未登录");
		}
		if (planFollowPageRequest.getPlanId() == null) {
			return CommonResponse.withErrorResp("推荐单方案ID为空");
		}
		try {
			QuizPlanFollowQueryVo quizPlanFollowQueryVo = new QuizPlanFollowQueryVo();
			DataPage<QuizPlanFollowDetail> page = new DataPage();
			quizPlanFollowQueryVo.setRecommendPlanId(planFollowPageRequest.getPlanId());
			quizPlanFollowQueryVo.setFollowStatus( FollowStatus.SUCCESS.getIndex());
			page.setPageSize(planFollowPageRequest.getPageSize());
			page.setPageNo(planFollowPageRequest.getPageNo());
			page.setOrderBy("create_time");
			PageResult<QuizPlanFollowDetail> pageResult = quizPlanFollowServiceClient.queryDetailPage(quizPlanFollowQueryVo, page);
			if (pageResult != null && pageResult.isSuccess() && pageResult.getPage() != null) {
				DataPage<QuizPlanFollowDetail> pageResultPage = pageResult.getPage();
				List<QuizPlanFollowDetail> dataList = pageResultPage.getDataList();
				DataPage<PlanFollowPageResponse> pagePlanFollow=new DataPage();
				BeanUtil.copyProperties(pageResultPage,pagePlanFollow, CopyOptions.create().ignoreNullValue());
				List<PlanFollowPageResponse> planVo=new ArrayList<>();
				if (CollectionUtil.isNotEmpty(dataList)) {
					for (QuizPlanFollowDetail quizPlanFollowDetail : dataList) {
						PlanFollowPageResponse planFollowPageResponse = new PlanFollowPageResponse();
						BeanUtil.copyProperties(quizPlanFollowDetail, planFollowPageResponse, CopyOptions.create().ignoreNullValue());
						planVo.add(planFollowPageResponse);
					}
				}
				pagePlanFollow.setDataList(planVo);
				return CommonResponse.withSuccessResp(pagePlanFollow);
			}
			logger.error(logPrefix + "用户：【{}】，错误信息 【{}】", userConsumer.getId(), pageResult.getErrorMsg());
			return CommonResponse.withErrorResp(pageResult.getErrorMsg());
		} catch (Exception e) {
			logger.error(logPrefix + "_发单详情页,用户：【{}】，参数param 【{}】", userConsumer.getId(), JSON.toJSONString(planFollowPageRequest));
			e.printStackTrace();
			return CommonResponse.withErrorResp("查询跟单详情异常");
		}
	}


	@RequestMapping(value = "/recommendDetail", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "发单详情页", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "发单详情页", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<RecommendDetailResponse> queryRecommendDetail(RecommendDetailRequest recommendDetailRequest, HttpServletRequest request) {
		UserConsumer userConsumer = getLoginUsr(request);
		if (userConsumer == null) {
			return CommonResponse.withErrorResp("用户未登录");
		}
		if (recommendDetailRequest.getId() == null ) {
			return CommonResponse.withErrorResp("参数为空");
		}
		try {
			ModelResult<QuizPlanRecommendDetail> modelResult = quizPlanRecommendServiceClient.queryRecommendDetails(recommendDetailRequest.getId(),userConsumer.getId());
			if (modelResult !=null && modelResult.isSuccess()&& modelResult.getModel()!=null){
				QuizPlanRecommendDetail planRecommendDetail = modelResult.getModel();
				RecommendDetailResponse recommendDetailResponse = new RecommendDetailResponse();
				BeanUtil.copyProperties(planRecommendDetail,recommendDetailResponse,CopyOptions.create().ignoreNullValue());
				QuizMatchGameDetail quizMatchGameDetail = planRecommendDetail.getQuizMatchGameDetail();
				if (quizMatchGameDetail!=null) {
					RecommendMatchResponse recommendMatchResponse = new RecommendMatchResponse();
					BeanUtil.copyProperties(quizMatchGameDetail, recommendMatchResponse, CopyOptions.create().ignoreNullValue());
					if (quizMatchGameDetail.getStatus() != null) {
						recommendMatchResponse.setMatchStatus(quizMatchGameDetail.getStatus());
					}
					recommendDetailResponse.setRecommendMatchResponse(recommendMatchResponse);
				}
				return CommonResponse.withSuccessResp(recommendDetailResponse);
			}
			logger.error(logPrefix + "查询发单详情页_用户：【{}】，错误信息 【{}】", userConsumer.getId(), modelResult.getErrorMsg());
			return CommonResponse.withErrorResp(modelResult.getErrorMsg());
		} catch (Exception e) {
			logger.error(logPrefix + "查询发单详情页_用户：【{}】，id：【{}】", userConsumer.getId(), recommendDetailRequest.getId());
			e.printStackTrace();
			return CommonResponse.withErrorResp("查询发单详情页异常");
		}
	}



}
