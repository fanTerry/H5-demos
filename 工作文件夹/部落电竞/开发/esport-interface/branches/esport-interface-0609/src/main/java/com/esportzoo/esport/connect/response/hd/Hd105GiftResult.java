package com.esportzoo.esport.connect.response.hd;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author lixisheng
 * @Date 2020/4/5 14:50
 */
@Data
public class Hd105GiftResult implements Serializable {


    private static final long serialVersionUID = 4928106983764222149L;
    /** 活动ID*/
    private Long hdId;
    /** 活动状态 0：无效，1：有效，2：暂停  ,含义见HdEnable */
    private Integer status;
    /**结束时间*/
    private Date endTime;
    /** 前几名可以获奖 */
    private Integer winTopNum;
    private List<Hd105GiftResponse>  hd105GiftResponses;


}
