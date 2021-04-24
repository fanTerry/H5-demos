package com.esportzoo.esport.connect.response.hd;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author lixisheng
 * @Date 2020/6/19 18:58
 */
@Data
public class UserHdGiftResponse implements Serializable {

    private static final long serialVersionUID = -6208262139740049853L;

    /**是否存在中奖纪录*/
    private Boolean existWinPrize=false;
    /** 用户ID */
    private Long userId;
    /** 用户账号 */
    private String account;
    /** 活动ID */
    private Long hdId;
    /** 礼品名称  */
    private String giftName;
    /** 礼品类型 */
    private Integer giftType;
    /** 礼品金额 */
    private BigDecimal amount;
    /** 状态 {@link HdUserGiftStatus}  0:未使用 1:已使用 2:已失效 3:使用中  9:使用异常 10无效 */
    private Integer status;

}
