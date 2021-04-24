package com.esportzoo.esport.connect.response.expert;

import java.io.Serializable;

/**
 * @author tingting.shen
 * @date 2019/05/10
 */
public class MatchVo implements Serializable{

	private static final long serialVersionUID = 3469595185503055922L;
	
	private Long matchId;
	
	private Long homeTeamId;
	private String homeTeamName;
	private String homeTeamLogo;
	
	private Long awayTeamId;
	private String awayTeamName;
	private String awayTeamLogo;
	private String leagueName = "";
	private String statusDescription ;
	private String [] matchTime  ;
	
	
	public Long getMatchId() {
		return matchId;
	}
	public Long getHomeTeamId() {
		return homeTeamId;
	}
	public String getHomeTeamName() {
		return homeTeamName;
	}
	public String getHomeTeamLogo() {
		return homeTeamLogo;
	}
	public Long getAwayTeamId() {
		return awayTeamId;
	}
	public String getAwayTeamName() {
		return awayTeamName;
	}
	public String getAwayTeamLogo() {
		return awayTeamLogo;
	}
	public void setMatchId(Long matchId) {
		this.matchId = matchId;
	}
	public void setHomeTeamId(Long homeTeamId) {
		this.homeTeamId = homeTeamId;
	}
	public void setHomeTeamName(String homeTeamName) {
		this.homeTeamName = homeTeamName;
	}
	public void setHomeTeamLogo(String homeTeamLogo) {
		this.homeTeamLogo = homeTeamLogo;
	}
	public void setAwayTeamId(Long awayTeamId) {
		this.awayTeamId = awayTeamId;
	}
	public void setAwayTeamName(String awayTeamName) {
		this.awayTeamName = awayTeamName;
	}
	public void setAwayTeamLogo(String awayTeamLogo) {
		this.awayTeamLogo = awayTeamLogo;
	}

	public String getLeagueName() {
		return leagueName;
	}

	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public String[] getMatchTime() {
		return matchTime;
	}

	public void setMatchTime(String[] matchTime) {
		this.matchTime = matchTime;
	}
}
