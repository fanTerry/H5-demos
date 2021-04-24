package com.esportzoo.esport.manager;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.redisclient.RedisClient;
import com.esportzoo.esport.client.service.cms.UserFollowServiceClient;
import com.esportzoo.esport.client.service.common.SysConfigPropertyServiceClient;
import com.esportzoo.esport.connect.request.MatchRequest;
import com.esportzoo.esport.connect.response.CommonResponse;
import com.esportzoo.esport.connect.response.league.*;
import com.esportzoo.esport.constants.SysConfigPropertyKey;
import com.esportzoo.esport.constants.VideoGame;
import com.esportzoo.esport.constants.cms.FollowObjectType;
import com.esportzoo.esport.constants.cms.FollowStatus;
import com.esportzoo.esport.domain.SysConfigProperty;
import com.esportzoo.esport.domain.UserFollow;
import com.esportzoo.esport.util.Application;
import com.esportzoo.esport.vo.cms.UserFollowQueryVo;
import com.esportzoo.leaguelib.client.service.postmatch.GameServiceClient;
import com.esportzoo.leaguelib.client.service.postmatch.LeagueServiceClient;
import com.esportzoo.leaguelib.client.service.prematch.MatchServiceClient;
import com.esportzoo.leaguelib.common.constants.MatchStatus;
import com.esportzoo.leaguelib.common.domain.*;
import com.esportzoo.leaguelib.common.result.MatchDataInfoResul;
import com.esportzoo.leaguelib.common.result.MatchDataResult;
import com.esportzoo.leaguelib.common.result.MatchResult;
import com.esportzoo.leaguelib.common.service.postmatch.SerieService;
import com.esportzoo.leaguelib.common.service.postmatch.TeamService;
import com.esportzoo.leaguelib.common.service.postmatch.TournamentService;
import com.esportzoo.leaguelib.common.vo.postmatch.*;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author haitao.li
 *获取赛事相关数据
 */
@Component
public class LeagueManager {

	private transient static final Logger logger = LoggerFactory.getLogger(LeagueManager.class);
	public static final String LOGGER_PREFIX = "查询赛事首页数据-";
	public static final String MATCH_LIST_CATCH = "matchListByLeagueId_";
	public static final String TEAM_LIST_CATCH = "teamListByLeagueId_";

	private static List<VideoGame> videoGames = VideoGame.getAllList();

	@Autowired
	MatchServiceClient matchServiceClient;
	@Autowired
	GameServiceClient gameServiceClient;
	@Autowired
	LeagueServiceClient leagueServiceClient;
	@Autowired
	private UserFollowServiceClient userFollowServiceClient;
	@Value("${expert.res.domain}")
	private String resDomain;
	@Autowired
	private CommonManager commonManager;

	@Autowired
	private Application application;
	@Autowired
	@Qualifier("serieServiceClient")
	private SerieService serieService;
	@Autowired
	@Qualifier("tournamentServiceClient")
	private TournamentService tournamentService;
	@Autowired
	@Qualifier("teamServiceClient")
	private TeamService teamService;
	@Autowired
	private SysConfigPropertyServiceClient sysConfigPropertyServiceClient;
	@Autowired
	private RedisClient redisClient;

	public static final int BOTTOM_LOAD_RANGE = 7;
	public static final int PULL_LOAD_RANGE = -7;

