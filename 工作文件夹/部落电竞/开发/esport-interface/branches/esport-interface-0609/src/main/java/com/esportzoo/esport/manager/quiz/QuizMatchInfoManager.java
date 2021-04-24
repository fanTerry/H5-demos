package com.esportzoo.esport.manager.quiz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.common.util.RandomUtil;
import com.esportzoo.esport.client.service.common.SysConfigPropertyServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.quiz.QuizMatchInfoRequest;
import com.esportzoo.esport.connect.response.quiz.QuizMatchGameResponse;
import com.esportzoo.esport.connect.response.quiz.QuizMatchResponse;
import com.esportzoo.esport.constants.CacheKeyConstant;
import com.esportzoo.esport.constants.SysConfigPropertyKey;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.quiz.client.service.quiz.QuizMatchGameServiceClient;
import com.esportzoo.quiz.client.service.quiz.QuizMatchServiceClient;
import com.esportzoo.quiz.constants.*;
import com.esportzoo.quiz.domain.quiz.QuizMatch;
import com.esportzoo.quiz.domain.quiz.QuizMatchGame;
import com.esportzoo.quiz.domain.quiz.QuizPlayDefine;
import com.esportzoo.quiz.util.CatchPlayDefineUtil;
import com.esportzoo.quiz.util.SpUtils;
import com.esportzoo.quiz.vo.quiz.QuizMatchGameQueryVo;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 获取竞猜赛事数据(赛事&题目&赔率)
 * @author tingjun.wang
 * @date 2019/10/23 9:41上午
 */
@Component
public class QuizMatchInfoManager {


	private transient Logger logger =  LoggerFactory.getLogger(QuizMatchInfoManager.class);
	private transient final Logger match_loggger = LoggerFactory.getLogger("match");

	private static String loggerPre = "竞猜首页_";

	@Autowired
	private QuizMatchServiceClient matchServiceClient;

	@Autowired
	private QuizMatchGameServiceClient matchGameServiceClient;

	@Autowired
	private CatchPlayDefineUtil playDefineUtil;

	@Autowired
	private SysConfigPropertyServiceClient sysConfigPropertyServiceClient;

	@Autowired
	private RedisClient redisClient;


