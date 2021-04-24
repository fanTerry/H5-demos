package com.esportzoo.esport.connect.response.quiz;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author jiajing.he
 * @date 2020/2/3 11:09
 */
@Data
public class QuizUserInfoResponse {

    /**推荐币余额*/
    private BigDecimal recScore = BigDecimal.ZERO;

    /**赠送推荐币余额*/
    private BigDecimal giftRecScore = BigDecimal.ZERO;

    /*总推荐币余额*/
    public BigDecimal ableRecScore;

    /** 用户昵称 **/
    private String nickName;

    /** 头像 **/
    private String icon;

    /**是否存在中奖纪录*/
    private Boolean existWinPrize;

    /**中奖总金额*/
    private BigDecimal winPrize;
}
