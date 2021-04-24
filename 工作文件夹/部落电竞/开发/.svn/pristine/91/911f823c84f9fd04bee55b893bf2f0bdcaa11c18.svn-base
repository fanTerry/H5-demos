package com.esportzoo.esport.controller.quiz;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.quiz.QuizMatchInfoRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.quiz.QuizLeagueResponse;
import com.esportzoo.esport.connect.response.quiz.QuizMatchGameResponse;
import com.esportzoo.esport.connect.response.quiz.QuizMatchResponse;
import com.esportzoo.esport.controller.BaseController;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.manager.quiz.QuizMatchInfoManager;
import com.esportzoo.quiz.client.service.quiz.QuizLeagueServiceClient;
import com.esportzoo.quiz.client.service.quiz.QuizMatchGameServiceClient;
import com.esportzoo.quiz.client.service.quiz.QuizMatchServiceClient;
import com.esportzoo.quiz.constants.*;
import com.esportzoo.quiz.domain.quiz.QuizLeague;
import com.esportzoo.quiz.domain.quiz.QuizMatch;
import com.esportzoo.quiz.domain.quiz.QuizMatchGame;
import com.esportzoo.quiz.domain.quiz.QuizPlayDefine;
import com.esportzoo.quiz.util.CatchPlayDefineUtil;
import com.esportzoo.quiz.vo.quiz.QuizLeagueQueryVo;
import com.esportzoo.quiz.vo.quiz.QuizMatchGameQueryVo;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 竞猜赛事相关:主要处理竞猜赛事&题目&赔率相关
 * @author jing.wu
 * @version 创建时间：2019年10月21日 下午6:30:08
 */
@Controller
@RequestMapping("/quiz/match")
public class QuizMatchInfoController extends BaseController {

	private transient final Logger logger = LoggerFactory.getLogger(getClass());
	private transient final Logger match_loggger = LoggerFactory.getLogger("match");
	private static String loggerPre = "竞赛赛事相关_";

	@Autowired
	private CatchPlayDefineUtil playDefineUtil;
	@Autowired
	private QuizLeagueServiceClient leagueServiceClient;
	@Autowired
	private QuizMatchGameServiceClient matchGameServiceClient;
	@Autowired
	private QuizMatchInfoManager quizMatchInfoManager;
	@Autowired
	private QuizMatchServiceClient quizMatchServiceClient;

	@Autowired
	private RedisClient redisClient;

