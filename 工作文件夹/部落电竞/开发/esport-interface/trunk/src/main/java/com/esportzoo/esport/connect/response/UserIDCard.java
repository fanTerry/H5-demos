package com.esportzoo.esport.connect.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author lixisheng
 * @Date 2020/5/14 11:03
 */
@Data
public class UserIDCard implements Serializable {

    private static final long serialVersionUID = -2431462751393252015L;
    /**状态码:详见状态码说明 01 通过，02不通过,202无法验证，
     * 203异常情况，204姓名格式不正确,205身份证格式不正确*/
    private Integer status;
    /**提示信息*/
    private String msg;
    /**身份证*/
    private String idCard;
    /**姓名*/
    private String name;
    /**性别*/
    private String sex;
    /**身份证所在地*/
    private String area;
    /**省*/
    private String province;
    /**市*/
    private String city;
    /**区县*/
    private String prefecture;
    /**出生年月*/
    private String birthday;
    /**地区代码 */
    private String addrCode;
    /**身份证最后一位*/
    private String lastCode;


}
