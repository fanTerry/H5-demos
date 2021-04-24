package com.esportzoo.esport.expert.controller;

import com.alibaba.fastjson.JSON;
import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.common.util.DateUtil;
import com.esportzoo.esport.client.service.common.SysConfigPropertyServiceClient;
import com.esportzoo.esport.client.service.expert.RecExpertColumnArticleServiceClient;
import com.esportzoo.esport.client.service.expert.RecExpertColumnServiceClient;
import com.esportzoo.esport.client.service.expert.RecExpertServiceClient;
import com.esportzoo.esport.constants.ExpertStatus;
import com.esportzoo.esport.constants.SysConfigPropertyKey;
import com.esportzoo.esport.constants.VideoGame;
import com.esportzoo.esport.constants.cms.expert.ExpertColumnStatus;
import com.esportzoo.esport.domain.RecExpert;
import com.esportzoo.esport.domain.RecExpertColumn;
import com.esportzoo.esport.domain.RecExpertColumnArticle;
import com.esportzoo.esport.domain.SysConfigProperty;
import com.esportzoo.esport.expert.result.ReturnResult;
import com.esportzoo.esport.expert.vo.MatchVo;
import com.esportzoo.esport.vo.expert.ExpertPublishArticleParam;
import com.esportzoo.esport.vo.expert.ExpertReconmendBuyPoint;
import com.esportzoo.esport.vo.expert.RecExpertColumnArticleQueryVo;
import com.esportzoo.esport.vo.expert.RecExpertColumnQueryVo;
import com.esportzoo.leaguelib.client.service.postmatch.LeagueServiceClient;
import com.esportzoo.leaguelib.client.service.postmatch.SerieServiceClient;
import com.esportzoo.leaguelib.client.service.postmatch.TournamentServiceClient;
import com.esportzoo.leaguelib.client.service.prematch.MatchServiceClient;
import com.esportzoo.leaguelib.common.constants.MatchStatus;
import com.esportzoo.leaguelib.common.domain.League;
import com.esportzoo.leaguelib.common.domain.Match;
import com.esportzoo.leaguelib.common.domain.Serie;
import com.esportzoo.leaguelib.common.domain.Tournament;
import com.esportzoo.leaguelib.common.vo.postmatch.MatchQueryVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/publish")
public class PublishController extends BaseController {
	
	@Autowired
	private MatchServiceClient matchServiceClient;
	@Autowired
	private RecExpertColumnServiceClient recExpertColumnServiceClient;
	@Autowired
	private RecExpertServiceClient recExpertServiceClient; 
	@Autowired
	private RecExpertColumnArticleServiceClient recExpertColumnArticleServiceClient;
	@Autowired
	private LeagueServiceClient leagueServiceClient;
	@Autowired
	private SerieServiceClient serieServiceClient;
	@Autowired
	private TournamentServiceClient tournamentServiceClient;
	@Autowired
	private SysConfigPropertyServiceClient sysConfigPropertyServiceClient;
	

	@RequestMapping(value = "index")
	public String jczq(HttpServletRequest request, Model model){
		RecExpert expert = getLoginExpert(request);
		if (expert == null) {
			return "";
		}
		Long expertId = expert.getId();
		model.addAttribute("expertId", expertId);
		model.addAttribute("nickName", expert.getNickName());
		
		if (expert.getStatus().intValue() != ExpertStatus.VALID.getIndex()) {
			model.addAttribute("isValidExpert", false);
		} else {
			model.addAttribute("isValidExpert", true);
		}
		
		boolean canCharge = false;
		SysConfigProperty sysConfigProperty = sysConfigPropertyServiceClient.getSysConfigPropertyByKey(SysConfigPropertyKey.EXPERT_PUBLISH_ARTICLE_CAN_CHARGE_SWITCH,0,0L);
		if (sysConfigProperty!=null) {
			String value = sysConfigProperty.getValue();
			if (StringUtils.isNotBlank(value) && value.trim().equals("1")) {
				canCharge = true;
			}
		}
		model.addAttribute("canCharge", canCharge);
		model.addAttribute("gameList", VideoGame.getAllList());
		return "/publish/publish-index1";
	}
	

