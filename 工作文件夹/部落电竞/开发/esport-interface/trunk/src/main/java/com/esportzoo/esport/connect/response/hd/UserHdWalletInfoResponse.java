package com.esportzoo.esport.connect.response.hd;

import com.esportzoo.esport.connect.response.BaseResponse;
import com.esportzoo.esport.domain.UserConsumer;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName UserHdWalletInfoResponse
 * @Description
 * @Author jiajing.he
 * @Date 2019/9/24  14:49
 * @Version 1.0
 **/
@Data
public class UserHdWalletInfoResponse implements Serializable {
    private static final long serialVersionUID = 3076664950167430697L;

    /**
     * 今天收入
     */
    private Double todayIncome=0.00;

    /**
     * 历史收入
     */
    private Double historyIncome=0.00;

    /**
     * 今日可提现额度
     */
    private Double todayGetMoney=0.00;

    /**
     * 用户头像
     */
    private String icon;

    /**
     * 用户昵称
     */
    private String nickName;
}
