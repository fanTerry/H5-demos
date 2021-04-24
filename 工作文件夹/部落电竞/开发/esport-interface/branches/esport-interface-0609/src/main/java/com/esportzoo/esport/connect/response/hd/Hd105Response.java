package com.esportzoo.esport.connect.response.hd;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author lixisheng
 * @Date 2020/3/30 15:13
 */
@Data
public class Hd105Response implements Serializable {


    private static final long serialVersionUID = -5034250232323279611L;

    /**中奖总金额 */
    private BigDecimal awardTtotal;
    /**用户id */
    private Long userId;

    private String nickName;

    private String userIcon;

    private Integer rank;

    private Hd105GiftResponse hd105GiftResponse;
}