	/**
	 * 首页赛事数据
	 * @param matchInfoRequest
	 * @param request
	 * @param dataPage
	 * @param userConsumer
	 * @return
	 */
	public DataPage<QuizMatchResponse> indexQuizMatch(QuizMatchInfoRequest matchInfoRequest, HttpServletRequest request, DataPage dataPage, UserConsumer userConsumer){

		IndexQuizMatchVo indexQuizMatchVo = new IndexQuizMatchVo();
		indexQuizMatchVo.setBeginDate(DateUtil.getDate2(matchInfoRequest.getStartTime()));
		indexQuizMatchVo.setEndDate(DateUtil.getDate2(matchInfoRequest.getEndTime()));
		indexQuizMatchVo.setLeagueId(matchInfoRequest.getLeagueId());
		indexQuizMatchVo.setVideogameId(matchInfoRequest.getVideoGameId());

		if (StringUtils.isNotEmpty( matchInfoRequest.getMatchStatusList() )){
			String[] statuss = matchInfoRequest.getMatchStatusList().split( "," );
			if (statuss != null) {
				List<Integer> statusList = new ArrayList<>( statuss.length );
				for (String s : statuss) {
					statusList.add( Integer.valueOf( s ) );
				}
				indexQuizMatchVo.setStatuss( statusList );
			}
		}

		String cacheKey = QuizConstants.INDEX_QUIZ_MATCH_CACHEKEY;

		try {
			List<QuizMatchResult> sourceList = redisClient.getObj(cacheKey);
			if (CollectionUtil.isNotEmpty(sourceList)){
				match_loggger.info("index赛事取缓存：{}",sourceList.size());
				//过滤条件
				sourceList = QuizMatchResult.pageConditionFilter(sourceList,indexQuizMatchVo.getStatuss(),
						indexQuizMatchVo.getVideogameId(),indexQuizMatchVo.getLeagueId(),
						indexQuizMatchVo.getBeginDate(),indexQuizMatchVo.getEndDate());
				//分页
				dataPage.pagination(sourceList);
				match_loggger.info("index缓存中分页后的赛事数据：{}",dataPage.getDataList().size());
			}else {
				//缓存没有就调server
				dataPage = matchServiceClient.indexMatch(indexQuizMatchVo,dataPage).getPage();
			}

			if (dataPage == null){
				match_loggger.info("{}查询赛事对阵信息为空，matchInfoRequest：{}，dapage：{}",loggerPre, JSON.toJSONString(matchInfoRequest),JSON.toJSONString(dataPage));
				return dataPage;
			}
			sourceList = dataPage.getDataList();

			List<QuizMatchResponse> dataList = new ArrayList<>();
			if (CollectionUtils.isEmpty(sourceList)){
				dataPage.setDataList(dataList);
				match_loggger.info("{}查询赛事对阵信息为空，matchInfoRequest：{}，dapage：{}",loggerPre, JSON.toJSONString(matchInfoRequest),JSON.toJSONString(dataPage));
				return dataPage;
			}
			for (QuizMatchResult quizMatchResult : sourceList) {
				//组装响应的赛事数据
				QuizMatchResponse matchResponse = new QuizMatchResponse();
				BeanUtils.copyProperties(quizMatchResult,matchResponse);
				matchResponse.setMatchId(quizMatchResult.getId());
				//部分属性
				matchResponse.setMatchStatus(quizMatchResult.getStatus());
				if (!StringUtils.isBlank(quizMatchResult.getScore()) && !quizMatchResult.getScore().contains("null")){
					String[] split = quizMatchResult.getScore().split("-");
					if (NumberUtil.isNumber(split[0])){
						matchResponse.setHomeScore(Integer.valueOf(split[0]));
						matchResponse.setAwayScore(Integer.valueOf(split[1]));
					}
				}
				dataList.add(matchResponse);
			}

			//logger.info("首页dataList:{}",JSON.toJSONStringWithDateFormat(dataList,"yyyy-MM-dd HH:mm:ss.ss", SerializerFeature.UseISO8601DateFormat));
			dataPage.setDataList(dataList);
		}catch (Exception e){
			logger.error("{}查询赛事信息异常，参数：{}",loggerPre, JSON.toJSONString(matchInfoRequest),e);
		}
		return dataPage;
	}

