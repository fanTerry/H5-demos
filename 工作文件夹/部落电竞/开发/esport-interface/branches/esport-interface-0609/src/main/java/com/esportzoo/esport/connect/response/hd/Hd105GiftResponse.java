package com.esportzoo.esport.connect.response.hd;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * @Author lixisheng
 * @Date 2020/3/18 18:01
 */
@Data
public class Hd105GiftResponse implements Serializable {

    private static final long serialVersionUID = 3102471987845518329L;
    /**
     * 活动ID
     */
    private Integer hdId;
    /**
     * 礼品ID
     */
    private Integer id;

    /**
     * 礼品名称
     */
    private String giftName;

    /**
     * 礼品剩余数量
     */
    private Integer giftRemainder;
    /**
     * 排行榜第几名
     */
    private Integer topNum;

    /** 礼品描述*/
    private String giftDesc;

    /**{@link HdGiftType}*/
    private Integer giftType;

    /**星星数量*/
    private Integer starNum;

    /**获奖人数*/
    private Integer winPrizeNum;

    private BigDecimal amount; // 礼品金额

    private String topRank;



}
