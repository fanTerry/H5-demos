package com.esportzoo.esport.connect.response;

import lombok.Data;
import org.apache.xml.serialize.Serializer;

import java.io.Serializable;

/**
 * @author jiajing.he
 * @date 2019/12/6 15:49
 */
@Data
public class BindingPhoneResponse implements Serializable {
    private static final long serialVersionUID = 5227203076358041480L;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 手机号
     */
    private String phone;
}
