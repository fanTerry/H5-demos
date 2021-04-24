package com.esportzoo.esport.connect.request.sms;

import com.esportzoo.esport.constants.sms.SendSmsEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jiajing.he
 * @date 2019/12/14 10:13
 */
@Data
public class SendALiYunSmsParam implements Serializable {

    /**
     * 手机号
     */
    private String phone;

    /**
     * 日志前缀
     */
    private String logPrefix;

    //private HttpServletRequest request;

    private SendSmsEnum sendSmsEnum;

    /**
     * 验证码
     */
    private String code;

    private String cacheCodeKey;
}