	/**
	 * @Description: 获取赛事首页数据
	 * @param matchDetailRequest
	 */
	public CommonResponse<JSONObject> getLeagueIndex(MatchRequest matchDetailRequest) {
		JSONObject jsonObject = new JSONObject();
		/*临时处理*/
		/*if (matchDetailRequest.getLoadType()==1 || matchDetailRequest.getLoadType()==0){
			jsonObject.put("matchResultList", Lists.newArrayList());
			return CommonResponse.withSuccessResp(jsonObject);
		}*/
		try {
			if (matchDetailRequest.getLeagueId() == null) {

			}
			if (matchDetailRequest.getVideogameId() == null) {
				/*logger.error(LOGGER_PREFIX + "游戏类型ID为空");
				return CommonResponse.withErrorResp(LOGGER_PREFIX + "游戏类型ID为空");*/
			}
			if (matchDetailRequest.getStatus() == null) {
				logger.warn(LOGGER_PREFIX + "赛事状态ID为空");
			}
			/** 竞猜入口配置 */
			String value = sysConfigPropertyServiceClient
					.getValueByCondition(SysConfigPropertyKey.QUIZ_MATCH_IN_SHOW_SWITCH, matchDetailRequest.getClientType(),
							matchDetailRequest.getAgentId());
			boolean quizShow = false;
			if (StringUtils.isNotBlank(value) && value.trim().equals("1")) {
				quizShow = true;
			}
			jsonObject.put("quizShow", quizShow);

			/** 配置筛选条件 */
			configGameAndMatchStaus(matchDetailRequest, jsonObject);
			MatchVo matchVo = new MatchVo();


			//自主创建赛事的联赛ID
			String leagueNameSlug = matchDetailRequest.getLeagueNameSlug();
			if (StringUtils.isNotBlank(leagueNameSlug)) {
				List<Long> leagueIdList = getLeagueIdListByName(leagueNameSlug);
				if (CollectionUtils.isEmpty(leagueIdList)) {
					logger.info("根据【{}】查询联赛为空", leagueNameSlug);
					jsonObject.put("matchResultList", new ArrayList<>());
					return CommonResponse.withSuccessResp(jsonObject);
				}
				matchVo.setLeagueIdList(leagueIdList);
			}

			configDate(matchVo, matchDetailRequest);

			if (matchDetailRequest.getStatus() == 3) {
				/** 查询全部 */
				BeanUtils.copyProperties(matchDetailRequest, matchVo, "status");
			} else {
				BeanUtils.copyProperties(matchDetailRequest, matchVo);
			}

			/*暂时去掉游戏ID筛选*/
			//			matchVo.setVideogameId(null);
			ModelResult<List<MatchResult>> modelResult = matchServiceClient.queryMatchList(matchVo);
			if (!modelResult.isSuccess()) {
				logger.error(LOGGER_PREFIX + "查询赛事数据失败，失败原因：{}", modelResult.getErrorMsg());
				return CommonResponse.withErrorResp(modelResult.getErrorMsg());
			}
			List<MatchResult> matchResultList = modelResult.getModel();
			List<MatchResponse> matchResponseList = new ArrayList<>();
			if (CollectionUtil.isNotEmpty(matchResultList)) {
				matchResponseList = convertMatchData(matchResultList, matchDetailRequest);
			}
			jsonObject.put("liveFlag", getMatchLiveFlag(matchDetailRequest.getClientType(), matchDetailRequest.getAgentId()));
			jsonObject.put("matchResultList", matchResponseList);


		} catch (Exception e) {
			logger.error(LOGGER_PREFIX + "发生异常，信息：{}", e);
			return CommonResponse.withErrorResp(LOGGER_PREFIX + "出现异常");
		}
		return CommonResponse.withSuccessResp(jsonObject);

	}

	private List<Long> getLeagueIdListByName(String leagueNameSlug) {
		String cacheKey = "LeagueIdListByName_" + leagueNameSlug;
		List<Long> collect = redisClient.getObj(cacheKey);
		if (collect == null) {
			logger.info("自主赛事ID缓存失效，leagueNameSlug：{}", leagueNameSlug);
			collect = new ArrayList<>();
			LeagueVo leagueVo = new LeagueVo();
			leagueVo.setSlug(leagueNameSlug);
			ModelResult<List<League>> listModelResult = leagueServiceClient.queryListByCondition(leagueVo);
			List<League> model = listModelResult.getModel();
			if (CollectionUtil.isNotEmpty(model)) {
				collect = model.stream().map(l -> l.getLeagueId()).collect(Collectors.toList());
				redisClient.setObj(cacheKey, 60 * 3, collect);
			}
		}
		return collect;
	}

