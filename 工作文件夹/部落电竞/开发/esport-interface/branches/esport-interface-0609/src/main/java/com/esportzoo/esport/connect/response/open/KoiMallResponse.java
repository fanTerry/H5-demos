package com.esportzoo.esport.connect.response.open;

import lombok.Data;

/**
* @Description 锦鲤商城地址请求响应
* @Author zheng.lin
* @Date 2020/6/3 16:17 
*/
@Data
public class KoiMallResponse {

    /**锦鲤商城地址*/
    private String koiMallUrl;

    public KoiMallResponse(){}

    public KoiMallResponse(String koiMallUrl) {
        this.koiMallUrl = koiMallUrl;
    }
}
