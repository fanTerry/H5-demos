package com.esportzoo.esport.connect.response.game;

import lombok.Data;

/**
 * 游戏自动登陆响应
 * @Author zheng.lin
 * @Date 2020/4/16 18:41
 */
@Data
public class GameAutoLoginResponse {

    /**游戏首页地址*/
    private String gameUrl;

    public GameAutoLoginResponse(){}

    public GameAutoLoginResponse(String gameUrl) {
        this.gameUrl = gameUrl;
    }
}