	/**
	 * 设置分页加载日期
	 * @param matchVo
	 * @param matchDetailRequest
	 */
	public void configDate(MatchVo matchVo, MatchRequest matchDetailRequest) {

		Date beginDay;
		Date endDay;
		Date toaay = DateUtil.offsetDay(new Date(), -2);
		Integer nextRange = 0;
		Integer currRange = 0;
		if (matchDetailRequest.getLoadType() == 0) {
			/** 加载当天以前的数据,只能一天一天加载 步长天数为1*/
			currRange = PULL_LOAD_RANGE * (matchDetailRequest.getPullPageNo() - 1) - 1;
			nextRange = PULL_LOAD_RANGE * matchDetailRequest.getPullPageNo();
			endDay = DateUtil.endOfDay(DateUtil.offsetDay(toaay, currRange));
			beginDay = DateUtil.beginOfDay(DateUtil.offsetDay(toaay, nextRange));

		} else if (matchDetailRequest.getLoadType() == 1) {
			/** 下一页，当天之后的数据 可一次性加载多天，LoadDayRange是步长天数*/
			currRange = matchDetailRequest.getBottomPageNo() * BOTTOM_LOAD_RANGE;
			nextRange = (matchDetailRequest.getBottomPageNo() + 1) * BOTTOM_LOAD_RANGE - 1;
			beginDay = DateUtil.beginOfDay(DateUtil.offsetDay(toaay, currRange));
			endDay = DateUtil.endOfDay(DateUtil.offsetDay(toaay, nextRange));
		} else {
			/** 刚进来加载，以第一页，取当天数据 */
			beginDay = DateUtil.beginOfDay(toaay);
			nextRange = (matchDetailRequest.getBottomPageNo() + 1) * BOTTOM_LOAD_RANGE - 1;
			endDay = DateUtil.endOfDay(DateUtil.offsetDay(toaay, nextRange));
		}

		/*临时处理*/
		//		beginDay = DateUtil.beginOfDay(DateUtil.offsetDay(toaay, -10));
		//		endDay  = DateUtil.endOfDay(DateUtil.offsetDay(toaay, 15));

		matchVo.setBeginDate(DateUtil.format(beginDay, "yyyy-MM-dd"));
		matchVo.setEndDate(DateUtil.format(endDay, "yyyy-MM-dd"));
	}