	@RequestMapping(value = "/indexQuizMatch", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "首页赛竞猜事信息", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "首页竞猜赛事信息", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<DataPage<QuizMatchResponse>> indexQuizMatch(QuizMatchInfoRequest matchInfoRequest, HttpServletRequest request){
		//暂时不需要用户登录
		long start = System.currentTimeMillis();
		DataPage<QuizMatchResponse> quizMatchResponseDataPage = null;
		try {
			DataPage dataPage = new DataPage();
			if (matchInfoRequest.getPageNo() !=null){
				dataPage.setPageNo(matchInfoRequest.getPageNo());
			}
			if (matchInfoRequest.getPageSize() != null){
				dataPage.setPageSize(matchInfoRequest.getPageSize());
			}else {
				dataPage.setPageSize(10);
			}

			String defaultStartTime = DateUtil.getDateString2(DateUtil.getCurrBeforeOrAferDays(-3));
			String defaultEndTime = DateUtil.getDateString2(DateUtil.getCurrBeforeOrAferDays(7));
			if (StringUtils.isBlank(matchInfoRequest.getStartTime())){
				matchInfoRequest.setStartTime(defaultStartTime);
			}
			if (StringUtils.isBlank(matchInfoRequest.getEndTime())){
				matchInfoRequest.setEndTime(defaultEndTime);
			}

			Long aLong = DateUtil.calcSubDay(matchInfoRequest.getStartTime(), matchInfoRequest.getEndTime());
			if (aLong > 16){
				matchInfoRequest.setStartTime(defaultStartTime);
				matchInfoRequest.setEndTime(defaultEndTime);
			}

			UserConsumer userConsumer = getLoginUsr(request);
			quizMatchResponseDataPage = quizMatchInfoManager.indexQuizMatch(matchInfoRequest, request, dataPage, userConsumer);
			long end = System.currentTimeMillis();
			match_loggger.info("首页竞猜所用毫秒数：{}",end-start);
		}catch (Exception e){
			logger.info("查询首页赛竞猜事信息异常,参数：{}", JSON.toJSONString(matchInfoRequest),e);
			return CommonResponse.withErrorResp("服务繁忙，努力加载中...");
		}
		return CommonResponse.withSuccessResp(quizMatchResponseDataPage);
	}


	@RequestMapping(value = "/leagueList", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "首页联赛", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "首页联赛信息", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<QuizLeagueResponse>> queryLeague(BaseRequest baseRequest) {
		/*联赛信息*/
		List<QuizLeagueResponse> responseList = new ArrayList<>();
		QuizLeagueQueryVo quizLeagueQueryVo = new QuizLeagueQueryVo();
		quizLeagueQueryVo.setStatus(QuizLeagueStatus.VALID.getIndex());

		quizLeagueQueryVo.setMatchBeginStartTime(DateUtil.getTodayStartTime());
		quizLeagueQueryVo.setMatchBeginEndTime(DateUtil.getDateAfterByZdDateAndDays(DateUtil.getTodayStartTime(),15));
		quizLeagueQueryVo.setMatchSuspended(QuizMatchSuspended.NO.getIndex());
		quizLeagueQueryVo.setMatchVisible(QuizMatchVisible.SHOW.getIndex());
		quizLeagueQueryVo.setMatchDeadlineStartTime(new Date());
		quizLeagueQueryVo.setMatchStatus(QuizMatchStatus.NOTSTART.getIndex());

		quizLeagueQueryVo.setGameSuspended(QuizMatchSuspended.NO.getIndex());
		quizLeagueQueryVo.setGameVisible(QuizMatchVisible.SHOW.getIndex());
		quizLeagueQueryVo.setGameStatus(QuizMatchStatus.NOTSTART.getIndex());

		try {
			List<QuizLeague> resultList = redisClient.getObj(QuizConstants.INDEX_QUIZ_LEAGUE_CACHEKEY);
			if (CollectionUtils.isEmpty(resultList)){
				logger.info("{}首页league缓存失效",logger);
				ModelResult<List<QuizLeague>> listModelResult = leagueServiceClient.indexQueryList(quizLeagueQueryVo);
				List<QuizLeague> sourceList = listModelResult.getModel();
				if (!listModelResult.isSuccess() || CollectionUtils.isEmpty(sourceList)){
					logger.error("{}查询联赛数据为空或异常{}",loggerPre,listModelResult.getErrorMsg());
				}
				//排序 按照开始时间升序
				resultList = sourceList.stream().sorted(Comparator.comparing(QuizLeague::getCreateTime)).collect(Collectors.toList());
				redisClient.setObj(QuizConstants.INDEX_QUIZ_LEAGUE_CACHEKEY,60*30,resultList);
			}
			for (QuizLeague quizLeague : resultList) {
				QuizLeagueResponse quizLeagueResponse = new QuizLeagueResponse();
				BeanUtil.copyProperties(quizLeague,quizLeagueResponse);
				quizLeagueResponse.setLeagueId(quizLeague.getId());
				responseList.add(quizLeagueResponse);
			}
		}catch (Exception e){
			logger.info("查询首页联赛信息异常",e);
			return CommonResponse.withErrorResp("服务繁忙，努力加载中...");
		}
		return CommonResponse.withSuccessResp(responseList);
	}


	@RequestMapping(value = "/updateSp", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "根据对局id更新赔率信息", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "根据对局id更新赔率信息", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<QuizMatchGameResponse>> updateMatchGameSp(String matchGameIds) {
		/*联赛信息*/
		List<QuizMatchGameResponse> responseList = new ArrayList<>();
		try {
			if (StringUtils.isBlank(matchGameIds)){
				logger.warn("matchGameIds 参数不能为空");
				return CommonResponse.withErrorResp("matchGameIds 参数不能为空");
			}
			Iterable<String> iterable = Splitter.on(",").trimResults().omitEmptyStrings().split(matchGameIds);

			List<Long> list = Lists.newArrayList();
			for (String matchId : iterable) {
				list.add(Long.valueOf(matchId));
			}
			//if(",".equals(matchGameIds.substring(matchGameIds.length()-1))){
			//	matchGameIds = matchGameIds.substring(0,matchGameIds.length()-1);
			//}
			//List<Long> list = Arrays.stream(matchGameIds.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
			ModelResult<List<QuizMatchGame>> listModelResult = matchGameServiceClient.listCacheByIds(list);
			if (!listModelResult.isSuccess() || CollectionUtil.isEmpty(listModelResult.getModel())){
				logger.info("gameList 对局详情查询结果为空，查询条件：{}", JSON.toJSONString(matchGameIds));
				return CommonResponse.withErrorResp("查询结果为空");
			}
			List<QuizMatchGame> matchGames = listModelResult.getModel();
			if (CollectionUtils.isNotEmpty(matchGames)){
				Map<Integer, QuizPlayDefine> playDefineMap = playDefineUtil.getPlayNoMap();
				for (QuizMatchGame matchGame : matchGames) {
					QuizMatch cacheQuizMatch = quizMatchInfoManager.getCacheQuizMatch(matchGame.getMatchId().longValue());
					QuizMatchGameResponse matchGameResponse = quizMatchInfoManager.getMatchGameResponse(matchGame, null, playDefineMap,cacheQuizMatch);
					if (matchGameResponse != null){
						responseList.add(matchGameResponse);
					}
				}
			}

		}catch (Exception e){
			logger.error("根据对局id更新赔率信息异常，参数：{}", JSON.toJSONString(matchGameIds),e);
			return CommonResponse.withErrorResp("更新赔率信息异常");
		}
		return CommonResponse.withSuccessResp(responseList);
	}

	@RequestMapping(value = "/gameList", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "首页更多竞猜及主题竞猜", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "首页主题竞猜", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<List<QuizMatchGameResponse>>> queryMatchGame(QuizMatchInfoRequest quizMatchInfoRequest, BaseRequest baseRequest, HttpServletRequest request) {
		long start = System.currentTimeMillis();
		if (quizMatchInfoRequest.getRecommend() == null && quizMatchInfoRequest.getMatchId() == null){
			return CommonResponse.withErrorResp("必要参数为空");
		}
		QuizMatchGameQueryVo quizMatchGameQueryVo = new QuizMatchGameQueryVo();
		//更多竞猜--根据赛事id查询每局信息
		if (quizMatchInfoRequest.getMatchId() != null){
			quizMatchGameQueryVo.setMatchId(quizMatchInfoRequest.getMatchId().intValue());
			if (quizMatchInfoRequest.getFinished() != null && quizMatchInfoRequest.getFinished()){
				//查询完结(赛事状态：/完场/异常/取消 || 奖池状态：已开奖/取消)的赛事
				quizMatchGameQueryVo.setAwardStatusList(Lists.newArrayList(QuizMatchGameAwardStatus.HAS_START.getIndex(),
						QuizMatchGameAwardStatus.OFF.getIndex()));
				quizMatchGameQueryVo.setStatuss(Lists.newArrayList(QuizMatchStatus.OVER.getIndex(),
						QuizMatchStatus.OFF.getIndex(),QuizMatchStatus.EXCEPTION.getIndex()));
				// 条件 或
				quizMatchGameQueryVo.setMatchStatusOrAwardStatus(Boolean.TRUE);
			}else {
				//待开奖已完场(待开奖 && 未开赛/赛中) 的赛事
				quizMatchGameQueryVo.setAwardStatusList(Lists.newArrayList(QuizMatchGameAwardStatus.WAIT.getIndex()));
				quizMatchGameQueryVo.setStatuss(Lists.newArrayList(QuizMatchStatus.NOTSTART.getIndex(), QuizMatchStatus.GOING.getIndex()));
			}
		}else{
			//不是更多竞猜，主题竞猜，只查推荐内容
			quizMatchGameQueryVo.setRecommend(QuizMatchRecommend.YES.getIndex());
			quizMatchGameQueryVo.setBeginStartTime(DateUtil.getCurrBeforeOrAferDays(-3));
			quizMatchGameQueryVo.setBeginEndTime(DateUtil.getCurrBeforeOrAferDays(7));
		}
		UserConsumer userConsumer = getLoginUsr(request);
		List<List<QuizMatchGameResponse>> resultList = quizMatchInfoManager.gameList(quizMatchGameQueryVo, userConsumer);

		if (CollectionUtils.isEmpty(resultList)){
			CommonResponse.withErrorResp("当前访问人数过多，稍微休息下");
		}
		long end = System.currentTimeMillis();
		logger.info("gameList 所用毫秒数：{}",end - start);
		return CommonResponse.withSuccessResp(resultList) ;
	}

	@RequestMapping(value = "/videoGames", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "游戏类别", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "游戏类别", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<Object>> videoGames(BaseRequest baseRequest, HttpServletRequest request) {
		//暂时不需要用户登录
		List<Object> videoGames = quizMatchInfoManager.getVideoGames(baseRequest.getClientType(),baseRequest.getAgentId());
		return CommonResponse.withSuccessResp(videoGames);

	}

	@RequestMapping(value = "/recommendGameList", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
//	@ApiOperation(value = "推荐的竞猜玩法", httpMethod = "GET", produces = "application/json")
	@ApiOperation(value = "推荐的竞猜玩法", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "推荐的竞猜玩法", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<List<QuizMatchResponse>> queryRecommendMatchGame(BaseRequest baseRequest, HttpServletRequest request) {
		UserConsumer userConsumer = getLoginUsr(request);
		logger.info("推荐的竞猜玩法,请求参数:baseRequest={}", JSON.toJSONString(baseRequest));
		List<QuizMatchResponse> resultList = quizMatchInfoManager.queryRecommendMatchGame(baseRequest, userConsumer);
		return CommonResponse.withSuccessResp(resultList) ;
	}



	@RequestMapping(value = "/queryQuizMatch", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
	@ApiOperation(value = "详情页竞猜事信息", httpMethod = "POST", consumes = "application/x-www-form-urlencoded", produces = "application/json")
	@ApiResponse(code = 200, message = "详情页竞猜赛事信息", response = CommonResponse.class)
	@ResponseBody
	public CommonResponse<QuizMatchResponse> queryQuizMatch(Long matchId, HttpServletRequest request){

		QuizMatchResponse  quizMatchResponse = new QuizMatchResponse();
		logger.info("参数matchId：{}", matchId);
		try {
			if (matchId==null){
				return CommonResponse.withErrorResp("赛事id为空");
			}
			QuizMatch quizMatch;
			quizMatch = redisClient.getObj(QuizConstants.QUIZ_MATCH_ID_CACHEKEY+matchId);
			if (quizMatch==null) {
				ModelResult<QuizMatch> quizMatchModelResult = quizMatchServiceClient.queryCacheById(matchId);
				if (quizMatchModelResult.isSuccess() && quizMatchModelResult.getModel() != null) {
					quizMatch = quizMatchModelResult.getModel();
				}
			}
			if (quizMatch!=null){
				BeanUtils.copyProperties(quizMatch, quizMatchResponse);

				quizMatchResponse.setMatchStatus(quizMatch.getStatus());
				quizMatchResponse.setMatchId(quizMatch.getId());
				if (!StringUtils.isBlank(quizMatch.getScore()) && !quizMatch.getScore().contains("null")) {
					String[] split = quizMatch.getScore().split("-");
					if (NumberUtil.isNumber(split[0])) {
						quizMatchResponse.setHomeScore(Integer.valueOf(split[0]));
						quizMatchResponse.setAwayScore(Integer.valueOf(split[1]));
					}
				}
				//直播视频地址
				if (StringUtils.isNotBlank( quizMatch.getLiveUrl() )){
					String[] split = quizMatch.getLiveUrl().split( "\\|\\|" );
					List<String> list = Lists.newArrayList( split );
					quizMatchResponse.setLiveVideoUrlList( list );
				}
			}
		}catch (Exception e){
			logger.info("查询竞猜赛事信息异常,参数matchId：{}", matchId,e);
			return CommonResponse.withErrorResp("查询竞猜赛事信息异常");
		}
		return CommonResponse.withSuccessResp(quizMatchResponse);
	}


}
