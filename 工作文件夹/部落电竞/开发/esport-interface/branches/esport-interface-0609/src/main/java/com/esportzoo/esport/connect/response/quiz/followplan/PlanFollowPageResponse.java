package com.esportzoo.esport.connect.response.quiz.followplan;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author lixisheng
 * @Date 2020/6/8 09:46
 */
@Data
public class PlanFollowPageResponse implements Serializable {

    private static final long serialVersionUID = -5537620387054531319L;

    /**跟投昵称 */
    private String followUserNickName;

    /**跟投用户ID*/
    private Long followUserId;

    /** 跟单金额 */
    private BigDecimal followCost;

    /**跟投订单中奖金额 */
    private BigDecimal winAward;
    /**
     *用户中奖分成
     */
    private BigDecimal winIncomeUser;
    /**
     *系统中奖分成
     */
    private BigDecimal winIncomeSys;

}
