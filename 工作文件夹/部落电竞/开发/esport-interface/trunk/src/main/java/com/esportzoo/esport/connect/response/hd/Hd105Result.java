package com.esportzoo.esport.connect.response.hd;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author lixisheng
 * @Date 2020/3/30 18:09
 */
@Data
public class Hd105Result implements Serializable {


    private static final long serialVersionUID = 7338754110403073920L;

    /**中奖总金额 */
    private BigDecimal awardTtotal;
    /**用户id */
    private Long userId;

    private String nickName;

    private String userIcon;

    private Integer rank;

    private Hd105GiftResponse hd105GiftResponse;

    /**本周结束时间*/
    private Date endTime;

    private List<Hd105Response> winTop;

}
