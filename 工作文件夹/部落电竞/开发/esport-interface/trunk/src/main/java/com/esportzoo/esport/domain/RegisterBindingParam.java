package com.esportzoo.esport.domain;

import com.esportzoo.esport.connect.request.BaseRequest;
import com.esportzoo.esport.constants.ThirdType;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * 第三方登录注册本站用户绑定手机参数
 * @author jiajing.he
 * @date 2020/3/2 10:50
 */
public class RegisterBindingParam implements Serializable {


    private static final long serialVersionUID = -8823726946390193781L;

    /**
     * 绑定手机号
     */
    private String phone;

    /**
     * 验证码
     */
    private String code;

    /**
     * 缓存用户信息的key
     */
    private String cacheKey;

    /**
     * 第三方登录类型 {@link ThirdType}
     */
    private Integer thirdType;

    /** 渠道号 */
    private Long agentId = 1000L;

    /** 客户端版本 */
    private String version;

    private HttpServletRequest request;

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    public Integer getThirdType() {
        return thirdType;
    }

    public void setThirdType(Integer thirdType) {
        this.thirdType = thirdType;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
