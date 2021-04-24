package com.esportzoo.esport.connect.response.quiz.followplan;

import com.esportzoo.quiz.constants.QuizOption;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author lixisheng
 * @Date 2020/6/10 18:03
 */
@Data
public class RecommendMatchResponse implements Serializable {

    private static final long serialVersionUID = -7160202817649788813L;

    /**赛事id */
    private Integer matchId;
    /**场次数（0表示全场，1第一场...，彩易科思赛事编号最后一位）*/
    private Integer gameNumber;
    /** 赛制 BO几 */
    private Integer gameNumbers;
    /**主队id */
    private Integer homeTeamId;
    /**主队名称 */
    private String homeTeamName;
    /**主队logo */
    private String homeTeamLogo;
    /**客队id */
    private Integer awayTeamId;
    /**客队名称 */
    private String awayTeamName;
    /** 表示该场比赛是否会开滚球，等于1、3或者等于0，代表不会开滚球；如果grounder等于2，代表会开滚球*/
    private Integer grounder;
    /**客队logo  */
    private String awayTeamLogo;
    /**玩法编号 */
    private Integer playNo;
    /**玩法名称 */
    private String playName;
    /** 开赛时间 */
    private Date startTime;
    /**赛事场次状态 0：未开赛 1：赛中 2：完场 3：异常 4：取消 {@link QuizMatchStatus}*/
    private Integer matchStatus;
    /** 奖池状态（彩易科思state），0表示待开奖，1表示已开奖，-1表示取消 {@link QuizMatchGameAwardStatus} */
    private Integer awardStatus;
    /** 比赛结果（彩易科思Res）,1表示该玩法投注选项主队赢得结果，或为大分或为奇数，2代表该玩法投注选项客队赢得结果或为小分或为偶数,R 为流局*/
    private String awardResult;
    private Integer videogameId;
    private String leagueName;
    private String homeTeamNameAbbr;
    private String awayTeamNameAbbr;
    private String subjectName;
    private List<QuizOption> quizOptions;
}