	/**
	 * 更多玩法和首页热点竞猜
	 * @param quizMatchGameQueryVo
	 * @param userConsumer
	 * @return
	 */
	public List<List<QuizMatchGameResponse>> gameList(QuizMatchGameQueryVo quizMatchGameQueryVo, UserConsumer userConsumer){
		List<List<QuizMatchGameResponse>> resultLists = new ArrayList<>();
		try {
			//是否是热点竞猜
			boolean type = quizMatchGameQueryVo.getRecommend() != null && quizMatchGameQueryVo.getRecommend() == QuizMatchRecommend.YES.getIndex();
			Integer matchId = quizMatchGameQueryVo.getMatchId();

			String cacheKey = QuizConstants.QUIZ_MATCH_GAME_MATCHID + matchId;
			if (type){
				cacheKey = QuizConstants.QUIZ_HOT_MATCH_GAME;
			}

			List<QuizMatchGame> matchGameList = null;
			if (matchId != null){
				matchGameList = redisClient.getObj(cacheKey);
			}
			if (matchId == null || matchGameList == null){
				ModelResult<List<QuizMatchGame>> listModelResult = matchGameServiceClient.queryGameList(quizMatchGameQueryVo);
				matchGameList = listModelResult.getModel();
				redisClient.setObj(cacheKey,QuizConstants.setMinutes(RandomUtil.getRandom(3,6)),matchGameList);
			}
			//logger.info("matchGameList:{}", JSON.toJSONString(matchGameList));
			matchGameList = QuizMatchGame.conditionFilter(matchGameList,quizMatchGameQueryVo);

			if (CollectionUtil.isEmpty(matchGameList)){
				logger.info("gameList 对局详情查询结果为空，原条件：{}，查询条件：{}",JSON.toJSONString(quizMatchGameQueryVo), JSON.toJSONString(quizMatchGameQueryVo));
				return resultLists;
			}

			List<QuizMatchGameResponse> resultList = new ArrayList<>();
			Map<Integer, QuizPlayDefine> playDefineMap = playDefineUtil.getPlayNoMap();
			QuizMatch cacheQuizMatch = getCacheQuizMatch(matchId.longValue());
			for (QuizMatchGame quizMatchGame : matchGameList) {
				QuizMatchGameResponse matchGameResponse = getMatchGameResponse(quizMatchGame,userConsumer,playDefineMap,cacheQuizMatch);
				if (matchGameResponse != null){
					resultList.add(matchGameResponse);
				}
			}
			//logger.info("resultList:{}", JSON.toJSONString(resultList));
			Map<Integer, List<QuizMatchGameResponse>> map;
			if (type){
				//首页热点竞猜,根据玩法分组
				map =resultList.stream().sorted(
						Comparator.comparing(QuizMatchGameResponse::getPlayNo))
						.collect(Collectors.toMap(key -> key.getPlayNo(), value -> Lists.newArrayList(value),
								(List<QuizMatchGameResponse> l1, List<QuizMatchGameResponse> l2) -> {
									l1.addAll(l2);
									return l1;
								}));
			}else {
				//更多竞猜 根据局次分组
				map = resultList.stream().sorted(
						Comparator.comparing(QuizMatchGameResponse::getGameNumber))
						.collect(Collectors.toMap(key -> key.getGameNumber(), value -> Lists.newArrayList(value),
								(List<QuizMatchGameResponse> l1, List<QuizMatchGameResponse> l2) -> {
									l1.addAll(l2);
									return l1;
								}));
				//获取场次
				Integer gameNumbers = cacheQuizMatch.getGameNumbers();
				for (Integer i = 0; i < gameNumbers+1; i++) {
					//设置场次对应的小局玩法，防止因为空玩法导致场次排序异常
					resultLists.add(new ArrayList<>());
				}
			}
			for (Map.Entry<Integer, List<QuizMatchGameResponse>> resultEntry : map.entrySet()) {
				List<QuizMatchGameResponse> value = resultEntry.getValue();
				if (type){
					resultLists.add(value);
				}else {
					resultLists.set(resultEntry.getKey(),value);
				}
			}
		}catch (Exception e){
			logger.error("{}gameList 获取首页推荐玩法或更多精彩数据异常，参数：{}",loggerPre,JSON.toJSONString(quizMatchGameQueryVo),e);
		}
		return resultLists;
	}

	/**
	 * 获取首页游戏类别
	 * @param clientType
	 * @param agentId
	 * @return
	 */
	public List<Object> getVideoGames(Integer clientType,Long agentId){
		String configValueByKey = sysConfigPropertyServiceClient.getValueByCondition(SysConfigPropertyKey.QUIZ_MATCH_VIDEOGAME_LIST,clientType,agentId);
		List<Object> videoGames = null;
		try {
			videoGames = JSON.parseArray(configValueByKey);
		}catch (Exception e){
			logger.error("竞猜首页从配置参数中获取游戏类型列表解析异常，{}",e.getMessage());
		}
		return videoGames;
	}

