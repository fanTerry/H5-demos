package com.esportzoo.esport.connect.response.matchtool;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author lixisheng
 * @Date 2019/11/22 15:49
 */
@Data
public class MatchToolRoomResponse implements Serializable {

    private static final long serialVersionUID = 7686108769936629557L;

    private Long userId;
    private String nickName;
    /** 头像 **/
    private String icon;
    /** 聊天室地址 */
    private String chatSocketUrl;
}