	/**
	 * 格式化赛事数据,按时间按联赛ID分组
	 * @param matchResultList
	 * @return
	 */
	public List<MatchResponse> convertMatchData(List<MatchResult> matchResultList, MatchRequest matchDetailRequest) {
		for (MatchResult matchResult : matchResultList) {
			String format = DateUtil.format(matchResult.getBeginAt(), "yyyy-MM-dd");
			matchResult.setSortMatchDate(DateUtil.parseDate(format));
		}
		/*如果用户登录，关联赛事关注状态*/
		if (matchDetailRequest.getUserId() != null) {
			UserFollowQueryVo userFollowQueryVo = new UserFollowQueryVo();
			userFollowQueryVo.setObjectType(FollowObjectType.MATCH.getIndex());
			userFollowQueryVo.setStatus(FollowStatus.FOLLOW.getIndex());
			userFollowQueryVo.setObjectIdList(matchResultList.stream().map(MatchResult::getId).collect(Collectors.toList()));
			userFollowQueryVo.setUserId(matchDetailRequest.getUserId());
			ModelResult<List<UserFollow>> followResult = userFollowServiceClient.queryListByObjectIdListAndUser(userFollowQueryVo);
			if (followResult.isSuccess()) {
				List<UserFollow> followList = followResult.getModel();
				List<Long> objectIdList = followList.stream().map(UserFollow::getObjectId).collect(Collectors.toList());
				for (MatchResult matchResult : matchResultList) {
					if (objectIdList.contains(matchResult.getId())) {
						matchResult.setFollowType(FollowStatus.FOLLOW.getIndex());
					} else {
						matchResult.setFollowType(FollowStatus.CANCEL.getIndex());
					}
				}
			} else {
				logger.error("查询用户赛事关注列表出错，错误信息：{}", followResult.getErrorMsg());
			}

		}


		/** 先按时间分组 */
		List<MatchResponse> resposeMatchList = Lists.newArrayList();
		Map<Date, List<MatchResult>> listMap = matchResultList.stream().collect(Collectors.groupingBy(MatchResult::getSortMatchDate));
		TreeMap<Date, List<MatchResult>> treeMap = MapUtil.sort(listMap);
		for (Date date : treeMap.keySet()) {

			List<MatchResult> matchResults = treeMap.get(date);
			/** 再按联赛分组 */
			Map<Long, List<MatchResult>> leagueMap = matchResults.stream().collect(Collectors.groupingBy(MatchResult::getLeagueId));
			for (Long leagueId : leagueMap.keySet()) {
				List<MatchResult> list = leagueMap.get(leagueId);
				MatchResult matchResult = list.get(0);
				MatchResponse detailResponse = new MatchResponse();
				List<Integer> videoGames = VideoGame.showVideoGame();
				if (!videoGames.contains(Integer.valueOf(matchResult.getVideogameId().intValue()))) {
					//不展示的游戏类型
					continue;
				}
				detailResponse.setGameType(matchResult.getVideogameId().intValue());
				/*设定中文日期*/
				Calendar instance = Calendar.getInstance(Locale.CHINA);
				instance.setTime(date);
				String cnWeekByCalendar = com.esportzoo.common.util.DateUtil.getCNWeekByCalendar(instance);
				detailResponse.setSortMatchDate(DateUtil.format(date, "MM月dd日") + " " + cnWeekByCalendar);
				detailResponse.setMatchResultList(list);

				/*更新联赛名*/
				if (StringUtils.isEmpty(matchResult.getLeagueName())) {
					ModelResult<League> modelResult = leagueServiceClient.queryByLeagueId(leagueId);
					detailResponse.setLegueName("暂无");
					if (modelResult.isSuccess() && modelResult.getModel() != null) {
						League league = modelResult.getModel();
						if (StringUtils.isNotBlank(league.getEsportName())) {
							detailResponse.setLegueName(league.getEsportName());
						} else {
							detailResponse.setLegueName(league.getName());
						}
					}
				} else {
					detailResponse.setLegueName(matchResult.getLeagueName());
				}

				for (MatchResult result : list) {
					result.setFormatBeginDate(DateUtil.format(result.getBeginAt(), "HH:mm"));
					result.setHomeScore(result.getHomeScore() == null ? 0 : result.getHomeScore());
					result.setAwayScore(result.getAwayScore() == null ? 0 : result.getAwayScore());
					result.setHomeTeamName(result.getHomeTeamName());
					result.setAwayTeamName(result.getAwayTeamName());
					result.setHomeTeamLogo(StringUtils.isNotBlank(result.getEsportHomeTeamLogo()) ?
							resDomain + result.getEsportHomeTeamLogo() :
							result.getHomeTeamLogo());
					result.setAwayTeamLogo(StringUtils.isNotBlank(result.getEsportAwayTeamLogo()) ?
							resDomain + result.getEsportAwayTeamLogo() :
							result.getAwayTeamLogo());
				}
				List<MatchResult> results = list.stream().sorted(Comparator.comparing((MatchResult o1) -> {
					if (o1.getStatus() == MatchStatus.RUNNING.getIndex()) {
						return -1;
					} else if (o1.getStatus() == MatchStatus.NOT_STARTED.getIndex()) {
						return 0;
					} else {
						return 1;
					}

				}).thenComparing(MatchResult::getBeginAt)).collect(Collectors.toList());

				Date beginAt = results.get(0).getBeginAt();
				detailResponse.setMatchResultList(results);
				detailResponse.setSortDate(beginAt);

				/** 直播，未开赛，结束排序 */
				/*CollUtil.sort(list, (o1, o2) -> {
					if (o1.getStatus() == MatchStatus.RUNNING.getIndex()) {
						return -1;
					} else if (o1.getStatus() == MatchStatus.NOT_STARTED.getIndex()) {
						return 0;
					} else {
						return 1;
					}

				});*/
				resposeMatchList.add(detailResponse);
			}
		}


		List<MatchResponse> matchResponseList = new ArrayList<>();

		//如果有王者的赛事 则不展示雷达赛事，如果没有 展示雷达赛事  首页已区分游戏类型和联赛，此处暂时注释
		/*Map<Integer, List<MatchResponse>> collect = resposeMatchList.stream().collect(Collectors.
				toMap(key -> key.getGameType(), value -> Lists.newArrayList(value), (List<MatchResponse> v1,List<MatchResponse> v2) ->{
					v2.addAll(v1);
					return v2;
				}));
		List<MatchResponse> responses = collect.get(Integer.valueOf(VideoGame.WZRY.getIndex()));
		if (CollectionUtil.isNotEmpty(responses)){
			matchResponseList.addAll(responses);
		}else if (matchDetailRequest.getPullPageNo() == 0){
			matchResponseList = resposeMatchList;
		}
		matchResponseList = matchResponseList.stream().sorted(Comparator.comparing(matchResponse -> matchResponse.getSortDate())).collect(Collectors.toList());*/

		matchResponseList = resposeMatchList.stream().sorted(Comparator.comparing(matchResponse -> matchResponse.getSortDate()))
				.collect(Collectors.toList());

		return matchResponseList;

	}

