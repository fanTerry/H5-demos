package com.esportzoo.esport.manager.quiz;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.appmodel.domain.result.PageResult;
import com.esportzoo.common.appmodel.page.DataPage;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.common.SysConfigPropertyServiceClient;
import com.esportzoo.esport.client.service.quiz.QuizMatchGameServiceClient;
import com.esportzoo.esport.client.service.quiz.QuizMatchServiceClient;
import com.esportzoo.esport.client.service.quiz.QuizTeamServiceClient;
import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.connect.request.quiz.QuizMatchInfoRequest;
import com.esportzoo.esport.connect.response.quiz.QuizMatchGameResponse;
import com.esportzoo.esport.connect.response.quiz.QuizMatchResponse;
import com.esportzoo.esport.constants.CacheKeyConstant;
import com.esportzoo.esport.constants.SysConfigPropertyKey;
import com.esportzoo.esport.constants.quiz.*;
import com.esportzoo.esport.domain.UserConsumer;
import com.esportzoo.esport.domain.quiz.QuizMatch;
import com.esportzoo.esport.domain.quiz.QuizMatchGame;
import com.esportzoo.esport.domain.quiz.QuizPlayDefine;
import com.esportzoo.esport.domain.quiz.QuizTeam;
import com.esportzoo.esport.util.CatchPlayDefineUtil;
import com.esportzoo.esport.util.SpUtils;
import com.esportzoo.esport.vo.quiz.QuizMatchGameQueryVo;
import com.google.common.collect.Lists;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
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

	private static String loggerPre = "竞猜首页_";

	@Autowired
	private QuizMatchServiceClient matchServiceClient;

	@Autowired
	private QuizMatchGameServiceClient matchGameServiceClient;

	@Autowired
	private QuizTeamServiceClient quizTeamServiceClient;

	@Autowired
	private CatchPlayDefineUtil playDefineUtil;

	@Autowired
	private SysConfigPropertyServiceClient sysConfigPropertyServiceClient;
	
	@Autowired
	private RedisClient redisClient;
	
	/**弹窗推荐的场次玩法缓存key*/
	private static String RECOMMEND_MATCH_GAME = "recommend_match_game{}";
	/**弹窗推荐给用户的的场次玩法缓存key*/
	private static String RECOMMEND_MATCH_TO_USER = "recommend_match_to_user{}";

	/**
	 * 首页赛事数据
	 * @param matchInfoRequest
	 * @param request
	 * @param dataPage
	 * @param userConsumer
	 * @return
	 */
	public DataPage<QuizMatchResponse> indexQuizMatch(QuizMatchInfoRequest matchInfoRequest, HttpServletRequest request, DataPage dataPage, UserConsumer userConsumer){

		PageResult<QuizMatchResult> matchPageResult = null;
		IndexQuizMatchVo indexQuizMatchVo = new IndexQuizMatchVo();
		//默认未来十五天内的
		indexQuizMatchVo.setBeginDate(DateUtil.getTodayStartTime());
		indexQuizMatchVo.setEndDate(DateUtil.getDateAfterByZdDateAndDays(DateUtil.getTodayStartTime(),15));
		if (StringUtils.isNotBlank(matchInfoRequest.getStartTime())){
			indexQuizMatchVo.setBeginDate(DateUtil.stringToDate(matchInfoRequest.getStartTime()));
		}
		if (StringUtils.isNotBlank(matchInfoRequest.getEndTime())){
			indexQuizMatchVo.setEndDate(DateUtil.stringToDate(matchInfoRequest.getEndTime()));
		}
		indexQuizMatchVo.setLeagueId(matchInfoRequest.getLeagueId());
		indexQuizMatchVo.setVideogameId(matchInfoRequest.getVideoGameId());

		try {
			matchPageResult = matchServiceClient.indexMatch(indexQuizMatchVo,dataPage);
			if (matchPageResult.getPage() == null){
				logger.info("{}查询赛事对阵信息为空，matchInfoRequest：{}，dapage：{}",loggerPre, JSON.toJSONString(matchInfoRequest),JSON.toJSONString(dataPage));
				return dataPage;
			}
			BeanUtils.copyProperties(matchPageResult.getPage(),dataPage,"dataList");
			List<QuizMatchResult> sourceList = matchPageResult.getPage().getDataList();

			List<QuizMatchResponse> dataList = new ArrayList<>();
			if (CollectionUtils.isEmpty(sourceList)){
				dataPage.setDataList(dataList);
				logger.info("{}查询赛事对阵信息为空，matchInfoRequest：{}，dapage：{}",loggerPre, JSON.toJSONString(matchInfoRequest),JSON.toJSONString(dataPage));
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
				setTeamLogo(matchResponse);
				QuizMatchGame matchGame = quizMatchResult.getMatchGame();

				//无竞猜玩法不展示
				if(matchGame == null){
					logger.info("该赛事无首页竞猜：{}",JSON.toJSONString(quizMatchResult));
					continue;
				}
				//设置首页玩法(matchGame)信息
				QuizMatchGameResponse matchGameResponse = getMatchGameResponse(matchGame,false,userConsumer);

				matchResponse.setQuizMathGame(matchGameResponse);
				dataList.add(matchResponse);
			}

			dataList.sort((m1, m2) -> {
				Integer integer = DateUtil.calcCompareDate3(m1.getStartTime(), m2.getStartTime());
				if (!integer.equals(0)) {
					//时间升序
					return integer;
				}
				if (m1.getBetTimes() != null && !m1.getBetTimes().equals(m2.getBetTimes())) {
					// 投注次数降序
					return m2.getBetTimes() - m1.getBetTimes();
				}
				//对阵id升序
				return m1.getMatchId().compareTo(m2.getMatchId());
			});
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
	 * @param distinctIndex
	 * @param userConsumer
	 * @return
	 */
	public List<List<QuizMatchGameResponse>> gameList(QuizMatchGameQueryVo quizMatchGameQueryVo, Boolean distinctIndex, UserConsumer userConsumer){
		List<List<QuizMatchGameResponse>> resultLists = new ArrayList<>();
		try {
			//是否是热点竞猜
			boolean type = quizMatchGameQueryVo.getRecommend() != null && quizMatchGameQueryVo.getRecommend() == QuizMatchRecommend.YES.getIndex();

			List<QuizMatchGameResponse> resultList = new ArrayList<>();
			//更多竞猜中要排除展示的id
			Long matchGameId = quizMatchGameQueryVo.getId();
			//设为空，不影响查询
			quizMatchGameQueryVo.setId(null);
			quizMatchGameQueryVo.setSuspended(QuizMatchSuspended.NO.getIndex());
			quizMatchGameQueryVo.setStatus(QuizMatchStatus.NOTSTART.getIndex());
			quizMatchGameQueryVo.setAwardStatus(QuizMatchGameAwardStatus.WAIT.getIndex());
			ModelResult<List<QuizMatchGame>> listModelResult = matchGameServiceClient.queryCacheList(quizMatchGameQueryVo);
			List<QuizMatchGame> matchGameList = listModelResult.getModel();

			if (!listModelResult.isSuccess() || CollectionUtil.isEmpty(matchGameList)){
				logger.info("gameList 对局详情查询结果为空，原条件：{}，查询条件：{}",JSON.toJSONString(quizMatchGameQueryVo), JSON.toJSONString(quizMatchGameQueryVo));
				return resultLists;
			}

			//数据处理-matchGame集合转QuizMatchGameResponse集合,及是否排除首页显示的玩法
			matchGameResponseList(distinctIndex, matchGameId, type, resultList, matchGameList, userConsumer);

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
				QuizMatch cacheQuizMatch = getCacheQuizMatch(quizMatchGameQueryVo.getMatchId().longValue());
				Integer gameNumbers = cacheQuizMatch.getGameNumbers();
				for (Integer i = 0; i < gameNumbers+1; i++) {
					//设置场次对应的小局玩法，防止因为空玩法导致场次排序异常
					resultLists.add(new ArrayList<>());
				}
			}
			for (Map.Entry<Integer, List<QuizMatchGameResponse>> resultEntry : map.entrySet()) {
				List<QuizMatchGameResponse> value = resultEntry.getValue();
				//玩法的排序依据
				Map<Integer, Integer> playDefineSort = CatchPlayDefineUtil.getPlayDefineSort();
				//排序
				value.sort((m1,m2)->{
					//多选项个数升序
					if (m1.getQuizOptions()!=null && m2.getQuizOptions() !=null &&
							m1.getQuizOptions().size() != m2.getQuizOptions().size()){
						return m1.getQuizOptions().size() - m2.getQuizOptions().size();
					}
					if (!m1.getSuspended().equals(m2.getSuspended())){
						//暂停投注的在前面 降序
						return m2.getSuspended() - m1.getSuspended();
					}
					if(!m1.getPlayNo().equals(m2.getPlayNo())){
						//玩法编号 升序
						Integer int1 = playDefineSort.get(m1.getPlayNo());
						Integer int2 = playDefineSort.get(m2.getPlayNo());
						if (int1!=null && int2!=null){
							return playDefineSort.get(m1.getPlayNo()) - playDefineSort.get(m2.getPlayNo());
						}else {
							return int1==null?1:-1;
						}
					}
					//id升序
					return m1.getId().compareTo(m2.getId());
				});
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
	 * 获取有缓存的赛事信息
	 * @param matchId
	 * @return
	 */
	public QuizMatch getCacheQuizMatch(Long matchId){
		try {
			ModelResult<QuizMatch> modelResult = matchServiceClient.queryCacheById(matchId);
			QuizMatch cacheQuizMatch = modelResult.getModel();
			return cacheQuizMatch;
		}catch (Exception e){
			logger.info("获取有缓存的竞猜赛事信息异常，matchId:{}",matchId,e);
		}
		return null;
	}

	/**
	 * 数据处理-matchGame集合转QuizMatchGameResponse集合
	 * 	设置第三方玩法名称
	 * 	展示排除指定玩法
	 * @param flag 是否排除指定玩法
	 * @param matchGameId 指定排除的首页对局竞猜id
	 * @param type 是否是设置截止时间
	 * @param list 结果list
	 * @param matchGameList 参数list
	 * @param userConsumer  当前登陆用户，可以为空
	 */
	public void matchGameResponseList(Boolean flag, Long matchGameId, Boolean type, List<QuizMatchGameResponse> list, List<QuizMatchGame> matchGameList, UserConsumer userConsumer) {
		if (CollectionUtil.isEmpty(matchGameList)){
			return;
		}
		for (QuizMatchGame quizMatchGame : matchGameList) {
			//跳过指定玩法
			/*if (flag!=null && flag && quizMatchGame.getId() == matchGameId){
				logger.info("首页已展示，跳过该玩法:{}", JSON.toJSONString(quizMatchGame.getPlayNo()));
				continue;
			}*/
			QuizMatchGameResponse matchGameResponse = getMatchGameResponse(quizMatchGame,type,userConsumer);
			list.add(matchGameResponse);
		}
	}


	/**
	 * 数据处理QuizMatchGame -> QuizMatchGameResponse
	 * @param matchGame
	 * @param userConsumer
	 * @return
	 */
	public QuizMatchGameResponse getMatchGameResponse(QuizMatchGame matchGame,Boolean type, UserConsumer userConsumer) {

		//响应的赛事详情数据
		QuizMatchGameResponse matchGameResponse = new QuizMatchGameResponse();
		if (matchGame == null){
			return null;
		}
		BeanUtils.copyProperties(matchGame,matchGameResponse);

		//热点竞猜-设置截止时间
		QuizMatch cacheQuizMatch = getCacheQuizMatch(Long.valueOf(matchGame.getMatchId()));
		if (type){
			matchGameResponse.setDeadLine(cacheQuizMatch.getDeadlineTime());
		}else {
			//队伍简称
			setTeamNameAndLogo(matchGameResponse,cacheQuizMatch.getVideogameId());
		}

		Integer playNo = matchGame.getPlayNo();
		/*设置选项集合*/
		//获得解析的sp对象
		List<SpInfoResult> spInfoResults = SpUtils.resolverSp(matchGame.getSp());
		//解析为选项集合
		String[] optionNames = {matchGameResponse.getHomeTeamName(),matchGameResponse.getAwayTeamName()};
		List<QuizOption> quizOptionList = SpUtils.SpInfoToQuizOption(playNo, spInfoResults, optionNames);
		
		Long userId = userConsumer == null ? null : userConsumer.getId();
		//去除赔率为负数的选项
		for (int i = 0; i < quizOptionList.size(); i++) {
			QuizOption quizOption = quizOptionList.get(i);
			if (quizOption.getOdds().compareTo(BigDecimal.ZERO)<=0){
				logger.info("赔率小于等于0不展示该选项：{}", JSON.toJSONString(quizOption));
				quizOptionList.remove(quizOption);
				i--;
			}
			//设置当前用户投注过该选项的次数
			quizOption.setQuizzedCount(0);
			if (userId != null) {
				String optionkey = StrUtil.format(CacheKeyConstant.USER_MATCHGAME_OPTION_COUNT, userId, matchGame.getId(), quizOption.getIndex());
				String optionCountStr = redisClient.get(optionkey);
				if (StringUtils.isNotBlank(optionCountStr) && StringUtils.isNumeric(optionCountStr)) {
					quizOption.setQuizzedCount(Integer.valueOf(optionCountStr));
				}
			}
		}
		if (quizOptionList.size()<2){
			//可投选项小于两个的玩法，不展示
			logger.info("该玩法可投选项小于2个，不展示id：{}，quizOptionList{}",matchGameResponse.getId(), JSON.toJSONString(quizOptionList));
			return null;
		}
		matchGameResponse.setQuizOptions(quizOptionList);


		//数据库玩法类型列表
		//玩法列表
		Map<Integer, QuizPlayDefine> playNoThirdNameMap = playDefineUtil.getPlayNoMap();
		//设置第三方玩法名称
		matchGameResponse.setThirdName(playNoThirdNameMap.get(matchGameResponse.getPlayNo()).getThirdName());
		//设置前端显示的玩法简称,覆盖playName
		matchGameResponse.setPlayName(playNoThirdNameMap.get(playNo).getPlayNameAbbr());
		//设置题目，暂为玩法名+附加分值
		if (CollectionUtil.isNotEmpty(spInfoResults)){
			//题目中内容取第一个选项的附加值
			String attach = spInfoResults.get(0).getAttach();
			String subjectName = playNoThirdNameMap.get(playNo).getSubjectName();
			if (StringUtils.isNotBlank(attach)){
				subjectName = String.format(subjectName,attach);
			}
			if (subjectName.contains("主队")){
				subjectName = subjectName.replaceAll("主队", matchGameResponse.getHomeTeamName());
			}
			matchGameResponse.setSubjectName(subjectName);
		}

		return matchGameResponse;
	}

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
	
	public List<QuizMatchGameResponse> queryRecommendMatchGame(BaseRequest baseRequest, UserConsumer userConsumer){
		List<QuizMatchGameResponse> matchGameList = new ArrayList<QuizMatchGameResponse>();
		Integer clientType = baseRequest.getClientType();
		Long agentId = baseRequest.getAgentId();
		try {
			if (userConsumer == null) {
				return matchGameList;
			}
			logger.info("推荐赛事弹窗：userId={},clientType={},agentId={}",userConsumer.getId(),clientType,agentId);
			String recommendCacheKey = StrUtil.format(RECOMMEND_MATCH_TO_USER, userConsumer.getId());
			String recommendCache = redisClient.get(recommendCacheKey);
			if (StringUtils.isBlank(recommendCache)) {
				//当日还没弹窗过
				String configValue = sysConfigPropertyServiceClient.getValueByCondition(SysConfigPropertyKey.RECOMMEND_MATCH_GAME_LIST,clientType,agentId);
				if (StringUtils.isNotBlank(configValue)) {
					//configValue 是以逗号分隔的的matchGameId
					String [] matchGameIdStr = configValue.split(",");
					for (String idStr : matchGameIdStr) {
						String cacheKey = StrUtil.format(RECOMMEND_MATCH_GAME, idStr);
						QuizMatchGameResponse quizMatchGameResponse = redisClient.getObj(cacheKey);
						if (quizMatchGameResponse == null) {
							ModelResult<QuizMatchGame> modelResult = matchGameServiceClient.queryById(Long.valueOf(idStr));
							if (modelResult.isSuccess() 
									&& modelResult.getModel() != null
									&& modelResult.getModel().getStatus().intValue() < 2) {
								//只推荐【未开赛 】和【 赛中】的
								quizMatchGameResponse = getMatchGameResponse(modelResult.getModel(), Boolean.TRUE, userConsumer);
								redisClient.setObj(cacheKey, 3600, quizMatchGameResponse);//缓存1小时
							}
						}
						matchGameList.add(quizMatchGameResponse);
					}
				}
				//设置当前用户已弹窗推荐过的缓存
				if (matchGameList.size() > 0) {
					//int expireInSec = DateUtil.getIntervalSecondMidnight();
					int expireInSec = 60;//TODO 上线时删除
					redisClient.set(recommendCacheKey, "1", expireInSec);
				}
			}
			
		} catch (Exception e) {
			logger.error("clientType={},agentId={},获取推荐的场次玩法异常:", clientType, agentId, e);
		}
		return matchGameList;
	}


	/**
	 * 替换对局的队伍名称为简称及队伍logo
	 */
	private void setTeamNameAndLogo(QuizMatchGameResponse matchGameResponse,Integer videogameId){
		try {
			ModelResult<QuizTeam> homeTeamNameResult = quizTeamServiceClient.queryCatchByName(matchGameResponse.getHomeTeamName(),videogameId);
			ModelResult<QuizTeam> awayTeamNameResult= quizTeamServiceClient.queryCatchByName(matchGameResponse.getAwayTeamName(),videogameId);
			QuizTeam homeTeamName = homeTeamNameResult.getModel();
			QuizTeam awayTeamName = awayTeamNameResult.getModel();
			if (homeTeamName != null){
				matchGameResponse.setHomeTeamName(homeTeamName.getNameAbbr());
				matchGameResponse.setHomeTeamLogo(homeTeamName.getLocalImageUrl()==null?homeTeamName.getImageUrl():homeTeamName.getLocalImageUrl());
			}
			if (awayTeamName!=null){
				matchGameResponse.setAwayTeamName(awayTeamName.getNameAbbr());
				matchGameResponse.setAwayTeamLogo(awayTeamName.getLocalImageUrl()==null?awayTeamName.getImageUrl():awayTeamName.getLocalImageUrl());
			}
		}catch (Exception e){
			logger.error("替换队伍名称为简称发生异常,matchGameResponse：{}",JSON.toJSONString(matchGameResponse),e);
		}
	}

	/**
	 * 替换对阵的队伍名称为简称及队伍logo
	 */
	private void setTeamLogo(QuizMatchResponse matchResponse){
		try {
			ModelResult<QuizTeam> homeTeamNameResult = quizTeamServiceClient.queryCatchByName(matchResponse.getHomeTeamName(),matchResponse.getVideogameId());
			ModelResult<QuizTeam> awayTeamNameResult= quizTeamServiceClient.queryCatchByName(matchResponse.getAwayTeamName(),matchResponse.getVideogameId());
			QuizTeam homeTeamName = homeTeamNameResult.getModel();
			QuizTeam awayTeamName = awayTeamNameResult.getModel();
			if (homeTeamName != null){
				matchResponse.setHomeTeamLogo(homeTeamName.getLocalImageUrl()==null?homeTeamName.getImageUrl():homeTeamName.getLocalImageUrl());
			}
			if (awayTeamName!=null){
				matchResponse.setAwayTeamLogo(awayTeamName.getLocalImageUrl()==null?awayTeamName.getImageUrl():awayTeamName.getLocalImageUrl());
			}
		}catch (Exception e){
			logger.error("替换队伍名称为简称发生异常,matchGameResponse：{}",JSON.toJSONString(matchResponse),e);
		}
	}
}
