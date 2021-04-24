package com.esportzoo.esport.connect.response.quiz.followplan;

import com.esportzoo.esport.constants.quiz.follow.RecommendStatus;
import com.esportzoo.esport.vo.quiz.QuizRecommendUserTenwinInfoVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description: 推荐大神用户相关信心
 *
 * @author: Haitao.Li
 *
 * @create: 2020-06-04 18:00
 **/
@Data
public class PlanRecommendResponse {

	/**
	 *方案ID
	 */
	private Long planId;

	/**
	 *方案编号
	 */
	private String planNo;

	/**
	 *用户ID
	 */
	private Long userId;

	/**
	 *用户账号
	 */
	private String account;

	/**
	 *游戏id
	 */
	private Integer videoGameId;

	/**
	 * 赛事id
	 */
	private Long matchId;

	/**
	 * 赛事编号 12位
	 */
	private String matchNo;

	/**
	 *方案推荐类型，1：默认推荐单 {@link RecommendType}
	 */
	private Integer recommendType;

	/**
	 *平台服务费率  例如 1  那就是 1%
	 */
	private Integer feeRate;
	/**
	 *发单人提成比例 例如 1  那就是 1%
	 */
	private Integer commissionRate;

	/**
	 *推荐单金额
	 */
	private BigDecimal recommendAmount;

	/**
	 *推荐结束时间（等于方案截止时间减去固定的推荐时间间隔）
	 */
	private Date recommendEndTime;

	/**
	 *置顶推荐位置，1：热门推荐  {@link RecommendPlace}
	 */
	private Integer recommendPlace;

	/**
	 *置顶推荐排序
	 */
	private Integer recommendSortNum;

	/**
	 *方案描述
	 */
	private String recommendDesc;

	/**
	 *跟投人数
	 */
	private Integer followCount;

	/**
	 *跟投总金额
	 */
	private BigDecimal followAmount;

	/**
	 *跟投中奖总金额
	 */
	private BigDecimal followWinAward;

	/**
	 *跟投分成总金额
	 */
	private BigDecimal followWinIncome;

	/**
	 *预计奖金
	 */
	private BigDecimal expectedAward;

	/**
	 *预计回报率
	 */
	private BigDecimal expectedReturnRate;

	/**
	 *渠道编号
	 */
	private Integer channelNo;

	/**
	 *客户端类型
	 */
	private Integer clientType;

	/**
	 *随机码
	 */
	private String inviteCode;

	/**
	 *推荐单状态，1：已推荐，2：取消推荐 {@link RecommendStatus}
	 */
	private Integer status;

	/**
	 *用户近10场战绩相关数据
	 */
	private QuizRecommendUserTenwinInfoVo tenwinInfo;
}