	/**
	 * @Description: 配置筛选参数
	 * @param matchDetailRequest
	 * @param jsonObject
	 * @Return void
	 */
	private void configGameAndMatchStaus(MatchRequest matchDetailRequest, JSONObject jsonObject) {
		List<MatchGameResponse> gameResponseList = Lists.newArrayList();
		for (VideoGame videoGame : videoGames) {
			MatchGameResponse gameResponse = new MatchGameResponse();
			gameResponse.setVideogameId(videoGame.getIndex());
			gameResponse.setVideoGameName(videoGame.getDescription());
			/*根据游戏tab筛选，暂时没有使用*/
			if (matchDetailRequest.getVideogameId() != null && matchDetailRequest.getVideogameId().equals(videoGame.getIndex())) {
				gameResponse.setSelected(true);
			} else {
				gameResponse.setSelected(false);
			}
			/** 查询当天各个游戏正在直播的赛事 */
			MatchVo vo = new MatchVo();
			vo.setStatus(MatchStatus.RUNNING.getIndex());
			vo.setVideogameId(videoGame.getIndex());
			vo.setBeginDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
			vo.setEndDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
			ModelResult<List<MatchResult>> listModelResult = null;
			try {
				listModelResult = matchServiceClient.queryMatchList(vo);
			} catch (Exception e) {
				logger.error("查询游戏赛事直播数出现异常，信息:{}", e);
				e.printStackTrace();
			}

			if (listModelResult.isSuccess() && listModelResult.getModel() != null) {
				gameResponse.setCurrLiveMatchNum(listModelResult.getModel().size());
			}
			gameResponseList.add(gameResponse);
		}
		jsonObject.put("gamesList", gameResponseList);
		List<MatchStatus> matchStatusList = MatchStatus.getAllList();
		jsonObject.put("statusList", matchStatusList);
	}

	public MatchDetailPageResponse getMatchDetailPageResponse(Long matchId, Integer clientType, Long agentId) {
		MatchDetailPageResponse response = new MatchDetailPageResponse();
		try {
			ModelResult<Match> modelResult = matchServiceClient.queryMatchByMatchId(matchId);
			if (!modelResult.isSuccess() || null == modelResult.getModel()) {
				logger.error("根据ID无法查询赛事，赛事ID={}", matchId);
				return response;
			} else {
				Match match = modelResult.getModel();
				match.setAwayTeamLogo(application.getMatchAwayTeamLogo(match));
				match.setHomeTeamLogo(application.getMatchHomeTeamLogo(match));
				BeanUtils.copyProperties(match, response);
				if (null != match.getLeagueId()) {
					ModelResult<League> leagueModelResult = leagueServiceClient.queryByLeagueId(match.getLeagueId());
					if (leagueModelResult.isSuccess() && null != leagueModelResult.getModel()) {
						response.setLeagueName(leagueModelResult.getModel().getName());
					}
				}
				response.setLiveFlag(getMatchLiveFlag(clientType, agentId));
			}
		} catch (Exception e) {
			logger.error("查询赛事详情数据异常:{}", e);
		}
		return response;
	}

