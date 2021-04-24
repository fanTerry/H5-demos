package com.esportzoo.esport.manager.match;

import java.util.ArrayList;
import java.util.List;

import com.esportzoo.quiz.client.service.quiz.QuizLeagueServiceClient;
import com.esportzoo.quiz.client.service.quiz.QuizMatchServiceClient;
import com.esportzoo.quiz.client.service.quiz.QuizTeamServiceClient;
import com.esportzoo.quiz.constants.QuizMatchStatus;
import com.esportzoo.quiz.domain.quiz.QuizLeague;
import com.esportzoo.quiz.domain.quiz.QuizMatch;
import com.esportzoo.quiz.domain.quiz.QuizTeam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import cn.hutool.core.date.DateUtil;

import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.esport.connect.response.expert.MatchVo;
import com.esportzoo.esport.util.Application;
import com.esportzoo.leaguelib.client.service.postmatch.LeagueServiceClient;
import com.esportzoo.leaguelib.client.service.prematch.MatchServiceClient;
import com.esportzoo.leaguelib.common.constants.MatchStatus;
import com.google.common.collect.Lists;

/**
 * @author tingting.shen
 * @date 2019/05/10
 */
@Component
public class MatchManager {

	private transient static final Logger logger = LoggerFactory.getLogger(MatchManager.class);

	@Autowired
	@Qualifier("matchServiceClient")
	MatchServiceClient matchServiceClient;

	@Autowired
	@Qualifier("quizMatchServiceClient")
	QuizMatchServiceClient quizMatchServiceClient;

	@Autowired
	@Qualifier("leagueServiceClient")
	LeagueServiceClient leagueServiceClient;

	@Autowired
	@Qualifier("quizLeagueServiceClient")
	QuizLeagueServiceClient quizLeagueServiceClient;

    @Autowired
    private QuizTeamServiceClient quizTeamServiceClient;

	@Autowired
	private Application application;

	public List<QuizMatch> getMatchList(List<Long> matchIds) {
//		ModelResult<List<Match>> modelResult = matchServiceClient.queryMatchListByMatchIds(matchIds);雷达数据源
		ModelResult<List<QuizMatch>> modelResult = quizMatchServiceClient.queryListByIds(matchIds);//预测数据源
		if (!modelResult.isSuccess()) {
			logger.info("根据matchIds获取比赛信息，调用接口返回错误，errMsg={}", modelResult.getErrorMsg());
			return null;
		}
		return modelResult.getModel();
	}

	public List<QuizMatch> getMatchListByMatchStr(String matchStr) {
		if (StringUtils.isBlank(matchStr)) {
			return null;
		}
		String[] ids = matchStr.split(",");
		if (ids.length <=0) {
			return null;
		}
		List<Long> idList = Lists.newArrayList();
		for (String matchId : ids) {
			if (StringUtils.isNotBlank(matchId)) {
				idList.add(Long.valueOf(matchId.trim()));
			}
		}
		return this.getMatchList(idList);
	}

	public List<MatchVo> getMatchVoListByMatchStr(String matchStr) {
		List<QuizMatch> matchList = this.getMatchListByMatchStr(matchStr);
		if (matchList==null || matchList.size()<=0) {
			return null;
		}
		List<MatchVo> matchVoList = new ArrayList<>();
		for (QuizMatch quizMatch : matchList) {
			MatchVo matchVo = new MatchVo();
			ModelResult<QuizLeague> modelResult = quizLeagueServiceClient.queryById(quizMatch.getLeagueId().longValue());//查询联赛简称
			if (modelResult.isSuccess() && modelResult.getModel() != null ) {
				QuizLeague quizLeague = modelResult.getModel();
				matchVo.setLeagueName(application.getLeagueNameAbbr(quizLeague));
			}else {
				matchVo.setLeagueName(quizMatch.getLeagueName());
			}

            matchVo.setGameNumbers(quizMatch.getGameNumbers());
			QuizMatchStatus matchStatus = QuizMatchStatus.valueOf(quizMatch.getStatus());
			if (matchStatus!=null){
				matchVo.setStatusDescription(matchStatus.getDescription());
				matchVo.setStatus(matchStatus.getIndex());
			}
			matchVo.setMatchId(quizMatch.getId());
            matchVo.setAwayTeamName(application.getQuizAwayTeamName(quizMatch));
			matchVo.setHomeTeamName(application.getQuizHomeTeamName(quizMatch));
            matchVo.setHomeTeamLogo(application.getQuizHomeTeamLogo(quizMatch));
			matchVo.setAwayTeamLogo(application.getQuizAwayTeamLogo(quizMatch));
			QuizMatch queryTeam = queryTeamId(quizMatch);
            matchVo.setHomeTeamId(queryTeam.getHomeTeamId().longValue());
            matchVo.setAwayTeamId(queryTeam.getAwayTeamId().longValue());
			String [] strings = new String[2];
			strings[0] = DateUtil.format(quizMatch.getStartTime(),"MM-dd");
			strings[1] = DateUtil.format(quizMatch.getStartTime(),"HH:mm");
			matchVo.setMatchTime(strings);
			matchVoList.add(matchVo);

		}
		return matchVoList;
	}

	private QuizMatch queryTeamId(QuizMatch quizMatch) {
		if (quizMatch==null) {
			return quizMatch;
		}
        try {
            ModelResult<QuizTeam> awayTeamResult = quizTeamServiceClient.queryCatchByName(quizMatch.getAwayTeamName(),quizMatch.getVideogameId());
            if (awayTeamResult.isSuccess() && awayTeamResult.getModel() != null ) {
                QuizTeam quizTeamAway = awayTeamResult.getModel();
                quizMatch.setAwayTeamId(quizTeamAway.getId().intValue());
            }
            ModelResult<QuizTeam> homeTeamResult = quizTeamServiceClient.queryCatchByName(quizMatch.getHomeTeamName(),quizMatch.getVideogameId());
            if (homeTeamResult.isSuccess() && homeTeamResult.getModel() != null ) {
                QuizTeam teamResultModel = homeTeamResult.getModel();
                quizMatch.setHomeTeamId(teamResultModel.getId().intValue());
            }
        } catch (Exception e) {
            logger.error("查询队伍信息异常，异常信息：{}", e);
            e.printStackTrace();
        }
		return quizMatch;
	}
	
}
