package com.esportzoo.esport.connect.request.app;

import com.esportzoo.esport.connect.request.BaseRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName AppClientInfo
 * @Description
 * @Author jiajing.he
 * @Date 2019/10/16  15:58
 * @Version 1.0
 **/
@Data
public class AppClientInfo extends BaseRequest implements Serializable {

    private static final long serialVersionUID = -7931698238902217424L;
    /**
     *手机唯一码
     */
    private String deviceId;

    /**
     *机型
     */
    private String deviceStyle;

    /**
     *操作系统的版本
     */
    private String osType;

    /**
     *手机屏幕分辨率
     */
    private String screenSize;

    /**
     *客户端类型
     */
    private Integer clientType;

    /**
     *客户端版本号
     */
    private String clientVersion;

    /**
     *渠道号
     */
    private String channelNo;

    /**
     *省份
     */
    private String province;

    /**
     *城市
     */
    private String city;

    /**
     *网络
     */
    private String network;

    /**
     *IP地址
     */
    private String ipAddress;

    /**
     *手机号
     */
    private String mobile;

    /**
     *运营商 移动 联通 电信 其它
     */
    private String carrier;

}
