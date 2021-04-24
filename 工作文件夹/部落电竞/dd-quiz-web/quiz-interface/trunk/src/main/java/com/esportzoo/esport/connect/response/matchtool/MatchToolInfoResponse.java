package com.esportzoo.esport.connect.response.matchtool;

import lombok.Data;

import java.io.Serializable;

/**
 * 赛事信息返回结果
 * @author jing.wu
 * @version 创建时间：2019年11月19日 上午10:29:02
 */
@Data
public class MatchToolInfoResponse implements Serializable {

	private static final long serialVersionUID = -8545812883504541612L;
	// 赛事id
	private Long matchId;
	// 赛事游戏
	private Integer gameId;
	/** 游戏名字 */
	private String gameName;
	/** 游戏的logo */
	private String gameLogo;
	/** 游戏背景图 */
	private String gameImage;
	// 赛事名称
	private String name;
	// 参赛队伍数
	private Integer teams;
	// 每队人数
	private Integer plays;
	// 赛程轮次
	private Integer rounds;
	// 轮次对应的时间 格式:YY-MM-DD hh:mm
	private String[] roundTimes;
	// 赛制：1淘汰制，2循环赛制
	private Integer matchType;
	// 报名截止时间 格式:YY-MM-DD hh:mm
	private String deadline;
	// 比赛开始时间 格式:YY-MM-DD hh:mm
	private String startTime;
	// 奖励说明
	private String rewardDesc;
	/** 赛事logo */
	private String logoUrl;
	/** 赛事背景图 */
	private String imageUrl;

}