	public MatchDataResult queryMatchDataByMatchId(Long matchId) {
		ModelResult<MatchDataResult> modelResult = matchServiceClient.queryMatchDataByMatchId(matchId);
		if (!modelResult.isSuccess() || null == modelResult.getModel()) {
			return null;
		} else {
			return modelResult.getModel();
		}
	}

	public List<MatchDataInfoResul> queryMatchResultInfoByMatchId(Long matchId) {
		ModelResult<List<MatchDataInfoResul>> modelResult = matchServiceClient.queryMatchResultInfoByMatchId(matchId);
		if (!modelResult.isSuccess() || null == modelResult.getModel()) {
			return null;
		} else {
			return modelResult.getModel();
		}
	}

	// 根据matchId查game信息
	public Set<String> queryGameByMatchId(Long matchId) {
		GameVo gameVo = new GameVo();
		gameVo.setMatchId(matchId);
		ModelResult<List<Game>> gameResult = gameServiceClient.queryListByCondition(gameVo);
		if (!gameResult.isSuccess() || null == gameResult.getModel()) {
			return null;
		} else {
			List<String> newList = gameResult.getModel().stream().sorted(Comparator.comparing(Game::getGameId))
					.map(x -> String.valueOf(x.getGameId())).collect(Collectors.toList());
			return new HashSet<String>(newList);
		}
	}

	// 获取赛事直播开关 true:打开,false:关闭
	public Boolean getMatchLiveFlag(Integer clientType, Long agentId) {
		SysConfigProperty sysConfigProperty = commonManager.getSysConfigByKey(SysConfigPropertyKey.MATCH_LIVE_SHOW_SWITCH, clientType, agentId);
		String value = sysConfigProperty.getValue();
		Boolean liveFlag = false;
		if (StringUtils.isNotBlank(value) && value.trim().equals("1")) {// 赛事直播是关闭的
			liveFlag = true;
		}
		return liveFlag;
	}

	/**
	 * 联赛队伍及赛程展示
	 * @param matchDetailRequest
	 * @return
	 */
	public CommonResponse<List<SerieResultVo>> getLeagueDetail(MatchRequest matchDetailRequest) {
		List<SerieResultVo> serieResultVoList = getSerieResultVoList(matchDetailRequest.getLeagueId(), matchDetailRequest.getVideogameId());
		/* 根据serie获取赛程信息，可只查一个 */
		for (SerieResultVo serieResultVo : serieResultVoList) {
			try {
				TournamentVo tournamentVo = new TournamentVo();
				tournamentVo.setSerieId(serieResultVo.getSerieId().intValue());
				tournamentVo.setVideogameId(matchDetailRequest.getVideogameId().longValue());
				logger.info("-getLeagueDetail- 数据库查询tournament，参数：{}", JSON.toJSONString(tournamentVo));
				//查询tournament
				ModelResult<List<Tournament>> tournamentModelResult = tournamentService.queryListByCondition(tournamentVo);
				if (!tournamentModelResult.isSuccess() && CollectionUtil.isEmpty(tournamentModelResult.getModel())) {
					logger.info("-getLeagueDetail- 查询数据库分组错误，信息：{}，参数：{}", tournamentModelResult.getErrorMsg(), JSON.toJSONString(tournamentVo));
				}
				List<Tournament> tournamentList = tournamentModelResult.getModel();

				//查询队伍集合
				List<TeamResultVo> teamResultVoList = null;
				if (StringUtils.isNotBlank(tournamentList.get(0).getTeams())) {
					List<Long> teamIdList = Arrays.asList(tournamentList.get(0).getTeams().split("\\,")).stream().map(s -> Long.valueOf(s))
							.collect(Collectors.toList());
					teamResultVoList = getTeamResultVoList(teamIdList, serieResultVo.getSerieId());
					serieResultVo.setTeamResultVoList(teamResultVoList);
				}

				List<TournamentResultVo> tournamentResultVoList = new ArrayList<>(tournamentList.size());
				//处理TournamentResultVo
				for (Tournament tournament : tournamentList) {
					TournamentResultVo tournamentResultVo = new TournamentResultVo();
					BeanUtils.copyProperties(tournament, tournamentResultVo);
					//赛事集合
					List<Long> tournamentIdList = new ArrayList<>();
					tournamentIdList.add(tournament.getTournamentId());
					List<List<MatchResult>> matchResultList = getMatchResultList(tournamentIdList, teamResultVoList.size());

					tournamentResultVo.setMatchResultList(matchResultList);

					tournamentResultVoList.add(tournamentResultVo);
				}
				serieResultVo.setTournamentResultVoList(tournamentResultVoList);
			} catch (Exception e) {
				logger.error("-getLeagueDetail- 处理tournament数据错误，serieResultVo：{}", JSON.toJSONString(serieResultVo), e);
			}
		}
		return CommonResponse.withSuccessResp(serieResultVoList);

	}

