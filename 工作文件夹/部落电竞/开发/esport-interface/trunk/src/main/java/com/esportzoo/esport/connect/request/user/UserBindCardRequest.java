package com.esportzoo.esport.connect.request.user;

import com.esportzoo.esport.connect.request.BaseRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class UserBindCardRequest extends BaseRequest implements Serializable {

    /**真实姓名*/
    private String trueName;
    /**银行*/
    private String bankName;
    /**银行分行*/
    private String branchBank;
    /**银行卡号*/
    private String bankNo;

}
