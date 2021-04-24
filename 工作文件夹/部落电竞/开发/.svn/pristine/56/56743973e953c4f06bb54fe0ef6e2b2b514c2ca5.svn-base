package com.esportzoo.esport.connect.request.matchtool;

import com.esportzoo.esport.connect.request.BaseRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建赛事请求参数
 * 
 * @author jing.wu
 * @version 创建时间：2019年11月19日 上午10:29:02
 */
@Data
public class MatchToolCreateRequest extends BaseRequest implements Serializable {

	private static final long serialVersionUID = -5401209033147870620L;

	// 赛事游戏
	private Integer gameId;
	// 赛事名称
	private String name;
	// 参赛队伍数
	private Integer teams;
	// 每队人数
	private Integer plays;
	// 赛程轮次
	private Integer rounds;
	// 轮次对应的时间  格式:YY-MM-DD hh:mm
	private String[] roundTimes;
	// 赛制：1淘汰制，2循环赛制
	private Integer matchType;
	// 报名截止时间 格式:YY-MM-DD hh:mm
	private String deadline;
	// 比赛开始时间 格式:YY-MM-DD hh:mm
	private String startTime;
	// 奖励说明
	private String rewardDesc;

}
