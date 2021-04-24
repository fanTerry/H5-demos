package com.esportzoo.esport.connect.response.quiz;

import com.esportzoo.quiz.constants.QuizOption;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 竞猜每场具体信息(赛事数据及赔率)
 * @author tingjun.wang
 * @date 2019/10/22 19:55下午
 */
@Data
public class QuizMatchGameResponse implements Serializable{

	private static final long serialVersionUID = -2709044501110241935L;

	private Long id;
	/**
	 *赛事id
	 */
	private Integer matchId;

	/**
	 *赛事编号（彩易科思13位）
	 */
	private String matchNo;

	/**
	 *场次数（0表示全场，1第一场...，彩易科思赛事编号最后一位）
	 */
	private Integer gameNumber;

	/**
	 * 游戏id
	 */
	private Integer videogameId;

	/**
	 *主队名称
	 */
	private String homeTeamName;
	private String homeTeamNameAbbr;

	/**
	 *主队logo
	 */
	private String homeTeamLogo;

	/**
	 *客队名称
	 */
	private String awayTeamName;
	private String awayTeamNameAbbr;

	/**
	 *客队logo
	 */
	private String awayTeamLogo;

	/**
	 *玩法编号
	 */
	private Integer playNo;
	/** 玩法排序 */
	private Integer playOrder;
	private BigDecimal playExtendNum;

	/**
	 *玩法名称
	 */
	private String playName;

	/**
	 *第三方玩法简称
	 */
	private String thirdName;

	/** 题目名称 */
	private String subjectName;

	/** 参与人数 */
	private Long joinNum;

	/** 截止时间 */
	private Date deadLine;
	/** 选项集合 */
	private List<QuizOption> quizOptions;

	/**
	 *是否暂停投注 0：否 1：是 {@link QuizMatchSuspended}
	 */
	private Integer suspended;

	/**
	 *赛事场次状态 0：未开赛 1：赛中 2：完场 3：异常 4：取消 {@link QuizMatchStatus}
	 */
	private Integer status;

	/**
	 * 奖池状态（彩易科思state），0表示待开奖，1表示已开奖，-1表示取消 {@link QuizMatchGameAwardStatus}
	 */
	private Integer awardStatus;

	/**
	 * 比赛结果（彩易科思Res）,1表示该玩法投注选项主队赢得结果，或为大分或为奇数，2代表该玩法投注选项客队赢得结果或为小分或为偶数
	 */
	private String awardResult;

	/**
	 * 派奖时间（彩易科思Tms）
	 */
	private Date awardTime;

}
