package com.esportzoo.esport.connect.response.hd;

import com.esportzoo.esport.hd.constants.HdUserGiftStatus;
import com.esportzoo.esport.hd.constants.ShowType;
import com.esportzoo.esport.hd.entity.HdUserGift;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ClassName ShowUserGiftResponse
 * @Description  答题首页广播信息
 * @Author jiajing.he
 * @Date 2019/9/23  16:52
 * @Version 1.0
 **/
@Data
public class AnnounceResponse implements Serializable {
    private static final long serialVersionUID = -645875426850527536L;

    /** 礼品名称   格式：5元红包，充20送10元优惠卡 */
    private String giftName;

    /** 礼品金额 */
    private BigDecimal amount;

    /** 状态 {@link HdUserGiftStatus}  0:未使用 1:已使用 2:已失效 3:使用中  9:使用异常 10无效 */
    private Integer status;
    /**
     * 用户头像
     */
    private String icon;


    /** 礼品类型 1：红包，2：充值优惠卡 */
    private Integer giftType;
    /**
     * 用户名
     */
    private String userName;

    /**
     * 答对题目数量
     */
    private Integer num;

    /**
     * 获取奖品时间
     */
    private Date createTime;

    /**
     * 奖品的其它属性
     */
   	private String giftProp;

    /**
     * {@link ShowType} 展示数据类型
     */
    private Integer showType;

}