	public List<QuizMatchResponse> queryRecommendMatchGame(BaseRequest baseRequest, UserConsumer userConsumer){
		List<QuizMatchResponse> quizMatchList = new ArrayList<QuizMatchResponse>();
		Integer clientType = baseRequest.getClientType();
		Long agentId = baseRequest.getAgentId();
		try {
			if (userConsumer == null) {
				return quizMatchList;
			}
			logger.info("推荐赛事弹窗：userId={},clientType={},agentId={}",userConsumer.getId(),clientType,agentId);

			String recommendCacheKey = StrUtil.format(QuizConstants.RECOMMEND_MATCH_TO_USER, userConsumer.getId());
			String recommendCache = redisClient.get(recommendCacheKey);
			if (StringUtils.isBlank(recommendCache)) {
				//当日还没弹窗过
				String configValue = sysConfigPropertyServiceClient.getValueByCondition(SysConfigPropertyKey.RECOMMEND_MATCH_GAME_LIST,clientType,agentId);
				Map<Integer, QuizPlayDefine> playDefineMap = playDefineUtil.getPlayNoMap();
				if (StringUtils.isNotBlank(configValue)) {
					//configValue 是以逗号分隔的的matchGameId
					String [] matchGameIdStr = configValue.split(",");
					for (String idStr : matchGameIdStr) {
						String cacheKey = StrUtil.format(QuizConstants.RECOMMEND_MATCH_GAME, idStr);
						QuizMatchResponse matchResponse = redisClient.getObj(cacheKey);
						if (matchResponse == null) {
							ModelResult<QuizMatchGame> modelResult = matchGameServiceClient.queryById(Long.valueOf(idStr));
							QuizMatchGame quizMatchGame = modelResult.getModel();
							//只推荐【未开赛 】和【 赛中】的 玩法
							if (modelResult.isSuccess()	&& quizMatchGame != null
									&& quizMatchGame.getStatus().intValue() < 2) {
								//赛事对阵信息
								ModelResult<QuizMatch> quizMatchModelResult = matchServiceClient.queryById(Long.valueOf(quizMatchGame.getMatchId()));
								QuizMatch quizMatch = quizMatchModelResult.getModel();
								if (quizMatchModelResult.isSuccess() &&  quizMatch != null) {
									matchResponse = new QuizMatchResponse();
									BeanUtils.copyProperties(quizMatch, matchResponse);
									matchResponse.setMatchId(quizMatch.getId());
									matchResponse.setMatchStatus(quizMatch.getStatus());
									if (!StringUtils.isBlank(quizMatch.getScore()) && !quizMatch.getScore().contains("null")){
										String[] split = quizMatch.getScore().split("-");
										if (NumberUtil.isNumber(split[0])){
											matchResponse.setHomeScore(Integer.valueOf(split[0]));
											matchResponse.setAwayScore(Integer.valueOf(split[1]));
										}
									}
									//场次玩法信息
									QuizMatchGameResponse quizMatchGameResponse = getMatchGameResponse(quizMatchGame, userConsumer,playDefineMap,quizMatch);

									matchResponse.setQuizMathGame(quizMatchGameResponse);
									redisClient.setObj(cacheKey, 600, matchResponse);//缓存10分钟
								}else {
									logger.info("推荐赛事弹窗,未找到match_id={}的赛事信息,result={}",quizMatchGame.getMatchId(),JSON.toJSONString(quizMatchModelResult));
								}
							}else {
								logger.info("推荐赛事弹窗,未找到match_game_id={}的赛事信息,result={}",idStr,JSON.toJSONString(modelResult));
							}
						}
						quizMatchList.add(matchResponse);
					}
				}
				//设置当前用户已弹窗推荐过的缓存
				if (quizMatchList.size() > 0) {
					int expireInSec = DateUtil.getIntervalSecondMidnight();
					redisClient.set(recommendCacheKey, "1", expireInSec);
				}
			}

		} catch (Exception e) {
			logger.error("clientType={},agentId={},获取推荐的场次玩法异常:", clientType, agentId, e);
		}
		return quizMatchList;
	}