	/**
	 * 根据联赛ID和游戏类型查询系列赛(如：夏季赛，秋季赛)
	 * @param leagueId 联赛ID
	 * @param videoGameId    游戏类型
	 * @return
	 */
	public List<SerieResultVo> getSerieResultVoList(Long leagueId, Integer videoGameId) {
		List<SerieResultVo> serieResultVoList = new ArrayList<>();
		SerieVo serieVo = new SerieVo();
		serieVo.setLeagueId(leagueId);
		serieVo.setVideogameId(videoGameId);
		serieVo.setYear(DateUtil.thisYear());
		//联赛的serie
		logger.info("-getLeagueDetail- 数据库查询serie参数：{}", JSON.toJSONString(serieVo));
		ModelResult<List<Serie>> listModelResult = serieService.queryListByCondition(serieVo);
		if (!listModelResult.isSuccess() && CollectionUtil.isEmpty(listModelResult.getModel())) {
			logger.info("-getLeagueDetail- 数据库查询Serie错误，或结果为空，参数：{}", JSON.toJSONString(serieVo));
			return null;
		}
		for (Serie serie : listModelResult.getModel()) {
			try {
				SerieResultVo serieResultVo = new SerieResultVo();
				BeanUtils.copyProperties(serie, serieResultVo);
				serieResultVoList.add(serieResultVo);
			} catch (Exception e) {
				logger.info("-getSerieResultVoList- 转换bean错误，serie:{}", e, JSON.toJSONString(serie));
			}
		}
		return serieResultVoList;
	}

	/**
	 *  跟警察战队ID集合获取战队信息
	 * @param teamIdList 战队ID
	 * @param serieId 系列赛ID，参与缓存的key
	 * @return
	 */
	public List<TeamResultVo> getTeamResultVoList(List<Long> teamIdList, Long serieId) {
		logger.info("-getLeagueDetail- 数据库查询队伍参数：{}", JSON.toJSONString(teamIdList));
		String teamKey = TEAM_LIST_CATCH.concat(serieId.toString());
		List<TeamResultVo> teamResultVoList = redisClient.getObj(teamKey);
		if (CollectionUtils.isEmpty(teamResultVoList)) {
			teamResultVoList = new ArrayList<>();
			logger.info("--queryTeamListByTeamIds-- 缓存失效");
			ModelResult<List<Team>> teamListModelResult = teamService.queryListByTeamIds(teamIdList);
			if (!teamListModelResult.isSuccess() && CollectionUtil.isEmpty(teamListModelResult.getModel())) {
				logger.info("-queryTeamListByTeamIds- 查询队伍列表错误，参数：{}", JSON.toJSONString(teamIdList));
				return null;
			}
			try {
				for (Team team : teamListModelResult.getModel()) {
					TeamResultVo teamResultVo = new TeamResultVo();
					BeanUtils.copyProperties(team, teamResultVo);
					teamResultVoList.add(teamResultVo);
				}
				if (redisClient.setObj(teamKey, 60 * 30, teamResultVoList)) {
					logger.info("-queryTeamListByTeamIds- 缓存战队列表成功,key:{}", teamKey);
				} else {
					logger.info("-queryTeamListByTeamIds- 缓存战队失败，key：{}", teamKey);
				}
			} catch (Exception e) {
				logger.info("-queryTeamListByTeamIds- 战队数据处理出错", e);
			}
		} else {
			logger.info("-queryTeamListByTeamIds- 缓存中已存在，数据长度：{}", teamResultVoList.size());
		}
		return teamResultVoList;
	}

