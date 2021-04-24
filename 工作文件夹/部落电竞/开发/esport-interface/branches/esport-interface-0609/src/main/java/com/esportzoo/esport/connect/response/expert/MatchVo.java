package com.esportzoo.esport.connect.response.expert;

import java.io.Serializable;

import lombok.Data;

/**
 * @author tingting.shen
 * @date 2019/05/10
 */
@Data
public class MatchVo implements Serializable {

	private static final long serialVersionUID = 3469595185503055922L;

	private Long matchId;

	private Long homeTeamId;
	private String homeTeamName;
	private String homeTeamLogo;

	private Long awayTeamId;
	private String awayTeamName;
	private String awayTeamLogo;
	private String leagueName = "";
	private String statusDescription;
	private String[] matchTime;
	/** 状态 0未开赛，1进行中，2已结束 */
	private Integer status;

	/**
	 * 赛制(多少场次/局)(彩易科思format)
	 */
	private Integer gameNumbers;

}