	/**
	 * 数据处理QuizMatchGame -> QuizMatchGameResponse
	 * @param matchGame
	 * @param userConsumer
	 * @return
	 */
	public QuizMatchGameResponse getMatchGameResponse(QuizMatchGame matchGame,UserConsumer userConsumer,
													  Map<Integer,QuizPlayDefine> playDefineMap,Object matchObject) {
		if (matchGame == null){
			return null;
		}
		Integer playNo = matchGame.getPlayNo();
		QuizPlayDefine quizPlayDefine = playDefineMap.get(playNo);
		if (quizPlayDefine == null){
			logger.info("playDefineMap中无该玩法，playNo：{}");
			return null;
		}
		//响应的赛事详情数据
		QuizMatchGameResponse matchGameResponse = new QuizMatchGameResponse();
		BeanUtils.copyProperties(matchGame,matchGameResponse);

		matchGameResponse.setThirdName(quizPlayDefine.getThirdName());
		boolean suspended = false;
		if (matchObject instanceof QuizMatch){
			QuizMatch quizMatch = (QuizMatch) matchObject;
			matchGameResponse.setHomeTeamNameAbbr(quizMatch.getHomeTeamNameAbbr());
			matchGameResponse.setHomeTeamLogo(quizMatch.getHomeTeamLogo());
			matchGameResponse.setAwayTeamNameAbbr(quizMatch.getAwayTeamNameAbbr());
			matchGameResponse.setAwayTeamLogo(quizMatch.getAwayTeamLogo());
			matchGameResponse.setVideogameId(quizMatch.getVideogameId());
			if (quizMatch.getSuspended() != null && quizMatch.getSuspended() == QuizMatchSuspended.YES.getIndex()){
				suspended = true;
			}
		}else if (matchObject instanceof QuizMatchResponse){
			QuizMatchResponse quizMatchResponse = (QuizMatchResponse) matchObject;
			matchGameResponse.setHomeTeamNameAbbr(quizMatchResponse.getHomeTeamNameAbbr());
			matchGameResponse.setHomeTeamLogo(quizMatchResponse.getHomeTeamLogo());
			matchGameResponse.setAwayTeamNameAbbr(quizMatchResponse.getAwayTeamNameAbbr());
			matchGameResponse.setAwayTeamLogo(quizMatchResponse.getAwayTeamLogo());
			matchGameResponse.setVideogameId(quizMatchResponse.getVideogameId());
			if (quizMatchResponse.getSuspended() != null && quizMatchResponse.getSuspended() == QuizMatchSuspended.YES.getIndex()){
				suspended = true;
			}
		}
		//总场暂停 小局也暂停
		if (suspended){
			matchGameResponse.setSuspended(QuizMatchSuspended.YES.getIndex());
		}

		List<SpInfoResult> spInfoResults = matchGame.getSpInfoResults();
		if (spInfoResults == null){
			spInfoResults = SpUtils.resolverSp(matchGame.getSp());
		}
		if (CollectionUtils.isEmpty(spInfoResults)){
			logger.info("场次id:[{}],sp为空，暂停投注", matchGameResponse.getId());
			matchGameResponse.setSuspended(QuizMatchSuspended.YES.getIndex());
			matchGameResponse.setQuizOptions(new ArrayList<>());
			return matchGameResponse;
		}

		matchGameResponse.setSubjectName(SpUtils.getPlaySubject(quizPlayDefine, spInfoResults.get(0).getAttach()
				, matchGameResponse.getVideogameId(), matchGameResponse.getHomeTeamNameAbbr()));

		//解析为选项集合
		String[] optionNames = {matchGameResponse.getHomeTeamNameAbbr(),matchGameResponse.getAwayTeamNameAbbr()};
		List<QuizOption> quizOptionList = SpUtils.SpInfoToQuizOption(playNo, spInfoResults, optionNames);

		for (int i = 0; i < quizOptionList.size(); i++) {
			QuizOption quizOption = quizOptionList.get(i);

			quizOption.setResult(matchGameResponse.getAwardResult());
			//设置当前用户投注过该选项的次数
			quizOption.setQuizzedCount(0);
			if (userConsumer != null && userConsumer.getId() != null) {
				String optionkey = StrUtil.format(CacheKeyConstant.USER_MATCHGAME_OPTION_COUNT, userConsumer.getId(), matchGame.getId(), quizOption.getIndex());
				String optionCountStr = redisClient.get(optionkey);
				if (StringUtils.isNotBlank(optionCountStr) && StringUtils.isNumeric(optionCountStr)) {
					quizOption.setQuizzedCount(Integer.valueOf(optionCountStr));
				}
			}
		}
		matchGameResponse.setQuizOptions(quizOptionList);
		return matchGameResponse;
	}

	/**
	 * 获取有缓存的赛事信息
	 * @param matchId
	 * @return
	 */
	public QuizMatch getCacheQuizMatch(Long matchId){
		try {
			QuizMatch cacheQuizMatch = redisClient.getObj(QuizConstants.QUIZ_MATCH_ID_CACHEKEY+matchId);
			if (cacheQuizMatch == null) {
				ModelResult<QuizMatch> modelResult = matchServiceClient.queryCacheById(matchId);
				cacheQuizMatch = modelResult.getModel();
			}
			return cacheQuizMatch;
		}catch (Exception e){
			logger.info("获取有缓存的竞猜赛事信息异常，matchId:{}",matchId,e);
		}
		return null;
	}
}