	@RequestMapping(value = "index1")
	public String jczq1(HttpServletRequest request, Model model) {
		RecExpert expert = getLoginExpert(request);
		if (expert == null) {
			return "";
		}
		Long expertId = expert.getId();
		model.addAttribute("expertId", expertId);
		model.addAttribute("nickName", expert.getNickName());

		if (expert.getStatus().intValue() != ExpertStatus.VALID.getIndex()) {
			model.addAttribute("isValidExpert", false);
		} else {
			model.addAttribute("isValidExpert", true);
		}

		model.addAttribute("gameList", VideoGame.getAllList());

		return "/publish/publish-index1";
	}



	@RequestMapping(value="getMatchList")
	@ResponseBody
	public ReturnResult getMatchList(Integer gameId) {
		try {
			MatchQueryVo matchQueryVo = new MatchQueryVo();
			matchQueryVo.setVideogameId(gameId);
			matchQueryVo.setStatus(MatchStatus.NOT_STARTED.getIndex());
			matchQueryVo.setBeginDate(DateUtil.addDay(-1).getTime());
			ModelResult<List<Match>> modelResult = matchServiceClient.queryMatchListByCondition(matchQueryVo);
			if (!modelResult.isSuccess()) {
				return new ReturnResult(false, modelResult.getErrorMsg());
			}
			List<Match> matchList = modelResult.getModel();
			matchList = matchList.stream().sorted((e1,e2)-> (int)(e1.getBeginAt().getTime()-e2.getBeginAt().getTime())).collect(Collectors.toList());
			return new ReturnResult(true, convert(matchList));
		} catch (Exception e) {
			logger.info("获取比赛列表，发生异常exception={}", e.getMessage(), e);
			return new ReturnResult(false, e.getMessage());
		}
 	}
	
