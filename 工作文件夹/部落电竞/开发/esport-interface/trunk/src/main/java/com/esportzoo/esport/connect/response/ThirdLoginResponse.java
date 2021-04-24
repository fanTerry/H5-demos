package com.esportzoo.esport.connect.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author lixisheng
 * @Date 2020/3/3 15:31
 */
@Data
public class ThirdLoginResponse implements Serializable {

    private static final long serialVersionUID = 1524858139749655438L;

    /**
     * 是否是新用户
     */
    private Boolean newUser;

    private H5LoginUserResponse h5LoginUserResponse;

    private String key;
}