	/**
	 * 根据锦标赛查询match，并按战队数分组
	 * @param tournamentIdList 锦标赛ID
	 * @param teamNumber    战队数
	 * @return
	 */
	private List<List<MatchResult>> getMatchResultList(List<Long> tournamentIdList, int teamNumber) {
		//比赛match列表,从缓存取
		String matchKey = MATCH_LIST_CATCH.concat(tournamentIdList.get(0).toString());
		List<List<MatchResult>> resultList = redisClient.getObj(matchKey);
		if (CollectionUtils.isEmpty(resultList)) {
			logger.info("-queryMatchListByTournamentIds- 缓存失效");
			resultList = new ArrayList<List<MatchResult>>();
			ModelResult<List<MatchResult>> matchListResult = matchServiceClient.queryMatchListByTournamentIds(tournamentIdList);
			if (!matchListResult.isSuccess() && CollectionUtil.isEmpty(matchListResult.getModel())) {
				logger.info("-queryMatchListByTournamentIds- 查询赛事列表错误，参数：{}", JSON.toJSONString(tournamentIdList));
				return null;
			}
			List<MatchResult> cacheMatchList = matchListResult.getModel();
			/* 根据战队数分组(每轮的比赛场次) 通常战队数量都是偶数*/
			if (teamNumber % 2 == 0) {
				int group = teamNumber / 2;
				logger.info("-getLeagueDetail- 战队数：{}，每轮比赛数：{}", teamNumber, group);
				resultList = fixedGrouping(cacheMatchList, group);
				if (resultList == null) {
					logger.info("赛程根据战队数分组结果为空");
				} else {
					//更新缓存
					if (redisClient.setObj(matchKey, 60 * 30, resultList)) {
						logger.info("-queryMatchListByTournamentIds- 缓存赛事列表成功,key:{}", matchKey);
					} else {
						logger.info("-queryMatchListByTournamentIds- 缓存赛事失败，key：{}", matchKey);
					}
				}
			}
		} else {
			logger.info("-queryMatchListByTournamentIds- 缓存中已存在，数据长度：{}", resultList.size());
		}
		return resultList;
	}


	/**
	 * 固定分组，
	 * @param source 要分组的数据
	 * @param n        每组n个元素
	 * @param <T>
	 * @return
	 */
	public static <T> List<List<T>> fixedGrouping(List<T> source, int n) {
		logger.info("-fixedGrouping- source长度：{}", source.size());
		if (null == source || source.size() == 0 || n <= 0) {
			return null;
		}
		List<List<T>> result = new ArrayList<List<T>>();


		int arrSize = source.size() % n == 0 ? source.size() / n : (source.size() / n) + 1;
		for (int i = 0; i < arrSize; i++) {
			List<T> sub = new ArrayList<>();
			for (int j = i * n; j <= n * (i + 1) - 1; j++) {
				if (j <= source.size() - 1) {
					sub.add(source.get(j));
				}
			}
			result.add(sub);
		}
		return result;
	}

	public List<LeagueObjectVo> getLeagueTypeList(Integer clientType, Long agentId) {
		//esport自主联赛
		String value = getLeagueTypeListStr(clientType, agentId);
		List<LeagueObjectVo> list = JSON.parseArray(value, LeagueObjectVo.class);
		return list;
	}

	public String getLeagueTypeListStr(Integer clientType, Long agentId) {
		//esport自主联赛
		SysConfigProperty sysConfigProperty = commonManager.getSysConfigByKey(SysConfigPropertyKey.MATCH_LEAGUE_TYPE_List, clientType, agentId);
		String value = sysConfigProperty.getValue();
		return value;
	}


}