	@RequestMapping(value="save")
	@ResponseBody
	public ReturnResult save(ExpertPublishArticleParam param, String baseFaceAnalysisStr, String buyPointsStr, Long expertId) {
		try {
			logger.info("保存专家文章，接收到的参数param={},baseFaceAnalysisStr={},buyPointsStr={}", JSON.toJSONString(param), baseFaceAnalysisStr, buyPointsStr);
			if (param.getGameId()!=null && VideoGame.needAttachMatchGameIndex().contains(param.getGameId())) {
				if (StringUtils.isBlank(param.getMatchIdList())) {
					return new ReturnResult(false, VideoGame.valueOf(param.getGameId()).getDescription() + "游戏必须关联赛事");
				}
			}
			
			RecExpert recExpert = recExpertServiceClient.queryById(expertId).getModel();
			param.setUserId(recExpert.getUserId());
			
			if (StringUtils.isNotBlank(param.getMatchIdList())) {
				String[] matchIdArr = param.getMatchIdList().split(",");
				List<Long> matchIdList = Arrays.asList(matchIdArr).stream().map(Long::parseLong).collect(Collectors.toList());
				List<Match> matchList = matchServiceClient.queryMatchListByMatchIds(matchIdList).getModel();
				for (Match match : matchList) {
					if (match.getStatus().intValue() != MatchStatus.NOT_STARTED.getIndex()) {
						logger.info("保存专家文章，选择了不是未开赛的赛事，param={}", JSON.toJSONString(param));
						return new ReturnResult(false, "您选择了进行中或已完场的赛事，请重新选择");
					}
				}
				
				RecExpertColumnArticleQueryVo queryVo = new RecExpertColumnArticleQueryVo();
				queryVo.setUserId(recExpert.getUserId());
				queryVo.setStartCreateTime(DateUtil.getTheDayZero());
				queryVo.setEndCreateTime(DateUtil.getTheDayMidnight());
				List<RecExpertColumnArticle> hasPublishedList = recExpertColumnArticleServiceClient.queryList(queryVo).getModel();
				List<String> cMatchIdList = Arrays.asList(matchIdArr);
				List<String> hMatchIdList = new ArrayList<>();
				if (hasPublishedList != null) {
					for (RecExpertColumnArticle article : hasPublishedList) {
						if (StringUtils.isNotBlank(article.getMatchIdList())) {
							hMatchIdList.addAll(Arrays.asList(article.getMatchIdList().split(",")));
						}
					}
				}
				logger.info("保存专家文章，，matchIdList={}, hMatchIdList={}", matchIdList, JSON.toJSONString(hMatchIdList));
				if (hMatchIdList.size()>0) {
					for (String matchId : cMatchIdList) {
						if (hMatchIdList.contains(matchId)) {
							String matchName = "";
							for (Match match : matchList) {
								if (match.getMatchId().longValue() == Long.parseLong(matchId)) {
									matchName = match.getHomeTeamName() + " vs " + match.getAwayTeamName();
									break;
								}
							}
							logger.info("专家发布文章，已经发布过关于此赛事的文章了，param={}, matchId={}, matchName={}", JSON.toJSONString(param), matchId, matchName);
							return new ReturnResult(false, "今天已经发布过" + matchName + "赛事的文章了");
						}
					}
				}
				
			}
			if (StringUtils.isNotBlank(baseFaceAnalysisStr)) {
				String[] sArr = baseFaceAnalysisStr.split("\\|");
				param.setBaseFaceAnalysis(Arrays.asList(sArr));
			}
			if (StringUtils.isNotBlank(buyPointsStr)) {
				List<ExpertReconmendBuyPoint> list = new ArrayList<>();
				String[] bArr = buyPointsStr.split(";");
				for (String buyPoint : bArr) {
					ExpertReconmendBuyPoint e = new ExpertReconmendBuyPoint();
					String[] eArr =  buyPoint.split("\\|");
					if (eArr.length == 2) {
						e.setPlayType(eArr[0]);
						try {
							e.setSp(new BigDecimal(eArr[1]));
						} catch (Exception e2) {
							logger.info("保存专家文章，解析赔率异常");
							e.setPlayType(buyPoint);
						}
					} else {
						e.setPlayType(buyPoint);
					}
					list.add(e);
				}
				param.setBuyPoints(list);
			}
			
			RecExpertColumnQueryVo recExpertColumnQueryVo = new RecExpertColumnQueryVo();
			recExpertColumnQueryVo.setUserId(recExpert.getUserId());
			recExpertColumnQueryVo.setStatus(ExpertColumnStatus.ENABLE.getIndex());
			ModelResult<List<RecExpertColumn>> modelResult1 = recExpertColumnServiceClient.queryList(recExpertColumnQueryVo);
			if (!modelResult1.isSuccess() || modelResult1.getModel()==null || modelResult1.getModel().size()<=0) {
				return new ReturnResult(false, "没有查询到默认专栏");
			}
			param.setColumnId(modelResult1.getModel().get(0).getId());
			
			ModelResult<Long> modelResult = recExpertColumnArticleServiceClient.expertPublishArticle(param);
			if (!modelResult.isSuccess()) {
				return new ReturnResult(false, modelResult.getErrorMsg());
			}
			return new ReturnResult(true, modelResult.getModel());
		} catch (Exception e) {
			logger.info("保存专家文章，发生异常exception={}", e.getMessage(), e);
			return new ReturnResult(false, e.getMessage());
		}
 	}
	
	
	private List<MatchVo> convert(List<Match> matchList) {
		List<MatchVo> resultList = new ArrayList<>();
		if (matchList==null || matchList.size()<=0) {
			return resultList;
		}
		for (Match match : matchList) {
			try {
				MatchVo matchVo = new MatchVo();
				BeanUtils.copyProperties(matchVo, match);
				Long leagueId = match.getLeagueId();
				if (leagueId != null) {
					ModelResult<League> modelResult1 = leagueServiceClient.queryByLeagueId(leagueId);
					if (modelResult1.isSuccess() && modelResult1.getModel()!=null) {
						League league = modelResult1.getModel();
						matchVo.setLeagueName(StringUtils.isNotBlank(league.getEsportName())?league.getEsportName():league.getName());
					}
				}
				Long serieId = match.getSerieId();
				if (serieId != null) {
					ModelResult<Serie> modelResult2 = serieServiceClient.queryBySerieId(serieId);
					if (modelResult2.isSuccess() && modelResult2.getModel()!=null) {
						Serie serie = modelResult2.getModel();
						matchVo.setSerieName(StringUtils.isNotBlank(serie.getName())?serie.getName():serie.getFullName());
					}
				}
				Long tournamentId = match.getTournamentId();
				if (tournamentId != null) {
					ModelResult<Tournament> modelResult3 = tournamentServiceClient.queryByTournamentId(tournamentId);
					if (modelResult3.isSuccess() && modelResult3.getModel()!=null) {
						Tournament tournament = modelResult3.getModel();
						matchVo.setTournamentName(StringUtils.isNotBlank(tournament.getName())?tournament.getName():tournament.getSlug());
					}
				}
				resultList.add(matchVo);
			} catch (Exception e) {
				continue;
			}
		}
		return resultList;
	}
	

}
