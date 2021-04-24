package com.esportzoo.esport.connect.response.quiz.followplan;

import com.esportzoo.esport.connect.response.quiz.QuizMatchResponse;
import com.esportzoo.esport.constants.quiz.QuizPlanWinStatus;
import com.esportzoo.esport.constants.quiz.follow.RecommendStatus;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author lixisheng
 * @Date 2020/6/9 18:57
 */
@Data
public class RecommendDetailResponse implements Serializable {
    private static final long serialVersionUID = -5537620387054531319L;

    private Long id;

    /**方案ID */
    private Long planId;

    /**用户ID */
    private Long userId;

    /**方案编号 */
    private String planNo;

    /**游戏id */
    private Integer videoGameId;

    /**推荐单金额 */
    private BigDecimal recommendAmount;
    /**
     *平台服务费率  例如 1  那就是 1%
     */
    private Integer feeRate;
    /**
     *发单人提成比例 例如 1  那就是 1%
     */
    private Integer commissionRate;

    /**预计回报率 */
    private BigDecimal expectedReturnRate;

    /**跟投人数 */
    private Integer followCount;

    /** 跟投总金额 */
    private BigDecimal followAmount;

    /**
     *推荐结束时间（等于方案截止时间减去固定的推荐时间间隔）
     */
    private Date recommendEndTime;

    /**
     *推荐单状态，1：已推荐，2：取消推荐 {@link RecommendStatus}
     */
    private Integer status;

    /** 中奖状态 {@link QuizPlanWinStatus} */
    private Integer resultStatus;

    private String userIcon;
    private String userNickName;

    /** 10场战绩标示 */
    private String tenScore;
    private String tenScoreStr;
    /** 近10场回报率 */
    private BigDecimal tenReturnRate;

    private RecommendMatchResponse recommendMatchResponse;
    /** 大神选择的哪个选项 （1,2或1-2这种） */
    private String recommendOption;


}
